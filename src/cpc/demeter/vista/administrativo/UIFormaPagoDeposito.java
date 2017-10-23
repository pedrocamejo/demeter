package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;


import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.Cheque;
import cpc.modelo.demeter.administrativo.FormaPagoDeposito;
import cpc.modelo.sigesp.basico.Banco;
import cpc.modelo.sigesp.basico.CuentaBancaria;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;

public class UIFormaPagoDeposito extends Window 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Banco>    			bancos = new ArrayList<Banco>();
	private List<CuentaBancaria>  	cuentas = new ArrayList<CuentaBancaria>();
	private List<Cheque>   			cheques = new ArrayList<Cheque>();
	
	private AnnotateDataBinder	databinder;
	private CompGrupoDatos	 	gbGeneral;
	private CompGrupoDatos	 	gbDetalle;

	private Textbox 		documento;

	private Combobox		banco;
	private Combobox 		cuenta;
		
	private Doublebox		efectivo;
	private Doublebox		monto;
	
	private Listbox 		listCheques;
	
    private Datebox   		fecha;
	
	private EventListener	controlador;
    
	private Button			aceptar;
	private Button			cancelar;
	
	private FormaPagoDeposito 	modelo;
	
	
	private Button 			agregar; //agregar Cheque 
	private Button			quitar; // quitar Queche 
	
	/********* Para la busquedad 
	 * @param clase **************/
	
	public UIFormaPagoDeposito  (String titulo, String ancho ,List<Banco> bancos) throws ExcDatosInvalidos
	{
		super(titulo,ancho,true);
		setMaximizable(true);
		this.bancos = bancos;
	}

	public void inicializar() 
	{

		gbGeneral = new CompGrupoDatos("Datos Generales",4);
		gbDetalle = new CompGrupoDatos("Detalle",1);
		 
		
		banco = new Combobox();
		banco.setModel(new ListModelArray(bancos));
		banco.addEventListener(Events.ON_SELECT,getControlador());
		
		cuenta = new Combobox();
		cuenta.setModel(new ListModelArray(cuentas));

		documento = new Textbox();
		
		monto = new Doublebox(0); 
		monto.setDisabled(true);
		
		efectivo = new Doublebox(0.0);
		efectivo.setValue(new Double(0.0));
		efectivo.addEventListener(Events.ON_BLUR,getControlador());
		
		listCheques = new Listbox();
		listCheques.setModel(new ListModelArray(cheques));
		listCheques.setWidth("600px");
	
		Listhead titulos = new  Listhead();

		Listheader nroCuenta = new Listheader("N° Cuenta ");
		nroCuenta.setTooltiptext("Numero Cuenta ");
		nroCuenta.setParent(titulos);
		
		Listheader nroCheque = new Listheader("N° Cheque ");
		nroCheque.setTooltiptext("Numero Cheque ");
		nroCheque.setParent(titulos);
		
		Listheader montoCheque = new Listheader("Monto ");
		montoCheque.setTooltiptext("Monto del Cheque ");
		montoCheque.setParent(titulos);
		
		listCheques.appendChild(titulos);
		listCheques.setItemRenderer(new ListitemRenderer() {
			@Override
			public void render(Listitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				arg0.setValue(arg1);
				Cheque cheque = (Cheque) arg1;
				new Listcell(cheque.getNroCuenta()).setParent(arg0);
				new Listcell(cheque.getNroCheque()).setParent(arg0);
				new Listcell(cheque.getMonto().toString()).setParent(arg0);
			}
		});
		
		fecha = new Datebox();
		
		
		aceptar  = new Button("aceptar");
			aceptar.addEventListener(Events.ON_CLICK,getControlador());
		
		cancelar = new Button("cancelar");
			cancelar.addEventListener(Events.ON_CLICK,getControlador());
		
		agregar = new Button("+");
		agregar.setTooltiptext("Agregar un Nuevo cheque ");
			agregar.addEventListener(Events.ON_CLICK,getControlador());
		
		quitar = new Button("-");
		quitar.setTooltiptext("Quitar Cheque ");
			quitar.addEventListener(Events.ON_CLICK,getControlador());
			
	}

	public void dibujar() 
	{
 
		gbGeneral.addComponente("Documento ",documento);
		gbGeneral.addComponente("Fecha",fecha);
		gbGeneral.addComponente("Banco",banco);
		gbGeneral.addComponente("N° Cuenta ",cuenta);
		gbGeneral.addComponente(" Saldo Efectivo :",efectivo);
		
		Hbox hbox = new Hbox();
			hbox.appendChild(agregar);
			hbox.appendChild(quitar);
			
		gbDetalle.addComponente(hbox);
		gbDetalle.addComponente(listCheques);
		

		hbox = new Hbox();
		hbox.setAlign("right");
		hbox.appendChild(new Label(" Total "));
		hbox.appendChild(monto);

		Div div = new Div();
		div.appendChild(hbox);
		
		gbDetalle.addComponente(hbox);

		gbGeneral.dibujar(this);
		gbDetalle.dibujar(this);
		
		Hbox botonera = new Hbox();
			botonera.setAlign("right");
			botonera.appendChild(aceptar);
			botonera.appendChild(cancelar);

			
		this.appendChild(botonera);
			
	}

	public void cargarValores() throws ExcDatosInvalidos {
		// TODO Auto-generated method stub

		databinder = new AnnotateDataBinder(this);
		databinder.bindBean("modelo",modelo);
		databinder.addBinding(fecha,"value","modelo.fecha",null,null,"save", null, null, null,null);
		databinder.addBinding(cuenta,"value","modelo.cuenta",null,null,"save", null, null, null,null);
		databinder.addBinding(documento,"value","modelo.documento",null,null,"save", null, null, null,null);
		databinder.addBinding(banco,"selectedItem","modelo.banco",null,null,"save", null, null, null,null);
		databinder.addBinding(efectivo,"value","modelo.efectivo",null,null,"save", null, null, null,null);
		databinder.addBinding(monto,"value","modelo.monto",null,null,"save", null, null, null,null);
		setCheques(getModelo().getCheques());

		FormaPagoDeposito formaPago = getModelo();
		fecha.setValue(formaPago.getFecha());
		cuenta.setValue(formaPago.getCuenta());
		documento.setValue(formaPago.getDocumento());
		
		if (formaPago.getBanco() != null)
		{
			banco.setValue(formaPago.getBanco().toString());
		}
		
		
		if  (formaPago.getEfectivo()!=null) 
		efectivo.setValue(formaPago.getEfectivo());
		monto.setValue(formaPago.getMonto());
		
	}

	public void activarConsulta()
	{
		fecha.setDisabled(true);
		monto.setDisabled(true);
		efectivo.setDisabled(false);
		aceptar.setVisible(false);
		cancelar.setVisible(false);
		agregar.setVisible(false); 
		quitar.setVisible(false); 
		
	}
 

	public void desactivar(int modoOperacion) {
		if (!(modoOperacion == Accion.EDITAR || modoOperacion== Accion.AGREGAR))
			activarConsulta();
	}


	public EventListener getControlador() {
		return controlador;
	}

	public void setControlador(EventListener controlador) {
		this.controlador = controlador;
	}

	public Doublebox getMonto() {
		return monto;
	}

	public Datebox getFecha() {
		return fecha;
	}

	public void setMonto(Doublebox monto) {
		this.monto = monto;
	}

	public void setFecha(Datebox fecha) {
		this.fecha = fecha;
	}


	public Button getAceptar() {
		return aceptar;
	}

	public void setAceptar(Button aceptar) {
		this.aceptar = aceptar;
	}

	public Button getCancelar() {
		return cancelar;
	}

	public void setCancelar(Button cancelar) {
		this.cancelar = cancelar;
	}

	public FormaPagoDeposito getModelo() {
		databinder.saveAll();
		return modelo;
	}

	public void setModelo(FormaPagoDeposito modelo) {
		this.modelo = modelo;
	}

	public List<Banco> getBancos() {
		return bancos;
	}

	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}

	public List<CuentaBancaria> getCuentas() {
		return cuentas;
	}

	public void setCuentas(List<CuentaBancaria> cuentas) {
		this.cuentas = cuentas;
		cuenta.setModel(new ListModelArray(cuentas));
		
	}

	public List<Cheque> getCheques() {
		return cheques;
	}

	public void setCheques(List<Cheque> cheques) {
		this.cheques = cheques;
		listCheques.setModel(new ListModelArray(cheques));
		
	}
 
	public Textbox getDocumento() {
		return documento;
	}

	public void setDocumento(Textbox documento) {
		this.documento = documento;
	}

	public Combobox getBanco() {
		return banco;
	}

	public void setBanco(Combobox banco) {
		this.banco = banco;
	}

	public Combobox getCuenta() {
		return cuenta;
	}

	public void setCuenta(Combobox cuenta) {
		this.cuenta = cuenta;
	}

	public Doublebox getEfectivo() {
		return efectivo;
	}

	public void setEfectivo(Doublebox efectivo) {
		this.efectivo = efectivo;
	}

	public Listbox getListCheques() {
		return listCheques;
	}

	public void setListCheques(Listbox listCheques) {
		this.listCheques = listCheques;
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

	public void agregarCheque(Cheque cheque)
	{
		cheques.add(cheque);
		listCheques.setModel(new ListModelArray(cheques));
		actualizarMontos();
	}

	public void actualizarMontos() {
		// TODO Auto-generated method stub
		Double total = new Double(0);
		for (Cheque cheque : cheques)
		{
			total += cheque.getMonto();
		}
		
	
		monto.setValue(total + efectivo.getValue() );
	}

	public void quitarCheque(Cheque cheque) {
		// TODO Auto-generated method stub
		 cheques.remove(cheque);
		 listCheques.setModel(new ListModelArray(cheques));
		 actualizarMontos();
	}

}


