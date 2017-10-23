package cpc.demeter.vista.ministerio;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.ministerio.dimension.UbicacionEstado;
import cpc.modelo.ministerio.dimension.UbicacionMunicipio;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UiMunicipio extends CompVentanaBase<UbicacionMunicipio> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739695024627594930L;
	// constraint="/.+@.+\.[a-z]+ /: correo no valido" />

	private CompGrupoDatos gbGeneral;
	private Label codigo;
	private Textbox descripcion;
	private CompBuscar<UbicacionEstado> estado;
	private List<UbicacionEstado> estados;

	public UiMunicipio(String titulo, int ancho, List<UbicacionEstado> estados)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.estados = estados;
	}

	public UiMunicipio(String titulo) throws ExcDatosInvalidos {
		super(titulo);
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);

		descripcion = new Textbox();
		codigo = new Label();
		estado = new CompBuscar<UbicacionEstado>(getEncabezadoEstado(), 0);

		estado.setAncho(550);
		estado.setWidth("400px");
		estado.setModelo(estados);
		descripcion.setWidth("400px");
		codigo.setWidth("50px");
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Codigo:", codigo);
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.addComponente("Estado:", estado);
		gbGeneral.dibujar(this);
		addBotonera();
	}

	private List<CompEncabezado> getEncabezadoEstado() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("estado", 250);
		titulo.setMetodoBinder("getNombre");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("pais", 250);
		titulo.setMetodoBinder("getStrPais");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;

	}

	public void cargarValores(UbicacionMunicipio arg0) throws ExcDatosInvalidos {
		if (getModelo().getId() != null)
			codigo.setValue("" + getModelo().getId());
		descripcion.setValue(getModelo().getNombre());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".nombre", null, null, "save", null, null,
				null, null);

		estado.setSeleccion(getModelo().getEstado());
		getBinder().addBinding(estado, "seleccion",
				getNombreModelo() + ".estado", null, null, "save", null, null,
				null, null);
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		descripcion.setDisabled(true);
		estado.setDisabled(true);
	}

	public void modoEdicion() {
		descripcion.setDisabled(false);
		estado.setDisabled(false);
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.descripcion = descripcion;
	}

	public CompBuscar<UbicacionEstado> getEstado() {
		return estado;
	}

	public void setEstado(CompBuscar<UbicacionEstado> estado) {
		this.estado = estado;
	}

	public List<UbicacionEstado> getEstados() {
		return estados;
	}

	public void setEstados(List<UbicacionEstado> estados) {
		this.estados = estados;
	}

}
