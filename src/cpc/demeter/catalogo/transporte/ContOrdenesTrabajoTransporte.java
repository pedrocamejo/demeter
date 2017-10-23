package cpc.demeter.catalogo.transporte;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.controlador.gestion.ContOrdenTrabajoMecanizado;
import cpc.demeter.controlador.transporte.ContCotizacionTransporte;
import cpc.demeter.controlador.transporte.ContOrdenTrabajoTransporte;
import cpc.modelo.demeter.administrativo.CotizacionTransporte;
import cpc.modelo.demeter.administrativo.EstadoContrato;
import cpc.modelo.demeter.gestion.OrdenTrabajoMecanizado;
import cpc.modelo.demeter.gestion.OrdenTrabajoTransporte;
import cpc.negocio.demeter.administrativo.NegocioCotizacion;
import cpc.negocio.demeter.administrativo.NegocioCotizacionTransporte;
import cpc.negocio.demeter.gestion.NegocioOrdenTrabajoMecanizado;
import cpc.negocio.demeter.gestion.NegocioOrdenTrabajoTransporte;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContOrdenesTrabajoTransporte extends ContCatalogo<OrdenTrabajoTransporte> implements
EventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8743098893653052530L;
	private AppDemeter app;
	private NegocioOrdenTrabajoTransporte negocio;

public ContOrdenesTrabajoTransporte (AccionFuncionalidad accionesValidas, AppDemeter app) throws ExcColumnasInvalidas, ExcArgumentoInvalido, ExcFiltroExcepcion
{
negocio = NegocioOrdenTrabajoTransporte.getInstance();
List<OrdenTrabajoTransporte> listaOrdenes = negocio.getTodos();
dibujar(accionesValidas, "ORDENES TRABAJO TRANSPORTE",
		listaOrdenes, app);
this.app = app;
}

	@Override
	public void onEvent(Event event) throws ExcFiltroExcepcion, ExcDatosInvalidos, ExcSeleccionNoValida {

		Integer accion = (Integer) event.getTarget().getAttribute("pos");
		if (accion != null) {
			OrdenTrabajoTransporte orden = getDatoSeleccionado();
			
			if (orden == null || accion == Accion.AGREGAR){
				orden = new OrdenTrabajoTransporte();
				new ContOrdenTrabajoTransporte(accion,orden, this, this.app);
			}else { 
					
			if (orden != null){
				if (accion <= Accion.PROCESAR || accion == Accion.ANULAR) {
					 NegocioOrdenTrabajoTransporte servicio = NegocioOrdenTrabajoTransporte.getInstance();
					 orden = servicio.inizializar(getDatoSeleccionado());
					 if (orden.getEstado().getDescripcion().equals("TERMINADO")&&accion==Accion.PROCESAR){
						 throw new ExcSeleccionNoValida("La Orden Ya fue procesada");
					 }
					 if (orden.getEstado().getDescripcion().equals("TERMINADO")&&accion==Accion.ANULAR){
						 throw new ExcSeleccionNoValida("La Orden Esta Procesada");
					 }
					 if (orden.getEstado().getDescripcion().equals("ANULADO")&&accion==Accion.ANULAR){
						 throw new ExcSeleccionNoValida("La Orden Ya Esta Anulada");
					 }
					 if (orden.getEstado().getDescripcion().equals("ANULADO")&&accion==Accion.PROCESAR){
						 throw new ExcSeleccionNoValida("no se puede procesar una orden anulada");
					 }
					new ContOrdenTrabajoTransporte(accion,orden, this, this.app);
					
					
				} else if (accion == Accion.IMPRIMIR_ITEM) {
					 NegocioOrdenTrabajoTransporte servicio = NegocioOrdenTrabajoTransporte.getInstance();
					 orden = servicio.inizializar(getDatoSeleccionado());
					imprimir(orden); // Llama a la venta de filtros del reporte
				} else if (accion == Accion.IMPRIMIR_TODO) {
					// No Implementado
				}
			} else {
				 // Imprime la Ruta del Operador, No la Orden de
							// Trabajo Mecanizado en sí.
			}}}
			
			
	
	}

	@Override
	public List<CompEncabezado> cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Control", 70);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroControl");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Unidad Ejecutora", 130);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrUnidadFuncional");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Productor", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrProductor");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 70);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo);

		return encabezado;
	}

	@Override
	public List cargarDato(OrdenTrabajoTransporte dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}
	public void imprimir(OrdenTrabajoTransporte orden) {
		 negocio = NegocioOrdenTrabajoTransporte.getInstance();

		// negocio.getPersistencia().getDatos(solicitudMecanizado).getDetalles();

		HashMap parametros = new HashMap();
		// parametros.put("filtro", String.format("%1$td/%1$tm/%1$tY",
		// inicio));
		parametros.put("fecha",String.format("%1$td/%1$tm/%1$tY", orden.getFecha()));
		parametros.put("usuario", app.getUsuario().toString());
		parametros.put("logo", Index.class.getResource("/").getPath()
				+ "../../imagenes/cintillo_inst.png");
		List<OrdenTrabajoTransporte> ordenes = new ArrayList<OrdenTrabajoTransporte>();
		ordenes.add(orden);
		JRDataSource ds = new JRBeanCollectionDataSource(
				ordenes);
		app.agregarReporte();
		Jasperreport reporte = app.getReporte();
		
		System.out.println(app.getUsuario().toString());
		reporte.setSrc("reportes/OrdenTrabajoTransporte2.jasper");
		reporte.setParameters(parametros);
		reporte.setDatasource(ds);
		reporte.setType("pdf");
	
	}
}
