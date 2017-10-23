package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import cpc.ares.modelo.Accion;
import cpc.demeter.controlador.administrativo.ContRecibo;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.FormaPago;
import cpc.modelo.demeter.administrativo.FormaPagoDeposito;
import cpc.modelo.demeter.administrativo.FormaPagoPunto;
import cpc.modelo.demeter.administrativo.FormaPagoTransferencia;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.demeter.administrativo.ReciboDocumentoFiscal;
import cpc.modelo.demeter.administrativo.ReciboNotaCargo;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIRecibo extends CompVentanaBase<Recibo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Cliente 			cliente = null;

	private Date 				 fechaCierre; 
	
	private Textbox 			 control;
	private Textbox              sede;
	private Textbox  			 razonSocial;
	private Textbox 			 estado;
	private Textbox 			 direccionFiscal;
	private Textbox				 concepto;
	
	private Datebox				 fecha;
	
	private CompGrupoDatos		 contenedorCliente;
	private CompGrupoDatos 		 contenedorFacturas;
	private CompGrupoDatos 		 contenedorNotas;
	
	private CompGrupoDatos		 encabezadoRecibo;
	private CompGrupoDatos		 contenedorFormaPago; // para los depositos
	private CompGrupoDatos 		 contenedorPie; // para poner el estado y el saldo  "total Pagado"

	private Doublebox			 monto;
	private Doublebox			 saldo;

	private Listbox				 listDocumento;
	private List<ReciboDocumentoFiscal> documentos = new ArrayList<ReciboDocumentoFiscal>();
	
	private Listbox				 listNota;
	private List<ReciboNotaCargo>       notas		= new ArrayList<ReciboNotaCargo>();
	
	private Listbox				 listFormaPago;
	private List<FormaPago>		 formasPagos = new ArrayList<FormaPago>();
	 
	private Button				 agregarPago;
	private Button				 quitarPago;
	
	
	private Button				seleccionarCliente;
	
	
	public UIRecibo(String titulo, int ancho, Date fechaCierre) throws ExcDatosInvalidos
	{
		super(titulo, ancho);
		this.fechaCierre = fechaCierre;
		inicializar();
	}

	public UIRecibo(Recibo modeloIn, String titulo, int ancho)	throws ExcDatosInvalidos
	{
		super(titulo, ancho);
		setModelo(modeloIn);
	}

	public void inicializar() {

		/***** Datos del Recibo *****/

		encabezadoRecibo= new CompGrupoDatos("Datos Generales ",4);
		contenedorFormaPago = new CompGrupoDatos(" Datos de Pagos "); // para los depositos
		contenedorPie =  new CompGrupoDatos("SALDOS ",4);

		contenedorFacturas = new CompGrupoDatos(" Documentos Fiscales ");
		contenedorNotas =	new CompGrupoDatos("Notas De Cargos");
		  
		control = new Textbox();
		control.setDisabled(true);
		fecha = new Datebox();
		fecha.setDisabled(true);
		sede = new Textbox();
		sede.setWidth("250px");
		sede.setDisabled(true);
		
		direccionFiscal = new  Textbox();
		direccionFiscal.setWidth("100%");
		direccionFiscal.setMaxlength(255);
		direccionFiscal.setRows(3);
		direccionFiscal.setDisabled(true);

		/********** Datos Generales *************/

		contenedorCliente = new CompGrupoDatos("Datos Productor", 2);
			contenedorCliente.setAnchoColumna(0,600);
			contenedorCliente.setAnchoColumna(1,100);

		razonSocial = new Textbox();
		razonSocial.setWidth("650px");
		razonSocial.setDisabled(true);

		/*** forma de Pago *****/
		
		estado = new Textbox();
		estado.setWidth("280px");
		estado.setDisabled(true);

		saldo = new Doublebox();
		saldo.setDisabled(true);
		saldo.setWidth("200px");
		saldo.setFormat("##,###,###,##0.00");
		saldo.setHeight("30px");
		saldo.setStyle("font-size: 20px; font-style:oblique; text-align:right;");
		
		monto = new Doublebox();
		monto.setDisabled(true);
		monto.setWidth("200px");
		monto.setFormat("##,###,###,##0.00");
		monto.setHeight("30px");
		monto.setStyle("font-size: 20px; font-style:oblique ; text-align:right;");

		/*********************** Seleccionar Usuario *********************************/
		
		seleccionarCliente = new Button("");
		seleccionarCliente.setImage("iconos/32x32/productor.png");
		
		/******************   Documentos Fiscales   *************************/
		listDocumento = new Listbox();
			Listhead titulo = new Listhead();
			Listheader header_nroControl = new Listheader("N° Control ");
			header_nroControl.setWidth("12%");
			Listheader header_nroDocumento = new Listheader(" N° Documento ");
			header_nroDocumento.setWidth("12%");
			Listheader header_fecha = new Listheader("Fecha");
			header_fecha.setWidth("12%");
			Listheader header_estado = new Listheader("Estado");
			header_estado.setWidth("12%");
			Listheader header_tipoDocumento = new Listheader(" Tipo Documento");
			header_tipoDocumento.setWidth("12%");
			Listheader header_montoAplicado = new Listheader(" Monto Aplicado ");
			header_montoAplicado.setWidth("14%");
			Listheader header_montoTotal = new Listheader(" Monto Total ");
			header_montoTotal.setWidth("14%");

			titulo.appendChild(header_nroControl);
			titulo.appendChild(header_nroDocumento);
			titulo.appendChild(header_fecha);
			titulo.appendChild(header_estado);
			titulo.appendChild(header_tipoDocumento);
			titulo.appendChild(header_montoAplicado);
			titulo.appendChild(header_montoTotal);

			listDocumento.appendChild(titulo);

			listDocumento.setItemRenderer(new ListitemRenderer() {
			
			@Override
			public void render(Listitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				ReciboDocumentoFiscal documento = (ReciboDocumentoFiscal) arg1;
				arg0.setValue(arg1);
				
				new Listcell(documento.getDocumentoFiscal().getNroControl().toString()).setParent(arg0);
				new Listcell(documento.getDocumentoFiscal().getStrNroDocumento().toString()).setParent(arg0);
				new Listcell(documento.getDocumentoFiscal().getStrFecha()).setParent(arg0);
				new Listcell(documento.getDocumentoFiscal().getStrEstado()).setParent(arg0);
				new Listcell(documento.getDocumentoFiscal().getTipoDocumento().getDescripcion()).setParent(arg0);
				new Listcell(documento.getStrMonto()).setParent(arg0);
				new Listcell(documento.getDocumentoFiscal().getStrTotal()).setParent(arg0);
				
			}
		});
			
			/******************   Notas De cargo   *************************/
			listNota = new Listbox();
				Listhead tituloNota = new Listhead();
				Listheader header_nroNota = new Listheader("N° Control ");
				header_nroControl.setWidth("12%");
				Listheader header_fechaNota = new Listheader("Fecha");
				header_fecha.setWidth("12%");
				Listheader header_estadoNota = new Listheader("Estado");
				header_estado.setWidth("12%");
				Listheader header_montoAplicadoNota = new Listheader(" Monto Aplicado ");
				header_montoAplicado.setWidth("14%");
				Listheader header_montoTotalNota = new Listheader(" Monto Total ");
				header_montoTotal.setWidth("14%");

				tituloNota.appendChild(header_nroNota);
				tituloNota.appendChild(header_fechaNota);
				tituloNota.appendChild(header_estadoNota);
				tituloNota.appendChild(header_montoAplicadoNota);
				tituloNota.appendChild(header_montoTotalNota);

				listNota.appendChild(tituloNota);

				listNota.setItemRenderer(new ListitemRenderer() {
				
				@Override
				public void render(Listitem arg0, Object arg1) throws Exception {
					// TODO Auto-generated method stub
					ReciboNotaCargo nota = (ReciboNotaCargo) arg1;
					arg0.setValue(arg1);
					
					new Listcell(nota.getNotaCargo().getNroNotaCargo().toString()).setParent(arg0);
					new Listcell(nota.getNotaCargo().getStrFecha()).setParent(arg0);
					new Listcell(nota.getNotaCargo().getStrAnulada()).setParent(arg0);
					new Listcell(nota.getStrMonto()).setParent(arg0);
					new Listcell(nota.getNotaCargo().getStrMonto()).setParent(arg0);
					
				}
			});
			 
		 
		/***************         Formas de Pagos   ***************/
		listFormaPago = new Listbox(); 
			Listhead tituloFormaPago = new Listhead();
			
			Listheader	tipoFormaPago = new Listheader("Tipo ");
			Listheader	monto = new Listheader(" Monto  ");
			Listheader	fehcaHeader = new Listheader(" Fecha ");
			
			tituloFormaPago.appendChild(tipoFormaPago);
			tituloFormaPago.appendChild(fehcaHeader);
			tituloFormaPago.appendChild(monto);
			
		listFormaPago.appendChild(tituloFormaPago);
		listFormaPago.setItemRenderer(new ListitemRenderer() {
			
			@Override
			public void render(Listitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				arg0.setValue(arg1);
				FormaPago pago =(FormaPago) arg1;
				if (FormaPagoDeposito.class.equals(arg1.getClass()))
				{
					FormaPagoDeposito deposito = (FormaPagoDeposito) arg1;
					new Listcell(pago.getTipoFormaPago().toString()+"("+deposito.getDocumento()+")").setParent(arg0);
				}
				else if (FormaPagoTransferencia.class.equals(arg1.getClass()))
				{
					FormaPagoTransferencia transferencia = (FormaPagoTransferencia) arg1;
					new Listcell(pago.getTipoFormaPago().toString()+"("+transferencia.getDocumento()+")").setParent(arg0);
				}
				else if (FormaPagoPunto.class.equals(arg1.getClass()))
				{
					FormaPagoPunto pagoPunto = (FormaPagoPunto) arg1;
					new Listcell(pago.getTipoFormaPago().toString()+"("+pagoPunto.getDocumento()+")").setParent(arg0);
				}
				else
				{
					new Listcell(pago.getTipoFormaPago().toString()).setParent(arg0);
				}
				new Listcell(pago.getStrFecha()).setParent(arg0);
				new Listcell(pago.getStrMonto()).setParent(arg0);

				ContRecibo controlador = (ContRecibo) getControlador();
				arg0.addEventListener(Events.ON_DOUBLE_CLICK,controlador.getDetalleFormaPago());				
			}
		});
		
		concepto = new Textbox();
		concepto.setRows(3);
		concepto.setWidth("500px");
		concepto.setMaxlength(250);
		
		
		 agregarPago = new Button("+");
		 quitarPago = new Button("-");	 
	}

	public void dibujar() {

		/************************************ Encabezado ****************************/

		encabezadoRecibo.addComponente("Nro Control  :", control);
		encabezadoRecibo.addComponente("fecha :", fecha);
		encabezadoRecibo.addComponente("Sede  :", sede);
		encabezadoRecibo.addComponente("Concepto  :", concepto);

		
		encabezadoRecibo.dibujar(this);

		/******************************* Datos Cliente  ****************************/
		CompGrupoDatos contenedorDatosCliente = new CompGrupoDatos("Cliente",2);
			contenedorDatosCliente.addComponente("Cliente",razonSocial);
			contenedorDatosCliente.addComponente("Direccion Fiscal :",direccionFiscal);
			contenedorDatosCliente.dibujar();
		
		contenedorCliente.addComponente(contenedorDatosCliente,seleccionarCliente);
		contenedorCliente.setAnchoColumna(1,70);
		contenedorCliente.dibujar(this);
		
		/********************** Facturacion *******************************/
		  
		contenedorFacturas.addComponente(listDocumento); 
		
		/********************** Notas de Cargos *******************************/
		contenedorNotas.addComponente(listNota);
		
		/********************** Forma Pago  *******************************/
		
		Hbox hbox = new Hbox();
			hbox.appendChild(agregarPago);
			hbox.appendChild(quitarPago);
			hbox.setStyle("padding-left:10px");

		contenedorFormaPago.addComponente(hbox);
		contenedorFormaPago.addComponente(listFormaPago);
		
		seleccionarCliente.addEventListener(Events.ON_CLICK,getControlador());
		agregarPago.addEventListener(Events.ON_CLICK,getControlador());
		quitarPago.addEventListener(Events.ON_CLICK,getControlador());
		 
		/***************************    contenedor Pie   ********************************************/

		
		contenedorPie.addComponente("Monto", monto);
		contenedorPie.addComponente("Saldo", saldo);

	}

	public void cargarValores(Recibo recibo) throws ExcDatosInvalidos 
	{
		formasPagos = new ArrayList<FormaPago>(getModelo().getFormaspago());
		listFormaPago.setModel(new ListModelList(formasPagos));
		control.setValue(getModelo().getControl());
		concepto.setValue(getModelo().getConcepto());
		documentos = new ArrayList<ReciboDocumentoFiscal>(recibo.getDocumentosFiscales()); 
		listDocumento.setModel(new ListModelList(documentos));
		notas = new ArrayList<ReciboNotaCargo>(recibo.getNotasCargos());
		listNota.setModel(new ListModelList(notas));
		if(recibo.getCliente() != null)
		{
			razonSocial.setText(recibo.getCliente().getNombres());
			direccionFiscal.setText(recibo.getCliente().getDireccion());
		}
		
		try 
		{
			ContRecibo controlador = (ContRecibo) getControlador();
			controlador.cargarTipo();
		} 
		catch (NullPointerException e)
		{
			
			e.printStackTrace();
		}
		fecha.setValue(getModelo().getFecha());
		estado.setValue(getModelo().getEstado());

		getBinder().addBinding(concepto, "value",getNombreModelo() + ".concepto", null, null, "save", null,null, null, null);
		saldo.setValue(getModelo().getSaldo());
		monto.setValue(getModelo().getMonto());
		getBinder().addBinding(monto, "value", getNombreModelo() + ".monto",null, null, "save", null, null, null, null);
		getBinder().addBinding(saldo, "value", getNombreModelo() + ".saldo",null, null, "save", null, null, null, null);
	
	}
   

	public Textbox getSede() {
		return sede;
	}
 

	public Textbox getRazonSocial() {
		return razonSocial;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void desactivar(int modoOperacion) {
		
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
		{
			 concepto.setDisabled(true);
			 contenedorFacturas.dibujar(this);
			 contenedorNotas.dibujar(this);
			 contenedorFormaPago.dibujar(this); 
			 contenedorPie.dibujar(this);
			 agregarPago.setVisible(false);
			 quitarPago.setVisible(false);
			 seleccionarCliente.setVisible(false);
		
			 if (modoOperacion == Accion.ANULAR)
			 {
				 addBotonera();
			 }
		}
		
	} 

	public void agregarPago(FormaPago pago) throws InterruptedException {
		// TODO Auto-generated method stub
		
		formasPagos.add(pago);
		actualizarSaldos();
		listFormaPago.setModel(new ListModelList(formasPagos));
		//actualizo el saldo de las facturas 
		actualizarSaldos();
	}

	

	public List<FormaPago> getFormasPagos() {
		return formasPagos;
	}

	public void setFormasPagos(List<FormaPago> formasPagos) {
		this.formasPagos = formasPagos;
	}

	public void removerFormaPago(FormaPago dato) 
	{
	 
	}

	public Button getseleccionarCliente() {
		return seleccionarCliente;
	}

	public void setseleccionarCliente(Button seleccionarCliente) {
		this.seleccionarCliente = seleccionarCliente;
	}


	public void actualizarSaldos() { 
		
		Double montoForma = getSaldoFormaPago();
		
		for(ReciboDocumentoFiscal recibo :documentos)
		{
			if(montoForma > recibo.getDocumentoFiscal().getMontoSaldo())
			{
				recibo.setMonto(recibo.getDocumentoFiscal().getMontoSaldo());
				montoForma -= recibo.getMonto();
			}
			else
			{
				recibo.setMonto(montoForma);
				break;
			}
		}
		//actualizo Listo 
		listDocumento.setModel(new ListModelList(documentos));
		montoForma = getSaldoFormaPago();
		Double montoFactura = getSaldoFactura();
		monto.setValue(montoForma-montoFactura);
		saldo.setValue(montoForma);
		
		
	}


	public void setCliente(Cliente cliente) {
		// TODO Auto-generated method stub
		 this.cliente = cliente;
		 seleccionarCliente.setVisible(false);
		 razonSocial.setText(cliente.getNombres());
		 direccionFiscal.setText(cliente.getDireccion());
		 getModelo().setCliente(cliente);
		 contenedorFacturas.dibujar(this);
		 contenedorFormaPago.dibujar(this); // para los depositos
		 contenedorPie.dibujar(this);
		 addBotonera();
	}
 
	public Cliente getCliente() {
		return cliente;
	}

	public Listbox getListDocumento() {
		return listDocumento;
	}

	public void setListDocumento(Listbox listDocumento) {
		this.listDocumento = listDocumento;
	}

	private Double getSaldoFactura() {
		Double saldo = new Double(0);
		for(ReciboDocumentoFiscal documento : documentos)
		{
			saldo += documento.getMonto();
		}
		return saldo;
	}

	private Double getSaldoFormaPago() {
		// TODO Auto-generated method stub
		Double saldo = new Double(0);
		for(FormaPago forma : formasPagos)
		{
			saldo += forma.getMonto();
		}
		return saldo;
	}

	public void quitarDocumentoFiscal() throws InterruptedException {
		// TODO Auto-generated method stub
		if(listDocumento.getSelectedItem() == null )
		{
			Messagebox.show("Usted Debe Seleccionar un Item ");
		}
		else
		{
			DocumentoFiscal documento = (DocumentoFiscal) listDocumento.getSelectedItem().getValue();
			documentos.remove(documento);
			listDocumento.setModel(new ListModelList(documentos));
			actualizarSaldos();
		}
	}

	public Button getAgregarPago() {
		return agregarPago;
	}

	public void setAgregarPago(Button agregarPago) {
		this.agregarPago = agregarPago;
	}

	public Button getQuitarPago() {
		return quitarPago;
	}

	public void setQuitarPago(Button quitarPago) {
		this.quitarPago = quitarPago;
	}

	public Listbox getListFormaPago() {
		return listFormaPago;
	}

	public void setListFormaPago(Listbox listFormaPago) {
		this.listFormaPago = listFormaPago;
	}

	public List<ReciboDocumentoFiscal> getDocumentos() {
		return documentos;
	}

	public void setDocumentos(List<ReciboDocumentoFiscal> documentos) {
		this.documentos = documentos;
	}

	public void setRazonSocial(Textbox razonSocial) {
		this.razonSocial = razonSocial;
	}

	public Textbox getDireccionFiscal() {
		return direccionFiscal;
	}

	public void setDireccionFiscal(Textbox direccionFiscal) {
		this.direccionFiscal = direccionFiscal;
	}

	public Doublebox getMonto() {
		return monto;
	}

	public void setMonto(Doublebox monto) {
		this.monto = monto;
	}

	public Doublebox getSaldo() {
		return saldo;
	}

	public void setSaldo(Doublebox saldo) {
		this.saldo = saldo;
	}
 	
	
	
}