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
import cpc.demeter.controlador.gestion.ContAlmacen;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.negocio.sigesp.NegocioAlmacen;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContAlmacenes extends ContCatalogo<Almacen> implements
		EventListener, Serializable {

	private static final long serialVersionUID = 3924078618473593264L;
	private AppDemeter app;

	public ContAlmacenes(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioAlmacen negocio = NegocioAlmacen.getInstance();
		this.app = app;
		dibujar(accionesValidas, "ALMACENES", negocio.getAlmacenes(), app);
	}

	public List<CompEncabezado> cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Nombre", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Descripción", 300);
		titulo.setOrdenable(false);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo Almacen", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreTipoAlmacen");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Responsable", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getResponsable");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				NegocioAlmacen servicio = NegocioAlmacen.getInstance();
				Almacen almacen = getDatoSeleccionado();
				servicio.setAlmacen(almacen);
				if (almacen == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Almacen del Catalogo");
				else
					new ContAlmacen(accion, almacen, this, app);
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un Almacen del Catalogo");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	public List cargarDato(Almacen arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

}
