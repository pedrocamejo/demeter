package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.zkoss.zhtml.Textarea;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import cpc.demeter.controlador.administrativo.ContContratoMecanizado;
import cpc.demeter.vista.UIInfoPlanFinanciamiento;
import cpc.modelo.demeter.administrativo.ContratoMecanizado;
import cpc.modelo.demeter.administrativo.CuotasAPagarCliente;
import cpc.modelo.demeter.administrativo.DefinicionDeCuotas;
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.administrativo.FrecuenciaDePago;
import cpc.modelo.demeter.administrativo.PlanFinanciamiento;
import cpc.modelo.demeter.administrativo.TipoContrato;
import cpc.modelo.demeter.gestion.SolicitudMecanizado;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.modelo.ministerio.gestion.UnidadProductiva;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class UIContratoMecanizado extends
		CompVentanaMaestroDetalle<ContratoMecanizado, DetalleContrato> {

	private static final long serialVersionUID = 6312240548253365129L;
	private List<Cliente> clientes;
	private List<SolicitudMecanizado> solicitudes;
	private List<TipoContrato> tiposContratos;
	private List<IProducto> productos;
	private List<PlanFinanciamiento> planesFinanciamiento;
	private Label lblNrocontrol, lblEstado, lblDetalleSolicitud,
			lblNombreSolicitante, lblDireccionPagador;
	private Label lblNombrePagador, lblCedulaRif, lblSede, lblDatosSolicitud;
	private CompCombobox<UnidadProductiva> unidadProduccion;

	private Label lblObjetoPlan, lblTipo, lblPorcDescuento, lblPorcInicial,
			lblMontoDescuento, lblMontoInicial, lblMontoContrato,
			lblMontoDeudor;
	private Datebox txtFecha, txtFechaDesde;
	private Intbox txtDiasVigencia;
	private CompBuscar<SolicitudMecanizado> solicitud;
	private CompBuscar<Cliente> cedulaPagador;
	private CompBuscar<Cliente> cedulaProductor;
	private CompCombobox<TipoContrato> tipoContrato;
	private CompCombobox<PlanFinanciamiento> planFinanciamiento;
	// private CompBuscar<PlanFinanciamiento> planFinanciamiento;

	private CompGrupoDatos grpDatosGenerales, grpDatosSolicitud, grpPagador,
			grpPlanFinanciamiento, grpCuotas;
	private Doublebox txtTotal;
	private Textarea txtObservacion;
	private Grid gridServicios, gridFinanciamientoContrato;

	private Checkbox chkGeneral;
	private UIInfoPlanFinanciamiento ventanaPlanFinanciamiento;
	private Button btnGeneracionDeCotas;

	public UIContratoMecanizado(String titulo, int ancho,
			List<Cliente> clientes, List<SolicitudMecanizado> solicitudes,
			List<TipoContrato> tiposContrato,
			List<PlanFinanciamiento> planesFinanciamiento,
			List<IProducto> productos) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.clientes = clientes;
		this.solicitudes = solicitudes;
		this.tiposContratos = tiposContrato;
		this.planesFinanciamiento = planesFinanciamiento;
		this.productos = productos;
	}

	public UIContratoMecanizado(ContratoMecanizado modeloIn, String titulo,
			int ancho) throws ExcDatosInvalidos {
		super(titulo, ancho);
		setModelo(modeloIn);
	}

	public void inicializar() {
		setGbEncabezado(1, "");
		setGbDetalle("Detalle Contrato");
		setGbPie(1);
		gridServicios = new Grid();
		gridFinanciamientoContrato = new Grid();
		btnGeneracionDeCotas = new Button("CALCULAR CUOTAS DE PAGO");
		lblNrocontrol = new Label("POR GENERAR");
		lblEstado = new Label("REGISTRO");
		lblCedulaRif = new Label();
		lblDetalleSolicitud = new Label();
		lblNombrePagador = new Label();
		lblNombreSolicitante = new Label();
		unidadProduccion = new CompCombobox<UnidadProductiva>();
		lblDireccionPagador = new Label();
		lblDatosSolicitud = new Label();
		lblObjetoPlan = new Label("");
		lblTipo = new Label("");
		lblPorcInicial = new Label("00.0");
		lblPorcDescuento = new Label("00.0");
		lblMontoInicial = new Label("0000.0");
		lblMontoDescuento = new Label("0000.0");
		lblMontoContrato = new Label("00.0");

		lblMontoDeudor = new Label("0000.0");

		txtFecha = new Datebox(new Date());
		txtFechaDesde = new Datebox(new Date());
		txtDiasVigencia = new Intbox(1);

		solicitud = new CompBuscar<SolicitudMecanizado>(getCatalogoSolicitud(),
				0);
		cedulaPagador = new CompBuscar<Cliente>(getEncabezadoCliente(), 0);
		cedulaProductor = new CompBuscar<Cliente>(getEncabezadoCliente(), 0);
		tipoContrato = new CompCombobox<TipoContrato>();
		// planFinanciamiento = new
		// CompBuscar<PlanFinanciamiento>(getEncabezadoPlanFinanciamiento(), 0);
		planFinanciamiento = new CompCombobox<PlanFinanciamiento>();

		grpDatosGenerales = new CompGrupoDatos("Datos Generales", 4);
		grpPagador = new CompGrupoDatos("Pagador del Contrato", 2);
		grpDatosSolicitud = new CompGrupoDatos(
				"Datos de la Solicitud y productor", 2);
		// grpServicios = new CompGrupoDatos("Servicios", 2);
		grpPlanFinanciamiento = new CompGrupoDatos("Financiamiento", 4);
		grpCuotas = new CompGrupoDatos("", 1);

		chkGeneral = new Checkbox();
		txtObservacion = new Textarea();
		txtTotal = new Doublebox();
		desactivarDetalle();
		unidadProduccion.setReadonly(true);
		unidadProduccion.setWidth("470px");
		txtFecha.setDisabled(true);
		txtTotal.setDisabled(true);

		txtTotal.setValue(0.00);
		cedulaPagador.setModelo(clientes);
		cedulaProductor.setModelo(clientes);
		cedulaProductor.setDisabled(true);
		clientes = null;
		solicitud.setModelo(solicitudes);
		planFinanciamiento.setModelo(planesFinanciamiento);
		tipoContrato.setModelo(tiposContratos);
		tipoContrato.setReadonly(true);

		/*
		 * try { ventanaPlanFinanciamiento = new UIInfoPlanFinanciamiento();
		 * 
		 * } catch (InterruptedException e) { e.printStackTrace(); }
		 */
		cedulaPagador.setAncho(500);
		cedulaProductor.setAncho(500);
		solicitud.setAncho(650);

		// planFinanciamiento.setAncho(550);
		// planFinanciamiento.getCatalogo().setWidth("900px");
		lblMontoContrato
				.setStyle("font-size: 13px; color:#000050; font-weight:bold");
		lblMontoInicial
				.setStyle("font-size: 13px; color:#004400; font-weight:bold");
		lblMontoDescuento
				.setStyle("font-size: 13px; color:#C03000; font-weight:bold");
		lblMontoDeudor.setStyle("font-size: 13px; font-weight:bold");

		solicitud.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
		cedulaPagador.addEventListener(CompBuscar.ON_SELECCIONO,
				getControlador());
		cedulaProductor.addEventListener(CompBuscar.ON_SELECCIONO,
				getControlador());
		planFinanciamiento.addEventListener(Events.ON_SELECTION,
				getControlador());
		chkGeneral.addEventListener(Events.ON_CHECK, getControlador());
		btnGeneracionDeCotas
				.addEventListener(Events.ON_CLICK, getControlador());
		tipoContrato.addEventListener(Events.ON_SELECTION, getControlador());

	}

	public void dibujar() {
		grpDatosGenerales.addComponente("Estado del Contrato:", lblEstado);
		grpDatosGenerales.addComponente("Nro Control:", lblNrocontrol);
		grpDatosGenerales.addComponente("Fecha del Contrato:", txtFecha);
		grpDatosGenerales.addComponente("Desde Fecha:", txtFechaDesde);
		grpDatosGenerales.addComponente("Duracion (en Dias):", txtDiasVigencia);
		grpDatosGenerales.addComponente("Tipo de Contrato:", tipoContrato);

		grpDatosSolicitud.addComponente("Solicitud:", solicitud);
		grpDatosSolicitud.addComponente("Productor", cedulaProductor);
		grpDatosSolicitud.addComponente("", lblDatosSolicitud);
		grpDatosSolicitud.addComponente("Nombre o Razon Social:",
				lblNombreSolicitante);
		grpDatosSolicitud.addComponente("Direccion :", unidadProduccion);
		grpDatosSolicitud.setAnchoColumna(0, 160);

		grpPagador.addComponente("Pagador:", cedulaPagador);
		grpPagador.addComponente("Nombre o Razon Social:", lblNombrePagador);
		grpPagador.addComponente("Direccion:", lblDireccionPagador);
		grpPagador.setAnchoColumna(0, 160);

		grpDatosGenerales.dibujar(getGbEncabezado());
		grpDatosSolicitud.dibujar(getGbEncabezado());
		grpPagador.dibujar(getGbEncabezado());

		setDetalles(cargarLista(), getModelo().getDetallesContrato(),
				encabezadosDetalle());
		getDetalle().setNuevo(new DetalleContrato());

		grpPlanFinanciamiento.addComponente("Plan de Financiamiento:",
				planFinanciamiento);
		grpPlanFinanciamiento.addComponente("", new Label());
		// grpPlanFinanciamiento.addComponente("Objeto:", lblObjetoPlan);
		// grpPlanFinanciamiento.addComponente("Monto del Contrato:",
		// lblMontoContrato);
		// grpPlanFinanciamiento.addComponente("Monto del Contrato:",
		// lblMontoContrato);
		grpPlanFinanciamiento.addComponente("%Inicial:", lblPorcInicial);
		grpPlanFinanciamiento.addComponente("Monto Inicial:", lblMontoInicial);
		grpPlanFinanciamiento.addComponente("%Desc:", lblPorcDescuento);
		grpPlanFinanciamiento.addComponente("Monto Descuento:",
				lblMontoDescuento);
		grpPlanFinanciamiento.addComponente("Monto Deudor:", lblMontoDeudor);
		/*
		 * grpPlanFinanciamiento.addComponente("%Inicial:", lblPorcInicial);
		 * grpPlanFinanciamiento.addComponente("Monto Inicial:",
		 * lblMontoInicial); grpPlanFinanciamiento.addComponente("%Desc:",
		 * lblPorcDescuento);
		 * grpPlanFinanciamiento.addComponente("Monto Descuento:",
		 * lblMontoDescuento); grpPlanFinanciamiento.addComponente("Tipo:",
		 * lblTipo); grpPlanFinanciamiento.addComponente("Monto Deudor:",
		 * lblMontoDeudor);
		 * grpPlanFinanciamiento.addComponente("Distribucion del Pago"
		 * ,btnGeneracionDeCotas);
		 */
		getGbPie().addComponenteMultiples(300, new Label("Monto Total:"),
				lblMontoContrato);
		grpPlanFinanciamiento.dibujar(getGbPie());

		/*
		 * grpCuotas.addComponente("",ventanaPlanFinanciamiento);
		 * grpCuotas.dibujar(getGbPie()); grpCuotas.setVisible(false);
		 */
		chkGeneral.setChecked(true);
		chkGeneral.setId("chkTodos");

		txtObservacion.setStyle("width: 850px");

		this.btnGeneracionDeCotas.setId("btnCalcular");
		dibujarEstructura();
		grpPlanFinanciamiento.setVisible(false);
		grpPagador.setVisible(false);

	}

	public void inicializarGridFinanciamiento(FrecuenciaDePago freciencia) {
	}

	public void mostrarVentanaInfoFinanciamiento(double monto,
			DefinicionDeCuotas def) throws InterruptedException {
		this.ventanaPlanFinanciamiento.dibujarFilasFinanciamiento(monto, def);
		/*
		 * this.ventanaPlanFinanciamiento.setParent(this);
		 * this.ventanaPlanFinanciamiento.doOverlapped();
		 * app.agregar(this.ventanaPlanFinanciamiento);
		 */
		this.grpCuotas.setVisible(true);
	}

	public void cargarValores(ContratoMecanizado arg0) throws ExcDatosInvalidos {
		if (getModelo().getTipoContrato() != null)
			lblNrocontrol.setValue("" + getModelo().getSerie());

		if (getModelo().getEstado() != null)
			lblEstado.setValue(getModelo().getEstado().getDescripcion());
		txtFecha.setValue(getModelo().getFecha());
		txtFechaDesde.setValue(getModelo().getFechaDesde());
		getBinder().addBinding(txtFechaDesde, "value",
				getNombreModelo() + ".fechaDesde", null, null, "save", null,
				null, null, null);

		txtDiasVigencia.setValue(getModelo().getDiasVigencia());
		getBinder().addBinding(txtDiasVigencia, "value",
				getNombreModelo() + ".diasVigencia", null, null, "save", null,
				null, null, null);

		/*
		 * planFinanciamiento.setSeleccion(getModelo().getPlanFinanciamiento());
		 * getBinder().addBinding(planFinanciamiento, "seleccion",
		 * getNombreModelo()+".planFinanciamiento", null, null, "save", null,
		 * null, null, null);
		 */
		if (getModelo().getMonto() != null)
			lblMontoContrato.setValue("" + getModelo().getMonto());
		getBinder().addBinding(lblMontoContrato, "value",
				getNombreModelo() + ".monto", null, null, "save", null, null,
				null, null);

		ContContratoMecanizado controlador = (ContContratoMecanizado) getControlador();
		if (getModelo().getTipoContrato() != null) {
			tipoContrato.setValue("" + getModelo().getTipoContrato());
			controlador.actualizarTipoContrato(getModelo().getTipoContrato());
		}
		getBinder().addBinding(tipoContrato, "seleccion",
				getNombreModelo() + ".tipoContrato", null, null, "save", null,
				null, null, null);
		if (getModelo().getSolicitud() != null) {
			solicitud.setSeleccion(getModelo().getSolicitud());
		}
		getBinder().addBinding(solicitud, "seleccion",
				getNombreModelo() + ".solicitud", null, null, "save", null,
				null, null, null);
		getBinder().addBinding(unidadProduccion, "seleccion",
				getNombreModelo() + ".unidadProductiva", null, null, "save",
				null, null, null, null);
		if (getModelo().getUnidadProductiva() != null) {
			try {
				controlador.actualizarProductor(getModelo()
						.getUnidadProductiva().getProductor());
				unidadProduccion
						.setSeleccion(getModelo().getUnidadProductiva());
			} catch (ExcFiltroExcepcion e) {
				e.printStackTrace();
			}
		}
		if (getModelo().getPagador() != null) {
			cedulaPagador.setSeleccion(getModelo().getPagador());
			getLblNombrePagador().setValue(
					getModelo().getPagador().getNombres());
			lblDireccionPagador.setValue(getModelo().getPagador()
					.getDireccion());
		}
		getBinder().addBinding(cedulaPagador, "seleccion",
				getNombreModelo() + ".pagador", null, null, "save", null, null,
				null, null);
		if (getModelo().getPlanFinanciamiento() != null) {
			planFinanciamiento
					.setSeleccion(getModelo().getPlanFinanciamiento());
			controlador.actualizarFinanciamiento();
		}
		getBinder().addBinding(planFinanciamiento, "seleccion",
				getNombreModelo() + ".planFinanciamiento", null, null, "save",
				null, null, null, null);
	}

	private List<CompEncabezado> getCatalogoSolicitud() {
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

		return encabezado;
	}

	public List<CompEncabezado> getEncabezadoSolicitud() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Fecha", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getFechaInicioPlanString");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nro", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNumeroControl");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Solicitante", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreRazonSocial");
		encabezado.add(titulo);

		return encabezado;

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

	/*
	 * private List<CompEncabezado> getEncabezadoPlanFinanciamiento(){
	 * List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
	 * CompEncabezado titulo;
	 * 
	 * titulo = new CompEncabezado("Plan",120);
	 * titulo.setMetodoBinder("getDescripcion"); encabezado.add(titulo);
	 * 
	 * 
	 * titulo = new CompEncabezado("Objeto",80);
	 * titulo.setMetodoBinder("getObjetoVenta"); titulo.setOrdenable(true);
	 * encabezado.add(titulo);
	 * 
	 * titulo = new CompEncabezado("Tipo",160);
	 * titulo.setMetodoBinder("getTipoFinanciamiento");
	 * titulo.setOrdenable(true); encabezado.add(titulo);
	 * 
	 * titulo = new CompEncabezado("%Inicial",30);
	 * titulo.setMetodoBinder("getPorcentajeInicial");
	 * titulo.setOrdenable(true); encabezado.add(titulo);
	 * 
	 * titulo = new CompEncabezado("%Descuento",30);
	 * titulo.setMetodoBinder("getPorcentajeDescuento");
	 * titulo.setOrdenable(true); encabezado.add(titulo);
	 * 
	 * return encabezado; }
	 */

	private List<CompEncabezado> getEncabezadoArticulo() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Serv/Art", 100);
		titulo.setMetodoBinder("getStrTipoProducto");
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

	private List<CompEncabezado> encabezadosDetalle() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Servicio/Articulo", 340);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getProducto");
		titulo.setMetodoModelo(".producto");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cantidad", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidad");
		titulo.setMetodoModelo(".cantidad");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Precio Und", 80);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getPrecioUnidad");
		titulo.setMetodoModelo(".precioUnidad");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);
		titulo = new CompEncabezado("Precio", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getSubtotal");
		titulo.setMetodoModelo(".subtotal");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cantidad Real", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidadReal");
		titulo.setMetodoModelo(".cantidadReal");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	private ArrayList<Component> cargarLista() {

		ArrayList<Component> lista = new ArrayList<Component>();
		CompBuscar<IProducto> servicio = new CompBuscar<IProducto>(
				getEncabezadoArticulo(), 3);

		servicio.setModelo(productos);
		Doublebox cantidad = new Doublebox();
		Doublebox precioUnidad = new Doublebox();
		Doublebox precio = new Doublebox();
		Doublebox cantidadReal = new Doublebox();
		servicio.setWidth("340px");
		cantidad.setWidth("60px");
		precioUnidad.setWidth("80px");
		precio.setWidth("90px");
		servicio.setAncho(650);
		// para blokear cantidad y servicio

		cantidad.setDisabled(true);
		precio.setDisabled(true);
		precioUnidad.setDisabled(true);
		cantidadReal.setDisabled(true);
		cantidad.addEventListener(Events.ON_CHANGE, getControlador());
		servicio.setListenerEncontrar(getControlador());

		lista.add(servicio);
		lista.add(cantidad);
		lista.add(precioUnidad);
		lista.add(precio);
		lista.add(cantidadReal);

		return lista;
	}

	public void setChekItems(boolean valor) {
		for (Object iter : gridServicios.getRows().getChildren()) {
			if (iter instanceof Row) {
				Row row = (Row) iter;
				for (Object obj : row.getChildren()) {
					if (obj instanceof Checkbox)
						((Checkbox) obj).setChecked(valor);
				}
			}
		}
	}

	public void actualizarMontoContrato() {
		double montoContrato = 0;
		for (Object iter : gridServicios.getRows().getChildren()) {
			if (iter instanceof Row) {
				Row row = (Row) iter;
				boolean suma = false;
				for (Object obj : row.getChildren()) {
					if (obj instanceof Checkbox) {
						suma = ((Checkbox) obj).isChecked();
					}
					if (obj instanceof Label && suma)
						if (((Label) obj).getId().toString()
								.contains("lblSubTotal")) {
							Label lblSubTotal = ((Label) obj);
							montoContrato += Double.parseDouble(lblSubTotal
									.getValue());
						}
				}
			}
		}
		this.lblMontoContrato.setValue("" + montoContrato);
	}

	public void dibujarFinanciamiento() {
		double montoContrato = Double.parseDouble(this.getLblMontoContrato()
				.getValue());
		double porcDescuento = Double.parseDouble(this.getLblPorcDescuento()
				.getValue());
		double montoDescuento = (montoContrato * (porcDescuento / 100));
		double porcInicial = Double.parseDouble(this.getLblPorcInicial()
				.getValue());
		double montoInicial = (montoContrato * (porcInicial / 100));
		double montoDeudor = (montoContrato - montoDescuento) - montoInicial;
		this.getLblMontoDescuento().setValue("" + montoDescuento);
		this.getLblMontoInicial().setValue("" + montoInicial);
		this.getLblMontoDeudor().setValue("" + montoDeudor);
	}

	/*
	 * private void cargarDetallesContrato(){
	 * 
	 * getGridServicios().getRows().getChildren().clear(); Row row = null; for
	 * (DetalleContrato detalle : getModelo().getDetallesContrato()) { Checkbox
	 * chk = new Checkbox(); chk.setChecked(true); chk.setDisabled(true); row =
	 * new Row(); row.appendChild(chk); row.appendChild(new
	 * Label(detalle.getProducto().getDescripcion())); row.appendChild(new
	 * Label(""+detalle.getPrecioUnidad())); row.appendChild(new
	 * Label(""+detalle.getUnidadCobro().getAbreviatura())); row.appendChild(new
	 * Label(""+detalle.getCantidad())); Label lblSubtotal= new
	 * Label(""+detalle.getSubTotal()); row.appendChild(lblSubtotal);
	 * //lblSubtotal.setTooltiptext(detalle.getServicio().getFormula());
	 * getGridServicios().getRows().appendChild(row); }
	 * 
	 * this.cmbSolicitud.setValue(getModelo().getSolicitud().getNroControl());
	 * this
	 * .lblDatosSolicitud.setValue(getModelo().getSolicitud().getNroControl()
	 * +" "+getModelo().getSolicitud().getTipoSolicitud());
	 * this.lblNombreSolicitante
	 * .setValue(getModelo().getSolicitud().getProductor().getIdentidadLegal());
	 * /
	 * /this.lblDireccionSolicitante.setValue(getModelo().getDireccion().toString
	 * ());
	 * 
	 * this.cmbPagador.setValue(getModelo().getPagador().getIdentidadLegal());
	 * this.lblNombrePagador.setValue(getModelo().getPagador().getNombres());
	 * //this
	 * .lblDireccionPagador.setValue(getModelo().getPagador().getDireccion(
	 * ).get(0).getSector());
	 * 
	 * this.cmbPlanFinanciamiento.setValue(getModelo().getPlanFinanciamiento().
	 * getDescripcion());
	 * this.lblObjetoPlan.setValue(cmbPlanFinanciamiento.getSeleccion
	 * ().getObjetoVenta());
	 * this.lblTipo.setValue(cmbPlanFinanciamiento.getSeleccion
	 * ().getTipoFinanciamiento().getDescripcion().toUpperCase());
	 * this.lblPorcInicial
	 * .setValue(""+cmbPlanFinanciamiento.getSeleccion().getPorcentajeInicial
	 * ());
	 * this.lblPorcDescuento.setValue(""+cmbPlanFinanciamiento.getSeleccion(
	 * ).getPorcentajeDescuento());
	 * 
	 * dibujarFinanciamiento();
	 * 
	 * }
	 * 
	 * public void actualizarModeloDetalleContrato(ContratoMecanizado contrato){
	 * List<DetalleContrato> detalles = new ArrayList<DetalleContrato>();
	 * for(Object iter : gridServicios.getRows().getChildren()){ if (iter
	 * instanceof Row){ Row row = (Row) iter;
	 * 
	 * DetalleContrato detalle = new DetalleContrato();
	 * detalle.setContrato(contrato); detalle.setProducto((IProducto)
	 * row.getAttribute("servicio")); detalle.setUnidadCobro((UnidadMedida)
	 * row.getAttribute("unidad")); boolean seleccionado = false; for(Object
	 * elemento : row.getChildren()){ if (elemento instanceof Checkbox){
	 * Checkbox chk = ((Checkbox) elemento); seleccionado = chk.isChecked();
	 * 
	 * }
	 * 
	 * if (elemento instanceof Label){ Label lbl = ((Label) elemento);
	 * 
	 * if (lbl.getId().contains("lblCantidad"))
	 * detalle.setCantidad(Double.parseDouble(lbl.getValue())); if
	 * (lbl.getId().contains("lblPrecio"))
	 * detalle.setPrecioUnidad(Double.parseDouble(lbl.getValue())); if
	 * (lbl.getId().contains("lblSubTotal"))
	 * detalle.setSubtotal(Double.parseDouble(lbl.getValue())); }
	 * 
	 * } if (seleccionado) detalles.add(detalle); } }
	 * contrato.setDetallesContrato(detalles);
	 * contrato.setCuotasAPagarCliente(ventanaPlanFinanciamiento
	 * .getCuotasAPagarCliente()); for (CuotasAPagarCliente capc :
	 * contrato.getCuotasAPagarCliente()) { capc.setContrato(contrato);
	 * capc.setPagador(contrato.getPagador()); }
	 * 
	 * }
	 */

	public void setModoVer() {
		this.txtDiasVigencia.setDisabled(true);
		this.txtFecha.setDisabled(true);
		this.txtFechaDesde.setDisabled(true);
		this.txtTotal.setDisabled(true);
		this.cedulaPagador.setDisabled(true);
		this.planFinanciamiento.setDisabled(true);
		this.solicitud.setDisabled(true);
		this.tipoContrato.setDisabled(true);
		this.cedulaProductor.setDisabled(true);
		this.unidadProduccion.setDisabled(true);
		this.btnGeneracionDeCotas.setLabel("VER CUOTAS DE PAGO");
		this.btnGeneracionDeCotas.setId("btnVer");
		desactivarDetalle();
	}

	public void mostrarCuotasAPagarDelCliente(List<CuotasAPagarCliente> lista) {
		this.ventanaPlanFinanciamiento.mostrarCuotasAPagarDelCliente(lista);
	}

	public CompBuscar<SolicitudMecanizado> getSolicitud() {
		return solicitud;
	}

	public CompBuscar<Cliente> getCedulaPagador() {
		return cedulaPagador;
	}

	public Label getLblNrocontrol() {
		return lblNrocontrol;
	}

	public Label getLblEstado() {
		return lblEstado;
	}

	public Label getLblDetalleSolicitud() {
		return lblDetalleSolicitud;
	}

	public Label getLblNombreSolicitante() {
		return lblNombreSolicitante;
	}

	public Label getLblDireccionPagador() {
		return lblDireccionPagador;
	}

	public Label getLblNombrePagador() {
		return lblNombrePagador;
	}

	public CompCombobox<UnidadProductiva> getUnidadProduccion() {
		return unidadProduccion;
	}

	public Label getLblCedulaRif() {
		return lblCedulaRif;
	}

	public Label getLblSede() {
		return lblSede;
	}

	public Datebox getTxtFecha() {
		return txtFecha;
	}

	public Datebox getTxtFechaDesde() {
		return txtFechaDesde;
	}

	public void setTxtFechaDesde(Datebox txtFechaDesde) {
		this.txtFechaDesde = txtFechaDesde;
	}

	public Intbox getTxtDiasVigencia() {
		return txtDiasVigencia;
	}

	public void setTxtDiasVigencia(Intbox txtDiasVigencia) {
		this.txtDiasVigencia = txtDiasVigencia;
	}

	public Doublebox getTxtTotal() {
		return txtTotal;
	}

	public void setTxtTotal(Doublebox txtTotal) {
		this.txtTotal = txtTotal;
	}

	public Textarea getTxtObservacion() {
		return txtObservacion;
	}

	public void setTxtObservacion(Textarea txtObservacion) {
		this.txtObservacion = txtObservacion;
	}

	public Grid getGridServicios() {
		return gridServicios;
	}

	public void setGridServicios(Grid gridServicios) {
		this.gridServicios = gridServicios;
	}

	public void limpiarGrid() {
		gridServicios.getRows().getChildren().clear();
	}

	public List<PlanFinanciamiento> getPlanesFinanciamiento() {
		return planesFinanciamiento;
	}

	public void setPlanesFinanciamiento(
			List<PlanFinanciamiento> planesFinanciamiento) {
		this.planesFinanciamiento = planesFinanciamiento;
	}

	public Label getLblObjetoPlan() {
		return lblObjetoPlan;
	}

	public void setLblObjetoPlan(Label lblObjetoPlan) {
		this.lblObjetoPlan = lblObjetoPlan;
	}

	public Label getLblTipo() {
		return lblTipo;
	}

	public void setLblTipo(Label lblTipo) {
		this.lblTipo = lblTipo;
	}

	public Label getLblPorcDescuento() {
		return lblPorcDescuento;
	}

	public void setLblPorcDescuento(Label lblPorcDescuento) {
		this.lblPorcDescuento = lblPorcDescuento;
	}

	public Label getLblPorcInicial() {
		return lblPorcInicial;
	}

	public void setLblPorcInicial(Label lblPorcInicial) {
		this.lblPorcInicial = lblPorcInicial;
	}

	public Label getLblMontoDescuento() {
		return lblMontoDescuento;
	}

	public void setLblMontoDescuento(Label lblMontoDescuento) {
		this.lblMontoDescuento = lblMontoDescuento;
	}

	public Label getLblMontoInicial() {
		return lblMontoInicial;
	}

	public void setLblMontoInicial(Label lblMontoInicial) {
		this.lblMontoInicial = lblMontoInicial;
	}

	public Label getLblMontoDeudor() {
		return lblMontoDeudor;
	}

	public void setLblMontoDeudor(Label lblMontoDeudor) {
		this.lblMontoDeudor = lblMontoDeudor;
	}

	/*
	 * public CompBuscar<PlanFinanciamiento> getPlanFinanciamiento() { return
	 * planFinanciamiento; }
	 * 
	 * public void setCmbPlanFinanciamiento( CompBuscar<PlanFinanciamiento>
	 * cmbPlanFinanciamiento) { this.planFinanciamiento = cmbPlanFinanciamiento;
	 * }
	 */

	public CompGrupoDatos getGrpPlanFinanciamiento() {
		return grpPlanFinanciamiento;
	}

	public Grid getGridFinanciamientoContrato() {
		return gridFinanciamientoContrato;
	}

	public void setGridFinanciamientoContrato(Grid gridFinanciamientoContrato) {
		this.gridFinanciamientoContrato = gridFinanciamientoContrato;
	}

	public Label getLblMontoContrato() {
		return lblMontoContrato;
	}

	public void setLblMontoContrato(Label lblMontoContrato) {
		this.lblMontoContrato = lblMontoContrato;
	}

	public Checkbox getChkGeneral() {
		return chkGeneral;
	}

	public void setChkGeneral(Checkbox chkGeneral) {
		this.chkGeneral = chkGeneral;
	}

	public Button getBtnVerDistribucionPago() {
		return btnGeneracionDeCotas;
	}

	public void setBtnVerDistribucionPago(Button btnVerDistribucionPago) {
		this.btnGeneracionDeCotas = btnVerDistribucionPago;
	}

	public Label getLblDatosSolicitud() {
		return lblDatosSolicitud;
	}

	public void setLblDatosSolicitud(Label lblDatosSolicitud) {
		this.lblDatosSolicitud = lblDatosSolicitud;
	}

	public CompCombobox<TipoContrato> getCmbTipoContrato() {
		return tipoContrato;
	}

	public void setCmbTipoContrato(CompCombobox<TipoContrato> cmbTipoContrato) {
		this.tipoContrato = cmbTipoContrato;
	}

	public Button getBtnGeneracionDeCotas() {
		return btnGeneracionDeCotas;
	}

	public void setBtnGeneracionDeCotas(Button btnGeneracionDeCotas) {
		this.btnGeneracionDeCotas = btnGeneracionDeCotas;
	}

	public List<CuotasAPagarCliente> getCuotasAPagarCliente() {
		return ventanaPlanFinanciamiento.getCuotasAPagarCliente();
	}

	public CompBuscar<Cliente> getCedulaProductor() {
		return cedulaProductor;
	}

	public CompCombobox<TipoContrato> getTipoContrato() {
		return tipoContrato;
	}

	public CompCombobox<PlanFinanciamiento> getPlanFinanciamiento() {
		return planFinanciamiento;
	}

	public CompGrupoDatos getGrpPagador() {
		return grpPagador;
	}

}