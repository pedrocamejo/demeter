package cpc.demeter.vista.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.demeter.administrativo.Cotizacion;
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.demeter.mantenimiento.SalidaExternaArticulo;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.modelo.sigesp.basico.Sede;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;

public class UICotizacionExpediente extends CompVentanaMaestroDetalle<Cotizacion, DetalleContrato> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3028544517928948872L;
	private List<IProducto> 			articuloVentas;
 	private List<ClienteAdministrativo> clientes;
	private List<Sede>      sedes;
	
 	private Cotizacion 					cotizacion;
	private int 						accion;
	private CompGrupoDatos 				cgdContrato;
	private Label 						lblCedulaRifPagado;
	private Label 						lblDireccionPagador;
	private Datebox 					dtmFechaEmision;
	private Datebox 					dtmFechaInicio;
	private Label 						lblFechaHasta;
	
	private Intbox 						lblDiasVigencia;
	private Label 						lblNumeroContrato;
	private CompBuscar<ClienteAdministrativo> cmbClientes;

	private CompGrupoDatos 				cgdTotales;
	private Doublebox 					base;
	private Doublebox					impuesto;
	private Doublebox 					total;
	private CompBuscar<IProducto> 		cmbArticuloventa;
	private Combobox   cmbsede;
	private String nombreSede;
	
	public UICotizacionExpediente(String titulo, int ancho, int modo,
			List<IProducto> articuloVentas, List<Almacen> almacenes,
			List<ClienteAdministrativo> clientes,
			SalidaExternaArticulo salidaExternaArticulo, Cotizacion cotizacion,List<Sede> sedes,String nombreSede) {
		super(titulo, ancho);
		this.articuloVentas = articuloVentas;
 
		this.clientes = clientes;
		this.cotizacion = cotizacion;
		this.accion = modo;
		this.sedes = sedes;
		this.nombreSede=nombreSede;

	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		cgdContrato = new CompGrupoDatos("Cotizacion",4);
		
		lblCedulaRifPagado = new Label();
		
		lblDireccionPagador = new Label();
		lblDireccionPagador.setWidth("650px");
		lblDireccionPagador.setMultiline(true);
		
		dtmFechaEmision = new Datebox();
		dtmFechaInicio = new Datebox();
		
		dtmFechaInicio.addEventListener(Events.ON_CHANGE, getControlador());
		
		lblFechaHasta = new Label();
		
		lblDiasVigencia = new Intbox();
		lblDiasVigencia.addEventListener("onChange", getControlador());

		lblNumeroContrato = new Label();
		
		cmbClientes = new CompBuscar<ClienteAdministrativo>(getEncabezadoCliente(), 1);
		cmbClientes.setModelo(clientes);
		cmbClientes.setAncho(650);

		cgdTotales = new CompGrupoDatos("Totales", 4);
 

		base = new Doublebox();
		impuesto = new Doublebox();
		total = new Doublebox();
		base.setReadonly(true);
		impuesto.setReadonly(true);
		total.setReadonly(true);
		
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

		setDetalles(cargarLista(), getModelo().getDetallesContrato(),encabezadosDetalle());
		getDetalle().setNuevo(new DetalleContrato());
		getDetalle().setListenerBorrar(getControlador());
	}

	@Override
	public void dibujar() {

		cgdContrato.addComponente("Cliente", cmbClientes);
		cgdContrato.addComponente("Cedula/Rif", lblCedulaRifPagado);
		cgdContrato.addComponente("Direccion", lblDireccionPagador);
		cgdContrato.addComponente("Fecha Emision", dtmFechaEmision);
		cgdContrato.addComponente("Fecha desde", dtmFechaInicio);
		cgdContrato.addComponente("Fecha hasta", lblFechaHasta);
		cgdContrato.addComponente("Dias de Vigencia",lblDiasVigencia);
		cgdContrato.addComponente("Numero Cotizacion", lblNumeroContrato);
		cgdContrato.addComponente("Sede",cmbsede);
		cgdTotales.addComponente("Base", base);
		cgdTotales.addComponente("Impuesto", impuesto);
		cgdTotales.addComponente("",new Label());
		cgdTotales.addComponente("Total", total);

		cgdContrato.dibujar(this);
		cgdTotales.dibujar(this);
		cmbClientes.setListenerEncontrar(getControlador());
		dibujarEstructura();
		addBotonera();
	}

	@Override
	public void cargarValores(Cotizacion dato) throws ExcDatosInvalidos {
		if (accion != Accion.AGREGAR) 
		{
			cmbClientes.setSeleccion(cotizacion.getPagador().getClienteAdministrativo());
			lblCedulaRifPagado.setValue(dato.getPagador().getIdentidadLegal());
			lblDireccionPagador.setValue(cotizacion.getPagador().getClienteAdministrativo().getCliente().getDireccion());
			dtmFechaEmision.setValue(cotizacion.getFecha());
			dtmFechaInicio.setValue(cotizacion.getFechaDesde());
			lblFechaHasta.setValue(cotizacion.getFechaHastaString());
			lblDiasVigencia.setValue(cotizacion.getDiasVigencia());
			lblNumeroContrato.setValue(cotizacion.getStrNroDocumento());
			total.setValue(cotizacion.getTotal());
			base.setValue(cotizacion.getMonto());
			impuesto.setValue(cotizacion.getTotal() - cotizacion.getMonto());

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
		}
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
		cmbArticuloventa  = new CompBuscar<IProducto>(getEncabezadoArticulo2(), 3);

		cmbArticuloventa.setModelo(articuloVentas);

		Doublebox cantidad = new Doublebox();
		cantidad.setFormat("##########.0000");
		Doublebox precioUnidad = new Doublebox();
		precioUnidad.setFormat("#############.0000");
		Doublebox precio = new Doublebox();
		precio.setFormat("#################.00");
		
		cmbArticuloventa.setWidth("340px");
		
		cantidad.setWidth("60px");
		precioUnidad.setWidth("80px");
		precio.setWidth("90px");
		
		cmbArticuloventa.setAncho(650);

		// para bloquear cantidad y servicio
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

	public CompBuscar<ClienteAdministrativo> getCmbClientes() {
		return cmbClientes;
	}

	public void setCmbClientes(CompBuscar<ClienteAdministrativo> cmbClientes) {
		this.cmbClientes = cmbClientes;
	}

	public Label getLblCedulaRifPagado() {
		return lblCedulaRifPagado;
	}

	public void setLblCedulaRifPagado(Label lblCedulaRifPagado) {
		this.lblCedulaRifPagado = lblCedulaRifPagado;
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
	
	
	public Combobox getCmbsede() {
		return cmbsede;
	}

	public void setCmbsede(Combobox cmbsede) {
		this.cmbsede = cmbsede;
	}

	public void desactivarEditar() {
		desactivarDetalle();
		getCmbClientes().setDisabled(true);
		
	}
	public void desactivarAnular() {
		getDtmFechaEmision().setDisabled(true);
		getDtmFechaInicio().setDisabled(true);
		desactivarDetalle();
		getLblDiasVigencia().setDisabled(true);
		getCmbClientes().setDisabled(true);
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



	public void activarConsulta() {
		// TODO Auto-generated method stub
		dtmFechaEmision.setDisabled(true);
		dtmFechaInicio.setDisabled(true);
		lblDiasVigencia.setDisabled(true);
		cmbClientes.setDisabled(true);
		base.setDisabled(true);
		impuesto.setDisabled(true);
		total.setDisabled(true);
		getDetalle().setDisable(true);
		
	}
	
	public Sede getSede()
	{		
 		return (Sede) (cmbsede.getSelectedItem() == null ?  null : cmbsede.getSelectedItem().getValue());
	}
	
	
	
}