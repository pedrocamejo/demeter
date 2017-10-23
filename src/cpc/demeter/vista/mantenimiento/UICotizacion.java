package cpc.demeter.vista.mantenimiento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.components.barbecue.BarcodeProviders.NW7Provider;

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
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.controlador.mantenimiento.ContContratoServicioTecnico;
import cpc.demeter.controlador.mantenimiento.ContCotizacion;
import cpc.demeter.controlador.mantenimiento.ContSolicitudServicioTecnico;
import cpc.demeter.vista.UIInfoPlanFinanciamiento;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.demeter.administrativo.ContratoServicioTecnico;
import cpc.modelo.demeter.administrativo.Cotizacion;
import cpc.modelo.demeter.administrativo.CuotasAPagarCliente;
import cpc.modelo.demeter.administrativo.DefinicionDeCuotas;
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.administrativo.FrecuenciaDePago;
import cpc.modelo.demeter.administrativo.Impuesto;
import cpc.modelo.demeter.administrativo.PlanFinanciamiento;
import cpc.modelo.demeter.administrativo.TipoContrato;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.demeter.mantenimiento.Consumible;
import cpc.modelo.demeter.mantenimiento.ConsumibleEquivalente;
import cpc.modelo.demeter.mantenimiento.DetalleSalidaExternaArticulo;
import cpc.modelo.demeter.mantenimiento.Herramienta;
import cpc.modelo.demeter.mantenimiento.Repuesto;
import cpc.modelo.demeter.mantenimiento.SalidaExternaArticulo;
import cpc.modelo.demeter.mantenimiento.SolicitudServicioTecnico;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.modelo.ministerio.gestion.UnidadProductiva;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.negocio.demeter.administrativo.NegocioCotizacion;
import cpc.negocio.demeter.mantenimiento.NegocioSalidaExternaArticulo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.ui.ComponentesAutomaticos;
import cpc.zk.componente.ventanas.CompGrupoBusqueda;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class UICotizacion extends
		CompVentanaMaestroDetalle<Cotizacion, DetalleSalidaExternaArticulo> {

	public UICotizacion(String titulo, int ancho, int modo,
			List<ArticuloVenta> articuloVentas, List<Almacen> almacenes,
			List<DetalleContrato> detallesContrato,
			List<ClienteAdministrativo> clientes,
			SalidaExternaArticulo salidaExternaArticulo, Cotizacion cotizacion) {
		super(titulo, ancho);

		this.articuloVentas = articuloVentas;
		this.almacenes = almacenes;
		this.detallesContrato = detallesContrato;
		this.clientes = clientes;
		this.salidaExternaArticulo = salidaExternaArticulo;
		this.cotizacion = cotizacion;
		this.accion = modo;
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3028544517928948872L;
	private List<ArticuloVenta> articuloVentas;
	private List<Almacen> almacenes;
	private List<DetalleContrato> detallesContrato;
	private List<ClienteAdministrativo> clientes;
	private SalidaExternaArticulo salidaExternaArticulo;
	private Cotizacion cotizacion;
	private int accion;

	private NegocioSalidaExternaArticulo servicioaArticulo = NegocioSalidaExternaArticulo
			.getInstance();
	private NegocioCotizacion servicioCotizacion = NegocioCotizacion
			.getInstance();

	private CompGrupoDatos cgdContrato;
	private Label LblCedulaRifPagado;
	private Label lblDireccionPagador;
	private Label lblFecha;
	private Label lblFechaHasta;
	private Label lblDiasVigencia;
	private Label lblNumeroSalida;
	private Label lblNumeroContrato;
	private CompBuscar<ClienteAdministrativo> cmbClientes;

	private CompGrupoDatos cgdTotales;
	private Doublebox base;
	private Doublebox impuesto;
	private Doublebox total;

	private CompLista<DetalleContrato> detalleContrato;

	private CompBuscar<ArticuloVenta> cmbArticuloventa;

	private CompGrupoBusqueda<Consumible> auxConsumible;
	private CompGrupoBusqueda<Herramienta> auxHerramienta;
	private CompGrupoBusqueda<Repuesto> auxRepuesto;

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		cgdContrato = new CompGrupoDatos("Cotizacion", 2);
		LblCedulaRifPagado = new Label();
		lblDireccionPagador = new Label();
		lblDireccionPagador.setWidth("650px");
		lblDireccionPagador.setMultiline(true);
		lblFecha = new Label();
		;

		lblFechaHasta = new Label();
		lblDiasVigencia = new Label();
		lblNumeroSalida = new Label();
		lblNumeroContrato = new Label();
		cmbClientes = new CompBuscar<ClienteAdministrativo>(
				getEncabezadoCliente(), 1);
		cmbClientes.setModelo(clientes);
		cmbClientes.setAncho(650);

		cgdTotales = new CompGrupoDatos("Totales", 2);

		base = new Doublebox();
		impuesto = new Doublebox();
		total = new Doublebox();
		base.setReadonly(true);
		impuesto.setReadonly(true);
		;
		total.setReadonly(true);

		try {
			auxConsumible = new CompGrupoBusqueda<Consumible>("Sector",
					getEncabezadoArticulo(), null,
					ComponentesAutomaticos.TEXTBOX);
			auxConsumible.setNuevo(new Consumible());
			auxConsumible.setModeloCatalogo(null);
			auxConsumible.setAnchoValores(120);
			auxConsumible.setAnchoCatalogo(800);
			auxConsumible.addListener(getControlador());
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			auxHerramienta = new CompGrupoBusqueda<Herramienta>("Sector",
					getEncabezadoArticulo(), null,
					ComponentesAutomaticos.TEXTBOX);
			auxHerramienta.setNuevo(new Herramienta());
			auxHerramienta.setModeloCatalogo(null);
			auxHerramienta.setAnchoValores(120);
			auxHerramienta.setAnchoCatalogo(800);
			auxHerramienta.addListener(getControlador());
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			auxRepuesto = new CompGrupoBusqueda<Repuesto>("Sector",
					getEncabezadoArticulo(), null,
					ComponentesAutomaticos.TEXTBOX);
			auxRepuesto.setNuevo(new Repuesto());
			auxRepuesto.setModeloCatalogo(null);
			auxRepuesto.setAnchoValores(120);
			auxRepuesto.setAnchoCatalogo(800);
			auxRepuesto.addListener(getControlador());
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		detalleContrato = new CompLista(encabezadosDetalleContrato());
		if (accion == Accion.AGREGAR) {
			setDetalles(cargarLista(), null, encabezadosPrimario());
			getDetalle().setListenerBorrar(getControlador());
			getDetalle().setNuevo(new DetalleSalidaExternaArticulo());
			getGbDetalle().appendChild(detalleContrato);
		} else {
			setDetalles(cargarLista(), cotizacion.getSalidaExternaArticulo()
					.getDetalleSalidaExternaArticulos(), encabezadosPrimario());
			getDetalle().setListenerBorrar(getControlador());
			getDetalle().setNuevo(new DetalleSalidaExternaArticulo());
			getGbDetalle().appendChild(detalleContrato);
		}

	}

	@Override
	public void dibujar() {

		cgdContrato.addComponente("Cliente", cmbClientes);
		cgdContrato.addComponente("Cedula/Rif", LblCedulaRifPagado);
		cgdContrato.addComponente("Direccion", lblDireccionPagador);
		cgdContrato.addComponente("valida hasta", lblFechaHasta);

		cgdContrato.addComponente("emitida el ", lblFecha);
		cgdContrato.addComponente("Numero Salida", lblNumeroSalida);
		cgdContrato.addComponente("Numero Cotizacion", lblNumeroContrato);

		cgdTotales.addComponente("base", base);
		cgdTotales.addComponente("impuesto", impuesto);
		cgdTotales.addComponente("total", total);

		cgdContrato.dibujar(this);
		cgdTotales.dibujar(this);
		cmbClientes.setListenerEncontrar(getControlador());
		dibujarEstructura();
		addBotonera();
	}

	@Override
	public void cargarValores(Cotizacion dato) throws ExcDatosInvalidos {
		if (accion == Accion.AGREGAR) {
		} else {
			cmbClientes.setSeleccion(cotizacion.getPagador()
					.getClienteAdministrativo());
			LblCedulaRifPagado.setValue(dato.getPagador().getIdentidadLegal());
			lblDireccionPagador.setValue(cotizacion.getPagador()
					.getClienteAdministrativo().getCliente().getDireccion());
			lblFecha.setValue(cotizacion.getFechaDesdeString());
			// java.text.DateFormat sdf = new
			// java.text.SimpleDateFormat("dd/MM/yyyy");
			// sdf.format(cotizacion.getFechaDesde());
			lblFechaHasta.setValue(cotizacion.getFechaHastaString());
			lblDiasVigencia.setValue(cotizacion.getDiasVigencia().toString());
			lblNumeroSalida.setValue(cotizacion.getSalidaExternaArticulo()
					.getStrNroDocumento());
			lblNumeroContrato.setValue(cotizacion.getStrNroDocumento());
			total.setValue(cotizacion.getTotal());
			base.setValue(cotizacion.getMonto());
			impuesto.setValue(cotizacion.getTotal() - cotizacion.getMonto());
			// cmbClientes.setSeleccion(cotizacion.getPagador());
			if (accion != Accion.AGREGAR) {
				detalleContrato.setModelo(cotizacion.getDetallesContrato());
				getDetalle().detach();
			}

		}
	}

	private List<CompEncabezado> getEncabezadoArticulo() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("CodigoFabricante", 150);
		titulo.setMetodoBinder("getCodigoFabricante");
		titulo.setOrdenable(true);
		lista.add(titulo);

		titulo = new CompEncabezado("CodigoSIGESP", 150);
		titulo.setMetodoBinder("getCodigoSIGESP");
		titulo.setOrdenable(true);
		lista.add(titulo);

		titulo = new CompEncabezado("DenominacionFabricante()", 150);
		titulo.setMetodoBinder("getDenominacionFabricante");
		titulo.setOrdenable(true);
		lista.add(titulo);

		titulo = new CompEncabezado("CodigoCCNU", 150);
		titulo.setMetodoBinder("getCodigoCCNU");
		titulo.setOrdenable(true);
		lista.add(titulo);

		return lista;
	}

	private List<CompEncabezado> encabezadosPrimario() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Articulo ", 200);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getArticuloVenta");
		titulo.setMetodoModelo(".articuloVenta");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cantidad", 160);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidad");
		titulo.setMetodoModelo(".cantidad");
		titulo.setOrdenable(true);
		// titulo.setMetodoModelo(".consumibleEquivalente");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Almacen O", 200);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getAlmacenOrigen");
		titulo.setMetodoModelo(".almacenOrigen");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Observacion", 160);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getObservacion");
		titulo.setMetodoModelo(".observacion");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> encabezadosDetalleContrato() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Servicio/Articulo", 360);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getProducto");
		titulo.setMetodoModelo(".producto");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cantidad", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidad");
		titulo.setMetodoModelo(".cantidad");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Precio Und", 90);
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

		titulo = new CompEncabezado("Cantidad Real", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidadReal");
		titulo.setMetodoModelo(".cantidadReal");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> getEncabezadoCliente() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Cedula", 120);
		titulo.setMetodoBinder("getCedulaCliente");

		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre", 450);
		titulo.setMetodoBinder("getNombreCliente");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	private ArrayList<Component> cargarLista() {
		ArrayList<Component> lista = new ArrayList<Component>();
		cmbArticuloventa = new CompBuscar<ArticuloVenta>(getEncabezadoArticulo(), 2);

		Doublebox cantidad = new Doublebox();
		CompBuscar<Almacen> almacenOrigen = new CompBuscar<Almacen>(getEncabezadoAlmacen(), 0);

		Textbox observacion = new Textbox();
		// Label codccnu = new Label();

		cmbArticuloventa.setWidth("160px");

		almacenOrigen.setWidth("180px");

		cantidad.setWidth("180px");
		observacion.setWidth("160px");
		// codccnu.setWidth("150px");
		// observaciones.setRows(2);

		/*
		 * denomiacionFabricanteeq.setMaxlength(250); codccnu.setMaxlength(250);
		 * codSigesp.setMaxlength(250); codFabricante.setMaxlength(250);
		 */
		cmbArticuloventa.setAncho(600);

		almacenOrigen.setAncho(600);

		// codigoActivo.setModelo(activos);
		// almacen.setModelo(almacenes);
		/*
		 * List<ConsumibleEquivalente> z = consumibleEquivalentes; Consumible []
		 * consumibles = new Consumible[z.size()]; int a = 0; for
		 * (ConsumibleEquivalente consumibleEquivalente : z) { consumibles[a]=
		 * (consumibleEquivalente.getConsumibleEquivalente()); a++; }
		 */

		// consumibleEq.setAttribute("Consumible", "consumible");

		// estadoActivo.setModelo(estadosActivo);

		try {
			cmbArticuloventa.setModelo(servicioaArticulo.getArticulosVentas());
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		almacenOrigen.setModelo(almacenes);

		cmbArticuloventa.setListenerEncontrar(getControlador());

		almacenOrigen.setListenerEncontrar(getControlador());
		cantidad.addEventListener(Events.ON_CHANGE, getControlador());
		lista.add(cmbArticuloventa);
		lista.add(cantidad);
		// lista.add(codccnu);
		lista.add(almacenOrigen);

		lista.add(observacion);
		/*
		 * for (Component component : lista) { component.getChildren(); if
		 * (component.getChildren().get(0) ==null){ lista.remove(component); } }
		 */

		return lista;
	}

	private List<CompEncabezado> getEncabezadoAlmacen() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Nombre. ", 100);
		titulo.setMetodoBinder("getNombre");
		lista.add(titulo);

		titulo = new CompEncabezado("Descripción. ", 100);
		titulo.setMetodoBinder("getDescripcion");
		lista.add(titulo);

		titulo = new CompEncabezado("Localidad. ", 250);
		titulo.setMetodoBinder("getLocalidad");
		lista.add(titulo);

		return lista;
	}

	public CompGrupoBusqueda<Consumible> getAuxConsumible() {
		return auxConsumible;
	}

	public void setAuxConsumible(CompGrupoBusqueda<Consumible> auxConsumible) {
		this.auxConsumible = auxConsumible;
	}

	public CompGrupoBusqueda<Herramienta> getAuxHerramienta() {
		return auxHerramienta;
	}

	public void setAuxHerramienta(CompGrupoBusqueda<Herramienta> auxHerramienta) {
		this.auxHerramienta = auxHerramienta;
	}

	public CompGrupoBusqueda<Repuesto> getAuxRepuesto() {
		return auxRepuesto;
	}

	public void setAuxRepuesto(CompGrupoBusqueda<Repuesto> auxRepuesto) {
		this.auxRepuesto = auxRepuesto;
	}

	public Doublebox getBase() {
		return base;
	}

	public void setBase(Doublebox base) {
		this.base = base;
	}

	public Doublebox getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(Doublebox impuesto) {
		this.impuesto = impuesto;
	}

	public Doublebox getTotal() {
		return total;
	}

	public void setTotal(Doublebox total) {
		this.total = total;
	}

	public CompBuscar<ClienteAdministrativo> getCmbClientes() {
		return cmbClientes;
	}

	public void setCmbClientes(CompBuscar<ClienteAdministrativo> cmbClientes) {
		this.cmbClientes = cmbClientes;
	}

	public Label getLblCedulaRifPagado() {
		return LblCedulaRifPagado;
	}

	public void setLblCedulaRifPagado(Label lblCedulaRifPagado) {
		LblCedulaRifPagado = lblCedulaRifPagado;
	}

	public Label getLblDireccionPagador() {
		return lblDireccionPagador;
	}

	public void setLblDireccionPagador(Label lblDireccionPagador) {
		this.lblDireccionPagador = lblDireccionPagador;
	}

	public Label getLblFecha() {
		return lblFecha;
	}

	public void setLblFecha(Label lblFecha) {
		this.lblFecha = lblFecha;
	}

	public Label getLblFechaHasta() {
		return lblFechaHasta;
	}

	public void setLblFechaHasta(Label lblFechaHasta) {
		this.lblFechaHasta = lblFechaHasta;
	}

	public CompLista<DetalleContrato> getDetalleContrato() {
		return detalleContrato;
	}

	public void setDetalleContrato(CompLista<DetalleContrato> detalleContrato) {
		this.detalleContrato = detalleContrato;
	}

}