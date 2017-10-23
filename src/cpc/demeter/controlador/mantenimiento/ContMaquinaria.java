package cpc.demeter.controlador.mantenimiento;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.mantenimiento.UIMaquinaria;
import cpc.modelo.demeter.mantenimiento.Maquinaria;
import cpc.negocio.demeter.mantenimiento.NegocioMaquinaria;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContMaquinaria extends ContVentanaBase<Maquinaria> {

	private static final long serialVersionUID = 2651165034127880437L;
	private NegocioMaquinaria negocio;
	private UIMaquinaria vista;
	private AppDemeter app;

	public ContMaquinaria(int modoOperacion, Maquinaria bien,
			ContCatalogo<Maquinaria> llamador, AppDemeter app)
			throws ExcDatosInvalidos {
		super(modoOperacion);
		this.app = app;
		this.negocio = NegocioMaquinaria.getInstance();
		try {
			if (datoNulo(bien) || modoAgregar()) {
				bien = new Maquinaria();
			}
		} catch (ExcSeleccionNoValida e) {

			e.printStackTrace();
		}
		setear(bien, new UIMaquinaria("EXPEDIENTE  ("
				+ Accion.TEXTO[modoOperacion] + ")", 780, this.app,
				NegocioMaquinaria.getInstance()), llamador, this.app);
		this.vista = (UIMaquinaria) getVista();
	}

	public void eliminar() {
		try {
			negocio.setMaquinaria(getDato());
			negocio.eliminar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() throws ExcFiltroExcepcion {
		try {
			negocio.setMaquinaria(getDato());
			negocio.guardar();
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("ERROR AL GUARDAR " + e.getMessage());
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		vista.getGrpDatosObjetoMant().validar();
		if (vista.getTxtPlaca().getValue() == null
				|| vista.getTxtPlaca().getValue() == "")
			throw new WrongValueException(vista.getTxtPlaca(),
					"Indique un valor");
		if (vista.getTxtSerialCarroceria().getValue() == null
				|| vista.getTxtSerialCarroceria().getValue() == "")
			throw new WrongValueException(vista.getTxtSerialCarroceria(),
					"Indique un valor");
		if (vista.getTxtSerialMotor().getValue() == null
				|| vista.getTxtSerialMotor().getValue() == "")
			throw new WrongValueException(vista.getTxtSerialMotor(),
					"Indique un valor");
		if (vista.getCmbTipoMedidaRendimiento().getSeleccion() == null)
			throw new WrongValueException(vista.getCmbTipoMedidaRendimiento(),
					"Seleccione un valor");

	}

	public void onEvent(Event event) throws Exception {
		if (event.getTarget() == getVista().getAceptar()) {
			procesarCRUD(event);
		} else if (event.getName() == CompBuscar.ON_SELECCIONO) {
			vista.actualizarModelo();
		} else if (event.getName() == Events.ON_SELECT) {
			vista.actualizarModelo();
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