package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;




import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Script;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index; 
import cpc.demeter.controlador.administrativo.ContCierreDiario;
import cpc.modelo.demeter.administrativo.CierreDiario;
import cpc.modelo.demeter.basico.Servicio;
import cpc.negocio.demeter.administrativo.NegocioCierreDiario;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContCierresDiarios extends ContCatalogo<CierreDiario> implements
		EventListener {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContCierresDiarios(AccionFuncionalidad accionesValidas,AppDemeter app) 
			throws SQLException, ExcAccesoInvalido, PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido, ExcFiltroExcepcion 
	{
		NegocioCierreDiario servicios = NegocioCierreDiario.getInstance();
		this.app = app;
		dibujar(accionesValidas, "CIERRES DIARIO", servicios.getTodos(), app);
		desactivarTipoReporte();
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Fecha", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto Facturado", 120);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getIngresoFacturado");
		// titulo.setMetodoModelo("usuario.identidad.cedula");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto Cobrado", 120);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getMontoRecibos");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Iva Generado", 120);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getImpuestoFacturado");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cuentas por pagar", 120);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getCuentasPorPagar");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto Depositado", 120);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getMontoDepositado");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);
		return encabezado;
	}

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		try {
			
			int accion = (Integer) event.getTarget().getAttribute("pos");
			
			if (accion <= Accion.CONSULTAR)
			{
				CierreDiario cierre = getDatoSeleccionado();
				new ContCierreDiario(accion, cierre, this, app);
			}
			else
			{
				NegocioCierreDiario negocio = NegocioCierreDiario.getInstance();

				Script scrip = new Script();
				scrip.setType("text/javascript");
				String icaro = (String) SpringUtil.getBean("icaro");
				StringBuilder cadena = new StringBuilder();
				cadena.append(icaro);
				CierreDiario cierre = getDatoSeleccionado();
				
				if (accion == Accion.IMPRIMIR_ITEM)
				{
					HashMap parametros = new HashMap();
					parametros.put("fecha",String.format("%1$td/%1$tm/%1$tY",cierre.getFecha()));

					negocio.setCierreDiario(cierre);
					cierre = negocio.getCierreDiario();

					parametros.put("coleccionDocumentos",new JRBeanCollectionDataSource(cierre.getDocumentos()));
					parametros.put("coleccionCtasPorCobrar",new JRBeanCollectionDataSource(cierre.getCuentasCobrar()));
					parametros.put("coleccionCtasPorPagar",new JRBeanCollectionDataSource(cierre.getCuentasPagadas()));
					parametros.put("coleccionDepositos",new JRBeanCollectionDataSource(negocio.getDepositos(negocio.getCierreDiario().getFecha())));
					parametros.put("coleccionAdelantos",new JRBeanCollectionDataSource(cierre.getCuentasAdelantos()));
					parametros.put("coleccionIngresoCaja",new JRBeanCollectionDataSource(negocio.getFormaPago(cierre.getFecha())));
					parametros.put("SUBREPORT_DIR", Index.class.getResource("/").getPath()+ "../../reportes/");
					parametros.put("usuario", app.getUsuario().toString());
					parametros.put("logo", Index.class.getResource("/").getPath() + "../../imagenes/cintillo_inst.png");

					List<CierreDiario> lista = new ArrayList<CierreDiario>();
					lista.add(cierre);
					
					JRDataSource ds = new JRBeanCollectionDataSource(lista);
					app.agregarReporte();

					Jasperreport reporte = app.getReporte();
					reporte.setSrc("reportes/cierreDetalle.jasper");

					reporte.setParameters(parametros);
					reporte.setDatasource(ds);

					reporte.setType("pdf");
					
					
				} 
				else if (accion == Accion.IMPRIMIR_TODO) 
				{
					HashMap parametros = new HashMap();
					parametros.put("usuario", app.getUsuario().toString());
					parametros.put("logo", Index.class.getResource("/").getPath() + "../../imagenes/cintillo_inst.png");
					JRDataSource ds = new JRBeanCollectionDataSource(negocio.getTodos());
					app.agregarReporte();
					Jasperreport reporte = app.getReporte();

					reporte.setSrc("reportes/cierreTodos.jasper");
					reporte.setParameters(parametros);
					reporte.setDatasource(ds);
					reporte.setType("pdf");
					
				}

			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void imprimir(Date fecha) {
		try {
			NegocioCierreDiario negocio = NegocioCierreDiario.getInstance();
			HashMap parametros = new HashMap();
			JRDataSource ds = new JRBeanCollectionDataSource(negocio.getDocumentos(fecha));
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();
			parametros.put("fecha", fecha);
			reporte.setSrc("reportes/facturasDia.jasper");
			reporte.setParameters(parametros);
			reporte.setDatasource(ds);
			reporte.setType("pdf");
		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(CierreDiario dato) {
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

}























