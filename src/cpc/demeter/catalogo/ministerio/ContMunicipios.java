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
import cpc.demeter.controlador.ministerio.ContMunicipio;
import cpc.modelo.ministerio.dimension.UbicacionMunicipio;
import cpc.negocio.ministerio.basico.NegocioMunicipio;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContMunicipios extends ContCatalogo<UbicacionMunicipio> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContMunicipios(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioMunicipio servicios = NegocioMunicipio.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Municipios", servicios.getTodos(), app);

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Nombre", 300);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 300);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Pais", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrPais");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				NegocioMunicipio servicio = NegocioMunicipio.getInstance();
				UbicacionMunicipio tipo = getDatoSeleccionado();
				servicio.setModelo(tipo);
				if (tipo == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un tipo de riego");
				else
					new ContMunicipio(accion, servicio.getModelo(), this, app);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un tipo de riego");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(UbicacionMunicipio tipo) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
