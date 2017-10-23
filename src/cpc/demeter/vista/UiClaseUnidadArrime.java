package cpc.demeter.vista;

import java.util.List;

import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.basico.ClaseUnidadArrime;
import cpc.modelo.demeter.basico.TipoUnidadArrime;

import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiClaseUnidadArrime extends CompVentanaBase<ClaseUnidadArrime> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1556427427460472235L;
	/**
	 * 
	 */

	private CompGrupoDatos grpGeneral;
	private Textbox txtDescripcion;
	private CompCombobox<TipoUnidadArrime> cmbTipo;
	private List<TipoUnidadArrime> lstTipos;

	public UiClaseUnidadArrime(String titulo, int ancho,
			List<TipoUnidadArrime> tipos) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.lstTipos = tipos;
	}

	public UiClaseUnidadArrime(String titulo) throws ExcDatosInvalidos {
		super(titulo);

	}

	public void inicializar() {
		grpGeneral = new CompGrupoDatos("Datos generales", 2);
		txtDescripcion = new Textbox();
		cmbTipo = new CompCombobox<TipoUnidadArrime>();

		cmbTipo.setModelo(lstTipos);
		txtDescripcion.setWidth("400px");

	}

	public void dibujar() {
		grpGeneral.setAnchoColumna(0, 100);
		grpGeneral.addComponente("Descripcion:", txtDescripcion);
		grpGeneral.addComponente("Tipo Unidad Arrime:", cmbTipo);
		grpGeneral.dibujar(this);

		addBotonera();

	}

	@Override
	public void cargarValores(ClaseUnidadArrime clase) throws ExcDatosInvalidos {
		if (getModelo().getId() != null)

			txtDescripcion.setValue(getModelo().getDescripcion());
		getBinder().addBinding(txtDescripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);
		cmbTipo.setSeleccion(getModelo().getTipo());
		getBinder().addBinding(cmbTipo, "seleccion",
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
		txtDescripcion.setDisabled(true);
		cmbTipo.setDisabled(true);
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

	public CompCombobox<TipoUnidadArrime> getCmbTipo() {
		return cmbTipo;
	}

	public void setCmbTipo(CompCombobox<TipoUnidadArrime> cmbTipo) {
		this.cmbTipo = cmbTipo;
	}

}
