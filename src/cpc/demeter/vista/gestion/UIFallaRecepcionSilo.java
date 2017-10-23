package cpc.demeter.vista.gestion;

import org.zkoss.zul.Textbox;
import cpc.demeter.AppDemeter;
import cpc.modelo.demeter.gestion.FallaRecepcionSilo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIFallaRecepcionSilo extends CompVentanaBase<FallaRecepcionSilo> {

	private static final long serialVersionUID = 995855838440816728L;
	private CompGrupoDatos agrupador;
	private Textbox descripcion;
	@SuppressWarnings("unused")
	private AppDemeter app;

	public UIFallaRecepcionSilo() {

	}

	public UIFallaRecepcionSilo(String titulo, int ancho, AppDemeter app) {
		super(titulo, ancho);
		this.app = app;
	}

	public void cargarValores(FallaRecepcionSilo arg0) throws ExcDatosInvalidos {
		descripcion.setValue(getModelo().getDescripcion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);
	}

	public void inicializar() {
		agrupador = new CompGrupoDatos("", 2);
		descripcion = new Textbox();
		descripcion.setWidth("400px");
	}

	public void dibujar() {
		agrupador.setAnchoColumna(0, 100);
		agrupador.addComponente("Descripcion:", descripcion);
		agrupador.dibujar(this);
		addBotonera();
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.descripcion = descripcion;
	}

}
