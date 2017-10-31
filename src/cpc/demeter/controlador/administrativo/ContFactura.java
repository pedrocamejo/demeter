package cpc.demeter.controlador.administrativo;

import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.Servicios;
import cpc.demeter.vista.administrativo.UIFactura;
import cpc.modelo.demeter.administrativo.Contrato;
import cpc.modelo.demeter.administrativo.DetalleDocumentoFiscal;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.Impuesto;
import cpc.modelo.demeter.administrativo.ImpuestoDocumentoFiscal;
import cpc.modelo.demeter.administrativo.ReciboDocumentoFiscal;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.basico.InteresMora;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.ministerio.basico.Telefono;
import cpc.modelo.ministerio.basico.TipoProductor;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.negocio.demeter.administrativo.NegocioFactura;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.controlador.ContVentanaMaestroDetalle;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompGrilla;
import cpc.zk.componente.ventanas.CompVentanaEntrada;
import cva.pc.demeter.excepciones.ExcAgregacionInvalida;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;
import cva.pc.demeter.utilidades.Fecha;
import cva.pc.demeter.utilidades.Real;

public class ContFactura extends ContVentanaMaestroDetalle<DocumentoFiscal, DetalleDocumentoFiscal> {

	private NegocioFactura servicio;
	private UIFactura vistaFactura;
	private List<Impuesto> impuestos = null;
	private List<ImpuestoDocumentoFiscal> impuestosFactura = null;
	private Date fechaCierre;
	private AppDemeter app;
	private InteresMora interesMora;
	private DetalleDocumentoFiscal detalleInteres;

	private CompVentanaEntrada vistaNroControl = new CompVentanaEntrada(this);
	private CompVentanaEntrada vistaCodigo = new CompVentanaEntrada(this);

