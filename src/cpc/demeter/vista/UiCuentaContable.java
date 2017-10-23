package cpc.demeter.vista;

import org.zkoss.zul.Textbox;
import cpc.ares.modelo.Accion;
import cpc.modelo.sigesp.basico.CuentaContable;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiCuentaContable extends CompVentanaBase<CuentaContable> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739695024627594930L;
	// constraint="/.+@.+\.[a-z]+ /: correo no valido" />

	private CompGrupoDatos gbGeneral;
	private Textbox codigo;

	private Textbox descripcion;

	// private Button aceptar, cancelar;
	// private DataBinder binder;

	public UiCuentaContable(String titulo, int ancho) throws ExcDatosInvalidos {
		super(titulo, ancho);
	}

	public UiCuentaContable(String titulo) throws ExcDatosInvalidos {
		super(titulo);

	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);

		descripcion = new Textbox();
		codigo = new Textbox();
		descripcion.setWidth("400px");
		codigo.setWidth("200px");
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Codigo:", codigo);
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.dibujar(this);

		addBotonera();

	}

	@Override
	public void cargarValores(CuentaContable arg0) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		codigo.setValue(getModelo().getId());
		getBinder().addBinding(codigo, "value", getNombreModelo() + ".id",
				null, null, "save", null, null, null, null);
		descripcion.setValue(getModelo().getDenominacion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".denominacion", null, null, "save", null,
				null, null, null);
	}

	public void activarConsulta() {
		codigo.setDisabled(true);
		descripcion.setDisabled(true);
	}

	public void modoEdicion() {
		codigo.setDisabled(true);
		descripcion.setDisabled(false);
	}

	public void modoNuevo() {
		codigo.setDisabled(false);
		descripcion.setDisabled(false);
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.AGREGAR)
			modoNuevo();
		else if (modoOperacion == Accion.EDITAR)
			modoEdicion();
		else
			activarConsulta();
	}

	public Textbox getCodigo() {
		return codigo;
	}

	public void setCodigo(Textbox codigo) {
		this.codigo = codigo;
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.descripcion = descripcion;
	}

}
