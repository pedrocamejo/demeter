package cpc.demeter.vista.mantenimiento;

import org.zkoss.zul.Textbox;
import cpc.demeter.AppDemeter;
import cpc.modelo.demeter.mantenimiento.EstadoFuncional;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIEstadoFuncional extends CompVentanaBase<EstadoFuncional> {

	private static final long serialVersionUID = 995855838440816728L;
	private CompGrupoDatos agrupador;
	private Textbox descripcion;
	@SuppressWarnings("unused")
	private AppDemeter app;

	public UIEstadoFuncional() {

	}

	public UIEstadoFuncional(String titulo, int ancho, AppDemeter app) {
		super(titulo, ancho);
		this.app = app;
	}

	public void cargarValores(EstadoFuncional arg0) throws ExcDatosInvalidos {
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
