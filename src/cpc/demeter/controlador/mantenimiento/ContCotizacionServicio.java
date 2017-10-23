package cpc.demeter.controlador.mantenimiento;

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
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;

import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.vista.mantenimiento.UICotizacionExpediente;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.demeter.administrativo.Cotizacion;
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.demeter.mantenimiento.SalidaExternaArticulo;
import cpc.modelo.ministerio.basico.TipoProductor;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.negocio.demeter.administrativo.NegocioClienteAdministrativo;
import cpc.negocio.demeter.administrativo.NegocioCotizacion; 
import cpc.negocio.sigesp.NegocioSede;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.utilidades.Fecha;
import cva.pc.demeter.utilidades.Real;
import cpc.zk.componente.controlador.ContCatalogo; 
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompGrilla;
import cpc.zk.componente.listas.ContVentanaBase;

@SuppressWarnings("serial")
public class ContCotizacionServicio extends ContVentanaBase<Cotizacion> implements EventListener {
	
	private NegocioCotizacion negocioCotizacion;
	private UICotizacionExpediente vistaCotizacion;
	private AppDemeter app;

	@SuppressWarnings("unused")
	public ContCotizacionServicio(int modoOperacion, Cotizacion cotizacion,	ContCatalogo<Cotizacion> llamador, AppDemeter app) 	throws ExcDatosInvalidos, ExcFiltroExcepcion {
		super(modoOperacion);
		// TODO Auto-generated constructor stub
		this.app = app;
		setDato(cotizacion);
		negocioCotizacion = NegocioCotizacion.getInstance();
		List<IProducto> articuloVentas  = new ArrayList<IProducto>();
		if (getDato().getTipoCotizacion().getId().equals(negocioCotizacion.getMantenimiento().getId()))
		{
			articuloVentas = negocioCotizacion.getArticulosServicios();
		}
		
		if (getDato().getTipoCotizacion().getId().equals(negocioCotizacion.getVialidad().getId()))
		{
			articuloVentas = negocioCotizacion.getArticulosServicioVialidad();;
		}
		
		if (getDato().getTipoCotizacion().getId().equals(negocioCotizacion.getTransporte().getId()))
		{
			articuloVentas = negocioCotizacion.getArticulosServicioTransporte();
		}
		
		List<Almacen> almacenes = negocioCotizacion.getAlmacenesSalidaExterna();
		List<DetalleContrato> detallesContrato = new ArrayList<DetalleContrato>();
		List<ClienteAdministrativo> clientes = negocioCotizacion.getClientesAdministrativos();
		SalidaExternaArticulo salidaExternaArticulo = new SalidaExternaArticulo();

		if (modoAgregar() || cotizacion == null) {

		} else {

			detallesContrato = cotizacion.getDetallesContrato();
			salidaExternaArticulo = cotizacion.getSalidaExternaArticulo();
		}

		vistaCotizacion =  new UICotizacionExpediente("Cotizacion de Servicio Tecnico",1200,getModoOperacion(), articuloVentas, 
					almacenes, clientes, salidaExternaArticulo, cotizacion,NegocioSede.getInstance().getTodos(),app.getSede().getNombre());
		setear(cotizacion,vistaCotizacion,llamador, app);
 
		vistaCotizacion = (UICotizacionExpediente) getVista();
		vistaCotizacion.getDtmFechaEmision().setValue(new Date());
		vistaCotizacion.getDtmFechaInicio().setValue(new Date());
		vistaCotizacion.getLblDiasVigencia().setValue(1);
		vistaCotizacion.getLblDiasVigencia().setReadonly(true);

		if (modoEditar())
		{
			vistaCotizacion.desactivarEditar();
		}
        if (modoAnular())
        {
        	vistaCotizacion.desactivarAnular();
        }
        if(modoConsulta())
        {
        	vistaCotizacion.activarConsulta();
        }
        vistaCotizacion.setTitle("Cotizaciones Simples de Transporte");
        vistaCotizacion.getDtmFechaEmision().setValue(new Date());
		vistaCotizacion.getDtmFechaInicio().setValue(new Date());
		vistaCotizacion.getLblDiasVigencia().setValue(1);
		vistaCotizacion.getLblDiasVigencia().setReadonly(true);
		vistaCotizacion.getDtmFechaEmision().setReadonly(true);
		vistaCotizacion.getDtmFechaInicio().setReadonly(true);
	}




	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(Event event) throws Exception 
	{
		ActualizarTotales(event);
		if (event.getName().equals(CompBuscar.ON_SELECCIONO)) 
		{
			if (event.getTarget() == vistaCotizacion.getCmbClientes())
			{
			   ClienteAdministrativo clienteAdministrativo =negocioCotizacion.getClienteAdministrativo(vistaCotizacion.getCmbClientes().getSeleccion());
			   vistaCotizacion.getCmbClientes().setSeleccion(clienteAdministrativo);
			   vistaCotizacion.getLblDireccionPagador().setValue(vistaCotizacion.getCmbClientes().getSeleccion()
					   					.getCliente().getDireccion());
			   vistaCotizacion.getLblCedulaRifPagado().setValue(
					   					vistaCotizacion.getCmbClientes().getSeleccion()
					   					.getCedulaCliente());
			   //genero reporte de deudas
			   NegocioClienteAdministrativo negocio = NegocioClienteAdministrativo.getInstance();
			   Object[] deudor = negocio.getPendienteDocumentosNotasTotales(clienteAdministrativo.getCliente());
			  
			   if ((((Double)deudor[3]+(Double)deudor[4]))>0){
					  Messagebox.show("Este Productor Posee Deudas Asociadas con la Institucion ");
					List<Object[]> deudores = new ArrayList<Object[]>();
					deudores.add(deudor);
					HashMap<String, String> parametros = new HashMap<String, String>();
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
			   }//end if Deuda 
			}
			else
			{
				actualizarArticulo((CompBuscar<IProducto>) event.getTarget());
			}
		}
		else if (event.getName().equals(Events.ON_CHANGING))
		{
			ActualizarFecha();
		}
		else if (event.getName().equals(Events.ON_CHANGE))
		{
			if (event.getTarget()==vistaCotizacion.getLblDiasVigencia())
			{
				ActualizarFecha();
			}
			else if (event.getTarget()==vistaCotizacion.getDtmFechaInicio())
			{
				ActualizarFecha();
			}
			else
			{
				actualizarPrecio((Doublebox) event.getTarget());
			}
		}
		if (event.getName().equals("onBorrarFila"))
		{
			ActualizarTotales();
		}
		else if (event.getName().equals(CompGrilla.ON_ELIMINA_FILA)) 
		{
			ActualizarTotales();
		}
		if (event.getTarget() == getVista().getAceptar()) 
		{
			validar();
			procesarCRUD(event);
		}

	}

