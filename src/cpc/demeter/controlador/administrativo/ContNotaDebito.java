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
import cpc.demeter.vista.administrativo.UINotaDebito;
import cpc.modelo.demeter.administrativo.DetalleDocumentoFiscal;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.Impuesto;
import cpc.modelo.demeter.administrativo.ImpuestoDocumentoFiscal;
import cpc.modelo.demeter.administrativo.NotaDebito;
import cpc.modelo.demeter.administrativo.ReciboDocumentoFiscal;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.basico.Labor;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.ministerio.basico.TipoProductor;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.negocio.demeter.administrativo.NegocioFactura;
import cpc.negocio.demeter.administrativo.NegocioNotaDebito;
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
public class ContNotaDebito extends
		ContVentanaMaestroDetalle<NotaDebito, DetalleDocumentoFiscal> {

	private NegocioNotaDebito servicio;
	private UINotaDebito vistaNota;
	private List<Impuesto> impuestos = null;
	private List<ImpuestoDocumentoFiscal> impuestosFactura = null;
	private Date fechaCierre;
	private AppDemeter app;

	private CompVentanaEntrada vistaNroControl = new CompVentanaEntrada(this);

	public ContNotaDebito(int modoOperacion, NotaDebito nota,
			ContCatalogo<NotaDebito> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido,
			ExcFiltroExcepcion {
		super(modoOperacion);
		this.app = app;
		servicio = NegocioNotaDebito.getInstance();
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
				&& !fechaCierre.equals(nota.getFecha())) {
			throw new ExcDatosInvalidos(
					"No se puede anular, causa: Fecha de Nota de Debito no pertenece al dia a cerrar");
		}

		List<DocumentoFiscal> facturas = null;
		impuestosFactura = new ArrayList<ImpuestoDocumentoFiscal>();
		List<IProducto> articulos = null;

		try {
		//	facturas = servicio.getFacturasActivas();
			facturas = servicio.getFacturasActivasProject();
			articulos = servicio.getProductos();
			if (!modoAgregar()) {
				articulos.addAll(servicio.getCostoBancario());
			}
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
		setear(servicio.getNota(), new UINotaDebito("NOTA DE DEBITO ("
				+ Accion.TEXTO[modoOperacion] + ")", 850, facturas, impuestos,
				articulos), llamador, this.app);
		vistaNota = (UINotaDebito) getVista();
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
			// imprimir(getDato(), app);
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
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
			this.app.mostrarError(e.getMessage());
		}
	}

	public void cargarModelo() throws ExcAgregacionInvalida {
		List<DetalleDocumentoFiscal> detalles = vistaNota.getModeloDetalle();
		List<Row> filas = vistaNota.getDetalle().getFilas().getChildren();
		int i = 0;

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
		getDato().setMontoTotal(Real.redondeoMoneda(getDato().getMontoTotal()));
		getDato().setMontoSaldo(Real.redondeoMoneda(getDato().getMontoTotal()));
		getDato().setImpuestos(impuestosFactura);
		for (ImpuestoDocumentoFiscal imp : impuestosFactura) {
			imp.setBase(imp.getBase());
			imp.setMonto(imp.getMonto());
			imp.setDocumento(getDato());
		}

	}

	@SuppressWarnings("unchecked")
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TipoFormaPago tipoFormaPago;
		Doublebox cantidad;
		Doublebox precio;
		Textbox descripcion;
		List<Row> filas = vistaNota.getDetalle().getFilas().getChildren();
		for (Row item : filas) {
			descripcion = (Textbox) item.getChildren().get(2);
			cantidad = (Doublebox) item.getChildren().get(1);
			precio = (Doublebox) item.getChildren().get(5);
			if (cantidad.getValue() <= 0)
				throw new WrongValueException(cantidad, "Cantidad no valida");
			if (descripcion.getValue().length() < 6)
				throw new WrongValueException(descripcion,
						"Descripcion no valida");
			if (precio.getValue() <= 0)
				throw new WrongValueException(precio, "Monto no valido");
		}
		/*
		 * if (vistaNota.getDireccion().getSeleccion() == null) throw new
		 * WrongValueException(vistaNota.getDireccion(), "Direccion no valida");
		 */
		if (vistaNota.getSubTotal().getValue() <= 0)
			throw new WrongValueException(vistaNota.getSubTotal(),
					"Monto no valido");
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
				if (event.getTarget() == getVista().getAceptar()) {
					procesarCRUD(event);
				} else if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {
					if (event.getTarget() == vistaNota.getFactura()) {
						DocumentoFiscal docu = vistaNota.getFactura().getSeleccion();
						docu =servicio.getFacturaProject( docu);
						vistaNota.getFactura().setSeleccion(docu);
						actualizarDatos();
					} else {
						actualizarServicio((CompBuscar<IProducto>) event
								.getTarget());
					}
				} else if (event.getName().equals(Events.ON_CHANGE)) {
					List<Row> filas = vistaNota.getDetalle().getFilas()
							.getChildren();

					for (Row row : filas) {
						if (event.getTarget() == ((Doublebox) row.getChildren()
								.get(1))) {
							actualizarPrecio((Doublebox) event.getTarget());
						}
					}
				}

				else if (event.getName().equals(CompGrilla.ON_ELIMINA_FILA))
					eliminadato((Row) event.getData());
			} catch (Exception e) {
				e.printStackTrace();
				this.app.mostrarError(e.getMessage());
			}
		}
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
						.show("El Nº de Control ingresado no coincide con el de la Nota de Débito",
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

	public void actualizarDatos() {
		DocumentoFiscal factura = vistaNota.getFactura().getValorObjeto();
		Cliente cliente = servicio.getCliente(factura.getBeneficiario());
		vistaNota.getRazonSocial().setValue(cliente.getNombres());
		vistaNota.getDireccion().setValue(
				factura.getDireccionBeneficiario().toString());
		vistaNota.getCedula().setValue(cliente.getIdentidadLegal());
		// vistaNota.actualizarModeloServicios(servicio.getServiciosFactura(factura.getId()));
		vistaNota.getDetalle().clear();
	}

	@SuppressWarnings("unchecked")
	public void actualizarServicio(CompBuscar<IProducto> componente)
			throws ExcFiltroExcepcion {
		IProducto producto = null;
		servicio = NegocioNotaDebito.getInstance();
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
		
		Double preciodefecto =new Double(0); 
		if (producto instanceof ArticuloVenta){
			preciodefecto = ((ArticuloVenta)producto).getPrecioBase();
		}else
		{
			preciodefecto = ((Labor)producto).getPrecioBase(tipo);
		}
		Impuesto impuestodefecto = producto.getImpuesto();
		((CompCombobox<Impuesto>) registro.getChildren()
				.get(4)).setSeleccion(impuestodefecto);
		((Doublebox) registro.getChildren().get(3)).setValue(preciodefecto);
		
		
		try {
			NegocioFactura nf = NegocioFactura.getInstance();
			nf.setFactura(vistaNota.getFactura().getSeleccion());
			List<DetalleDocumentoFiscal> listaDetalle = nf.getFactura()
					.getDetalles();
			Double precio = new Double(0);
			Double porcentaje = new Double(0);
			Impuesto impuesto = new Impuesto();
			int a = listaDetalle.size();
			if (a != 0) {
				for (DetalleDocumentoFiscal detalleDocumentoFiscal : listaDetalle) {
					if (producto.equals(detalleDocumentoFiscal.getServicio())) {
						precio = detalleDocumentoFiscal.getPrecioUnitario();
						impuesto = detalleDocumentoFiscal.getAlicuota();
						if (producto instanceof ArticuloVenta) {
							try {
								((Textbox) registro.getChildren().get(2))
										.setValue(((ArticuloVenta) componente
												.getSeleccion())
												.getStrUnidadMedida());
							} catch (Exception e) {
								precio = new Double(0);
								e.printStackTrace();
								throw new ExcFiltroExcepcion(
										"El Item seleccionado no tiene un precio valido... Se espcificara 0 como valor por defecto");
							}
						}
						try {
							((Doublebox) registro.getChildren().get(3))
									.setValue(precio);
							double cantidad = ((Doublebox) registro
									.getChildren().get(1)).getValue();
							((Doublebox) registro.getChildren().get(5))
									.setValue(precio * cantidad);
							System.out.println(detalleDocumentoFiscal
									.getAlicuota());

							((CompCombobox<Impuesto>) registro.getChildren()
									.get(4)).setSeleccion(impuesto);
						} catch (Exception e) {
							throw new ExcFiltroExcepcion(
									"Problemas con el servicio");
						}

						try {
							System.out.println(detalleDocumentoFiscal
									.getAlicuota());
							((DetalleDocumentoFiscal) registro
									.getAttribute("dato"))
									.setAlicuota(detalleDocumentoFiscal
											.getAlicuota());

							actualizarTotales();
							DesactivarDetalle();
						} catch (Exception e) {
							throw new ExcFiltroExcepcion(
									"Problemas con el impuesto");
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
		DetalleDocumentoFiscal detalle = null;
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

			// ((DetalleDocumentoFiscal) item.getAttribute("dato"))
			// .getAlicuota();

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
			double cantidad = ((Doublebox) registro.getChildren().get(1))
					.getValue();
			((Doublebox) registro.getChildren().get(5)).setValue(cantidad
					* precio);
			actualizarTotales();
		} catch (Exception e) {
			// throw new ExcFiltroExcepcion("Problemas con el servicio");
			if (registro != null)
				throw new WrongValueException(((CompBuscar<IProducto>) registro
						.getChildren().get(0)),
						"coloca el impuesto y el precio unitario antes de la cantidad");
			else
				app.mostrarError("Problemas con producto seleccionado");
		}
	}

	public void eliminadato(Row fila) throws ExcFiltroExcepcion {
		actualizarTotales();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void imprimir(NotaDebito documento, IZkAplicacion app) {
		try {
			NegocioFactura negocio = NegocioFactura.getInstance();
			List<ImpuestoDocumentoFiscal> impuestosFactura = negocio
					.InicializarImpuestosFactura();
			negocio.setFactura(documento);
			negocio.getFactura().setBeneficiario(
					negocio.getCliente(documento.getBeneficiario()));
			negocio.actualizarImpuestoFactura(impuestosFactura, negocio
					.getFactura().getImpuestos());
			HashMap parametros = new HashMap();
			JRDataSource ds = new JRBeanCollectionDataSource(negocio
					.getFactura().getDetalles());
			System.out.println("direccion fiscal "
					+ negocio.getFactura().getDireccionFiscal());
			System.out.println("direccion beneficiario "
					+ negocio.getFactura().getDireccionBeneficiario());
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

	public EventListener getDetalleRecibo() {
		// TODO Auto-generated method stub
		return new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				Object obj = vistaNota.getListRecibos().getSelectedItem().getValue();
				if(obj.getClass() == ReciboDocumentoFiscal.class)
				{
					ReciboDocumentoFiscal reciboDocumentoFiscal = (ReciboDocumentoFiscal) obj;
					ContNotaDebito controlador = (ContNotaDebito) vistaNota.getControlador();
					
					ContRecibo contRecibo = new ContRecibo(Accion.CONSULTAR, reciboDocumentoFiscal.getRecibo(),controlador,controlador.app);
				
				}
				
				
			}
		};
	}
	
	
	


	public EventListener agregarTodosItems() {
		// TODO Auto-generated method stub
		return new EventListener() {
			
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				ContNotaDebito.this.agregarTodoFactura();
				
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