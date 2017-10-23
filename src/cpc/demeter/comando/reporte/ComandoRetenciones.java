package cpc.demeter.comando.reporte;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;




import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import cpc.ares.interfaz.IAplicacion;
import cpc.ares.interfaz.IComando;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.Index;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.negocio.demeter.administrativo.NegocioFormaPago;
import cpc.negocio.demeter.administrativo.NegocioRecibo;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompVentanaDesdeHasta;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.utilidades.Fecha;

public class ComandoRetenciones implements IComando, EventListener {

	private static final long serialVersionUID = -2688081335887322152L;
	private AccionFuncionalidad funcionalidades;
	private Map<String, Object> mapa = new Hashtable<String, Object>();
	private int id;
	private NegocioFormaPago negocio;
	private IZkAplicacion app;
	@SuppressWarnings("unchecked")
	private CompVentanaDesdeHasta  vistaReporte;

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
		vistaReporte.setTitle("REINTEGROS");
		
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
				Date desde =  vistaReporte.getFechaInicio().getValue();
				Date hasta =  vistaReporte.getFechaFinal().getValue();
				imprimir(desde,hasta);
			} catch (Exception e) {
				e.printStackTrace();
				app.mostrarError(e.getMessage());
			}
		}

	}

	private void imprimir(Date desde, Date hasta) {
		// TODO Auto-generated method stub

		try {
			negocio = NegocioFormaPago.getInstance();
			List lista = negocio.getRetencionesreporte(desde,hasta); 

			HashMap parametros = new HashMap();
			parametros.put("img", Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");
			parametros.put("desde",Fecha.obtenerFecha(desde));
			parametros.put("hasta",Fecha.obtenerFecha(hasta));
			JRDataSource ds = new JRBeanCollectionDataSource(lista);
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();
			reporte.setSrc("reportes/retensiones.jasper");
			reporte.setParameters(parametros);
			reporte.setDatasource(ds);
			reporte.setType("xls");
			
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
		
		
	}

	 

}
