package cpc.demeter.vista.ministerio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.api.Button;

import cpc.ares.modelo.Accion;
import cpc.demeter.controlador.ministerio.ContUbicacionFisica;
import cpc.modelo.demeter.basico.TipoCoordenadaGeografica;
import cpc.modelo.demeter.basico.TipoDocumentoTierra;
import cpc.modelo.ministerio.basico.CoordenadaGeografica;
import cpc.modelo.ministerio.basico.LinderoDireccion;
import cpc.modelo.ministerio.basico.TipoSuperficie;
import cpc.modelo.ministerio.basico.TipoUbicacion;
import cpc.modelo.ministerio.dimension.UbicacionDireccion;
import cpc.modelo.ministerio.dimension.UbicacionSector;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.modelo.ministerio.gestion.SuperficieUnidad;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompGrillaConBoton;
import cpc.zk.componente.listas.CompListaBusca;
import cpc.zk.componente.ui.ComponentesAutomaticos;
import cpc.zk.componente.ventanas.CompGrupoBusqueda;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class UiUbicacionFisica extends CompVentanaBase<UbicacionDireccion> {

	private static final long serialVersionUID = 6799050286658762453L;
	private CompGrupoDatos gbGeneral;
	// private CompGrupoDatos gbExtra1;
	private CompGrupoDatos gbExtra2;
	private CompGrupoDatos gbExtra3;
	private Textbox descripcion;
	private Textbox referencia;
	private Textbox nombre;
	private Textbox documento;
	private Doublebox superficieTotal;
	private Label estadoLbl, parroquiaLbl, propietarioLbl;

	// private CompBuscar<UbicacionSector> sector;
	private CompGrupoBusqueda<UbicacionSector> sector;
	private CompCombobox<TipoUbicacion> tipoPropiedad;
	private CompCombobox<TipoDocumentoTierra> tipoDocumentoLegal;
	private CompGrillaConBoton<LinderoDireccion> linderos;
	private CompGrillaConBoton<SuperficieUnidad> superficies;
	private CompGrillaConBoton<CoordenadaGeografica> coordenadas;
	private CompCombobox<TipoCoordenadaGeografica> datum;
	private CompListaBusca<Productor> propietario;
	private List<CompEncabezado> listaDetalle;

	private List<Productor> lProductores;
	private List<TipoDocumentoTierra> tiposDocumentos;
	private List<UbicacionSector> sectores;
	private List<TipoUbicacion> tiposPropiedad;
	private List<TipoCoordenadaGeografica> tiposCoordenadas;
	private List<TipoSuperficie> tiposSuperficies;
	private String[] ORIENTACION = { "NORTE", "SUR", "ESTE", "OESTE" };

	public UiUbicacionFisica(String titulo, int ancho,
			List<UbicacionSector> sectores,
			List<TipoUbicacion> tiposUbicaciones,
			List<TipoCoordenadaGeografica> tiposCoordenadas,
			List<TipoSuperficie> tiposSuperficies,
			List<Productor> lProductores,
			List<TipoDocumentoTierra> tiposDocumentos) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.sectores = sectores;
		this.tiposPropiedad = tiposUbicaciones;
		this.tiposCoordenadas = tiposCoordenadas;
		this.tiposSuperficies = tiposSuperficies;
		this.lProductores = lProductores;
		this.tiposDocumentos = tiposDocumentos;
	}

	public UiUbicacionFisica(String titulo) throws ExcDatosInvalidos {
		super(titulo);
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);

		// gbExtra1 = new CompGrupoDatos("Superficie",1);
		gbExtra2 = new CompGrupoDatos("Georreferenciación", 1);
		gbExtra3 = new CompGrupoDatos("Linderos", 1);
		descripcion = new Textbox();
		referencia = new Textbox();
		nombre = new Textbox();
		documento = new Textbox();
		// Establecer tamaño maximo del Nro Documento (30 Carac. por BD)
		documento.setMaxlength(30);
		estadoLbl = new Label();
		parroquiaLbl = new Label();
		propietarioLbl = new Label("Propietarios");

		datum = new CompCombobox<TipoCoordenadaGeografica>();
		tipoPropiedad = new CompCombobox<TipoUbicacion>();
		tipoDocumentoLegal = new CompCombobox<TipoDocumentoTierra>();
		// sector = new CompBuscar<UbicacionSector>(getEncabezadoSector(), 0);
		propietario = new CompListaBusca<Productor>(encabezadosProductores(),
				1, getControlador());
		superficieTotal = new Doublebox();
		coordenadas = new CompGrillaConBoton<CoordenadaGeografica>(
				cargarListaCoordenadas(), getModelo()
						.getCoordenadasGeograficas(), encabezadoCoordenadas(),
				getControlador());
		superficies = new CompGrillaConBoton<SuperficieUnidad>(
				cargarListaSuperficies(), getModelo().getSuperficies(),
				encabezadosSuperficies(), getControlador());
		linderos = new CompGrillaConBoton<LinderoDireccion>(
				cargarListaLinderos(), getModelo().getLinderos(),
				encabezadoLindero(), getControlador());

		try {
			sector = new CompGrupoBusqueda<UbicacionSector>("Sector",
					getEncabezadoSector(), getModelo().getSector(),
					ComponentesAutomaticos.TEXTBOX);
			sector.setNuevo(new UbicacionSector());
			sector.setModeloCatalogo(sectores);
			sector.setAnchoValores(120);
			sector.setAnchoCatalogo(800);
			sector.addListener(getControlador());
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		datum.setModelo(tiposCoordenadas);
		tipoDocumentoLegal.setModelo(tiposDocumentos);
		propietario.setModelo(lProductores);
		propietario.setAncho(400);

		coordenadas.setNuevo(new CoordenadaGeografica());
		linderos.setNuevo(new LinderoDireccion());
		superficies.setNuevo(new SuperficieUnidad());

		tipoPropiedad.setModelo(tiposPropiedad);
		gbGeneral.setAnchoColumna(0, 150);

		sector.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
		datum.addEventListener(Events.ON_SELECTION, getControlador());
		tipoPropiedad.addEventListener(Events.ON_SELECTION, getControlador());
		descripcion.setWidth("450px");
		referencia.setWidth("450px");
		nombre.setWidth("150px");
		sector.setVisibleEditar(false);
	}

	public void dibujar() {
		sector.dibujar();
		this.appendChild(sector);
		gbGeneral.setAnchoColumna(0, 100);
		/*
		 * gbGeneral.addComponente(estadoLbl);
		 * gbGeneral.addComponente(parroquiaLbl);
		 * gbGeneral.addComponente("Sector:", sector);
		 */
		gbGeneral.addComponente("Direccion:", descripcion);
		gbGeneral.addComponente("Referencia:", referencia);
		gbGeneral.addComponente("Tipo Propiedad:", tipoPropiedad);
		gbGeneral.addComponenteMultiples("Documento Legal:", 90,
				tipoDocumentoLegal, documento);
		gbGeneral.addComponente(propietarioLbl);
		gbGeneral.addComponente(propietario);
		gbGeneral.addComponente("Supeficie Total (Has):", superficieTotal);
		gbGeneral.dibujar(this);

		superficies.dibujar();
		coordenadas.dibujar();
		linderos.dibujar();
		// gbExtra1.addComponente(superficies);
		gbExtra2.addComponenteMultiples("Datum :", 150, datum);
		gbExtra2.addComponente(coordenadas);
		gbExtra3.addComponente(linderos);
		// gbExtra1.dibujar(this);
		gbExtra2.dibujar(this);
		gbExtra3.dibujar(this);

		datum.setReadonly(true);
		tipoPropiedad.setReadonly(true);
		tipoDocumentoLegal.setReadonly(true);
		addBotonera();
		ocultarPropietarios();
	}

	public void cargarValores(UbicacionDireccion arg0) throws ExcDatosInvalidos {
		getBinder().addBinding(sector, "modelo", getNombreModelo() + ".sector",
				null, null, "save", null, null, null, null);
		if (getModelo().getDescripcion() != null)
			descripcion.setValue(getModelo().getDescripcion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);
		if (getModelo().getTipoDocumentoTierra() != null)
			tipoDocumentoLegal.setSeleccion(getModelo()
					.getTipoDocumentoTierra());
		getBinder().addBinding(tipoDocumentoLegal, "seleccion",
				getNombreModelo() + ".tipoDocumentoTierra", null, null, "save",
				null, null, null, null);
		if (getModelo().getSuperficie() != null)
			superficieTotal.setValue(getModelo().getSuperficie());
		getBinder().addBinding(superficieTotal, "value",
				getNombreModelo() + ".superficie", null, null, "save", null,
				null, null, null);
		if (getModelo().getReferencia() != null)
			referencia.setValue(getModelo().getReferencia());
		getBinder().addBinding(referencia, "value",
				getNombreModelo() + ".referencia", null, null, "save", null,
				null, null, null);
		if (getModelo().getReferencia() != null)
			referencia.setValue(getModelo().getReferencia());
		getBinder().addBinding(referencia, "value",
				getNombreModelo() + ".referencia", null, null, "save", null,
				null, null, null);
		if (getModelo().getTipoUbicacion() != null) {
			tipoPropiedad.setSeleccion(getModelo().getTipoUbicacion());
			if (getModelo().getTipoUbicacion().getPropietario().booleanValue())
				mostrarPropietarios();
			else
				ocultarPropietarios();
		}
		getBinder().addBinding(tipoPropiedad, "seleccion",
				getNombreModelo() + ".tipoUbicacion", null, null, "save", null,
				null, null, null);
		if (getModelo().getPropietarios() != null)
			propietario.setModeloSelect(getModelo().getPropietarios());
		getBinder().addBinding(propietario, "modeloSelect",
				getNombreModelo() + ".propietarios", null, null, "save", null,
				null, null, null);
		if (getModelo().getDocumentoLegal() != null)
			documento.setValue(getModelo().getDocumentoLegal());
		getBinder().addBinding(documento, "value",
				getNombreModelo() + ".documentoLegal", null, null, "save",
				null, null, null, null);
		if (getModelo().getSector() != null) {
			try {
				sector.actualizarValores();
				dibujarDetalle();
			} catch (ExcAccesoInvalido e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExcFiltroExcepcion e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void ocultarPropietarios() {
		propietarioLbl.setVisible(false);
		propietario.setVisible(false);
	}

	public void mostrarPropietarios() {
		propietarioLbl.setVisible(true);
		propietario.setVisible(true);
	}

	private List<CompEncabezado> getEncabezadoSector() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Sector", 250);
		titulo.setMetodoBinder("getNombre");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Parroquia", 250);
		titulo.setMetodoBinder("getStrParroquia");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Municipio", 200);
		titulo.setMetodoBinder("getStrMunicipio");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 200);
		titulo.setMetodoBinder("getStrEstado");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Pais", 150);
		titulo.setMetodoBinder("getStrPais");
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
		descripcion.setDisabled(true);
		sector.setModoEdicion(false);
		referencia.setDisabled(true);
		tipoPropiedad.setDisabled(true);
		tipoDocumentoLegal.setDisabled(true);
		documento.setDisabled(true);
		propietario.setDisabled(true);
		superficieTotal.setDisabled(true);
		datum.setDisabled(true);
		coordenadas.setDisabled(true);
		linderos.setDisabled(true);
	}

	public void modoEdicion() {
		descripcion.setDisabled(false);
		sector.setModoEdicion(true);
		referencia.setDisabled(false);
		tipoPropiedad.setDisabled(false);
		tipoDocumentoLegal.setDisabled(false);
		documento.setDisabled(false);
		propietario.setDisabled(false);
		superficieTotal.setDisabled(false);
		datum.setDisabled(false);
		coordenadas.setDisabled(false);
		linderos.setDisabled(false);
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.descripcion = descripcion;
	}

	private List<CompEncabezado> encabezadoCoordenadas() {
		CompEncabezado titulo;
		listaDetalle = new ArrayList<CompEncabezado>();
		titulo = new CompEncabezado("Uso", 50);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getHusoUTM");
		titulo.setMetodoModelo(".husoUTM");

		listaDetalle.add(titulo);
		titulo = new CompEncabezado("Norte/Latitud", 120);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getEjeY");
		titulo.setMetodoModelo(".ejeY");

		listaDetalle.add(titulo);
		titulo = new CompEncabezado("Este/Longitud", 120);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getEjeX");
		titulo.setMetodoModelo(".ejeX");
		listaDetalle.add(titulo);

		return listaDetalle;
	}

	private ArrayList<Component> cargarListaCoordenadas() {
		ArrayList<Component> lista = new ArrayList<Component>();

		Intbox uso = new Intbox();
		Doublebox latitud = new Doublebox();
		Doublebox longitud = new Doublebox();

		uso.setWidth("40px");
		latitud.setWidth("120px");
		longitud.setWidth("120px");

		lista.add(uso);
		lista.add(latitud);
		lista.add(longitud);
		return lista;
	}

	private List<CompEncabezado> encabezadoLindero() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Orientacion", 70);
		titulo.setMetodoComponente("indiceSeleccionado");
		titulo.setMetodoBinder("getOrientacion");
		titulo.setMetodoModelo(".orientacion");
		encabezado.add(titulo);
		titulo = new CompEncabezado("Lindero", 230);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNombre");
		titulo.setMetodoModelo(".nombre");
		encabezado.add(titulo);

		return encabezado;
	}

	private ArrayList<Component> cargarListaLinderos() {
		ArrayList<Component> lista = new ArrayList<Component>();

		CompCombobox<String> orientacion = new CompCombobox<String>();
		orientacion.setModelo(Arrays.asList(ORIENTACION));
		orientacion.setReadonly(true);
		Textbox lindero = new Textbox();
		orientacion.setWidth("70px");
		lindero.setWidth("350px");
		lindero.setMultiline(true);
		lindero.setCols(2);
		lista.add(orientacion);
		lista.add(lindero);
		return lista;
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

	private List<CompEncabezado> encabezadosSuperficies() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Tipo Superficie", 200);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getTipoSuperficie");
		titulo.setMetodoModelo(".tipoSuperficie");

		encabezado.add(titulo);
		titulo = new CompEncabezado("Extención hras", 100);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getSuperficie");
		titulo.setMetodoModelo(".superficie");
		encabezado.add(titulo);
		return encabezado;
	}

	private ArrayList<Component> cargarListaSuperficies() {
		ArrayList<Component> lista = new ArrayList<Component>();

		CompCombobox<TipoSuperficie> tipo = new CompCombobox<TipoSuperficie>();
		Doublebox spuerficie = new Doublebox();

		tipo.setWidth("200px");
		spuerficie.setWidth("110px");

		tipo.setModelo(tiposSuperficies);

		lista.add(tipo);
		lista.add(spuerficie);
		return lista;
	}

	public void dibujarDetalle() throws ExcFiltroExcepcion {
		if (getModelo().getCoordenadasGeograficas() != null
				&& getModelo().getCoordenadasGeograficas().size() > 0) {
			ContUbicacionFisica control;
			control = (ContUbicacionFisica) getControlador();
			/*
			 * for(CoordenadaGeografica item:
			 * getModelo().getCoordenadasGeograficas()){
			 * coordenadas.agregar(item); }
			 */
			try {
				datum.setSeleccion(getModelo().getCoordenadasGeograficas()
						.get(0).getTipo());
				control.SuichearGeorreferenciacion();
			} catch (NullPointerException e) {
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			}
		}
	}

	public void actualizarEncabezados(boolean visibleUso) {
		CompEncabezado encabezadoUso = listaDetalle.get(0);
		encabezadoUso.setVisible(visibleUso);
		coordenadas.clear();
		coordenadas.actualizarEncabezados();
	}

	public Button getBtnProductores() {
		return propietario.getBotonCrear();
	}

	public Textbox getReferencia() {
		return referencia;
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

	public CompGrupoBusqueda<UbicacionSector> getSector() {
		return sector;
	}

	public Button getEditarSector() {
		return sector.getEditar();
	}

	public Button getCrearSector() {
		return sector.getCrear();
	}

	public CompGrillaConBoton<SuperficieUnidad> getSuperficies() {
		return superficies;
	}

	public CompGrillaConBoton<CoordenadaGeografica> getCoordenadas() {
		return coordenadas;
	}

	public CompGrillaConBoton<LinderoDireccion> getLinderos() {
		return linderos;
	}

	public CompCombobox<TipoUbicacion> getTipoPropiedad() {
		return tipoPropiedad;
	}

	public CompListaBusca<Productor> getPropietario() {
		return propietario;
	}

	public CompCombobox<TipoCoordenadaGeografica> getDatum() {
		return datum;
	}
}
