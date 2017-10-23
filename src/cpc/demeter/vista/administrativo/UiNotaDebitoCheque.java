package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.soap.Text;

import org.zkoss.zhtml.Div;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Html;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Listbox;

import antlr.debug.Event;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.Cheque;
import cpc.modelo.demeter.administrativo.DetalleDocumentoFiscal;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.NotaDebito;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UiNotaDebitoCheque extends  CompVentanaBase<NotaDebito> {

	private static final long serialVersionUID = 6312240548253365129L;
	
	/*** datos Basicos *****/ 
	private CompGrupoDatos 					encfactura;
	private Textbox 						control;
	private Textbox							sede;
	private Datebox 						fecha;
	private CompBuscar<DocumentoFiscal> 	factura;
	private List<DocumentoFiscal>			facuras;


	/************ Datos nombre o  Razon social ***********/
	private CompGrupoDatos 					cliente; 
	private Textbox							cedula;
	private Textbox 						razonSocial;
	private Textbox 						direccion;
	
	/*****************  Observacion ********************/
	private CompGrupoDatos 					pieIzquierdo;
	private Textbox 						observacion;
	private Textbox							estado;
	//private List<IProducto> 			servicios;
	//private List<Impuesto> 				impuestos;

/********************* Listado de Cheques ***********************/

	private CompGrupoDatos 					contCheque;
	private Button							agregar;
	private Listbox  						cheque;
	private List<Cheque> 					cheques;
	
	/******************* Listado Nota de Debito *************/
	
	private CompGrupoDatos				 	contDetalle;
	private Button							quitDetalle;
	private Listbox						 	detalle;
	private List<DetalleDocumentoFiscal> 	detalles = new ArrayList<DetalleDocumentoFiscal>();
	
	/********************* Totales  **************/
	private CompGrupoDatos 					pieDerecho;
	private Doublebox 						subTotal;
	private Doublebox 						total;
	private Doublebox 						impuesto[]; // array de impuestos que se van a aplicar
	private Doublebox 						Baseimpuesto[]; // array de base de impuesto que se van
													// aplicar
	private Doublebox 						porcentajeImpuesto[]; // % del dinero del impuesto que se
	
	
	 
	
	
	public UiNotaDebitoCheque(String titulo, int ancho,	List<DocumentoFiscal> factu ) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.facuras = factu;

	}


	public void inicializar() {
		encfactura = new CompGrupoDatos("Detalle Nota Debito",4);

		control = new Textbox();
			control.setDisabled(true);
			
		sede = new Textbox();
			sede.setDisabled(true);
			sede.setWidth("250px");
		
		fecha = new Datebox();
			fecha.setDisabled(true);
			
		factura = new CompBuscar<DocumentoFiscal>(getEncabezadoFactura(), 0);
			factura.setModelo(facuras);
			factura.setDisabled(true);
			factura.setWidth("100px");
			factura.setAncho(680);
			factura.setListenerEncontrar(getControlador());
			
		/************ Datos nombre o  Razon social ***********/

		cliente = new CompGrupoDatos("Nombre o Razon Social", 2);

		cedula = new Textbox();
			cedula.setDisabled(true);

		razonSocial = new Textbox();
			razonSocial.setDisabled(true);
			razonSocial.setWidth("550px");
		
		direccion = new Textbox();
			direccion.setDisabled(true);
			direccion.setWidth("550px");

		
		/*****************  Observacion ********************/
	
		pieIzquierdo = new CompGrupoDatos("Observaciones", 1);
		
		observacion = new Textbox();
			observacion.setWidth("440px");
			observacion.setRows(4);

		estado = new Textbox();
			estado.setDisabled(true);
		
		/********************* Listado de Cheques ***********************/
		contCheque = new CompGrupoDatos("Cheques ",1);
		
		agregar = new Button("Agregar");
			agregar.setTooltiptext("Agregar un cheque al detalle de la nota de debito");
			agregar.addEventListener(Events.ON_CLICK,getControlador());
			 
		cheque = new Listbox();
			Listhead head = new Listhead();
				new Listheader("Nro Cheque ").setParent(head);
				new Listheader("Nro Cuenta").setParent(head);
				new Listheader("Monto").setParent(head);
				cheque.appendChild(head);
				cheque.setItemRenderer(new ListitemRenderer() {
						@Override
					public void render(Listitem arg0, Object arg1) throws Exception {
						// TODO Auto-generated method stub
						arg0.setValue(arg1);
						arg0.addEventListener(Events.ON_CLICK,getControlador());
						Cheque cheque = (Cheque) arg1;
						new Listcell(cheque.getNroCheque()).setParent(arg0);
						new Listcell(cheque.getNroCuenta()).setParent(arg0);
						new Listcell(cheque.getStrMonto()).setParent(arg0);
					}
				});
		
		contDetalle = new CompGrupoDatos("Detalle Nota ",1);
				
		quitDetalle = new Button("Quitar");
			quitDetalle.setTooltiptext("Quitar un Cheque de la lista de Detalles");
			quitDetalle.addEventListener(Events.ON_CLICK,getControlador());
		
		detalle = new Listbox();
		Listhead head2 = new Listhead();
		new Listheader(" Servicio/ Producto  ").setParent(head2);
		new Listheader(" Cantidad").setParent(head2);
		new Listheader(" Descripcion ").setParent(head2);
		new Listheader(" Precio Unitario ").setParent(head2);
		new Listheader(" Inpuesto ").setParent(head2);
		new Listheader(" Precio ").setParent(head2);
		
		detalle.appendChild(head2);
		detalle.setItemRenderer(new ListitemRenderer() {
				@Override
			public void render(Listitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				arg0.setValue(arg1);
				arg0.addEventListener(Events.ON_CLICK,getControlador());
				DetalleDocumentoFiscal detalle = (DetalleDocumentoFiscal) arg1;
				Double cantidad =  new Double(detalle.getCantidad());
				Double precioUnitario = new Double(detalle.getPrecioUnitario());
				Double precio = new Double(detalle.getPrecio());
				new Listcell(detalle.getServicio().toString()).setParent(arg0);
				new Listcell(cantidad.toString()).setParent(arg0);
				new Listcell(detalle.getComplementoDescripcion()).setParent(arg0);
				new Listcell(precioUnitario.toString()).setParent(arg0);
				new Listcell(detalle.getAlicuotaNombre()).setParent(arg0);
				new Listcell(precio.toString()).setParent(arg0);
						
			}
		});
		detalle.setModel(new ListModelList(detalles));
		
		/********************* Totales  **************/

		pieDerecho = new CompGrupoDatos("Totales", 5);


		subTotal = new Doublebox();
			subTotal.setDisabled(true);
			subTotal.setFormat("##,###,###,##0.00");
		
		total = new Doublebox();
			total.setDisabled(true);
			total.setFormat("##,###,###,##0.00");
	}

	public void dibujar() {

		cliente.setAnchoColumna(0, 200);

		encfactura.addComponente("Nro Control  :", control);
		encfactura.addComponente("Fecha :", fecha);
		encfactura.addComponente("Sede  :", sede);
		encfactura.addComponente("Factura  :", factura);
		encfactura.dibujar(this);

		/** grilla para mostrar en una sola linea la cedula y el Nro Telefonico ***/

		Grid grilla = new Grid();
			Columns columnas = new Columns();
			columnas.appendChild(new Column());
			columnas.appendChild(new Column());
			columnas.appendChild(new Column());
		
			grilla.appendChild(columnas);
			Rows filas = new Rows();
				Row row = new Row();
				row.appendChild(cedula);
				row.appendChild(new Label("Telefono:"));
				filas.appendChild(row);

				
		grilla.appendChild(filas);
		/******************************/

		cliente.addComponente("Cliente:", razonSocial);
		cliente.addComponente("Direccion:", direccion);
		cliente.addComponente("Cedula/RIF:", grilla);
		cliente.dibujar(this);
		
		/******* Litado de Cheques **/
		
		contCheque.addComponente(agregar);
		contCheque.addComponente(cheque);
	//	contCheque.addComponente(new Html("<a href='/aquivalaayuda'> has Click Aqui si Requieres Ayuda</a>"));
		contCheque.dibujar(this);
		
		contCheque.dibujar();
		
		
		
		/***************Detalle nota de Debito **************/
		
		Div botones= new Div();
			botones.appendChild(quitDetalle);
			
		contDetalle.addComponente(botones);
		contDetalle.addComponente(detalle);
		contDetalle.dibujar(this);

		 
		pieIzquierdo.addComponente(observacion);
	
		Hbox caja = new Hbox();
		caja.appendChild(new Label("Estado"));
		caja.appendChild(estado);

		pieIzquierdo.addComponente(caja);

		pieDerecho.addComponente(new Label(""));
		pieDerecho.addComponente("", new Label(""));
		pieDerecho.addComponente("Subtotal:", subTotal);

		
		/*for (int i = 0; i < impuestos.size(); i++) {
			porcentajeImpuesto[i].setWidth("25px");
			pieDerecho.addComponente(Baseimpuesto[i]);
			pieDerecho.addComponente("%", porcentajeImpuesto[i]);
			pieDerecho.addComponente(impuestos.get(i).getDescripcion(),	impuesto[i]);
		}
*/
		pieDerecho.addComponente(new Label(""));
		pieDerecho.addComponente("", new Label(""));
		pieDerecho.addComponente("Total", total);

		Hbox box = new Hbox();
			pieIzquierdo.dibujar(box);
			pieDerecho.dibujar(box);
		this.appendChild(box);
		
		
		agregar.addEventListener(Events.ON_CLICK,getControlador());
		addBotonera();
	}

	public void cargarValores(NotaDebito nota) throws ExcDatosInvalidos {

		control.setValue(getModelo().getStrNroDocumento());
		fecha.setValue(getModelo().getFecha());

		try {

			cedula.setValue(getModelo().getBeneficiario().getIdentidadLegal());
			factura.setSeleccion(getModelo().getFactura());
			razonSocial.setValue(getModelo().getNombreBeneficiario());
			direccion.setValue(getModelo().getDireccionBeneficiario()
					.toString());

		} catch (NullPointerException e)
		{
			// TODO: handle exception
		}

		getBinder().addBinding(factura, "seleccion",getNombreModelo() + ".factura", null, null, "save", null, null,	null, null);
		subTotal.setValue(Math.abs(getModelo().getMontoBase()));
		getBinder().addBinding(subTotal, "value",getNombreModelo() + ".montoBase", null, null, "save", null,	null, null, null);

		total.setValue(Math.abs(getModelo().getMontoTotal()));
		getBinder().addBinding(total, "value",	getNombreModelo() + ".montoTotal", null, null, "save", null,null, null, null);

		observacion.setValue(getModelo().getObservacion());
		getBinder().addBinding(observacion, "value",getNombreModelo() + ".observacion", null, null, "save", null,	null, null, null);
	
		try
		{
			estado.setValue(getModelo().getEstado().getDescripcion());
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
		razonSocial.setConstraint("no empty : Razon Social no valida");

	}

	private List<CompEncabezado> encabezadosPrimario() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Servicio Afectado", 340);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getServicio");
		titulo.setMetodoModelo(".servicio");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cantidad", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidad");
		titulo.setMetodoModelo(".cantidad");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Descripcion", 150);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getComplementoDescripcion");
		titulo.setMetodoModelo(".complementoDescripcion");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Precio Und", 80);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getPrecioUnitario");
		titulo.setMetodoModelo(".precioUnitario");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Impuesto", 150);
		// titulo.setMetodoComponente("value");
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getAlicuota");
		// titulo.setMetodoModelo(".complementoDescripcion");
		titulo.setMetodoModelo(".alicuota");
		// titulo.setOrdenable(true);
		titulo.setVisible(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Precio", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getPrecio");
		titulo.setMetodoModelo(".precio");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		
		return encabezado;
	}

	private List<CompEncabezado> getEncabezadoFactura()
	{
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Factura", 100);
		titulo.setMetodoBinder("getStrNroDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 100);
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 120);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre", 250);
		titulo.setMetodoBinder("getNombreBeneficiario");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto", 100);
		titulo.setMetodoBinder("getStrTotal");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> getEncabezadoArticulo() 
	{
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

		titulo = new CompEncabezado("Fabricante", 250);
		titulo.setMetodoBinder("getCodigoFabricante");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}
		
	private List<CompEncabezado> getEncabezadoCheque() {
		// TODO Auto-generated method stub
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado(" ID ", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nro Cheque", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroCheque");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nro Cuenta ", 80);
		titulo.setMetodoBinder("getNroCuenta");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrMonto");
		encabezado.add(titulo);

		return encabezado;
	}
 

	public Textbox getSede() {
		return sede;
	}

	public Textbox getCedula() {
		return cedula;
	}

	public Doublebox getSubTotal() {
		return subTotal;
	}

	public Doublebox[] getPorcentajeImpuesto() {
		return porcentajeImpuesto;
	}

	public Doublebox[] getImpuesto() {
		return impuesto;
	}

	public Doublebox getTotal() {
		return total;
	}

	public CompBuscar<DocumentoFiscal> getFactura() {
		return factura;
	}



	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			activarConsulta();
		if (modoOperacion == Accion.CORREGIR)
			getFactura().setDisabled(true);
		else
			modoEdicion();
	}

	public void activarConsulta() {
		razonSocial.setDisabled(true);
		factura.setDisabled(true);
		direccion.setDisabled(true);
		observacion.setDisabled(true);
	//	desactivarDetalle();
	}

	public void modoEdicion() {
		razonSocial.setDisabled(true);
		factura.setDisabled(false);
		observacion.setDisabled(false);
	}

	public Doublebox[] getBaseimpuesto() {
		return Baseimpuesto;
	}

	public Textbox getRazonSocial() {
		return razonSocial;
	}

	public Textbox getDireccion() {
		return direccion;
	}


	public boolean desactivarnota(int modo) {
		

		return true;
	}
 
	
	public void ActualizarDetalles() {
		detalle.setModel(new ListModelList(detalles));
	}

	public List<Cheque> getCheques() {
		return cheques;
	}

	public void setCheques(List<Cheque> cheques) {
		this.cheques = cheques;
	}

	public void actualizarcheques() {
		// TODO Auto-generated method stub
		cheque.setModel(new ListModelArray(cheques));
	}

	public Button getAgregar() {
		return agregar;
	}

	public void setAgregar(Button agregar) {
		this.agregar = agregar;
	}

	public Listbox getCheque() {
		return cheque;
	}

	public void setCheque(Listbox cheque) {
		this.cheque = cheque;
	}

	
	public void agregardetalle(DetalleDocumentoFiscal valor)
	{
		detalles.add(valor);
		detalle.setModel(new ListModelList(detalles));
	}


	public void actualizarTotales() {
		// TODO Auto-generated method stub
		 
	   Double monto = new Double(0);
	   
	   for(DetalleDocumentoFiscal detalle : detalles)
	   {
		   monto += detalle.getPrecio();
	   }
	   
	   subTotal.setValue(monto);
	   total.setValue(monto);
	}


	public Button getQuitDetalle() {
		return quitDetalle;
	}


	public void setQuitDetalle(Button quitDetalle) {
		this.quitDetalle = quitDetalle;
	}


	public Listbox getDetalle() {
		return detalle;
	}


	public void setDetalle(Listbox detalle) {
		this.detalle = detalle;
	}


	public void quitarDetalle(DetalleDocumentoFiscal detalle2) {
		// TODO Auto-generated method stub
		for(int i = 0; i < detalles.size(); i++)
		{
			DetalleDocumentoFiscal detalle = detalles.get(i);
			if(detalle.getId() == null)
			{ // si NO tiene id asociado  lo busco por direccion de memoria :-D 
				if(detalle == detalle2)
				{
						//es el mismo
					detalles.remove(i);
					this.detalle.setModel(new ListModelList(detalles));
					return;
				};
			}// si tiene id asociado 
			else if(detalle.getId().equals(detalle2))
			{
				// es el mismo
				detalles.remove(i);
				this.detalle.setModel(new ListModelList(detalles));
				return;
			}
		}
			
		}


	public List<DetalleDocumentoFiscal> getDetalles() {
		return detalles;
	}


	public void setDetalles(List<DetalleDocumentoFiscal> detalles) {
		this.detalles = detalles;
	}


	public void actualizarModeloDetalle() {
		// TODO Auto-generated method stub
		getModelo().setDetalles(detalles);
	}

	
}
