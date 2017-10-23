package cpc.demeter.vista.ministerio;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Button;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.basico.SectorAgricola;
import cpc.modelo.demeter.basico.TipoVerificacionSuelo;
import cpc.modelo.ministerio.basico.TipoRiego;
import cpc.modelo.ministerio.basico.TipoSuelo;
import cpc.modelo.ministerio.basico.TipoTenenciaTierra;
import cpc.modelo.ministerio.basico.TipoUsoTierra;
import cpc.modelo.ministerio.basico.TipoVialidad;
import cpc.modelo.ministerio.dimension.UbicacionDireccion;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.modelo.ministerio.gestion.UnidadProductiva;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompGrillaConBoton;
import cpc.zk.componente.ui.ComponentesAutomaticos;
import cpc.zk.componente.ventanas.CompGrupoBusqueda;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cpc.modelo.ministerio.gestion.UnidadProductivaTipoRiego;

public class UiUnidadProductiva extends CompVentanaBase<UnidadProductiva> {

	private static final long serialVersionUID = 6799050286658762453L;
	private CompGrupoDatos gbGeneral;
	private Label codigo;
	private Textbox nombre;
	private Doublebox txtDistanciaAEmprea, txtSupActualAprovechable,
			txtSupPotencialAprovechable, txtSupBajoReservaForestal;
	private Intbox txtNroLotes;
	private Label estadoLbl, parroquiaLbl;

	private CompCombobox<TipoUsoTierra> tipoUsoTierra;
	private CompCombobox<SectorAgricola> sectorAgricola;
	private CompCombobox<TipoSuelo> tipoSuelo;
	private CompCombobox<TipoRiego> tipoRiego;
	private CompCombobox<TipoVialidad> tipoVialidad;
	private CompCombobox<TipoTenenciaTierra> tipoTenenciaTierra;
	private CompCombobox<TipoVerificacionSuelo> cmbTipoVerificacionSuelo;

	private CompGrupoBusqueda<UbicacionDireccion> ubicacionFisica;

	private CompGrupoBusqueda<Productor> productor;

	private List<Productor> lProductores;
	private List<UbicacionDireccion> direcciones;
	private List<TipoUsoTierra> tiposUsosTierra;

	private List<TipoRiego> tiposRiego;
	private List<TipoVialidad> tiposVialidades;
	private List<TipoSuelo> tiposSuelos;
	private List<TipoTenenciaTierra> tiposTenenciasTierra;
	private List<SectorAgricola> sectoresAgricolas;
	private List<TipoVerificacionSuelo> ltipoVerifSuelo;

	private CompGrillaConBoton<UnidadProductivaTipoRiego> lTiposRiego;

