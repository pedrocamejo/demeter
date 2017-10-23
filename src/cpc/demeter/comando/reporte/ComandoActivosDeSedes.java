package cpc.demeter.comando.reporte;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;





import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zkplus.spring.SpringUtil;

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
import cpc.modelo.demeter.administrativo.ContratoMecanizado;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.ImpuestoDocumentoFiscal;
import cpc.modelo.demeter.gestion.OrdenTrabajoMecanizado;
import cpc.modelo.ministerio.dimension.UbicacionSector;
import cpc.negocio.demeter.administrativo.NegocioFactura;
import cpc.persistencia.SessionDao;
import cpc.persistencia.sigesp.implementacion.PerUbicacionGeografica;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompVentanaDesdeHasta;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ComandoActivosDeSedes implements IComando, EventListener {

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
		List<String> estado = new ArrayList<String>();
		estado.add("Activos Usados");
		estado.add("Activos No Usados");

		vistaReporte = new CompVentanaDesdeHasta(this);
		vistaReporte.setTitle("Activos Presentes En las Sedes");
		vistaReporte.setSoloOtro("reporte", estado);
		app.agregarHija(vistaReporte);
	}

	/*
	 * public void mostrarDialogoDeFechas() throws ExcFiltroExcepcion {
	 * 
	 * vistaReporte= new CompVentanaDesdeHasta<OrdenTrabajoMecanizado>( this);
	 * //NegocioOrdenTrabajoMecanizado negocio =
	 * NegocioOrdenTrabajoMecanizado.getInstance();
	 * app.agregarHija(vistaReporte);
	 * 
	 * }
	 */

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
			String icaro = (String) SpringUtil.getBean("contrato");
			// StringBuilder cadena = new StringBuilder();
			String cadena = "";
			cadena = cadena + icaro;
			String usuario = app.getUsuario().toString();
			cadena = cadena + "reporteDeActivosSedes";
			cadena = cadena + "&usuario=" + usuario;

			Execution ad = Executions.getCurrent();
			System.out.println(cadena);
			ad.sendRedirect(cadena);

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

}
