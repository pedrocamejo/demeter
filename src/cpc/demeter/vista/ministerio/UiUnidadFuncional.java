package cpc.demeter.vista.ministerio;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.ministerio.dimension.TipoUnidadFuncional;
import cpc.modelo.ministerio.dimension.UbicacionDireccion;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UiUnidadFuncional extends CompVentanaBase<UnidadFuncional> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739695024627594930L;
	// constraint="/.+@.+\.[a-z]+ /: correo no valido" />

	private CompGrupoDatos gbGeneral, gbDireccion;
	private Textbox descripcion;
	private CompCombobox<TipoUnidadFuncional> tipoUnidad;
	private CompBuscar<UbicacionDireccion> ubicacion;
	private Label estado, municipio, parroquia, sector, descripcionDireccion,
			referencia;
	private Checkbox sede;
	private Textbox serie;

	private List<UbicacionDireccion> direcciones;
	private List<TipoUnidadFuncional> tiposUnidades;

	public UiUnidadFuncional(String titulo, int ancho,
			List<UbicacionDireccion> direcciones,
			List<TipoUnidadFuncional> tiposUnidades) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.direcciones = direcciones;
		this.tiposUnidades = tiposUnidades;
	}

	public UiUnidadFuncional(String titulo) throws ExcDatosInvalidos {
		super(titulo);

	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);
		gbDireccion = new CompGrupoDatos("Ubicacion Fisica", 2);
		descripcion = new Textbox();
		serie = new Textbox();
		estado = new Label();
		municipio = new Label();
		parroquia = new Label();
		sector = new Label();
		referencia = new Label();
		descripcionDireccion = new Label();
		sede = new Checkbox();
		ubicacion = new CompBuscar<UbicacionDireccion>(getCatalogoDireccion(),
				4);
		tipoUnidad = new CompCombobox<TipoUnidadFuncional>();
		descripcion.setWidth("400px");
		sede.setWidth("50px");
		ubicacion.setWidth("250px");
		ubicacion.setAncho(450);
		tipoUnidad.setModelo(tiposUnidades);
		ubicacion.setModelo(direcciones);
		ubicacion.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
	}

	private List<CompEncabezado> getCatalogoDireccion() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Estado", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Municipio", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrMunicipio");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Parroquia", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrParroquia");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Sector", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrSector");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Descripcion", 300);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);
		return encabezado;
	}

	public void dibujar() {
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.addComponente("Tipo Unidad Funcional:", tipoUnidad);
		gbGeneral.addComponente("es Sede :", sede);
		gbGeneral.addComponente("Serie :", serie);
		gbGeneral.dibujar(this);
		gbDireccion.addComponente("Direccion:", ubicacion);
		gbDireccion.addComponente("Estado:", estado);
		gbDireccion.addComponente("Municipio :", municipio);
		gbDireccion.addComponente("parroquia :", parroquia);
		gbDireccion.addComponente("Sector :", sector);
		gbDireccion.addComponente("Descripcion :", descripcionDireccion);
		gbDireccion.addComponente("Referencia :", referencia);
		gbDireccion.dibujar(this);
		addBotonera();

	}

	public void cargarValores(UnidadFuncional arg0) throws ExcDatosInvalidos {

		descripcion.setValue(getModelo().getDescripcion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);
		sede.setChecked(getModelo().isSede());
		getBinder().addBinding(sede, "checked", getNombreModelo() + ".sede",
				null, null, "save", null, null, null, null);
		tipoUnidad.setSeleccion(getModelo().getTipo());
		getBinder().addBinding(tipoUnidad, "seleccion",
				getNombreModelo() + ".tipo", null, null, "save", null, null,
				null, null);
		ubicacion.setSeleccion(getModelo().getUbicacion());
		getBinder().addBinding(ubicacion, "seleccion",
				getNombreModelo() + ".ubicacion", null, null, "save", null,
				null, null, null);
		actualizarDireccion();
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void actualizarDireccion() {
		UbicacionDireccion direccion = ubicacion.getSeleccion();
		if (direccion != null) {
			estado.setValue(direccion.getStrEstado());
			municipio.setValue(direccion.getStrMunicipio());
			parroquia.setValue(direccion.getStrParroquia());
			sector.setValue(direccion.getStrSector());
			descripcionDireccion.setValue(direccion.getDescripcion());
			referencia.setValue(direccion.getReferencia());
		}
	}

	public void activarConsulta() {
		descripcion.setDisabled(true);
		serie.setDisabled(true);
		ubicacion.setDisabled(true);
		tipoUnidad.setDisabled(true);
	}

	public void modoEdicion() {
		descripcion.setDisabled(false);
		serie.setDisabled(false);
		ubicacion.setDisabled(false);
		tipoUnidad.setDisabled(false);
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.descripcion = descripcion;
	}

	public List<UbicacionDireccion> getDirecciones() {
		return direcciones;
	}

	public void setDirecciones(List<UbicacionDireccion> direcciones) {
		this.direcciones = direcciones;
	}

	public CompBuscar<UbicacionDireccion> getUbicacion() {
		return ubicacion;
	}

	public Checkbox getSede() {
		return sede;
	}

	public Textbox getSerie() {
		return serie;
	}

}
