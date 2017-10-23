package cpc.demeter.controlador.administrativo;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
 
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event; 
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Jasperreport; 
import org.zkoss.zul.Label; 
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.Servicios;
import cpc.demeter.vista.administrativo.UiNotaDebitoCheque;
import cpc.modelo.demeter.administrativo.Cheque;
import cpc.modelo.demeter.administrativo.DetalleDocumentoFiscal;
import cpc.modelo.demeter.administrativo.DocumentoFiscal; 
import cpc.modelo.demeter.administrativo.ImpuestoDocumentoFiscal;
import cpc.modelo.demeter.administrativo.NotaDebito;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.negocio.demeter.administrativo.NegocioFactura;
import cpc.negocio.demeter.administrativo.NegocioNotaDebito; 
import cpc.persistencia.demeter.implementacion.administrativo.PerEstadoDocumentoFiscal;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion; 
import cpc.zk.componente.listas.ContVentanaBase;
import cpc.zk.componente.ventanas.CompVentanaEntrada;
import cva.pc.demeter.excepciones.ExcAgregacionInvalida;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;
 

/*

los datos  requeridos en las tablas :-d voy a mandar a buscar con un like sobre la tabla costobancario 

insert into   basico.tbl_dem_producto
		(seq_idproducto,str_descripcion,seq_idtipoproducto,int_idtipoimpuesto) 
values (nextval('basico.tbl_dem_producto_seq_idproducto_seq'),'DEVOLUCION DE CHEQUE',4,2)


insert into basico.tbl_dem_costobancario
	(int_idproducto,dbl_precio,bol_activo)
	values(currval('basico.tbl_dem_producto_seq_idproducto_seq'),0,True)


*/
public class ContNotaDebitoCheque extends ContVentanaBase<NotaDebito> {



	private NegocioNotaDebito 				 servicio;
	private UiNotaDebitoCheque			 	 vistaNota;

	private Date 							 fechaCierre;
	private AppDemeter 						 app;

	private CompVentanaEntrada 				 vistaNroControl = new CompVentanaEntrada(this); //esto es para que ?
	
	
	
	/*se va a usar solo el articulo DEVOLUCION DE CHEQUE que esta en Costo Bancario cuando venta
	 *  el vendra con su impuesto esto creo que no requiere algun impuesto se pagaron en la factura */
	private List<DocumentoFiscal>		 	 facturas ;
	private IProducto				 		 producto; 
	
	
	public ContNotaDebitoCheque(int modoOperacion, NotaDebito nota,	ContCatalogo<NotaDebito> llamador, AppDemeter app)throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido,ExcFiltroExcepcion
	{
		super(modoOperacion);
		this.app = app;
		servicio = NegocioNotaDebito.getInstance();
		fechaCierre = servicio.getFechaCierre();
	
		if (modoOperacion == Accion.ANULAR && !fechaCierre.equals(nota.getFecha()))
		{
			throw new ExcDatosInvalidos("No se puede anular, causa: Fecha de Nota de Debito no pertenece al dia a cerrar");
		}
		
		facturas = servicio.getFacturasCanceladasCheque();
	    producto = servicio.getCostoBancario("DEVOLUCION DE CHEQUE");
		nota = servicio.NotaNueva();

		servicio.setNota(nota); //<-- cagada si se supone que el servicio es estatico y esta en todos lados el mismo objecto ps!! por eso es una cagada

		vistaNota = new UiNotaDebitoCheque("NOTA DE DEBITO ("+ Accion.TEXTO[modoOperacion] + ")",850,facturas);
		setear(servicio.getNota(),vistaNota, llamador, this.app);
		
		vistaNota = (UiNotaDebitoCheque) getVista();
		vistaNota.getSede().setValue(app.getSede().getNombre());
		vistaNota.desactivar(modoOperacion);


	}

	public void eliminar() throws ExcFiltroExcepcion {

	}

