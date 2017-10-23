package cpc.demeter.vista.ministerio;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.ministerio.basico.TipoVialidad;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiTipoVialidad extends CompVentanaBase<TipoVialidad> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739695024627594930L;
	// constraint="/.+@.+\.[a-z]+ /: correo no valido" />

	private CompGrupoDatos gbGeneral;
	private Label codigo;
	private Textbox descripcion;

	// private Button aceptar, cancelar;
	// private DataBinder binder;

	public UiTipoVialidad(String titulo, int ancho) throws ExcDatosInvalidos {
		super(titulo, ancho);
	}

	public UiTipoVialidad(String titulo) throws ExcDatosInvalidos {
		super(titulo);

	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);

		descripcion = new Textbox();
		codigo = new Label();
		descripcion.setWidth("400px");
		codigo.setWidth("50px");
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Codigo:", codigo);
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.dibujar(this);

		addBotonera();

	}

	@Override
	public void cargarValores(TipoVialidad arg0) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		if (getModelo().getId() != null)
			codigo.setValue("" + getModelo().getId());
		descripcion.setValue(getModelo().getDenotacion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".denotacion", null, null, "save", null,
				null, null, null);
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		descripcion.setDisabled(true);
	}

	public void modoEdicion() {
		descripcion.setDisabled(false);
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.descripcion = descripcion;
	}

}
