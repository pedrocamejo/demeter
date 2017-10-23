package cpc.demeter.catalogo.vialidad;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.controlador.mantenimiento.ContCotizacionServicio;
import cpc.modelo.demeter.administrativo.Cotizacion;
import cpc.modelo.demeter.administrativo.TipoCotizacion;
import cpc.negocio.demeter.administrativo.NegocioCotizacion;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContCotizaciones extends ContCatalogo<Cotizacion> implements
		EventListener {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;
	private NegocioCotizacion negocioCotizacion;

	public ContCotizaciones(AccionFuncionalidad accionesValidas, AppDemeter app)		throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,	ExcArgumentoInvalido, ExcFiltroExcepcion {
		
		/**** Puro las Cotizaciones Asociadas a Mantenimiento *****/
		negocioCotizacion = NegocioCotizacion.getInstance();
		dibujar(accionesValidas, "COTIZACIONES MANTENIMIENTO ",	negocioCotizacion.getCotizacionesExpedeinteVialidad(), app);
		this.app = app;
	}

	public List<CompEncabezado> cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Control", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrNroDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getFechaString");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cliente", 300);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreCliente");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto Base", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMonto");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getEstado");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Sede", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getSede");
		encabezado.add(titulo);

		
		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
	try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			
			if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR || accion == Accion.ELIMINAR)
			{
				Cotizacion cotizacion = getDatoSeleccionado();
				if (accion == Accion.AGREGAR)
				{
					cotizacion = new Cotizacion();
					TipoCotizacion tipo = negocioCotizacion.getVialidad();
					cotizacion.setTipoCotizacion(tipo);
					new ContCotizacionServicio(accion, cotizacion, this, this.app);
				} 
				else if(accion ==  Accion.CONSULTAR) 
				{	
					if(cotizacion == null)
					{
						throw new Exception("No ha seleccionado ningun registro");
					}
					new ContCotizacionServicio(accion, cotizacion, this, this.app);
				}
				else if(accion == Accion.ANULAR ) 
				{	
					if(cotizacion == null)
					{
						throw new Exception("No ha seleccionado ningun registro");
					}
					if (cotizacion.getFacturado())
					{	
						throw new Exception("El Documento ya esta facturado");
					}
					new ContCotizacionServicio(accion, cotizacion, this, this.app);
				}

			}
			else
			{
				imprimir(getDatoSeleccionado());
			}
		} // end try 
		catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

 
	@SuppressWarnings("unchecked")
	protected List cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

	@Override
	public List<Cotizacion> cargarDato(Cotizacion arg0) {
		return null;
	} 

	public void imprimir(Cotizacion cotizacion) {
		try {
			HashMap<String, String> parametros = new HashMap<String, String>();
			parametros.put("fecha",String.format("%1$td/%1$tm/%1$tY", cotizacion.getFecha()));
			parametros.put("usuario", app.getUsuario().toString());
			parametros.put("logo", Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");
			JRDataSource ds = new JRBeanCollectionDataSource(cotizacion.getDetallesContrato());
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();
			reporte.setSrc("reportes/CotizacionVialidadSimple.jasper");
			reporte.setParameters(parametros);
			reporte.setDatasource(ds);
			reporte.setType("pdf");
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ninguna solicitud");
		} catch (Exception e) {
			app.mostrarError(e.getMessage());
		}
	}

}