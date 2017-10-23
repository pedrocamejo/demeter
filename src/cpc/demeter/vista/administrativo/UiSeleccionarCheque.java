package cpc.demeter.vista.administrativo;


import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
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
import cpc.demeter.controlador.administrativo.ContSeleccionarCheque;
import cpc.modelo.demeter.administrativo.Cheque;
import cpc.zk.componente.ventanas.CompGrupoDatos;


public class UiSeleccionarCheque extends Window {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Listbox 					cheques;
	private List<Cheque>				modelo;
	private CompGrupoDatos 				contenedor;
	private CompGrupoDatos 				contenedorBus;
	private Textbox 					busquedad;
	private Button 						buscar;
	private Combobox 					combobusquedad;
	private Button						agregar;
	private Button						 aceptar;
	private Button						 cancelar;
	private ContSeleccionarCheque	 	 controlador;

	
	public UiSeleccionarCheque(String title, String border, boolean closable,ContSeleccionarCheque controlador, List<Cheque> modelo) {
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
	

		Div div = new Div();
		div.setAlign("right");
		Hbox botonera = new Hbox();
		botonera.appendChild(aceptar);
		botonera.appendChild(cancelar);
		div.appendChild(botonera);
		
		contenedor.addComponente(cheques);
		contenedor.addComponente(div);
		contenedor.dibujar(this);
	}

	private void inicializar() {
		// TODO Auto-generated method stub 
		
		
		cheques = new Listbox();
		cheques.setMold("paging");
		cheques.setPageSize(12);
		cheques.setWidth("98%");
		cheques.setSclass("tabla_catalago");
			/*****titulos de la Lista ****/
		Listhead titulo = new Listhead();
		new Listheader("id").setParent(titulo);
		new Listheader("Nro Cheque ").setParent(titulo);
		new Listheader("Nro Cuenta ").setParent(titulo);
		new Listheader("Monto ").setParent(titulo);
		titulo.setParent(cheques);
		cheques.setItemRenderer(new ListitemRenderer() {
				@Override
				public void render(Listitem arg0, Object arg1) throws Exception {
					// TODO Auto-generated method stub
					arg0.setValue(arg1);
					arg0.addEventListener(Events.ON_DOUBLE_CLICK,controlador.seleccionarLista());
					Cheque cheque = (Cheque) arg1;
					new Listcell(cheque.getId().toString()).setParent(arg0);
					new Listcell(cheque.getNroCheque()).setParent(arg0);
					new Listcell(cheque.getNroCuenta()).setParent(arg0);
					new Listcell(cheque.getMonto().toString()).setParent(arg0);
				}
			});
		cheques.setModel(new ListModelList(modelo));
			
		contenedor = new CompGrupoDatos(" Listado de Cheques ",1);
		contenedor.setAlign("right");
		contenedorBus = new CompGrupoDatos("Busquedad",1);
		
		
		busquedad = new Textbox();
		busquedad.setTooltiptext("Buscar  ");
			
			
		buscar = new Button("Buscar");
		buscar.addEventListener(Events.ON_CLICK, controlador);
		new Comboitem("").setValue("0");
			
			
		combobusquedad = new Combobox();
	
		Comboitem  comb = 	new Comboitem("id");
 		comb.setValue(0);
 		comb.setParent(combobusquedad);

 		comb = new Comboitem("Nro Cheque ");
		comb.setValue(1);
		comb.setParent(combobusquedad);
			 				
		comb = new Comboitem("Nro Cuenta");
		comb.setValue(2);
		comb.setParent(combobusquedad);
			 		
		comb = new Comboitem("Monto");
	 	comb.setValue(3);
	 	comb.setParent(combobusquedad);
			 		
			
		aceptar = new Button("Seleccionar");
		aceptar.addEventListener(Events.ON_CLICK,controlador);
		aceptar.setSclass("seleccionar_boton");

		cancelar = new Button("Cancelar");
		cancelar.setSclass("seleccionar_boton");
		cancelar.addEventListener(Events.ON_CLICK,controlador);
		
		
		agregar = new Button("Agregar Nuevo Registro");
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

	public List<Cheque> getModelo() {
		return modelo;
	}

	public void setModelo(List<Cheque> modelo) {
		this.modelo = modelo;
	}

	public Listbox getCheques() {
		return cheques;
	}

	public void setCheques(Listbox cheques) {
		this.cheques = cheques;
	}
	
	public Combobox getCombobusquedad() {
		return combobusquedad;
	}

	public void setCombobusquedad(Combobox combobusquedad) {
		this.combobusquedad = combobusquedad;
	}


	public void modoOperacion(Integer modoOperaion) {
		// TODO Auto-generated method stub
		if(modoOperaion == Accion.CONSULTAR)
		{
			agregar.setDisabled(true);
		}
	}

	public Button getAgregar() {
		return agregar;
	}

	public void setAgregar(Button agregar) {
		this.agregar = agregar;
	}
 

}
