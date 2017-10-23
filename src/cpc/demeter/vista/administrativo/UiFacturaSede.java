package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.Contrato;
import cpc.modelo.demeter.administrativo.DetalleDocumentoFiscal;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.Impuesto;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.persistencia.demeter.implementacion.PerSede;
import cpc.persistencia.demeter.implementacion.administrativo.PerFactura;
import cpc.persistencia.sigesp.implementacion.PerUnidadAdministrativa;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;

public class UiFacturaSede extends
		CompVentanaMaestroDetalle<DocumentoFiscal, DetalleDocumentoFiscal> {

	private static final long serialVersionUID = 6312240548253365129L;
	private List<Cliente> clientes;
	private List<IProducto> servicios;
	private List<Impuesto> impuestos;

	private Textbox control, sede;
	private Datebox fecha;
	private CompBuscar<Cliente> cedula;
	private Label telefono, razonSocial;
	private Textbox direccion;
	private CompGrupoDatos cliente, encfactura, pieIzquierdo, pieDerecho;
	private Doublebox subTotal, descuento, porcentajeDescuento, total, neto;
	private Doublebox impuesto[];
	private Doublebox Baseimpuesto[];
	private Doublebox porcentajeImpuesto[];
	private Textbox observacion, estado;

	private CompBuscar<Contrato> contrato;
	private CompCombobox<String> tipo;
	private Doublebox adelanto;
	private Label lblAdelanto;
	private Checkbox aplicarInteres;
	private Doublebox interesMora;
	private Label totalInteres;
	private Checkbox chkVentaArticulos;
	private Radiogroup rdoGrupo;
	private CompBuscar<IProducto> servicio;

	private boolean desactivarpreciounidad;

	private CompCombobox<UnidadAdministrativa> sedeorigen;
	private Datebox fechareal;

	public UiFacturaSede(String titulo, int ancho, List<Cliente> clientes,
			List<IProducto> servicios, List<Impuesto> impuestos)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.clientes = clientes;
		this.servicios = servicios;
		this.impuestos = impuestos;
	}

	public UiFacturaSede(DocumentoFiscal modeloIn, String titulo, int ancho)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		setModelo(modeloIn);
	}

	public void inicializar() {
		setGbEncabezado(1, "");
		setGbDetalle("Detalle factura");
		setGbPie(2);
		control = new Textbox();
		sede = new Textbox();
		cedula = new CompBuscar<Cliente>(getEncabezadoCliente(), 0);
		telefono = new Label();
		fecha = new Datebox();
		tipo = new CompCombobox<String>();
		cliente = new CompGrupoDatos("Nombre o Razon Social", 2);
		encfactura = new CompGrupoDatos("Datos basicos", 4);
		pieDerecho = new CompGrupoDatos("Totales", 5);
		pieIzquierdo = new CompGrupoDatos("Observaciones", 1);
		contrato = new CompBuscar<Contrato>(getEncabezadoContrato(), 0);
		razonSocial = new Label();
		direccion = new Textbox();
		subTotal = new Doublebox();
		descuento = new Doublebox();
		observacion = new Textbox();
		estado = new Textbox();
		porcentajeDescuento = new Doublebox();
		neto = new Doublebox();
		total = new Doublebox();
		impuesto = new Doublebox[impuestos.size()];
		Baseimpuesto = new Doublebox[impuestos.size()];
		porcentajeImpuesto = new Doublebox[impuestos.size()];
		lblAdelanto = new Label("Adelantos :");
		adelanto = new Doublebox();
		interesMora = new Doublebox();
		aplicarInteres = new Checkbox("Aplicar Interes");
		totalInteres = new Label();
		chkVentaArticulos = new Checkbox();
		sedeorigen = new CompCombobox<UnidadAdministrativa>();
		fechareal = new Datebox();

		for (int i = 0; i < impuestos.size(); i++) {
			porcentajeImpuesto[i] = new Doublebox(impuestos.get(i)
					.getPorcentaje());
			impuesto[i] = new Doublebox();
			Baseimpuesto[i] = new Doublebox();
			impuesto[i].setAttribute("dato", impuestos.get(i));
			impuesto[i].setDisabled(true);
			porcentajeImpuesto[i].setDisabled(true);
			Baseimpuesto[i].setDisabled(true);
			impuesto[i].setFormat("##,###,###,##0.00");
			Baseimpuesto[i].setWidth("100");
			Baseimpuesto[i].setFormat("##,###,###,##0.00");
		}
		/*
		 * cliente.setStyle(
		 * "background-color:#0099CC;font-size:20px;font-weight:bold;color:white"
		 * ); encfactura.setStyle(
		 * "background-color:#0099CC;font-size:20px;font-weight:bold;color:white"
		 * ); pieDerecho.setStyle(
		 * "background-color:#0099CC;font-size:20px;font-weight:bold;color:white"
		 * ); pieIzquierdo.setStyle(
		 * "background-color:#0099CC;font-size:20px;font-weight:bold;color:white"
		 * );
		 */
		String[] tipos = { "Pre-Servicio", "Post-Servicio" };
		adelanto.setReadonly(true);
		tipo.setModelo(Arrays.asList(tipos));
		control.setDisabled(true);
		sede.setDisabled(true);
		estado.setDisabled(true);
		fecha.setDisabled(true);
		subTotal.setDisabled(true);
		total.setDisabled(true);
		descuento.setDisabled(true);
		porcentajeDescuento.setDisabled(true);
		neto.setDisabled(true);
		cedula.setModelo(clientes);
		direccion.setMultiline(true);
		direccion.setRows(2);
		tipo.setReadonly(true);
		interesMora.setDisabled(true);
		sedeorigen.setModelo(new PerUnidadAdministrativa()
				.getUnidadesTraslado());

	}

	public void dibujar() {
		// setGbEncabezado(4,"Encabezado");
		// addGbEncabezado("Sede  :", sede);
		observacion.setWidth("400px");
		razonSocial.setWidth("550px");
		cedula.setAncho(650);
		contrato.setAncho(500);
		// rdoGrupo.setWidth("5000px");

		direccion.setWidth("550px");
		sede.setWidth("250px");
		contrato.setWidth("200px");

		subTotal.setFormat("##,###,###,##0.00");
		total.setFormat("##,###,###,##0.00");
		descuento.setFormat("##,###,###,##0.00");
		porcentajeDescuento.setFormat("##,###,###,##0.00");
		neto.setFormat("##,###,###,##0.00");
		/*
		 * rdoGrupo.appendItem("OTROS SERVICIOS ", "1");
		 * rdoGrupo.appendItem("MECANIZADO AGRICOLA", "2");
		 * rdoGrupo.appendItem("TRANSPORTE ","2");
		 * rdoGrupo.appendItem("MAQUINARIA PESADA ", "3");
		 * rdoGrupo.appendItem("SERVICIOS AGROPECUARIOS", "4");
		 * rdoGrupo.appendItem("AGENTE AUTORIZADO","5");
		 * rdoGrupo.appendItem("VENTA DE EQUIPOS Y ARTICULO","6");
		 */
		porcentajeDescuento.setWidth("40px");
		cliente.setAnchoColumna(0, 200);
		encfactura.addComponente("Nro Control  :", control);
		encfactura.addComponente("Fecha :", fecha);
		encfactura.addComponente("Sede  :", sede);
		encfactura.addComponente("Tipo  :", tipo);
		encfactura.addComponente("Contrato  :", contrato);
		encfactura.addComponente(lblAdelanto);
		encfactura.addComponente(adelanto);
		// encfactura.addComponente("Venta de Articulos",chkVentaArticulos);
		// encfactura.addComponente(rdoGrupo);

		encfactura.dibujar(getGbEncabezado());

		Grid grilla = new Grid();
		Columns columnas = new Columns();
		grilla.setStyle("border-style : hidden; border-color :white");
		columnas.appendChild(new Column());
		columnas.appendChild(new Column());
		columnas.appendChild(new Column());
		grilla.appendChild(columnas);

		Rows filas = new Rows();
		Row row = new Row();
		row.setStyle("background-color:white; color:black; border-style : hidden; border-color :white");
		row.appendChild(cedula);
		row.appendChild(new Label("Telefono:"));
		row.appendChild(telefono);
		filas.appendChild(row);
		grilla.appendChild(filas);

		cliente.addComponente("Cedula/RIF:", grilla);
		cliente.addComponente("Cliente:", razonSocial);
		cliente.addComponente("Direccion:", direccion);
		cliente.dibujar(getGbEncabezado());
		// addGbEncabezado(encfactura);
		// addGbEncabezado(cliente);
		// List<Articulo> articulos =
		// ArticuloPersistencia.getArticulos(getModelo());

		setDetalles(cargarLista(desactivarpreciounidad), getModelo()
				.getDetalles(), encabezadosPrimario());
		getDetalle().setNuevo(new DetalleDocumentoFiscal());
		pieIzquierdo.addComponente(observacion);
		observacion.setRows(3);

		Hbox caja = new Hbox();
		caja.appendChild(new Label("Estado"));
		caja.appendChild(estado);
		pieIzquierdo.addComponente(caja);
		pieIzquierdo.addComponenteMultiples(100, aplicarInteres);
		pieIzquierdo.addComponenteMultiples("% a Aplicar", 100, interesMora,
				totalInteres);
		pieIzquierdo.addComponente("sede origen", sedeorigen);
		pieIzquierdo.addComponente("fecha real", fechareal);

		pieDerecho.addComponente(new Label(""));
		pieDerecho.addComponente("", new Label(""));
		pieDerecho.addComponente("Subtotal:", subTotal);
		pieDerecho.addComponente(new Label(""));
		pieDerecho.addComponente("%", porcentajeDescuento);
		pieDerecho.addComponente("Descuento", descuento);
		pieDerecho.addComponente(new Label(""));
		pieDerecho.addComponente("", new Label(""));
		pieDerecho.addComponente("Neto : ", neto);

		for (int i = 0; i < impuestos.size(); i++) {
			porcentajeImpuesto[i].setWidth("40px");
			pieDerecho.addComponente(Baseimpuesto[i]);
			pieDerecho.addComponente("%", porcentajeImpuesto[i]);
			pieDerecho.addComponente(impuestos.get(i).getDescripcion(),
					impuesto[i]);
		}
		pieDerecho.addComponente(new Label(""));
		pieDerecho.addComponente("", new Label(""));
		pieDerecho.addComponente("Total", total);
		pieDerecho.setAlign("right");
		getGbPie().dibujarGrupos(pieIzquierdo, pieDerecho);
		dibujarEstructura();

		cedula.setListenerEncontrar(getControlador());
		contrato.setListenerEncontrar(getControlador());
		getDetalle().setListenerBorrar(getControlador());
		tipo.addEventListener(Events.ON_SELECT, getControlador());
		aplicarInteres.addEventListener(Events.ON_CHECK, getControlador());
		interesMora.addEventListener(Events.ON_CHANGE, getControlador());
		chkVentaArticulos.addEventListener(Events.ON_CHECK, this);

		sedeorigen.addEventListener(Events.ON_SELECT, getControlador());
		fechareal.addEventListener(Events.ON_SELECTION, getControlador());

	}

	public Checkbox getAplicarInteres() {
		return aplicarInteres;
	}

	public void cargarValores(DocumentoFiscal factura) throws ExcDatosInvalidos {
		control.setValue(getModelo().getStrNroDocumento());
		fecha.setValue(getModelo().getFecha());
		if (getModelo().getPostServicio() != null) {
			if (getModelo().getPostServicio())
				// tipo.setSelectedIndex(1);
				tipo.setText("Post-Servicio");
			else
				// tipo.setSelectedIndex(0);
				tipo.setText("Pre-Servicio");
		}
		getBinder().addBinding(cedula, "seleccion",
				getNombreModelo() + ".beneficiario", null, null, "save", null,
				null, null, null);
		try {
			razonSocial.setValue(getModelo().getBeneficiario().getNombres());
			cedula.setSeleccion(getModelo().getBeneficiario());
			if (getModelo().getBeneficiario().getTelefonos().size() > 0)
				telefono.setValue(getModelo().getBeneficiario().getTelefonos()
						.get(0).getNumero());
		} catch (NullPointerException e) {

		}
		direccion.setValue(getModelo().getDireccionFiscal());
		// getBinder().addBinding(direccion, "value",
		// getNombreModelo()+".direccionFiscal", null, null, "save", null, null,
		// null, null);
		subTotal.setValue(getModelo().getMontoBase());
		getBinder().addBinding(subTotal, "value",
				getNombreModelo() + ".montoBase", null, null, "save", null,
				null, null, null);
		// razonSocial.agregarListener(getControlador());
		descuento.setValue(getModelo().getMontoDescuento());
		getBinder().addBinding(descuento, "value",
				getNombreModelo() + ".montoDescuento", null, null, "save",
				null, null, null, null);
		porcentajeDescuento.setValue(getModelo().getPorcDescuento());
		getBinder().addBinding(porcentajeDescuento, "value",
				getNombreModelo() + ".porcDescuento", null, null, "save", null,
				null, null, null);
		neto.setValue(getModelo().getNeto());
		if (getModelo().getContrato() != null) {
			List<Contrato> contratos = new ArrayList<Contrato>();
			contratos.add(getModelo().getContrato());
			contrato.setModelo(contratos);
			contrato.setSeleccion(getModelo().getContrato());
		}
		getBinder().addBinding(contrato, "seleccion",
				getNombreModelo() + ".contrato", null, null, "save", null,
				null, null, null);
		// porcentajeImpuesto.setValue(getModelo().getPorcImpuesto());
		// getBinder().addBinding(porcentajeImpuesto, "value",
		// getNombreModelo()+".porcImpuesto", null, null, "save", null, null,
		// null, null);
		total.setValue(getModelo().getMontoTotal());
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

		}
		cedula.setConstraint("no empty : Razon Social no valida");
		//

		if (esFacturaVentaArticulos(factura)) {
			chkVentaArticulos.setChecked(true);
			contrato.setDisabled(true);
		}
	}

	public void suichearContrato(boolean post) {
		cedula.setDisabled(post);
		contrato.setDisabled(!post);
		lblAdelanto.setVisible(post);
		adelanto.setVisible(post);

	}

	private List<CompEncabezado> encabezadosPrimario() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Servicio/Articulo", 340);
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

		titulo = new CompEncabezado("Extra Descripcion", 150);
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
		titulo = new CompEncabezado("Precio", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getPrecio");
		titulo.setMetodoModelo(".precio");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> getEncabezadoContrato() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Nro control", 140);
		// titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrNroDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("fecha", 100);
		titulo.setMetodoBinder("getFechaString");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Productor", 250);
		titulo.setMetodoBinder("getNombreCliente");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;

	}

	private List<CompEncabezado> getEncabezadoCliente() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Cedula", 120);
		titulo.setMetodoBinder("getIdentidadLegal");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre", 450);
		titulo.setMetodoBinder("getNombres");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> getEncabezadoArticulo() {
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

	private ArrayList<Component> cargarLista(boolean desactivarpreciounidad) {
		ArrayList<Component> lista = new ArrayList<Component>();
		servicio = new CompBuscar<IProducto>(getEncabezadoArticulo(), 3);
		Textbox descripcion = new Textbox();
		Doublebox cantidad = new Doublebox();
		Doublebox precioUnidad = new Doublebox();
		Doublebox precio = new Doublebox();

		servicio.setWidth("320px");
		descripcion.setWidth("150px");

		cantidad.setWidth("50px");
		precioUnidad.setWidth("80px");
		precio.setWidth("90px");
		servicio.setAncho(750);
		servicio.setModelo(servicios);

		precio.setDisabled(true);
		precioUnidad.setDisabled(desactivarpreciounidad);

		if (getModelo().getPostServicio() != null) {
			cantidad.setDisabled(true);
			servicio.setDisabled(true);
		}
		// precioUnidad.addEventListener(Events.ON_CHANGE, getControlador());

		cantidad.addEventListener(Events.ON_CHANGE, getControlador());
		precioUnidad.addEventListener(Events.ON_CHANGE, getControlador());
		servicio.setListenerEncontrar(getControlador());

		lista.add(servicio);

		lista.add(cantidad);
		lista.add(descripcion);
		lista.add(precioUnidad);
		lista.add(precio);
		return lista;
	}

	public Textbox getDireccion() {
		return direccion;
	}

	public Doublebox getDescuento() {
		return descuento;
	}

	public void setDescuento(Doublebox descuento) {
		this.descuento = descuento;
	}

	public List<IProducto> getServicios() {
		return servicios;
	}

	public Textbox getSede() {
		return sede;
	}

	public CompBuscar<Cliente> getCedula() {
		return cedula;
	}

	public Label getRazonSocial() {
		return razonSocial;
	}

	public Label getTelefono() {
		return telefono;
	}

	public Doublebox getSubTotal() {
		return subTotal;
	}

	public Doublebox getPorcentajeDescuento() {
		return porcentajeDescuento;
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

	public Doublebox getNeto() {
		return neto;
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		cedula.setDisabled(true);
		contrato.setDisabled(true);
		direccion.setDisabled(true);
		observacion.setDisabled(true);
		interesMora.setDisabled(true);
		aplicarInteres.setDisabled(true);
		chkVentaArticulos.setDisabled(true);
		tipo.setDisabled(true);
		desactivarDetalle();

	}

	public void modoEdicion() {
		cedula.setDisabled(false);
		tipo.setDisabled(false);
		contrato.setDisabled(true);
		direccion.setDisabled(false);
		observacion.setDisabled(false);
		interesMora.setDisabled(true);
		aplicarInteres.setDisabled(false);
		observacion.setDisabled(true);

	}

	public Doublebox[] getBaseimpuesto() {
		return Baseimpuesto;
	}

	public CompBuscar<Contrato> getContrato() {
		return contrato;
	}

	public void refrescarContratos(List<Contrato> contratos) {
		contrato.setModelo(contratos);
	}

	public CompCombobox<String> getTipo() {
		return tipo;
	}

	public Doublebox getAdelanto() {
		return adelanto;
	}

	public Doublebox getInteresMora() {
		return interesMora;
	}

	public Label getTotalInteres() {
		return totalInteres;
	}

	public Checkbox getChkVentaArticulos() {
		return chkVentaArticulos;
	}

	public void setChkVentaArticulos(Checkbox chkVentaArticulos) {
		this.chkVentaArticulos = chkVentaArticulos;
	}

	public void onEvent(Event event) throws Exception {
		super.onEvent(event);

		if (event.getTarget().equals(chkVentaArticulos)) {
			this.tipo.setSelectedIndex(0);
			this.contrato.setDisabled(chkVentaArticulos.isChecked());
		}
	}

	public static boolean esFacturaVentaArticulos(DocumentoFiscal doc) {
		try {
			for (DetalleDocumentoFiscal det : doc.getDetalles()) {
				if (!(det.getServicio() instanceof ArticuloVenta)) {
					return false;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	public void setservicios(List<IProducto> servicios) {
		servicio.setModelo(servicios);
	}

	public CompBuscar<Contrato> removercontrato() {
		CompBuscar<Contrato> a = null;
		/*
		 * //encfactura.removeChild(contrato);
		 * encfactura.addComponente("Contrato  :", a);
		 * //cliente.removeChild(cedula); tipo.setValue("Pre-Servicio");
		 * activarDetalle();
		 */
		contrato = a;
		return contrato;
	}

	public CompBuscar<IProducto> getservicio() {
		return servicio;
	}

	public boolean DesactivarPrecioUnidad(int modo) {
		desactivarpreciounidad = true;
		if (modo != Accion.AGREGAR)
			desactivarpreciounidad = true;
		else
			desactivarpreciounidad = false;
		return desactivarpreciounidad = true;

	}

	@SuppressWarnings("unused")
	public CompCombobox<UnidadAdministrativa> getSedeOrigen() {
		return sedeorigen;
	}

	public Datebox getFechareal() {
		return fechareal;
	}

	public Textbox getObservacion() {
		return observacion;
	}

}