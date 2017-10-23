package cpc.demeter.catalogo.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.mantenimiento.ContMantenimientoMaquinaria;
import cpc.modelo.demeter.mantenimiento.MantenimientoMaquinaria;
import cpc.negocio.demeter.mantenimiento.NegocioLote;
import cpc.negocio.demeter.mantenimiento.NegocioMantenimientoMaquinaria;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContMantntosMaquinaria extends	ContCatalogo<MantenimientoMaquinaria>
{

	private static final long serialVersionUID = -5149724463777733770L;
	private AppDemeter app;

	public ContMantntosMaquinaria() {
		super();
	}

	public ContMantntosMaquinaria(AccionFuncionalidad accionesValidas, AppDemeter app) throws ExcColumnasInvalidas, ExcArgumentoInvalido,ExcFiltroExcepcion
	{
		NegocioMantenimientoMaquinaria servicio = new NegocioMantenimientoMaquinaria().getInstance();
		this.app = app;
		dibujar(accionesValidas, "MANTENIMIENTO POR MAQUINARIA",servicio.getMantenimientoMaquinaria(), app);
	}

	public ContMantntosMaquinaria(int modoOperacion) {
		super(modoOperacion);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() 
	{
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Id", 20);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Modelo Maquinaria ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getModeloMaquinaria");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Marca Maquinaria ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMarcaMaquinaria");
		encabezado.add(titulo);

		
		return encabezado;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(MantenimientoMaquinaria arg0) {

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {

		return null;
	}

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception
	{
		int accion = (Integer) event.getTarget().getAttribute("pos");
		if (accion <= Accion.CONSULTAR)
		{
			MantenimientoMaquinaria mntMaquinaria = getDatoSeleccionado();
			if (mntMaquinaria == null && accion != Accion.AGREGAR) 
			{
				app.mostrarError("Debe seleccionar un dato del catalago");
			} 
			else
			{
				new ContMantenimientoMaquinaria(accion, mntMaquinaria, this,app);
			}
		}
	}

}
