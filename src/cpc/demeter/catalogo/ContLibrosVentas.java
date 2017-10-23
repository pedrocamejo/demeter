package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.controlador.administrativo.ContLibroVenta;
import cpc.modelo.demeter.administrativo.LibroVenta;
import cpc.modelo.demeter.administrativo.LibroVentaDetalle;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.negocio.demeter.administrativo.NegocioLibroVenta;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.ventanas.CompVentana;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContLibrosVentas extends ContCatalogo<LibroVenta> implements
		EventListener {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContLibrosVentas(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioLibroVenta servicios = NegocioLibroVenta.getInstance();
		this.app = app;
		dibujar(accionesValidas, "LIBROS DE VENTA", servicios.getTodos(),
				this.app);
		setIdposicionReporte(3);
		desactivarTipoReporte();

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Mes", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrMes");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Año", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getAno");

		encabezado.add(titulo);

		titulo = new CompEncabezado("Documentos", 120);
		titulo.setOrdenable(true);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getStrCantidadDocumentos");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto base", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrMontoBase");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto Total", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrMontoTotal");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.CONSULTAR)
			{
				LibroVenta cierre = getDatoSeleccionado();
				new ContLibroVenta(accion, cierre, this, this.app);
			}
			else
			{
				imprimirLibro(getDatoSeleccionado());
			}
			
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void imprimirLibro(final LibroVenta libro) throws ExcAccesoInvalido, SuspendNotAllowedException, InterruptedException {
		Window w = new Window("Tipo","none",true);
		w.setWidth("400px");
		w.setMode("modal");
		this.app.agregar(w);
		
		final Combobox combo = new Combobox();
		combo.appendItem("excel");
		combo.appendItem("zip");
		combo.setSelectedIndex(0);
		Button procesar = new Button("procesar");
		procesar.addEventListener(Events.ON_CLICK,new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub

				if(combo.getSelectedItem().getLabel().equals("excel")){
					HashMap parametros = new HashMap();
					NegocioLibroVenta negocio = NegocioLibroVenta.getInstance();
					List<LibroVentaDetalle> detalle = negocio
							.getDetalleLibroVenta(libro);
					for (LibroVentaDetalle libroVentaDetalle : detalle) {
						System.out.println("Documento: "
								+ libroVentaDetalle.getDocumento().getStrNroDocumento()
								+ "recibos: " + libroVentaDetalle.getNumerosREcibos()
								+ "FORMAS PAGO:  ");
					}
					JRDataSource ds = new JRBeanCollectionDataSource(detalle);
					parametros.put("FechaAño", libro.getAno());
					parametros.put("FechaMes", libro.getMes());
					parametros.put("exportParameter", app.getXLSParameters(false));
					app.agregarReporte();
					Jasperreport reporte = app.getReporte();
					reporte.setSrc("reportes/rpt_librodeventas.jasper");
					reporte.setParameters(parametros);
					reporte.setDatasource(ds);
					reporte.setType(getTipoReporte());
				}else{
					ByteArrayOutputStream zip = NegocioLibroVenta.getInstance().XmlSigespLibroByte(libro.getId());
					Filedownload.save(zip.toByteArray(),"application/zip","libroventa.zip");
				}
			}
		});
		Hbox hbox = new Hbox();
		hbox.setAlign("center");
		hbox.setStyle("margin: 4px");
		hbox.appendChild(combo);
		hbox.appendChild(procesar);
		w.appendChild(hbox);
		w.doModal();

	}

	@SuppressWarnings("unchecked")
	public List cargarDato(LibroVenta dato) {
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

}
