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
import cpc.demeter.controlador.ministerio.ContOrganizacion;
import cpc.modelo.ministerio.gestion.Organizacion;
import cpc.negocio.ministerio.basico.NegocioOrganizacion;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContOrganizaciones extends ContCatalogo<Organizacion> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContOrganizaciones(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioOrganizacion servicios = NegocioOrganizacion.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Organizaciones", servicios.getTodos(), app);

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("nombre", 350);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDenotacion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo Organizacion", 350);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipoOrganizacion");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				NegocioOrganizacion servicio = NegocioOrganizacion
						.getInstance();
				Organizacion tipo = getDatoSeleccionado();
				servicio.setModelo(tipo);
				if (tipo == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un tipo de riego");
				else
					new ContOrganizacion(accion, servicio.getModelo(), this,
							app);
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
	public List cargarDato(Organizacion tipo) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
