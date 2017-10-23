package cpc.demeter.controlador.gestion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Row;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.gestion.UiTrabajoMecanizado;
import cpc.modelo.demeter.basico.Labor;
import cpc.modelo.demeter.gestion.DetalleMaquinariaOrdenTrabajo;
import cpc.modelo.demeter.gestion.DetalleTrabajoRealizadoMecanizado;
import cpc.modelo.demeter.gestion.LaborOrdenServicio;
import cpc.modelo.demeter.gestion.OrdenTrabajoMecanizado;
import cpc.modelo.demeter.gestion.TrabajoRealizadoMecanizado;
import cpc.negocio.demeter.gestion.NegocioTrabajoMecanizado;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcAgregacionInvalida;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContTrabajoMecanizado extends
		ContVentanaBase<TrabajoRealizadoMecanizado> {

	private static final long serialVersionUID = 6184414588153046382L;
	private AppDemeter app;
	private UiTrabajoMecanizado vista;
	private NegocioTrabajoMecanizado negocio;

	public ContTrabajoMecanizado(int modoOperacion,	TrabajoRealizadoMecanizado ordenTrabajo, ICompCatalogo<TrabajoRealizadoMecanizado> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida
	{
		super(modoOperacion);
		this.app = app;
		try
		{
			negocio = NegocioTrabajoMecanizado.getInstance();
			if (ordenTrabajo == null || modoAgregar()) {
				ordenTrabajo = negocio.getNuevaOrdenTrabajo();
			}
			List<OrdenTrabajoMecanizado> ordenes = negocio.getOrdenesTrabajosProject();
			vista = new UiTrabajoMecanizado("Trabajo realizado Mecanizado-- ("
					+ Accion.TEXTO[modoOperacion] + ")", 750, ordenes);
			setear(ordenTrabajo, vista, llamador, app);
			vista = ((UiTrabajoMecanizado) getVista());
			vista.desactivar(modoOperacion);

		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun Servicio");
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void eliminar() {
		try {
			negocio = NegocioTrabajoMecanizado.getInstance();
			negocio.setModelo(getDato());
			negocio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void guardar() {
		try {
			negocio = NegocioTrabajoMecanizado.getInstance();
			negocio.setModelo(getDato());
			actualizarDetalle();
			negocio.guardar(negocio.getModelo().getId());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError("Error al Guardar Trabajo realizado."
					+ e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void actualizarDetalle() throws ExcAgregacionInvalida {
		try {
			OrdenTrabajoMecanizado orden = vista.getNroControl().getSeleccion();
			orden = negocio.getInstance().getOrdenTrabajoProject(orden);
			
			getDato().setOrdenTrabajo(orden);
			getDato().setLabor(getDato().getOrdenTrabajoMecanizado().getLaborOrden().getLabor());
			getDato().setUnidadTrabajoRealizado(getDato().getOrdenTrabajoMecanizado().getLaborOrden().getUnidad());

			List<DetalleTrabajoRealizadoMecanizado> detalles = new ArrayList<DetalleTrabajoRealizadoMecanizado>();
			List<Row> filas = vista.getDetalles().getFilas().getChildren();
			
			DetalleTrabajoRealizadoMecanizado detalle;
			
			for (Row item : filas) 
			{
				detalle = (DetalleTrabajoRealizadoMecanizado) item.getAttribute("dato");
				detalle.setHorometroEfectivoFinal(((Doublebox) item.getChildren().get(6)).getValue());
				detalle.setHorometroEfectivoInicio(((Doublebox) item.getChildren().get(5)).getValue());
				detalle.setHorometroFinal(((Doublebox) item.getChildren().get(4)).getValue());
				detalle.setHorometroInicio(((Doublebox) item.getChildren().get(3)).getValue());
				detalle.setTrabajo(getDato());
				detalles.add(detalle);
			}
			
			getDato().setDetalles(detalles);
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new ExcAgregacionInvalida("Problema actualizando detalle, "	+ e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void validarDetalle() throws WrongValuesException 
	{
		List<Row> filas = vista.getDetalles().getFilas().getChildren();
		for (Row item : filas) 
		{
			if (((Doublebox) item.getChildren().get(3)).getValue() != null || ((Doublebox) item.getChildren().get(4)).getValue() != null)
			{
				
				if (((Doublebox) item.getChildren().get(6)).getValue() < ((Doublebox) item.getChildren().get(5)).getValue())
				{
					throw new WrongValueException((Doublebox) item.getChildren().get(6),"Horometro final no puede ser Menor al final");
				}
				
				if (((Doublebox) item.getChildren().get(4)).getValue() < ((Doublebox) item.getChildren().get(3)).getValue())
				{
					throw new WrongValueException((Doublebox) item.getChildren().get(4),"Horometro final no puede ser Menor al final");
				}
				
				if (((Doublebox) item.getChildren().get(6)).getValue() > ((Doublebox) item.getChildren().get(4)).getValue())
				{
					throw new WrongValueException((Doublebox) item.getChildren().get(6),"Horometro Efectivo final no puede ser Mayor al final total");
				}
				
				if (((Doublebox) item.getChildren().get(5)).getValue() > ((Doublebox) item.getChildren().get(4)).getValue())
				{
					throw new WrongValueException((Doublebox) item.getChildren().get(5),"Horometro Efectivo Inicial no puede ser Mayor al final total");
					
				}
				
				if (((Doublebox) item.getChildren().get(6)).getValue() < ((Doublebox) item.getChildren().get(3)).getValue())
				{
					throw new WrongValueException((Doublebox) item.getChildren().get(6),"Horometro Efectivo final no puede ser Menor al total");
				}

				if (((Doublebox) item.getChildren().get(5)).getValue() < ((Doublebox) item.getChildren().get(3)).getValue())
				{
					throw new WrongValueException((Doublebox) item.getChildren().get(5),"Horometro Efectivo Inicial no puede ser Menor al total");
				}
				
			} 
			else 
			{
				((Doublebox) item.getChildren().get(3)).setValue(null);
				((Doublebox) item.getChildren().get(4)).setValue(null);
				((Doublebox) item.getChildren().get(5)).setValue(null);
				((Doublebox) item.getChildren().get(6)).setValue(null);
			}

		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR) 
		{
			if ((vista.getNroControl().getSeleccion() == null))
			{
				throw new WrongValueException(vista.getNroControl(),"Indique una Orden de Trabajo");
			}

			if ((vista.getCantidadTrabajo().getValue() == null))
			{
				throw new WrongValueException(vista.getCantidadTrabajo(),"Indique Cantidad Trabajada");
			}
			if ((vista.getInicio().getValue() == null))
			{
				throw new WrongValueException(vista.getInicio(),"Indique Inicio Labor");
			}
			if ((vista.getFin().getValue() == null))
			{
				throw new WrongValueException(vista.getFin(),"Indique Final de labor");
			}
			else if (vista.getFin().getValue().compareTo(vista.getFin().getValue()) < 0)
			{
				throw new WrongValueException(vista.getFin(),"Final no debe ser menor a Inicio");
			}
			validarDetalle();
		}

	}

	public void onEvent(Event event) throws Exception 
	{
		if (event.getTarget() == getVista().getAceptar())
		{
			validar();
			procesarCRUD(event);
		}
		else if (event.getName() == CompBuscar.ON_SELECCIONO && event.getTarget() == vista.getNroControl()) 
		{
			OrdenTrabajoMecanizado orden = vista.getNroControl().getSeleccion();
			orden = negocio.getInstance().getOrdenTrabajoProject(orden);
			actualizarOrdenTrabajo(orden);
		}
	}

	public void actualizarOrdenTrabajo(OrdenTrabajoMecanizado orden) throws ExcFiltroExcepcion 
	{
		negocio = NegocioTrabajoMecanizado.getInstance();
	
		if (orden != null) 
		{
			orden = negocio.detDetalleOrdenTrabajo(orden);
		
			if (orden.getSolicitud() != null)
			{
				vista.getSolicitud().setValue(orden.getSolicitud().getNroControl());
			}
			
			if (orden.getContrato() != null)
			{
				vista.getContrato().setValue(orden.getContrato().getStrNroDocumento());
			}
			
			vista.getNombreProductor().setValue(orden.getProductor().getNombres());
			vista.getCedula().setValue(orden.getProductor().getIdentidadLegal());
			vista.getUnidadFuncional().setValue(orden.getUnidadFuncional().getDescripcion());
			
			///Disparo con Silenciador estara bien esto ? 
			try
			{
				vista.getUnidadProductiva().setValue(orden.getUnidadProductiva().toString());
			}
			
			catch (Exception e) {

			}
			
			Labor labor = orden.getLaborOrden().getLabor();
			vista.getLabor().setValue(labor.getDescripcion());
			vista.getUnidadTrabajada().setValue(orden.getLaborOrden().getUnidad().getDescripcion());
			vista.getFisico().setValue(orden.getLaborOrden().getFisico());
			vista.getCantidad().setValue(orden.getLaborOrden().getCantidad());
			vista.getPases().setValue(orden.getLaborOrden().getPases());
			vista.getTotal().setValue(orden.getLaborOrden().getCalculo());
			vista.getSede().setValue(orden.getSede().getNombre());
			vista.getFecha().setValue(orden.getFecha());

			///Disparo con Silenciador estara bien esto ? 
			try 
			{
				vista.getUnidadproducida().setModelo(negocio.getUnidadProduccion(orden.getLaborOrden().getLabor()));
			} 
			catch (NullPointerException e) {

			}
			
			if (orden.getProductor().getFechaIngreso() != null)
			{
				vista.getIngreso().setValue(orden.getProductor().getFechaIngreso().toString());
			}
			
			LaborOrdenServicio a = orden.getLaborOrden();
			vista.refrescarDatosLabor(a, false);
			vista.SetVisibleGbProducido(orden.getProduccion());
			if (modoAgregar())
			{	
				for (DetalleMaquinariaOrdenTrabajo item : orden.getEquipos()) 
				{
					if (item.getOperativa())
					{
						DetalleTrabajoRealizadoMecanizado detalle = new DetalleTrabajoRealizadoMecanizado();
						detalle.setEquipo(item.getMaquinaria());
						detalle.setOperador(item.getOperador());
						detalle.setImplemento(item.getImplemento());
						vista.getDetalles().agregar(detalle);
					}
				}
			}
		}
	}

	public void mostrarDetalle() 
	{
		if (!modoAgregar())
		{
			if (getDato().getDetalles() != null)
			{
				for (DetalleTrabajoRealizadoMecanizado item : getDato().getDetalles())
				{
					vista.getDetalles().agregar(item);
				}
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

}
