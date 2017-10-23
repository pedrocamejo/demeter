package cpc.demeter.controlador.ministerio;

import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.ministerio.UiSector;
import cpc.modelo.ministerio.dimension.UbicacionParroquia;
import cpc.modelo.ministerio.dimension.UbicacionSector;
import cpc.negocio.ministerio.basico.NegocioSector;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContSector extends ContVentanaBase<UbicacionSector> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioSector servicio;
	private AppDemeter app;
	private UiSector vista;

	public ContSector(int modoOperacion, UbicacionSector tipo,
			ICompCatalogo<UbicacionSector> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			if (tipo == null || modoAgregar())
				tipo = new UbicacionSector();
			servicio = NegocioSector.getInstance();
			List<UbicacionParroquia> parroquias;
			parroquias = servicio.getParroquias();
			setear(tipo, new UiSector("SECTOR (" + Accion.TEXTO[modoOperacion]
					+ ")", 550, parroquias), llamador, app);
			vista = ((UiSector) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void eliminar() {

		try {
			servicio = NegocioSector.getInstance();
			servicio.setModelo(getDato());
			servicio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {

		try {
			servicio = NegocioSector.getInstance();
			servicio.setModelo(getDato());
			servicio.guardar(getDato().getId());
			setDato(servicio.getModelo());
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
				&& vista.getParroquia().getSeleccion() == null) {
			throw new WrongValueException(vista.getParroquia(),
					"Indique una Parroquia");
		}
		if (getModoOperacion() == Accion.AGREGAR) {
			try {
				List<UbicacionSector> sectores = servicio
						.getTodosPorNombreYParroquia(vista.getDescripcion()
								.getValue(), vista.getParroquia()
								.getSeleccion());
				if (sectores.size() > 0) {
					throw new WrongValueException(vista.getDescripcion(),
							"Esta parroquia ya se encuentra registrada");
				}
			} catch (ExcFiltroExcepcion e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void onEvent(Event arg0) throws Exception {
		validar();
		try {
			procesarCRUD(arg0);
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError("problemas al almacenar sector");
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
