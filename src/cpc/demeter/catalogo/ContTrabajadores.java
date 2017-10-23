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
import cpc.demeter.controlador.ContTrabajador;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.negocio.demeter.basico.NegocioTrabajador;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContTrabajadores extends ContCatalogo<Trabajador> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContTrabajadores(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioTrabajador negocio = NegocioTrabajador.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Trabajadores", negocio.getTodos(), app);

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Cedula", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroCedula");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombres", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Apellidos", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getApellido");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo de Trabajador", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipoTrabajador");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cargo", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrCargo");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				Trabajador servicio = getDatoSeleccionado();

				if (servicio == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Trabajador");
				else {
					NegocioTrabajador negocio = NegocioTrabajador.getInstance();
					negocio.setTrabajo(servicio);
					new ContTrabajador(accion, negocio.getModelo(), this, app);
				}
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un trabajador");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(Trabajador arg0) {
		return null;
	}

}
