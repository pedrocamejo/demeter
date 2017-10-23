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
import cpc.demeter.controlador.ContConfigurarTipoServicio;
import cpc.modelo.demeter.administrativo.CuentasTipoServicio;
import cpc.modelo.sigesp.basico.Sede;
import cpc.negocio.demeter.NegocioCuentasTipoServicio;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContConfigurarTiposServicios extends
		ContCatalogo<CuentasTipoServicio> implements EventListener,
		Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;
	private NegocioCuentasTipoServicio negocio;

	public ContConfigurarTiposServicios(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		this.negocio = NegocioCuentasTipoServicio.getInstance();
		this.app = app;
		dibujar(accionesValidas, "TIPOS DE SERVICIOS - CUENTAS",
				negocio.getTodos(new Sede(app.getSede().getEmpresa()
						.getCodigo(), app.getSede().getIdSede(), "", null,
						false)), app);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Tipo Servicio", 550);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getTipoServicio");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cuenta Contable de Ingreso", 550);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCuentaContableIngresosSede");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cuenta Prepuestaria", 550);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCuentaPresupuestariaIngresosSede");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				CuentasTipoServicio objeto = getDatoSeleccionado();
				this.negocio.setCuentasTipoServicio(objeto);
				if (objeto == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Tipo de Servicio");
				else
					new ContConfigurarTipoServicio(accion, objeto, this, app);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un Tipo de Servicio");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(CuentasTipoServicio arg0) {
		return null;
	}

}
