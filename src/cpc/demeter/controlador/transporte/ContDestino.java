package cpc.demeter.controlador.transporte;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;



import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.transporte.ContDestinos;
import cpc.demeter.vista.transporte.UIDestino;
import cpc.modelo.demeter.transporte.Destino;
import cpc.modelo.ministerio.dimension.UbicacionEstado;
import cpc.modelo.ministerio.dimension.UbicacionMunicipio;
import cpc.modelo.ministerio.dimension.UbicacionParroquia;
import cpc.modelo.ministerio.dimension.UbicacionSector;
import cpc.modelo.sigesp.indice.ParroquiaPK;
import cpc.negocio.demeter.mantenimiento.NegocioEnteExterno;
import cpc.negocio.demeter.transporte.NegocioDestino;
import cpc.negocio.ministerio.basico.NegocioEstado;
import cpc.negocio.ministerio.basico.NegocioMunicipio;
import cpc.negocio.ministerio.basico.NegocioSector;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContDestino extends ContVentanaBase<Destino>{

	private AppDemeter app;
	private UIDestino vista;
	private Destino destino;
	
	private NegocioDestino servicio;
	
	private static final long serialVersionUID = 1L;
 
	public ContDestino(int modoOperacion,Destino destino, ContDestinos llamador, AppDemeter app) throws ExcDatosInvalidos, ExcFiltroExcepcion  
	{
		super(modoOperacion);
		this.app = app;
		this.destino = destino;
		if (this.destino == null || modoAgregar())
		{
			this.destino= new Destino();
		} 
		setear(this.destino, new UIDestino("Destinos ("+Accion.TEXTO[modoOperacion]+")", 700), llamador, app);
		vista = ((UIDestino)getVista()); 
		servicio =  NegocioDestino.getInstance();
		List<UbicacionEstado> estados = NegocioEstado.getInstance().getTodos();
		vista.CargarListados(estados);
		vista.desactivar(modoOperacion);
	}


	@Override
	public void guardar() throws ExcFiltroExcepcion {
			try {
				servicio =  NegocioDestino.getInstance();
				Destino destino = getDato();
				destino.setDescripcion(vista.getDestino().getValue());
				destino.setSector((UbicacionSector) vista.getSector().getSeleccion());
				servicio.Guardar(destino);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {		
		if (getModoOperacion()!= Accion.ELIMINAR && (vista.getDestino().getValue()==null || vista.getDestino().getValue()==""))
		{			
			throw new  WrongValueException(((UIDestino) getVista()).getDestino() ,"Indique una Destino");
		}		
		if(vista.getDestino().getValue().trim().length() < 10)
		{
			throw new  WrongValueException(((UIDestino) getVista()).getDestino() ,"La descripcion Debe Tener al menos 10 caracteres");
		}
		if(vista.getSector().getSeleccion() == null)
		{
			throw new  WrongValueException(((UIDestino) getVista()).getDestino() ,"seleccion un Sector ");
		}

	}

	
	public void onEvent(Event arg0) throws Exception {
		
		if(vista.getEstado() == arg0.getTarget())
		{
			UbicacionEstado estado = (UbicacionEstado) vista.getEstado().getSeleccion();
			List<UbicacionMunicipio> municipios = servicio.municipios(estado);
			vista.getMunicipio().setModelo(municipios);
			vista.getParroquia().setModelo(new ArrayList<UbicacionParroquia>());
			vista.getSector().setModelo(new ArrayList<UbicacionSector>());
	   }
	   else if(vista.getMunicipio() == arg0.getTarget())
	   {
	    	UbicacionMunicipio municipio = (UbicacionMunicipio) vista.getMunicipio().getSeleccion();
	    	List<UbicacionParroquia> parroquias = servicio.parroquias(municipio);
	    	vista.getParroquia().setModelo(parroquias);
	    	vista.getSector().setModelo(new ArrayList<UbicacionSector>()); 
	   }
	   else if(vista.getParroquia() == arg0.getTarget())
	   {
	    	UbicacionParroquia parroquia = (UbicacionParroquia) vista.getParroquia().getSeleccion();
	    	List<UbicacionSector> sectores = servicio.sectores(parroquia);
	    	vista.getSector().setModelo(sectores);
	   }		
	   else
	   {
			validar();
	   	    procesarCRUD(arg0);
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

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		try {
			servicio =  NegocioDestino.getInstance();
			Destino destino = getDato();
			servicio.Eliminar(destino);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
  
}
