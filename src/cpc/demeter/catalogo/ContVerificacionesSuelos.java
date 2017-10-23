package cpc.demeter.catalogo;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.ContVerificacionSuelo;
import cpc.modelo.demeter.basico.TipoVerificacionSuelo;
import cpc.modelo.sigesp.basico.Cargo;
import cpc.negocio.demeter.basico.NegocioVerificacionSuelo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContVerificacionesSuelos extends
		ContCatalogo<TipoVerificacionSuelo> {

	private static final long serialVersionUID = -4602011856599684640L;
	// @SuppressWarnings("unused")
	private AppDemeter app;

	public ContVerificacionesSuelos(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		super();
		NegocioVerificacionSuelo cargos = NegocioVerificacionSuelo
				.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Tipos de Verificacion de Suelos",
				cargos.getTodos(), app);
	}

	public ContVerificacionesSuelos() {
		super();
	}

	public ContVerificacionesSuelos(int modoOperacion) {
		super(modoOperacion);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		// titulo = new CompEncabezado("Id",20);
		// titulo.setOrdenable(true);
		// titulo.setMetodoBinder("getId");
		// encabezado.add(titulo);

		titulo = new CompEncabezado("Descripcion", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				NegocioVerificacionSuelo servicio = NegocioVerificacionSuelo
						.getInstance();
				TipoVerificacionSuelo verificacionSuelo = getDatoSeleccionado();
				servicio.setModelo(verificacionSuelo);
				if (verificacionSuelo == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione una Verificacion");
				else
					new ContVerificacionSuelo(accion, servicio.getModelo(),
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
	public List cargarDato(Cargo arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(TipoVerificacionSuelo arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}