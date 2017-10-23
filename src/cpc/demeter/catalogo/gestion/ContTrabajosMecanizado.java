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
import cpc.demeter.controlador.gestion.*;
import cpc.modelo.demeter.gestion.TrabajoRealizadoMecanizado;
import cpc.negocio.demeter.gestion.NegocioTrabajoMecanizado;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContTrabajosMecanizado extends
		ContCatalogo<TrabajoRealizadoMecanizado> implements EventListener,
		Serializable, ICompCatalogo<TrabajoRealizadoMecanizado> {

	private static final long serialVersionUID = 7520298721611436558L;
	private AppDemeter app;

	public ContTrabajosMecanizado(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		this.app = app;
		NegocioTrabajoMecanizado servicios = NegocioTrabajoMecanizado
				.getInstance();
		dibujar(accionesValidas, "TRABAJOS REALIZADOS MECANIZADO AGRICOLA ",
				servicios.getTodosProject(), app);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Orden Trabajo", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroControlOrden");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Productor", 170);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getProductor");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Labor", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrLabor");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Laborado", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCantidadLaborada");
		encabezado.add(titulo);
		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.CONSULTAR) {
				NegocioTrabajoMecanizado servicio = NegocioTrabajoMecanizado
						.getInstance();
				TrabajoRealizadoMecanizado solicitud = getDatoSeleccionado();
				solicitud =servicio.getTrabajoRealizadoMecanizadoProject(solicitud);
				servicio.setTrabajoMecanizado(solicitud);
				new ContTrabajoMecanizado(accion, servicio.getModelo(), this,
						this.app);
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
	public List cargarDato(TrabajoRealizadoMecanizado dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
