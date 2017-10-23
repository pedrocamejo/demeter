package cpc.demeter.catalogo.gestion;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.gestion.ContDesincorporacionActivo;
import cpc.modelo.sigesp.basico.ActivoAlmacen;
import cpc.negocio.demeter.gestion.NegocioActivoAlmacen;
import cpc.persistencia.sigesp.implementacion.PerTipoAlmacen;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;

public class ContDesincorporacionesActivos extends ContCatalogo<ActivoAlmacen> {

	private static final long serialVersionUID = 107030076501961519L;
	private AppDemeter app;
	private NegocioActivoAlmacen negocio;

	public ContDesincorporacionesActivos(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws ExcColumnasInvalidas, ExcArgumentoInvalido {
		this.negocio = NegocioActivoAlmacen.getInstance();
		this.app = app;
		dibujar(accionesValidas, "DESINCORPORACIONES DE ACTIVOS",
				negocio.obtenerTodosSinAlmacenOperativo(new PerTipoAlmacen()
						.getTipoMecanizado()), app);
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(ActivoAlmacen arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

	public List<CompEncabezado> cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Serial", 160);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getSerialActivo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre", 250);
		titulo.setOrdenable(false);
		titulo.setMetodoBinder("getNombreActivo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Marca", 150);
		titulo.setOrdenable(false);
		titulo.setMetodoBinder("getMarcaActivo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Modelo", 140);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getModeloActivo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre Almacen", 180);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreAlmacen");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo Almacen", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreTipoAlmacen");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion == Accion.EDITAR) {
				ActivoAlmacen dato = getDatoSeleccionado();
				new ContDesincorporacionActivo(accion, dato, this, app);
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			this.app.mostrarError("Debe Seleccionar un activo");
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	}
}
