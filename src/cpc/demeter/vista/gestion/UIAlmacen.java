package cpc.demeter.vista.gestion;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.ministerio.dimension.UbicacionDireccion;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.modelo.sigesp.basico.TipoAlmacen;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UIAlmacen extends CompVentanaBase<Almacen> {

	private static final long serialVersionUID = -1632785579564735106L;
	private UnidadAdministrativa unidadAdministrativa;
	private List<TipoAlmacen> tipoAlmacenes;
	private List<UbicacionDireccion> ubicaciones;
	private List<Trabajador> trabajadores;
	private List<UnidadAdministrativa> unidades;
	private List<UnidadFuncional> unidadesEjecutoras;

	private Boolean unidadesGlobales;
	private int accion;

	private CompGrupoDatos gbGeneral;
	private Label lblNombre, lblDescripcion, lblLocalidad, lblTelefono,
			lblUnidadAdministrativa, lblTipoAlmacen, lblUbicacionGeografica,
			lbltrabajador, lblEstado, lblUnidadFuncional;
	private Textbox txtNombre, txtDescripcion, txtLocalidad, txtTelefono;
	private CompCombobox<TipoAlmacen> cmbTipoAlmacen;
	private CompBuscar<UbicacionDireccion> cmbUbicacionGeografica;
	private CompBuscar<Trabajador> cmbTrabajador;
	private Checkbox chkEstado;
	private CompBuscar<UnidadAdministrativa> cmbUnidadesAdministrativas;
	private CompCombobox<UnidadFuncional> unidadFuncional;

	public UIAlmacen(String titulo, int ancho,
			List<UbicacionDireccion> ubicaciones,
			List<Trabajador> trabajadores,
			UnidadAdministrativa unidadAdministrativa,
			List<TipoAlmacen> tipoAlmacenes, int accion,
			List<UnidadAdministrativa> unidades, Boolean unidadesGlobales,
			List<UnidadFuncional> unidadesFuncionales) {
		super(titulo, ancho);
		this.unidadAdministrativa = unidadAdministrativa;
		this.tipoAlmacenes = tipoAlmacenes;
		this.ubicaciones = ubicaciones;
		this.trabajadores = trabajadores;
		this.accion = accion;
		this.unidades = unidades;
		this.unidadesGlobales = unidadesGlobales;
		this.unidadesEjecutoras = unidadesFuncionales;
	}

	public UIAlmacen(String titulo, int ancho, int accion) {
		super(titulo, ancho);
		this.accion = accion;
	}

	public UIAlmacen(String titulo) {
		super(titulo);
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos Generales", 2);

		switch (accion) {
		case Accion.AGREGAR:
			txtNombre = new Textbox();
			txtDescripcion = new Textbox();
			txtLocalidad = new Textbox();
			txtTelefono = new Textbox();
			if (unidadesGlobales)
				cmbUnidadesAdministrativas = new CompBuscar<UnidadAdministrativa>(
						getEncabezadoUnidad(), 1);
			else
				lblUnidadAdministrativa = new Label();

			cmbTipoAlmacen = new CompCombobox<TipoAlmacen>();
			cmbUbicacionGeografica = new CompBuscar<UbicacionDireccion>(
					getEncabezadoUbicacionGeografica(), 3);
			cmbTrabajador = new CompBuscar<Trabajador>(
					getEncabezadoTrabajador(), 1);
			unidadFuncional = new CompCombobox<UnidadFuncional>();
			unidadFuncional.setReadonly(true);
			chkEstado = new Checkbox();
			break;
		case Accion.EDITAR:
			txtNombre = new Textbox();
			txtDescripcion = new Textbox();
			txtLocalidad = new Textbox();
			txtTelefono = new Textbox();
			if (unidadesGlobales)
				cmbUnidadesAdministrativas = new CompBuscar<UnidadAdministrativa>(
						getEncabezadoUnidad(), 1);
			else
				lblUnidadAdministrativa = new Label();

			cmbTipoAlmacen = new CompCombobox<TipoAlmacen>();
			cmbUbicacionGeografica = new CompBuscar<UbicacionDireccion>(
					getEncabezadoUbicacionGeografica(), 3);
			cmbTrabajador = new CompBuscar<Trabajador>(
					getEncabezadoTrabajador(), 1);
			unidadFuncional = new CompCombobox<UnidadFuncional>();
			unidadFuncional.setReadonly(true);
			chkEstado = new Checkbox();
			break;
		case Accion.CONSULTAR:
			lblNombre = new Label();
			lblDescripcion = new Label();
			lblLocalidad = new Label();
			lblTelefono = new Label();
			lblUnidadAdministrativa = new Label();
			lblTipoAlmacen = new Label();
			lblUbicacionGeografica = new Label();
			lbltrabajador = new Label();
			lblUnidadFuncional = new Label();
			lblEstado = new Label();
			break;
		}

	}

	public void dibujar() {
		switch (accion) {
		case Accion.AGREGAR:
			txtNombre.setWidth("500px");
			txtNombre.setRows(2);
			txtNombre.setMaxlength(254);
			txtDescripcion.setWidth("500px");
			txtDescripcion.setRows(2);
			txtDescripcion.setMaxlength(254);
			txtLocalidad.setWidth("400px");
			txtLocalidad.setMaxlength(254);
			txtTelefono.setMaxlength(12);

			gbGeneral.addComponente("Nombre :", txtNombre);
			gbGeneral.addComponente("Descripción :", txtDescripcion);
			gbGeneral.addComponente("Localidad :", txtLocalidad);
			gbGeneral.addComponente("Telefono :", txtTelefono);

			if (unidadesGlobales) {
				cmbUnidadesAdministrativas.setAncho(600);
				cmbUnidadesAdministrativas.setWidth("500px");
				cmbUnidadesAdministrativas.setModelo(unidades);
				gbGeneral.addComponente("Unidad Administrativa",
						cmbUnidadesAdministrativas);
			} else {
				lblUnidadAdministrativa.setWidth("350px");
				gbGeneral.addComponente("Unidad Administrativa",
						lblUnidadAdministrativa);
			}

			cmbTipoAlmacen.setModelo(tipoAlmacenes);
			cmbUbicacionGeografica.setWidth("450px");
			cmbUbicacionGeografica.setAncho(800);
			cmbUbicacionGeografica.setModelo(ubicaciones);
			cmbTrabajador.setWidth("350px");
			cmbTrabajador.setAncho(600);
			cmbTrabajador.setModelo(trabajadores);
			unidadFuncional.setModelo(unidadesEjecutoras);
			unidadFuncional.setWidth("300px");

			gbGeneral.addComponente("Tipo Almacen ", cmbTipoAlmacen);
			gbGeneral.addComponente("Ubicación", cmbUbicacionGeografica);
			gbGeneral.addComponente("Responsable", cmbTrabajador);
			gbGeneral.addComponente("Unidad Funcional: ", unidadFuncional);
			gbGeneral
					.addComponente(
							"Estatus (Marcar para Activarlo, Desmarcarlo para Inactivarlo) ",
							chkEstado);
			gbGeneral.dibujar(this);
			break;
		case Accion.EDITAR:
			txtNombre.setWidth("500px");
			txtNombre.setRows(2);
			txtNombre.setMaxlength(254);
			txtDescripcion.setWidth("500px");
			txtDescripcion.setRows(2);
			txtDescripcion.setMaxlength(254);
			txtLocalidad.setWidth("400px");
			txtLocalidad.setMaxlength(254);
			txtTelefono.setMaxlength(12);

			gbGeneral.addComponente("Nombre :", txtNombre);
			gbGeneral.addComponente("Descripción :", txtDescripcion);
			gbGeneral.addComponente("Localidad :", txtLocalidad);
			gbGeneral.addComponente("Telefono :", txtTelefono);

			if (unidadesGlobales) {
				cmbUnidadesAdministrativas.setAncho(600);
				cmbUnidadesAdministrativas.setWidth("500px");
				cmbUnidadesAdministrativas.setModelo(unidades);
				gbGeneral.addComponente("Unidad Administrativa",
						cmbUnidadesAdministrativas);
			} else {
				lblUnidadAdministrativa.setWidth("350px");
				gbGeneral.addComponente("Unidad Administrativa",
						lblUnidadAdministrativa);
			}

			cmbTipoAlmacen.setModelo(tipoAlmacenes);
			cmbUbicacionGeografica.setWidth("450px");
			cmbUbicacionGeografica.setAncho(800);
			cmbUbicacionGeografica.setModelo(ubicaciones);
			cmbTrabajador.setWidth("350px");
			cmbTrabajador.setAncho(600);
			cmbTrabajador.setModelo(trabajadores);
			unidadFuncional.setModelo(unidadesEjecutoras);
			unidadFuncional.setWidth("300px");

			gbGeneral.addComponente("Tipo Almacen ", cmbTipoAlmacen);
			gbGeneral.addComponente("Ubicación", cmbUbicacionGeografica);
			gbGeneral.addComponente("Responsable", cmbTrabajador);
			gbGeneral.addComponente("Unidad Funcional: ", unidadFuncional);
			gbGeneral
					.addComponente(
							"Estatus (Marcar para Activarlo, Desmarcarlo para Inactivarlo) ",
							chkEstado);
			gbGeneral.dibujar(this);
			break;
		case Accion.CONSULTAR:
			gbGeneral.addComponente("Nombre: ", lblNombre);
			gbGeneral.addComponente("Descripcion: ", lblDescripcion);
			gbGeneral.addComponente("Localidad: ", lblLocalidad);
			gbGeneral.addComponente("Teléfono: ", lblTelefono);
			gbGeneral.addComponente("Unidad Administrativa: ",
					lblUnidadAdministrativa);
			gbGeneral.addComponente("Tipo Almacen: ", lblTipoAlmacen);
			gbGeneral.addComponente("Ubicación Geográfica: ",
					lblUbicacionGeografica);
			gbGeneral.addComponente("Trabajador: ", lbltrabajador);
			gbGeneral.addComponente("Unidad Funcional: ", lblUnidadFuncional);
			gbGeneral.addComponente("Estatus del Almacen: ", lblEstado);
			gbGeneral.dibujar(this);
			break;
		}
		addBotonera();

	}

	public void cargarValores(Almacen arg0) throws ExcDatosInvalidos {
		switch (accion) {
		case Accion.AGREGAR:
			txtNombre.setValue(getModelo().getNombre());
			getBinder().addBinding(txtNombre, "value",
					getNombreModelo() + ".nombre", null, null, "save", null,
					null, null, null);

			txtDescripcion.setValue(getModelo().getDescripcion());
			getBinder().addBinding(txtDescripcion, "value",
					getNombreModelo() + ".descripcion", null, null, "save",
					null, null, null, null);

			txtLocalidad.setValue(getModelo().getLocalidad());
			getBinder().addBinding(txtLocalidad, "value",
					getNombreModelo() + ".localidad", null, null, "save", null,
					null, null, null);

			txtTelefono.setValue(getModelo().getTelefono());
			getBinder().addBinding(txtTelefono, "value",
					getNombreModelo() + ".telefono", null, null, "save", null,
					null, null, null);

			if (unidadesGlobales) {
				cmbUnidadesAdministrativas.setSeleccion(getModelo()
						.getUnidadAdministrativa());
				getBinder().addBinding(cmbUnidadesAdministrativas, "seleccion",
						getNombreModelo() + ".unidadAdministrativa", null,
						null, "save", null, null, null, null);
			} else {
				lblUnidadAdministrativa.setValue(unidadAdministrativa
						.getNombre());
			}

			cmbTipoAlmacen.setSeleccion(getModelo().getTipoAlmacen());
			getBinder().addBinding(cmbTipoAlmacen, "seleccion",
					getNombreModelo() + ".tipoAlmacen", null, null, "save",
					null, null, null, null);

			cmbUbicacionGeografica.setSeleccion(getModelo()
					.getUbicacionGeografica());
			getBinder().addBinding(cmbUbicacionGeografica, "seleccion",
					getNombreModelo() + ".ubicacionGeografica", null, null,
					"save", null, null, null, null);

			cmbTrabajador.setSeleccion(getModelo().getTrabajador());
			getBinder().addBinding(cmbTrabajador, "seleccion",
					getNombreModelo() + ".trabajador", null, null, "save",
					null, null, null, null);

			unidadFuncional.setSeleccion(getModelo().getUnidadFuncional());
			getBinder().addBinding(unidadFuncional, "seleccion",
					getNombreModelo() + ".unidadFuncional", null, null, "save",
					null, null, null, null);

			chkEstado.setChecked(getModelo().isEstadoAlmacen());
			getBinder().addBinding(chkEstado, "checked",
					getNombreModelo() + ".estadoAlmacen", null, null, "save",
					null, null, null, null);

			break;
		case Accion.EDITAR:
			txtNombre.setValue(getModelo().getNombre());
			getBinder().addBinding(txtNombre, "value",
					getNombreModelo() + ".nombre", null, null, "save", null,
					null, null, null);

			txtDescripcion.setValue(getModelo().getDescripcion());
			getBinder().addBinding(txtDescripcion, "value",
					getNombreModelo() + ".descripcion", null, null, "save",
					null, null, null, null);

			txtLocalidad.setValue(getModelo().getLocalidad());
			getBinder().addBinding(txtLocalidad, "value",
					getNombreModelo() + ".localidad", null, null, "save", null,
					null, null, null);

			txtTelefono.setValue(getModelo().getTelefono());
			getBinder().addBinding(txtTelefono, "value",
					getNombreModelo() + ".telefono", null, null, "save", null,
					null, null, null);

			if (unidadesGlobales) {
				cmbUnidadesAdministrativas.setSeleccion(getModelo()
						.getUnidadAdministrativa());
				getBinder().addBinding(cmbUnidadesAdministrativas, "seleccion",
						getNombreModelo() + ".unidadAdministrativa", null,
						null, "save", null, null, null, null);
			} else {
				lblUnidadAdministrativa.setValue(unidadAdministrativa
						.getNombre());
			}

			cmbTipoAlmacen.setSeleccion(getModelo().getTipoAlmacen());
			getBinder().addBinding(cmbTipoAlmacen, "seleccion",
					getNombreModelo() + ".tipoAlmacen", null, null, "save",
					null, null, null, null);

			cmbUbicacionGeografica.setSeleccion(getModelo()
					.getUbicacionGeografica());
			getBinder().addBinding(cmbUbicacionGeografica, "seleccion",
					getNombreModelo() + ".ubicacionGeografica", null, null,
					"save", null, null, null, null);

			cmbTrabajador.setSeleccion(getModelo().getTrabajador());
			getBinder().addBinding(cmbTrabajador, "seleccion",
					getNombreModelo() + ".trabajador", null, null, "save",
					null, null, null, null);

			unidadFuncional.setSeleccion(getModelo().getUnidadFuncional());
			getBinder().addBinding(unidadFuncional, "seleccion",
					getNombreModelo() + ".unidadFuncional", null, null, "save",
					null, null, null, null);

			chkEstado.setChecked(getModelo().isEstadoAlmacen());
			getBinder().addBinding(chkEstado, "checked",
					getNombreModelo() + ".estadoAlmacen", null, null, "save",
					null, null, null, null);

			break;
		case Accion.CONSULTAR:
			lblNombre.setValue(getModelo().getNombre());
			lblDescripcion.setValue(getModelo().getDescripcion());
			lblLocalidad.setValue(getModelo().getLocalidad());
			lblTelefono.setValue(getModelo().getTelefono());
			lblUnidadAdministrativa.setValue(getModelo()
					.getUnidadAdministrativa().getNombre());
			lblTipoAlmacen.setValue(getModelo().getNombreTipoAlmacen());
			lblUbicacionGeografica.setValue(getModelo()
					.getUbicacionGeografica().toString());
			lbltrabajador.setValue(getModelo().getTrabajador().getNombre()
					+ " " + getModelo().getTrabajador().getApellido());
			lblUnidadFuncional.setValue(getModelo().getNombreUnidadFuncional());
			if (getModelo().isEstadoAlmacen())
				lblEstado.setValue("Activo");
			else
				lblEstado.setValue("Inactivo");
			break;
		}

	}

	private List<CompEncabezado> getEncabezadoUnidad() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Codigo. ", 150);
		titulo.setMetodoBinder("getSede");
		lista.add(titulo);

		titulo = new CompEncabezado("Nombre. ", 300);
		titulo.setMetodoBinder("getNombre");
		lista.add(titulo);

		return lista;
	}

	public List<CompEncabezado> getEncabezadoUbicacionGeografica() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

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

	public List<CompEncabezado> getEncabezadoTrabajador() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Cedula", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroCedula");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombres", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Apellidos", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getApellido");
		encabezado.add(titulo);

		return encabezado;
	}

	public void modoVer() {
		txtNombre.setDisabled(true);
		txtDescripcion.setDisabled(true);
		txtLocalidad.setDisabled(true);
		txtTelefono.setDisabled(true);

		cmbTipoAlmacen.setDisabled(true);
		cmbUbicacionGeografica.setDisabled(true);
		cmbTrabajador.setDisabled(true);

		chkEstado.setDisabled(true);
	}

	public CompGrupoDatos getGbGeneral() {
		return gbGeneral;
	}

	public void setGbGeneral(CompGrupoDatos gbGeneral) {
		this.gbGeneral = gbGeneral;
	}

	public Label getLblUnidadAdministrativa() {
		return lblUnidadAdministrativa;
	}

	public void setLblUnidadAdministrativa(Label LblUnidadAdministrativa) {
		this.lblUnidadAdministrativa = LblUnidadAdministrativa;
	}

	public Textbox getTxtNombre() {
		return txtNombre;
	}

	public void setTxtNombre(Textbox txtNombre) {
		this.txtNombre = txtNombre;
	}

	public Textbox getTxtDescripcion() {
		return txtDescripcion;
	}

	public void setTxtDescripcion(Textbox txtDescripcion) {
		this.txtDescripcion = txtDescripcion;
	}

	public Textbox getTxtLocalidad() {
		return txtLocalidad;
	}

	public void setTxtLocalidad(Textbox txtLocalidad) {
		this.txtLocalidad = txtLocalidad;
	}

	public Textbox getTxtTelefono() {
		return txtTelefono;
	}

	public void setTxtTelefono(Textbox txtTelefono) {
		this.txtTelefono = txtTelefono;
	}

	public CompCombobox<TipoAlmacen> getCmbTipoAlmacen() {
		return cmbTipoAlmacen;
	}

	public void setCmbTipoAlmacen(CompCombobox<TipoAlmacen> cmbTipoAlmacen) {
		this.cmbTipoAlmacen = cmbTipoAlmacen;
	}

	public CompBuscar<UbicacionDireccion> getCmbUbicacionGeografica() {
		return cmbUbicacionGeografica;
	}

	public void setCmbUbicacionGeografica(
			CompBuscar<UbicacionDireccion> cmbUbicacionGeografica) {
		this.cmbUbicacionGeografica = cmbUbicacionGeografica;
	}

	public CompBuscar<Trabajador> getCmbTrabajador() {
		return cmbTrabajador;
	}

	public void setCmbTrabajador(CompBuscar<Trabajador> cmbTrabajador) {
		this.cmbTrabajador = cmbTrabajador;
	}

	public Checkbox getChkEstado() {
		return chkEstado;
	}

	public void setChkEstado(Checkbox chkEstado) {
		this.chkEstado = chkEstado;
	}

	public List<UnidadFuncional> getUnidadesEjecutoras() {
		return unidadesEjecutoras;
	}

	public void setUnidadesEjecutoras(List<UnidadFuncional> unidadesEjecutoras) {
		this.unidadesEjecutoras = unidadesEjecutoras;
	}

	public CompCombobox<UnidadFuncional> getUnidadFuncional() {
		return unidadFuncional;
	}

	public void setUnidadFuncional(CompCombobox<UnidadFuncional> unidadFuncional) {
		this.unidadFuncional = unidadFuncional;
	}

	public CompBuscar<UnidadAdministrativa> getCmbUnidadesAdministrativas() {
		return cmbUnidadesAdministrativas;
	}

	public void setCmbUnidadesAdministrativas(
			CompBuscar<UnidadAdministrativa> cmbUnidadesAdministrativas) {
		this.cmbUnidadesAdministrativas = cmbUnidadesAdministrativas;
	}

}
