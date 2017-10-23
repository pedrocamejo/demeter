package cpc.demeter.vista.ministerio;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.ministerio.dimension.UbicacionMunicipio;
import cpc.modelo.ministerio.dimension.UbicacionParroquia;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UiParroquia extends CompVentanaBase<UbicacionParroquia> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739695024627594930L;
	// constraint="/.+@.+\.[a-z]+ /: correo no valido" />

	private CompGrupoDatos gbGeneral;
	private Label codigo;
	private Textbox descripcion;

	private CompBuscar<UbicacionMunicipio> municipio;
	private List<UbicacionMunicipio> municipios;

	// private Button aceptar, cancelar;
	// private DataBinder binder;

	public UiParroquia(String titulo, int ancho,
			List<UbicacionMunicipio> municipios) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.municipios = municipios;
	}

	public UiParroquia(String titulo) throws ExcDatosInvalidos {
		super(titulo);

	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);

		descripcion = new Textbox();
		codigo = new Label();
		municipio = new CompBuscar<UbicacionMunicipio>(getEncabezadoEstado(), 0);
		municipio.setAncho(700);
		municipio.setWidth("400px");
		municipio.setModelo(municipios);

		descripcion.setWidth("400px");
		codigo.setWidth("50px");
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Codigo:", codigo);
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.addComponente("municipio:", municipio);
		gbGeneral.dibujar(this);

		addBotonera();

	}

	private List<CompEncabezado> getEncabezadoEstado() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("municipio", 250);
		titulo.setMetodoBinder("getNombre");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("estado", 250);
		titulo.setMetodoBinder("getStrEstado");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("pais", 150);
		titulo.setMetodoBinder("getStrPais");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;

	}

	public void cargarValores(UbicacionParroquia arg0) throws ExcDatosInvalidos {
		if (getModelo().getId() != null)
			codigo.setValue("" + getModelo().getId());
		descripcion.setValue(getModelo().getNombre());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".nombre", null, null, "save", null, null,
				null, null);
		municipio.setSeleccion(getModelo().getMunicipio());
		getBinder().addBinding(municipio, "seleccion",
				getNombreModelo() + ".municipio", null, null, "save", null,
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
	}

	public void modoEdicion() {
		descripcion.setDisabled(false);
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.descripcion = descripcion;
	}

	public CompBuscar<UbicacionMunicipio> getMunicipio() {
		return municipio;
	}

}
