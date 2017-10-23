package cpc.demeter.catalogo.vialidad;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;





import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zkplus.spring.SpringUtil;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.controlador.vialidad.ContCotizacionVialidad;
import cpc.modelo.demeter.administrativo.CotizacionVialidad;
import cpc.modelo.demeter.administrativo.EstadoContrato;
import cpc.negocio.demeter.administrativo.NegocioCotizacion;
import cpc.negocio.demeter.administrativo.NegocioCotizacionVialidad;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContCotizacionesVialidad extends ContCatalogo<CotizacionVialidad> implements EventListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2087062091112520L;
	private AppDemeter app;
	private NegocioCotizacionVialidad negocioCotizacionVialidad;
	
	public ContCotizacionesVialidad(AccionFuncionalidad accionesValidas, AppDemeter app)
		throws SQLException, ExcAccesoInvalido, PropertyVetoException,
		ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
		ExcArgumentoInvalido, ExcFiltroExcepcion
	{
		
		negocioCotizacionVialidad = NegocioCotizacionVialidad.getInstance();
		dibujar(accionesValidas, "COTIZACIONES",negocioCotizacionVialidad.getCotizacionesVialidad(), app);
		this.app = app;
	}
	
	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
	CompEncabezado titulo;
	List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
	
	titulo = new CompEncabezado("Control", 150);
	titulo.setOrdenable(true);
	titulo.setMetodoBinder("getStrNroDocumento");
	encabezado.add(titulo);
	
	titulo = new CompEncabezado("Fecha", 100);
	titulo.setOrdenable(true);
	titulo.setMetodoBinder("getFechaString");
	encabezado.add(titulo);
	
	titulo = new CompEncabezado("Cliente", 300);
	titulo.setOrdenable(true);
	titulo.setMetodoBinder("getNombreCliente");
	encabezado.add(titulo);
	
	titulo = new CompEncabezado("Monto Base", 100);
	titulo.setOrdenable(true);
	titulo.setMetodoBinder("getMonto");
	encabezado.add(titulo);
	
	titulo = new CompEncabezado("Estado", 120);
	titulo.setOrdenable(true);
	titulo.setMetodoBinder("getEstado");
	encabezado.add(titulo);
	
	titulo = new CompEncabezado("Tipo", 120);
	titulo.setOrdenable(true);
	titulo.setMetodoBinder("getStrTipo");
	encabezado.add(titulo);
	
	titulo = new CompEncabezado("Sede", 120);
	titulo.setOrdenable(true);
	titulo.setMetodoBinder("getSede");
	encabezado.add(titulo);
	
	return encabezado;
	}
	
	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR || accion == Accion.PROCESAR) {
				CotizacionVialidad cotizacion = getDatoSeleccionado();
				if (cotizacion != null && accion == Accion.ANULAR
						&& cotizacion.getEstado().getId().equals(EstadoContrato.ESTADO_ANULADO))
					throw new Exception("El Documento ya esta Anulado");
				if (cotizacion != null  && accion == Accion.EDITAR 	&& cotizacion.getFacturado())
					throw new Exception("El Documento ya esta facturado");
				if (cotizacion != null && accion == Accion.PROCESAR) {
					boolean a = (cotizacion.getEstado().getDescripcion()
							.equals("Por Firmar"));
					boolean b = (cotizacion.getEstado().getDescripcion()
							.equals("Activo"));
					if (!a && !b)
						throw new Exception("Solo puede Procesar Documentos en estado Por Firmar O Activo");
				}
				if (cotizacion == null || accion == Accion.AGREGAR)
					cotizacion = new CotizacionVialidad();
				new ContCotizacionVialidad(accion, cotizacion, this, this.app);
			}
			else
				imprimir(getDatoSeleccionado());
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun registro");
		} catch (Exception e) {
			app.mostrarError(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(CotizacionVialidad arg0) {
		return null;
	}
	
	private void imprimirDetalle() {
		
		
		String icaro = (String) SpringUtil.getBean("contrato");
		String cadena = "";
		cadena = cadena + icaro;
		CotizacionVialidad ctto = getDatoSeleccionado();
		switch (ctto.getTipoContrato().getId()) {
		case 1:
			cadena = cadena + "rpt_contratoserviciomecanizadogravado&numerocontrato=";
			break;
		case 2:
			cadena = cadena + "rpt_contratoserviciomecanizadoexonerado_carta&numerocontrato=";
			break;
		case 3:
			cadena = cadena + "rpt_contratoserviciomecanizadocredito_carta&numerocontrato=";
			break;
		}
		cadena = cadena + ctto.getId() + "&Sede="
			+ app.getSede().getNombre()
			+ "&jdbcUrl=" + SessionDao.getConfiguration().getProperty(
					"hibernate.connection.url");
		Execution ad = Executions.getCurrent();
		ad.sendRedirect(cadena);
	}

	
	public void imprimir(CotizacionVialidad cotizacion) {
		try {
			HashMap<String, String> parametros = new HashMap<String, String>();
			parametros.put("fecha",String.format("%1$td/%1$tm/%1$tY", cotizacion.getFecha()));
			parametros.put("usuario", app.getUsuario().toString());
			parametros.put("logo", Index.class.getResource("/").getPath()
					+ "../../imagenes/cintillo_inst.png");
			JRDataSource ds = new JRBeanCollectionDataSource(cotizacion.getDetallesContrato());
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();
			if (cotizacion.getTipoContrato().getNombre().equals("SERVICIO A PRODUCTOR BENEFICIARIO ORGANISMO CREDITICIO"))
				reporte.setSrc("reportes/CotizacionVialidadBeneficiario.jasper");
			else
				reporte.setSrc("reportes/CotizacionVialidad.jasper");
			reporte.setParameters(parametros);
			reporte.setDatasource(ds);
			reporte.setType("pdf");
	} catch (NullPointerException e) {
		e.printStackTrace();
		app.mostrarError("No ha seleccionado ninguna solicitud");
	} catch (Exception e) {
		app.mostrarError(e.getMessage());
	}
	}

}