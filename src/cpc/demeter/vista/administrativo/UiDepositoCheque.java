package cpc.demeter.vista.administrativo;

import java.util.List;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.Cheque;
import cpc.modelo.demeter.administrativo.FormaPagoCheque;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;

public class UiDepositoCheque extends Window 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private AnnotateDataBinder	databinder;

	private CompGrupoDatos	gbGeneral;
	private CompGrupoDatos 	listado;
	
    private Datebox   		fecha;
    private EventListener	controlador;

    private Button 			agregar; //agregar Cheque 
	private Button			aceptar;
	private Button			cancelar;
	private FormaPagoCheque modelo;


	private Textbox        nroCuenta;
	private Textbox        nroCheque;
	
	
	
	public UiDepositoCheque  (String titulo, String ancho,FormaPagoCheque modelo, List<Cheque> cheques)throws ExcDatosInvalidos
	{
		super(titulo,ancho,true);
		this.modelo = modelo;
		setMaximizable(true);

	}

	public void inicializar() 
	{

		gbGeneral = new CompGrupoDatos("Datos Generales",2);
		gbGeneral.setWidth("600px");
		fecha = new Datebox();
		
	    nroCheque = new Textbox();
	    nroCheque.setDisabled(true);
	    nroCuenta = new Textbox();
	    nroCuenta.setDisabled(true);
	    
		listado = new CompGrupoDatos(1);
		listado.setTitulo("Datos Cheques ");
		
		agregar = new Button("Catalogo de Chques ");
		agregar.setTooltiptext(" Cargar cheques nuevos o buscar uno existente disponible  ");
		agregar.addEventListener(Events.ON_CLICK,getControlador());
		
		aceptar  = new Button("aceptar");
		aceptar.addEventListener(Events.ON_CLICK,getControlador());
		
		cancelar = new Button("cancelar");
		cancelar.addEventListener(Events.ON_CLICK,getControlador());

	}

	public void dibujar() 
	{
		gbGeneral.addComponente("Fecha",fecha);
		gbGeneral.addComponente("",agregar);
		gbGeneral.addComponente("Nro Cuenta",nroCuenta);
		gbGeneral.addComponente("Nro Cheque",nroCheque);
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
		
		FormaPagoCheque formaPagoCheque = (FormaPagoCheque) modelo;
		
		if (formaPagoCheque.getCheque() == null )
		{
			Cheque cheque = new Cheque();
			formaPagoCheque.setCheque(cheque);
		}
		else
		{
			nroCheque.setValue(formaPagoCheque.getCheque().getNroCheque());
			nroCuenta.setValue(formaPagoCheque.getCheque().getNroCuenta());
		}

		databinder.loadAll();
		fecha.setValue(formaPagoCheque.getFecha());
	}

	public void activarConsulta()
	{
		fecha.setDisabled(true);
		aceptar.setVisible(false);
		agregar.setVisible(false);  
		aceptar.setVisible(false);
		cancelar.setVisible(false);
		nroCuenta.setDisabled(true);
		nroCheque.setDisabled(true);
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


	public Datebox getFecha() {
		return fecha;
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

	public FormaPagoCheque getModelo() {
		databinder.loadAll();
		databinder.saveAll();
		return modelo;
	}


	public void setModelo(FormaPagoCheque modelo) {
		this.modelo = modelo;
	}
 
	public Button getAgregar() {
		return agregar;
	}

	public void setAgregar(Button agregar) {
		this.agregar = agregar;
	}


	public void agregarCheque(Cheque cheque) {
		// TODO Auto-generated method stub
		modelo.setCheque(cheque);
		modelo.setMonto(cheque.getMonto());
		nroCheque.setText(cheque.getNroCheque());
		nroCuenta.setText(cheque.getNroCuenta());
	}
	
	public  Cheque  getCheque() {
		return modelo.getCheque();
	}

	public Textbox getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(Textbox nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public Textbox getNroCheque() {
		return nroCheque;
	}

	public void setNroCheque(Textbox nroCheque) {
		this.nroCheque = nroCheque;
	}
 
}
