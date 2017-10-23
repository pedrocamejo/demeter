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
import cpc.demeter.controlador.ministerio.ContSector;
import cpc.modelo.ministerio.dimension.UbicacionSector;
import cpc.negocio.ministerio.basico.NegocioSector;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContSectores extends ContCatalogo<UbicacionSector> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContSectores(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioSector servicios = NegocioSector.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Sectores", servicios.getTodos(), app);

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("N°codigo", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Parroquia", 200);
		titulo.setMetodoBinder("getStrParroquia");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("municipio", 150);
		titulo.setMetodoBinder("getStrMunicipio");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("estado", 150);
		titulo.setMetodoBinder("getStrEstado");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("pais", 150);
		titulo.setMetodoBinder("getStrPais");
		titulo.setOrdenable(true);
		encabezado.add(titulo);
		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				NegocioSector servicio = NegocioSector.getInstance();
				UbicacionSector tipo = getDatoSeleccionado();
				servicio.setModelo(tipo);
				if (tipo == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un tipo de riego");
				else
					new ContSector(accion, servicio.getModelo(), this, app);
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
	public List cargarDato(UbicacionSector tipo) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
