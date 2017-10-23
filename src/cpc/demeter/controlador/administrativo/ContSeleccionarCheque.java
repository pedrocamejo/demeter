package cpc.demeter.controlador.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listitem;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.administrativo.ContFormaPagoDeposito;
import cpc.demeter.vista.administrativo.UiSeleccionarCheque;
import cpc.modelo.demeter.administrativo.Cheque;
import cpc.negocio.demeter.administrativo.NegocioCheque;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContSeleccionarCheque implements EventListener {

	private NegocioCheque 		 	negocio;
	private AppDemeter 			 	app;
	private Object    			    llamador;
	private UiSeleccionarCheque  	vista;
	private Integer 				modoOperacion;

	public ContSeleccionarCheque(Integer modoOperaion,ContFormaPago llamador,AppDemeter app) throws SuspendNotAllowedException, InterruptedException, ExcFiltroExcepcion {
		this.app = app;
		this.setLlamador(llamador);
		this.setModoOperacion(modoOperaion);
		negocio = NegocioCheque.getInstance();
		vista = new  UiSeleccionarCheque("Seleccionar Cheque ","none",true,this,negocio.getchequesNoUsados());
		app.agregarHija(vista);
		vista.modoOperacion(modoOperaion);
		vista.doModal();
		
	}

	public ContSeleccionarCheque(int modoOperaion, Object llamador, AppDemeter app) throws SuspendNotAllowedException, InterruptedException, ExcFiltroExcepcion {
		// TODO Auto-generated constructor stub
		this.app = app;
		this.setLlamador(llamador);
		this.setModoOperacion(modoOperaion);
		negocio = NegocioCheque.getInstance();
		vista = new  UiSeleccionarCheque("Seleccionar Cheque ","none",true,this,negocio.getchequesNoUsados());
		app.agregarHija(vista);
		vista.modoOperacion(modoOperaion);
		vista.doModal();
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub
		if (vista.getBuscar() == arg0.getTarget() || vista.getBusquedad() == arg0.getTarget())
		{
			if (vista.getCombobusquedad().getSelectedItem() != null)
			{
				Integer indice = (Integer) vista.getCombobusquedad().getSelectedItem().getValue();
				vista.getCheques().setModel(new ListModelList(getBusquedad(indice, vista.getBusquedad().getText())));
			} 
	   }
	   else if (vista.getAceptar() == arg0.getTarget())
	   {
		   if(vista.getCheques().getSelectedItem() != null)
		   {
			   Cheque cheque = (Cheque) vista.getCheques().getSelectedItem().getValue();
			   System.out.println("Cheque Seleccionado <--"+cheque);
			   seleccionar(cheque);
			   vista.detach();
		   }
	   }
	   else if (vista.getCancelar() == arg0.getTarget()) 
	   {
		   vista.detach();
	   }
	   else if(vista.getAgregar() == arg0.getTarget())
	   {
		   new ContCheque(Accion.AGREGAR,new Cheque(),this,this.app);
	   }

	}

	private List<Cheque> getBusquedad(Integer indice, String busquedad)
	{
		// TODO Auto-generated method stub
		if (busquedad.trim().length() == 0)
		{
			return vista.getModelo();
		}

		List<Cheque> lista = new ArrayList<Cheque>();

		for (Cheque cheque : vista.getModelo())
		{
			switch (indice) 
			{
			case 0:
				if (cheque.getId().toString().trim().toUpperCase().contains(busquedad.trim().toUpperCase()))
				{
					lista.add(cheque);
				}
				break;
			case 1:
				if (cheque.getNroCheque().trim().toUpperCase().contains(busquedad.trim().toUpperCase()))
				{
					lista.add(cheque);
				}
				break;
			case 2:
				if (cheque.getNroCuenta().trim().toUpperCase().contains(busquedad.trim().toUpperCase()))
				{
					lista.add(cheque);
				}
				break;
			case 3:
				if (cheque.getMonto().toString().trim().toUpperCase().contains(busquedad.trim().toUpperCase()))
				{
					lista.add(cheque);
				}
				break;
			}
		}
		return lista;
	}

	public EventListener seleccionarLista()
	{
		// TODO Auto-generated method stub
		return new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				Listitem itme = (Listitem) arg0.getTarget();
				Cheque cheque =(Cheque) itme.getValue();
				
				seleccionar(cheque);
			}
		};
	}

	protected void seleccionar(Cheque cheque) throws InterruptedException {
		
		if(llamador.getClass().equals(ContFormaPagoDeposito.class))
		{
			ContFormaPagoDeposito formaPago = (ContFormaPagoDeposito) llamador;
			formaPago.agregarCheque(cheque);
		}
		else if (llamador.getClass().equals(ContFormaPagoCheque.class))
		{
			ContFormaPagoCheque formaPagoCheque = (ContFormaPagoCheque) llamador;
			formaPagoCheque.agregarcheque(cheque);
		}
		vista.detach();
	}
 

	public Object getLlamador() {
		return llamador;
	}

	public void setLlamador(Object llamador) {
		this.llamador = llamador;
	}

	public Integer getModoOperacion() {
		return modoOperacion;
	}

	public void setModoOperacion(Integer modoOperacion) {
		this.modoOperacion = modoOperacion;
	}

}
