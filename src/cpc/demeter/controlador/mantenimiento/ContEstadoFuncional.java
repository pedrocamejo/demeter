package cpc.demeter.controlador.mantenimiento;

import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.mantenimiento.ContEstadosFuncional;
import cpc.demeter.vista.mantenimiento.UIEstadoFuncional;
import cpc.modelo.demeter.mantenimiento.EstadoFuncional;
import cpc.negocio.demeter.mantenimiento.NegocioEstadoFuncional;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContEstadoFuncional extends ContVentanaBase<EstadoFuncional> {

	private static final long serialVersionUID = 1180434079481225766L;
	private NegocioEstadoFuncional servicio;
	private UIEstadoFuncional vista;
	private AppDemeter app;

	public ContEstadoFuncional(int modoOperacion, EstadoFuncional momento,
			ContEstadosFuncional llamador, AppDemeter app)
			throws ExcDatosInvalidos {
		super(modoOperacion);
		this.app = app;
		this.servicio = NegocioEstadoFuncional.getInstance();
		try {
			if (datoNulo(momento) || modoAgregar()) {
				momento = new EstadoFuncional();
			}
		} catch (ExcSeleccionNoValida e) {

			e.printStackTrace();
		}
		setear(momento, new UIEstadoFuncional("Estado Funcional  ("
				+ Accion.TEXTO[modoOperacion] + ")", 550, this.app), llamador,
				this.app);
		this.vista = (UIEstadoFuncional) getVista();
	}

	public void eliminar() {
		try {
			servicio.setEstadoFuncional(getDato());
			servicio.eliminar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void guardar() {
		try {
			servicio.setEstadoFuncional(getDato());
			servicio.guardar();
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

	}

	public void onEvent(Event event) throws Exception {
		System.out.println("hay evento " + event.getName() + " en "
				+ event.getTarget().getClass().getName());
		if (event.getTarget() == getVista().getAceptar()) {
			procesarCRUD(event);
		} else if (event.getName() == CompBuscar.ON_SELECCIONO) {
			vista.actualizarModelo();
		} else if (event.getName() == Events.ON_SELECT) {
			vista.actualizarModelo();
		}
	}

	@Override
	public void anular() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub

	}
}
