package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.List;

 
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.controlador.administrativo.ContNotaCredito;
import cpc.modelo.demeter.administrativo.DetalleDocumentoFiscal;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.Impuesto;
import cpc.modelo.demeter.administrativo.NotaCredito;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.negocio.demeter.basico.NegocioArticuloVenta;
import cpc.persistencia.demeter.implementacion.administrativo.PerFactura;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class UINotaCredito extends
		CompVentanaMaestroDetalle<NotaCredito, DetalleDocumentoFiscal> {

	private static final long serialVersionUID = 6312240548253365129L;
	private List<DocumentoFiscal> facuras;
	private List<IProducto> servicios;
	private List<Impuesto> impuestos;

	private CompBuscar<IProducto> servicio;
	private Textbox control, sede, cedula;
	private Datebox fecha;
	private Textbox razonSocial;
	private Textbox direccion;
	private Textbox direccionfiscal;
	private CompGrupoDatos cliente, encfactura, pieIzquierdo, pieDerecho;
	private Doublebox subTotal, total;
	private Doublebox impuesto[];
	private Doublebox Baseimpuesto[];
	private Doublebox porcentajeImpuesto[];
	private Textbox observacion, estado;
	private CompBuscar<DocumentoFiscal> factura;
	private boolean desactivarnota;
	private Button  agregarTodo;
	
	public UINotaCredito(String titulo, int ancho, List<DocumentoFiscal> factu,
			List<Impuesto> impuestos, List<IProducto> servicios)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.facuras = factu;
		this.impuestos = impuestos;
		this.servicios = servicios;
	}

	public UINotaCredito(NotaCredito modeloIn, String titulo, int ancho)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		setModelo(modeloIn);
	}

	public void inicializar() {
		setGbEncabezado(1, "");
		setGbDetalle("Detalle Nota Credito");
		setGbPie(2);
		control = new Textbox();
		sede = new Textbox();
		cedula = new Textbox();
		fecha = new Datebox();
		cliente = new CompGrupoDatos("Nombre o Razon Social", 2);
		encfactura = new CompGrupoDatos("Datos basicos", 4);
		pieDerecho = new CompGrupoDatos("Totales", 5);
		pieIzquierdo = new CompGrupoDatos("Observaciones", 1);
		factura = new CompBuscar<DocumentoFiscal>(getEncabezadoFactura(), 0);
		razonSocial = new Textbox();
		direccion = new Textbox();
		desactivarnota = true;
		agregarTodo= new Button("Agregar Todo");
		agregarTodo.setImage((String) SpringUtil.getBean("iconoAdd"));
		agregarTodo.addEventListener(Events.ON_CLICK, ((ContNotaCredito) getControlador()).agregarTodosItems());

		subTotal = new Doublebox();
		observacion = new Textbox();
		estado = new Textbox();
		total = new Doublebox();
		impuesto = new Doublebox[impuestos.size()];
		Baseimpuesto = new Doublebox[impuestos.size()];
		porcentajeImpuesto = new Doublebox[impuestos.size()];
		for (int i = 0; i < impuestos.size(); i++) {
			porcentajeImpuesto[i] = new Doublebox(impuestos.get(i).getPorcentaje());
			impuesto[i] = new Doublebox();
			Baseimpuesto[i] = new Doublebox();
			impuesto[i].setAttribute("dato", impuestos.get(i));
			impuesto[i].setDisabled(true);
			porcentajeImpuesto[i].setDisabled(true);
			Baseimpuesto[i].setDisabled(true);
			impuesto[i].setFormat("##,###,###,##0.00");
			Baseimpuesto[i].setFormat("##,###,###,##0.00");
		}
		cedula.setDisabled(true);
		razonSocial.setDisabled(true);
		direccion.setDisabled(true);
		control.setDisabled(true);
		sede.setDisabled(true);
		estado.setDisabled(true);
		fecha.setDisabled(true);
		subTotal.setDisabled(true);
		total.setDisabled(true);
		factura.setModelo(facuras);
	}

	public void dibujar() {
		observacion.setWidth("440px");
		razonSocial.setWidth("550px");
		subTotal.setFormat("##,###,###,##0.00");
		total.setFormat("##,###,###,##0.00");

		direccion.setWidth("550px");
		sede.setWidth("250px");
		factura.setWidth("100px");
		factura.setAncho(680);

		cliente.setAnchoColumna(0, 200);
		encfactura.addComponente("Nro Control  :", control);
		encfactura.addComponente("Fecha :", fecha);
		encfactura.addComponente("Sede  :", sede);
		encfactura.addComponente("Factura  :", factura);

		encfactura.dibujar(getGbEncabezado());
		cliente.addComponente("Cliente:", razonSocial);
		cliente.addComponente("Direccion:", direccion);
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

		cliente.addComponente("Cedula/RIF:", grilla);
		cliente.dibujar(getGbEncabezado());

		try {
			setDetalles(cargarLista(desactivarnota), getModelo().getDetalles(),encabezadosPrimario());
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getDetalle().setNuevo(new DetalleDocumentoFiscal());
		pieIzquierdo.addComponente(observacion);
		observacion.setRows(4);

		Hbox caja = new Hbox();
		caja.appendChild(new Label("Estado"));
		caja.appendChild(estado);

		pieIzquierdo.addComponente(caja);
		pieDerecho.addComponente(new Label(""));
		pieDerecho.addComponente("", new Label(""));
		pieDerecho.addComponente("Subtotal:", subTotal);
		for (int i = 0; i < impuestos.size(); i++) {
			porcentajeImpuesto[i].setWidth("25px");
			pieDerecho.addComponente(Baseimpuesto[i]);
			pieDerecho.addComponente("%", porcentajeImpuesto[i]);
			pieDerecho.addComponente(impuestos.get(i).getDescripcion(),
					impuesto[i]);
		}
		pieDerecho.addComponente(new Label(""));
		pieDerecho.addComponente("", new Label(""));
		pieDerecho.addComponente("Total", total);
		getGbPie().dibujarGrupos(pieIzquierdo, pieDerecho);

		dibujarEstructura();
		factura.setListenerEncontrar(getControlador());
		getDetalle().setListenerBorrar(getControlador());
		agregarTodo.setParent(getDetalle().getBoton().getParent());;
	}

	public void cargarValores(NotaCredito nota) throws ExcDatosInvalidos {
		control.setValue(getModelo().getStrNroDocumento());
		fecha.setValue(getModelo().getFecha());

		try {
			cedula.setValue(getModelo().getBeneficiario().getIdentidadLegal());
			factura.setSeleccion(getModelo().getFactura());
			razonSocial.setValue(getModelo().getNombreBeneficiario());
			direccion.setValue(getModelo().getDireccionBeneficiario()
					.toString());
			direccion.setValue(getModelo().getFactura().getDireccionFiscal());
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
		getBinder().addBinding(factura, "seleccion",
				getNombreModelo() + ".factura", null, null, "save", null, null,
				null, null);
		subTotal.setValue(Math.abs(getModelo().getMontoBase()));
		getBinder().addBinding(subTotal, "value",
				getNombreModelo() + ".montoBase", null, null, "save", null,
				null, null, null);
		total.setValue(Math.abs(getModelo().getMontoTotal()));
		getBinder().addBinding(total, "value",
				getNombreModelo() + ".montoTotal", null, null, "save", null,
				null, null, null);

		observacion.setValue(getModelo().getObservacion());
		getBinder().addBinding(observacion, "value",
				getNombreModelo() + ".observacion", null, null, "save", null,
				null, null, null);
		try {
			estado.setValue(getModelo().getEstado().getDescripcion());
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
		razonSocial.setConstraint("no empty : Razon Social no valida");
		//

	}

	private List<CompEncabezado> encabezadosPrimario() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Servicio Afectado", 350);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getServicio");
		titulo.setMetodoModelo(".servicio");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cantidad", 70);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidad");
		titulo.setMetodoModelo(".cantidad");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Descripcion", 160);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getComplementoDescripcion");
		titulo.setMetodoModelo(".complementoDescripcion");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Precio Und", 90);
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

		titulo = new CompEncabezado("Precio", 100);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getPrecio");
		titulo.setMetodoModelo(".precio");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> encabezadosPrimario2() {

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

		titulo = new CompEncabezado("Impuesto", 120);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getDescripcion");
		// titulo.setMetodoBinder("getPrecioUnitario");
		// titulo.setMetodoModelo(".precioUnitario");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Precio", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getPrecio");
		titulo.setMetodoModelo(".precio");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> getEncabezadoFactura() {
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

		titulo = new CompEncabezado("Fabricante", 250);
		titulo.setMetodoBinder("getCodigoFabricante");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	private ArrayList<Component> cargarLista(boolean a)
			throws ExcFiltroExcepcion {
		a = getDesactivarNota();
		ArrayList<Component> lista = new ArrayList<Component>();
		servicio = new CompBuscar<IProducto>(getEncabezadoArticulo(), 3);
		Textbox descripcion = new Textbox();
		Doublebox cantidad = new Doublebox();
		Doublebox precioUnidad = new Doublebox();
		Doublebox precio = new Doublebox();
		CompCombobox<Impuesto> impuesto = new CompCombobox<Impuesto>();

		servicio.setWidth("320px");
		descripcion.setWidth("150px");

		cantidad.setWidth("60px");
		precioUnidad.setWidth("80px");
		precio.setWidth("90px");
		servicio.setAncho(750);

		servicio.setModelo(servicios);
		precio.setDisabled(true);
		precioUnidad.setDisabled(!desactivarnota);
		impuesto.setDisabled(!desactivarnota);
		impuesto.setReadonly(true);

		impuesto.setModelo(NegocioArticuloVenta.getInstance().getImpuestos());
		impuesto.setWidth("120px");

		precioUnidad.addEventListener(Events.ON_CHANGE, getControlador());
		cantidad.addEventListener(Events.ON_CHANGE, getControlador());
		impuesto.addEventListener(Events.ON_CHANGE, getControlador());

		servicio.setListenerEncontrar(getControlador());

		lista.add(servicio);

		lista.add(cantidad);
		lista.add(descripcion);
		lista.add(precioUnidad);
		lista.add(impuesto);
		lista.add(precio);
		return lista;
	}

	public List<IProducto> getServicios() {
		return servicios;
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
		else
			modoEdicion();
	}

	public void activarConsulta() {
		razonSocial.setDisabled(true);
		factura.setDisabled(true);
		direccion.setDisabled(true);
		observacion.setDisabled(true);
		desactivarDetalle();
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

	public Textbox getDireccionFiscal() {
		return direccionfiscal;
	}

	public void actualizarModeloServicios(List<IProducto> servicios) {
		this.servicios = servicios;
		servicio.setModelo(servicios);
		// getDetalle().clear();
	}

	public boolean desactivarnota(int modo) {
		desactivarnota = true;
		if (modo != Accion.AGREGAR)
			desactivarnota = true;
		else if (new PerFactura().getServiciosFacturados(
				getFactura().getValorObjeto().getId()).size() == 0)
			desactivarnota = false;
		return desactivarnota;

	}

	public boolean getDesactivarNota() {
		return desactivarnota;
	}
  

}
