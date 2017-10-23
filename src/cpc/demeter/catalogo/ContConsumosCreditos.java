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
import cpc.demeter.controlador.administrativo.ContConsumoCredito;
import cpc.modelo.demeter.administrativo.ConsumoCredito;
import cpc.negocio.demeter.administrativo.NegocioConsumoCredito;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContConsumosCreditos extends ContCatalogo<ConsumoCredito>
		implements EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContConsumosCreditos(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioConsumoCredito servicios = NegocioConsumoCredito.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Consumos Notas Credito",
				servicios.getTodos(), app);
		desactivarTipoReporte();
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Nota Credito", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrNota");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipoConsumo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrfecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cliente", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrPagador");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto", 100);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getStrMonto");
		
		titulo = new CompEncabezado("Estado", 100);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getEstado");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				NegocioConsumoCredito servicio = NegocioConsumoCredito.getInstance();
				ConsumoCredito consumo = getDatoSeleccionado();
				servicio.setConsumoCredito(consumo);
				
				if (consumo == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Consumo");
				else
					new ContConsumoCredito(accion,servicio.getConsumoCredito(), this, app);
			}
			if (accion == Accion.ANULAR) {
				NegocioConsumoCredito servicio = NegocioConsumoCredito
						.getInstance();
				ConsumoCredito consumo = getDatoSeleccionado();
				servicio.setConsumoCredito(consumo);
				if (consumo == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Consumo");
				else
					new ContConsumoCredito(accion,
							servicio.getConsumoCredito(), this, app);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un Consumo");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(ConsumoCredito dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
