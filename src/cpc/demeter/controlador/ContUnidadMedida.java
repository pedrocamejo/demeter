package cpc.demeter.controlador;

import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContUnidadesMedida;
import cpc.demeter.vista.UiUnidadMedida;
import cpc.modelo.demeter.basico.TipoUnidadMedida;
import cpc.modelo.demeter.basico.UnidadMedida;
import cpc.negocio.demeter.basico.NegocioUnidadMedida;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContUnidadMedida extends ContVentanaBase<UnidadMedida> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioUnidadMedida servicio;
	private AppDemeter app;
	private UiUnidadMedida vista;

	public ContUnidadMedida(int modoOperacion, UnidadMedida tipo,
			ContUnidadesMedida llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			if (tipo == null || modoAgregar())
				tipo = new UnidadMedida();
			servicio = NegocioUnidadMedida.getInstance();
			List<TipoUnidadMedida> tipos;
			tipos = servicio.getTipos();
			setear(tipo, new UiUnidadMedida("UNIDAD DE MEDIDA ("
					+ Accion.TEXTO[modoOperacion] + ")", 550, tipos), llamador,
					app);
			vista = ((UiUnidadMedida) getVista());
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
			servicio = NegocioUnidadMedida.getInstance();
			servicio.setModelo(getDato());
			servicio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {
		try {
			servicio = NegocioUnidadMedida.getInstance();
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
