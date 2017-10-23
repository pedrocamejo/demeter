package cpc.demeter.controlador.transporte;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.vista.transporte.UICotizacionTransporte;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.demeter.administrativo.CotizacionTransporte;
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.demeter.transporte.UbicacionTransporte;
import cpc.modelo.ministerio.basico.TipoProductor;
import cpc.modelo.sigesp.basico.CuentaBancaria;
import cpc.negocio.demeter.NegocioCuentaBancaria;
import cpc.negocio.demeter.administrativo.NegocioClienteAdministrativo;
import cpc.negocio.demeter.administrativo.NegocioCotizacionTransporte;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompGrilla;
import cpc.zk.componente.listas.ContVentanaBase;
import cpc.zk.componente.ventanas.CompGrupoBusqueda;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.utilidades.Fecha;
import cva.pc.demeter.utilidades.Real;

public class ContCotizacionTransporte extends
		ContVentanaBase<CotizacionTransporte> implements EventListener {
	private NegocioCotizacionTransporte negocioCotizacion;
	private UICotizacionTransporte vistaCotizacion;
	private AppDemeter app;

	public ContCotizacionTransporte(int modoOperacion,
			CotizacionTransporte cotizacion,
			ContCatalogo<CotizacionTransporte> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcFiltroExcepcion {
		super(modoOperacion);
		// TODO Auto-generated constructor stub
		this.app = app;
		negocioCotizacion = NegocioCotizacionTransporte.getInstance();
		List<IProducto> articuloVentas ;
		
		if (modoAgregar() ) {
		articuloVentas = negocioCotizacion.getServiciosTransporteActivos() ;
		}else { articuloVentas = negocioCotizacion.getServiciosTransporte();}
		

		List<DetalleContrato> detallesContrato = new ArrayList<DetalleContrato>();
		List<ClienteAdministrativo> clientes = negocioCotizacion
				.getClientesAdministrativos();
		List<UbicacionTransporte> ubicacionTransportes = negocioCotizacion
				.getUbicacionesTransportes();
		

		if (modoAgregar() || cotizacion == null) {

		} else {

			detallesContrato = cotizacion.getDetallesContrato();

		}

		setDato(cotizacion);

		setear(cotizacion, new UICotizacionTransporte("Cotizacion de Transportes",
				800, getModoOperacion(), articuloVentas, detallesContrato,
				clientes, ubicacionTransportes, cotizacion),
				llamador, app);
		// new UICotizacion("Cotizacion de Articulo", 730, articuloVentas,
		// almacenes, detallesContrato, clientes, salidaExternaArticulo,
		// cotizacion);
		vistaCotizacion = (UICotizacionTransporte) getVista();
		if(modoAgregar()){
		vistaCotizacion.getActivar().detach();
		vistaCotizacion.getRechazar().detach();
		}
		if (modoEditar()){
			vistaCotizacion.getActivar().detach();
			vistaCotizacion.getRechazar().detach();
			vistaCotizacion.desactivarEditar();}
		if (modoAnular() || modoConsulta()){
			vistaCotizacion.desactivarAnular();
		}
		if (modoProcesar()){
			vistaCotizacion.desactivarProcesar();
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7124058797951366375L;

	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(Event event) throws Exception {

		if (event.getTarget()==vistaCotizacion.getOrigen().getCrear()){
			new ContUbicacionTransporte(Accion.AGREGAR, null, vistaCotizacion.getOrigen(), app);
		}
		
		if (event.getTarget()==vistaCotizacion.getDestino().getCrear()){
			new ContUbicacionTransporte(Accion.AGREGAR, null, vistaCotizacion.getDestino(), app);
		}
		
		System.out.println(event.getName());
		// validarexistencia(event);
		ActualizarTotales(event);

		if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {
		
			if (event.getTarget() == vistaCotizacion.getCmbClientes()) {
				ClienteAdministrativo clienteenriquesido = vistaCotizacion
						.getCmbClientes().getSeleccion();
				clienteenriquesido = negocioCotizacion
						.getClienteAdministrativo(clienteenriquesido);
				vistaCotizacion.getCmbClientes().setSeleccion(
						clienteenriquesido);
				vistaCotizacion.getLblDireccionPagador().setValue(
						vistaCotizacion.getCmbClientes().getSeleccion()
								.getCliente().getDireccion());
				vistaCotizacion.getLblCedulaRifPagado().setValue(
						vistaCotizacion.getCmbClientes().getSeleccion()
								.getCedulaCliente());
				
				 NegocioClienteAdministrativo negocio = NegocioClienteAdministrativo.getInstance();
				 Object[] deudor = negocio.getPendienteDocumentosNotasTotales(clienteenriquesido.getCliente());
				 
				 //[0]cliente
				 //[1]List<DocumentosFiscales>
				 //[2]List<NotasCargo>
				 //[3]TotalDocumentosFiscales
				 //[4]TotalNotasCargo
				 
				 if ((((Double)deudor[3]+(Double)deudor[4]))>0){
					  Messagebox.show("Este Productor Posee Deudas Asociadas con la Institucion ");
					List<Object[]> deudores = new ArrayList<Object[]>();
					deudores.add(deudor);
						HashMap parametros = new HashMap();
						parametros.put("usuario", app.getUsuario().toString());
						parametros.put("logo", Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");

					
						JRDataSource ds = null;
						ds = new JRBeanCollectionDataSource(deudores);
						app.agregarReporte();
						Jasperreport reporte = app.getReporte();
						reporte.setSrc("reportes/reporteconsolidadodedeudas.jasper");
						reporte.setParameters(parametros);
						reporte.setDatasource(ds);
						reporte.setType("pdf");
					
					 
				 }
					 
					 
				 
				//se muestra ventada de advertencia si el productor debe 
			/*	   NegocioClienteAdministrativo negocio = NegocioClienteAdministrativo.getInstance();
				   List<DocumentoFiscal> documentos = negocio.getDocumentosFiscalesActivos(clienteenriquesido.getCliente());
				   if(documentos.size() != 0)
				   {
					   Messagebox.show("Este Productor Posee Deudas Asociadas con la Institucion ");
					   try {
								NegocioCuentaBancaria cuentas = NegocioCuentaBancaria.getInstance();
								String a = new String();
								HashMap parametros = new HashMap();
								parametros.put("usuario", app.getUsuario().toString());
								parametros.put("logo", Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");

								List<CuentaBancaria> b = cuentas.CuentaBancarias("007");
								for (CuentaBancaria cuentaBancaria : b)
								{   if (cuentaBancaria != null)
									{
										String c = (cuentaBancaria.getStrBanco() + " " + " " + cuentaBancaria.getNroCuenta() + " " + "<br>");
										if (c != null) {a = a + c;}
									}
								}
								parametros.put("cuenta", a);
								JRDataSource ds = null;
								ds = new JRBeanCollectionDataSource(documentos);
								app.agregarReporte();
								Jasperreport reporte = app.getReporte();
								reporte.setSrc("reportes/clienteAdministrativoFiltro.jasper");
								reporte.setParameters(parametros);
								reporte.setDatasource(ds);
								reporte.setType("pdf");
							
							} catch (Exception e) {
								e.printStackTrace();
								throw new Exception("Error al Intentar Generar el Reporte ");
							}
				   }
			*/} else if(isDirecciones(event)) {	ActualizarDireccion(event);}
			else
				actualizarArticulo((CompBuscar<IProducto>) event.getTarget());
		} else if (event.getName().equals(Events.ON_CHANGING))
			ActualizarFecha();
		else if (event.getName().equals(Events.ON_CHANGE)) {
			if (event.getTarget() == vistaCotizacion.getLblDiasVigencia())
				ActualizarFecha();
			else if (event.getTarget() == vistaCotizacion.getDtmFechaInicio())
				ActualizarFecha();
			else
				actualizarPrecio((Doublebox) event.getTarget());
		}
		if (event.getName().equals("onBorrarFila"))
			ActualizarTotales();
		else if (event.getName().equals(CompGrilla.ON_ELIMINA_FILA))

			ActualizarTotales();
		if (event.getTarget() == getVista().getAceptar()) {
			validar();
			procesarCRUD(event);

		}
		if (event.getName().equals(Events.ON_CLICK)){
			if (event.getTarget().equals(vistaCotizacion.getRechazar())){
				getDato().setEstado(negocioCotizacion.getInstance().getEstadoRechazado());
				vistaCotizacion.getLblEstado().setValue(getDato().getEstadoString());
				vistaCotizacion.getTxtMotivoRechazo().setVisible(true);
				
			}
			if (event.getTarget().equals(vistaCotizacion.getActivar())){
				getDato().setEstado(negocioCotizacion.getInstance().getEstadoActivo());
				vistaCotizacion.getLblEstado().setValue(getDato().getEstadoString());
			}
		}

	}

	/*
	 * public void validarexsistencia(CompBuscar<Almacen> almacenorigen,
	 * CompBuscar<ArticuloVenta> articulodeventa, Doublebox cantidad) throws
	 * ExcDatosInvalidos, ExcSeleccionNoValida, InterruptedException,
	 * ExcFiltroExcepcion { ArticuloVenta z = articulodeventa.getSeleccion();
	 * Almacen x = almacenorigen.getSeleccion(); Double c = cantidad.getValue();
	 * boolean a = true;
	 * 
	 * a = negocioCotizacion.ValidarExistencia(z, x, c);
	 * 
	 * if (!a) { int sa = Messagebox
	 * .show("El Articulo Seleccionado no esta presente quiere ver los equivalentes"
	 * , "Advertencia", Messagebox.OK | Messagebox.CANCEL, Messagebox.QUESTION);
	 * System.out.println(sa);
	 * 
	 * if (sa == 1) { mostrarEquivalente(z); } if (2 == sa) { }
	 * 
	 * }
	 * 
	 * }
	 */

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

				// vistaCotizacion.getDetalleContrato().refrescar();

				// List<DetalleContrato> auxiliar2 =
				// vistaCotizacion.getDetalleContrato().getModelo();

				// Row fila = (Row) event.getTarget().getParent();

				List<Row> filas = vistaCotizacion.getDetalle().getFilas()
						.getChildren();

				for (Row item : filas) {

					((CompBuscar<IProducto>) item.getChildren().get(0))
							.setSeleccion(negocioCotizacion
									.enriqueserProducto(((CompBuscar<IProducto>) item
											.getChildren().get(0))
											.getSeleccion()));

					IProducto articulodeventa = ((CompBuscar<IProducto>) item
							.getChildren().get(0)).getSeleccion();

					Double cantidad = ((Doublebox) item.getChildren().get(1))
							.getValue();

					if ((articulodeventa != null) && (cantidad != null)) {
						double preciobase = 0.0;

						if (articulodeventa instanceof ArticuloVenta) {
							ArticuloVenta art = (ArticuloVenta) articulodeventa;
							preciobase = art.getPrecioBase();
							System.out.printf("precio %.2f", preciobase);
						} else {
							preciobase = articulodeventa.getPrecioBase(vistaCotizacion
											.getCmbClientes().getSeleccion()
											.getCliente().getTipo());
						}
						acumuladobase = acumuladobase + (preciobase * cantidad);
						acumuladoimpuesto = acumuladoimpuesto
								+ (articulodeventa.getIvaProducto() * cantidad);
						DetalleContrato detalleContrato = new DetalleContrato();
						detalleContrato.setCantidad(cantidad);
						detalleContrato.setProducto(articulodeventa);
						detalleContrato.setPrecioUnidad(preciobase);
						detalleContrato.setSubtotal(cantidad * preciobase);

						// List<DetalleContrato> auxiliar =
						// vistaCotizacion.getDetalleContrato().getModelo();
						// System.out.println(auxiliar.size());
					}
				}

				vistaCotizacion.getBase().setValue(
						Real.redondeoMoneda(acumuladobase));
				vistaCotizacion.getImpuesto().setValue(
						Real.redondeoMoneda(acumuladoimpuesto));
				// vistaCotizacion.getTotal().setValue(Real.redondeoMoneda(acumuladobase
				// + acumuladoimpuesto));
				vistaCotizacion.getTotal().setValue(
						Real.redondeoMoneda(acumuladobase)
								+ Real.redondeoMoneda(acumuladoimpuesto));

			}

	}

	/*
	 * public void mostrarEquivalente(ArticuloVenta articulodeventa) throws
	 * ExcFiltroExcepcion, ExcDatosInvalidos, ExcSeleccionNoValida
	 * 
	 * { Consumible consumible = new PerConsumible()
	 * .getConsumiblearticulo(articulodeventa); if (consumible != null) { new
	 * ContConsumible(Accion.CONSULTAR, consumible,
	 * vistaCotizacion.getAuxConsumible(), this.app); }
	 * 
	 * Herramienta herramienta = new PerHerramienta()
	 * .getHerramientaarticulo(articulodeventa); if (herramienta != null) { new
	 * ContHerramienta(Accion.CONSULTAR, herramienta,
	 * vistaCotizacion.getAuxHerramienta(), this.app); } Repuesto repuesto = new
	 * PerRepuesto() .getRepuestoarticulo(articulodeventa);
	 * 
	 * if (herramienta != null) { new ContRepuesto(Accion.CONSULTAR, repuesto,
	 * vistaCotizacion.getAuxRepuesto(), this.app); }
	 * 
	 * }
	 */
	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
	//	if (modoAgregar())
	//		getDato().setEstado((new PerEstadoContrato().getCulminado()));		
		vistaCotizacion.actualizarModelo();
		CotizacionTransporte a = getDato();
		getDato().setPagador(
				vistaCotizacion.getCmbClientes().getSeleccion().getCliente());

		getDato().setDetallesContrato(vistaCotizacion.getColeccionGrilla());

		getDato().setFecha(vistaCotizacion.getDtmFechaEmision().getValue());
		getDato().setFechaDesde(vistaCotizacion.getDtmFechaInicio().getValue());
		getDato().setDiasVigencia(
				vistaCotizacion.getLblDiasVigencia().getValue());

		getDato().setTotal(vistaCotizacion.getTotal().getValue());
		getDato().setMonto(vistaCotizacion.getBase().getValue());
		getDato().setSaldo(vistaCotizacion.getTotal().getValue());
		getDato().setExpediente(false);
		getDato().setObservacion(vistaCotizacion.getObservacion().getValue());
		CotizacionTransporte as = getDato();
		for (DetalleContrato item : getDato().getDetallesContrato()) {
			item.setContrato(as);
			item.setImpuesto(item.getProducto().getImpuesto());
			item.setPrestado(true);
			item.setPrestado(false);
		}
		
		/*UbicacionTransporte direccionO = new UbicacionTransporte();
		direccionO.setDireccion(vistaCotizacion.getDireccionOrigen().getValue());
		direccionO.setReferencia(vistaCotizacion.getRefereciaOrigen().getValue());
		direccionO.setUbicacionMunicipio(vistaCotizacion.getCmbMnunicipioOrigen().getSeleccion());
		direccionO.setUbicacionParroquia(vistaCotizacion.getCmbParroquiaOrigen().getSeleccion());
		direccionO.setUbicacionSector(vistaCotizacion.getCmbsectorOrigen().getSeleccion());
		
		UbicacionTransporte direccionD = new UbicacionTransporte();
		direccionD.setDireccion(vistaCotizacion.getDireccionDestino().getValue());
		direccionD.setReferencia(vistaCotizacion.getRefereciaDestino().getValue());
		direccionD.setUbicacionMunicipio(vistaCotizacion.getCmbMnunicipioDestino().getSeleccion());
		direccionD.setUbicacionParroquia(vistaCotizacion.getCmbParroquiaDestino().getSeleccion());
		direccionD.setUbicacionSector(vistaCotizacion.getCmbsectorDestino().getSeleccion());*/
	//	getDato().setDireccionOrigen(direccionO);
	//	getDato().setDireccionLlegada(direccionD);
		getDato().setDireccionOrigen(vistaCotizacion.getOrigen().getModelo());
		getDato().setDireccionLlegada(vistaCotizacion.getDestino().getModelo());
		
		try {
			negocioCotizacion.guardar(getDato());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	refrescarCatalogo();
	
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
		if (vistaCotizacion.getDtmFechaEmision().getValue() == null)
			throw new WrongValueException(vistaCotizacion.getDtmFechaEmision(),
					"Debe indicar una fecha de emision");
		if (vistaCotizacion.getDtmFechaInicio().getValue() == null)
			throw new WrongValueException(vistaCotizacion.getDtmFechaInicio(),
					"Debe indicar la fecha de inicio");
		if (vistaCotizacion.getLblDiasVigencia().getValue() == null)
			throw new WrongValueException(vistaCotizacion.getLblDiasVigencia(),
					"Debe indicar los dias de vigencia");
		List<Row> filas = vistaCotizacion.getDetalle().getFilas().getChildren();
		boolean z = vistaCotizacion.getDetalle().getFilas().getChildren()
				.isEmpty();
		if (z) {
			throw new WrongValueException(vistaCotizacion.getDetalle(),
					"debe indicar al menos un articulo");
		}
		if (modoAnular()) {
			int hijos = 0;
			try {
				hijos = negocioCotizacion.getHijosActivos(getDato());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (hijos > 0) {
				throw new WrongValueException(vistaCotizacion.getDetalle(),
						"Esta Cotizacion Tiene Hijos Activos (Factura)");
			}
			try {
				negocioCotizacion.anular(getDato());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		validarDetalle();
	}
	public void validarProcesar() throws WrongValuesException, ExcEntradaInconsistente {
		negocioCotizacion = NegocioCotizacionTransporte.getInstance();
		if (getDato().getEstado().equals(negocioCotizacion.getEstadoRechazado()))
		throw new WrongValueException(vistaCotizacion.getTxtMotivoRechazo(),"debe indicar un motivo de rechazo");
		
	}

	@Override
	public void anular() throws ExcFiltroExcepcion {
		negocioCotizacion= NegocioCotizacionTransporte.getInstance();
		try {
			int hijos = negocioCotizacion.getHijosActivos(getDato());
			if (hijos>0)
				throw new Exception("La Cotizacion Tiene Hijos Activos");
			negocioCotizacion.anular(getDato());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		refrescarCatalogo();
		vistaCotizacion.close();

	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		CotizacionTransporte orden = getDato();
		if (vistaCotizacion.getTxtMotivoRechazo().getValue()!=null)
			getDato().setMotivoRechazo(vistaCotizacion.getTxtMotivoRechazo().getValue());
		negocioCotizacion = NegocioCotizacionTransporte.getInstance();
		negocioCotizacion.procesar(orden);
		vistaCotizacion.close();
	}

	@SuppressWarnings("unchecked")
	public void ActualizarTotales() {
		Double acumuladobase = 0.0;
		Double acumuladoimpuesto = 0.0;
		// List<DetalleContrato> auxiliar3 =
		// vistaCotizacion.getDetalleContrato().getModelo();
		//
		// vistaCotizacion.getDetalleContrato().getChildren().clear();
		// vistaCotizacion.getDetalleContrato().getModelo().clear();
		// vistaCotizacion.getDetalleContrato().refrescar();

		// List<DetalleContrato> auxiliar2 =
		// vistaCotizacion.getDetalleContrato().getModelo();

		// Row fila = (Row) event.getTarget().getParent();

		List<Row> filas = vistaCotizacion.getDetalle().getFilas().getChildren();

		for (Row item : filas) {
			/*
			 * ArticuloVenta articulodeventa = ((CompBuscar<ArticuloVenta>) item
			 * .getChildren().get(0)).getSeleccion();
			 * 
			 * Double cantidad = ((Doublebox) item.getChildren().get(1))
			 * .getValue();
			 * 
			 * if ((articulodeventa != null) && (cantidad != null)) {
			 * acumuladobase = acumuladobase + (articulodeventa.getPrecioBase()
			 * * cantidad); acumuladoimpuesto = acumuladoimpuesto +
			 * (articulodeventa.getIvaProducto() * cantidad); DetalleContrato
			 * detalleContrato = new DetalleContrato();
			 * detalleContrato.setCantidad(cantidad);
			 * detalleContrato.setProducto(articulodeventa); detalleContrato
			 * .setPrecioUnidad(articulodeventa.getPrecioBase());
			 * detalleContrato.setSubtotal(cantidad
			 * articulodeventa.getPrecioBase());
			 */
			// vistaCotizacion.getDetalleContrato().agregar(detalleContrato);
			// List<DetalleContrato> auxiliar =
			// vistaCotizacion.getDetalleContrato().getModelo();
			// System.out.println(auxiliar.size());
			IProducto articulodeventa = ((CompBuscar<IProducto>) item
					.getChildren().get(0)).getSeleccion();
			articulodeventa = negocioCotizacion
					.enriqueserProducto(articulodeventa);
			Double cantidad = ((Doublebox) item.getChildren().get(1))
					.getValue();
			Double preciounidad = ((Doublebox) item.getChildren().get(2))
					.getValue();
			TipoProductor tipoCliente = vistaCotizacion.getCmbClientes()
					.getSeleccion().getCliente().getTipo();

			if ((articulodeventa != null) && (cantidad != null)) {
				if (articulodeventa instanceof ArticuloVenta) {
					// acumuladobase = acumuladobase+
					// (((ArticuloVenta)articulodeventa).getPrecioBase() *
					// cantidad);
					// acumuladoimpuesto = acumuladoimpuesto+
					// (articulodeventa.getIvaProducto() * cantidad);
					acumuladobase = acumuladobase + (preciounidad * cantidad);
					double porimpuesto = ((((ArticuloVenta) articulodeventa)
							.getImpuesto().getPorcentaje()) / 100);
					acumuladoimpuesto = acumuladoimpuesto
							+ ((porimpuesto * preciounidad) * cantidad);
					DetalleContrato detalleContrato = new DetalleContrato();
					detalleContrato.setCantidad(cantidad);
					detalleContrato.setProducto(articulodeventa);
					// detalleContrato.setPrecioUnidad(((ArticuloVenta)articulodeventa).getPrecioBase());
					detalleContrato.setPrecioUnidad(preciounidad);
					detalleContrato.setSubtotal(cantidad * preciounidad);
				} else {
					// acumuladobase = acumuladobase+
					// (articulodeventa.getPrecioBase(tipoCliente) * cantidad);
					// acumuladoimpuesto = acumuladoimpuesto+
					// (articulodeventa.getIvaProducto() * cantidad);
					acumuladobase = acumuladobase + (preciounidad * cantidad);
					double porimpuesto = ((articulodeventa.getImpuesto()
							.getPorcentaje()) / 100);
					acumuladoimpuesto = acumuladoimpuesto
							+ ((porimpuesto * preciounidad) * cantidad);
					DetalleContrato detalleContrato = new DetalleContrato();
					detalleContrato.setCantidad(cantidad);
					detalleContrato.setProducto(articulodeventa);
					// detalleContrato.setPrecioUnidad(articulodeventa.getPrecioBase(tipoCliente));
					detalleContrato.setPrecioUnidad(preciounidad);
					detalleContrato.setSubtotal(cantidad * preciounidad);
				}

			}
		}

		vistaCotizacion.getBase().setValue(Real.redondeoMoneda(acumuladobase));
		vistaCotizacion.getImpuesto().setValue(
				Real.redondeoMoneda(acumuladoimpuesto));
		// vistaCotizacion.getTotal().setValue(Real.redondeoMoneda(acumuladobase
		// + acumuladoimpuesto));
		vistaCotizacion.getTotal().setValue(
				Real.redondeoMoneda(acumuladobase)
						+ Real.redondeoMoneda(acumuladoimpuesto));

	}

	@SuppressWarnings({ "unchecked", "unused" })
	public void validarDetalle() throws WrongValuesException,
			ExcEntradaInconsistente {
		List<Row> filas = vistaCotizacion.getDetalle().getFilas().getChildren();
		int i = 0;
		for (Row row : filas) {
			i++;
			CompBuscar<ArticuloVenta> articulo = (CompBuscar<ArticuloVenta>) row
					.getChildren().get(0);

			Doublebox cantidad = (Doublebox) row.getChildren().get(1);
			if (articulo.getSeleccion() == null) {
				throw new WrongValueException(articulo,
						"El Articulo no puede ser nulo");
			}

			if (cantidad.getValue() == null || cantidad.getValue().equals(0)) {
				throw new WrongValueException(cantidad,
						"la cantidad no puede ser nula o cero");
			}

		}
		if (i > 1)
			throw new WrongValueException(vistaCotizacion.getDetalle(),
					"cotizacion no puede tener mas de " + getMaximoRenglon()
							+ "renglones");
	}

	public void actualizarArticulo(CompBuscar<IProducto> componente)
			throws ExcFiltroExcepcion {
		System.out.println("actualizarServicio");
		IProducto producto = null;
		Double precio = 0.0;
		Row registro = (Row) componente.getParent();
		try {
			producto = componente.getSeleccion();
			producto = negocioCotizacion.enriqueserProducto(producto);
		} catch (NullPointerException e) {
			throw new ExcFiltroExcepcion(
					"Problemas con el producto seleccionado");
		}

		try {
			if (producto instanceof ArticuloVenta) {
				ArticuloVenta art = (ArticuloVenta) componente.getSeleccion();
				precio = art.getPrecioBase();
				System.out.printf("precio %.2f", precio);
			} else {
				precio = producto.getPrecioBase(vistaCotizacion.getCmbClientes()
								.getSeleccion().getCliente().getTipo());
			}
			((Doublebox) registro.getChildren().get(2)).setValue(precio);
			Double cantidad = ((Doublebox) registro.getChildren().get(1))
					.getValue();
			if (cantidad == null) {
				((Doublebox) registro.getChildren().get(1)).setValue(1.0);
				cantidad = 1.0;
			}
			((Doublebox) registro.getChildren().get(3)).setValue(precio * cantidad);
			ActualizarTotales();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcFiltroExcepcion("Problemas con el servicio");
		}
	}

	public void actualizarPrecio(Doublebox componente)
			throws ExcFiltroExcepcion {
		try {
			Row registro = (Row) componente.getParent();

			Doublebox t1 = ((Doublebox) registro.getChildren().get(1));
			Doublebox t2 = ((Doublebox) registro.getChildren().get(2));
			Doublebox t3 = ((Doublebox) registro.getChildren().get(3));

			if (componente.equals(t1)) {
				System.out.println(1);
			}
			;
			if (componente.equals(t2)) {
				System.out.println(2);
			}
			;
			if (componente.equals(t3)) {
				System.out.println(3);
			}
			;

			if (componente.equals(t1)) {

				double precio = ((Doublebox) registro.getChildren().get(2))
						.getValue();
				((Doublebox) registro.getChildren().get(3)).setValue(componente
						.getValue() * precio);
				ActualizarTotales();
			}
			;

			if (componente.equals(t2)) {
				double cantidad = ((Doublebox) registro.getChildren().get(1))
						.getValue();
				((Doublebox) registro.getChildren().get(3)).setValue(componente
						.getValue() * cantidad);
				ActualizarTotales();
			}
			;

		} catch (Exception e) {
			throw new ExcFiltroExcepcion("Problemas con el servicio");
		}
	}

	@SuppressWarnings("unchecked")
	public void ActualizarFecha() {
		Date fechai = vistaCotizacion.getDtmFechaInicio().getValue();
		Integer diasvigencia = vistaCotizacion.getLblDiasVigencia().getValue();
		Datebox sss = vistaCotizacion.getDtmFechaInicio();
		List dd = vistaCotizacion.getDtmFechaInicio().getChildren();

		Calendar calendario = Calendar.getInstance();
		calendario.setTime(fechai);
		if (diasvigencia != null)
			calendario.add(Calendar.DATE, diasvigencia);
		Date fechah = calendario.getTime();

		vistaCotizacion.getLblFechaHasta().setValue(Fecha.obtenerFecha(fechah));

	}

	private Integer getMaximoRenglon() {

		return (Integer) SpringUtil.getBean("lineasFactura");
	}

	public boolean isDirecciones(Event event) {
		List<CompGrupoBusqueda> lista = new ArrayList<CompGrupoBusqueda>();
/*
		lista.add(vistaCotizacion.getCmbMnunicipioDestino());
		lista.add(vistaCotizacion.getCmbMnunicipioOrigen());
		lista.add(vistaCotizacion.getCmbParroquiaDestino());
		lista.add(vistaCotizacion.getCmbParroquiaOrigen());
		lista.add(vistaCotizacion.getCmbsectorDestino());
		lista.add(vistaCotizacion.getCmbsectorOrigen());
		lista.add(vistaCotizacion.getCmbMnunicipioDestino());
*/
		lista.add(vistaCotizacion.getOrigen());
		lista.add(vistaCotizacion.getDestino());
		if (lista.contains(event.getTarget())) {
			return true;
		} else
			return false;
	}

	public void ActualizarDireccion(Event event) throws ExcFiltroExcepcion {
/*
		if (event.getTarget().equals(vistaCotizacion.getCmbMnunicipioDestino())) {
			vistaCotizacion.getCmbParroquiaDestino().setModelo(negocioCotizacion.getUbicacionesParroquias((vistaCotizacion.getCmbMnunicipioDestino().getSeleccion())));
			
		};
		if (event.getTarget().equals(vistaCotizacion.getCmbMnunicipioOrigen())) {
			vistaCotizacion.getCmbParroquiaOrigen().setModelo(negocioCotizacion.getUbicacionesParroquias((vistaCotizacion.getCmbMnunicipioOrigen().getSeleccion())));
			
		}
		;
		if (event.getTarget().equals(vistaCotizacion.getCmbParroquiaDestino())) {
			
			UbicacionMunicipio municipio = vistaCotizacion.getCmbParroquiaDestino().getSeleccion().getMunicipio();
			
			vistaCotizacion.getCmbMnunicipioDestino().setSeleccion(municipio);
			vistaCotizacion.getCmbsectorDestino().setModelo(negocioCotizacion.getUbicacionesSectores(vistaCotizacion.getCmbParroquiaDestino().getSeleccion()));
			
		}
		;
		if (event.getTarget().equals(vistaCotizacion.getCmbParroquiaOrigen())) {
			
UbicacionMunicipio municipio = vistaCotizacion.getCmbParroquiaOrigen().getSeleccion().getMunicipio();
			
			vistaCotizacion.getCmbMnunicipioOrigen().setSeleccion(municipio);
			vistaCotizacion.getCmbsectorOrigen().setModelo(negocioCotizacion.getUbicacionesSectores(vistaCotizacion.getCmbParroquiaOrigen().getSeleccion()));
			
		}
		;
		if (event.getTarget().equals(vistaCotizacion.getCmbsectorDestino())) {
			
			UbicacionMunicipio municipio = vistaCotizacion.getCmbsectorDestino().getSeleccion().getParroquia().getMunicipio();
			 UbicacionParroquia parroquia = vistaCotizacion.getCmbsectorDestino().getSeleccion().getParroquia();
			 
			vistaCotizacion.getCmbMnunicipioDestino().setSeleccion(municipio);
			vistaCotizacion.getCmbParroquiaDestino().setSeleccion(parroquia);
			
			
			
		}
		;
		if (event.getTarget().equals(vistaCotizacion.getCmbsectorOrigen())) {
			UbicacionMunicipio municipio = vistaCotizacion.getCmbsectorOrigen().getSeleccion().getParroquia().getMunicipio();
			UbicacionParroquia parroquia = vistaCotizacion.getCmbsectorOrigen().getSeleccion().getParroquia();
			 
			vistaCotizacion.getCmbMnunicipioOrigen().setSeleccion(municipio);
			vistaCotizacion.getCmbParroquiaOrigen().setSeleccion(parroquia);
			
		}
		;*/
		

	}

}
