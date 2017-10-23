package cpc.demeter.controlador.mantenimiento;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.mantenimiento.UIContratoServicioTecnico;
import cpc.demeter.vista.mantenimiento.UICotizacion;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.demeter.administrativo.Cotizacion;
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.mantenimiento.Consumible;
import cpc.modelo.demeter.mantenimiento.DetalleSalidaExternaArticulo;
import cpc.modelo.demeter.mantenimiento.Herramienta;
import cpc.modelo.demeter.mantenimiento.Repuesto;
import cpc.modelo.demeter.mantenimiento.SalidaExternaArticulo;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.negocio.demeter.administrativo.NegocioCotizacion;
import cpc.negocio.demeter.mantenimiento.NegocioSalidaExternaArticulo;
import cpc.persistencia.demeter.implementacion.mantenimiento.PerConsumible;
import cpc.persistencia.demeter.implementacion.mantenimiento.PerHerramienta;
import cpc.persistencia.demeter.implementacion.mantenimiento.PerRepuesto;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.controlador.ContVentanaMaestroDetalle;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompGrilla;
import cpc.zk.componente.listas.CompGrillaConBoton;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcEntradaInvalida;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;
import cva.pc.demeter.utilidades.Real;

public class ContCotizacion extends ContVentanaBase<Cotizacion> implements
		EventListener {
	private NegocioCotizacion negocioCotizacion;
	private UICotizacion vistaCotizacion;
	private AppDemeter app;

	public ContCotizacion(int modoOperacion, Cotizacion cotizacion,
			ContCatalogo<Cotizacion> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcFiltroExcepcion {
		super(modoOperacion);
		// TODO Auto-generated constructor stub
		this.app = app;
		negocioCotizacion = NegocioCotizacion.getInstance();
		List<ArticuloVenta> articuloVentas = negocioCotizacion.getArticulos();

		List<Almacen> almacenes = negocioCotizacion.getAlmacenesSalidaExterna();
		List<DetalleContrato> detallesContrato = new ArrayList<DetalleContrato>();
		List<ClienteAdministrativo> clientes = negocioCotizacion
				.getClientesAdministrativos();
		SalidaExternaArticulo salidaExternaArticulo = new SalidaExternaArticulo();

		if (modoAgregar() || cotizacion == null) {

		} else {

			detallesContrato = cotizacion.getDetallesContrato();
			salidaExternaArticulo = cotizacion.getSalidaExternaArticulo();
		}

		setDato(cotizacion);
		setear(cotizacion, new UICotizacion("Cotizacion de Articulo", 1200,
				getModoOperacion(), articuloVentas, almacenes,
				detallesContrato, clientes, salidaExternaArticulo, cotizacion),
				llamador, app);
		// new UICotizacion("Cotizacion de Articulo", 730, articuloVentas,
		// almacenes, detallesContrato, clientes, salidaExternaArticulo,
		// cotizacion);
		vistaCotizacion = (UICotizacion) getVista();

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7124058797951366375L;

	@Override
	public void onEvent(Event event) throws Exception {

		System.out.println(event.getName());
		validarexistencia(event);
		ActualizarTotales(event);

		if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {

			if (event.getTarget() == vistaCotizacion.getCmbClientes()) {
            ClienteAdministrativo clienteAdministrativo=  negocioCotizacion.getClienteAdministrativo(vistaCotizacion.getCmbClientes().getSeleccion());
            vistaCotizacion.getCmbClientes().setSeleccion(clienteAdministrativo);
				vistaCotizacion.getLblDireccionPagador().setValue(
						vistaCotizacion.getCmbClientes().getSeleccion()
								.getCliente().getDireccion());
				vistaCotizacion.getLblCedulaRifPagado().setValue(
						vistaCotizacion.getCmbClientes().getSeleccion()
								.getCedulaCliente());
			}
		}

		if (event.getName().equals("onBorrarFila"))
			ActualizarTotales();

		if (event.getTarget() == getVista().getAceptar()) {
			validar();
			procesarCRUD(event);

		}

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
		ArticuloVenta z = articulodeventa.getSeleccion();
		Almacen x = almacenorigen.getSeleccion();
		Double c = cantidad.getValue();
		boolean a = true;

		a = negocioCotizacion.ValidarExistencia(z, x, c);

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

	@SuppressWarnings({ "unused", "unchecked" })
	public void ActualizarTotales(Event event) {

		if (event.getName().equals(Events.ON_CHANGE)
				|| event.getName().equals(CompBuscar.ON_SELECCIONO))

			if (event.getTarget().getParent().getClass() == Row.class) {
				Double acumuladobase = 0.0;
				Double acumuladoimpuesto = 0.0;
				// List<DetalleContrato> auxiliar3 =
				// vistaCotizacion.getDetalleContrato().getModelo();
				//
				vistaCotizacion.getDetalleContrato().getChildren().clear();
				vistaCotizacion.getDetalleContrato().getModelo().clear();
				// vistaCotizacion.getDetalleContrato().refrescar();

				// List<DetalleContrato> auxiliar2 =
				// vistaCotizacion.getDetalleContrato().getModelo();

				// Row fila = (Row) event.getTarget().getParent();

				List<Row> filas = vistaCotizacion.getDetalle().getFilas()
						.getChildren();

				for (Row item : filas) {
					ArticuloVenta articulodeventa = ((CompBuscar<ArticuloVenta>) item
							.getChildren().get(0)).getSeleccion();

					Double cantidad = ((Doublebox) item.getChildren().get(1))
							.getValue();

					if ((articulodeventa != null) && (cantidad != null)) {
						acumuladobase = acumuladobase
								+ (articulodeventa.getPrecioBase() * cantidad);
						acumuladoimpuesto = acumuladoimpuesto
								+ (articulodeventa.getIvaProducto() * cantidad);
						DetalleContrato detalleContrato = new DetalleContrato();
						detalleContrato.setCantidad(cantidad);
						detalleContrato.setProducto(articulodeventa);
						detalleContrato.setPrecioUnidad(articulodeventa
								.getPrecioBase());
						detalleContrato.setSubtotal(cantidad
								* articulodeventa.getPrecioBase());

						vistaCotizacion.getDetalleContrato().agregar(
								detalleContrato);
						// List<DetalleContrato> auxiliar =
						// vistaCotizacion.getDetalleContrato().getModelo();
						// System.out.println(auxiliar.size());
					}
				}

				vistaCotizacion.getBase().setValue(Real.redondeoMoneda(acumuladobase));
				vistaCotizacion.getImpuesto().setValue(Real.redondeoMoneda(acumuladoimpuesto));
			//	vistaCotizacion.getTotal().setValue(Real.redondeoMoneda(acumuladobase + acumuladoimpuesto));
				vistaCotizacion.getTotal().setValue(Real.redondeoMoneda(acumuladobase) + Real.redondeoMoneda(acumuladoimpuesto));

			}

	}

	public void mostrarEquivalente(ArticuloVenta articulodeventa)
			throws ExcFiltroExcepcion, ExcDatosInvalidos, ExcSeleccionNoValida

	{
		Consumible consumible = new PerConsumible()
				.getConsumiblearticulo(articulodeventa);
		if (consumible != null) {
			new ContConsumible(Accion.CONSULTAR, consumible,
					vistaCotizacion.getAuxConsumible(), this.app);
		}

		Herramienta herramienta = new PerHerramienta()
				.getHerramientaarticulo(articulodeventa);
		if (herramienta != null) {
			new ContHerramienta(Accion.CONSULTAR, herramienta,
					vistaCotizacion.getAuxHerramienta(), this.app);
		}
		Repuesto repuesto = new PerRepuesto()
				.getRepuestoarticulo(articulodeventa);

		if (herramienta != null) {
			new ContRepuesto(Accion.CONSULTAR, repuesto,
					vistaCotizacion.getAuxRepuesto(), this.app);
		}

	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		vistaCotizacion.actualizarModelo();
		Cotizacion a = getDato();
		getDato().setPagador(
				vistaCotizacion.getCmbClientes().getSeleccion().getCliente());
		List<DetalleContrato> detallecontrato = vistaCotizacion
				.getDetalleContrato().getModelo();
		for (DetalleContrato detalleContrato2 : detallecontrato) {
			detalleContrato2.setContrato(getDato());
			detalleContrato2.setImpuesto(detalleContrato2.getProducto().getImpuesto());
			detalleContrato2.setPrestado(true);
		}
		getDato().setDetallesContrato(detallecontrato);
		SalidaExternaArticulo salidaExternaArticulo = new SalidaExternaArticulo();
		List<DetalleSalidaExternaArticulo> sa = vistaCotizacion.getDetalle()
				.getColeccion();
		for (DetalleSalidaExternaArticulo detalleSalidaExternaArticulo : sa) {
			detalleSalidaExternaArticulo.setMovimiento(salidaExternaArticulo);
		}
		salidaExternaArticulo.setDetalleSalidaExternaArticulos(sa);
		salidaExternaArticulo.setUsuario(app.getUsuario().getNombreIdentidad());
		salidaExternaArticulo.setDestinatario(vistaCotizacion.getCmbClientes()
				.getSeleccion());
		salidaExternaArticulo.setCotizacion(getDato());

		getDato().setSalidaExternaArticulo(salidaExternaArticulo);
		getDato().setTotal(vistaCotizacion.getTotal().getValue());
		getDato().setMonto(vistaCotizacion.getBase().getValue());
		getDato().setSaldo(vistaCotizacion.getTotal().getValue());
		getDato().setTipoCotizacion(negocioCotizacion.getMantenimiento());
		Cotizacion as = getDato();

		try {
			negocioCotizacion.guardar(getDato());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		ActualizarTotales();
		// TODO Auto-generated method stub

	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		if (vistaCotizacion.getCmbClientes().getSeleccion() == null)
			throw new WrongValueException(vistaCotizacion.getCmbClientes(),
					"Debe seleccionar un cliente");
		List<Row> filas = vistaCotizacion.getDetalle().getFilas().getChildren();
		boolean z = vistaCotizacion.getDetalle().getFilas().getChildren()
				.isEmpty();
		if (z) {
			throw new WrongValueException(vistaCotizacion.getDetalle(),
					"debe indicar al menos un articulo");
		}

		validarDetalle();
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

	@SuppressWarnings("unchecked")
	public void ActualizarTotales() {
		Double acumuladobase = 0.0;
		Double acumuladoimpuesto = 0.0;
		// List<DetalleContrato> auxiliar3 =
		// vistaCotizacion.getDetalleContrato().getModelo();
		//
		vistaCotizacion.getDetalleContrato().getChildren().clear();
		vistaCotizacion.getDetalleContrato().getModelo().clear();
		// vistaCotizacion.getDetalleContrato().refrescar();

		// List<DetalleContrato> auxiliar2 =
		// vistaCotizacion.getDetalleContrato().getModelo();

		// Row fila = (Row) event.getTarget().getParent();

		List<Row> filas = vistaCotizacion.getDetalle().getFilas().getChildren();

		for (Row item : filas) {

			ArticuloVenta articulodeventa = ((CompBuscar<ArticuloVenta>) item
					.getChildren().get(0)).getSeleccion();

			Double cantidad = ((Doublebox) item.getChildren().get(1))
					.getValue();

			if ((articulodeventa != null) && (cantidad != null)) {
				acumuladobase = acumuladobase
						+ (articulodeventa.getPrecioBase() * cantidad);
				acumuladoimpuesto = acumuladoimpuesto
						+ (articulodeventa.getIvaProducto() * cantidad);
				DetalleContrato detalleContrato = new DetalleContrato();
				detalleContrato.setCantidad(cantidad);
				detalleContrato.setProducto(articulodeventa);
				detalleContrato
						.setPrecioUnidad(articulodeventa.getPrecioBase());
				detalleContrato.setSubtotal(cantidad
						* articulodeventa.getPrecioBase());

				vistaCotizacion.getDetalleContrato().agregar(detalleContrato);
				// List<DetalleContrato> auxiliar =
				// vistaCotizacion.getDetalleContrato().getModelo();
				// System.out.println(auxiliar.size());
			}
		}

		vistaCotizacion.getBase().setValue(Real.redondeoMoneda(acumuladobase));
		vistaCotizacion.getImpuesto().setValue(Real.redondeoMoneda(acumuladoimpuesto));
		vistaCotizacion.getTotal().setValue(Real.redondeoMoneda(acumuladobase) + Real.redondeoMoneda(acumuladoimpuesto));

	}

	@SuppressWarnings({ "unchecked", "unused" })
	public void validarDetalle() throws WrongValuesException,
			ExcEntradaInconsistente {
		List<Row> filas = vistaCotizacion.getDetalle().getFilas().getChildren();

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

			if (cantidad.getValue() == null || cantidad.getValue().equals(0)) {
				throw new WrongValueException(cantidad,
						"la cantidad no puede ser nula o cero");
			}

		}
	}

}
