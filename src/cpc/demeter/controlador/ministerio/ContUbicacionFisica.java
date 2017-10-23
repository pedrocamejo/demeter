package cpc.demeter.controlador.ministerio;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.ministerio.UiUbicacionFisica;
import cpc.modelo.demeter.administrativo.Impuesto;
import cpc.modelo.demeter.basico.TipoCoordenadaGeografica;
import cpc.modelo.demeter.basico.TipoDocumentoTierra;
import cpc.modelo.ministerio.basico.CoordenadaGeografica;
import cpc.modelo.ministerio.basico.LinderoDireccion;
import cpc.modelo.ministerio.basico.TipoSuperficie;
import cpc.modelo.ministerio.basico.TipoUbicacion;
import cpc.modelo.ministerio.dimension.UbicacionDireccion;
import cpc.modelo.ministerio.dimension.UbicacionSector;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.negocio.ministerio.basico.NegocioDireccion;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContUbicacionFisica extends ContVentanaBase<UbicacionDireccion> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioDireccion servicio;
	private AppDemeter app;
	private UiUbicacionFisica vista;

	public ContUbicacionFisica(int modoOperacion, UbicacionDireccion tipo,
			ICompCatalogo<UbicacionDireccion> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			if (tipo == null || modoAgregar())
				tipo = new UbicacionDireccion();
			servicio = NegocioDireccion.getInstance();
			List<UbicacionSector> sectores = servicio.getSectores();
			List<TipoUbicacion> tiposUbicacion = servicio.getTiposUbicaciones();
			List<TipoSuperficie> tiposSuperficies = servicio.getSuperficies();
			List<Productor> productores = servicio.getProductores();
			List<TipoCoordenadaGeografica> tiposCoordenadas = servicio
					.getTiposCoordenadaGeograficas();
			List<TipoDocumentoTierra> tiposDocumentosTierra = servicio
					.getTiposDocumentosTierra();
			setear(tipo, new UiUbicacionFisica("UBICACION FISICA ("
					+ Accion.TEXTO[modoOperacion] + ")", 650, sectores,
					tiposUbicacion, tiposCoordenadas, tiposSuperficies,
					productores, tiposDocumentosTierra), llamador, app);
			vista = ((UiUbicacionFisica) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void eliminar() {

		try {
			servicio = NegocioDireccion.getInstance();
			servicio.setModelo(getDato());
			servicio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() throws ExcFiltroExcepcion {
		try {
			validar();
			validarLinderosyGeoreferenciacion();
			servicio = NegocioDireccion.getInstance();
			actualizarModelo();
			servicio.setModelo(getDato());
			servicio.guardar(getDato().getId());
		} catch (Exception e) {
			throw new ExcFiltroExcepcion(e.fillInStackTrace());
		}
	}

	private void actualizarModelo() {
		if (modoAgregar() || modoEditar()) {
			actualizarCooedenadas();
			actualizarLinderos();
			// actualizarSuperficies();
			actualizarProductores();
		}
	}

	@SuppressWarnings("unchecked")
	private void actualizarCooedenadas() {
		List<CoordenadaGeografica> coordenadas = new ArrayList<CoordenadaGeografica>();
		CoordenadaGeografica coordenada;
		try {

			vista.getCoordenadas().refrescar();
			List<Row> filas = vista.getCoordenadas().getFilas().getChildren();
			for (Row item : filas) {
				coordenada = (CoordenadaGeografica) item.getAttribute("dato");
				coordenada.setDireccion(getDato());
				if (vista.getDatum().getSeleccion() == null)
					throw new WrongValueException(vista.getDescripcion(),
							"Indique un Datum");
				coordenada.setTipo(vista.getDatum().getSeleccion());
				coordenadas.add(coordenada);
			}
		} catch (NullPointerException e) {
		}
		getDato().setCoordenadasGeograficas(coordenadas);
	}

	@SuppressWarnings("unchecked")
	private void actualizarLinderos() {
		List<LinderoDireccion> linderos = new ArrayList<LinderoDireccion>();
		LinderoDireccion lindero;
		try {
			List<Row> filas = vista.getLinderos().getFilas().getChildren();
			vista.getLinderos().refrescar();
			for (Row item : filas) {
				lindero = (LinderoDireccion) item.getAttribute("dato");
				if (lindero == null) {
					throw new WrongValueException(vista.getLinderos(),
							"Indique una Descripcion");
				}
				lindero.setDireccion(getDato());
				linderos.add(lindero);
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
		getDato().setLinderos(linderos);
	}

	/*
	 * @SuppressWarnings("unchecked") private void actualizarSuperficies(){
	 * List<SuperficieUnidad> superficies = new ArrayList<SuperficieUnidad>();
	 * SuperficieUnidad superficie; try{ List<Row> filas =
	 * vista.getSuperficies().getFilas().getChildren();
	 * vista.getSuperficies().refrescar(); for (Row item : filas) { superficie =
	 * (SuperficieUnidad) item.getAttribute("dato");
	 * superficie.setUnidadProductiva(getDato()); superficies.add(superficie); }
	 * }catch (NullPointerException e) { // TODO: handle exception }
	 * getDato().setSuperficies(superficies); }
	 */

	@SuppressWarnings("unchecked")
	private void actualizarProductores() {
		try {
			List<Cliente> clientes = vista.getPropietario().getModeloSelect();
			for (Cliente item : clientes) {
				getDato().addPropietario(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR
				&& (vista.getDescripcion().getValue() == null || vista
						.getDescripcion().getValue() == "")) {
			throw new WrongValueException(vista.getDescripcion(),
					"Indique una Descripcion");
		}
	}

	public void onEvent(Event arg0) throws Exception {

		if (arg0.getTarget() == vista.getBtnProductores()) {
			new ContProductor(Accion.AGREGAR, null, vista.getPropietario(), app);
		} else if (arg0.getTarget() == vista.getCrearSector()) {
			new ContSector(Accion.AGREGAR, null, vista.getSector(), app);
		} else if (arg0.getTarget() == vista.getEditarSector()) {
			new ContSector(Accion.EDITAR, vista.getSector().getModelo(),
					vista.getSector(), app);
		} else if (arg0.getTarget() == vista.getSector().getConsultar()) {
			new ContSector(Accion.CONSULTAR, vista.getSector().getModelo(),
					vista.getSector(), app);
		} else if (arg0.getTarget() == vista.getTipoPropiedad()) {
			Suichearpropietario();
		} else if (arg0.getTarget() == vista.getDatum()) {
			SuichearGeorreferenciacion();
		} else
			try {
				procesarCRUD(arg0);
			} catch (Exception e) {
				e.printStackTrace();
				throw e;
				// app.mostrarError(e.getMessage());
			}

	}

	@SuppressWarnings({ "unchecked", "unused" })
	public void validarLinderosyGeoreferenciacion()
			throws WrongValuesException, ExcEntradaInconsistente {

		List<CoordenadaGeografica> coordenadas = new ArrayList<CoordenadaGeografica>();
		CoordenadaGeografica coordenada;
		List<LinderoDireccion> linderos = new ArrayList<LinderoDireccion>();
		LinderoDireccion lindero;

		vista.getCoordenadas().refrescar();
		List<Row> filas = vista.getCoordenadas().getFilas().getChildren();
		for (Row item : filas) {
			Intbox a = (Intbox) item.getChildren().get(0);
			Doublebox b = (Doublebox) item.getChildren().get(1);
			Doublebox c = (Doublebox) item.getChildren().get(1);

			if (vista.getDatum().getSeleccion().isUTM())
				c = (Doublebox) item.getChildren().get(2);

			if (b.getValue() == null || c.getValue() == null) {
				throw new WrongValueException(c, "Indique un Datum");
			}
		}

		filas = vista.getLinderos().getFilas().getChildren();
		vista.getLinderos().refrescar();
		for (Row item : filas) {
			String aux = ((CompCombobox<String>) item.getChildren().get(0))
					.getSeleccion();
			Textbox descripcion = (Textbox) item.getChildren().get(1);
			;
			if (aux == null || descripcion.getValue() == null) {
				throw new WrongValueException(descripcion, "Indique un lindero");
			}

		}

	}

	private void Suichearpropietario() {
		/*
		 * TipoUbicacion tipoUbicacion =
		 * vista.getTipoPropiedad().getSeleccion(); if (tipoUbicacion != null){
		 * if (tipoUbicacion.getPropietario().booleanValue()){
		 * vista.mostrarPropietarios(); }else vista.ocultarPropietarios(); }
		 */
	}

	public void SuichearGeorreferenciacion() {
		TipoCoordenadaGeografica tipo = vista.getDatum().getSeleccion();
		if (tipo != null) {
			vista.actualizarEncabezados(tipo.isUTM());
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
