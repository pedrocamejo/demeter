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
import cpc.demeter.Index;
import cpc.demeter.controlador.inventario.ContEntradaArticulo;
import cpc.modelo.demeter.mantenimiento.EntradaArticulo;
import cpc.negocio.demeter.mantenimiento.NegocioEntradaArticulo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContEntradasArticulos extends ContCatalogo<EntradaArticulo>
		implements EventListener, Serializable {

	


	/**
	 * 
	 */
	private static final long serialVersionUID = 7413159359979260189L;
	private AppDemeter app;

	public ContEntradasArticulos(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioEntradaArticulo servicios = NegocioEntradaArticulo.getInstance();
		this.app = app;
		List<EntradaArticulo> z = servicios.getTodos();
		dibujar(accionesValidas, "Entradas De Articulos", servicios.getTodos(),
				app);

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("usuario", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getUsuario");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Responsable", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getResponsable");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("codigo", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrNroDocumento");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {

				NegocioEntradaArticulo servicio = NegocioEntradaArticulo
						.getInstance();
				EntradaArticulo entradaArticulo = getDatoSeleccionado();
				servicio.setEntradaArticulo(entradaArticulo);
				if (entradaArticulo == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Banco");
				else
					new ContEntradaArticulo(accion,
							servicio.getEntradaArticulo(),
							servicio.getAlmacenesOrigen(),
							servicio.getAlmacenesDestino(), this, app);

			}

			if (accion == Accion.IMPRIMIR_ITEM) {
				EntradaArticulo entrada = getDatoSeleccionado();
				if (entrada == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione una Entrada del Catalogo");
				else
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

	@SuppressWarnings("unchecked")
	public List cargarDato(EntradaArticulo dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public void imprimir() {
		HashMap parametros = new HashMap();

		NegocioEntradaArticulo negocio = NegocioEntradaArticulo.getInstance();
		negocio.setEntradaArticulo(getDatoSeleccionado());
		EntradaArticulo recepcion = negocio.getEntradaArticulo();
		parametros.put("rutaImagenEncabezado", Index.class.getResource("/")
				.getPath() + "../../imagenes/cintillo_inst.png");
		JRDataSource ds = new JRBeanCollectionDataSource(
				recepcion.getDetalleEntradaArticulos());
		app.agregarReporte();
		Jasperreport reporte = app.getReporte();

		reporte.setSrc("reportes/EntradasArticulos.jasper");
		reporte.setParameters(parametros);
		reporte.setDatasource(ds);
		reporte.setType(getTipoReporte());
	}

}