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
import org.zkoss.zul.Script;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.controlador.administrativo.ContAprobacionDebitoInterno;
import cpc.demeter.controlador.administrativo.ContAprobacionReversoRecibo;
import cpc.modelo.demeter.administrativo.AprobacionDebitoInterno;
import cpc.modelo.demeter.administrativo.AprobacionReversoRecibo;
import cpc.negocio.demeter.administrativo.NegocioAprobacionDebito;
import cpc.negocio.demeter.administrativo.NegocioAprobacionDebitoInterno;
import cpc.negocio.demeter.administrativo.NegocioAprobacionDescuento;
import cpc.negocio.demeter.administrativo.NegocioAprobacionReversoRecibo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.ventanas.CompVentanaDesdeHasta;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContAprobacionDebitosInternos extends
		ContCatalogo<AprobacionDebitoInterno> implements EventListener {

	/**
	 * 
	 */

	/**
	 * 
	 */
	
	private AppDemeter app;
	private CompVentanaDesdeHasta vistaReporte;

	public  ContAprobacionDebitosInternos(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioAprobacionDebitoInterno servicios = NegocioAprobacionDebitoInterno.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Aprobacion Debitos Internos",
				servicios.getTodos(), app);
		desactivarTipoReporte();
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Fecha", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFechaAprobacion");
		encabezado.add(titulo);

	
		titulo = new CompEncabezado("Ci/Rif Beneficiario", 120);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getIdLegalBeneficiario");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Beneficiario", 120);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getNombreBeneficiario");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Sede ", 120);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getUnidadAdministrativa");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Responsable ", 120);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getNombreResponsable");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Cedula Responsable", 120);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getCedulaResponsable");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);
		
		
		titulo = new CompEncabezado("getNroControlDocumentoAfectado", 120);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getNroControlDocumentoAfectado");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("getNroDocumentoAfectado", 120);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getNroDocumentoAfectado");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);
		return encabezado;
	}

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		try {
			
			Object aa = event.getTarget().getAttribute("pos");
			if (aa!=null){
				int  accion = (Integer) event.getTarget().getAttribute("pos");
				if (accion <= Accion.CONSULTAR) {
					AprobacionDebitoInterno aprobacionDebitoInterno = getDatoSeleccionado();
					new ContAprobacionDebitoInterno(accion, aprobacionDebitoInterno, this,
							app);
				} else {
					
					if (accion == Accion.IMPRIMIR_ITEM) {
						AprobacionDebitoInterno aprobacionDebitoInterno = getDatoSeleccionado();
						ContAprobacionDebitoInterno.imprimir(aprobacionDebitoInterno, app);
			
					} 
					else if (accion == Accion.IMPRIMIR_TODO) {
						menuImprimir();
					}	
			
			}
		
				
				

			}else if (event.getTarget() == vistaReporte.getAceptar()) {
				imprimirTodo();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	/*
	 * @SuppressWarnings("unchecked") public void imprimir(Date fecha){ try{
	 * NegocioCierreDiario negocio = NegocioCierreDiario.getInstance(); HashMap
	 * parametros = new HashMap(); JRDataSource ds =new
	 * JRBeanCollectionDataSource(negocio.getDocumentos(fecha));
	 * app.agregarReporte(); Jasperreport reporte = app.getReporte();
	 * parametros.put("fecha", fecha);
	 * reporte.setSrc("reportes/facturasDia.jasper");
	 * reporte.setParameters(parametros); reporte.setDescuentoDatasource(ds);
	 * reporte.setType("pdf");
	 * 
	 * 
	 * }catch (Exception e) { e.getStackTrace(); } }
	 */

	@SuppressWarnings("unchecked")
	public List cargarDato(AprobacionDebitoInterno dato) {
		return null;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void menuImprimir() throws ExcFiltroExcepcion {
	

		vistaReporte = new CompVentanaDesdeHasta(this);
		vistaReporte.setTitle("Aprobaciones por Dia");

	//	vistaReporte.setOtro("estado:", estado);
		app.agregarHija(vistaReporte);
	}
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public void imprimirTodo() {
		try {
			NegocioAprobacionDebito negocio = NegocioAprobacionDebito.getInstance();
			Date inicio = vistaReporte.getFechaInicio().getValue();
			Date fin = vistaReporte.getFechaFinal().getValue();
			HashMap parametros = new HashMap();
			parametros
					.put("inicio", String.format("%1$td/%1$tm/%1$tY", inicio));
			parametros.put("fin", String.format("%1$td/%1$tm/%1$tY", fin));
			parametros.put("usuario", app.getUsuario().toString());
			parametros.put("logo", Index.class.getResource("/").getPath()
					+ "../../imagenes/cintillo_inst.png");
			// parametros.put("estado",String , tipo);
/*
				JRDataSource ds = new JRBeanCollectionDataSource(negocio.getTodosfecha(String.format("%1$td/%1$tm/%1$tY", inicio),								String.format("%1$td/%1$tm/%1$tY", fin)));
				*/
				JRDataSource ds = null;
			if (inicio==null||fin==null){
			 ds = new JRBeanCollectionDataSource(negocio.getAll());}
			else 
			 ds = new JRBeanCollectionDataSource(negocio.getAll(inicio,fin));
				app.agregarReporte();
				Jasperreport reporte = app.getReporte();
				reporte.setSrc("reportes/aprobacionesDia.jasper");
				reporte.setParameters(parametros);
				reporte.setDatasource(ds);
				reporte.setType("pdf");
			

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
			vistaReporte.close();
		} catch (Exception e) {
			e.getStackTrace();
		}
	}
}