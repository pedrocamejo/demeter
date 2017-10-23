package cpc.demeter.controlador.transporte;
 

 
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.transporte.ContSolicitudesTransporte;
import cpc.demeter.vista.transporte.UISolicitudTransporte;
import cpc.modelo.demeter.transporte.SolicitudTransporte;
import cpc.negocio.demeter.transporte.NegocioSolicitudTransporte;
import cpc.negocio.ministerio.basico.NegocioSector;
import cpc.negocio.ministerio.basico.NegocioUbicacionTransporte;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente; 
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContSolicitudTransporte extends ContVentanaBase<SolicitudTransporte>{

	private AppDemeter app;

	private UISolicitudTransporte vista;
	private NegocioSolicitudTransporte servicio;
	private static final long serialVersionUID = 1L;
 
	
	public ContSolicitudTransporte(int modoOperacion,SolicitudTransporte solicitud,ContSolicitudesTransporte llamador, AppDemeter app) throws ExcDatosInvalidos, ExcFiltroExcepcion  
	{
		super(modoOperacion);
		this.app = app; 
		servicio = NegocioSolicitudTransporte.getInstance();
		try {
			if (solicitud == null || modoAgregar())
			{
				solicitud = new SolicitudTransporte();
			}
			vista =  new UISolicitudTransporte("Solicitud ("+ Accion.TEXTO[modoOperacion] + ")", 850, 
							servicio.getGerencias(),NegocioSector.getInstance().getTodos());
			setear(solicitud,vista, llamador, app);
			vista.desactivar(modoOperacion);

		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		}

	}
	
	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub
			
	
		procesarCRUD(arg0);
	}


	@Override
	public void anular() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		try {
			servicio.anular(getDato());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		try {
			validar();
			servicio.guardar(getDato());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			app.mostrarError(e.getMessage());
			e.printStackTrace();
			
		}

	}


	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		
		
		
		
	}


	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		if (vista.getGerencia().getSelectedItem() == null) 
		{
			throw new WrongValueException(vista.getGerencia(),"Seleccione ");
		}
		if (vista.getSector().getSeleccion() == null) 
		{
			throw new WrongValueException(vista.getSector(),"Seleccione ");
		}
		
		if (vista.getFechasalida().getValue()== null) 
		{
			throw new WrongValueException(vista.getFechasalida(),"Seleccione ");
		}
		if (vista.getFechallegada().getValue() == null) 
		{
			throw new WrongValueException(vista.getFechallegada(),"Seleccione ");
		}
		if (vista.getLugarSalida().getValue().trim().length() ==  0 ) 
		{
			throw new WrongValueException(vista.getLugarSalida()," Ingrese un Valor");
		}
		if (vista.getMotivo().getValue().trim().length() ==  0 ) 
		{
			throw new WrongValueException(vista.getMotivo()," Ingrese un Valor");
		}
		if (vista.getNroPasajero().getValue()== null ) 
		{
			throw new WrongValueException(vista.getNroPasajero()," Ingrese un Valor");
		}	
		
		if(getModoOperacion() == Accion.PROCESAR)
		{
			if (vista.getProcesar().getSelectedItem() == null) 
			{
				throw new WrongValueException(vista.getGerencia(),"Seleccione ");
			}
			if (vista.getJustificacion().getValue().trim().length() ==  0 ) 
			{
				throw new WrongValueException(vista.getJustificacion()," Ingrese un Valor");
			}
		
		}


	} 
}
