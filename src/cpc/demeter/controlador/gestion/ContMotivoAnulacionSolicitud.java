package cpc.demeter.controlador.gestion;

import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.gestion.ContMotivosAnulacionesSolitudes;
import cpc.demeter.vista.gestion.UIMotivoAnulacionSolicitud;
import cpc.modelo.demeter.basico.TipoSolicitud;
import cpc.modelo.demeter.gestion.MotivoAnulacionSolicitud;
import cpc.negocio.demeter.gestion.NegocioMotivoAnulacionSolicitud;
import cpc.persistencia.demeter.implementacion.gestion.PerTipoSolicitud;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContMotivoAnulacionSolicitud extends
		ContVentanaBase<MotivoAnulacionSolicitud> {

	private static final long serialVersionUID = 7019770158879383086L;
	private NegocioMotivoAnulacionSolicitud servicio;
	private AppDemeter app;
	private UIMotivoAnulacionSolicitud vista;

	public ContMotivoAnulacionSolicitud(int modoOperacion,
			MotivoAnulacionSolicitud motivo,
			ContMotivosAnulacionesSolitudes llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcFiltroExcepcion {
		super(modoOperacion);
		this.app = app;

		servicio = NegocioMotivoAnulacionSolicitud.getInstance();

		try {
			// corre
			// @SuppressWarnings("unchecked")
			// List<TipoSolicitud> tiposolicitud = (List<TipoSolicitud>)
			// motivo.getTipoSolicitud();
			List<TipoSolicitud> tiposolicitud = new PerTipoSolicitud().getAll();
			if (motivo == null || modoAgregar()) {
				motivo = new MotivoAnulacionSolicitud();
				motivo.setActivo(true);
			}
			setear(motivo,
					new UIMotivoAnulacionSolicitud("MOTIVO DE ANULACION ("
							+ Accion.TEXTO[modoOperacion] + ")", 500,
							modoOperacion, tiposolicitud), llamador, app);
			vista = (UIMotivoAnulacionSolicitud) getVista();
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha Seleccionado nigun campo");
		}
	}

	public void eliminar() {

	}

	public void guardar() {

		try {
			vista.actualizarModelo();
			if (modoAgregar())
				// corre
				// getDato().setActivo(true);
				getDato().setTipoSolicitud(vista.getTipo().getSeleccion());
			servicio.setMotivoMotivoAnulacionSolicitud(getDato());
			servicio.guardar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (vista.getTxtdescripcion().getValue().isEmpty())
			throw new WrongValueException(vista.getTxtdescripcion(),
					"No puede dejar la descripcion en blanco");
		if (vista.getTipo().getSeleccion() == null) {
			throw new WrongValueException(vista.getTipo(),
					"Indique el tipo de Solicitud");
		}
		if (modoEditar()) {
			if (servicio.getusado(getDato()))
				throw new WrongValueException(vista.getTipo(),
						"no se puede editar el motivo ya fue usado");

		}
	}

	public void onEvent(Event event) throws Exception {
		procesarCRUD(event);
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
