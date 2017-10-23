package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Doublebox;

import cpc.demeter.controlador.administrativo.ContNotaCargo;
import cpc.modelo.demeter.administrativo.ConceptoNotaCargo;
import cpc.modelo.demeter.administrativo.NotaCargo;
import cpc.modelo.demeter.administrativo.ReciboNotaCargo;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UiNotaCargo extends CompVentanaBase<NotaCargo>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8614811245933567251L;
	private CompGrupoDatos  encabezado; // grupo general
	private Label numeroNota;
	private Label fecha;
	private CompBuscar<Cliente> cliente;
	private Label nombre;
	private Label telefonos;
	
	private CompGrupoDatos  detalleNota; // grupo general
	private Textbox observacion;
	private Doublebox saldo;
	private Doublebox monto;	
	private CompCombobox<ConceptoNotaCargo> concepto;
	private List<ConceptoNotaCargo> conceptos;
	private List<Cliente> clientes;
	
	//para mostrar grupo de recibos asociados a
	private CompGrupoDatos GrupoRecibos;
	private Listbox				 listRecibos;
	
	
	public UiNotaCargo(String titulo,int ancho, List<ConceptoNotaCargo> conceptos, List<Cliente> clientes) {
		super(titulo,ancho);
		this.conceptos = conceptos;
		this.clientes = clientes;
	}


	public void inicializar(){
		encabezado = new CompGrupoDatos("Datos generales ",2); // grupo general
		numeroNota = new Label();
		fecha = new Label();
		cliente = new CompBuscar<Cliente>(getEncabezadoCliente(),0);
		cliente.setAncho(650);;
		cliente.setListenerEncontrar(getControlador());
		nombre = new Label();
		telefonos = new Label();
		
		detalleNota = new CompGrupoDatos("Detalles de la Nota",0);   // grupo general
		observacion = new Textbox();
		observacion.setMultiline(true);
		observacion.setMaxlength(255);
		observacion.setWidth("800px"); ;
		
	
		saldo =  new Doublebox();
		saldo.setReadonly(true);
		monto =  new Doublebox();
		monto.addEventListener(Events.ON_CHANGE, getControlador());
		concepto =  new CompCombobox<ConceptoNotaCargo>();
		concepto.addEventListener(Events.ON_SELECTION, getControlador());
		
		GrupoRecibos = new CompGrupoDatos("Recibos", 1);
		
		
		/***************         Recibos  ***************/
			listRecibos = new Listbox();
			Listhead tituloRecibo = new Listhead();
			
			Listheader	numeroRecibo = new Listheader("Numero Recibo ");
			Listheader	monto = new Listheader(" Monto Aplicado  ");
						
			tituloRecibo.appendChild(numeroRecibo);
			
			tituloRecibo.appendChild(monto);
			
			listRecibos.appendChild(tituloRecibo);
			listRecibos.setItemRenderer(new ListitemRenderer() {
			
			@Override
			public void render(Listitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				arg0.setValue(arg1);
				ReciboNotaCargo reciboNota =(ReciboNotaCargo ) arg1;
				
				new Listcell(reciboNota.getRecibo().getControl()).setParent(arg0);
				new Listcell(reciboNota.getStrMonto()).setParent(arg0);
				
				
					
				
				
				ContNotaCargo controlador = (ContNotaCargo) getControlador();
				
				
				arg0.addEventListener (Events.ON_DOUBLE_CLICK,controlador.getDetalleRecibo() );
				
			}
			
			});
			
			
			
		
	};
	
	public void dibujar(){
		encabezado.addComponente("fecha",fecha);
		encabezado.addComponente("Nro Control",numeroNota);
		encabezado.addComponente("Cliente",cliente);
		encabezado.addComponente("Nombre",nombre);
		encabezado.addComponente("Telefono",telefonos);
		
		detalleNota.addComponente("Concepto",concepto);
		detalleNota.addComponente("monto",monto);
		detalleNota.addComponente("saldo",saldo);
		detalleNota.addComponente("Observacion",observacion);
		encabezado.dibujar(this);
		detalleNota.dibujar(this);
		GrupoRecibos.addComponente(listRecibos);
		GrupoRecibos.dibujar(this);
		addBotonera();
		
		getBinder().addBinding(this.cliente, "value",	getNombreModelo() + ".cliente", null, null, "save", null,null, null, null);
		//getBinder().addBinding(this.numeroNota, "value",	getNombreModelo() + ".nroCheque", null, null, "save", null,null, null, null);
		getBinder().addBinding(this.monto, "value",	getNombreModelo() + ".monto", null, null, "save", null,null, null, null);
		getBinder().addBinding(this.saldo, "value",	getNombreModelo() + ".montoSaldo", null, null, "save", null,null, null, null);
		getBinder().addBinding(this.concepto, "value",	getNombreModelo() + ".concepto", null, null, "save", null,null, null, null);
		
	};
	
	private List<CompEncabezado> getEncabezadoCliente() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Cedula", 120);
		titulo.setMetodoBinder("getIdentidadLegal");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre", 450);
		titulo.setMetodoBinder("getNombres");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}


	@Override
	public void cargarValores(NotaCargo nota) throws ExcDatosInvalidos {
		if (nota!=null){
			cliente.setModelo(clientes);
		
			concepto.setModelo(conceptos);
			if (nota.getId()!=null){
			//encabezado; // grupo general
			numeroNota.setValue(nota.getNroNotaCargo().toString());
			fecha.setValue(nota.getStrFecha());
		
			cliente.setSeleccion(nota.getCliente());
			cliente.setText(nota.getCedulaRifCliente());
			nombre.setValue(nota.getNombreCliente());;
			telefonos.setValue(nota.getCliente().getStrTelefonos());
			
			
			//detalleNota; // grupo general
			observacion.setText(nota.getObservacion());
			
			saldo.setValue(nota.getMontoSaldo());
			monto.setValue(nota.getMonto());	
			
			
			concepto.setSeleccion(nota.getConcepto());
			ArrayList<ReciboNotaCargo> recibos = new ArrayList<ReciboNotaCargo>(getModelo().getRecibos());
			ListModelList modelListRecibos = new ListModelList(recibos);
			listRecibos.setModel(modelListRecibos);
		
			}
			
		}	
		getBinder().addBinding(concepto, "seleccion",
				getNombreModelo() + ".concepto", null, null, "save", null,
				null, null, null);
		getBinder().addBinding(observacion, "value",
				getNombreModelo() + ".observacion", null, null, "save", null,
				null, null, null);
		getBinder().addBinding(monto, "value",
				getNombreModelo() + ".monto", null, null, "save", null,
				null, null, null);
		getBinder().addBinding(cliente, "seleccion",
				getNombreModelo() + ".cliente", null, null, "save", null, null,
				null, null);
		

		
	}


	public CompGrupoDatos getEncabezado() {
		return encabezado;
	}


	public void setEncabezado(CompGrupoDatos encabezado) {
		this.encabezado = encabezado;
	}


	public Label getNumeroNota() {
		return numeroNota;
	}


	public void setNumeroNota(Label numeroNota) {
		this.numeroNota = numeroNota;
	}


	public Label getFecha() {
		return fecha;
	}


	public void setFecha(Label fecha) {
		this.fecha = fecha;
	}


	public CompBuscar<Cliente> getCliente() {
		return cliente;
	}


	public void setCliente(CompBuscar<Cliente> cliente) {
		this.cliente = cliente;
	}


	public Label getNombre() {
		return nombre;
	}


	public void setNombre(Label nombre) {
		this.nombre = nombre;
	}


	public Label getTelefonos() {
		return telefonos;
	}


	public void setTelefonos(Label telefonos) {
		this.telefonos = telefonos;
	}


	public CompGrupoDatos getDetalleNota() {
		return detalleNota;
	}


	public void setDetalleNota(CompGrupoDatos detalleNota) {
		this.detalleNota = detalleNota;
	}


	public Textbox getObservacion() {
		return observacion;
	}


	public void setObservacion(Textbox observacion) {
		this.observacion = observacion;
	}


	public Doublebox getSaldo() {
		return saldo;
	}


	public void setSaldo(Doublebox saldo) {
		this.saldo = saldo;
	}


	public Doublebox getMonto() {
		return monto;
	}


	public void setMonto(Doublebox monto) {
		this.monto = monto;
	}


	public CompCombobox<ConceptoNotaCargo> getConcepto() {
		return concepto;
	}


	public void setConcepto(CompCombobox<ConceptoNotaCargo> concepto) {
		this.concepto = concepto;
	}


	public List<ConceptoNotaCargo> getConceptos() {
		return conceptos;
	}


	public void setConceptos(List<ConceptoNotaCargo> conceptos) {
		this.conceptos = conceptos;
	}


	public List<Cliente> getClientes() {
		return clientes;
	}


	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}
	
	public void modoConsulta(){
		observacion.setReadonly(true);
		monto.setReadonly(true);
		saldo.setReadonly(true);
		concepto.setDisabled(true);
		cliente.setDisabled(true);
	}


	public CompGrupoDatos getGrupoRecibos() {
		return GrupoRecibos;
	}


	public void setGrupoRecibos(CompGrupoDatos grupoRecibos) {
		GrupoRecibos = grupoRecibos;
	}


	public Listbox getListRecibos() {
		return listRecibos;
	}


	public void setListRecibos(Listbox listRecibos) {
		this.listRecibos = listRecibos;
	};
	
}
