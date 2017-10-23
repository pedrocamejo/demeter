package cpc.demeter.catalogo.mantenimiento;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Filedownload;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.controlador.mantenimiento.ContLote;
import cpc.demeter.controlador.mantenimiento.ContOrdenGarantia;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.demeter.mantenimiento.Lote;
import cpc.modelo.demeter.mantenimiento.OrdenGarantia;
import cpc.modelo.demeter.mantenimiento.TipoGarantia;
import cpc.negocio.demeter.mantenimiento.NegocioGarantia;
import cpc.negocio.demeter.mantenimiento.NegocioMaquinariaExterna;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class CatlogoOrdenGarantia extends ContCatalogo<OrdenGarantia> {

	private static final long serialVersionUID = 1693946024302981254L;
	private AppDemeter app;
	private NegocioMaquinariaExterna negocio;

	public CatlogoOrdenGarantia(AccionFuncionalidad accionesValidas,AppDemeter app) throws ExcColumnasInvalidas, ExcArgumentoInvalido, ExcFiltroExcepcion {
		super();
		negocio = NegocioMaquinariaExterna.getInstance();
		this.app = app;
		dibujar(accionesValidas, "ORDENES DE MANTENIMIENTO DE GARANTIAS ",	negocio.ListarOrdenes(), app);
	}

	public CatlogoOrdenGarantia() {
		super();
	}

	public CatlogoOrdenGarantia(int modoOperacion) {
		super(modoOperacion);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Id", 20);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Maquinaria ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getSerialcarroceria");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Encargador ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getEncargado");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Detalle Garantia ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDetalleGarantiaSTR");
		encabezado.add(titulo);

		titulo = new CompEncabezado(" Estatus  ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getEstatusStr");
		encabezado.add(titulo);
		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {

			int accion = (Integer) event.getTarget().getAttribute("pos");
			OrdenGarantia dato = getDatoSeleccionado();

			if (dato == null && accion != Accion.AGREGAR)
			{
				app.mostrarError("Debe seleccionar un dato del catalago");
			} else 
			{
				if (accion == Accion.ANULAR && !(dato.getEstatus()==0 || dato.getEstatus()==1)) //las unicas que se anulan son las activas o nuevas 
				{
					app.mostrarError("No Puede Anular Esta Orden Estatus " + dato.getEstatusStr());
				} 
				else if (accion == Accion.PROCESAR && dato.getEstatus() != 1) // procesar o finalizar las nuevas 
				{
					app.mostrarError("No Puede Procesar Esta Orden Estatus "+ dato.getEstatusStr());
				}
				else if (accion == Accion.IMPRIMIR_ITEM)
				{

					try {
						IZkAplicacion a = (IZkAplicacion) TransactionSynchronizationManager.getResource("obj");
						boolean b = TransactionSynchronizationManager.hasResource("obj");
						boolean igual = app.equals(a);
						if (!igual) 
						{
							if (b) {
								TransactionSynchronizationManager.unbindResource("obj");
							}
							TransactionSynchronizationManager.bindResource(
									"obj", app);
						}

						List<OrdenGarantia> lista = new ArrayList<OrdenGarantia>();
						lista.add(dato);
						if (dato.getEstatus() == 0) {
							dato.setEstatus(1);
							negocio.guardar(dato);
						}

						try {
							File file = new File(Index.class.getResource("/").getPath() + "../../imagenes/cintillo_inst.png");
							Map mapa = new HashedMap();
							mapa.put("cintillo", file);
							mapa.put("SUBREPORT_DIR", Index.class.getResource("/").getPath()+ "../../reportes/mantenimiento/OrdenGarantia/");
							JRDataSource ds = new JRBeanCollectionDataSource(lista);
							String url = (Index.class.getResource("/").getPath()+ "../../reportes/mantenimiento/OrdenGarantia/OrdenTrabajoGarantia.jasper").trim();
							JasperReport reporte = (JasperReport) JRLoader.loadObject(new File(url));
							byte[] resultado = JasperRunManager.runReportToPdf(reporte,mapa,ds);
							Filedownload.save(resultado,"application/pdf","ordenGarantia.pdf");
						} catch (Exception e) {
							e.printStackTrace();
							app.mostrarError(e.getMessage());
						}

						
						

					} 
					catch (Exception e) {
						e.printStackTrace();
					}

				}
				else {
					new ContOrdenGarantia(accion, dato, this, app);
				}

			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun registro");
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(OrdenGarantia arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}
}
