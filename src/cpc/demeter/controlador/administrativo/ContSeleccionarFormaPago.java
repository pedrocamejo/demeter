package cpc.demeter.controlador.administrativo;


import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listitem;

import cpc.demeter.AppDemeter;
import cpc.demeter.vista.administrativo.UISeleccionarFormaPago;
import cpc.modelo.demeter.administrativo.FormaPago;
import cpc.modelo.demeter.administrativo.FormaPagoCheque;
import cpc.modelo.demeter.administrativo.FormaPagoEfectivo;
import cpc.negocio.demeter.administrativo.NegocioFormaPago;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContSeleccionarFormaPago  implements EventListener {

	private NegocioFormaPago 		 	negocio;
	private AppDemeter 			 		app;
	private Object    			  	  llamador;
	private UISeleccionarFormaPago  	vista;
	private Integer 					modoOperacion;

	public ContSeleccionarFormaPago(Integer modoOperaion,Object llamador,AppDemeter app ) throws SuspendNotAllowedException, InterruptedException, ExcFiltroExcepcion {
		this.app = app;
		this.setLlamador(llamador);
		this.setModoOperacion(modoOperaion);

		negocio = NegocioFormaPago.getInstance();
		vista = new  UISeleccionarFormaPago("Seleccionar Recibo","none",true,this,negocio.getRecibosFormaPagoNoDepositado());
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
				vista.getFormapagos().setModel(new ListModelList(getBusquedad(indice, vista.getBusquedad().getText())));
			} 
	   }
	   else if (vista.getAceptar() == arg0.getTarget())
	   {
		   if(vista.getFormapagos().getSelectedItem() != null)
		   {
			   FormaPago formaPago = (FormaPago) vista.getFormapagos().getSelectedItem().getValue();
			   seleccionar(formaPago);
			   vista.detach();
		   }
	   }
	   else if (vista.getCancelar() == arg0.getTarget()) 
	   {
		   vista.detach();
	   }

	}

	private List getBusquedad(Integer indice, String busquedad)
	{
		// TODO Auto-generated method stub
		busquedad = busquedad.trim().toUpperCase();
		
		if (busquedad.length() == 0)
		{
			return vista.getModelo();
		}
		List lista = new ArrayList();
		for (FormaPago formaPago : vista.getModelo())
		{
			
						
			switch (indice) 
			{
			case 0:
				if (formaPago.getRecibo().getControl().toUpperCase().trim().contains(busquedad))
				{
					lista.add(formaPago);
				}
				break;
			case 1:
				if (formaPago.getStrFecha().trim().toUpperCase().contains(busquedad))
				{
					lista.add(formaPago);
				}
				break;
			case 2:
				if (formaPago.getTipoFormaPago().getDescripcion().trim().toUpperCase().contains(busquedad))
				{
					lista.add(formaPago);
				}
				break;
			case 3:
				String detalle = new String();
				if (formaPago.getClass().equals(FormaPagoCheque.class))
				{
					FormaPagoCheque formaPagoCheque = (FormaPagoCheque) formaPago;
					detalle = formaPagoCheque.detalle();
				}
				if (formaPago.getClass().equals(FormaPagoEfectivo.class))
				{
					detalle = "PAGO EFECTIVO ";
				}
				if (detalle.trim().toUpperCase().contains(busquedad))
				{
					lista.add(formaPago);
				}
				break;
			case 4:
				if (formaPago.getStrMonto().trim().toUpperCase().contains(busquedad))
				{
					lista.add(formaPago);
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
				FormaPago recibo =(FormaPago) itme.getValue();
				seleccionar(recibo);
			}
		};
	}

	protected void seleccionar(FormaPago formaPago) throws InterruptedException {
 
		if(llamador.getClass().equals(ContDeposito.class))
		{
			ContDeposito cont = (ContDeposito) llamador;
			cont.agregar_FormaPago(formaPago);
		}

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
