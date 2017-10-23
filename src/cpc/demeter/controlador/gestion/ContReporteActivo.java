package cpc.demeter.controlador.gestion;

import java.util.Date;
import java.util.HashMap;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Listbox;

import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.comando.ComandoReporteActivo;
import cpc.demeter.reporte.datasource.CustomDataSourceMovimientoActivo;
import cpc.demeter.vista.gestion.UIReporteActivo;
import cpc.negocio.demeter.gestion.NegocioReporteActivo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;
import cva.pc.demeter.utilidades.Fecha;

public class ContReporteActivo implements EventListener {

	private NegocioReporteActivo servicio;
	private AppDemeter app;
	private UIReporteActivo vista;

	public ContReporteActivo(ComandoReporteActivo llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcFiltroExcepcion {
		this.servicio = NegocioReporteActivo.getInstance();
		this.app = app;
		this.vista = new UIReporteActivo(this, "Listado de Reportes", 500);
		this.app.agregarAEscritorio(vista);
	}

	public void onEvent(Event event) throws Exception {
		if (event.getTarget() == vista.getAceptar()) {
			validar(vista.getCmbTipoReporte().getSelectedIndex());
			imprimir(vista.getCmbTipoReporte().getSelectedIndex());
			vista.close();
		}
		if (event.getName() == Events.ON_SELECT) {
			System.out.println("Id: "
					+ ((Listbox) event.getTarget()).getSelectedItem()
							.getIndex());
			if (((Listbox) event.getTarget()).getAttribute("nombre").equals(
					"reporte"))
				vista.grupoFiltroVisible(vista.getCmbTipoReporte()
						.getSelectedIndex());
		}
	}

	private void validar(int valor) throws WrongValuesException {
		switch (valor) {
		case 2:
			if (vista.getCmbActivosI().getSeleccion() == null)
				throw new WrongValueException(vista.getCmbActivosI(),
						"Debe seleccionar un activo");
			if (vista.getCmbActivosH().getSeleccion() == null)
				throw new WrongValueException(vista.getCmbActivosH(),
						"Debe seleccionar un activo");
		}
		if (vista.getCmbFormatoRpt().getSelectedItem() == null) {
			throw new WrongValueException(vista.getCmbFormatoRpt(),
					"Debe seleccionar el Formato para el Reporte");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void imprimir(int valor) {
		Date fecha = new Date();
		HashMap parametros = new HashMap();
		System.out.println(Fecha.obtenerFecha(fecha).substring(0, 2));

		parametros.put("nombreReporte", vista.getCmbTipoReporte()
				.getSelectedItem().getLabel());

		parametros.put("rutaImagenEncabezado", Index.class.getResource("/")
				.getPath() + "../../imagenes/cintillo_inst.png");
		// Cargar Parametros del Exporter
		if (vista.getCmbFormatoRpt().getSelectedItem().getValue().toString() == UIReporteActivo.XLS) {
			parametros.put("exportParameter", app.getXLSParameters(true));
		}
		switch (valor) {
		case 0:
			parametros.put("dia", Fecha.obtenerFecha(fecha).substring(0, 2));
			parametros.put("mes", Fecha.obtenerMesEnLetra(fecha));
			parametros.put("fecha", Fecha.obtenerFecha(fecha));
			JRDataSource ds = new JRBeanCollectionDataSource(
					servicio.obtenerAlmacenesActivos());
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();
			if(vista.getCmbFormatoRpt().getSelectedItem().getValue().toString() == UIReporteActivo.XLS) {
				reporte.setSrc("reportes/"
						+"InventarioActivosXLS.jasper");
			}else 
			reporte.setSrc("reportes/"
					+ vista.getCmbTipoReporte().getSelectedItem().getValue());
			reporte.setParameters(parametros);
			reporte.setDatasource(ds);
			reporte.setType(vista.getCmbFormatoRpt().getSelectedItem()
					.getValue().toString());
			break;
		case 1:
			try {
				parametros.put("fecha", Fecha.obtenerFecha(fecha));
				parametros.put("unidadAdministrativa", servicio
						.obtenerUnidadAdministrativa().getNombre());
				app.agregarReporte();
				Jasperreport reporte2 = app.getReporte();

				reporte2.setSrc("reportes/"
						+ vista.getCmbTipoReporte().getSelectedItem()
								.getValue());
				reporte2.setParameters(parametros);
				reporte2.setDatasource(new CustomDataSourceMovimientoActivo(
						servicio.obtenerTrasladosPorFecha(vista
								.getDtmFechaInicial().getValue(), vista
								.getDtmFechaFinal().getValue())));
				reporte2.setType(vista.getCmbFormatoRpt().getSelectedItem()
						.getValue().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case 2:
			try {
				parametros.put("fecha", Fecha.obtenerFecha(fecha));
				parametros.put("unidadAdministrativa", servicio
						.obtenerUnidadAdministrativa().getNombre());
				app.agregarReporte();
				Jasperreport reporte3 = app.getReporte();

				reporte3.setSrc("reportes/"
						+ vista.getCmbTipoReporte().getSelectedItem()
								.getValue());
				reporte3.setParameters(parametros);
				reporte3.setDatasource(new CustomDataSourceMovimientoActivo(
						servicio.obtenerMovimientosPorActivo(vista
								.getCmbActivosI().getSeleccion(), vista
								.getCmbActivosH().getSeleccion())));
				reporte3.setType(vista.getCmbFormatoRpt().getSelectedItem()
						.getValue().toString());

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

}
