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
import cpc.modelo.demeter.gestion.OrdenTransporteProduccion;
import cpc.negocio.demeter.gestion.NegocioOrdenTransporteProduccion;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContOrdenesTransportesProduccion extends
		ContCatalogo<OrdenTransporteProduccion> implements EventListener,
		Serializable, ICompCatalogo<OrdenTransporteProduccion> {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContOrdenesTransportesProduccion(
			AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		this.app = app;
		NegocioOrdenTransporteProduccion servicios = NegocioOrdenTransporteProduccion
				.getInstance();
		dibujar(accionesValidas, "ORDEN TRANSPORTE COSECHA ",
				servicios.getTodos(), app);
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
		titulo.setMetodoBinder("getStrUnidadFuncional");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Productor", 170);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrProductor");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Rubro", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrRubro");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Unidad Arrime", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrSilo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.PROCESAR || accion <= Accion.ANULAR) {
				NegocioOrdenTransporteProduccion servicio = NegocioOrdenTransporteProduccion
						.getInstance();
				OrdenTransporteProduccion solicitud = getDatoSeleccionado();
				servicio.setOrdenTrabajo(solicitud);
				new ContOrdenTransporteProduccion(accion, servicio.getModelo(),
						this, this.app);
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
	public List cargarDato(OrdenTransporteProduccion dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
