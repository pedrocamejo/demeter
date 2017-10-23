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
import cpc.demeter.controlador.ContExSigesp;
import cpc.modelo.demeter.administrativo.sigesp.ExtracionDatosSigesp;
import cpc.negocio.demeter.administrativo.sigesp.NegocioExtracionDatosSigesp;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContExportarSigesp  extends ContCatalogo<ExtracionDatosSigesp> implements EventListener, Serializable 
{
	
	private static final long serialVersionUID = 3924078618473593264L;
	private AppDemeter app;
	
	public ContExportarSigesp(AccionFuncionalidad accionesValidas, AppDemeter app) throws SQLException, ExcAccesoInvalido, PropertyVetoException, ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,	ExcArgumentoInvalido, ExcFiltroExcepcion 
	{
		NegocioExtracionDatosSigesp negocio = NegocioExtracionDatosSigesp.getInstance();
		this.app = app;
		dibujar(accionesValidas, "ALMACENES", negocio.getExtracionesDatosSigesp(), app);
	}
	
	public List<CompEncabezado> cargarEncabezado() 
	{
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Nombre", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombre");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Mes", 300);
		titulo.setOrdenable(false);
		titulo.setMetodoBinder("getMes");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Año", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getAno");
		encabezado.add(titulo);

		return encabezado;
	}
	
	public void onEvent(Event event) throws Exception {
	try {
		int accion = (Integer) event.getTarget().getAttribute("pos");
	
		if (accion <= Accion.CONSULTAR || accion == Accion.CORREGIR ) 
		{
			ExtracionDatosSigesp sigesp = getDatoSeleccionado();
			if (sigesp == null && accion != Accion.AGREGAR)
				app.mostrarError("Seleccione un Almacen del Catalogo");
			else
				new ContExSigesp(accion, sigesp, this, app);
		 }
	
	} catch (NullPointerException e) {
		e.printStackTrace();
		app.mostrarError("Seleccione un Almacen del Catalogo");
	} catch (Exception e) {
		e.printStackTrace();
		app.mostrarError(e.getMessage());
	}
	
	}
	
	@SuppressWarnings("unchecked")
	public List cargarDato(ExtracionDatosSigesp arg0) {
	return null;
	}
	
	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
	return null;
	}
	
}
