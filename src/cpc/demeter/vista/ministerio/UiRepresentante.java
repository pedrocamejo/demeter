package cpc.demeter.vista.ministerio;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.ministerio.basico.EstadoCivil;
import cpc.modelo.ministerio.basico.Genero;
import cpc.modelo.ministerio.basico.GradoInstruccion;
import cpc.modelo.ministerio.basico.Nacionalidad;
import cpc.modelo.ministerio.gestion.ProductorJuridico;
import cpc.modelo.ministerio.gestion.Representante;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.listas.CompRadioBotonLineal;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UiRepresentante extends CompVentanaBase<Representante> {

	private static final long serialVersionUID = -5739695024627594930L;
	private CompGrupoDatos gbGeneral, gbEmail, gbEmpresas;

	private Textbox cedula;
	private Textbox primerNombre;
	private Textbox segundoNombre;
	private Textbox primerApellido;
	private Textbox segundoApellido;
	private Textbox email;

	private Datebox fecha;
	private CompCombobox<GradoInstruccion> gradoInstruccion;
	private CompCombobox<EstadoCivil> estadoCivil;
	private CompCombobox<Nacionalidad> nacionalidad;
	private CompRadioBotonLineal<Genero> genero;

	private List<GradoInstruccion> gradosInstruccion;
	private List<EstadoCivil> estadosCiviles;
	private List<Nacionalidad> nacionalidades;
	private List<Genero> generos;
	private CompLista<ProductorJuridico> empresas;

	public UiRepresentante(String titulo, int ancho,
			List<GradoInstruccion> gradosInstruccion,
			List<EstadoCivil> estadosCiviles,
			List<Nacionalidad> nacionalidades, List<Genero> generos)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.gradosInstruccion = gradosInstruccion;
		this.estadosCiviles = estadosCiviles;
		this.nacionalidades = nacionalidades;
		this.generos = generos;
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 4);
		gbEmail = new CompGrupoDatos("", 2);
		gbEmpresas = new CompGrupoDatos("Empresas", 1);
		gradoInstruccion = new CompCombobox<GradoInstruccion>();
		estadoCivil = new CompCombobox<EstadoCivil>();
		nacionalidad = new CompCombobox<Nacionalidad>();
		genero = new CompRadioBotonLineal<Genero>(generos, true);
		primerNombre = new Textbox();
		segundoNombre = new Textbox();
		primerApellido = new Textbox();
		segundoApellido = new Textbox();
		cedula = new Textbox();
		email = new Textbox();
		fecha = new Datebox();
		empresas = new CompLista<ProductorJuridico>(getEncabezadoEmpresa());

		email.setWidth("400px");
		gradoInstruccion.setModelo(gradosInstruccion);
		estadoCivil.setModelo(estadosCiviles);
		nacionalidad.setModelo(nacionalidades);
		gradoInstruccion.setReadonly(true);
		estadoCivil.setReadonly(true);
		nacionalidad.setReadonly(true);
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Cedula:", cedula);
		gbGeneral.addComponente("Fecha Nacimiento:", fecha);
		gbGeneral.addComponente("Primer Nombre:", primerNombre);
		gbGeneral.addComponente("Segundo Nombre:", segundoNombre);
		gbGeneral.addComponente("Primer Apellido:", primerApellido);
		gbGeneral.addComponente("Segundo Apellido:", segundoApellido);

		gbGeneral.addComponente("Grado Instruccion:", gradoInstruccion);
		gbGeneral.addComponente("Estado Civil:", estadoCivil);
		gbGeneral.addComponente("Nacionalidad:", nacionalidad);
		gbGeneral.addComponente("Genero:", genero);
		gbEmail.addComponente("Email:", email);
		gbEmpresas.addComponente(empresas);
		gbGeneral.dibujar(this);
		gbEmail.dibujar(this);
		gbEmpresas.dibujar(this);
		addBotonera();
	}

	@Override
	public void cargarValores(Representante servicio) throws ExcDatosInvalidos {
		gradoInstruccion.setSeleccion(getModelo().getGradoInstruccion());
		getBinder().addBinding(gradoInstruccion, "seleccion",
				getNombreModelo() + ".gradoInstruccion", null, null, "save",
				null, null, null, null);
		estadoCivil.setSeleccion(getModelo().getEstadoCivil());
		getBinder().addBinding(estadoCivil, "seleccion",
				getNombreModelo() + ".estadoCivil", null, null, "save", null,
				null, null, null);
		nacionalidad.setSeleccion(getModelo().getNacionalidad());
		getBinder().addBinding(nacionalidad, "seleccion",
				getNombreModelo() + ".nacionalidad", null, null, "save", null,
				null, null, null);
		genero.setSeleccion(getModelo().getGenero());
		cedula.setValue(getModelo().getCedula());
		getBinder().addBinding(cedula, "value", getNombreModelo() + ".cedula",
				null, null, "save", null, null, null, null);
		primerNombre.setValue(getModelo().getPrimerNombre());
		getBinder().addBinding(primerNombre, "value",
				getNombreModelo() + ".primerNombre", null, null, "save", null,
				null, null, null);
		segundoNombre.setValue(getModelo().getSegundoNombre());
		getBinder().addBinding(segundoNombre, "value",
				getNombreModelo() + ".segundoNombre", null, null, "save", null,
				null, null, null);
		primerApellido.setValue(getModelo().getPrimerApellido());
		getBinder().addBinding(primerApellido, "value",
				getNombreModelo() + ".primerApellido", null, null, "save",
				null, null, null, null);
		segundoApellido.setValue(getModelo().getSegundoApellido());
		getBinder().addBinding(segundoApellido, "value",
				getNombreModelo() + ".segundoApellido", null, null, "save",
				null, null, null, null);
		fecha.setValue(getModelo().getFechaNacimiento());
		getBinder().addBinding(fecha, "value",
				getNombreModelo() + ".fechaNacimiento", null, null, "save",
				null, null, null, null);
		email.setValue(getModelo().getEmail());
		getBinder().addBinding(email, "value", getNombreModelo() + ".email",
				null, null, "save", null, null, null, null);
		if (getModelo().getProductores() != null)
			empresas.setModelo(getModelo().getProductores());
	}

	private List<CompEncabezado> getEncabezadoEmpresa() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Tipo", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("RIF", 100);
		titulo.setMetodoBinder("getIdentidadLegal");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Razon Social", 250);
		titulo.setMetodoBinder("getNombres");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;

	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		primerApellido.setDisabled(true);
		primerNombre.setDisabled(true);
		segundoApellido.setDisabled(true);
		segundoNombre.setDisabled(true);
		cedula.setDisabled(true);
		estadoCivil.setDisabled(true);
		nacionalidad.setDisabled(true);
		genero.setDisabled(true);
		fecha.setDisabled(true);
		gradoInstruccion.setDisabled(true);
		email.setDisabled(true);
	}

	public void modoEdicion() {
		primerApellido.setDisabled(false);
		primerNombre.setDisabled(false);
		segundoApellido.setDisabled(false);
		segundoNombre.setDisabled(false);
		cedula.setDisabled(false);
		estadoCivil.setDisabled(false);
		nacionalidad.setDisabled(false);
		genero.setDisabled(false);
		fecha.setDisabled(false);
		gradoInstruccion.setDisabled(false);
		email.setDisabled(false);
	}

	public Textbox getCedula() {
		return cedula;
	}

	public Textbox getPrimerNombre() {
		return primerNombre;
	}

	public Textbox getSegundoNombre() {
		return segundoNombre;
	}

	public Textbox getPrimerApellido() {
		return primerApellido;
	}

	public Textbox getSegundoApellido() {
		return segundoApellido;
	}

	public Textbox getEmail() {
		return email;
	}

	public Datebox getFecha() {
		return fecha;
	}

	public CompCombobox<GradoInstruccion> getGradoInstruccion() {
		return gradoInstruccion;
	}

	public CompCombobox<EstadoCivil> getEstadoCivil() {
		return estadoCivil;
	}

	public CompCombobox<Nacionalidad> getNacionalidad() {
		return nacionalidad;
	}

	public CompRadioBotonLineal<Genero> getGenero() {
		return genero;
	}

}
