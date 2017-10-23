package cpc.demeter.vista.administrativo;

 
import java.util.ArrayList;
import java.util.List;
 
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
import cpc.modelo.demeter.administrativo.NotaDebito;
import cpc.modelo.demeter.administrativo.ReciboDocumentoFiscal;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;


public class UiNotaDebitoRecibo extends CompVentanaBase<NotaDebito> {


	private CompGrupoDatos  	contNota;
	private CompGrupoDatos      contCliente;
	private CompGrupoDatos		contRecibo;
	private CompGrupoDatos      contDetalleSaldo;

	private List<ReciboDocumentoFiscal>  	recibos = new ArrayList<ReciboDocumentoFiscal>();	
	private Listbox 						listRecibos;
	
	
	// Operaciones Sobre un Recibo 
	private Button 			agregar; 
	private Button  		quitar;

	private Doublebox 		saldo;
	private Doublebox 		monto;

	/**************** Datos de la Nota   ****************/
	private Textbox 	nroNota;
	private Textbox		nroDocumento;
	
	private Textbox		fecha;
	private Textbox		estado;
	
	private Textbox		beneficiario;
	private Textbox		nombre;
	

	private Textbox		observacion;
	
	private Textbox		tipoDocumento;
	private Textbox		factura;
	
	
	
	public UiNotaDebitoRecibo(String titulo, int ancho) {
		// TODO Auto-generated constructor stub
		super(titulo, ancho);
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		contNota = new CompGrupoDatos("Datos Nota ",4);
		contNota.setAnchoColumna(0,100);
		contNota.setAnchoColumna(2,100);

		
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
		monto.setTooltiptext(" Monto Total de la Nota ");
		
		
		/*************************** DATOS DE LA Nota **************************/
		nroNota = new Textbox();
		nroNota.setDisabled(true);
		nroNota.setWidth("99%");
		
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
		
		factura = new Textbox(); 
		factura.setDisabled(true);
		factura.setWidth("99%");
		
		
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
		
		contNota.addComponente("Nro Nota  : ",nroNota);
		contNota.addComponente("Nro Documento :",nroDocumento);
		contNota.addComponente("Fecha :",fecha);
		contNota.addComponente("Estado :",estado);
		contNota.addComponente("Tipo  : ",tipoDocumento);
		contNota.addComponente("Factura Asociada  :",factura);
		contNota.addComponente("Observaciones :",observacion);
		contNota.dibujar(this);
		
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
	public void cargarValores(NotaDebito dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub

		NotaDebito nota = (NotaDebito) getModelo();

		nroNota.setValue(nota.getStrNroDocumento());
		nroDocumento.setValue(nota.getNroDocumento().toString());
		
		fecha.setValue(nota.getStrFecha());
		estado.setValue(nota.getStrEstado());
		beneficiario.setValue(nota.getBeneficiario().getIdentidadLegal()); 
		nombre.setValue(nota.getNombreBeneficiario());
		observacion.setValue(nota.getObservacion());
		tipoDocumento.setValue(nota.getStrTipoDocumento());
		factura.setText(nota.getFactura().getStrNroDocumento());
		getBinder().addBinding(saldo, "value",getNombreModelo() + ".montoSaldo", null, null, "save", null,null, null, null);
		monto.setValue(nota.getMontoTotal());
		saldo.setValue(nota.getMontoSaldo());
		
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
			if (recibo.getId()==null){
			montoPagado += recibo.getMonto();
			}
		}

		saldo.setValue((getModelo().getMontoSaldo() -montoPagado < 0 ? 0 :  getModelo().getMontoSaldo() -montoPagado));
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