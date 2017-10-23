package cpc.demeter.controlador.mantenimiento;

import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.mantenimiento.UIObjetoMantenimiento;
import cpc.modelo.demeter.mantenimiento.Maquinaria;
import cpc.modelo.demeter.mantenimiento.ObjetoMantenimiento;
import cpc.negocio.demeter.mantenimiento.NegocioObjetoMantenimiento;

import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContObjetoMantenimiento extends
		ContVentanaBase<ObjetoMantenimiento> {

	private static final long serialVersionUID = 2651165034127880437L;
	private NegocioObjetoMantenimiento negocio;
	private UIObjetoMantenimiento vista;
	private AppDemeter app;

	public ContObjetoMantenimiento(int modoOperacion, ObjetoMantenimiento bien,
			ContCatalogo<ObjetoMantenimiento> llamador, AppDemeter app)
			throws ExcDatosInvalidos {
		super(modoOperacion);
		this.app = app;
		this.negocio = NegocioObjetoMantenimiento.getInstance();
		try {
			if (datoNulo(bien) || modoAgregar()) {
				bien = new Maquinaria();
			}
		} catch (ExcSeleccionNoValida e) {

			e.printStackTrace();
		}
		setear(bien, new UIObjetoMantenimiento("Expediente  ("
				+ Accion.TEXTO[modoOperacion] + ")", 750, this.app,
				NegocioObjetoMantenimiento.getInstance()), llamador, this.app);
		this.vista = (UIObjetoMantenimiento) getVista();
	}

	public void eliminar() {
		try {
			negocio.setBienProduccion(getDato());
			negocio.eliminar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		try {
			negocio.setBienProduccion(getDato());
			negocio.guardar();
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("ERROR AL GUARDAR");
		}
	}

	@Override
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