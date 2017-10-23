package cpc.demeter.controlador;

import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContCuentasBancarias;
import cpc.demeter.vista.administrativo.UiCuentaBancaria;
import cpc.modelo.sigesp.basico.Banco;
import cpc.modelo.sigesp.basico.CuentaBancaria;
import cpc.modelo.sigesp.basico.CuentaContable;
import cpc.modelo.sigesp.basico.TipoCuenta;
import cpc.negocio.demeter.NegocioCuentaBancaria;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContCuentaBancaria extends ContVentanaBase<CuentaBancaria> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioCuentaBancaria servicio;
	private AppDemeter app;
	private UiCuentaBancaria vista;

	public ContCuentaBancaria(int modoOperacion, CuentaBancaria banco,
			ContCuentasBancarias llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		servicio = NegocioCuentaBancaria.getInstance();
		this.app = app;
		try {
			if (banco == null || modoAgregar())
				banco = new CuentaBancaria();
			List<Banco> bancos = servicio.getBancos();
			List<TipoCuenta> tiposCuentas = servicio.getTiposCuentas();
			List<CuentaContable> cuentasContables = servicio
					.getCuentasContables();
			setear(banco, new UiCuentaBancaria("CUENTAS BANCARIAS ("
					+ Accion.TEXTO[modoOperacion] + ")", 550, bancos,
					tiposCuentas, cuentasContables), llamador, app);
			vista = ((UiCuentaBancaria) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
			app.mostrarError("problemas con los datos: " + e.getMessage());
		}
	}

	public void eliminar() {

		try {
			servicio = NegocioCuentaBancaria.getInstance();
			servicio.setCuentaBanco(getDato());
			servicio.eliminar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {

		try {
			servicio = NegocioCuentaBancaria.getInstance();
			servicio.setCuentaBanco(getDato());
			servicio.guardar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (vista.getTipoCuenta().getSelectedIndex() < 0)
			throw new WrongValueException(vista.getTipoCuenta(),
					"Indique el Tipo de Cuenta");
		if (vista.getBanco().getSelectedIndex() < 0)
			throw new WrongValueException(vista.getBanco(), "Indique el Banco");
		if (vista.getNroCuenta().getValue() == "")
			throw new WrongValueException(vista.getNroCuenta(),
					"Indique el Numero de Cuenta");
		if (vista.getDescripcion().getValue() == "")
			throw new WrongValueException(vista.getDescripcion(),
					"Indique la Descripcion");
		if (vista.getCuentaContable().getSelectedIndex() < 0)
			throw new WrongValueException(vista.getCuentaContable(),
					"Seleccione la Cuenta Contable");

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
