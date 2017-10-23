package cpc.demeter.controlador;

import java.util.List;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.ministerio.ContUbicacionFisica;
import cpc.demeter.vista.UiUnidadArrime;
import cpc.modelo.demeter.basico.TipoUnidadArrime;
import cpc.modelo.demeter.basico.UnidadArrime;
import cpc.modelo.ministerio.dimension.UbicacionDireccion;
import cpc.negocio.demeter.basico.NegocioUnidadArrime;

import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContUnidadArrime extends ContVentanaBase<UnidadArrime> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioUnidadArrime servicio;
	private AppDemeter app;
	private UiUnidadArrime vista;

	public ContUnidadArrime(int modoOperacion, UnidadArrime silo,
			ICompCatalogo<UnidadArrime> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			servicio = NegocioUnidadArrime.getInstance();
			if (silo == null || modoAgregar())
				silo = new UnidadArrime();
			else {
				if (silo.getId() != null) {
					servicio.setModelo(silo);
					servicio.enriqueserDireccion();
					silo = servicio.getModelo();
				}
			}
			List<TipoUnidadArrime> tipoA = servicio.getTiposUnidadesArrime();
			List<UbicacionDireccion> direcciones = servicio.getDirecciones();
			setear(silo, new UiUnidadArrime("UNIDAD DE ARRIME ("
					+ Accion.TEXTO[modoOperacion] + ")", 620, tipoA,
					direcciones), llamador, app);
			vista = ((UiUnidadArrime) getVista());
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
			servicio = NegocioUnidadArrime.getInstance();
			servicio.setModelo(getDato());
			servicio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {

		try {
			servicio = NegocioUnidadArrime.getInstance();
			servicio.setModelo(getDato());
			servicio.guardar(getDato().getId());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR) {
			if (vista.getDescripcion().getValue() == null
					|| vista.getDescripcion().getValue() == "")
				throw new WrongValueException(vista.getDescripcion(),
						"Indique una Descripcion");
			if (vista.getTipoUnidad().getSeleccion() == null)
				throw new WrongValueException(vista.getDescripcion(),
						"Indique un Tipo de Unidad de Arrime");
			if (vista.getUbicacionFisica().getModelo() == null)
				throw new WrongValueException(vista.getUbicacionFisica(),
						"Indique una Ubicacion Fisica");
		}

	}

	public void onEvent(Event event) throws Exception {
		if (event.getTarget() == getVista().getAceptar()) {
			validar();
			procesarCRUD(event);
		} else if (event.getTarget() == vista.getUbicacionFisica()
				.getConsultar()) {
			new ContUbicacionFisica(Accion.CONSULTAR, vista
					.getUbicacionFisica().getModelo(),
					vista.getUbicacionFisica(), app);
		} else
			ActualizarClase();
	}

	private void ActualizarClase() throws ExcFiltroExcepcion {
		TipoUnidadArrime tipo = vista.getTipoUnidad().getSeleccion();
		if (tipo != null)
			vista.refrescarClaseUnidad(servicio.getClasesUnidadesArrime(tipo));
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
