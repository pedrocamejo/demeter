package cpc.demeter.catalogo.mantenimiento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.mantenimiento.ContEnteExterno;
import cpc.modelo.demeter.mantenimiento.EnteExterno;
import cpc.negocio.demeter.mantenimiento.NegocioEnteExterno;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContEntesExternos extends ContCatalogo<EnteExterno> implements
		EventListener, Serializable {

	private static final long serialVersionUID = 1L;

	private AppDemeter app;

	public ContEntesExternos(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {

		NegocioEnteExterno servicios = NegocioEnteExterno.getInstance();
		this.app = app;
		dibujar(accionesValidas, "ENTES EXTERNOS", servicios.getTodos(), app);
	}

	@Override
	public List<CompEncabezado> cargarEncabezado() {
		CompEncabezado titulo;

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("ID", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("descripcion", 550);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Direccion", 550);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDireccion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Telefono", 550);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getTelefono1");
		encabezado.add(titulo);

		return encabezado;
	}

	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub
		try {
			int accion = (Integer) arg0.getTarget().getAttribute("pos");

			if (accion == Accion.AGREGAR) {
				new ContEnteExterno(accion, null, this, app);
			}
			if (accion == Accion.CONSULTAR) {
				EnteExterno ente = getDatoSeleccionado();
				new ContEnteExterno(accion, ente, this, app);
			}
			if (accion == Accion.EDITAR) {
				EnteExterno ente = getDatoSeleccionado();
				new ContEnteExterno(accion, ente, this, app);
			}
			if (accion == Accion.ELIMINAR) {
				EnteExterno ente = getDatoSeleccionado();
				new ContEnteExterno(accion, ente, this, app);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

	@Override
	public List cargarDato(EnteExterno dato) {
		// TODO Auto-generated method stub
		return null;
	}

}
