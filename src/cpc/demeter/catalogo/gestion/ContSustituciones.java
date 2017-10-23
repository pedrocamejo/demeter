package cpc.demeter.catalogo.gestion;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Script;
import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.gestion.ContSustitucion;
import cpc.modelo.demeter.gestion.Sustitucion;
import cpc.negocio.demeter.gestion.NegocioSustitucion;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContSustituciones extends ContCatalogo<Sustitucion> {

	private static final long serialVersionUID = -3727563103572490818L;
	private AppDemeter app;

	public ContSustituciones(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		super();
		NegocioSustitucion negocio = NegocioSustitucion.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Sustituciones", negocio.getTodosProject(),
				app);
	}

	public ContSustituciones() {
		super();
	}

	public ContSustituciones(int modoOperacion) {
		super(modoOperacion);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Fecha Registro", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getFechaRegistro");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Operador Anterior", 170);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getOperadorAnterior");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Operador Actual", 170);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getOperadorActual");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Maquinaria Anterior", 180);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMaquinaAnterior");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Maquinaria Actual", 180);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMaquinaActual");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Causa de Sustitucion", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCausa");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			NegocioSustitucion servicio = NegocioSustitucion.getInstance();
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR) {
				Sustitucion sustitucion = getDatoSeleccionado();
			
				sustitucion =servicio.getSustitucionProject(sustitucion); 
				new ContSustitucion(accion, sustitucion, this, app);
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
	public List cargarDato(Sustitucion arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}
}