package cpc.demeter.catalogo.mantenimiento;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.event.Event;
import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.mantenimiento.ContMaquinaria;
import cpc.modelo.demeter.mantenimiento.Maquinaria;
import cpc.negocio.demeter.mantenimiento.NegocioMaquinaria;

import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContMaquinarias extends ContCatalogo<Maquinaria> {

	private static final long serialVersionUID = 1693946024302981254L;
	private AppDemeter app;

	public ContMaquinarias(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		super();
		NegocioMaquinaria negocio = NegocioMaquinaria.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Expedientes de Maquinarias y/o Equipos",
				negocio.getTodos(), app);
	}

	public ContMaquinarias() {
		super();
	}

	public ContMaquinarias(int modoOperacion) {
		super(modoOperacion);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Codigo", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCodigo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Maquinaria y/o Equipo", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Categoria", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCategoria");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getTipo");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR) {
				Maquinaria maq = getDatoSeleccionado();
				new ContMaquinaria(accion, maq, this, app);
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun registro");
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(Maquinaria arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}
}