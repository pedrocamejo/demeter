package cpc.demeter.controlador.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

import cpc.demeter.AppDemeter;
import cpc.demeter.vista.administrativo.UiSeleccionarNotaCredito;
import cpc.demeter.vista.administrativo.UiSeleccionarRecibo;
import cpc.modelo.demeter.administrativo.NotaCredito;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.negocio.demeter.administrativo.NegocioNotaCredito;
import cpc.negocio.demeter.administrativo.NegocioRecibo;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContSeleccionarNotaCredito implements EventListener {

	private NegocioNotaCredito 	 	negocio;
	private AppDemeter 			 	app;
	private Object    			    llamador;
	private UiSeleccionarNotaCredito  	vista;
	private Integer 				modoOperacion;
	private Cliente				    cliente;

	public ContSeleccionarNotaCredito(Integer modoOperaion,Object llamador,AppDemeter app,Cliente cliente ) throws SuspendNotAllowedException, InterruptedException, ExcFiltroExcepcion {
		this.app = app;
		this.setLlamador(llamador);
		this.setModoOperacion(modoOperaion);
		this.cliente = cliente;
		negocio = NegocioNotaCredito.getInstance();
		vista = new  UiSeleccionarNotaCredito("Seleccionar Nota Credito ","none",true,this,negocio.getNotaCreditosConSaldo(cliente));
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
				vista.getNotasCredito().setModel(new ListModelList(getBusquedad(indice, vista.getBusquedad().getText())));
			} 
	   }
	   else if (vista.getAceptar() == arg0.getTarget())
	   {

		   if(vista.getNotasCredito().getSelectedItem() != null)
		   {
			   NotaCredito nota = (NotaCredito) vista.getNotasCredito().getSelectedItem().getValue();
			   seleccionar(nota);
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
		for (NotaCredito credito : vista.getModelo())
		{
			switch (indice) 
			{
			case 0:
				if (credito.getStrNroDocumento().toUpperCase().trim().contains(busquedad))
				{
					lista.add(credito);
				}
				break;
			case 1:
				if (credito.getStrFacturaAfectada().trim().toUpperCase().contains(busquedad))
				{
					lista.add(credito);
				}
				break;
			case 2:
				if (credito.getStrFecha().trim().toUpperCase().contains(busquedad))
				{
					lista.add(credito);
				}
				break;
			case 3:
				if (credito.getStrTotal().trim().toUpperCase().contains(busquedad))
				{
					lista.add(credito);
				}
				break;
			case 4:
				 if(credito.getStrSaldo().trim().toUpperCase().contains(busquedad))
				 {
					 lista.add(credito);
				 }
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
				NotaCredito notaCredito =(NotaCredito) itme.getValue();
				seleccionar(notaCredito);
			}
		};
	}

	protected void seleccionar(NotaCredito nota) throws InterruptedException {

		if(llamador.getClass().equals(ContReintegro.class))
		{
			ContReintegro contReintegro = (ContReintegro) llamador;
			contReintegro.agregarNota(nota);
			vista.onClose();
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

