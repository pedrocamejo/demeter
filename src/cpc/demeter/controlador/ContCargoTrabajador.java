package cpc.demeter.controlador;

import java.util.List;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContCargosTrabajadores;
import cpc.demeter.vista.UiCargoTrabajador;
import cpc.modelo.demeter.basico.CargoTrabajador;
import cpc.negocio.demeter.NegocioCargoTrabajador;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContCargoTrabajador extends ContVentanaBase<CargoTrabajador> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioCargoTrabajador servicio;
	private AppDemeter app;
	private UiCargoTrabajador vista;

	public ContCargoTrabajador(int modoOperacion, CargoTrabajador clase,
			ContCargosTrabajadores llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			if (clase == null || modoAgregar())
				clase = new CargoTrabajador();
			servicio = NegocioCargoTrabajador.getInstance();
			List<CargoTrabajador> clases;
			clases = servicio.getTodos();
			setear(clase, new UiCargoTrabajador("CARGO DEL TRABAJADOR ("
					+ Accion.TEXTO[modoOperacion] + ")", 550, clases),
					llamador, app);
			vista = ((UiCargoTrabajador) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
		}
	}

	public void eliminar() {

		try {
			servicio = NegocioCargoTrabajador.getInstance();
			servicio.setModelo(getDato());
			servicio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {

		try {
			servicio = NegocioCargoTrabajador.getInstance();
			servicio.setModelo(getDato());
			servicio.guardar(getDato().getId());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR
				&& (vista.getDescripcion().getValue() == null || vista
						.getDescripcion().getValue() == "")) {
			throw new WrongValueException(vista.getDescripcion(),
					"Indique una Descripcion");
		}

	}

	public void onEvent(Event arg0) throws Exception {
		validar();
		procesarCRUD(arg0);

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
