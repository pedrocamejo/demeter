package cpc.demeter.catalogo;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.ContCargoTrabajador;
import cpc.modelo.demeter.basico.CargoTrabajador;
import cpc.modelo.sigesp.basico.Cargo;
import cpc.negocio.demeter.NegocioCargoTrabajador;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContCargosTrabajadores extends ContCatalogo<CargoTrabajador> {

	private static final long serialVersionUID = -4602011856599684640L;
	// @SuppressWarnings("unused")
	private AppDemeter app;

	public ContCargosTrabajadores(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		super();
		NegocioCargoTrabajador cargos = NegocioCargoTrabajador.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Cargos de Trabajadores", cargos.getTodos(),
				app);
	}

	public ContCargosTrabajadores() {
		super();
	}

	public ContCargosTrabajadores(int modoOperacion) {
		super(modoOperacion);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		// titulo = new CompEncabezado("Id",20);
		// titulo.setOrdenable(true);
		// titulo.setMetodoBinder("getId");
		// encabezado.add(titulo);

		titulo = new CompEncabezado("Descripcion", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				NegocioCargoTrabajador servicio = NegocioCargoTrabajador
						.getInstance();
				CargoTrabajador cargoTrabajador = getDatoSeleccionado();
				servicio.setModelo(cargoTrabajador);
				if (cargoTrabajador == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Cargo");
				else
					new ContCargoTrabajador(accion, servicio.getModelo(), this,
							app);
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
	public List cargarDato(Cargo arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(CargoTrabajador arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}