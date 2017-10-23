package cpc.demeter.controlador.vialidad;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Button;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.ministerio.ContUbicacionFisica;
import cpc.demeter.vista.vialidad.UICotizacionVialidad;
import cpc.modelo.demeter.administrativo.ArchivoContrato;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.demeter.administrativo.CotizacionVialidad;
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.administrativo.TipoContrato;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.ministerio.basico.TipoProductor;
import cpc.modelo.ministerio.dimension.UbicacionDireccion;
import cpc.negocio.demeter.administrativo.NegocioCotizacionVialidad;
import cpc.negocio.sigesp.NegocioSede;
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

public class ContCotizacionVialidad extends ContVentanaBase<CotizacionVialidad>
		implements EventListener {
	private NegocioCotizacionVialidad negocioCotizacion;
	private UICotizacionVialidad vistaCotizacion;
	private AppDemeter app;

	public ContCotizacionVialidad(int modoOperacion,
			CotizacionVialidad cotizacion,
			ContCatalogo<CotizacionVialidad> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcFiltroExcepcion {
		super(modoOperacion);
		// TODO Auto-generated constructor stub
		this.app = app;
		negocioCotizacion = NegocioCotizacionVialidad.getInstance();
		List<IProducto> articuloVentas;
		
		if (modoAgregar()) {
			articuloVentas = negocioCotizacion.getServiciosVialidadActivos();
		} else {
			articuloVentas = negocioCotizacion.getServiciosVialidad();
		}

		List<DetalleContrato> detallesContrato = new ArrayList<DetalleContrato>();
		List<ClienteAdministrativo> clientes = negocioCotizacion
				.getClientesAdministrativos();
		List<UbicacionDireccion> ubicaciones = negocioCotizacion
				.getUbicaciones();
		List<TipoContrato> tipoContratos= negocioCotizacion.getTiposContrato();

		if (modoAgregar() || cotizacion == null) {

		} else {

			detallesContrato = cotizacion.getDetallesContrato();

		}

		setDato(cotizacion);
		
		setear(cotizacion, new UICotizacionVialidad("Cotizacion de Servicios de Vialidad",
				800, getModoOperacion(), articuloVentas, detallesContrato,
				clientes,ubicaciones, cotizacion,tipoContratos, NegocioSede.getInstance().getTodos(),app.getSede().getNombre())
					, llamador, app);
		vistaCotizacion = (UICotizacionVialidad) getVista();
		if (modoEditar())
			vistaCotizacion.desactivarEditar();
		if (modoAnular() || modoConsulta())
			vistaCotizacion.desactivarAnular();

	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7124058797951366375L;

	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(Event event) throws Exception {
		ActualizarTotales(event);
		if (event.getName().equals(Events.ON_CLICK)) {
			if (event.getTarget() instanceof Button ) {
				Button boton = (Button) event.getTarget();
				String nombre = boton.getLabel();
				if 	(nombre.equals("Subir Archivo")) {
					Row fila =(Row)event.getTarget().getParent();
					Object obj = fila.getValue();
					Object obj2 = fila.getAttribute("dato");
					((ArchivoContrato)obj2).setNombreArchivo("prieba");
					fila.setAttribute("dato",obj2);
					Object obj33 = fila.getAttribute("dato");
					System.gc();
					Media media = Fileupload.get();
					Desktop desktop = app.getWin().getDesktop();
					String rutaO = desktop.getWebApp().getRealPath("/entradas")
							+ "/";
					String rutaD = desktop.getWebApp().getRealPath("/entradas")
							+ "/descomprimidos/";
					System.out.println(rutaO);
					boolean as = SubirArchivos(media, rutaO,fila);
				}
				if 	(nombre.equals("Bajar Archivo")){
					Row fila =(Row)event.getTarget().getParent();
					Row fila3 =(Row)event.getTarget().getParent();
					bajarArchivo(fila);
				}
			} // end if (event.getTarget() instanceof Button )
		}
		if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {

			if (event.getTarget() == vistaCotizacion.getCmbClientesPagador()) {
				ClienteAdministrativo clienteenriquesido = vistaCotizacion
						.getCmbClientesPagador().getSeleccion();
				clienteenriquesido = negocioCotizacion
						.getClienteAdministrativo(clienteenriquesido);
				vistaCotizacion.getCmbClientesPagador().setText(
								clienteenriquesido.getNombreCliente());
				vistaCotizacion.getCmbClientesPagador().setSeleccion(
						clienteenriquesido);
				vistaCotizacion.getLblDireccionPagador().setValue(
						vistaCotizacion.getCmbClientesPagador().getSeleccion()
								.getCliente().getDireccion());
				vistaCotizacion.getLblCedulaRifPagado().setValue(
						vistaCotizacion.getCmbClientesPagador().getSeleccion()
								.getCedulaCliente());
			} 
			else if (event.getTarget() == vistaCotizacion.getCmbClientesBeneficiario()) {
				ClienteAdministrativo clienteenriquesido = vistaCotizacion
						.getCmbClientesBeneficiario().getSeleccion();
				clienteenriquesido = negocioCotizacion
						.getClienteAdministrativo(clienteenriquesido);
				vistaCotizacion.getCmbClientesBeneficiario().setText(clienteenriquesido.getNombreCliente());;
				vistaCotizacion.getCmbClientesBeneficiario().setSeleccion(
						clienteenriquesido);
				vistaCotizacion.getLblDireccionBeneficiario().setValue(
						vistaCotizacion.getCmbClientesBeneficiario().getSeleccion()
								.getCliente().getDireccion());
				vistaCotizacion.getLblCedulaRifBeneficiario().setValue(
						vistaCotizacion.getCmbClientesBeneficiario().getSeleccion()
								.getCedulaCliente());
			} 
			else if (isDirecciones(event)) {
				ActualizarDireccion(event);
			} else
				actualizarArticulo((CompBuscar<IProducto>) event.getTarget());
		} 
		else if (event.getName().equals(Events.ON_CHANGING))
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
		else if (event.getTarget() == vistaCotizacion.getCgbubicacionFisica().getCrear()) {
			System.out.println("Creando Ubicacion");
			new ContUbicacionFisica(Accion.AGREGAR, null,
					vistaCotizacion.getCgbubicacionFisica(), app);
			}
		else if (event.getTarget() ==vistaCotizacion.getTipoContrato())
			actualizarTipoContrato(vistaCotizacion.getTipoContrato()
					.getSeleccion());

	}

	
	public void actualizarTipoContrato(TipoContrato tipo) {
		if (vistaCotizacion == null)
			vistaCotizacion = (UICotizacionVialidad) getVista();
		switch (tipo.getId()) {
		case 1:
			vistaCotizacion.getCgdBeneficario().setVisible(false);
			break;
		case 2:
			vistaCotizacion.getCgdBeneficario().setVisible(false);
			break;
		case 3:
			vistaCotizacion.getCgdBeneficario().setVisible(true);
		}
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	public void ActualizarTotales(Event event) {

		if (event.getName().equals(Events.ON_CHANGE)
				|| event.getName().equals(CompBuscar.ON_SELECCIONO))
			if (event.getTarget().getParent().getClass() == Row.class) {
				Double acumuladobase = 0.0;
				Double acumuladoimpuesto = 0.0;
				List<Row> filas = vistaCotizacion.getDetalle().getFilas().getChildren();
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
							preciobase = art
									.getPrecioBase();
							System.out.printf("precio %.2f", preciobase);
						} else {
							preciobase = articulodeventa
									.getPrecioBase(vistaCotizacion
											.getCmbClientesPagador().getSeleccion()
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
					}
				}

				vistaCotizacion.getBase().setValue(
						Real.redondeoMoneda(acumuladobase));
				vistaCotizacion.getImpuesto().setValue(
						Real.redondeoMoneda(acumuladoimpuesto));
				vistaCotizacion.getTotal().setValue(
						Real.redondeoMoneda(acumuladobase)
								+ Real.redondeoMoneda(acumuladoimpuesto));
			}
	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		vistaCotizacion.actualizarModelo();
		
		getDato().setPagador( vistaCotizacion.getCmbClientesPagador().getSeleccion().getCliente());

		if (vistaCotizacion.getCmbClientesBeneficiario().getSeleccion()!=null) 
			getDato().setBeneficiario(vistaCotizacion.getCmbClientesBeneficiario().getSeleccion().getCliente());


		getDato().setDetallesContrato(vistaCotizacion.getColeccionGrilla());
		getDato().setFecha(vistaCotizacion.getDtmFechaEmision().getValue());
		getDato().setFechaDesde(vistaCotizacion.getDtmFechaInicio().getValue());
		getDato().setDiasVigencia(vistaCotizacion.getLblDiasVigencia().getValue());

		getDato().setTotal(vistaCotizacion.getTotal().getValue());
		getDato().setMonto(vistaCotizacion.getBase().getValue());
		getDato().setSaldo(vistaCotizacion.getTotal().getValue());
		getDato().setSede(vistaCotizacion.getSede());
		
		// getDato().setExpediente(true);
		List<ArchivoContrato> archivos = vistaCotizacion.getArchivos().getColeccion();
		for (ArchivoContrato archivoContrato : archivos) {
				archivoContrato.setContrato(getDato());	
		}
		getDato().setArchivoContrato(archivos);
		CotizacionVialidad as = getDato();
		
		for (DetalleContrato item : getDato().getDetallesContrato()) {
			item.setContrato(as);
			item.setImpuesto(item.getProducto().getImpuesto());
			item.setPrestado(true);
			item.setPrestado(false);
		}
		
		getDato().setTipoContrato(vistaCotizacion.getTipoContrato().getSeleccion());
		getDato().setUbicacion(vistaCotizacion.getCgbubicacionFisica().getModelo());
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
		if (vistaCotizacion.getCmbClientesPagador().getSeleccion() == null)
			throw new WrongValueException(vistaCotizacion.getCmbClientesPagador(),
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
		
		if (vistaCotizacion.getTipoContrato().getSeleccion()==null){
			throw new WrongValueException(vistaCotizacion.getTipoContrato(),
					"debe indicar el tipo de contrato");
		}
		
		if (vistaCotizacion.getTipoContrato().getSeleccion()==null){
			throw new WrongValueException(vistaCotizacion.getTipoContrato(),
					"debe indicar el tipo de contrato");
		}
		 if (vistaCotizacion.getCmbClientesBeneficiario().getSeleccion() == null
				&& vistaCotizacion.getTipoContrato().getSeleccion().getId() == 3)
			throw new WrongValueException(vistaCotizacion.getCmbClientesBeneficiario(),
					"Seleccione un Beneficiario");
		
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
		List<Row> filas = vistaCotizacion.getDetalle().getFilas().getChildren();

		for (Row item : filas) {
			IProducto articulodeventa = ((CompBuscar<IProducto>) item
					.getChildren().get(0)).getSeleccion();
			articulodeventa = negocioCotizacion
					.enriqueserProducto(articulodeventa);
			Double cantidad = ((Doublebox) item.getChildren().get(1))
					.getValue();
			Double preciounidad = ((Doublebox) item.getChildren().get(2))
					.getValue();
			TipoProductor tipoCliente = vistaCotizacion.getCmbClientesPagador()
					.getSeleccion().getCliente().getTipo();

			if ((articulodeventa != null) && (cantidad != null)) {
				if (articulodeventa instanceof ArticuloVenta) {
					acumuladobase = acumuladobase + (preciounidad * cantidad);
					double porimpuesto = ((((ArticuloVenta) articulodeventa)
							.getImpuesto().getPorcentaje()) / 100);
					acumuladoimpuesto = acumuladoimpuesto
							+ ((porimpuesto * preciounidad) * cantidad);
					DetalleContrato detalleContrato = new DetalleContrato();
					detalleContrato.setCantidad(cantidad);
					detalleContrato.setProducto(articulodeventa);
					detalleContrato.setPrecioUnidad(preciounidad);
					detalleContrato.setSubtotal(cantidad * preciounidad);
				} else {
					acumuladobase = acumuladobase + (preciounidad * cantidad);
					double porimpuesto = ((articulodeventa.getImpuesto()
							.getPorcentaje()) / 100);
					acumuladoimpuesto = acumuladoimpuesto
							+ ((porimpuesto * preciounidad) * cantidad);
					DetalleContrato detalleContrato = new DetalleContrato();
					detalleContrato.setCantidad(cantidad);
					detalleContrato.setProducto(articulodeventa);
					detalleContrato.setPrecioUnidad(preciounidad);
					detalleContrato.setSubtotal(cantidad * preciounidad);
				}

			}
		}

		vistaCotizacion.getBase().setValue(Real.redondeoMoneda(acumuladobase));
		vistaCotizacion.getImpuesto().setValue(
				Real.redondeoMoneda(acumuladoimpuesto));
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
		if (i > getMaximoRenglon())
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
				precio =art.getPrecioBase();
				System.out.printf("precio %.2f", precio);
			} else {
				precio = producto
						.getPrecioBase(vistaCotizacion.getCmbClientesPagador()
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
			//Doublebox t3 = ((Doublebox) registro.getChildren().get(3));
			if (componente.equals(t1)) {
				double precio = ((Doublebox) registro.getChildren().get(2))
						.getValue();
				((Doublebox) registro.getChildren().get(3)).setValue(componente
						.getValue() * precio);
				ActualizarTotales();
			}

			if (componente.equals(t2)) {
				double cantidad = ((Doublebox) registro.getChildren().get(1))
						.getValue();
				((Doublebox) registro.getChildren().get(3)).setValue(componente
						.getValue() * cantidad);
				ActualizarTotales();
			}

		} catch (Exception e) {
			throw new ExcFiltroExcepcion("Problemas con el servicio");
		}
	}

	@SuppressWarnings("unchecked")
	public void ActualizarFecha() {
		Date fechai = vistaCotizacion.getDtmFechaInicio().getValue();
		Integer diasvigencia = vistaCotizacion.getLblDiasVigencia().getValue();
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
		
		if (lista.contains(event.getTarget())) {
			return true;
		} else
			return false;
	}

	public void ActualizarDireccion(Event event) throws ExcFiltroExcepcion {

	}

	public static boolean saveFile(Media media, String path) {
		boolean uploaded = false;
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			InputStream ins = media.getStreamData();
			in = new BufferedInputStream(ins);

			String fileName = media.getName();
			File arc = new File(path + fileName);
			OutputStream aout = new FileOutputStream(arc);
			out = new BufferedOutputStream(aout);

			byte buffer[] = new byte[1024];
			int ch = in.read(buffer);
			while (ch != -1) {
				out.write(buffer, 0, ch);
				ch = in.read(buffer);
			}
			uploaded = true;
		} catch (IOException ie) {
			throw new RuntimeException(ie);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return uploaded;
	}

	//se rediceño estaba guardando la ruta a el archivo y no el archivo
	public static boolean SubirArchivos(Media media, String path, Row fila) {
		boolean uploaded = false;
		  try {  
			  Object obj2 = fila.getAttribute("dato");
			  ((ArchivoContrato)obj2).setNombreArchivo(media.getName());
			  InputStream datos = media.getStreamData();
			  byte[] bytes = InputStreamToByteArray(datos);
			  ((ArchivoContrato)obj2).setArchivo(bytes);
			  fila.setAttribute("dato", obj2);
			  ((Textbox ) fila.getChildren().get(2)).setValue(media.getName());
			  uploaded=true;
		} 
		catch (IOException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		}
		return uploaded;
	}

	public void bajarArchivo(Row fila) throws IOException, ClassNotFoundException {
	    ArchivoContrato obj2 = (ArchivoContrato) fila.getAttribute("dato");
	    InputStream  arraydedatos = ByteArrayToInputStreamTo(obj2.getArchivo());
	    Desktop desktop = app.getWin().getDesktop();
		String path = desktop.getWebApp().getRealPath("/entradas")+ "/";
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(arraydedatos);
			String fileName = obj2.getNombreArchivo();
			File arc = new File(path + fileName);
			OutputStream aout = new FileOutputStream(arc);
			out = new BufferedOutputStream(aout);
			byte buffer[] = new byte[1024];
			int ch = in.read(buffer);
			while (ch != -1) {
				out.write(buffer, 0, ch);
				ch = in.read(buffer);
			}
			System.out.println(arc.isFile());
			System.out.println(arc.exists());
		FileInputStream axu = new FileInputStream(arc);
			Filedownload.save(axu, "text/html", arc.getName()); 
		} catch (IOException ie) {
			throw new RuntimeException(ie);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		//el archivo file sole existe en memoria por lo que hace una  
		/// java.io.FileNotFoundException hay que verificar que este escrito en disco para leerlo
	}

	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(obj);
		return b.toByteArray();
	}

	public static Object deserialize(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}

	public static byte[] InputStreamToByteArray(InputStream is) throws IOException{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[16384];
		while ((nRead = is.read(data, 0, data.length)) != -1) {
		  buffer.write(data, 0, nRead);
		}
		buffer.flush();
		return buffer.toByteArray();
		
	}
	
	public static InputStream ByteArrayToInputStreamTo(byte[] is) throws IOException{
		InputStream myInputStream = new ByteArrayInputStream(is);
		return myInputStream;
	}
}
