package cpc.demeter.controlador;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContConfigurarTiposServicios;
import cpc.demeter.vista.UiCuentasTipoServicio;
import cpc.modelo.demeter.administrativo.CuentasTipoServicio;
import cpc.modelo.sigesp.basico.Sede;
import cpc.negocio.demeter.NegocioCuentasTipoServicio;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContConfigurarTipoServicio extends
		ContVentanaBase<CuentasTipoServicio> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioCuentasTipoServicio servicio;
	private AppDemeter app;
	private UiCuentasTipoServicio vista;

	public ContConfigurarTipoServicio(int modoOperacion,
			CuentasTipoServicio objeto, ContConfigurarTiposServicios llamador,
			AppDemeter app) throws ExcDatosInvalidos, ExcSeleccionNoValida,
			ExcFiltroExcepcion {
		super(modoOperacion);
		this.app = app;
		servicio = NegocioCuentasTipoServicio.getInstance();
		Sede sede = new Sede(app.getSede().getEmpresa().getCodigo(), app
				.getSede().getIdSede(), "", null, false);
		try {
			if (objeto == null || modoAgregar())
				objeto = new CuentasTipoServicio();

			vista = new UiCuentasTipoServicio("TIPO DE SERVICIO ("
					+ Accion.TEXTO[modoOperacion] + ")", 600,
					servicio.getCuentasContables(),
					servicio.getTipoServicios(), sede);
			setear(objeto, vista, llamador, app);
			vista = ((UiCuentasTipoServicio) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		}
	}

	public void eliminar() {

		try {
			servicio = NegocioCuentasTipoServicio.getInstance();
			servicio.setCuentasTipoServicio(getDato());
			servicio.eliminar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {

		try {
			servicio = NegocioCuentasTipoServicio.getInstance();
			servicio.setCuentasTipoServicio(getDato());
			servicio.guardar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR
				&& (vista.getCmbTipoServicio().getSelectedIndex() < 0)) {
			throw new WrongValueException(vista.getCmbTipoServicio(),
					"Indique el Tipo de Servicio");
		}
		if (getModoOperacion() != Accion.ELIMINAR
				&& (vista.getCuentaContable().getValue() == "")) {
			throw new WrongValueException(vista.getCuentaContable(),
					"Indique la Cuenta Contable de Ingreso");
		}

		if (getModoOperacion() != Accion.ELIMINAR
				&& (vista.getCuentaPresupuestaria().getValue() == "")) {
			throw new WrongValueException(vista.getCuentaPresupuestaria(),
					"Indique la Cuenta Presupuestaria de Ingreso");
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
