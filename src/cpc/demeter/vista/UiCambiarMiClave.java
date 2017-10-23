package cpc.demeter.vista;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import cpc.ares.modelo.Usuario;
import cpc.ares.persistencia.PerUsuario;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompGrupoDatos;

public class UiCambiarMiClave extends Window implements EventListener {

	private static final long serialVersionUID = 8740789284517423186L;

	private Usuario usuario;
	private IZkAplicacion app;
	private Textbox txtNuevaClave, txtReNuevaClave;
	private Button aceptar, cancelar;

	public UiCambiarMiClave(IZkAplicacion app, Usuario usuario) {
		this.usuario = usuario;
		this.app = app;

		CompGrupoDatos grp = new CompGrupoDatos(2);
		grp.addComponente("Usuario", new Label(usuario.getNombreIdentidad()));
		grp.addComponente("Nombre", new Label(usuario.getNombre()));
		grp.addComponente("Apellido", new Label(usuario.getApellido()));
		aceptar = new Button("Aceptar");
		cancelar = new Button("Cancelar");
		txtNuevaClave = new Textbox();
		txtReNuevaClave = new Textbox();

		txtNuevaClave.setType("password");
		txtReNuevaClave.setType("password");

		txtNuevaClave.setId("txtNuevaClave");
		txtReNuevaClave.setId("txtReNuevaClave");

		grp.addComponente("Nueva Contraseña", txtNuevaClave);
		grp.addComponente("Nueva Contraseña repetida", txtReNuevaClave);
		grp.dibujar(this);
		this.appendChild(aceptar);
		this.appendChild(cancelar);
		aceptar.addEventListener(Events.ON_CLICK, this);
		aceptar.addEventListener(Events.ON_OK, this);
		cancelar.addEventListener(Events.ON_CLICK, this);
		cancelar.addEventListener(Events.ON_CANCEL, this);
		try {
			this.setMode("modal");
			this.setTitle("Cambiar Mi Contraseña");
			this.setClosable(false);
			this.setBorder("normal");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private void validar() {
		if (txtNuevaClave.getValue().length() < 6)
			throw new WrongValueException(txtNuevaClave,
					"Clave muy Corta, longitud minima de 6 caracteres");
		if (txtReNuevaClave.getValue().length() < 6)
			throw new WrongValueException(txtReNuevaClave,
					"Clave muy Corta, longitud minima de 6 caracteres");
		if (!txtNuevaClave.getValue().matches("[a-z]+[0-9]+[0-9a-z]*"))
			throw new WrongValueException(
					txtNuevaClave,
					"Debe comenzar con un caracter, tener al menos un numero y . Indique caracteres en minusculas");
		if (!txtNuevaClave.getValue().equals(txtReNuevaClave.getValue())) {
			throw new WrongValueException(txtReNuevaClave,
					"diferencias al validar");
		}
	}

	public void onEvent(Event evento) throws Exception {
		if (evento.getTarget() == aceptar) {
			validar();
			usuario.setContrasena(txtNuevaClave.getValue());
			PerUsuario.cambiarClave(usuario);
			app.mostrarInformacion("Se ha cambiado su clave con Exito. Verifique este cambio reiniciando la Sesion.");
			this.onClose();
		} else
			this.onClose();
	}

}
