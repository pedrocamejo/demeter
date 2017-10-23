package cpc.demeter.controlador.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.record.formula.ErrPtg;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContCheques;
import cpc.demeter.vista.administrativo.ContFormaPagoDeposito;
import cpc.demeter.vista.administrativo.UiCheque;
import cpc.modelo.demeter.administrativo.Cheque;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.negocio.demeter.administrativo.NegocioBanco;
import cpc.negocio.demeter.administrativo.NegocioCheque;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContCheque extends ContVentanaBase<Cheque> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioCheque 		servicio;
	private AppDemeter 			app;
	private UiCheque 			vista;
	private Object 				llamador; // para agregar un cheque :-D

	
	public ContCheque(int modoOperacion, Cheque cheque, ContCheques llamador,AppDemeter app) throws ExcDatosInvalidos, ExcSeleccionNoValida,ExcFiltroExcepcion {
		super(modoOperacion);
		this.app = app;
		List<Recibo> recibos = new ArrayList<Recibo>();
		servicio = NegocioCheque.getInstance();
		try {
			if (cheque == null || modoAgregar())
			{
				cheque = new Cheque();
			}
			
			if(modoConsulta() || modoEditar() || modoEliminar())
			{
				recibos = servicio.getRecibos(cheque);
				if (modoEditar()){
				for (Recibo recibo : recibos) {
				if (!recibo.isAnulado())
				throw new ExcDatosInvalidos("No se Puede Editar, causa: el cheque esta utilizado en el Recibo N°: "+recibo.getControl());
				}
				}
			}
	
			setear(cheque, new UiCheque("CHEQUE ("+ Accion.TEXTO[modoOperacion] + ")", 550,recibos), llamador, app);
			vista = ((UiCheque) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			
			//this.app.mostrarError("No ha seleccionado ningun campo");
			this.app.mostrarError(e.getMensaje());
		}
	}
 

	public ContCheque(int modoOperacion, Cheque cheque,Object llamador, AppDemeter app) {
		// TODO Auto-generated constructor stub
		super(modoOperacion);
		this.app = app;
		this.llamador = llamador;
		servicio = NegocioCheque.getInstance();
		this.vista =  new UiCheque("CHEQUE ("+ Accion.TEXTO[modoOperacion] + ")", 550,new ArrayList<Recibo>());
		setearII(modoOperacion,cheque);
	}

	private void setearII(int modoOperacion,Cheque cheque) // al especialito :-D 
	{
		try {
			setDato(cheque);
			this.vista.setMode("modal");
			this.vista.setModelo(cheque);
			this.vista.setControlador(this);
			this.vista.inicializar();
			this.vista.dibujar();
			this.vista.cargarValores(getDato());
			this.vista.desactivar(modoOperacion);
			this.app.agregarHija(this.vista);
			this.vista.doModal();
 		} catch (Exception e) {
			e.printStackTrace();
			//throw new ExcDatosInvalidos(e.getMessage());
		}
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub
		
		if(arg0.getTarget() == vista.getAceptar())
		{

			if(llamador == null)
			{
				procesarCRUD(arg0);
			}
			else if (llamador.getClass().equals(ContSeleccionarCheque.class))
			{
				validar();
				vista.actualizarModelo();
				Cheque cheque =  vista.getModelo();
				vista.detach();
				
				boolean b = TransactionSynchronizationManager.hasResource("obj");
				if (b)
				{
					TransactionSynchronizationManager.unbindResource("obj");
				}
				TransactionSynchronizationManager.bindResource("obj", app);
				
				servicio.Guardar(cheque);
				ContSeleccionarCheque contSeleccionarCheque =(ContSeleccionarCheque) llamador;
				contSeleccionarCheque.seleccionar(cheque);
			}
		}

	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		try {
			validar();
			
			servicio.Guardar(getDato());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			app.mostrarError(e.getMessage());
			e.printStackTrace();
			
		}
	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		try {
			servicio.eliminar(getDato());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub

		if (vista.getNroCuenta().getText().trim().length() == 0) 
		{
			throw new WrongValueException(vista.getNroCuenta(),	"Ingrese Un Nuemro de cuenta");
		}
		if (vista.getNroCheque().getValue().trim().length() == 0)
		{
			throw new WrongValueException(vista.getNroCheque(),	"Ingrese el Nro del cheque ");
		}
		
		if (vista.getMonto().getValue() <= 0)
		{
			throw new WrongValueException(vista.getMonto(), " Monto Invalido");
		}
		
		//validar que el Nro de cheque 
		boolean duplicado = false;
		try 
		{
			duplicado = servicio.getnroChequeUsado(vista.getNroCheque().getValue().trim(),vista.getNroCuenta().getValue().trim());
		} catch (WrongValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			
		} 
		if (duplicado && this.getModoOperacion() != Accion.EDITAR)
		{
			throw new WrongValueException(vista.getNroCheque(),	"Este Cheque Ya ah sido Registrado ");
		} 
		
	}

	@Override
	public void anular() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub

	}
 

}
