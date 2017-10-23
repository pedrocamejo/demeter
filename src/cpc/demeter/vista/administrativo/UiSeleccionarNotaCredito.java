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
import cpc.demeter.controlador.administrativo.ContSeleccionarNotaCredito;
import cpc.modelo.demeter.administrativo.NotaCredito;
 
import cpc.zk.componente.ventanas.CompGrupoDatos;

public class UiSeleccionarNotaCredito  extends Window {


	private Listbox 					notasCredito;
	private List<NotaCredito>	 		modelo;

	private CompGrupoDatos 				contenedor;
	private CompGrupoDatos 				contenedorBus;
	
	/****** Para la Busqeudad ***************/
	
	private Textbox 					busquedad;
	private Button 						buscar;
	private Combobox 					combobusquedad;
	
	private Button						 agregar;
	private Button						 aceptar;
	private Button						 cancelar;
	
	private ContSeleccionarNotaCredito	 	 controlador;
	
	public Combobox getCombobusquedad() {
		return combobusquedad;
	}

	public void setCombobusquedad(Combobox combobusquedad) {
		this.combobusquedad = combobusquedad;
	}


	public UiSeleccionarNotaCredito(String title, String border, boolean closable,ContSeleccionarNotaCredito controlador, List<NotaCredito> modelo) {
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
			
		contenedor.addComponente(notasCredito);
		contenedor.addComponente(botonera);
		contenedor.dibujar(this);
	}

	private void inicializar() {
		// TODO Auto-generated method stub 
		
		notasCredito = new Listbox();
		notasCredito.setMold("paging");
		notasCredito.setPageSize(12);
		notasCredito.setWidth("600px");
			/*****titulos de la Lista ****/
			Listhead titulo = new Listhead();
				new Listheader("Nro Nota Credito").setParent(titulo);
				new Listheader("Factura Afec.").setParent(titulo);
				new Listheader("Fecha ").setParent(titulo);
				new Listheader("Monto Total ").setParent(titulo);
				new Listheader("Por Pagar ").setParent(titulo);
		
			titulo.setParent(notasCredito);
			notasCredito.setTooltiptext("haga doble Click para Seleccionar un Elemento");
			notasCredito.setItemRenderer(new ListitemRenderer() {
				@Override
				public void render(Listitem arg0, Object arg1) throws Exception {
					// TODO Auto-generated method stub
					arg0.setValue(arg1);
					NotaCredito nota = (NotaCredito) arg1;
					new Listcell(nota.getStrNroDocumento()).setParent(arg0);
					new Listcell(nota.getStrFacturaAfectada()).setParent(arg0);
					new Listcell(nota.getStrFecha()).setParent(arg0);
					new Listcell(nota.getStrTotal()).setParent(arg0);
					new Listcell(nota.getStrSaldo()).setParent(arg0);
				}
			});
			notasCredito.setModel(new ListModelList(modelo));
			
		contenedor = new CompGrupoDatos(" Listado de Notas de Credito ",1);
		contenedorBus = new CompGrupoDatos("Busquedad",1);
		
		busquedad = new Textbox();
			busquedad.setTooltiptext("Valor del Elemento Buscado ");
			
		buscar = new Button("Buscar");
			buscar.addEventListener(Events.ON_CLICK, controlador);
			new Comboitem("").setValue("0");
			
		combobusquedad = new Combobox();
	
		 Comboitem  comb = new Comboitem("Nro Nota Credito");
				comb.setValue(0);
		 		comb.setParent(combobusquedad);
			 				
		 comb = new Comboitem("Factura Afec. ");
				comb.setValue(1);
		 		comb.setParent(combobusquedad);
			 		
		 comb = new Comboitem("Fecha ");
	 		comb.setValue(2);
	 		comb.setParent(combobusquedad);

	 	comb = new Comboitem("Monto Total");
		 		comb.setValue(3);
		 		comb.setParent(combobusquedad);

	 	comb = new Comboitem("Por Pagar ");
		 		comb.setValue(4);
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

	public List<NotaCredito> getModelo() {
		return modelo;
	}

	public void setModelo(List<NotaCredito> modelo) {
		this.modelo = modelo;
	}

	
	public void modoOperacion(Integer modoOperaion) {
		// TODO Auto-generated method stub
		if(modoOperaion == Accion.CONSULTAR)
		{
			agregar.setDisabled(true);
		}
	}

	
	public Listbox getNotasCredito() {
		return notasCredito;
	}

	public void setNotasCredito(Listbox notasCredito) {
		this.notasCredito = notasCredito;
	}

	public Button getAgregar() {
		return agregar;
	}

	public void setAgregar(Button agregar) {
		this.agregar = agregar;
	}
  
}
