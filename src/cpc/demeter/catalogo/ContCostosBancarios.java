package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;




import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Script;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.ContArticuloVenta;
import cpc.demeter.controlador.ContCostoBancario;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.basico.CostoBancario;
import cpc.modelo.demeter.basico.Labor;
import cpc.negocio.demeter.basico.NegocioArticuloVenta;
import cpc.negocio.demeter.basico.NegocioCostoBancario;
import cpc.negocio.demeter.basico.NegocioLabor;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContCostosBancarios extends ContCatalogo<CostoBancario> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContCostosBancarios(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioCostoBancario negocio = NegocioCostoBancario.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Articulos Venta", negocio.getTodos(), app);
	}

	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("id", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Descripcion", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Impuesto", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getIvaProducto");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Precio", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getPrecio");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR) {
				CostoBancario labor = getDatoSeleccionado();
				NegocioCostoBancario negocio = NegocioCostoBancario
						.getInstance();
				if (labor != null) {
					negocio.setModelo(labor);
				} else
					negocio.nuevo();
				if (labor == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un producto");
				else
					new ContCostoBancario(accion, negocio.getModelo(), this,
							app);
			}
			if (accion == Accion.IMPRIMIR_TODO) {
				imprimirListadoServicios();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un Servicio");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

	private void imprimirListadoServicios() throws ExcFiltroExcepcion {
		HashMap parametros = new HashMap();
		NegocioArticuloVenta negocio = NegocioArticuloVenta.getInstance();
		List<ArticuloVenta> lista = negocio.getTodos();
		JRDataSource ds = new JRBeanCollectionDataSource(lista);
		app.agregarReporte();
		Jasperreport reporte = app.getReporte();
		reporte.setSrc("reportes/Articulos.jasper");
		parametros.put("logo", AppDemeter.class.getResource("/").getPath()
				+ "cpc/demeter/imagenes/cintillo_inst.png");
		parametros.put("usuario", app.getUsuario().getNombreIdentidad() + " "
				+ app.getUsuario().getApellido() + " ["
				+ app.getUsuario().getNombre() + "]");
		reporte.setParameters(parametros);
		reporte.setDatasource(ds);
		reporte.setType(getTipoReporte());
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(ArticuloVenta arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List cargarDato(CostoBancario dato) {
		// TODO Auto-generated method stub
		return null;
	}

}