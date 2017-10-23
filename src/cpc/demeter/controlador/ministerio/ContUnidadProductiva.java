package cpc.demeter.controlador.ministerio;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Row;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ministerio.ContUnidadesProductivas;
import cpc.demeter.vista.ministerio.UiUnidadProductiva;
import cpc.modelo.demeter.basico.SectorAgricola;
import cpc.modelo.demeter.basico.TipoVerificacionSuelo;
import cpc.modelo.ministerio.basico.TipoRiego;
import cpc.modelo.ministerio.basico.TipoSuelo;
import cpc.modelo.ministerio.basico.TipoTenenciaTierra;
import cpc.modelo.ministerio.basico.TipoUsoTierra;
import cpc.modelo.ministerio.basico.TipoVialidad;
import cpc.modelo.ministerio.dimension.UbicacionDireccion;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.modelo.ministerio.gestion.ProductorJuridico;
import cpc.modelo.ministerio.gestion.ProductorNatural;
import cpc.modelo.ministerio.gestion.UnidadProductiva;
import cpc.negocio.ministerio.basico.NegocioUnidadProductiva;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;
import cpc.modelo.ministerio.gestion.UnidadProductivaTipoRiego;

public class ContUnidadProductiva extends ContVentanaBase<UnidadProductiva> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioUnidadProductiva servicio;
	private AppDemeter app;
	private UiUnidadProductiva vista;

	public ContUnidadProductiva(int modoOperacion, UnidadProductiva unidad,
			ContUnidadesProductivas llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			servicio = NegocioUnidadProductiva.getInstance();
			if (unidad == null || modoAgregar())
				unidad = new UnidadProductiva();
			else {
				if (unidad.getId() != null) {
					servicio.setModelo(unidad);
					servicio.enriqueserDireccion();
					// servicio.enriqueserProductor();
					unidad = servicio.getModelo();
				}
			}
			servicio.setModelo(unidad);
			List<UbicacionDireccion> direcciones = servicio.getDirecciones();
			List<TipoUsoTierra> tiposUsosTierra = servicio.getUsosTierra();
			List<TipoSuelo> tiposSuelos = servicio.getSuelos();
			List<TipoRiego> tiposRiego = servicio.getRiegos();
			List<TipoVialidad> tiposVialidades = servicio.getVialidades();
			List<TipoTenenciaTierra> tiposTenenciasTierra = servicio
					.getTenenciasTierra();
			List<Productor> productores = servicio.getProductores();
			List<SectorAgricola> sectoresA = servicio.getSectoresAgricolas();
			List<TipoVerificacionSuelo> tiposVerifSuelo = servicio
					.getTiposVerifiSuelo();
			if (!modoAgregar()) {
				if (unidad != null && unidad.getId() != null)
					servicio.enriqueserDireccion();
			}
			setear(unidad, new UiUnidadProductiva("UNIDAD PRODUCTIVA ("
					+ Accion.TEXTO[modoOperacion] + ")", 900, direcciones,
					tiposUsosTierra, tiposSuelos, tiposRiego, tiposVialidades,
					tiposTenenciasTierra, productores, sectoresA,
					tiposVerifSuelo), llamador, app);
			vista = ((UiUnidadProductiva) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
		}
	}

	public void eliminar() {

		try {
			servicio = NegocioUnidadProductiva.getInstance();
			servicio.setModelo(getDato());
			servicio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {

		try {
			servicio = NegocioUnidadProductiva.getInstance();
			servicio.setModelo(getDato());
			actualizarTiposRiego();
			servicio.guardar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR) {
			if ((vista.getUbicacionFisica().getModelo() == null)) {
				throw new WrongValueException(vista.getUbicacionFisica(),
						"Indique una Ubicacion Fisica");
			}
			if (vista.getProductor().getModelo() == null)
				throw new WrongValueException(vista.getProductor(),
						"Indique productor");
			if ((vista.getNombre().getValue().length() < 2))
				throw new WrongValueException(vista.getNombre(),
						"Indique una Descripcion");
			if ((vista.getTipoTenenciaTierra().getSeleccion() == null))
				throw new WrongValueException(vista.getTipoTenenciaTierra(),
						"Indique Tipo Tenencia de la tierra");
			if (vista.getTipoSuelo().getSeleccion() != null
					&& vista.getCmbTipoVerificacionSuelo().getSeleccion() == null)
				throw new WrongValueException(
						vista.getCmbTipoVerificacionSuelo(),
						"Indique el Tipo de Verificacion del Suelo");
			if ((vista.getTipoVialidad().getSeleccion() == null))
				throw new WrongValueException(vista.getTipoVialidad(),
						"Indique un Tipo de Vialidad");
			if (vista.getlTiposRiego().getFilas().getChildren().size() < 1)
				throw new WrongValueException(vista.getlTiposRiego(),
						"Indique un Tipo de Riego");
			if (!esSumaSuperficiesValidas())
				throw new WrongValueException(vista,
						"La suma de las superficies caracterizadas exceden la superficie total");

		}

	}

	public void onEvent(Event event) throws Exception {
		System.out.println(event.getName() + " " + event.getTarget());
		if (event.getTarget() == getVista().getAceptar()) {
			validar();
			try {
				procesarCRUD(event);
			} catch (Exception e) {
				e.printStackTrace();
				app.mostrarError(e.getMessage());
			}
		} else if (event.getTarget() == vista.getCrearUbicacionFisica()) {
			System.out.println("Creando Ubicacion");
			new ContUbicacionFisica(Accion.AGREGAR, null,
					vista.getUbicacionFisica(), app);
		} else if (event.getTarget() == vista.getEditUbicacionFisica()) {
			new ContUbicacionFisica(Accion.EDITAR, vista.getUbicacionFisica()
					.getModelo(), vista.getUbicacionFisica(), app);
		} else if (event.getTarget() == vista.getCrearProductor()) {
			new ContProductor(Accion.AGREGAR, null, vista.getProductor(), app);
		} else if (event.getTarget() == vista.getUbicacionFisica()
				.getConsultar()) {
			new ContUbicacionFisica(Accion.CONSULTAR, vista
					.getUbicacionFisica().getModelo(),
					vista.getUbicacionFisica(), app);
		} else if (event.getTarget() == vista.getProductor().getConsultar()) {
			if (getDato().getProductor().getTipo().isJuridico())
				new ContProductor(Accion.CONSULTAR,
						(ProductorJuridico) getDato().getProductor(),
						vista.getProductor(), app);
			else
				new ContProductor(Accion.CONSULTAR,
						(ProductorNatural) getDato().getProductor(),
						vista.getProductor(), app);
		} else if (event.getTarget() == vista.getEditProductor()) {
			new ContProductor(Accion.EDITAR, vista.getProductor().getModelo(),
					vista.getProductor(), app);
		}

	}

	@SuppressWarnings("unchecked")
	private void actualizarTiposRiego() {
		List<UnidadProductivaTipoRiego> tiposRiego = new ArrayList<UnidadProductivaTipoRiego>();
		UnidadProductivaTipoRiego unidadtipo;
		try {

			vista.getlTiposRiego().refrescar();
			List<Row> filas = vista.getlTiposRiego().getFilas().getChildren();
			Double dblSuma = 0.00;
			for (Row item : filas) {

				unidadtipo = (UnidadProductivaTipoRiego) item
						.getAttribute("dato");
				if (unidadtipo.getTipoRiego() == null) {
					throw new WrongValueException(item,
							"Seleccione un Tipo de Riego");
				}
				if (unidadtipo.getSuperficie() == null) {
					throw new WrongValueException(item,
							"Indique (Has) en la Columna Superficie de Tipos de Riego");
				} else {
					dblSuma += unidadtipo.getSuperficie();
				}

				unidadtipo.setUnidadProductiva(getDato());
				tiposRiego.add(unidadtipo);
			}
			if (dblSuma > getDato().getUbicacion().getSuperficie()) {
				throw new WrongValueException(
						"Se excede en el Total de Superficies en los Tipos de Riego con respecto a la Superficie Total");
			}

		} catch (NullPointerException e) {
		}

		getDato().setTiposRiego(tiposRiego);

		if (tiposRiegoRepetidos()) {
			throw new WrongValueException(
					"No pueden repetirse los tipos de Riego");
		}

	}

	private boolean esSumaSuperficiesValidas() {
		double dblActuales = 0.00, dblPotenciales = 0.00, dblBajoReserva = 0.00;

		if (vista.getTxtSupActualAprovechable().getValue() != null)
			dblActuales = vista.getTxtSupActualAprovechable().getValue();

		if (vista.getTxtSupPotencialAprovechable().getValue() != null)
			dblPotenciales = vista.getTxtSupPotencialAprovechable().getValue();

		if (vista.getTxtSupBajoReservaForestal().getValue() != null)
			dblBajoReserva = vista.getTxtSupBajoReservaForestal().getValue();

		if ((dblActuales + dblPotenciales + dblBajoReserva) > vista
				.getUbicacionFisica().getModelo().getSuperficie())
			return false;

		return true;
	}

	private boolean tiposRiegoRepetidos() {
		List<TipoRiego> lista = new ArrayList<TipoRiego>();
		for (UnidadProductivaTipoRiego utr : getDato().getTiposRiego()) {
			if (lista.contains(utr.getTipoRiego()))
				return true;
			else
				lista.add(utr.getTipoRiego());
		}

		return false;
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
