package cpc.demeter.vista.ministerio;

import java.util.List;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.ministerio.gestion.Organizacion;
import cpc.modelo.ministerio.gestion.TipoOrganizacion;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiOrganizacion extends CompVentanaBase<Organizacion> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739695024627594930L;
	// constraint="/.+@.+\.[a-z]+ /: correo no valido" />

	private CompGrupoDatos gbGeneral;
	private Label codigo;
	private Textbox descripcion;

	private CompCombobox<TipoOrganizacion> tipo;
	private List<TipoOrganizacion> tipos;

	// private Button aceptar, cancelar;
	// private DataBinder binder;

	public UiOrganizacion(String titulo, int ancho, List<TipoOrganizacion> tipos)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.tipos = tipos;
	}

	public UiOrganizacion(String titulo) throws ExcDatosInvalidos {
		super(titulo);

	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);

		descripcion = new Textbox();
		codigo = new Label();
		tipo = new CompCombobox<TipoOrganizacion>();
		descripcion.setMaxlength(48);
		tipo.setModelo(tipos);
		descripcion.setWidth("400px");
		codigo.setWidth("50px");
		tipo.setReadonly(true);
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Codigo:", codigo);
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.addComponente("Tipo Organizacion:", tipo);
		gbGeneral.dibujar(this);

		addBotonera();

	}

	@Override
	public void cargarValores(Organizacion arg0) throws ExcDatosInvalidos {
		if (getModelo().getId() != null)
			codigo.setValue("" + getModelo().getId());
		descripcion.setValue(getModelo().getDenotacion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".denotacion", null, null, "save", null,
				null, null, null);

		tipo.setSeleccion(getModelo().getTipoOrganizacion());
		getBinder().addBinding(tipo, "seleccion",
				getNombreModelo() + ".tipoOrganizacion", null, null, "save",
				null, null, null, null);

	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		descripcion.setDisabled(true);
		tipo.setDisabled(true);
	}

	public void modoEdicion() {
		descripcion.setDisabled(false);
		tipo.setDisabled(false);
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

	public CompCombobox<TipoOrganizacion> getTipo() {
		return tipo;
	}

}
