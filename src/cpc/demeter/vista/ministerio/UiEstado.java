package cpc.demeter.vista.ministerio;

import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.controlador.ministerio.ContEstado;
import cpc.modelo.ministerio.dimension.UbicacionEje;
import cpc.modelo.ministerio.dimension.UbicacionEstado;
import cpc.modelo.ministerio.dimension.UbicacionPais;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiEstado extends CompVentanaBase<UbicacionEstado> {

	private static final long serialVersionUID = -5739695024627594930L;
	private CompGrupoDatos gbGeneral;
	private Label codigo;
	private Textbox descripcion;
	private CompCombobox<UbicacionPais> pais;
	private CompCombobox<UbicacionEje> eje;

	private List<UbicacionPais> paises;

	// private List<UbicacionEje> ejes;

	// private Button aceptar, cancelar;
	// private DataBinder binder;

	public UiEstado(String titulo, int ancho, List<UbicacionPais> paises)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.paises = paises;
	}

	public UiEstado(String titulo) throws ExcDatosInvalidos {
		super(titulo);
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);
		descripcion = new Textbox();
		codigo = new Label();
		pais = new CompCombobox<UbicacionPais>();
		eje = new CompCombobox<UbicacionEje>();
		pais.setModelo(paises);
		pais.addEventListener(Events.ON_SELECTION, getControlador());
		descripcion.setWidth("400px");
		codigo.setWidth("50px");
		pais.setReadonly(true);
		eje.setReadonly(true);
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Codigo:", codigo);
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.addComponente("pais:", pais);
		gbGeneral.addComponente("eje:", eje);
		gbGeneral.dibujar(this);

		addBotonera();
	}

	@Override
	public void cargarValores(UbicacionEstado arg0) throws ExcDatosInvalidos {
		if (getModelo().getId() != null)
			codigo.setValue("" + getModelo().getId());
		descripcion.setValue(getModelo().getNombre());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".nombre", null, null, "save", null, null,
				null, null);

		pais.setSeleccion(getModelo().getPais());
		getBinder().addBinding(pais, "seleccion", getNombreModelo() + ".pais",
				null, null, "save", null, null, null, null);
		if (getModelo().getPais() != null) {
			ContEstado control = (ContEstado) getControlador();
			control.ActualizarEjes(getModelo().getPais());
		}

		eje.setSeleccion(getModelo().getEje());
		getBinder().addBinding(eje, "seleccion", getNombreModelo() + ".eje",
				null, null, "save", null, null, null, null);
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		descripcion.setDisabled(true);
		eje.setDisabled(true);
		pais.setDisabled(true);
	}

	public void modoEdicion() {
		descripcion.setDisabled(false);
		eje.setDisabled(false);
		pais.setDisabled(false);
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.descripcion = descripcion;
	}

	public List<UbicacionPais> getPaises() {
		return paises;
	}

	public void setPaises(List<UbicacionPais> paises) {
		this.paises = paises;
	}

	public CompCombobox<UbicacionPais> getPais() {
		return pais;
	}

	public void setPais(CompCombobox<UbicacionPais> pais) {
		this.pais = pais;
	}

	public void refrescarEjes(List<UbicacionEje> ejes) {
		eje.setModelo(ejes);
	}

}
