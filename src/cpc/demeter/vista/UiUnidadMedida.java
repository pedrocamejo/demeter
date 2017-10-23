package cpc.demeter.vista;

import java.util.List;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.basico.TipoUnidadMedida;
import cpc.modelo.demeter.basico.UnidadMedida;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiUnidadMedida extends CompVentanaBase<UnidadMedida> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739695024627594930L;
	// constraint="/.+@.+\.[a-z]+ /: correo no valido" />

	private CompGrupoDatos gbGeneral;
	private Label codigo;
	private Textbox descripcion;
	private Textbox abreviatura;
	private CompCombobox<TipoUnidadMedida> tipo;
	private List<TipoUnidadMedida> tipos;

	// private Button aceptar, cancelar;
	// private DataBinder binder;

	public UiUnidadMedida(String titulo, int ancho, List<TipoUnidadMedida> tipos)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.tipos = tipos;
	}

	public UiUnidadMedida(String titulo) throws ExcDatosInvalidos {
		super(titulo);

	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);

		descripcion = new Textbox();
		abreviatura = new Textbox();
		codigo = new Label();
		tipo = new CompCombobox<TipoUnidadMedida>();

		tipo.setModelo(tipos);
		descripcion.setWidth("400px");
		codigo.setWidth("50px");
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Codigo:", codigo);
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.addComponente("Abreviatura:", abreviatura);
		gbGeneral.addComponente("Tipo Unidad:", tipo);
		gbGeneral.dibujar(this);

		addBotonera();

	}

	@Override
	public void cargarValores(UnidadMedida arg0) throws ExcDatosInvalidos {
		if (getModelo().getId() != null)
			codigo.setValue("" + getModelo().getId());
		descripcion.setValue(getModelo().getDescripcion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);
		abreviatura.setValue(getModelo().getAbreviatura());
		getBinder().addBinding(abreviatura, "value",
				getNombreModelo() + ".abreviatura", null, null, "save", null,
				null, null, null);

		tipo.setSeleccion(getModelo().getTipo());
		getBinder().addBinding(tipo, "seleccion", getNombreModelo() + ".tipo",
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

	public CompCombobox<TipoUnidadMedida> getTipo() {
		return tipo;
	}

	public void setTipo(CompCombobox<TipoUnidadMedida> pais) {
		this.tipo = pais;
	}

	public List<TipoUnidadMedida> getTipos() {
		return tipos;
	}

	public void setTipos(List<TipoUnidadMedida> tipos) {
		this.tipos = tipos;
	}
}
