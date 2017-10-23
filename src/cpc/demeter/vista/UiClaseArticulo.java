package cpc.demeter.vista;

import java.util.List;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.basico.ClaseArticulo;
import cpc.modelo.demeter.basico.TipoArticulo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiClaseArticulo extends CompVentanaBase<ClaseArticulo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739695024627594930L;
	// constraint="/.+@.+\.[a-z]+ /: correo no valido" />

	private CompGrupoDatos gbGeneral;
	private Label codigo;
	private Textbox descripcion;

	private CompCombobox<TipoArticulo> tipo;
	private List<TipoArticulo> tipos;

	// private Button aceptar, cancelar;
	// private DataBinder binder;

	public UiClaseArticulo(String titulo, int ancho, List<TipoArticulo> tipos)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.tipos = tipos;
	}

	public UiClaseArticulo(String titulo) throws ExcDatosInvalidos {
		super(titulo);

	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);

		descripcion = new Textbox();
		codigo = new Label();
		tipo = new CompCombobox<TipoArticulo>();

		tipo.setModelo(tipos);
		descripcion.setWidth("400px");
		codigo.setWidth("50px");
		tipo.setReadonly(true);
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Codigo:", codigo);
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.addComponente("Tipo Articulo:", tipo);
		gbGeneral.dibujar(this);

		addBotonera();

	}

	@Override
	public void cargarValores(ClaseArticulo arg0) throws ExcDatosInvalidos {
		if (getModelo().getId() != null)
			codigo.setValue("" + getModelo().getId());
		descripcion.setValue(getModelo().getDescripcion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);

		tipo.setSeleccion(getModelo().getTipoArticulo());
		getBinder().addBinding(tipo, "seleccion",
				getNombreModelo() + ".tipoArticulo", null, null, "save", null,
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

	public Label getCodigo() {
		return codigo;
	}

	public void setCodigo(Label codigo) {
		this.codigo = codigo;
	}

	public CompCombobox<TipoArticulo> getTipo() {
		return tipo;
	}

}
