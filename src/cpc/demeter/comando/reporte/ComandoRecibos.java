package cpc.demeter.comando.reporte;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import cpc.ares.interfaz.IAplicacion;
import cpc.ares.interfaz.IComando;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.Index;
import cpc.modelo.demeter.gestion.OrdenTrabajoMecanizado;
import cpc.negocio.demeter.administrativo.NegocioRecibo;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompVentanaDesdeHasta;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ComandoRecibos implements IComando, EventListener {

	private static final long serialVersionUID = -2688081335887322152L;
	private AccionFuncionalidad funcionalidades;
	private Map<String, Object> mapa = new Hashtable<String, Object>();
	private int id;
	private IZkAplicacion app;
	@SuppressWarnings("unchecked")
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
		vistaReporte.setVisibleOtro(false);
		vistaReporte.setTitle("Recibos Diarios");

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
		// TODO Auto-generated method stub
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

	@SuppressWarnings({ "unchecked" })
	public void imprimir() {
		try {
			NegocioRecibo negocio = NegocioRecibo.getInstance();

			Date inicio = vistaReporte.getFechaInicio().getValue();
			Date fin = vistaReporte.getFechaFinal().getValue();

			HashMap parametros = new HashMap();
			parametros
					.put("inicio", String.format("%1$td/%1$tm/%1$tY", inicio));
			parametros.put("fin", String.format("%1$td/%1$tm/%1$tY", fin));
			parametros.put("usuario", app.getUsuario().toString());
			parametros.put("logo", Index.class.getResource("/").getPath()
					+ "../../imagenes/cintillo_inst.png");
			JRDataSource ds = new JRBeanCollectionDataSource(
					negocio.getRecibos(inicio, fin));
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();

			reporte.setSrc("reportes/Recibos2.jasper");
			reporte.setParameters(parametros);
			reporte.setDatasource(ds);
			reporte.setType("pdf");
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

}
