package cpc.demeter.catalogo;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Script;
import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.ContEspecialidadCargo;
import cpc.modelo.demeter.basico.EspecilidadCargo;
import cpc.negocio.demeter.NegocioEspecialidadCargo;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContEspecialidadCargos extends ContCatalogo<EspecilidadCargo> {

	private static final long serialVersionUID = 3901559127383820146L;
	private AppDemeter app;

	public ContEspecialidadCargos(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		super();
		NegocioEspecialidadCargo especialidades = NegocioEspecialidadCargo
				.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Especialidad de Cargos",
				especialidades.getTodos(), app);
	}

	public ContEspecialidadCargos() {
		super();
	}

	public ContEspecialidadCargos(int modoOperacion) {
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
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR) {
				EspecilidadCargo especialidad = getDatoSeleccionado();
				new ContEspecialidadCargo(accion, especialidad, this, app);
			} else {
				Script scrip = new Script();
				scrip.setType("text/javascript");
				StringBuilder cadena = new StringBuilder();
				cadena.append(SpringUtil.getBean("icaro"));
				cadena.append("jdbcUrl=");
				cadena.append(SessionDao.getConfiguration().getProperty(
						"hibernate.connection.url"));
				cadena.append("'); ");
				scrip.setContent(cadena.toString());
				this.app.agregar(scrip);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(EspecilidadCargo arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}
}