	public ContFactura(int modoOperacion, DocumentoFiscal factura, ContCatalogo<DocumentoFiscal> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido, ExcFiltroExcepcion
	{
		super(modoOperacion);
		this.app = app;
		servicio = NegocioFactura.getInstance();
		fechaCierre = servicio.getFechaCierre();
 
		if (modoOperacion == Accion.AGREGAR) {
			Date fechaservidor = new Date();
			String strfechas = Fecha.obtenerFecha(fechaservidor);
			String strfechac  = Fecha.obtenerFecha(fechaCierre);
			if(!strfechac.equals(strfechas)){
				throw new ExcDatosInvalidos("No se puede Crear, causa: Fecha de cierre no es igual a la fecha actual ");
			}
		} 
		if (modoOperacion == Accion.ANULAR) 
		{
			if (!fechaCierre.equals(factura.getFecha()))
				throw new ExcDatosInvalidos("No se puede anular, causa: Fecha de factura no pertenece al dia a cerrar");
		}

		List<Cliente> clientes = null;
		impuestosFactura = new ArrayList<ImpuestoDocumentoFiscal>();
		List<IProducto> articulos = null;
		
		try {
			clientes = servicio.getClientesProject();
			if (modoAgregar()){
				articulos = null;
			}
			else{
				articulos = servicio.getTodosProductos();
			}
			impuestos = servicio.getTodosImpuestos();
			impuestosFactura = servicio.InicializarImpuestosFactura();
		} 
		catch (ExcFiltroExcepcion e)
		{
			e.printStackTrace();
		}
		
		if (factura != null){
			servicio.setFactura(factura);
		}

		factura = servicio.getFactura();
		if (datoNulo(factura) || modoAgregar()) {
			factura = servicio.facturaNueva();
			servicio.setFactura(factura);
		}
		else {
			factura.setBeneficiario(servicio.getCliente(factura	.getBeneficiario()));
			servicio.actualizarImpuestoFactura(impuestosFactura,factura.getImpuestos());
			factura.setDetalles(servicio.getFactura().getDetalles());
		}

		vistaFactura = new UIFactura("FACTURA (" + Accion.TEXTO[modoOperacion]+ ")", 1200, clientes, articulos, impuestos);
		setear(factura, vistaFactura, llamador, app);
		vistaFactura = (UIFactura) getVista();
		vistaFactura.getSede().setValue(app.getSede().getNombre());
		vistaFactura.desactivar(modoOperacion);
		if (!modoAgregar())
			actualizarImpuestos();

	}

	public void eliminar() throws ExcFiltroExcepcion {

	}

	public void guardar() throws ExcFiltroExcepcion {
		try {
			getVista().actualizarModelo();
			cargarModelo();
			if (vistaFactura.getTipo().getSeleccion() == "Pre-Servicio") {
				getDato().setContrato(null);
			}
			servicio.guardar(getDato());
			mostrarVistaNroControl(servicio.getFactura(), app);
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("Error al generar Factura");
		}
	}

	public void mostrarVistaNroControl(DocumentoFiscal documento, AppDemeter app) {
		vistaNroControl = new CompVentanaEntrada(this);
		vistaNroControl.setTitle("Verificación del Nº de Control");
		vistaNroControl.settextopregunta("N° de Control");
		app.agregarHija(vistaNroControl);
	}

	public void mostrarvistaCodigo(AppDemeter app) {
		vistaCodigo = new CompVentanaEntrada(this);
		Servicios servicio = new Servicios(app);
		vistaCodigo
				.setTitle("introduzca el codigo correspondiente a este numero");
		vistaCodigo.getGbGeneral().getLblTitulo()
				.setValue("introduzca el codigo correspondiente a este numero");
		vistaCodigo.settextopregunta(servicio.generarsemilla().toString());
		try {
			System.out.println(servicio.generarHash(vistaCodigo
					.gettextopregunta().getValue()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		app.agregarHija(vistaCodigo);
	}

	public void anular() {
		try {
			if (servicio.getHijosActivos(getDato().getId()) > 0)
				throw new ExcArgumentoInvalido("La factura tiene documentos activos asociado, no puede anular por esto");
			servicio.anular(getDato());
			refrescarCatalogo();
			vistaFactura.close();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void cargarModelo() throws ExcAgregacionInvalida, ExcFiltroExcepcion {

		if (vistaFactura.getAplicarInteres().isChecked() && vistaFactura.getInteresMora().getValue() != null) {
			vistaFactura.getDetalle().agregar(detalleInteres);
			actualizarTotales();
		}
		if (vistaFactura.getTipo().getSeleccion() == "Pre-Servicio") {
			getDato().setPostServicio(false);
			getDato().setContrato(null);
		} 
		else
		{
			getDato().setPostServicio(true);
		}
		getDato().setDetalles(vistaFactura.getModeloDetalle());
		getDato().setBeneficiario(vistaFactura.getCedula().getValorObjeto());
		
		if (vistaFactura.getDireccion().getValue().trim().length() > 149)
			getDato().setDireccionFiscal(
					vistaFactura.getDireccion().getValue().trim()
							.substring(0, 149));
		else
			getDato().setDireccionFiscal(
					vistaFactura.getDireccion().getValue().trim());
		
		getDato().setMontoTotal(Real.redondeoMoneda(getDato().getMontoTotal()));

		for (ImpuestoDocumentoFiscal imp : impuestosFactura) {
			imp.setDocumento(getDato());
		}	

		getDato().setImpuestos(impuestosFactura);
		if (vistaFactura.getTipo().getSeleccion() == "Post-Servicio")			
			{
			getDato().setMontoSaldo(Real.redondeoMoneda(getDato().getMontoTotal()));
			}
		else
			getDato().setMontoSaldo(
					Real.redondeoMoneda(getDato().getMontoTotal()));
		if (vistaFactura.getCreditoOContado().getSelectedItem().getValue()=="Credito")
			getDato().setCredito(true);
		else 	getDato().setCredito(false);
	}

	@SuppressWarnings("unchecked")
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TipoFormaPago tipoFormaPago;
		if (vistaFactura.getTipo().getSelectedItem() == null) {
			throw new WrongValueException(vistaFactura.getTipo(), "Tipo no valido");
		}
		CompBuscar<IProducto> laborDetalle;
		Doublebox cantidad;
		Doublebox precio;
		List<Row> filas = vistaFactura.getDetalle().getFilas().getChildren();
		int i = 0;

		if (vistaFactura.getTipo().getSeleccion() == "Post-Servicio") {
			if (vistaFactura.getContrato().getSeleccion() == null) throw new WrongValueException(vistaFactura.getContrato(), "Para este tipo de factura el Contrato debe ser indicado");
		}
		for (Row item : filas) {
			i++;
			laborDetalle = (CompBuscar<IProducto>) item.getChildren().get(0);
			if (laborDetalle.getSeleccion() == null){
				throw new WrongValueException(laborDetalle,	"No indico el servicio a Facturar");
			}
			cantidad = (Doublebox) item.getChildren().get(1);
			precio = (Doublebox) item.getChildren().get(4);
			if (cantidad.getValue() <= 0){
				throw new WrongValueException(cantidad, "Cantidad no valida");
			}
			if (precio.getValue() <= 0) {
				throw new WrongValueException(precio, "Monto no valido");
			}
			if (laborDetalle.getSeleccion() == null){
				throw new WrongValueException(laborDetalle,	"Debe seleccionar servicio");
			}
		}

		Integer maximo = getMaximoRenglon();
		if (vistaFactura.getAplicarInteres().isChecked()) {
			maximo--;
		}

		if (i > maximo) {
			throw new WrongValueException(vistaFactura.getDetalle(),"Factura no puede tener mas de " + maximo + "renglones");
		}

		if (vistaFactura.getSubTotal().getValue() <= 0){
			throw new WrongValueException(vistaFactura.getSubTotal(),"Monto no valido");
		}
		try {
			servicio.getExpedienteAdministrativo(getDato().getBeneficiario());
		}
		catch (ExcFiltroExcepcion e) {
			throw new WrongValueException(vistaFactura.getCedula(), "Productor sin expediente Administrativo");
		}
		
		if (vistaFactura.getCreditoOContado().getSelectedItem() == null) {
			throw new WrongValueException(vistaFactura.getCreditoOContado(), "Debe indicar si es credito o contado");
		}

	}

	public boolean nroControlValido(DocumentoFiscal documento,
			Textbox txtNroControl) throws InterruptedException {
		try {
			if (txtNroControl.getText().trim().isEmpty()) {
				app.mostrarError("El nro de control no puede estar vacio");
				vistaNroControl.getEntrada().setFocus(true);
				return false;
			} else if (documento.getNroDocumento().compareTo(
					Integer.valueOf(txtNroControl.getText())) != 0) {
				Messagebox
						.show("El Nº de Control ingresado no coincide con el de la factura",
								"Error", Messagebox.OK, Messagebox.ERROR);
				return false;
			}
			return true;
		} catch (NumberFormatException e) {
			// TODO: handle exception
			Messagebox.show("El texto debe estar en formato numérico", "Error",
					Messagebox.OK, Messagebox.ERROR);
			return false;
		}
	}

	public boolean codigoValido(Textbox txtNroControl, Label codigo)
			throws InterruptedException {
		Servicios servicio = new Servicios(app);
		try {
			if (txtNroControl.getText().trim().isEmpty()) {
				app.mostrarError("El nro de control no puede estar vacio");
				vistaNroControl.getEntrada().setFocus(true);
				return false;
			} else if (!txtNroControl.getText().equals(
					servicio.generarHash(codigo.getValue()))) {
				Messagebox.show("El codigo no coincide", "Error",
						Messagebox.OK, Messagebox.ERROR);
				return false;
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			Messagebox.show("El texto debe estar en formato numérico", "Error",
					Messagebox.OK, Messagebox.ERROR);
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
	
		// mostrarvistaCodigo( app);
		if (event.getTarget() == vistaNroControl.getAceptar())
		{
			if (nroControlValido(servicio.getFactura(),vistaNroControl.getEntrada()))
			{
				vistaNroControl.close();
				imprimir(servicio.getFactura(), app);
			}
		}

		else if (event.getTarget() == vistaCodigo.getAceptar()) 
		{
			if (codigoValido(vistaCodigo.getEntrada(),vistaCodigo.gettextopregunta()))
			{
				vistaFactura.activarDescuento();
				vistaCodigo.close();
				System.out.println("ok");
			}
		}

		else if (event.getTarget() == vistaCodigo.getCancelar()) 
		{
			vistaFactura.getChkDescuento().setChecked(false);
		}

		else if (event.getTarget() == getVista().getAceptar())
		{
			procesarCRUD(event);

		}
		else if (event.getName().equals(Events.ON_SELECT)) 
		{
			if (vistaFactura.getCreditoOContado().getSelectedItem()==null){
				String[] tipos = { "Pre-Servicio", "Post-Servicio" };
				vistaFactura.getTipo().setModelo(Arrays.asList(tipos));
				throw new WrongValueException(vistaFactura.getCreditoOContado(),
						"Debe indicar si es credito o contado");
			}
			seleccionarTipo(vistaFactura.getTipo().getSeleccion());
		} 
		else if (event.getName().equals(CompBuscar.ON_SELECCIONO))
		{
			if (event.getTarget() == vistaFactura.getCedula())
			{
				Cliente cliente1 = vistaFactura.getCedula().getSeleccion();
				cliente1=servicio.getClienteproject(cliente1);
				vistaFactura.getCedula().setSeleccion(cliente1 );
				actualizarDatos();
			}
			else if (event.getTarget() == vistaFactura.getContrato()) 
			{
				actualizarContrato();
				actualizarTotalInteres();
			}
			else
			{  	
				actualizarServicio((CompBuscar<IProducto>) event.getTarget());
				actualizarTotalInteres();
			}
			
		}
		else if (event.getName().equals(Events.ON_CHANGE))
		{
			if (event.getTarget() == vistaFactura.getInteresMora())
			{
				actualizarTotalInteres();
			}
			if (event.getTarget() == vistaFactura.getPorcentajeDescuento())
			{
				actualizarTotales();
			}
			else
			{
				actualizarPrecio((Doublebox) event.getTarget());
				actualizarTotalInteres();
			}

		} 
		else if (event.getName().equals(CompGrilla.ON_ELIMINA_FILA))
		{
			eliminadato((Row) event.getData());
			actualizarTotalInteres();
		}
		else if (event.getName().equals(Events.ON_CHECK)) 
		{
			System.out.println(event.getName());
			System.out.println(event.getData());
			System.out.println(event.getTarget());
			System.out.println(vistaFactura.getAplicarInteres());
			System.out.println(vistaFactura.getChkDescuento());
			if (event.getTarget().equals(vistaFactura.getAplicarInteres())) 
			{
				System.out.println(event.getTarget());
				System.out.println(vistaFactura.getAplicarInteres());
				activarInteres();
			}
			if(event.getTarget().getParent().equals(vistaFactura.getCreditoOContado())) {
				actualizarTotales();
			}
			if (event.getTarget().equals(vistaFactura.getChkDescuento()))
			{
				mostrarvistaCodigo(app);
			}

		}else if (event.getName().equals(Events.ON_CLICK)){
			if (event.getTarget().equals(vistaFactura.getDetalle().getBoton())){
				if (vistaFactura.getCreditoOContado().getSelectedItem()==null){
					throw new WrongValueException(vistaFactura.getCreditoOContado(),
							"Debe indicar si es credito o contado");
				}
			}
		}
		
		

	}

	public void actualizarTotalInteres() {
		Double porcentaje = vistaFactura.getInteresMora().getValue();
		if (porcentaje != null) {
			try {
				if (interesMora == null)
					interesMora = servicio.getInteresMora(1);
				if (detalleInteres == null) {
					detalleInteres = new DetalleDocumentoFiscal();
					detalleInteres.setServicio(interesMora);
				}
				Double valor = vistaFactura.getSubTotal().getValue();
				valor = (valor * porcentaje / 100.00);
				Impuesto impuesto = interesMora.getImpuesto();
				interesMora.setPrecio(valor);
				detalleInteres.setCantidad(1);
				detalleInteres.setComplementoDescripcion(porcentaje + "%");
				detalleInteres.setAlicuota(impuesto);
				detalleInteres.setPrecioUnitario(valor);
				detalleInteres.setAlicuota(impuesto);
				vistaFactura.getTotalInteres().setValue(
						" = Bs "
								+ new DecimalFormat("##,###,##0.00")
										.format(valor)
								+ " + IVA "
								+ new DecimalFormat("##,###,##0.00")
										.format(interesMora.getPrecioNeto()));

			} catch (ExcFiltroExcepcion e) {
				e.printStackTrace();
			}
		}
	}

	public void activarInteres() {
		vistaFactura.getInteresMora().setDisabled(
				!vistaFactura.getAplicarInteres().isChecked());
		if (!vistaFactura.getAplicarInteres().isChecked()) {
			vistaFactura.getTotalInteres().setValue("");
			vistaFactura.getInteresMora().setValue(0.0);
		} else
			actualizarTotalInteres();
	}

	public void agregarLineaInteres() {

	}

	public void actualizarDatos() throws ExcFiltroExcepcion {
		if (vistaFactura.getTipo().getSelectedItem() == null)
			throw new ExcFiltroExcepcion("No ha definido, tipo de factura");
		Cliente cliente = vistaFactura.getCedula().getValorObjeto();
		if (cliente != null) {
			cliente = servicio.getCliente(cliente);
			try {
				if (cliente.getTelefonos().size() >= 0) {
					Telefono tele = cliente.getTelefonos().get(0);
					vistaFactura.getTelefono().setValue(
							"(" + tele.getCodigoArea().getCodigoArea() + ")"
									+ tele.getNumero());
				}
			} catch (IndexOutOfBoundsException e) {
			}
			vistaFactura.getRazonSocial().setValue(cliente.getNombres());
			vistaFactura.getDireccion().setValue(cliente.getDireccion());
			vistaFactura.getDetalle().clear();
		}
	}

	public void seleccionarTipo(String tipo) throws ExcFiltroExcepcion {
		if (tipo == "Pre-Servicio") { // Pre
			vistaFactura.activarDetalle();
			vistaFactura.getDetalle().clear();

			if (modoAgregar()){
			vistaFactura.setservicios(servicio.getProductosActivos());}
			else  {
				vistaFactura.setservicios(servicio.getProductos());}
			vistaFactura.suichearContrato(false);
			actualizarContrato();

		} else {
			vistaFactura.getDetalle().clear();
			vistaFactura.setservicios(servicio.getProductos());
			vistaFactura.desactivarDetalle();
			vistaFactura.suichearContrato(true);
			vistaFactura.refrescarContratos(servicio.getContratosPost());
		}

	}

	private void actualizarContrato() throws ExcFiltroExcepcion {
		Contrato contrato = vistaFactura.getContrato().getValorObjeto();
		if (contrato != null) {

			boolean post = false;
			try {
				if (vistaFactura.getTipo().getSeleccion() == "Pre-Servicio") {
					post = false;
					vistaFactura.getContrato().setAttribute("Contrato", null);
					vistaFactura.getContrato().setSeleccion(null);
					vistaFactura.getContrato().setValue(null);
					
					actualizarTotales();

				} else {
					post = true;
					Cliente cliente = contrato.getPagador();
					vistaFactura.getCedula().setSeleccion(cliente);
					cliente = servicio.getCliente(cliente);
					List<DetalleDocumentoFiscal> detalles = null;
					try {
						detalles = servicio.getDetalleContrato(contrato, post);
						if (cliente.getTelefonos().size() >= 0) {
							Telefono tele = cliente.getTelefonos().get(0);
							vistaFactura.getTelefono().setValue(
									"(" + tele.getCodigoArea().getCodigoArea()
											+ ")" + tele.getNumero());
						}
					} catch (ExcFiltroExcepcion e) {
						e.printStackTrace();
						throw e;
					} catch (IndexOutOfBoundsException e) {

					}

					System.out.println("Buscando Cliente");
					vistaFactura.getRazonSocial()
							.setValue(cliente.getNombres());
					vistaFactura.getDireccion()
							.setValue(cliente.getDireccion());
					vistaFactura.getDetalle().clear();
					if (detalles != null) {
						for (DetalleDocumentoFiscal item : detalles) {
							if (item.getCantidad() != 0)
								vistaFactura.getDetalle().agregar(item);
						}
					}
					actualizarTotales();
					vistaFactura.getDetalle().setDisable(true);
					try {
						servicio.getExpedienteAdministrativo(cliente);
					} catch (ExcFiltroExcepcion e) {
						app.mostrarError("Productor sin expediente Administrativo por favor generarlo");
					}

				}

			} catch (Exception e) {
				throw new ExcFiltroExcepcion("No se ha seleccionado el tipo"
						+ e);
			}
		}
	}

	public void actualizarServicio(CompBuscar<IProducto> componente) throws ExcFiltroExcepcion
	{
		IProducto producto = null;
		servicio = NegocioFactura.getInstance();
		Row registro = (Row) componente.getParent();
	
		try 
		{
		
			producto = componente.getSeleccion();
			producto = servicio.enriqueserProducto(producto);
		}
		catch (NullPointerException e)
		{
			throw new ExcFiltroExcepcion("Problemas con el Item seleccionado");
		}
		TipoProductor tipo = null;
		try {
			tipo = vistaFactura.getCedula().getSeleccion().getTipo();
		} catch (NullPointerException e) {
			throw new WrongValueException(vistaFactura.getCedula(),
					"Debe seleccionar productor previo");
		}
		try {
			Double precio = producto.getPrecioBase(tipo);
			if (producto instanceof ArticuloVenta) {
				try {
					precio = ((ArticuloVenta) componente.getSeleccion())
							.getPrecio();
					((Textbox) registro.getChildren().get(2))
							.setValue(((ArticuloVenta) componente
									.getSeleccion()).getStrUnidadMedida());
				} catch (Exception e) {
					precio = new Double(0);
					e.printStackTrace();
					throw new ExcFiltroExcepcion(
							"El Item seleccionado no tiene un precio valido... Se espcificara 0 como valor por defecto");
				}
			}
			try {
				((Doublebox) registro.getChildren().get(3)).setValue(precio);
				double cantidad = ((Doublebox) registro.getChildren().get(1))
						.getValue();
				((Doublebox) registro.getChildren().get(4)).setValue(Math
						.round(precio * cantidad * 10000.00) / 10000.00);
				((DetalleDocumentoFiscal) registro.getAttribute("dato"))
						.setServicio(producto);
				((DetalleDocumentoFiscal) registro.getAttribute("dato"))
						.setAlicuota(producto.getImpuesto());
				actualizarTotales();
			} catch (Exception e) {
				e.printStackTrace();
				throw new ExcFiltroExcepcion(
						"El Item seleccionado no tiene un precio valido...Consulte el precio en el Maestro del Articulo de Ventas");

			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcFiltroExcepcion("Problemas con el Item Seleccionado. "
					+ e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void actualizarTotales() throws ExcFiltroExcepcion {
		adecuarIva();
		double bruto = 0;
		double neto;
		double total;
		double valor;
		double desc = 0;
		double acuDesc = 0;
		List<Row> filas = vistaFactura.getDetalle().getFilas().getChildren();
		servicio.InicializarImpuestosFactura(impuestosFactura);
		acuDesc = 0;
		for (Row item : filas) {
			valor = ((Doublebox) item.getChildren().get(4)).getValue();
			desc = new Real(valor * vistaFactura.getPorcentajeDescuento().getValue() / 10000.00) .getValor();
			servicio.actualizarImpuestoFactura(impuestosFactura,
					((DetalleDocumentoFiscal) item.getAttribute("dato"))
							.getAlicuota(), valor - desc);
			bruto += valor;
			acuDesc += desc;
		}
		neto = bruto - acuDesc;
		double impuestoTotal = servicio.getTotalImpuestoFactura(impuestosFactura);
		total = Real.redondeoMoneda(neto) + Real.redondeoMoneda(impuestoTotal);
		vistaFactura.getSubTotal().setValue(Real.redondeoMoneda(bruto));
		vistaFactura.getDescuento().setValue(acuDesc);
		for (int i = 0; i < impuestosFactura.size(); i++) {
			vistaFactura.getBaseimpuesto()[i].setValue(Real.redondeoMoneda(impuestosFactura.get(i).getBase()));
			vistaFactura.getImpuesto()[i].setValue(Real.redondeoMoneda(impuestosFactura.get(i).getMonto()));
		}
		vistaFactura.getNeto().setValue(Real.redondeoMoneda(neto));
		vistaFactura.getTotal().setValue(total);
	}

	public void actualizarImpuestos() throws ExcFiltroExcepcion {
		for (int i = 0; i < impuestosFactura.size(); i++) {
			vistaFactura.getBaseimpuesto()[i].setValue(Real.redondeoMoneda(impuestosFactura.get(i).getBase()));
			vistaFactura.getImpuesto()[i].setValue(Real.redondeoMoneda(impuestosFactura.get(i).getMonto()));
		}
	}

	@SuppressWarnings("unchecked")
	public void actualizarPrecio(Doublebox componente)
			throws WrongValueException {
		Row registro = null;
		try {
			registro = (Row) componente.getParent();
			double precio = ((Doublebox) registro.getChildren().get(3))
					.getValue();
			((Doublebox) registro.getChildren().get(4)).setValue(componente
					.getValue() * precio);
			actualizarTotales();
		} catch (Exception e) {
			if (registro != null)
				throw new WrongValueException(((CompBuscar<IProducto>) registro
						.getChildren().get(0)), "Problemas con el servicio");
			else
				app.mostrarError("Problemas con producto seleccionado");
		}
	}

	public void eliminadato(Row fila) throws ExcFiltroExcepcion {
		actualizarTotales();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void imprimir(DocumentoFiscal documento, IZkAplicacion app)
			throws ExcFiltroExcepcion {
		try {
			NegocioFactura negocio = NegocioFactura.getInstance();
			List<ImpuestoDocumentoFiscal> impuestosFactura = negocio
					.InicializarImpuestosFactura();
			negocio.setFactura(documento);
			negocio.getFactura().setBeneficiario(
					negocio.getCliente(documento.getBeneficiario()));
			negocio.actualizarImpuestoFactura(impuestosFactura, negocio
					.getFactura().getImpuestos());

			System.out.println("prueba para crear ventana "
					+ negocio.getFactura().getStrNroDocumento());
			System.out.println("prueba para crear ventana 2"
					+ negocio.getFactura().getNroDocumento());

			HashMap parametros = new HashMap();
			JRDataSource ds = new JRBeanCollectionDataSource(negocio
					.getFactura().getDetalles());
			System.out.println("direccion fiscal "+ negocio.getFactura().getDireccionFiscal());
			System.out.println("direccion beneficiario "+ negocio.getFactura().getDireccionBeneficiario());
			System.out.println(documento.getTotalImponible());
			System.out.println(documento.getTotalExcento());
			System.out.println(documento.getTotalImpuesto());
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();
			parametros.put("NroFacturaNota", "");
			reporte.setSrc("reportes/factura.jasper");
			reporte.setParameters(parametros);
			reporte.setDatasource(ds);
			reporte.setType("pdf");
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	private Integer getMaximoRenglon() {
		return (Integer) SpringUtil.getBean("lineasFactura");
	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub

	}

	public EventListener getDetalleRecibo() {
		// TODO Auto-generated method stub
		return new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				Object obj = vistaFactura.getListRecibos().getSelectedItem().getValue();
				if(obj.getClass() == ReciboDocumentoFiscal.class)
				{
					ReciboDocumentoFiscal reciboDocumentoFiscal = (ReciboDocumentoFiscal) obj;
					ContFactura controlador = (ContFactura) vistaFactura.getControlador();
					ContRecibo contRecibo = new ContRecibo(Accion.CONSULTAR, reciboDocumentoFiscal.getRecibo(),controlador,controlador.app);
				}
			}
		};
	};
	
	public void adecuarIva() {
		if  (vistaFactura.getCreditoOContado().getSelectedItem().getValue().equals("Contado Electronico")) {
			List<Row> filas = vistaFactura.getDetalle().getFilas().getChildren();
			Double neto = vistaFactura.getMontoNetoIVA();
			for (Row item : filas) {
				DetalleDocumentoFiscal detalle= ((DetalleDocumentoFiscal) item.getAttribute("dato"));
				if(detalle.getAlicuota() != null ){
					if (!detalle.getAlicuota().equals(servicio.getImpuestoExento())){
						if(neto < 2000000.00){
							detalle.setAlicuota(servicio.getIva9());
						}
						else{
							detalle.setAlicuota(servicio.getIva7());
						}
					}
				}
			}
		}
		else{
			List<Row> filas = vistaFactura.getDetalle().getFilas().getChildren();
			for (Row item : filas) {
				DetalleDocumentoFiscal detalle= ((DetalleDocumentoFiscal) item.getAttribute("dato"));
				if(detalle.getAlicuota() != null ){
					if (detalle.getAlicuota()!=null){
						if (!detalle.getAlicuota().equals(servicio.getImpuestoExento())){
							detalle.setAlicuota(servicio.getIva());
						}
					}
				}
			}
		}
	}
}
