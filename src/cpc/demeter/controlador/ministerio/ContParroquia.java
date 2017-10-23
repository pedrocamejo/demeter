package cpc.demeter.controlador.ministerio;

import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ministerio.ContParroquias;
import cpc.demeter.vista.ministerio.UiParroquia;
import cpc.modelo.ministerio.dimension.UbicacionMunicipio;
import cpc.modelo.ministerio.dimension.UbicacionParroquia;
import cpc.negocio.ministerio.basico.NegocioParroquia;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContParroquia extends ContVentanaBase<UbicacionParroquia> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioParroquia servicio;
	private AppDemeter app;
	private UiParroquia vista;

	public ContParroquia(int modoOperacion, UbicacionParroquia tipo,
			ContParroquias llamador, AppDemeter app) throws ExcDatosInvalidos,
			ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			if (tipo == null || modoAgregar())
				tipo = new UbicacionParroquia();
			servicio = NegocioParroquia.getInstance();
			List<UbicacionMunicipio> municipios;
			municipios = servicio.getMunicipios();
			setear(tipo, new UiParroquia("PARROQUIA ("
					+ Accion.TEXTO[modoOperacion] + ")", 550, municipios),
					llamador, app);
			vista = ((UiParroquia) getVista());
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
			servicio = NegocioParroquia.getInstance();
			servicio.setModelo(getDato());
			servicio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {
		try {
			servicio = NegocioParroquia.getInstance();
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
				&& vista.getMunicipio().getSeleccion() == null) {
			throw new WrongValueException(vista.getDescripcion(),
					"Indique una Descripcion");
		}
	}

	public void onEvent(Event arg0) throws Exception {
		validar();
		try {
			procesarCRUD(arg0);
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError("Error al almacenar Parroquia");
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
