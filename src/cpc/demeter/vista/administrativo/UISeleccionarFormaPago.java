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

import cpc.demeter.controlador.administrativo.ContSeleccionarFormaPago;
import cpc.demeter.controlador.administrativo.ContSeleccionarRecibo;
import cpc.modelo.demeter.administrativo.FormaPago;
import cpc.modelo.demeter.administrativo.FormaPagoCheque;
import cpc.modelo.demeter.administrativo.FormaPagoEfectivo;
import cpc.zk.componente.ventanas.CompGrupoDatos;

public class UISeleccionarFormaPago extends Window {


	private Listbox 					formapagos;
	private List<FormaPago>				modelo;
	
	private CompGrupoDatos 				contenedor;
	private CompGrupoDatos 				contenedorBus;
	
	/****** Para la Busqeudad ***************/
	
	private Textbox 					busquedad;
	private Button 						buscar;
	private Combobox 					combobusquedad;
	
	private Button						 aceptar;
	private Button						 cancelar;
	
	private ContSeleccionarFormaPago	 	 controlador;
	
	public Combobox getCombobusquedad() {
		return combobusquedad;
	}

	public void setCombobusquedad(Combobox combobusquedad) {
		this.combobusquedad = combobusquedad;
	}


	public UISeleccionarFormaPago(String title, String border, boolean closable,ContSeleccionarFormaPago controlador, List modelo) {
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

		contenedorBus.addComponente(hbox);
		contenedorBus.dibujar(this);
	
		Hbox botonera = new Hbox();
			botonera.appendChild(aceptar);
			botonera.appendChild(cancelar);
			
		contenedor.addComponente(formapagos);
		contenedor.addComponente(botonera);
		contenedor.dibujar(this);
	}

	private void inicializar() {
		// TODO Auto-generated method stub 
		
		formapagos = new Listbox();
		formapagos.setMold("paging");
		formapagos.setPageSize(12);
		formapagos.setWidth("600px");
			/*****titulos de la Lista ****/
			Listhead titulo = new Listhead();
				new Listheader("Nro Recibo").setParent(titulo);
				new Listheader("Fecha ").setParent(titulo);
				new Listheader("Tipo Forma Pago ").setParent(titulo);
				new Listheader("Datos Forma ").setParent(titulo);
				new Listheader("Monto ").setParent(titulo);

				
				
			titulo.setParent(formapagos);
			formapagos.setTooltiptext("haga doble Click para Seleccionar un Elemento");
			formapagos.setItemRenderer(new ListitemRenderer() {
				@Override
				public void render(Listitem arg0, Object arg1) throws Exception {
					// TODO Auto-generated method stub
					arg0.setValue(arg1);
					arg0.addEventListener(Events.ON_DOUBLE_CLICK,controlador.seleccionarLista()); //para seleccionar un elemento de la lista
					
					if (arg1.getClass().equals(FormaPagoCheque.class))
					{
						FormaPagoCheque formaPagoCheque = (FormaPagoCheque) arg1;
						
						new Listcell(formaPagoCheque.getRecibo().getControl()).setParent(arg0);
						new Listcell(formaPagoCheque.getStrFecha()).setParent(arg0);
						new Listcell(formaPagoCheque.getTipoFormaPago().getDescripcion()).setParent(arg0);
						
						String detalle = new String();
						
						detalle = formaPagoCheque.detalle(); 
						new Listcell(detalle).setParent(arg0);
						new Listcell(formaPagoCheque.getStrMonto()).setParent(arg0);

					}
					if (arg1.getClass().equals(FormaPagoEfectivo.class))
					{
						FormaPagoEfectivo formaPagoEfectivo = (FormaPagoEfectivo) arg1;
						
						new Listcell(formaPagoEfectivo.getRecibo().getControl()).setParent(arg0);
						new Listcell(formaPagoEfectivo.getStrFecha()).setParent(arg0);
						new Listcell(formaPagoEfectivo.getTipoFormaPago().getDescripcion()).setParent(arg0);
						
						String detalle = new String();
						
						detalle = "PAGO EFECTIVO ";
						new Listcell(detalle).setParent(arg0);
						new Listcell(formaPagoEfectivo.getStrMonto()).setParent(arg0);
					}
				}
			});
			formapagos.setModel(new ListModelList(modelo));
			
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
		 		
		comb = new Comboitem("Tipo Forma Pago ");
	 	comb.setValue(2);
	 	comb.setParent(combobusquedad);

	 	comb = new Comboitem("Datos Forma ");
		comb.setValue(3);
		comb.setParent(combobusquedad);
	 		
		comb = new Comboitem("Monto");
		comb.setValue(4);
		comb.setParent(combobusquedad);

		aceptar = new Button("aceptar");
			aceptar.setTooltiptext("Carga la Forma Seleccionado en la Lista");
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

	public List<FormaPago> getModelo() {
		return modelo;
	}

	public void setModelo(List<FormaPago> modelo) {
		this.modelo = modelo;
	}

	
	public void modoOperacion(Integer modoOperaion) {
		// TODO Auto-generated method stub
	}
	
	public Listbox getFormapagos() {
		return formapagos;
	}

	public void setFormapagos(Listbox formapagos) {
		this.formapagos = formapagos;
	}


	
}
