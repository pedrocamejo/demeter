package cpc.demeter.catalogo.gestion;

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
import cpc.demeter.controlador.gestion.ContTransferenciaActivo;
import cpc.modelo.demeter.gestion.Movimiento;
import cpc.negocio.demeter.gestion.NegocioTransferenciaActivo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContTraferenciasActivos extends ContCatalogo<Movimiento> implements
		EventListener, Serializable {

	private static final long serialVersionUID = 2139742154136431708L;
	private AppDemeter app;

	public ContTraferenciasActivos(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioTransferenciaActivo negocio = NegocioTransferenciaActivo
				.getInstance();
		this.app = app;
		dibujar(accionesValidas, "TRANSFERENCIAS DE ACTIVOS",
				negocio.getTransferencias(), app);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(Movimiento arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

	@Override
	public List<CompEncabezado> cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Número", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getControl");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 80);
		titulo.setOrdenable(false);
		titulo.setMetodoBinder("getfechastring");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Registrado Por", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getUsuario");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				Movimiento movimiento = getDatoSeleccionado();
				if (movimiento == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione una Transferencia del Catalogo");
				else
					new ContTransferenciaActivo(accion, movimiento, this, app);
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione una Transferencia del Catalogo");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

}
