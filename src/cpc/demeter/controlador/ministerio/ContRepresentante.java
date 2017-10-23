package cpc.demeter.controlador.ministerio;

import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.ministerio.UiRepresentante;
import cpc.modelo.ministerio.basico.EstadoCivil;
import cpc.modelo.ministerio.basico.Genero;
import cpc.modelo.ministerio.basico.GradoInstruccion;
import cpc.modelo.ministerio.basico.Nacionalidad;
import cpc.modelo.ministerio.gestion.Representante;
import cpc.negocio.ministerio.basico.NegocioRepresentante;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContRepresentante extends ContVentanaBase<Representante> {

	private static final long serialVersionUID = 6184414588153046382L;
	private AppDemeter app;
	private UiRepresentante vista;
	private NegocioRepresentante negocio;

	public ContRepresentante(int modoOperacion, Representante representante,
			ICompCatalogo<Representante> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			if (representante == null || modoAgregar()) {
				if (modoAgregar())
					System.out.println("Modo Agregar");
				representante = new Representante();
			}

			negocio = NegocioRepresentante.getInstance();
			List<EstadoCivil> estadosCiviles = negocio.getEstadosCiviles();
			List<Genero> generos = negocio.getGeneros();
			List<GradoInstruccion> gradosInstruccion = negocio
					.getGradoInstrucciones();
			List<Nacionalidad> nacionalidades = negocio.getNacionalidades();
			// List<CodigoArea> codigosArea = negocio.getCodigosArea();

			vista = new UiRepresentante("Representante ("
					+ Accion.TEXTO[modoOperacion] + ")", 700,
					gradosInstruccion, estadosCiviles, nacionalidades, generos);
			setear(representante, vista, llamador, app);
			vista = ((UiRepresentante) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun Servicio");
		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
		}
	}

	public void eliminar() {
		try {
			negocio = NegocioRepresentante.getInstance();
			negocio.setModelo(getDato());
			negocio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() throws ExcFiltroExcepcion {
		try {
			negocio = NegocioRepresentante.getInstance();
			getDato().setGenero(vista.getGenero().getSeleccion());
			negocio.setModelo(getDato());
			negocio.guardar(negocio.getModelo().getId());
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError("No se pudo Almacenar representante, "
					+ e.getMessage());
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR) {
			if (vista.getCedula().getValue() == null
					|| vista.getCedula().getValue() == "") {
				throw new WrongValueException(vista.getCedula(),
						"Indique Valor cedula/ RIF");
			}
			if (!vista.getCedula().getValue().matches("[VveEPp]?-[0-9]{5,9}")) {
				throw new WrongValueException(vista.getCedula(),
						"Cedula no Valida");
			}
			if (vista.getFecha().getValue() == null) {
				throw new WrongValueException(vista.getFecha(), "Indique fecha");
			}
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
							.matches("\\w+\\.?\\w+@\\w+\\.\\w+"))) {
				throw new WrongValueException(vista.getEmail(),
						"Correo no valido");
			}
		}
	}

	public void onEvent(Event arg0) throws Exception {
		validar();
		procesarCRUD(arg0);

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
