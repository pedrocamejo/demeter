package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.modelo.demeter.administrativo.PlanFinanciamiento;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContFinanciamientos extends ContCatalogo<PlanFinanciamiento>
		implements EventListener {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContFinanciamientos(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		this.app = app;
		/*
		 * Nego
		 * 
		 * List<PlanFinanciamiento> financiamientos = manejador.getAll();
		 */
		// dibujar(accionesValidas, "Planes Financiamiento", financiamientos,
		// AppDemeter.getInstancia().getAmbiente());
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Descripcion", 150);
		titulo.setOrdenable(true);
		// titulo.setAlineacion(CompEncabezado.CENTRO);
		titulo.setMetodoBinder("getDescripcion");
		// titulo.setMetodoModelo("usuario.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("% Descuento", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getPorcentajeDescuento");
		// titulo.setMetodoModelo("usuario.identidad.cedula");
		encabezado.add(titulo);

		titulo = new CompEncabezado("% Inicial", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getPorcentajeInicial");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.CONSULTAR) {
				// new ContUsuario2(accion, getDatoSeleccionado(), this);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		} catch (Exception e) {
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(PlanFinanciamiento dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
