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
import cpc.demeter.controlador.gestion.ContMotivoAnulacionSolicitud;
import cpc.demeter.controlador.gestion.ContMotivoTransferenciaActivo;
import cpc.modelo.demeter.gestion.MotivoAnulacionSolicitud;
import cpc.modelo.demeter.gestion.MotivoTransferenciaActivo;
import cpc.negocio.demeter.gestion.NegocioMotivoAnulacionSolicitud;
import cpc.negocio.demeter.gestion.NegocioMotivoTransferenciaActivo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContMotivosAnulacionesSolitudes extends
		ContCatalogo<MotivoAnulacionSolicitud> implements EventListener,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -296749573227008494L;
	private AppDemeter app;

	public ContMotivosAnulacionesSolitudes(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioMotivoAnulacionSolicitud negocio = NegocioMotivoAnulacionSolicitud
				.getInstance();
		this.app = app;
		dibujar(accionesValidas, "MOTIVOS DE ANULACION DE SOLICITUDES",
				negocio.obetenerTodos(), app);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(MotivoAnulacionSolicitud arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

	@Override
	public List<CompEncabezado> cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Descripción", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMotivo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipoSolicitud");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 25);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				MotivoAnulacionSolicitud entrada = getDatoSeleccionado();
				if (entrada == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Motivo del Catalogo");
				else
					new ContMotivoAnulacionSolicitud(accion, entrada, this, app);
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un Motivo del Catalogo");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

}
