package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.controlador.administrativo.ContNotaCargo;
import cpc.demeter.controlador.administrativo.ContNotaCargoProcesar;
import cpc.modelo.demeter.administrativo.NotaCargo;
import cpc.negocio.demeter.administrativo.NegocioNotaCargo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.ventanas.CompVentanaEntrada;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContNotasCargo extends ContCatalogo<NotaCargo> implements
		EventListener {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;
	private CompVentanaEntrada vistaNroControl = new CompVentanaEntrada(this);

	public ContNotasCargo(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioNotaCargo servicios = NegocioNotaCargo.getInstance();
		this.app = app;
		//dibujar(accionesValidas, "FACTURAS", servicios.getTodos(), app);
		dibujar(accionesValidas, "Notas De Cargos Extras", servicios.getTodos(), app);
		desactivarTipoReporte();
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Nº Control", 60);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroNotaCargo");
		encabezado.add(titulo);

		
		titulo = new CompEncabezado("Fecha", 60);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cliente", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreCliente");
		encabezado.add(titulo);

		titulo = new CompEncabezado("CedRif", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCedulaRifCliente");
		encabezado.add(titulo);

		

		titulo = new CompEncabezado("Monto Total", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrMonto");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Saldo", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrSaldo");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrAnulada");
		encabezado.add(titulo);
		
		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		System.out.println(event.getName());
		Component a = event.getTarget();
		System.out.println(event.getTarget());
		
		try
		{
			this.app.limpiarReporteExterno();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		try {
			
				int accion = (Integer) event.getTarget().getAttribute("pos");
				
				if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR) 
				{
					//	DocumentoFiscal factura = getDatoSeleccionado();
					NegocioNotaCargo servicios = NegocioNotaCargo.getInstance();
					NotaCargo notaCargo = getDatoSeleccionado();
					new ContNotaCargo(accion,notaCargo,this,app);
				} 
			
				else if (accion == Accion.IMPRIMIR_ITEM) {
					// DocumentoFiscal factura = getDatoSeleccionado();
					//mostrarVistaNroControl(app);
					Imprimir(accion);
				}
			 
				else if(accion == Accion.PROCESAR)
				{
					NegocioNotaCargo servicios = NegocioNotaCargo.getInstance();
					NotaCargo notaCargo =getDatoSeleccionado();
					if(!notaCargo.isCancelada() && !notaCargo.isAnulada())
					{
						new ContNotaCargoProcesar(accion, notaCargo, this, app);
					}
					else
					{
						Messagebox.show(" No puede Procesar esta Factura  ");
					}
				}
			
		} catch (NullPointerException e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	}

	public List cargarDato(NotaCargo notaCargo) {
		return null;
	}

	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

	public void Imprimir(int accion) throws JRException {
		NotaCargo nota = getDatoSeleccionado();
		HashMap parametros = new HashMap();
		parametros.put("logo",Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");
		List<NotaCargo> notas = new ArrayList<NotaCargo>();
		notas.add(nota);
		JRDataSource ds = new JRBeanCollectionDataSource(notas);
		String url = (Index.class.getResource("/").getPath()+ "../../reportes/NotaDeCargos.jasper").trim();
		JasperReport reporte = (JasperReport) JRLoader.loadObject(new File(url));
		byte[] resultado = JasperRunManager.runReportToPdf(reporte,parametros,new JRBeanCollectionDataSource(notas));
		Filedownload.save(resultado,"application/pdf","NotaDeCargos_"+nota.getStrNroNota()+".pdf");
	}

	

	
}