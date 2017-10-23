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
import cpc.demeter.catalogo.gestion.ContEntradasActivos;
import cpc.demeter.vista.gestion.UIEntradaActivo;
import cpc.modelo.demeter.gestion.EntradaActivo;
import cpc.modelo.demeter.gestion.MotivoTransferenciaActivo;
import cpc.modelo.demeter.gestion.Movimiento;
import cpc.modelo.demeter.gestion.SalidaActivo;
import cpc.modelo.demeter.gestion.TrasladoActivo;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.sigesp.basico.Activo;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.negocio.demeter.gestion.NegocioTrasladoActivo;
import cpc.persistencia.demeter.implementacion.gestion.PerEstadoMovimientoActivo;
import cpc.persistencia.sigesp.implementacion.PerTipoAlmacen;
import cpc.zk.componente.controlador.ContVentanaMaestroDetalle;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompGrilla;
import cva.pc.demeter.excepciones.ExcAgregacionInvalida;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContEntradaActivo extends
		ContVentanaMaestroDetalle<TrasladoActivo, EntradaActivo> {

	private static final long serialVersionUID = 1294758335697442863L;
	private NegocioTrasladoActivo negocio;
	private UIEntradaActivo vista;
	private AppDemeter app;

	public ContEntradaActivo(int modoOperacion, TrasladoActivo entrada,
			ContEntradasActivos llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido,
			ExcFiltroExcepcion {
		super(modoOperacion);
		this.app = app;

		List<Almacen> almacenes = new ArrayList<Almacen>();
		// List<ActaAutorizacion> actas = null;
		List<MotivoTransferenciaActivo> estados = null;
		List<UnidadAdministrativa> unidadesAdministrativas = null;
		List<Activo> activos = new ArrayList<Activo>();
		List<Movimiento> movimientos = new ArrayList<Movimiento>();
		List<Modelo> modelos = null;
		String mensaje = new String();
		negocio = NegocioTrasladoActivo.getInstance();

		try {
			if ((entrada != null && modoConsulta())
					|| (entrada != null && modoAnular())) {
				negocio.setTrasladoActivo(entrada);
				entrada = negocio.getTrasladoActivo();
				if (modoAnular()) {
					if (entrada
							.getEstado()
							.getIdEstado()
							.equals(new PerEstadoMovimientoActivo()
									.getAnulado().getIdEstado()))
						throw new ExcDatosInvalidos(
								"No se puede Anular una entrada ya Anulada");

					mensaje = "Primero debe anular las siguientes Salidas : ";
					for (Movimiento item : negocio
							.obtenerSalidasConActivosDeRecepcion(negocio
									.obtenerActivosDeEntrada())) {
						movimientos.add(item);
						mensaje = mensaje + item.getControl() + " ";
					}
					if (movimientos.size() > 0)
						throw new ExcDatosInvalidos(mensaje);
				}
				entrada.setFechaAnulacion(new Date());
				entrada.setAnuladoPor(app.getUsuario().getNombre());
				setear(entrada,
						new UIEntradaActivo("ENTRADA ACTIVO ("
								+ Accion.TEXTO[modoOperacion] + ")", 1050,
								modoOperacion, new PerEstadoMovimientoActivo()
										.getAnulado()), llamador, app);
			}

			if (entrada == null || modoAgregar()) {
				modelos = negocio.obtenerModelosActivos();
				entrada = negocio.nuevaEntrada(app.getUsuario().getNombre());
				if (negocio.getModoFuncionamientoGlobal()) {
					unidadesAdministrativas = negocio.obtenerSedes();
				} else {
					almacenes = negocio
							.obtenerAlmacenesPorUnidadAdministrativa(
									entrada.getUnidadAdministrativa(),
									new PerTipoAlmacen().getTipoMecanizado());
				}
				// actas = negocio.obtenerActasEntradas();
				estados = negocio.getEstadosActivos();
				setear(entrada,
						new UIEntradaActivo("ENTRADA ACTIVO ("
								+ Accion.TEXTO[modoOperacion] + ")", 1050,
								almacenes, modoOperacion,
								new PerEstadoMovimientoActivo().getAnulado(), // actas,
								estados, unidadesAdministrativas, negocio
										.getModoFuncionamientoGlobal(),
								activos, modelos), llamador, app);
			}

			vista = (UIEntradaActivo) getVista();
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
			vista.actualizarModelo();
			cargarModelo();
			negocio.setTrasladoActivo(getDato());
			negocio.guardar(negocio.obtenerControlSede(getDato()
					.getUnidadAdministrativa()));
			imprimir();
			refrescarCatalogo();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("Error al generar la Recepción");
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
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

		for (EntradaActivo item : vista.getDetalle().getColeccion()) {
			if (item.getActivo() == null || item.getAlmacen() == null
					|| item.getMotivo() == null)
				throw new WrongValueException(vista.getDetalle().getGrilla(),
						"Existen Valores Nulos en el Detalle, Verifique y Vuelva Aceptar");
		}
	}

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		if (event.getTarget() == vista.getAceptar())

			procesarCRUD(event);

		if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {

			/*
			 * if
			 * (((CompBuscar<Activo>)event.getTarget()).getAttribute("nombre")
			 * .equals("acta"))
			 * agregarDetallesDeActa(vista.getCmpActaMovimiento(
			 * ).getSeleccion());
			 */

			if (((CompBuscar<UnidadAdministrativa>) event.getTarget())
					.getAttribute("nombre").equals("unidades")) {
				if (vista.getAlmacenes() != null) {
					vista.getAlmacenes().clear();
				}
				try {

					for (Almacen item2 : negocio
							.obtenerAlmacenesPorUnidadAdministrativa(vista
									.getCmbUnidadAdministrativas()
									.getSeleccion(), new PerTipoAlmacen()
									.getTipoMecanizado())) {
						vista.getAlmacenes().add(item2);
					}
				} catch (ExcFiltroExcepcion e) {
					throw new WrongValueException(
							((CompBuscar<UnidadAdministrativa>) event
									.getTarget()),
							"No existen almacenes para esta Unidad");
				}
			} else if (((CompBuscar<Modelo>) event.getTarget()).getAttribute(
					"nombre").equals("modelos")) {
				if (vista.getActivos() != null)
					vista.getActivos().clear();
				for (Activo item3 : negocio.obtenerActivosPorModelo(vista
						.getCmbModelos().getSeleccion()))
					vista.getActivos().add(item3);
				System.out.println("dispara modelo");
			} else if (((CompBuscar<Activo>) event.getTarget()).getAttribute(
					"nombre").equals("activo")) {
				actualizarFila((CompBuscar<Activo>) event.getTarget());
			}

		}
		if (event.getName().equals(CompGrilla.ON_ELIMINA_FILA)) {
			Row a = (Row) event.getData();
			CompBuscar<Activo> b = (CompBuscar<Activo>) a.getChildren().get(0);
			if (b.getSeleccion() == null) {
				limpiarfila();
			} else {
				limpiarfila();
				eliminaractivo(b.getSeleccion());

				eliminadato((Row) event.getData());

				System.out.println("aa");
			}
		}
	}

	/*
	 * public void agregarDetallesDeActa(ActaAutorizacion actaAutorizacion){
	 * EntradaActivo entrada;
	 * 
	 * vista.getDetalle().clear(); if (negocio.getModoFuncionamientoGlobal()){
	 * for (DetalleActaAutorizacion item :
	 * negocio.obtenerDetalleActasRecepcionesPorUnidad(
	 * vista.getCmbUnidadAdministrativas().getSeleccion(), actaAutorizacion)){
	 * entrada = new EntradaActivo(); entrada.setActivo(item.getActivo());
	 * vista.getDetalle().agregar(entrada); } } else { for
	 * (DetalleActaAutorizacion item :
	 * negocio.obtenerDetalleActasRecepcionesPorUnidad(
	 * vista.getModelo().getUnidadAdministrativa(), actaAutorizacion)){ entrada
	 * = new EntradaActivo(); entrada.setActivo(item.getActivo());
	 * vista.getDetalle().agregar(entrada); } } }
	 */

	public void actualizarFila(CompBuscar<Activo> componente)
			throws ExcFiltroExcepcion {
		try {
			Row registro = (Row) componente.getParent();
			((Label) registro.getChildren().get(1)).setValue(componente
					.getValorObjeto().getNombre());
		} catch (Exception e) {
			throw new ExcFiltroExcepcion("Problemas con el Activo");
		}
	}

	public void cargarModelo() throws ExcAgregacionInvalida {
		try {
			getDato().setEntradas(vista.getModeloDetalle());
		} catch (ExcAgregacionInvalida e) {
			e.printStackTrace();
		}
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
			// throw new ExcArgumentoInvalido("Error al anular la Entrada");
		}
	}

	public void eliminadato(Row fila) throws ExcFiltroExcepcion {

	}

	public synchronized void eliminaractivo(Activo A) throws ExcFiltroExcepcion {
		/*
		 * List<SalidaActivo> a = vista.getColeccionGrilla(); for (int i = 0; i
		 * < a.size(); i++) { if
		 * (a.get(i).getActivo().getSerial()==A.getSerial()){ if
		 * (vista.getColeccionGrilla().remove(i) !=
		 * null){System.out.println("lo hace1");} }
		 * 
		 * }
		 */
	}

	public synchronized void limpiarfila() throws ExcFiltroExcepcion {
		List<EntradaActivo> a = vista.getColeccionGrilla();
		for (int i = 0; i < a.size(); i++) {
			if (a.get(i).getActivo() == null) {
				if (vista.getColeccionGrilla().remove(i) != null) {
					System.out.println("lo hace");
				}
			}

		}
		List<EntradaActivo> b = vista.getColeccionGrilla();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void imprimir() {
		HashMap parametros = new HashMap();

		TrasladoActivo traslado = negocio.getTrasladoActivo();

		/*
		 * parametros.put("numeroActa", traslado.getNumeroActaAutorizacion());
		 * parametros.put("fechaActa", traslado.getFechaActaAutorizacion());
		 */
		parametros.put("observacionGeneral", traslado.getObservaciones());
		parametros.put("rutaImagenEncabezado", Index.class.getResource("/")
				.getPath() + "../../imagenes/cintillo_inst.png");
		parametros.put("nombreConductor", traslado.getResponsableTraslado());
		parametros.put("cedulaConductor", traslado.getCedulaResponsable());
		parametros.put("vehiculo", traslado.getVehiculo());
		parametros.put("placaVehiculo", traslado.getPlacaVehiculo());

		JRDataSource ds = new JRBeanCollectionDataSource(traslado.getEntradas());
		app.agregarReporte();
		Jasperreport reporte = app.getReporte();
		reporte.setSrc("reportes/Recepcion.jasper");
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
