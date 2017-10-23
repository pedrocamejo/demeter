package cpc.demeter.controlador.administrativo;

import java.util.Date;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import cpc.ares.modelo.Accion;
import cpc.ares.modelo.Sede;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContControlSedes;
import cpc.demeter.vista.administrativo.UiControlSede;
import cpc.modelo.demeter.administrativo.ControlSede;
import cpc.negocio.demeter.NegocioControlSede;
import cpc.persistencia.demeter.implementacion.PerControlSede;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContControlSede extends ContVentanaBase<ControlSede> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioControlSede servicio;
	private AppDemeter app;

	public ContControlSede(int modoOperacion, ControlSede sede,
			ContControlSedes llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;

		try {
			setear(sede, new UiControlSede("CONFIGURACION SEDE ("
					+ Accion.TEXTO[modoOperacion] + ")", 700), llamador,
					this.app);
			((UiControlSede) getVista()).desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		}
	}

	public void eliminar() {

		try {
			servicio = NegocioControlSede.getInstance();
			servicio.setControlSede(getDato());
			servicio.eliminar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {

		try {
			servicio = NegocioControlSede.getInstance();
			servicio.setControlSede(getDato());
			servicio.guardar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		UiControlSede vista = (UiControlSede) getVista();
		Date fecha1 = vista.getUltimoCierre().getValue();

		ControlSede sede22;
		try {
			sede22 = new PerControlSede().getControlSede();
			Date fecha2 = sede22.getFechaCierreFactura();
			boolean a = vista.getUltimoCierre().getValue()
					.before(sede22.getFechaCierreFactura());
			boolean b = vista.getUltimoCierre().getValue()
					.after(sede22.getFechaCierreFactura());
			if (a) {
				throw new WrongValueException(
						((UiControlSede) getVista()).getUltimoCierre(),
						"la fecha no puede ser menor a la anterior");
			}

		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onEvent(Event event) throws Exception {
		System.out.println("hay evento " + event.getName() + " en "
				+ event.getTarget().getClass().getName());
		try {
			if (event.getTarget() == getVista().getAceptar()) {
				procesarCRUD(event);
			} else if (event.getName().equals(Events.ON_CHECK)) {
				((UiControlSede) getVista()).activarmodo();
			}
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
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
