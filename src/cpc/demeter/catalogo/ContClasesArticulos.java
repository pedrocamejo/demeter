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
import cpc.demeter.controlador.ContClaseArticulo;
import cpc.modelo.demeter.basico.ClaseArticulo;
import cpc.negocio.demeter.basico.NegocioClaseArticulo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContClasesArticulos extends ContCatalogo<ClaseArticulo> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContClasesArticulos(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioClaseArticulo negocioFacturas = NegocioClaseArticulo
				.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Clases de Articulos",
				negocioFacturas.getTodos(), app);

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Tipo de Articulo", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipoArticulo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("descripcion", 350);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				ClaseArticulo servicio = getDatoSeleccionado();

				if (servicio == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Servicio");
				else {
					NegocioClaseArticulo negocio = NegocioClaseArticulo
							.getInstance();
					negocio.setModelo(servicio);
					new ContClaseArticulo(accion, negocio.getModelo(), this,
							app);
				}
			}
			if (accion == Accion.IMPRIMIR_TODO) {
				// imprimirListadoServicios();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un Servicio");
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
	public List cargarDato(ClaseArticulo arg0) {
		return null;
	}

}
