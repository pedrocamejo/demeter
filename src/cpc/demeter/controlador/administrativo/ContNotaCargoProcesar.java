package cpc.demeter.controlador.administrativo;


 
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContNotasCargo;
import cpc.demeter.vista.administrativo.UINotaCargoProcesar;
import cpc.modelo.demeter.administrativo.NotaCargo;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.demeter.administrativo.ReciboNotaCargo;
import cpc.negocio.demeter.administrativo.NegocioNotaCargo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContNotaCargoProcesar extends ContVentanaBase<NotaCargo> {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = -8813118455843546959L;
	private NegocioNotaCargo 		servicio;
	private UINotaCargoProcesar 	vista;
	private AppDemeter 			app;
	
	
	public ContNotaCargoProcesar(int modoOperacion,NotaCargo notaCargo,	ContNotasCargo llamador, AppDemeter app) throws ExcDatosInvalidos {
		super(modoOperacion);
		this.app = app;
		servicio = NegocioNotaCargo.getInstance();
		setear(notaCargo, new UINotaCargoProcesar(" Pagar Nota de Cargos (" + Accion.TEXTO[modoOperacion] + ")", 900), llamador, this.app);
		vista = (UINotaCargoProcesar) getVista();
	
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub
		
		if (arg0.getTarget() == vista.getAgregar())
		{
			if (vista.getSaldo().getValue() <= 0 )
			{
				Messagebox.show("Nota Saldada ");
			}
			else
			{
				new ContSeleccionarRecibo(Accion.AGREGAR,this,app, vista.getModelo().getCliente());
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
				ReciboNotaCargo recibo =  (ReciboNotaCargo) vista.getListRecibos().getSelectedItem().getValue();
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

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
	
		
	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		if(vista.getRecibos().size() == 0)
		{
			 throw new  WrongValueException(vista.getListRecibos(),"Debe Agregar al Menos un recibo"); 
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
	try {
			
			validar();
			
			vista.actualizarModelo();
			NotaCargo nota = vista.getModelo();
			nota.setRecibos(vista.getRecibos());
			
			servicio.pagarNotaCargo(nota);
			refrescarCatalogo();
			vista.detach();
			
		} catch (WrongValuesException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExcEntradaInconsistente e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void agregarRecibo(Recibo recibo) {
		// TODO Auto-generated method stub
		for(ReciboNotaCargo reciboVista: vista.getRecibos())
		{
			if(recibo.getId().equals(reciboVista.getRecibo().getId()))
			{
				return;
			}
		}
		ReciboNotaCargo nuevoRecibo = new ReciboNotaCargo();
		nuevoRecibo.setNotaCargo(getDato());
		nuevoRecibo.setRecibo(recibo);
		vista.agregarRecibo(nuevoRecibo);
	}
}