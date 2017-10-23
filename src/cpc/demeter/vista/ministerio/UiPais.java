package cpc.demeter.vista.ministerio;

import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.ministerio.dimension.UbicacionPais;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiPais extends CompVentanaBase<UbicacionPais> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739695024627594930L;
	// constraint="/.+@.+\.[a-z]+ /: correo no valido" />

	private CompGrupoDatos gbGeneral;
	private Label codigo;
	private Textbox descripcion;
	private Checkbox local;

	// private Button aceptar, cancelar;
	// private DataBinder binder;

	public UiPais(String titulo, int ancho) throws ExcDatosInvalidos {
		super(titulo, ancho);
	}

	public UiPais(String titulo) throws ExcDatosInvalidos {
		super(titulo);

	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);

		descripcion = new Textbox();
		codigo = new Label();
		local = new Checkbox();
		descripcion.setWidth("400px");
		codigo.setWidth("50px");
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Codigo:", codigo);
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.addComponente("local :", local);
		gbGeneral.dibujar(this);

		addBotonera();

	}

	public void cargarValores(UbicacionPais arg0) throws ExcDatosInvalidos {
		if (getModelo().getId() != null)
			codigo.setValue("" + getModelo().getId());
		descripcion.setValue(getModelo().getNombre());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".nombre", null, null, "save", null, null,
				null, null);

		local.setChecked(getModelo().isLocal());
		getBinder().addBinding(local, "checked", getNombreModelo() + ".local",
				null, null, "save", null, null, null, null);
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		descripcion.setDisabled(true);
		local.setDisabled(true);
	}

	public void modoEdicion() {
		descripcion.setDisabled(false);
		local.setDisabled(false);
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.descripcion = descripcion;
	}

	public Checkbox getLocal() {
		return local;
	}

	public void setLocal(Checkbox local) {
		this.local = local;
	}

}
