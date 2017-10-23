package cpc.demeter.catalogo.gestion;

import java.beans.PropertyVetoException;
import java.io.Serializable;
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

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.gestion.ContOrdenTrabajoMecanizado;
import cpc.modelo.demeter.gestion.OperadorOrdenMecanizado;
import cpc.modelo.demeter.gestion.OrdenTrabajoMecanizado;
import cpc.modelo.ministerio.dimension.UbicacionSector;
import cpc.negocio.demeter.gestion.NegocioOrdenTrabajoMecanizado;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.ventanas.CompVentanaDesdeHasta;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContOrdenesTrabajoMecanizado extends
		ContCatalogo<OrdenTrabajoMecanizado> implements EventListener,
		Serializable, ICompCatalogo<OrdenTrabajoMecanizado> {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;
	private CompVentanaDesdeHasta<UbicacionSector> vistaReporte;

	public ContOrdenesTrabajoMecanizado(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		this.app = app;
		NegocioOrdenTrabajoMecanizado servicios = NegocioOrdenTrabajoMecanizado
				.getInstance();
		dibujar(accionesValidas, "ORDEN TRABAJO MECANIZADO AGRICOLA ",
				servicios.getTodosProject(), app);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
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

		titulo = new CompEncabezado("Sector", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrSector");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Labor", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrLabor");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Rubro", 70);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrRubro");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 70);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {

			Integer accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion != null) {
				if (accion <= Accion.PROCESAR || accion == Accion.ANULAR) {
					NegocioOrdenTrabajoMecanizado servicio = NegocioOrdenTrabajoMecanizado
							.getInstance();
					OrdenTrabajoMecanizado solicitud = getDatoSeleccionado();
					solicitud = servicio.getOrdenMecanizadoProject(solicitud);
					solicitud=servicio.setOrdenTrabajo(solicitud);
					new ContOrdenTrabajoMecanizado(accion,
							solicitud, this, this.app);
				} else if (accion == Accion.IMPRIMIR_ITEM) {
					menuImprimir(); // Llama a la venta de filtros del reporte
				} else if (accion == Accion.IMPRIMIR_TODO) {
					// No Implementado
				}
			} else {
				imprimir(); // Imprime la Ruta del Operador, No la Orden de
							// Trabajo Mecanizado en sí.
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		} catch (ExcSeleccionNoValida e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
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
		/*if (vistaReporte.getOtro().getSeleccion() == null) {
			app.mostrarError("Debe seleccionar un Sector");
			vistaReporte.getOtro().setFocus(true);
			return false;
		}*/
		return true;
	}

	public void menuImprimir() throws ExcFiltroExcepcion {
		vistaReporte = new CompVentanaDesdeHasta<UbicacionSector>(this);
		vistaReporte.setVisibleOtro(true);
		vistaReporte.setTexto("Sector");
		NegocioOrdenTrabajoMecanizado negocio = NegocioOrdenTrabajoMecanizado
				.getInstance();
		vistaReporte.getOtro().setModelo(negocio.getSectores(), "getParroquia");
		app.agregarHija(vistaReporte);
	}

	@SuppressWarnings("unchecked")
	public void imprimir() {
		try {
			if (criteriosValidos()) {
				NegocioOrdenTrabajoMecanizado negocio = NegocioOrdenTrabajoMecanizado
						.getInstance();
				Date inicio = vistaReporte.getFechaInicio().getValue();
				Date fin = vistaReporte.getFechaFinal().getValue();
				UbicacionSector sector = vistaReporte.getOtro().getSeleccion();
				List<OperadorOrdenMecanizado> planificacion = negocio.getListadoOperador(inicio, fin, sector);
				
				if (planificacion.size() > 0) {
					HashMap parametros = new HashMap();
					JRDataSource ds = new JRBeanCollectionDataSource(
							planificacion);
					app.agregarReporte();
					Jasperreport reporte = app.getReporte();
					reporte.setSrc("reportes/rutaOperador.jasper");
					reporte.setParameters(parametros);
					reporte.setDatasource(ds);
					reporte.setType("pdf");
				} else {
					app.mostrarError("No existen datos para los criterios de busqueda especificados");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(OrdenTrabajoMecanizado dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
