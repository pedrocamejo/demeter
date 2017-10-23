package cpc.demeter.vista.mantenimiento;

import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Textbox;

import cpc.demeter.AppDemeter;
import cpc.modelo.demeter.mantenimiento.TipoMedidaRendimiento;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UITipoMedidaRendimiento extends
		CompVentanaBase<TipoMedidaRendimiento> {

	private static final long serialVersionUID = 995855838440816728L;
	private CompGrupoDatos agrupador;
	private Textbox descripcion;
	private Checkbox tipoHora;
	@SuppressWarnings("unused")
	private AppDemeter app;

	public UITipoMedidaRendimiento() {

	}

	public UITipoMedidaRendimiento(String titulo, int ancho, AppDemeter app) {
		super(titulo, ancho);
		this.app = app;
	}

	public void inicializar() {
		agrupador = new CompGrupoDatos("", 2);
		descripcion = new Textbox();
		tipoHora = new Checkbox();

		descripcion.setWidth("400px");
	}

	public void dibujar() {
		agrupador.setAnchoColumna(0, 100);
		agrupador.addComponente("Descripcion:", descripcion);
		agrupador.addComponente("Tipo Hora: ", tipoHora);
		agrupador.dibujar(this);
		addBotonera();
	}

	public void cargarValores(TipoMedidaRendimiento arg0)
			throws ExcDatosInvalidos {

		descripcion.setValue(getModelo().getDescripcion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);
		tipoHora.setChecked(getModelo().getTipoHora());
		getModelo().setTipoHora(tipoHora.isChecked());

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