package cpc.demeter.controlador.gestion;

import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.gestion.UISustitucion;
import cpc.modelo.demeter.gestion.DetalleMaquinariaOrdenTrabajo;
import cpc.modelo.demeter.gestion.Sustitucion;
import cpc.modelo.demeter.mantenimiento.ObjetoMantenimiento;
import cpc.modelo.demeter.mantenimiento.RegistroFalla;
import cpc.negocio.demeter.gestion.NegocioSustitucion;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContSustitucion extends ContVentanaBase<Sustitucion> {

	private NegocioSustitucion servicio;
	private UISustitucion vista;
	private AppDemeter app;

	public ContSustitucion(int modoOperacion, Sustitucion sustitucion,
			ContCatalogo<Sustitucion> llamador, AppDemeter app)
			throws ExcDatosInvalidos {
		super(modoOperacion);
		this.app = app;
		this.servicio = NegocioSustitucion.getInstance();
		try {
			if (datoNulo(sustitucion) || modoAgregar()) {
				sustitucion = new Sustitucion();
			}
		} catch (ExcSeleccionNoValida e) {

			e.printStackTrace();
		}
		setear(sustitucion, new UISustitucion("SUSTITUCION  ("
				+ Accion.TEXTO[modoOperacion] + ")", 750, this.app), llamador,
				this.app);
		this.vista = (UISustitucion) getVista();
		if (modoOperacion == Accion.CONSULTAR)
			vista.setLectura();
	}

	private static final long serialVersionUID = -6203766251486301230L;

	public void eliminar() throws ExcFiltroExcepcion {

	}

	public void guardar() throws ExcFiltroExcepcion {
		try {

			getModelo();
			servicio.setSustitucion(getDato());

			RegistroFalla registroFalla = null;
			if (vista.getuIRegistroFalla() != null)
				registroFalla = vista.getuIRegistroFalla().getModelo();

			servicio.guardar(vista.getLstOrdenesTrabajo().getModelo(),
					registroFalla);
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("ERROR AL GUARDAR");
		}
	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

		if (vista.getLstOrdenesTrabajo().getModelo().size() == 0)
			throw new WrongValueException(vista.getBtnBuscarOrdenes(),
					"La Lista de Ordenes debe tener al menos un detalle");

		if (vista.getCatMaquinariaCambio().getSeleccion() == null
				&& vista.getCatOperadorCambio().getSeleccion() == null)
			throw new WrongValueException(vista.getGrpSustitucion(),
					"Indique un Operador o Maquinaria");

		if (vista.getCatMaquinariaBusqueda().getSeleccion()
				.equals(vista.getCatMaquinariaCambio().getSeleccion())
				&& vista.getCatOperadorBusqueda().getSeleccion()
						.equals(vista.getCatOperadorCambio().getSeleccion()))
			throw new WrongValueException(vista.getGrpSustitucion(),
					"La sustitucion debe ser distinta en Operador y Maquinaria");

		if (vista.getRdoGrupo().getSelectedIndex() < 0)
			throw new WrongValueException(vista.getRdoGrupo(),
					"Selecione una Causa");

		if (vista.getuIRegistroFalla() == null
				&& vista.getRdoGrupo().getSelectedIndex() == 1) {
			vista.getRdoGrupo().setSelectedIndex(-1);
			throw new WrongValueException(vista.getRdoGrupo(),
					"Selecione una Causa");
		}

	}

	private void getModelo() {
		vista.obtenerOrdenesAfectadas();
		getDato().setFechaRegistro(new Date());
		getDato().setCausa(
				vista.getOpciones()[vista.getRdoGrupo().getSelectedIndex()]);

		getDato().setMaquinaActual(
				vista.getCatMaquinariaCambio().getSeleccion());
		getDato().setMaquinaAnterior(
				vista.getCatMaquinariaBusqueda().getSeleccion());
		getDato()
				.setOperadorActual(vista.getCatOperadorCambio().getSeleccion());
		getDato().setOperadorAnterior(
				vista.getCatOperadorBusqueda().getSeleccion());

		if (getDato().getMaquinaActual() == null)
			getDato().setMaquinaActual(getDato().getMaquinaAnterior());

		if (getDato().getOperadorActual() == null)
			getDato().setOperadorActual(getDato().getOperadorAnterior());

		if (vista.getLstOrdenesTrabajo().getModelo().get(0).getImplemento() != null) {
			getDato().setImplemantoAnterior(
					vista.getLstOrdenesTrabajo().getModelo().get(0)
							.getImplemento());
			getDato().setImplementoActual(
					vista.getLstOrdenesTrabajo().getModelo().get(0)
							.getImplemento());
		}
		getDato().setObservacion(vista.getTxtObservacion().getValue());

		if (vista.getuIRegistroFalla() != null
				&& vista.getuIRegistroFalla().getModelo() != null) {
			getDato().setCausaPorFallaMaq(true);
		}

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