package cpc.demeter.controlador.gestion;

import java.util.List;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContAlmacenes;
import cpc.demeter.vista.gestion.UIAlmacen;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.ministerio.dimension.UbicacionDireccion;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.modelo.sigesp.basico.TipoAlmacen;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.negocio.sigesp.NegocioAlmacen;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContAlmacen extends ContVentanaBase<Almacen> {

	private static final long serialVersionUID = -6988032414939364860L;
	private NegocioAlmacen servicio;
	private AppDemeter app;
	private UIAlmacen vista;
	private UnidadAdministrativa unidadAdministrativa;

	public ContAlmacen(int modoOperacion, Almacen almacen,
			ContAlmacenes llamador, AppDemeter app) throws ExcDatosInvalidos,
			ExcSeleccionNoValida, ExcFiltroExcepcion {
		super(modoOperacion);
		this.app = app;

		List<TipoAlmacen> tipoAlmacenes = null;
		List<UbicacionDireccion> ubicaciones = null;
		List<Trabajador> trabajadores = null;
		List<UnidadAdministrativa> unidades = null;
		List<UnidadFuncional> unidadesFuncionales = null;
		servicio = NegocioAlmacen.getInstance();

		try {
			if (almacen == null || modoAgregar()) {
				if (servicio.getModoFuncionamientoGlobal())
					unidades = servicio.obtenerSedes();
				else
					this.unidadAdministrativa = servicio
							.obtenerUnidadAdministrativa();

				tipoAlmacenes = servicio.getTiposAlmacen();
				ubicaciones = servicio.getUbicaciones();
				trabajadores = servicio.getTrabajadores();
				unidadesFuncionales = servicio.getUnidadesEjecutoras();

				almacen = new Almacen();
				setear(almacen,
						new UIAlmacen("ALMACEN (" + Accion.TEXTO[modoOperacion]
								+ ")", 700, ubicaciones, trabajadores,
								unidadAdministrativa, tipoAlmacenes,
								modoOperacion, unidades, servicio
										.getModoFuncionamientoGlobal(),
								unidadesFuncionales), llamador, app);
			}

			if (almacen != null && modoEditar()) {
				if (servicio.getModoFuncionamientoGlobal())
					unidades = servicio.obtenerSedes();
				else
					this.unidadAdministrativa = servicio
							.obtenerUnidadAdministrativa();

				tipoAlmacenes = servicio.getTiposAlmacen();
				ubicaciones = servicio.getUbicaciones();
				trabajadores = servicio.getTrabajadores();
				unidadesFuncionales = servicio.getUnidadesEjecutoras();

				setear(almacen,
						new UIAlmacen("ALMACEN (" + Accion.TEXTO[modoOperacion]
								+ ")", 700, ubicaciones, trabajadores,
								unidadAdministrativa, tipoAlmacenes,
								modoOperacion, unidades, servicio
										.getModoFuncionamientoGlobal(),
								unidadesFuncionales), llamador, app);
			}

			if (almacen != null && modoConsulta())
				setear(almacen,
						new UIAlmacen("ALMACEN (" + Accion.TEXTO[modoOperacion]
								+ ")", 700, modoOperacion), llamador, app);

			vista = (UIAlmacen) getVista();
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha Seleccionado nigun campo");
		}
	}

	public void eliminar() {

	}

	public void guardar() {

		try {

			servicio = NegocioAlmacen.getInstance();
			if (!servicio.getModoFuncionamientoGlobal())
				getDato().setUnidadAdministrativa(unidadAdministrativa);
			servicio.setAlmacen(getDato());
			servicio.guardar();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (vista.getTxtNombre().getValue().length() == 0)
			throw new WrongValueException(vista.getTxtNombre(),
					"Debe llenar este campo");
		if (vista.getTxtDescripcion().getValue().length() == 0)
			throw new WrongValueException(vista.getTxtDescripcion(),
					"Debe llenar este campo");
		if (vista.getTxtLocalidad().getValue().length() == 0)
			throw new WrongValueException(vista.getTxtLocalidad(),
					"Debe llenar este campo");
		if (vista.getTxtTelefono().getValue().length() == 0)
			throw new WrongValueException(vista.getTxtTelefono(),
					"Debe llenar este campo");
		if (servicio.getModoFuncionamientoGlobal()) {
			if (vista.getCmbUnidadesAdministrativas().getSeleccion() == null)
				throw new WrongValueException(
						vista.getCmbUnidadesAdministrativas(),
						"Debe seleccionar una Unidad");
		}
		if (vista.getCmbTipoAlmacen().getSeleccion() == null)
			throw new WrongValueException(vista.getCmbTipoAlmacen(),
					"Debe seleccionar un Almacen");
		if (vista.getCmbUbicacionGeografica().getSeleccion() == null)
			throw new WrongValueException(vista.getCmbUbicacionGeografica(),
					"Debe seleccionar una ubicación");
		if (vista.getCmbTrabajador().getSeleccion() == null)
			throw new WrongValueException(vista.getCmbTrabajador(),
					"Debe seleccionar un Trabajador");
		if (vista.getUnidadFuncional().getSeleccion() == null)
			throw new WrongValueException(vista.getUnidadFuncional(),
					"Debe seleccionar una Unidad Funcional");
	}

	public void onEvent(Event event) throws Exception {
		procesarCRUD(event);
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
