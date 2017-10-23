package cpc.demeter.controlador;

import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContClasesUnidadArrime;

import cpc.demeter.vista.UiClaseUnidadArrime;

import cpc.modelo.demeter.basico.ClaseUnidadArrime;
import cpc.modelo.demeter.basico.TipoUnidadArrime;

import cpc.negocio.demeter.basico.NegocioClaseUnidadArrime;

import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContClaseUnidadArrime extends ContVentanaBase<ClaseUnidadArrime> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioClaseUnidadArrime servicio;
	private AppDemeter app;
	private UiClaseUnidadArrime vista;

	public ContClaseUnidadArrime(int modoOperacion, ClaseUnidadArrime clase,
			ContClasesUnidadArrime llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			if (clase == null || modoAgregar())
				clase = new ClaseUnidadArrime();
			servicio = NegocioClaseUnidadArrime.getInstance();
			List<TipoUnidadArrime> clases;
			clases = servicio.obtenerTiposUnidadArrime();
			setear(clase, new UiClaseUnidadArrime("CLASE UNIDAD DE ARRIME ("
					+ Accion.TEXTO[modoOperacion] + ")", 550, clases),
					llamador, app);
			vista = ((UiClaseUnidadArrime) getVista());
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
			servicio = NegocioClaseUnidadArrime.getInstance();
			servicio.setModelo(getDato());
			servicio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {

		try {
			servicio = NegocioClaseUnidadArrime.getInstance();
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
		if (getModoOperacion() != Accion.ELIMINAR
				&& (vista.getCmbTipo().getSelectedIndex() < 0)) {
			throw new WrongValueException(vista.getCmbTipo(),
					"Indique un Tipo de Arrime");
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
