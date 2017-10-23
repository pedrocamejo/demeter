package cpc.demeter.controlador.gestion;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.catalogo.gestion.ContTraferenciasActivos;
import cpc.demeter.vista.gestion.UITransferenciaActivo;
import cpc.modelo.demeter.gestion.MotivoTransferenciaActivo;
import cpc.modelo.demeter.gestion.Movimiento;
import cpc.modelo.demeter.gestion.TransferenciaActivo;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.sigesp.basico.Activo;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.negocio.demeter.gestion.NegocioTransferenciaActivo;
import cpc.persistencia.sigesp.implementacion.PerTipoAlmacen;
import cpc.zk.componente.controlador.ContVentanaMaestroDetalle;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompGrilla;
import cva.pc.demeter.excepciones.ExcAgregacionInvalida;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContTransferenciaActivo extends
		ContVentanaMaestroDetalle<Movimiento, TransferenciaActivo> {

	private static final long serialVersionUID = 1294758335697442863L;
	private NegocioTransferenciaActivo negocio;
	private UITransferenciaActivo vista;
	private AppDemeter app;

	public ContTransferenciaActivo(int modoOperacion, Movimiento movimiento,
			ContTraferenciasActivos llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido,
			ExcFiltroExcepcion {
		super(modoOperacion);
		this.app = app;

		List<Activo> activos = new ArrayList<Activo>();
		List<Almacen> almacenes = new ArrayList<Almacen>();
		List<MotivoTransferenciaActivo> motivos = null;
		List<UnidadAdministrativa> unidadesAdministrativas = null;
		negocio = NegocioTransferenciaActivo.getInstance();

		try {
			if (movimiento != null || modoConsulta()) {
				negocio.setMovimiento(movimiento);
				movimiento = negocio.getMovimiento();
				setear(movimiento, new UITransferenciaActivo(
						"TRASNFERENCIA ACTIVO ENTRE ALMACEN ("
								+ Accion.TEXTO[modoOperacion] + ")", 1200,
						modoOperacion), llamador, app);
			}

			if (movimiento == null || modoAgregar()) {
				motivos = negocio.getMotivosTransferenciasActivos();
				movimiento = negocio.nuevaTransferencia(app.getUsuario()
						.getNombre());
				if (negocio.getModoFuncionamientoGlobal()) {
					unidadesAdministrativas = negocio
							.obtenerUnidadesAdministrativas();
				} else {
					activos = negocio.obtenerActivosPorAlmacen(movimiento
							.getUnidadAdministrativa());
					almacenes = negocio
							.obtenerAlmacenesPorUnidadAdministrativa(
									movimiento.getUnidadAdministrativa(),
									new PerTipoAlmacen().getTipoMecanizado());
				}
				setear(movimiento, new UITransferenciaActivo(
						"TRASNFERENCIA ACTIVO ENTRE ALMACEN ("
								+ Accion.TEXTO[modoOperacion] + ")", 1250,
						unidadesAdministrativas, activos, almacenes, motivos,
						modoOperacion, negocio.getModoFuncionamientoGlobal()),
						llamador, app);
			}

			vista = (UITransferenciaActivo) getVista();
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha Seleccionado nigun campo");
		}

	}

	public void eliminar() throws ExcFiltroExcepcion {
	}

	public void guardar() throws ExcFiltroExcepcion {
		try {
			vista.actualizarModelo();
			cargarModelo();
			negocio.setMovimiento(getDato());
			negocio.guardar(negocio.obtenerControlSede(getDato()
					.getUnidadAdministrativa()));
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("Error al generar la Transferencia");
		}
	}

	@SuppressWarnings("unchecked")
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

		if (vista.getDetalle().getColeccion().size() == 0)
			throw new WrongValueException(vista.getDetalle(),
					"Debe agregar un Detalle");

		try {
			for (TransferenciaActivo item : vista.getModeloDetalle()) {
				if (item.getActivo() == null || item.getAlmacenActual() == null
						|| item.getMotivo() == null)
					throw new WrongValueException(vista.getDetalle()
							.getGrilla(),
							"Existen Valores Nulos en el Detalle, Verifique y Vuelva Aceptar");
			}
		} catch (ExcAgregacionInvalida e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		List<Almacen> almacenes = null;
		if (event.getTarget() == vista.getAceptar())
			procesarCRUD(event);
		if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {
			if (((CompBuscar<Activo>) event.getTarget()).getAttribute("nombre")
					.equals("activo")) {
				actualizarFila((CompBuscar<Activo>) event.getTarget());
			} else {
				try {
					if (((CompBuscar<Almacen>) event.getTarget()).getAttribute(
							"nombre").equals("almacen"))
						validaAlmacenActual(
								(CompBuscar<Almacen>) event.getTarget(), event);
				} catch (NullPointerException e) {
					throw new WrongValueException(
							((CompBuscar<Almacen>) event.getTarget()),
							"Debe Seleccionar un activo");
				}

			}
			if (((CompBuscar<UnidadAdministrativa>) event.getTarget())
					.getAttribute("nombre").equals("unidades")) {
				if (vista.getActivos() != null && vista.getAlmacenes() != null) {
					vista.getActivos().clear();
					vista.getAlmacenes().clear();
				}

				try {
					almacenes = negocio
							.obtenerAlmacenesPorUnidadAdministrativa(vista
									.getCmbUnidadAdministrativas()
									.getSeleccion(), new PerTipoAlmacen()
									.getTipoMecanizado());
					for (Almacen item2 : almacenes) {
						vista.getAlmacenes().add(item2);
					}
				} catch (ExcFiltroExcepcion e) {
					throw new WrongValueException(
							((CompBuscar<UnidadAdministrativa>) event
									.getTarget()),
							"No existen almacenes para esta Unidad");
				}

				try {
					for (Activo item : negocio.obtenerActivosPorAlmacen(vista
							.getCmbUnidadAdministrativas().getSeleccion())) {
						vista.getActivos().add(item);
					}
				} catch (NullPointerException e) {
					throw new WrongValueException(
							((CompBuscar<UnidadAdministrativa>) event
									.getTarget()),
							"No existen activos en los almacenes de Esta Unidad");
				}
			}
		}
		if (event.getName().equals("onBorrarFila")) {
		}
		;
	}

	public void actualizarFila(CompBuscar<Activo> componente)
			throws ExcFiltroExcepcion {
		try {
			Row registro = (Row) componente.getParent();
			((Label) registro.getChildren().get(1)).setValue(componente
					.getValorObjeto().getNombre());
			((Label) registro.getChildren().get(2)).setValue(negocio
					.obtenerAlmacenAnterior(componente.getValorObjeto())
					.getNombre());
		} catch (Exception e) {
			throw new ExcFiltroExcepcion("Problemas con el Activo");
		}
	}

	@SuppressWarnings("unchecked")
	public void validaAlmacenActual(CompBuscar<Almacen> componente, Event event)
			throws Exception, WrongValueException {
		Almacen almacen;

		Row registro = (Row) componente.getParent();
		almacen = negocio.obtenerAlmacenAnterior(((CompBuscar<Activo>) registro
				.getChildren().get(0)).getValorObjeto());
		if (almacen.getId().equals(componente.getValorObjeto().getId())) {
			throw new WrongValueException(
					((CompBuscar<Almacen>) event.getTarget()),
					"El Almacen actual no puede ser igual al Anterior");
		}

	}

	public void cargarModelo() throws ExcAgregacionInvalida {
		for (TransferenciaActivo detalle : vista.getModeloDetalle()) {
			detalle.setAlmacenAnterior(negocio.obtenerAlmacenAnterior(detalle
					.getActivo()));
			System.out.println(detalle.getNombreAlmacenActual());
		}
		getDato().setTransferencias(vista.getModeloDetalle());
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
