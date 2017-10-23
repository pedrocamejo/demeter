package cpc.demeter.catalogo;

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
import cpc.demeter.controlador.administrativo.ContControlSede;
import cpc.modelo.demeter.administrativo.ControlSede;
import cpc.negocio.demeter.NegocioControlSede;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContControlSedes extends ContCatalogo<ControlSede> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContControlSedes(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioControlSede servicios = NegocioControlSede.getInstance();
		this.app = app;
		dibujar(accionesValidas, "CONTROL DE SEDE", servicios.getTodos(), app);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Sede", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrSede");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Serie", 40);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getSerie");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cuenta Contable", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCuentaContableIngresosSede");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cuenta Presupuestaria", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCuentaPresupuestariaIngresosSede");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Recibo", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getControlRecibo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Factura", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getControlFactura");
		encabezado.add(titulo);
		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.CONSULTAR) {
				NegocioControlSede servicio = NegocioControlSede.getInstance();
				ControlSede controlSede = getDatoSeleccionado();
				servicio.setControlSede(controlSede);
				new ContControlSede(accion, servicio.getControlSede(), this,
						app);
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
	public List cargarDato(ControlSede dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