	public void guardar() throws ExcFiltroExcepcion {
		try {
			getVista().actualizarModelo();
			cargarModelo();
			getDato().setEstado(new PerEstadoDocumentoFiscal().getEstadoNuevaFactura());
			getDato().setMontoSaldo(vistaNota.getTotal().getValue());
			servicio.guardar(getDato());
			mostrarVistaNroControl(servicio.getNota(), app);
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

	public void cargarModelo() throws ExcAgregacionInvalida 
	{

	}

	
	public void validar() throws WrongValuesException, ExcEntradaInconsistente 
	{

		if(vistaNota.getFactura().getSeleccion() == null)
		{
			throw new WrongValueException(vistaNota.getFactura(),"Debe seleccionar 1 factura");
		}
		if(vistaNota.getDetalles().size() == 0)
		{
			throw new WrongValueException(vistaNota.getFactura()," La Nota debe Poseer por lo menos 1 Detalle ");
		}
	
	}

	public void onEvent(Event event) throws Exception
	{
		if( vistaNota.getAgregar() == event.getTarget() )
		{
			//armo un detalle para el deposito y lo mando a guardar ;-D
			Cheque cheque = (Cheque) (vistaNota.getCheque().getSelectedItem() == null ?null:vistaNota.getCheque().getSelectedItem().getValue());
			DocumentoFiscal factura = vistaNota.getFactura().getSeleccion();
			// requiero el cheque y la factura :-D
			
			if(cheque != null & factura != null)
			{
				DetalleDocumentoFiscal detalle = new DetalleDocumentoFiscal();
					detalle.setDocumento(factura);
					detalle.setComplementoDescripcion(cheque.getNroCheque());
					detalle.setCantidad(1);
					detalle.setPrecioUnitario(cheque.getMonto());
					detalle.setServicio(producto);
					detalle.setAlicuota(producto.getImpuesto());
				vistaNota.agregardetalle(detalle);
				vistaNota.actualizarTotales();
			}
			else 
			{
				app.mostrarError("Debes Seleccionar 1 cheque ");
			}
		}
		else if(vistaNota.getQuitDetalle() == event.getTarget())
		{
			DetalleDocumentoFiscal detalle =(DetalleDocumentoFiscal) (vistaNota.getDetalle().getSelectedItem() == null ? null:vistaNota.getDetalle().getSelectedItem().getValue());
			if(detalle != null)
			{
				vistaNota.quitarDetalle(detalle);
			}
			else
			{
				app.mostrarError("Debe Seleccionar 1 Detalle ");
			}
		}
		else if ( vistaNroControl.getAceptar() == event.getTarget() )
		{
			if (nroControlValido(servicio.getNota(), vistaNroControl.getEntrada()))
			{
				vistaNroControl.close();
			    imprimir(servicio.getNota(), app);
			}
		}
	 	else if (vistaNota.getFactura() == event.getTarget()) 
		{
				actualizarDatosFactura();
		}
		else
		{
			procesarCRUD(event);
		}
	}


	public boolean nroControlValido(DocumentoFiscal documento, Textbox txtNroControl) throws InterruptedException {
		try {
			if (txtNroControl.getText().trim().isEmpty())
			{
				app.mostrarError("El nro de control no puede estar vacío");
				vistaNroControl.getEntrada().setFocus(true);
				return false;
			}
			else if (documento.getNroDocumento().compareTo(Integer.valueOf(txtNroControl.getText())) != 0)
			{
				Messagebox.show("El Nº de Control ingresado no coincide con el de la Nota de Débito","Error", Messagebox.OK, Messagebox.ERROR);
				return false;
			}
			return true;
		} catch (NumberFormatException e) 
		{
			// TODO: handle exception
			Messagebox.show("El texto debe estar en formato numérico", "Error",Messagebox.OK, Messagebox.ERROR);
			return false;
		}
	}


	public void actualizarDatosFactura() throws ExcFiltroExcepcion
	{
		DocumentoFiscal factura = vistaNota.getFactura().getValorObjeto();

		Cliente cliente = servicio.getCliente(factura.getBeneficiario());
	
		vistaNota.getRazonSocial().setValue(cliente.getNombres());
		vistaNota.getDireccion().setValue(factura.getDireccionBeneficiario().toString());
		vistaNota.getCedula().setValue(cliente.getIdentidadLegal());

		
		// Mostrar el Listado de Cheques asociados a esa factura :-D
		
		List<Cheque> cheques = servicio.getCheques(factura);
		if(cheques.size() == 0)
		{
			app.mostrarAdvertencia("Esta Factura no posee ningun Cheque Asociado");
		}

		vistaNota.setCheques(cheques);
		vistaNota.actualizarcheques();
		List<DetalleDocumentoFiscal> detalles = new ArrayList<DetalleDocumentoFiscal>();
		vistaNota.setDetalles(detalles);
		vistaNota.ActualizarDetalles();
	}


	
	public static void imprimir(NotaDebito documento, IZkAplicacion app) {
		try {
			
			NegocioFactura negocio = NegocioFactura.getInstance();

			List<ImpuestoDocumentoFiscal> impuestosFactura = negocio.InicializarImpuestosFactura();

			negocio.setFactura(documento);
			negocio.getFactura().setBeneficiario(negocio.getCliente(documento.getBeneficiario()));
			negocio.actualizarImpuestoFactura(impuestosFactura, negocio.getFactura().getImpuestos());

			HashMap parametros = new HashMap();

			JRDataSource ds = new JRBeanCollectionDataSource(negocio.getFactura().getDetalles());
			System.out.println("direccion fiscal "+ negocio.getFactura().getDireccionFiscal());
			System.out.println("direccion beneficiario "+ negocio.getFactura().getDireccionBeneficiario());
		
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();
			parametros.put("NroFacturaNota", "Factura Afectada:  " + documento.getStrFacturaAfectada());
			reporte.setSrc("reportes/factura.jasper");
			reporte.setParameters(parametros);
			reporte.setDatasource(ds);
			reporte.setType("pdf");

		} catch (Exception e) {
			e.getStackTrace();
		}
	}


	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		try
		{
			validar();
		}
		catch (WrongValuesException e)
		{
			app.mostrarError(e.getMessage());
		
		}
		catch (ExcEntradaInconsistente e)
		{
		
			app.mostrarError(e.getMessage());
		}
		guardar();
		refrescarCatalogo();
		vistaNota.close();
	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		try
		{
			vistaNota.actualizarModeloDetalle();
			validar();
			guardar();
			this.getLlamador().agregarDato(getDato());
			refrescarCatalogo();
			vistaNota.close();
			
			//  lo agrego 
			
		}
		catch (WrongValuesException e)
		{
			app.mostrarError(e.getMessage());
			e.printStackTrace();
		}
		catch (ExcEntradaInconsistente e)
		{
			app.mostrarError(e.getMessage());
			e.printStackTrace();
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			app.mostrarError(e.getMessage());
			e.printStackTrace();
		} 
	
	}
 
	public boolean codigoValido(Textbox txtNroControl, Label codigo)throws InterruptedException 
	{
		Servicios servicio = new Servicios(app);
		try {
	
			if (txtNroControl.getText().trim().isEmpty()) 
			{
				app.mostrarError("El nro de control no puede estar vacio");
				vistaNroControl.getEntrada().setFocus(true);
				return false;
			}
			else if (!txtNroControl.getText().equals(servicio.generarHash(codigo.getValue())))
			{
				Messagebox.show("El codigo no coincide", "Error",Messagebox.OK, Messagebox.ERROR);
				return false;
			}
			return true;
		}
		catch (Exception e) 
		{
			// TODO: handle exception
			Messagebox.show("El texto debe estar en formato numérico", "Error",Messagebox.OK, Messagebox.ERROR);
			return false;
		}
	}

	 

}
