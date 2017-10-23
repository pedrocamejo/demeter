package cpc.demeter.vista.gestion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Timebox;

import cpc.ares.modelo.Accion;
import cpc.demeter.controlador.gestion.ContTrabajoMecanizado;
import cpc.modelo.demeter.basico.Labor;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.basico.UnidadMedida;
import cpc.modelo.demeter.gestion.*;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompGrilla;
import cpc.zk.componente.listas.CompTexto;
import cpc.zk.componente.modelo.ColumnaAuxiliar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class UiTrabajoMecanizado extends
		CompVentanaBase<TrabajoRealizadoMecanizado> {

	private static final long serialVersionUID = -5739695024627594930L;
	private CompGrupoDatos gbGeneral, gbEmail, gbEmpresas, gbtrabajado,
			GbProducido;

	private Textbox nombreProductor;
	private Textbox ingreso;
	private Textbox sede;
	private Doublebox pases;
	private Doublebox fisico;
	private Doublebox cantidad;
	private Doublebox total;
	private Label unidadFisica;
	private Label lpases;
	private Label lcantidad;
	private CompBuscar<OrdenTrabajoMecanizado> nroControl;
	private Datebox fecha;
	private CompTexto<UnidadFuncional> unidadFuncional;
	private Textbox unidadProductiva;
	private CompTexto<Labor> labor;
	private Textbox cedula;
	private Textbox contrato;
	private Textbox solicitud;

	private Doublebox cantidadTrabajo;
	private Doublebox cantidadProducido;
	private CompTexto<UnidadMedida> unidadTrabajada;
	private CompTexto<UnidadMedida> unidadproducida;
	private Timebox inicio;
	private Timebox fin;

	private List<OrdenTrabajoMecanizado> ordenesTrabajo;
	private CompGrilla<DetalleTrabajoRealizadoMecanizado> detalles;

	public UiTrabajoMecanizado(String titulo, int ancho,
			List<OrdenTrabajoMecanizado> ordenesTrabajo)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.ordenesTrabajo = ordenesTrabajo;
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Orden Trabajo", 4);
		gbEmail = new CompGrupoDatos("Productor", 2);
		gbEmpresas = new CompGrupoDatos("Labor", 2);
		GbProducido = new CompGrupoDatos("Producido", 3);
		gbtrabajado = new CompGrupoDatos("Trabajado", 4);
		unidadFuncional = new CompTexto<UnidadFuncional>();

		labor = new CompTexto<Labor>();
		unidadProductiva = new Textbox();

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
		nroControl = new CompBuscar<OrdenTrabajoMecanizado>(
				EncabezadoOrdenTrabajo(), 0);
		cedula = new Textbox();
		contrato = new Textbox();
		solicitud = new Textbox();
		fecha = new Datebox();

		cantidadTrabajo = new Doublebox();
		cantidadProducido = new Doublebox();
		unidadTrabajada = new CompTexto<UnidadMedida>();
		unidadproducida = new CompTexto<UnidadMedida>();
		detalles = new CompGrilla<DetalleTrabajoRealizadoMecanizado>();
		inicio = new Timebox(new Date());
		fin = new Timebox(new Date());

		encabezadosPrimario();
		unidadProductiva.setMultiline(true);
		unidadProductiva.setRows(2);
		nroControl.setAncho(800);
		nroControl.setModelo(ordenesTrabajo);
		fisico.setDisabled(true);
		cantidad.setDisabled(true);
		pases.setDisabled(true);
		cedula.setDisabled(true);
		total.setDisabled(true);
		contrato.setDisabled(true);
		solicitud.setDisabled(true);
		cedula.setDisabled(true);
		unidadProductiva.setDisabled(true);
		setVisibleCantidad(false);
		setVisiblePases(false);

		nroControl.setDisabled(false);
		sede.setDisabled(true);
		fecha.setDisabled(false);
		ingreso.setDisabled(true);
		nombreProductor.setDisabled(true);
		detalles.setPermiteEliminar(false);
		detalles.addEncabezadoAuxiliar(encabezadosSuperiorPrimario());
		detalles.setEncabezados(encabezadosPrimario());
		detalles.setComponentes(cargarListaDetalle());

		nombreProductor.setWidth("435px");
		labor.setWidth("500px");
		unidadProductiva.setWidth("435px");
		cedula.setWidth("100px");
		contrato.setWidth("100px");
		solicitud.setWidth("100px");
		ingreso.setWidth("100px");
		nroControl.setWidth("100px");
		nroControl.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Orden Trabajo:", nroControl);
		gbGeneral.addComponente("Fecha :", fecha);
		gbGeneral.addComponente("Sede :", sede);
		gbGeneral.addComponente("Unidad Ejecutora :", unidadFuncional);
		gbGeneral.addComponente("Contrato :", contrato);
		gbGeneral.addComponente("Solicitud :", solicitud);
		gbEmpresas.setAnchoColumna(0, 110);
		gbEmail.setAnchoColumna(0, 170);
		gbEmail.addComponenteMultiples("Cedula:", 170, cedula, new Label(
				"Fecha Ingreso:"), ingreso);
		gbEmail.addComponente("Nombre Completo:", nombreProductor);
		gbEmail.addComponente("Unidad Produccion:", unidadProductiva);
		gbEmpresas.addComponente("Labor:", labor);
		gbEmpresas.addComponenteMultiples("Fisico:", 50, fisico, unidadFisica,
				lpases, pases, lcantidad, cantidad, new Label("Total:"), total);
		gbtrabajado.addComponente("Inicio:", inicio);
		gbtrabajado.addComponente("Fin:", fin);
		gbtrabajado.addComponente("Cantidad Trabajada:", cantidadTrabajo);
		gbtrabajado.addComponente(unidadTrabajada);
		GbProducido.addComponente("Cantidad Producida:", cantidadProducido);
		GbProducido.addComponente(unidadproducida);
		gbGeneral.dibujar(this);
		gbEmail.dibujar(this);
		gbEmpresas.dibujar(this);
		detalles.dibujar();
		this.appendChild(detalles);
		gbtrabajado.dibujar(this);
		GbProducido.dibujar(this);
		addBotonera();
	}

	public void cargarValores(TrabajoRealizadoMecanizado solicitudMeca)
			throws ExcDatosInvalidos {
		nroControl.setSeleccion(getModelo().getOrdenTrabajoMecanizado());
		getBinder().addBinding(nroControl, "seleccion",
				getNombreModelo() + ".ordenTrabajo", null, null, "save", null,
				null, null, null);
		cantidadProducido.setValue(getModelo().getCantidadProduccion());
		getBinder().addBinding(cantidadProducido, "value",
				getNombreModelo() + ".cantidadProduccion", null, null, "save",
				null, null, null, null);
		cantidadTrabajo.setValue(getModelo().getCantidadTrabajo());
		getBinder().addBinding(cantidadTrabajo, "value",
				getNombreModelo() + ".cantidadTrabajo", null, null, "save",
				null, null, null, null);
		if (getModelo().getUnidadProduccionRealizada() != null)
			unidadproducida.setModelo(getModelo()
					.getUnidadProduccionRealizada());
		getBinder().addBinding(unidadproducida, "modelo",
				getNombreModelo() + ".unidadProduccionRealizada", null, null,
				"save", null, null, null, null);
		if (getModelo().getUnidadTrabajoRealizado() != null)
			unidadTrabajada.setModelo(getModelo().getUnidadTrabajoRealizado());
		getBinder().addBinding(unidadTrabajada, "modelo",
				getNombreModelo() + ".unidadTrabajoRealizado", null, null,
				"save", null, null, null, null);
		inicio.setValue(getModelo().getHoraInicio());
		getBinder().addBinding(inicio, "value",
				getNombreModelo() + ".horaInicio", null, null, "save", null,
				null, null, null);
		fin.setValue(getModelo().getHoraFinal());
		getBinder().addBinding(fin, "value", getNombreModelo() + ".horaFinal",
				null, null, "save", null, null, null, null);
		try {
			cargarDetalle();
		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
		}
		fecha.setConstraint("before "
				+ String.format("%1$tY%1$tm%1$td", new Date()));
	}

	public void cargarDetalle() throws ExcFiltroExcepcion {
		if (getModelo().getOrdenTrabajoMecanizado() != null) {
			ContTrabajoMecanizado controlador = (ContTrabajoMecanizado) getControlador();
			controlador.actualizarOrdenTrabajo(getModelo()
					.getOrdenTrabajoMecanizado());
			controlador.mostrarDetalle();
		}
	}

	public void setVisibleProduccion(boolean visible) {
		GbProducido.setVisible(visible);
	}

	public void refrescarDatosLabor(LaborOrdenServicio labor, boolean nueva) {
		desactivarLabor(!nueva);
		// unidadFisica.setValue(labor.getUnidad().getAbreviatura());
		unidadFisica.setValue(labor.getUnidad().getDescripcion());

		fisico.setValue(labor.getFisico());
		pases.setValue(labor.getPases());
		cantidad.setValue(labor.getCantidad());
		total.setValue(labor.getCalculo());
		visibilidadLabor(labor.getLabor().getServicio().getManejaPases(),
				labor.isCantidadVisible());
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

	private List<CompEncabezado> encabezadosPrimario() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Operador", 100);
		titulo.setMetodoComponente("modelo");
		titulo.setMetodoBinder("getOperador");
		titulo.setMetodoModelo(".operador");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Maquinaria", 180);
		titulo.setMetodoComponente("modelo");
		titulo.setMetodoBinder("getEquipo");
		titulo.setMetodoModelo(".equipo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Implemento", 180);
		titulo.setMetodoComponente("modelo");
		titulo.setMetodoBinder("getImplemento");
		titulo.setMetodoModelo(".implemento");
		encabezado.add(titulo);

		// titulo = new CompEncabezado("Horometro Inicio",100);
		titulo = new CompEncabezado("Inicio", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getHorometroInicio");
		titulo.setMetodoModelo(".horometroInicio");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Final", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getHorometroFinal");
		titulo.setMetodoModelo(".horometroFinal");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Inicio", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getHorometroEfectivoInicio");
		titulo.setMetodoModelo(".horometroEfectivoInicio");
		encabezado.add(titulo);

		// titulo = new CompEncabezado("H Efectivo final",100);
		titulo = new CompEncabezado("Final", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getHorometroEfectivoFinal");
		titulo.setMetodoModelo(".horometroEfectivoFinal");
		encabezado.add(titulo);

		return encabezado;
	}

	private List<ColumnaAuxiliar> encabezadosSuperiorPrimario() {
		List<ColumnaAuxiliar> encabezado = new ArrayList<ColumnaAuxiliar>();
		ColumnaAuxiliar titulo;
		titulo = new ColumnaAuxiliar();
		titulo.setDescripcion("Personal");
		titulo.setAlineacion(CompEncabezado.CENTRO);
		titulo.setColumnas(1);
		encabezado.add(titulo);

		titulo = new ColumnaAuxiliar();
		titulo.setDescripcion("Equipos");
		titulo.setAlineacion(CompEncabezado.CENTRO);
		titulo.setColumnas(2);
		encabezado.add(titulo);

		titulo = new ColumnaAuxiliar();
		titulo.setDescripcion("Horometro Total");
		titulo.setAlineacion(CompEncabezado.CENTRO);
		titulo.setColumnas(2);
		encabezado.add(titulo);

		titulo = new ColumnaAuxiliar();
		titulo.setDescripcion("Horometro Efectivo");
		titulo.setAlineacion(CompEncabezado.CENTRO);
		titulo.setColumnas(2);
		encabezado.add(titulo);

		return encabezado;
	}

	private ArrayList<Component> cargarListaDetalle() {
		ArrayList<Component> lista = new ArrayList<Component>();
		CompTexto<ImplementoUnidad> implemento = new CompTexto<ImplementoUnidad>();
		CompTexto<MaquinariaUnidad> maquinaria = new CompTexto<MaquinariaUnidad>();
		CompTexto<Trabajador> operador = new CompTexto<Trabajador>();
		Doublebox horometroInicio = new Doublebox();
		Doublebox horometroFinal = new Doublebox();
		Doublebox horometroEfectivoInicio = new Doublebox();
		Doublebox horometroEfectivoFinal = new Doublebox();

		horometroEfectivoFinal.setWidth("50px");
		horometroEfectivoInicio.setWidth("50px");
		horometroFinal.setWidth("50px");
		horometroInicio.setWidth("50px");

		lista.add(operador);
		lista.add(maquinaria);
		lista.add(implemento);
		lista.add(horometroInicio);
		lista.add(horometroFinal);
		lista.add(horometroEfectivoInicio);
		lista.add(horometroEfectivoFinal);
		return lista;
	}

	public List<CompEncabezado> EncabezadoOrdenTrabajo() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

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
		titulo.setMetodoBinder("getStrUnidadFuncional");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Productor", 170);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrProductor");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Labor", 150);
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

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		cedula.setDisabled(true);
		detalles.setDisable(true);
		nroControl.setDisabled(true);
		inicio.setDisabled(true);
		fin.setDisabled(true);
		cantidadTrabajo.setDisabled(true);
		cantidadProducido.setDisabled(true);
	}

	public void modoEdicion() {
		cedula.setDisabled(false);
		detalles.setDisable(false);
		nroControl.setDisabled(false);
		inicio.setDisabled(false);
		fin.setDisabled(false);
		cantidadTrabajo.setDisabled(false);
		cantidadProducido.setDisabled(false);
	}

	public CompBuscar<OrdenTrabajoMecanizado> getNroControl() {
		return nroControl;
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

	public Label getUnidadFisica() {
		return unidadFisica;
	}

	public Datebox getFecha() {
		return fecha;
	}

	public CompTexto<UnidadFuncional> getUnidadFuncional() {
		return unidadFuncional;
	}

	public Textbox getUnidadProductiva() {
		return unidadProductiva;
	}

	public CompTexto<Labor> getLabor() {
		return labor;
	}

	public Textbox getCedula() {
		return cedula;
	}

	public Textbox getContrato() {
		return contrato;
	}

	public Textbox getSolicitud() {
		return solicitud;
	}

	public Doublebox getCantidadTrabajo() {
		return cantidadTrabajo;
	}

	public Doublebox getCantidadProducido() {
		return cantidadProducido;
	}

	public CompTexto<UnidadMedida> getUnidadTrabajada() {
		return unidadTrabajada;
	}

	public CompTexto<UnidadMedida> getUnidadproducida() {
		return unidadproducida;
	}

	public CompGrilla<DetalleTrabajoRealizadoMecanizado> getDetalles() {
		return detalles;
	}

	public Timebox getInicio() {
		return inicio;
	}

	public Timebox getFin() {
		return fin;
	}

	public void SetVisibleGbProducido(boolean visible) {
		GbProducido.setVisible(visible);
	}
}
