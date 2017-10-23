package cpc.demeter.controlador.gestion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.catalogo.gestion.ContPrestamosActivos;
import cpc.demeter.vista.gestion.UiPrestamoActivo;
import cpc.modelo.demeter.gestion.PrestamoActivo;
import cpc.modelo.demeter.gestion.TrasladoActivo;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.ministerio.gestion.ProductorJuridico;
import cpc.modelo.sigesp.basico.Activo;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.negocio.demeter.gestion.NegocioTrasladoActivo;
import cpc.persistencia.demeter.implementacion.gestion.PerEstadoMovimientoActivo;
import cpc.zk.componente.controlador.ContVentanaMaestroDetalle;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cva.pc.demeter.excepciones.ExcAgregacionInvalida;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContPrestamoActivo extends
		ContVentanaMaestroDetalle<TrasladoActivo, PrestamoActivo> {

	private static final long serialVersionUID = 6319099205216067187L;
	private NegocioTrasladoActivo negocio;
	private UiPrestamoActivo vista;
	private AppDemeter app;

	public ContPrestamoActivo(int modoOperacion, TrasladoActivo prestamo,
			ContPrestamosActivos llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido,
			ExcFiltroExcepcion {
		super(modoOperacion);
		this.app = app;

		List<Activo> activos = new ArrayList<Activo>();
		List<Modelo> modelos = null;
		List<UnidadAdministrativa> unidades = null;
		List<ProductorJuridico> unidadesTraslado = null;
		negocio = NegocioTrasladoActivo.getInstance();

		try {
			if ((prestamo != null && modoConsulta())
					|| (prestamo != null && modoAnular())) {
				negocio.setTrasladoActivo(prestamo);
				prestamo = negocio.getTrasladoActivo();
				if (prestamo
						.getEstado()
						.getIdEstado()
						.equals(new PerEstadoMovimientoActivo().getAnulado()
								.getIdEstado())
						&& modoAnular())
					throw new ExcDatosInvalidos(
							"No se puede Anular un Prestamo ya Anulado");
				prestamo.setFechaAnulacion(new Date());
				prestamo.setAnuladoPor(app.getUsuario().getNombre());
				setear(prestamo,
						new UiPrestamoActivo("PRESTAMO DE ACTIVO ("
								+ Accion.TEXTO[modoOperacion] + ")", 900,
								modoOperacion, new PerEstadoMovimientoActivo()
										.getAnulado()), llamador, app);
			}

			if (prestamo == null || modoAgregar()) {
				modelos = negocio.obtenerModelosActivos();
				unidadesTraslado = negocio.obtenerProductoresJuridicos();
				prestamo = negocio.nuevoPrestamo(app.getUsuario().getNombre());
				if (negocio.getModoFuncionamientoGlobal()) {
					unidades = negocio.obtenerSedes();
				}
				setear(prestamo,
						new UiPrestamoActivo("PRESTAMO DE ACTIVO ("
								+ Accion.TEXTO[modoOperacion] + ")", 1000,
								modoOperacion, new PerEstadoMovimientoActivo()
										.getAnulado(), unidades, negocio
										.getModoFuncionamientoGlobal(),
								activos, modelos, unidadesTraslado), llamador,
						app);
			}

			vista = (UiPrestamoActivo) getVista();
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMensaje());
		}
	}

	public void eliminar() throws ExcFiltroExcepcion {

	}

	public void guardar() throws ExcFiltroExcepcion {
		try {
			for (int i = 0; i < vista.getDetalle().getColeccion().size(); i++) {
				vista.getDetalle()
						.getColeccion()
						.get(i)
						.setAlmacen(
								negocio.ObtenerAlmacen(vista.getDetalle()
										.getColeccion().get(i).getActivo()));
				vista.getDetalle()
						.getColeccion()
						.get(i)
						.setEnteJuridico(
								vista.getCmbEntesJuridicos().getSeleccion());
				vista.getDetalle()
						.getColeccion()
						.get(i)
						.setMotivo(
								negocio.ObtenerEstado(vista.getDetalle()
										.getColeccion().get(i).getActivo()));
			}
			vista.actualizarModelo();
			cargarModelo();

			negocio.setTrasladoActivo(getDato());
			negocio.guardar(negocio.obtenerControlSede(getDato()
					.getUnidadAdministrativa()));
			// imprimir();
			refrescarCatalogo();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("Error al generar el Prestamo");
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (vista.getCmbEntesJuridicos().getSeleccion() == null)
			throw new WrongValueException(vista.getCmbEntesJuridicos(),
					"Debe seleccionar un Ente Jurídico");
		if (vista.getTxtResponsableTraslado().getValue().isEmpty())
			throw new WrongValueException(vista.getTxtResponsableTraslado(),
					"Debe Colocar el Nombre del " + "Responsable");
		if (vista.getTxtCedulaResponsable().getValue().isEmpty())
			throw new WrongValueException(vista.getTxtCedulaResponsable(),
					"Debe Colocar la Cédula del " + "Responsable");
		if (vista.getTxtVehiculo().getValue().isEmpty())
			throw new WrongValueException(vista.getTxtVehiculo(),
					"Debe Colocar la descripción " + "del Vehículo");
		if (vista.getTxtPlacaVehiculo().getValue().isEmpty())
			throw new WrongValueException(vista.getTxtPlacaVehiculo(),
					"Debe Colocar la Placa " + "del Vehículo");
		if (vista.getDetalle().getColeccion().size() == 0)
			throw new WrongValueException(vista.getDetalle(),
					"Debe agregar un Detalle");

		for (PrestamoActivo item : vista.getDetalle().getColeccion()) {
			// if (item.getActivo() == null || item.getMotivo() == null)
			if (item.getActivo() == null)
				throw new WrongValueException(vista.getDetalle().getGrilla(),
						"Existen Valores Nulos en el Detalle, Verifique y Vuelva Aceptar");
		}
	}

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		if (event.getTarget() == vista.getAceptar())

			procesarCRUD(event);

		if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {
			if (((CompBuscar<Modelo>) event.getTarget()).getAttribute("nombre")
					.equals("modelos")) {
				if (vista.getActivos() != null)
					vista.getActivos().clear();
				for (Activo item : negocio
						.obtenerActivosPorModeloSinAlmacenOperativo(vista
								.getCmbModelos().getSeleccion()))
					vista.getActivos().add(item);
			}

			if (((CompBuscar<Activo>) event.getTarget()).getAttribute("nombre")
					.equals("activo")) {
				actualizarFila((CompBuscar<Activo>) event.getTarget());
			}
		}
	}

	public void actualizarFila(CompBuscar<Activo> componente)
			throws ExcFiltroExcepcion {
		try {
			Row registro = (Row) componente.getParent();
			((Label) registro.getChildren().get(1)).setValue(componente
					.getValorObjeto().getNombre());
			((Label) registro.getChildren().get(2)).setValue(negocio
					.ObtenerAlmacen(componente.getSeleccion()).getNombre());
		} catch (Exception e) {
			throw new ExcFiltroExcepcion("Problemas con el Activo");
		}
	}

	public void cargarModelo() throws ExcAgregacionInvalida {
		getDato().setPrestamos(vista.getModeloDetalle());
	}

	public void anular() {
		try {
			if (vista.getTxtMotivoAnulacion().getValue().isEmpty())
				throw new WrongValueException(vista.getTxtMotivoAnulacion(),
						"Debe indicar el motivo de la Anulación");
			vista.actualizarModelo();
			negocio.anular(getDato());
			refrescarCatalogo();
			vista.close();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
			vista.getTxtMotivoAnulacion().setFocus(true);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void imprimir() {
		HashMap parametros = new HashMap();

		TrasladoActivo traslado = negocio.getTrasladoActivo();

		parametros.put("observacionGeneral", traslado.getObservaciones());
		parametros.put("rutaImagenEncabezado", Index.class.getResource("/")
				.getPath() + "../../imagenes/cintillo_inst.png");
		parametros.put("nombreConductor", traslado.getResponsableTraslado());
		parametros.put("cedulaConductor", traslado.getCedulaResponsable());
		parametros.put("vehiculo", traslado.getVehiculo());
		parametros.put("placaVehiculo", traslado.getPlacaVehiculo());

		JRDataSource ds = new JRBeanCollectionDataSource(traslado.getSalidas());
		app.agregarReporte();
		Jasperreport reporte = app.getReporte();

		reporte.setSrc("reportes/PrestamoActivos.jasper");
		reporte.setParameters(parametros);
		reporte.setDatasource(ds);
		reporte.setType("pdf");
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
