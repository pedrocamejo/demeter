package cpc.demeter.catalogo;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.administrativo.ContAprobacionExoneracionContrato;
import cpc.modelo.demeter.administrativo.AprobacionExoneracionContrato;
import cpc.negocio.demeter.administrativo.NegocioAprobacionExoneracionContrato;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContAprobacionesExoneracionContrato extends
		ContCatalogo<AprobacionExoneracionContrato> implements EventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -30127671000988233L;
	/**
	 * 
	 */

	private AppDemeter app;

	public ContAprobacionesExoneracionContrato(
			AccionFuncionalidad accionesValidas, AppDemeter app)
			throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioAprobacionExoneracionContrato negocio = NegocioAprobacionExoneracionContrato
				.getInstance();

		dibujar(accionesValidas, "Aprobaciones De Exoneracion",
				negocio.getTodos(), app);
		this.app = app;

	};

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("NroControlSol", 50);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getNumeroExoneracion");
		// titulo.setMetodoModelo("usuario.identidad.cedula");
		encabezado.add(titulo);
		titulo = new CompEncabezado("sede", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getSede");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha Solicitud", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getFechaSolicitud");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nº contrato", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNumeroContrato");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Pagador", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getPagador");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Ced/Rif", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCedRif");
		encabezado.add(titulo);

		titulo = new CompEncabezado("MontoBase", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrMontoBase");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
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
					AprobacionExoneracionContrato aprobacion = getDatoSeleccionado();
					new ContAprobacionExoneracionContrato(accion, aprobacion,
							this, app);
				}	if (accion == Accion.IMPRIMIR_ITEM) {
					AprobacionExoneracionContrato aprobacion = getDatoSeleccionado();
					ContAprobacionExoneracionContrato.imprimir(aprobacion,app);
				}
				else {

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
	public List cargarDato(AprobacionExoneracionContrato dato) {
		return null;
	}

	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
