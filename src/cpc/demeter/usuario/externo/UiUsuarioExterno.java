package cpc.demeter.usuario.externo;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cpc.modelo.demeter.mantenimiento.UsuarioMantenimiento;

import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;

public class UiUsuarioExterno extends Window {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Datos del usuario :D
	private Textbox cedula; // nombre o razon social
	private Textbox contrasena;
	private CompGrupoDatos gbglobal; // = new
										// CompGrupoDatos("Elaborado por ente ",4);
	private Button iniciar;
	private Button limpiar;
	private Combobox tipodocumento;
	private EventListener controlador;

	public UiUsuarioExterno() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void inicializar() {
		// TODO Auto-generated method stub
		gbglobal = new CompGrupoDatos("Inicio de Session :", 2);
		cedula = new Textbox();
		contrasena = new Textbox();
		tipodocumento = new Combobox();

		iniciar = new Button("Iniciar");
		limpiar = new Button("Limpiar");
		iniciar.addEventListener(Events.ON_CLICK, getControlador());
		limpiar.addEventListener(Events.ON_CLICK, getControlador());
		contrasena.addEventListener(Events.ON_OK, getControlador());
		cedula.addEventListener(Events.ON_CLICK, getControlador());
	}

	public void dibujar() {

		gbglobal.setAnchoColumna(0, 100);
		gbglobal.setAnchoColumna(1, 200);

		Comboitem combitem = new Comboitem("V-");
		combitem.setValue("V-");
		Comboitem combitem2 = new Comboitem("E-");
		combitem2.setValue("E-");
		Comboitem combitem3 = new Comboitem("J-");
		combitem3.setValue("J-");
		Comboitem combitem4 = new Comboitem("G-");
		combitem4.setValue("G-");

		tipodocumento.appendChild(combitem);
		tipodocumento.appendChild(combitem2);
		tipodocumento.appendChild(combitem3);
		tipodocumento.appendChild(combitem4);
		tipodocumento.setWidth("20px");

		limpiar.setWidth("30px");

		Hbox divcedula = new Hbox();
		divcedula.setWidth("99%");

		cedula.setWidth("80%");
		cedula.setMaxlength(250);

		divcedula.appendChild(tipodocumento);
		divcedula.appendChild(cedula);

		contrasena.setWidth("60%");
		contrasena.setType("password");
		contrasena.setMaxlength(250);

		Div div2 = new Div();
		div2.setWidth("99%");
		div2.setAlign("Center");
		div2.appendChild(limpiar);
		div2.appendChild(iniciar);

		gbglobal.addComponente("Cedula o Rif:", divcedula);
		gbglobal.addComponente("Contraseña  :", contrasena);
		gbglobal.addComponente(new Div(), div2);
		gbglobal.dibujar(this);
	}

	public void cargarValores(UsuarioMantenimiento dato)
			throws ExcDatosInvalidos {
		// TODO Auto-generated method stub

	}

	public Button getIniciar() {
		return iniciar;
	}

	public void setIniciar(Button iniciar) {
		this.iniciar = iniciar;
	}

	public Button getLimpiar() {
		return limpiar;
	}

	public void setLimpiar(Button limpiar) {
		this.limpiar = limpiar;
	}

	public void limpiar() {
		contrasena.setText("");
		cedula.setText("");

	}

	public Textbox getCedula() {
		return cedula;
	}

	public void setCedula(Textbox cedula) {
		this.cedula = cedula;
	}

	public Textbox getContrasena() {
		return contrasena;
	}

	public void setContrasena(Textbox contrasena) {
		this.contrasena = contrasena;
	}

	public Combobox getTipodocumento() {
		return tipodocumento;
	}

	public void setTipodocumento(Combobox tipodocumento) {
		this.tipodocumento = tipodocumento;
	}

	public EventListener getControlador() {
		return controlador;
	}

	public void setControlador(EventListener controlador) {
		this.controlador = controlador;
	}

}
