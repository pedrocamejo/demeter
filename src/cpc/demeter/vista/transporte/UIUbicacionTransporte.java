package cpc.demeter.vista.transporte;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.transporte.UbicacionTransporte;
import cpc.modelo.ministerio.dimension.UbicacionMunicipio;
import cpc.modelo.ministerio.dimension.UbicacionParroquia;
import cpc.modelo.ministerio.dimension.UbicacionSector;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UIUbicacionTransporte extends CompVentanaBase<UbicacionTransporte> {

	/**
	 * 
	 */
	
	private List<UbicacionMunicipio> municipios;
	private List<UbicacionParroquia> parroquias;
	private List<UbicacionSector> sectores;
	
	private CompGrupoDatos grupo;
	private CompBuscar<UbicacionMunicipio> cmbMunicipio;
	private CompBuscar<UbicacionParroquia> cmbParroquia;
	private CompBuscar<UbicacionSector> cmbsector;
	private Textbox direccion;
	private Textbox referecia;
	

	public  UIUbicacionTransporte(String titulo, int ancho,
			List<UbicacionMunicipio> municipios,List<UbicacionParroquia> parroquias,List<UbicacionSector> sectores) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.parroquias = parroquias;
		this.sectores=sectores;
		this.municipios=municipios;
	}

	public UIUbicacionTransporte(String titulo) throws ExcDatosInvalidos {
		super(titulo);
	}

	public void inicializar() {
		grupo = new CompGrupoDatos("Direccion Origen", 2);
		cmbMunicipio = new CompBuscar<UbicacionMunicipio>(getEncabezadoMunicipio(), 1);
		cmbMunicipio.setModelo(municipios);
		cmbMunicipio.setAncho(750);
		cmbMunicipio.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
		cmbParroquia = new CompBuscar<UbicacionParroquia>(getEncabezadoParroquia(), 1);
		cmbParroquia.setModelo(parroquias);
		cmbParroquia.setAncho(750);
		cmbParroquia.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
		cmbsector = new CompBuscar<UbicacionSector>(getEncabezadoSector(), 1);
		cmbsector.setModelo(sectores);
		cmbsector.setAncho(750);
		cmbsector.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
		direccion = new Textbox();
		direccion.setMultiline(true);
		direccion.setWidth("550px");
		direccion.setRows(2);
		direccion.setMaxlength(150);
		referecia = new Textbox();
		referecia.setMultiline(true);
		referecia.setRows(2);
		referecia.setWidth("550px");
		referecia.setMaxlength(150);
	
	}

	public void dibujar() {
		grupo.addComponente("Municipio", cmbMunicipio); 
		grupo.addComponente("Parroquia", cmbParroquia); 
		grupo.addComponente("Sector", cmbsector); 
		grupo.addComponente("Direccion", direccion); 
		grupo.addComponente("Referencia", referecia);
		grupo.dibujar(this);
		addBotonera();

	}

	private List<CompEncabezado> getEncabezadoEstado() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Parroquia", 250);
		titulo.setMetodoBinder("getNombre");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("municipio", 200);
		titulo.setMetodoBinder("getStrMunicipio");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("estado", 200);
		titulo.setMetodoBinder("getStrEstado");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("pais", 150);
		titulo.setMetodoBinder("getStrPais");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;

	}

	public void cargarValores(UbicacionTransporte arg0) throws ExcDatosInvalidos {
		if (getModelo().getId() != null){
			
		
		cmbMunicipio.setSeleccion(getModelo().getUbicacionMunicipio());
		cmbMunicipio.setText(getModelo().getUbicacionMunicipio().getNombre());
		cmbParroquia.setSeleccion(getModelo().getUbicacionParroquia());;
		cmbParroquia.setText(getModelo().getUbicacionParroquia().getNombre());;
		cmbsector.setSeleccion(getModelo().getUbicacionSector());
		cmbsector.setText(getModelo().getUbicacionSector().getNombre());
		direccion.setValue(getModelo().getDireccion());
		referecia.setValue(getModelo().getReferencia());
			
			
		}
		
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		
		
	}

	public void modoEdicion() {
		
	}

	
	private List<CompEncabezado> getEncabezadoMunicipio() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		titulo = new CompEncabezado("Nombre", 300);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 300);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo);
		return encabezado;
	}
	
	private List<CompEncabezado> getEncabezadoParroquia() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Nombre", 300);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("municipio", 200);
		titulo.setMetodoBinder("getStrMunicipio");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("estado", 200);
		titulo.setMetodoBinder("getStrEstado");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		

		return encabezado;
		
	}
	
	private List<CompEncabezado> getEncabezadoSector() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		
		titulo = new CompEncabezado("Nombre", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Parroquia", 200);
		titulo.setMetodoBinder("getStrParroquia");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("municipio", 150);
		titulo.setMetodoBinder("getStrMunicipio");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("estado", 150);
		titulo.setMetodoBinder("getStrEstado");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		
		return encabezado;
	}

	public CompBuscar<UbicacionMunicipio> getCmbMunicipio() {
		return cmbMunicipio;
	}

	public void setCmbMunicipio(CompBuscar<UbicacionMunicipio> cmbMunicipio) {
		this.cmbMunicipio = cmbMunicipio;
	}

	public CompBuscar<UbicacionParroquia> getCmbParroquia() {
		return cmbParroquia;
	}

	public void setCmbParroquia(CompBuscar<UbicacionParroquia> cmbParroquia) {
		this.cmbParroquia = cmbParroquia;
	}

	public CompBuscar<UbicacionSector> getCmbsector() {
		return cmbsector;
	}

	public void setCmbsector(CompBuscar<UbicacionSector> cmbsector) {
		this.cmbsector = cmbsector;
	}

	public Textbox getDireccion() {
		return direccion;
	}

	public void setDireccion(Textbox direccion) {
		this.direccion = direccion;
	}

	public Textbox getReferecia() {
		return referecia;
	}

	public void setReferecia(Textbox referecia) {
		this.referecia = referecia;
	}

}