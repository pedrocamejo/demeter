package cpc.demeter.controlador.ministerio;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.ministerio.UiProductor;
import cpc.modelo.demeter.basico.CodigoArea;
import cpc.modelo.demeter.basico.SectorAgricola;
import cpc.modelo.demeter.vistas.ProductorView;
import cpc.modelo.excepcion.ExcTipoNoValido;
import cpc.modelo.ministerio.basico.EstadoCivil;
import cpc.modelo.ministerio.basico.Genero;
import cpc.modelo.ministerio.basico.GradoInstruccion;
import cpc.modelo.ministerio.basico.Nacionalidad;
import cpc.modelo.ministerio.basico.Telefono;
import cpc.modelo.ministerio.basico.TipoProductor;
import cpc.modelo.ministerio.gestion.InstitucionCrediticia;
import cpc.modelo.ministerio.gestion.Organizacion;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.modelo.ministerio.gestion.ProductorJuridico;
import cpc.modelo.ministerio.gestion.ProductorNatural;
import cpc.modelo.ministerio.gestion.Representante;
import cpc.negocio.ministerio.basico.NegocioProductor;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContProductor extends ContVentanaBase<Productor> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioProductor servicio;
	private AppDemeter app;
	private UiProductor vista;
	private ProductorNatural modeloNatural;
	private ProductorJuridico modeloJuridico;
	private boolean tipo;

	public ContProductor(int modoOperacion, Productor productor,ICompCatalogo  llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			servicio = NegocioProductor.getInstance();
			if (productor == null || modoAgregar())
				productor = servicio.nuevo();
			else if (productor != null) {
				if (productor.getId() != null) {
					servicio.setModelo(productor);
					servicio.enriqueserDatos();
					if (productor instanceof ProductorNatural) {
						modeloNatural = servicio.getModeloNatural();
					} else {
						modeloJuridico = (ProductorJuridico) servicio
								.getModeloJuridico();
					}
					productor = servicio.getModelo();
				}
			}
			List<TipoProductor> tipos = servicio.getTiposProductores();
			List<Organizacion> organizaciones = servicio.getOrganizaciones();
			List<InstitucionCrediticia> financiamientos = servicio
					.getFinanciamientos();
			List<SectorAgricola> sectoresAgriciolas = servicio
					.getSectoresAgricolas();
			List<EstadoCivil> estadosCiviles = servicio.getEstadosCiviles();
			List<Genero> generos = servicio.getGeneros();
			List<GradoInstruccion> gradosInstruccion = servicio
					.getGradoInstrucciones();
			List<Nacionalidad> nacionalidades = servicio.getNacionalidades();
			List<CodigoArea> codigosArea = servicio.getCodigosArea();
			List<Representante> representantes = servicio.getRepresentantes();
			setear(productor, new UiProductor("PRODUCTOR (" + Accion.TEXTO[modoOperacion] + ")", 650, tipos,
					organizaciones, financiamientos, nacionalidades,
					estadosCiviles, generos, sectoresAgriciolas,gradosInstruccion, codigosArea, representantes), llamador,
					app);
			vista = ((UiProductor) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		} catch (ExcFiltroExcepcion e) {
			this.app.mostrarError(e.getMessage());
			e.printStackTrace();
		} catch (ExcTipoNoValido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void eliminar() {

		try {
			servicio = NegocioProductor.getInstance();
			servicio.setModelo(getDato());
			servicio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {
		try {
			servicio = NegocioProductor.getInstance();
			actualizarModelo(servicio);
			servicio.guardar(getDato().getId());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	private void actualizarModelo(NegocioProductor servicio) {
		if (modoAgregar() || modoEditar()) {
			tipo = getDato().getTipo().isJuridico();
			if (tipo) {
				modeloJuridico = new ProductorJuridico(getDato());
				enriquecerJuridico();
				servicio.setModelo(modeloJuridico);
				actualizarTelefonos(modeloJuridico);
				System.out.println("Juridico");
				setDato(modeloJuridico);
			} else {
				System.out.println("Natural");
				modeloNatural = new ProductorNatural(getDato());
				enriquecerNatural();
				actualizarTelefonos(modeloNatural);
				servicio.setModelo(modeloNatural);
				setDato(modeloNatural);
			}
			servicio.getModelo().setIdentidadLegal(
					servicio.getModelo().getIdentidadLegal().toUpperCase());
		}
	}

	@SuppressWarnings("unchecked")
	private void actualizarTelefonos(Productor productor) {
		List<Telefono> telefonos = new ArrayList<Telefono>();
		Telefono telefono;
		try {
			List<Row> filas = vista.getTelefonos().getFilas().getChildren();
			vista.getTelefonos().refrescar();
			for (Row item : filas) {
				telefono = (Telefono) item.getAttribute("dato");
				telefono.setCliente(productor);
				telefonos.add(telefono);
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
		productor.setTelefonos(telefonos);
	}

	@SuppressWarnings("unchecked")
	private void actualizarRepresentantes(ProductorJuridico productor) {
		try {
			List<Representante> representantes = vista.getRepresentantes()
					.getModeloSelect();
			for (Representante item : representantes) {
				System.out.printf("$s \n", item.getApellidos());
				productor.addRepresentante(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void enriquecerNatural() {
		modeloNatural.setPrimerNombre(vista.getPrimerNombre().getValue().trim()
				.toUpperCase());
		modeloNatural.setPrimerApellido(vista.getPrimerApellido().getValue()
				.trim().toUpperCase());
		modeloNatural.setSegundoApellido(vista.getSegundoApellido().getValue()
				.trim().toUpperCase());
		modeloNatural.setSegundoNombre(vista.getSegundoNombre().getValue()
				.trim().toUpperCase());
		modeloNatural.actualizarNombres();
		modeloNatural.setMilitante(vista.getMilitante().isChecked());
		modeloNatural.setGradoInstruccion(vista.getGradoInstruccion()
				.getSeleccion());
		modeloNatural.setEstadoCivil(vista.getEstadoCivil().getSeleccion());
		modeloNatural.setNacionalidad(vista.getNacionalidad().getSeleccion());
		modeloNatural.setEmail(vista.getEmail().getValue());
		modeloNatural.setGenero(vista.getGenero().getSeleccion());
		modeloNatural.setCargaFamiliar(vista.getCargaFamiliar().getValue());
	}

	private void enriquecerJuridico() {
		modeloJuridico.setNombres(modeloJuridico.getNombres().toUpperCase());
		actualizarRepresentantes(modeloJuridico);
	}

	@SuppressWarnings("unchecked")
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR) {
			if (vista.getTipo().getSeleccion() == null) {
				throw new WrongValueException(vista.getTipo(),
						"Indique tipo Productor");
			}
			if (vista.getIdentidad().getValue() == null
					|| vista.getIdentidad().getValue() == "") {
				throw new WrongValueException(vista.getIdentidad(),
						"Indique Valor cedula/ RIF");
			}
			if (!vista.getIdentidad().getValue()
					.matches("[JjGgVveEPpCc]?-[0-9]{5,9}")) {
				throw new WrongValueException(vista.getIdentidad(),
						"Cedula no Valida");
			}
			if (vista.getFechaNacimiento().getValue() == null) {
				throw new WrongValueException(vista.getFechaNacimiento(),
						"Indique fecha");
			}
			if (vista.getTipoJuridico().isChecked())
				validarJuridico();
			else
				validarNatural();
			if (vista.getFechaInicio().getValue() == null) {
				throw new WrongValueException(vista.getFechaInicio(),
						"Indique Fecha");
			}

			if (vista.getOrganizado().isChecked()
					&& vista.getOrganizacion().getSeleccion() == null) {
				throw new WrongValueException(vista.getOrganizacion(),
						"Indique Organizacion");
			}

			if (vista.getDireccionFiscal().getValue().length() == 0
					|| vista.getDireccionFiscal().getValue().length() >= 150) {
				throw new WrongValueException(
						vista.getDireccionFiscal(),
						"la direccion fiscal no puede estar en blanco y debe de tener menos de 150 caracteres ");
			}

			List<Row> filas = vista.getTelefonos().getFilas().getChildren();
			vista.getTelefonos().refrescar();
			CompCombobox<CodigoArea> comboArea;
			Textbox nro;
			System.out.println(filas);

			if (filas.isEmpty() || filas == null)
				throw new WrongValueException(
						"Por favo Indique al menos un telefono");

			for (Row item : filas) {
				comboArea = (CompCombobox<CodigoArea>) item.getChildren()
						.get(0);
				nro = (Textbox) item.getChildren().get(1);
				if (comboArea.getSeleccion() == null)
					throw new WrongValueException(comboArea, "Indique Codigo");
				if (nro.getText() == null || nro.getText().length() < 7)
					throw new WrongValueException(comboArea,
							"Indique Numero valido");
			}
		}
	}

	private void validarJuridico() throws WrongValuesException,
			ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR) {
			if (vista.getRazonSocial().getValue() == null
					|| vista.getRazonSocial().getValue() == "") {
				throw new WrongValueException(vista.getRazonSocial(),
						"Indique Razon Social");

			}

			if (vista.getRepresentantes().getModeloSelect().size() == 0) {
				throw new WrongValueException(vista.getRepresentantes(),
						"Indique al menos un representante");
			}
		}
	}

	private void validarNatural() throws WrongValuesException,
			ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR) {
			if (vista.getPrimerNombre().getValue() == null
					|| vista.getPrimerNombre().getValue() == "") {
				throw new WrongValueException(vista.getPrimerNombre(),
						"Indique Primer Nombre");
			}
			if (vista.getPrimerApellido().getValue() == null
					|| vista.getPrimerApellido().getValue() == "") {
				throw new WrongValueException(vista.getPrimerApellido(),
						"Indique Primer Apellido");
			}

			if (vista.getNacionalidad().getSeleccion() == null) {
				throw new WrongValueException(vista.getNacionalidad(),
						"Indique Nacionalidad");
			}
			if (vista.getEstadoCivil().getSeleccion() == null) {
				throw new WrongValueException(vista.getEstadoCivil(),
						"Indique Estado Civil");
			}
			if (vista.getGradoInstruccion().getSeleccion() == null) {
				throw new WrongValueException(vista.getGradoInstruccion(),
						"Indique el grado de Instrucción");
			}
			if (vista.getGenero().getSeleccion() == null) {
				throw new WrongValueException(vista.getGenero(),
						"Indique Genero");
			}
			if (vista.getEmail().getValue() == null
					|| (vista.getEmail().getValue().length() > 0 && !vista
							.getEmail().getValue()
							.matches("\\w+\\.?\\w+@\\w+\\.\\w+\\.?\\w+"))) {
				throw new WrongValueException(vista.getEmail(),
						"Correo no valido");

			}

		}
	}

	public void onEvent(Event event) throws Exception {
		System.out.println(event.getTarget() + "  " + event.getName());
		if (event.getTarget() == getVista().getAceptar()) {
			validar();
			try {
				procesarCRUD(event);
			} catch (Exception e) {
				e.printStackTrace();
				app.mostrarError(e.getMessage());
			}
		} else if (event.getName().equals(Events.ON_SELECT)) {
			if (event.getTarget() == vista.getTipo())
				actualizarDatos();
		} else if (event.getName().equals(Events.ON_CHECK)) {
			vista.getOrganizacion().setDisabled(
					!vista.getOrganizado().isChecked());
		} else if (event.getTarget() == vista.getBotonRepresentante()) {
			new ContRepresentante(Accion.AGREGAR, null,
					vista.getRepresentantes(), app);
		}
	}

	public void actualizarDatos() {
		vista.actualizarDatos();

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
