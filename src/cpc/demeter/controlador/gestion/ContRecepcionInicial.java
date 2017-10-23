package cpc.demeter.controlador.gestion;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
import cpc.demeter.catalogo.gestion.ContRecepcionesIniciales;
import cpc.demeter.vista.gestion.UIRecepcionInicialActivo;
import cpc.modelo.demeter.gestion.EntradaActivo;
import cpc.modelo.demeter.gestion.MotivoTransferenciaActivo;
import cpc.modelo.demeter.gestion.Movimiento;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.sigesp.basico.Activo;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.negocio.demeter.gestion.NegocioRecepcionInicial;
import cpc.persistencia.demeter.implementacion.gestion.PerEstadoMovimientoActivo;
import cpc.persistencia.sigesp.implementacion.PerTipoAlmacen;
import cpc.zk.componente.controlador.ContVentanaMaestroDetalle;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cva.pc.demeter.excepciones.ExcAgregacionInvalida;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContRecepcionInicial extends
		ContVentanaMaestroDetalle<Movimiento, EntradaActivo> {

	private static final long serialVersionUID = 1294758335697442863L;
	private NegocioRecepcionInicial negocio;
	private UIRecepcionInicialActivo vista;
	private AppDemeter app;

	public ContRecepcionInicial(int modoOperacion, Movimiento movimiento,
			ContRecepcionesIniciales llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido,
			ExcFiltroExcepcion {
		super(modoOperacion);
		this.app = app;

		List<Activo> activos = new ArrayList<Activo>();
		List<Almacen> almacenes = new ArrayList<Almacen>();
		List<MotivoTransferenciaActivo> estados = null;
		List<UnidadAdministrativa> unidadesAdministrativas = null;
		List<Movimiento> movimientos = new ArrayList<Movimiento>();
		String mensaje = new String();
		List<Modelo> modelos = null;
		negocio = NegocioRecepcionInicial.getInstance();

		try {
			if ((movimiento != null && modoConsulta())
					|| (movimiento != null && modoAnular())) {
				negocio.setRecepcionIncial(movimiento);
				movimiento = negocio.getRecepcionIncial();
				if (modoAnular()) {
					if (movimiento
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
				if (modoAnular()) {
					movimiento.setFechaAnulacion(new Date());
					movimiento.setAnuladoPor(app.getUsuario().getNombre());
				}
				setear(movimiento,
						new UIRecepcionInicialActivo(
								"TOMA DE INVENTARIO DE ACTIVO" + " ("
										+ Accion.TEXTO[modoOperacion] + ")",
								1100, modoOperacion,
								new PerEstadoMovimientoActivo().getAnulado()),
						llamador, app);
			}

			if (movimiento == null || modoAgregar()) {
				// activos = negocio.obtenerActivos();
				modelos = negocio.obtenerModelosConActivos();
				// System.out.println("tamañor activo "+activos.size());
				movimiento = negocio.nuevaRecepcionIncial(app.getUsuario()
						.getNombre());
				if (negocio.getModoFuncionamientoGlobal()) {
					unidadesAdministrativas = negocio
							.obtenerUnidadesAdministrativas();
				} else {
					// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
					// "Advertencia 17",JOptionPane.WARNING_MESSAGE);
					almacenes = negocio
							.obtenerAlmacenesPorUnidadAdministrativa(
									movimiento.getUnidadAdministrativa(),
									new PerTipoAlmacen().getTipoMecanizado());
				}
				estados = negocio.getEstadosActivos();
				setear(movimiento,
						new UIRecepcionInicialActivo(
								"TOMA DE INVENTARIO DE ACTIVO ("
										+ Accion.TEXTO[modoOperacion] + ")",
								1100, activos, app.getUsuario().getNombre(),
								almacenes, modoOperacion,
								new PerEstadoMovimientoActivo().getAnulado(),
								estados, unidadesAdministrativas, negocio
										.getModoFuncionamientoGlobal(), modelos),
						llamador, app);
			}
			// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
			// "Advertencia 16",JOptionPane.WARNING_MESSAGE);
			vista = (UIRecepcionInicialActivo) getVista();
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMensaje());
		}

	}

	public void eliminar() throws ExcFiltroExcepcion {
		// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
		// "Advertencia 16",JOptionPane.WARNING_MESSAGE);
	}

	public void guardar() throws ExcFiltroExcepcion {
		try {

			vista.actualizarModelo();
			cargarModelo();
			negocio.setRecepcionIncial(getDato());
			negocio.guardar(negocio.obtenerControlSede(getDato()
					.getUnidadAdministrativa()));
			imprimir();
			refrescarCatalogo();
		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("Error al procesar la Recepción");
		} catch (ExcAgregacionInvalida e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("Error al Cargar Detalle");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (vista.getDetalle().getColeccion().size() == 0)
			throw new WrongValueException(vista.getDetalle(),
					"Debe agregar un Detalle");
		List<Row> filas = vista.getDetalle().getFilas().getChildren();
		for (Row item : filas) {
			CompBuscar<Activo> iActivo = (CompBuscar<Activo>) item
					.getChildren().get(0);
			CompBuscar<Almacen> iAlmacen = (CompBuscar<Almacen>) item
					.getChildren().get(2);
			CompBuscar<MotivoTransferenciaActivo> iMotivo = (CompBuscar<MotivoTransferenciaActivo>) item
					.getChildren().get(3);
			if (iActivo.getSeleccion() == null)
				throw new WrongValueException(iActivo, "No ha definido Activo");
			if (iAlmacen.getSeleccion() == null)
				throw new WrongValueException(iAlmacen,
						"No ha definido Almacen");
			if (iMotivo.getSeleccion() == null)
				throw new WrongValueException(iMotivo, "No ha definido motivo");
		}
	}

	public synchronized void limpiar() throws WrongValuesException,
			ExcEntradaInconsistente {

		List<Row> filas = new CopyOnWriteArrayList<Row>(vista.getDetalle()
				.getFilas().getChildren());
		// System.out.println("original"+filas.size());
		for (Row item : filas) {
			CompBuscar<Activo> iActivo = (CompBuscar<Activo>) item
					.getChildren().get(0);
			CompBuscar<Almacen> iAlmacen = (CompBuscar<Almacen>) item
					.getChildren().get(2);
			CompBuscar<MotivoTransferenciaActivo> iMotivo = (CompBuscar<MotivoTransferenciaActivo>) item
					.getChildren().get(3);
			if (iActivo.getSeleccion() == null)
				vista.getDetalle().getFilas().removeChild(item);

		}
		// System.out.println("despues de ta¿ratada"+filas.size());
		// System.out.println("tamaño normal"+vista.getDetalle().getFilas().getChildren().size());
	}

	/*
	 * for (EntradaActivo item : vista.getDetalle().getColeccion()){ if
	 * (item.getActivo() == null || item.getAlmacen() == null ||
	 * item.getMotivo() == null) throw new
	 * WrongValueException(vista.getDetalle().getGrilla(),
	 * "Existen Valores Nulos en el Detalle, Verifique y Vuelva Aceptar"); }
	 */

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		if (event.getTarget() == vista.getAceptar())

			procesarCRUD(event);
		// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
		// "Advertencia 2",JOptionPane.WARNING_MESSAGE);

		if (event.getName().equals("onBorrarFila"))
			System.out.println("Evento elimina fila");
		if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {
			// limpiar();

			if (((CompBuscar<Activo>) event.getTarget()).getAttribute("nombre")
					.equals("activo")) {
				actualizarFila((CompBuscar<Activo>) event.getTarget());
				// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
				// "Advertencia 3",JOptionPane.WARNING_MESSAGE);
			} else if (((CompBuscar<UnidadAdministrativa>) event.getTarget())
					.getAttribute("nombre").equals("unidades")) {
				if (vista.getActivos() != null && vista.getAlmacenes() != null) {
					vista.getAlmacenes().clear();
				}

				try {
					// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
					// "Advertencia 4",JOptionPane.WARNING_MESSAGE);
					for (Almacen item2 : negocio
							.obtenerAlmacenesPorUnidadAdministrativa(vista
									.getCmbUnidadAdministrativas()
									.getSeleccion(), new PerTipoAlmacen()
									.getTipoMecanizado())) {
						vista.getAlmacenes().add(item2);
						// JOptionPane.showMessageDialog(null,
						// Desktop.getDesktop(),
						// "Advertencia 15",JOptionPane.WARNING_MESSAGE);
					}
				} catch (ExcFiltroExcepcion e) {
					throw new WrongValueException(
							((CompBuscar<UnidadAdministrativa>) event
									.getTarget()),
							"No existen almacenes para esta Unidad");
				}
			} else if (((CompBuscar<Modelo>) event.getTarget()).getAttribute(
					"nombre").equals("modelos"))
				// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
				// "Advertencia 5",JOptionPane.WARNING_MESSAGE);
				// limpiar();
				if (vista.getActivos() != null)
					vista.getActivos().clear();
			limpiar();
			for (Activo item3 : negocio.obtenerActivosPorModelo(vista
					.getCmbModelos().getSeleccion()))
				vista.getActivos().add(item3);
			// limpiar();
		}

	}

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

	public void cargarModelo() throws ExcAgregacionInvalida,
			WrongValuesException, ExcEntradaInconsistente {
		try {
			// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
			// "Advertencia 12",JOptionPane.WARNING_MESSAGE);
			getDato().setEntradas(vista.getModeloDetalle());
			// if (modoAnular())
			// getDato().setMotivoAnulacion(vista.getTxtMotivoAnulacion().getText());
			// // Asignar el motivo de Anulación
			// limpiar();
		} catch (ExcAgregacionInvalida e) {
			e.printStackTrace();
		}
	}

	public void anular() {
		try {
			vista.actualizarModelo();
			// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
			// "Advertencia 13",JOptionPane.WARNING_MESSAGE);
			cargarModelo();
			negocio.anular(getDato());
			refrescarCatalogo();

			vista.close();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError("No se pudo realizar la anulacion por :"
					+ e.getMessage());

		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void imprimir() {
		// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
		// "Advertencia 14",JOptionPane.WARNING_MESSAGE);
		HashMap parametros = new HashMap();

		Movimiento recepcion = negocio.getRecepcionIncial();

		parametros.put("rutaImagenEncabezado", Index.class.getResource("/")
				.getPath() + "../../imagenes/cintillo_inst.png");
		System.out.println("ruta reporte :"
				+ parametros.get("rutaImagenEncabezado"));
		JRDataSource ds = new JRBeanCollectionDataSource(
				recepcion.getEntradas());
		app.agregarReporte();
		Jasperreport reporte = app.getReporte();

		reporte.setSrc("reportes/TomaInventarioActivos.jasper");
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
