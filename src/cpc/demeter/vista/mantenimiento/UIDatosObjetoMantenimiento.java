package cpc.demeter.vista.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;
import cpc.modelo.demeter.mantenimiento.EstadoFuncional;
import cpc.modelo.demeter.mantenimiento.Fabricante;
import cpc.modelo.demeter.mantenimiento.Lote;
import cpc.modelo.demeter.mantenimiento.ObjetoMantenimiento;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.modelo.sigesp.basico.Activo;
import cpc.modelo.sigesp.basico.Categoria;
import cpc.modelo.sigesp.basico.Marca;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.modelo.sigesp.basico.Tipo;
import cpc.negocio.demeter.mantenimiento.NegocioObjetoMantenimiento;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompGrillaConBoton;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.utilidades.Color;

public class UIDatosObjetoMantenimiento extends CompGrupoDatos {

	private static final long serialVersionUID = 995855838440816728L;
	private Checkbox chkPropio;
	private Textbox txtCodigo, txtDescripcion, txtSerial;
	private Spinner spnAnoFabricacion;

	private CompBuscar<Activo> catActivos;
	private CompCombobox<Categoria> cmbCategoria;
	private CompCombobox<Tipo> cmbTipo;
	private CompCombobox<Marca> cmbMarca;
	private CompCombobox<Modelo> cmbModelo;
	private CompCombobox<Fabricante> cmbFabricante;
	private CompCombobox<EstadoFuncional> cmbEstadoFunional;

	private CompCombobox<String> cmbColor;
	private CompBuscar<Cliente> catPropietarios;

	private CompGrillaConBoton<ObjetoMantenimiento> lstComponentes;

	private Div divColor;
	private Vbox contFoto;
	private CompCombobox<Lote> cmbLote;
	private Button btnCargarFoto;
	private NegocioObjetoMantenimiento negocioObjetoMantenimiento;
	private Window ventanaPadre;
	private ObjetoMantenimiento objetoMantenimiento;

	public UIDatosObjetoMantenimiento() {

	}

	public UIDatosObjetoMantenimiento(NegocioObjetoMantenimiento negocio,
			ObjetoMantenimiento objeto, Window ventanaPadre) {
		super("Datos Generales", 4);
		this.negocioObjetoMantenimiento = negocio;
		this.ventanaPadre = ventanaPadre;
		this.objetoMantenimiento = objeto;
		this.inicializar();
		this.dibujar();
	}

	public void inicializar() {

		chkPropio = new Checkbox();
		txtCodigo = new Textbox();
		txtDescripcion = new Textbox();
		txtSerial = new Textbox();

		cmbCategoria = new CompCombobox<Categoria>();
		cmbTipo = new CompCombobox<Tipo>();
		cmbMarca = new CompCombobox<Marca>();
		cmbModelo = new CompCombobox<Modelo>();
		cmbFabricante = new CompCombobox<Fabricante>();
		cmbColor = new CompCombobox<String>();
		cmbLote = new CompCombobox<Lote>();
		cmbEstadoFunional = new CompCombobox<EstadoFuncional>();
		divColor = new Div();
		btnCargarFoto = new Button("Foto o Imagen");
		contFoto = new Vbox();
		catActivos = new CompBuscar<Activo>(getEncabezadoActivos(), 0);
		catPropietarios = new CompBuscar<Cliente>(getEncabezadoPropietarios(),
				2);

		lstComponentes = new CompGrillaConBoton<ObjetoMantenimiento>(
				getListaComponentes(), objetoMantenimiento.getComponentes(),
				getEncabezadosComponente(), this);

		spnAnoFabricacion = new Spinner(1995);
		txtCodigo.setWidth("250px");
		txtDescripcion.setWidth("250px");
		txtDescripcion.setRows(3);

		catActivos.setWidth("275x");
		catPropietarios.setWidth("275x");

		cmbCategoria.setWidth("235px");
		cmbMarca.setWidth("235px");
		cmbFabricante.setWidth("235px");
		cmbColor.setWidth("235px");
		cmbLote.setWidth("235px");

		divColor.setWidth("20px");
		divColor.setHeight("20px");
		divColor.setStyle("border: 1px solid #94B09C; margin: 2px");

		contFoto.setWidth("64px");
		contFoto.setHeight("64px");
		contFoto.setStyle("border: 1px solid #94B09C; margin: 2px");

		cmbCategoria.setReadonly(true);
		catPropietarios.setDisabled(true);

		cmbColor.setReadonly(true);
		cmbFabricante.setReadonly(true);
		cmbEstadoFunional.setReadonly(true);
		cmbLote.setReadonly(true);
		cmbMarca.setReadonly(true);
		cmbModelo.setReadonly(true);
		cmbTipo.setReadonly(true);
		spnAnoFabricacion.setConstraint("no zero, no negative");

		catActivos.setDisabled(false);
		catActivos.setAncho(550);
		chkPropio.setChecked(true);

		lstComponentes.setNuevo(new ObjetoMantenimiento());
		lstComponentes.setWidthGrid(265);
		lstComponentes.setStyle("border: 1px solid #94B09C");

		lstComponentes.getBoton().setLabel("");

	}

