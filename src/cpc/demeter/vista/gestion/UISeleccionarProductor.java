package cpc.demeter.vista.gestion;

import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cpc.demeter.comando.reporte.ContSeleccionarProductor;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.zk.componente.ventanas.CompGrupoDatos;

public class UISeleccionarProductor  extends Window {


	private Listbox 					productores;
	private List<Productor>				modelo;
	private CompGrupoDatos 				contenedor;
	private CompGrupoDatos 				contenedorBus;
	
	/****** Para la Busqeudad ***************/
	
	private Textbox 					busquedad;
	private Button 						buscar;
	private Combobox 					combobusquedad;
	
	private Button						 aceptar;
	private Button						 cancelar;
	private ContSeleccionarProductor	  controlador;
	
	public Combobox getCombobusquedad() {
		return combobusquedad;
	}

	public void setCombobusquedad(Combobox combobusquedad) {
		this.combobusquedad = combobusquedad;
	}


	public UISeleccionarProductor(String title, String border, boolean closable,ContSeleccionarProductor controlador, List<Productor> modelo) {
		super(title, border, closable);
		this.setWidth("850px");
		this.controlador = controlador;
		this.setModelo(modelo);
		
		inicializar();
		dibujar();
	}

	private void dibujar() {
		// TODO Auto-generated method stub
		
		Hbox hbox = new Hbox();
			hbox.appendChild(new Label("Busquedad"));
			hbox.appendChild(combobusquedad);
			hbox.appendChild(busquedad);
			hbox.appendChild(buscar);
 
		contenedorBus.addComponente(hbox);
			contenedorBus.dibujar(this);
	
		Hbox botonera = new Hbox();
			botonera.appendChild(aceptar);
			botonera.appendChild(cancelar);
			
		contenedor.addComponente(productores);
		contenedor.addComponente(botonera);
		contenedor.dibujar(this);
	}

	private void inicializar() {
		// TODO Auto-generated method stub 
		
		
		productores = new Listbox();
		productores.setMold("paging");
		productores.setPageSize(12);
		productores.setWidth("100%");
			/*****titulos de la Lista ****/
 
			Listhead titulo = new Listhead();
				new Listheader("Identidad").setParent(titulo);
				new Listheader("Nombre ").setParent(titulo);
				new Listheader("Direccion").setParent(titulo);

			titulo.setParent(productores);
			productores.setTooltiptext("haga doble Click para Seleccionar un Elemento");
			productores.setItemRenderer(new ListitemRenderer() {
				@Override
				public void render(Listitem arg0, Object arg1) throws Exception {
					// TODO Auto-generated method stub
					arg0.setValue(arg1);
					arg0.addEventListener(Events.ON_DOUBLE_CLICK,controlador.seleccionarLista()); //para seleccionar un elemento de la lista
	 				Productor p = (Productor) arg1;
					new Listcell(p.getIdentidadLegal()).setParent(arg0);
					new Listcell(p.getNombres()).setParent(arg0);
					new Listcell(p.getDireccion()).setParent(arg0);
					
					
				}
			});
			productores.setModel(new ListModelList(modelo));
			
		contenedor = new CompGrupoDatos(" Listado de Productores ",1);
		contenedorBus = new CompGrupoDatos("Busquedad",1);
		
		
		busquedad = new Textbox();
			busquedad.setTooltiptext("Valor del Elemento Buscado ");
			
			
		buscar = new Button("Buscar");
			buscar.addEventListener(Events.ON_CLICK, controlador);
			new Comboitem("").setValue("0");
			
			
		combobusquedad = new Combobox();
	
		 Comboitem  comb = 	new Comboitem("identidad");
 		 comb.setValue(0);
  		 comb.setParent(combobusquedad);

  		 comb = new Comboitem("Nombre");
		 comb.setValue(1);
		 comb.setParent(combobusquedad);
			 		
		 comb = new Comboitem("Direccion");
	 	 comb.setValue(2);
	 	 comb.setParent(combobusquedad);
			 		
			
		aceptar = new Button("aceptar");
			aceptar.setTooltiptext("Carga Productor  Seleccionado en la Lista");
			aceptar.addEventListener(Events.ON_CLICK,controlador);
			
			
		cancelar = new Button("cancelar");
			cancelar.setTooltiptext("Cancela la Busquedad");
			cancelar.addEventListener(Events.ON_CLICK,controlador);
			
 
	}

	public Button getAceptar() {
		return aceptar;
	}

	public void setAceptar(Button aceptar) {
		this.aceptar = aceptar;
	} 
	 

	public Textbox getBusquedad() {
		return busquedad;
	}

	public void setBusquedad(Textbox busquedad) {
		this.busquedad = busquedad;
	}

	public Button getBuscar() {
		return buscar;
	}

	public void setBuscar(Button buscar) {
		this.buscar = buscar;
	}

	public Button getCancelar() {
		return cancelar;
	}

	public void setCancelar(Button cancelar) {
		this.cancelar = cancelar;
	}

	public List<Productor> getModelo() {
		return modelo;
	}

	public void setModelo(List<Productor> modelo) {
		this.modelo = modelo;
	}
 
	public void modoOperacion(Integer modoOperaion) {
		// TODO Auto-generated method stub
	 
	}

 
	public Listbox getProductores() {
		return productores;
	}

	public void setProductores(Listbox productores) {
		this.productores = productores;
	}
 
	
	
}
