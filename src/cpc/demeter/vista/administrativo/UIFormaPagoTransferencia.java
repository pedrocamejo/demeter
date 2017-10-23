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
import cpc.modelo.demeter.administrativo.FormaPagoTransferencia;
import cpc.modelo.sigesp.basico.Banco;
import cpc.modelo.sigesp.basico.CuentaBancaria;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;

public class UIFormaPagoTransferencia  extends Window 
{

	private List<Banco>    			bancos = new ArrayList<Banco>();
	private List<CuentaBancaria>  	cuentas = new ArrayList<CuentaBancaria>();
	
	private AnnotateDataBinder	databinder;
	private CompGrupoDatos	 	gbGeneral;

	private Textbox 		documento;
	private Combobox		banco;
	private Combobox 		cuenta;
	private Doublebox		monto;
	private Datebox   		fecha;
	
	private EventListener	controlador;
    
	private Button			aceptar;
	private Button			cancelar;
	
	private FormaPagoTransferencia 	modelo;
	
	
	/********* Para la busquedad 
	 * @param clase **************/
	
	public UIFormaPagoTransferencia  (String titulo, String ancho ,List<Banco> bancos) throws ExcDatosInvalidos
	{
		super(titulo,ancho,true);
		setMaximizable(true);
		this.bancos = bancos;
	
	}

	public void inicializar() 
	{

		gbGeneral = new CompGrupoDatos("Datos Generales",4);
		 
		
		banco = new Combobox();
		banco.setModel(new ListModelArray(bancos));
		banco.addEventListener(Events.ON_SELECT,getControlador());
		
		cuenta = new Combobox();
		cuenta.setModel(new ListModelArray(cuentas));
		
		documento = new Textbox();
		
		monto = new Doublebox(0);  
		
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
		
		fecha = new Datebox();
		
		
		aceptar  = new Button("aceptar");
			aceptar.addEventListener(Events.ON_CLICK,getControlador());
		
		cancelar = new Button("cancelar");
			cancelar.addEventListener(Events.ON_CLICK,getControlador());
		
			
	}

	public void dibujar() 
	{
 
		gbGeneral.addComponente("Documento ",documento);
		gbGeneral.addComponente("Fecha",fecha);
		gbGeneral.addComponente("Banco",banco);
		gbGeneral.addComponente("N° Cuenta ",cuenta);
		gbGeneral.addComponente("Monto ",monto);
		gbGeneral.dibujar(this);

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
		databinder.addBinding(monto,"value","modelo.monto",null,null,"save", null, null, null,null);
		
		FormaPagoTransferencia formaPago = getModelo();
		fecha.setValue(formaPago.getFecha());
		cuenta.setValue(formaPago.getCuenta());
		documento.setValue(formaPago.getDocumento());
		
		if (formaPago.getBanco() != null)
		{
			banco.setValue(formaPago.getBanco().toString());
		}
		
		monto.setValue(formaPago.getMonto());
	}

	public void activarConsulta()
	{
		fecha.setDisabled(true);
		monto.setDisabled(true);
		cuenta.setDisabled(true);
		banco.setDisabled(true);
		documento.setDisabled(true);
		aceptar.setVisible(false);

	}

	public void modoEdicion() 
	{
	
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.EDITAR || modoOperacion== Accion.AGREGAR)
			modoEdicion();
		else
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

	public FormaPagoTransferencia getModelo() {
		databinder.saveAll();
		return modelo;
	}

	public void setModelo(FormaPagoTransferencia modelo) {
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

}