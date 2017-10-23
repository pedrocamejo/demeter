package cpc.demeter.catalogo;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.event.Event;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.modelo.sigesp.basico.Cargo;
import cpc.negocio.sigesp.NegocioCargo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContCargos extends ContCatalogo<Cargo> {

	private static final long serialVersionUID = -4602011856599684640L;
	@SuppressWarnings("unused")
	private AppDemeter app;

	public ContCargos(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		super();
		NegocioCargo condiciones = NegocioCargo.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Cargos de Trabajodes",
				condiciones.getTodos(), app);
	}

	public ContCargos() {
		super();
	}

	public ContCargos(int modoOperacion) {
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

		titulo = new CompEncabezado("Descripcion", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {

	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(Cargo arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}
}