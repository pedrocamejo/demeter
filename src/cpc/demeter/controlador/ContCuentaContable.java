package cpc.demeter.controlador;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContCuentasContables;
import cpc.demeter.vista.UiCuentaContable;
import cpc.modelo.sigesp.basico.CuentaContable;
import cpc.negocio.demeter.NegocioCuentaContable;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContCuentaContable extends ContVentanaBase<CuentaContable> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioCuentaContable servicio;
	private AppDemeter app;
	private UiCuentaContable vista;

	public ContCuentaContable(int modoOperacion, CuentaContable cuenta,
			ContCuentasContables llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			if (cuenta == null || modoAgregar())
				cuenta = new CuentaContable();
			setear(cuenta, new UiCuentaContable("CUENTA CONTABLE ("
					+ Accion.TEXTO[modoOperacion] + ")", 550), llamador, app);
			vista = ((UiCuentaContable) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		}
	}

	public void eliminar() {

		try {
			servicio = NegocioCuentaContable.getInstance();
			servicio.setCuentaContable(getDato());
			servicio.eliminar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() throws ExcFiltroExcepcion {
		servicio = NegocioCuentaContable.getInstance();
		servicio.setCuentaContable(getDato());
		servicio.guardar(modoAgregar());
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (vista.getCodigo().getValue() == ""
				|| vista.getCodigo().getValue().equals(null))
			throw new WrongValueException(vista.getCodigo(),
					"Indique el Codigo");
		if (vista.getDescripcion().getValue() == ""
				|| vista.getDescripcion().getValue().equals(null))
			throw new WrongValueException(vista.getDescripcion(),
					"Indique la Descripcion");

	}

	public void onEvent(Event arg0) throws Exception {
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
