package cpc.demeter.vista.ministerio;

import java.util.Arrays;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Textbox;
import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.basico.CodigoArea;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiCodigoArea extends CompVentanaBase<CodigoArea> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4364711571022079166L;
	private CompGrupoDatos gbGeneral;
	private Textbox codigo;
	private Textbox descripcion;
	private CompCombobox<String> tipo;

	public UiCodigoArea(String titulo, int ancho) throws ExcDatosInvalidos {
		super(titulo, ancho);
	}

	public UiCodigoArea(String titulo) throws ExcDatosInvalidos {
		super(titulo);
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);
		descripcion = new Textbox();
		codigo = new Textbox();
		tipo = new CompCombobox<String>();
		String[] tipos = { "MOVIL", "FIJO", "TELEFAX" };
		tipo.setModelo(Arrays.asList(tipos));
		descripcion.setWidth("400px");
		codigo.setWidth("50px");
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Codigo:", codigo);
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.addComponente("Tipo:", tipo);
		gbGeneral.dibujar(this);
		addBotonera();
	}

	public void cargarValores(CodigoArea arg0) throws ExcDatosInvalidos {
		codigo.setValue(getModelo().getCodigoArea());
		getBinder().addBinding(codigo, "value",
				getNombreModelo() + ".codigoArea", null, null, "save", null,
				null, null, null);
		descripcion.setValue(getModelo().getDescripcion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);
		if (getModelo().getTipo() != null)
			tipo.setSelectedIndex(getModelo().getTipo());
		getBinder().addBinding(tipo, "selectedIndex",
				getNombreModelo() + ".tipo", null, null, "save", null, null,
				null, null);
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
		codigo.setDisabled(true);
	}

	public void modoEdicion() {
		descripcion.setDisabled(false);
		tipo.setDisabled(false);
		codigo.setDisabled(false);
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.descripcion = descripcion;
	}

	public Textbox getCodigo() {
		return codigo;
	}

	public Combobox getTipo() {
		return tipo;
	}

}
