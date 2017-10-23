package cpc.demeter.controlador.administrativo;

import java.sql.SQLException;
import java.util.List;

import org.hibernate.HibernateException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.ContTipoDocumentoTierra;
import cpc.demeter.vista.administrativo.UIContratoMecanizado;
import cpc.modelo.demeter.administrativo.ContratoMecanizado;
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.administrativo.PlanFinanciamiento;
import cpc.modelo.demeter.administrativo.TipoContrato;
import cpc.modelo.demeter.gestion.SolicitudMecanizado;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.ministerio.basico.TipoProductor;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.negocio.demeter.administrativo.NegocioContratoMecanizado;
import cpc.persistencia.demeter.implementacion.administrativo.PerEstadoContrato;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.controlador.ContVentanaMaestroDetalle;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompGrilla;
import cva.pc.demeter.excepciones.ExcAgregacionInvalida;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;
import cva.pc.demeter.utilidades.Fecha;
import cva.pc.demeter.utilidades.Formateador;
import cva.pc.demeter.utilidades.Real;

public class ContContratoMecanizado extends
		ContVentanaMaestroDetalle<ContratoMecanizado, DetalleContrato> {

	private static final long serialVersionUID = 3482809265694914317L;
	private NegocioContratoMecanizado negocioContrato;
	private UIContratoMecanizado vistaContrato;
	private AppDemeter app;

	public ContContratoMecanizado(int modoOperacion,
			ContratoMecanizado contrato,
			ContCatalogo<ContratoMecanizado> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		List<Cliente> clientes = null;
		List<SolicitudMecanizado> solicitudes = null;
		List<PlanFinanciamiento> planesFinaciamineto = null;
		List<TipoContrato> tiposContrato = null;
		List<IProducto> articulos = null;

		negocioContrato = NegocioContratoMecanizado.getInstance();

		try {
			clientes = negocioContrato.getClientesProject();
			
			planesFinaciamineto = negocioContrato.getPlanesFinanciamientos();
			tiposContrato = negocioContrato.getTiposContrato();
			articulos = negocioContrato.getProductos();
			solicitudes = negocioContrato.getSolicitudes();
			if (contrato.getId() == null)
				contrato = new ContratoMecanizado();
			else {
				contrato = negocioContrato.getDetallesDeContrato(contrato);
				// contrato = negocioContrato.getCuotasContrato(contrato);
				contrato.setPagador(negocioContrato.getCliente(contrato
						.getPagador()));
				negocioContrato.setContrato(contrato);
			}

		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
		}
		setear(contrato, new UIContratoMecanizado("Contrato Mecanizado", 730,
				clientes, solicitudes, tiposContrato, planesFinaciamineto,
				articulos), llamador, app);
		vistaContrato = (UIContratoMecanizado) getVista();
		vistaContrato.desactivarDetalle();
		if (modoOperacion == Accion.CONSULTAR)
			vistaContrato.setModoVer();
		if (modoOperacion == Accion.PROCESAR) {
			vistaContrato.setModoVer();
			activarRealizado();
		}
	}

	public void eliminar() {

	}

	public void guardar() {
		getVista().actualizarModelo();
		try {
			negocioContrato.setContrato(cargarModelo());
			negocioContrato.guardar(negocioContrato.getContrato());
		} catch (HibernateException e) {
			e.printStackTrace();
			try {
				Messagebox.show(e.getMessage(), "Error de Persistencia ",
						Messagebox.OK, Messagebox.ERROR);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
			try {
				Messagebox.show(e.getMessage(), "Error de Persistencia ",
						Messagebox.OK, Messagebox.ERROR);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		} catch (ExcAgregacionInvalida e) {
			e.printStackTrace();
			try {
				Messagebox.show(e.getMessage(), "Error actualizado detalle",
						Messagebox.OK, Messagebox.ERROR);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public ContratoMecanizado cargarModelo() throws ExcAgregacionInvalida {
		ContratoMecanizado ctto = getDato();
		ctto.setDetallesContrato(vistaContrato.getModeloDetalle());
		ctto.setPagador(vistaContrato.getCedulaPagador().getSeleccion());
		ctto.setUnidadProductiva(vistaContrato.getUnidadProduccion()
				.getSeleccion());
		ctto.setMonto(vistaContrato.getTxtTotal().getValue());
		ctto.setFacturado(false);
		if (vistaContrato.getTipoContrato().getSeleccion().getId() != 3)
			ctto.setPagador(vistaContrato.getCedulaProductor().getSeleccion());
		// ctto.setCuotasAPagarCliente(vista.getCuotasAPagarCliente());
		double total = 0.0;
		for (DetalleContrato item : ctto.getDetallesContrato()) {
			item.setContrato(ctto);
			item.setImpuesto(item.getProducto().getImpuesto());
			item.setPrestado(false);
			total += Real
					.redondeoMoneda(item.getSubtotal()
							* (1 + item.getProducto().getImpuesto()
									.getPorcentaje() / 100));
		}
		if (ctto.getTipoContrato().getId() == 2) {
			ctto.setTotal(0.0);
			ctto.setSaldo(0.0);
		} else {
			ctto.setTotal(total);
			ctto.setSaldo(total);
		}
		/*
		 * for (CuotasAPagarCliente item : ctto.getCuotasAPagarCliente()) {
		 * item.setContrato(ctto); item.setPagador(ctto.getPagador()); }
		 */
		return ctto;
	}

	@SuppressWarnings("unchecked")
	private void validarDetalle() throws WrongValuesException,
			ExcEntradaInconsistente {
		List<Row> filas = vistaContrato.getDetalle().getFilas().getChildren();
		if (filas.size() < 1)
			throw new WrongValueException(vistaContrato.getDetalle(),
					"Indique Servicios");
		CompBuscar<IProducto> servicio;
		Doublebox cantidad;
		for (Row item : filas) {
			servicio = (CompBuscar<IProducto>) item.getChildren().get(0);
			cantidad = (Doublebox) item.getChildren().get(1);
			if (servicio.getSeleccion() == null)
				throw new WrongValueException(servicio,
						"Servicio no seleccionado");
			if (cantidad.getValue() == null)
				throw new WrongValueException(cantidad, "Indicar cantidad");

		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (vistaContrato.getTxtFechaDesde().getValue()
				.before(new Fecha().getTime()))
			throw new WrongValueException(vistaContrato.getTxtFechaDesde(),
					"El inicio de vigencia del Contrato debe ser posterior al dia de Hoy");
		if (vistaContrato.getTxtDiasVigencia().getValue() == null
				|| vistaContrato.getTxtDiasVigencia().getValue() <= 0)
			throw new WrongValueException(vistaContrato.getTxtDiasVigencia(),
					"Valor invalido");
		/*
		 * if (vista.getSolicitud().getSeleccion()==null) throw new
		 * WrongValueException(vista.getSolicitud(),"Seleccione una Solicitud");
		 */
		if (vistaContrato.getUnidadProduccion().getSeleccion() == null)
			throw new WrongValueException(vistaContrato.getUnidadProduccion(),
					"Seleccione la unidad de produccion");
		if (vistaContrato.getTipoContrato().getSeleccion() == null)
			throw new WrongValueException(vistaContrato.getCmbTipoContrato(),
					"Seleccione la tipo de Contrato");
		else if (vistaContrato.getCedulaPagador().getSeleccion() == null
				&& vistaContrato.getTipoContrato().getSeleccion().getId() == 3)
			throw new WrongValueException(vistaContrato.getCedulaPagador(),
					"Seleccione un Pagador");
		else if (vistaContrato.getPlanFinanciamiento().getSeleccion() == null
				&& vistaContrato.getTipoContrato().getSeleccion().getId() == 1)
			throw new WrongValueException(
					vistaContrato.getPlanFinanciamiento(),
					"Seleccione un tipo finanaciamiento");
		/*
		 * if (vista.getPlanFinanciamiento().getSeleccion()==null) throw new
		 * WrongValueException
		 * (vista.getPlanFinanciamiento(),"Seleccione un Plan de Financiamiento"
		 * );
		 */
		if (Double.parseDouble(vistaContrato.getLblMontoContrato().getValue()) <= 0.00)
			throw new WrongValueException(vistaContrato.getAceptar(),
					"Imposible guardar un Contrato con monto = 0.00");
		validarDetalle();
	}

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		System.out.println("hay evento " + event.getName() + " en "
				+ event.getTarget().getClass().getName());
		if (event.getTarget() == getVista().getAceptar()) {
			if (!modoAnular() || !modoProcesar())
				procesarCRUD(event);
			if (modoProcesar())
				procesar();
			if (modoAnular())
				anular();
		} else if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {
			if (event.getTarget() == vistaContrato.getCedulaPagador()) {
				Cliente cliente = vistaContrato.getCedulaPagador()
						.getSeleccion();
				cliente = negocioContrato.getInstance().getClienteProject(cliente);
				actualizarPagador(cliente);
			} else if (event.getTarget() == vistaContrato.getCedulaProductor()) {
				Cliente cliente = vistaContrato.getCedulaProductor()
						.getSeleccion();
				actualizarProductor(cliente);
			} else if (event.getTarget() == vistaContrato.getSolicitud()) {
				SolicitudMecanizado solicitud = vistaContrato.getSolicitud()
						.getSeleccion();
				actualizarDatosSolicitud(solicitud);
				vistaContrato.desactivarDetalle();
			} else
				actualizarServicio((CompBuscar<IProducto>) event.getTarget());
		} else if (event.getName().equals(Events.ON_CHANGE))
			actualizarPrecio((Doublebox) event.getTarget());
		else if (event.getName().equals(CompGrilla.ON_ELIMINA_FILA))
			eliminadato((Row) event.getData());
		else if (event.getTarget() == vistaContrato.getTipoContrato())
			actualizarTipoContrato(vistaContrato.getTipoContrato()
					.getSeleccion());
		else if (event.getTarget() == vistaContrato.getPlanFinanciamiento())
			actualizarFinanciamiento();
	}

	public void actualizarPagador(Cliente cliente) throws ExcFiltroExcepcion {
		if (cliente != null) {
			cliente = negocioContrato.getCliente(cliente);
			vistaContrato.getLblNombrePagador().setValue(cliente.getNombres());
			vistaContrato.getLblDireccionPagador().setValue(
					cliente.getDireccion());
		}
	}

	public void actualizarTipoContrato(TipoContrato tipo) {
		if (vistaContrato == null)
			vistaContrato = (UIContratoMecanizado) getVista();
		switch (tipo.getId()) {
		case 1:
			// GRAVADO
			vistaContrato.getGrpPlanFinanciamiento().setVisible(true);
			vistaContrato.getGrpPagador().setVisible(false);
			break;
		case 2:
			// Exonerado
			vistaContrato.getGrpPlanFinanciamiento().setVisible(false);
			vistaContrato.getGrpPagador().setVisible(false);
			break;
		case 3:
			// pagado por otro
			vistaContrato.getGrpPlanFinanciamiento().setVisible(false);
			vistaContrato.getGrpPagador().setVisible(true);
		}
	}

	public void actualizarDatosSolicitud(SolicitudMecanizado solicitud)
			throws ExcFiltroExcepcion {
		if (solicitud != null) {
			Cliente cliente = solicitud.getProductor();
			actualizarProductor(cliente);
			vistaContrato.getUnidadProduccion().setSeleccion(
					solicitud.getDireccion());
			vistaContrato.getDetalle().clear();
			vistaContrato.getTxtTotal().setValue(0.00);
			vistaContrato.getLblMontoContrato().setValue(
					Formateador.formatearMoneda(0.00));
			List<DetalleContrato> detalles = negocioContrato
					.getDetalleSolicitud(solicitud);
			if (detalles != null) {
				for (DetalleContrato item : detalles) {
					vistaContrato.getDetalle().agregar(item);
				}
			}
			actualizarTotales();
		}
	}

	public void actualizarProductor(Cliente cliente) throws ExcFiltroExcepcion {
		if (cliente != null) {
			vistaContrato.getCedulaProductor().setSeleccion(cliente);
			cliente = negocioContrato.getCliente(cliente);
			vistaContrato.getLblNombreSolicitante().setValue(
					cliente.getNombres());
			vistaContrato.getUnidadProduccion().setModelo(
					negocioContrato.getDirecciones(cliente));
		}
	}

	public void actualizarDistribucionPago() throws NumberFormatException,
			InterruptedException {
		if (vistaContrato.getBtnVerDistribucionPago().getId()
				.equals("btnCalcular")) {
			PlanFinanciamiento plan = vistaContrato.getPlanFinanciamiento()
					.getSeleccion();
			if (plan != null)
				if (Double.parseDouble(vistaContrato.getLblMontoDeudor()
						.getValue()) > 0.00000)
					vistaContrato.mostrarVentanaInfoFinanciamiento(Double
							.parseDouble(vistaContrato.getLblMontoDeudor()
									.getValue()), plan.getDefCuotas());
				else {
					throw new WrongValueException(
							vistaContrato.getBtnVerDistribucionPago(),
							"Seleccione un Plan de Financiamiento. Asegurese de que la deuda del Pagador es mayor de cero (0.00)");
				}
		} else if (vistaContrato.getBtnVerDistribucionPago().getId()
				.equals("btnVer")) {
			vistaContrato.mostrarCuotasAPagarDelCliente(getVista().getModelo()
					.getCuotasAPagarCliente());
		}
	}

	/*
	 * public void actualizarFinanciamiento(){ PlanFinanciamiento plan =
	 * vista.getPlanFinanciamiento().getSeleccion(); if (plan!=null){
	 * vista.getLblObjetoPlan().setValue(plan.getObjetoVenta());
	 * vista.getLblTipo
	 * ().setValue(plan.getTipoFinanciamiento().getDescripcion().toUpperCase());
	 * vista.getLblPorcInicial().setValue(""+plan.getPorcentajeInicial());
	 * vista.getLblPorcDescuento().setValue(""+plan.getPorcentajeDescuento());
	 * vista.dibujarFinanciamiento(); if (plan.isObjetoVenta()){
	 * 
	 * } } }
	 */

	public void actualizarFinanciamiento() {
		try {
			PlanFinanciamiento plan = vistaContrato.getPlanFinanciamiento()
					.getSeleccion();
			Double monto = 0.0;
			Double inicial = 0.0;
			Double descuento = 0.0;
			if (vistaContrato.getLblMontoContrato().getValue() != "")
				monto = Double.valueOf(vistaContrato.getLblMontoContrato()
						.getValue());
			if (plan != null) {
				vistaContrato.getLblPorcInicial().setValue(
						"" + plan.getPorcentajeInicial());
				vistaContrato.getLblPorcDescuento().setValue(
						"" + plan.getPorcentajeDescuento());
				descuento = Real.redondeoMoneda(monto
						* (plan.getPorcentajeDescuento() / 100.0));
				vistaContrato.getLblMontoDescuento().setValue("" + descuento);
				inicial = Real.redondeoMoneda((monto - descuento)
						* (plan.getPorcentajeInicial() / 100.0));
				vistaContrato.getLblMontoInicial().setValue("" + inicial);
				vistaContrato.getLblMontoDeudor().setValue(
						"" + Real.redondeoMoneda(monto - inicial - descuento));
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
	}

	public void actualizarServicio(CompBuscar<IProducto> componente)
			throws ExcFiltroExcepcion {
		System.out.println("actualizarServicio");
		IProducto producto = null;
		negocioContrato = NegocioContratoMecanizado.getInstance();
		Row registro = (Row) componente.getParent();
		try {
			producto = componente.getSeleccion();
			producto = negocioContrato.enriqueserProducto(producto);
		} catch (NullPointerException e) {
			throw new ExcFiltroExcepcion(
					"Problemas con el producto seleccionado");
		}
		TipoProductor tipo = null;
		try {
			if (vistaContrato.getTipoContrato().getSeleccion().getId() == 3)
				tipo = vistaContrato.getCedulaPagador().getSeleccion()
						.getTipo();
			else
				tipo = vistaContrato.getCedulaProductor().getSeleccion()
						.getTipo();
		} catch (NullPointerException e) {
			throw new ExcFiltroExcepcion("No ha seleccionado el Productor");
		}
		try {
			Double precio = Real.redondeoMoneda(producto.getPrecioBase(tipo));
			System.out.printf("precio %.2f", precio);
			((Doublebox) registro.getChildren().get(2)).setValue(precio);
			Double cantidad = ((Doublebox) registro.getChildren().get(1))
					.getValue();
			if (cantidad == null) {
				((Doublebox) registro.getChildren().get(1)).setValue(1.0);
				cantidad = 1.0;
			}
			((Doublebox) registro.getChildren().get(3)).setValue(Real
					.redondeoMoneda(precio * cantidad));
			actualizarTotales();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcFiltroExcepcion("Problemas con el servicio");
		}
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public void actualizarTotales() throws ExcFiltroExcepcion {
		double bruto = 0;
		double valor;
		double desc = 0;
		double acuDesc = 0;
		List<Row> filas = vistaContrato.getDetalle().getFilas().getChildren();
		acuDesc = 0;
		for (Row item : filas) {
			valor = ((Doublebox) item.getChildren().get(3)).getValue();
			bruto += Real.redondeoMoneda(valor);
			acuDesc += Real.redondeoMoneda(desc);
		}
		vistaContrato.getTxtTotal().setValue(bruto);
		vistaContrato.getLblMontoContrato().setValue("" + bruto);
		actualizarFinanciamiento();
		/*
		 * neto = bruto-acuDesc; double impuestoTotal =
		 * servicio.getTotalImpuestoFactura(impuestosFactura); total =
		 * neto+impuestoTotal; vista.getSubTotal().setValue(bruto);
		 * vista.getDescuento().setValue(acuDesc); for (int i=0;
		 * i<impuestosFactura.size();i++){
		 * vista.getBaseimpuesto()[i].setValue(impuestosFactura
		 * .get(i).getBase());
		 * vista.getImpuesto()[i].setValue(impuestosFactura.get(i).getMonto());
		 * } vista.getNeto().setValue(neto); vista.getTotal().setValue(total);
		 */
	}

	public void actualizarPrecio(Doublebox componente)
			throws ExcFiltroExcepcion {
		try {
			Row registro = (Row) componente.getParent();
			double precio = ((Doublebox) registro.getChildren().get(2))
					.getValue();
			((Doublebox) registro.getChildren().get(3)).setValue(componente
					.getValue() * precio);
			actualizarTotales();
		} catch (Exception e) {
			throw new ExcFiltroExcepcion("Problemas con el servicio");
		}
	}

	public void eliminadato(Row fila) throws ExcFiltroExcepcion {
		actualizarTotales();
	}

	public void anular() {
		/*
		 * getVista().actualizarModelo();
		 * negocioContrato.setContrato(vistaContrato.getModelo());
		 * vistaContrato.
		 * actualizarModeloDetalleContrato(vistaContrato.getModelo());
		 */
		try {
			negocioContrato.anular(negocioContrato.getContrato());
			refrescarCatalogo();
			vistaContrato.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void procesar() {
		getVista().actualizarModelo();
		try {
			validarprocesar();
		} catch (WrongValuesException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			app.mostrarError(e1.getMessage());
		} catch (ExcEntradaInconsistente e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			app.mostrarError(e1.getMensajeError());
		}
		ContratoMecanizado ctto = getDato();
		negocioContrato.setContrato(ctto);
		try {
			negocioContrato.procesar(negocioContrato.getContrato());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		refrescarCatalogo();
		vistaContrato.close();

	}

	private void validarprocesar() throws WrongValuesException,
			ExcEntradaInconsistente {
		List<Row> filas = vistaContrato.getDetalle().getFilas().getChildren();

		Doublebox cantidadreal;
		for (Row item : filas) {

			cantidadreal = (Doublebox) item.getChildren().get(4);

			if (cantidadreal.getValue() == null)
				throw new WrongValueException(cantidadreal,
						"Debe Indicar la cantidad real");

		}
	}

	private void activarRealizado() {
		List<Row> filas = vistaContrato.getDetalle().getFilas().getChildren();
		for (Row row : filas) {
			Doublebox servicio = (Doublebox) row.getChildren().get(4);
			servicio.setDisabled(false);
		}
	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

}
