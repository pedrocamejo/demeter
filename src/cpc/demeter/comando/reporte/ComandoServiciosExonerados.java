package cpc.demeter.comando.reporte;

import java.util.ArrayList;
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
import cpc.modelo.demeter.administrativo.DetalleExoneracionContrato;
import cpc.modelo.demeter.gestion.OrdenTrabajoMecanizado;
import cpc.negocio.demeter.administrativo.NegocioAprobacionExoneracionContrato;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompVentanaDesdeHasta;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ComandoServiciosExonerados implements IComando, EventListener {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 949338068749549239L;
	/**
	 * 
	 */
	
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
		List<String> estado = new ArrayList<String>();
		estado.add("Preservicios");
		estado.add("Postservicios");
		estado.add("Todas");

		vistaReporte = new CompVentanaDesdeHasta(this);
		vistaReporte.setTitle("Libro De Ventas Vs Productos/Servicos");

		// vistaReporte.setOtro("estado:", estado) ;
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

	@SuppressWarnings({ "unchecked", "deprecation" })
	public void imprimir() {
		try {
			NegocioAprobacionExoneracionContrato negocio = NegocioAprobacionExoneracionContrato.getInstance();
			Date inicio = vistaReporte.getFechaInicio().getValue();
			Date fin = vistaReporte.getFechaFinal().getValue();
			// String tipo = vistaReporte.getOtro().getValue();
			HashMap parametros = new HashMap();
			parametros.put("desde", String.format("%1$td/%1$tm/%1$tY", inicio));
			parametros.put("hasta", String.format("%1$td/%1$tm/%1$tY", fin));
			parametros.put("usuario", app.getUsuario().toString());
			parametros.put("logo", Index.class.getResource("/").getPath()
					+ "../../imagenes/cintillo_inst.png");
			parametros.put("exportParameter", app.getXLSParameters(false));
			// parametros.put("estado",String , tipo);

			/*
			 * if (tipo.equals("Preservicios")) { JRDataSource ds =new
			 * JRBeanCollectionDataSource
			 * (negocio.getTodosprefecha(String.format("%1$td/%1$tm/%1$tY",
			 * inicio), String.format("%1$td/%1$tm/%1$tY", fin)));
			 * 
			 * app.agregarReporte(); Jasperreport reporte = app.getReporte();
			 * reporte.setSrc("reportes/facturasDia.jasper");
			 * reporte.setParameters(parametros); reporte.setDatasource(ds);
			 * reporte.setType("pdf");
			 * 
			 * }
			 * 
			 * else if (tipo.equals("Postservicios")){ JRDataSource ds =new
			 * JRBeanCollectionDataSource
			 * (negocio.getTodosPostfecha(String.format("%1$td/%1$tm/%1$tY",
			 * inicio), String.format("%1$td/%1$tm/%1$tY", fin)));
			 * app.agregarReporte(); Jasperreport reporte = app.getReporte();
			 * reporte.setSrc("reportes/facturasDia.jasper");
			 * reporte.setParameters(parametros); reporte.setDatasource(ds);
			 * reporte.setType("pdf"); } else { JRDataSource ds =new
			 * JRBeanCollectionDataSource
			 * (negocio.getTodosfecha(String.format("%1$td/%1$tm/%1$tY",
			 * inicio), String.format("%1$td/%1$tm/%1$tY", fin)));
			 * app.agregarReporte(); Jasperreport reporte = app.getReporte();
			 * reporte.setSrc("reportes/facturasDia.jasper");
			 * reporte.setParameters(parametros); reporte.setDatasource(ds);
			 * reporte.setType("pdf"); }
			 */
			List<DetalleExoneracionContrato> lista = negocio.getDetalles(inicio, fin);
			JRDataSource ds = new JRBeanCollectionDataSource(
					lista);
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();
			//reporte.setSrc("reportes/listadoProductosFacturados.jasper");
			reporte.setSrc("reportes/serviciosExonerados.jasper");
			reporte.setParameters(parametros);
			reporte.setDatasource(ds);
			reporte.setType("xls");
			;

			// JRDataSource ds =new
			// JRBeanCollectionDataSource(negocio.getTodos());

			/*
			 * app.agregarReporte(); Jasperreport reporte = app.getReporte();
			 * 
			 * reporte.setSrc("reportes/facturasDia.jasper");
			 * reporte.setParameters(parametros); reporte.setDatasource(ds);
			 * reporte.setType("pdf");
			 */
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

}