package cpc.demeter.vista.mantenimiento;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.mantenimiento.ContEnteExterno;
import cpc.modelo.demeter.mantenimiento.UsuarioMantenimiento;
import cpc.zk.componente.ventanas.CompGrupoDatos;

public class UIUsuarioMantenimiento extends Window {

	private static final long serialVersionUID = 1L;
	private EventListener controlador;
	private Textbox cedula;
	private Textbox nombres;
	private Textbox apellidos;
	private Textbox contrasena;
	private Textbox contrasena1;

	private Button Aceptar;
	private Button Cancelar;
	private CompGrupoDatos grupo;
	private Boolean estado;
	private Button cambiar; // cambiar contraseña :D
	private Integer modo; // 0 Ver que hay de nuevo :D viejo jeje consulta Ver
							// :D ps
							// 1 agregar Campos Limpios :D
							// 2 Modificar Campos llenos :D menos el de
							// contraseña que mostrar si es nuevo o si exige
							// cambiar de contrasena

	private Label label1;
	private Label label2;
	private Boolean cambiocontrasena = false;

	private UsuarioMantenimiento usuario;

	/**
	 * 
	 */
	public UIUsuarioMantenimiento() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param title
	 * @param border
	 * @param closable
	 */
	public UIUsuarioMantenimiento(String title, String border,
			boolean closable, UsuarioMantenimiento usuario, Integer modo,
			ContEnteExterno ente) {
		super(title, border, closable);

		this.controlador = ente;
		this.usuario = usuario;
		this.modo = modo;
		this.setWidth("400px");
		inicializar();
		dibujar();
		// TODO Auto-generated constructor stub
	}

	public Button getCambiar() {
		return cambiar;
	}

	public void setCambiar(Button cambiar) {
		this.cambiar = cambiar;
	}

	public void inicializar() {
		// TODO Auto-generated method stub
		cedula = new Textbox();
		nombres = new Textbox();
		apellidos = new Textbox();
		contrasena = new Textbox();
		contrasena1 = new Textbox();
		label1 = new Label("Contraseña ");
		label2 = new Label("Cambiar Contraseña ");
		cambiar = new Button("Cambiar Contraseña ");
		Aceptar = new Button("Aceptar");
		Cancelar = new Button("Cancelar");

		Aceptar.addEventListener(Events.ON_CLICK, controlador);
		Cancelar.addEventListener(Events.ON_CLICK, controlador);
		cambiar.addEventListener(Events.ON_CLICK, controlador);

	}

	public EventListener getControlador() {
		return controlador;
	}

	public void setControlador(EventListener controlador) {
		this.controlador = controlador;
	}

	public Textbox getCedula() {
		return cedula;
	}

	public void setCedula(Textbox cedula) {
		this.cedula = cedula;
	}

	public Textbox getNombres() {
		return nombres;
	}

	public void setNombres(Textbox nombres) {
		this.nombres = nombres;
	}

	public Textbox getApellidos() {
		return apellidos;
	}

	public void setApellidos(Textbox apellidos) {
		this.apellidos = apellidos;
	}

	public Textbox getContrasena() {
		return contrasena;
	}

	public void setContrasena(Textbox contrasena) {
		this.contrasena = contrasena;
	}

	public Textbox getContrasena1() {
		return contrasena1;
	}

	public void setContrasena1(Textbox contrasena1) {
		this.contrasena1 = contrasena1;
	}

	public CompGrupoDatos getGrupo() {
		return grupo;
	}

	public void setGrupo(CompGrupoDatos grupo) {
		this.grupo = grupo;
	}

	public Boolean getEstado() {
		return estado;
	}

	public void setEstado(Boolean estado) {
		this.estado = estado;
	}

	public Integer getModo() {
		return modo;
	}

	public void setModo(Integer modo) {
		this.modo = modo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Button getAceptar() {
		return Aceptar;
	}

	public void setAceptar(Button aceptar) {
		Aceptar = aceptar;
	}

	public Button getCancelar() {
		return Cancelar;
	}

	public void setCancelar(Button cancelar) {
		Cancelar = cancelar;
	}

	public void dibujar() {
		// TODO Auto-generated method stub
		cedula.setConstraint("no empty,/[V|E]-.+[0-9]+/:Cedula Invalida V-0000");
		cedula.setMaxlength(20);

		nombres.setConstraint("no empty");
		nombres.setMaxlength(250);

		apellidos.setConstraint("no empty");
		apellidos.setMaxlength(250);

		contrasena.setConstraint("no empty");
		contrasena.setType("password");
		contrasena.setMaxlength(35);

		contrasena1.setConstraint("no empty");
		contrasena1.setType("password");
		contrasena1.setMaxlength(35);

		grupo = new CompGrupoDatos("Usuarios Mantenimiento", 2);
		grupo.setAnchoColumna(0, 100);

		grupo.addComponente("Cedula", cedula);
		grupo.addComponente("Nombres", nombres);
		grupo.addComponente("Apellidos", apellidos);
		grupo.addComponente(label1, contrasena);
		grupo.addComponente(label2, contrasena1);

		if (modo != 1) // nuevo
		{
			// llenado de los campos :D
			label1.setVisible(false);
			contrasena.setVisible(false);
			label2.setVisible(false);
			contrasena1.setVisible(false);
			grupo.addComponente(" ", cambiar);
			cedula.setValue(usuario.getCedula());
			cedula.setDisabled(true);
			nombres.setValue(usuario.getNombres());
			apellidos.setValue(usuario.getApellidos());
			contrasena.setValue(usuario.getContrasena());
			contrasena1.setValue(usuario.getContrasena());

		}

		Div botones = new Div();
		botones.appendChild(Aceptar);
		botones.appendChild(Cancelar);
		grupo.addComponente("", botones);
		grupo.dibujar(this);
		Satear();

	}

	public void cambiarcontrasena() {
		label1.setVisible(true);
		contrasena.setVisible(true);
		label2.setVisible(true);
		contrasena1.setVisible(true);
		cambiocontrasena = true;

	}

	public Boolean getCambiocontrasena() {
		return cambiocontrasena;
	}

	public void setCambiocontrasena(Boolean cambiocontrasena) {
		this.cambiocontrasena = cambiocontrasena;
	}

	public void Satear() {

	}

	public String getNombreModelo() {
		String cadena = usuario.getClass().getName().toLowerCase();
		return cadena.substring(cadena.lastIndexOf(".") + 1);
	}

	public UsuarioMantenimiento getUsuario() {
		usuario.setCedula(cedula.getText().toUpperCase().trim());
		usuario.setNombres(nombres.getText());
		usuario.setApellidos(apellidos.getText());
		return usuario;
	}

	public void setUsuario(UsuarioMantenimiento usuario) {
		this.usuario = usuario;
	}

}
