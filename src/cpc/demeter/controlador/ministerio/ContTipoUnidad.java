package cpc.demeter.controlador.ministerio;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.ministerio.UiTipoUnidad;
import cpc.modelo.ministerio.dimension.TipoUnidad;
import cpc.negocio.ministerio.basico.NegocioTipoUnidad;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContTipoUnidad extends ContVentanaBase<TipoUnidad> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioTipoUnidad servicio;
	private AppDemeter app;
	private UiTipoUnidad vista;

	public ContTipoUnidad(int modoOperacion, TipoUnidad unidad,
			ICompCatalogo<TipoUnidad> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			if (unidad == null || modoAgregar())
				unidad = new TipoUnidad();
			setear(unidad, new UiTipoUnidad("TIPO DE UNIDAD (UNIDAD) ("
					+ Accion.TEXTO[modoOperacion] + ")", 550), llamador, app);
			vista = ((UiTipoUnidad) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		}
	}

	public void eliminar() {

		try {
			servicio = NegocioTipoUnidad.getInstance();
			servicio.setModelo(getDato());
			servicio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void guardar() {

		try {
			servicio = NegocioTipoUnidad.getInstance();
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
