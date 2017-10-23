package cpc.demeter.controlador.mantenimiento;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import util.Seguridad;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter; 
import cpc.demeter.catalogo.mantenimiento.ContEntesExternos;

import cpc.demeter.vista.mantenimiento.UIEnteExterno;
import cpc.demeter.vista.mantenimiento.UIUsuarioMantenimiento;
import cpc.modelo.demeter.mantenimiento.EnteExterno;
import cpc.modelo.demeter.mantenimiento.UsuarioMantenimiento;
import cpc.negocio.demeter.mantenimiento.NegocioEnteExterno;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContEnteExterno extends ContVentanaBase<EnteExterno> {

	private AppDemeter app;
	private UIEnteExterno vista;
	private UIUsuarioMantenimiento vistaUsuario;
	private NegocioEnteExterno servicio;

	private static final long serialVersionUID = 1L;

	public ContEnteExterno(int modoOperacion, EnteExterno ente,
			ContEntesExternos llamador, AppDemeter app)
			throws ExcDatosInvalidos {
		super(modoOperacion);
		this.app = app;
		if (ente == null || modoAgregar()) {
			ente = new EnteExterno();

		}
		setear(ente, new UIEnteExterno("Ente Externo("
				+ Accion.TEXTO[modoOperacion] + ")", 700), llamador, app);
		vista = ((UIEnteExterno) getVista());
		vista.desactivar(modoOperacion);
	}

	public void eliminar() {

		try {

			servicio = NegocioEnteExterno.getInstance();
			servicio.Eliminar(getDato());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {

		try {
			servicio = NegocioEnteExterno.getInstance();
			servicio.guardar(getDato());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR
				&& (vista.getDescripcion().getValue() == null || vista
						.getDescripcion().getValue() == "")) {
			throw new WrongValueException(
					((UIEnteExterno) getVista()).getDescripcion(),
					"Indique una Descripcion");
		}
	}

	public void onEvent(Event arg0) throws Exception {

		if (vista.getAgregar() == arg0.getTarget()) {
			UsuarioMantenimiento usuario = new UsuarioMantenimiento();
			vistaUsuario = new UIUsuarioMantenimiento("Prueba", "null", true,
					usuario, 1, this);
			this.app.agregarHija(vistaUsuario);
			vistaUsuario.doModal();
		}
		/*
		 * else if(vista.getEliminar()== arg0.getTarget()) {
		 * UsuarioMantenimiento usuario = vista.seleccion(); if(usuario == null)
		 * { app.mostrarError("Debe Seleccionar 1 Item "); } else {
		 * vistaUsuario= new
		 * UIUsuarioMantenimiento("Prueba","null",true,usuario,3,this);
		 * this.app.agregarHija(vistaUsuario); vistaUsuario.doModal();
		 * vista.ActualizarLista(); } }
		 */
		else if (vista.getModificar() == arg0.getTarget()) {
			UsuarioMantenimiento usuario = vista.seleccion();
			if (usuario == null) {
				app.mostrarError("Debe Seleccionar 1 Item ");
			} else {
				vistaUsuario = new UIUsuarioMantenimiento("Prueba", "null",
						true, usuario, 2, this);
				this.app.agregarHija(vistaUsuario);
				vistaUsuario.doModal();
				vista.ActualizarLista();
			}
		} else if (vista.getVisualizar() == arg0.getTarget()) {

			UsuarioMantenimiento usuario = vista.seleccion();
			if (usuario == null) {
				app.mostrarError("Debe Seleccionar 1 Item ");
			} else {

				vistaUsuario = new UIUsuarioMantenimiento("Prueba", "null",
						true, usuario, 0, this);
				this.app.agregarHija(vistaUsuario);
				System.out.println("dime q Pajo :D 2");
				vistaUsuario.doModal();
				vista.ActualizarLista();
			}
		} else if (vistaUsuario != null) // cosas que se hacen sobre los
											// Usuarios :D
		{
			if (vistaUsuario.getAceptar() == arg0.getTarget()) {

				UsuarioMantenimiento usuario = vistaUsuario.getUsuario();

				if (vistaUsuario.getModo() == 2) // Modificado
				{
					if (vistaUsuario.getCambiocontrasena()) {
						if (vistaUsuario
								.getContrasena()
								.getText()
								.equals(vistaUsuario.getContrasena1().getText())) {
							usuario.setContrasena(Seguridad
									.getMD5(vistaUsuario.getContrasena()
											.getText()));
							vistaUsuario.detach();
							vista.ActualizarLista();
						} else
							this.app.mostrarError("La Contrasena Deben Coincidir ");
					} else {
						vistaUsuario.detach();
						vista.ActualizarLista();
					}
				}
				/*
				 * else if(vistaUsuario.getModo() == 3) // Eliminar :D {
				 * 
				 * if(Messagebox.show(
				 * "Seguro de Eliminar el Registros Seleccionado?","Eliminar",
				 * Messagebox.YES + Messagebox.NO,Messagebox.QUESTION) ==
				 * Messagebox.YES) { if(!validarCedula()) // si esta en la base
				 * de datos lo elimino si no solo lo quito de la lista :D {
				 * Object[] datos = app.getDatosAuditoriaSede(); servicio =
				 * NegocioEnteExterno.getInstance();
				 * servicio.eliminarUsuarioMantenimiento(usuario,datos); }
				 * ente.getUsuarios().remove(usuario); } vistaUsuario.detach();
				 * vista.ActualizarLista(); }
				 */
				else if (vistaUsuario.getModo() == 1) // modo 1 Nuevo : D
				{
					// validar que la Cedula sea Correcta :D que no este ya
					// Registrada en la base de datos :D
					if (validarCedula()) {
						if (vistaUsuario
								.getContrasena()
								.getText()
								.equals(vistaUsuario.getContrasena1().getText())) {
							usuario.setContrasena(Seguridad
									.getMD5(vistaUsuario.getContrasena()
											.getText()));
							usuario.setEnte(getDato());
							getDato().getUsuarios().add(usuario);
							vistaUsuario.detach();
							vista.ActualizarLista();

						} else {
							this.app.mostrarError("La Contrasena Debe Coincidir ");
						}
					} else {
						this.app.mostrarError("Esta Cedula Pertenece a un Usuario ");
					}
				} else if (vistaUsuario.getCambiar() == arg0.getTarget()) {
					vistaUsuario.cambiarcontrasena();
				}
			} else if (vistaUsuario.getCancelar() == arg0.getTarget()) {
				vistaUsuario.detach();
			} else if (vistaUsuario.getCambiar() == arg0.getTarget()) {
				vistaUsuario.cambiarcontrasena();
			}
		}
		validar();
		procesarCRUD(arg0);
	}

	public Boolean validarCedula() // retorna True si no esta :D
	{
		UsuarioMantenimiento usuario = vistaUsuario.getUsuario();

		servicio = NegocioEnteExterno.getInstance();
		getDato().setDescripcion(vista.getDescripcion().getText());
		if (servicio.Perteneceentidad(usuario) == null) {
			return true;
		} else
			return false;
	}

	@Override
	public void anular() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub

	}
}
