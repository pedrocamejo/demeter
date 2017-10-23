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

import cpc.demeter.controlador.ContCuentaBancaria;
import cpc.modelo.sigesp.basico.CuentaBancaria;
import cpc.negocio.demeter.NegocioCuentaBancaria;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContCuentasBancarias extends ContCatalogo<CuentaBancaria>
		implements EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContCuentasBancarias(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioCuentaBancaria servicios = NegocioCuentaBancaria.getInstance();
		this.app = app;
		dibujar(accionesValidas, "CUENTAS BANCARIAS", servicios.getTodos(), app);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Tipo", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipoCuenta");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Banco", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrBanco");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nro Cuenta", 170);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroCuenta");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Decripcion", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cuenta Contable", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrCuentaContable");
		encabezado.add(titulo);
		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.CONSULTAR) {
				NegocioCuentaBancaria servicio = NegocioCuentaBancaria
						.getInstance();
				CuentaBancaria cuenta = getDatoSeleccionado();
				servicio.setCuentaBanco(cuenta);
				if (cuenta == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione una Cuenta");
				else
					new ContCuentaBancaria(accion, servicio.getCuentaBanco(),
							this, app);
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
	public List cargarDato(CuentaBancaria dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
