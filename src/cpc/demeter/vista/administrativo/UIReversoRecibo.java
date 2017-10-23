package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
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

import cpc.modelo.demeter.administrativo.FormaPago;
import cpc.modelo.demeter.administrativo.FormaPagoDeposito;
import cpc.modelo.demeter.administrativo.FormaPagoTransferencia;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.demeter.administrativo.ReversoFormaPago;
import cpc.modelo.demeter.administrativo.ReversoRecibo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UIReversoRecibo	extends CompVentanaBase<ReversoRecibo>  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6192400844745674882L;
	private CompGrupoDatos 		 contenedorRecibo;
	private CompBuscar<Recibo> recibo;
	private Doublebox montoRecibo;
	private Textbox observacion;
	private Label estado;
	private Label cedulaRif;
	private Label cliente;
	
	private CompGrupoDatos 		 contenedorFormasReverso;
	private Listbox				 listReversoFormaPago;
	private List<ReversoFormaPago>       reveversoFormasPagos	= new ArrayList<ReversoFormaPago>();
	private List<Recibo> recibos = new ArrayList<Recibo>();
	private Button botonAutorizar ;
	//private CompGrupoDatos encabezadoReverso;
	
	public UIReversoRecibo (String titulo, int ancho,List<Recibo> recibos){
		super(titulo, ancho);
		this.recibos=recibos;
	}
	
	public void inicializar(){
	contenedorRecibo= new CompGrupoDatos("Datos Recibo Afectado ",2);
		recibo= new CompBuscar<Recibo>(getEncabezadorecibo(), 1);
		recibo.setAncho(800);
		montoRecibo = new Doublebox(0.0);
		observacion = new Textbox();
		observacion.setMaxlength(255);

		observacion.setWidth("700px");
		estado = new Label();
		cedulaRif = new Label();
		cliente = new Label();
		botonAutorizar = new Button("Autorizar");
		contenedorFormasReverso = new CompGrupoDatos("Formas de pago afectadas");
		listReversoFormaPago = new Listbox(); 
		Listhead tituloFormaPago = new Listhead();
		
		Listheader	tipoFormaPago = new Listheader("Tipo ");
		Listheader	monto = new Listheader(" Monto  ");
		Listheader	fehcaHeader = new Listheader(" Fecha ");
		
		tituloFormaPago.appendChild(tipoFormaPago);
		tituloFormaPago.appendChild(fehcaHeader);
		tituloFormaPago.appendChild(monto);
		
		listReversoFormaPago.appendChild(tituloFormaPago);
		listReversoFormaPago.setItemRenderer(new ListitemRenderer() {
		
		@Override
		public void render(Listitem arg0, Object arg1) throws Exception {
			// TODO Auto-generated method stub
			arg0.setValue(arg1);
			ReversoFormaPago rePago =(ReversoFormaPago) arg1;
			FormaPago pago=rePago.getFormaPago();
			
			if (FormaPagoDeposito.class.equals(pago.getClass()))
			{
				FormaPagoDeposito deposito = (FormaPagoDeposito) pago;
				new Listcell(pago.getTipoFormaPago().toString()+"("+deposito.getDocumento()+")").setParent(arg0);
			}
			else if (FormaPagoTransferencia.class.equals(pago.getClass()))
			{
				FormaPagoTransferencia transferencia = (FormaPagoTransferencia) pago;
				new Listcell(pago.getTipoFormaPago().toString()+"("+transferencia.getDocumento()+")").setParent(arg0);
			}
			else
			{
				new Listcell(pago.getTipoFormaPago().toString()).setParent(arg0);
			}
			new Listcell(pago.getStrFecha()).setParent(arg0);
			new Listcell(rePago.getStrMonto()).setParent(arg0);
		}
	});
	recibo.setListenerEncontrar(getControlador());
	botonAutorizar.addEventListener(Events.ON_CLICK, getControlador());
	recibo.setModelo(recibos);
	}

	
	@Override
	public void cargarValores(ReversoRecibo dato) throws ExcDatosInvalidos {
		if (dato.getReciboAfectado()!=null){
		recibo.setSeleccion(dato.getReciboAfectado());
		Recibo recibo = getRecibo().getSeleccion();
		getRecibo().setText(recibo.getControl());
		getCliente().setValue(recibo.getStrPagador());
		getCedulaRif().setValue(recibo.getStrIdentidadLegalPagador());
		estado.setValue("");
		observacion.setValue(dato.getObservacion());}
		montoRecibo.setValue(dato.getMontoReversado());
		if (dato.getReversoFormaPagos()!=null)
		listReversoFormaPago.setModel(new ListModelList(dato.getReversoFormaPagos()));
	
	}
	
	private List<CompEncabezado> getEncabezadorecibo() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Nro Recibo", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getControl");
		encabezado.add(titulo);
 
		titulo = new CompEncabezado("Fecha", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Beneficiario", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrPagador");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Ced/Rif", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrIdentidadLegalPagador");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Concepto", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getConcepto");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto", 90);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getMonto");
		encabezado.add(titulo); 

		titulo = new CompEncabezado("Saldo", 90);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getSaldo");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Estado", 90);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo); 
		
		return encabezado;
	}

	public void dibujar(){
		contenedorRecibo.addComponente("Recibo", recibo);
		contenedorRecibo.addComponente("cliente",cliente);
		contenedorRecibo.addComponente("Cedula/Rif",cedulaRif);
		contenedorRecibo.addComponente("monto Reversado",montoRecibo);
		contenedorRecibo.addComponente("observacion",observacion);
		contenedorRecibo.addComponente(botonAutorizar);
	
	contenedorFormasReverso.addComponente(listReversoFormaPago);
	
		contenedorRecibo.dibujar(this);
		contenedorFormasReverso.dibujar(this);
		addBotonera();
		
	}

	public CompGrupoDatos getContenedorRecibo() {
		return contenedorRecibo;
	}

	public void setContenedorRecibo(CompGrupoDatos contenedorRecibo) {
		this.contenedorRecibo = contenedorRecibo;
	}

	public CompBuscar<Recibo> getRecibo() {
		return recibo;
	}

	public void setRecibo(CompBuscar<Recibo> recibo) {
		this.recibo = recibo;
	}

	public Doublebox getMontoRecibo() {
		return montoRecibo;
	}

	public void setMontoRecibo(Doublebox montoRecibo) {
		this.montoRecibo = montoRecibo;
	}

	public Textbox getObservacion() {
		return observacion;
	}

	public void setObservacion(Textbox observacion) {
		this.observacion = observacion;
	}

	public Label getEstado() {
		return estado;
	}

	public void setEstado(Label estado) {
		this.estado = estado;
	}

	public Label getCedulaRif() {
		return cedulaRif;
	}

	public void setCedulaRif(Label cedulaRif) {
		this.cedulaRif = cedulaRif;
	}

	public Label getCliente() {
		return cliente;
	}

	public void setCliente(Label cliente) {
		this.cliente = cliente;
	}

	public CompGrupoDatos getContenedorFormasReverso() {
		return contenedorFormasReverso;
	}

	public void setContenedorFormasReverso(CompGrupoDatos contenedorFormasReverso) {
		this.contenedorFormasReverso = contenedorFormasReverso;
	}

	public Listbox getListReversoFormaPago() {
		return listReversoFormaPago;
	}

	public void setListReversoFormaPago(Listbox listReversoFormaPago) {
		this.listReversoFormaPago = listReversoFormaPago;
	}

	public List<ReversoFormaPago> getReveversoFormasPagos() {
		return reveversoFormasPagos;
	}

	public void setReveversoFormasPagos(List<ReversoFormaPago> reveversoFormasPagos) {
		this.reveversoFormasPagos = reveversoFormasPagos;
	}

	public List<Recibo> getRecibos() {
		return recibos;
	}

	public void setRecibos(List<Recibo> recibos) {
		this.recibos = recibos;
	} 
	
	public void activarConsulta(){
		montoRecibo.setReadonly(true);
		observacion.setReadonly(true);
		recibo.setDisabled(true);
		botonAutorizar.setVisible(false);
	}

	public Button getBotonAutorizar() {
		return botonAutorizar;
	}

	public void setBotonAutorizar(Button botonAutorizar) {
		this.botonAutorizar = botonAutorizar;
	}
	

}
