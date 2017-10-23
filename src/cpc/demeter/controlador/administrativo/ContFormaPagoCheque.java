package cpc.demeter.controlador.administrativo;

import java.io.Serializable;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter; 
import cpc.demeter.vista.administrativo.UiDepositoCheque;
import cpc.modelo.demeter.administrativo.Cheque;
import cpc.modelo.demeter.administrativo.FormaPagoCheque;
import cpc.negocio.demeter.administrativo.NegocioCheque;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContFormaPagoCheque implements EventListener, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AppDemeter 				app;
	private UiDepositoCheque		vista;
	private ContRecibo				llamador;
	private FormaPagoCheque			modelo;
	
	public ContFormaPagoCheque(int modoOperacion,ContRecibo llamador, AppDemeter app,FormaPagoCheque modelo) throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcFiltroExcepcion
	{
		this.app = app;
		this.modelo = modelo ;
		try {
			this.llamador = llamador;
			vista = new UiDepositoCheque("FORMA PAGO ","900px",modelo,NegocioCheque.getInstance().getchequesNoUsados());
			setear(modoOperacion,modelo, vista, app);
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		}
	}

	private void setear(int modoOperacion,FormaPagoCheque obj, UiDepositoCheque vista , AppDemeter app2) 
	{
		try {
			vista.setMode("modal");
			vista.setModelo(modelo);
			vista.setControlador(this);
			vista.inicializar();
			vista.dibujar();
			vista.cargarValores();
			vista.desactivar(modoOperacion);
		 	app.agregarHija(vista);
			vista.doModal();
		
		} catch (Exception e) {
			e.printStackTrace();
			//throw new ExcDatosInvalidos(e.getMessage());
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente, WrongValueException, ExcFiltroExcepcion {
		
		if(vista.getFecha().getValue() == null )
		{
			throw new WrongValueException(vista.getFecha()," Fecha Invalida  ");
		}
		
		 

		if(vista.getNroCheque().getValue().trim().length() == 0 || vista.getNroCuenta().getValue().trim().length() == 0 )
		{
			throw new WrongValueException(vista," Seleccione al menos 1 cheque ");
		}

	}

	public void onEvent(Event arg0) throws Exception {

		if(arg0.getTarget() == vista.getAceptar())
		{
			validar();
			FormaPagoCheque  modelo = vista.getModelo();
			llamador.agregarFormaPago(modelo);
			vista.detach();
		}
		else if (vista.getCancelar() == arg0.getTarget())
		{
			vista.detach();
		}
		else if(vista.getAgregar() == arg0.getTarget())
		{
			new ContSeleccionarCheque(Accion.AGREGAR,this,app);
		}
	}

	public void agregarcheque (Cheque cheque)
	{
		vista.agregarCheque(cheque);
	}
	
}
