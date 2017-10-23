package cpc.demeter.controlador.ministerio;

import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.ministerio.UiUnidadFuncional;
import cpc.modelo.ministerio.dimension.TipoUnidadFuncional;
import cpc.modelo.ministerio.dimension.UbicacionDireccion;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.negocio.ministerio.basico.NegocioUnidadFuncional;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContUnidadFuncional extends ContVentanaBase<UnidadFuncional> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioUnidadFuncional servicio;
	private AppDemeter app;
	private UiUnidadFuncional vista;

	public ContUnidadFuncional(int modoOperacion, UnidadFuncional unidad,
			ICompCatalogo<UnidadFuncional> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			if (unidad == null || modoAgregar())
				unidad = new UnidadFuncional();
			servicio = NegocioUnidadFuncional.getInstance();
			List<TipoUnidadFuncional> tipos = servicio
					.getTiposUnidadesFuncionales();
			List<UbicacionDireccion> direcciones = servicio
					.getUbicacionesFisicas();
			setear(unidad, new UiUnidadFuncional(
					"UNIDAD EJECUTORA (FUNCIONAL) ("
							+ Accion.TEXTO[modoOperacion] + ")", 550,
					direcciones, tipos), llamador, app);
			vista = ((UiUnidadFuncional) getVista());
			if (!modoAgregar())
				vista.getSerie().setValue(servicio.getSerie());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		} catch (ExcFiltroExcepcion e) {
			this.app.mostrarError(e.getMessage());
			e.printStackTrace();
		}
	}

	public void eliminar() {

		try {
			servicio = NegocioUnidadFuncional.getInstance();
			servicio.setModelo(getDato());
			servicio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {

		try {
			servicio = NegocioUnidadFuncional.getInstance();
			servicio.setModelo(getDato());
			servicio.guardar(vista.getSerie().getValue());
		} catch (WrongValueException e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
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
		if (arg0.getName() == CompBuscar.ON_SELECCIONO)
			vista.actualizarDireccion();
		else {
			validar();
			procesarCRUD(arg0);
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