	@SuppressWarnings("unchecked")
	public void ActualizarTotales(Event event)
	{
		if (event.getName().equals(Events.ON_CHANGE) || event.getName().equals(CompBuscar.ON_SELECCIONO)) 
			if (event.getTarget().getParent().getClass() == Row.class) 
			{
				Double acumuladobase = 0.0;
				Double acumuladoimpuesto = 0.0;

				List<Row> filas = vistaCotizacion.getDetalle().getFilas().getChildren();

				for (Row item : filas) 
				{
					
					((CompBuscar<IProducto>) item.getChildren().get(0)).setSeleccion(
							 negocioCotizacion.enriqueserProducto(
									 ((CompBuscar<IProducto>) item.getChildren().get(0)).getSeleccion()));

					IProducto articulodeventa = ((CompBuscar<IProducto>) item.getChildren().get(0)).getSeleccion();
					Double cantidad = ((Doublebox) item.getChildren().get(1)).getValue();
					Doublebox precio = (Doublebox) item.getChildren().get(2);
					
					Double precioNuevo = (precio.getValue() == null ? 0 : precio.getValue());
					
					if ((articulodeventa != null) && (cantidad != null)) 
					{
						double preciobase = 0.0;
		                if (articulodeventa instanceof ArticuloVenta)
		                {
		    				ArticuloVenta art = (ArticuloVenta) articulodeventa;
							precioNuevo = ( precioNuevo == 0 ?  art.getPrecioBase() : precioNuevo );
	    					preciobase = precioNuevo;
		                }
		                else
		                {
		    				precioNuevo = ( precioNuevo == 0 ?  articulodeventa.getPrecioBase(
	    						 	vistaCotizacion.getCmbClientes().getSeleccion().getCliente().getTipo()): precioNuevo );
	    					preciobase = precioNuevo;
    					}
						Double iva =  (articulodeventa.getImpuesto().getPorcentaje()/100) * precioNuevo;
		                acumuladobase = acumuladobase + (preciobase * cantidad);
		                acumuladoimpuesto = acumuladoimpuesto + (iva * cantidad);
		                DetalleContrato detalleContrato = new DetalleContrato();
						detalleContrato.setCantidad(cantidad);
						detalleContrato.setProducto(articulodeventa);
						detalleContrato.setPrecioUnidad(preciobase);
						detalleContrato.setSubtotal(cantidad* preciobase);
					}
				}

				vistaCotizacion.getBase().setValue(Real.redondeoMoneda(acumuladobase));
				vistaCotizacion.getImpuesto().setValue(Real.redondeoMoneda(acumuladoimpuesto));
				vistaCotizacion.getTotal().setValue(Real.redondeoMoneda(acumuladobase )+Real.redondeoMoneda(acumuladoimpuesto));

			}
	}

