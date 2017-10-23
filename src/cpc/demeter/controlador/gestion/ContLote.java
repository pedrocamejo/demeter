package cpc.demeter.controlador.gestion;

import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.mantenimiento.UILote;
import cpc.modelo.demeter.mantenimiento.Lote;
import cpc.negocio.demeter.mantenimiento.NegocioLote;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContLote extends ContVentanaBase<Lote> {

	private NegocioLote servicio;
	private UILote vista;
	private AppDemeter app;

	public ContLote(int modoOperacion, Lote lote, ContCatalogo<Lote> llamador,
			AppDemeter app) throws ExcDatosInvalidos {
		super(modoOperacion);
		this.app = app;
		this.servicio = NegocioLote.getInstance();
		try {
			if (datoNulo(lote) || modoAgregar()) {
				lote = new Lote();
			}
		} catch (ExcSeleccionNoValida e) {

			e.printStackTrace();
		}
		setear(lote, new UILote("Lote  (" + Accion.TEXTO[modoOperacion] + ")",
				550, this.app), llamador, this.app);
		this.vista = (UILote) getVista();
	}

	private static final long serialVersionUID = -6203766251486301230L;

	public void eliminar() {
		try {
			servicio.setLote(getDato());
			servicio.eliminar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {
		try {
			servicio.setLote(getDato());
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