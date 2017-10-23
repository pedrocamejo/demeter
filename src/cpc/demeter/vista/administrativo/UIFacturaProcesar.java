package cpc.demeter.vista.administrativo;


import java.util.ArrayList;
import java.util.List;

import javax.jws.WebParam.Mode;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.demeter.administrativo.ReciboDocumentoFiscal;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIFacturaProcesar extends CompVentanaBase<DocumentoFiscal> {

	private CompGrupoDatos  	contFactura;
	private CompGrupoDatos      contCliente;
	private CompGrupoDatos		contRecibo;
	private CompGrupoDatos      contDetalleSaldo;

	private List<ReciboDocumentoFiscal>  	recibos = new ArrayList<ReciboDocumentoFiscal>();	
	private Listbox 			listRecibos;
	
	
	// Operaciones Sobre un Recibo 
	private Button 			agregar; 
	private Button  		quitar;

	private Doublebox 		saldo;
	private Doublebox 		monto;

	/**************** Datos de la Factura   ****************/
	private Textbox 	nroFactura;
	private Textbox		nroDocumento;
	
	private Textbox		fecha;
	private Textbox		estado;
	
	private Textbox		beneficiario;
	private Textbox		nombre;
	

	private Textbox		observacion;
	
	private Textbox		tipoDocumento;
	private Textbox		contrato;
	
	
	
	public UIFacturaProcesar(String titulo, int ancho) {
		// TODO Auto-generated constructor stub
		super(titulo, ancho);
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		contFactura = new CompGrupoDatos("Datos Factura",4);
		contFactura.setAnchoColumna(0,100);
		contFactura.setAnchoColumna(2,100);

		
		contRecibo  = new CompGrupoDatos("Datos Recibo ",1);
		
		contDetalleSaldo = new CompGrupoDatos("Datos Saldo ",4);
		contCliente = new CompGrupoDatos("Datos Cliente ",4);
		
		agregar = new Button("Agregar");
		agregar.setTooltiptext("Agregar Recibo ");
		agregar.addEventListener(Events.ON_CLICK,getControlador());
		
		quitar = new Button("Quitar");
		quitar.setTooltiptext("Quitar Recibo ");
		quitar.addEventListener(Events.ON_CLICK,getControlador());
		 
		saldo = new Doublebox();
		saldo.setWidth("99%");
		saldo.setDisabled(true);
		saldo.setTooltiptext(" Cantidad total de la Deuda ");
		
		monto = new Doublebox();
		monto.setWidth("99%");
		monto.setDisabled(true);
		monto.setTooltiptext(" Monto Total de la Factura ");
		
		
		/*************************** DATOS DE LA FACTURA **************************/
		nroFactura = new Textbox();
		nroFactura.setDisabled(true);
		nroFactura.setWidth("99%");
		
		nroDocumento = new Textbox();
		nroDocumento.setDisabled(true);
		nroDocumento.setWidth("99%");
		
		
		fecha = new Textbox();
		fecha.setDisabled(true);
		fecha.setWidth("99%");
		
		estado = new Textbox();
		estado.setDisabled(true);
		estado.setWidth("99%");
		
		beneficiario = new Textbox();
		beneficiario.setDisabled(true);
		beneficiario.setWidth("99%");
		
		nombre = new Textbox();
		nombre.setDisabled(true);
		nombre.setWidth("99%");
		
		
		observacion = new Textbox();
		observacion.setRows(2);
		observacion.setDisabled(true);
		observacion.setWidth("99%");
		
		tipoDocumento= new Textbox();
		tipoDocumento.setDisabled(true);
		tipoDocumento.setWidth("99%");
		
		contrato = new Textbox(); 
		contrato.setDisabled(true);
		contrato.setWidth("99%");
		
		
		listRecibos = new Listbox();
		listRecibos.setMold("paging");
		listRecibos.setPageSize(12);
			/*****titulos de la Lista ****/
			Listhead titulo = new Listhead();
				new Listheader("Nro Recibo").setParent(titulo);
				new Listheader("Fecha").setParent(titulo);
				new Listheader("Monto ").setParent(titulo);
				new Listheader("Saldo ").setParent(titulo);
				Listheader montoheader =  new Listheader("Monto Aplicado ");
				montoheader.setParent(titulo);
				montoheader.setTooltiptext("Saldo Aplicado a la Factura ");

			titulo.setParent(listRecibos);
			listRecibos.setTooltiptext("haga doble Click Detallar un Elemento ");
			listRecibos.setItemRenderer(new ListitemRenderer() {
				@Override
				public void render(Listitem arg0, Object arg1) throws Exception {
					// TODO Auto-generated method stub
					arg0.setValue(arg1);
					arg0.addEventListener(Events.ON_DOUBLE_CLICK,getControlador()); //para seleccionar un elemento de la lista
					ReciboDocumentoFiscal recibo = (ReciboDocumentoFiscal) arg1;
					new Listcell(recibo.getRecibo().getControl()).setParent(arg0);
					new Listcell(recibo.getRecibo().getStrFecha()).setParent(arg0);
					new Listcell(recibo.getRecibo().getStrMonto()).setParent(arg0);
					new Listcell(recibo.getRecibo().getStrSaldo()).setParent(arg0);
					new Listcell(recibo.getStrMonto()).setParent(arg0);
				}
			});
			listRecibos.setModel(new ListModelList(recibos));
		
	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub
		
		contFactura.addComponente("Nro Factura  : ",nroFactura);
		contFactura.addComponente("Nro Documento :",nroDocumento);
		contFactura.addComponente("Fecha :",fecha);
		contFactura.addComponente("Estado :",estado);
		contFactura.addComponente("Tipo  : ",tipoDocumento);
		contFactura.addComponente("Contrato  :",contrato);
	    contFactura.addComponente("Observaciones :",observacion);
		contFactura.dibujar(this);
		
	    contCliente.addComponente("CI/RIF :",beneficiario);
	    contCliente.addComponente("Nombre : ",nombre);
	    contCliente.dibujar(this);

	    Hbox botonera = new Hbox(); 
	    	botonera.appendChild(agregar);
	    	botonera.appendChild(quitar); 
	    
	    contRecibo.addComponente(botonera);
	    contRecibo.addComponente(listRecibos);
	    contRecibo.dibujar(this);
	    
	    contDetalleSaldo.addComponente("Monto :",monto);
	    contDetalleSaldo.addComponente("Saldo :",saldo);
	    
	    contDetalleSaldo.dibujar(this); 
	    
		addBotonera();

	}

	@Override
	public void cargarValores(DocumentoFiscal dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub

		DocumentoFiscal factura = (DocumentoFiscal) getModelo();

		nroFactura.setValue(factura.getStrNroDocumento());
		nroDocumento.setValue(factura.getNroDocumento().toString());
		
		fecha.setValue(factura.getStrFecha());
		estado.setValue(factura.getStrEstado());
		beneficiario.setValue(factura.getBeneficiario().getIdentidadLegal()); 
		nombre.setValue(factura.getNombreBeneficiario());
		observacion.setValue(factura.getObservacion());
		tipoDocumento.setValue(factura.getStrTipoDocumento());

		getBinder().addBinding(saldo, "value",getNombreModelo() + ".montoSaldo", null, null, "save", null,null, null, null);
		monto.setValue(factura.getMontoTotal());
		saldo.setValue(factura.getMontoSaldo());
		
		recibos =new ArrayList<ReciboDocumentoFiscal>(getModelo().getRecibos()); 
		listRecibos.setModel(new ListModelArray(recibos));
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
  
	public List<ReciboDocumentoFiscal> getRecibos() {
		return recibos;
	}

	public void setRecibos(List<ReciboDocumentoFiscal> recibos) {
		this.recibos = recibos;
	}

	public void agregarRecibo(ReciboDocumentoFiscal recibo) {
		
		
		Double diferencia =  saldo.getValue() - recibo.getRecibo().getSaldo();
		Double aplicado =(diferencia< 0 ? saldo.getValue():recibo.getRecibo().getSaldo());
		
		recibo.setMonto(aplicado);
		recibo.getRecibo().setSaldo(recibo.getRecibo().getSaldo() - aplicado);

		recibos.add(recibo);
		listRecibos.setModel(new ListModelArray(recibos));

		calcularSaldo();
	}

	private void calcularSaldo() {
		// TODO Auto-generated method stub
		Double montoPagado = new Double(0);
		
		for(ReciboDocumentoFiscal recibo :recibos )
		{
			if(recibo.getId() == null )
			{
				montoPagado += recibo.getMonto();
			}
		}
		
	
		saldo.setValue((getModelo().getMontoSaldo() -montoPagado < 0 ? 
				   0 :  getModelo().getMontoSaldo() -montoPagado));
	}

	public Doublebox getSaldo() {
		return saldo;
	}

	public void setSaldo(Doublebox saldo) {
		this.saldo = saldo;
	}

	public Listbox getListRecibos() {
		return listRecibos;
	}

	public void setListRecibos(Listbox listRecibos) {
		this.listRecibos = listRecibos;
	}

	public void quitarRecibo(ReciboDocumentoFiscal recibo) {
		// TODO Auto-generated method stub
		//re3cibos no Asignados 
		recibos.remove(recibo);
		listRecibos.setModel(new ListModelArray(recibos));
		calcularSaldo();
		
	}
	
}
