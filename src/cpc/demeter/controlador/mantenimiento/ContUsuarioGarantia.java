package cpc.demeter.controlador.mantenimiento;

import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.demeter.ActivacionGarantia;
import cpc.demeter.usuario.externo.UiUsuarioExterno;
import cpc.modelo.demeter.mantenimiento.UsuarioMantenimiento;
import cpc.negocio.demeter.mantenimiento.NegocioGarantia;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContUsuarioGarantia extends ContVentanaBase<UsuarioMantenimiento> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioGarantia servicio;
	private UsuarioMantenimiento usuario;
	private ActivacionGarantia app;
	private UiUsuarioExterno vista;

	public ContUsuarioGarantia(int i, ActivacionGarantia app) {
		super(i);
		this.app = app;

		servicio = NegocioGarantia.getInstance();
		vista = new UiUsuarioExterno();
		vista.setTitle("Inicar Session");
		vista.setControlador(this);
		vista.inicializar();
		vista.dibujar();
		try {

			this.app.agregarAEscritorio(vista);
			vista.setMode("modal");
		} catch (SuspendNotAllowedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub

		if (arg0.getTarget() == vista.getIniciar()
				| arg0.getTarget() == vista.getCedula()
				| arg0.getTarget() == vista.getContrasena()) {
			InciarSession();
		}
		if (arg0.getTarget() == vista.getLimpiar()) {
			vista.limpiar();
		}
	}

	public UsuarioMantenimiento getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioMantenimiento usuario) {
		this.usuario = usuario;
	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	public void InciarSession() throws WrongValuesException,
			ExcEntradaInconsistente {

		validar();

		try {
			String cedula = (String) vista.getTipodocumento().getSelectedItem()
					.getValue();
			cedula = cedula + vista.getCedula().getText().trim();
			String Contrasena = vista.getContrasena().getValue().trim();

			usuario = servicio.IniciarSession(cedula, Contrasena);
			app.MostrarUsaurio(usuario);

			app.crearListaGarantia(usuario.getEnte());

			vista.detach();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			app.mostrarError(e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		if (vista.getCedula().getValue().trim().length() < 5) {
			throw new WrongValueException(vista.getCedula(),
					"Ingrese una Cedula Valida");
		}
		if (vista.getTipodocumento().getSelectedItem() == null) {
			throw new WrongValueException(vista.getTipodocumento(),
					"Seleccione un Items ");
		}
		if (vista.getContrasena().getText().trim().length() == 0) {
			throw new WrongValueException(vista.getContrasena(),
					"Ingrese una Contraseña ");
		}
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
