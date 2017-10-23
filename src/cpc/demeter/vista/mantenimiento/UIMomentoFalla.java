package cpc.demeter.vista.mantenimiento;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import cpc.demeter.AppDemeter;
import cpc.modelo.demeter.mantenimiento.MomentoFalla;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIMomentoFalla extends CompVentanaBase<MomentoFalla> {

	private static final long serialVersionUID = 995855838440816728L;
	private CompGrupoDatos agrupador;
	private Label lblId;
	private Textbox descripcion;
	@SuppressWarnings("unused")
	private AppDemeter app;

	public UIMomentoFalla() {

	}

	public UIMomentoFalla(String titulo, int ancho, AppDemeter app) {
		super(titulo, ancho);
		this.app = app;
	}

	public void inicializar() {
		agrupador = new CompGrupoDatos("", 2);
		descripcion = new Textbox();
		lblId = new Label();
		descripcion.setWidth("400px");
	}

	public void dibujar() {
		agrupador.setAnchoColumna(0, 100);
		agrupador.addComponente("Id:", lblId);
		agrupador.addComponente("Descripcion:", descripcion);

		agrupador.dibujar(this);
		addBotonera();
	}

	public void cargarValores(MomentoFalla arg0) throws ExcDatosInvalidos {

		if (getModelo().getId() != null)
			lblId.setValue("" + getModelo().getId());

		descripcion.setValue(getModelo().getDescripcion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);
	}

	public void setLectura() {
		this.descripcion.setReadonly(true);
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.descripcion = descripcion;
	}
}