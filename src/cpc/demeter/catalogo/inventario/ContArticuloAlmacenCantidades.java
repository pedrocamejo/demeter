package cpc.demeter.catalogo.inventario;

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
import cpc.demeter.controlador.mantenimiento.ContConsumible;
import cpc.modelo.demeter.basico.Labor;
import cpc.modelo.demeter.mantenimiento.ArticuloAlmacenCantidad;
import cpc.modelo.demeter.mantenimiento.Consumible;
import cpc.negocio.demeter.basico.NegocioLabor;
import cpc.negocio.demeter.mantenimiento.NegocioArticuloAlmacenCantidad;
import cpc.negocio.demeter.mantenimiento.NegocioConsumible;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContArticuloAlmacenCantidades extends
		ContCatalogo<ArticuloAlmacenCantidad> implements EventListener,
		Serializable {

	;
	/**
	 * 
	 */
	private static final long serialVersionUID = 7860235157000693625L;
	private AppDemeter app;

	public ContArticuloAlmacenCantidades(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioArticuloAlmacenCantidad servicios = NegocioArticuloAlmacenCantidad
				.getInstance();
		this.app = app;
		dibujar(accionesValidas, "ArticulosEnAlmacenes", servicios.getTodos(),
				app);
		setIdposicionReporte(3);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Den. Articulo", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrArticuloVentaDen");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cod. Articulo", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrArticuloVentaCodFabricante");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Almacen", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrAlmacen");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cantidad", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCantidad");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
			}
			if (accion == Accion.IMPRIMIR_TODO) {
				imprimir();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un Banco");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	private void imprimir() throws ExcFiltroExcepcion {
		HashMap parametros = new HashMap();
		NegocioArticuloAlmacenCantidad servicios = NegocioArticuloAlmacenCantidad
				.getInstance();
		List<ArticuloAlmacenCantidad> lista = servicios.getTodos();
	
		JRDataSource ds = new JRBeanCollectionDataSource(lista);
		app.agregarReporte();
		Jasperreport reporte = app.getReporte();
		reporte.setSrc("reportes/ArticulosAlmacenCantidadLista.jasper");
		//parametros.put("logo", AppDemeter.class.getResource("/").getPath()+ "cpc/demeter/imagenes/cintillo_inst.png");
		parametros.put("usuario", app.getUsuario().getNombreIdentidad() + " "
				+ app.getUsuario().getApellido() + " ["
				+ app.getUsuario().getNombre() + "]");
		reporte.setParameters(parametros);
		reporte.setDatasource(ds);
		reporte.setType(getTipoReporte());
		
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(ArticuloAlmacenCantidad dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
