package cpc.demeter.catalogo.transporte;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;





import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zkplus.spring.SpringUtil;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.controlador.mantenimiento.ContCotizacionServicio;
import cpc.modelo.demeter.administrativo.Cotizacion;
import cpc.modelo.demeter.administrativo.EstadoContrato;
import cpc.modelo.demeter.administrativo.TipoCotizacion;
import cpc.negocio.demeter.administrativo.NegocioCotizacion;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContCotizacionesSimplesTransporte extends ContCatalogo<Cotizacion> implements
		EventListener {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;
	private NegocioCotizacion negocioCotizacion;

	public ContCotizacionesSimplesTransporte(AccionFuncionalidad accionesValidas, AppDemeter app)		throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,	ExcArgumentoInvalido, ExcFiltroExcepcion {
		
		/**** Puro las Cotizaciones Asociadas a Transporte *****/
		negocioCotizacion = NegocioCotizacion.getInstance();
		dibujar(accionesValidas, "COTIZACIONES TRANSPORTE ",	negocioCotizacion.getCotizacionesExpedienteTransporte(), app);
		this.app = app;
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
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

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
	try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR || accion == Accion.PROCESAR)
			{
				Cotizacion cotizacion = getDatoSeleccionado();
				
				if (cotizacion != null	&& accion == Accion.ANULAR 	&& cotizacion.getEstado().getId()
								.equals(EstadoContrato.ESTADO_ANULADO))
				{
					throw new Exception("El Documento ya esta Anulado");
					
				}
				
				if (cotizacion != null	&& accion == Accion.ANULAR )
				{
					
					new ContCotizacionServicio(accion, cotizacion, this, this.app);
			
				}
				if (cotizacion != null && accion == Accion.EDITAR && cotizacion.getFacturado())
				{
					throw new Exception("El Documento ya esta facturado");
				}

				if (cotizacion != null && accion == Accion.PROCESAR) 
				{
					boolean a = (cotizacion.getEstado().getDescripcion().equals("Por Firmar"));
					boolean b = (cotizacion.getEstado().getDescripcion().equals("Activo"));
					boolean c = (!a && !b);
					if (c)
						throw new Exception("Solo puede Procesar Documentos en estado Por Firmar O Activo");
				}
				if (cotizacion == null || accion == Accion.AGREGAR)
				{
					cotizacion = new Cotizacion();
					TipoCotizacion tipo = negocioCotizacion.getTransporte();
					cotizacion.setTipoCotizacion(tipo);
					new ContCotizacionServicio(accion, cotizacion, this, this.app);
					
				} 
				
				if (cotizacion != null || accion == Accion.CONSULTAR)
				{
				
					TipoCotizacion tipo = negocioCotizacion.getTransporte();
					cotizacion.setTipoCotizacion(tipo);
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
			app.mostrarError("No ha seleccionado ningun registro");
		} catch (Exception e) {
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(Cotizacion arg0) {
		return null;
	}

	private void imprimirDetalle() {
 
		String icaro = (String) SpringUtil.getBean("contrato");
 		String cadena = "";
		cadena = cadena + icaro;
		Cotizacion ctto = getDatoSeleccionado();
		switch (ctto.getTipoContrato().getId()) {
		case 1:
			// cadena.append("rpt_contratoserviciomecanizadogravado&numerocontrato=");
			cadena = cadena
					+ "rpt_contratoserviciomecanizadogravado&numerocontrato=";
			break;
		case 2:
			// cadena.append("rpt_contratoserviciomecanizadoexonerado&numerocontrato=");
			cadena = cadena
					+ "rpt_contratoserviciomecanizadoexonerado_carta&numerocontrato=";
			break;
		case 3:
			// cadena.append("rpt_contratoserviciomecanizadocredito&numerocontrato=");
			cadena = cadena
					+ "rpt_contratoserviciomecanizadocredito_carta&numerocontrato=";
			break;
		}
		cadena = cadena
				+ ctto.getId()
				+ "&Sede="
				+ app.getSede().getNombre()
				+ "&jdbcUrl="
				+ SessionDao.getConfiguration().getProperty(
						"hibernate.connection.url");
		// cadena=cadena+","+"'name'"+",'toolbar=1,scrollbars=1,location=1,status=1,menubar=1,resizable=1,width=800,height=600'"+");";

		/*
		 * cadena.append("window.open("); cadena.append(icaro);
		 * ContratoMecanizado ctto = getDatoSeleccionado(); switch
		 * (ctto.getTipoContrato().getId()) { case 1:
		 * //cadena.append("rpt_contratoserviciomecanizadogravado&numerocontrato="
		 * ); cadena.append(
		 * "rpt_contratoserviciomecanizadogravado_carta&numerocontrato=");
		 * break; case 2:
		 * //cadena.append("rpt_contratoserviciomecanizadoexonerado&numerocontrato="
		 * ); cadena.append(
		 * "rpt_contratoserviciomecanizadoexonerado_carta&numerocontrato=");
		 * break; case 3:
		 * //cadena.append("rpt_contratoserviciomecanizadocredito&numerocontrato="
		 * ); cadena.append(
		 * "rpt_contratoserviciomecanizadocredito_carta&numerocontrato=");
		 * break; } String cadena2=
		 * "http://172.16.5.93:8080/icaro/demeter?reporte=rpt_contratoserviciomecanizadogravado_carta&numerocontrato=5&Sede=ESPC TRUJILLO&jdbcUrl=jdbc:postgresql://localhost:5432/db_demeter_trujillo"
		 * ; Desktop a = app.getWin().getDesktop(); a.getUpdateURI(cadena2);
		 * 
		 * cadena.append(ctto.getId()); cadena.append("&Sede=");
		 * cadena.append(app.getSede().getNombre()); cadena.append("&jdbcUrl=");
		 * cadena.append(SessionDao.getConfiguration().getProperty(
		 * "hibernate.connection.url")); cadena.append("'"); //);
		 * cadena.append(","); cadena.append("'name'"); // cadena.append(",");
		 * cadena.append(
		 * ",'toolbar=1,scrollbars=1,location=1,status=1,menubar=1,resizable=1,width=800,height=600'"
		 * ); cadena.append(");"); // String a = "This will close the window";
		 * // cadena.append("confirm("+a+");"
		 * 
		 * System.out.println(cadena.toString());
		 * 
		 * // scrip.setContent(cadena.toString()); // app.agregar(scrip);
		 */
		Execution ad = Executions.getCurrent();
		System.out.println(cadena);
		ad.sendRedirect(cadena);
	}

	public void imprimir(Cotizacion cotizacion) {
		try {
			NegocioCotizacion negocio = NegocioCotizacion.getInstance();

			// negocio.getPersistencia().getDatos(solicitudMecanizado).getDetalles();

			HashMap parametros = new HashMap();
			// parametros.put("filtro", String.format("%1$td/%1$tm/%1$tY",
			// inicio));
			parametros.put("fecha",
					String.format("%1$td/%1$tm/%1$tY", cotizacion.getFecha()));
			parametros.put("usuario", app.getUsuario().toString());
			parametros.put("logo", Index.class.getResource("/").getPath()
					+ "../../imagenes/cintillo_inst.png");
			JRDataSource ds = new JRBeanCollectionDataSource(
					cotizacion.getDetallesContrato());
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();
			System.out.println(cotizacion.getFecha());
			System.out.println(app.getUsuario().toString());
			reporte.setSrc("reportes/CotizacionSimpleTransporte.jasper");
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