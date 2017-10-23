package cpc.demeter.vista.administrativo;

import java.util.List;

import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.sigesp.basico.Banco;
import cpc.modelo.sigesp.basico.CuentaBancaria;
import cpc.modelo.sigesp.basico.CuentaContable;
import cpc.modelo.sigesp.basico.TipoCuenta;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiFormaPago extends CompVentanaBase<CuentaBancaria> {

	
	private static final long serialVersionUID = -5739695024627594930L;
	// constraint="/.+@.+\.[a-z]+ /: correo no valido" />

	private CompGrupoDatos gbGeneral;
	private CompCombobox<Banco> banco;
	private CompCombobox<TipoCuenta> tipoCuenta;
	private CompCombobox<CuentaContable> cuentaContable;
	private Textbox nroCuenta;
	private Textbox descripcion;

	private List<Banco> bancos;
	private List<CuentaContable> cuentasContables;
	private List<TipoCuenta> tiposCuentas;

	// private Button aceptar, cancelar;
	// private DataBinder binder;

	public UiFormaPago(String titulo, int ancho, List<Banco> bancos,
			List<TipoCuenta> tiposCuentas, List<CuentaContable> cuentasContables)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.bancos = bancos;
		this.cuentasContables = cuentasContables;
		this.tiposCuentas = tiposCuentas;
	}

	public UiFormaPago(String titulo) throws ExcDatosInvalidos {
		super(titulo);

	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);

		descripcion = new Textbox();
		nroCuenta = new Textbox();

		banco = new CompCombobox<Banco>();
		cuentaContable = new CompCombobox<CuentaContable>();
		tipoCuenta = new CompCombobox<TipoCuenta>();

		descripcion.setWidth("400px");
		nroCuenta.setWidth("400px");

		banco.setWidth("400px");
		cuentaContable.setWidth("400px");
		tipoCuenta.setWidth("400px");

		banco.setModelo(bancos);
		cuentaContable.setModelo(cuentasContables);
		tipoCuenta.setModelo(tiposCuentas);
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Tipo Cuenta:", tipoCuenta);
		gbGeneral.addComponente("Banco:", banco);
		gbGeneral.addComponente("Numero Cuenta:", nroCuenta);
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.addComponente("Cuenta Contable:", cuentaContable);
		gbGeneral.dibujar(this);

		addBotonera();

	}

	@Override
	public void cargarValores(CuentaBancaria arg0) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		tipoCuenta.setSeleccion(getModelo().getCodigoTipoCuenta());
		getBinder().addBinding(tipoCuenta, "seleccion",
				getNombreModelo() + ".codigoTipoCuenta", null, null, "save",
				null, null, null, null);

		banco.setSeleccion(getModelo().getBanco());
		getBinder().addBinding(banco, "seleccion",
				getNombreModelo() + ".banco", null, null, "save", null, null,
				null, null);

		nroCuenta.setValue(getModelo().getNroCuenta());
		getBinder().addBinding(nroCuenta, "value",
				getNombreModelo() + ".nroCuenta", null, null, "save", null,
				null, null, null);

		descripcion.setValue(getModelo().getDescripcion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);

		cuentaContable.setSeleccion(getModelo().getCuenta());
		getBinder().addBinding(cuentaContable, "seleccion",
				getNombreModelo() + ".cuenta", null, null, "save", null, null,
				null, null);
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.AGREGAR)
			modoNuevo();
		else if (modoOperacion == Accion.EDITAR)
			modoEdicion();
		else
			activarConsulta();
	}

	public void activarConsulta() {
		cuentaContable.setDisabled(true);
		tipoCuenta.setDisabled(true);
		banco.setDisabled(true);
		nroCuenta.setDisabled(true);
		descripcion.setDisabled(true);

	}

	public void modoEdicion() {
		cuentaContable.setDisabled(false);
		tipoCuenta.setDisabled(false);
		banco.setDisabled(true);
		nroCuenta.setDisabled(true);
		descripcion.setDisabled(true);
	}

	public void modoNuevo() {
		cuentaContable.setDisabled(false);
		tipoCuenta.setDisabled(false);
		banco.setDisabled(false);
		nroCuenta.setDisabled(false);
		descripcion.setDisabled(false);
	}

}
