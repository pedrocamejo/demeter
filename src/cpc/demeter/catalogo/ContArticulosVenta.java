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

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.ContArticuloVenta;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.negocio.demeter.basico.NegocioArticuloVenta;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContArticulosVenta extends ContCatalogo<ArticuloVenta> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContArticulosVenta(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioArticuloVenta negocio = NegocioArticuloVenta.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Articulos Venta", negocio.getTodos(), app);
	}

	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Codigo SIGESP", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCodigoSIGESP");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Codigo Fabricante", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCodigoFabricante");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Descripcion", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Denominacion Fabricante", 230);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDenominacionFabricante");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipoArticulo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Unidad de Medida", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("ESTADO", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrInactivo");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR) {
				ArticuloVenta labor = getDatoSeleccionado();
				NegocioArticuloVenta negocio = NegocioArticuloVenta
						.getInstance();
				if (labor != null) {
					negocio.setArticulo(labor);
				} else
					negocio.nuevo();
				if (labor == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un producto");
				else
					new ContArticuloVenta(accion, negocio.getModelo(), this,
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

}
