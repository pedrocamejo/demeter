package cpc.demeter.controlador.administrativo;




import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.ContVentanaBase;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.administrativo.UiNotaDebitoRecibo;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.NotaDebito;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.demeter.administrativo.ReciboDocumentoFiscal;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.negocio.demeter.administrativo.NegocioDocumentoFiscal;
import cpc.negocio.demeter.administrativo.NegocioNotaDebito;

import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;


public class ContNotaDebitoRecibo extends ContVentanaBase<NotaDebito> {
	
	private NegocioDocumentoFiscal servicio;
	private UiNotaDebitoRecibo vista;
	private AppDemeter app;

	
	public ContNotaDebitoRecibo(int modoOperacion, NotaDebito nota,	ContCatalogo<NotaDebito> llamador, AppDemeter app) throws ExcDatosInvalidos, ExcDatosNoValido, ExcSeleccionNoValida
	{
		super(modoOperacion);
		this.app = app;
		servicio = NegocioDocumentoFiscal.getInstance();
		setear(nota, new UiNotaDebitoRecibo("NOTA DE DEBITO ("+ Accion.TEXTO[modoOperacion] + ")", 850),llamador, this.app);
		vista = (UiNotaDebitoRecibo) getVista();
	 
	}

	public void eliminar() throws ExcFiltroExcepcion {

	}

	public void guardar() throws ExcFiltroExcepcion {

	}

	public void anular() {

	}


	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if(vista.getRecibos().size() == 0)
		{
			 throw new  WrongValueException(vista.getListRecibos(),"Debe Agregar al Menos un recibo"); 
		}
	}

	public void onEvent(Event arg0) throws Exception {
		
		if (arg0.getTarget() == vista.getAgregar())
		{
			if (vista.getSaldo().getValue() <= 0 )
			{
				Messagebox.show("Nota Saldada ");
			}
			else
			{
				new ContSeleccionarRecibo(Accion.AGREGAR,this,app, vista.getModelo().getBeneficiario());
			}
		}
		else if(arg0.getTarget() == vista.getQuitar())
		{
			if(vista.getListRecibos().getSelectedItem() == null )
			{
				Messagebox.show("Seleccione un Item ");
			}
			else
			{
				ReciboDocumentoFiscal recibo =  (ReciboDocumentoFiscal) vista.getListRecibos().getSelectedItem().getValue();
				if(recibo.getId() == null)
				{
					vista.quitarRecibo(recibo);
				}
				else
				{
					Messagebox.show("No Puedes Quitar este Recibo ");
				}
			}
		} 
		else if(arg0.getTarget() == vista.getAceptar())
		{
			procesarCRUD(arg0);
		}
		
	}
	

	public static void imprimir(NotaDebito documento, IZkAplicacion app) {

	}


	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		
		validar();
		try {
			vista.actualizarModelo();
			DocumentoFiscal documento = vista.getModelo();
			documento.setRecibos(vista.getRecibos());
			servicio.pagarDocumentoFiscal(documento);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		refrescarCatalogo();
		vista.detach();
	}
	

	public void agregarRecibo(Recibo recibo) {
		// TODO Auto-generated method stub
		for(ReciboDocumentoFiscal reciboVista: vista.getRecibos())
		{
			if(recibo.getId().equals(reciboVista.getRecibo().getId()))
			{
				return;
			}
		}
		ReciboDocumentoFiscal nuevoRecibo = new ReciboDocumentoFiscal();
		nuevoRecibo.setDocumentoFiscal(getDato());
		nuevoRecibo.setRecibo(recibo);
		vista.agregarRecibo(nuevoRecibo);
	}



}