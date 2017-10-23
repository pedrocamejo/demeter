package cpc.demeter.controlador.mantenimiento;

import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.mantenimiento.ContFabricantes;
import cpc.demeter.vista.mantenimiento.UIFabricante;
import cpc.modelo.demeter.mantenimiento.Fabricante;
import cpc.negocio.demeter.mantenimiento.NegocioFabricante;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContFabricante extends ContVentanaBase<Fabricante> {

	private static final long serialVersionUID = 1180434079481225766L;
	private NegocioFabricante servicio;
	private UIFabricante vista;
	private AppDemeter app;

	public ContFabricante(int modoOperacion, Fabricante momento,
			ContFabricantes llamador, AppDemeter app) throws ExcDatosInvalidos {
		super(modoOperacion);
		this.app = app;
		this.servicio = NegocioFabricante.getInstance();
		try {
			if (datoNulo(momento) || modoAgregar()) {
				momento = new Fabricante();
			}
		} catch (ExcSeleccionNoValida e) {

			e.printStackTrace();
		}
		setear(momento, new UIFabricante("Fabricante  ("
				+ Accion.TEXTO[modoOperacion] + ")", 550, this.app), llamador,
				this.app);
		this.vista = (UIFabricante) getVista();
	}

	public void eliminar() {
		try {
			servicio.setFabricante(getDato());
			servicio.eliminar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void guardar() {
		try {
			servicio.setFabricante(getDato());
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
