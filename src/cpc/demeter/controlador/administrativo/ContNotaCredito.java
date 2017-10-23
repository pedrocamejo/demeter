package cpc.demeter.controlador.administrativo;

import java.util.ArrayList;
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
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.administrativo.UINotaCredito;
import cpc.modelo.demeter.administrativo.DetalleDocumentoFiscal;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.Impuesto;
import cpc.modelo.demeter.administrativo.ImpuestoDocumentoFiscal;
import cpc.modelo.demeter.administrativo.NotaCredito;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.basico.Producto;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.ministerio.basico.TipoProductor;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.negocio.demeter.administrativo.NegocioFactura;
import cpc.negocio.demeter.administrativo.NegocioNotaCredito;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.controlador.ContVentanaMaestroDetalle;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompGrilla;
import cpc.zk.componente.ventanas.CompVentanaEntrada;
import cva.pc.demeter.excepciones.ExcAgregacionInvalida;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;
import cva.pc.demeter.utilidades.Fecha;
import cva.pc.demeter.utilidades.Real;

@SuppressWarnings("serial")
public class ContNotaCredito extends
		ContVentanaMaestroDetalle<NotaCredito, DetalleDocumentoFiscal> {

	private NegocioNotaCredito servicio;
	private UINotaCredito vistaNota;
	private List<Impuesto> impuestos = null;
	private List<ImpuestoDocumentoFiscal> impuestosFactura = null;

	private Date fechaCierre;
	private AppDemeter app;

	private CompVentanaEntrada vistaNroControl = new CompVentanaEntrada(this);

	public ContNotaCredito(int modoOperacion, NotaCredito nota,
			ContCatalogo<NotaCredito> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido,
			ExcFiltroExcepcion {
		super(modoOperacion);
		this.app = app;
		servicio = NegocioNotaCredito.getInstance();
		fechaCierre = servicio.getFechaCierre();
		
		if (modoOperacion == Accion.AGREGAR) {
			Date fechaservidor = new Date();
			String strfechas = Fecha.obtenerFecha(fechaservidor);
			String strfechac  = Fecha.obtenerFecha(fechaCierre);
			boolean condicion = strfechac.equals(strfechas);
				if (!condicion)
					throw new ExcDatosInvalidos(
							"No se puede Crear, causa: Fecha de cierre no pertenece al dia ");
			}
		if (modoOperacion == Accion.ANULAR
				&& !fechaCierre.equals(nota.getFecha()))
			throw new ExcDatosInvalidos(
					"No se puede anular, causa: Fecha de Nota de Credito no pertenece al dia a cerrar");
		List<DocumentoFiscal> facturas = null;

		impuestosFactura = new ArrayList<ImpuestoDocumentoFiscal>();
		List<IProducto> articulos = null;
		try {
			facturas = servicio.getFacturasActivasProject();
			articulos = servicio.getProductos();
			impuestos = servicio.getTodosImpuestos();
			impuestosFactura = servicio.InicializarImpuestosNota();
		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
		}

		if (datoNulo(nota) || modoAgregar()) {
			nota = servicio.NotaNueva();
		}
		servicio.setNota(nota);
		if (!modoAgregar()) {
			servicio.actualizarImpuestoFactura(impuestosFactura, servicio
					.getNota().getImpuestos());
		}
		setear(servicio.getNota(), new UINotaCredito("NOTA DE CREDITO ("
				+ Accion.TEXTO[modoOperacion] + ")", 850, facturas, impuestos,
				articulos), llamador, this.app);
		vistaNota = (UINotaCredito) getVista();
		vistaNota.getSede().setValue(app.getSede().getNombre());
		vistaNota.desactivar(modoOperacion);
		if (!modoAgregar())
			actualizarImpuestos();
	}

	public void eliminar() throws ExcFiltroExcepcion {

	}

	public void guardar() throws ExcFiltroExcepcion {
		try {
			getVista().actualizarModelo();
			cargarModelo();
			servicio.guardar(getDato());
			mostrarVistaNroControl(servicio.getNota(), app);
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void mostrarVistaNroControl(DocumentoFiscal documento, AppDemeter app) {
		vistaNroControl = new CompVentanaEntrada(this);
		vistaNroControl.setTitle("Verificación del Nº de Control");
		vistaNroControl.settextopregunta("N° de Control");
		app.agregarHija(vistaNroControl);
	}

	public void anular() {
		try {
			servicio.anular(getDato());
			refrescarCatalogo();
			vistaNota.close();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void cargarModelo() throws ExcAgregacionInvalida {
		List<DetalleDocumentoFiscal> detalles = vistaNota.getModeloDetalle();
		int i = 0;

		@SuppressWarnings("unchecked")
		List<Row> filas = vistaNota.getDetalle().getFilas().getChildren();
		for (Row row : filas) {
			@SuppressWarnings("unchecked")
			Impuesto aux = ((CompCombobox<Impuesto>) row.getChildren().get(4))
					.getSeleccion();

			detalles.get(i).setAlicuota(aux);
			i++;
		}

		getDato().setDetalles(detalles);
		// getDato().setBeneficiario(vistaNota.getRazonSocial().getValorObjeto());
		// getDato().setDireccionBeneficiario(vistaNota.getDireccion().getSeleccion());
		getDato().setImpuestos(impuestosFactura);
		getDato()
				.setDireccionFiscal(vistaNota.getModelo().getDireccionFiscal());
		getDato().setMontoTotal(Real.redondeoMoneda(getDato().getMontoTotal()));
		for (ImpuestoDocumentoFiscal imp : impuestosFactura) {
			imp.setBase(imp.getBase());
			imp.setMonto(imp.getMonto());
			imp.setDocumento(getDato());
		}

	}

	@SuppressWarnings("unchecked")
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TipoFormaPago tipoFormaPago;
		CompBuscar<Producto> servicio;
		Doublebox cantidad;
		Doublebox precio;
		Textbox descripcion;
		List<Row> filas = vistaNota.getDetalle().getFilas().getChildren();
		for (Row item : filas) {
			servicio = (CompBuscar<Producto>) item.getChildren().get(0);
			descripcion = (Textbox) item.getChildren().get(2);
			if (servicio.getSeleccion() == null)
				throw new WrongValueException(servicio,
						"No indico el servicio a Facturar");
			cantidad = (Doublebox) item.getChildren().get(1);
			precio = (Doublebox) item.getChildren().get(5);
			if (cantidad.getValue() <= 0)
				throw new WrongValueException(cantidad, "Cantidad no valida");
			if (descripcion.getValue().length() < 5
					|| descripcion.getValue().length() >= 30)
				throw new WrongValueException(descripcion,
						"Descripcion no valida debe poseer de 5 a 30 caracteres");
			if (precio.getValue() <= 0)
				throw new WrongValueException(precio, "Monto no valido");
			if (servicio.getSeleccion() == null)
				throw new WrongValueException(servicio,
						"Debe seleccionar servicio");
				}
		/*
		 * if (vistaNota.getDireccion().getSeleccion() == null) throw new
		 * WrongValueException(vistaNota.getDireccion(), "Direccion no valida");
		 */
		if (vistaNota.getSubTotal().getValue() <= 0)
			throw new WrongValueException(vistaNota.getSubTotal(),
					"Monto no valido");
		getVista().actualizarModelo();
		try {
			cargarModelo();
		} catch (ExcAgregacionInvalida e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (!this.servicio.ValidadTotales(getDato()))
			throw new WrongValueException(vistaNota.getTotal(),
					"los totales en notas de credito superan el monto de la factura");
	}

	public boolean nroControlValido(DocumentoFiscal documento,
			Textbox txtNroControl) throws InterruptedException {
		try {
			if (txtNroControl.getText().trim().isEmpty()) {
				app.mostrarError("El nro de control no puede estar vacío");
				vistaNroControl.getEntrada().setFocus(true);
				return false;
			} else if (documento.getNroDocumento().compareTo(
					Integer.valueOf(txtNroControl.getText())) != 0) {
				Messagebox
						.show("El Nº de Control ingresado no coincide con el de la Nota de Crédito",
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

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		if (event.getTarget() == vistaNroControl.getAceptar()) {
			if (nroControlValido(servicio.getNota(),
					vistaNroControl.getEntrada())) {
				vistaNroControl.close();
				imprimir(servicio.getNota(), app);
			}
		} else {
			try {
				if (event.getTarget() == getVista().getAceptar())

					procesarCRUD(event);

				else if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {
					if (event.getTarget() == vistaNota.getFactura()) 
					{
						DocumentoFiscal docu = vistaNota.getFactura().getSeleccion();
						docu =	servicio.getFacturaProject(docu);
						vistaNota.getFactura().setSeleccion(docu);
						actualizarDatos();
						DesactivarDetalle();
					} 
					else
					{
						System.out.println(event.getTarget());
						actualizarServicio((CompBuscar<IProducto>) event.getTarget());
					}
				} 
				else if (event.getName().equals(Events.ON_CHANGE)) {
					List<Row> filas = vistaNota.getDetalle().getFilas()
							.getChildren();
					for (Row row : filas) {
						if (event.getTarget() == ((Doublebox) row.getChildren()
								.get(1)))
							actualizarPrecio((Doublebox) event.getTarget());
					}

				}

				else if (event.getName().equals(CompGrilla.ON_ELIMINA_FILA))
					eliminadato((Row) event.getData());
			} catch (Exception e) {
				e.printStackTrace();
				app.mostrarError(e.getMessage());
			}
		}
	}

	public void actualizarDatos() {
		DocumentoFiscal factura = vistaNota.getFactura().getValorObjeto();
		Cliente cliente = servicio.getCliente(factura.getBeneficiario());
		// List<Docum> detallesFactura =
		// servicio.getInstance().getServiciosFactura(factura.getId());
		vistaNota.getRazonSocial().setValue(cliente.getNombres());
		vistaNota.getDireccion().setValue(
				factura.getDireccionBeneficiario().toString());
		vistaNota.getCedula().setValue(cliente.getIdentidadLegal());
		List<IProducto> serviciosfactura = servicio.getServiciosFactura(factura
				.getId());
		vistaNota.desactivarnota(getModoOperacion());
		if (serviciosfactura.size() == 0) {
			try {
				serviciosfactura = servicio.getProductos();
			} catch (ExcFiltroExcepcion e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		vistaNota.actualizarModeloServicios(serviciosfactura);
		vistaNota.getDetalle().clear();
	}

	@SuppressWarnings("unchecked")
	public void actualizarServicio(CompBuscar<IProducto> componente)
			throws ExcFiltroExcepcion {
		IProducto producto = null;
		servicio = NegocioNotaCredito.getInstance();
		Row registro = (Row) componente.getParent();
		try {
			producto = componente.getSeleccion();
			producto = servicio.enriqueserProducto(producto);
		} catch (NullPointerException e) {
			throw new ExcFiltroExcepcion(
					"Problemas con el producto seleccionado");
		}
		TipoProductor tipo = null;
		try {
			tipo = vistaNota.getFactura().getSeleccion().getBeneficiario()
					.getTipo();
		} catch (NullPointerException e) {
			throw new ExcFiltroExcepcion("No ha seleccionado el Productor");
		}
		try {
			NegocioFactura nf = NegocioFactura.getInstance();
			nf.setFactura(vistaNota.getFactura().getSeleccion());
			List<DetalleDocumentoFiscal> listaDetalle = nf.getFactura().getDetalles();
			Double precio = new Double(0);
			Impuesto impuesto = new Impuesto();
			for (DetalleDocumentoFiscal detalleDocumentoFiscal : listaDetalle) {
					if (producto.equals(detalleDocumentoFiscal.getServicio())) {
						precio = detalleDocumentoFiscal.getPrecioUnitario();
						impuesto = detalleDocumentoFiscal.getAlicuota();
						if (producto instanceof ArticuloVenta) {
							try {
								((Textbox) registro.getChildren().get(2)).setValue(((ArticuloVenta) componente.getSeleccion()).getStrUnidadMedida());
							} catch (Exception e) {
								precio = new Double(0);
								e.printStackTrace();
								throw new ExcFiltroExcepcion("El Item seleccionado no tiene un precio valido... Se espcificara 0 como valor por defecto");
							}
						}
						try {
							((Doublebox) registro.getChildren().get(3)).setValue(precio);
							double cantidad = ((Doublebox) registro	.getChildren().get(1)).getValue();
							((Doublebox) registro.getChildren().get(5)).setValue(precio * cantidad);
							((CompCombobox<Impuesto>) registro.getChildren().get(4)).setSeleccion(impuesto);
						} catch (Exception e) {
							throw new ExcFiltroExcepcion("Problemas con el servicio");
						}

						if (vistaNota.getDesactivarNota())
						{
							try {
								System.out.println(detalleDocumentoFiscal.getAlicuota());
								((DetalleDocumentoFiscal) registro
										.getAttribute("dato"))
										.setAlicuota(detalleDocumentoFiscal
										.getAlicuota());

								actualizarTotales();
								DesactivarDetalle();
							} catch (Exception e) {
								throw new ExcFiltroExcepcion(
										"Problemas con el servicio");
							}
						}

					}

				}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcFiltroExcepcion("Problemas con el Item Seleccionado. "
					+ e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void actualizarTotales() throws ExcFiltroExcepcion {
		double bruto = 0;
		double total;
		double valor;
		double desc = 0;
		List<Row> filas = vistaNota.getDetalle().getFilas().getChildren();
		servicio.InicializarImpuestosFactura(impuestosFactura);
		for (Row item : filas) {
			valor = ((Doublebox) item.getChildren().get(5)).getValue();
			Impuesto aux = ((CompCombobox<Impuesto>) item.getChildren().get(4))
					.getSeleccion();
			Double precio = ((Doublebox) item.getChildren().get(3)).getValue();
			if (aux == null) {
				precio = null;
				((Doublebox) item.getChildren().get(3)).setValue(precio);
				((Doublebox) item.getChildren().get(1)).setValue(precio);
				throw new WrongValueException(vistaNota.getDetalle(),
						"Indique el impuesto antes de colocar la cantidad");

			}

			if (precio == null || precio == 0) {
				precio = null;
				((Doublebox) item.getChildren().get(3)).setValue(precio);
				((Doublebox) item.getChildren().get(1)).setValue(precio);
				throw new WrongValueException(vistaNota.getDetalle(),
						"Indique el precio unitario antes de colocar la cantidad");
			}
			servicio.actualizarImpuestoFactura(impuestosFactura, aux, valor
					- desc);
			bruto += valor;
		}
		double impuestoTotal = servicio
				.getTotalImpuestoFactura(impuestosFactura);
		total = Real.redondeoMoneda(bruto) + Real.redondeoMoneda(impuestoTotal);
		vistaNota.getSubTotal().setValue(Real.redondeoMoneda(bruto));
		for (int i = 0; i < impuestosFactura.size(); i++) {
			vistaNota.getBaseimpuesto()[i].setValue(Real.redondeoMoneda(impuestosFactura
					.get(i).getBase()));
			vistaNota.getImpuesto()[i].setValue(Real.redondeoMoneda(impuestosFactura
					.get(i).getMonto()));
		}
		vistaNota.getTotal().setValue(total);
	}

	public void actualizarImpuestos() throws ExcFiltroExcepcion {
		for (int i = 0; i < impuestosFactura.size(); i++) {
			vistaNota.getBaseimpuesto()[i].setValue(Real.redondeoMoneda(impuestosFactura.get(i)
					.getBase()));
			vistaNota.getImpuesto()[i].setValue(Real.redondeoMoneda(impuestosFactura.get(i)
					.getMonto()));
		}
	}

	public void actualizarPrecio(Doublebox componente)
			throws ExcFiltroExcepcion {
		Row registro = null;
		try {
			registro = (Row) componente.getParent();
			double precio = ((Doublebox) registro.getChildren().get(3))
					.getValue();
			((Doublebox) registro.getChildren().get(5)).setValue(componente
					.getValue() * precio);

		} catch (Exception e) {
			app.mostrarError("Problemas con producto seleccionado 80");
			e.printStackTrace();
		}

		actualizarTotales();
	}

	public void eliminadato(Row fila) throws ExcFiltroExcepcion {
		actualizarTotales();
	}
 

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void imprimir(NotaCredito documento, IZkAplicacion app) {
		try {
			NegocioNotaCredito negocio = NegocioNotaCredito.getInstance();
			List<ImpuestoDocumentoFiscal> impuestosFactura = negocio
					.InicializarImpuestosNota();
			negocio.setNota(documento);
			negocio.getNota().setBeneficiario(
					negocio.getCliente(documento.getBeneficiario()));
			negocio.getNota()
					.setDireccionFiscal(documento.getDireccionFiscal());
			negocio.actualizarImpuestoFactura(impuestosFactura, negocio
					.getNota().getImpuestos());
			HashMap parametros = new HashMap();

			JRDataSource ds = new JRBeanCollectionDataSource(negocio.getNota()
					.getDetalles());
			System.out.println("direccion fiscal "
					+ negocio.getNota().getDireccionFiscal());
			System.out.println("direccion beneficiario "
					+ negocio.getNota().getDireccionBeneficiario());
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();
			parametros.put("NroFacturaNota",
					"Factura Afectada:  " + documento.getStrFacturaAfectada());
			reporte.setSrc("reportes/factura.jasper");
			reporte.setParameters(parametros);
			reporte.setDatasource(ds);
			reporte.setType("pdf");
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
 

	@SuppressWarnings("unchecked")
	public void DesactivarDetalle() {
		List<Row> filas = vistaNota.getDetalle().getFilas().getChildren();
		for (Row row : filas) {
			boolean a = vistaNota.getDesactivarNota();
			System.out.println("va a desactivar");
			((CompCombobox<Impuesto>) row.getChildren().get(4)).setDisabled(a);
			((Doublebox) row.getChildren().get(3)).setDisabled(a);

		}

	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub

	}

	public EventListener agregarTodosItems() {
		// TODO Auto-generated method stub
		return new EventListener() {
			
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				ContNotaCredito.this.agregarTodoFactura();
				
			}
		};
	}

	protected void agregarTodoFactura() throws ExcFiltroExcepcion {
		NegocioFactura nf = NegocioFactura.getInstance();
		nf.setFactura(vistaNota.getFactura().getSeleccion());
		List<DetalleDocumentoFiscal> listaDetalle = nf.getFactura().getDetalles();
		vistaNota.getDetalle().clear();
		for(DetalleDocumentoFiscal dt: listaDetalle){
			vistaNota.getDetalle().getGrilla().agregar(dt);
		}
		actualizarTotales();
	}

}