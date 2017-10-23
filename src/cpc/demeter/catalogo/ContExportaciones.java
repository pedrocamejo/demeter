package cpc.demeter.catalogo;

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
import cpc.demeter.controlador.ContExportar;
import cpc.modelo.demeter.sincronizacion.Exportar;
import cpc.negocio.demeter.NegocioExportar;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContExportaciones extends ContCatalogo<Exportar> implements EventListener, Serializable
                              
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AppDemeter app;
	
	
	public ContExportaciones(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		
		NegocioExportar servicios = NegocioExportar.getInstance();
		this.app = app;
		dibujar(accionesValidas, "EXPORTACIONES ", servicios.getExportaciones(), app);
		
	}
	
	@Override
	public void onEvent(Event event) throws Exception {
		// TODO Auto-generated method stub
		
	 

		try {
				int accion = (Integer) event.getTarget().getAttribute("pos");
				Exportar exportar = getDatoSeleccionado();
				
				if (accion <= Accion.CONSULTAR)
				{    
				
					new ContExportar(Accion.AGREGAR, exportar,this, app);
				} 
				
		} catch (NullPointerException e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	
	}

	@Override
	public List<CompEncabezado> cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Id", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("sede", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getSede");
		encabezado.add(titulo);

		titulo = new CompEncabezado("MD5", 550);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMd5");
		encabezado.add(titulo);

		return encabezado;
	}

	@Override
	public List cargarDato(Exportar dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
