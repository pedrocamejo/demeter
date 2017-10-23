package cpc.demeter.vista.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Listhead;

import com.sun.org.apache.xml.internal.security.encryption.AgreementMethod;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.mantenimiento.PlantaDistribuidora;
import cpc.modelo.demeter.mantenimiento.TipoGarantia;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UIPlantaDistribuidora extends CompVentanaBase<PlantaDistribuidora> {

	private CompGrupoDatos gbGeneral;
	private Textbox nombre;
	private Textbox documento;
	private Textbox direccion;
	private Textbox telefonos;
	private Listbox productos;
	private Combobox tipoFabrica; // Pauny O Don Roque

	private Button cargar;
	private Button quitar;
	private Window ventanaaux;
	private Button aceptaraux; // el seleccionar de la ventana auxiliar
	private CompBuscar<Modelo> modelos;
	private List<Modelo> listmodelos;
	private List<TipoGarantia> lisProductos;

	public UIPlantaDistribuidora(String titulo, int ancho, List<Modelo> modelo) {
		// TODO Auto-generated constructor stub
		super(titulo, ancho);
		listmodelos = modelo;
	}

	@Override
	public void cargarValores(PlantaDistribuidora arg0)
			throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		nombre.setValue(getModelo().getNombre());
		documento.setValue(getModelo().getDocumento());
		direccion.setValue(getModelo().getDireccion());
		telefonos.setValue(getModelo().getTelefonos());
		seleccionarFabrica(getModelo().getTipo());
		getBinder().addBinding(nombre, "value", getNombreModelo() + ".nombre",
				null, null, "save", null, null, null, null);
		getBinder().addBinding(documento, "value",
				getNombreModelo() + ".documento", null, null, "save", null,
				null, null, null);
		getBinder().addBinding(direccion, "value",
				getNombreModelo() + ".direccion", null, null, "save", null,
				null, null, null);
		getBinder().addBinding(telefonos, "value",
				getNombreModelo() + ".telefonos", null, null, "save", null,
				null, null, null);

		lisProductos = new ArrayList<TipoGarantia>(getModelo().getProductos());

		// binding para los productos
		productos.setModel(new ListModelArray(lisProductos));

	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Nombre :", nombre);
		gbGeneral.addComponente("RIF. :", documento);
		gbGeneral.addComponente("Dirección ", direccion);
		gbGeneral.addComponente("Telefonos ", telefonos);
		gbGeneral.addComponente("Tipo de Fabrica", tipoFabrica);
		encabezadoProductos();
		gbGeneral.dibujar(this);

		CompGrupoDatos compproductos = new CompGrupoDatos("DETALLE DE PRODUCCION", 2);
		compproductos.setAnchoColumna(0, 50);

		Hbox botones = new Hbox();
		botones.appendChild(cargar);
		botones.appendChild(quitar);
		compproductos.addComponente(botones, productos);
		compproductos.dibujar(this);
		addBotonera();
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		gbGeneral = new CompGrupoDatos("Datos generales", 4);
		nombre = new Textbox();
		nombre.setMaxlength(250);

		documento = new Textbox();
		documento.setMaxlength(250);

		direccion = new Textbox();
		direccion.setRows(3);
		direccion.setWidth("90%");
		direccion.setMaxlength(250);

		telefonos = new Textbox();
		telefonos.setMaxlength(250);

		productos = new Listbox();
		productos.setItemRenderer(new ListitemRenderer() {

			@Override
			public void render(Listitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				arg0.setValue(arg1);
				TipoGarantia g = (TipoGarantia) arg1;
				new Listcell(g.getModelo().getMarca().toString())
						.setParent(arg0);
				new Listcell(g.getModelo().getCodigoModelo()).setParent(arg0);
				new Listcell(g.getModelo().getDescripcionModelo())
						.setParent(arg0);

			}
		});

		cargar = new Button("+");
		quitar = new Button("-");

		modelos = new CompBuscar<Modelo>(cargarEncabezadomodelo(), 2);
		modelos.setAncho(300);
		modelos.setWidth("300px");
		modelos.setModelo(listmodelos);

		aceptaraux = new Button("aceptar");

		cargar.addEventListener(Events.ON_CLICK, getControlador());
		quitar.addEventListener(Events.ON_CLICK, getControlador());
		aceptaraux.addEventListener(Events.ON_CLICK, getControlador());

		// ventana auxiliar que se dibujara con el evento cargar

		ventanaaux = new Window("Seleccionar Modelo", "none", true);
		CompGrupoDatos comventaaux = new CompGrupoDatos("Seleccionar Modelo", 1);
		comventaaux.addComponente(modelos);
		comventaaux.addComponente(aceptaraux);
		comventaaux.dibujar(ventanaaux);

		tipoFabrica = comboFabrica();
		tipoFabrica.addEventListener(Events.ON_SELECT, getControlador());

	}

	private Combobox comboFabrica() {
		// TODO Auto-generated method stub
		Combobox box = new Combobox();
		Comboitem item = new Comboitem();
		item.setLabel("Pauny");
		item.setValue(0);
		item.setImage("/imagenes/LOGOPauny32.jpg");
		item.setParent(box);

		item = new Comboitem();
		item.setLabel("Don Roque");
		item.setValue(1);
		item.setImage("/imagenes/LogoDonRoque32.jpg");
		item.setParent(box);

		return box;
	}

	public void seleccionarFabrica(Integer i) {
		List<Comboitem> items = tipoFabrica.getItems();
		for (Comboitem combo : items) {
			Integer valor = (Integer) combo.getValue();
			if (valor.equals(i)) {
				tipoFabrica.setSelectedItem(combo);
				break;
			}
		}
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		nombre.setDisabled(true);
		documento.setDisabled(true);
		direccion.setDisabled(true);
		telefonos.setDisabled(true);
		cargar.setDisabled(true);
		quitar.setDisabled(true);
		tipoFabrica.setDisabled(true);
	}

	public void modoEdicion() {

		nombre.setDisabled(false);
		documento.setDisabled(false);
		direccion.setDisabled(false);
		telefonos.setDisabled(false);
	}

	public CompGrupoDatos getGbGeneral() {
		return gbGeneral;
	}

	public void setGbGeneral(CompGrupoDatos gbGeneral) {
		this.gbGeneral = gbGeneral;
	} 

	public Textbox getNombre() {
		return nombre;
	}

	public void setNombre(Textbox nombre) {
		this.nombre = nombre;
	}

	public Textbox getDocumento() {
		return documento;
	}

	public void setDocumento(Textbox documento) {
		this.documento = documento;
	}

	public Textbox getDireccion() {
		return direccion;
	}

	public void setDireccion(Textbox direccion) {
		this.direccion = direccion;
	}

	public Textbox getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(Textbox telefonos) {
		this.telefonos = telefonos;
	}

	private void encabezadoProductos() {
		if (productos != null) {
			Listhead titulo = new Listhead();

			Listheader t1 = new Listheader("Marca");
			Listheader t2 = new Listheader("Modelo");
			Listheader t3 = new Listheader("Codigo Modelo");

			titulo.appendChild(t1);
			titulo.appendChild(t2);
			titulo.appendChild(t3);
			productos.appendChild(titulo);
		}
	}

	public List<CompEncabezado> cargarEncabezadomodelo() {
		CompEncabezado titulo;

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		titulo = new CompEncabezado("Marca ", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMarca");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Codigo Modelo", 130);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCodigoModelo");
		encabezado.add(titulo);

		titulo = new CompEncabezado(" Modelo ", 130);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcionModelo");
		encabezado.add(titulo);
		return encabezado;

	}

	public Button getCargar() {
		return cargar;
	}

	public void setCargar(Button cargar) {
		this.cargar = cargar;
	}

	public Button getQuitar() {
		return quitar;
	}

	public void setQuitar(Button quitar) {
		this.quitar = quitar;
	}

	public Window getVentanaaux() {
		return ventanaaux;
	}

	public void setVentanaaux(Window ventanaaux) {

		this.ventanaaux = ventanaaux;
	}

	public Button getAceptaraux() {
		return aceptaraux;
	}

	public List<Modelo> getListmodelos() {
		return listmodelos;
	}

	public void setListmodelos(List<Modelo> listmodelos) {
		this.listmodelos = listmodelos;
	}

	public List<TipoGarantia> getLisProductos() {
		return lisProductos;
	}

	public void setLisProductos(List<TipoGarantia> lisProductos) {
		this.lisProductos = lisProductos;
	}

	public CompBuscar<Modelo> getModelos() {
		return modelos;
	}

	public void setModelos(CompBuscar<Modelo> modelos) {
		this.modelos = modelos;
	}

	public void actualizarproductos() {
		// TODO Auto-generated method stub
		productos.setModel(new ListModelArray(lisProductos));
	}

	public TipoGarantia getDatoSeleccionado() {
		// TODO Auto-generated method stub
		return (TipoGarantia) (productos.getSelectedItem() == null ? null
				: productos.getSelectedItem().getValue());
	}

	public Combobox getTipoFabrica() {
		return tipoFabrica;
	}

	public void setTipoFabrica(Combobox tipoFabrica) {
		this.tipoFabrica = tipoFabrica;
	}

}
