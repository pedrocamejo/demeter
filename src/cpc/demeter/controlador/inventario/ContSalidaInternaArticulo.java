package cpc.demeter.controlador.inventario;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.inventario.ContSalidasInternasArticulos;
import cpc.demeter.controlador.mantenimiento.ContConsumible;
import cpc.demeter.controlador.mantenimiento.ContHerramienta;
import cpc.demeter.controlador.mantenimiento.ContRepuesto;
import cpc.demeter.vista.inventario.UiSalidaInternaArticulo;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.mantenimiento.Consumible;
import cpc.modelo.demeter.mantenimiento.DetalleSalidaInternaArticulo;
import cpc.modelo.demeter.mantenimiento.Herramienta;
import cpc.modelo.demeter.mantenimiento.Repuesto;
import cpc.modelo.demeter.mantenimiento.SalidaInternaArticulo;
import cpc.modelo.sigesp.basico.Activo;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.negocio.demeter.mantenimiento.NegocioSalidaInternaArticulo;
import cpc.persistencia.demeter.implementacion.mantenimiento.PerConsumible;
import cpc.persistencia.demeter.implementacion.mantenimiento.PerHerramienta;
import cpc.persistencia.demeter.implementacion.mantenimiento.PerRepuesto;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcEntradaInvalida;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContSalidaInternaArticulo extends
		ContVentanaBase<SalidaInternaArticulo> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioSalidaInternaArticulo servicio;
	private AppDemeter app;
	private UiSalidaInternaArticulo vista;

	public ContSalidaInternaArticulo(int modoOperacion,
			SalidaInternaArticulo salidaInternaArticulo,
			List<Almacen> almacenesOrigen, List<Almacen> almacenesDestino,
			List<Trabajador> trabajadores, List<Activo> activos,
			ContSalidasInternasArticulos llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;

		try {
			if (salidaInternaArticulo == null || modoAgregar()) {
				salidaInternaArticulo = new SalidaInternaArticulo();

			}
			setDato(salidaInternaArticulo);
			setear(getDato(),
					new UiSalidaInternaArticulo("Consumo de Articulos", 1000,
							modoOperacion, salidaInternaArticulo,
							salidaInternaArticulo
									.getDetalleSalidaInternaArticulos(),
							almacenesOrigen, almacenesDestino, trabajadores,
							activos), llamador, app);
			vista = ((UiSalidaInternaArticulo) getVista());
			vista.desactivar(getModoOperacion());
			// prueba();

		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		}

	}

	@SuppressWarnings("unchecked")
	public void eliminar() {

	}

	public void guardar() {
		NegocioSalidaInternaArticulo negocio = servicio.getInstance();
		getDato().setUsuario(app.getUsuario().toString());
		getDato().setFecha(new Date());

		getDato().setActivo(vista.getCmbActivo().getSeleccion());
		getDato().setTrabajador(vista.getCmbTrabajadores().getSeleccion());

		List<DetalleSalidaInternaArticulo> detalleSalidaInternaArticulos = vista
				.getDetalle().getColeccion();

		getDato().setDetalleSalidaInternaArticulos(
				detalleSalidaInternaArticulos);
		negocio.setSalidaInternaArticulo(getDato());
		try {
			negocio.guardar(negocio.getSalidaInternaArticulo());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List<ArticuloVenta> getArticuloVentas() {
		List<ArticuloVenta> za = new ArrayList<ArticuloVenta>();
		List<Row> filas = vista.getDetalle().getFilas().getChildren();
		for (Row row : filas) {
			CompBuscar<ArticuloVenta> a = (CompBuscar<ArticuloVenta>) row
					.getChildren().get(0);
			za.add(a.getSeleccion());
		}

		return za;

	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (vista.getCmbActivo().getSeleccion() == null) {
			throw new WrongValueException(vista.getCmbActivo(),
					"El Articulo no puede ser nulo");
		}

		if (vista.getCmbTrabajadores().getSeleccion() == null) {
			throw new WrongValueException(vista.getCmbTrabajadores(),
					"El Articulo no puede ser nulo");
		}

		List<Row> filas = vista.getDetalle().getFilas().getChildren();
		boolean z = vista.getDetalle().getFilas().getChildren().isEmpty();
		if (z) {
			throw new WrongValueException(vista.getDetalle(),
					"debe indicar al menos un articulo");
		}
		validarDetalle();
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public void validarDetalle() throws WrongValuesException,
			ExcEntradaInconsistente {
		List<Row> filas = vista.getDetalle().getFilas().getChildren();

		for (Row row : filas) {

			CompBuscar<ArticuloVenta> articulo = (CompBuscar<ArticuloVenta>) row
					.getChildren().get(0);
			CompBuscar<Almacen> almacenOrigen = (CompBuscar<Almacen>) row
					.getChildren().get(2);

			Doublebox cantidad = (Doublebox) row.getChildren().get(1);
			if (articulo.getSeleccion() == null) {
				throw new WrongValueException(articulo,
						"El Articulo no puede ser nulo");
			}

			if (almacenOrigen.getSeleccion() == null) {
				throw new WrongValueException(almacenOrigen,
						"indique el almacen de origen");
			}

			if (cantidad.getValue() == null || (cantidad.getValue() == 0)) {
				throw new WrongValueException(cantidad,
						"la cantidad no puede ser nula o cero");
			}

		}
	}

	public void onEvent(Event event) throws Exception {
		/*
		 * if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {
		 * 
		 * 
		 * if (event.getTarget().getParent().getClass() == Row.class) { Row fila
		 * = (Row) event.getTarget().getParent();
		 * 
		 * @SuppressWarnings("unchecked") CompBuscar<Almacen> almacenorigen =
		 * (CompBuscar<Almacen>) fila .getChildren().get(2);
		 * 
		 * @SuppressWarnings("unchecked") CompBuscar<ArticuloVenta>
		 * articulodeventa = (CompBuscar<ArticuloVenta>) fila
		 * .getChildren().get(0); Doublebox cantidad = (Doublebox)
		 * fila.getChildren().get(1);
		 * 
		 * if (event.getTarget() == (CompBuscar<ArticuloVenta>) fila
		 * .getChildren().get(0)) {
		 * 
		 * if ((almacenorigen.getSeleccion() != null) && (cantidad.getValue() !=
		 * null)) { ArticuloVenta z = articulodeventa.getSeleccion(); Almacen x
		 * = almacenorigen.getSeleccion(); Double c = cantidad.getValue();
		 * boolean a = true;
		 * 
		 * NegocioSalidaInternaArticulo negocio = servicio .getInstance(); a =
		 * negocio.ValidarExistencia(z, x, c);
		 * 
		 * if (!a) { int sa = Messagebox .show(
		 * "El Articulo Seleccionado no esta presente quiere ver los equivalentes"
		 * , "Advertencia", Messagebox.OK | Messagebox.CANCEL,
		 * Messagebox.QUESTION); System.out.println(sa); //
		 * Messagebox.show("Error is pressed", "Error", // Messagebox.OK,
		 * Messagebox.ERROR); // Consumible consumible = new //
		 * PerConsumible().getConsumiblearticulo
		 * (articulodeventa.getSeleccion()); // ComandoConsumible ww = new
		 * ComandoConsumible(); // ContConsumibles llmadorss = new //
		 * ContConsumibles(ww.getAccionFuncionalidad(), // app); if (sa == 1) {
		 * Consumible consumible = new PerConsumible()
		 * .getConsumiblearticulo(articulodeventa .getSeleccion()); new
		 * ContConsumible(Accion.CONSULTAR, consumible,
		 * vista.getAuxconsumible(), this.app); } if (2 == sa) { } // new
		 * ContConsumible(Accion.CONSULTAR, //
		 * consumible,vista.getAuxconsumible() , this.app); }
		 * 
		 * }
		 * 
		 * System.out.println("ok"); }
		 * 
		 * if (event.getTarget() == (CompBuscar<Almacen>) fila
		 * .getChildren().get(2)) { System.out.println("metodo 2");
		 * 
		 * if ((articulodeventa.getSeleccion() != null) && (cantidad.getValue()
		 * != null)) { ArticuloVenta z = articulodeventa.getSeleccion(); Almacen
		 * x = almacenorigen.getSeleccion(); Double c = cantidad.getValue();
		 * boolean a = true;
		 * 
		 * NegocioSalidaInternaArticulo negocio = servicio .getInstance(); a =
		 * negocio.ValidarExistencia(z, x, c);
		 * 
		 * if (!a) { int sa = Messagebox .show(
		 * "El Articulo Seleccionado no esta presente quiere ver los equivalentes"
		 * , "Advertencia", Messagebox.OK | Messagebox.CANCEL,
		 * Messagebox.QUESTION); System.out.println(sa); //
		 * Messagebox.show("Error is pressed", "Error", // Messagebox.OK,
		 * Messagebox.ERROR); // Consumible consumible = new //
		 * PerConsumible().getConsumiblearticulo
		 * (articulodeventa.getSeleccion()); // ComandoConsumible ww = new
		 * ComandoConsumible(); // ContConsumibles llmadorss = new //
		 * ContConsumibles(ww.getAccionFuncionalidad(), // app); if (sa == 1) {
		 * Consumible consumible = new PerConsumible()
		 * .getConsumiblearticulo(articulodeventa .getSeleccion()); new
		 * ContConsumible(Accion.CONSULTAR, consumible,
		 * vista.getAuxconsumible(), this.app); } if (2 == sa) { } // new
		 * ContConsumible(Accion.CONSULTAR, //
		 * consumible,vista.getAuxconsumible() , this.app); }
		 * 
		 * }
		 * 
		 * System.out.println("ok2");
		 * 
		 * }
		 * 
		 * }
		 * 
		 * 
		 * }
		 */
		/*
		 * else if (event.getName().equals(Events.ON_CHANGE)) {
		 * 
		 * if (event.getTarget().getParent().getClass() == Row.class) { Row fila
		 * = (Row) event.getTarget().getParent();
		 * 
		 * @SuppressWarnings("unchecked") CompBuscar<Almacen> almacenorigen =
		 * (CompBuscar<Almacen>) fila .getChildren().get(2);
		 * 
		 * @SuppressWarnings("unchecked") CompBuscar<ArticuloVenta>
		 * articulodeventa = (CompBuscar<ArticuloVenta>) fila
		 * .getChildren().get(0); Doublebox cantidad = (Doublebox)
		 * fila.getChildren().get(1);
		 * 
		 * if (event.getTarget() == (Doublebox) fila.getChildren().get(1)) {
		 * 
		 * if ((articulodeventa.getSeleccion() != null) &&
		 * (almacenorigen.getSeleccion() != null)) { ArticuloVenta z =
		 * articulodeventa.getSeleccion(); Almacen x =
		 * almacenorigen.getSeleccion(); Double c = cantidad.getValue(); boolean
		 * a = true;
		 * 
		 * NegocioSalidaInternaArticulo negocio = servicio .getInstance(); a =
		 * negocio.ValidarExistencia(z, x, c);
		 * 
		 * if (!a) { int sa = Messagebox .show(
		 * "El Articulo Seleccionado no esta presente quiere ver los equivalentes"
		 * , "Advertencia", Messagebox.OK | Messagebox.CANCEL,
		 * Messagebox.QUESTION); System.out.println(sa);
		 * 
		 * if (sa == 1) { Consumible consumible = new PerConsumible()
		 * .getConsumiblearticulo(articulodeventa .getSeleccion()); new
		 * ContConsumible(Accion.CONSULTAR, consumible,
		 * vista.getAuxconsumible(), this.app); } if (2 == sa) { }
		 * 
		 * }
		 * 
		 * } } } }
		 */
		System.out.println(event.getTarget() + event.getName());
		if (event.getTarget() == vista.getCmbActivo()) {
			vista.getLblDatosActivo().setValue(
					vista.getCmbActivo().getSeleccion().getDescripcionLarga());
		}

		validarexistencia(event);
		if (event.getTarget() == getVista().getAceptar()) {
			validar();
			procesarCRUD(event);

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

	public void validarexistencia(Event event) throws ExcDatosInvalidos,
			ExcSeleccionNoValida, InterruptedException, ExcFiltroExcepcion,
			WrongValuesException, ExcEntradaInconsistente, ExcEntradaInvalida,
			SQLException, ExcArgumentoInvalido {

		if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {

			if (event.getTarget().getParent().getClass() == Row.class) {
				Row fila = (Row) event.getTarget().getParent();
				@SuppressWarnings("unchecked")
				CompBuscar<Almacen> almacenorigen = (CompBuscar<Almacen>) fila
						.getChildren().get(2);
				@SuppressWarnings("unchecked")
				CompBuscar<ArticuloVenta> articulodeventa = (CompBuscar<ArticuloVenta>) fila
						.getChildren().get(0);
				Doublebox cantidad = (Doublebox) fila.getChildren().get(1);

				if (event.getTarget() == (CompBuscar<ArticuloVenta>) fila
						.getChildren().get(0)) {

					if ((almacenorigen.getSeleccion() != null)
							&& (cantidad.getValue() != null)) {

						validarexsistencia(almacenorigen, articulodeventa,
								cantidad);

					}

				}

				System.out.println("ok");

				if (event.getTarget() == (CompBuscar<Almacen>) fila
						.getChildren().get(2)) {
					System.out.println("metodo 2");

					if ((articulodeventa.getSeleccion() != null)
							&& (cantidad.getValue() != null)) {

						validarexsistencia(almacenorigen, articulodeventa,
								cantidad);

					}

				}

				System.out.println("ok2");

			}

		} else if (event.getName().equals(Events.ON_CHANGE)) {

			if (event.getTarget().getParent().getClass() == Row.class) {
				Row fila = (Row) event.getTarget().getParent();
				@SuppressWarnings("unchecked")
				CompBuscar<Almacen> almacenorigen = (CompBuscar<Almacen>) fila
						.getChildren().get(2);
				@SuppressWarnings("unchecked")
				CompBuscar<ArticuloVenta> articulodeventa = (CompBuscar<ArticuloVenta>) fila
						.getChildren().get(0);
				Doublebox cantidad = (Doublebox) fila.getChildren().get(1);

				if (event.getTarget() == (Doublebox) fila.getChildren().get(1)) {

					if ((articulodeventa.getSeleccion() != null)
							&& (almacenorigen.getSeleccion() != null)) {

						validarexsistencia(almacenorigen, articulodeventa,
								cantidad);

					}

				}

			}

		}
		if (event.getName().equals("onOK")) {
			/*
			 * System.out.println(event.getTarget().toString());
			 * 
			 * @SuppressWarnings("unchecked") CompBuscar<ArticuloVenta>
			 * articulodeventa = (CompBuscar<ArticuloVenta>) event .getTarget();
			 * Consumible consumible = new PerConsumible()
			 * .getConsumiblearticulo(articulodeventa.getSeleccion()); new
			 * ContConsumible(Accion.CONSULTAR, consumible,
			 * vista.getAuxconsumible(), this.app);
			 */
		}

		// TODO Auto-generated method stub

	}

	public void validarexsistencia(CompBuscar<Almacen> almacenorigen,
			CompBuscar<ArticuloVenta> articulodeventa, Doublebox cantidad)
			throws ExcDatosInvalidos, ExcSeleccionNoValida,
			InterruptedException, ExcFiltroExcepcion {
		NegocioSalidaInternaArticulo negocio = servicio.getInstance();
		ArticuloVenta z = articulodeventa.getSeleccion();
		Almacen x = almacenorigen.getSeleccion();
		Double c = cantidad.getValue();
		boolean a = true;

		a = negocio.ValidarExistencia(z, x, c);

		if (!a) {
			int sa = Messagebox
					.show("El Articulo Seleccionado no esta presente quiere ver los equivalentes",
							"Advertencia", Messagebox.OK | Messagebox.CANCEL,
							Messagebox.QUESTION);
			System.out.println(sa);

			if (sa == 1) {
				mostrarEquivalente(z);
			}
			if (2 == sa) {
			}

		}

	}

	public void mostrarEquivalente(ArticuloVenta articulodeventa)
			throws ExcFiltroExcepcion, ExcDatosInvalidos, ExcSeleccionNoValida

	{
		Consumible consumible = new PerConsumible()
				.getConsumiblearticulo(articulodeventa);
		Herramienta herramienta = new PerHerramienta()
				.getHerramientaarticulo(articulodeventa);
		Repuesto repuesto = new PerRepuesto()
				.getRepuestoarticulo(articulodeventa);

		if (consumible != null) {
			new ContConsumible(Accion.CONSULTAR, consumible,
					vista.getAuxConsumible(), this.app);
		}

		if (herramienta != null) {
			new ContHerramienta(Accion.CONSULTAR, herramienta,
					vista.getAuxHerramienta(), this.app);
		}

		if (repuesto != null) {
			new ContRepuesto(Accion.CONSULTAR, repuesto,
					vista.getAuxRepuesto(), this.app);
		}

	}

}