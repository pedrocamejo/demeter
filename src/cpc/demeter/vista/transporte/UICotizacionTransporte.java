package cpc.demeter.vista.transporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.demeter.administrativo.CotizacionTransporte;
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.demeter.transporte.UbicacionTransporte;
import cpc.negocio.demeter.administrativo.NegocioCotizacion;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ui.ComponentesAutomaticos;
import cpc.zk.componente.ventanas.CompGrupoBusqueda;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;


public class UICotizacionTransporte extends
		CompVentanaMaestroDetalle<CotizacionTransporte, DetalleContrato> {

	public UICotizacionTransporte(String titulo, int ancho, int modo,
			List<IProducto> articuloVentas, 
			List<DetalleContrato> detallesContrato,
			List<ClienteAdministrativo> clientes,
			List<UbicacionTransporte> ubicacionTransportes,
			 CotizacionTransporte cotizacion) {
		super(titulo, ancho);

		this.articuloVentas = articuloVentas;
		this.detallesContrato = detallesContrato;
		this.clientes = clientes;
		this.cotizacion = cotizacion;
		this.accion = modo;
		this.ubicaciones=ubicacionTransportes;
	/*	this.municipios=municipios;
		this.parroquias=parroquias;
		this.sectores=sectores;*/
		// TODO Auto-generated constructor stub
		
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -3028544517928948872L;
	private List<IProducto> articuloVentas;
	
	private List<DetalleContrato> detallesContrato;
	private List<ClienteAdministrativo> clientes;

	private CompGrupoBusqueda<UbicacionTransporte> origen;
	private CompGrupoBusqueda<UbicacionTransporte> destino;
	
	private List<UbicacionTransporte> ubicaciones;
	
	
	
	private CotizacionTransporte cotizacion;

	
	private int accion;

	
	private NegocioCotizacion servicioCotizacion = NegocioCotizacion.getInstance();

	private CompGrupoDatos cgdContrato;
	private Label LblCedulaRifPagado;
	private Label lblDireccionPagador;
	private Datebox dtmFechaEmision;
	private Datebox dtmFechaInicio;
	private Label lblFechaHasta;
	private Intbox lblDiasVigencia;
	private Textbox observacion;

	private CompBuscar<ClienteAdministrativo> cmbClientes;

	private CompGrupoDatos cgdTotales;
	private Doublebox base;
	private Doublebox impuesto;
	private Doublebox total;

	private CompBuscar<IProducto> cmbArticuloventa;

	private CompGrupoDatos cgdGeneral;
	private Label			lblFecha;
	private Label			lblEstado;
	private Label 			lblNumeroContrato;
	private	 Textbox		txtMotivoRechazo;
	protected Button 		activar, rechazar;
	
	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		cgdContrato = new CompGrupoDatos("Cotizacion", 2);
		LblCedulaRifPagado = new Label();
		lblDireccionPagador = new Label();
		lblDireccionPagador.setWidth("650px");
		lblDireccionPagador.setMultiline(true);
		dtmFechaEmision = new Datebox(new Date());
		dtmFechaEmision.setDisabled(true);
		dtmFechaInicio = new Datebox();
		observacion = new Textbox();
		observacion.setMaxlength(255);
		observacion.setRows(3);
		
		
		dtmFechaInicio.addEventListener(Events.ON_CHANGE, getControlador());
			
		lblFechaHasta = new Label();
		lblDiasVigencia = new Intbox();
		lblDiasVigencia.addEventListener("onChange", getControlador());
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
			origen = new CompGrupoBusqueda<UbicacionTransporte>("Direccion Origen",
					getEncabezadoUbicaciones(), getModelo().getDireccionOrigen(),
					ComponentesAutomaticos.LABEL);
			origen.setNuevo(new UbicacionTransporte());
			origen.setModeloCatalogo(ubicaciones);
			origen.setAnchoValores(120);
			origen.setAnchoCatalogo(800);
			origen.addListener(getControlador());
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		try {
			destino = new CompGrupoBusqueda<UbicacionTransporte>("Direccion Llegada",
					getEncabezadoUbicaciones(), getModelo().getDireccionLlegada(),
					ComponentesAutomaticos.LABEL);
			destino.setNuevo(new UbicacionTransporte());
			destino.setModeloCatalogo(ubicaciones);
			destino.setAnchoValores(120);
			destino.setAnchoCatalogo(800);
			destino.addListener(getControlador());
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		cgdGeneral	= new CompGrupoDatos("GENERAL",2);
		lblFecha	= new Label();
		lblEstado	= new Label();
		lblNumeroContrato = new Label();
		txtMotivoRechazo = new Textbox();
		activar		= new Button("ACTIVAR");
		rechazar	= new Button("RECHAZAR");
		activar.setVisible(false);
		rechazar.setVisible(false);
		activar.addEventListener(Events.ON_CLICK, getControlador());
		rechazar.addEventListener(Events.ON_CLICK, getControlador());
		txtMotivoRechazo.setVisible(false);
		setDetalles(cargarLista(), getModelo().getDetallesContrato(),
				encabezadosDetalle());
		getDetalle().setNuevo(new DetalleContrato());
		getDetalle().setListenerBorrar(getControlador());

	}

	private List<CompEncabezado> getEncabezadoUbicaciones() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		
		
		titulo = new CompEncabezado("Municipio", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreMunicipio");
		titulo.setMetodoModelo(".ubicacionMunicipio");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Parroquia", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreParroquia");
		titulo.setMetodoModelo(".ubicacionParroquia");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Sector", 200);
		titulo.setMetodoBinder("getNombreSector");
		titulo.setMetodoModelo(".ubicacionSector");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Direccion", 150);
		titulo.setMetodoBinder("getDireccion");
		titulo.setOrdenable(true);
	
		encabezado.add(titulo);

		titulo = new CompEncabezado("Referencia", 150);
		titulo.setMetodoBinder("getReferencia");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		
		return encabezado;
	}

	@Override
	public void dibujar() {
	
		cgdContrato.addComponente("Cliente", cmbClientes);
		cgdContrato.addComponente("Cedula/Rif", LblCedulaRifPagado);
		cgdContrato.addComponente("Direccion", lblDireccionPagador);
		cgdContrato.addComponente("Fecha Emision", dtmFechaEmision);
		cgdContrato.addComponente("Fecha desde", dtmFechaInicio);
		cgdContrato.addComponente("fecha hasta", lblFechaHasta);
		cgdContrato.addComponente("Dias de Vigencia",lblDiasVigencia);
		cgdContrato.addComponente("Observacion",observacion);
//		cgdContrato.addComponente("Numero Salida", lblNumeroSalida);
		
		cgdTotales.addComponente("base", base);
		cgdTotales.addComponente("impuesto", impuesto);
		cgdTotales.addComponente("total", total);
		
		
		cgdGeneral.addComponente("Numero Cotizacion", lblNumeroContrato);
		cgdGeneral.addComponente("Fecha",lblFecha);
		cgdGeneral.addComponente("estado",lblEstado);
		cgdGeneral.addComponente("motivoRechazo",txtMotivoRechazo);
		cgdGeneral.addComponente(activar);
		cgdGeneral.addComponente(rechazar);

/*
		grupoOrigen.addComponente("Municipio", cmbMnunicipioOrigen); 
		grupoOrigen.addComponente("Parroquia", cmbParroquiaOrigen); 
		grupoOrigen.addComponente("Sector", cmbsectorOrigen); 
		grupoOrigen.addComponente("Direccion", direccionOrigen); 
		grupoOrigen.addComponente("Municipio", refereciaOrigen); 
		 
		grupoDestino.addComponente("Municipio", cmbMnunicipioDestino); 
		grupoDestino.addComponente("Parroquia", cmbParroquiaDestino); 
		grupoDestino.addComponente("Sector", cmbsectorDestino); 
		grupoDestino.addComponente("Direccion", direccionDestino); 
		grupoDestino.addComponente("Municipio", refereciaDestino); 
		
		*/
		
		
		
		
		
		cgdGeneral.dibujar(this);
		origen.dibujar();
		this.appendChild(origen);
		destino.dibujar();
		this.appendChild(destino);
		
		cgdContrato.dibujar(this);
		
		cgdTotales.dibujar(this);
		cmbClientes.setListenerEncontrar(getControlador());
		
		/*cmbMnunicipioOrigen.setListenerEncontrar(getControlador());
		cmbParroquiaOrigen.setListenerEncontrar(getControlador());
		cmbsectorOrigen.setListenerEncontrar(getControlador());


		cmbMnunicipioDestino.setListenerEncontrar(getControlador());
		cmbParroquiaDestino.setListenerEncontrar(getControlador());
		cmbsectorDestino.setListenerEncontrar(getControlador());
	*/
		
		
		
		dibujarEstructura();
		addBotonera();
	}

	@Override
	public void cargarValores(CotizacionTransporte dato) throws ExcDatosInvalidos {
		if (accion == Accion.AGREGAR) {
		} else {

			cmbClientes.setSeleccion(cotizacion.getPagador()
					.getClienteAdministrativo());
			cmbClientes.setText(cotizacion.getPagador()
					.getClienteAdministrativo().getNombreCliente()); 
					
			LblCedulaRifPagado.setValue(dato.getPagador().getIdentidadLegal());
			lblDireccionPagador.setValue(cotizacion.getPagador()
					.getClienteAdministrativo().getCliente().getDireccion());
			dtmFechaEmision.setValue(cotizacion.getFecha());
			dtmFechaInicio.setValue(cotizacion.getFechaDesde());
			observacion.setValue(cotizacion.getObservacion());
			// java.text.DateFormat sdf = new
			// java.text.SimpleDateFormat("dd/MM/yyyy");
			// sdf.format(cotizacion.getFechaDesde());
			lblFechaHasta.setValue(cotizacion.getFechaHastaString());
			lblDiasVigencia.setValue(cotizacion.getDiasVigencia());
			//lblNumeroSalida.setValue(cotizacion.getSalidaExternaArticulo().getStrNroDocumento());
			lblNumeroContrato.setValue(cotizacion.getStrNroDocumento());
			total.setValue(cotizacion.getTotal());
			base.setValue(cotizacion.getMonto());
			impuesto.setValue(cotizacion.getTotal() - cotizacion.getMonto());
		
			lblFecha.setValue(cotizacion.getFechaString());	
			lblEstado.setValue(cotizacion.getEstadoString());	
			if (cotizacion.getMotivoRechazo()!=null){
				txtMotivoRechazo.setVisible(true);
				txtMotivoRechazo.setValue(cotizacion.getMotivoRechazo());
			} 
			 
			
			
			System.out.println(getNombreModelo());
			
			getBinder().addBinding(origen, "modelo", getNombreModelo() + ".direccionOrigen",
					null, null, "save", null, null, null, null);
			getBinder().addBinding(destino, "modelo", getNombreModelo() + ".direccionLlegada",
					null, null, "save", null, null, null, null);
			if (dato.getDireccionOrigen() != null) {
				try {
					origen.actualizarValores();
					
				} catch (ExcAccesoInvalido e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			if (dato.getDireccionLlegada() != null) {
				try {
					destino.actualizarValores();
					
				} catch (ExcAccesoInvalido e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
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
		cmbArticuloventa  = new CompBuscar<IProducto>(
				getEncabezadoArticulo2(), 3);

		cmbArticuloventa.setModelo(articuloVentas);
		Doublebox cantidad = new Doublebox();
		Doublebox precioUnidad = new Doublebox();
		Doublebox precio = new Doublebox();
	//	Doublebox cantidadReal = new Doublebox();
		cmbArticuloventa.setWidth("340px");
		cantidad.setWidth("60px");
		precioUnidad.setWidth("80px");
		precio.setWidth("90px");
		cmbArticuloventa.setAncho(650);
		// para blokear cantidad y servicio

		cantidad.setDisabled(false);
		precio.setDisabled(true);
		precioUnidad.setDisabled(true);
	//	cantidadReal.setDisabled(true);
		cantidad.addEventListener(Events.ON_CHANGE, getControlador());
		precioUnidad.addEventListener(Events.ON_CHANGE, getControlador());
		cmbArticuloventa.setListenerEncontrar(getControlador());

		lista.add(cmbArticuloventa);
		lista.add(cantidad);
		lista.add(precioUnidad);
		lista.add(precio);
	//	lista.add(cantidadReal);

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

	

	public Label getLblFechaHasta() {
		return lblFechaHasta;
	}

	public void setLblFechaHasta(Label lblFechaHasta) {
		this.lblFechaHasta = lblFechaHasta;
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

		/*titulo = new CompEncabezado("Cantidad Real", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidadReal");
		titulo.setMetodoModelo(".cantidadReal");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);*/

		return encabezado;
	}

	public Intbox getLblDiasVigencia() {
		return lblDiasVigencia;
	}

	public void setLblDiasVigencia(Intbox lblDiasVigencia) {
		this.lblDiasVigencia = lblDiasVigencia;
	}

	public Datebox getDtmFechaEmision() {
		return dtmFechaEmision;
	}

	public void setDtmFechaEmision(Datebox dtmFechaEmision) {
		this.dtmFechaEmision = dtmFechaEmision;
	}

	public Datebox getDtmFechaInicio() {
		return dtmFechaInicio;
	}

	public void setDtmFechaInicio(Datebox dtmFechaInicio) {
		this.dtmFechaInicio = dtmFechaInicio;
	}
	
	
	public void desactivarEditar() {
		getCmbClientes().setDisabled(true);
		desactivarDetalle();
		origen.setDisabled(true);
		destino.setDisabled(true);
		
	}
	public void desactivarAnular() {
		getDtmFechaEmision().setDisabled(true);
		getDtmFechaInicio().setDisabled(true);
		desactivarDetalle();
		origen.setDisabled(true);
		destino.setDisabled(true);
		getLblDiasVigencia().setDisabled(true);
		getCmbClientes().setDisabled(true);
		observacion.setDisabled(true);
	}
	public void desactivarProcesar(){
		activar.setVisible(true);
		rechazar.setVisible(true);
		getDtmFechaEmision().setDisabled(true);
		getDtmFechaInicio().setDisabled(true);
		desactivarDetalle();
		origen.setDisabled(true);
		destino.setDisabled(true);
		getLblDiasVigencia().setDisabled(true);
		getCmbClientes().setDisabled(true);
		
		txtMotivoRechazo.setReadonly(false);
		observacion.setDisabled(true);
		
	}
	
	private List<CompEncabezado> getEncabezadoArticulo2() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Serv/Art", 100);
		titulo.setMetodoBinder("getStrTipoProducto");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo", 150);
		titulo.setMetodoBinder("getStrTipo");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Clase", 150);
		titulo.setMetodoBinder("getStrClase");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Producto", 250);
		titulo.setMetodoBinder("getDescripcion");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fabricante", 250);
		titulo.setMetodoBinder("getCodigoFabricante");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	public CompGrupoBusqueda<UbicacionTransporte> getOrigen() {
		return origen;
	}

	public void setOrigen(CompGrupoBusqueda<UbicacionTransporte> origen) {
		this.origen = origen;
	}

	public CompGrupoBusqueda<UbicacionTransporte> getDestino() {
		return destino;
	}

	public void setDestino(CompGrupoBusqueda<UbicacionTransporte> destino) {
		this.destino = destino;
	}

	public Button getActivar() {
		return activar;
	}

	public void setActivar(Button activar) {
		this.activar = activar;
	}

	public Button getRechazar() {
		return rechazar;
	}

	public void setRechazar(Button rechazar) {
		this.rechazar = rechazar;
	}

	public Textbox getTxtMotivoRechazo() {
		return txtMotivoRechazo;
	}

	public void setTxtMotivoRechazo(Textbox txtMotivoRechazo) {
		this.txtMotivoRechazo = txtMotivoRechazo;
	}

	public Label getLblEstado() {
		return lblEstado;
	}

	public void setLblEstado(Label lblEstado) {
		this.lblEstado = lblEstado;
	}

	public Textbox getObservacion() {
		return observacion;
	}

	public void setObservacion(Textbox observacion) {
		this.observacion = observacion;
	}
	
	
}