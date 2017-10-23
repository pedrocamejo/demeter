package cpc.demeter.controlador.mantenimiento;

import java.util.Date;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.mantenimiento.ContRegistrosFallas;
import cpc.demeter.vista.mantenimiento.UIRegistroFalla;
import cpc.modelo.demeter.mantenimiento.RegistroFalla;
import cpc.negocio.demeter.mantenimiento.NegocioRegistroFalla;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContRegistroFalla extends ContVentanaBase<RegistroFalla> {

	private static final long serialVersionUID = 1180434079481225766L;
	private NegocioRegistroFalla servicio;
	private UIRegistroFalla vista;
	private AppDemeter app;

	public ContRegistroFalla(int modoOperacion, RegistroFalla momento,
			ContRegistrosFallas llamador, AppDemeter app)
			throws ExcDatosInvalidos {
		super(modoOperacion);
		this.app = app;
		this.servicio = NegocioRegistroFalla.getInstance();
		try {
			if (datoNulo(momento) || modoAgregar()) {
				momento = new RegistroFalla();
			}
		} catch (ExcSeleccionNoValida e) {

			e.printStackTrace();
		}
		setear(momento, new UIRegistroFalla("Registro de Falla  ("
				+ Accion.TEXTO[modoOperacion] + ")", 550, this.app), llamador,
				this.app);
		this.vista = (UIRegistroFalla) getVista();
	}

	public void eliminar() {

		try {
			servicio.setRegistroFalla(getDato());
			servicio.eliminar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() throws ExcFiltroExcepcion {
		try {
			getDato().setFechaRegistro(new Date());
			servicio.setRegistroFalla(getDato());
			servicio.guardar();
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("ERROR AL GUARDAR");
		}
	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (vista.getCatObjetoMantenimiento().getSeleccion() == null)
			throw new WrongValueException(vista.getCatObjetoMantenimiento(),
					"Seleccione un valor");

		if (vista.getCmbTipoFalla().getSeleccion() == null)
			throw new WrongValueException(vista.getCmbTipoFalla(),
					"Seleccione un valor");

		if (vista.getCmbMomentoFalla().getSeleccion() == null)
			throw new WrongValueException(vista.getCmbMomentoFalla(),
					"Seleccione un valor");

		if (vista.getFechaFalla().getValue() == null)
			throw new WrongValueException(vista.getFechaFalla(),
					"Indique un valor");

		if (vista.getCatTrabajadorReporta().getSeleccion() == null)
			throw new WrongValueException(vista.getCatTrabajadorReporta(),
					"Seleccione un valor");

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
