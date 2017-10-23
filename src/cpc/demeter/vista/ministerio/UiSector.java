package cpc.demeter.vista.ministerio;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.ministerio.dimension.UbicacionParroquia;
import cpc.modelo.ministerio.dimension.UbicacionSector;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UiSector extends CompVentanaBase<UbicacionSector> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739695024627594930L;
	// constraint="/.+@.+\.[a-z]+ /: correo no valido" />

	private CompGrupoDatos gbGeneral;
	private Label codigo;
	private Textbox descripcion;

	private CompBuscar<UbicacionParroquia> parroquia;
	private List<UbicacionParroquia> parroquias;

	// private Button aceptar, cancelar;
	// private DataBinder binder;

	public UiSector(String titulo, int ancho,
			List<UbicacionParroquia> parroquias) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.parroquias = parroquias;
	}

	public UiSector(String titulo) throws ExcDatosInvalidos {
		super(titulo);
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);

		descripcion = new Textbox();
		codigo = new Label();
		parroquia = new CompBuscar<UbicacionParroquia>(getEncabezadoEstado(), 0);
		parroquia.setAncho(820);
		parroquia.setWidth("400px");
		parroquia.setModelo(parroquias);

		descripcion.setWidth("400px");
		codigo.setWidth("50px");
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Codigo:", codigo);
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.addComponente("parroquia:", parroquia);
		gbGeneral.dibujar(this);

		addBotonera();

	}

	private List<CompEncabezado> getEncabezadoEstado() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Parroquia", 250);
		titulo.setMetodoBinder("getNombre");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("municipio", 200);
		titulo.setMetodoBinder("getStrMunicipio");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("estado", 200);
		titulo.setMetodoBinder("getStrEstado");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("pais", 150);
		titulo.setMetodoBinder("getStrPais");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;

	}

	public void cargarValores(UbicacionSector arg0) throws ExcDatosInvalidos {
		if (getModelo().getId() != null)
			codigo.setValue("" + getModelo().getId());
		descripcion.setValue(getModelo().getNombre());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".nombre", null, null, "save", null, null,
				null, null);

		parroquia.setSeleccion(getModelo().getParroquia());
		getBinder().addBinding(parroquia, "seleccion",
				getNombreModelo() + ".parroquia", null, null, "save", null,
				null, null, null);

	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		descripcion.setDisabled(true);
		parroquia.setDisabled(true);
	}

	public void modoEdicion() {
		descripcion.setDisabled(false);
		parroquia.setDisabled(false);
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.descripcion = descripcion;
	}

	public CompBuscar<UbicacionParroquia> getParroquia() {
		return parroquia;
	}

}
