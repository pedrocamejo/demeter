package cpc.demeter.controlador.administrativo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Filedownload;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.controlador.mantenimiento.garantia.ContSelecionarCliente;
import cpc.demeter.vista.administrativo.ContFormaPagoDeposito;
import cpc.demeter.vista.administrativo.UIRecibo;
import cpc.demeter.vista.administrativo.UISeleccionFormaPago;
import cpc.modelo.demeter.administrativo.FormaPago;
import cpc.modelo.demeter.administrativo.FormaPagoCheque;
import cpc.modelo.demeter.administrativo.FormaPagoDeposito;
import cpc.modelo.demeter.administrativo.FormaPagoEfectivo;
import cpc.modelo.demeter.administrativo.FormaPagoPunto;
import cpc.modelo.demeter.administrativo.FormaPagoRetencion;
import cpc.modelo.demeter.administrativo.FormaPagoTransferencia;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.demeter.basico.TipoFormaPago;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.negocio.demeter.administrativo.NegocioFormaPago;
import cpc.negocio.demeter.administrativo.NegocioRecibo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.controlador.ContControlGeneral;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContRecibo extends ContVentanaBase<Recibo> 
{
	
	private static final long serialVersionUID = -4005176298792986376L;
	private NegocioRecibo			 servicio;
	private UIRecibo 				 vistaRecibo;
	private UISeleccionFormaPago  	 uiseleccionFormaPago;
	private AppDemeter app;

	public ContRecibo(int modoOperacion, Recibo recibo,	ContCatalogo<Recibo> llamador, AppDemeter app) throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido, ExcFiltroExcepcion
	{
		super(modoOperacion);
		this.app = app;
		servicio = NegocioRecibo.getInstance();
		Date fechaCierre = servicio.getFechaCierre();
		
		if (modoOperacion == Accion.ANULAR && !fechaCierre.equals(recibo.getFecha()))
		{
			throw new ExcDatosInvalidos("No se puede anular, causa: Fecha de recibo no pertenece al dia a cerrar");
		}
		if (datoNulo(recibo) || modoAgregar())
		{
			recibo = servicio.ReciboNuevo();
		}
		setear(recibo, new UIRecibo("RECIBO  (" + Accion.TEXTO[modoOperacion] + ")", 1200, recibo.getFecha()), llamador, this.app);
		vistaRecibo = (UIRecibo) getVista();
		vistaRecibo.getSede().setText(this.app.getSede().toString());
		vistaRecibo.desactivar(modoOperacion);

	}

	public ContRecibo(int consultar, Recibo recibo, ContControlGeneral controlador,
			AppDemeter app) throws ExcDatosInvalidos, ExcFiltroExcepcion, ExcDatosNoValido, ExcSeleccionNoValida {
		super(consultar);
		
		this.app = app;
		
		servicio = NegocioRecibo.getInstance();
		servicio.setRecibo(recibo);
		Recibo auxiliar = servicio.getRecibo();
		UIRecibo vista = new UIRecibo("RECIBO  (" + Accion.TEXTO[consultar] + ")", 900, recibo.getFecha());
		setear (consultar,auxiliar, vista,  this.app);
		vistaRecibo = (UIRecibo) getVista();
		//vistaRecibo;
		//vistaRecibo.desactivar(consultar);
		// TODO Auto-generated constructor stub
	}

	public void eliminar() throws ExcFiltroExcepcion {
		servicio.eliminar();
	}

	public void anular() throws ExcFiltroExcepcion {
	
		try { 
			
			servicio.anular(getDato());
			refrescarCatalogo();
			vistaRecibo.close();
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	}

	public void guardar() throws ExcFiltroExcepcion {
		try
		{
			vistaRecibo.actualizarModelo();
			getDato().setFormaspago(vistaRecibo.getFormasPagos());
			servicio.guardar(getDato());
			imprimir(getDato(), app);

		} 
		catch (Exception e)
		{
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("Error al generar recibo, "+ e.getMessage());
		}

	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
	
		 	if(vistaRecibo.getCliente() == null)
			{
				throw new WrongValueException(vistaRecibo.getRazonSocial(),"Seleccione un Cliente ");
			}
			else if(vistaRecibo.getFormasPagos().size() == 0)
			{
				throw new WrongValueException(vistaRecibo.getListFormaPago(),"El Recibo Debe tener al menos 1 Forma de Pago ");
			}
			else if(vistaRecibo.getMonto().getValue() <=  0 )
			{
				throw new WrongValueException(vistaRecibo.getMonto()," Monto invalido ");
			}
			else if(vistaRecibo.getSaldo().getValue() <=  0 )
			{
				throw new WrongValueException(vistaRecibo.getSaldo()," Saldo invalido ");
			} 
	}

	public void onEvent(Event event) throws Exception
	{
		
		if(event.getTarget() == vistaRecibo.getAceptar())
		{
			procesarCRUD(event);
		} 
		else if (event.getTarget() == vistaRecibo.getseleccionarCliente())
		{
			new ContSelecionarCliente(Accion.CONSULTAR,this,app,null);
		}
	 	else if(event.getTarget() ==  vistaRecibo.getAgregarPago())
		{	
			 uiseleccionFormaPago =  new UISeleccionFormaPago("Forma de Pago","none",true,this);
			app.agregarHija(uiseleccionFormaPago);
			uiseleccionFormaPago.doModal();
			//new ContFormaPago(Accion.AGREGAR,this, app);
		}
		else if (uiseleccionFormaPago != null )
		{
			if (event.getTarget() ==  uiseleccionFormaPago.getCheque())
			{
				TipoFormaPago tipoFormaPago =  NegocioFormaPago.getInstance().getTipoFormaCheque();
				FormaPagoCheque formaPagoCheque = new FormaPagoCheque();
				formaPagoCheque.setTipoFormaPago(tipoFormaPago);
				
				uiseleccionFormaPago.detach();
				new ContFormaPagoCheque(Accion.AGREGAR,this,app,formaPagoCheque);
			}
			else if (event.getTarget() ==  uiseleccionFormaPago.getEfectivo())
			{
				TipoFormaPago tipoFormaPago =  NegocioFormaPago.getInstance().getTipoFormaEfectivo();
				FormaPagoEfectivo formaPago = new FormaPagoEfectivo();
				formaPago.setTipoFormaPago(tipoFormaPago);
				
				uiseleccionFormaPago.detach();
				new ContFormaPago(Accion.AGREGAR,this,app,formaPago);
			}
			else if (event.getTarget() ==  uiseleccionFormaPago.getRetencionIVA())
			{
				TipoFormaPago tipoFormaPago =  NegocioFormaPago.getInstance().getTipoFormaRetencionIVA();
				FormaPagoRetencion formaPago = new FormaPagoRetencion();
				formaPago.setTipoFormaPago(tipoFormaPago);
				
				uiseleccionFormaPago.detach();
				new ContFormaPago(Accion.AGREGAR,this,app,formaPago);
				
			
			}
			else if (event.getTarget() ==  uiseleccionFormaPago.getRetencionISLR())
			{
				TipoFormaPago tipoFormaPago =  NegocioFormaPago.getInstance().getTipoFormaRetencionISLR();
				FormaPagoRetencion formaPago = new FormaPagoRetencion();
				formaPago.setTipoFormaPago(tipoFormaPago);
				
				uiseleccionFormaPago.detach();
				new ContFormaPago(Accion.AGREGAR,this,app,formaPago);
			
			}
			else if (event.getTarget() ==  uiseleccionFormaPago.getRetencion1x100())
			{
				TipoFormaPago tipoFormaPago =  NegocioFormaPago.getInstance().getTipoFormaRetencion1x100();
				FormaPagoRetencion formaPago = new FormaPagoRetencion();
				formaPago.setTipoFormaPago(tipoFormaPago);
				
				uiseleccionFormaPago.detach();
				new ContFormaPago(Accion.AGREGAR,this,app,formaPago);
			
			}
			else if (event.getTarget() ==  uiseleccionFormaPago.getDeposito())
			{
				TipoFormaPago tipoFormaPago =  NegocioFormaPago.getInstance().getTipoFormaDeposito();
				FormaPagoDeposito formaPago = new FormaPagoDeposito();
				formaPago.setTipoFormaPago(tipoFormaPago);
				
				uiseleccionFormaPago.detach();
				new ContFormaPagoDeposito(Accion.AGREGAR,this,app,formaPago);
			
			}
			else if (event.getTarget() ==  uiseleccionFormaPago.getTransferencia())
			{
				TipoFormaPago tipoFormaPago =  NegocioFormaPago.getInstance().getTipoFormaTransferencia();
				FormaPagoTransferencia formaPago = new FormaPagoTransferencia();
				formaPago.setTipoFormaPago(tipoFormaPago);
				
				uiseleccionFormaPago.detach();
				new ContFormaPagoTransferencia(Accion.AGREGAR,this,app,formaPago);
		 
			}
			else if (event.getTarget() ==  uiseleccionFormaPago.getPunto())
			{
				TipoFormaPago tipoFormaPago =  NegocioFormaPago.getInstance().getTipoFormaPunto();
				FormaPagoPunto formaPago = new FormaPagoPunto();
				formaPago.setTipoFormaPago(tipoFormaPago);
				uiseleccionFormaPago.detach();
				new ContFormaPagoPunto(Accion.AGREGAR,this,app,formaPago);
		 	}
			 
		}
	}


	public void cargarTipo() {
		if (vistaRecibo == null)
			vistaRecibo = (UIRecibo) getVista();
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void imprimir(Recibo documento, IZkAplicacion app) {
		try {
			HashMap parametros = new HashMap();
			parametros.put("logo",Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");
			List<Recibo> recibos = new ArrayList<Recibo>();
			recibos.add(documento);
			//JRDataSource ds = new JRBeanCollectionDataSource(recibos);
			String url = (Index.class.getResource("/").getPath()+ "../../reportes/recibo.jasper").trim();
			JasperReport reporte = (JasperReport) JRLoader.loadObject(new File(url));
			byte[] resultado = JasperRunManager.runReportToPdf(reporte,parametros,new JRBeanCollectionDataSource(recibos));
			Filedownload.save(resultado,"application/pdf","recibo"+documento.getControl()+".pdf");
	
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void actualizarTotal() throws InterruptedException
	{
		vistaRecibo.actualizarSaldos();
	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
	}

	public void agregarFormaPago(FormaPago pago) throws InterruptedException {
		// TODO Auto-generated method stub
		this.vistaRecibo.agregarPago(pago);
	}

	public void agregarCliente(Cliente cliente) {
		// TODO Auto-generated method stub
		vistaRecibo.setCliente(cliente);
	}

	public EventListener getDetalleFormaPago() {
		// TODO Auto-generated method stub
		return new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				Object obj = vistaRecibo.getListFormaPago().getSelectedItem().getValue();
				if(obj.getClass() == FormaPagoEfectivo.class)
				{
					FormaPagoEfectivo formapago = (FormaPagoEfectivo) obj;
					new ContFormaPago(Accion.CONSULTAR,ContRecibo.this,app,formapago);
				}
				else if(obj.getClass() == FormaPagoRetencion.class)
				{
					FormaPagoRetencion formapago = (FormaPagoRetencion) obj;
					new ContFormaPago(Accion.CONSULTAR,ContRecibo.this,app,formapago);
				}
				else if(obj.getClass() == FormaPagoCheque.class)
				{
					FormaPagoCheque formapago = (FormaPagoCheque) obj;
					new ContFormaPagoCheque(Accion.CONSULTAR,ContRecibo.this,app,formapago);
				}
				else if(obj.getClass() == FormaPagoTransferencia.class)
				{
					FormaPagoTransferencia formapago = (FormaPagoTransferencia) obj;
					new ContFormaPagoTransferencia(Accion.CONSULTAR,ContRecibo.this,app,formapago);
				}
				else if(obj.getClass() == FormaPagoDeposito.class)
				{
					FormaPagoDeposito formapago = (FormaPagoDeposito) obj;
					new ContFormaPagoDeposito(Accion.CONSULTAR,ContRecibo.this,app,formapago);
				} 
				
			}
		};
	};

	private void setear(int modoOperacion,Recibo obj, UIRecibo vista , AppDemeter app2) 
	{
		try {
			vista.setModelo(obj);
			vista.setMode("modal");
			vista.setControlador(this);
			vista.inicializar();
			vista.dibujar();
			vista.cargarValores(obj);
			vista.getSede().setText(this.app.getSede().toString());
		 	vista.desactivar(modoOperacion);
		 	vista.getListFormaPago().setDisabled(true);		 	
			app.agregarHija(vista);
			vista.doModal();
		
		} catch (Exception e) {
			e.printStackTrace();
			//throw new ExcDatosInvalidos(e.getMessage());
		}
	}
}
