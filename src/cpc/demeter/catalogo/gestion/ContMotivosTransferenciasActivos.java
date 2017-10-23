package cpc.demeter.catalogo.gestion;

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
import cpc.demeter.controlador.gestion.ContMotivoTransferenciaActivo;
import cpc.modelo.demeter.gestion.MotivoTransferenciaActivo;
import cpc.negocio.demeter.gestion.NegocioMotivoTransferenciaActivo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContMotivosTransferenciasActivos extends
		ContCatalogo<MotivoTransferenciaActivo> implements EventListener,
		Serializable {

	private static final long serialVersionUID = 2139742154136431708L;
	private AppDemeter app;

	public ContMotivosTransferenciasActivos(
			AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioMotivoTransferenciaActivo negocio = NegocioMotivoTransferenciaActivo
				.getInstance();
		this.app = app;
		dibujar(accionesValidas, "MOTIVOS DE TRANSFERENCIAS DE ACTIVOS",
				negocio.obetenerTodos(), app);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(MotivoTransferenciaActivo arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

	@Override
	public List<CompEncabezado> cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Descripción", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				MotivoTransferenciaActivo entrada = getDatoSeleccionado();
				if (entrada == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Motivo del Catalogo");
				else
					new ContMotivoTransferenciaActivo(accion, entrada, this,
							app);
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un Motivo del Catalogo");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

}
