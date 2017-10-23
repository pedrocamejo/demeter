package cpc.demeter.controlador.transporte;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.ministerio.UiSector;
import cpc.demeter.vista.transporte.UIUbicacionTransporte;
import cpc.modelo.demeter.interfaz.IDaoGenerico;
import cpc.modelo.demeter.transporte.UbicacionTransporte;
import cpc.modelo.ministerio.dimension.UbicacionMunicipio;
import cpc.modelo.ministerio.dimension.UbicacionParroquia;
import cpc.modelo.ministerio.dimension.UbicacionSector;
import cpc.negocio.ministerio.NegocioGenerico;
import cpc.negocio.ministerio.basico.NegocioSector;
import cpc.negocio.ministerio.basico.NegocioUbicacionTransporte;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cpc.zk.componente.ventanas.CompGrupoBusqueda;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContUbicacionTransporte extends ContVentanaBase<UbicacionTransporte> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3538357002794019527L;
	private NegocioUbicacionTransporte servicio;
	private AppDemeter app;
	private UIUbicacionTransporte vista;

	public  ContUbicacionTransporte(int modoOperacion, UbicacionTransporte tipo,
			ICompCatalogo<UbicacionTransporte> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			if (tipo == null || modoAgregar())
				tipo = new UbicacionTransporte();
		
			servicio = NegocioUbicacionTransporte.getInstance();
			List<UbicacionParroquia> parroquias= servicio.getUbicacionesParroquias();
			List<UbicacionMunicipio> Municipios= servicio.getUbicacionesMunicipios();
			List<UbicacionSector> sectores= servicio.getUbicacionesSectores();
			setear(tipo, new UIUbicacionTransporte("Ubicacion (" + Accion.TEXTO[modoOperacion]
					+ ")", 550, Municipios,parroquias,sectores), llamador, app);
			vista = ((UIUbicacionTransporte) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void eliminar() {
/*
		try {
			servicio = NegocioSector.getInstance();
			servicio.setModelo(getDato());
			servicio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}*/
	}

	public void guardar() {

		try {
			vista.actualizarModelo();
		/*	UbicacionTransporte direccionO = new UbicacionTransporte();
			direccionO.setDireccion(vistaCotizacion.getDireccionOrigen().getValue());
			direccionO.setReferencia(vistaCotizacion.getRefereciaOrigen().getValue());
			direccionO.setUbicacionMunicipio(vistaCotizacion.getCmbMnunicipioOrigen().getSeleccion());
			direccionO.setUbicacionParroquia(vistaCotizacion.getCmbParroquiaOrigen().getSeleccion());
			direccionO.setUbicacionSector(vistaCotizacion.getCmbsectorOrigen().getSeleccion());*/
		//	servicio = NegocioSector.getInstance();
			//servicio.setModelo(getDato());
			getDato().setUbicacionMunicipio(vista.getCmbMunicipio().getSeleccion());
			getDato().setUbicacionParroquia(vista.getCmbParroquia().getSeleccion());
			getDato().setUbicacionSector(vista.getCmbsector().getSeleccion());
			getDato().setDireccion(vista.getDireccion().getValue());
			getDato().setReferencia(vista.getReferecia().getValue());
			servicio.guardar(getDato());
			setDato(getDato());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		
	if(	vista.getCmbMunicipio().getSeleccion()==null){
		throw new WrongValueException(vista.getCmbMunicipio(),
				"Indique un munipio");
	};
/*	if(	vista.getCmbParroquia().getSeleccion()==null){
		throw new WrongValueException(vista.getCmbParroquia(),
				"Indique una parroquia");
	};
	if(	vista.getCmbsector().getSeleccion()==null){
		throw new WrongValueException(vista.getCmbsector(),
				"Indique un sector");
	};*/
	if(	vista.getDireccion().getValue().isEmpty()){
		throw new WrongValueException(	vista.getDireccion(),
				"Debe indicar la direccion ");
	};
/*	if(	vista.getReferecia().getValue().isEmpty()){
		throw new WrongValueException(vista.getReferecia(),
				"Indique un punto de referencia");
	};*/
		
		
		/*
		if (getModoOperacion() != Accion.ELIMINAR
				&& (vista.getDescripcion().getValue() == null || vista
						.getDescripcion().getValue() == "")) {
			throw new WrongValueException(vista.getDescripcion(),
					"Indique una Descripcion");
		}
		if (getModoOperacion() != Accion.ELIMINAR
				&& vista.getParroquia().getSeleccion() == null) {
			throw new WrongValueException(vista.getParroquia(),
					"Indique una Parroquia");
		}
		if (getModoOperacion() == Accion.AGREGAR) {
			try {
				List<UbicacionSector> sectores = servicio
						.getTodosPorNombreYParroquia(vista.getDescripcion()
								.getValue(), vista.getParroquia()
								.getSeleccion());
				if (sectores.size() > 0) {
					throw new WrongValueException(vista.getDescripcion(),
							"Esta parroquia ya se encuentra registrada");
				}
			} catch (ExcFiltroExcepcion e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	}

	public void onEvent(Event arg0) throws Exception {
		if (isDirecciones(arg0)){
			ActualizarDireccion(arg0);
		}else {
		
		validar();
		try {
			procesarCRUD(arg0);
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError("problemas al almacenar sector");
		}
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
	
	
	
	
	public boolean isDirecciones(Event event) {
		List<CompBuscar> lista = new ArrayList<CompBuscar>();

		lista.add(vista.getCmbMunicipio());
		
		lista.add(vista.getCmbParroquia());
		
		lista.add(vista.getCmbsector());
		
		

		
		if (lista.contains(event.getTarget())) {
			return true;
		} else
			return false;
	}

	public void ActualizarDireccion(Event event) throws ExcFiltroExcepcion {

		if (event.getTarget().equals(vista.getCmbMunicipio())) {
			//vista.getCmbParroquia().setModelo(servicio.getUbicacionesParroquias((vista.getCmbMunicipio().getSeleccion())));
			UbicacionMunicipio municipio = vista.getCmbMunicipio().getSeleccion();
			System.out.println(municipio.toString());
			vista.getCmbMunicipio().setText(municipio.getNombre());
			vista.getCmbParroquia().setModelo(servicio.getUbicacionesParroquias(municipio));
			vista.getCmbsector().setSeleccion(null);
			vista.getCmbParroquia().setSeleccion(null);
		};
	
		
		if (event.getTarget().equals(vista.getCmbParroquia())) {
			UbicacionParroquia parroquia = vista.getCmbParroquia().getSeleccion();
			UbicacionMunicipio municipio = parroquia.getMunicipio();
			
			vista.getCmbMunicipio().setSeleccion(municipio);
			vista.getCmbMunicipio().setText(municipio.getNombre());
			vista.getCmbsector().setModelo(servicio.getUbicacionesSectores(parroquia));
			vista.getCmbsector().setSeleccion(null);
		};
		
		if (event.getTarget().equals(vista.getCmbsector())) {
			UbicacionSector sector = vista.getCmbsector().getSeleccion();
			System.out.println(sector);
			UbicacionMunicipio municipio = sector.getParroquia().getMunicipio();
			System.out.println(municipio);
			 UbicacionParroquia parroquia = sector.getParroquia();
			 System.out.println(parroquia);
			 vista.getCmbMunicipio().setModelo(servicio.getUbicacionesMunicipios());
			 vista.getCmbParroquia().setModelo(servicio.getUbicacionesParroquias(municipio));
			vista.getCmbMunicipio().setSeleccion(municipio);
			vista.getCmbMunicipio().setText(municipio.getNombre());;
			vista.getCmbParroquia().setSeleccion(parroquia);
			vista.getCmbParroquia().setText(parroquia.getNombre());
			vista.getCmbsector().setText(sector.getNombre());
			
			
		};
		

	}

}
