package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.ContLabor;
import cpc.modelo.demeter.basico.Labor;
import cpc.negocio.demeter.basico.NegocioLabor;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.ventanas.CompVentanaDesdeHasta;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContLabores extends ContCatalogo<Labor> implements EventListener,
		Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;
	private CompVentanaDesdeHasta vistaReporte;


	public ContLabores(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioLabor negocio = NegocioLabor.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Labores", negocio.getTodos(), app);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Descripcion", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Servicio", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrClase");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo Servicio", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Medida Gestion", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrUnidadMedidagestion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Medida Cobro", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrUnidadMedidaCobro");
		encabezado.add(titulo);

		titulo = new CompEncabezado("ESTADO", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrInactivo");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			Integer accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion != null) {
				

				if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR) {
					Labor labor = getDatoSeleccionado();
					NegocioLabor negocio = NegocioLabor.getInstance();
					if (labor != null) {
						negocio.setLabor(labor);
					} else
						negocio.nuevo();
					if (labor == null && accion != Accion.AGREGAR)
						app.mostrarError("Seleccione un Servicio");
					else
						new ContLabor(accion, negocio.getModelo(), this, app);
				}
				if (accion == Accion.IMPRIMIR_TODO) {
				menuImprimir();
				}
				if (accion == Accion.IMPRIMIR_ITEM) {
					imprimirLaborDetallada(getDatoSeleccionado());
				}
				}else imprimirLabor1();
			} catch (NullPointerException e) {
				e.printStackTrace();
				app.mostrarError("Seleccione un Servicio");
			} catch (Exception e) {
				e.printStackTrace();
				app.mostrarError(e.getMessage());
			}

	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

	@SuppressWarnings("unchecked")
	public void imprimirLabor1() throws ExcAccesoInvalido {
		HashMap parametros = new HashMap();
		NegocioLabor negocio = NegocioLabor.getInstance();
		List<Labor> labores = new ArrayList<Labor>();
		if (vistaReporte.getOtro().getSeleccion() == "Labores Activas")
			 labores =negocio.getTodosActivos();
		if (vistaReporte.getOtro().getSeleccion() == "Labores Inactivas")
			 labores =negocio.getTodosInactivos();
		if (vistaReporte.getOtro().getSeleccion() == "Todas")
			labores=getDatos();
		List<Labor> lista = negocio.enriqueserDatos(labores);
		/*
		 * for (Labor labor : lista) {
		 * System.out.printf("%s %s %s %.4f %.4f %.4f %.4f\n",
		 * labor.getDescripcion(), labor.getStrUnidadMedidagestion(),
		 * labor.getStrUnidadMedidaCobro(), labor.getPrecio1(),
		 * labor.getPrecio2(), labor.getPrecio3(), labor.getPrecio4()); }
		 */
		JRDataSource ds = new JRBeanCollectionDataSource(lista);
		app.agregarReporte();
		Jasperreport reporte = app.getReporte();
		reporte.setSrc("reportes/Labor1.jasper");
		parametros.put("logo", AppDemeter.class.getResource("/").getPath()
				+ "cpc/demeter/imagenes/cintillo_inst.png");
		parametros.put("usuario", app.getUsuario().getNombreIdentidad() + " "
				+ app.getUsuario().getApellido() + " ["
				+ app.getUsuario().getNombre() + "]");
		reporte.setParameters(parametros);
		reporte.setDatasource(ds);
		reporte.setType(getTipoReporte());
	}

	@SuppressWarnings("unchecked")
	public void imprimirLaborDetallada(Labor labor) throws ExcAccesoInvalido {
		HashMap parametros = new HashMap();
		NegocioLabor negocio = NegocioLabor.getInstance();
		negocio.setLabor(labor);

		List<Labor> labores = new ArrayList<Labor>();
		labores.add(labor);
		labores = negocio.enriqueserDatos(labores);

		JRDataSource ds = new JRBeanCollectionDataSource(labores);
		app.agregarReporte();
		Jasperreport reporte = app.getReporte();
		reporte.setSrc("reportes/labor3.jasper");
		parametros.put("SUBREPORT_DIR", AppDemeter.class.getResource("/")
				.getPath() + "cpc/demeter/reporte/fuentes/");
		parametros.put("logo", AppDemeter.class.getResource("/").getPath()
				+ "cpc/demeter/imagenes/cintillo_inst.png");
		parametros.put("usuario", app.getUsuario().getNombreIdentidad() + " "
				+ app.getUsuario().getApellido() + " ["
				+ app.getUsuario().getNombre() + "]");

		reporte.setParameters(parametros);
		reporte.setDatasource(ds);
		reporte.setType(getTipoReporte());
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(Labor arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	@SuppressWarnings("rawtypes")
	public void menuImprimir() throws ExcFiltroExcepcion {
		List<String> tipo = new ArrayList<String>();
		tipo.add("Labores Activas");
		tipo.add("Labores Inactivas");
		tipo.add("Todas");
		vistaReporte = new CompVentanaDesdeHasta(this);
		vistaReporte.setTexto("Reporte De Labores");
		vistaReporte.setSoloOtro("tipo:", tipo);
		app.agregarHija(vistaReporte);
	}
}
