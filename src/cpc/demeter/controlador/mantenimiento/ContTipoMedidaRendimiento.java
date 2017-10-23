package cpc.demeter.controlador.mantenimiento;

import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.mantenimiento.ContTiposMedidaRendimientos;
import cpc.demeter.vista.mantenimiento.UITipoMedidaRendimiento;
import cpc.modelo.demeter.mantenimiento.TipoMedidaRendimiento;
import cpc.negocio.demeter.mantenimiento.NegocioTipoMedidaRendimiento;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContTipoMedidaRendimiento extends
		ContVentanaBase<TipoMedidaRendimiento> {

	private static final long serialVersionUID = 1180434079481225766L;
	private NegocioTipoMedidaRendimiento servicio;
	private UITipoMedidaRendimiento vista;
	private AppDemeter app;

	public ContTipoMedidaRendimiento(int modoOperacion,
			TipoMedidaRendimiento momento,
			ContTiposMedidaRendimientos llamador, AppDemeter app)
			throws ExcDatosInvalidos {
		super(modoOperacion);
		this.app = app;
		this.servicio = NegocioTipoMedidaRendimiento.getInstance();
		try {
			if (datoNulo(momento) || modoAgregar()) {
				momento = new TipoMedidaRendimiento();
			}
		} catch (ExcSeleccionNoValida e) {

			e.printStackTrace();
		}
		setear(momento, new UITipoMedidaRendimiento(
				"Tipo Medida Rendimiento  (" + Accion.TEXTO[modoOperacion]
						+ ")", 550, this.app), llamador, this.app);
		this.vista = (UITipoMedidaRendimiento) getVista();
	}

	public void eliminar() {
		try {
			servicio.setTipoMedidaRendimiento(getDato());
			servicio.eliminar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void guardar() {
		try {
			servicio.setTipoMedidaRendimiento(getDato());
			servicio.guardar();
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

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
