package cpc.demeter.catalogo.mantenimiento;

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
import cpc.demeter.controlador.mantenimiento.ContConsumible;

import cpc.modelo.demeter.mantenimiento.Consumible;

import cpc.negocio.demeter.mantenimiento.NegocioConsumible;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContConsumibles extends ContCatalogo<Consumible> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContConsumibles(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioConsumible servicios = NegocioConsumible.getInstance();
		this.app = app;
		dibujar(accionesValidas, "CONSUMIBLES", servicios.getTodos(), app);

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Codigo", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre", 550);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDenominacionFabricante");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				NegocioConsumible servicio = NegocioConsumible.getInstance();
				Consumible consumible = getDatoSeleccionado();
				servicio.setConsumible(consumible);
				if (consumible == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Banco");
				else
					new ContConsumible(accion, servicio.getConsumible(), this,
							app);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un Banco");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(Consumible dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
