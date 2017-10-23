package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.jfree.ui.Align;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox; 
import org.zkoss.zul.Vbox;
 

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.NotaCredito;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.demeter.administrativo.Reintegro;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIReintegro extends CompVentanaBase<Reintegro> {
	
	private Textbox 			     control;
	private CompGrupoDatos		     contenedor;
	private CompGrupoDatos 			 datosreinegro;
	private CompGrupoDatos 			 datosNotaCredito;
	private CompGrupoDatos		     datosCliente;
	private CompGrupoDatos 			 pie;
	
	private Textbox 				 cedula;
	private Textbox				 	 nombre;
	
	private List<Recibo> 			 recibos = new ArrayList<Recibo>();
	private Listbox				     listRecibos;
	private Combobox 			     tipo; //de nota de credito o de recibo 
	private Button 				     seleccionarCliente;
 
	
	//Operaciones Sobre el Recibo 
	private Button 					agregar;
	private Button 					quitar;
	private Doublebox 				montoTotal;
	
	//Datos del la Nota de Credito
	
	private Textbox 				factura;
	private Textbox 				nroNotaCredito;
	private Textbox 				totalNota; // monto TOtal Nota Credito 
	private Textbox 				porpagar;
	
	private Comboitem				ItemNull;
	
	//cargar Nota de Credito 
	private Button 					notaCredito;
	
	
	public UIReintegro( String titulo, int ancho)	throws ExcDatosInvalidos
	{
		super(titulo, ancho);
	}

	public void inicializar() {

		control = new Textbox();
		control.setDisabled(true);
		
		seleccionarCliente = new Button("");
		seleccionarCliente.setImage("iconos/32x32/productor.png");
		
		seleccionarCliente.setTooltiptext("Seleccionar 1 Cliente");
		seleccionarCliente.addEventListener(Events.ON_CLICK,getControlador());
		datosreinegro = new CompGrupoDatos();
		datosNotaCredito = new CompGrupoDatos("Datos Nota Credito ",4);
		contenedor = new CompGrupoDatos("Datos Generales ",4);
		datosCliente = new CompGrupoDatos("Datos Cliente",6);
	  
	    cedula = new Textbox();
	    cedula.setDisabled(true);
	    nombre = new Textbox();
	    nombre.setDisabled(true);
	    
	 	tipo = new Combobox();
	 	
	 	
	 	Comboitem item = new Comboitem("Recibos");
	 	item.setValue(Reintegro.REINTEGRO_RECIBOS);
	 	tipo.appendChild(item);
	 	 
	 	item = new Comboitem("Nota Credito");
	 	item.setValue(Reintegro.REINTEGRO_CREDITO);
	 	tipo.appendChild(item);

	 	ItemNull = new Comboitem(" Seleccionar ");
	 	ItemNull.setValue(0);
	 	tipo.appendChild(ItemNull);

	 	tipo.addEventListener(Events.ON_SELECT,getControlador());
	 	
	 	agregar = new Button();
	 	agregar.setTooltiptext("Agregar Recibo ");
	 	agregar.addEventListener(Events.ON_CLICK,getControlador());
	 	agregar.setImage("iconos/24x24/agregar.png");
	 	
	 	quitar =new Button();
	 	quitar.setTooltiptext("Quitar un Recibo ");
	 	quitar.addEventListener(Events.ON_CLICK,getControlador());
	 	quitar.setImage("iconos/24x24/quitar.png");
	 	
	 	pie = new CompGrupoDatos(3);
	 	pie.setAnchoColumna(0,250);
	 	
	 	montoTotal = new Doublebox();
	 	montoTotal.setDisabled(true);
	 	montoTotal.setFormat("###,###,###,###0.00");
	 	

		listRecibos = new Listbox();
		listRecibos.setMold("paging");
		listRecibos.setPageSize(12);
			/*****titulos de la Lista ****/
			Listhead titulo = new Listhead();
				new Listheader("Nro Recibo").setParent(titulo);
				new Listheader("Fecha").setParent(titulo);
				new Listheader("Monto ").setParent(titulo);
				new Listheader("Saldo ").setParent(titulo);

			titulo.setParent(listRecibos);
			listRecibos.setTooltiptext("haga doble Click Detallar un Elemento ");
			listRecibos.setItemRenderer(new ListitemRenderer() {
				@Override
				public void render(Listitem arg0, Object arg1) throws Exception {
					// TODO Auto-generated method stub
					arg0.setValue(arg1);
					Recibo recibo = (Recibo) arg1;
					new Listcell(recibo.getControl()).setParent(arg0);
					new Listcell(recibo.getStrFecha()).setParent(arg0);
					new Listcell(recibo.getStrMonto()).setParent(arg0);
					new Listcell(recibo.getStrSaldo()).setParent(arg0);
				}
			});
			listRecibos.setModel(new ListModelList(recibos));
	 	
			
			factura = new Textbox();
			nroNotaCredito= new Textbox();
			totalNota= new Textbox(); // monto TOtal Nota Credito 
			porpagar= new Textbox();
			
			
	}
	
	
	public void dibujar() {
		
		notaCredito  = new Button("Nota Credito ");
		notaCredito.setTooltiptext("Seleccionar una Nota de Credito");
		notaCredito.addEventListener(Events.ON_CLICK,getControlador());

		contenedor.addComponente("Nro Control ",control);
		contenedor.addComponente("Tipo de Reintegro :",tipo);
		
		
		
		Div cont = new Div();
		
		cont.setAlign("right");
		cont.appendChild(seleccionarCliente);
		
		datosCliente.appendChild(cont);
		datosCliente.addComponente("Cedula :",cedula);
		datosCliente.addComponente("Nombre ",nombre);
		datosCliente.addComponente("",cont);
		
		pie.addComponente(new Label());
		pie.addComponente("Monto Total ",montoTotal);
		
		datosCliente.dibujar(this);
		contenedor.dibujar(this); 
		pie.dibujar(this);
		addBotonera();
	}
 


	public void desactivar(int modoOperacion) {

		if (modoOperacion == Accion.CONSULTAR )
		{
			//activar modo consulta 
			activarConsulta();
		}
		else if (modoOperacion == Accion.AGREGAR )
		{
			tipo.setSelectedItem(ItemNull);
		}
		
	}

	
	private void activarConsulta() {
		// TODO Auto-generated method stub
	
		getAceptar().setVisible(false);
		getCancelar().setVisible(false);
		tipo.setDisabled(true);
		tipo.setText(getModelo().getStrTipo());
		seleccionarCliente.setVisible(false);
		
		if (getModelo().getTipo() == Reintegro.REINTEGRO_RECIBOS)
		{
			recibos = new ArrayList<Recibo>(getModelo().getRecibos());
			listRecibos.setModel(new ListModelList(recibos));
			datosreinegro.addComponente(listRecibos);
			datosreinegro.setAnchoColumna(0,800);
			tipo.setDisabled(true);
			datosreinegro.dibujar(contenedor);
		}
		else if (getModelo().getTipo()  == Reintegro.REINTEGRO_CREDITO)
		{
			datosNotaCredito.addComponente("Nota Credito :",nroNotaCredito);
			datosNotaCredito.addComponente("Factura Afect. :",factura);
			datosNotaCredito.addComponente("Total Nota   :",totalNota);
			datosNotaCredito.addComponente("Por Pagar    :",porpagar);
			datosNotaCredito.setAnchoColumna(0,100);
			datosNotaCredito.dibujar(contenedor);
			datosNotaCredito.appendChild(notaCredito);

			factura.setText(getModelo().getNotaCredito().getStrFacturaAfectada());
			factura.setDisabled(true);
			
			nroNotaCredito.setText(getModelo().getNotaCredito().getStrNroDocumento());
			nroNotaCredito.setDisabled(true);
			
			totalNota.setText(getModelo().getNotaCredito().getStrTotal());
			totalNota.setDisabled(true);
			
			porpagar.setText(getModelo().getNotaCredito().getStrSaldo());
			porpagar.setDisabled(true);
			
			notaCredito.setVisible(false);
			
		}
		
	}

	public Combobox getTipo() {
		return tipo;
	}

	public void setTipo(Combobox tipo) {
		this.tipo = tipo;
	}

	public Button getSeleccionarCliente() {
		return seleccionarCliente;
	}

	public void setSeleccionarCliente(Button seleccionarCliente) {
		this.seleccionarCliente = seleccionarCliente;
	}

	public Button getNotaCredito() {
		return notaCredito;
	}

	public void setNotaCredito(Button notaCredito) {
		this.notaCredito = notaCredito;
	}

	@Override
	public void cargarValores(Reintegro dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		control.setText(dato.getControl());
		montoTotal.setValue(getModelo().getMontoTotal());

		getBinder().addBinding(montoTotal, "value",getNombreModelo() + ".montoTotal", null, null, "save", null,null, null, null);
		getBinder().addBinding(tipo,"selectedItem",getNombreModelo() + ".tipo", null, null, "save", null,null, null, null);
		
		tipo.setText((getModelo().getTipo() == Reintegro.REINTEGRO_RECIBOS? "Recibos":"Nota Credito"));
		
		if(getModelo().getCliente() != null)
		{
			cedula.setText(getModelo().getCliente().getIdentidadLegal());
			nombre.setText(getModelo().getCliente().getNombres());
		}
		
		recibos = new ArrayList<Recibo>(getModelo().getRecibos());
		listRecibos.setModel(new ListModelList(recibos));
		

		
	}

	public void agregarCliente(Cliente cliente) {
		// TODO Auto-generated method stub
		seleccionarCliente.setVisible(false);
		getModelo().setCliente(cliente);
		
		cedula.setText(cliente.getIdentidadLegal());
		nombre.setText(cliente.getNombres());
		
		
	}

	public Button getAgregar() {
		return agregar;
	}

	public void setAgregar(Button agregar) {
		this.agregar = agregar;
	}

	public Button getQuitar() {
		return quitar;
	}

	public void setQuitar(Button quitar) {
		this.quitar = quitar;
	}

	public void activarTipo() {
		// TODO Auto-generated method stub
		//si el tipo es Recibo pongo la lista de Recibos y le pongo el boton de Seleccionar Recibo 
		// si el tipo es Nota de Credito Cargo las notas de Credito y al seleccionar la nota de credito le cargo los recibos 
		if(tipo.getSelectedItem().getValue().equals(Reintegro.REINTEGRO_RECIBOS))
		{
			Vbox botonera = new Vbox();
			botonera.appendChild(agregar);
			botonera.appendChild(quitar);
			datosreinegro.addComponente(botonera,listRecibos);
			datosreinegro.setAnchoColumna(0,100);
			tipo.setDisabled(true);
			datosreinegro.dibujar(contenedor);
		}	
		else if (tipo.getSelectedItem().getValue().equals(Reintegro.REINTEGRO_CREDITO))
		{
			
			Vbox botonera = new Vbox();
			botonera.appendChild(notaCredito);
			
			datosNotaCredito.addComponente("Nota Credito :",nroNotaCredito);
			datosNotaCredito.addComponente("Factura Afect. :",factura);
			datosNotaCredito.addComponente("Total Nota   :",totalNota);
			datosNotaCredito.addComponente("Por Pagar    :",porpagar);
			datosNotaCredito.setAnchoColumna(0,100);
			tipo.setDisabled(true);
			datosNotaCredito.dibujar(contenedor);
			datosNotaCredito.appendChild(notaCredito);
		}
	}

	public Listbox getListRecibos() {
		return listRecibos;
	}

	public void setListRecibos(Listbox listRecibos) {
		this.listRecibos = listRecibos;
	}

	public void agregarRecibo(Recibo recibo) {
		// TODO Auto-generated method stub
		
		for(Recibo rec : recibos)
		{
			if(rec.equals(recibo))
			{
				return;
			}
		}
		recibos.add(recibo);
		listRecibos.setModel(new ListModelList(recibos));
		//calcular el Monto 
		
		calcularMonto();
	
	}

	public void quitar(Recibo recibo) {
		// TODO Auto-generated method stub

		recibos.remove(recibo);
		listRecibos.setModel(new ListModelList(recibos));
		//Calcular el Monto 
		calcularMonto();
		
	}

	public List<Recibo> getRecibos() {
		return recibos;
	}

	public void setRecibos(List<Recibo> recibos) {
		this.recibos = recibos;
	}

	private void calcularMonto() {
		// TODO Auto-generated method stub
		Double monto = new Double(0);
		if(tipo.getSelectedItem().getValue().equals(Reintegro.REINTEGRO_RECIBOS))
		{
			for(Recibo recibo : recibos)
			{
				
				monto += recibo.getSaldo();
			}
		}
		else
		{
			monto +=getModelo().getNotaCredito().getMontoSaldo()* - 1;
		}

		montoTotal.setValue(monto);

	}
	
	
	

	public Doublebox getMontoTotal() {
		return montoTotal;
	}

	public void setMontoTotal(Doublebox montoTotal) {
		this.montoTotal = montoTotal;
	}

	public void agregarNotaCredito(NotaCredito nota) {
		// TODO Auto-generated method stub
		
		notaCredito.setVisible(false);
		getModelo().setNotaCredito(nota);
		
		factura.setText(nota.getStrFacturaAfectada());
		factura.setDisabled(true);
		
		nroNotaCredito.setText(nota.getStrNroDocumento());
		nroNotaCredito.setDisabled(true);
		
		totalNota.setText(nota.getStrTotal());
		totalNota.setDisabled(true);
		
		porpagar.setText(nota.getStrSaldo());
		porpagar.setDisabled(true);
		
		calcularMonto();
		
	}
}