package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.Cheque;
import cpc.modelo.demeter.administrativo.FormaPago;
import cpc.modelo.demeter.administrativo.FormaPagoCheque;
import cpc.modelo.demeter.basico.TipoFormaPago;
import cpc.modelo.sigesp.basico.Banco;
import cpc.modelo.sigesp.basico.CuentaBancaria;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;

public class UiDepositoGeneral extends Window 
{

	
	
	private AnnotateDataBinder				databinder;
	
	private CompGrupoDatos		 			gbGeneral;

    private Doublebox						monto;
	private Datebox   						fecha;
	private EventListener					controlador;

	private Button							aceptar;
	private Button							cancelar;
	
	private FormaPago 							modelo;
	
	
	/********* Para la busquedad 
	 * @param clase **************/
	
	public UiDepositoGeneral  (String titulo, String ancho ) throws ExcDatosInvalidos
	{
		super(titulo,ancho,true);
		setMaximizable(true);
	}

	public void inicializar() 
	{

		gbGeneral = new CompGrupoDatos("Datos Generales",4);
	   
		fecha = new Datebox();

		monto = new Doublebox(0); 

		aceptar  = new Button("aceptar");
			aceptar.addEventListener(Events.ON_CLICK,getControlador());
		
		cancelar = new Button("cancelar");
			cancelar.addEventListener(Events.ON_CLICK,getControlador());
		
	}

	public void dibujar() 
	{
 		gbGeneral.addComponente("Fecha",fecha);
		gbGeneral.addComponente(" Total :",monto);
		gbGeneral.dibujar(this);

		Hbox botonera = new Hbox();
			botonera.setAlign("right");
			botonera.appendChild(aceptar);
			botonera.appendChild(cancelar);
			
		this.appendChild(botonera);
			
	}

	public void cargarValores(FormaPago arg0) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub

		databinder = new AnnotateDataBinder(this);
		databinder.bindBean("modelo",modelo);
		databinder.addBinding(monto,"value","modelo.monto",null,null,"save", null, null, null,null);
		databinder.addBinding(fecha,"value","modelo.fecha",null,null,"save", null, null, null,null);
		
		FormaPago formaPago =(FormaPago) getModelo();
		monto.setValue(formaPago.getMonto());
		fecha.setValue(formaPago.getFecha());
		
	}

	public void activarConsulta()
	{
		fecha.setDisabled(true);
		monto.setDisabled(true);
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
	public void ActivarEfectivo() {
		// TODO Auto-generated method stub
	 monto.setValue(0);
	
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

	public Object getModelo() {
		databinder.saveAll();
		return modelo;
	}

	public void setModelo(FormaPago modelo) {
		this.modelo = modelo;
	}

	
}
