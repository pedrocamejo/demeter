package cpc.demeter.vista;

import java.util.List;
import org.zkoss.zul.Textbox;
import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.basico.CargoTrabajador;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiCargoTrabajador extends CompVentanaBase<CargoTrabajador> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1556427427460472235L;
	/**
	 * 
	 */

	private CompGrupoDatos grpGeneral;
	private Textbox txtDescripcion;
	private CompCombobox<CargoTrabajador> cmbTipo;
	private List<CargoTrabajador> lstTipos;

	public UiCargoTrabajador(String titulo, int ancho,
			List<CargoTrabajador> tipos) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.lstTipos = tipos;
	}

	public UiCargoTrabajador(String titulo) throws ExcDatosInvalidos {
		super(titulo);

	}

	public void inicializar() {
		grpGeneral = new CompGrupoDatos("Datos generales", 2);
		txtDescripcion = new Textbox();
		cmbTipo = new CompCombobox<CargoTrabajador>();

		cmbTipo.setModelo(lstTipos);
		txtDescripcion.setWidth("400px");

	}

	public void dibujar() {
		grpGeneral.setAnchoColumna(0, 100);

		grpGeneral.addComponente("Descripcion del Cargo:", txtDescripcion);
		// grpGeneral.addComponente("Cargo:", cmbTipo);
		grpGeneral.dibujar(this);

		addBotonera();

	}

	@Override
	public void cargarValores(CargoTrabajador clase) throws ExcDatosInvalidos {
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

	public CompCombobox<CargoTrabajador> getCmbTipo() {
		return cmbTipo;
	}

	public void setCmbTipo(CompCombobox<CargoTrabajador> cmbTipo) {
		this.cmbTipo = cmbTipo;
	}

}
