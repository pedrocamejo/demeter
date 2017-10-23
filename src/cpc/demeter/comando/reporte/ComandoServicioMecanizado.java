package cpc.demeter.comando.reporte;

import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;

import cpc.ares.interfaz.IAplicacion;
import cpc.ares.interfaz.IComando;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.Index;
import cpc.modelo.demeter.gestion.OrdenTrabajoMecanizado;
import cpc.modelo.demeter.gestion.SabanaMecanizadoAgricola;
import cpc.modelo.ministerio.dimension.UbicacionSector;
import cpc.negocio.demeter.gestion.NegocioOrdenTrabajoMecanizado;
import cpc.persistencia.demeter.implementacion.gestion.PerSabanaMecanizadoAgricola;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompVentanaDesdeHasta;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ComandoServicioMecanizado implements IComando, EventListener {

	private static final long serialVersionUID = -2688081335887322152L;
	private AccionFuncionalidad funcionalidades;
	private Map<String, Object> mapa = new Hashtable<String, Object>();
	private int id;
	private IZkAplicacion app;
	private CompVentanaDesdeHasta vistaReporte;

	public void ejecutar() {
		try {
			menuImprimir();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void menuImprimir() throws ExcFiltroExcepcion {
		vistaReporte = new CompVentanaDesdeHasta(this);
		// vistaReporte.setSoloFecha();
		vistaReporte.setTitle("Servicio de Mecanizacion");
		vistaReporte.setOtro("Sector:", NegocioOrdenTrabajoMecanizado
				.getInstance().getSectores());
		vistaReporte.getOtro().setModelo(
				NegocioOrdenTrabajoMecanizado.getInstance().getSectores(),
				"getParroquia");
		vistaReporte.setVisibleTipoReporte(true);
		vistaReporte.setVisibleFormatoRpt(true);
		app.agregarHija(vistaReporte);
	}

	public void mostrarDialogoDeFechas() throws ExcFiltroExcepcion {
		vistaReporte = new CompVentanaDesdeHasta<OrdenTrabajoMecanizado>(this);
		// NegocioOrdenTrabajoMecanizado negocio =
		// NegocioOrdenTrabajoMecanizado.getInstance();
		app.agregarHija(vistaReporte);
	}

	public IAplicacion getApp() {
		return app;
	}

	public void setApp(IAplicacion arg0) {
		this.app = (IZkAplicacion) arg0;
	}

	public Map<String, Object> getParametros() {
		return mapa;
	}

	public void setParametros(Map<String, Object> parametros) {
		this.mapa = parametros;
	}

	public void agregarParametro(String codigo, Object valor) {
		mapa.put(codigo, valor);
	}

	public AccionFuncionalidad getAccionFuncionalidad() {
		return funcionalidades;
	}

	public void setAccionFuncionalidad(AccionFuncionalidad funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

	public Object seleccionar(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void onEvent(Event event) throws Exception {
		if (event.getTarget() == vistaReporte.getAceptar()) {
			try {
				imprimir();
			} catch (Exception e) {
				e.printStackTrace();
				app.mostrarError(e.getMessage());
			}
		}
	}

	public boolean criteriosValidos() {
		if (vistaReporte.getFechaInicio().getValue() == null) {
			app.mostrarError("Debe especificar una fecha de inicio para el reporte");
			vistaReporte.getFechaInicio().setFocus(true);
			return false;
		}
		if (vistaReporte.getFechaFinal().getValue() == null) {
			app.mostrarError("Debe especificar una fecha de fin para el reporte");
			vistaReporte.getFechaFinal().setFocus(true);
			return false;
		}
		if (vistaReporte.getFechaInicio().getValue()
				.after(vistaReporte.getFechaFinal().getValue())) {
			app.mostrarError("El intervalo de fecha es inválido");
			vistaReporte.getFechaInicio().setFocus(true);
			return false;
		}
		if (!vistaReporte.getResumido().isChecked()
				&& !vistaReporte.getDetallado().isChecked()) {
			app.mostrarError("Debe seleccionar el tipo de Reporte");
			vistaReporte.getResumido().setFocus(true);
			return false;
		}
		if (vistaReporte.getCmbFormatoRpt().getSelectedItem() == null) {
			app.mostrarError("Debe seleccionar el Formato para el Reporte");
			vistaReporte.getCmbFormatoRpt().setFocus(true);
			return false;
		}
		return true;
	}

	@SuppressWarnings({ "unchecked" })
	public void imprimir() {
		try {
			if (criteriosValidos()) {
				Date inicio = vistaReporte.getFechaInicio().getValue();
				Date fin = vistaReporte.getFechaFinal().getValue();
				UbicacionSector sector = null;
				try {
					if (!vistaReporte.getOtro().getValue().trim().isEmpty()) {
						if (vistaReporte.getOtro().getSeleccion() != null)
							sector = (UbicacionSector) vistaReporte.getOtro().getSeleccion();
						else
							throw new ExcArgumentoInvalido("Sector no válido");
					}
					// Crear criterios para el Reporte
					List<SabanaMecanizadoAgricola> listaSabanaMecanizadoAgricola = new PerSabanaMecanizadoAgricola().getSabanaServicioMecanizacion(inicio, fin, sector);
					// Definición de Parametros
					if (listaSabanaMecanizadoAgricola.size() > 0) {
						HashMap parametros = new HashMap();
						String fecIni, fecFin = "";
						parametros.put("sede", app.getUsuario().getSede().getNombre());
						parametros.put("fecha",String.format("%1$td/%1$tm/%1$tY", inicio));
						parametros.put("fechaFin",String.format("%1$td/%1$tm/%1$tY", fin));
						parametros.put("usuario", app.getUsuario().toString());
						parametros.put("logo", Index.class.getResource("/") .getPath()+ "../../imagenes/cintillo_inst.png");
						parametros.put("SUBREPORT_DIR", Index.class.getResource("/").getPath() + "cpc/demeter/reporte/fuentes/");
						parametros.put("coleccionMecanizadoAgricola",new JRBeanCollectionDataSource(listaSabanaMecanizadoAgricola));
						// Cargar Parametros del Exporter
						if (vistaReporte.getCmbFormatoRpt().getSelectedItem().getValue().toString() == CompVentanaDesdeHasta.XLS) {
							parametros.put("exportParameter",app.getXLSParameters(true));
						}
						// Cargar el DataSource del Reporte Principal
						JRDataSource ds = new JRBeanCollectionDataSource(listaSabanaMecanizadoAgricola);
						// Mostrar el Reporte
						app.agregarReporte();
						Jasperreport reporte = app.getReporte();
						if (vistaReporte.getResumido().isChecked())
							reporte.setSrc("reportes/serviciomecanizacion2.jasper");
						else
							reporte.setSrc("reportes/serviciomecanizacion_detallado.jasper");
						reporte.setParameters(parametros);
						reporte.setDatasource(ds);
						reporte.setType(vistaReporte.getCmbFormatoRpt().getSelectedItem().getValue().toString());
					} else {
						app.mostrarError("No existen datos para los criterios de busqueda especificados");
					}
				} catch (ExcArgumentoInvalido e) {
					// TODO: handle exception
					app.mostrarError(e.getMessage());
				} catch (Exception e) {
					// TODO: handle exception
					app.mostrarError(e.getMessage());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			// e.getStackTrace();
		}
	}
}
