package cpc.demeter.controlador;

import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.UiTrabajador;
import cpc.modelo.demeter.basico.CargoTrabajador;
import cpc.modelo.demeter.basico.FuncionTrabajador;
import cpc.modelo.demeter.basico.TipoTrabajador;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.negocio.demeter.basico.NegocioTrabajador;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContTrabajador extends ContVentanaBase<Trabajador> {

	private static final long serialVersionUID = 6184414588153046382L;
	private AppDemeter app;
	private UiTrabajador vista;
	private NegocioTrabajador negocio;

	public ContTrabajador(int modoOperacion, Trabajador trabajador,
			ICompCatalogo<Trabajador> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			if (trabajador == null || modoAgregar())
				trabajador = new Trabajador();
			negocio = NegocioTrabajador.getInstance();
			List<TipoTrabajador> tipos = negocio.getTiposTrabajadores();
			List<CargoTrabajador> cargos = negocio.getCargosTrabajadores();
			List<FuncionTrabajador> funciones = negocio
					.getFuncionesTrabajadores();
			vista = new UiTrabajador("TRABAJADOR ("
					+ Accion.TEXTO[modoOperacion] + ")", 530, tipos, cargos,
					funciones);
			setear(trabajador, vista, llamador, app);
			vista = ((UiTrabajador) getVista());
			vista.desactivar(modoOperacion);
			tipos = null;
			cargos = null;
			funciones = null;
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun Servicio");
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void eliminar() {

		try {
			negocio = NegocioTrabajador.getInstance();
			negocio.setModelo(getDato());
			negocio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() throws ExcFiltroExcepcion {
		try {
			negocio = NegocioTrabajador.getInstance();
			negocio.setModelo(getDato());
			negocio.guardar(negocio.getModelo().getId());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcFiltroExcepcion(e.getMessage());
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR) {
			if (vista.getCedula().getValue() == "") {
				throw new WrongValueException(vista.getDescripcion(),
						"Indique cedula");
			}
			if (!vista.getCedula().getValue().matches("[VvEePp]?-[0-9]{5,9}")) {
				throw new WrongValueException(vista.getCedula(),
						"Cedula no valida, ejemplo V-12123456");
			}
			if (vista.getNombre().getValue() == "") {
				throw new WrongValueException(vista.getNombre(),
						"Indique Nombres");
			}
			if (vista.getApellido().getValue() == "") {
				throw new WrongValueException(vista.getApellido(),
						"Indique Apellidos");
			}
			if (vista.getTipoTrabajador().getSeleccion() == null) {
				throw new WrongValueException(vista.getTipoTrabajador(),
						"Indique un Tipo de Trabajador");
			}
		}
	}

	public void onEvent(Event arg0) throws Exception {
		validar();
		try {
			procesarCRUD(arg0);
		} catch (ExcFiltroExcepcion e) {
			app.mostrarError(e.getMessage());
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
