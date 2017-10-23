package cpc.demeter.vista;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.basico.ClaseUnidadArrime;
import cpc.modelo.demeter.basico.TipoUnidadArrime;
import cpc.modelo.demeter.basico.UnidadArrime;
import cpc.modelo.ministerio.dimension.UbicacionDireccion;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ui.ComponentesAutomaticos;
import cpc.zk.componente.ventanas.CompGrupoBusqueda;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;

public class UiUnidadArrime extends CompVentanaBase<UnidadArrime> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8201499264180717495L;
	private CompGrupoDatos gbGeneral;
	private Textbox descripcion;
	private CompCombobox<TipoUnidadArrime> tipoUnidad;
	private CompCombobox<ClaseUnidadArrime> claseUnidad;
	private Checkbox publico;
	private Doublebox capacidadOperativa;
	private CompGrupoBusqueda<UbicacionDireccion> ubicacionFisica;

	private List<TipoUnidadArrime> tiposSilos;
	private List<UbicacionDireccion> direcciones;

	public UiUnidadArrime(String titulo, int ancho,
			List<TipoUnidadArrime> tipos, List<UbicacionDireccion> direcciones)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.tiposSilos = tipos;
		this.direcciones = direcciones;
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);
		tipoUnidad = new CompCombobox<TipoUnidadArrime>();
		claseUnidad = new CompCombobox<ClaseUnidadArrime>();
		capacidadOperativa = new Doublebox();
		descripcion = new Textbox();
		publico = new Checkbox("Publico");
		try {
			ubicacionFisica = new CompGrupoBusqueda<UbicacionDireccion>(
					"Ubicacion Fisica", getEncabezadoSector(), getModelo()
							.getDireccion(), ComponentesAutomaticos.TEXTBOX);
			ubicacionFisica.setNuevo(new UbicacionDireccion());
			ubicacionFisica.setModeloCatalogo(direcciones);
			ubicacionFisica.setAnchoValores(150);
			ubicacionFisica.setAnchoCatalogo(500);
			ubicacionFisica.setVisibleEditar(false);
			ubicacionFisica.setVisibleCrear(false);
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tipoUnidad.setWidth("380px");
		tipoUnidad.setWidth("380px");
		descripcion.setWidth("390px");

		tipoUnidad.setModelo(tiposSilos);
		tipoUnidad.setReadonly(true);
		claseUnidad.setReadonly(true);
		tipoUnidad.addEventListener(Events.ON_SELECTION, getControlador());
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 150);
		gbGeneral.addComponente("Tipo:", tipoUnidad);
		gbGeneral.addComponente("Clase:", claseUnidad);
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.addComponenteMultiples("Capacidad Operativa:", 120,
				capacidadOperativa, new Label("TON"));
		gbGeneral.addComponente("tipo Institucion :", publico);
		gbGeneral.dibujar(this);
		ubicacionFisica.dibujar();
		this.appendChild(ubicacionFisica);
		addBotonera();
	}

	public void cargarValores(UnidadArrime servicio) throws ExcDatosInvalidos {
		tipoUnidad.setSeleccion(getModelo().getTipo());
		getBinder().addBinding(tipoUnidad, "seleccion",
				getNombreModelo() + ".tipo", null, null, "save", null, null,
				null, null);
		claseUnidad.setSeleccion(getModelo().getClase());
		getBinder().addBinding(claseUnidad, "seleccion",
				getNombreModelo() + ".clase", null, null, "save", null, null,
				null, null);
		descripcion.setValue(getModelo().getDescripcion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);
		capacidadOperativa.setValue(getModelo().getCapacidadOperativa());
		getBinder().addBinding(capacidadOperativa, "value",
				getNombreModelo() + ".capacidadOperativa", null, null, "save",
				null, null, null, null);
		if (getModelo().getPublico() != null)
			publico.setChecked(!getModelo().getPublico());
		getBinder().addBinding(publico, "checked",
				getNombreModelo() + ".publico", null, null, "save", null, null,
				null, null);

		getBinder().addBinding(ubicacionFisica, "modelo",
				getNombreModelo() + ".direccion", null, null, "save", null,
				null, null, null);
		try {
			ubicacionFisica.addListener(getControlador());
			ubicacionFisica.actualizarValores();
		} catch (NullPointerException e) {
		} catch (ExcAccesoInvalido e) {
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<CompEncabezado> getEncabezadoSector() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Documento", 100);
		titulo.setMetodoBinder("getDocumentoLegal");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Descripcion", 250);
		titulo.setMetodoBinder("getDescripcion");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Sector", 150);
		titulo.setMetodoBinder("getStrSector");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Parroquia", 150);
		titulo.setMetodoBinder("getStrParroquia");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Municipio", 150);
		titulo.setMetodoBinder("getStrMunicipio");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 150);
		titulo.setMetodoBinder("getStrEstado");
		titulo.setOrdenable(true);
		encabezado.add(titulo);
		return encabezado;
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		descripcion.setDisabled(true);
		// ubicacionFisica.setDisabled(true);
		publico.setDisabled(true);
		tipoUnidad.setDisabled(true);
		capacidadOperativa.setDisabled(true);
		ubicacionFisica.setModoEdicion(false);
	}

	public void modoEdicion() {
		descripcion.setDisabled(false);
		// ubicacionFisica.setDisabled(false);
		publico.setDisabled(false);
		tipoUnidad.setDisabled(false);
		ubicacionFisica.setModoEdicion(true);
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public CompCombobox<TipoUnidadArrime> getTipoUnidad() {
		return tipoUnidad;
	}

	public CompCombobox<ClaseUnidadArrime> getClaseUnidad() {
		return claseUnidad;
	}

	public void refrescarClaseUnidad(List<ClaseUnidadArrime> clases) {
		claseUnidad.setModelo(clases);
	}

	public Doublebox getCapacidadOperativa() {
		return capacidadOperativa;
	}

	public Checkbox getPublico() {
		return publico;
	}

	public CompGrupoBusqueda<UbicacionDireccion> getUbicacionFisica() {
		return ubicacionFisica;
	}

}
