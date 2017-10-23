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

import cpc.demeter.controlador.ContCuentaContable;
import cpc.modelo.sigesp.basico.CuentaContable;
import cpc.negocio.demeter.NegocioCuentaContable;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContCuentasContables extends ContCatalogo<CuentaContable>
		implements EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContCuentasContables(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioCuentaContable servicios = NegocioCuentaContable.getInstance();
		this.app = app;
		dibujar(accionesValidas, "CUENTAS CONTABLES", servicios.getTodos(), app);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Codigo", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre", 450);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDenominacion");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.CONSULTAR) {
				NegocioCuentaContable servicio = NegocioCuentaContable
						.getInstance();
				CuentaContable cuenta = getDatoSeleccionado();
				servicio.setCuentaContable(cuenta);
				if (cuenta == null && accion != Accion.AGREGAR)
					app.mostrarError("Selececcione una Cuenta");
				else
					new ContCuentaContable(accion,
							servicio.getCuentaContable(), this, app);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Selececcione una Cuenta");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(CuentaContable dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
