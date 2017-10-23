package cpc.demeter.vista;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.controlador.ContLabor;
import cpc.modelo.demeter.administrativo.Impuesto;
import cpc.modelo.demeter.basico.Labor;
import cpc.modelo.demeter.basico.Servicio;
import cpc.modelo.demeter.basico.UnidadMedida;
import cpc.modelo.demeter.basico.UsoPreciosProducto;
import cpc.modelo.ministerio.basico.TipoProductor;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompListaMultiple;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;

import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class UILabor extends
		CompVentanaMaestroDetalle<Labor, UsoPreciosProducto> {

	private static final long serialVersionUID = 6312240548253365129L;
	private List<TipoProductor> tiposProductores;
	private List<UnidadMedida> unidades;
	private List<Servicio> servicios;
	private List<Impuesto> impuestos;

	private CompCombobox<UnidadMedida> unidadGestion;
	private CompCombobox<UnidadMedida> unidadCobro;
	private CompCombobox<Impuesto> impuesto;
	private CompCombobox<Servicio> servicio;
	private Checkbox activo;
	private Textbox descripcion;

	// private AppDemeter app;

	public UILabor(String titulo, int ancho,
			List<TipoProductor> tiposProductores, List<Servicio> servicios,
			List<Impuesto> impuestos, List<UnidadMedida> unidades)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.tiposProductores = tiposProductores;
		this.servicios = servicios;
		this.impuestos = impuestos;
		this.unidades = unidades;
		// this.app = app;
		inicializar();
	}

	public void inicializar() {
		setGbEncabezado(1, "");
		setGbDetalle("Forma de pago");
		descripcion = new Textbox();
		servicio = new CompCombobox<Servicio>();
		unidadCobro = new CompCombobox<UnidadMedida>();
		unidadGestion = new CompCombobox<UnidadMedida>();
		impuesto = new CompCombobox<Impuesto>();
		activo = new Checkbox();
		unidadCobro.setAutocomplete(false);
		unidadGestion.setAutocomplete(false);
		servicio.setAutocomplete(false);
		impuesto.setAutocomplete(false);
		activo.setDisabled(true);
		servicio.setModelo(servicios);
		impuesto.setModelo(impuestos);
		unidadCobro.setModelo(unidades);

		servicio.setReadonly(true);
		unidadCobro.setReadonly(true);
		unidadGestion.setReadonly(true);
		impuesto.setReadonly(true);

	}

	public void dibujar() {
		setGbEncabezado(2, "Labor");
		descripcion.setWidth("350px");
		servicio.setWidth("350px");
		servicio.setWidth("350px");
		unidadGestion.setWidth("350px");
		unidadCobro.setWidth("350px");
		getGbEncabezado().addComponente("Descripción :", descripcion);
		getGbEncabezado().addComponente("Servicio :", servicio);
		getGbEncabezado().addComponente("Unidad Gestion:", unidadGestion);
		getGbEncabezado().addComponente("Unidad Cobro:", unidadCobro);
		getGbEncabezado().addComponente("Impuesto Asociado:", impuesto);
		getGbEncabezado().addComponente("Activo", activo);
		setDetalles(cargarLista(), getModelo().getListaPrecios(),
				encabezadosPrimario());
		getDetalle().setNuevo(new UsoPreciosProducto());
		dibujarEstructura();

		servicio.addEventListener(Events.ON_SELECTION, getControlador());
		servicio.addEventListener(Events.ON_SELECT, getControlador());
		impuesto.addEventListener(Events.ON_SELECTION, getControlador());
	}

	public void cargarValores(Labor labor) throws ExcDatosInvalidos {
		descripcion.setValue(getModelo().getDescripcion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);
		servicio.setSeleccion(getModelo().getServicio());
		getBinder().addBinding(servicio, "seleccion",
				getNombreModelo() + ".servicio", null, null, "save", null,
				null, null, null);
		impuesto.setSeleccion(getModelo().getImpuesto());
		getBinder().addBinding(impuesto, "seleccion",
				getNombreModelo() + ".impuesto", null, null, "save", null,
				null, null, null);
		getBinder().addBinding(unidadGestion, "seleccion",
				getNombreModelo() + ".medidaGestion", null, null, "save", null,
				null, null, null);
		getBinder().addBinding(unidadCobro, "seleccion",
				getNombreModelo() + ".medidaCobro", null, null, "save", null,
				null, null, null);
		activo.setChecked(getModelo().getActivo());

		try {
			ContLabor control = (ContLabor) getControlador();
			control.actualizarUnidadGestion();
			unidadGestion.setSeleccion(getModelo().getMedidaGestion());
			// control.actualizarUnidadCobro();
			unidadCobro.setSeleccion(getModelo().getMedidaCobro());

		} catch (NullPointerException e) {
			// TODO: handle exception
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private List<CompEncabezado> encabezadosPrimario() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Productores", 200);
		titulo.setMetodoComponente("modeloSelect");
		titulo.setMetodoBinder("getTiposProductores");
		titulo.setMetodoModelo(".tiposProductores");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Base", 100);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getPrecio");
		titulo.setMetodoModelo(".precio");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Impuesto", 100);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getIva");
		titulo.setMetodoModelo(".iva");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Neto", 100);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getTotal");
		titulo.setMetodoModelo(".total");
		encabezado.add(titulo);

		return encabezado;
	}

	private ArrayList<Component> cargarLista() {
		ArrayList<Component> lista = new ArrayList<Component>();

		CompListaMultiple tipoProductor = new CompListaMultiple("Tipos");
		Doublebox bruto = new Doublebox();
		Doublebox impuesto = new Doublebox();
		Doublebox neto = new Doublebox();

		tipoProductor.setWidth("190px");
		tipoProductor.setRows(3);
		bruto.setWidth("90px");
		impuesto.setWidth("90px");
		neto.setWidth("90px");
		impuesto.setDisabled(true);
		neto.setDisabled(true);

		tipoProductor.setModelo(tiposProductores);
		bruto.addEventListener(Events.ON_CHANGE, getControlador());

		lista.add(tipoProductor);
		lista.add(bruto);
		lista.add(impuesto);
		lista.add(neto);

		return lista;
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		servicio.setDisabled(true);
		descripcion.setDisabled(true);
		unidadCobro.setDisabled(true);
		unidadGestion.setDisabled(true);
		impuesto.setDisabled(true);
		desactivarDetalle();
	}

	public void modoEdicion() {
		servicio.setDisabled(false);
		descripcion.setDisabled(false);
		unidadCobro.setDisabled(false);
		unidadGestion.setDisabled(false);
		impuesto.setDisabled(false);
	}

	public void refrescarUnidadCobro(List<UnidadMedida> unidades) {
		unidadCobro.setModelo(unidades);
	}

	public void refrescarUnidadGestion(List<UnidadMedida> unidades) {
		unidadGestion.setModelo(unidades);
	}

	public CompCombobox<UnidadMedida> getUnidadGestion() {
		return unidadGestion;
	}

	public CompCombobox<UnidadMedida> getUnidadCobro() {
		return unidadCobro;
	}

	public CompCombobox<Impuesto> getImpuesto() {
		return impuesto;
	}

	public CompCombobox<Servicio> getServicio() {
		return servicio;
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public Checkbox getActivo() {
		return activo;
	}

}