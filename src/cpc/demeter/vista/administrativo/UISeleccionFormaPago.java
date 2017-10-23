package cpc.demeter.vista.administrativo;

import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

import cpc.zk.componente.ventanas.CompGrupoDatos;
 

public class UISeleccionFormaPago  extends Window {

	private static final long serialVersionUID = 1L;

	private EventListener 		controlador;
	
	private CompGrupoDatos 			operacionSede;
	private CompGrupoDatos 			operacionBancaria;
	private CompGrupoDatos 			operacionOtras;
	
	private Button 				cheque;
	private Button 				efectivo;
	private Button 				deposito;
	private Button 				transferencia;
	private Button 				punto;
	private Button 				retencionIVA;
	private Button 				retencionISLR;
	private Button 				retencion1x100;
	
	
	private Div					contenedor;
	
	public UISeleccionFormaPago(String title, String border,boolean closable,EventListener controlador) throws SuspendNotAllowedException, InterruptedException
	{
		super(title, border, closable);
		this.setWidth("600px");
		this.controlador = controlador;
		// TODO Auto-generated constructor stub
		inicializar();
		dibujar();
	}

	private void dibujar() {
		// TODO Auto-generated method stub
		
		Div div = new Div();
		div.setAlign("center");
		div.appendChild(efectivo);
		operacionSede.addComponente(div);
		
		div = new Div();
		div.setAlign("center");
		div.appendChild(cheque);
		operacionSede.addComponente(div);
		
		div = new Div();
		div.setAlign("center");
		div.appendChild(deposito);
		operacionBancaria.addComponente(div);
		
		div = new Div();
		div.setAlign("center");
		div.appendChild(transferencia);
		operacionBancaria.addComponente(div);
		
		div = new Div();
		div.setAlign("center");
		div.appendChild(punto);
		operacionBancaria.addComponente(div);

		
		
		div = new Div();
		div.setAlign("center");
		div.appendChild(retencionIVA);
		operacionOtras.addComponente(div);
		
		div = new Div();
		div.setAlign("center");
		div.appendChild(retencionISLR);
		operacionOtras.addComponente(div);
		
		div = new Div();
		div.setAlign("center");
		div.appendChild(retencion1x100);
		operacionOtras.addComponente(div);
		
		operacionSede.dibujar(this);
		operacionBancaria.dibujar(this);
		operacionOtras.dibujar(this);
		
		
}



	private void inicializar() {
		// TODO Auto-generated method stub
		
		contenedor = new Div();
		contenedor.setWidth("99%");
		contenedor.setHeight("99%");
		
		operacionSede    = new CompGrupoDatos("Operaciones Sede",1);
		operacionBancaria = new CompGrupoDatos("Operaciones Bancarias ",1);
		operacionOtras = new CompGrupoDatos("Otras Operaciones ",1);
		
		cheque        = new Button("CHEQUE");
		cheque.addEventListener(Events.ON_CLICK,controlador);
		cheque.setWidth("300px");
		cheque.setStyle("margin-top:5px; font-size:20px;");
		
		deposito      = new Button("DEPOSITOS");
		deposito.addEventListener(Events.ON_CLICK,controlador);
		deposito.setWidth("300px");
		deposito.setStyle("margin-top:5px; font-size:20px;");
		
		retencionIVA      = new Button("RETENCION IVA ");
		retencionIVA.addEventListener(Events.ON_CLICK,controlador);
		retencionIVA.setWidth("300px");
		retencionIVA.setStyle("margin-top:5px; font-size:20px;");
		
		retencionISLR      = new Button("RETENCION ISLR ");
		retencionISLR.addEventListener(Events.ON_CLICK,controlador);
		retencionISLR.setWidth("300px");
		retencionISLR.setStyle("margin-top:5px; font-size:20px;");

		retencion1x100      = new Button("RETENCION 1x100");
		retencion1x100.addEventListener(Events.ON_CLICK,controlador);
		retencion1x100.setWidth("300px");
		retencion1x100.setStyle("margin-top:5px; font-size:20px;");

		efectivo      = new Button("EFECTIVO");
		efectivo.addEventListener(Events.ON_CLICK,controlador);
		efectivo.setWidth("300px");
		efectivo.setStyle("margin-top:5px; font-size:20px;");
		
		transferencia = new Button("TRANSFERENCIAS");
		transferencia.addEventListener(Events.ON_CLICK,controlador);
		transferencia.setWidth("300px");
		transferencia.setStyle("margin-top:5px; font-size:20px;");

		punto = new Button("Punto Electronico");
		punto.addEventListener(Events.ON_CLICK,controlador);
		punto.setWidth("300px");
		punto.setStyle("margin-top:5px; font-size:20px;");

		
	}



	public EventListener getControlador() {
		return controlador;
	}



	public void setControlador(EventListener controlador) {
		this.controlador = controlador;
	}



	public Button getCheque() {
		return cheque;
	}



	public void setCheque(Button cheque) {
		this.cheque = cheque;
	}



	public Button getDeposito() {
		return deposito;
	}



	public void setDeposito(Button deposito) {
		this.deposito = deposito;
	}



	public Button getTransferencia() {
		return transferencia;
	}

	public void setTransferencia(Button transferencia) {
		this.transferencia = transferencia;
	}



	public Button getEfectivo() {
		return efectivo;
	}



	public void setEfectivo(Button efectivo) {
		this.efectivo = efectivo;
	}



	public Button getRetencionIVA() {
		return retencionIVA;
	}



	public void setRetencionIVA(Button retencionIVA) {
		this.retencionIVA = retencionIVA;
	}



	public Button getRetencionISLR() {
		return retencionISLR;
	}



	public void setRetencionISLR(Button retencionISLR) {
		this.retencionISLR = retencionISLR;
	}



	public Button getRetencion1x100() {
		return retencion1x100;
	}



	public void setRetencion1x100(Button retencion1x100) {
		this.retencion1x100 = retencion1x100;
	}



	public Button getPunto() {
		return punto;
	}



	public void setPunto(Button punto) {
		this.punto = punto;
	}


 
	
	
}
