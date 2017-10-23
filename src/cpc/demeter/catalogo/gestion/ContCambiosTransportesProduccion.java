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
import cpc.modelo.demeter.gestion.DetalleOrdenTransporteProduccion;
import cpc.negocio.demeter.gestion.NegocioDetalleOrdenTransporteProduccion;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContCambiosTransportesProduccion extends
		ContCatalogo<DetalleOrdenTransporteProduccion> implements
		EventListener, Serializable,
		ICompCatalogo<DetalleOrdenTransporteProduccion> {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContCambiosTransportesProduccion(
			AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		this.app = app;
		NegocioDetalleOrdenTransporteProduccion servicios = NegocioDetalleOrdenTransporteProduccion
				.getInstance();
		dibujar(accionesValidas, "CAMBIOS ORDEN TRANSPORTE COSECHA ",
				servicios.getTodos(), app);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Control", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrOrden");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Origen", 350);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrOrigen");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Destino", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrDestino");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Falla", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFalla");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.PROCESAR || accion <= Accion.ANULAR) {
				NegocioDetalleOrdenTransporteProduccion servicio = NegocioDetalleOrdenTransporteProduccion
						.getInstance();
				DetalleOrdenTransporteProduccion solicitud = getDatoSeleccionado();
				servicio.setModelo(solicitud);
				new ContCambioOrdenTransporte(accion, servicio.getModelo(),
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
	public List cargarDato(DetalleOrdenTransporteProduccion dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