	public UiUnidadProductiva(String titulo, int ancho,
			List<UbicacionDireccion> direcciones,
			List<TipoUsoTierra> tiposUsosTierra, List<TipoSuelo> tiposSuelos,
			List<TipoRiego> tiposRiego, List<TipoVialidad> tiposVialidades,
			List<TipoTenenciaTierra> tiposTenenciasTierra,
			List<Productor> lProductores,
			List<SectorAgricola> sectoresAgricolas,
			List<TipoVerificacionSuelo> lTipoVerifSuelo)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.direcciones = direcciones;
		this.tiposUsosTierra = tiposUsosTierra;
		this.tiposSuelos = tiposSuelos;
		this.tiposRiego = tiposRiego;
		this.tiposVialidades = tiposVialidades;
		this.tiposTenenciasTierra = tiposTenenciasTierra;
		this.lProductores = lProductores;
		this.sectoresAgricolas = sectoresAgricolas;
		this.ltipoVerifSuelo = lTipoVerifSuelo;
	}

	public UiUnidadProductiva(String titulo) throws ExcDatosInvalidos {
		super(titulo);
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos Generales", 4);

		nombre = new Textbox();
		txtDistanciaAEmprea = new Doublebox();
		txtSupActualAprovechable = new Doublebox();
		txtSupPotencialAprovechable = new Doublebox();
		txtSupBajoReservaForestal = new Doublebox();
		txtNroLotes = new Intbox();

		codigo = new Label();
		estadoLbl = new Label();
		parroquiaLbl = new Label();

		tipoUsoTierra = new CompCombobox<TipoUsoTierra>();
		tipoSuelo = new CompCombobox<TipoSuelo>();
		tipoRiego = new CompCombobox<TipoRiego>();
		tipoVialidad = new CompCombobox<TipoVialidad>();
		tipoTenenciaTierra = new CompCombobox<TipoTenenciaTierra>();
		cmbTipoVerificacionSuelo = new CompCombobox<TipoVerificacionSuelo>();
		sectorAgricola = new CompCombobox<SectorAgricola>();
		lTiposRiego = new CompGrillaConBoton<UnidadProductivaTipoRiego>(
				cargarListaTipos(), getModelo().getTiposRiego(),
				encabezadoTiposRiego(), getControlador());
		try {
			ubicacionFisica = new CompGrupoBusqueda<UbicacionDireccion>(
					"Ubicacion Fisica", getEncabezadoSector(), getModelo()
							.getUbicacion(), ComponentesAutomaticos.LABEL);
			ubicacionFisica.setNuevo(new UbicacionDireccion());
			ubicacionFisica.setModeloCatalogo(direcciones);
			ubicacionFisica.setAnchoValores(200);
			ubicacionFisica.setAnchoCatalogo(500);

			productor = new CompGrupoBusqueda<Productor>("Productor",
					encabezadosProductores(), getModelo().getProductor(),
					ComponentesAutomaticos.TEXTBOX);
			productor.setNuevo(new Productor());
			productor.setModeloCatalogo(lProductores);
			productor.setAnchoValores(200);
			productor.setAnchoCatalogo(500);
			productor.setVisibleCrear(false);
			productor.setVisibleEditar(false);
			ubicacionFisica.setVisibleEditar(false);
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tipoUsoTierra.setModelo(tiposUsosTierra);
		tipoSuelo.setModelo(tiposSuelos);
		tipoRiego.setModelo(tiposRiego);
		sectorAgricola.setModelo(sectoresAgricolas);
		tipoVialidad.setModelo(tiposVialidades);
		tipoTenenciaTierra.setModelo(tiposTenenciasTierra);
		cmbTipoVerificacionSuelo.setModelo(this.ltipoVerifSuelo);

		tipoUsoTierra.setReadonly(true);
		tipoSuelo.setReadonly(true);
		tipoRiego.setReadonly(true);
		tipoVialidad.setReadonly(true);
		tipoTenenciaTierra.setReadonly(true);
		sectorAgricola.setReadonly(true);
		cmbTipoVerificacionSuelo.setReadonly(true);

		nombre.setWidth("200px");
		tipoSuelo.setWidth("185px");
		tipoUsoTierra.setWidth("185px");
		tipoVialidad.setWidth("185px");

		lTiposRiego.setNuevo(new UnidadProductivaTipoRiego());
		lTiposRiego.setWidthGrid(220);
		lTiposRiego.setStyle("border: 1px solid #D2EDD7");
		lTiposRiego.getBoton().setLabel("Agregar");

		txtDistanciaAEmprea.setConstraint("no negative");
		txtSupActualAprovechable.setConstraint("no negative");
		txtSupBajoReservaForestal.setConstraint("no negative");
		txtSupPotencialAprovechable.setConstraint("no negative");

		txtNroLotes.setConstraint("no negative");

	}

	public void dibujar() {
		ubicacionFisica.dibujar();
		this.appendChild(ubicacionFisica);
		productor.dibujar();
		this.appendChild(productor);
		gbGeneral.setAnchoColumna(0, 250);

		lTiposRiego.dibujar();

		gbGeneral.addComponente("Nombre de la Unidad:", nombre);
		gbGeneral.addComponente("Sector Agricola:", sectorAgricola);
		gbGeneral.addComponente("Tipo Suelo:", tipoSuelo);
		gbGeneral.addComponente("Tipo Verificacion Suelo:",
				cmbTipoVerificacionSuelo);
		gbGeneral.addComponente("Tipo Uso:", tipoUsoTierra);
		gbGeneral.addComponente("Tipo Tenencia Tierra:", tipoTenenciaTierra);
		gbGeneral.addComponente("Tipo Vialidad:", tipoVialidad);
		gbGeneral.addComponente("Dist Respecto a Empresa (Km):",
				txtDistanciaAEmprea);
		gbGeneral.addComponente("Superf Aprovechable Actual(Has):",
				txtSupActualAprovechable);
		gbGeneral.addComponente("Superf Aprovechable Potenciales(Has):",
				txtSupPotencialAprovechable);
		gbGeneral.addComponente("Superf Bajo Reserva Forestal (Has):",
				txtSupBajoReservaForestal);
		gbGeneral.addComponente("Numero Lotes:", txtNroLotes);
		gbGeneral.addComponente("Tipo de Riego:", lTiposRiego);
		gbGeneral.dibujar(this);
		addBotonera();
	}

	public void cargarValores(UnidadProductiva arg0) throws ExcDatosInvalidos {
		if (getModelo().getId() != null)
			codigo.setValue("" + getModelo().getId().toString());

		if (getModelo().getNombre() != null)
			nombre.setValue(getModelo().getNombre());
		getBinder().addBinding(nombre, "value", getNombreModelo() + ".nombre",
				null, null, "save", null, null, null, null);

		if (getModelo().getSectorAgricola() != null)
			sectorAgricola.setSeleccion(getModelo().getSectorAgricola());
		getBinder().addBinding(sectorAgricola, "seleccion",
				getNombreModelo() + ".sectorAgricola", null, null, "save",
				null, null, null, null);

		if (getModelo().getTipoUsoTierra() != null)
			tipoUsoTierra.setSeleccion(getModelo().getTipoUsoTierra());
		getBinder().addBinding(tipoUsoTierra, "seleccion",
				getNombreModelo() + ".tipoUsoTierra", null, null, "save", null,
				null, null, null);

		if (getModelo().getCondicionTenenciaTierra() != null)
			tipoTenenciaTierra.setSeleccion(getModelo()
					.getCondicionTenenciaTierra());
		getBinder().addBinding(tipoTenenciaTierra, "seleccion",
				getNombreModelo() + ".condicionTenenciaTierra", null, null,
				"save", null, null, null, null);

		if (getModelo().getTipoSuelo() != null)
			tipoSuelo.setSeleccion(getModelo().getTipoSuelo());
		getBinder().addBinding(tipoSuelo, "seleccion",
				getNombreModelo() + ".tipoSuelo", null, null, "save", null,
				null, null, null);

		if (getModelo().getTipoVialidad() != null)
			tipoVialidad.setSeleccion(getModelo().getTipoVialidad());
		getBinder().addBinding(tipoVialidad, "seleccion",
				getNombreModelo() + ".tipoVialidad", null, null, "save", null,
				null, null, null);

		getBinder().addBinding(ubicacionFisica, "modelo",
				getNombreModelo() + ".ubicacion", null, null, "save", null,
				null, null, null);
		getBinder().addBinding(productor, "modelo",
				getNombreModelo() + ".productor", null, null, "save", null,
				null, null, null);

		try {
			ubicacionFisica.addListener(getControlador());
			ubicacionFisica.actualizarValores();
			productor.addListener(getControlador());
			productor.actualizarValores();
		} catch (NullPointerException e) {
		} catch (ExcAccesoInvalido e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		txtDistanciaAEmprea.setValue(getModelo().getDistanciaEmpresa());
		getBinder().addBinding(txtDistanciaAEmprea, "value",
				getNombreModelo() + ".distanciaEmpresa", null, null, "save",
				null, null, null, null);

		txtNroLotes.setValue(getModelo().getNumeroDeLotes());
		getBinder().addBinding(txtNroLotes, "value",
				getNombreModelo() + ".numeroDeLotes", null, null, "save", null,
				null, null, null);

		txtSupActualAprovechable.setValue(getModelo()
				.getSuperficieActualmenteAprovechable());
		getBinder().addBinding(txtSupActualAprovechable, "value",
				getNombreModelo() + ".superficieActualmenteAprovechable", null,
				null, "save", null, null, null, null);

		txtSupBajoReservaForestal.setValue(getModelo()
				.getSuperficieBajoReservaForestal());
		getBinder().addBinding(txtSupBajoReservaForestal, "value",
				getNombreModelo() + ".superficieBajoReservaForestal", null,
				null, "save", null, null, null, null);

		txtSupPotencialAprovechable.setValue(getModelo()
				.getSuperficiePotencialmenteAprovechable());
		getBinder().addBinding(txtSupPotencialAprovechable, "value",
				getNombreModelo() + ".superficiePotencialmenteAprovechable",
				null, null, "save", null, null, null, null);

		if (getModelo().getTipoVerificacionSuelo() != null)
			cmbTipoVerificacionSuelo.setSeleccion(getModelo()
					.getTipoVerificacionSuelo());
		getBinder().addBinding(cmbTipoVerificacionSuelo, "seleccion",
				getNombreModelo() + ".tipoVerificacionSuelo", null, null,
				"save", null, null, null, null);

	}

	private List<CompEncabezado> getEncabezadoSector() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("N° Codigo", 50);
		titulo.setMetodoBinder("getStrCodigo");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Documento", 100);
		titulo.setMetodoBinder("getDocumentoLegal");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Descripcion", 250);
		titulo.setMetodoBinder("getDescripcion");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Sector", 150);
		titulo.setMetodoBinder("getStrSector");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Parroquia", 150);
		titulo.setMetodoBinder("getStrParroquia");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Municipio", 150);
		titulo.setMetodoBinder("getStrMunicipio");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 150);
		titulo.setMetodoBinder("getStrEstado");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Superf. Total", 150);
		titulo.setMetodoBinder("getSuperficiesString");
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
		productor.setModoEdicion(false);
		ubicacionFisica.setModoEdicion(false);
		nombre.setDisabled(true);
		sectorAgricola.setDisabled(true);
		tipoSuelo.setDisabled(true);
		cmbTipoVerificacionSuelo.setDisabled(true);
		tipoUsoTierra.setDisabled(true);
		tipoTenenciaTierra.setDisabled(true);
		tipoVialidad.setDisabled(true);
		txtDistanciaAEmprea.setDisabled(true);
		txtSupActualAprovechable.setDisabled(true);
		txtSupPotencialAprovechable.setDisabled(true);
		txtSupBajoReservaForestal.setDisabled(true);
		txtNroLotes.setDisabled(true);
		lTiposRiego.setDisabled(true);
	}

	public void modoEdicion() {
		productor.setModoEdicion(true);
		ubicacionFisica.setModoEdicion(true);
		nombre.setDisabled(false);
		sectorAgricola.setDisabled(false);
		tipoSuelo.setDisabled(false);
		cmbTipoVerificacionSuelo.setDisabled(false);
		tipoUsoTierra.setDisabled(false);
		tipoTenenciaTierra.setDisabled(false);
		tipoVialidad.setDisabled(false);
		txtDistanciaAEmprea.setDisabled(false);
		txtSupActualAprovechable.setDisabled(false);
		txtSupPotencialAprovechable.setDisabled(false);
		txtSupBajoReservaForestal.setDisabled(false);
		txtNroLotes.setDisabled(false);
		lTiposRiego.setDisabled(false);

	}

	private List<CompEncabezado> encabezadosProductores() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Tipo productor", 110);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrTipo");

		encabezado.add(titulo);
		titulo = new CompEncabezado("RIF/Cedula", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getIdentidadLegal");

		encabezado.add(titulo);
		titulo = new CompEncabezado("Razon Social/Nombres", 150);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNombres");

		encabezado.add(titulo);
		return encabezado;
	}

	private ArrayList<Component> cargarListaTipos() {
		ArrayList<Component> lista = new ArrayList<Component>();
		CompCombobox<TipoRiego> cmbTiposRiego = new CompCombobox<TipoRiego>();
		cmbTiposRiego.setModelo(this.tiposRiego);
		cmbTiposRiego.setWidth("100px");
		cmbTiposRiego.setReadonly(true);

		Doublebox dblSuperficie = new Doublebox();
		dblSuperficie.setWidth("60px");
		dblSuperficie.setConstraint("no negative,no zero,no empty");

		lista.add(cmbTiposRiego);
		lista.add(dblSuperficie);
		return lista;
	}

	private List<CompEncabezado> encabezadoTiposRiego() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Tipo", 110);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getTipoRiego");
		titulo.setMetodoModelo(".tipoRiego");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Superficie", 70);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getSuperficie");
		titulo.setMetodoModelo(".superficie");
		encabezado.add(titulo);

		return encabezado;
	}

	public CompGrupoBusqueda<Productor> getProductor() {
		return productor;
	}

	public Label getCodigo() {
		return codigo;
	}

	public Textbox getNombre() {
		return nombre;
	}

	public Label getEstadoLbl() {
		return estadoLbl;
	}

	public Label getParroquiaLbl() {
		return parroquiaLbl;
	}

	public CompCombobox<TipoUsoTierra> getTipoUsoTierra() {
		return tipoUsoTierra;
	}

	public CompCombobox<TipoSuelo> getTipoSuelo() {
		return tipoSuelo;
	}

	public CompCombobox<TipoRiego> getTipoRiego() {
		return tipoRiego;
	}

	public CompCombobox<TipoVialidad> getTipoVialidad() {
		return tipoVialidad;
	}

	public CompCombobox<TipoTenenciaTierra> getTipoTenenciaTierra() {
		return tipoTenenciaTierra;
	}

	public CompGrupoBusqueda<UbicacionDireccion> getUbicacionFisica() {
		return ubicacionFisica;
	}

	public Button getCrearUbicacionFisica() {
		return ubicacionFisica.getCrear();
	}

	public Button getEditUbicacionFisica() {
		return ubicacionFisica.getEditar();
	}

	public Button getCrearProductor() {
		return productor.getCrear();
	}

	public Button getEditProductor() {
		return productor.getEditar();
	}

	public Doublebox getTxtDistanciaAEmprea() {
		return txtDistanciaAEmprea;
	}

	public void setTxtDistanciaAEmprea(Doublebox txtDistanciaAEmprea) {
		this.txtDistanciaAEmprea = txtDistanciaAEmprea;
	}

	public Doublebox getTxtSupActualAprovechable() {
		return txtSupActualAprovechable;
	}

	public void setTxtSupActualAprovechable(Doublebox txtSupActualAprovechable) {
		this.txtSupActualAprovechable = txtSupActualAprovechable;
	}

	public Doublebox getTxtSupPotencialAprovechable() {
		return txtSupPotencialAprovechable;
	}

	public void setTxtSupPotencialAprovechable(
			Doublebox txtSupPotencialAprovechable) {
		this.txtSupPotencialAprovechable = txtSupPotencialAprovechable;
	}

	public Doublebox getTxtSupBajoReservaForestal() {
		return txtSupBajoReservaForestal;
	}

	public void setTxtSupBajoReservaForestal(Doublebox txtSupBajoReservaForestal) {
		this.txtSupBajoReservaForestal = txtSupBajoReservaForestal;
	}

	public Intbox getTxtNroLotes() {
		return txtNroLotes;
	}

	public void setTxtNroLotes(Intbox txtNroLotes) {
		this.txtNroLotes = txtNroLotes;
	}

	public CompCombobox<TipoVerificacionSuelo> getCmbTipoVerificacionSuelo() {
		return cmbTipoVerificacionSuelo;
	}

	public void setCmbTipoVerificacionSuelo(
			CompCombobox<TipoVerificacionSuelo> cmbTipoVerificacionSuelo) {
		this.cmbTipoVerificacionSuelo = cmbTipoVerificacionSuelo;
	}

	public CompGrillaConBoton<UnidadProductivaTipoRiego> getlTiposRiego() {
		return lTiposRiego;
	}

	public void setlTiposRiego(
			CompGrillaConBoton<UnidadProductivaTipoRiego> lTiposRiego) {
		this.lTiposRiego = lTiposRiego;
	}

}
