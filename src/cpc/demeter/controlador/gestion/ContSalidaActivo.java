package cpc.demeter.controlador.gestion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.catalogo.gestion.ContSalidasActivos;
import cpc.demeter.vista.gestion.UISalidaActivo;
import cpc.modelo.demeter.gestion.SalidaActivo;
import cpc.modelo.demeter.gestion.TrasladoActivo;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.sigesp.basico.Activo;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.negocio.demeter.gestion.NegocioTrasladoActivo;
import cpc.persistencia.demeter.implementacion.gestion.PerEstadoMovimientoActivo;
import cpc.zk.componente.controlador.ContVentanaMaestroDetalle;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompGrilla;
import cpc.zk.componente.listas.CompGrillaConBoton;
import cva.pc.demeter.excepciones.ExcAgregacionInvalida;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContSalidaActivo extends
		ContVentanaMaestroDetalle<TrasladoActivo, SalidaActivo> {

	private static final long serialVersionUID = 1294758335697442863L;
	private NegocioTrasladoActivo negocio;
	private UISalidaActivo vista;
	private AppDemeter app;

	public ContSalidaActivo(int modoOperacion, TrasladoActivo salida,
			ContSalidasActivos llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido,
			ExcFiltroExcepcion {
		super(modoOperacion);
		this.app = app;

		// List<ActaAutorizacion> actas = null;
		List<Activo> activos = new ArrayList<Activo>();
		List<Modelo> modelos = null;
		List<UnidadAdministrativa> unidades = null;
		List<UnidadAdministrativa> unidadesTraslado = null;
		negocio = NegocioTrasladoActivo.getInstance();

		try {
			if ((salida != null && modoConsulta())
					|| (salida != null && modoAnular())) {
				negocio.setTrasladoActivo(salida);
				salida = negocio.getTrasladoActivo();
				if (salida
						.getEstado()
						.getIdEstado()
						.equals(new PerEstadoMovimientoActivo().getAnulado()
								.getIdEstado())
						&& modoAnular())
					throw new ExcDatosInvalidos(
							"No se puede Anular una Salida ya Anulada");
				salida.setFechaAnulacion(new Date());
				salida.setAnuladoPor(app.getUsuario().getNombre());
				setear(salida,
						new UISalidaActivo("SALIDA ACTIVO ("
								+ Accion.TEXTO[modoOperacion] + ")", 900,
								modoOperacion, new PerEstadoMovimientoActivo()
										.getAnulado()), llamador, app);
			}

			if (salida == null || modoAgregar()) {
				modelos = negocio.obtenerModelosActivos();
				unidadesTraslado = negocio.obtenerSedesTraslado();
				salida = negocio.nuevaSalida(app.getUsuario().getNombre());
				if (negocio.getModoFuncionamientoGlobal()) {
					unidades = negocio.obtenerSedes();
				}
				// actas = negocio.obtenerActasSalidas();
				setear(salida,
						new UISalidaActivo("SALIDA ACTIVO ("
								+ Accion.TEXTO[modoOperacion] + ")", 1000,
								modoOperacion, new PerEstadoMovimientoActivo()
										.getAnulado(), unidades, negocio
										.getModoFuncionamientoGlobal(),
								activos, modelos, unidadesTraslado), llamador,
						app);
			}

			vista = (UISalidaActivo) getVista();
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMensaje());
		}

	}

	public void eliminar() {

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
		List<SalidaActivo> a = vista.getColeccionGrilla();
		for (int i = 0; i < a.size(); i++) {
			if (a.get(i).getActivo() == null) {
				if (vista.getColeccionGrilla().remove(i) != null) {
					System.out.println("lo hace");
				}
			}

		}
		List<SalidaActivo> b = vista.getColeccionGrilla();
	}

	public void eliminadato(Row fila) throws ExcFiltroExcepcion {

	}

	public void guardar() throws ExcFiltroExcepcion {
		try {
			vista.actualizarModelo();
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
						.setUnidadAdministrativa(
								vista.getCmbUnidadesParaTrasladar()
										.getSeleccion());
				vista.getDetalle()
						.getColeccion()
						.get(i)
						.setMotivo(
								negocio.ObtenerEstado(vista.getDetalle()
										.getColeccion().get(i).getActivo()));
			}

			cargarModelo();

			negocio.setTrasladoActivo(getDato());
			negocio.guardar(negocio.obtenerControlSede(getDato()
					.getUnidadAdministrativa()));
			imprimir();
			refrescarCatalogo();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("Error al generar el Traslado");
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (vista.getCmbUnidadesParaTrasladar().getSeleccion() == null)
			throw new WrongValueException(vista.getCmbUnidadesParaTrasladar(),
					"Debe seleccionar una Unidad");
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

		for (SalidaActivo item : vista.getDetalle().getColeccion()) {
			if (item.getActivo() == null)
				throw new WrongValueException(vista.getDetalle().getGrilla(),
						"Existen Valores Nulos en el Detalle, Verifique y Vuelva Aceptar");
		}

	}

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		System.out.println(event.getName());

		if (event.getTarget() == vista.getAceptar())

			procesarCRUD(event);

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
			/*
			 * 
			 * CompGrillaConBoton<SalidaActivo> c = vista.getDetalle(); Row a =
			 * (Row) event.getData(); CompBuscar<Activo> b =
			 * (CompBuscar<Activo>) a.getChildren().get(0); if
			 * (b.getSeleccion()==null){ limpiarfila(); } else {
			 * eliminaractivo(b.getSeleccion());
			 * 
			 * 
			 * eliminadato((Row) event.getData());
			 * 
			 * 
			 * limpiarfila(); System.out.println("aa"); }
			 */limpiarfila();
		}
		;

		if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {
			/*
			 * if
			 * (((CompBuscar<ActaAutorizacion>)event.getTarget()).getAttribute
			 * ("nombre").equals("acta")){
			 * System.out.println("Entro al evento"); try {
			 * agregarDetallesDeActa
			 * (vista.getCmpActaMovimiento().getSeleccion()); } catch
			 * (NullPointerException e){ e.printStackTrace(); } }
			 */
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

	/*
	 * public void agregarDetallesDeActa(ActaAutorizacion actaAutorizacion){
	 * SalidaActivo entrada; DetalleActaSalida itemf = null;
	 * 
	 * vista.getDetalle().clear(); if (negocio.getModoFuncionamientoGlobal()){
	 * try { for (DetalleActaSalida item :
	 * negocio.obtenerDetalleActasSalidasPorUnidad(
	 * vista.getCmbUnidadAdministrativas().getSeleccion(), actaAutorizacion)){
	 * System.out.println("Entro al ciclo"); itemf = item; entrada = new
	 * SalidaActivo(); entrada.setActivo(item.getActivo());
	 * entrada.setUnidadAdministrativa(item.getUnidadAdministrativaDestino());
	 * entrada.setAlmacen(negocio.ObtenerAlmacen(item.getActivo()));
	 * entrada.setMotivo(negocio.ObtenerEstado(item.getActivo()));
	 * 
	 * vista.getDetalle().agregar(entrada); } } catch (NullPointerException e){
	 * e.printStackTrace();
	 * app.mostrarError("El activo de codigo: "+itemf.getActivo
	 * ().getCodigoActivo()+
	 * " ejemplar: "+itemf.getActivo().getIdEjemplarActivo()+
	 * " serial: "+itemf.getActivo().getSerial()+" No pertenece a un almacen");
	 * } } else { try { for (DetalleActaSalida item :
	 * negocio.obtenerDetalleActasSalidasPorUnidad(
	 * vista.getModelo().getUnidadAdministrativa(), actaAutorizacion)){ itemf =
	 * item; entrada = new SalidaActivo(); entrada.setActivo(item.getActivo());
	 * entrada.setUnidadAdministrativa(item.getUnidadAdministrativaDestino());
	 * entrada.setAlmacen(negocio.ObtenerAlmacen(item.getActivo()));
	 * entrada.setMotivo(negocio.ObtenerEstado(item.getActivo()));
	 * 
	 * vista.getDetalle().agregar(entrada); } } catch (NullPointerException e){
	 * e.printStackTrace();
	 * app.mostrarError("El activo de codigo: "+itemf.getActivo
	 * ().getCodigoActivo()+
	 * " ejemplar: "+itemf.getActivo().getIdEjemplarActivo()+
	 * " serial: "+itemf.getActivo().getSerial()+" No pertenece a un almacen");
	 * } } }
	 */

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
		getDato().setSalidas(vista.getModeloDetalle());
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
			// throw new ExcArgumentoInvalido("Error al anular la Entrada");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

		JRDataSource ds = new JRBeanCollectionDataSource(traslado.getSalidas());
		app.agregarReporte();
		Jasperreport reporte = app.getReporte();

		reporte.setSrc("reportes/SalidaActivos.jasper");
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
