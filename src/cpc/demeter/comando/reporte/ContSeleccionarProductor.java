package cpc.demeter.comando.reporte;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listitem;

import cpc.demeter.controlador.administrativo.ContFormaPagoCheque;
import cpc.demeter.vista.gestion.UISeleccionarProductor;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContSeleccionarProductor  implements EventListener  {
	
	private IZkAplicacion 			 	app;
	private ComandoEstadosCuenta    	llamador;
	private UISeleccionarProductor  	vista;
	private Integer 				    modoOperacion; // 1 Solvente 2 Deudor 

	public ContSeleccionarProductor(Integer modoOperaion,ComandoEstadosCuenta llamador,IZkAplicacion app,List<Productor> productores) throws SuspendNotAllowedException, InterruptedException, ExcFiltroExcepcion {
		this.app = app;
		this.llamador = llamador;
		this.setModoOperacion(modoOperaion);
		vista = new  UISeleccionarProductor("Seleccionar Productor ","none",true,this,productores);
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
				vista.getProductores().setModel(new ListModelList(getBusquedad(indice, vista.getBusquedad().getText())));
			} 
	   }
	   else if (vista.getAceptar() == arg0.getTarget())
	   {
		   if(vista.getProductores().getSelectedItem() != null)
		   {
			   Productor productor = (Productor) vista.getProductores().getSelectedItem().getValue();
			   seleccionar(productor);
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
		if (busquedad.trim().length() == 0)
		{
			return vista.getModelo();
		}

		List lista = new ArrayList();
		
		for (Productor productor : vista.getModelo())
		{
			switch (indice) 
			{
			case 0:
				if (productor.getIdentidadLegal().toString().trim().toUpperCase().contains(busquedad.trim().toUpperCase()))
				{
					lista.add(productor);
				}
				break;
			case 1:
				if (productor.getNombres().trim().toUpperCase().contains(busquedad.trim().toUpperCase()))
				{
					lista.add(productor);
				}
				break;
			case 2:
				if (productor.getDireccion().trim().toUpperCase().contains(busquedad.trim().toUpperCase()))
				{
					lista.add(productor);
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
				Productor productor =(Productor) itme.getValue();
				seleccionar(productor);
			}
		};
	}

	protected void seleccionar(Productor productor) throws InterruptedException {
		
		if(modoOperacion == 1 ) // este es solventes
		{
			llamador.agregarSolvente(productor);
		}
		if(modoOperacion == 2 ) // este es deudor
		{
			llamador.agregarDeudor(productor);
		}

	}
  
 
	public Integer getModoOperacion() {
		return modoOperacion;
	}

	public void setModoOperacion(Integer modoOperacion) {
		this.modoOperacion = modoOperacion;
	}


}
