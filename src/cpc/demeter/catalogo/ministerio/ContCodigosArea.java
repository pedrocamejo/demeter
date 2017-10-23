package cpc.demeter.catalogo.ministerio;

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
import cpc.demeter.controlador.ministerio.ContCodigoArea;
import cpc.modelo.demeter.basico.CodigoArea;
import cpc.negocio.demeter.NegocioCodigoArea;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContCodigosArea extends ContCatalogo<CodigoArea> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContCodigosArea(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioCodigoArea servicios = NegocioCodigoArea.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Codigos Telefonicos de Area",
				servicios.getTodos(), app);

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Codigo", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCodigoArea");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Descripcion", 220);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipo");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				NegocioCodigoArea servicio = NegocioCodigoArea.getInstance();
				CodigoArea tipo = getDatoSeleccionado();
				servicio.setModelo(tipo);
				if (tipo == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Codigo de Area");
				else
					new ContCodigoArea(accion, servicio.getModelo(), this, app);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un Codigo de Area");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(CodigoArea tipo) {
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

}
