package cpc.demeter.comando.reporte;

import java.util.ArrayList;
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
import cpc.negocio.demeter.gestion.NegocioReporteActivo;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompVentanaDesdeHasta;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ComandoActivosMaquinariaImplementosSede implements IComando, EventListener {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4824019739244448832L;
	private AccionFuncionalidad funcionalidades;
	private Map<String, Object> mapa = new Hashtable<String, Object>();
	private int id;
	private IZkAplicacion app;
	@SuppressWarnings("unchecked")
	private CompVentanaDesdeHasta vistaReporte;

	public void ejecutar() {
		try {
		//	menuImprimir();
imprimir();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void menuImprimir() throws ExcFiltroExcepcion {
		List<String> estado = new ArrayList<String>();
		estado.add("Preservicios");
		estado.add("Postservicios");
		estado.add("Todas");

		vistaReporte = new CompVentanaDesdeHasta(this);
		vistaReporte.getFechaInicio().detach();;
		vistaReporte.getFechaInicio().detach();;
		vistaReporte.setTitle("Maquinaria EN Campo");

	//	vistaReporte.setOtro("estado:", estado);
		//vistaReporte.setVisible(visible)
		vistaReporte.setVisibleFormatoRpt(true);
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

	
			try {

				imprimir();
			} catch (Exception e) {
				e.printStackTrace();
				app.mostrarError(e.getMessage());
			}
		

	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	public void imprimir() {
		try {
			NegocioReporteActivo negocio = NegocioReporteActivo.getInstance();
			//Date inicio = vistaReporte.getFechaInicio().getValue();
			//Date fin = vistaReporte.getFechaFinal().getValue();
			//String tipo = vistaReporte.getOtro().getValue();
			//String tipoReporte = (String) vistaReporte.getCmbFormatoRpt().getSelectedItem().getValue();
			HashMap parametros = new HashMap();
			//parametros.put("inicio", String.format("%1$td/%1$tm/%1$tY", inicio));
			//parametros.put("fin", String.format("%1$td/%1$tm/%1$tY", fin));
			//parametros.put("usuario", app.getUsuario().toString());
		//	parametros.put("logo", Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");
			// parametros.put("estado",String , tipo);
			
			
			List<Object> maquinarias =new ArrayList<Object>();
			maquinarias.add(	negocio.getTodaMaquinariaEimplementoSede());
				JRDataSource ds = new JRBeanCollectionDataSource(
				maquinarias	);
				app.agregarReporte();
				Jasperreport reporte = app.getReporte();
				reporte.setParameters(parametros);
				reporte.setDatasource(ds);
				parametros.put("exportParameter", app.getXLSParameters(false));
				reporte.setSrc("reportes/ActivosMaquinariaImplementosSede.jasper");
				reporte.setType("xls");
				
			

			;

			
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

}
