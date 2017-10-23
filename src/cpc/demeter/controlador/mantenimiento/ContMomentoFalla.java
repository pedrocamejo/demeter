package cpc.demeter.controlador.mantenimiento;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.mantenimiento.UIMomentoFalla;
import cpc.modelo.demeter.mantenimiento.MomentoFalla;
import cpc.negocio.demeter.mantenimiento.NegocioMomentoFalla;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;
import cva.pc.demeter.utilidades.ValidaEntrada;

public class ContMomentoFalla extends ContVentanaBase<MomentoFalla> {

	private static final long serialVersionUID = 1180434079481225766L;
	private NegocioMomentoFalla negocio;
	private UIMomentoFalla vista;
	private AppDemeter app;

	public ContMomentoFalla(int modoOperacion, MomentoFalla momento,
			ContCatalogo<MomentoFalla> llamador, AppDemeter app)
			throws ExcDatosInvalidos {
		super(modoOperacion);
		this.app = app;
		this.negocio = NegocioMomentoFalla.getInstance();
		try {
			if (datoNulo(momento) || modoAgregar()) {
				momento = new MomentoFalla();
			}
		} catch (ExcSeleccionNoValida e) {

			e.printStackTrace();
		}
		setear(momento, new UIMomentoFalla("Momento de Falla  ("
				+ Accion.TEXTO[modoOperacion] + ")", 550, this.app), llamador,
				this.app);
		this.vista = (UIMomentoFalla) getVista();
		if (modoOperacion == Accion.CONSULTAR) {
			vista.setLectura();
		}
	}

	@Override
	public void eliminar() {

		try {
			negocio.setMomentoFalla(getDato());
			negocio.eliminar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		try {
			negocio.setMomentoFalla(getDato());
			negocio.guardar();
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("ERROR AL GUARDAR");
		}
	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (vista.getDescripcion().getValue() == null
				|| vista.getDescripcion().getValue() == "")
			throw new WrongValueException(vista.getDescripcion(),
					"Indique un valor");
		if (!ValidaEntrada.esTamanoValido(vista.getDescripcion().getValue(), 5))
			throw new WrongValueException(vista.getDescripcion(),
					"Valor muy corto para una descripcion");
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