package cpc.demeter.vista.administrativo;

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

import cpc.ares.modelo.Accion; 
import cpc.demeter.controlador.administrativo.ContSeleccionarRecibo;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cva.pc.componentes.CompEncabezado;

public class UiSeleccionarRecibo extends Window {


	private Listbox 					recibos;
	private List<Recibo>				modelo;
	
	private CompGrupoDatos 				contenedor;
	private CompGrupoDatos 				contenedorBus;
	
	/****** Para la Busqeudad ***************/
	
	private Textbox 					busquedad;
	private Button 						buscar;
	private Combobox 					combobusquedad;
	
	private Button						 agregar;
	private Button						 aceptar;
	private Button						 cancelar;
	
	private ContSeleccionarRecibo	 	 controlador;
	
	public Combobox getCombobusquedad() {
		return combobusquedad;
	}

	public void setCombobusquedad(Combobox combobusquedad) {
		this.combobusquedad = combobusquedad;
	}


	public UiSeleccionarRecibo(String title, String border, boolean closable,ContSeleccionarRecibo controlador, List<Recibo> modelo) {
		super(title, border, closable);
	
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
			hbox.appendChild(agregar);
		contenedorBus.addComponente(hbox);
			contenedorBus.dibujar(this);
	
		Hbox botonera = new Hbox();
			botonera.appendChild(aceptar);
			botonera.appendChild(cancelar);
			
		contenedor.addComponente(recibos);
		contenedor.addComponente(botonera);
		contenedor.dibujar(this);
	}

	private void inicializar() {
		// TODO Auto-generated method stub 
		
		recibos = new Listbox();
		recibos.setMold("paging");
		recibos.setPageSize(12);
		recibos.setWidth("600px");
			/*****titulos de la Lista ****/
			Listhead titulo = new Listhead();
				new Listheader("Nro Recibo").setParent(titulo);
				new Listheader("Fecha").setParent(titulo);
				new Listheader("Saldo ").setParent(titulo);
				new Listheader("Monto ").setParent(titulo);
		
			titulo.setParent(recibos);
			recibos.setTooltiptext("haga doble Click para Seleccionar un Elemento");
			recibos.setItemRenderer(new ListitemRenderer() {
				@Override
				public void render(Listitem arg0, Object arg1) throws Exception {
					// TODO Auto-generated method stub
					arg0.setValue(arg1);
					arg0.addEventListener(Events.ON_DOUBLE_CLICK,controlador.seleccionarLista()); //para seleccionar un elemento de la lista
					Recibo recibo = (Recibo) arg1;
					
					new Listcell(recibo.getControl()).setParent(arg0);
					new Listcell(recibo.getStrFecha()).setParent(arg0);
					new Listcell(recibo.getStrSaldo()).setParent(arg0);
					new Listcell(recibo.getStrMonto()).setParent(arg0);
				}
			});
			recibos.setModel(new ListModelList(modelo));
			
		contenedor = new CompGrupoDatos(" Listado de Recibos ",1);
		contenedorBus = new CompGrupoDatos("Busquedad",1);
		
		busquedad = new Textbox();
			busquedad.setTooltiptext("Valor del Elemento Buscado ");
			
		buscar = new Button("Buscar");
			buscar.addEventListener(Events.ON_CLICK, controlador);
			new Comboitem("").setValue("0");
			
		combobusquedad = new Combobox();
	
		 Comboitem  comb = new Comboitem("Nro Recibo ");
				comb.setValue(0);
		 		comb.setParent(combobusquedad);
			 				
		 comb = new Comboitem("Fecha ");
				comb.setValue(1);
		 		comb.setParent(combobusquedad);
			 		
		 comb = new Comboitem("Saldo");
	 		comb.setValue(2);
	 		comb.setParent(combobusquedad);

	 	comb = new Comboitem("Monto");
		 		comb.setValue(3);
		 		comb.setParent(combobusquedad);

	 		
		aceptar = new Button("aceptar");
			aceptar.setTooltiptext("Carga el Recibo Seleccionado en la Lista");
			aceptar.addEventListener(Events.ON_CLICK,controlador);
			
		cancelar = new Button("cancelar");
			cancelar.setTooltiptext("Cancela la Busquedad");
			cancelar.addEventListener(Events.ON_CLICK,controlador);
			
		agregar = new Button("Agregar");
			agregar.setVisible(false);
			agregar.setTooltiptext("Agregar un Recibo a la Lista");
			agregar.addEventListener(Events.ON_CLICK,controlador);
			
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

	public List<Recibo> getModelo() {
		return modelo;
	}

	public void setModelo(List<Recibo> modelo) {
		this.modelo = modelo;
	}

	
	public void modoOperacion(Integer modoOperaion) {
		// TODO Auto-generated method stub
		if(modoOperaion == Accion.CONSULTAR)
		{
			agregar.setDisabled(true);
		}
	}
	
	public Listbox getRecibos() {
		return recibos;
	}

	public void setRecibos(Listbox recibos) {
		this.recibos = recibos;
	}

	public Button getAgregar() {
		return agregar;
	}

	public void setAgregar(Button agregar) {
		this.agregar = agregar;
	}
  
}
