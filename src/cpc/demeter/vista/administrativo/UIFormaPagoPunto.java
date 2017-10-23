package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.databind.AnnotateDataBinder;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.FormaPagoPunto;
import cpc.modelo.sigesp.basico.Banco;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;

public class UIFormaPagoPunto  extends Window 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<Banco>    			bancos = new ArrayList<Banco>();
	private List<String>  			tipos =  FormaPagoPunto.TIPO_FORMA_PAGO;
	
	private AnnotateDataBinder	databinder;
	private CompGrupoDatos	 	gbGeneral;

	private Textbox 		nrotarjeta;
	private Textbox 		documento;

	private Combobox		banco;
	private Combobox 		tipo;
	private Datebox   		fecha;
	private Doublebox		monto;
	
	private EventListener	controlador;
    
	private Button			aceptar;
	private Button			cancelar;

	private FormaPagoPunto 	modelo;

	
	public UIFormaPagoPunto(String titulo, String ancho ,List<Banco> bancos) throws ExcDatosInvalidos
	{
		super(titulo,ancho,true);
		setMaximizable(true);
		this.bancos = bancos;
	}

	public void inicializar() 
	{

		gbGeneral = new CompGrupoDatos("Datos Generales",4);
		gbGeneral.setWidth("600px");
		
		banco = new Combobox();
		banco.setModel(new ListModelArray(bancos));
		
		tipo = new Combobox();
		tipo.setModel(new ListModelArray(tipos));
		
		monto = new Doublebox(0.0);
		
		nrotarjeta = new Textbox();
		nrotarjeta.setMaxlength(35);
		
		documento = new Textbox();
		nrotarjeta.setMaxlength(35);
		
		fecha = new Datebox();
		fecha.setValue(new Date());
		
		aceptar  = new Button("aceptar");
		aceptar.addEventListener(Events.ON_CLICK,getControlador());

		cancelar = new Button("cancelar");
		cancelar.addEventListener(Events.ON_CLICK,getControlador());
	}

	public void dibujar() 
	{
 		gbGeneral.addComponente("Fecha ",fecha);
 		gbGeneral.addComponente("Documento ",documento);
 		gbGeneral.addComponente("Banco",banco);
		gbGeneral.addComponente("Nro Tarjeta ",nrotarjeta);
		gbGeneral.addComponente(" Tipo ",tipo);
		gbGeneral.addComponente(" Monto  ",monto);
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
		databinder.addBinding(nrotarjeta,"value","modelo.nrotarjeta",null,null,"save", null, null, null,null);
		databinder.addBinding(documento,"value","modelo.documento",null,null,"save", null, null, null,null);
		databinder.addBinding(monto,"value","modelo.monto",null,null,"save", null, null, null,null);
		databinder.addBinding(banco,"selectedItem","modelo.banco",null,null,"save", null, null, null,null);
		databinder.addBinding(tipo,"selectedItem","modelo.tipo",null,null,"save", null, null, null,null);
		
		FormaPagoPunto formaPago = getModelo();
		fecha.setValue(formaPago.getFecha());
		nrotarjeta.setValue(formaPago.getNrotarjeta());
		tipo.setSelectedIndex(formaPago.tipo_index_item());
		documento.setText(formaPago.getDocumento());
		if (formaPago.getBanco() != null)
		{
			banco.setValue(formaPago.getBanco().toString());
		}
		
		monto.setValue(formaPago.getMonto());
	}

	public void activarConsulta()
	{
		fecha.setDisabled(true);
		banco.setDisabled(true);
		tipo.setDisabled(true);
		nrotarjeta.setDisabled(true);
		monto.setDisabled(true);
		documento.setDisabled(true);
		aceptar.setVisible(false);
	}

	
	public void desactivar(int modoOperacion) {
		if (! (modoOperacion == Accion.EDITAR || modoOperacion== Accion.AGREGAR))
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

	public void setModelo(FormaPagoPunto modelo) {
		this.modelo = modelo;
	}
	
	public FormaPagoPunto getModelo() {
		databinder.saveAll();
		return modelo;
	}

	public List<Banco> getBancos() {
		return bancos;
	}

	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}


	public Combobox getBanco() {
		return banco;
	}

	public void setBanco(Combobox banco) {
		this.banco = banco;
	}

	public List<String> getTipos() {
		return tipos;
	}

	public void setTipos(List<String> tipos) {
		this.tipos = tipos;
	}

	public CompGrupoDatos getGbGeneral() {
		return gbGeneral;
	}

	public void setGbGeneral(CompGrupoDatos gbGeneral) {
		this.gbGeneral = gbGeneral;
	}

	public Textbox getNrotarjeta() {
		return nrotarjeta;
	}

	public void setNrotarjeta(Textbox nrotarjeta) {
		this.nrotarjeta = nrotarjeta;
	}

	public Combobox getTipo() {
		return tipo;
	}

	public void setTipo(Combobox tipo) {
		this.tipo = tipo;
	}

	public Doublebox getMonto() {
		return monto;
	}

	public void setMonto(Doublebox monto) {
		this.monto = monto;
	}

	public Textbox getDocumento() {
		return documento;
	}

	public void setDocumento(Textbox documento) {
		this.documento = documento;
	}
}