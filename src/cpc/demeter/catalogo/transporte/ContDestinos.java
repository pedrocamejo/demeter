package cpc.demeter.catalogo.transporte;

import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter; 
import cpc.demeter.controlador.transporte.ContDestino;
import cpc.modelo.demeter.transporte.Destino;
import cpc.modelo.ministerio.dimension.UbicacionEstado;
import cpc.negocio.demeter.transporte.NegocioDestino;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContDestinos  extends ContCatalogo<Destino> implements EventListener,Serializable{
	 
	

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContDestinos(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioDestino servicios = NegocioDestino.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Destinos", servicios.getTodos(), app);

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("ID",100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Descripcion",550);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);
		
		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) 
			{
				NegocioDestino servicio = NegocioDestino.getInstance();
				Destino destino = getDatoSeleccionado();
				if (destino  == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un tipo de riego");
				else
			    	new ContDestino(accion,destino,this, app);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un tipo de riego");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(UbicacionEstado tipo) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List cargarDato(Destino arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	  
		
 

	}
