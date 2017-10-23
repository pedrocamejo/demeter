package cpc.demeter.controlador.transporte;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.basico.Labor;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.gestion.ImplementoUnidad;
import cpc.modelo.demeter.gestion.MaquinariaUnidad;
import cpc.modelo.demeter.gestion.OrdenTrabajoTransporte;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.demeter.mantenimiento.DetalleTransferenciaArticulo;
import cpc.modelo.demeter.transporte.UbicacionTransporte;
import cpc.modelo.ministerio.basico.TipoProductor;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.negocio.demeter.gestion.NegocioOrdenTrabajoMecanizado;
import cpc.negocio.demeter.gestion.NegocioOrdenTrabajoTransporte;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.utilidades.Real;

public class ContOrdenTrabajoTransporte extends ContVentanaBase<OrdenTrabajoTransporte> implements
EventListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9221226381777175653L;
	private AppDemeter app;
	private NegocioOrdenTrabajoTransporte negocio;
	private UIOrdenTrabajoTransporte vista;
	public ContOrdenTrabajoTransporte(Integer accion,
			OrdenTrabajoTransporte modelo,
			ContCatalogo<OrdenTrabajoTransporte> llamador,
			AppDemeter app) throws ExcFiltroExcepcion, ExcDatosInvalidos {
		super(accion);
	
		this.app = app;
		negocio = NegocioOrdenTrabajoTransporte.getInstance();
		setDato(modelo);
		
		List<Trabajador> operadores=new ArrayList<Trabajador>();
		List<IProducto> articulos=new ArrayList<IProducto>();
		List<UbicacionTransporte>ubicaciones=new ArrayList<UbicacionTransporte>();
		if (modoAgregar() ) {
		operadores = negocio.getOperadores();
			 articulos = negocio.getServiciosTransporte(); 
			 ubicaciones = negocio.getUbicacionTransportes();
		}else {
			ubicaciones = negocio.getUbicacionTransportes();
			operadores = negocio.getOperadores();
			 
			Labor labor = (Labor) getDato().getCotizacionTransporte().getDetallesContrato().get(0).getProducto(); 
			List<IProducto> labores = negocio.getLabores(labor.getServicio()); 
			//vista.setLabores(labores);
			articulos = labores;
		}
	
		List<UnidadFuncional> unidades = negocio.getUnidadesFuncionales();                 
		UIOrdenTrabajoTransporte vistass = new  UIOrdenTrabajoTransporte("ORDEN TRANSPORTE", 850, getModoOperacion(), operadores,ubicaciones,articulos,unidades);
	
		 setear (modelo, vistass,llamador, app);
		
		 vista = (UIOrdenTrabajoTransporte) getVista();
		if (modoAgregar() ) {
			vista.getCotizacion().setModelo(	negocio.getCotizacionestransporteActivas());
			
		
		}else {
			vista.getCotizacion().setModelo(negocio.getCotizacionTransporte());
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
		
		
		
		if (event.getTarget() == getVista().getAceptar()) 
		{
			procesarCRUD(event);

		}
		if (event.getName().equals("onChange")){
			vista.getDetallecierre().refrescarColeccion();
			actualizarDetalleCotizacion();
		}
		if (event.getName().equals("onSelect")){

			vista.getMaquinaria().setModelo(negocio.getMaquinarias(vista.getUnidad().getSeleccion()));
			vista.getImplemento().setModelo(negocio.getImplementos(vista.getUnidad().getSeleccion()) );
			
		}
		
		if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {
			if (event.getTarget().equals(vista.getCotizacion())){
				vista.actualizarCotizacion();
				
			}
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
					vista.getDetallecierre().refrescarColeccion();
					actualizarDetalleCotizacion();
					
				} 
			  }
			 }
			
		}
		
	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		negocio = NegocioOrdenTrabajoTransporte.getInstance();
		OrdenTrabajoTransporte orden = getDato();
		orden.setCotizacionTransporte(vista.getCotizacion().getSeleccion());
		orden.setEquipos(vista.getColeccionGrilla());
		orden.setFecha(new Date());
		orden.setUnidadFuncional(vista.getUnidad().getSeleccion());
		orden.setInicioServicio(vista.getFechaSalida().getValue());
		orden.setCerrada(false);
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
		negocio = NegocioOrdenTrabajoTransporte.getInstance();
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
		negocio = NegocioOrdenTrabajoTransporte.getInstance();
		OrdenTrabajoTransporte orden = getDato();
		orden.setUbicacionFinal(vista.getDestino().getModelo());
		orden.setUbicacionInico(vista.getOrigen().getModelo());
		orden.setKilometraje(vista.getDblKilometraje().getValue());
		orden.setFinServicio(vista.getFechaRegreso().getValue());
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
	
	public void validar() throws WrongValuesException, ExcEntradaInconsistente 
	{
		if (getModoOperacion() != Accion.ELIMINAR) 
		{
			if ((vista.getCotizacion().getSeleccion() == null))
			{
				throw new WrongValueException(vista.getCotizacion(),	"Indique una labor");
			}
			
			Date fechaDesde = vista.getCotizacion().getSeleccion().getFechaDesde();
			Date fechaSalida = vista.getFechaSalida().getValue();
			Date fechaHasta = vista.getCotizacion().getSeleccion().getFechaHasta();
			if (fechaSalida==null){throw new WrongValueException(vista.getFechaSalida(),	"debe espeficar la fecha de salida");}
			if (fechaSalida!=null){
				boolean uno = fechaSalida.before((fechaDesde));
				boolean dos = fechaDesde.before(fechaSalida);	
				if	(fechaSalida.before((fechaDesde)))
				{
			throw new WrongValueException(vista.getFechaSalida(),	"la fecha no puede ser menor a la fecha inicio");
				}
				boolean tres = fechaHasta.after(fechaSalida);
				boolean cuatro = fechaSalida.after(fechaHasta);
				if (fechaSalida.after(fechaHasta))
				{
			throw new WrongValueException(vista.getFechaSalida(),	"la fecha no puede ser mayor a la fecha finalizacion");
				}
			}

			
			
			validarDetalle();
		}

	}
	
	public void validadCierre(){
		
	
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
				if ((vista.getDblKilometraje().getValue() == null|| vista.getDblKilometraje().equals(new Double(0))))
				{
					throw new WrongValueException(vista.getDblKilometraje(),	"Indique un kilometraje");
				}
				if (vista.getFechaRegreso().getValue()==null||(vista.getFechaRegreso().getValue().before(vista.getFechaSalida().getValue())))
						{
					throw new WrongValueException(vista.getFechaRegreso(),	"la fecha no puede ser menor a la fecha der salida");
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

		try {
			if (producto instanceof ArticuloVenta) {
				ArticuloVenta art = (ArticuloVenta) componente.getSeleccion();
				precio = art.getPrecioBase();
				System.out.printf("precio %.2f", precio);
			} else {
				precio = producto
						.getPrecioBase(vista.getCotizacion()
								.getSeleccion().getPagador().getTipo());
			}
			((Doublebox) registro.getChildren().get(2)).setValue(precio);
			Double cantidad = ((Doublebox) registro.getChildren().get(1))
					.getValue();
			if (cantidad == null) {
				((Doublebox) registro.getChildren().get(1)).setValue(1.0);
				cantidad = 1.0;
			}
			((Doublebox) registro.getChildren().get(3)).setValue(precio * cantidad);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcFiltroExcepcion("Problemas con el servicio");
		}
	}
	
	public void actualizarDetalleCotizacion(){
		List<DetalleContrato> listCotizacion = vista.getCompLista().getModelo(); 
//		List<DetalleContrato> listCierre1 = vista.getDetallecierre().getColeccion();
		vista.getDetallecierre().refrescar();
//		List<DetalleContrato> listCierre2 = vista.getDetallecierre().getColeccion();
		vista.getDetallecierre().refrescarColeccion();
//		List<DetalleContrato> listCierre3 = vista.getDetallecierre().getColeccion();
		List<DetalleContrato> listCierre = vista.getDetallecierre().getColeccion();
		//se comparan los detalles de las cotizacion  con los del cierre
		
		//1 si es igual no se hace nada 
	
		
		if (listCotizacion.get(0).getProducto().equals(listCierre.get(0).getProducto())){
			DetalleContrato detalleCotiza =  listCotizacion.get(0);
			DetalleContrato detalleCierre = listCierre.get(0);
			detalleCierre.setCantidadReal(detalleCierre.getCantidad());
			detalleCotiza.setCantidad(detalleCierre.getCantidad());
			detalleCotiza.setCantidadReal(detalleCierre.getCantidadReal());
			IProducto producto = negocio.enriqueserProducto(detalleCierre.getProducto());
			TipoProductor tipo = vista.getCotizacion().getSeleccion().getPagador().getTipo();
			Double preciobase = producto.getPrecioBase(tipo );
			detalleCotiza.setSubtotal(preciobase*detalleCierre.getCantidadReal());
			/*
			Double impuesto = (detalleCotiza.getImpuesto().getPorcentaje()/100*detalleCotiza.getSubtotal());
			Double base = new Double(detalleCotiza.getSubtotal());
			Double total = Real.redondeoMoneda(base) +Real.redondeoMoneda(impuesto);
			
 			
			detalleCotiza.getContrato().setTotal(total );
			detalleCotiza.getContrato().setMonto(Real.redondeoMoneda(base) );
			*/
			vista.getCompLista().clear();
			vista.getCompLista().setModelo(listCotizacion);
			
			
		}else {
			DetalleContrato detalleCotiza =  listCotizacion.get(0);
			detalleCotiza.setCantidad(new Double(0));
			detalleCotiza.setCantidadReal(new Double(0));
			detalleCotiza.setSubtotal(new Double(0));
						
			DetalleContrato detalleNuevo = new DetalleContrato();
			DetalleContrato detallecierre = listCierre.get(0);
			
			IProducto producto = negocio.enriqueserProducto(detallecierre.getProducto());
			TipoProductor tipo = vista.getCotizacion().getSeleccion().getPagador().getTipo();
			Double preciobase = producto.getPrecioBase(tipo);
			detallecierre.setCantidadReal(detallecierre.getCantidad());
			detalleNuevo.setCantidad(detallecierre.getCantidad());
			detalleNuevo.setCantidadReal(detallecierre.getCantidadReal());
			detalleNuevo.setSubtotal(detallecierre.getSubtotal());
			detalleNuevo.setProducto(detallecierre.getProducto());
			detalleNuevo.setImpuesto(detallecierre.getProducto().getImpuesto());
			detalleNuevo.setPrecioUnidad(preciobase);
			detalleNuevo.setContrato(detallecierre.getContrato());
			/*	
			Double impuesto = (detalleNuevo.getImpuesto().getPorcentaje()/100*detallecierre.getSubtotal());
			Double base = new Double(detallecierre.getSubtotal());
			Double total = Real.redondeoMoneda(base) +Real.redondeoMoneda(impuesto);
			
 			
			detalleNuevo.getContrato().setTotal(total );
			detalleNuevo.getContrato().setMonto(Real.redondeoMoneda(base) );
			*/
			listCotizacion.add(detalleNuevo);
		/*	
			for (DetalleContrato detalleContrato : listCotizacion) {
				System.out.println(detalleContrato);
				if (detalleContrato.getId()==null){
					//se deben de borrar todos lo que no sean el objeto detalleNuevo
					if (detalleContrato.getProducto()!=detalleNuevo.getProducto()){
						System.out.println("se debe borrar");
						listCotizacion.remove(detalleContrato);}
				}else System.out.println("este queda"); 
			
			} 
			
		*/
			for (Iterator<DetalleContrato > iter = listCotizacion.listIterator(); iter.hasNext(); ) {
				DetalleContrato detalleContrato =iter.next();
				System.out.println(detalleContrato);
				if (detalleContrato.getId()==null){
					//se deben de borrar todos lo que no sean el objeto detalleNuevo
					if (detalleContrato.getProducto()!=detalleNuevo.getProducto()){
						System.out.println("se debe borrar");
						iter.remove();}
				}else System.out.println("este queda"); 
			}
			
			
			vista.getCompLista().clear();
			//vista.getCompLista().quitarItem();
			vista.getCompLista().setModelo(listCotizacion);
			
			
			
			
			/*for (DetalleContrato detalleCotizacion : listCotizacion) {
				IProducto productoCoti = detalleCotizacion.getProducto();
				Double cantidadCoti = detalleCotizacion.getCantidad();
				Double cantidadRealCoti = detalleCotizacion.getCantidadReal();
				
				for (DetalleContrato detalleContrato : listCierre) {
					IProducto productoCier = detalleCotizacion.getProducto();
					Double cantidadCier = detalleCotizacion.getCantidad();
					Double cantidadRealCier = detalleCotizacion.getCantidadReal();
					
					if (productoCier==productoCoti){
							detalleCotizacion.setCantidad(cantidadCier);
							detalleCotizacion.setCantidadReal(cantidadRealCier);
							negocio.enriqueserProducto(productoCier);
							TipoProductor tipo = vista.getCotizacion().getSeleccion().getPagador().getTipo();
							Double preciobase = productoCier.getPrecioBase(tipo );
						
							detalleCotizacion.setSubtotal(cantidadRealCier*productoCier.getPrecioBase(vista.getCotizacion().getSeleccion().getPagador().getTipo()));
					}else {
						DetalleContrato detalleaux = new DetalleContrato();
						detalleaux.setCantidad(cantidadCier);
						detalleaux.setCantidadReal(cantidadRealCier);
						detalleaux.setSubtotal(cantidadRealCier*productoCier.getPrecioBase(vista.getCotizacion().getSeleccion().getPagador().getTipo()));
						detalleaux.setContrato(vista.getCotizacion().getSeleccion());
						
						detalleCotizacion.setCantidad(new Double(0));
						detalleCotizacion.setCantidadReal(new Double(0));
						detalleCotizacion.setSubtotal(new Double(0));
						
						listCotizacion.add(detalleaux);
						*/
					}
					
				}
				
				
			
		
		
	
	
	public void actualizarLaboresCierre(){
		Labor labor = (Labor) vista.getCotizacion().getSeleccion().getDetallesContrato().get(0).getProducto(); 
		List<IProducto> labores = negocio.getLabores(labor.getServicio()); 
		vista.setLabores(labores);
	}
}
