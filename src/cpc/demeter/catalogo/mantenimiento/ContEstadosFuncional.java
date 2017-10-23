package cpc.demeter.catalogo.mantenimiento;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkex.zul.Jasperreport;
import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.mantenimiento.ContEstadoFuncional;
import cpc.modelo.demeter.mantenimiento.EstadoFuncional;
import cpc.negocio.demeter.mantenimiento.NegocioEstadoFuncional;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContEstadosFuncional extends ContCatalogo<EstadoFuncional> {

	private static final long serialVersionUID = -5149724463777733770L;
	private AppDemeter app;

	public ContEstadosFuncional() {
		super();
	}

	public ContEstadosFuncional(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioEstadoFuncional estado = NegocioEstadoFuncional.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Estado Funcional", estado.getTodos(), app);
	}

	public ContEstadosFuncional(int modoOperacion) {
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

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(EstadoFuncional arg0) {

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {

		return null;
	}

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			NegocioEstadoFuncional negocio = NegocioEstadoFuncional
					.getInstance();
			if (accion <= Accion.CONSULTAR) {
				EstadoFuncional estado = getDatoSeleccionado();
				negocio.setEstadoFuncional(estado);
				if (estado == null && accion != Accion.AGREGAR) {
					app.mostrarError("Debe seleccionar un dato del catalago");

				} else
					new ContEstadoFuncional(accion, estado, this, app);

			}
			if (accion == Accion.IMPRIMIR_TODO) {

				HashMap parametros = new HashMap();
				JRDataSource ds = new JRBeanCollectionDataSource(
						negocio.getTodos());
				app.agregarReporte();
				Jasperreport reporte = app.getReporte();

				parametros.put("logo", AppDemeter.class.getResource("/")
						.getPath() + "cpc/demeter/imagenes/cintillo_inst.png");
				parametros.put("usuario", app.getUsuario().getNombreIdentidad()
						+ " " + app.getUsuario().getApellido() + " ["
						+ app.getUsuario().getNombre() + "]");
				reporte.setSrc("reportes/rpt_fallasensilo.jasper");
				reporte.setParameters(parametros);
				reporte.setDatasource(ds);
				reporte.setType(getTipoReporte());
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Debe seleccionar un dato del catalago");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

}
