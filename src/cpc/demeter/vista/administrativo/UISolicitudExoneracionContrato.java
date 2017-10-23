package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.controlador.administrativo.ContFactura;
import cpc.demeter.controlador.administrativo.ContSolicitudExoneracionContrato;
import cpc.modelo.demeter.administrativo.AprobacionReversoRecibo;
import cpc.modelo.demeter.administrativo.Contrato;
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.administrativo.ReciboDocumentoFiscal;
import cpc.modelo.demeter.administrativo.SolicitudExoneracionContrato;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UISolicitudExoneracionContrato extends CompVentanaBase<SolicitudExoneracionContrato>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -682997605512629804L;
	private int accion;
	private CompGrupoDatos gbGeneral;
	private CompBuscar<Contrato> contrato;
	private Label numerocontrato,pagador,cedrif,monto,fechacontrato,fechaSolictud,fechaaprobacionRechazo;
	private Listbox				 listDetalles;
	private Button 	SubirArchivo,BajarArchivo;
	private Textbox motivo;
	private List<Contrato> contratos;
	public UISolicitudExoneracionContrato
	(String titulo, int ancho, int modo,List<Contrato> contratos
			){
		super(titulo, ancho);
		this.accion = modo;
		this.contratos=contratos;
	}
			
	private List<CompEncabezado> getEncabezadoContrato() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Nro control", 140);
		// titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrNroDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("fecha", 100);
		titulo.setMetodoBinder("getFechaString");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Productor", 250);
		titulo.setMetodoBinder("getNombreCliente");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;

	}
	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		gbGeneral = new CompGrupoDatos("Datos generales", 2);
		contrato=new CompBuscar<Contrato>(getEncabezadoContrato(),0);
		contrato.setAncho(550);
		contrato.setModelo(contratos);
		numerocontrato = new Label("numero Contrato");
		pagador = new Label("Pagador");
		cedrif = new Label("CedRif");
		monto = new Label("Monto");
		fechacontrato = new Label("Fecha Contrato");
		fechaSolictud = new Label("Fecha Solictud");
		motivo = new Textbox();
		motivo.setWidth("550px");
		motivo.setMultiline(true);
		fechaaprobacionRechazo = new Label();
		listDetalles = new Listbox();
		SubirArchivo=new Button("Subir Archivo");
		SubirArchivo.addEventListener(Events.ON_CLICK, getControlador());
		BajarArchivo=new Button("Bajar Archivo");
		BajarArchivo.addEventListener(Events.ON_CLICK, getControlador());
		Listhead tituloDetalles = new Listhead();
		
		Listheader	servicioProducto = new Listheader("servicio Producto");
		Listheader	cantidadinicio = new Listheader(" Cantidad Inicio ");
		Listheader	cantidadFinal = new Listheader(" Cantidad Final");
		Listheader	Precio = new Listheader(" precio  ");
					
		tituloDetalles.appendChild(servicioProducto);
		tituloDetalles.appendChild(cantidadinicio);
		tituloDetalles.appendChild(cantidadFinal);
		tituloDetalles.appendChild(Precio);
		
		listDetalles.appendChild(tituloDetalles);
		listDetalles.setItemRenderer(new ListitemRenderer() {
		
		@Override
		public void render(Listitem arg0, Object arg1) throws Exception {
			// TODO Auto-generated method stub
			arg0.setValue(arg1);
			DetalleContrato detalleContrato =(DetalleContrato ) arg1;
			
			new Listcell(detalleContrato.getStrProducto()).setParent(arg0);
			new Listcell(detalleContrato.getCantidad().toString()).setParent(arg0);
			new Listcell(String.valueOf(detalleContrato.getCantidadReal())).setParent(arg0);
			new Listcell(detalleContrato.getSubTotal()).setParent(arg0);
			
			ContSolicitudExoneracionContrato controlador = (ContSolicitudExoneracionContrato) getControlador();
			
			
		//	arg0.addEventListener (Events.ON_DOUBLE_CLICK,controlador.getDetalleRecibo() );
			
		}
	});
	}
	@Override
	public void dibujar() {
		// TODO Auto-generated method stub
		gbGeneral.addComponente("fecha Sol:",fechaSolictud)
		;
		gbGeneral.addComponente("Contrato", contrato);
		gbGeneral.addComponente("Pagador",pagador);
		gbGeneral.addComponente("CedRif",cedrif);
		gbGeneral.addComponente("Monto: ",monto);
		gbGeneral.addComponente("motivo",motivo);
		gbGeneral.addComponente("Fecha Contrato:",fechacontrato);
		gbGeneral.addComponente("Fecha Aprobacion Rechazo",fechaaprobacionRechazo);
		gbGeneral.addComponente("Detalles Contrato",listDetalles);
		contrato.setListenerEncontrar(getControlador());
		gbGeneral.addComponente("Bajar Solicitud",SubirArchivo);
		gbGeneral.addComponente("Subir Aprobacion/Rechazo",BajarArchivo);
		
		gbGeneral.dibujar(this);
		addBotonera();
	}
	@Override
	public void cargarValores(SolicitudExoneracionContrato datos)
			throws ExcDatosInvalidos {
		SolicitudExoneracionContrato dato = getModelo();
		fechaSolictud.setValue(dato.getFechaSolicitud().toString());
		if (accion!=Accion.AGREGAR){
		contrato.setSeleccion(dato.getContrato());
		contrato.setText(dato.getContrato().getStrNroDocumento());
		pagador.setValue(dato.getContrato().getPagador().getNombres());
		cedrif.setValue(dato.getContrato().getPagador().getIdentidadLegal());
		monto.setValue(dato.getContrato().getMonto().toString());
		motivo.setValue(dato.getMotivo());
		
		fechacontrato.setValue(dato.getContrato().getFechaString());
		if (dato.getFechaAprobacion()!=null){
			fechaaprobacionRechazo.setValue(dato.getStrFechaAprobacion());
			
		}
		if (dato.getContrato().getDetallesContrato()!=null)
			listDetalles.setModel(new ListModelList(dato.getContrato().getDetallesContrato()));
		
		
		
		
		// TODO Auto-generated method stub
		}
		
	}

	public CompGrupoDatos getGbGeneral() {
		return gbGeneral;
	}

	public void setGbGeneral(CompGrupoDatos gbGeneral) {
		this.gbGeneral = gbGeneral;
	}

	public CompBuscar<Contrato> getContrato() {
		return contrato;
	}

	public void setContrato(CompBuscar<Contrato> contrato) {
		this.contrato = contrato;
	}

	public Label getNumerocontrato() {
		return numerocontrato;
	}

	public void setNumerocontrato(Label numerocontrato) {
		this.numerocontrato = numerocontrato;
	}

	public Label getPagador() {
		return pagador;
	}

	public void setPagador(Label pagador) {
		this.pagador = pagador;
	}

	public Label getCedrif() {
		return cedrif;
	}

	public void setCedrif(Label cedrif) {
		this.cedrif = cedrif;
	}

	public Label getMonto() {
		return monto;
	}

	public void setMonto(Label monto) {
		this.monto = monto;
	}

	public Label getFechacontrato() {
		return fechacontrato;
	}

	public void setFechacontrato(Label fechacontrato) {
		this.fechacontrato = fechacontrato;
	}

	public Label getFechaSolictud() {
		return fechaSolictud;
	}

	public void setFechaSolictud(Label fechaSolictud) {
		this.fechaSolictud = fechaSolictud;
	}

	public Label getFechaaprobacionRechazo() {
		return fechaaprobacionRechazo;
	}

	public void setFechaaprobacionRechazo(Label fechaaprobacionRechazo) {
		this.fechaaprobacionRechazo = fechaaprobacionRechazo;
	}

	public Listbox getListDetalles() {
		return listDetalles;
	}

	public void setListDetalles(Listbox listDetalles) {
		this.listDetalles = listDetalles;
	}

	public Button getSubirArchivo() {
		return SubirArchivo;
	}

	public void setSubirArchivo(Button subirArchivo) {
		SubirArchivo = subirArchivo;
	}

	public Button getBajarArchivo() {
		return BajarArchivo;
	}

	public void setBajarArchivo(Button bajarArchivo) {
		BajarArchivo = bajarArchivo;
	}

	public List<Contrato> getContratos() {
		return contratos;
	}

	public void setContratos(List<Contrato> contratos) {
		this.contratos = contratos;
	}
	public void desactivar(int modoOperacion,SolicitudExoneracionContrato solicitud) {
		if (modoOperacion==Accion.AGREGAR)
		modoAgregar();
		if (modoOperacion==Accion.PROCESAR)
			modoProcesar();
		if (modoOperacion==Accion.CONSULTAR)
			modoConsulta(solicitud);

	}
    
    private void modoAgregar(){
    	getBajarArchivo().detach();
    	getSubirArchivo().detach();
    }
    private void modoConsulta(SolicitudExoneracionContrato solicitud){
    	getContrato().setDisabled(true);
    	getSubirArchivo().detach();
    	getMotivo().setReadonly(true);
    	System.out.println(solicitud.getStrEstadoExoneracion());
    	if (!solicitud.getStrEstadoExoneracion().equals("POR EXONERAR"))
    		getBajarArchivo().detach();
    }
    private void modoProcesar(){
    	getContrato().setDisabled(true);
    	getBajarArchivo().detach();
    	getAceptar().setDisabled(true);
    	getMotivo().setReadonly(true); 
    }

	public Textbox getMotivo() {
		return motivo;
	}

	public void setMotivo(Textbox motivo) {
		this.motivo = motivo;
	}
    
}