	public void dibujar() {
		addComponente("Propio : ", chkPropio);
		addComponente("", new Label());
		addComponente("Codigo :", txtCodigo);
		addComponente("", new Label());
		addComponente("Nombre o Descripcion :", txtDescripcion);
		addComponente(btnCargarFoto);
		addComponente(contFoto);

		addComponente("Categoria :", cmbCategoria);
		addComponente("Tipo :", cmbTipo);
		addComponente("Marca :", cmbMarca);
		addComponente("Modelo :", cmbModelo);
		addComponente("Activo :", catActivos);
		addComponente("Serial :", txtSerial);

		addComponente("Fabricante :", cmbFabricante);
		addComponente("Año de Fabricacion :", spnAnoFabricacion);
		addComponente("Lote de Fabricacion :", cmbLote);
		addComponente("Estado Funcional :", cmbEstadoFunional);
		addComponente("Color Predominante :", cmbColor);
		addComponente("Color Referencial :", divColor);
		addComponente("Propietario :", catPropietarios);
		addComponente("", new Label());
		addComponente("Componentes :", lstComponentes);
		addComponente("", new Label());

		cmbColor.addEventListener(Events.ON_SELECT, this);
		cmbCategoria.addEventListener(Events.ON_SELECT, this);
		cmbMarca.addEventListener(Events.ON_SELECT, this);
		cmbModelo.addEventListener(Events.ON_SELECT, this);
		catActivos.getCatalogo().addEventListener(Events.ON_SELECT, this);
		catPropietarios.getCatalogo().addEventListener(Events.ON_SELECT, this);

		btnCargarFoto.addEventListener(Events.ON_CLICK, this);
		chkPropio.addEventListener(Events.ON_CHECK, this);

		setColores();
		setCategorias();
		setMarcas();
		setFabricantes();
		setEstadoFuncional();
		setLotes();
		setPropietarios();

		lstComponentes.dibujar();
		dibujar(this.ventanaPadre);

	}

	public void setLectura() {
		this.txtDescripcion.setReadonly(true);
	}

	private List<CompEncabezado> getEncabezadoActivos() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Denominación", 300);
		titulo.setMetodoBinder("getNombre");
		lista.add(titulo);

		titulo = new CompEncabezado("Serial", 150);
		titulo.setMetodoBinder("getSerial");
		lista.add(titulo);

		titulo = new CompEncabezado("Codigo", 120);
		titulo.setMetodoBinder("getCodigoActivo");
		lista.add(titulo);

		titulo = new CompEncabezado("Ejemplar", 120);
		titulo.setMetodoBinder("getIdEjemplarActivo");
		lista.add(titulo);

		titulo = new CompEncabezado("Marca", 140);
		titulo.setMetodoBinder("getDescripcionMarca");
		lista.add(titulo);

		titulo = new CompEncabezado("Modelo", 140);
		titulo.setMetodoBinder("getDescripcionModelo");
		lista.add(titulo);

