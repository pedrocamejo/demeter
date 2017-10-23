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
import cpc.demeter.controlador.mantenimiento.ContRegistroFalla;
import cpc.modelo.demeter.mantenimiento.RegistroFalla;
import cpc.negocio.demeter.mantenimiento.NegocioRegistroFalla;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContRegistrosFallas extends ContCatalogo<RegistroFalla> {

	private static final long serialVersionUID = -5149724463777733770L;
	private AppDemeter app;

	public ContRegistrosFallas() {
		super();
	}

	public ContRegistrosFallas(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioRegistroFalla falla = NegocioRegistroFalla.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Registro de fallas", falla.getTodos(), app);
	}

	public ContRegistrosFallas(int modoOperacion) {
		super(modoOperacion);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Objeto Mantenimiento", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getObjetoMantenimiento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo de Falla", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getTipoFalla");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Momento de Falla", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMomentoFalla");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha Registro", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getFechaRegistro");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha Falla", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getFechaFalla");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Trabajador Reporta", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getTrabajadorReporta");
		encabezado.add(titulo);

		return encabezado;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(RegistroFalla arg0) {

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
			NegocioRegistroFalla negocio = NegocioRegistroFalla.getInstance();
			if (accion <= Accion.CONSULTAR) {
				RegistroFalla falla = getDatoSeleccionado();
				negocio.setRegistroFalla(falla);
				if (falla == null && accion != Accion.AGREGAR) {
					app.mostrarError("Debe seleccionar un dato del catalago");

				} else
					new ContRegistroFalla(accion, falla, this, app);

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
