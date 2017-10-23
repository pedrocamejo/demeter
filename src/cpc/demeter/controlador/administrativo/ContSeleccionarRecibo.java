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
import cpc.demeter.vista.administrativo.UiSeleccionarRecibo;
import cpc.modelo.demeter.administrativo.Cheque;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.ministerio.gestion.Cliente;

import cpc.negocio.demeter.administrativo.NegocioRecibo;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContSeleccionarRecibo  implements EventListener {

	private NegocioRecibo 		 	negocio;
	private AppDemeter 			 	app;
	private Object    			    llamador;
	private UiSeleccionarRecibo  	vista;
	private Integer 				modoOperacion;
	private Cliente				    cliente;

	public ContSeleccionarRecibo(Integer modoOperaion,Object llamador,AppDemeter app,Cliente cliente ) throws SuspendNotAllowedException, InterruptedException, ExcFiltroExcepcion {
		this.app = app;
		this.setLlamador(llamador);
		this.setModoOperacion(modoOperaion);
		this.cliente = cliente;
		negocio = NegocioRecibo.getInstance();
		vista = new  UiSeleccionarRecibo("Seleccionar Recibo","none",true,this,negocio.getRecibos(cliente));
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
				vista.getRecibos().setModel(new ListModelList(getBusquedad(indice, vista.getBusquedad().getText())));
			} 
	   }
	   else if (vista.getAceptar() == arg0.getTarget())
	   {
		   if(vista.getRecibos().getSelectedItem() != null)
		   {
			   Recibo recibo = (Recibo) vista.getRecibos().getSelectedItem().getValue();
			   seleccionar(recibo);
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
		for (Recibo recibo : vista.getModelo())
		{
			switch (indice) 
			{
			case 0:
				if (recibo.getControl().toUpperCase().trim().contains(busquedad))
				{
					lista.add(recibo);
				}
				break;
			case 1:
				if (recibo.getStrFecha().trim().toUpperCase().contains(busquedad))
				{
					lista.add(recibo);
				}
				break;
			case 2:
				if (recibo.getStrMonto().trim().toUpperCase().contains(busquedad))
				{
					lista.add(recibo);
				}
				break;
			case 3:
				if (recibo.getStrSaldo().trim().toUpperCase().contains(busquedad))
				{
					lista.add(recibo);
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
				Recibo recibo =(Recibo) itme.getValue();
				seleccionar(recibo);
			}
		};
	}

	
	//depende el llamador agrega el recibo a la lista 
	protected void seleccionar(Recibo recibo) throws InterruptedException {
 
		if(llamador.getClass().equals(ContFacturaProcesar.class))
		{
			ContFacturaProcesar contFacturaProcesar = (ContFacturaProcesar) llamador;
			contFacturaProcesar.agregarRecibo(recibo);
		}
		
		else if(llamador.getClass().equals(ContReintegro.class))
		{
			ContReintegro contReintegro = (ContReintegro) llamador;
			contReintegro.agregarRecibo(recibo);
		}
		else if(llamador.getClass().equals(ContNotaDebitoRecibo.class))
		{
			ContNotaDebitoRecibo contNota = (ContNotaDebitoRecibo) llamador;
			contNota.agregarRecibo(recibo);
		}
		else if(llamador.getClass().equals(ContNotaCargoProcesar.class))
		{
			ContNotaCargoProcesar contNotaCargo = (ContNotaCargoProcesar) llamador;
			contNotaCargo.agregarRecibo(recibo);
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
