package cpc.demeter.controlador.gestion;
 

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.gestion.UIMaquinariaImpropiaProcesar;
import cpc.modelo.demeter.gestion.EstadoMaquinariaImpropia;
import cpc.modelo.demeter.gestion.MaquinariaImpropia;
import cpc.negocio.demeter.gestion.NegocioMaquinariaImpropia;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContMaquinariaImpropiaProcesar extends ContVentanaBase<MaquinariaImpropia> {

	private NegocioMaquinariaImpropia			 servicio;
	private UIMaquinariaImpropiaProcesar		 vista;
	private AppDemeter							 app;

	public ContMaquinariaImpropiaProcesar(int modoOperacion, MaquinariaImpropia maquinaria, ContCatalogo<MaquinariaImpropia> llamador, AppDemeter app) throws ExcDatosInvalidos, ExcFiltroExcepcion {
		super(modoOperacion);
		this.app = app;
		this.servicio = NegocioMaquinariaImpropia.getInstance();
		
		List estados = new ArrayList();
		
		if(modoAnular())
		{
			estados.add(servicio.getDesactivado());
			estados.add(servicio.getDesincorporado());
			estados.add(servicio.getMigrado());
		}

		vista = new UIMaquinariaImpropiaProcesar("Maquinaria Impropia Procesar ",900, this.app,modoOperacion,estados);
		setear(maquinaria, vista , llamador, this.app);
		this.vista = (UIMaquinariaImpropiaProcesar) getVista();
		this.vista.desactivar(modoOperacion);
		
	}

	
	public void eliminar() {

	}

	public void guardar() {
		
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if(Accion.PROCESAR == getModoOperacion())
		{
		// validar unicidad Serial Chasis Serial Otro 
			if (vista.getSerialChasis().getValue().trim().length() == 0)
			{
				throw new WrongValueException(vista.getSerialChasis(),"Ingrese el Serial de Chasis de la Maquinaria ");
			}
			
			if (vista.getSerialOtro().getValue().trim().length() == 0)
			{
				throw new WrongValueException(vista.getSerialOtro(),"Ingrese Otro  Serial de la Maquinaria ");
			}
	
			MaquinariaImpropia maquinaria = servicio.getMaquinaria(vista.getSerialChasis().getValue());
			if(maquinaria != null )
			{
				if (!maquinaria.getId().equals(getDato().getId()))
				{
					throw new WrongValueException(vista.getSerialChasis(),"Este Serial ah sido Asignado a Otra Maquinaria ");
				}
			}
			
			maquinaria = servicio.getMaquinariaOtroSerial(vista.getSerialOtro().getValue());
			if(maquinaria != null )
			{
				if (!maquinaria.getId().equals(getDato().getId()))
				{
					throw new WrongValueException(vista.getSerialOtro(),"Este Serial ah sido Asignado a Otra Maquinaria ");
				}
			}
		}
		if(Accion.ANULAR == getModoOperacion())
		{
			if(vista.getEstado().getSelectedItem() == null ){
				throw new WrongValueException(vista.getEstado()," Seleccione el Estado de la Maquinaria ");
			}
		}

	}

	public void onEvent(Event event) throws Exception 
	{

		if(vista.getAceptar() == event.getTarget() && modoProcesar())
		{
			System.out.println("<--------------------------->");
			procesar();
		}
		else if(vista.getAceptar() == event.getTarget() && modoAnular())
		{
			anular2();
		}
		else if (vista.getEstado() == event.getTarget())
		{
			EstadoMaquinariaImpropia estado = (EstadoMaquinariaImpropia) vista.getEstado().getSelectedItem().getValue();
			if(estado.getId().equals(EstadoMaquinariaImpropia.MIGRADO))
			{
				vista.getLblEstadoInfo().setValue("Maquinaria Convertida a Bien Nacional ");
			}
			else if(estado.getId().equals(EstadoMaquinariaImpropia.DESACTIVADO))
			{
				vista.getLblEstadoInfo().setValue("Maquinaria Desactivada ");
			}
			else if(estado.getId().equals(EstadoMaquinariaImpropia.DESINCORPORADO))
			{
				vista.getLblEstadoInfo().setValue("Maquinaria No Operativa ");
			}
		}
	}


	public void anular2() throws Exception  {
		// TODO Auto-generated method stub
			validar();
			vista.actualizarModelo();
			int respuesta = Messagebox.show("Deseas Verificar esta Maquinaria Luego no Podras Modificarla ","Verificar Maquinaria Impropia ? ",
					Messagebox.YES | Messagebox.NO ,Messagebox.INFORMATION );			

			if(respuesta == Messagebox.YES){
				boolean b = TransactionSynchronizationManager.hasResource("obj");
				if (b)
				{
					TransactionSynchronizationManager.unbindResource("obj");
				}
				EstadoMaquinariaImpropia estado = (EstadoMaquinariaImpropia) vista.getEstado().getSelectedItem().getValue();
				TransactionSynchronizationManager.bindResource("obj", app);
				getDato().setEstado(estado);
				servicio.CambiarEstado(getDato());
				refrescarCatalogo();
				vista.detach();
			}
	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		validar();
		vista.actualizarModelo();
		try {
			
			boolean b = TransactionSynchronizationManager.hasResource("obj");
			if (b)
			{
				TransactionSynchronizationManager.unbindResource("obj");
			}
			TransactionSynchronizationManager.bindResource("obj", app);
			getDato().setEstado(servicio.getVerificado());
			servicio.CambiarEstado(getDato());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			app.mostrarError(e.getMessage());
			e.printStackTrace();
		}
		refrescarCatalogo();
		vista.detach();
	}


	@Override
	public void anular() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		
	}
}