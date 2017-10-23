package cpc.demeter.vista.gestion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Radio;
import org.zkoss.zul.Radiogroup;
import org.zkoss.zul.Textbox;

import cpc.demeter.AppDemeter;
import cpc.demeter.vista.mantenimiento.UIRegistroFalla;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.gestion.DetalleMaquinariaOrdenTrabajo;
import cpc.modelo.demeter.gestion.MaquinariaUnidad;
import cpc.modelo.demeter.gestion.OrdenTrabajo;
import cpc.modelo.demeter.gestion.Sustitucion;
import cpc.modelo.demeter.gestion.SustitucionOrden;
import cpc.modelo.demeter.mantenimiento.RegistroFalla;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.negocio.demeter.gestion.NegocioSustitucion;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class UISustitucion extends CompVentanaBase<Sustitucion> {

	private static final long serialVersionUID = -3572524741697517560L;

	private CompGrupoDatos grpBusqueda, grpSustitucion, grpOrdenesAfectadas,
			grpCausaSustitucion, grpObservacion;
	private Radiogroup rdoGrupo;
	private CompCombobox<UnidadFuncional> cmbUnidadesFuncional;
	private CompBuscar<Trabajador> catOperadorBusqueda, catOperadorCambio;
	private CompBuscar<MaquinariaUnidad> catMaquinariaBusqueda,
			catMaquinariaCambio;
	private CompLista<DetalleMaquinariaOrdenTrabajo> lstOrdenesTrabajo;
	private Button btnBuscarOrdenes;
	private Textbox txtObservacion;
	private String[] opciones = { "TRANSFERENCIA DE MAQUINARIA",
			"FALLA EN MAQUINARIA", "CAMBIO OPERADOR / MAQUINARIA" };
	private UIRegistroFalla uIRegistroFalla;

	private AppDemeter app;

	public UISustitucion(String titulo, int ancho, AppDemeter app) {
		super(titulo, ancho);
		this.app = app;

	}

	public void inicializar() {
		grpCausaSustitucion = new CompGrupoDatos("Causa de Sustitucion", 1);
		grpBusqueda = new CompGrupoDatos("Busqueda", 2);
		grpSustitucion = new CompGrupoDatos("Sustituir por", 2);
		grpOrdenesAfectadas = new CompGrupoDatos("Para las Ordenes de Trabajo",
				2);
		grpObservacion = new CompGrupoDatos("Observaciones", 1);
		rdoGrupo = new Radiogroup();
		txtObservacion = new Textbox();
		cmbUnidadesFuncional = new CompCombobox<UnidadFuncional>();
		catMaquinariaBusqueda = new CompBuscar<MaquinariaUnidad>(
				getEncabezadoEquipo(), 3);
		catMaquinariaCambio = new CompBuscar<MaquinariaUnidad>(
				getEncabezadoEquipo(), 3);
		catOperadorBusqueda = new CompBuscar<Trabajador>(
				getEncabezadoOperadores(), 1);
		catOperadorCambio = new CompBuscar<Trabajador>(
				getEncabezadoOperadores(), 1);
		btnBuscarOrdenes = new Button("Buscar las Ordenes a Afectar");
		lstOrdenesTrabajo = new CompLista<DetalleMaquinariaOrdenTrabajo>(
				getEncabezadosDetalle());
	}

	public void dibujar() {
		rdoGrupo.appendItem("Transferencia de Maquinaria", "1");
		rdoGrupo.appendItem("Falla en Maquinaria", "2");
		rdoGrupo.appendItem("Cambio Operador / Maquinaria", "3");

		grpBusqueda.addComponente("Unidad Ejecutora : ", cmbUnidadesFuncional);
		grpBusqueda.addComponente("Operador : ", catOperadorBusqueda);
		grpBusqueda.addComponente("Maquinaria : ", catMaquinariaBusqueda);
		grpBusqueda.addComponente(btnBuscarOrdenes);
		grpBusqueda.dibujar(this);

		grpSustitucion.addComponente("Operador : ", catOperadorCambio);
		grpSustitucion.addComponente("Maquinaria : ", catMaquinariaCambio);

		grpSustitucion.dibujar(this);

		grpOrdenesAfectadas.addComponente(lstOrdenesTrabajo);
		grpOrdenesAfectadas.dibujar(this);

		grpCausaSustitucion.addComponente(rdoGrupo);
		grpCausaSustitucion.dibujar(this);

		grpObservacion.addComponente(txtObservacion);
		grpObservacion.dibujar(this);

		cargarListas();

		catMaquinariaBusqueda.setAncho(550);
		catMaquinariaCambio.setAncho(550);

		catOperadorBusqueda.setAncho(550);
		catOperadorCambio.setAncho(550);

		cmbUnidadesFuncional.addEventListener(Events.ON_SELECT, this);
		catMaquinariaBusqueda.getCatalogo().addEventListener(Events.ON_SELECT,
				this);
		catOperadorBusqueda.getCatalogo().addEventListener(Events.ON_SELECT,
				this);
		btnBuscarOrdenes.addEventListener(Events.ON_CLICK, this);
		rdoGrupo.addEventListener(Events.ON_CHECK, this);

		cmbUnidadesFuncional.setReadonly(true);

		txtObservacion.setRows(3);
		txtObservacion.setWidth("600px");
		addBotonera();
	}

	public void cargarValores(Sustitucion sustitucion) throws ExcDatosInvalidos {

		catOperadorBusqueda.setSeleccion(getModelo().getOperadorAnterior());
		catOperadorCambio.setSeleccion(getModelo().getOperadorActual());

		if (getModelo().getMaquinaAnterior() != null) {
			cmbUnidadesFuncional.setSeleccion(getModelo().getMaquinaAnterior()
					.getUnidad());
			try {
				catMaquinariaBusqueda.setModelo(NegocioSustitucion
						.getInstance().obtenerMaquinariasPorUnidadFuncional(
								getModelo().getMaquinaAnterior().getUnidad()));
				catMaquinariaCambio.setModelo(NegocioSustitucion.getInstance()
						.obtenerMaquinariasPorUnidadFuncional(
								getModelo().getMaquinaAnterior().getUnidad()));
				catMaquinariaBusqueda.setSeleccion(getModelo()
						.getMaquinaAnterior());
				catMaquinariaCambio
						.setSeleccion(getModelo().getMaquinaActual());
			} catch (ExcFiltroExcepcion e) {
				e.printStackTrace();
			}

		}

		cargarOrdenAfectadas();

		if (getModelo().getCausa() != null) {
			rdoGrupo.getChildren().clear();
			rdoGrupo.appendChild(new Label(getModelo().getCausa().toUpperCase()));
		}

		txtObservacion.setValue(getModelo().getObservacion());
		getBinder().addBinding(txtObservacion, "value",
				getNombreModelo() + ".observacion", null, null, "save", null,
				null, null, null);

	}

	public void obtenerOrdenesAfectadas() {
		List<SustitucionOrden> sos = new ArrayList<SustitucionOrden>();
		try {
			for (DetalleMaquinariaOrdenTrabajo dm : lstOrdenesTrabajo
					.getModelo()) {
				SustitucionOrden so = new SustitucionOrden();
				so.setOrdenTrabajo(dm.getOrdenTrabajo());
				so.setSustitucion(getModelo());
				if (!contieneOrdenTrabajo(sos, dm.getOrdenTrabajo()))
					;
				sos.add(so);
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		getModelo().setSustitucionesOrden(sos);

	}

	private void cargarOrdenAfectadas() {
		List<DetalleMaquinariaOrdenTrabajo> lista = new ArrayList<DetalleMaquinariaOrdenTrabajo>();

		if (getModelo().getSustitucionesOrden() != null) {

			for (SustitucionOrden so : getModelo().getSustitucionesOrden()) {
				DetalleMaquinariaOrdenTrabajo dmot = new DetalleMaquinariaOrdenTrabajo();
				dmot.setOperador(getModelo().getOperadorActual());
				dmot.setMaquinaria(getModelo().getMaquinaActual());
				dmot.setImplemento(getModelo().getImplementoActual());
				dmot.setOrdenTrabajo(so.getOrdenTrabajo());
				lista.add(dmot);
			}
			lstOrdenesTrabajo.clear();
			lstOrdenesTrabajo.setModelo(lista);
		}
	}

	private List<CompEncabezado> getEncabezadoEquipo() {

		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Codigo. ", 120);
		titulo.setMetodoBinder("getCodigoActivo");
		lista.add(titulo);

		titulo = new CompEncabezado("Ejemplar. ", 120);
		titulo.setMetodoBinder("getIdEjemplarActivo");
		lista.add(titulo);

		titulo = new CompEncabezado("Serial. ", 150);
		titulo.setMetodoBinder("getSerial");
		lista.add(titulo);

		titulo = new CompEncabezado("Denominación. ", 300);
		titulo.setMetodoBinder("getNombre");
		lista.add(titulo);

		titulo = new CompEncabezado("Marca. ", 140);
		titulo.setMetodoBinder("getMarca");
		lista.add(titulo);

		titulo = new CompEncabezado("Modelo. ", 140);
		titulo.setMetodoBinder("getModelo");
		lista.add(titulo);

		return lista;
	}

	private List<CompEncabezado> getEncabezadoOperadores() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Cedula", 100);
		titulo.setMetodoBinder("getNroCedula");
		lista.add(titulo);

		titulo = new CompEncabezado("Nombre", 200);
		titulo.setMetodoBinder("getNombre");
		lista.add(titulo);

		titulo = new CompEncabezado("Apellido", 200);
		titulo.setMetodoBinder("getApellido");
		lista.add(titulo);

		titulo = new CompEncabezado("Direccion", 300);
		titulo.setMetodoBinder("getDireccion");
		lista.add(titulo);

		return lista;

	}

	private List<CompEncabezado> getEncabezadosDetalle() {

		List<CompEncabezado> listaDetalle = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Orden de Trabajo", 180);
		titulo.setMetodoBinder("getOrdenTrabajoStr");
		listaDetalle.add(titulo);

		titulo = new CompEncabezado("Operador", 180);
		titulo.setMetodoBinder("getOperador");

		listaDetalle.add(titulo);

		titulo = new CompEncabezado("Maquinaria", 220);
		titulo.setMetodoBinder("getMaquinaria");

		listaDetalle.add(titulo);

		titulo = new CompEncabezado("Implemento", 220);
		titulo.setMetodoBinder("getImplemento");

		listaDetalle.add(titulo);

		return listaDetalle;

	}

	private void cargarListas() {
		try {
			cmbUnidadesFuncional.setModelo(NegocioSustitucion.getInstance()
					.obteneUnidadesFuncionales());
		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
			app.mostrarError("Error al cargar las Unidades Funcionales "
					+ e.getMessage());
		}

		try {
			catOperadorBusqueda.setModelo(NegocioSustitucion.getInstance()
					.obtenerOperadores());
			catOperadorCambio.setModelo(NegocioSustitucion.getInstance()
					.obtenerOperadores());
		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
			app.mostrarError("Error al cargar los Operadores " + e.getMessage());
		}
	}

	@Override
	public void onEvent(Event event) throws Exception {
		super.onEvent(event);

		if (event.getTarget() == cmbUnidadesFuncional) {
			if (cmbUnidadesFuncional != null) {
				catMaquinariaBusqueda.setModelo(NegocioSustitucion
						.getInstance().obtenerMaquinariasPorUnidadFuncional(
								cmbUnidadesFuncional.getSeleccion()));
				catMaquinariaCambio.setModelo(NegocioSustitucion.getInstance()
						.obtenerMaquinariasPorUnidadFuncional(
								cmbUnidadesFuncional.getSeleccion()));
			}
		}

		if (event.getTarget() == btnBuscarOrdenes) {
			if (catMaquinariaBusqueda.getSeleccion() != null
					&& catOperadorBusqueda.getSeleccion() != null) {
				List<DetalleMaquinariaOrdenTrabajo> lista = NegocioSustitucion
						.getInstance().obtenerOrdenesPorMaquinariaYOperador(
								catMaquinariaBusqueda.getSeleccion(),
								catOperadorBusqueda.getSeleccion());
				if (lista != null) {
					lstOrdenesTrabajo.getItems().clear();
					lstOrdenesTrabajo.setModelo(lista);
				}
			} else
				app.mostrarError("Seleccione un Operador y Una Maquinaria");
		}

		if (event.getTarget() instanceof Radio) {
			if (((Radio) event.getTarget()).getRadiogroup().getSelectedIndex() == 1) {
				capturarFalla();
			}
		}

	}

	public void capturarFalla() {
		if (rdoGrupo.getSelectedIndex() == 1
				&& catOperadorBusqueda.getSeleccion() != null) {
			uIRegistroFalla = new UIRegistroFalla("REGISTRO DE FALLA PARA "
					+ catMaquinariaBusqueda.getSeleccion(), 700, this.app);
			uIRegistroFalla.inicializar();
			uIRegistroFalla.dibujar();
			uIRegistroFalla.getCatTrabajadorReporta().setSeleccion(
					catOperadorBusqueda.getSeleccion());
			uIRegistroFalla.getCatObjetoMantenimiento().setVisible(false);
			uIRegistroFalla.setClosable(false);
			uIRegistroFalla.getCancelar().setVisible(false);
			uIRegistroFalla.getAceptar().addEventListener(Events.ON_CLICK,
					new EventListener() {
						public void onEvent(Event arg0) throws Exception {
							if (uIRegistroFalla.getCmbTipoFalla()
									.getSeleccion() == null)
								throw new WrongValueException(uIRegistroFalla
										.getCmbTipoFalla(),
										"Seleccione un valor");
							if (uIRegistroFalla.getCmbMomentoFalla()
									.getSeleccion() == null)
								throw new WrongValueException(uIRegistroFalla
										.getCmbMomentoFalla(),
										"Seleccione un valor");
							if (uIRegistroFalla.getFechaFalla().getValue() == null)
								throw new WrongValueException(uIRegistroFalla
										.getFechaFalla(), "Indique");

							RegistroFalla reg = new RegistroFalla();
							reg.setFechaRegistro(new Date());
							reg.setFechaFalla(uIRegistroFalla.getFechaFalla()
									.getValue());
							reg.setTipoFalla(uIRegistroFalla.getCmbTipoFalla()
									.getSeleccion());
							reg.setMomentoFalla(uIRegistroFalla
									.getCmbMomentoFalla().getSeleccion());
							reg.setTrabajadorReporta(uIRegistroFalla
									.getCatTrabajadorReporta().getSeleccion());

							uIRegistroFalla.setModelo(reg);
							uIRegistroFalla.onClose();

						}
					});
			app.getPadre().getVentana().appendChild(uIRegistroFalla);

		}

	}

	public void setLectura() {
		this.grpBusqueda.getLblTitulo().setValue("VALORES ANTERIORES");

		this.catMaquinariaBusqueda.setDisabled(true);
		this.catMaquinariaCambio.setDisabled(true);

		this.catOperadorBusqueda.setDisabled(true);
		this.catOperadorCambio.setDisabled(true);

		this.btnBuscarOrdenes.setVisible(false);

		this.grpSustitucion.getLblTitulo().setValue("SUSTITUIDO POR");
		this.grpOrdenesAfectadas.getLblTitulo().setValue("ORDENES AFECTADAS");

		this.txtObservacion.setDisabled(true);

	}

	private boolean contieneOrdenTrabajo(
			List<SustitucionOrden> sustitcionesOrden, OrdenTrabajo orden) {
		for (SustitucionOrden so : sustitcionesOrden) {
			if (so.getOrdenTrabajo().equals(orden))
				return true;
		}
		return false;
	}

	public Radiogroup getRdoGrupo() {
		return rdoGrupo;
	}

	public void setRdoGrupo(Radiogroup rdoGrupo) {
		this.rdoGrupo = rdoGrupo;
	}

	public CompCombobox<UnidadFuncional> getCmbUnidadesFuncional() {
		return cmbUnidadesFuncional;
	}

	public void setCmbUnidadesFuncional(
			CompCombobox<UnidadFuncional> cmbUnidadesFuncional) {
		this.cmbUnidadesFuncional = cmbUnidadesFuncional;
	}

	public CompBuscar<Trabajador> getCatOperadorBusqueda() {
		return catOperadorBusqueda;
	}

	public void setCatOperadorBusqueda(
			CompBuscar<Trabajador> catOperadorBusqueda) {
		this.catOperadorBusqueda = catOperadorBusqueda;
	}

	public CompBuscar<Trabajador> getCatOperadorCambio() {
		return catOperadorCambio;
	}

	public void setCatOperadorCambio(CompBuscar<Trabajador> catOperadorCambio) {
		this.catOperadorCambio = catOperadorCambio;
	}

	public CompBuscar<MaquinariaUnidad> getCatMaquinariaBusqueda() {
		return catMaquinariaBusqueda;
	}

	public void setCatMaquinariaBusqueda(
			CompBuscar<MaquinariaUnidad> catMaquinariaBusqueda) {
		this.catMaquinariaBusqueda = catMaquinariaBusqueda;
	}

	public CompBuscar<MaquinariaUnidad> getCatMaquinariaCambio() {
		return catMaquinariaCambio;
	}

	public void setCatMaquinariaCambio(
			CompBuscar<MaquinariaUnidad> catMaquinariaCambio) {
		this.catMaquinariaCambio = catMaquinariaCambio;
	}

	public CompLista<DetalleMaquinariaOrdenTrabajo> getLstOrdenesTrabajo() {
		return lstOrdenesTrabajo;
	}

	public void setLstOrdenesTrabajo(
			CompLista<DetalleMaquinariaOrdenTrabajo> lstOrdenesTrabajo) {
		this.lstOrdenesTrabajo = lstOrdenesTrabajo;
	}

	public Button getBtnBuscarOrdenes() {
		return btnBuscarOrdenes;
	}

	public void setBtnBuscarOrdenes(Button btnBuscarOrdenes) {
		this.btnBuscarOrdenes = btnBuscarOrdenes;
	}

	public Textbox getTxtObservacion() {
		return txtObservacion;
	}

	public void setTxtObservacion(Textbox txtObservacion) {
		this.txtObservacion = txtObservacion;
	}

	public String[] getOpciones() {
		return opciones;
	}

	public void setOpciones(String[] opciones) {
		this.opciones = opciones;
	}

	public CompGrupoDatos getGrpSustitucion() {
		return grpSustitucion;
	}

	public void setGrpSustitucion(CompGrupoDatos grpSustitucion) {
		this.grpSustitucion = grpSustitucion;
	}

	public UIRegistroFalla getuIRegistroFalla() {
		return uIRegistroFalla;
	}

	public void setuIRegistroFalla(UIRegistroFalla uIRegistroFalla) {
		this.uIRegistroFalla = uIRegistroFalla;
	}

}