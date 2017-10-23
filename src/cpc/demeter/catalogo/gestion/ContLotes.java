package cpc.demeter.catalogo.gestion;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Script;
import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.gestion.ContLote;
import cpc.modelo.demeter.mantenimiento.Lote;
import cpc.negocio.demeter.mantenimiento.NegocioLote;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContLotes extends ContCatalogo<Lote> {

	private static final long serialVersionUID = -3727563103572490818L;
	private AppDemeter app;

	public ContLotes(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		super();
		NegocioLote lotes = NegocioLote.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Lotes", lotes.getTodos(), app);
	}

	public ContLotes() {
		super();
	}

	public ContLotes(int modoOperacion) {
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
				Lote sistema = getDatoSeleccionado();
				new ContLote(accion, sistema, this, app);
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
	public List cargarDato(Lote arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}
}