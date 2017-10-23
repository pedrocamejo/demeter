package cpc.demeter.vista.ministerio;

import java.util.List;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.ministerio.gestion.InstitucionCrediticia;
import cpc.modelo.ministerio.gestion.TipoFinanciamientoCrediticio;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiInstitucionCrediticia extends
		CompVentanaBase<InstitucionCrediticia> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739695024627594930L;
	// constraint="/.+@.+\.[a-z]+ /: correo no valido" />

	private CompGrupoDatos gbGeneral;
	private Label codigo;
	private Textbox descripcion;

	private CompCombobox<TipoFinanciamientoCrediticio> tipoFinanciamiento;
	private List<TipoFinanciamientoCrediticio> tipos;

	// private Button aceptar, cancelar;
	// private DataBinder binder;

	public UiInstitucionCrediticia(String titulo, int ancho,
			List<TipoFinanciamientoCrediticio> tipos) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.tipos = tipos;
	}

	public UiInstitucionCrediticia(String titulo) throws ExcDatosInvalidos {
		super(titulo);

	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);

		descripcion = new Textbox();
		codigo = new Label();
		tipoFinanciamiento = new CompCombobox<TipoFinanciamientoCrediticio>();

		tipoFinanciamiento.setModelo(tipos);
		descripcion.setWidth("400px");
		codigo.setWidth("50px");
		tipoFinanciamiento.setReadonly(true);
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Codigo:", codigo);
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.addComponente("Tipo Financiamiento:", tipoFinanciamiento);
		gbGeneral.dibujar(this);

		addBotonera();

	}

	@Override
	public void cargarValores(InstitucionCrediticia arg0)
			throws ExcDatosInvalidos {
		if (getModelo().getId() != null)
			codigo.setValue("" + getModelo().getId());
		descripcion.setValue(getModelo().getDenotacion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".denotacion", null, null, "save", null,
				null, null, null);

		tipoFinanciamiento.setSeleccion(getModelo().getTipoFinanciamiento());
		getBinder().addBinding(tipoFinanciamiento, "seleccion",
				getNombreModelo() + ".tipoFinanciamiento", null, null, "save",
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
		tipoFinanciamiento.setDisabled(true);
	}

	public void modoEdicion() {
		descripcion.setDisabled(false);
		tipoFinanciamiento.setDisabled(false);
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

	public CompCombobox<TipoFinanciamientoCrediticio> getTipoFinanciamiento() {
		return tipoFinanciamiento;
	}

}
