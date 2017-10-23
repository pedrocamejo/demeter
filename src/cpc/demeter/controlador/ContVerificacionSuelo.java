package cpc.demeter.controlador;

import java.util.List;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContVerificacionesSuelos;
import cpc.demeter.vista.UiVerificacionSuelo;
import cpc.modelo.demeter.basico.TipoVerificacionSuelo;
import cpc.negocio.demeter.basico.NegocioVerificacionSuelo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContVerificacionSuelo extends
		ContVentanaBase<TipoVerificacionSuelo> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioVerificacionSuelo servicio;
	private AppDemeter app;
	private UiVerificacionSuelo vista;

	public ContVerificacionSuelo(int modoOperacion,
			TipoVerificacionSuelo clase, ContVerificacionesSuelos llamador,
			AppDemeter app) throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			if (clase == null || modoAgregar())
				clase = new TipoVerificacionSuelo();
			servicio = NegocioVerificacionSuelo.getInstance();
			List<TipoVerificacionSuelo> clases;
			clases = servicio.getTodos();
			setear(clase, new UiVerificacionSuelo("TIPOS DE VERIFICACION ("
					+ Accion.TEXTO[modoOperacion] + ")", 550, clases),
					llamador, app);
			vista = ((UiVerificacionSuelo) getVista());
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
			servicio = NegocioVerificacionSuelo.getInstance();
			servicio.setModelo(getDato());
			servicio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {

		try {
			servicio = NegocioVerificacionSuelo.getInstance();
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
