package cpc.demeter.catalogo.gestion;

import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;

import cpc.demeter.controlador.administrativo.ContFactura;
import cpc.demeter.controlador.gestion.ContSolicitudMecanizado;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.gestion.EstadoSolicitud;
import cpc.modelo.demeter.gestion.SolicitudMecanizado;
import cpc.negocio.demeter.gestion.NegocioSolicitudMecanizado;
import cpc.persistencia.demeter.implementacion.gestion.PerEstadoSolicitud;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContSolicitudesMecanizado extends
		ContCatalogo<SolicitudMecanizado> implements EventListener,
		Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContSolicitudesMecanizado(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		this.app = app;
		NegocioSolicitudMecanizado servicios = NegocioSolicitudMecanizado
				.getInstance();
		dibujar(accionesValidas, "SOLICITUDES MECANIZADO AGRICOLA ",
				servicios.getSolicitudesProject(), app);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Control", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroControl");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Unidad Ejecutora", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrUnidadEjecutora");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Productor", 170);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrProductor");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Servicio", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrServicio");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstadoSolicitud");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			EstadoSolicitud aprobada = new PerEstadoSolicitud().getaprobada();
			EstadoSolicitud recibida = new PerEstadoSolicitud().getrecibida();
			if (accion == Accion.EDITAR) {
				NegocioSolicitudMecanizado servicio = NegocioSolicitudMecanizado
						.getInstance();
				SolicitudMecanizado solicitud = getDatoSeleccionado();
				solicitud = servicio.getSolicitudEnriquesida(solicitud);
				servicio.setSolicitud(solicitud);
				if (!servicio.getModelo().getEstadosolicitud().getId()
						.equals(recibida.getId()))
					throw new Exception(
							"Solo Puede Editar Solicitudes En Estado de Recibida");
				new ContSolicitudMecanizado(accion, servicio.getModelo(), this,
						app);
			}
			if (accion == Accion.PROCESAR) {
				NegocioSolicitudMecanizado servicio = NegocioSolicitudMecanizado
						.getInstance();
				SolicitudMecanizado solicitud = getDatoSeleccionado();
				solicitud = servicio.getSolicitudEnriquesida(solicitud);
				servicio.setSolicitud(solicitud);
				/*
				 * System.out.println("estado de la sol"+
				 * servicio.getModelo().getEstadosolicitud().getEstado());
				 * System.out.println("estado de la recibida"+ new
				 * PerEstadoSolicitud().getrecibida().getEstado());
				 * System.out.println("estado de la aprovada"+ new
				 * PerEstadoSolicitud().getaprobada().getEstado());
				 */
				boolean a = servicio.getModelo().getEstadosolicitud().getId()
						.equals(recibida.getId());
				boolean b = servicio.getModelo().getEstadosolicitud().getId()
						.equals(aprobada.getId());
				boolean c = (!a && b) || (a && !b);
				// (servicio.getModelo().getEstadosolicitud() != new
				// PerEstadoSolicitud().getaprobada()) &&
				if ((!c)) {
					throw new Exception(
							"Solo Puede Procesar Solictudes En Estado de Recibida o Aprobada");
				}

				/*
				 * if
				 * ((servicio.getModelo().getEstadosolicitud().getEstado().equals
				 * (aprobada))) { throw new Exception(
				 * "Solo Puede Procesar Solictudes En Estado de Recibida o Aprobada"
				 * ); }
				 */
				new ContSolicitudMecanizado(accion, servicio.getModelo(), this,
						app);

			}

			/*
			 * if (accion <= Accion.PROCESAR|| accion == Accion.ANULAR){
			 * NegocioSolicitudMecanizado servicio =
			 * NegocioSolicitudMecanizado.getInstance(); SolicitudMecanizado
			 * solicitud = getDatoSeleccionado();
			 * servicio.setSolicitud(solicitud);
			 * 
			 * new ContSolicitudMecanizado(accion, servicio.getModelo(), this,
			 * app);
			 */
			if (accion == Accion.CONSULTAR) {
				NegocioSolicitudMecanizado servicio = NegocioSolicitudMecanizado
						.getInstance();
				SolicitudMecanizado solicitud = getDatoSeleccionado();
				solicitud = servicio.getSolicitudEnriquesida(solicitud);
				servicio.setSolicitud(solicitud);
				new ContSolicitudMecanizado(accion, servicio.getModelo(), this,
						this.app);
			}

			if (accion == Accion.AGREGAR) {
				NegocioSolicitudMecanizado servicio = NegocioSolicitudMecanizado
						.getInstance();
				SolicitudMecanizado solicitud = getDatoSeleccionado();
				solicitud = servicio.getSolicitudEnriquesida(solicitud);
				servicio.setSolicitud(solicitud);
				new ContSolicitudMecanizado(accion, servicio.getModelo(), this,
						this.app);
			}

			else if (accion == Accion.IMPRIMIR_ITEM) {
				NegocioSolicitudMecanizado servicio = NegocioSolicitudMecanizado
						.getInstance();
				SolicitudMecanizado solicitud = getDatoSeleccionado();
				solicitud = servicio.getSolicitudEnriquesida(solicitud);
				ContSolicitudMecanizado.imprimir(solicitud, app);
				// ContFactura.imprimir(factura,app);
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
	public List cargarDato(SolicitudMecanizado dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
