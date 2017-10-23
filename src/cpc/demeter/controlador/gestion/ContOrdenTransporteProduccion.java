package cpc.demeter.controlador.gestion;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Row;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.gestion.UiOrdenTransporteProduccion;
import cpc.modelo.demeter.basico.Labor;
import cpc.modelo.demeter.basico.Rubro;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.basico.UnidadArrime;
import cpc.modelo.demeter.gestion.*;
import cpc.modelo.ministerio.basico.Usos;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.negocio.demeter.gestion.NegocioOrdenTransporteProduccion;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcAgregacionInvalida;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContOrdenTransporteProduccion extends
		ContVentanaBase<OrdenTransporteProduccion> {

	private static final long serialVersionUID = 6184414588153046382L;
	private AppDemeter app;
	private UiOrdenTransporteProduccion vista;
	private NegocioOrdenTransporteProduccion negocio;
	private DetalleOrdenTrabajo labor;

	public ContOrdenTransporteProduccion(int modoOperacion,
			OrdenTransporteProduccion ordenTrabajo,
			ICompCatalogo<OrdenTransporteProduccion> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			negocio = NegocioOrdenTransporteProduccion.getInstance();
			boolean cerrado = false;
			if (ordenTrabajo == null || modoAgregar()) {
				ordenTrabajo = negocio.getNuevaOrdenTrabajo();
				cerrado = false;
			} else {
				if (ordenTrabajo.getEstado().isFinalizada()) {
					cerrado = true;
					if (modoAnular())
						throw new ExcSeleccionNoValida(
								"No puede Anular una orden Cerrada");
					if (modoProcesar())
						throw new ExcSeleccionNoValida(
								"No puede Cerrar una orden ya Cerrada");
				}
				if (!ordenTrabajo.getEstado().isActiva()) {
					if (modoAnular())
						throw new ExcSeleccionNoValida(
								"No puede Anular una orden ya Anulada");
					if (modoProcesar())
						throw new ExcSeleccionNoValida(
								"No puede Cerrar una orden Anulada");
				}
			}

			List<Labor> labores = negocio.getLabores();
			List<Productor> productores = negocio.getProductores();
			List<Trabajador> trabajadores = negocio.getChoferes();
			List<UnidadFuncional> unidadesEjecutoras = negocio
					.getUnidadesEjecutoras();
			List<Rubro> rubros = negocio.getRubros();
			List<OrdenTrabajoMecanizado> ordenes = negocio
					.getOrdenesProduccion();
			List<UnidadArrime> silos = negocio.getSilos();
			List<Usos> usos = negocio.getUsos();
			if (ordenTrabajo.getDetalles() != null
					&& ordenTrabajo.getDetalles().size() > 0)
				labor = ordenTrabajo.getDetalles().get(0);
			vista = new UiOrdenTransporteProduccion(
					"Orden Transporte Cosecha (" + Accion.TEXTO[modoOperacion]
							+ ")", 720, unidadesEjecutoras, labores,
					trabajadores, productores, rubros, ordenes, silos, usos,
					cerrado);
			setear(ordenTrabajo, vista, llamador, app);
			vista = ((UiOrdenTransporteProduccion) getVista());
			vista.desactivar(modoOperacion);
			if (getDato() != null && getDato().getId() != null) {
				vista.getRutasTransporte().setModelo(
						negocio.getDetallesRutasOrden(getDato()));
				if (modoProcesar()) {
					vista.getDestinoReal().setModelo(
							negocio.getDestinoReal(getDato()));
				}
			}
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun Servicio");
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			throw new ExcSeleccionNoValida(e.getMessage());
		}
	}

	public void eliminar() {

		try {
			negocio = NegocioOrdenTransporteProduccion.getInstance();
			negocio.setModelo(getDato());
			negocio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {
		try {
			negocio = NegocioOrdenTransporteProduccion.getInstance();
			actualizar();
			negocio.setModelo(getDato());
			if (modoAgregar())
				negocio.guardar(negocio.getModelo());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError("Error guardando la orden, " + e.getMessage());
		}
	}

	public void cerrar() throws ExcFiltroExcepcion {
		if (modoProcesar()) {
			try {
				vista.actualizarModelo();
				validarCierre();
				actualizarCierre();
				negocio.setModelo(getDato());
				negocio.cerrar(negocio.getModelo());
				refrescarCatalogo();
				vista.close();

			} catch (Exception e) {
				e.printStackTrace();
				app.mostrarError("Error anulando la orden, " + e.getMessage());
			}
		}
	}

	public void anular() throws ExcFiltroExcepcion {
		if (modoAnular()) {
			try {
				negocio.anular(negocio.getModelo());
				refrescarCatalogo();
				vista.close();
			} catch (Exception e) {
				e.printStackTrace();
				app.mostrarError("Error anulando la orden, " + e.getMessage());
			}
		}
	}

	private void actualizarCierre() throws ExcAgregacionInvalida {
		if (modoProcesar()) {
			DetalleOrdenTrabajo detalle = getDato().getDetalles().get(0);
			detalle.setCantidadEjecutada(vista.getTotalRealProducido()
					.getValue());
		}
		if (vista.getKilometrajeFinal() != null) {
			getDato().setCantidadKilometros(
					vista.getKilometrajeFinal().getValue()
							- vista.getKilometrajeInicial().getValue());
		}
	}

	private void actualizar() throws ExcAgregacionInvalida {
		if (modoAgregar()) {
			DetalleOrdenTrabajo detalle = new DetalleOrdenTrabajo();
			detalle.setLabor(vista.getLabor().getSeleccion());
			detalle.setCantidadSolicitada(vista.getTotalPosibleProducido()
					.getValue());
			detalle.setCantidadEjecutada(vista.getTotalRealProducido()
					.getValue());
			detalle.setOrden(getDato());
			detalle.setUnidadTrabajo(vista.getUnidadTrabajo().getModelo());
			labor = detalle;
		}
		List<DetalleOrdenTrabajo> detalles = new ArrayList<DetalleOrdenTrabajo>();
		detalles.add(labor);
		getDato().setDetalles(detalles);
		actualizarMaquinaria();
		/*
		 * if (vista.getKilometrajeFinal() != null)
		 * getDato().setCantidadKilometros
		 * (vista.getKilometrajeFinal().getValue()-
		 * vista.getKilometrajeInicial().getValue());
		 */
	}

	@SuppressWarnings("unchecked")
	private void actualizarMaquinaria() throws ExcAgregacionInvalida {
		try {
			List<DetalleMaquinariaOrdenTrabajo> detalles = new ArrayList<DetalleMaquinariaOrdenTrabajo>();
			List<Row> filas = vista.getDetalle().getFilas().getChildren();
			DetalleMaquinariaOrdenTrabajo detalle;
			for (Row item : filas) {
				detalle = (DetalleMaquinariaOrdenTrabajo) item
						.getAttribute("dato");
				detalle.setOrdenTrabajo(getDato());
				detalles.add(detalle);
			}
			getDato().setEquipos(detalles);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcAgregacionInvalida("Problema Actualizado equipos, "
					+ e.getMessage());
		}
	}

	public void validarCierre() throws WrongValuesException,
			ExcEntradaInconsistente {
		if ((vista.getTotalRealProducido().getValue() == null)) {
			throw new WrongValueException(vista.getTotalRealProducido(),
					"Indique un valor");
		}
		if (vista.getTiempoEspera().getValue() == null) {
			throw new WrongValueException(vista.getTiempoEspera(),
					"Indique tiempo espera");
		}
		if (vista.getUso().getSeleccion() == null) {
			throw new WrongValueException(vista.getUso(), "Indique valor");
		}
		if (vista.getKilometrajeFinal().getValue() == null) {
			throw new WrongValueException(vista.getKilometrajeFinal(),
					"Indique valor");
		}
		if (vista.getKilometrajeFinal().getValue() <= vista
				.getKilometrajeInicial().getValue()) {
			throw new WrongValueException(vista.getKilometrajeFinal(),
					"Indique un Kilometraje Mayor al Inicial");
		}
	}

	@SuppressWarnings("unchecked")
	public void validarDetalle() throws WrongValuesException,
			ExcEntradaInconsistente {
		List<Row> filas = vista.getDetalle().getFilas().getChildren();
		if (filas.size() < 1)
			throw new WrongValueException(vista.getDetalle(),
					"Indique Conductor y equipo");
		CompCombobox<Trabajador> trabajador;
		CompBuscar<MaquinariaUnidad> maquinaria;
		for (Row item : filas) {
			trabajador = (CompCombobox<Trabajador>) item.getChildren().get(0);
			maquinaria = (CompBuscar<MaquinariaUnidad>) item.getChildren().get(
					1);
			if (trabajador.getSeleccion() == null)
				throw new WrongValueException(trabajador,
						"Conductor no seleccionado");
			if (maquinaria.getSeleccion() == null)
				throw new WrongValueException(maquinaria,
						"Maquinaria no seleccionado");

		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR) {
			if ((vista.getUnidadFuncional().getSeleccion() == null))
				throw new WrongValueException(vista.getUnidadFuncional(),
						"Indique Unidad Funcional");
			if ((vista.getRubro().getSeleccion() == null))
				throw new WrongValueException(vista.getRubro(), "Indique Rubro");
			if ((vista.getCedula().getSeleccion() == null))
				throw new WrongValueException(vista.getCedula(),
						"Indique Cedula");
			if ((vista.getUnidadProductiva().getSeleccion() == null))
				throw new WrongValueException(vista.getUnidadProductiva(),
						"Indique Origen");
			if ((vista.getDestino().getSeleccion() == null))
				throw new WrongValueException(vista.getDestino(),
						"Indique Destino");
			if ((vista.getLabor().getSeleccion() == null))
				throw new WrongValueException(vista.getLabor(), "Indique Labor");
			if ((vista.getTotalPosibleProducido().getValue() == null))
				throw new WrongValueException(vista.getTotalPosibleProducido(),
						"Indique valor");
			if ((vista.getTotalTrabajado().getValue() == null))
				throw new WrongValueException(vista.getTotalTrabajado(),
						"Indique valor");
			if ((vista.getKilometrajeInicial().getValue() == null))
				throw new WrongValueException(vista.getKilometrajeInicial(),
						"Indique valor");
			validarDetalle();
		}
	}

	public void onEvent(Event event) throws Exception {
		System.out.println("hay evento " + event.getName() + " en "
				+ event.getTarget().getClass().getName());
		if (event.getTarget() == vista.getAceptar()) {

			procesarCRUD(event);

		} else if (event.getName() == CompBuscar.ON_SELECCIONO
				&& event.getTarget() == vista.getOrdenMecanizado()) {
			actualizarOrden(vista.getOrdenMecanizado().getSeleccion());
		} else if (event.getName() == Events.ON_SELECTION
				&& event.getTarget() == vista.getUnidadFuncional()) {
			actualizarActivos(vista.getUnidadFuncional().getSeleccion());
		} else if (event.getName() == CompBuscar.ON_SELECCIONO
				&& event.getTarget() == vista.getCedula()) {
			actualizarDatosProductor(vista.getCedula().getSeleccion());
		} else if (event.getName() == Events.ON_SELECTION
				&& event.getTarget() == vista.getLabor()) {
			actualizarLabor(vista.getLabor().getSeleccion());
		} else if (event.getName() == Events.ON_CHANGE) {
			if (event.getTarget() == vista.getCantidad()
					|| event.getTarget() == vista.getCantidadViaje())
				vista.getTotalTrabajado().setValue(
						vista.getCantidad().getValue()
								* vista.getCantidadViaje().getValue());
			else
				vista.getKilometrajeTotal().setValue(
						vista.getKilometrajeFinal().getValue()
								- vista.getKilometrajeInicial().getValue());
		}
	}

	public void actualizarActivos(UnidadFuncional unidad)
			throws ExcFiltroExcepcion {
		vista.getDetalle().clear();
		List<ImplementoUnidad> implementos = negocio.getImplementos(unidad);
		List<MaquinariaUnidad> equipos = negocio.getMaquinarias(unidad);
		vista.refrescarImplementos(implementos);
		vista.refrescarMaquinaria(equipos);
	}

	public void actualizarOrden(OrdenTrabajoMecanizado orden)
			throws ExcFiltroExcepcion {
		if (orden != null) {
			vista.getUnidadFuncional().setSeleccion(orden.getUnidadFuncional());
			vista.getRubro().setSeleccion(orden.getRubro());
			vista.getCedula().setSeleccion(orden.getProductor());
			actualizarDatosProductor(orden.getProductor());
			vista.getUnidadProductiva().setSeleccion(
					orden.getUnidadProductiva().getUbicacion());
			actualizarActivos(orden.getUnidadFuncional());
			vista.getTotalPosibleProducido().setValue(
					negocio.getProduccionOrden(orden));
		}
	}

	public void actualizarDatosProductor(Productor productor)
			throws ExcFiltroExcepcion {
		if (productor != null) {
			productor = negocio.enriqueserProductor(productor);
			vista.refrescarProductor(productor);
		}
	}

	public void actualizarLabor(Labor labor) throws ExcFiltroExcepcion {
		if (labor != null) {
			try {
				vista.getUnidadFisica().setModelo(
						negocio.getUnidadProduccion(labor));
				vista.getUnidadTrabajo().setModelo(
						negocio.getUnidadTrabajo(labor));
				vista.getUnidadProduccion().setModelo(
						negocio.getUnidadProduccion(labor));
			} catch (NullPointerException e) {
				app.mostrarError("Verificar Labor seleccionada");
			}
		}
	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		try {
			cerrar();
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			app.mostrarError(e.getMensajeError());
		}

	}
}
