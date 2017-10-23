package cpc.demeter.vista.gestion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import cpc.ares.modelo.Accion;
import cpc.demeter.controlador.gestion.ContOrdenTrabajoMecanizado;
import cpc.modelo.demeter.administrativo.ContratoMecanizado;
import cpc.modelo.demeter.basico.CicloProductivo;
import cpc.modelo.demeter.basico.Labor;
import cpc.modelo.demeter.basico.PlanServicio;
import cpc.modelo.demeter.basico.Rubro;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.basico.UnidadMedida;
import cpc.modelo.demeter.mantenimiento.MomentoFalla;
import cpc.modelo.demeter.mantenimiento.TipoFalla;
import cpc.modelo.demeter.gestion.*;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.modelo.ministerio.gestion.InstitucionCrediticia;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.modelo.ministerio.gestion.UnidadProductiva;
import cpc.modelo.sigesp.basico.Activo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.listas.CompTexto;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class UiFallaOrdenTrabajoM
		extends
		CompVentanaMaestroDetalle<OrdenTrabajoMecanizado, DetalleMaquinariaOrdenTrabajo> {

	private static final long serialVersionUID = -5739695024627594930L;
	private CompGrupoDatos gbGeneral, gbProductor, gbLabor, gbCierreTrabajo,
			gbCierreProducido, gbFalla, gbObservacion;

	private Textbox nombreProductor;
	private Textbox ingreso;
	private Textbox sede;
	private Intbox tiempoEspera;
	private Doublebox pases;
	private Doublebox fisico;
	private Doublebox cantidad;
	private Doublebox total;
	private CompTexto<UnidadMedida> unidadFisica;
	private Label lpases;
	private Label lcantidad;
	private Textbox nroControl;
	private Datebox fecha, inicio;
	private CompCombobox<UnidadFuncional> unidadFuncional;

	private CompCombobox<CicloProductivo> cicloProductivo;
	private CompCombobox<UnidadProductiva> unidadProductiva;
	private CompCombobox<Rubro> rubro;
	private CompCombobox<PlanServicio> planServicio;
	private CompCombobox<Labor> labor;

	private CompLista<TrabajoRealizadoMecanizado> detalleTrabajos;
	private CompBuscar<Productor> cedula;
	private CompBuscar<ContratoMecanizado> contrato;
	private CompBuscar<SolicitudMecanizado> solicitud;
	private CompCombobox<InstitucionCrediticia> financiamiento;
	private List<CompEncabezado> listaDetalle;
	private CompCombobox<Trabajador> tecnicoCampo;
	private Checkbox apto;

	private CompBuscar<MaquinariaUnidad> maquinaria;
	private CompBuscar<ImplementoUnidad> implemento;

	private Doublebox totalPosibleProducido;
	private Doublebox totalTrabajado;
	private Doublebox totalFisicoTrabajado;
	private Doublebox totalRealProducido;
	private Checkbox usaTransporte;

	private Textbox observacion;

	private CompCombobox<TipoFalla> tipoFalla;
	private CompCombobox<MomentoFalla> momentoFalla;
	private CompCombobox<Activo> equipo;

	private List<CicloProductivo> ciclosProductivos;
	private List<UnidadFuncional> unidadesEjecutoras;
	private List<PlanServicio> planesServicio;
	private List<Trabajador> operadores;
	private List<Trabajador> tecnicos;
	private List<Productor> productores;
	private List<Rubro> rubros;
	private List<Labor> labores;
	private List<ContratoMecanizado> contratos;
	private List<SolicitudMecanizado> solicitudes;
	private List<InstitucionCrediticia> financiamientos;
	private List<TipoFalla> fallas;
	private List<MomentoFalla> momentosFalla;

	private boolean modoNuevo;
	private boolean anulado;
	private boolean cerrado;

	public UiFallaOrdenTrabajoM(String titulo, int ancho,
			List<CicloProductivo> ciclosProductivos,
			List<UnidadFuncional> unidadesEjecutoras,
			List<PlanServicio> planesServicio, List<Labor> labores,
			List<Trabajador> trabajadores, List<Productor> productores,
			List<Rubro> rubros, List<InstitucionCrediticia> financiamientos,
			List<ContratoMecanizado> contratos,
			List<SolicitudMecanizado> solicitudes, List<Trabajador> tecnicos,
			List<TipoFalla> fallas, List<MomentoFalla> momentosFalla,
			boolean modoNuevo, boolean anulado, boolean cerrado)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.ciclosProductivos = ciclosProductivos;
		this.unidadesEjecutoras = unidadesEjecutoras;
		this.planesServicio = planesServicio;
		this.operadores = trabajadores;
		this.productores = productores;
		this.rubros = rubros;
		this.solicitudes = solicitudes;
		this.contratos = contratos;
		this.financiamientos = financiamientos;
		this.labores = labores;
		this.fallas = fallas;
		this.momentosFalla = momentosFalla;
		this.tecnicos = tecnicos;

		this.modoNuevo = modoNuevo;
		this.anulado = anulado;
		this.cerrado = cerrado;
	}

	public void inicializar() {
		setGbEncabezado(1, "");
		setGbDetalle("Detalle Solicitud");
		setGbPie(1, "");
		gbGeneral = new CompGrupoDatos("Datos generales", 4);
		gbProductor = new CompGrupoDatos("Productor", 2);
		gbLabor = new CompGrupoDatos("Labor", 2);
		gbCierreTrabajo = new CompGrupoDatos("Cierre Trabajo", 4);
		gbCierreProducido = new CompGrupoDatos("Cierre Produccion", 3);
		gbFalla = new CompGrupoDatos("Fallas", 4);
		gbObservacion = new CompGrupoDatos("Observacion", 1);
		unidadFuncional = new CompCombobox<UnidadFuncional>();
		cicloProductivo = new CompCombobox<CicloProductivo>();
		labor = new CompCombobox<Labor>();
		tecnicoCampo = new CompCombobox<Trabajador>();
		apto = new Checkbox("Apto.     ");
		unidadProductiva = new CompCombobox<UnidadProductiva>();
		planServicio = new CompCombobox<PlanServicio>();
		rubro = new CompCombobox<Rubro>();
		financiamiento = new CompCombobox<InstitucionCrediticia>();
		tiempoEspera = new Intbox();
		pases = new Doublebox();
		cantidad = new Doublebox();
		fisico = new Doublebox();
		total = new Doublebox();
		nombreProductor = new Textbox();
		ingreso = new Textbox();
		sede = new Textbox();
		unidadFisica = new CompTexto<UnidadMedida>();
		lpases = new Label("Pases :");
		lcantidad = new Label("Cantidad :");
		nroControl = new Textbox();
		cedula = new CompBuscar<Productor>(getEncabezadoCliente(), 0);
		contrato = new CompBuscar<ContratoMecanizado>(getEncabezadoContrato(),
				0);
		solicitud = new CompBuscar<SolicitudMecanizado>(
				getEncabezadoSolicitud(), 0);
		detalleTrabajos = new CompLista<TrabajoRealizadoMecanizado>(
				encabezadosTrabajosDiarios());
		fecha = new Datebox();
		inicio = new Datebox();

		observacion = new Textbox();

		totalPosibleProducido = new Doublebox();
		totalTrabajado = new Doublebox();
		totalFisicoTrabajado = new Doublebox();
		totalRealProducido = new Doublebox();
		usaTransporte = new Checkbox("Transporte Interno");

		tipoFalla = new CompCombobox<TipoFalla>();
		momentoFalla = new CompCombobox<MomentoFalla>();
		equipo = new CompCombobox<Activo>();

		encabezadosPrimario();

		unidadFuncional.setModelo(unidadesEjecutoras);
		cicloProductivo.setModelo(ciclosProductivos);
		planServicio.setModelo(planesServicio);
		financiamiento.setModelo(financiamientos);
		// rubro.setModelo(rubros);
		rubro.setModelo(rubros, "getStrNombreRubro");
		contrato.setModelo(contratos);
		solicitud.setModelo(solicitudes);
		labor.setModelo(labores);
		cedula.setModelo(productores);
		tecnicoCampo.setModelo(tecnicos);
		tipoFalla.setModelo(fallas);
		momentoFalla.setModelo(momentosFalla);

		tecnicoCampo.setReadonly(true);
		fisico.setDisabled(true);
		cantidad.setDisabled(true);
		pases.setDisabled(true);
		total.setDisabled(true);
		setVisibleCantidad(false);
		setVisiblePases(false);
		nroControl.setDisabled(true);
		sede.setDisabled(true);
		fecha.setDisabled(true);
		ingreso.setDisabled(true);
		nombreProductor.setDisabled(true);
		tiempoEspera.setDisabled(true);
		totalPosibleProducido.setDisabled(true);
		totalTrabajado.setDisabled(true);

		observacion.setMultiline(true);
		observacion.setCols(2);
		observacion.setWidth("590px");
		nombreProductor.setWidth("420px");
		financiamiento.setWidth("420px");
		labor.setWidth("500px");
		unidadProductiva.setWidth("420px");
		cedula.setWidth("100px");
		cedula.setAncho(500);
		tecnicoCampo.setWidth("500px");
		contrato.setWidth("100px");
		contrato.setAncho(500);
		solicitud.setWidth("100px");
		solicitud.setAncho(500);
		ingreso.setWidth("100px");

		unidadFuncional.setReadonly(true);
		cicloProductivo.setReadonly(true);
		financiamiento.setReadonly(true);
		labor.setReadonly(true);
		rubro.setReadonly(true);
		planServicio.setReadonly(true);
		unidadProductiva.setReadonly(true);

		cedula.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
		solicitud.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
		contrato.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
		labor.addEventListener(Events.ON_SELECTION, getControlador());
		cantidad.addEventListener(Events.ON_CHANGE, getControlador());
		pases.addEventListener(Events.ON_CHANGE, getControlador());
		fisico.addEventListener(Events.ON_CHANGE, getControlador());
		unidadFuncional.addEventListener(Events.ON_SELECTION, getControlador());
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Nro Control:", nroControl);
		gbGeneral.addComponente("Fecha :", fecha);
		gbGeneral.addComponente("Sede :", sede);
		gbGeneral.addComponente("Unidad Ejecutora :", unidadFuncional);
		gbGeneral.addComponente("Contrato :", contrato);
		gbGeneral.addComponente("Solicitud :", solicitud);

		gbGeneral.addComponente("Ciclo Productivo :", cicloProductivo);
		gbGeneral.addComponente("Rubro :", rubro);
		gbGeneral.addComponente("Plan servicio :", planServicio);
		gbGeneral.addComponente("Fecha Inicio:", inicio);
		gbGeneral.addComponente("Tiempo Espera ", tiempoEspera);

		gbProductor.setAnchoColumna(0, 170);
		gbProductor.addComponenteMultiples("Cedula:", 170, cedula, new Label(
				"Fecha Ingreso:"), ingreso);
		gbProductor.addComponente("Nombre Completo:", nombreProductor);
		gbProductor.addComponente("Unidad Produccion:", unidadProductiva);
		gbProductor.addComponente("Financiamiento:", financiamiento);

		gbLabor.addComponenteMultiples("Tecnico Campo:", 520, tecnicoCampo,
				apto);
		gbLabor.addComponente("Labor:", labor);
		gbLabor.addComponenteMultiples("Fisico:", 90, fisico, unidadFisica,
				lpases, pases, lcantidad, cantidad, new Label("Total:"), total);

		gbObservacion.addComponente(observacion);

		gbCierreTrabajo.addComponente("Total Trabajado:", totalTrabajado);
		gbCierreTrabajo.addComponente("Total Fisico Trabajado:",
				totalFisicoTrabajado);

		gbCierreProducido.addComponente("Total Produccion (posible):",
				totalPosibleProducido);
		gbCierreProducido.addComponente(usaTransporte);
		gbCierreProducido.addComponente("Total Real Producido:",
				totalRealProducido);

		gbFalla.addComponente("Tipo de Falla:", tipoFalla);
		gbFalla.addComponente("Momento Falla", momentoFalla);
		gbFalla.addComponente("Equipo:", equipo);

		gbGeneral.dibujar(getGbEncabezado());
		gbProductor.dibujar(getGbEncabezado());
		gbLabor.dibujar(getGbEncabezado());

		setDetalles(cargarListaDetalle(), getModelo().getEquipos(),
				listaDetalle);
		getDetalle().setNuevo(new DetalleMaquinariaOrdenTrabajo());
		getGbDetalle().appendChild(detalleTrabajos);
		gbObservacion.dibujar(getGbPie());
		gbCierreTrabajo.dibujar(getGbPie());
		gbCierreProducido.dibujar(getGbPie());
		gbFalla.dibujar(getGbPie());
		dibujarEstructura();
	}

	public void cargarValores(OrdenTrabajoMecanizado solicitudMeca)
			throws ExcDatosInvalidos {
		unidadFuncional.setSeleccion(getModelo().getUnidadFuncional());
		getBinder().addBinding(unidadFuncional, "seleccion",
				getNombreModelo() + ".unidadFuncional", null, null, "save",
				null, null, null, null);
		cicloProductivo.setSeleccion(getModelo().getCiclo());
		getBinder().addBinding(cicloProductivo, "seleccion",
				getNombreModelo() + ".ciclo", null, null, "save", null, null,
				null, null);
		planServicio.setSeleccion(getModelo().getPlan());
		getBinder().addBinding(planServicio, "seleccion",
				getNombreModelo() + ".plan", null, null, "save", null, null,
				null, null);
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
		inicio.setValue(getModelo().getInicioServicio());
		getBinder().addBinding(inicio, "value",
				getNombreModelo() + ".inicioServicio", null, null, "save",
				null, null, null, null);

		rubro.setSeleccion(getModelo().getRubro());
		getBinder().addBinding(rubro, "seleccion",
				getNombreModelo() + ".rubro", null, null, "save", null, null,
				null, null);
		financiamiento.setSeleccion(getModelo().getFinanciamiento());
		getBinder().addBinding(financiamiento, "seleccion",
				getNombreModelo() + ".financiamiento", null, null, "save",
				null, null, null, null);
		refrescarProductor(getModelo().getProductor());
		unidadProductiva.setSeleccion(getModelo().getUnidadProductiva());
		getBinder().addBinding(unidadProductiva, "seleccion",
				getNombreModelo() + ".unidadProductiva", null, null, "save",
				null, null, null, null);
		if (getModelo().getSolicitud() != null)
			solicitud.setSeleccion((SolicitudMecanizado) getModelo()
					.getSolicitud());
		getBinder().addBinding(solicitud, "seleccion",
				getNombreModelo() + ".solicitud", null, null, "save", null,
				null, null, null);
		if (getModelo().getSolicitud() != null)
			contrato.setSeleccion((ContratoMecanizado) getModelo()
					.getContrato());
		getBinder().addBinding(contrato, "seleccion",
				getNombreModelo() + ".contrato", null, null, "save", null,
				null, null, null);
		if (getModelo().getLaborOrden() != null) {
			LaborOrdenServicio labor = getModelo().getLaborOrden();
			this.labor.setSeleccion(labor.getLabor());
			cantidad.setValue(labor.getCantidad());
			pases.setValue(labor.getPases());
			fisico.setValue(labor.getFisico());
			total.setValue(labor.getCalculo());
			unidadFisica.setModelo(labor.getUnidad());
			unidadFisica.setValue(labor.getUnidad().getAbreviatura());
			visibilidadLabor(labor.getLabor().getServicio().getManejaPases(),
					labor.getLabor().getServicio().getManejaCantidades());
		}
		if (getModelo().getSede() != null)
			sede.setValue(getModelo().getSede().getNombre());
		if (getModelo().getNroControl() != null)
			nroControl.setValue(getModelo().getNroControl());
		if (getModelo().getTrabajosRealizadosMecanizado() != null)
			detalleTrabajos.setModelo(getModelo()
					.getTrabajosRealizadosMecanizado());
		if (getModelo().getTecnicoCampo() != null)
			tecnicoCampo.setSeleccion(getModelo().getTecnicoCampo());
		getBinder().addBinding(tecnicoCampo, "seleccion",
				getNombreModelo() + ".tecnicoCampo", null, null, "save", null,
				null, null, null);
		if (getModelo().getActaProduccion() != null)
			apto.setChecked(getModelo().getActaProduccion());
		getBinder().addBinding(apto, "checked",
				getNombreModelo() + ".actaProduccion", null, null, "save",
				null, null, null, null);
		if (getModelo().getTransportado() != null)
			usaTransporte.setChecked(getModelo().getTransportado());
		if (getModelo().getCantidadFisicaTrabajada() != null)
			totalFisicoTrabajado.setValue(getModelo()
					.getCantidadFisicaTrabajada());
		getBinder().addBinding(totalFisicoTrabajado, "value",
				getNombreModelo() + ".cantidadFisicaTrabajada", null, null,
				"save", null, null, null, null);
		if (getModelo().getCantidadRealProducida() != null)
			totalRealProducido.setValue(getModelo().getCantidadRealProducida());
		getBinder().addBinding(totalRealProducido, "value",
				getNombreModelo() + ".cantidadRealProducida", null, null,
				"save", null, null, null, null);
		if (getModelo().getProduccion() != null)
			setVisibleProduccion(getModelo().getProduccion());
		try {
			dibujarDetalle();
		} catch (Exception e) {
			throw new ExcDatosInvalidos();
		}
	}

	private void setVisibleProduccion(boolean produccion) {
		apto.setVisible(produccion);
		if (!modoNuevo)
			gbCierreProducido.setVisible(produccion);
	}

	private void setVisiblePases(boolean pase) {
		lpases.setVisible(pase);
		pases.setVisible(pase);
	}

	private void setVisibleCantidad(boolean cantidad) {
		lcantidad.setVisible(cantidad);
		this.cantidad.setVisible(cantidad);
	}

	public void visibilidadLabor(boolean pases, boolean cantidad) {
		setVisibleCantidad(cantidad);
		setVisiblePases(pases);
	}

	public void desactivarLabor(boolean nuevo) {
		cantidad.setDisabled(nuevo);
		pases.setDisabled(nuevo);
		fisico.setDisabled(nuevo);
	}

	private void encabezadosPrimario() {

		listaDetalle = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Operador", 180);
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

		// operador.setModelo(operadores);
		operador.setModelo(operadores, "getNroCedula");
		lista.add(operador);
		lista.add(maquinaria);
		lista.add(implemento);
		return lista;
	}

	public void dibujarDetalle() throws ExcFiltroExcepcion {
		if (getModelo().getDetalles() != null
				&& getModelo().getUnidadFuncional() != null) {
			ContOrdenTrabajoMecanizado control;
			control = (ContOrdenTrabajoMecanizado) getControlador();
			control.actualizarActivos(getModelo().getUnidadFuncional());
			for (DetalleMaquinariaOrdenTrabajo item : getModelo().getEquipos()) {
				getDetalle().agregar(item);
			}
		}
	}

	public void refrescarProductor(Productor productor) {
		if (productor != null) {
			nombreProductor.setValue(productor.getNombres());
			unidadProductiva.setModelo(productor.getUnidadesproduccion());
			ingreso.setValue(productor.getSrtFechaIngreso());
		}
	}

	private List<CompEncabezado> encabezadosTrabajosDiarios() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Fecha", 90);
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Trabajo Realizado", 120);
		titulo.setMetodoBinder("getCantidadTrabajo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tiempo Trabajo", 120);
		titulo.setMetodoBinder("horasTrabajadas");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Horometro Consumo", 120);
		titulo.setMetodoBinder("getTotalHorometro");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Horometro Efectivo", 120);
		titulo.setMetodoBinder("getHorometroEfectivo");
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> getEncabezadoContrato() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Control", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrNroDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getFechaString");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cliente", 300);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreCliente");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto Total", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMonto");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getEstado");
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> getEncabezadoSolicitud() {
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

		titulo = new CompEncabezado("Unidad Ejecutora", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrUnidadEjecutora");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Productor", 170);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrProductor");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Servicio", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrServicio");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
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

	public CompCombobox<CicloProductivo> getCicloProductivo() {
		return cicloProductivo;
	}

	public CompCombobox<UnidadProductiva> getUnidadProductiva() {
		return unidadProductiva;
	}

	public List<CicloProductivo> getCiclosProductivos() {
		return ciclosProductivos;
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
		else if (modoOperacion == Accion.CORREGIR)
			modoFalla();
		else if (modoOperacion == Accion.PROCESAR)
			modoCierre();
	}

	public void activarConsulta() {
		cedula.setDisabled(true);
		rubro.setDisabled(true);
		unidadFuncional.setDisabled(true);
		cicloProductivo.setDisabled(true);
		planServicio.setDisabled(true);
		financiamiento.setDisabled(true);
		contrato.setDisabled(true);
		solicitud.setDisabled(true);
		inicio.setDisabled(true);
		labor.setDisabled(true);
		tecnicoCampo.setDisabled(true);
		apto.setDisabled(true);
		unidadProductiva.setDisabled(true);
		desactivarDetalle();
		if (anulado)
			gbFalla.setVisible(true);
		else
			gbFalla.setVisible(false);
		if (cerrado) {
			gbCierreProducido.setVisible(getModelo().getProduccion());
			gbCierreTrabajo.setVisible(true);
		} else {
			gbCierreProducido.setVisible(false);
			gbCierreTrabajo.setVisible(false);
		}
		observacion.setDisabled(true);

		totalPosibleProducido.setDisabled(true);
		totalTrabajado.setDisabled(true);
		totalFisicoTrabajado.setDisabled(true);
		totalRealProducido.setDisabled(true);
		usaTransporte.setDisabled(true);

		tipoFalla.setDisabled(true);
		momentoFalla.setDisabled(true);
		equipo.setDisabled(true);
		apto.setVisible(getModelo().getProduccion());
	}

	public void modoFalla() {
		cedula.setDisabled(true);
		rubro.setDisabled(true);
		unidadFuncional.setDisabled(true);
		cicloProductivo.setDisabled(true);
		planServicio.setDisabled(true);
		financiamiento.setDisabled(true);
		contrato.setDisabled(true);
		solicitud.setDisabled(true);
		inicio.setDisabled(true);
		labor.setDisabled(true);
		tecnicoCampo.setDisabled(true);
		apto.setDisabled(true);
		unidadProductiva.setDisabled(true);
		gbFalla.setVisible(true);
		gbCierreProducido.setVisible(false);
		gbCierreTrabajo.setVisible(false);

		tipoFalla.setDisabled(false);
		momentoFalla.setDisabled(false);
		equipo.setDisabled(false);
	}

	public void modoCierre() {
		cedula.setDisabled(true);
		rubro.setDisabled(true);
		unidadFuncional.setDisabled(true);
		cicloProductivo.setDisabled(true);
		planServicio.setDisabled(true);
		financiamiento.setDisabled(true);
		contrato.setDisabled(true);
		solicitud.setDisabled(true);
		inicio.setDisabled(true);
		labor.setDisabled(true);
		tecnicoCampo.setDisabled(true);
		unidadProductiva.setDisabled(true);
		apto.setDisabled(true);
		gbCierreProducido.setVisible(getModelo().getProduccion());
		gbCierreTrabajo.setVisible(true);
		gbFalla.setVisible(false);

		totalRealProducido.setDisabled(true);
		usaTransporte.setDisabled(true);
		tipoFalla.setDisabled(true);
		momentoFalla.setDisabled(true);
		equipo.setDisabled(true);
		apto.setVisible(getModelo().getProduccion());
	}

	public void modoNuevo() {
		cedula.setDisabled(false);
		rubro.setDisabled(false);
		apto.setVisible(false);
		unidadFuncional.setDisabled(false);
		cicloProductivo.setDisabled(false);
		planServicio.setDisabled(false);
		unidadProductiva.setDisabled(false);
		gbFalla.setVisible(false);
		gbCierreProducido.setVisible(false);
		gbCierreTrabajo.setVisible(false);
		observacion.setDisabled(false);

	}

	public Intbox getTiempoEspera() {
		return tiempoEspera;
	}

	public Doublebox getPases() {
		return pases;
	}

	public Doublebox getFisico() {
		return fisico;
	}

	public Doublebox getCantidad() {
		return cantidad;
	}

	public Doublebox getTotal() {
		return total;
	}

	public CompTexto<UnidadMedida> getUnidadFisica() {
		return unidadFisica;
	}

	public Datebox getInicio() {
		return inicio;
	}

	public CompCombobox<Rubro> getRubro() {
		return rubro;
	}

	public CompCombobox<PlanServicio> getPlanServicio() {
		return planServicio;
	}

	public CompBuscar<ContratoMecanizado> getContrato() {
		return contrato;
	}

	public CompBuscar<SolicitudMecanizado> getSolicitud() {
		return solicitud;
	}

	public CompCombobox<InstitucionCrediticia> getFinanciamiento() {
		return financiamiento;
	}

	public void refrescarLabor(List<Labor> labores) {
		this.labores = labores;
		labor.setModelo(this.labores);
	}

	public void refrescarDatosLabor(LaborOrdenServicio labor, boolean nueva) {
		desactivarLabor(!nueva);
		unidadFisica.setModelo(labor.getUnidad());
		unidadFisica.setValue(labor.getUnidad().getAbreviatura());
		// if (!nueva){
		fisico.setValue(labor.getFisico());
		pases.setValue(labor.getPases());
		cantidad.setValue(labor.getCantidad());
		total.setValue(labor.getCalculo());
		visibilidadLabor(labor.isPasesVisible(), labor.isCantidadVisible());
		apto.setVisible(labor.getLabor().getServicio().getProduccion());
	}

	public CompLista<TrabajoRealizadoMecanizado> getDetalleTrabajos() {
		return detalleTrabajos;
	}

	public Doublebox getTotalPosibleProducido() {
		return totalPosibleProducido;
	}

	public Doublebox getTotalTrabajado() {
		return totalTrabajado;
	}

	public Doublebox getTotalFisicoTrabajado() {
		return totalFisicoTrabajado;
	}

	public Doublebox getTotalRealProducido() {
		return totalRealProducido;
	}

	public Checkbox getUsaTransporte() {
		return usaTransporte;
	}

	public CompCombobox<TipoFalla> getTipoFalla() {
		return tipoFalla;
	}

	public CompCombobox<Activo> getEquipo() {
		return equipo;
	}

}
