package cpc.demeter.vista.ministerio;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.api.Button;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.basico.CodigoArea;
import cpc.modelo.demeter.basico.SectorAgricola;
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
import cpc.modelo.ministerio.gestion.UnidadProductiva;
import cpc.negocio.ministerio.basico.NegocioProductor;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompGrillaConBoton;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.listas.CompListaBusca;
import cpc.zk.componente.listas.CompListaMultiple;
import cpc.zk.componente.listas.CompRadioBotonLineal;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UiProductor extends CompVentanaBase<Productor> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 399920353170409763L;
	private Textbox identidad;
	private Textbox razonSocial;
	private Textbox email;
	private Textbox paginaWeb;

	private Checkbox organizado;
	private Checkbox tipoJuridico;
	private Checkbox agroVenezuela;
	private Datebox fechaNacimiento;
	private Datebox fechaInicio;

	private CompCombobox<TipoProductor> tipo;
	private CompCombobox<Organizacion> organizacion;
	private CompListaMultiple financiamiento;
	private CompCombobox<SectorAgricola> sectorAgricola;

	private CompGrillaConBoton<Telefono> telefonos;
	private CompLista<UnidadProductiva> unidadesproductivas;

	private Doublebox hrasTotales;

	private CompGrupoDatos productor, natural, juridico, generico, nombres,
			cedulaG, direccion;

	private Label cedulaRif;
	private Label paginaLbl, emailLbl;
	private Label nacimientoCreacion;
	private Label razonSocialLbl;
	private Textbox primerNombre;
	private Textbox segundoNombre;
	private Textbox primerApellido;
	private Textbox segundoApellido;
	private Checkbox militante;
	private CompCombobox<EstadoCivil> estadoCivil;
	private CompCombobox<Nacionalidad> nacionalidad;
	private CompCombobox<GradoInstruccion> gradoInstruccion;
	private CompRadioBotonLineal<Genero> genero;
	private Intbox cargaFamiliar;
	private Textbox direccionFiscal;

	private List<TipoProductor> tiposClientes;
	private List<Organizacion> organizaciones;
	private List<SectorAgricola> sectoresAgricolas;
	private List<InstitucionCrediticia> financiamientos;
	private List<EstadoCivil> estadosCiviles;
	private List<Nacionalidad> nacionalidades;
	private List<Genero> generos;
	private List<GradoInstruccion> gradosInstuccion;
	private List<CodigoArea> codigosTelefonos;
	private List<Representante> representantesLista;

	private CompListaBusca<Representante> representantes;

	public UiProductor(String titulo, int ancho, List<TipoProductor> tipos,
			List<Organizacion> organizaciones,
			List<InstitucionCrediticia> financiamientos,
			List<Nacionalidad> nacionalidades,
			List<EstadoCivil> estadosCiviles, List<Genero> generos,
			List<SectorAgricola> sectoresAgricolas,
			List<GradoInstruccion> gradosInstuccion,
			List<CodigoArea> codigosTelefonos,
			List<Representante> representantesLista) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.tiposClientes = tipos;
		this.organizaciones = organizaciones;
		this.financiamientos = financiamientos;
		this.nacionalidades = nacionalidades;
		this.estadosCiviles = estadosCiviles;
		this.generos = generos;
		this.sectoresAgricolas = sectoresAgricolas;
		this.gradosInstuccion = gradosInstuccion;
		this.codigosTelefonos = codigosTelefonos;
		this.representantesLista = representantesLista;
	}

	public UiProductor(String titulo) throws ExcDatosInvalidos {
		super(titulo);
	}

	public void inicializar() {

		cedulaRif = new Label();
		nacimientoCreacion = new Label();
		paginaLbl = new Label("Pagina Web:");
		emailLbl = new Label("Correo Electronico:");
		razonSocialLbl = new Label("Razón Social");

		identidad = new Textbox();
		razonSocial = new Textbox();
		primerApellido = new Textbox();
		primerNombre = new Textbox();
		segundoNombre = new Textbox();
		segundoApellido = new Textbox();
		email = new Textbox();
		paginaWeb = new Textbox();

		fechaInicio = new Datebox();
		fechaNacimiento = new Datebox();

		hrasTotales = new Doublebox();
		cargaFamiliar = new Intbox();

		tipo = new CompCombobox<TipoProductor>();
		organizacion = new CompCombobox<Organizacion>();

		sectorAgricola = new CompCombobox<SectorAgricola>();
		estadoCivil = new CompCombobox<EstadoCivil>();
		nacionalidad = new CompCombobox<Nacionalidad>();
		gradoInstruccion = new CompCombobox<GradoInstruccion>();
		direccionFiscal = new Textbox();

		financiamiento = new CompListaMultiple("Financiamientos");

		genero = new CompRadioBotonLineal<Genero>(generos, true);

		militante = new Checkbox("Militante");
		tipoJuridico = new Checkbox("Juridico");
		organizado = new Checkbox("Organizado");
		agroVenezuela = new Checkbox("AgroVenezuela");

		telefonos = new CompGrillaConBoton<Telefono>(cargarListaTelefonos(),
				getModelo().getTelefonos(), encabezadosTelefonos(),
				getControlador());
		representantes = new CompListaBusca<Representante>(
				encabezadosRepresentantes(), 0, getControlador());
		unidadesproductivas = new CompLista<UnidadProductiva>(
				getEncabezadoSector());
		productor = new CompGrupoDatos("Tipo Productor", 3);
		natural = new CompGrupoDatos("", 2);
		juridico = new CompGrupoDatos("Representantes", 2);
		generico = new CompGrupoDatos("Comun", 4);
		nombres = new CompGrupoDatos("Datos Personales", 4);
		cedulaG = new CompGrupoDatos("", 4);
		direccion = new CompGrupoDatos("Unidades Produccion", 1);

		generico.setAnchoColumna(2, 150);
		hrasTotales.setDisabled(true);
		telefonos.setNuevo(new Telefono());
		razonSocial.setWidth("400px");
		direccionFiscal.setWidth("450px");
		direccionFiscal.setMaxlength(250);
		paginaWeb.setWidth("400px");
		direccionFiscal.setMultiline(true);
		direccionFiscal.setRows(2);
		email.setWidth("400px");
		tipoJuridico.setWidth("250px");
		genero.setWidth("250px");
		financiamiento.setWidth("180px");
		tipoJuridico.setDisabled(true);
		representantes.setAncho(550);
		representantes.setWidth("550px");
		cedulaG.setVisible(false);
		nombres.setVisible(false);
		juridico.setVisible(false);
		generico.setVisible(false);
		financiamiento.setRows(3);
		tipo.setModelo(tiposClientes);
		organizacion.setModelo(organizaciones);
		sectorAgricola.setModelo(sectoresAgricolas);
		estadoCivil.setModelo(estadosCiviles);
		nacionalidad.setModelo(nacionalidades);
		financiamiento.setModelo(financiamientos);
		gradoInstruccion.setModelo(gradosInstuccion);
		tipo.addEventListener(Events.ON_SELECT, getControlador());
		organizado.addEventListener(Events.ON_CHECK, getControlador());
		telefonos.addEventListener(Events.ON_CLICK, getControlador());
		representantes.setModelo(representantesLista);

		nacionalidad.setReadonly(true);
		gradoInstruccion.setReadonly(true);
		estadoCivil.setReadonly(true);
		organizacion.setReadonly(true);
		tipo.setReadonly(true);
		organizacion.setDisabled(true);
	}

	public void dibujar() {
		productor.setAnchoColumna(0, 100);
		productor.addComponente("Tipo Productor:", tipo);
		productor.addComponente(tipoJuridico);
		representantes.setAncho(450);
		cedulaG.addComponente(cedulaRif);
		cedulaG.addComponente(identidad);
		cedulaG.addComponente(nacimientoCreacion);
		cedulaG.addComponente(fechaNacimiento);

		nombres.addComponente("Primer Nombre:", primerNombre);
		nombres.addComponente("Segundo Nombre:", segundoNombre);
		nombres.addComponente("Primer Apellido:", primerApellido);
		nombres.addComponente("Segundo Apellido:", segundoApellido);
		nombres.addComponente("Nacionalidad:", nacionalidad);
		nombres.addComponente("Estado Civil:", estadoCivil);
		nombres.addComponente("Nivel Instruccion:", gradoInstruccion);
		nombres.addComponente("Carga Familiar:", cargaFamiliar);
		nombres.addComponente("Genero :", genero);
		nombres.addComponente("", militante);

		natural.addComponente(razonSocialLbl);
		natural.addComponente(razonSocial);
		natural.addComponente("Direccion Fiscal:", direccionFiscal);
		natural.addComponente(emailLbl);
		natural.addComponente(email);
		natural.addComponente(paginaLbl);
		natural.addComponente(paginaWeb);

		juridico.addComponente("Representantes:", representantes);
		// juridico.addComponente("Fecha Creación:", fechaNacimiento);
		telefonos.dibujar();
		generico.addComponente("Fecha Incorporación:", fechaInicio);

		generico.addComponente(organizado);
		generico.addComponente(organizacion);
		generico.addComponente("Financiamiento:", financiamiento);
		generico.addComponente("Telefonos:", telefonos);
		generico.addComponente("Pertenece a Misión:", agroVenezuela);

		direccion.addComponente(unidadesproductivas);
		productor.dibujar(this);
		cedulaG.dibujar(this);
		nombres.dibujar(this);
		natural.dibujar(this);
		juridico.dibujar(this);
		generico.dibujar(this);
		direccion.dibujar(this);
		addBotonera();

	}

	@Override
	public void cargarValores(Productor arg0) throws ExcDatosInvalidos {
		tipo.setSeleccion(getModelo().getTipo());
		getBinder().addBinding(tipo, "seleccion", getNombreModelo() + ".tipo",
				null, null, "save", null, null, null, null);
		try {
			if (getModelo().getTipo() != null)
				;
			tipoJuridico.setChecked(getModelo().getTipo().isJuridico());
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
		razonSocial.setValue(getModelo().getNombres());
		getBinder().addBinding(razonSocial, "value",
				getNombreModelo() + ".nombres", null, null, "save", null, null,
				null, null);
		fechaInicio.setValue(getModelo().getFechaIngreso());
		getBinder().addBinding(fechaInicio, "value",
				getNombreModelo() + ".fechaIngreso", null, null, "save", null,
				null, null, null);
		identidad.setValue(getModelo().getIdentidadLegal());
		getBinder().addBinding(identidad, "value",
				getNombreModelo() + ".identidadLegal", null, null, "save",
				null, null, null, null);
		fechaNacimiento.setValue(getModelo().getFechaNacimiento());
		getBinder().addBinding(fechaNacimiento, "value",
				getNombreModelo() + ".fechaNacimiento", null, null, "save",
				null, null, null, null);
		getBinder().addBinding(organizado, "checked",
				getNombreModelo() + ".organizado", null, null, "save", null,
				null, null, null);
		organizacion.setSeleccion(getModelo().getOrganizacion());
		getBinder().addBinding(organizacion, "seleccion",
				getNombreModelo() + ".organizacion", null, null, "save", null,
				null, null, null);
		financiamiento.setModeloSelect(getModelo().getFinanciamientos());
		getBinder().addBinding(financiamiento, "modeloSelect",
				getNombreModelo() + ".financiamientos", null, null, "save",
				null, null, null, null);
		telefonos.setColeccion(getModelo().getTelefonos());
		if (getModelo().getAgroVenezuela() != null)
			agroVenezuela.setChecked(getModelo().getAgroVenezuela());
		getBinder().addBinding(agroVenezuela, "checked",
				getNombreModelo() + ".agroVenezuela", null, null, "save", null,
				null, null, null);
		direccionFiscal.setValue(getModelo().getDireccion());
		getBinder().addBinding(direccionFiscal, "value",
				getNombreModelo() + ".direccion", null, null, "save", null,
				null, null, null);
		actualizarDatos();
		if (getModelo().getUnidadesproduccion() != null)
			unidadesproductivas.setModelo(getModelo().getUnidadesproduccion());
		try {
			if (!tipoJuridico.isChecked()) {
				cargarNatural();
			} else {
				cargarJuridico();
			}

			if (getModelo().isOrganizado() != null)
				organizado.setChecked(getModelo().isOrganizado());
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
		}

	}

	private void cargarJuridico() throws ExcDatosInvalidos {
		NegocioProductor negocio = NegocioProductor.getInstance();
		try {
			ProductorJuridico prod = (ProductorJuridico) negocio.getModelo();
			paginaWeb.setValue(prod.getPaginaWeb());
			representantes.setModeloSelect(prod.getRepresentantes());
		} catch (ClassCastException e) {
			cargarNatural();
		}
	}

	private void cargarNatural() throws ExcDatosInvalidos {
		NegocioProductor negocio = NegocioProductor.getInstance();
		try {
			ProductorNatural prod = (ProductorNatural) negocio.getModelo();
			prod = negocio.getProductorNatural();
			primerApellido.setValue(prod.getPrimerApellido());
			primerNombre.setValue(prod.getPrimerNombre());
			segundoApellido.setValue(prod.getSegundoApellido());
			segundoNombre.setValue(prod.getSegundoNombre());
			nacionalidad.setSeleccion(prod.getNacionalidad());
			estadoCivil.setSeleccion(prod.getEstadoCivil());
			gradoInstruccion.setSeleccion(prod.getGradoInstruccion());
			genero.setSeleccion(prod.getGenero());
			email.setValue(prod.getEmail());
			cargaFamiliar.setValue(prod.getCargaFamiliar());
			militante.setChecked(prod.isMilitante());
		} catch (ClassCastException e) {
			cargarJuridico();
		}
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
		identidad.setDisabled(true);
		estadoCivil.setDisabled(true);
		nacionalidad.setDisabled(true);
		genero.setDisabled(true);
		fechaInicio.setDisabled(true);
		fechaNacimiento.setDisabled(true);
		gradoInstruccion.setDisabled(true);
		email.setDisabled(true);
		representantes.setDisabled(true);
		telefonos.setDisable(true);

		organizado.setDisabled(true);
		financiamiento.setDisabled(true);
		direccionFiscal.setDisabled(true);
		militante.setDisabled(true);
		cargaFamiliar.setDisabled(true);
		tipo.setDisabled(true);
	}

	public void modoEdicion() {
		primerApellido.setDisabled(false);
		primerNombre.setDisabled(false);
		segundoApellido.setDisabled(false);
		segundoNombre.setDisabled(false);
		identidad.setDisabled(false);
		estadoCivil.setDisabled(false);
		nacionalidad.setDisabled(false);
		genero.setDisabled(false);
		fechaInicio.setDisabled(false);
		fechaNacimiento.setDisabled(false);
		gradoInstruccion.setDisabled(false);
		email.setDisabled(false);
		representantes.setDisabled(false);
		telefonos.setDisable(false);
		// organizacion.setDisabled(false);
		organizado.setDisabled(false);
		financiamiento.setDisabled(false);
		direccionFiscal.setDisabled(false);
		militante.setDisabled(false);
		cargaFamiliar.setDisabled(false);
		tipo.setDisabled(false);
	}

	public CompCombobox<TipoProductor> getTipo() {
		return tipo;
	}

	public void setTipo(CompCombobox<TipoProductor> tipo) {
		this.tipo = tipo;
	}

	public CompCombobox<Organizacion> getOrganizacion() {
		return organizacion;
	}

	public void setOrganizacion(CompCombobox<Organizacion> organizacion) {
		this.organizacion = organizacion;
	}

	public CompListaMultiple getFinanciamiento() {
		return financiamiento;
	}

	public void setFinanciamiento(CompListaMultiple financiamiento) {
		this.financiamiento = financiamiento;
	}

	public CompGrillaConBoton<Telefono> getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(CompGrillaConBoton<Telefono> telefonos) {
		this.telefonos = telefonos;
	}

	public Checkbox getMilitante() {
		return militante;
	}

	public void setMilitante(Checkbox militante) {
		this.militante = militante;
	}

	public CompCombobox<EstadoCivil> getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(CompCombobox<EstadoCivil> estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public CompCombobox<Nacionalidad> getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(CompCombobox<Nacionalidad> nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public CompCombobox<GradoInstruccion> getGradoInstruccion() {
		return gradoInstruccion;
	}

	public void setGradoInstruccion(
			CompCombobox<GradoInstruccion> gradoInstruccion) {
		this.gradoInstruccion = gradoInstruccion;
	}

	public CompRadioBotonLineal<Genero> getGenero() {
		return genero;
	}

	public void setGenero(CompRadioBotonLineal<Genero> genero) {
		this.genero = genero;
	}

	public Textbox getIdentidad() {
		return identidad;
	}

	public Textbox getRazonSocial() {
		return razonSocial;
	}

	public Textbox getEmail() {
		return email;
	}

	public Checkbox getOrganizado() {
		return organizado;
	}

	public Checkbox getTipoJuridico() {
		return tipoJuridico;
	}

	public Datebox getFechaNacimiento() {
		return fechaNacimiento;
	}

	public Datebox getFechaInicio() {
		return fechaInicio;
	}

	public CompCombobox<SectorAgricola> getSectorAgricola() {
		return sectorAgricola;
	}

	public Doublebox getHrasTotales() {
		return hrasTotales;
	}

	public CompGrupoDatos getProductor() {
		return productor;
	}

	public CompGrupoDatos getNatural() {
		return natural;
	}

	public CompGrupoDatos getJuridico() {
		return juridico;
	}

	public CompGrupoDatos getGenerico() {
		return generico;
	}

	public Label getCedulaRif() {
		return cedulaRif;
	}

	public Label getNacimientoCreacion() {
		return nacimientoCreacion;
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

	public Intbox getCargaFamiliar() {
		return cargaFamiliar;
	}

	public List<SectorAgricola> getSectoresAgricolas() {
		return sectoresAgricolas;
	}

	public List<EstadoCivil> getEstadosCiviles() {
		return estadosCiviles;
	}

	public List<GradoInstruccion> getGradosInstuccion() {
		return gradosInstuccion;
	}

	public CompGrupoDatos getCedulaG() {
		return cedulaG;
	}

	public Textbox getDireccionFiscal() {
		return direccionFiscal;
	}

	private List<CompEncabezado> getEncabezadoSector() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Sector Agricola", 120);
		titulo.setMetodoBinder("getStrSectorAgricola");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Ubicacion", 400);
		titulo.setMetodoBinder("getStrUbicacion");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("nombre", 120);
		titulo.setMetodoBinder("getNombre");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> encabezadosTelefonos() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Codigo", 60);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getCodigoArea");
		titulo.setMetodoModelo(".codigoArea");

		encabezado.add(titulo);
		titulo = new CompEncabezado("Numero", 70);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNumero");
		titulo.setMetodoModelo(".numero");
		encabezado.add(titulo);
		return encabezado;
	}

	private List<CompEncabezado> encabezadosRepresentantes() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Cedula", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCedula");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombres", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombres");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Apellidos", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getApellidos");
		encabezado.add(titulo);
		return encabezado;
	}

	private ArrayList<Component> cargarListaTelefonos() {
		ArrayList<Component> lista = new ArrayList<Component>();

		CompCombobox<CodigoArea> tipo = new CompCombobox<CodigoArea>();
		Textbox numero = new Textbox();
		numero.setConstraint("/[0-9]*/: Solo Numeros validos");
		tipo.setWidth("50px");
		tipo.setReadonly(true);
		numero.setWidth("60px");
		numero.setMaxlength(8);
		tipo.setModelo(codigosTelefonos);

		lista.add(tipo);
		lista.add(numero);
		return lista;
	}

	public void actualizarDatos() {
		try {
			TipoProductor tipo = getTipo().getSeleccion();
			getTipoJuridico().setChecked(tipo.isJuridico());
			if (!getCedulaG().isVisible()) {
				getCedulaG().setVisible(true);
				getGenerico().setVisible(true);
			}
			if (tipo.isJuridico()) {
				razonSocial.setVisible(true);
				razonSocialLbl.setVisible(true);
				paginaLbl.setVisible(true);
				paginaWeb.setVisible(true);
				emailLbl.setVisible(false);
				email.setVisible(false);
				nombres.setVisible(false);
				getJuridico().setVisible(true);
				getCedulaRif().setValue("RIF:");
				getNacimientoCreacion().setValue("Fecha Creación:");

			} else {
				razonSocial.setVisible(false);
				razonSocialLbl.setVisible(false);
				paginaLbl.setVisible(false);
				paginaWeb.setVisible(false);
				emailLbl.setVisible(true);
				email.setVisible(true);

				getJuridico().setVisible(false);
				nombres.setVisible(true);
				getCedulaRif().setValue("Cedula:");
				getNacimientoCreacion().setValue("FechaNacimiento:");
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
		}

	}

	public Button getBotonRepresentante() {
		return representantes.getBotonCrear();
	}

	public CompListaBusca<Representante> getRepresentantes() {
		return representantes;
	}

}
