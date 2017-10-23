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
import cpc.demeter.controlador.ContClaseUnidadArrime;

import cpc.modelo.demeter.basico.ClaseUnidadArrime;
import cpc.modelo.demeter.basico.UnidadMedida;
import cpc.negocio.demeter.basico.NegocioClaseUnidadArrime;

import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContClasesUnidadArrime extends ContCatalogo<ClaseUnidadArrime>
		implements EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContClasesUnidadArrime(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioClaseUnidadArrime servicios = NegocioClaseUnidadArrime
				.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Clases Unidad de Arrime",
				servicios.getTodos(), app);

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Tipo", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getTipo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Descripcion", 350);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				NegocioClaseUnidadArrime servicio = NegocioClaseUnidadArrime
						.getInstance();
				ClaseUnidadArrime claseUnidadArrime = getDatoSeleccionado();
				servicio.setModelo(claseUnidadArrime);
				if (claseUnidadArrime == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione una clase de Arrime");
				else
					new ContClaseUnidadArrime(accion, servicio.getModelo(),
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
	public List cargarDato(UnidadMedida tipo) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(ClaseUnidadArrime arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
