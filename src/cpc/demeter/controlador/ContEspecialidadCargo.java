package cpc.demeter.controlador;

import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.UIEspecialidaCargo;
import cpc.modelo.demeter.basico.EspecilidadCargo;
import cpc.negocio.demeter.NegocioEspecialidadCargo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContEspecialidadCargo extends ContVentanaBase<EspecilidadCargo> {

	private static final long serialVersionUID = 2651165034127880437L;
	private NegocioEspecialidadCargo servicio;
	private UIEspecialidaCargo vista;
	private AppDemeter app;

	public ContEspecialidadCargo(int modoOperacion,
			EspecilidadCargo especialidad,
			ContCatalogo<EspecilidadCargo> llamador, AppDemeter app)
			throws ExcDatosInvalidos {
		super(modoOperacion);
		this.app = app;
		this.servicio = NegocioEspecialidadCargo.getInstance();
		try {
			if (datoNulo(especialidad) || modoAgregar()) {
				especialidad = new EspecilidadCargo();
			}
		} catch (ExcSeleccionNoValida e) {

			e.printStackTrace();
		}
		setear(especialidad, new UIEspecialidaCargo("Especialidad del Cargo  ("
				+ Accion.TEXTO[modoOperacion] + ")", 550, this.app), llamador,
				this.app);
		this.vista = (UIEspecialidaCargo) getVista();
	}

	@Override
	public void eliminar() {

		try {
			servicio.setEspecialidadCargo(getDato());
			servicio.eliminar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@Override
	public void guardar() {
		try {
			servicio.setEspecialidadCargo(getDato());
			servicio.guardar();
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

	}

	public void onEvent(Event event) throws Exception {
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