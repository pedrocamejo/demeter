package cpc.demeter.vista.gestion;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.gestion.MotivoTransferenciaActivo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIMotivoTransferenciaActivo extends
		CompVentanaBase<MotivoTransferenciaActivo> {

	private static final long serialVersionUID = 5107922780341632881L;
	private int accion;
	private Textbox txtdescripcion;
	private Label lbldescripcion;
	private CompGrupoDatos gbGeneral;

	public UIMotivoTransferenciaActivo(String titulo, int ancho, int modo) {
		super(titulo, ancho);
		this.accion = modo;
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos", 2);
		if (accion == Accion.AGREGAR || accion == Accion.EDITAR)
			txtdescripcion = new Textbox();
		if (accion == Accion.CONSULTAR)
			lbldescripcion = new Label();

	}

	public void dibujar() {
		if (accion == Accion.AGREGAR || accion == Accion.EDITAR) {
			txtdescripcion.setWidth("350px");
			txtdescripcion.setMaxlength(50);
			gbGeneral.addComponente("Descripción: ", txtdescripcion);
			gbGeneral.dibujar(this);
		}

		if (accion == Accion.CONSULTAR) {
			gbGeneral.addComponente("Descripción: ", lbldescripcion);
			gbGeneral.dibujar(this);
		}
		addBotonera();
	}

	public void cargarValores(MotivoTransferenciaActivo arg0)
			throws ExcDatosInvalidos {
		if (accion == Accion.AGREGAR || accion == Accion.EDITAR) {
			txtdescripcion.setValue(getModelo().getDescripcion());
			getBinder().addBinding(txtdescripcion, "value",
					getNombreModelo() + ".descripcion", null, null, "save",
					null, null, null, null);
		}

		if (accion == Accion.CONSULTAR)
			lbldescripcion.setValue(getModelo().getDescripcion());
	}

	public int getAccion() {
		return accion;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	public Textbox getTxtdescripcion() {
		return txtdescripcion;
	}

	public void setTxtdescripcion(Textbox txtdescripcion) {
		this.txtdescripcion = txtdescripcion;
	}

	public Label getLbldescripcion() {
		return lbldescripcion;
	}

	public void setLbldescripcion(Label lbldescripcion) {
		this.lbldescripcion = lbldescripcion;
	}

}