		return lista;
	}

	private List<CompEncabezado> getEncabezadoPropietarios() {

		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Tipo", 150);
		titulo.setMetodoBinder("getStrTipo");
		lista.add(titulo);

		titulo = new CompEncabezado("Cedula/RIF ", 100);
		titulo.setMetodoBinder("getIdentidadLegal");
		lista.add(titulo);

		titulo = new CompEncabezado("Nombre / Razon Social", 300);
		titulo.setMetodoBinder("getNombres");
		lista.add(titulo);

		titulo = new CompEncabezado("Direccion", 300);
		titulo.setMetodoBinder("getDireccion");
		lista.add(titulo);

		return lista;
	}

	public Textbox getDescripcion() {
		return txtDescripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.txtDescripcion = descripcion;
	}

	private void setColores() {
		cmbColor.setModelo(new Color().getColores());
	}

	private void setCategorias() {
		try {
			cmbCategoria.setModelo(negocioObjetoMantenimiento.getCategorias());
		} catch (ExcFiltroExcepcion e) {
			try {
				Messagebox.show("ocurrio un Error al cargar las Categorias");
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void setMarcas() {
		try {
			cmbMarca.setModelo(negocioObjetoMantenimiento.getMarcas());
		} catch (ExcFiltroExcepcion e) {
			try {
				Messagebox.show("ocurrio un Error al cargar las Marcas");
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void setFabricantes() {
		try {
			cmbFabricante
					.setModelo(negocioObjetoMantenimiento.getFabricantes());
		} catch (ExcFiltroExcepcion e) {
			try {
				Messagebox.show("ocurrio un Error al cargar las Fabricantes");
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void setEstadoFuncional() {
		try {
			cmbEstadoFunional.setModelo(negocioObjetoMantenimiento
					.getEstadoFuncional());
		} catch (ExcFiltroExcepcion e) {
			try {
				Messagebox
						.show("ocurrio un Error al cargar los Estados Funcional");
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void setLotes() {
		try {
			cmbLote.setModelo(negocioObjetoMantenimiento.getLotes());
		} catch (ExcFiltroExcepcion e) {
			try {
				Messagebox.show("ocurrio un Error al cargar los Lotes");
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private ArrayList<Component> getListaComponentes() {

		ArrayList<Component> lista = new ArrayList<Component>();
		CompBuscar<ObjetoMantenimiento> catComponentes = new CompBuscar<ObjetoMantenimiento>(
				getEncabezadoComponentes(), 2);
		catComponentes.setWidth("215px");
		catComponentes.getCatalogo().setWidth("225px");
		catComponentes.setAncho(800);

		try {
			catComponentes.setModelo(negocioObjetoMantenimiento
					.getObjetosMantenimiento());
		} catch (ExcFiltroExcepcion e) {
			try {
				Messagebox.show("ocurrio un Error al cargar los Componentes");
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		lista.add(catComponentes);
		return lista;

	}

	private List<CompEncabezado> getEncabezadoComponentes() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Codigo", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCodigo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Descripcion", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Categoria", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCategoria");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getTipo");
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> getEncabezadosComponente() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Componente", 200);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getCodigo");
		titulo.setMetodoModelo(".codigo");
		encabezado.add(titulo);

		return encabezado;
	}

	@Override
	public void onEvent(Event event) throws Exception {

		super.onEvent(event);

		if (event.getTarget() == cmbColor)
			divColor.setStyle("border: 1px solid #94B09C; margin: 2px; background-color:#"
					+ new Color().getColorHexadecimal(cmbColor
							.getSelectedIndex()));

		if (event.getTarget() == cmbCategoria)
			cmbTipo.setModelo(negocioObjetoMantenimiento
					.getTiposPorCategoria(cmbCategoria.getSeleccion()));

		if (event.getTarget() == cmbMarca)
			cmbModelo.setModelo(negocioObjetoMantenimiento
					.getModelosPorMarca(cmbMarca.getSeleccion()));

		if (event.getTarget() == cmbModelo && chkPropio.isChecked())
			setCatalogoActivos(cmbModelo.getSeleccion(), true);

		if (event.getTarget() == catActivos.getCatalogo()) {
			setObjetoMantenimientoPorActivo();
		}

		if (event.getTarget() == btnCargarFoto)
			setUpload();

		if (event.getTarget() == chkPropio) {
			catPropietarios.setDisabled(chkPropio.isChecked());
			catActivos.setDisabled(!chkPropio.isChecked());
		}
	}

	public void setCatalogoActivos(Modelo modelo, boolean accesible) {
		this.catActivos.setDisabled(!accesible);

		this.catActivos.getCatalogo().clearSelection();
		this.catActivos.setValue(null);
		this.catActivos.setSeleccion(null);
		this.catActivos.setModelo(negocioObjetoMantenimiento
				.getActivosAlmacenados(modelo));
	}

	public void setPropietarios() {
		try {
			this.catPropietarios.setModelo(negocioObjetoMantenimiento
					.getClientes());
			this.catPropietarios.setAncho(550);
		} catch (ExcFiltroExcepcion e) {
			try {
				Messagebox.show("ocurrio un Error al cargar los Propietarios");
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void setObjetoMantenimientoPorActivo() {
		this.txtCodigo.setText(null);
		this.txtDescripcion.setText(null);
		this.txtSerial.setText(null);
		this.cmbCategoria.setValue(null);
		this.cmbTipo.setValue(null);
		this.cmbMarca.setValue(null);
		this.cmbModelo.setValue(null);

		if (catActivos.getSeleccion() != null) {
			this.txtCodigo.setText(catActivos.getSeleccion().getCodigoActivo()
					+ catActivos.getSeleccion().getIdEjemplarActivo());
			this.txtDescripcion.setText(catActivos.getSeleccion().getNombre()
					.trim()
					+ " "
					+ catActivos.getSeleccion().getMarca().getDescripcion()
							.trim()
					+ " "
					+ catActivos.getSeleccion().getModelo()
							.getDescripcionModelo().trim());
			this.txtSerial.setText(catActivos.getSeleccion().getSerial());

			this.cmbCategoria.setSeleccion(catActivos.getSeleccion()
					.getCategoria());
			try {
				this.cmbTipo.setModelo(negocioObjetoMantenimiento
						.getTiposPorCategoria(cmbCategoria.getSeleccion()));
			} catch (ExcFiltroExcepcion e) {
				e.printStackTrace();
			}
			this.cmbTipo.setSeleccion(this.catActivos.getSeleccion().getTipo());
			this.cmbMarca.setSeleccion(this.catActivos.getSeleccion()
					.getMarca());
			this.cmbModelo.setSeleccion(this.catActivos.getSeleccion()
					.getModelo());
		}
	}

	public void validar() {
		if (txtCodigo.getValue() == null || txtCodigo.getValue() == "")
			throw new WrongValueException(txtCodigo, "Indique un valor");
		if (txtDescripcion.getValue() == null
				|| txtDescripcion.getValue() == "")
			throw new WrongValueException(txtDescripcion, "Indique un valor");
		if (cmbCategoria.getSeleccion() == null)
			throw new WrongValueException(cmbCategoria, "Seleccione un valor");
		if (cmbTipo.getSeleccion() == null)
			throw new WrongValueException(cmbTipo, "Seleccione un valor");
		if (cmbMarca.getSeleccion() == null)
			throw new WrongValueException(cmbMarca, "Seleccione un valor");
		if (cmbModelo.getSeleccion() == null)
			throw new WrongValueException(cmbModelo, "Seleccione un valor");
		if (chkPropio.isChecked() && catActivos.getSeleccion() == null)
			throw new WrongValueException(catActivos,
					"Seleccione un valor o desmarque la Opcion 'Propio' ");
		if (txtSerial.getValue() == null || txtSerial.getValue() == "")
			throw new WrongValueException(txtSerial, "Indique un valor");
		if (cmbFabricante.getSeleccion() == null)
			throw new WrongValueException(cmbFabricante, "Seleccione un valor");
		if (cmbLote.getSeleccion() == null)
			throw new WrongValueException(cmbLote, "Seleccione un valor");
		if (cmbEstadoFunional.getSeleccion() == null)
			throw new WrongValueException(cmbEstadoFunional,
					"Seleccione un valor");
		if (cmbColor.getSelectedIndex() < 0)
			throw new WrongValueException(cmbColor, "Seleccione un valor");
		if (!chkPropio.isChecked() && catPropietarios.getSeleccion() == null)
			throw new WrongValueException(catPropietarios,
					"Seleccione un valor o marque la Opcion 'Propio' ");

	}

	private void setUpload() {
		try {
			Object media = Fileupload.get();

			if (media instanceof org.zkoss.image.Image) {
				Image image = new Image();

				image.setContent((org.zkoss.image.Image) media);

				image.setWidth("64px");
				image.setHeight("64px");

				contFoto.getChildren().clear();
				image.setParent(contFoto);

			} else if (media != null)
				Messagebox.show("Not an image: " + media, "Error",
						Messagebox.OK, Messagebox.ERROR);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public Checkbox getChkPropio() {
		return chkPropio;
	}

	public void setChkPropio(Checkbox chkPropio) {
		this.chkPropio = chkPropio;
	}

	public Textbox getTxtCodigo() {
		return txtCodigo;
	}

	public void setTxtCodigo(Textbox txtCodigo) {
		this.txtCodigo = txtCodigo;
	}

	public Textbox getTxtDescripcion() {
		return txtDescripcion;
	}

	public void setTxtDescripcion(Textbox txtDescripcion) {
		this.txtDescripcion = txtDescripcion;
	}

	public Textbox getTxtSerial() {
		return txtSerial;
	}

	public void setTxtSerial(Textbox txtSerial) {
		this.txtSerial = txtSerial;
	}

	public Spinner getSpnAnoFabricacion() {
		return spnAnoFabricacion;
	}

	public void setSpnAnoFabricacion(Spinner spnAnoFabricacion) {
		this.spnAnoFabricacion = spnAnoFabricacion;
	}

	public CompBuscar<Activo> getCatActivos() {
		return catActivos;
	}

	public void setCatActivos(CompBuscar<Activo> catActivos) {
		this.catActivos = catActivos;
	}

	public CompCombobox<Categoria> getCmbCategoria() {
		return cmbCategoria;
	}

	public void setCmbCategoria(CompCombobox<Categoria> cmbCategoria) {
		this.cmbCategoria = cmbCategoria;
	}

	public CompCombobox<Tipo> getCmbTipo() {
		return cmbTipo;
	}

	public void setCmbTipo(CompCombobox<Tipo> cmbTipo) {
		this.cmbTipo = cmbTipo;
	}

	public CompCombobox<Marca> getCmbMarca() {
		return cmbMarca;
	}

	public void setCmbMarca(CompCombobox<Marca> cmbMarca) {
		this.cmbMarca = cmbMarca;
	}

	public CompCombobox<Modelo> getCmbModelo() {
		return cmbModelo;
	}

	public void setCmbModelo(CompCombobox<Modelo> cmbModelo) {
		this.cmbModelo = cmbModelo;
	}

	public CompCombobox<Fabricante> getCmbFabricante() {
		return cmbFabricante;
	}

	public void setCmbFabricante(CompCombobox<Fabricante> cmbFabricante) {
		this.cmbFabricante = cmbFabricante;
	}

	public CompCombobox<EstadoFuncional> getCmbEstadoFunional() {
		return cmbEstadoFunional;
	}

	public void setCmbEstadoFunional(
			CompCombobox<EstadoFuncional> cmbEstadoFunional) {
		this.cmbEstadoFunional = cmbEstadoFunional;
	}

	public CompCombobox<String> getCmbColor() {
		return cmbColor;
	}

	public void setCmbColor(CompCombobox<String> cmbColor) {
		this.cmbColor = cmbColor;
	}

	public CompBuscar<Cliente> getCatPropietarios() {
		return catPropietarios;
	}

	public void setCatPropietarios(CompBuscar<Cliente> catPropietarios) {
		this.catPropietarios = catPropietarios;
	}

	public CompGrillaConBoton<ObjetoMantenimiento> getLstComponentes() {
		return lstComponentes;
	}

	public Div getDivColor() {
		return divColor;
	}

	public void setDivColor(Div divColor) {
		this.divColor = divColor;
	}

	public Vbox getContFoto() {
		return contFoto;
	}

	public void setContFoto(Vbox contFoto) {
		this.contFoto = contFoto;
	}

	public CompCombobox<Lote> getCmbLote() {
		return cmbLote;
	}

	public void setCmbLote(CompCombobox<Lote> cmbLote) {
		this.cmbLote = cmbLote;
	}

	public Button getBtnCargarFoto() {
		return btnCargarFoto;
	}

	public void setBtnCargarFoto(Button btnCargarFoto) {
		this.btnCargarFoto = btnCargarFoto;
	}

	public NegocioObjetoMantenimiento getNegocioObjetoMantenimiento() {
		return negocioObjetoMantenimiento;
	}

	public void setNegocioObjetoMantenimiento(
			NegocioObjetoMantenimiento negocioObjetoMantenimiento) {
		this.negocioObjetoMantenimiento = negocioObjetoMantenimiento;
	}
}