package cpc.demeter.vista.gestion;


import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.Sede;
import cpc.demeter.AppDemeter;
import cpc.modelo.demeter.gestion.EstadoMaquinariaImpropia;
import cpc.modelo.demeter.gestion.MaquinariaImpropia;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIMaquinariaImpropiaProcesar extends CompVentanaBase<MaquinariaImpropia> {

	private static final long serialVersionUID = -3572524741697517560L;
	
	private Textbox			serialChasis;
	private Textbox			serialOtro;
	private Textbox			procedencia;
	private Textbox			nombre;
	 
   	private Textbox			marca;
   	private Textbox    		modeloMaquinaria;
   	
   	private Textbox 		categoria;
   	private Textbox 		tipo;
   	private Textbox 		unidad;
   	private Label			lblEstadoInfo;
   	private Textbox			observacion;
   	
   	private List estados = new ArrayList();
   	private Combobox 	estado = new Combobox();
   	
   	private CompGrupoDatos  contenedor;
   	private CompGrupoDatos  contenedorEstado;

   	
   	
   	private int operacion;
   	
   	public UIMaquinariaImpropiaProcesar(String titulo, int ancho, AppDemeter app,int operacion, List estados)
	{	// TODO Auto-generated constructor stub
		super(titulo,ancho);
		this.operacion = operacion;
		this.estados = estados;
		setWidth("70%");
		setMaximizable(true);
		
	}

	@Override
	public void cargarValores(MaquinariaImpropia arg0) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		
		serialChasis.setValue(getModelo().getSerialChasis());
		serialOtro.setValue(getModelo().getSerialOtro());
		procedencia.setValue(getModelo().getProcedencia());
		nombre.setValue(getModelo().getNombre());
		marca.setValue(getModelo().getMarca().getDescripcion());
		modeloMaquinaria.setValue(getModelo().getModelo().getDescripcionModelo());
		categoria.setValue(getModelo().getCategoria().getDescripcionCategoria());
		tipo.setValue(getModelo().getTipo().getDescripcionTipo());
		unidad.setValue(getModelo().getMaquinariaUnidad().getUnidad().getDescripcion());
		observacion.setValue(getModelo().getObservacion());
		
		
		Comboitem item = new Comboitem();
		item.setValue(getModelo().getEstado());
		item.setLabel(getModelo().getEstado().getDescripcion());

		getBinder().addBinding(serialChasis,"value",getNombreModelo()+".serialChasis",null, null, "save",null,null,null,null);
		getBinder().addBinding(serialOtro,"value",getNombreModelo()+".serialOtro",null, null, "save",null,null,null,null);
		getBinder().addBinding(observacion,"value",getNombreModelo()+".observacion",null, null, "save",null,null,null,null);

		
	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub
		
		contenedor.addComponente("Serial Chasis ",serialChasis);
		contenedor.addComponente("Serial Otro  ",serialOtro);

		contenedor.addComponente(new Label("Marca "));
		contenedor.addComponente(marca);
		
		contenedor.addComponente(new Label("Modelo "));
		contenedor.addComponente(modeloMaquinaria);

		contenedor.addComponente(new Label("Categoria "));
		contenedor.addComponente(categoria);
		
		contenedor.addComponente(new Label(" tipo "));
		contenedor.addComponente(tipo);

		contenedor.addComponente(" Nombre Maquinaria  ", nombre);
		contenedor.addComponente(" Procedencia  Maquinaria ",procedencia);
		contenedor.addComponente(" Unidad : ",unidad);

		contenedor.dibujar(this);
		if(operacion == Accion.ANULAR)
		{
			contenedorEstado.addComponente("Estado Maquinaria",estado);
			contenedorEstado.addComponente("",lblEstadoInfo);
			contenedorEstado.addComponente("Observacion :" ,observacion);
			contenedorEstado.dibujar(this);
		}
		addBotonera();
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub

		contenedor = new CompGrupoDatos("Datos Maquinaria ",4);
		contenedorEstado = new CompGrupoDatos("Cambiar Estado Maquinaria ");

		serialChasis = new Textbox();
		serialChasis.setMaxlength(35);
		serialChasis.addEventListener(Events.ON_BLUR,new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				serialChasis.setValue(serialChasis.getText().toUpperCase().trim());
			}
		});

		serialOtro = new Textbox();
		serialOtro.setMaxlength(35);
		serialOtro.addEventListener(Events.ON_BLUR,new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				serialOtro.setValue(serialOtro.getText().toUpperCase().trim());
			}
		});
		

		observacion  = new Textbox();
		observacion.setRows(3);
		observacion.setMaxlength(255);
		
		procedencia = new Textbox();
		procedencia.setDisabled(true);

		nombre = new Textbox();
		nombre.setDisabled(true);

		marca = new Textbox();
		marca.setDisabled(true);

		modeloMaquinaria = new Textbox();
		modeloMaquinaria.setDisabled(true);

		categoria= new Textbox();
		categoria.setDisabled(true);

		tipo= new Textbox();
		tipo.setDisabled(true);
		
		unidad = new Textbox();
		unidad.setDisabled(true);
		
		
		estado = new Combobox();
		estado.setItemRenderer(new ComboitemRenderer() {
			
			@Override
			public void render(Comboitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				
				EstadoMaquinariaImpropia estado = (EstadoMaquinariaImpropia) arg1;
				arg0.setLabel(estado.getDescripcion());
				arg0.setValue(arg1);
				
				if(estado.getId().equals(EstadoMaquinariaImpropia.MIGRADO))
				{
					arg0.setTooltiptext("Maquinaria Convertida a Bien Nacional ");
				}
				else if(estado.getId().equals(EstadoMaquinariaImpropia.DESACTIVADO))
				{
					arg0.setTooltiptext("Maquinaria Desactivada ");
				}
				else if(estado.getId().equals(EstadoMaquinariaImpropia.DESINCORPORADO))
				{
					arg0.setTooltiptext("Maquinaria No Operativa ");
				}
			}
		});
		estado.addEventListener(Events.ON_SELECT,getControlador());
		estado.setModel(new ListModelArray(estados));
		lblEstadoInfo = new Label("-----");
		lblEstadoInfo.setStyle("color: #B4886B; font-weight: bold; width: 150px;");

	}

	public Textbox getSerialChasis() {
		return serialChasis;
	}

	public void setSerialChasis(Textbox serialChasis) {
		this.serialChasis = serialChasis;
	}

	public Textbox getSerialOtro() {
		return serialOtro;
	}

	public void setSerialOtro(Textbox serialOtro) {
		this.serialOtro = serialOtro;
	}

	public Textbox getProcedencia() {
		return procedencia;
	}

	public void setProcedencia(Textbox procedencia) {
		this.procedencia = procedencia;
	}

	public Textbox getNombre() {
		return nombre;
	}

	public void setNombre(Textbox nombre) {
		this.nombre = nombre;
	}
 

	public Textbox getMarca() {
		return marca;
	}

	public void setMarca(Textbox marca) {
		this.marca = marca;
	}
 

	public Textbox getModeloMaquinaria() {
		return modeloMaquinaria;
	}

	public void setModeloMaquinaria(Textbox modeloMaquinaria) {
		this.modeloMaquinaria = modeloMaquinaria;
	}
 
	public Textbox getCategoria() {
		return categoria;
	}

	public void setCategoria(Textbox categoria) {
		this.categoria = categoria;
	}

	 
	public Textbox getTipo() {
		return tipo;
	}

	public void setTipo(Textbox tipo) {
		this.tipo = tipo;
	}

	public CompGrupoDatos getContenedor() {
		return contenedor;
	}

	public void setContenedor(CompGrupoDatos contenedor) {
		this.contenedor = contenedor;
	}

	public void desactivar(int modoOperacion) {
		// TODO Auto-generated method stub
		procedencia.setDisabled(true);
		nombre.setDisabled(true);
		marca.setDisabled(true);
		modeloMaquinaria.setDisabled(true);
		categoria.setDisabled(true);
		tipo.setDisabled(true);
		
		if(modoOperacion== Accion.ANULAR)
		{
			serialChasis.setDisabled(true);
			serialOtro.setDisabled(true);
		}
		
		
	}

	public Label getLblEstadoInfo() {
		return lblEstadoInfo;
	}

	public void setLblEstadoInfo(Label lblEstadoInfo) {
		this.lblEstadoInfo = lblEstadoInfo;
	}

	public Combobox getEstado() {
		return estado;
	}

	public void setEstado(Combobox estado) {
		this.estado = estado;
	}
	
	

	
}