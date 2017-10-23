package cpc.demeter.controlador.gestion;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.gestion.ContMotivosTransferenciasActivos;
import cpc.demeter.vista.gestion.UIMotivoTransferenciaActivo;
import cpc.modelo.demeter.gestion.MotivoTransferenciaActivo;
import cpc.negocio.demeter.gestion.NegocioMotivoTransferenciaActivo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContMotivoTransferenciaActivo extends
		ContVentanaBase<MotivoTransferenciaActivo> {

	private static final long serialVersionUID = -6988032414939364860L;
	private NegocioMotivoTransferenciaActivo servicio;
	private AppDemeter app;
	private UIMotivoTransferenciaActivo vista;

	public ContMotivoTransferenciaActivo(int modoOperacion,
			MotivoTransferenciaActivo motivo,
			ContMotivosTransferenciasActivos llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcFiltroExcepcion {
		super(modoOperacion);
		this.app = app;

		servicio = NegocioMotivoTransferenciaActivo.getInstance();

		try {
			if (motivo == null || modoAgregar())
				motivo = new MotivoTransferenciaActivo();

			setear(motivo, new UIMotivoTransferenciaActivo(
					"MOTIVO DE TRANSFERENCIA (" + Accion.TEXTO[modoOperacion]
							+ ")", 500, modoOperacion), llamador, app);
			vista = (UIMotivoTransferenciaActivo) getVista();
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
			servicio.setMotivoTransferenciaActivo(getDato());
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
