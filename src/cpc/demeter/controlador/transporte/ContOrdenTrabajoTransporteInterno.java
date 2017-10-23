package cpc.demeter.controlador.transporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.transporte.UIOrdenTrabajoTransporte;
import cpc.demeter.vista.transporte.UIOrdenTrabajoTransporteInterno;
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.basico.Labor;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.gestion.DetalleOrdenTrabajo;
import cpc.modelo.demeter.gestion.ImplementoUnidad;
import cpc.modelo.demeter.gestion.MaquinariaUnidad;
import cpc.modelo.demeter.gestion.OrdenTrabajoTransporte;
import cpc.modelo.demeter.gestion.OrdenTrabajoTransporteInterno;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.demeter.mantenimiento.DetalleTransferenciaArticulo;
import cpc.modelo.demeter.transporte.UbicacionTransporte;
import cpc.modelo.ministerio.basico.TipoProductor;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.negocio.demeter.gestion.NegocioOrdenTrabajoTransporteInterno;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.utilidades.Real;

public class ContOrdenTrabajoTransporteInterno extends ContVentanaBase<OrdenTrabajoTransporteInterno> implements
EventListener{

	/**
	 * 
	 */
	
	private AppDemeter app;
	private NegocioOrdenTrabajoTransporteInterno negocio;
	private UIOrdenTrabajoTransporteInterno vista;
	public ContOrdenTrabajoTransporteInterno(Integer accion,
			OrdenTrabajoTransporteInterno modelo,
			ContCatalogo<OrdenTrabajoTransporteInterno> llamador,
			AppDemeter app) throws ExcFiltroExcepcion, ExcDatosInvalidos {
		super(accion);
	
		this.app = app;
		negocio = NegocioOrdenTrabajoTransporteInterno.getInstance();
		
		
		List<Trabajador> operadores=new ArrayList<Trabajador>();
		List<IProducto> articulos=new ArrayList<IProducto>();
		List<UbicacionTransporte>ubicaciones=new ArrayList<UbicacionTransporte>();
		if (modoAgregar() ) {
			setDato(modelo);
			operadores = negocio.getOperadores();
			 articulos = negocio.getServiciosTransporte(); 
			 ubicaciones = negocio.getUbicacionTransportes();
		}else {
			modelo =negocio.inizializar(modelo);
			setDato(modelo);
			ubicaciones = negocio.getUbicacionTransportes();
			operadores = negocio.getOperadores();
			 articulos = negocio.getServiciosTransporte(); 
		 
		
		}
	
		List<UnidadFuncional> unidades = negocio.getUnidadesFuncionales();                 
		UIOrdenTrabajoTransporteInterno vistass = new  UIOrdenTrabajoTransporteInterno("ORDEN TRANSPORTE Interno", 850, getModoOperacion(), operadores,ubicaciones,articulos,unidades);
	
		 setear (modelo, vistass,llamador, app);
		
		 vista = (UIOrdenTrabajoTransporteInterno) getVista();
		if (modoAgregar() ) {
			
			
		
		}
		if (modoProcesar() ) {
			
			activarDetalleOrden();
			
		}
		else {
			
		}
		if(modoAgregar()){vista.modoAgregar();}
		if(modoConsulta()){vista.modoConsultar();}
		if (modoAnular())vista.modoConsultar();
		if(modoProcesar()){vista.modoProcesar();}

		
	}

	@Override
	public void onEvent(Event event) throws Exception {
		if (event.getTarget()==vista.getOrigen().getCrear()){
			new ContUbicacionTransporte(Accion.AGREGAR, null, vista.getOrigen(), app);
		}
		
		if (event.getTarget()==vista.getDestino().getCrear()){
			new ContUbicacionTransporte(Accion.AGREGAR, null, vista.getDestino(), app);
		}
		if (event.getTarget()==vista.getOrigenReal().getCrear()){
			new ContUbicacionTransporte(Accion.AGREGAR, null, vista.getOrigenReal(), app);
		}
		
		if (event.getTarget()==vista.getDestinoReal().getCrear()){
			new ContUbicacionTransporte(Accion.AGREGAR, null, vista.getDestinoReal(), app);
		}
		
		
		
		if (event.getTarget() == getVista().getAceptar()) 
		{
			procesarCRUD(event);

		}
		if (event.getName().equals("onChange")){

		
		}
		if (event.getName().equals("onSelect")){

			vista.getMaquinaria().setModelo(negocio.getMaquinarias(vista.getUnidad().getSeleccion()));
			vista.getImplemento().setModelo(negocio.getImplementos(vista.getUnidad().getSeleccion()) );
			
		}
		
		if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {
			
			if (event.getTarget().equals(vista.getCmbArticuloventa())){
				actualizarArticulo((CompBuscar<IProducto>) event.getTarget());
			}
			 Component error = event.getTarget();
			 Map<?, ?> map = error.getAttributes();
			 if (!map.isEmpty()){
			  String nombre = (String) map.get("nombre"); 
			  if (nombre!=null){
			  if (event.getName().equals(CompBuscar.ON_SELECCIONO) 	&& event.getTarget().getAttribute("nombre")	.equals("maquinaria"))
				{
					// Para Maquinaria
					CompBuscar<MaquinariaUnidad> componente = (CompBuscar<MaquinariaUnidad>) event.getTarget();
					actualizarSerialMaquinaria(componente);
				} 
				else if (event.getName().equals(CompBuscar.ON_SELECCIONO)&& event.getTarget().getAttribute("nombre").equals("implemento"))
				{
					// Para implemento
					CompBuscar<ImplementoUnidad> componente = (CompBuscar<ImplementoUnidad>) event.getTarget();
					actualizarSerialImplemento(componente);
				} 
				else if (event.getName().equals(CompBuscar.ON_SELECCIONO)&& event.getTarget().getAttribute("nombre").equals("articulo"))
				{
					// Para implemento
					CompBuscar<IProducto> componente = (CompBuscar<IProducto>) event.getTarget();
					actualizarArticulo(componente);
			
					
					
				} 
			  }
			 }
			
		}
		
	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		
		negocio = NegocioOrdenTrabajoTransporteInterno.getInstance();
		OrdenTrabajoTransporteInterno  orden = getDato();
		//List<Row> filas = vista.getCompListaDetalleOrden().getFilas().getChildren();
		/*List<DetalleOrdenTrabajo> detalles = new ArrayList<DetalleOrdenTrabajo>();
		for (Row row : filas) {
			DetalleOrdenTrabajo detalle = new DetalleOrdenTrabajo();
			CompBuscar<IProducto> articulo = (CompBuscar<IProducto>) row.getChildren().get(0);
			Labor	labor =(Labor) articulo.getSeleccion();
			
			Doublebox solictado = (Doublebox) row.getChildren().get(2);
			Doublebox ejecutado = (Doublebox) row.getChildren().get(3);
			detalle.setLabor(labor);
			detalle.setOrden(orden);
			detalle.setUnidadTrabajo(labor.getMedidaGestion());
			detalle.setCantidadSolicitada(solictado.getValue());
			detalle.setCantidadEjecutada(ejecutado.getValue());
			detalles.add(detalle);		
		}
		orden.setDetalles(detalles);*/
		vista.getCompListaDetalleOrden().refrescar();
	List<DetalleOrdenTrabajo> deatlles = vista.getCompListaDetalleOrden().getColeccion();
	for (DetalleOrdenTrabajo detalleOrdenTrabajo : deatlles) {
		
		detalleOrdenTrabajo.setOrden(orden);
		detalleOrdenTrabajo.setUnidadTrabajo(detalleOrdenTrabajo.getLabor().getMedidaGestion());
		System.out.println(detalleOrdenTrabajo.getLabor());
	}
		orden.setDetalles(vista.getCompListaDetalleOrden().getColeccion());
		orden.setEquipos(vista.getColeccionGrilla());
		orden.setFecha(new Date());
		orden.setUnidadFuncional(vista.getUnidad().getSeleccion());
		orden.setInicioServicio(vista.getDtmFechaInicio().getValue());
		orden.setFinServicio(vista.getDtmFechaFin().getValue());
		orden.setCerrada(false);
		orden.setUbicacionFinal(vista.getDestino().getModelo());
		orden.setUbicacionInico(vista.getOrigen().getModelo());
		negocio.setModelo(getDato());
		if (modoAgregar())
		{
			try {
				negocio.guardar(negocio.getModelo());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void anular() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		negocio = NegocioOrdenTrabajoTransporteInterno.getInstance();
		negocio.anular(getDato());
		vista.close();
	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		validadCierre();
		negocio = NegocioOrdenTrabajoTransporteInterno.getInstance();
		OrdenTrabajoTransporteInterno orden = getDato();
		orden.setUbicacionFinalReal(vista.getDestinoReal().getModelo());
		orden.setUbicacionInicoReal(vista.getOrigenReal().getModelo());
		orden.setKilometraje(vista.getDblKilometraje().getValue());
		orden.setFinServicioVerificado(vista.getDtmFechaFinVerificada().getValue());
		orden.setInicioServicioVerificado(vista.getDtmFechaInicioVerificada().getValue());
		orden.setCerrada(true);
		try {
			negocio.cerrar(orden);
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		vista.close();
	}

	public void actualizarSerialMaquinaria(
			CompBuscar<MaquinariaUnidad> componente) throws ExcFiltroExcepcion {
		try {
			Row registro = (Row) componente.getParent();
			((Label) registro.getChildren().get(2)).setValue(componente.getValorObjeto().toString());
		} catch (Exception e) {
			throw new ExcFiltroExcepcion("Problemas con la Maquinaria");
		}
	}

	public void actualizarSerialImplemento(
			CompBuscar<ImplementoUnidad> componente) throws ExcFiltroExcepcion {
		try {
			Row registro = (Row) componente.getParent();
			((Label) registro.getChildren().get(4)).setValue(componente
					.getValorObjeto().toString());
		} catch (Exception e) {
			throw new ExcFiltroExcepcion("Problemas con el Implemento");
		}
	}
	
	@SuppressWarnings("unchecked")
	public void validarDetalle() throws WrongValuesException,ExcEntradaInconsistente
	{
		List<Row> filas = vista.getDetalle().getFilas().getChildren();
		if (filas.size() < 1)
		{	
			throw new WrongValueException(vista.getDetalle(),"Indique Operador y equipo");
		}
		CompCombobox<Trabajador> trabajador;
		CompBuscar<MaquinariaUnidad> maquinaria;
		
		for (Row item : filas) 
		{
			trabajador = (CompCombobox<Trabajador>) item.getChildren().get(0);
			maquinaria = (CompBuscar<MaquinariaUnidad>) item.getChildren().get(1);
			if (trabajador.getSeleccion() == null)
			{
				throw new WrongValueException(trabajador,"Operador no seleccionado");
			}
			if (maquinaria.getSeleccion() == null)
			{
				throw new WrongValueException(maquinaria,"Maquinaria no seleccionado");
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void validarDetalleOrden() throws WrongValuesException,ExcEntradaInconsistente
	{
		List<Row> filas = vista.getCompListaDetalleOrden().getFilas().getChildren();
		if (filas.size() < 1)
		{	
			throw new WrongValueException(vista.getDetalle(),"Indique Operador y equipo");
		}
		Doublebox unidadSolicitada;
		Doublebox unidadEjecutada;
		
		for (Row item : filas) 
		{
			unidadSolicitada = (Doublebox) item.getChildren().get(2);
			unidadEjecutada  = (Doublebox) item.getChildren().get(3);
			/*if (unidadEjecutada.getValue() == null||unidadEjecutada.getValue()<=new Double(0.0))
			{
				throw new WrongValueException(unidadEjecutada,"debe indicar un valor ");
			}*/
			if (unidadSolicitada.getValue() == null||unidadSolicitada.getValue()<=new Double(0.0))
			{
				throw new WrongValueException(unidadSolicitada,"debe indicar un valor");
			}
			
		}
	}
	
	public void validar() throws WrongValuesException, ExcEntradaInconsistente 
	{
		if (getModoOperacion() != Accion.ELIMINAR) 
		{
			if ((vista.getOrigen().getModelo() == null))
			{
				throw new WrongValueException(vista.getOrigen(),	"Indique el punto de origen");
			}

			if ((vista.getDestino().getModelo() == null))
			{
				throw new WrongValueException(vista.getDestino(),	"Indique el punto de Destino");
			}
			if (vista.getDtmFechaFin().getValue()==null||(vista.getDtmFechaFin().getValue().before(vista.getDtmFechaInicio().getValue())))
			{
		throw new WrongValueException(vista.getDtmFechaFin(),	"la fecha no puede ser menor a la fecha der salida");
			}

			
			
			validarDetalle();
			validarDetalleOrden();
		}

	}
	
	public void validadCierre(){
		
	
			if (getModoOperacion() != Accion.ELIMINAR) 
			{
				if ((vista.getOrigenReal().getModelo() == null))
				{
					throw new WrongValueException(vista.getOrigen(),	"Indique el punto de origen");
				}

				if ((vista.getDestinoReal().getModelo() == null))
				{
					throw new WrongValueException(vista.getDestino(),	"Indique el punto de Destino");
				}
				if ((vista.getDblKilometraje().getValue() == null|| vista.getDblKilometraje().equals(new Double(0))))
				{
					throw new WrongValueException(vista.getDblKilometraje(),	"Indique un kilometraje");
				}
				if (vista.getDtmFechaFinVerificada().getValue()==null||(vista.getDtmFechaFinVerificada().getValue().before(vista.getDtmFechaInicioVerificada().getValue())))
						{
					throw new WrongValueException(vista.getDtmFechaFinVerificada(),	"la fecha no puede ser menor a la fecha der salida");
						}
			
			}

	}
	
	public void actualizarArticulo(CompBuscar<IProducto> componente)
			throws ExcFiltroExcepcion {
		System.out.println("actualizarServicio");
		IProducto producto = null;
		Double precio = 0.0;
		Row registro = (Row) componente.getParent();
		try {
			producto = componente.getSeleccion();
			producto = negocio.enriqueserProducto(producto);
		} catch (NullPointerException e) {
			throw new ExcFiltroExcepcion(
					"Problemas con el producto seleccionado");
		}
		Label unidad = (Label)registro.getChildren().get(1);
		unidad.setValue(((Labor)producto).getStrUnidadMedidagestion());

		try {
			} catch (Exception e) {
			e.printStackTrace();
			throw new ExcFiltroExcepcion("Problemas con el servicio");
		}
	}
	
	public void activarDetalleOrden(){
		List<Row> filas = vista.getCompListaDetalleOrden().getFilas().getChildren();
		for (Row row : filas) {
			Doublebox ejecutado = (Doublebox) row.getChildren().get(3);
			ejecutado.setDisabled(false);
		}
		
	}
				
				
			
		
		
	
	

}
