package cpc.demeter.catalogo.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.mantenimiento.ContMaquinariaExterna;
import cpc.modelo.demeter.mantenimiento.MaquinariaExterna;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.negocio.demeter.mantenimiento.NegocioMaquinariaExterna;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class CatMaquinariaExterna extends ContCatalogo<MaquinariaExterna> {

	private static final long serialVersionUID = -5149724463777733770L;
	private AppDemeter app;

	public CatMaquinariaExterna() {
		super();
	}

	public CatMaquinariaExterna(AccionFuncionalidad accionesValidas,AppDemeter app) throws ExcColumnasInvalidas, ExcArgumentoInvalido,	ExcFiltroExcepcion 
	{
		NegocioMaquinariaExterna negocioMaquinaria = NegocioMaquinariaExterna.getInstance();
		this.app = app;
		List<MaquinariaExterna> lista = negocioMaquinaria.getMaquinarias();
		dibujar(accionesValidas, "MAQUINARIA EXTERNA", lista, app);

	}

	public CatMaquinariaExterna(int modoOperacion) {
		super(modoOperacion);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Id", 20);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Serial Carroceria ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getSerialcarroceria");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Serial Motor ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getSerialMotor");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Propietario ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrPropietario");
		encabezado.add(titulo);

		return encabezado;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(MaquinariaExterna arg0) {

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {

		return null;
	}

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {

		int accion = (Integer) event.getTarget().getAttribute("pos");

		if (accion <= Accion.CONSULTAR)
		{
			MaquinariaExterna maquinaria = getDatoSeleccionado();
			if (maquinaria == null && accion != Accion.AGREGAR)
			{
				app.mostrarError("Debe seleccionar un dato del catalago");
			}
			else
			{
				new ContMaquinariaExterna(accion, maquinaria, this, app);
				
			}

		}

	}

}
