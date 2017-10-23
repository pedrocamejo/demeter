package cpc.demeter.controlador.administrativo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.catalogo.ContReintegros;
import cpc.demeter.controlador.mantenimiento.garantia.ContSelecionarCliente;
import cpc.demeter.vista.administrativo.ContFormaPagoDeposito;
import cpc.demeter.vista.administrativo.UIRecibo;
import cpc.demeter.vista.administrativo.UIReintegro;
import cpc.demeter.vista.administrativo.UISeleccionFormaPago;
import cpc.modelo.demeter.administrativo.FormaPago;
import cpc.modelo.demeter.administrativo.FormaPagoCheque;
import cpc.modelo.demeter.administrativo.FormaPagoDeposito;
import cpc.modelo.demeter.administrativo.FormaPagoEfectivo;
import cpc.modelo.demeter.administrativo.FormaPagoRetencion;
import cpc.modelo.demeter.administrativo.FormaPagoTransferencia;
import cpc.modelo.demeter.administrativo.NotaCredito;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.demeter.administrativo.Reintegro;
import cpc.modelo.demeter.basico.ConceptoPago;
import cpc.modelo.demeter.basico.TipoFormaPago;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.negocio.demeter.administrativo.NegocioFormaPago;
import cpc.negocio.demeter.administrativo.NegocioRecibo;
import cpc.negocio.demeter.administrativo.NegocioReintegro;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContReintegro  extends ContVentanaBase<Reintegro> 
{
	private NegocioReintegro	 servicio;
	private UIReintegro 		 vista;
	private AppDemeter			 app;

	public ContReintegro(int modoOperacion, Reintegro reintegro, ContReintegros contReintegros, AppDemeter app) throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido, ExcFiltroExcepcion
	{
		super(modoOperacion);
		this.app = app;
		servicio = NegocioReintegro.getInstance();
		if (datoNulo(reintegro) || modoAgregar())
		{
			reintegro = new Reintegro();
		}
		setear(reintegro, new UIReintegro("Reintegro  (" + Accion.TEXTO[modoOperacion] + ")", 900 ), contReintegros, this.app);
		vista = (UIReintegro) getVista();
		vista.desactivar(modoOperacion);
	}

	public void eliminar() throws ExcFiltroExcepcion {
	
	}

	public void anular() throws ExcFiltroExcepcion {
	

	}

	public void guardar() throws ExcFiltroExcepcion {
		try
		{
			vista.actualizarModelo();
			
			if(getDato().getTipo() == Reintegro.REINTEGRO_RECIBOS)
			{
				getDato().setRecibos(vista.getRecibos());
			}
			servicio.guardar(getDato());
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("Error al generar el Reintegro, "+ e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		 
	
		if (vista.getModelo().getCliente() == null)
		{
			throw new WrongValueException(vista,"Seleccione un Cliente ");
		}
		else if(vista.getTipo().getSelectedItem() == null )
		{
			throw new WrongValueException(vista.getTipo(),"Seleccione un Tipo ");
		}
		else if(vista.getMontoTotal().getValue().equals(0.0))
		{
			throw new WrongValueException(vista.getTipo(),"No Puedes realizar un Reintegro por 0 bolivares ");
		}
		else if (vista.getTipo().getSelectedItem() != null)
		{
			if(vista.getTipo().getSelectedItem().getValue() == Reintegro.REINTEGRO_CREDITO && vista.getModelo().getNotaCredito() ==  null)
			{
				throw new WrongValueException(vista.getNotaCredito()," Carge una Nota de Credito ");
			}
			else if(vista.getTipo().getSelectedItem().getValue() == Reintegro.REINTEGRO_RECIBOS && vista.getRecibos().size() == 0 )
			{
				throw new WrongValueException(vista.getListRecibos()," Carge la Lista de Recibos ");
			}
		}
		
		
	}

	public void onEvent(Event event) throws Exception
	{

		if(event.getTarget() == vista.getSeleccionarCliente())
		{
			new ContSelecionarCliente(Accion.AGREGAR,this,app,null);
		}
		else if(event.getTarget() == vista.getAgregar() )
		{
			new ContSeleccionarRecibo(Accion.AGREGAR,this,app,vista.getModelo().getCliente());
		}
		else if(event.getTarget() == vista.getQuitar())
		{
			if(vista.getListRecibos().getSelectedItem() == null)
			{
				Messagebox.show("Debe Seleccionar un Item ");
			}
			else
			{
				Recibo recibo = (Recibo) vista.getListRecibos().getSelectedItem().getValue();
				vista.quitar(recibo);
				
			}
		}
		else if(event.getTarget() == vista.getTipo())
		{
			if (vista.getModelo().getCliente() == null)
			{
				Messagebox.show("Carge Primero el Cliente ");
			}
			else 
				if(vista.getTipo().getSelectedItem() != null)
				{
					vista.activarTipo();
				}
		}
		
		else if (event.getTarget() == vista.getNotaCredito())
		{
 
			new ContSeleccionarNotaCredito(Accion.AGREGAR,this,app,vista.getModelo().getCliente());
		}
		else if(event.getTarget() == vista.getAceptar())
		{
			procesarCRUD(event);
		}
		else if (event.getTarget() == vista.getCancelar())
		{
			vista.close();
		}
 			
	}



	
	@SuppressWarnings("rawtypes")
	public static void imprimir(Reintegro reintegro, IZkAplicacion app) {
		try {
			/*for(Recibo recibo : reintegro.getRecibos())
			{
				System.out.println(recibo.getFormaPagosHtml());
			} */
			
			String srcReporte  = Index.class.getResource("/").getPath() + (reintegro.getTipo() == Reintegro.REINTEGRO_RECIBOS ? "../../reportes/reintegro.jasper":"../../reportes/reintegroNotaCredito.jasper");
			HashMap parametros = new HashMap();
			parametros.put("logo", Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");
			parametros.put("sede",app.getSede().toString());
			List<Reintegro> recibos = new ArrayList<Reintegro>();
			recibos.add(reintegro);
			JRDataSource ds = new JRBeanCollectionDataSource(recibos);

			JasperReport reporte = (JasperReport) JRLoader.loadObject(new File(srcReporte));
			byte[] resultado = JasperRunManager.runReportToPdf(reporte,parametros,new JRBeanCollectionDataSource(recibos));
			Filedownload.save(resultado,"application/pdf","reintegro.pdf");
			
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
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

	public void agregarCliente(Cliente cliente) {
		// TODO Auto-generated method stub
		vista.agregarCliente(cliente);
		
		
	}

	public void agregarRecibo(Recibo recibo) {
		// TODO Auto-generated method stub
		
		vista.agregarRecibo(recibo);
		
	}

	public void agregarNota(NotaCredito nota) {
		// TODO Auto-generated method stub
		vista.agregarNotaCredito(nota);
		
	}

	
}
