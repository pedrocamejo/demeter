package cpc.demeter.vista.gestion;

/*import java.awt.Checkbox;*/
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import cpc.ares.modelo.Accion;
import cpc.demeter.controlador.gestion.ContOrdenTransporteProduccion;
import cpc.modelo.demeter.basico.Labor;
import cpc.modelo.demeter.basico.Rubro;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.basico.UnidadArrime;
import cpc.modelo.demeter.basico.UnidadMedida;
import cpc.modelo.demeter.gestion.*;

import cpc.modelo.ministerio.basico.Usos;
import cpc.modelo.ministerio.dimension.UbicacionDireccion;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.listas.CompRadioBotonLineal;
import cpc.zk.componente.listas.CompTexto;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class UiOrdenTransporteProduccion
		extends
		CompVentanaMaestroDetalle<OrdenTransporteProduccion, DetalleMaquinariaOrdenTrabajo> {

	private static final long serialVersionUID = -5739695024627594930L;
	private CompGrupoDatos gbGeneral;
	private CompGrupoDatos gbProductor;
	private CompGrupoDatos gbLabor;
	private CompGrupoDatos gbRutas;
	private CompGrupoDatos gbCierreProducido;
	private CompGrupoDatos gbObservacion;

	private Textbox nombreProductor;
	private Textbox ingreso;
	private Textbox sede;
	private CompTexto<UnidadMedida> unidadFisica;
	private CompTexto<UnidadMedida> unidadProduccion;
	private CompTexto<UnidadMedida> unidadTrabajo;
	private Textbox nroControl;
	private Datebox fecha, inicio, llegada;
	private CompCombobox<UnidadFuncional> unidadFuncional;
	private CompRadioBotonLineal<Usos> uso;

	private CompCombobox<UbicacionDireccion> unidadProductiva;

	private CompCombobox<Rubro> rubro;
	private CompCombobox<Labor> labor;

	private CompBuscar<Productor> cedula;
	private CompBuscar<OrdenTrabajoMecanizado> ordenMecanizado;
	private CompBuscar<UnidadArrime> destinoSolicitado;
	private CompTexto<UnidadArrime> destinoReal;

	private List<CompEncabezado> listaDetalle;

	private CompBuscar<MaquinariaUnidad> maquinaria;
	private CompBuscar<ImplementoUnidad> implemento;

	private Doublebox totalPosibleProducido;
	private Doublebox totalTrabajado;
	private Doublebox totalRealProducido;
	private Doublebox humedad;
	private Doublebox pesoAcondicionado;
	private Doublebox kilometrajeInicial;
	private Doublebox kilometrajeFinal;
	private Doublebox kilometrajeTotal;
	// private Doublebox cantidadViajes;
	private Doublebox tiempoEspera;
	private Intbox cantidad;
	private Doublebox cantidadViaje;

	private Textbox observacion;

	private List<UnidadFuncional> unidadesEjecutoras;
	private List<Trabajador> conductores;
	private List<Productor> productores;
	private List<Rubro> rubros;
	private List<Labor> labores;
	private List<UnidadArrime> silos;
	private List<OrdenTrabajoMecanizado> ordenesMecanizado;

	private boolean cerrado;
	private List<Usos> usos;
	private CompLista<DetalleOrdenTransporteProduccion> rutasTransporte;

	public UiOrdenTransporteProduccion(String titulo, int ancho,
			List<UnidadFuncional> unidadesEjecutoras, List<Labor> labores,
			List<Trabajador> trabajadores, List<Productor> productores,
			List<Rubro> rubros, List<OrdenTrabajoMecanizado> ordenesMecanizado,
			List<UnidadArrime> silos, List<Usos> usos, boolean cerrado)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.unidadesEjecutoras = unidadesEjecutoras;
		this.conductores = trabajadores;
		this.productores = productores;
		this.rubros = rubros;
		this.labores = labores;
		this.cerrado = cerrado;
		this.ordenesMecanizado = ordenesMecanizado;
		this.silos = silos;
		this.usos = usos;
	}

	public void inicializar() {
		setGbEncabezado(1, "");
		setGbDetalle("Detalle Solicitud");
		setGbPie(1, "");
		gbGeneral = new CompGrupoDatos("Datos generales", 4);
		gbProductor = new CompGrupoDatos("Origen/Destino", 2);
		gbLabor = new CompGrupoDatos("Labor", 2);
		gbCierreProducido = new CompGrupoDatos("Cierre Produccion", 2);
		gbObservacion = new CompGrupoDatos("Observacion", 1);
		gbRutas = new CompGrupoDatos("Rutas", 1);
		unidadFuncional = new CompCombobox<UnidadFuncional>();
		uso = new CompRadioBotonLineal<Usos>(usos, true);
		labor = new CompCombobox<Labor>();
		unidadProductiva = new CompCombobox<UbicacionDireccion>();
		rubro = new CompCombobox<Rubro>();
		unidadProduccion = new CompTexto<UnidadMedida>();
		unidadTrabajo = new CompTexto<UnidadMedida>();

		tiempoEspera = new Doublebox();
		nombreProductor = new Textbox();
		ingreso = new Textbox();
		sede = new Textbox();
		observacion = new Textbox();
		nroControl = new Textbox();

		unidadFisica = new CompTexto<UnidadMedida>();

		cedula = new CompBuscar<Productor>(getEncabezadoCliente(), 0);
		ordenMecanizado = new CompBuscar<OrdenTrabajoMecanizado>(
				getEncabezadoOrden(), 0);
		destinoSolicitado = new CompBuscar<UnidadArrime>(
				cargarEncabezadoSilo(), 2);
		destinoReal = new CompTexto<UnidadArrime>();
		fecha = new Datebox();
		inicio = new Datebox();
		llegada = new Datebox();

		cantidadViaje = new Doublebox();
		cantidad = new Intbox(1);
		totalTrabajado = new Doublebox();

		totalPosibleProducido = new Doublebox();

		totalRealProducido = new Doublebox();
		humedad = new Doublebox();
		pesoAcondicionado = new Doublebox();

		kilometrajeInicial = new Doublebox();
		kilometrajeFinal = new Doublebox();
		kilometrajeTotal = new Doublebox();
		rutasTransporte = new CompLista<DetalleOrdenTransporteProduccion>(
				getRuta());
		encabezadosPrimario();

		unidadFuncional.setModelo(unidadesEjecutoras);
		ordenMecanizado.setModelo(ordenesMecanizado);
		// rubro.setModelo(rubros);
		rubro.setModelo(rubros, "getStrNombreRubro");
		labor.setModelo(labores);
		cedula.setModelo(productores);
		destinoSolicitado.setModelo(silos);
		totalTrabajado.setDisabled(true);
		nroControl.setDisabled(true);
		sede.setDisabled(true);
		fecha.setDisabled(true);
		ingreso.setDisabled(true);
		nombreProductor.setDisabled(true);
		kilometrajeTotal.setDisabled(true);

		observacion.setMultiline(true);
		observacion.setCols(2);
		observacion.setWidth("590px");
		nombreProductor.setWidth("420px");
		labor.setWidth("500px");
		unidadProductiva.setWidth("420px");
		cedula.setWidth("100px");
		cedula.setAncho(500);
		destinoSolicitado.setWidth("550px");
		destinoSolicitado.setAncho(700);
		destinoReal.setWidth("550px");
		ordenMecanizado.setWidth("100px");
		ordenMecanizado.setAncho(720);
		ingreso.setWidth("100px");

		unidadFuncional.setReadonly(true);
		labor.setReadonly(true);
		rubro.setReadonly(true);
		unidadProductiva.setReadonly(true);
		cedula.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
		ordenMecanizado.addEventListener(CompBuscar.ON_SELECCIONO,
				getControlador());
		labor.addEventListener(Events.ON_SELECTION, getControlador());
		cantidad.addEventListener(Events.ON_CHANGE, getControlador());
		cantidadViaje.addEventListener(Events.ON_CHANGE, getControlador());
		unidadFuncional.addEventListener(Events.ON_SELECTION, getControlador());
		kilometrajeFinal.addEventListener(Events.ON_CHANGE, getControlador());
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Nro Control:", nroControl);
		gbGeneral.addComponente("Fecha :", fecha);
		gbGeneral.addComponente("Sede :", sede);
		gbGeneral.addComponente("Unidad Ejecutora :", unidadFuncional);
		gbGeneral.addComponente("Orden Mecanizado :", ordenMecanizado);

		gbGeneral.addComponente("Rubro :", rubro);
		gbGeneral.addComponente("Fecha Inicio:", inicio);

		gbProductor.setAnchoColumna(0, 170);
		gbProductor.addComponenteMultiples("Cedula:", 170, cedula, new Label(
				"Fecha Ingreso:"), ingreso);
		gbProductor.addComponente("Nombre Completo:", nombreProductor);
		gbProductor.addComponente("Origen:", unidadProductiva);
		gbProductor.addComponente("Destino Solicitado:", destinoSolicitado);

		gbLabor.addComponente("Labor:", labor);
		gbLabor.addComponenteMultiples("Transportado (Posible):", 100,
				totalPosibleProducido, unidadProduccion);
		gbLabor.addComponenteMultiples("Distancia:", 100, cantidadViaje,
				new Label("Cantidad:"), cantidad, new Label("Total:"),
				totalTrabajado, unidadTrabajo);
		gbLabor.addComponenteMultiples("Kilometraje Inicial:", 100,
				kilometrajeInicial);
		gbObservacion.addComponente(observacion);
		gbRutas.addComponente(rutasTransporte);

		gbCierreProducido.addComponente("Destino Definitivo:", destinoReal);
		gbCierreProducido.addComponenteMultiples("Peso Real:", 120,
				totalRealProducido, unidadFisica, new Label(
						"Peso Acondiconado:"), pesoAcondicionado, new Label(
						"TONELADA [TON]"));
		gbCierreProducido.addComponenteMultiples("Humedad:", 150, humedad,
				new Label("GRADOS [°C]"), new Label("   "));
		gbCierreProducido.addComponente("Uso:", uso);
		gbCierreProducido.addComponenteMultiples("Dias de Espera: ", 150,
				tiempoEspera, new Label("Fecha LLegada:"), llegada);
		gbCierreProducido.addComponenteMultiples("Kilometraje Final:", 100,
				kilometrajeFinal, new Label("Total:"), kilometrajeTotal);
		gbGeneral.dibujar(getGbEncabezado());
		gbProductor.dibujar(getGbEncabezado());
		gbLabor.dibujar(getGbEncabezado());

		setDetalles(cargarListaDetalle(), getModelo().getEquipos(),
				listaDetalle);
		getDetalle().setNuevo(new DetalleMaquinariaOrdenTrabajo());
		gbCierreProducido.dibujar(getGbPie());
		gbObservacion.dibujar(getGbPie());
		gbRutas.dibujar(getGbPie());
		dibujarEstructura();
	}

	public void cargarValores(OrdenTransporteProduccion solicitudMeca)
			throws ExcDatosInvalidos {
		unidadFuncional.setSeleccion(getModelo().getUnidadFuncional());
		getBinder().addBinding(unidadFuncional, "seleccion",
				getNombreModelo() + ".unidadFuncional", null, null, "save",
				null, null, null, null);
		/*
		 * servicio.setSeleccion(getModelo().); getBinder().addBinding(servicio,
		 * "seleccion", getNombreModelo()+".servicio", null, null, "save", null,
		 * null, null, null);
		 */
		cedula.setSeleccion(getModelo().getProductor());
		getBinder().addBinding(cedula, "seleccion",
				getNombreModelo() + ".productor", null, null, "save", null,
				null, null, null);
		fecha.setValue(getModelo().getFecha());
		getBinder().addBinding(fecha, "value", getNombreModelo() + ".fecha",
				null, null, "save", null, null, null, null);
		inicio.setValue(getModelo().getFechaSalida());
		getBinder().addBinding(inicio, "value",
				getNombreModelo() + ".fechaSalida", null, null, "save", null,
				null, null, null);
		llegada.setValue(getModelo().getFechaEntradaArrime());
		getBinder().addBinding(llegada, "value",
				getNombreModelo() + ".fechaEntradaArrime", null, null, "save",
				null, null, null, null);
		humedad.setValue(getModelo().getGradosHumedad());
		getBinder().addBinding(humedad, "value",
				getNombreModelo() + ".gradosHumedad", null, null, "save", null,
				null, null, null);
		uso.setSeleccion(getModelo().getUsoPersona());
		getBinder().addBinding(uso, "seleccion",
				getNombreModelo() + ".usoPersona", null, null, "save", null,
				null, null, null);

		pesoAcondicionado.setValue(getModelo().getPesoAcondicionado());
		getBinder().addBinding(pesoAcondicionado, "value",
				getNombreModelo() + ".pesoAcondicionado", null, null, "save",
				null, null, null, null);
		if (getModelo().getCantidadViajes() != null)
			cantidad.setValue(getModelo().getCantidadViajes());
		getBinder().addBinding(cantidad, "value",
				getNombreModelo() + ".cantidadViajes", null, null, "save",
				null, null, null, null);
		if (getModelo().getTotalRecorrido() != null)
			totalTrabajado.setValue(getModelo().getTotalRecorrido());
		getBinder().addBinding(totalTrabajado, "value",
				getNombreModelo() + ".totalRecorrido", null, null, "save",
				null, null, null, null);
		if (getModelo().getTotalRecorrido() != null
				&& getModelo().getCantidadViajes() != null)
			cantidadViaje.setValue(getModelo().getTotalRecorrido()
					/ getModelo().getCantidadViajes());
		kilometrajeInicial.setValue(getModelo().getKilometrajeInicio());
		getBinder().addBinding(kilometrajeInicial, "value",
				getNombreModelo() + ".kilometrajeInicio", null, null, "save",
				null, null, null, null);

		kilometrajeFinal.setValue(getModelo().getKilometrajeFinal());
		getBinder().addBinding(kilometrajeFinal, "value",
				getNombreModelo() + ".kilometrajeFinal", null, null, "save",
				null, null, null, null);

		kilometrajeTotal.setValue(getModelo().getCantidadKilometros());
		getBinder().addBinding(kilometrajeTotal, "value",
				getNombreModelo() + ".cantidadKilometros", null, null, "save",
				null, null, null, null);

		rubro.setSeleccion(getModelo().getRubro());
		getBinder().addBinding(rubro, "seleccion",
				getNombreModelo() + ".rubro", null, null, "save", null, null,
				null, null);
		refrescarProductor(getModelo().getProductor());
		unidadProductiva.setSeleccion(getModelo().getOrigen());
		getBinder().addBinding(unidadProductiva, "seleccion",
				getNombreModelo() + ".origen", null, null, "save", null, null,
				null, null);
		if (getModelo().getOrdenTrabajoMecanizado() != null)
			ordenMecanizado.setSeleccion(getModelo()
					.getOrdenTrabajoMecanizado());
		getBinder().addBinding(ordenMecanizado, "seleccion",
				getNombreModelo() + ".ordenTrabajoMecanizado", null, null,
				"save", null, null, null, null);

		if (getModelo().getUnidadArrimeSolicitada() != null)
			destinoSolicitado.setSeleccion(getModelo()
					.getUnidadArrimeSolicitada());
		getBinder().addBinding(destinoSolicitado, "seleccion",
				getNombreModelo() + ".unidadArrimeSolicitada", null, null,
				"save", null, null, null, null);

		if (getModelo().getUnidadArrimeDestino() != null)
			destinoReal.setModelo(getModelo().getUnidadArrimeDestino());
		getBinder().addBinding(destinoReal, "modelo",
				getNombreModelo() + ".unidadArrimeDestino", null, null, "save",
				null, null, null, null);
		if (getModelo().getTiempoEspera() != null)
			tiempoEspera.setValue(getModelo().getTiempoEspera());
		getBinder().addBinding(tiempoEspera, "value",
				getNombreModelo() + ".tiempoEspera", null, null, "save", null,
				null, null, null);
		if (getModelo().getSede() != null)
			sede.setValue(getModelo().getSede().getNombre());
		if (getModelo().getNroControl() != null)
			nroControl.setValue(getModelo().getNroControl());

		if (getModelo().getProduccionReal() != null)
			totalRealProducido.setValue(getModelo().getProduccionReal());
		getBinder().addBinding(totalRealProducido, "value",
				getNombreModelo() + ".produccionReal", null, null, "save",
				null, null, null, null);

		try {
			dibujarDetalle();
		} catch (Exception e) {
			throw new ExcDatosInvalidos();
		}
	}

	private List<CompEncabezado> getRuta() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Fecha", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Origen", 300);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrOrigen");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Destino", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrDestino");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Falla", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFalla");
		encabezado.add(titulo);
		return encabezado;
	}

	private void encabezadosPrimario() {

		listaDetalle = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Conductor", 180);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getOperador");
		titulo.setMetodoModelo(".operador");
		listaDetalle.add(titulo);

		titulo = new CompEncabezado("Maquinaria", 220);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getMaquinaria");
		titulo.setMetodoModelo(".maquinaria");
		listaDetalle.add(titulo);

		titulo = new CompEncabezado("Implemento", 220);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getImplemento");
		titulo.setMetodoModelo(".implemento");
		listaDetalle.add(titulo);
	}

	private ArrayList<Component> cargarListaDetalle() {
		ArrayList<Component> lista = new ArrayList<Component>();
		maquinaria = new CompBuscar<MaquinariaUnidad>(getEncabezadoEquipo(), 3);
		implemento = new CompBuscar<ImplementoUnidad>(getEncabezadoEquipo(), 3);
		CompCombobox<Trabajador> operador = new CompCombobox<Trabajador>();

		maquinaria.setWidth("200px");
		maquinaria.setAncho(650);
		implemento.setWidth("200px");
		implemento.setAncho(650);
		operador.setWidth("160px");

		// operador.setModelo(conductores);
		operador.setModelo(conductores, "getNroCedula");
		lista.add(operador);
		lista.add(maquinaria);
		lista.add(implemento);
		return lista;
	}

	public void dibujarDetalle() throws ExcFiltroExcepcion {
		ContOrdenTransporteProduccion control;
		control = (ContOrdenTransporteProduccion) getControlador();
		if (getModelo().getUnidadFuncional() != null) {
			control.actualizarActivos(getModelo().getUnidadFuncional());
		}
		if (getModelo().getEquipos() != null) {
			for (DetalleMaquinariaOrdenTrabajo item : getModelo().getEquipos()) {
				getDetalle().agregar(item);
			}
		}
		if (getModelo().getDetalles() != null
				&& getModelo().getDetalles().size() > 0) {
			DetalleOrdenTrabajo detalle = getModelo().getDetalles().get(0);
			if (detalle != null) {
				labor.setSeleccion(detalle.getLabor());
				totalPosibleProducido.setValue(detalle.getCantidadSolicitada());
				control.actualizarLabor(detalle.getLabor());
				unidadTrabajo.setModelo(detalle.getUnidadTrabajo());
			}
		}
	}

	public void refrescarProductor(Productor productor) {
		if (productor != null) {
			nombreProductor.setValue(productor.getNombres());
			unidadProductiva.setModelo(productor.getDirecciones());
			ingreso.setValue(productor.getSrtFechaIngreso());
		}
	}

	private List<CompEncabezado> getEncabezadoOrden() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Control", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroControl");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Unidad Ejecutora", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrUnidadFuncional");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Productor", 170);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrProductor");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Labor", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrLabor");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Rubro", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrRubro");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo);
		return encabezado;
	}

	public List<CompEncabezado> cargarEncabezadoSilo() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Clase", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo de Silo", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipoSilo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("ubicacion", 350);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrUbicacionFisica");
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> getEncabezadoEquipo() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Codigo. ", 120);
		titulo.setMetodoBinder("getCodigoActivo");
		lista.add(titulo);

		titulo = new CompEncabezado("Ejemplar. ", 120);
		titulo.setMetodoBinder("getIdEjemplarActivo");
		lista.add(titulo);

		titulo = new CompEncabezado("Serial. ", 150);
		titulo.setMetodoBinder("getSerial");
		lista.add(titulo);

		titulo = new CompEncabezado("Denominación. ", 300);
		titulo.setMetodoBinder("getNombre");
		lista.add(titulo);

		titulo = new CompEncabezado("Marca. ", 140);
		titulo.setMetodoBinder("getMarca");
		lista.add(titulo);

		titulo = new CompEncabezado("Modelo. ", 140);
		titulo.setMetodoBinder("getModelo");
		lista.add(titulo);

		return lista;
	}

	public void actualizarEncabezados(boolean visiblePases,
			boolean visibleCantidad) {
		CompEncabezado encabezadoPases = listaDetalle.get(2);
		CompEncabezado encabezadoCantidad = listaDetalle.get(1);
		encabezadoCantidad.setVisible(visibleCantidad);
		encabezadoPases.setVisible(visiblePases);
		getDetalle().clear();
		getDetalle().actualizarEncabezados();
	}

	private List<CompEncabezado> getEncabezadoCliente() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Cedula", 120);
		titulo.setMetodoBinder("getIdentidadLegal");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre", 450);
		titulo.setMetodoBinder("getNombres");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	public CompBuscar<Productor> getCedula() {
		return cedula;
	}

	public Textbox getPrimerNombre() {
		return nombreProductor;
	}

	public Textbox getSegundoNombre() {
		return ingreso;
	}

	public Textbox getPrimerApellido() {
		return sede;
	}

	public Textbox getSegundoApellido() {
		return nroControl;
	}

	public Datebox getFecha() {
		return fecha;
	}

	public Textbox getNombreProductor() {
		return nombreProductor;
	}

	public Textbox getIngreso() {
		return ingreso;
	}

	public Textbox getSede() {
		return sede;
	}

	public Textbox getNroControl() {
		return nroControl;
	}

	public CompCombobox<UnidadFuncional> getUnidadFuncional() {
		return unidadFuncional;
	}

	public CompCombobox<UbicacionDireccion> getUnidadProductiva() {
		return unidadProductiva;
	}

	public void refrescarMaquinaria(List<MaquinariaUnidad> activos) {
		maquinaria.setModelo(activos);
	}

	public void refrescarImplementos(List<ImplementoUnidad> activos) {
		implemento.setModelo(activos);
	}

	public void refrescarServicio() {
		getDetalle().clear();
	}

	public CompCombobox<Labor> getLabor() {
		return labor;
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			activarConsulta();
		else if (modoOperacion == Accion.AGREGAR)
			modoNuevo();
		else if (modoOperacion == Accion.PROCESAR)
			modoCierre();
	}

	public void activarConsulta() {
		cedula.setDisabled(true);
		rubro.setDisabled(true);
		unidadFuncional.setDisabled(true);
		ordenMecanizado.setDisabled(true);
		inicio.setDisabled(true);
		labor.setDisabled(true);
		llegada.setDisabled(true);
		humedad.setDisabled(true);
		unidadProductiva.setDisabled(true);
		destinoSolicitado.setDisabled(true);
		pesoAcondicionado.setDisabled(true);
		cantidadViaje.setDisabled(true);
		cantidad.setDisabled(true);
		kilometrajeInicial.setDisabled(true);
		kilometrajeFinal.setDisabled(true);
		kilometrajeTotal.setDisabled(true);
		uso.setDisabled(true);

		desactivarDetalle();
		if (cerrado) {
			gbCierreProducido.setVisible(true);
		} else {
			gbCierreProducido.setVisible(false);
		}
		observacion.setDisabled(true);

		totalPosibleProducido.setDisabled(true);
		totalTrabajado.setDisabled(true);
		totalRealProducido.setDisabled(true);
		tiempoEspera.setDisabled(true);
	}

	public void modoCierre() {
		cedula.setDisabled(true);
		rubro.setDisabled(true);
		unidadFuncional.setDisabled(true);
		ordenMecanizado.setDisabled(true);
		inicio.setDisabled(true);
		labor.setDisabled(true);
		desactivarDetalle();
		cantidadViaje.setDisabled(true);
		cantidad.setDisabled(true);
		kilometrajeInicial.setDisabled(true);
		kilometrajeFinal.setDisabled(false);
		destinoSolicitado.setDisabled(true);

		unidadProductiva.setDisabled(true);
		gbCierreProducido.setVisible(true);
		totalPosibleProducido.setDisabled(true);
		totalTrabajado.setDisabled(true);
		totalRealProducido.setDisabled(false);

	}

	public void modoNuevo() {
		cedula.setDisabled(false);
		rubro.setDisabled(false);
		unidadFuncional.setDisabled(false);
		unidadProductiva.setDisabled(false);
		gbCierreProducido.setVisible(false);
		observacion.setDisabled(false);

	}

	public Doublebox getTiempoEspera() {
		return tiempoEspera;
	}

	public Intbox getCantidad() {
		return cantidad;
	}

	public CompTexto<UnidadMedida> getUnidadFisica() {
		return unidadFisica;
	}

	public Datebox getInicio() {
		return inicio;
	}

	public Datebox getFechaEntradaArrime() {
		return llegada;
	}

	public CompCombobox<Rubro> getRubro() {
		return rubro;
	}

	public CompBuscar<OrdenTrabajoMecanizado> getOrdenMecanizado() {
		return ordenMecanizado;
	}

	public void refrescarDatosLabor(LaborOrdenServicio labor, boolean nueva) {
		/*
		 * desactivarLabor(!nueva); unidadFisica.setModelo(labor.getUnidad());
		 * unidadFisica.setValue(labor.getUnidad().getAbreviatura()); //if
		 * (!nueva){ fisico.setValue(labor.getFisico());
		 * pases.setValue(labor.getPases());
		 * cantidad.setValue(labor.getCantidad());
		 * total.setValue(labor.getCalculo());
		 * visibilidadLabor(labor.isPasesVisible(), labor.isCantidadVisible());
		 * //}
		 */
	}

	public Doublebox getTotalPosibleProducido() {
		return totalPosibleProducido;
	}

	public Doublebox getTotalTrabajado() {
		return totalTrabajado;
	}

	public Doublebox getTotalRealProducido() {
		return totalRealProducido;
	}

	public CompTexto<UnidadMedida> getUnidadProduccion() {
		return unidadProduccion;
	}

	public CompTexto<UnidadMedida> getUnidadTrabajo() {
		return unidadTrabajo;
	}

	public CompBuscar<UnidadArrime> getDestino() {
		return destinoSolicitado;
	}

	public Doublebox getKilometrajeFinal() {
		return kilometrajeFinal;
	}

	public void setKilometrajeFinal(Doublebox kilometrajeFinal) {
		this.kilometrajeFinal = kilometrajeFinal;
	}

	public Doublebox getKilometrajeInicial() {
		return kilometrajeInicial;
	}

	public void setKilometrajeInicial(Doublebox kilometrajeInicial) {
		this.kilometrajeInicial = kilometrajeInicial;
	}

	public CompTexto<UnidadArrime> getDestinoReal() {
		return destinoReal;
	}

	public Datebox getLlegada() {
		return llegada;
	}

	public CompRadioBotonLineal<Usos> getUso() {
		return uso;
	}

	public Textbox getObservacion() {
		return observacion;
	}

	public void setObservacion(Textbox observacion) {
		this.observacion = observacion;
	}

	public CompLista<DetalleOrdenTransporteProduccion> getRutasTransporte() {
		return rutasTransporte;
	}

	public Doublebox getCantidadViaje() {
		return cantidadViaje;
	}

	public Doublebox getPesoAcondicionado() {
		return pesoAcondicionado;
	}

	public Doublebox getKilometrajeTotal() {
		return kilometrajeTotal;
	}

}
