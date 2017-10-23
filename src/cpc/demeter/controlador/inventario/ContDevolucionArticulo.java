package cpc.demeter.controlador.inventario;

import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Row;

import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.inventario.ContDevolucionesArticulos;
import cpc.demeter.vista.inventario.UiDevolucionArticulo;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.mantenimiento.DetalleDevolucionArticulo;
import cpc.modelo.demeter.mantenimiento.DetalleSalidaExternaArticulo;
import cpc.modelo.demeter.mantenimiento.DetalleSalidaInternaArticulo;
import cpc.modelo.demeter.mantenimiento.DevolucionArticulo;
import cpc.modelo.demeter.mantenimiento.MovimientoArticulo;
import cpc.modelo.demeter.mantenimiento.SalidaExternaArticulo;
import cpc.modelo.demeter.mantenimiento.SalidaInternaArticulo;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.negocio.demeter.mantenimiento.NegocioDevolucionArticulo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContDevolucionArticulo extends ContVentanaBase<DevolucionArticulo> {

	private NegocioDevolucionArticulo servicio;
	private AppDemeter app;
	private UiDevolucionArticulo vista;

	public ContDevolucionArticulo(int modoOperacion,
			DevolucionArticulo devolucionArticulo,
			List<Almacen> almacenesOrigen, List<Almacen> almacenesDestino,
			List<Trabajador> trabajadores,
			List<ClienteAdministrativo> destinatarios,

			List<MovimientoArticulo> movimientoArticulos,
			ContDevolucionesArticulos llamador, AppDemeter app) {
		super(modoOperacion);
		this.app = app;

		try {
			if (devolucionArticulo == null || modoAgregar()) {
				devolucionArticulo = new DevolucionArticulo();

			}

			setDato(devolucionArticulo);
			setear(getDato(), new UiDevolucionArticulo(
					"Devolucion de Articulos", 1200, modoOperacion,
					devolucionArticulo, almacenesOrigen, almacenesDestino,
					trabajadores, destinatarios, movimientoArticulos),
					llamador, app);
			vista = ((UiDevolucionArticulo) getVista());
			vista.desactivar(getModoOperacion());
			// prueba();

		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		}

	}

	@Override
	public void onEvent(Event event) throws Exception {
		// TODO Auto-generated method stub

		if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {

			if (event.getTarget() == vista.getCmbMovimientoArticulo()) {
				MostrarDetallesOriginal();
				vista.getCmbCliente().setDisabled(true);
				if (vista.getCmbMovimientoArticulo().getSeleccion().getClass()
						.equals(SalidaExternaArticulo.class)) {
					SalidaExternaArticulo salidae = (SalidaExternaArticulo) vista
							.getCmbMovimientoArticulo().getSeleccion();
					vista.getCmbCliente().setSeleccion(
							salidae.getDestinatario());
				}
			}
		}

		else if (event.getTarget() == getVista().getAceptar()) {
			validar();
			procesarCRUD(event);

		}

	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

		NegocioDevolucionArticulo negocio = servicio.getInstance();
		// getDato().setUsuario(app.getUsuario().getNombreIdentidad());
		// getDato().setFecha(new Date());

		if (vista.getCmbMovimientoArticulo().getSeleccion().getClass() == SalidaExternaArticulo.class) {
			getDato().setSalidaExternaArticulo(
					(SalidaExternaArticulo) vista.getCmbMovimientoArticulo()
							.getSeleccion());

			SalidaExternaArticulo salida = (SalidaExternaArticulo) vista
					.getCmbMovimientoArticulo().getSeleccion();
			vista.getCmbCliente().setSeleccion(salida.getDestinatario());
		}
		;

		if (vista.getCmbMovimientoArticulo().getSeleccion().getClass() == SalidaInternaArticulo.class) {
			getDato().setSalidaInternaArticulo(
					(SalidaInternaArticulo) vista.getCmbMovimientoArticulo()
							.getSeleccion());
		}

		List<DetalleDevolucionArticulo> detalleDevolucionArticulos = vista
				.getDetalle().getColeccion();

		getDato().setDetalleDevolucionArticulos(detalleDevolucionArticulos);
		getDato().setUsuario(app.getUsuario().toString());
		getDato().setFecha(new Date());
		getDato().setDestinatario(vista.getCmbCliente().getSeleccion());
		getDato().setTrabajador(vista.getCmbTrabajador().getSeleccion());
		negocio.setDevolucionArticulo(getDato());
		try {
			negocio.guardar(negocio.getDevolucionArticulo());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		if (vista.getCmbTrabajador().getSeleccion() == null) {
			throw new WrongValueException(vista.getCmbTrabajador(),
					"indique el Cliente que retira el articulo");
		}
		if (vista.getCmbMovimientoArticulo().getSeleccion() == null) {
			throw new WrongValueException(vista.getCmbMovimientoArticulo(),
					"indique el Movimiento al que se le hara la devolucion");
		}
		validarDetalle();
	}

	public void validarDetalle() throws WrongValuesException,
			ExcEntradaInconsistente {

		boolean z = vista.getDetalle().getFilas().getChildren().isEmpty();
		if (z) {
			throw new WrongValueException(vista.getDetalle(),
					"debe indicar al menos un articulo");
		}

		List<Row> filas = vista.getDetalle().getFilas().getChildren();

		for (Row row : filas) {

			CompBuscar<ArticuloVenta> articulo = (CompBuscar<ArticuloVenta>) row
					.getChildren().get(0);
			CompBuscar<Almacen> almacenOrigen = (CompBuscar<Almacen>) row
					.getChildren().get(2);

			Doublebox cantidad = (Doublebox) row.getChildren().get(1);
			CompBuscar<Almacen> almacendestino = (CompBuscar<Almacen>) row
					.getChildren().get(3);
			if (articulo.getSeleccion() == null) {
				throw new WrongValueException(articulo,
						"El Articulo no puede ser nulo");
			}

			if (almacenOrigen.getSeleccion() == null) {
				throw new WrongValueException(almacenOrigen,
						"indique el almacen de origen");
			}

			if (almacendestino.getSeleccion() == null) {
				throw new WrongValueException(almacenOrigen,
						"indique el almacen destino");
			}
			if (cantidad.getValue() == null) {
				throw new WrongValueException(cantidad,
						"la cantidad no puede ser nula");
			}

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

	public void MostrarDetallesOriginal() {
		vista.getDetalleSalidaExterna().clear();
		vista.getDetalleSalidaInterna().clear();
		MovimientoArticulo a = vista.getCmbMovimientoArticulo().getSeleccion();

		Class<? extends MovimientoArticulo> clase = a.getClass();
		SalidaInternaArticulo salidaInterna = new SalidaInternaArticulo();
		Class<? extends SalidaInternaArticulo> clase2 = salidaInterna
				.getClass();
		SalidaExternaArticulo salidaExterna = new SalidaExternaArticulo();
		Class<? extends SalidaExternaArticulo> clase3 = salidaExterna
				.getClass();

		if (a.getClass().equals(clase3)) {
			List<DetalleSalidaExternaArticulo> b = a
					.getDetalleSalidaExternaArticulos();
			vista.getDetalleSalidaExterna().clear();
			for (DetalleSalidaExternaArticulo detalleSalidaExternaArticulo : b) {
				vista.getDetalleSalidaExterna().agregar(
						detalleSalidaExternaArticulo);
				vista.getDetalleSalidaExterna().setVisible(true);
			}
		}

		if (a.getClass().equals(clase2)) {
			vista.getDetalleSalidaInterna().clear();
			List<DetalleSalidaInternaArticulo> c = a
					.getDetalleSalidaInternaArticulos();
			for (DetalleSalidaInternaArticulo detalleSalidaInternaArticulo : c) {
				vista.getDetalleSalidaInterna().agregar(
						detalleSalidaInternaArticulo);
				vista.getDetalleSalidaInterna().setVisible(true);
			}
		}

		cargarDetalles();

		desactivarColumnasDetalles();
	}

	public void cargarDetalles() {
		vista.getDetalle().clear();
		List<DetalleSalidaExternaArticulo> modelo = vista
				.getDetalleSalidaExterna().getModelo();

		List<DetalleSalidaInternaArticulo> modelo1 = vista
				.getDetalleSalidaInterna().getModelo();

		if (modelo != null) {
			for (DetalleSalidaExternaArticulo detalleSalidaExternaArticulo : modelo) {
				DetalleDevolucionArticulo aux = new DetalleDevolucionArticulo();
				aux.setAlmacenOrigen(detalleSalidaExternaArticulo
						.getAlmacenOrigen());
				aux.setArticuloVenta(detalleSalidaExternaArticulo
						.getArticuloVenta());
				aux.setCantidad(detalleSalidaExternaArticulo.getCantidad());
				vista.getDetalle().agregar(aux);
			}
		}

		if (modelo1 != null) {
			for (DetalleSalidaInternaArticulo detalleSalidaInternaArticulo : modelo1) {
				DetalleDevolucionArticulo aux = new DetalleDevolucionArticulo();
				aux.setAlmacenOrigen(detalleSalidaInternaArticulo
						.getAlmacenOrigen());
				aux.setArticuloVenta(detalleSalidaInternaArticulo
						.getArticuloVenta());
				aux.setCantidad(detalleSalidaInternaArticulo.getCantidad());
				vista.getDetalle().agregar(aux);
			}
		}

	}

	@SuppressWarnings("unchecked")
	public void desactivarColumnasDetalles() {
		List<Row> d = vista.getDetalle().getFilas().getChildren();
		for (Row row : d) {
			((CompBuscar<ArticuloVenta>) row.getChildren().get(0))
					.setDisabled(true);
			((CompBuscar<Almacen>) row.getChildren().get(2)).setDisabled(true);
		}

	}

}
