package cpc.demeter.catalogo.mantenimiento;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Filedownload;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.controlador.mantenimiento.ContCertificado;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.demeter.mantenimiento.CertificadoOrigen;
import cpc.modelo.demeter.mantenimiento.MaquinariaExterna;
import cpc.modelo.demeter.mantenimiento.PlantaDistribuidora;
import cpc.negocio.demeter.administrativo.NegocioFormaPago;
import cpc.negocio.demeter.mantenimiento.NegocioCertificadoOrigen;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContOrigen extends ContCatalogo<CertificadoOrigen> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContOrigen(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioCertificadoOrigen servicio = NegocioCertificadoOrigen
				.getInstance();
		this.app = app;
		dibujar(accionesValidas, "ORIGEN MAQUINARIA",
				servicio.getCertificados(), app);

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Nro Control ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroControl");
		encabezado.add(titulo);

		titulo = new CompEncabezado(" Planta Origen  ", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStr_PlantaOrigen");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Serial Carroceria ", 160);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStr_SerialCarroceria");
		encabezado.add(titulo);



		titulo = new CompEncabezado(" Propietario ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrPropietario");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Factura   ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroFactura");
		encabezado.add(titulo);

		
		titulo = new CompEncabezado("Seguridad ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroSeguridad");
		encabezado.add(titulo);

		
		
		titulo = new CompEncabezado("Estatus ", 60);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getEstatus");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR)
			{
				
				NegocioCertificadoOrigen servicio = NegocioCertificadoOrigen.getInstance();
				CertificadoOrigen certificadoOrigen = getDatoSeleccionado();
			
				if (certificadoOrigen == null && accion != Accion.AGREGAR)
				{
					app.mostrarError("Seleccione un item");
				}
				else {
					if (Accion.EDITAR == accion)
					{
						if (certificadoOrigen.getStatus() == 1) // esta generada
						{
							app.mostrarError("No puede Modificar este certificado ");
						} 
						else // nueva
						{
							new ContCertificado(accion, certificadoOrigen,this, app);
						}
					}
					else // nueva o consultar
					{
						new ContCertificado(accion, certificadoOrigen, this,app);
					}
				}

			} else if (Accion.PROCESAR == accion) {
				NegocioCertificadoOrigen servicio = NegocioCertificadoOrigen
						.getInstance();
				CertificadoOrigen certificadoOrigen = getDatoSeleccionado();

				if (certificadoOrigen == null) {
					app.mostrarError("Seleccione un Imten");
				} else {
					if (!certificadoOrigen.getStatus().equals(0)) // si no es
																	// nuevo
					{
						app.mostrarError("Este Certificado de Origen ya ah sido Procesado ");
					} else {
						new ContCertificado(accion, certificadoOrigen, this,
								app);
					}
				}
			} 
			else if (Accion.IMPRIMIR_ITEM == accion)
			{
				CertificadoOrigen certificadoOrigen = getDatoSeleccionado();
				if (certificadoOrigen.getStatus().equals(1))
				{
					List<CertificadoOrigen> lista = new ArrayList<CertificadoOrigen>();
					lista.add(certificadoOrigen);
					HashMap parametros = new HashMap();
					File file;
					// 0 Pauny
					// 1 Don Roque
					if (certificadoOrigen.getPlantaOrigen().getTipo() == 0) 
					{
						file = new File(Index.class.getResource("/").getPath()+ "../../imagenes/LOGOPauny.jpg");
					} 
					else
					{
						file = new File(Index.class.getResource("/").getPath()+ "../../imagenes/LogoDonRoque.jpg");
					}
					File file2 = new File(Index.class.getResource("/").getPath() + "../../imagenes/gobiern.jpg");
					File file3 = new File(Index.class.getResource("/").getPath() + "../../iconos/32x32/mecanizado.png");
					File file4 = new File(Index.class.getResource("/").getPath() + "../../imagenes/fabricamini.jpg");
					File file5 = new File(Index.class.getResource("/").getPath() + "../../iconos/32x32/trabajador.png");
					parametros.put("logoTipo", file);
					parametros.put("logovenezuela", file2);
					parametros.put("logoMaquinaria", file3);
					parametros.put("logoPlanta", file4);
					parametros.put("logoComprador", file5);
					JRDataSource ds = new JRBeanCollectionDataSource(lista);
					app.agregarReporte();
					String url = (Index.class.getResource("/").getPath()+ "../../reportes/mantenimiento/Certificado/CertificadoOrigen.jasper").trim();
					JasperReport reporte = (JasperReport) JRLoader.loadObject(new File(url));
					byte[] resultado = JasperRunManager.runReportToPdf(reporte,parametros,ds);
					Filedownload.save(resultado,"application/pdf","certificadoEnsamblaje.pdf");
				} else {
					app.mostrarError("No se Puede Generar este Certificado ");
				}

			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un Banco");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(CertificadoOrigen dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
