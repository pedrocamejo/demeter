package cpc.demeter.vista.ministerio;

import java.util.List;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.ministerio.basico.Nacionalidad;
import cpc.modelo.ministerio.dimension.UbicacionPais;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiNacionalidad extends CompVentanaBase<Nacionalidad> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739695024627594930L;
	// constraint="/.+@.+\.[a-z]+ /: correo no valido" />

	private CompGrupoDatos gbGeneral;
	private Label codigo;
	private Textbox descripcion;

	private CompCombobox<UbicacionPais> pais;
	private List<UbicacionPais> paises;

	// private Button aceptar, cancelar;
	// private DataBinder binder;

	public UiNacionalidad(String titulo, int ancho, List<UbicacionPais> paises)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.paises = paises;
	}

	public UiNacionalidad(String titulo) throws ExcDatosInvalidos {
		super(titulo);

	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);

		descripcion = new Textbox();
		codigo = new Label();
		pais = new CompCombobox<UbicacionPais>();

		pais.setModelo(paises);
		descripcion.setWidth("400px");
		codigo.setWidth("50px");
		pais.setReadonly(true);
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Codigo:", codigo);
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.addComponente("Pais:", pais);
		gbGeneral.dibujar(this);

		addBotonera();

	}

	@Override
	public void cargarValores(Nacionalidad arg0) throws ExcDatosInvalidos {
		if (getModelo().getId() != null)
			codigo.setValue("" + getModelo().getId());
		descripcion.setValue(getModelo().getDenotacion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".denotacion", null, null, "save", null,
				null, null, null);

		pais.setSeleccion(getModelo().getPais());
		getBinder().addBinding(pais, "seleccion", getNombreModelo() + ".pais",
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
		pais.setDisabled(true);
	}

	public void modoEdicion() {
		descripcion.setDisabled(false);
		pais.setDisabled(false);
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

	public CompCombobox<UbicacionPais> getPais() {
		return pais;
	}

	public void setPais(CompCombobox<UbicacionPais> pais) {
		this.pais = pais;
	}

	public List<UbicacionPais> getPaises() {
		return paises;
	}

	public void setPaises(List<UbicacionPais> paises) {
		this.paises = paises;
	}
}
