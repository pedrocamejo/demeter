package cpc.demeter.vista;

import org.zkoss.zul.Textbox;
import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.basico.TipoDocumentoTierra;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiTipoDocumentoPropiedad extends
		CompVentanaBase<TipoDocumentoTierra> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7000747994771867453L;
	private CompGrupoDatos grpGeneral;
	private Textbox txtDescripcion;

	public UiTipoDocumentoPropiedad(String titulo, int ancho)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
	}

	public UiTipoDocumentoPropiedad(String titulo) throws ExcDatosInvalidos {
		super(titulo);
	}

	public void inicializar() {
		grpGeneral = new CompGrupoDatos("Datos generales", 2);
		txtDescripcion = new Textbox();
		txtDescripcion.setWidth("400px");
	}

	public void dibujar() {
		grpGeneral.setAnchoColumna(0, 100);

		grpGeneral.addComponente("Descripcion de la Verificacion:",
				txtDescripcion);
		// grpGeneral.addComponente("Cargo:", cmbTipo);
		grpGeneral.dibujar(this);

		addBotonera();
	}

	@Override
	public void cargarValores(TipoDocumentoTierra clase)
			throws ExcDatosInvalidos {
		if (getModelo().getId() != null)

			txtDescripcion.setValue(getModelo().getDescripcion());
		getBinder().addBinding(txtDescripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);

	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		txtDescripcion.setDisabled(true);
	}

	public void modoEdicion() {
		txtDescripcion.setDisabled(false);
	}

	public Textbox getDescripcion() {
		return txtDescripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.txtDescripcion = descripcion;
	}

}
