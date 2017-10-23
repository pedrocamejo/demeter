package cpc.demeter.vista;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.basico.Labor;
import cpc.modelo.demeter.basico.Servicio;
import cpc.modelo.demeter.basico.TipoServicio;
import cpc.modelo.demeter.basico.TipoUnidadMedida;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UiServicio extends CompVentanaBase<Servicio> {

	private static final long serialVersionUID = -5739695024627594930L;
	private CompGrupoDatos gbGeneral, gbLabor;
	private Textbox descripcion;
	private CompCombobox<TipoUnidadMedida> tipoUnidad;
	private CompCombobox<TipoUnidadMedida> unidadTrabajo;
	private CompCombobox<TipoUnidadMedida> unidadProduccion;
	private CompCombobox<TipoServicio> tipoServicio;
	private CompLista<Labor> labores;
	private List<TipoUnidadMedida> unidadesMedida;
	private List<TipoUnidadMedida> unidadesMedidaSimples;
	private List<TipoServicio> tiposServicios;
	private Checkbox pases;
	private Checkbox cantidad;
	private Checkbox produccion;
	private Label lproduccion;

	public UiServicio(String titulo, int ancho,
			List<TipoUnidadMedida> unidadesMedida,
			List<TipoUnidadMedida> unidadesMedidaSimples,
			List<TipoServicio> tiposServicios) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.unidadesMedida = unidadesMedida;
		this.tiposServicios = tiposServicios;
		this.unidadesMedidaSimples = unidadesMedidaSimples;
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);
		gbLabor = new CompGrupoDatos("Labores", 1);
		tipoServicio = new CompCombobox<TipoServicio>();
		tipoUnidad = new CompCombobox<TipoUnidadMedida>();

		descripcion = new Textbox();

		labores = new CompLista<Labor>(getEncabezadoLabor());
		pases = new Checkbox("Utiliza pases");
		cantidad = new Checkbox("Utiliza Cantidad");
		produccion = new Checkbox("Genera producción");
		unidadTrabajo = new CompCombobox<TipoUnidadMedida>();
		unidadProduccion = new CompCombobox<TipoUnidadMedida>();
		lproduccion = new Label("Unidad Medida produccion:");

		tipoServicio.setWidth("380px");
		tipoUnidad.setWidth("380px");
		unidadTrabajo.setWidth("380px");
		unidadProduccion.setWidth("380px");
		tipoUnidad.setWidth("380px");
		descripcion.setWidth("390px");

		tipoServicio.setModelo(tiposServicios);
		tipoUnidad.setModelo(unidadesMedida);
		unidadProduccion.setModelo(unidadesMedidaSimples);
		unidadTrabajo.setModelo(unidadesMedidaSimples);
		tipoUnidad.setModelo(unidadesMedida);

		tipoServicio.setReadonly(true);
		unidadTrabajo.setReadonly(true);
		unidadProduccion.setReadonly(true);
		tipoUnidad.setReadonly(true);
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 150);
		gbGeneral.addComponente("Tipo de Servicio:", tipoServicio);
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.addComponente("Tipo Unidad Medida:", tipoUnidad);
		gbGeneral.addComponente("Unidad Medida Trabajo:", unidadTrabajo);
		gbGeneral.addComponenteMultiples("", 120, pases, cantidad, produccion);
		gbGeneral.addComponente(lproduccion);
		gbGeneral.addComponente(unidadProduccion);
		gbGeneral.dibujar(this);
		gbLabor.addComponente(labores);
		gbLabor.dibujar(this);
		addBotonera();
		tipoUnidad.addEventListener(Events.ON_SELECTION, getControlador());
		produccion.addEventListener(Events.ON_CHECK, getControlador());
	}

	public void cargarValores(Servicio servicio) throws ExcDatosInvalidos {
		tipoServicio.setSeleccion(getModelo().getTipoServicio());
		getBinder().addBinding(tipoServicio, "seleccion",
				getNombreModelo() + ".tipoServicio", null, null, "save", null,
				null, null, null);
		tipoUnidad.setSeleccion(getModelo().getTipoUnidadMedida());
		getBinder().addBinding(tipoUnidad, "seleccion",
				getNombreModelo() + ".tipoUnidadMedida", null, null, "save",
				null, null, null, null);
		descripcion.setValue(getModelo().getDescripcion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);
		unidadTrabajo.setSeleccion(getModelo().getTipoUnidadTrabajo());
		getBinder().addBinding(unidadTrabajo, "seleccion",
				getNombreModelo() + ".tipoUnidadTrabajo", null, null, "save",
				null, null, null, null);
		unidadProduccion.setSeleccion(getModelo().getTipoUnidadProduccion());
		getBinder().addBinding(unidadProduccion, "seleccion",
				getNombreModelo() + ".tipoUnidadProduccion", null, null,
				"save", null, null, null, null);
		if (getModelo().getManejaPases() != null)
			pases.setChecked(getModelo().getManejaPases());
		getBinder().addBinding(pases, "checked",
				getNombreModelo() + ".manejaPases", null, null, "save", null,
				null, null, null);
		if (getModelo().getProduccion() != null) {
			produccion.setChecked(getModelo().getProduccion());
			visibilidadProduccion(getModelo().getProduccion());
		} else
			visibilidadProduccion(false);
		getBinder().addBinding(produccion, "checked",
				getNombreModelo() + ".produccion", null, null, "save", null,
				null, null, null);
		if (getModelo().getManejaCantidades() != null)
			cantidad.setChecked(getModelo().getManejaCantidades());
		getBinder().addBinding(cantidad, "checked",
				getNombreModelo() + ".manejaCantidades", null, null, "save",
				null, null, null, null);
		if (getModelo().getLabores() != null)
			labores.setModelo(getModelo().getLabores());
	}

	private List<CompEncabezado> getEncabezadoLabor() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Descripcion", 250);
		titulo.setMetodoBinder("getDescripcion");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Uni Labor", 125);
		titulo.setMetodoBinder("getStrUnidadMedidagestion");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Uni Cobro", 125);
		titulo.setMetodoBinder("getStrUnidadMedidaCobro");
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
		tipoServicio.setDisabled(true);
		descripcion.setDisabled(true);
		tipoUnidad.setDisabled(true);
		unidadProduccion.setDisabled(true);
		unidadTrabajo.setDisabled(true);
		cantidad.setDisabled(true);
		pases.setDisabled(true);
		produccion.setDisabled(true);
	}

	public void modoEdicion() {
		tipoServicio.setDisabled(false);
		descripcion.setDisabled(false);
		tipoUnidad.setDisabled(false);
		unidadProduccion.setDisabled(false);
		unidadTrabajo.setDisabled(false);
		cantidad.setDisabled(false);
		pases.setDisabled(false);
		produccion.setDisabled(false);
	}

	public void visibilidadProduccion(Boolean valor) {
		lproduccion.setVisible(valor);
		unidadProduccion.setVisible(valor);
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public CompCombobox<TipoUnidadMedida> getTipoUnidad() {
		return tipoUnidad;
	}

	public CompCombobox<TipoServicio> getTipoServicio() {
		return tipoServicio;
	}

	public Checkbox getPases() {
		return pases;
	}

	public Checkbox getCantidad() {
		return cantidad;
	}

	public CompCombobox<TipoUnidadMedida> getUnidadTrabajo() {
		return unidadTrabajo;
	}

	public CompCombobox<TipoUnidadMedida> getUnidadProduccion() {
		return unidadProduccion;
	}

	public Checkbox getProduccion() {
		return produccion;
	}
}
