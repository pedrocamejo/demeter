package cpc.demeter.vista.mantenimiento;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

import com.sun.accessibility.internal.resources.accessibility;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.modelo.demeter.mantenimiento.ClaseMaquinaria;
import cpc.modelo.demeter.mantenimiento.DetalleGarantina;
import cpc.modelo.demeter.mantenimiento.Serie;
import cpc.modelo.demeter.mantenimiento.TipoGarantia;
import cpc.modelo.demeter.mantenimiento.TipoMaquinariaVenta;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UITipoGarantia extends CompVentanaBase<TipoGarantia> {

	private static final long serialVersionUID = 1L;

	private CompGrupoDatos			 gbGeneral;
	private CompGrupoDatos 			 datosmodelo;
	private CompGrupoDatos 			 datosmotor;

	private CompBuscar<Modelo> 	 			 modelo;
	private CompBuscar<Modelo> 				 motormodelo;
	private CompBuscar<TipoMaquinariaVenta>	 tipoMaquinaria; // agricola vial
	private CompBuscar<ClaseMaquinaria>		 claseMaquinaria;
	private CompBuscar<Serie> 				 serie; // / Nro de la Serie

	
	private List<Modelo> 				 modelos = new ArrayList<Modelo>();
	private List<ClaseMaquinaria> 		 claseMaquinarias;
	private List<TipoMaquinariaVenta> 	tipoMaquinarias;
	private List<Serie> 			  	series = new ArrayList<Serie>();;
	
	private Textbox 				 	descripcion; 
	private Listbox 				 	detalles;
	private List<DetalleGarantina>  	detalleslista = new ArrayList<DetalleGarantina>();

	private Button 						 agregarDetalle;
	private Button 					 	quitarDetalle;

	private AppDemeter 				 app;

	private Doublebox				 peso;
	private Textbox				     motortipo; // diesel combustible ect..
	private Textbox 				 potencia; // potencia del motor
	private Textbox 				 fuerza;
	private Textbox 				 colorPrimario;
	private Textbox 				 colorSecundario; 

 

	public Button getAgregarDetalle() {
		return agregarDetalle;
	}

	public Button getQuitarDetalle() {
		return quitarDetalle;
	}
 

	public void setAgregarDetalle(Button agregarDetalle) {
		this.agregarDetalle = agregarDetalle;
	}

	public void setQuitarDetalle(Button quitarDetalle) {
		this.quitarDetalle = quitarDetalle;
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.descripcion = descripcion;
	}

	public UITipoGarantia() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param titulo
	 * @param ancho
	 */
	public UITipoGarantia(String titulo, int ancho, List<Modelo> modelos,
			List<ClaseMaquinaria> clases, List<TipoMaquinariaVenta> tipos,  List<Serie> series) {
		super(titulo, ancho);
		this.modelos = modelos;
		this.tipoMaquinarias = tipos;
		this.claseMaquinarias = clases;
		this.series = series;
		// TODO Auto-generated constructor stub
	}

	public AppDemeter getApp() {
		return app;
	}

	public void setApp(AppDemeter app) {
		this.app = app;
	}

	/**
	 * @param titulo
	 */

	public UITipoGarantia(String titulo) {
		super(titulo);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub

		setMaximizable(true);
 

		descripcion = new Textbox();
		descripcion.setMaxlength(250);
		descripcion.setRows(3);
		descripcion.setWidth("90%");
		
		gbGeneral = new CompGrupoDatos("Datos Garantia ", 2);
		datosmodelo = new CompGrupoDatos("Datos del Modelo ", 4);
		datosmotor = new CompGrupoDatos("Datos del Motor ", 4);
		
		serie = new CompBuscar<Serie>(cargarEncabezadoSerie(),1);
		serie.setAncho(450);
		serie.setWidth("250px");
		serie.setModelo(series);
		serie.setListenerEncontrar(getControlador());
		
		motormodelo = new CompBuscar<Modelo>(cargarEncabezadomodelo(), 2);
		motormodelo.setAncho(450);
		motormodelo.setWidth("250px");
		motormodelo.setModelo(modelos);
		motormodelo.setListenerEncontrar(getControlador());
		
		modelo = new CompBuscar<Modelo>(cargarEncabezadomodelo(), 2);
		modelo.setAncho(450);
		modelo.setWidth("450px");
		modelo.setModelo(modelos);
		modelo.setListenerEncontrar(getControlador());

		claseMaquinaria = new CompBuscar<ClaseMaquinaria>(cargarEncabezado(), 1);
		tipoMaquinaria = new CompBuscar<TipoMaquinariaVenta>(
				cargarEncabezado(), 1);

		claseMaquinaria.setAncho(200);
		claseMaquinaria.setWidth("200px");
		claseMaquinaria.setModelo(claseMaquinarias);

		tipoMaquinaria.setAncho(200);
		tipoMaquinaria.setWidth("200px");
		tipoMaquinaria.setModelo(tipoMaquinarias);
 
		agregarDetalle = new Button();
		agregarDetalle.setImage("/iconos/24x24/agregar.png");
		agregarDetalle.addEventListener(Events.ON_CLICK, getControlador());
		agregarDetalle.setSclass("btnEdicion");
		

		quitarDetalle = new Button();
		quitarDetalle.setImage("/iconos/24x24/quitar.png");
		quitarDetalle.addEventListener(Events.ON_CLICK, getControlador());
		quitarDetalle.setSclass("btnEdicion");

		
		peso = new Doublebox();
		
		motortipo = new Textbox(); // diesel combustible ect..
		motortipo.setMaxlength(100);

		potencia = new Textbox(); // potencia del motor
		potencia.setMaxlength(100);
		
		fuerza = new Textbox();
		fuerza.setMaxlength(100);
		
		colorPrimario = new Textbox();
		colorPrimario.setMaxlength(100);
		
		colorSecundario = new Textbox();
		colorSecundario.setMaxlength(100);
	
		detalles= new Listbox();
		detalles.setWidth("450px");
		detalles.setItemRenderer(new ListitemRenderer() {
			@Override
			public void render(Listitem arg0, Object arg1) throws Exception {
				DetalleGarantina detalle = (DetalleGarantina) arg1;
				arg0.setValue(arg1);
				arg0.setLabel(detalle.getDescripcion());
			}
		});
		
	}

	
	public List<CompEncabezado> cargarEncabezadoSerie() {
		CompEncabezado titulo;

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		titulo = new CompEncabezado(" ID ", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado(" Nro Serie ", 130);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);
 
		return encabezado;

	}

	public List<CompEncabezado> cargarEncabezadomodelo() {
		CompEncabezado titulo;

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		titulo = new CompEncabezado("Marca ", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMarca");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Codigo Modelo", 130);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCodigoModelo");
		encabezado.add(titulo);

		titulo = new CompEncabezado(" Modelo ", 130);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcionModelo");
		encabezado.add(titulo);
		return encabezado;

	}

	// voy a usar el mismo para la clase y para el tipo a la final
	// el objecto tiene la misma estructura id , descripcion
	public List<CompEncabezado> cargarEncabezado() {
		CompEncabezado titulo;

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		titulo = new CompEncabezado("id", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado(" Descripción ", 130);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);
		return encabezado;

	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub
		gbGeneral.setAnchoColumna(0, 100); 
		gbGeneral.addComponente("Descripcion:", descripcion);
		gbGeneral.addComponente("Modelo", modelo);

		datosmodelo.addComponente("Peso ", peso);
		datosmodelo.addComponente("Nro Serie ", serie);
		datosmodelo.addComponente("Clase ", claseMaquinaria);
		datosmodelo.addComponente("Tipo  ", tipoMaquinaria);
		datosmodelo.addComponente("Color Primario ", colorPrimario);
		datosmodelo.addComponente("Color Secundario ", colorSecundario);

		datosmotor.addComponente("Modelo Motor ", motormodelo);
		datosmotor.addComponente("Tipo Motor  ", motortipo);
		datosmotor.addComponente("Fuerza  motor ", fuerza);
		datosmotor.addComponente("Potencia Motor", potencia);

		gbGeneral.dibujar(this);
		datosmodelo.dibujar(this);
		datosmotor.dibujar(this);

		Hbox botonera = new Hbox(); 
		Div div = new Div();
		div.appendChild(agregarDetalle);
		div.appendChild(quitarDetalle);
		botonera.appendChild(detalles);
		botonera.appendChild(div);
		gbGeneral.addComponente("Servicios ", botonera);
		addBotonera();

	}

	@Override
	public void cargarValores(TipoGarantia dato) throws ExcDatosInvalidos {
	 
		descripcion.setValue(getModelo().getDescripcion());
		peso.setValue((getModelo().getPeso() == null ? 0 : getModelo().getPeso()));
		motortipo.setValue(getModelo().getMotortipo());
		potencia.setValue(getModelo().getPotencia());
		fuerza.setValue(getModelo().getFuerza());
		colorPrimario.setValue(getModelo().getColorPrimario());
		colorSecundario.setValue(getModelo().getColorSecundario());
		detalleslista = new ArrayList<DetalleGarantina>(getModelo().getDetallesGarantia());

		getBinder().addBinding(modelo, "seleccion",	getNombreModelo() + ".modelo", null, null, "save", null, null,null, null);
		getBinder().addBinding(serie, "seleccion",	getNombreModelo() + ".serie", null, null, "save", null, null,null, null);
		getBinder().addBinding(motormodelo, "seleccion",getNombreModelo() + ".modeloMotor", null, null, "save", null,null, null, null);
		getBinder().addBinding(claseMaquinaria, "seleccion",getNombreModelo() + ".clase", null, null, "save", null, null,null, null);
		getBinder().addBinding(tipoMaquinaria, "seleccion",	getNombreModelo() + ".tipo", null, null, "save", null, null,null, null);

		getBinder().addBinding(descripcion, "value",getNombreModelo() + ".descripcion", null, null, "save", null,null, null, null);
		getBinder().addBinding(peso, "value", getNombreModelo() + ".peso",	null, null, "save", null, null, null, null);
		getBinder().addBinding(motortipo, "value",	getNombreModelo() + ".motortipo", null, null, "save", null,	null, null, null);
		getBinder().addBinding(potencia, "value",getNombreModelo() + ".potencia", null, null, "save", null,null, null, null);
		getBinder().addBinding(fuerza, "value", getNombreModelo() + ".fuerza",null, null, "save", null, null, null, null);
		getBinder().addBinding(colorPrimario, "value",getNombreModelo() + ".colorPrimario", null, null, "save", null,null, null, null);
		getBinder().addBinding(colorSecundario, "value",getNombreModelo() + ".colorSecundario", null, null, "save",	null, null, null, null);
		getBinder().addBinding(serie, "value", getNombreModelo() + ".serie",null, null, "save", null, null, null, null);

		claseMaquinaria.setSeleccion(getModelo().getClase());
		tipoMaquinaria.setSeleccion(getModelo().getTipo());

		serie.setSeleccion(getModelo().getSerie());
		modelo.setSeleccion(getModelo().getModelo());
		motormodelo.setSeleccion(getModelo().getModeloMotor());

		detalles.setModel(new ListModelArray(detalleslista));
		
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		descripcion.setDisabled(true); 
		modelo.setDisabled(true);
		agregarDetalle.setDisabled(true);
		quitarDetalle.setDisabled(true);

		peso.setDisabled(true);
		motortipo.setDisabled(true);
		potencia.setDisabled(true);
		fuerza.setDisabled(true);
		colorPrimario.setDisabled(true);
		colorSecundario.setDisabled(true);
		serie.setDisabled(true);
		claseMaquinaria.setDisabled(true);
		tipoMaquinaria.setDisabled(true);
		motormodelo.setDisabled(true);

		
		getAceptar().setVisible(false);
	}

	public void modoEdicion() {
		descripcion.setDisabled(false);
		agregarDetalle.setDisabled(false);
		quitarDetalle.setDisabled(false);

	}
 

	public void agregardetalle(DetalleGarantina dt) {
		// TODO Auto-generated method stub
		detalleslista.add(dt);
		detalles.setModel(new ListModelArray(detalleslista));
	}

	public void quitardetalle(DetalleGarantina dt) {
		
		for(DetalleGarantina detalle: detalleslista)
		{
			if(dt.getDescripcion().equals(detalle.getDescripcion()))
			{
				detalleslista.remove(dt);
				break;
			}
		}
		detalles.setModel(new ListModelArray(detalleslista));
	}

	public DetalleGarantina getDatoSeleccionado() {
		// TODO Auto-generated method stub
		return (DetalleGarantina) (detalles.getSelectedItem() != null ? detalles
				.getSelectedItem().getValue() : null);
	}

	public CompBuscar<Modelo> getModeloMaquina() {
		return modelo;
	}

	public void setModeloMaquina(CompBuscar<Modelo> modelo) {
		this.modelo = modelo;
	}

	public CompBuscar<TipoMaquinariaVenta> getTipoMaquinaria() {
		return tipoMaquinaria;
	}

	public void setTipoMaquinaria(CompBuscar<TipoMaquinariaVenta> tipoMaquinaria) {
		this.tipoMaquinaria = tipoMaquinaria;
	}

	public CompBuscar<Serie> getSerie() {
		return serie;
	}

	public void setSerie(CompBuscar<Serie> serie) {
		this.serie = serie;
	}

	public List<Serie> getSeries() {
		return series;
	}

	public void setSeries(List<Serie> series) {
		this.series = series;
	}

	public Doublebox getPeso() {
		return peso;
	}

	public void setPeso(Doublebox peso) {
		this.peso = peso;
	}

	public Textbox getMotortipo() {
		return motortipo;
	}

	public void setMotortipo(Textbox motortipo) {
		this.motortipo = motortipo;
	}

	public Textbox getPotencia() {
		return potencia;
	}

	public void setPotencia(Textbox potencia) {
		this.potencia = potencia;
	}

	public Textbox getFuerza() {
		return fuerza;
	}

	public void setFuerza(Textbox fuerza) {
		this.fuerza = fuerza;
	}

	public Textbox getColorPrimario() {
		return colorPrimario;
	}

	public void setColorPrimario(Textbox colorPrimario) {
		this.colorPrimario = colorPrimario;
	}

	public Textbox getColorSecundario() {
		return colorSecundario;
	}

	public CompBuscar<Modelo> getMotormodelo() {
		return motormodelo;
	}

	public void setMotormodelo(CompBuscar<Modelo> motormodelo) {
		this.motormodelo = motormodelo;
	}

	public CompBuscar<ClaseMaquinaria> getClaseMaquinaria() {
		return claseMaquinaria;
	}

	public void setClaseMaquinaria(CompBuscar<ClaseMaquinaria> claseMaquinaria) {
		this.claseMaquinaria = claseMaquinaria;
	}

	public List<TipoMaquinariaVenta> getTipoMaquinarias() {
		return tipoMaquinarias;
	}

	public void setTipoMaquinarias(List<TipoMaquinariaVenta> tipoMaquinarias) {
		this.tipoMaquinarias = tipoMaquinarias;
	}

	public void setColorSecundario(Textbox colorSecundario) {
		this.colorSecundario = colorSecundario;
	}
 
	
	
	public List<DetalleGarantina> getDetalleslista() {
		return detalleslista;
	}

	public void setDetalleslista(List<DetalleGarantina> detalleslista) {
		this.detalleslista = detalleslista;
	}

	public void setmodooperacion(int modoOperacion) {
		// TODO Auto-generated method stub
		if(modoOperacion == Accion.CONSULTAR)
		{
			activarConsulta();
		}
		if(modoOperacion == Accion.ELIMINAR)
		{
			activarConsulta();
			getAceptar().setVisible(true);
		}
		
	}
	
	
	

}
