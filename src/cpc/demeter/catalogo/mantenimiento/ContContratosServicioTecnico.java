package cpc.demeter.catalogo.mantenimiento;

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
import cpc.demeter.controlador.mantenimiento.ContContratoServicioTecnico;
import cpc.modelo.demeter.administrativo.ContratoServicioTecnico;
import cpc.modelo.demeter.administrativo.EstadoContrato;
import cpc.negocio.demeter.administrativo.NegocioContratoServicioTecnico;
import cpc.negocio.demeter.administrativo.NegocioCotizacion;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContContratosServicioTecnico extends
		ContCatalogo<ContratoServicioTecnico> implements EventListener {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;
	private NegocioContratoServicioTecnico negocioContrato;

	public ContContratosServicioTecnico(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		negocioContrato = NegocioContratoServicioTecnico.getInstance();
		dibujar(accionesValidas, "TOTALIZAR SERVICIO",
				negocioContrato.getContratos(), app);
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
			if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR
					|| accion == Accion.PROCESAR) {
				ContratoServicioTecnico contrato = getDatoSeleccionado();
				if (contrato != null
						&& accion == Accion.ANULAR
						&& contrato.getEstado().getId()
								.equals(EstadoContrato.ESTADO_ANULADO))
					throw new Exception("El Documento ya esta Anulado");
				if (contrato != null && accion == Accion.PROCESAR) {
					boolean a = (contrato.getEstado().getDescripcion()
							.equals("Por Firmar"));
					boolean b = (contrato.getEstado().getDescripcion()
							.equals("Activo"));
					boolean c = (!a && !b);
					if (c)
						throw new Exception(
								"Solo puede Procesar Documentos en estado Por Firmar O Activo");
				}

				if (contrato == null || accion == Accion.AGREGAR)
					contrato = new ContratoServicioTecnico();
				new ContContratoServicioTecnico(accion, contrato, this,
						this.app);

			} else
				imprimir(getDatoSeleccionado());

		} catch (NullPointerException e) {
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
	public List cargarDato(ContratoServicioTecnico arg0) {
		return null;
	}

	private void imprimirDetalle() {

		String icaro = (String) SpringUtil.getBean("contrato");
		String cadena = "";
		cadena = cadena + icaro;
		ContratoServicioTecnico ctto = getDatoSeleccionado();
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

	public void imprimir(ContratoServicioTecnico expediente) {
		try {
			NegocioCotizacion negocio = NegocioCotizacion.getInstance();

			// negocio.getPersistencia().getDatos(solicitudMecanizado).getDetalles();

			HashMap parametros = new HashMap();
			// parametros.put("filtro", String.format("%1$td/%1$tm/%1$tY",
			// inicio));
			parametros.put("fecha",
					String.format("%1$td/%1$tm/%1$tY", expediente.getFecha()));
			parametros.put("usuario", app.getUsuario().toString());
			parametros.put("logo", Index.class.getResource("/").getPath()
					+ "../../imagenes/cintillo_inst.png");
			JRDataSource ds = new JRBeanCollectionDataSource(
					expediente.getDetallesContrato());
			JRDataSource salidas = new JRBeanCollectionDataSource(
					expediente.getDetalleContratoSalidaArticulos());
			JRDataSource devoluciones = new JRBeanCollectionDataSource(
					expediente.getDetalleContratoDevolucionArticulos());
			parametros.put("salidas", salidas);
			parametros.put("devoluciones", devoluciones);
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();
			System.out.println(expediente.getFecha());
			System.out.println(app.getUsuario().toString());
			reporte.setSrc("reportes/ExpedienteServicioTecnico.jasper");
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
