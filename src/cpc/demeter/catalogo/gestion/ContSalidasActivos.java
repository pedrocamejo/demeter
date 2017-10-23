package cpc.demeter.catalogo.gestion;

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
import cpc.demeter.Index;
import cpc.demeter.controlador.gestion.ContSalidaActivo;
import cpc.modelo.demeter.gestion.TrasladoActivo;
import cpc.negocio.demeter.gestion.NegocioTrasladoActivo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContSalidasActivos extends ContCatalogo<TrasladoActivo> implements
		EventListener, Serializable {

	private static final long serialVersionUID = 2139742154136431708L;
	private AppDemeter app;
	private NegocioTrasladoActivo negocio;

	public ContSalidasActivos(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		this.negocio = NegocioTrasladoActivo.getInstance();
		this.app = app;
		dibujar(accionesValidas, "SALIDAS DE ACTIVOS",
				negocio.obtenerSalidas(), app);
		desactivarTipoReporte();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(TrasladoActivo arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

	@Override
	public List<CompEncabezado> cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Número", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getControl");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 80);
		titulo.setOrdenable(false);
		titulo.setMetodoBinder("getfechastring");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 100);
		titulo.setOrdenable(false);
		titulo.setMetodoBinder("getNombreEstado");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Registrado Por", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getUsuario");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			this.app.limpiarReporte();
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR) {
				TrasladoActivo salida = getDatoSeleccionado();
				if (salida == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione una Salida del Catalogo");
				else
					new ContSalidaActivo(accion, salida, this, app);
			}

			if (accion == Accion.IMPRIMIR_ITEM) {
				TrasladoActivo entrada = getDatoSeleccionado();
				if (entrada == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione una Salida del Catalogo");
				else
					imprimir();
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione una Salida del Catalogo");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void imprimir() {
		HashMap parametros = new HashMap();

		negocio = NegocioTrasladoActivo.getInstance();
		negocio.setTrasladoActivo(getDatoSeleccionado());
		TrasladoActivo traslado = negocio.getTrasladoActivo();
		parametros.put("observacionGeneral", traslado.getObservaciones());
		parametros.put("rutaImagenEncabezado", Index.class.getResource("/")
				.getPath() + "../../imagenes/cintillo_inst.png");
		parametros.put("nombreConductor", traslado.getResponsableTraslado());
		parametros.put("cedulaConductor", traslado.getCedulaResponsable());
		parametros.put("vehiculo", traslado.getVehiculo());
		parametros.put("placaVehiculo", traslado.getPlacaVehiculo());

		JRDataSource ds = new JRBeanCollectionDataSource(traslado.getSalidas());
		app.agregarReporte();
		Jasperreport reporte = app.getReporte();

		reporte.setSrc("reportes/SalidaActivos.jasper");
		reporte.setParameters(parametros);
		reporte.setDatasource(ds);
		reporte.setType(getTipoReporte());
	}

}
