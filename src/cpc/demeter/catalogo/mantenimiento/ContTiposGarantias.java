package cpc.demeter.catalogo.mantenimiento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.mantenimiento.ContTipoGarantia;
import cpc.modelo.demeter.mantenimiento.TipoGarantia;
import cpc.negocio.demeter.mantenimiento.NegocioTipoGarantia;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContTiposGarantias extends ContCatalogo<TipoGarantia> implements
		EventListener, Serializable {

	private static final long serialVersionUID = 1L;

	private AppDemeter app;

	public ContTiposGarantias(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {

		NegocioTipoGarantia servicios = NegocioTipoGarantia.getInstance();
		this.app = app;
		dibujar(accionesValidas, "TIPO GARANTIA", servicios.getTodos(), app);
	}

	@Override
	public List<CompEncabezado> cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("ID", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Descripcion", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Modelo", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getModeloSTR");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Marca", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMarcaSTR");
		encabezado.add(titulo);

		return encabezado;
	}

	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub
		try {
			int accion = (Integer) arg0.getTarget().getAttribute("pos");

			if (accion == Accion.AGREGAR) {
				new ContTipoGarantia(accion, null, this, app);
			}
			if (accion == Accion.CONSULTAR) {
				TipoGarantia tipoGarantia = getDatoSeleccionado();
				if (tipoGarantia != null) {
					new ContTipoGarantia(accion, tipoGarantia, this, app);
				} else {
					app.mostrarAdvertencia("Debe Seleccionar un Items ");
				}
			}
			if (accion == Accion.EDITAR) {
				TipoGarantia tipoGarantia = getDatoSeleccionado();
				if (tipoGarantia != null) {
					new ContTipoGarantia(accion, tipoGarantia, this, app);
				} else {
					app.mostrarAdvertencia("Debe Seleccionar un Items ");
				}
			}
			if (accion == Accion.ELIMINAR) {
				TipoGarantia tipoGarantia = getDatoSeleccionado();
				if (tipoGarantia != null) {
					new ContTipoGarantia(accion, tipoGarantia, this, app);
				} else {
					app.mostrarAdvertencia("Debe Seleccionar un Items ");
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

	@Override
	public List cargarDato(TipoGarantia dato) {
		// TODO Auto-generated method stub
		return null;
	}

}
