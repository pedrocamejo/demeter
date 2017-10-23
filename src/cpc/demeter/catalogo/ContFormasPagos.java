package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;

import cpc.demeter.controlador.administrativo.ContFormaPago;
import cpc.modelo.demeter.administrativo.FormaPago;
import cpc.negocio.demeter.administrativo.NegocioFormaPago;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContFormasPagos extends ContCatalogo<FormaPago> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContFormasPagos(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		this.app = app;
		NegocioFormaPago servicios = NegocioFormaPago.getInstance();
		dibujar(accionesValidas, "CORRECCION FORMAS DE PAGO",
				servicios.getAllPagos(), app);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Forma", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipoFormaPago");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Recibo", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrRecibo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Factura", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFactura");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Pagador", 140);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrProductor");
		encabezado.add(titulo);

		titulo = new CompEncabezado("banco", 140);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrBanco");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cuenta", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCuenta");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Documento", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto", 90);
		// titulo.setOrdenable(true);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getStrMonto");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.CONSULTAR) {
				NegocioFormaPago servicio = NegocioFormaPago.getInstance();
				FormaPago pago = getDatoSeleccionado();
				servicio.setPago(pago);
			//	new ContFormaPago(accion, servicio.getModelo(), this, this.app);
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
	public List cargarDato(FormaPago dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
