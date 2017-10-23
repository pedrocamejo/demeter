package cpc.demeter.controlador.mantenimiento;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.mantenimiento.UIContratoServicioTecnico;
import cpc.demeter.vista.mantenimiento.UICotizacion;
import cpc.demeter.vista.mantenimiento.UICotizacionExpediente;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.demeter.administrativo.Cotizacion;
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.administrativo.Impuesto;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.demeter.mantenimiento.Consumible;
import cpc.modelo.demeter.mantenimiento.DetalleSalidaExternaArticulo;
import cpc.modelo.demeter.mantenimiento.Herramienta;
import cpc.modelo.demeter.mantenimiento.Repuesto;
import cpc.modelo.demeter.mantenimiento.SalidaExternaArticulo;
import cpc.modelo.ministerio.basico.TipoProductor;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.negocio.demeter.administrativo.NegocioContratoMecanizado;
import cpc.negocio.demeter.administrativo.NegocioCotizacion;
import cpc.negocio.demeter.mantenimiento.NegocioSalidaExternaArticulo;
import cpc.negocio.sigesp.NegocioSede;
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
import cva.pc.demeter.utilidades.Fecha;
import cva.pc.demeter.utilidades.Real;

public class ContCotizacionExpediente extends ContVentanaBase<Cotizacion> implements
		EventListener {
	private NegocioCotizacion negocioCotizacion;
	private UICotizacionExpediente vistaCotizacion;
	private AppDemeter app;

	public ContCotizacionExpediente(int modoOperacion, Cotizacion cotizacion,
			ContCatalogo<Cotizacion> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcFiltroExcepcion {
		super(modoOperacion);
		// TODO Auto-generated constructor stub
		this.app = app;
		negocioCotizacion = NegocioCotizacion.getInstance();
		List<IProducto> articuloVentas = negocioCotizacion.getArticulosServicios();

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
		setear(cotizacion, new UICotizacionExpediente("Cotizacion Expediente", 800,
				getModoOperacion(), articuloVentas, almacenes,
				 clientes, salidaExternaArticulo, cotizacion, NegocioSede.getInstance().getTodos(),app.getSede().getNombre()),
				llamador, app);
		// new UICotizacion("Cotizacion de Articulo", 730, articuloVentas,
		// almacenes, detallesContrato, clientes, salidaExternaArticulo,
		// cotizacion);
		vistaCotizacion = (UICotizacionExpediente) getVista();
        if (modoEditar())
        	vistaCotizacion.desactivarEditar();
        if (modoAnular()||modoConsulta())
        	vistaCotizacion.desactivarAnular();
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7124058797951366375L;

	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(Event event) throws Exception {

		System.out.println(event.getName());
	//	validarexistencia(event);
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
			else actualizarArticulo((CompBuscar<IProducto>) event.getTarget());
		}
		else if (event.getName().equals(Events.ON_CHANGING))
			ActualizarFecha();
		else if (event.getName().equals(Events.ON_CHANGE)){
			if (event.getTarget()==vistaCotizacion.getLblDiasVigencia())
				ActualizarFecha();
				else if (event.getTarget()==vistaCotizacion.getDtmFechaInicio())
					ActualizarFecha();
				else
			actualizarPrecio((Doublebox) event.getTarget());
			}
		if (event.getName().equals("onBorrarFila"))
			{eliminadato((Row) event.getData());
			ActualizarTotales();}
		 else if (event.getName().equals(CompGrilla.ON_ELIMINA_FILA)) 
				{eliminadato((Row) event.getData());
				ActualizarTotales();}
		if (event.getTarget() == getVista().getAceptar()) {
			validar();
			procesarCRUD(event);

		}

	}


/*
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

	}*/

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
					
					((CompBuscar<IProducto>) item
							.getChildren().get(0)).setSeleccion(negocioCotizacion.enriqueserProducto(((CompBuscar<IProducto>) item
							.getChildren().get(0)).getSeleccion()));
					
					IProducto articulodeventa = ((CompBuscar<IProducto>) item
							.getChildren().get(0)).getSeleccion();

					Double cantidad = ((Doublebox) item.getChildren().get(1))
							.getValue();

					if ((articulodeventa != null) && (cantidad != null)) {
						  double preciobase = 0.0;
		                    
		                    if (articulodeventa instanceof ArticuloVenta){
		    					ArticuloVenta art = (ArticuloVenta) articulodeventa;
		    			 preciobase = ( art.getPrecioBase());
		    			System.out.printf("precio %.2f", preciobase);}
		    				else {
		    				 preciobase = articulodeventa.getPrecioBase(vistaCotizacion.getCmbClientes().getSeleccion().getCliente().getTipo()); 
		    				}
						acumuladobase = acumuladobase
								+ (preciobase * cantidad);
						acumuladoimpuesto = acumuladoimpuesto
								+ (articulodeventa.getIvaProducto() * cantidad);
						DetalleContrato detalleContrato = new DetalleContrato();
						detalleContrato.setCantidad(cantidad);
						detalleContrato.setProducto(articulodeventa);
						detalleContrato.setPrecioUnidad(preciobase);
						detalleContrato.setSubtotal(cantidad
								* preciobase);

				
						// List<DetalleContrato> auxiliar =
						// vistaCotizacion.getDetalleContrato().getModelo();
						// System.out.println(auxiliar.size());
					}
				}

				vistaCotizacion.getBase().setValue(Real.redondeoMoneda(acumuladobase));
				vistaCotizacion.getImpuesto().setValue(Real.redondeoMoneda(acumuladoimpuesto));
				//vistaCotizacion.getTotal().setValue(Real.redondeoMoneda(acumuladobase + acumuladoimpuesto));
				vistaCotizacion.getTotal().setValue(Real.redondeoMoneda(acumuladobase) + Real.redondeoMoneda(acumuladoimpuesto));

			}

	}
