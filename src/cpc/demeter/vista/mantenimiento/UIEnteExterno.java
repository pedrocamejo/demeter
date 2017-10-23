package cpc.demeter.vista.mantenimiento;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;

import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.mantenimiento.EnteExterno;
import cpc.modelo.demeter.mantenimiento.UsuarioMantenimiento;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIEnteExterno extends CompVentanaBase<EnteExterno> {

	private static final long serialVersionUID = 1L;
	private CompGrupoDatos gbGeneral;
	private Intbox id;
	private Textbox descripcion;

	private Textbox direccion;
	private Textbox telefono1;
	private Textbox telefono2;

	private Div contenedor;
	private Hbox divisor;
	private Listbox listausarios; // lista de usaurios :D

	private Vbox grupoBoton;
	private Button agregar;
	// private Button eliminar;
	private Button modificar;
	private Button visualizar;

	public Listbox getListausarios() {

		return listausarios;

	}

	public void setListausarios(Listbox listausarios) {
		this.listausarios = listausarios;
	}

	public UsuarioMantenimiento seleccion() {

		if (listausarios.getSelectedCount() == 0) {
			return null;
		}
		UsuarioMantenimiento usuario = (UsuarioMantenimiento) listausarios
				.getSelectedItem().getValue();
		return usuario;
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public UIEnteExterno() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param titulo
	 * @param ancho
	 */
	public UIEnteExterno(String titulo, int ancho) {
		super(titulo, ancho);

		// TODO Auto-generated constructor stub
	}

	/**
	 * @param titulo
	 */
	public UIEnteExterno(String titulo) {
		super(titulo);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		id = new Intbox();
		id.setDisabled(true);
		descripcion = new Textbox();
		gbGeneral = new CompGrupoDatos();
		contenedor = new Div();
		divisor = new Hbox();
		grupoBoton = new Vbox();
		listausarios = new Listbox();

		agregar = new Button();
		modificar = new Button();
		visualizar = new Button();

		agregar.addEventListener(Events.ON_CLICK, getControlador());
		modificar.addEventListener(Events.ON_CLICK, getControlador());
		visualizar.addEventListener(Events.ON_CLICK, getControlador());

		agregar.setImage("/imagenes/agregar.png");
		agregar.setTooltip("Agregar Personal ");

		visualizar.setImage("/imagenes/lupa.png");
		visualizar.setTooltip("Visualizar ");

		modificar.setImage("/imagenes/modificar.png");
		modificar.setTooltip(" Modificar Personal ");

		direccion = new Textbox();
		direccion.setMaxlength(250);
		direccion.setWidth("99%");
		telefono1 = new Textbox();
		telefono1.setMaxlength(15);
		telefono1.setWidth("99%");
		telefono2 = new Textbox();
		telefono2.setMaxlength(15);
		telefono2.setWidth("99%");

	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente(" ID :", id);
		descripcion.setWidth("100%");
		gbGeneral.addComponente("Descripción :", descripcion);
		gbGeneral.addComponente("Dirección :", direccion);
		gbGeneral.addComponente("Telefono 1:", telefono1);
		gbGeneral.addComponente("Telefono 2 :", telefono2);

		gbGeneral.dibujar(this);

		divisor.setHeight("100%");
		divisor.setWidth("100%");

		visualizar.setWidth("25px");
		visualizar.setHeight("20px");

		agregar.setWidth("25px");
		agregar.setHeight("20px");

		modificar.setWidth("25px");
		modificar.setHeight("20px");

		grupoBoton.appendChild(visualizar);
		grupoBoton.appendChild(agregar);
		grupoBoton.appendChild(modificar);

		contenedor.appendChild(divisor);
		divisor.appendChild(grupoBoton);
		this.appendChild(divisor);
		ActualizarLista();
		addBotonera();
		id.setValue(getModelo().getId());
		descripcion.setValue(getModelo().getDescripcion());

	}

	public void ActualizarLista() {

		Map mapa = new HashMap();
		mapa.put("lista", getModelo().getUsuarios());
		if (listausarios != null) {
			listausarios.detach();
		}
//		listausarios = (Listbox) Executions.createComponents("/ListaUsuarios.zul", null, mapa);
		listausarios.detach();
		divisor.appendChild(listausarios);
	}

	@Override
	public void cargarValores(EnteExterno dato) throws ExcDatosInvalidos {

		id.setValue(getModelo().getId());
		descripcion.setText(getModelo().getDescripcion());
		direccion.setText(getModelo().getDireccion());
		telefono1.setText(getModelo().getTelefono1());
		telefono2.setText(getModelo().getTelefono2());

		getBinder().addBinding(id, "value", getNombreModelo() + ".id", null,
				null, "save", null, null, null, null);
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);
		getBinder().addBinding(direccion, "value",
				getNombreModelo() + ".direccion", null, null, "save", null,
				null, null, null);
		getBinder().addBinding(telefono1, "value",
				getNombreModelo() + ".telefono1", null, null, "save", null,
				null, null, null);
		getBinder().addBinding(telefono2, "value",
				getNombreModelo() + ".telefono2", null, null, "save", null,
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
		direccion.setDisabled(true);
		telefono1.setDisabled(true);
		telefono2.setDisabled(true);
	}

	public void modoEdicion() {
		descripcion.setDisabled(false);
		direccion.setDisabled(false);
		telefono1.setDisabled(false);
		telefono2.setDisabled(false);
	}

	public Button getAgregar() {
		return agregar;
	}

	public void setAgregar(Button agregar) {
		this.agregar = agregar;
	}

	public Button getModificar() {
		return modificar;
	}

	public void setModificar(Button modificar) {
		this.modificar = modificar;
	}

	public Button getVisualizar() {
		return visualizar;
	}

	public void setVisualizar(Button visualizar) {
		this.visualizar = visualizar;
	}

	public Textbox getDireccion() {
		return direccion;
	}

	public Textbox getTelefono1() {
		return telefono1;
	}

	public Textbox getTelefono2() {
		return telefono2;
	}

	public void setDireccion(Textbox direccion) {
		this.direccion = direccion;
	}

	public void setTelefono1(Textbox telefono1) {
		this.telefono1 = telefono1;
	}

	public void setTelefono2(Textbox telefono2) {
		this.telefono2 = telefono2;
	}

}
