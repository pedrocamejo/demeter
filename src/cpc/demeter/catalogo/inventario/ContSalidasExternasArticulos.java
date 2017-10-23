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
import cpc.demeter.controlador.inventario.ContSalidaExternaArticulo;
import cpc.demeter.controlador.inventario.ContSalidaInternaArticulo;
import cpc.demeter.controlador.mantenimiento.ContConsumible;

import cpc.modelo.demeter.mantenimiento.ArticuloAlmacenCantidad;
import cpc.modelo.demeter.mantenimiento.Consumible;
import cpc.modelo.demeter.mantenimiento.EntradaArticulo;
import cpc.modelo.demeter.mantenimiento.SalidaExternaArticulo;
import cpc.modelo.demeter.mantenimiento.SalidaInternaArticulo;

import cpc.negocio.demeter.mantenimiento.NegocioArticuloAlmacenCantidad;
import cpc.negocio.demeter.mantenimiento.NegocioConsumible;
import cpc.negocio.demeter.mantenimiento.NegocioEntradaArticulo;
import cpc.negocio.demeter.mantenimiento.NegocioSalidaExternaArticulo;
import cpc.negocio.demeter.mantenimiento.NegocioSalidaInternaArticulo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContSalidasExternasArticulos extends
		ContCatalogo<SalidaExternaArticulo> implements EventListener,
		Serializable {

	;
	/**
	 * 
	 */
	private static final long serialVersionUID = -8249381232624236758L;
	/**
	 * 
	 */

	private AppDemeter app;

	public ContSalidasExternasArticulos(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioSalidaExternaArticulo servicios = NegocioSalidaExternaArticulo
				.getInstance();
		this.app = app;
		List<SalidaExternaArticulo> z = servicios.getTodos();
		dibujar(accionesValidas, "Salidas De Articulos", servicios.getTodos(),
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

		titulo = new CompEncabezado("Destinatario", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDestinatario");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("codigo", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrNroDocumento");
		encabezado.add(titulo);
		/*
		 * titulo = new CompEncabezado("activo",250); titulo.setOrdenable(true);
		 * titulo.setMetodoBinder("getActivo"); encabezado.add(titulo);
		 */

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {

				NegocioSalidaExternaArticulo servicio = NegocioSalidaExternaArticulo
						.getInstance();
				SalidaExternaArticulo salidaExternaArticulo = getDatoSeleccionado();
				servicio.setSalidaExternaArticulo(salidaExternaArticulo);
				if (salidaExternaArticulo == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Banco");
				else
					new ContSalidaExternaArticulo(accion,
							servicio.getSalidaExternaArticulo(),
							servicio.getAlmacenesSalidaExterna(),
							servicio.getAlmacenesDestino(),
							servicio.getDestinatarios(), this, app);

			}

			if (accion == Accion.IMPRIMIR_ITEM) {
				SalidaExternaArticulo salida = getDatoSeleccionado();
				if (salida == null && accion != Accion.AGREGAR)
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
	public List cargarDato(SalidaExternaArticulo salidaExternaArticulo) {
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

		NegocioSalidaExternaArticulo negocio = NegocioSalidaExternaArticulo
				.getInstance();
		negocio.setSalidaExternaArticulo(getDatoSeleccionado());
		SalidaExternaArticulo salida = negocio.getSalidaExternaArticulo();
		parametros.put("rutaImagenEncabezado", Index.class.getResource("/")
				.getPath() + "../../imagenes/cintillo_inst.png");
		JRDataSource ds = new JRBeanCollectionDataSource(
				salida.getDetalleSalidaExternaArticulos());
		app.agregarReporte();
		Jasperreport reporte = app.getReporte();

		reporte.setSrc("reportes/SalidasArticulos.jasper");
		reporte.setParameters(parametros);
		reporte.setDatasource(ds);
		reporte.setType(getTipoReporte());
	}

}