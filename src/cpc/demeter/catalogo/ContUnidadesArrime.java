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
import cpc.demeter.controlador.ContUnidadArrime;
import cpc.modelo.demeter.basico.UnidadArrime;
import cpc.negocio.demeter.basico.NegocioUnidadArrime;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContUnidadesArrime extends ContCatalogo<UnidadArrime> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContUnidadesArrime(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioUnidadArrime negocio = NegocioUnidadArrime.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Unidades de Arrime", negocio.getTodos(), app);

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Clase", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo de Silo", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipoSilo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("ubicacion", 350);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrUbicacionFisica");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				UnidadArrime servicio = getDatoSeleccionado();
				if (servicio == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Silo");
				else {
					NegocioUnidadArrime negocio = NegocioUnidadArrime
							.getInstance();
					negocio.setModelo(servicio);
					new ContUnidadArrime(accion, negocio.getModelo(), this, app);
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un Silo");
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
	@Override
	public List cargarDato(UnidadArrime arg0) {
		return null;
	}

}
