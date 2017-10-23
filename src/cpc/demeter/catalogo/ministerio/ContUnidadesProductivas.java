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
import cpc.demeter.controlador.ministerio.ContUnidadProductiva;
import cpc.modelo.ministerio.gestion.UnidadProductiva;
import cpc.negocio.ministerio.basico.NegocioUnidadProductiva;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContUnidadesProductivas extends ContCatalogo<UnidadProductiva>
		implements EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContUnidadesProductivas(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioUnidadProductiva servicios = NegocioUnidadProductiva
				.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Unidades Productivas", servicios.getTodos(),
				app);

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("codigo", 110);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Sector Agricola", 110);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrSectorAgricola");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Uso Tierra", 110);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipoUsoTierra");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Productor", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrProductores");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Ubicacion", 400);
		titulo.setMetodoBinder("getStrUbicacion");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				NegocioUnidadProductiva servicio = NegocioUnidadProductiva
						.getInstance();
				UnidadProductiva tipo = getDatoSeleccionado();
				servicio.setUnidadProductiva(tipo);
				if (tipo == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un tipo de riego");
				else
					new ContUnidadProductiva(accion, servicio.getModelo(),
							this, app);
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
	public List cargarDato(UnidadProductiva tipo) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
