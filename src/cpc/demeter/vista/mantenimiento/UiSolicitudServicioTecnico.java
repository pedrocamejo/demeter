package cpc.demeter.vista.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.controlador.gestion.ContSolicitudMecanizado;
import cpc.demeter.controlador.mantenimiento.ContSolicitudServicioTecnico;
import cpc.modelo.demeter.basico.CicloProductivo;
import cpc.modelo.demeter.basico.PlanServicio;
import cpc.modelo.demeter.basico.Rubro;
import cpc.modelo.demeter.basico.Servicio;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.basico.UnidadMedida;
import cpc.modelo.demeter.gestion.AnulacionSolicitud;
import cpc.modelo.demeter.gestion.DetalleSolicitud;
import cpc.modelo.demeter.gestion.MotivoAnulacionSolicitud;

import cpc.modelo.demeter.gestion.UnidadSolicitada;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.demeter.mantenimiento.SolicitudServicioTecnico;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.modelo.ministerio.gestion.InstitucionCrediticia;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.modelo.ministerio.gestion.UnidadProductiva;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.persistencia.demeter.implementacion.gestion.PerAnulacionSolicitud;
import cpc.persistencia.demeter.implementacion.gestion.PerEstadoSolicitud;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompDetalleGrilla;
import cpc.zk.componente.listas.CompTexto;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class UiSolicitudServicioTecnico extends
		CompVentanaMaestroDetalle<SolicitudServicioTecnico, DetalleSolicitud> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2935044110987183727L;

	private CompGrupoDatos gbGeneral, gbEmail, gbEmpresas, gbAnulacion,
			gbObservacion;

	private Textbox nombreProductor;
	private Textbox ingreso;
	private Textbox sede;
	private Textbox nroControl;
	private Textbox observacion;
	private Textbox serial;
	private Datebox fecha;
	private CompCombobox<UnidadFuncional> unidadFuncional;

	private CompCombobox<Trabajador> responsable;
	// private CompCombobox<UnidadProductiva> unidadProductiva;
	// private Label codigounidadProductiva;

	private Label marca;

	private CompCombobox<Servicio> servicio;

	private CompCombobox<InstitucionCrediticia> financiamiento;
	// private Checkbox prestado;
	// private Checkbox planificado;
	// private Checkbox aprobado;
	private Label estadosolictud;
	private CompBuscar<IProducto> producto;
	private CompBuscar<Productor> cedula;

	private List<CompEncabezado> listaDetalle;

	private List<CicloProductivo> ciclosProductivos;
	private List<UnidadFuncional> unidadesEjecutoras;
	private CompBuscar<Modelo> cmbModelos;
	private List<PlanServicio> planesServicio;
	private List<Servicio> servicios;
	private List<Trabajador> trabajadores;
	private List<Productor> productores;
	private List<Rubro> rubros;
	private List<InstitucionCrediticia> financiamientos;
	private List<Modelo> modelos;
	private Textbox usuarioanul;
	private Datebox fechaanul;
	private CompCombobox<MotivoAnulacionSolicitud> motivo;
	private List<MotivoAnulacionSolicitud> motivos;

	protected Button aprobar, rechazar;

	// corre prueba con lo de cantidad

	private CompEncabezado encabezadoPases;
	private CompEncabezado encabezadoCantidad;
	private boolean desactivacantidad;

	public UiSolicitudServicioTecnico(String titulo, int ancho,
			List<CicloProductivo> ciclosProductivos,
			List<UnidadFuncional> unidadesEjecutoras,
			List<PlanServicio> planesServicio, List<Servicio> servicios,
			List<Trabajador> trabajadores, List<Productor> productores,
			List<Rubro> rubros, List<InstitucionCrediticia> financiamientos,
			List<MotivoAnulacionSolicitud> motivos, List<Modelo> modelos)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.ciclosProductivos = ciclosProductivos;
		this.unidadesEjecutoras = unidadesEjecutoras;
		this.planesServicio = planesServicio;
		this.servicios = servicios;
		this.trabajadores = trabajadores;
		this.productores = productores;
		this.rubros = rubros;
		this.financiamientos = financiamientos;
		this.motivos = motivos;
		this.modelos = modelos;
	}

	public void inicializar() {
		setGbEncabezado(1, "");
		setGbDetalle("Detalle Solicitud");
		gbGeneral = new CompGrupoDatos("Datos generales", 4);
		gbEmail = new CompGrupoDatos("Productor", 2);
		gbEmpresas = new CompGrupoDatos("Labor", 2);
		gbAnulacion = new CompGrupoDatos("anulacion", 2);
		gbObservacion = new CompGrupoDatos("Observacion", 1);

		unidadFuncional = new CompCombobox<UnidadFuncional>();
		responsable = new CompCombobox<Trabajador>();
		servicio = new CompCombobox<Servicio>();
		// unidadProductiva = new CompCombobox<UnidadProductiva>();
		cmbModelos = new CompBuscar<Modelo>(getEncabezadoModelo(), 1);
		// codigounidadProductiva = new Label();
		financiamiento = new CompCombobox<InstitucionCrediticia>();

		// prestado = new Checkbox("Prestado");
		// planificado = new Checkbox("Planificado");
		// aprobado = new Checkbox("Aprobado");
		estadosolictud = new Label();
		marca = new Label();
		marca.setWidth("250px");
		nombreProductor = new Textbox();
		ingreso = new Textbox();
		sede = new Textbox();
		nroControl = new Textbox();
		observacion = new Textbox();
		serial = new Textbox();
		cedula = new CompBuscar<Productor>(getEncabezadoCliente(), 0);
		fecha = new Datebox();

		usuarioanul = new Textbox();
		fechaanul = new Datebox();
		motivo = new CompCombobox<MotivoAnulacionSolicitud>();

		aprobar = new Button("Aprobar");
		rechazar = new Button("Rechazar");

		encabezadoCantidad = new CompEncabezado();
		encabezadoPases = new CompEncabezado();
		encabezadosPrimario();

		unidadFuncional.setModelo(unidadesEjecutoras);

		responsable.setModelo(trabajadores);

		// rubro.setModelo(rubros);

		servicio.setModelo(servicios);
		financiamiento.setModelo(financiamientos);
		motivo.setModelo(motivos);
		nroControl.setDisabled(true);
		cedula.setModelo(productores);
		sede.setDisabled(true);
		fecha.setDisabled(true);
		ingreso.setDisabled(true);
		nombreProductor.setDisabled(true);
		/*
		 * prestado.setDisabled(true); planificado.setDisabled(true);
		 * aprobado.setDisabled(true);
		 */
		estadosolictud.setVisible(true);
		nombreProductor.setWidth("420px");
		responsable.setWidth("420px");
		servicio.setWidth("420px");
		// unidadProductiva.setWidth("410px");
		// codigounidadProductiva.setWidth("250px");
		financiamiento.setWidth("410px");
		cedula.setWidth("100px");
		estadosolictud.setWidth("100px");
		estadosolictud.setStyle("font-size: 18px; font-weigth: bold;");
		cedula.setAncho(500);
		ingreso.setWidth("100px");
		unidadFuncional.setReadonly(true);

		servicio.setReadonly(true);
		responsable.setReadonly(true);

		financiamiento.setReadonly(true);
		// unidadProductiva.setReadonly(true);
		gbAnulacion.setVisible(false);
		aprobar.setVisible(false);
		rechazar.setVisible(false);
		cedula.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
		servicio.addEventListener(Events.ON_SELECTION, getControlador());
		// unidadProductiva.addEventListener(Events.ON_SELECTION,
		// getControlador());
		aprobar.addEventListener(Events.ON_CLICK, getControlador());
		rechazar.addEventListener(Events.ON_CLICK, getControlador());

	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Nro Control:", nroControl);
		gbGeneral.addComponente("Fecha :", fecha);
		gbGeneral.addComponente("Sede :", sede);
		gbGeneral.addComponente("Unidad Ejecutora:", unidadFuncional);

		gbGeneral.addComponente("", new Label());
		// para eliminar cuando este listo
		// gbGeneral.addComponenteMultiples("", 170,
		// prestado,planificado,aprobado);
		gbGeneral.addComponente("Estado de Solicitud", estadosolictud);
		gbGeneral.addComponente(aprobar);
		gbGeneral.addComponente(rechazar);

		gbEmail.setAnchoColumna(0, 170);
		gbEmail.addComponenteMultiples("Cedula:", 170, cedula, new Label(
				"Fecha Ingreso:"), ingreso);
		gbEmail.addComponente("Nombre Completo:", nombreProductor);
		// gbEmail.addComponente("Unidad Produccion:", unidadProductiva);

		cmbModelos.setAncho(550);
		cmbModelos.setWidth("300px");
		cmbModelos.setModelo(modelos);
		cmbModelos.setAttribute("nombre", "modelos");
		cmbModelos.setListenerEncontrar(getControlador());

		observacion.setRows(2);
		observacion.setWidth("600px");
		observacion.setMaxlength(200);
		gbEmail.addComponente("Marca:", marca);
		gbEmail.addComponente("Modelo: ", cmbModelos);
		gbEmail.addComponente("Serial: ", serial);

		// gbEmail.addComponente("Codigo UnidadProduccion:",
		// codigounidadProductiva);
		gbEmail.addComponente("Financiamiento:", financiamiento);

		gbEmpresas.addComponente("Capturado por:", responsable);
		gbEmpresas.addComponente("Servicio:", servicio);

		gbAnulacion.addComponente("Anulado por:", usuarioanul);
		gbAnulacion.addComponente("Fecha Anulacion:", fechaanul);
		gbAnulacion.addComponente("motivo anulacion", motivo);

		gbObservacion.addComponente(observacion);
		gbGeneral.dibujar(getGbEncabezado());
		gbEmail.dibujar(getGbEncabezado());
		gbEmpresas.dibujar(getGbEncabezado());
		gbAnulacion.dibujar(getGbEncabezado());
		gbObservacion.dibujar(getGbEncabezado());
		setDetalles(cargarListaDetalle(), getModelo().getDetalles(),
				listaDetalle);
		getDetalle().setNuevo(new DetalleSolicitud());
		dibujarEstructura();

	}

	public Textbox getSerial() {
		return serial;
	}

	public void setSerial(Textbox serial) {
		this.serial = serial;
	}

	public Label getMarca() {
		return marca;
	}

	public void setMarca(Label marca) {
		this.marca = marca;
	}

	public void cargarValores(SolicitudServicioTecnico solicitudMeca)
			throws ExcDatosInvalidos {
		unidadFuncional.setSeleccion(getModelo().getUnidadEjecutora());
		getBinder().addBinding(unidadFuncional, "seleccion",
				getNombreModelo() + ".unidadEjecutora", null, null, "save",
				null, null, null, null);
		responsable.setSeleccion(getModelo().getResponsable());
		getBinder().addBinding(responsable, "seleccion",
				getNombreModelo() + ".responsable", null, null, "save", null,
				null, null, null);
		servicio.setSeleccion(getModelo().getServicio());
		getBinder().addBinding(servicio, "seleccion",
				getNombreModelo() + ".servicio", null, null, "save", null,
				null, null, null);
		cedula.setSeleccion(getModelo().getProductor());
		getBinder().addBinding(cedula, "seleccion",
				getNombreModelo() + ".productor", null, null, "save", null,
				null, null, null);
		fecha.setValue(getModelo().getFecha());
		getBinder().addBinding(fecha, "value", getNombreModelo() + ".fecha",
				null, null, "save", null, null, null, null);
		financiamiento.setSeleccion(getModelo().getFinanciamiento());
		getBinder().addBinding(financiamiento, "seleccion",
				getNombreModelo() + ".financiamiento", null, null, "save",
				null, null, null, null);
		refrescarProductor(getModelo().getProductor());
		if (getModelo().getModelo() != null) {
			cmbModelos.setSeleccion(getModelo().getModelo());
			getBinder().addBinding(cmbModelos, "seleccion",
					getNombreModelo() + ".modelo", null, null, "save", null,
					null, null, null);
			marca.setValue(getModelo().getModelo().getDescripcionMarca());
		}
		serial.setValue(getModelo().getSerial());
		observacion.setValue(getModelo().getObservacion());
		// if (getModelo().getStrProductor())
		// getBinder().addBinding(unidadProductiva, "seleccion",
		// getNombreModelo()+".direccion", null, null, "save", null, null, null,
		// null);
		/*
		 * prestado.setChecked(getModelo().getPrestada());
		 * aprobado.setChecked(getModelo().getAprobada());
		 * planificado.setChecked(getModelo().getPlanificada());
		 */
		estadosolictud.setValue(getModelo().getEstadosolicitud().getEstado());
		if (getModelo().getSede() != null)
			sede.setValue(getModelo().getSede().getNombre());
		if (getModelo().getNroControl() != null)
			nroControl.setValue(getModelo().getNroControl());
		actualizarEncabezados(getModelo().isManejaPases(), getModelo()
				.isManejaCantidad());
		String a = getModelo().getEstadosolicitud().getEstado();
		Object b = new PerEstadoSolicitud().getrechazada().getEstado();
		boolean c = a.equals(b);
		if (c) {
			try {
				AnulacionSolicitud anul = new PerAnulacionSolicitud()
						.getAnulacionServicioTecnico(getModelo());
				System.out.println(anul.getusuario());
				System.out.println(anul.getfechaanulacion());
				System.out.println("motivo de anulacion "
						+ anul.getmotivoanulacionsolicitud().getMotivo());
				usuarioanul.setValue(anul.getusuario());
				fechaanul.setValue(anul.getfechaanulacion());

				motivo.setSeleccion(anul.getmotivoanulacionsolicitud()
						.getMotivo());

			} catch (ExcFiltroExcepcion e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		try {
			dibujarDetalle();
		} catch (Exception e) {
			throw new ExcDatosInvalidos();
		}
	}

	public void dibujarDetalle() throws ExcFiltroExcepcion {
		if (getModelo().getDetalles() != null) {
			ContSolicitudServicioTecnico control;
			control = (ContSolicitudServicioTecnico) getControlador();
			control.actualizarServicio();
			for (DetalleSolicitud item : getModelo().getDetalles()) {
				getDetalle().agregar(item);
				// refrescarUnidades(item.getSolicitado(),
				// (CompDetalleGrilla<UnidadSolicitada>)registro.getChildren().get(3));
			}
		}
	}

	public void refrescarProductor(Productor productor) {
		if (productor != null) {
			nombreProductor.setValue(productor.getNombres());
			// unidadProductiva.setModelo(productor.getUnidadesproduccion());
			ingreso.setValue(productor.getSrtFechaIngreso());
		}
	}

	private void encabezadosPrimario() {

		listaDetalle = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Servicio/Articulo", 240);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getProducto");
		titulo.setMetodoModelo(".producto");
		// titulo.setOrdenable(true);
		listaDetalle.add(titulo);

		titulo = new CompEncabezado("Cantidad", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidad");
		titulo.setMetodoModelo(".cantidad");
		// titulo.setVisible(false);
		// titulo.setOrdenable(true);
		listaDetalle.add(titulo);

		titulo = new CompEncabezado("Pases", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getPases");
		titulo.setMetodoModelo(".pases");
		// titulo.setVisible(false);
		// titulo.setOrdenable(true);
		listaDetalle.add(titulo);

		titulo = new CompEncabezado("Cantidad x Unidad", 260);
		titulo.setMetodoComponente("coleccion");
		titulo.setMetodoBinder("getSolicitado");
		titulo.setMetodoModelo(".solicitado");
		// titulo.setOrdenable(true);
		listaDetalle.add(titulo);
	}

	private ArrayList<Component> cargarListaDetalle() {
		ArrayList<Component> lista = new ArrayList<Component>();
		producto = new CompBuscar<IProducto>(getEncabezadoArticulo(), 3);
		Doublebox cantidad = new Doublebox();
		Doublebox pases = new Doublebox();
		CompDetalleGrilla<UnidadSolicitada> unidadServicios = new CompDetalleGrilla<UnidadSolicitada>(
				getControlador());
		unidadServicios.setEncabezados(encabezadosUnidades());
		unidadServicios.setComponentes(cargarListaUnidades());
		unidadServicios.setWidth("250px");
		unidadServicios.setClase(UnidadSolicitada.class);
		servicio.setWidth("250px");
		cantidad.setWidth("60px");
		pases.setWidth("60px");
		producto.setAncho(650);
		producto.setListenerEncontrar(getControlador());

		lista.add(producto);
		lista.add(cantidad);
		lista.add(pases);
		lista.add(unidadServicios);

		return lista;
	}

	private List<CompEncabezado> getEncabezadoArticulo() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Serv/Art", 100);
		// titulo.setMetodoBinder("getStrTipoProducto");
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo", 150);
		titulo.setMetodoBinder("getStrTipo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Clase", 150);
		titulo.setMetodoBinder("getStrClase");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Producto", 250);
		titulo.setMetodoBinder("getDescripcion");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
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

	private List<CompEncabezado> encabezadosUnidades() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Cantidad", 70);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidad");
		titulo.setMetodoModelo(".cantidad");
		encabezado.add(titulo);
		titulo = new CompEncabezado("Unidad", 170);
		titulo.setMetodoComponente("modelo");
		titulo.setMetodoBinder("getUnidad");
		titulo.setMetodoModelo(".unidad");
		encabezado.add(titulo);

		return encabezado;
	}

	private ArrayList<Component> cargarListaUnidades() {
		ArrayList<Component> lista = new ArrayList<Component>();
		CompTexto<UnidadMedida> tipo = new CompTexto<UnidadMedida>();
		Doublebox numero = new Doublebox();
		tipo.setWidth("190px");
		numero.setWidth("60px");
		numero.setDisabled(desactivacantidad);
		lista.add(numero);
		lista.add(tipo);

		return lista;
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

	public void refrescarUnidades(List<UnidadMedida> unidades,
			CompDetalleGrilla<UnidadSolicitada> detalleUnidad) {
		detalleUnidad.clear();
		UnidadSolicitada unidad;
		List<UnidadSolicitada> unidadesS = new ArrayList<UnidadSolicitada>();
		for (UnidadMedida item : unidades) {
			unidad = new UnidadSolicitada();
			unidad.setUnidad(item);
			unidadesS.add(unidad);
		}
		detalleUnidad.setColeccion(unidadesS);
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

	public CompCombobox<Trabajador> getResponsable() {
		return responsable;
	}

	/*
	 * public CompCombobox<UnidadProductiva> getUnidadProductiva() { return
	 * unidadProductiva; }
	 */

	// public Label getcodigounidadProductiva(){ return codigounidadProductiva;
	// }

	/*
	 * public Checkbox getPrestado() { return prestado; }
	 * 
	 * public Checkbox getPlanificado() { return planificado; }
	 * 
	 * public Checkbox getAprobado() { return aprobado; }
	 */

	public List<CicloProductivo> getCiclosProductivos() {
		return ciclosProductivos;
	}

	public void setProductos(List<IProducto> productos) {
		producto.setModelo(productos);
	}

	public void refrescarServicio() {
		getDetalle().clear();
	}

	public CompCombobox<Servicio> getServicio() {
		return servicio;
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR) {

			activarConsulta();
		}
		if (modoOperacion == Accion.PROCESAR) {
			modoProcesar();
		} else
			modoEdicion();
	}

	public void activarConsulta() {
		cedula.setDisabled(true);

		unidadFuncional.setDisabled(true);

		responsable.setDisabled(true);
		servicio.setDisabled(true);
		// unidadProductiva.setDisabled(true);
		desactivarDetalle();
		financiamiento.setDisabled(true);
		String a = estadosolictud.getValue();
		String b = new PerEstadoSolicitud().getrechazada().getEstado();
		boolean c = a.equals(b);
		if (c) {
			gbAnulacion.setVisible(true);
			usuarioanul.setDisabled(true);
			fechaanul.setDisabled(true);
			motivo.setDisabled(true);
		}
	}

	public void modoProcesar() {
		cedula.setDisabled(true);

		unidadFuncional.setDisabled(true);

		responsable.setDisabled(true);
		servicio.setDisabled(true);
		// unidadProductiva.setDisabled(true);
		financiamiento.setDisabled(true);
		/*
		 * prestado.setDisabled(true); planificado.setDisabled(true);
		 * aprobado.setDisabled(true);
		 */
		gbAnulacion.setVisible(false);
		usuarioanul.setDisabled(true);
		fechaanul.setDisabled(true);
		motivo.setDisabled(true);
		aprobar.setVisible(true);
		rechazar.setVisible(true);

		desactivarDetalle();

	}

	public void modoEdicion() {
		cedula.setDisabled(false);

		unidadFuncional.setDisabled(false);
		responsable.setDisabled(false);
		// unidadProductiva.setDisabled(false);
		servicio.setDisabled(false);

	}

	public void modoanulacion() {
		cedula.setDisabled(true);

		unidadFuncional.setDisabled(true);
		responsable.setDisabled(true);
		servicio.setDisabled(true);
		// unidadProductiva.setDisabled(true);

		gbAnulacion.setVisible(true);
		usuarioanul.setDisabled(true);
		fechaanul.setDisabled(true);

	}

	public void activarAnular() {
		gbAnulacion.setVisible(true);
		usuarioanul.setDisabled(true);
		fechaanul.setDisabled(true);
		motivo.setDisabled(false);
	}

	public CompCombobox<MotivoAnulacionSolicitud> getmotivo() {
		return motivo;
	}

	public Button getAprovar() {
		return aprobar;
	}

	public Button getRechazar() {
		return rechazar;
	}

	public Label getEstadoSolictud() {
		return estadosolictud;
	}

	public Boolean DesactivarCantidad(int modooperacion) {
		if (modooperacion == Accion.AGREGAR || modooperacion == Accion.EDITAR)
			desactivacantidad = false;
		else {
			desactivacantidad = true;

		}
		return desactivacantidad;
	}

	public CompBuscar<Modelo> getCmbModelos() {
		return cmbModelos;
	}

	public void setCmbModelos(CompBuscar<Modelo> cmbModelos) {
		this.cmbModelos = cmbModelos;
	}

	private List<CompEncabezado> getEncabezadoModelo() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Marca. ", 250);
		titulo.setMetodoBinder("getDescripcionMarca");
		titulo.setOrdenable(true);
		lista.add(titulo);

		titulo = new CompEncabezado("Modelo. ", 250);
		titulo.setMetodoBinder("getDescripcionModelo");
		titulo.setOrdenable(true);
		lista.add(titulo);

		return lista;
	}

}