/*
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
*/
	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		vistaCotizacion.actualizarModelo();
		Cotizacion a = getDato();
		getDato().setPagador(
				vistaCotizacion.getCmbClientes().getSeleccion().getCliente());
		
		getDato().setDetallesContrato(vistaCotizacion.getColeccionGrilla());
		
		getDato().setFecha(vistaCotizacion.getDtmFechaEmision().getValue());
		getDato().setFechaDesde(vistaCotizacion.getDtmFechaInicio().getValue());
		getDato().setDiasVigencia(vistaCotizacion.getLblDiasVigencia().getValue());
		
		getDato().setTotal(vistaCotizacion.getTotal().getValue());
		getDato().setMonto(vistaCotizacion.getBase().getValue());
		getDato().setSaldo(vistaCotizacion.getTotal().getValue());
		getDato().setExpediente(true);
		getDato().setTipoCotizacion(negocioCotizacion.getMantenimiento());
		getDato().setSede(vistaCotizacion.getSede());
		
		Cotizacion as = getDato();
		List<DetalleContrato> d = vistaCotizacion.getColeccionGrilla();
		List<DetalleContrato> dd = getDato().getDetallesContrato();
		
		for (DetalleContrato item : getDato().getDetallesContrato()) {
			item.setContrato(as);
			item.setImpuesto(item.getProducto().getImpuesto());
			item.setPrestado(true);
			item.setPrestado(false);
			}
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
		if (vistaCotizacion.getSede() == null)
			throw new WrongValueException(vistaCotizacion.getCmbsede(),
					"Debe seleccionar una sede ");
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
		if (modoAnular()){
		int hijos = 0;
		try {
			hijos = negocioCotizacion.getHijosActivos(getDato());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (hijos>0){
			throw new WrongValueException(vistaCotizacion.getDetalle(),
					"Esta Cotizacion Tiene Hijos Activos (Factura)");
		}
		}
		validarDetalle();
	}

	@Override
	public void anular() throws ExcFiltroExcepcion {
	int hijos;
	try {
	
		negocioCotizacion.anular(getDato());
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
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
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	public void ActualizarTotales() {
		Double acumuladobase = 0.0;
		Double acumuladoimpuesto = 0.0;
		// List<DetalleContrato> auxiliar3 =
		// vistaCotizacion.getDetalleContrato().getModelo();
		//
	//	vistaCotizacion.getDetalleContrato().getChildren().clear();
	//	vistaCotizacion.getDetalleContrato().getModelo().clear();
		// vistaCotizacion.getDetalleContrato().refrescar();

		// List<DetalleContrato> auxiliar2 =
		// vistaCotizacion.getDetalleContrato().getModelo();

		// Row fila = (Row) event.getTarget().getParent();

		List<Row> filas = vistaCotizacion.getDetalle().getFilas().getChildren();

		for (Row item : filas) {
/*
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
*/
			//	vistaCotizacion.getDetalleContrato().agregar(detalleContrato);
				// List<DetalleContrato> auxiliar =
				// vistaCotizacion.getDetalleContrato().getModelo();
				// System.out.println(auxiliar.size());
			IProducto articulodeventa = ((CompBuscar<IProducto>) item
					.getChildren().get(0)).getSeleccion();
                     articulodeventa= negocioCotizacion.enriqueserProducto(articulodeventa);
			Double cantidad = ((Doublebox) item.getChildren().get(1))
					.getValue();
			Double preciounidad = ((Doublebox) item.getChildren().get(2))
					.getValue();
			TipoProductor tipoCliente = vistaCotizacion.getCmbClientes().getSeleccion().getCliente().getTipo();

			if ((articulodeventa != null) && (cantidad != null)) {
				if (articulodeventa instanceof ArticuloVenta ){
					//acumuladobase = acumuladobase+ (((ArticuloVenta)articulodeventa).getPrecioBase() * cantidad);	
					//acumuladoimpuesto = acumuladoimpuesto+ (articulodeventa.getIvaProducto() * cantidad);
					acumuladobase = acumuladobase+ (preciounidad * cantidad);
					 double porimpuesto = ((((ArticuloVenta)articulodeventa).getImpuesto().getPorcentaje())/100);
					acumuladoimpuesto = acumuladoimpuesto+ ((porimpuesto*preciounidad) * cantidad);
					DetalleContrato detalleContrato = new DetalleContrato();
					detalleContrato.setCantidad(cantidad);
					detalleContrato.setProducto(articulodeventa);
				//	detalleContrato.setPrecioUnidad(((ArticuloVenta)articulodeventa).getPrecioBase());
					detalleContrato.setPrecioUnidad(preciounidad);
					detalleContrato.setSubtotal(cantidad*preciounidad);
				}
				else {
					//acumuladobase = acumuladobase+ (articulodeventa.getPrecioBase(tipoCliente) * cantidad);	
					//acumuladoimpuesto = acumuladoimpuesto+ (articulodeventa.getIvaProducto() * cantidad);
					acumuladobase = acumuladobase+ (preciounidad * cantidad);	
					 double porimpuesto = ((articulodeventa.getImpuesto().getPorcentaje())/100);
					acumuladoimpuesto = acumuladoimpuesto+ ((porimpuesto*preciounidad) * cantidad);
					DetalleContrato detalleContrato = new DetalleContrato();
					detalleContrato.setCantidad(cantidad);
					detalleContrato.setProducto(articulodeventa);
				//	detalleContrato.setPrecioUnidad(articulodeventa.getPrecioBase(tipoCliente));
					detalleContrato.setPrecioUnidad(preciounidad);
					detalleContrato.setSubtotal(cantidad* preciounidad);
				}
				
			
			
			
			}
		}

		vistaCotizacion.getBase().setValue(Real.redondeoMoneda(acumuladobase));
		vistaCotizacion.getImpuesto().setValue(Real.redondeoMoneda(acumuladoimpuesto));
	//	vistaCotizacion.getTotal().setValue(Real.redondeoMoneda(acumuladobase + acumuladoimpuesto));
		vistaCotizacion.getTotal().setValue(Real.redondeoMoneda(acumuladobase) + Real.redondeoMoneda(acumuladoimpuesto));

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
		if (i > getMaximoRenglon())
			throw new WrongValueException(vistaCotizacion.getDetalle(),
					"cotizacion no puede tener mas de " + getMaximoRenglon() + "renglones");
	}

	
	public void actualizarArticulo(CompBuscar<IProducto> componente)
			throws ExcFiltroExcepcion {
		System.out.println("actualizarServicio");
		IProducto producto = null;
		Double precio =0.0;
		Row registro = (Row) componente.getParent();
		try {
			producto = componente.getSeleccion();
			producto =	negocioCotizacion.enriqueserProducto(producto);
		} catch (NullPointerException e) {
			throw new ExcFiltroExcepcion(
					"Problemas con el producto seleccionado");
		}
	
			try {
				if (producto instanceof ArticuloVenta){
					ArticuloVenta art = (ArticuloVenta) componente.getSeleccion();
			 precio = art.getPrecioBase();
			System.out.printf("precio %.2f", precio);}
				else {
				 precio = producto.getPrecioBase(vistaCotizacion.getCmbClientes().getSeleccion().getCliente().getTipo()); 
				}
			((Doublebox) registro.getChildren().get(2)).setValue(precio);
			Double cantidad = ((Doublebox) registro.getChildren().get(1))
					.getValue();
			if (cantidad == null) {
				((Doublebox) registro.getChildren().get(1)).setValue(1.0);
				cantidad = 1.0;
			}
			((Doublebox) registro.getChildren().get(3)).setValue((precio * cantidad));
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
			
			if (componente.equals(t1)){
				System.out.println(1);
			};
			if (componente.equals(t2)){
				System.out.println(2);
			};
			if (componente.equals(t3)){
				System.out.println(3);
			};
			
			if (componente.equals(t1)){
			
			
			double precio = ((Doublebox) registro.getChildren().get(2))
					.getValue();
			((Doublebox) registro.getChildren().get(3)).setValue(componente
					.getValue() * precio);
			ActualizarTotales();
			};
			
			
			
			if (componente.equals(t2)){
				double cantidad = ((Doublebox) registro.getChildren().get(1))
						.getValue();
				((Doublebox) registro.getChildren().get(3)).setValue(componente
						.getValue() * cantidad);
				ActualizarTotales();
			};
			
			
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
		if  (diasvigencia!=null)
		calendario.add(Calendar.DATE, diasvigencia);
		Date fechah = calendario.getTime();
		
	vistaCotizacion.getLblFechaHasta().setValue(	Fecha.obtenerFecha(fechah));
		
	}

	
	
	private Integer getMaximoRenglon() {
 
		return (Integer) SpringUtil.getBean("lineasFactura");
	}
	public void eliminadato(Row fila) throws ExcFiltroExcepcion {
		ActualizarTotales();
	}
	
}
