package cpc.demeter.controlador;

import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContServicios;
import cpc.demeter.vista.UiServicio;
import cpc.modelo.demeter.basico.Servicio;
import cpc.modelo.demeter.basico.TipoServicio;
import cpc.modelo.demeter.basico.TipoUnidadMedida;
import cpc.negocio.demeter.basico.NegocioServicio;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContServicio extends ContVentanaBase<Servicio> {

	private static final long serialVersionUID = 6184414588153046382L;
	private AppDemeter app;
	private UiServicio vista;
	private NegocioServicio negocio;

	public ContServicio(int modoOperacion, Servicio servicio,
			ContServicios llamador, AppDemeter app) throws ExcDatosInvalidos,
			ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			if (servicio == null || modoAgregar())
				servicio = new Servicio();
			negocio = NegocioServicio.getInstance();
			List<TipoUnidadMedida> unidades = negocio.getTiposUnidades();
			List<TipoUnidadMedida> unidadesSimples = negocio
					.getTiposUnidadesSimples();
			List<TipoServicio> servicios = negocio.getTipos();

			vista = new UiServicio("Servicio (" + Accion.TEXTO[modoOperacion]
					+ ")", 580, unidades, unidadesSimples, servicios);
			setear(servicio, vista, llamador, app);
			vista = ((UiServicio) getVista());

		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun Servicio");
		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
		}
	}

	public void eliminar() {

		try {
			negocio = NegocioServicio.getInstance();
			negocio.setModelo(getDato());
			negocio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {

		try {
			negocio = NegocioServicio.getInstance();
			negocio.setModelo(getDato());
			negocio.guardar(negocio.getModelo().getId());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR
				&& (vista.getDescripcion().getValue() == null || vista
						.getDescripcion().getValue() == "")) {
			throw new WrongValueException(vista.getDescripcion(),
					"Indique una Descripcion");
		}
		if (getModoOperacion() != Accion.ELIMINAR
				&& (vista.getTipoServicio().getSeleccion() == null)) {
			throw new WrongValueException(vista.getTipoServicio(),
					"Indique un Tipo de servicio");
		}
		if (getModoOperacion() != Accion.ELIMINAR
				&& (vista.getTipoUnidad().getSeleccion() == null)) {
			throw new WrongValueException(vista.getTipoUnidad(),
					"Indique un Tipo de Unidad de Medida");
		}
	}

	public void onEvent(Event arg0) throws Exception {
		if (arg0.getTarget() == vista.getTipoUnidad())
			actualizarUnidades();
		if (arg0.getTarget() == vista.getProduccion())
			vista.visibilidadProduccion(vista.getProduccion().isChecked());
		else {
			validar();
			procesarCRUD(arg0);
		}
	}

	private void actualizarUnidades() {
		TipoUnidadMedida tipo = vista.getTipoUnidad().getSeleccion();
		if (tipo != null) {
			if (!tipo.isCompuesto()) {
				vista.getUnidadTrabajo().setSeleccion(tipo);
				vista.getUnidadProduccion().setSeleccion(tipo);
			}
		}
	}

	@Override
	public void anular() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub

	}

}
