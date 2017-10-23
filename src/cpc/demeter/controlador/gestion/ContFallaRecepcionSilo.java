package cpc.demeter.controlador.gestion;

import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.gestion.ContFallasRecepcionSilos;
import cpc.demeter.vista.gestion.UIFallaRecepcionSilo;
import cpc.modelo.demeter.gestion.FallaRecepcionSilo;
import cpc.negocio.demeter.gestion.NegocioFallaRecepcionSilo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContFallaRecepcionSilo extends ContVentanaBase<FallaRecepcionSilo> {

	private static final long serialVersionUID = 1180434079481225766L;
	private NegocioFallaRecepcionSilo servicio;
	private UIFallaRecepcionSilo vista;
	private AppDemeter app;

	public ContFallaRecepcionSilo(int modoOperacion,
			FallaRecepcionSilo momento, ContFallasRecepcionSilos llamador,
			AppDemeter app) throws ExcDatosInvalidos {
		super(modoOperacion);
		this.app = app;
		this.servicio = NegocioFallaRecepcionSilo.getInstance();
		try {
			if (datoNulo(momento) || modoAgregar()) {
				momento = new FallaRecepcionSilo();
			}
		} catch (ExcSeleccionNoValida e) {

			e.printStackTrace();
		}
		setear(momento, new UIFallaRecepcionSilo("Sistema  ("
				+ Accion.TEXTO[modoOperacion] + ")", 550, this.app), llamador,
				this.app);
		this.vista = (UIFallaRecepcionSilo) getVista();
	}

	public void eliminar() {
		try {
			servicio.setFallaRecepcionSilo(getDato());
			servicio.eliminar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {
		try {
			servicio.setFallaRecepcionSilo(getDato());
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
