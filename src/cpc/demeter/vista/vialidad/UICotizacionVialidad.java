package cpc.demeter.vista.vialidad;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;

import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.ArchivoContrato;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.demeter.administrativo.CotizacionVialidad;
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.administrativo.TipoContrato;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.ministerio.dimension.UbicacionDireccion;
import cpc.modelo.sigesp.basico.Sede;
import cpc.negocio.demeter.administrativo.NegocioCotizacionVialidad;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompGrillaConBoton;
import cpc.zk.componente.ui.ComponentesAutomaticos;
import cpc.zk.componente.ventanas.CompGrupoBusqueda;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;


public class UICotizacionVialidad extends
		CompVentanaMaestroDetalle<CotizacionVialidad, DetalleContrato> {

	private static final long serialVersionUID = 1193107134381909424L;
	private List<IProducto> articuloVentas;
	private List<Sede>      sedes;
	private List<DetalleContrato> detallesContrato;
	private List<ClienteAdministrativo> clientes;
	private List<UbicacionDireccion> direcciones;
 	
	private CompGrillaConBoton<ArchivoContrato> archivos;
	private CompGrupoBusqueda<UbicacionDireccion> cgbubicacionFisica;
	private CotizacionVialidad cotizacion;
	private List<TipoContrato> tiposContratos;
	private CompCombobox<TipoContrato> tipoContrato;
	private int accion;
	private NegocioCotizacionVialidad  servicioCotizacion = NegocioCotizacionVialidad.getInstance();
	private CompGrupoDatos cgdContrato;
	private CompGrupoDatos cgdPagador;
	private CompGrupoDatos cgdBeneficario;
	private CompGrupoDatos cgdArchivos;
	private Label LblCedulaRifPagado;
	private Label lblDireccionPagador;
	
	private Label LblCedulaRifBeneficiario;
	private Label lblDireccionBeneficiario;
	
	private Datebox dtmFechaEmision;
	private Datebox dtmFechaInicio;
	private Label lblFechaHasta;
	
	private Intbox lblDiasVigencia;

	private Label lblNumeroContrato;
	private CompBuscar<ClienteAdministrativo> cmbClientesPagador;
	private CompBuscar<ClienteAdministrativo> cmbClientesBeneficiario;
	private Combobox   cmbsede;
	private CompGrupoDatos cgdTotales;
	private Doublebox base;
	private Doublebox impuesto;
	private Doublebox total;
	private CompBuscar<IProducto> cmbArticuloventa;
	private String nombreSede;

	
	public UICotizacionVialidad(String titulo, int ancho, int modo,
			List<IProducto> articuloVentas, List<DetalleContrato> detallesContrato,
			List<ClienteAdministrativo> clientes,List<UbicacionDireccion> direcciones,
			 CotizacionVialidad cotizacion, List<TipoContrato> tipoContratos, List<Sede> sedes,String nombreSede) {
		super(titulo, ancho);

		this.articuloVentas = articuloVentas;
		this.detallesContrato = detallesContrato;
		this.clientes = clientes;
		this.cotizacion = cotizacion;
		this.direcciones=direcciones;
		this.accion = modo;
		this.tiposContratos =tipoContratos;
		this.sedes = sedes;
		this.nombreSede=nombreSede;
	
	}
	
	@Override
	public void inicializar() {

		cgdContrato = new CompGrupoDatos("Cotizacion", 4);
		cgdBeneficario = new CompGrupoDatos("Beneficiario", 2);
		cgdPagador = new CompGrupoDatos("Pagador", 2);
		cgdArchivos = new CompGrupoDatos("Archivos Anexos", 1);
		LblCedulaRifPagado = new Label();
		lblDireccionPagador = new Label();
		lblDireccionPagador.setWidth("650px");
		lblDireccionPagador.setMultiline(true);
		
		LblCedulaRifBeneficiario = new Label();
		lblDireccionBeneficiario = new Label();
		lblDireccionBeneficiario.setWidth("650px");
		lblDireccionBeneficiario.setMultiline(true);
		
		dtmFechaEmision = new Datebox();
		dtmFechaInicio = new Datebox();
		dtmFechaInicio.addEventListener(Events.ON_CHANGE, getControlador());
		
		lblFechaHasta = new Label();
		lblDiasVigencia = new Intbox();
		lblDiasVigencia.addEventListener("onChange", getControlador());
	
		tipoContrato = new CompCombobox<TipoContrato>();
		tipoContrato.setModelo(tiposContratos);
		tipoContrato.setReadonly(true);
		tipoContrato.addEventListener(Events.ON_SELECTION, getControlador());
		lblNumeroContrato = new Label();
		cmbClientesPagador = new CompBuscar<ClienteAdministrativo>(
				getEncabezadoCliente(), 1);
		cmbClientesPagador.setModelo(clientes);
		cmbClientesPagador.setAncho(650);

		cmbClientesBeneficiario = new CompBuscar<ClienteAdministrativo>(
				getEncabezadoCliente(), 1);
		cmbClientesBeneficiario.setModelo(clientes);
		cmbClientesBeneficiario.setAncho(650);
		
		cmbsede = new Combobox();
		for(Sede sede: sedes)
		{
			Comboitem cmb = new Comboitem(sede.getNombre());
			cmb.setValue(sede);
			cmbsede.appendChild(cmb);
			if(sede.getNombre().contains(nombreSede))
			{	 
				cmbsede.setSelectedItem(cmb);
			}
		}

		cgdTotales = new CompGrupoDatos("Totales", 2);

		base = new Doublebox();
		impuesto = new Doublebox();
		total = new Doublebox();
		base.setReadonly(true);
		impuesto.setReadonly(true);
	
		total.setReadonly(true);
			setDetalles(cargarLista(), getModelo().getDetallesContrato(),
				encabezadosDetalle());
		getDetalle().setNuevo(new DetalleContrato());
		getDetalle().setListenerBorrar(getControlador());
		
		try {
			cgbubicacionFisica = new CompGrupoBusqueda<UbicacionDireccion>(
					"Ubicacion Fisica", getEncabezadoUbicacion(), getModelo()
							.getUbicacion(), ComponentesAutomaticos.LABEL);
			cgbubicacionFisica.setNuevo(new UbicacionDireccion());
			cgbubicacionFisica.setModeloCatalogo(direcciones);
			cgbubicacionFisica.setAnchoValores(200);
			cgbubicacionFisica.setAnchoCatalogo(500);
			cgbubicacionFisica.addListener(getControlador());
		} catch (Exception e) {
		}

		archivos = new CompGrillaConBoton<ArchivoContrato>(cargarListaArchivos(),
				getModelo().getArchivoContrato(), encabezadosArchivos(),
				getControlador());
		
		archivos.setNuevo(new ArchivoContrato());
	}


	@Override
	public void dibujar() {

		cgdContrato.addComponente("Fecha Emision", dtmFechaEmision);
		cgdContrato.addComponente("Fecha desde", dtmFechaInicio);
		cgdContrato.addComponente("fecha hasta", lblFechaHasta);
		cgdContrato.addComponente("Dias de Vigencia",lblDiasVigencia);
		cgdContrato.addComponente("Numero Cotizacion", lblNumeroContrato);
		cgdContrato.addComponente("Tipo de Contrato:", tipoContrato);
		cgdContrato.addComponente("Sede",cmbsede);
		archivos.dibujar();
		
		cgdPagador.addComponente("Cliente", cmbClientesPagador);
		cgdPagador.addComponente("Cedula/Rif", LblCedulaRifPagado);
		cgdPagador.addComponente("Direccion", lblDireccionPagador);
		
		cgdBeneficario.addComponente("Cliente", cmbClientesBeneficiario);
		cgdBeneficario.addComponente("Cedula/Rif", LblCedulaRifBeneficiario);
		cgdBeneficario.addComponente("Direccion", lblDireccionBeneficiario);
		
		cgdArchivos.addComponente(archivos);
		
		cgdTotales.addComponente("base", base);
		cgdTotales.addComponente("impuesto", impuesto);
		cgdTotales.addComponente("total", total);

		cgdContrato.dibujar(this);
		cgdArchivos.dibujar(this);
		cgdPagador.dibujar(this);
		cgdBeneficario.dibujar(this);
		cgbubicacionFisica.dibujar();
		this.appendChild(cgbubicacionFisica);
		
		cgdTotales.dibujar(this);
		cmbClientesPagador.setListenerEncontrar(getControlador());
		cmbClientesBeneficiario.setListenerEncontrar(getControlador());
		
		
		dibujarEstructura();
		cgdBeneficario.setVisible(false);
		addBotonera();
	}

	@Override
	public void cargarValores(CotizacionVialidad dato) throws ExcDatosInvalidos {
		if (accion == Accion.AGREGAR) {
		} else {
            tipoContrato.setSeleccion(cotizacion.getTipoContrato());
			cmbClientesPagador.setSeleccion(cotizacion.getPagador()
					.getClienteAdministrativo());
			cmbClientesPagador.setText(cotizacion.getPagador()
					.getClienteAdministrativo().getNombreCliente()); 
					
			LblCedulaRifPagado.setValue(dato.getPagador().getIdentidadLegal());
			lblDireccionPagador.setValue(cotizacion.getPagador()
					.getClienteAdministrativo().getCliente().getDireccion());
			if(cotizacion.getBeneficiario()!=null) {cgdBeneficario.setVisible(true);
			cmbClientesBeneficiario.setSeleccion(cotizacion.getBeneficiario()
					.getClienteAdministrativo());
			cmbClientesBeneficiario.setText(cotizacion.getBeneficiario()
					.getClienteAdministrativo().getNombreCliente()); 
			LblCedulaRifBeneficiario.setValue(dato.getBeneficiario().getIdentidadLegal());
			lblDireccionBeneficiario.setValue(cotizacion.getBeneficiario()
					.getClienteAdministrativo().getCliente().getDireccion());}
		
			
			dtmFechaEmision.setValue(cotizacion.getFecha());
			dtmFechaInicio.setValue(cotizacion.getFechaDesde());

			
			if(cotizacion.getSede() != null)
			{
				//Comboitem cm = new Comboitem(cotizacion.getSede().getNombre());
				for(Object cm : cmbsede.getChildren())
				{                     
			 
					if(cm instanceof Comboitem )
					{
						Sede sedeaux = (Sede) ((Comboitem) cm).getValue();
						if(sedeaux.equals(cotizacion.getSede()))
						{
							cmbsede.setSelectedItem( (Comboitem) cm);
							break;
						}
					}
				}
				
			}
			lblFechaHasta.setValue(cotizacion.getFechaHastaString());
			lblDiasVigencia.setValue(cotizacion.getDiasVigencia());
			
			lblNumeroContrato.setValue(cotizacion.getStrNroDocumento());
			total.setValue(cotizacion.getTotal());
			base.setValue(cotizacion.getMonto());
			impuesto.setValue(cotizacion.getTotal() - cotizacion.getMonto());
			getBinder().addBinding(cgbubicacionFisica, "modelo",
					getNombreModelo() + ".ubicacion", null, null, "save", null,
					null, null, null);
		

			try {
				cgbubicacionFisica.addListener(getControlador());
				cgbubicacionFisica.actualizarValores();
			} catch (Exception e) {
				// TODO: handle exception
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
		cmbArticuloventa  = new CompBuscar<IProducto>(
				getEncabezadoArticulo2(), 3);

		cmbArticuloventa.setModelo(articuloVentas);
		Doublebox cantidad = new Doublebox();
		Doublebox precioUnidad = new Doublebox();
		Doublebox precio = new Doublebox();

		cmbArticuloventa.setWidth("340px");
		cantidad.setWidth("60px");
		precioUnidad.setWidth("80px");
		precio.setWidth("90px");
		cmbArticuloventa.setAncho(650);

		cantidad.setDisabled(false);
		precio.setDisabled(true);
		precioUnidad.setDisabled(false);

		cantidad.addEventListener(Events.ON_CHANGE, getControlador());
		precioUnidad.addEventListener(Events.ON_CHANGE, getControlador());
		cmbArticuloventa.setListenerEncontrar(getControlador());

		lista.add(cmbArticuloventa);
		lista.add(cantidad);
		lista.add(precioUnidad);
		lista.add(precio);

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

	public CompGrupoDatos getCgdPagador() {
		return cgdPagador;
	}

	public void setCgdPagador(CompGrupoDatos cgdPagador) {
		this.cgdPagador = cgdPagador;
	}

	public CompGrupoDatos getCgdBeneficario() {
		return cgdBeneficario;
	}

	public void setCgdBeneficario(CompGrupoDatos cgdBeneficario) {
		this.cgdBeneficario = cgdBeneficario;
	}

	public Label getLblCedulaRifBeneficiario() {
		return LblCedulaRifBeneficiario;
	}

	public void setLblCedulaRifBeneficiario(Label lblCedulaRifBeneficiario) {
		LblCedulaRifBeneficiario = lblCedulaRifBeneficiario;
	}

	public Label getLblDireccionBeneficiario() {
		return lblDireccionBeneficiario;
	}

	public void setLblDireccionBeneficiario(Label lblDireccionBeneficiario) {
		this.lblDireccionBeneficiario = lblDireccionBeneficiario;
	}

	public CompBuscar<ClienteAdministrativo> getCmbClientesPagador() {
		return cmbClientesPagador;
	}

	public void setCmbClientesPagador(
			CompBuscar<ClienteAdministrativo> cmbClientesPagador) {
		this.cmbClientesPagador = cmbClientesPagador;
	}

	public CompBuscar<ClienteAdministrativo> getCmbClientesBeneficiario() {
		return cmbClientesBeneficiario;
	}

	public void setCmbClientesBeneficiario(
			CompBuscar<ClienteAdministrativo> cmbClientesBeneficiario) {
		this.cmbClientesBeneficiario = cmbClientesBeneficiario;
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
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cantidad", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidad");
		titulo.setMetodoModelo(".cantidad");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Precio Und", 80);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getPrecioUnidad");
		titulo.setMetodoModelo(".precioUnidad");

		encabezado.add(titulo);
		titulo = new CompEncabezado("Precio", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getSubtotal");
		titulo.setMetodoModelo(".subtotal");
		encabezado.add(titulo);

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
		getDtmFechaEmision().setDisabled(true);
		getDtmFechaInicio().setDisabled(true);
		desactivarDetalle();
		getLblDiasVigencia().setDisabled(true);
		getCmbClientesPagador().setDisabled(true);
		getCmbClientesBeneficiario().setDisabled(true);
		
		desactivarDetalle();
		cgbubicacionFisica.setDisabled(true);
		archivos.setDisable(true);
		
	}
	public void desactivarAnular() {

		getDtmFechaEmision().setDisabled(true);
		getDtmFechaInicio().setDisabled(true);
		desactivarDetalle();
		getLblDiasVigencia().setDisabled(true);
		getCmbClientesPagador().setDisabled(true);
		getCmbClientesBeneficiario().setDisabled(true);
		getCmbsede().setDisabled(true);
		desactivarDetalle();
		cgbubicacionFisica.setDisabled(true);
		archivos.setDisable(true);
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

	
	
	public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}
	
	private List<CompEncabezado> getEncabezadoUbicacion() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("N° Codigo", 50);
		titulo.setMetodoBinder("getStrCodigo");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Documento", 100);
		titulo.setMetodoBinder("getDocumentoLegal");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Descripcion", 250);
		titulo.setMetodoBinder("getDescripcion");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Sector", 150);
		titulo.setMetodoBinder("getStrSector");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Parroquia", 150);
		titulo.setMetodoBinder("getStrParroquia");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Municipio", 150);
		titulo.setMetodoBinder("getStrMunicipio");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 150);
		titulo.setMetodoBinder("getStrEstado");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Superf. Total", 150);
		titulo.setMetodoBinder("getSuperficiesString");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}
	private ArrayList<Component> cargarListaArchivos() {
		ArrayList<Component> lista = new ArrayList<Component>();

		Button subir  = new Button();
		subir.setLabel("Subir Archivo");
		subir.addEventListener(Events.ON_CLICK, getControlador());
		subir.setDisabled(habilitarBoton(accion));
		Button bajar  = new Button("Bajar Archivo");
		bajar.addEventListener(Events.ON_CLICK, getControlador());
		Textbox nombre = new Textbox();
		
		subir.setWidth("50px");
		bajar.setWidth("50px");
		nombre.setWidth("300px");
		

		lista.add(subir);
		lista.add(bajar);
		lista.add(nombre);
		return lista;
	}
	private boolean habilitarBoton(int accion){
		return accion == Accion.AGREGAR ?  false: true;  
	}

	private List<CompEncabezado> encabezadosArchivos() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Subir", 30);
	

		encabezado.add(titulo);
		titulo = new CompEncabezado("Bajar",30);
		
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("nombre", 80);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNombreArchivo");
		titulo.setMetodoModelo(".nombreArchivo");

		encabezado.add(titulo);
		
		return encabezado;
	}
	
	
	public CompGrillaConBoton<ArchivoContrato> getArchivos() {
		return archivos;
	}

	public void setArchivos(CompGrillaConBoton<ArchivoContrato> archivos) {
		this.archivos = archivos;
	}

	public CompGrupoBusqueda<UbicacionDireccion> getCgbubicacionFisica() {
		return cgbubicacionFisica;
	}

	public CompCombobox<TipoContrato> getTipoContrato() {
		return tipoContrato;
	}

	public void setTipoContrato(CompCombobox<TipoContrato> tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	public void setCgbubicacionFisica(
			CompGrupoBusqueda<UbicacionDireccion> cgbubicacionFisica) {
		this.cgbubicacionFisica = cgbubicacionFisica;
	}

	public Combobox getCmbsede() {
		return cmbsede;
	}

	public void setCmbsede(Combobox cmbsede) {
		this.cmbsede = cmbsede;
	}
	
	public Sede getSede()
	{		
 		return (Sede) (cmbsede.getSelectedItem() == null ?  null : cmbsede.getSelectedItem().getValue());
	}
	
}