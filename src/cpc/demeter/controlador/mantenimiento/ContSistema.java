package cpc.demeter.controlador.mantenimiento;

import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.mantenimiento.UISistema;

import cpc.modelo.demeter.mantenimiento.Sistema;
import cpc.negocio.demeter.mantenimiento.NegocioSistemaBienProduccion;

import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContSistema extends ContVentanaBase<Sistema> {

	private NegocioSistemaBienProduccion servicio;
	private UISistema vista;
	private AppDemeter app;

	public ContSistema(int modoOperacion, Sistema sistema,
			ContCatalogo<Sistema> llamador, AppDemeter app)
			throws ExcDatosInvalidos {
		super(modoOperacion);
		this.app = app;
		this.servicio = NegocioSistemaBienProduccion.getInstance();
		try {
			if (datoNulo(sistema) || modoAgregar()) {
				sistema = new Sistema();
			}
		} catch (ExcSeleccionNoValida e) {

			e.printStackTrace();
		}
		setear(sistema, new UISistema("Sistema  ("
				+ Accion.TEXTO[modoOperacion] + ")", 550, this.app), llamador,
				this.app);
		this.vista = (UISistema) getVista();
	}

	private static final long serialVersionUID = -6203766251486301230L;

	public void eliminar() {
		try {
			servicio.setSistemaBienProduccion(getDato());
			servicio.eliminar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {
		try {
			servicio.setSistemaBienProduccion(getDato());
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