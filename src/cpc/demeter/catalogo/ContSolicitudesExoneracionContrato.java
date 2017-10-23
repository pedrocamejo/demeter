package cpc.demeter.catalogo;

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
import cpc.demeter.controlador.administrativo.ContAprobacionReversoRecibo;
import cpc.demeter.controlador.administrativo.ContSolicitudExoneracionContrato;
import cpc.modelo.demeter.administrativo.AprobacionDescuento;
import cpc.modelo.demeter.administrativo.AprobacionReversoRecibo;
import cpc.modelo.demeter.administrativo.ContratoMecanizado;
import cpc.modelo.demeter.administrativo.SolicitudExoneracionContrato;
import cpc.negocio.demeter.administrativo.NegocioAprobacionDebito;
import cpc.negocio.demeter.administrativo.NegocioAprobacionDescuento;
import cpc.negocio.demeter.administrativo.NegocioContratoMecanizado;
import cpc.negocio.demeter.administrativo.NegocioSolicitudExoneracion;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.ventanas.CompVentanaDesdeHasta;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContSolicitudesExoneracionContrato extends
		ContCatalogo<SolicitudExoneracionContrato> implements EventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3903966709749323610L;
	private AppDemeter app;

	public ContSolicitudesExoneracionContrato(
			AccionFuncionalidad accionesValidas, AppDemeter app)
			throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioSolicitudExoneracion negocio = NegocioSolicitudExoneracion
				.getInstance();

		dibujar(accionesValidas, "Solicitudes De Exoneracion",
				negocio.getTodos(), app);
		this.app = app;

	};

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("NroControl", 50);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getNroControl");
		// titulo.setMetodoModelo("usuario.identidad.cedula");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha Solicitud", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getFechaSolicitud");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha Aprobacion", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getFechaAprobacion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Contrato", 120);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getStrContrato");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("estado", 120);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getStrEstadoExoneracion");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);
		
		return encabezado;
	}

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		try {

			Object aa = event.getTarget().getAttribute("pos");
			if (aa != null) {
				int accion = (Integer) event.getTarget().getAttribute("pos");
				if (accion <= Accion.PROCESAR) {
					SolicitudExoneracionContrato solicitud = getDatoSeleccionado();
					new ContSolicitudExoneracionContrato(accion,
							solicitud, this, app);
				} else {
					
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
	public List cargarDato(SolicitudExoneracionContrato dato) {
		return null;
	}

	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

	

}