	public void guardar() throws ExcFiltroExcepcion {

		try 
		{
			vistaCotizacion.actualizarModelo();
			getDato().setPagador(vistaCotizacion.getCmbClientes().getSeleccion().getCliente());
			getDato().setDetallesContrato(vistaCotizacion.getColeccionGrilla());
			getDato().setFecha(vistaCotizacion.getDtmFechaEmision().getValue());
			getDato().setFechaDesde(vistaCotizacion.getDtmFechaInicio().getValue());
			getDato().setDiasVigencia(vistaCotizacion.getLblDiasVigencia().getValue());
			getDato().setTotal(vistaCotizacion.getTotal().getValue());
			getDato().setMonto(vistaCotizacion.getBase().getValue());
			getDato().setSaldo(vistaCotizacion.getTotal().getValue());
			getDato().setExpediente(true);
			getDato().setSede(vistaCotizacion.getSede());
			Cotizacion as = getDato();
			for (DetalleContrato item : getDato().getDetallesContrato()) 
			{
				item.setContrato(as);
				item.setImpuesto(item.getProducto().getImpuesto());
				item.setPrestado(true);
				item.setPrestado(false);
			}
			negocioCotizacion.guardar(getDato());
		}
		catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		try 
		{
			vistaCotizacion.actualizarModelo();
			negocioCotizacion.eliminar(getDato());
		}
		catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		if (vistaCotizacion.getCmbClientes().getSeleccion() == null)
		{
			throw new WrongValueException(vistaCotizacion.getCmbClientes(),	"Debe seleccionar un cliente");
		}
		if (vistaCotizacion.getDtmFechaEmision().getValue() == null)
		{
			throw new WrongValueException(vistaCotizacion.getDtmFechaEmision(),	"Debe indicar una fecha de emision");
		}
		if (vistaCotizacion.getSede() == null){
			throw new WrongValueException(vistaCotizacion.getCmbsede(),"Debe seleccionar una sede ");
		}
		if (vistaCotizacion.getDtmFechaInicio().getValue() == null)
		{
			throw new WrongValueException(vistaCotizacion.getDtmFechaInicio(),	"Debe indicar la fecha de inicio");
		}
		if (vistaCotizacion.getLblDiasVigencia() == null)
		{
			throw new WrongValueException(vistaCotizacion.getLblDiasVigencia(),	"Debe indicar los dias de vigencia");
		}
		if (vistaCotizacion.getDetalle().getFilas().getChildren().isEmpty()) 
		{
			throw new WrongValueException(vistaCotizacion.getDetalle(),"debe indicar al menos un articulo");
		}
		if (modoAnular())
		{
			int hijos = 0;
			try {
				hijos = negocioCotizacion.getHijosActivos(getDato());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			if (hijos>0)
			{
				throw new WrongValueException(vistaCotizacion.getDetalle(),	"Esta Cotizacion Tiene Hijos Activos (Factura)");
			}
		}
		validarDetalle();
	}

	@Override
	public void anular() throws ExcFiltroExcepcion 
	{
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
		for (Row item : filas) 
		{
			IProducto articulodeventa = ((CompBuscar<IProducto>) item.getChildren().get(0)).getSeleccion();
			articulodeventa= negocioCotizacion.enriqueserProducto(articulodeventa);
			Double cantidad = ((Doublebox) item.getChildren().get(1)).getValue();
			Doublebox precio =((Doublebox) item.getChildren().get(2)); 
			Double precioNuevo = (precio.getValue() == null ? 0 : precio.getValue());
			TipoProductor tipoCliente = vistaCotizacion.getCmbClientes().getSeleccion().getCliente().getTipo();
			if ((articulodeventa != null) && (cantidad != null)) 
			{
				if (articulodeventa instanceof ArticuloVenta )
				{
					precioNuevo =( precioNuevo == 0 ? ((ArticuloVenta)articulodeventa).getPrecioBase() : precioNuevo );
					acumuladobase = acumuladobase+ ( precioNuevo * cantidad);	
					Double iva =  (articulodeventa.getImpuesto().getPorcentaje()/100) * precioNuevo;
					acumuladoimpuesto = acumuladoimpuesto + ( iva * cantidad);
					DetalleContrato detalleContrato = new DetalleContrato();
					detalleContrato.setCantidad(cantidad);
					detalleContrato.setProducto(articulodeventa);
					detalleContrato	.setPrecioUnidad(((ArticuloVenta)articulodeventa).getPrecioBase());
					detalleContrato.setSubtotal(cantidad*((ArticuloVenta) articulodeventa).getPrecioBase());
				}
				else
				{
					precioNuevo = ( precioNuevo == 0 ?  articulodeventa.getPrecioBase(tipoCliente) : precioNuevo );
					acumuladobase = acumuladobase+ ( precioNuevo * cantidad);	
					Double iva =  (articulodeventa.getImpuesto().getPorcentaje()/100) * precioNuevo;
					acumuladoimpuesto = acumuladoimpuesto + ( iva * cantidad);
					DetalleContrato detalleContrato = new DetalleContrato();
					detalleContrato.setCantidad(cantidad);
					detalleContrato.setProducto(articulodeventa);
					detalleContrato	.setPrecioUnidad(articulodeventa.getPrecioBase(tipoCliente));
					detalleContrato.setSubtotal(cantidad* articulodeventa.getPrecioBase(tipoCliente));
				}
			}
		}
		
		vistaCotizacion.getBase().setValue(Real.redondeoMoneda(acumuladobase));
		vistaCotizacion.getImpuesto().setValue(Real.redondeoMoneda(acumuladoimpuesto));
		vistaCotizacion.getTotal().setValue(Real.redondeoMoneda(acumuladobase)+Real.redondeoMoneda( acumuladoimpuesto));

	}

	@SuppressWarnings("unchecked")
	public void validarDetalle() throws WrongValuesException,ExcEntradaInconsistente 
	{
		List<Row> filas = vistaCotizacion.getDetalle().getFilas().getChildren();
        int i = 0;
        for (Row row : filas) 
        {
            i++;
 
			CompBuscar<ArticuloVenta> articulo = (CompBuscar<ArticuloVenta>) row.getChildren().get(0);
			Doublebox cantidad = (Doublebox) row.getChildren().get(1);
			if (articulo.getSeleccion() == null) 
			{
				throw new WrongValueException(articulo," Seleccione un Articulo ");
			}
			if (cantidad.getValue() == null || cantidad.getValue().equals(0))
			{
				throw new WrongValueException(cantidad,"La cantidad debe ser mayor a 0 ");
			}
		}
        if (i > getMaximoRenglon())
        {
			throw new WrongValueException(vistaCotizacion.getDetalle(),	"cotizacion no puede tener mas de " + getMaximoRenglon() + "renglones");
        }
	}

	
	public void actualizarArticulo(CompBuscar<IProducto> componente)throws ExcFiltroExcepcion 
	{
		IProducto producto = null;
		Double precio =0.0;
		Row registro = (Row) componente.getParent();

		try {
			producto = componente.getSeleccion();
			producto =	negocioCotizacion.enriqueserProducto(producto);
		} 
		catch (NullPointerException e)  {
			throw new ExcFiltroExcepcion("Problemas con el producto seleccionado");
		}

		try 
		{
			if (producto instanceof ArticuloVenta)
			{
				ArticuloVenta art = (ArticuloVenta) componente.getSeleccion();
				precio =art.getPrecioBase();
			}
			else
			{
				precio = producto.getPrecioBase(vistaCotizacion.getCmbClientes().getSeleccion().getCliente().getTipo()); 
			}
			((Doublebox) registro.getChildren().get(2)).setValue(precio);
			Double cantidad = ((Doublebox) registro.getChildren().get(1)).getValue();
			if (cantidad == null) {
				((Doublebox) registro.getChildren().get(1)).setValue(1.0);
				cantidad = 1.0;
			}
			((Doublebox) registro.getChildren().get(3)).setValue(
					Math.floor(precio * cantidad * 10000.00) / 10000.00);
			ActualizarTotales();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			throw new ExcFiltroExcepcion("Problemas con el servicio");
		}
	}
	
	public void actualizarPrecio(Doublebox componente)throws ExcFiltroExcepcion
	{
		try {
			Row registro = (Row) componente.getParent();
			double cantidad = ((Doublebox) registro.getChildren().get(1)).getValue();
			double precio = ((Doublebox) registro.getChildren().get(2)).getValue();
			((Doublebox) registro.getChildren().get(3)).setValue(	Math.round(precio * cantidad * 10000.00) / 10000.00);
			ActualizarTotales();
		} 
		catch (Exception e) 
		{
			throw new ExcFiltroExcepcion("Problemas con el servicio");
		}
	}
	
	public void ActualizarFecha() {
		Date fechai = vistaCotizacion.getDtmFechaInicio().getValue();
		Integer diasvigencia = vistaCotizacion.getLblDiasVigencia().getValue();
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(fechai);
		if  (diasvigencia!=null)
		calendario.add(Calendar.DATE, diasvigencia);
		Date fechah = calendario.getTime();
		
		vistaCotizacion.getLblFechaHasta().setValue(Fecha.obtenerFecha(fechah));
		
	}

	private Integer getMaximoRenglon()
	{
		return (Integer) SpringUtil.getBean("lineasFactura");
	}
	
}
