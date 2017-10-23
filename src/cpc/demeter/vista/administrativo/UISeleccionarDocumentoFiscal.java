package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.controlador.administrativo.ContSeleccionarDocumentoFiscal;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UISeleccionarDocumentoFiscal extends CompVentanaBase<DocumentoFiscal> 
{

	private static final long serialVersionUID = 1L;
	
	public static String NControl = "N° Control";
	public static String NDocumento = "N° Documento";
	public static String Fecha = "Fecha";
	public static String Estado = "Estado";
	public static String TipoDocumento = "TipoDocumento";
	public static String Contrato = "Contrato";
	public static String MontoTotal = "MontoTotal";
	public static String MontoSaldo = "MontoSaldo";
	
	private CompGrupoDatos        div;
	
	private Listbox 			  documentosFiscales;
	private List<DocumentoFiscal> modelo = new ArrayList<DocumentoFiscal>();
	private List<DocumentoFiscal> busquedad = new ArrayList<DocumentoFiscal>();

	private Combobox 			  campos; // campos para la busquedad
	private Textbox 			  txtbuscar;
	private Button 				  detalle;
	private Button 				  buscar;
	private Button				  seleccionar;
 

	
	public UISeleccionarDocumentoFiscal(String titulo, int ancho, List<DocumentoFiscal> modelo,	ContSeleccionarDocumentoFiscal controlador , IZkAplicacion app) throws SuspendNotAllowedException, InterruptedException, ExcDatosInvalidos {
		// TODO Auto-generated constructor stub
		super(titulo, ancho);
		this.setMaximizable(true);
		this.modelo = modelo;
		setControlador(controlador);
		setClosable(true);
		dibujarVentana();
		cargar();
	} 

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub

		div = new CompGrupoDatos();
		seleccionar = new Button("Seleccionar");

		campos = crearbuscar();
		
			
		txtbuscar = new Textbox();
		buscar = new Button("Buscar");
			buscar.addEventListener(Events.ON_CLICK,getControlador());
		
		detalle = new Button("detalle");
		buscar.addEventListener(Events.ON_CLICK, getControlador());
		txtbuscar.addEventListener(Events.ON_OK, getControlador());

		detalle.addEventListener(Events.ON_CLICK, getControlador());
		seleccionar.addEventListener(Events.ON_CLICK, getControlador());

		detalle.setImage("/iconos/24x24/consultar.png");
		
		documentosFiscales = new Listbox();
		
		documentosFiscales.setItemRenderer(new ListitemRenderer() {
			
			@Override
			public void render(Listitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				
				DocumentoFiscal documento = (DocumentoFiscal) arg1;
				arg0.setValue(arg1);

				new Listcell(documento.getNroControl().toString()).setParent(arg0);
				new Listcell(documento.getStrNroDocumento().toString()).setParent(arg0);
				new Listcell(documento.getStrFecha()).setParent(arg0);
				new Listcell(documento.getStrEstado()).setParent(arg0);
				new Listcell(documento.getTipoDocumento().getDescripcion()).setParent(arg0);
				new Listcell((documento.getStrNroContrato() == null ? "S/C":documento.getStrNroContrato())).setParent(arg0);
				new Listcell(documento.getStrTotal()).setParent(arg0);
				new Listcell(documento.getStrSaldo()).setParent(arg0);
				
			}
		});
		
		documentosFiscales.setModel(new ListModelArray(modelo));
		documentosFiscales.setMold("paging");
		documentosFiscales.setPageSize(12);
		
		
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
		Listheader header_contrato = new Listheader(" Contrato");
		header_contrato.setWidth("12%");
		Listheader header_montoTotal = new Listheader(" Monto Total");
		header_montoTotal.setWidth("14%");
		Listheader header_montoSaldo = new Listheader(" Monto Saldo ");
		header_montoSaldo.setWidth("14%");

		
		titulo.appendChild(header_nroControl);
		titulo.appendChild(header_nroDocumento);
		titulo.appendChild(header_fecha);
		titulo.appendChild(header_estado);
		titulo.appendChild(header_tipoDocumento);
		titulo.appendChild(header_contrato);
		titulo.appendChild(header_montoTotal);
		titulo.appendChild(header_montoSaldo);
		
		documentosFiscales.appendChild(titulo);
			
		
	}
 

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub
		
		Hbox div1 = new Hbox();
			div1.appendChild(campos);
			div1.appendChild(txtbuscar);
			div1.appendChild(buscar);
			
			Div div_aux = new Div();
				div1.appendChild(div_aux);
				div_aux.setAlign("rigth");
				div_aux.appendChild(seleccionar);
			
	
		Hbox div2 = new Hbox();
			div2.appendChild(detalle);

		div.addComponente(div2, div1);
		div.dibujar(this);
		this.appendChild(documentosFiscales);
		
	}

	
	public Textbox getTxtbuscar() {
		return txtbuscar;
	}

	public void setTxtbuscar(Textbox txtbuscar) {
		this.txtbuscar = txtbuscar;
	}

	public Button getBuscar() {
		return buscar;
	}

	public void setBuscar(Button buscar) {
		this.buscar = buscar;
	}

	public Combobox getCampos() {
		return campos;
	}

	public void setCampos(Combobox campos) {
		this.campos = campos;
	}

	public Button getDetalle() {
		return detalle;
	}

	public void setDetalle(Button detalle) {
		this.detalle = detalle;
	}


	public Button getSeleccionar() {
		return seleccionar;
	}

	public void setSeleccionar(Button seleccionar) {
		this.seleccionar = seleccionar;
	}

	public Combobox crearbuscar() {
		List lista = new ArrayList();
			lista.add(NControl);
			lista.add(NDocumento);
			lista.add(Fecha);
			lista.add(Estado);
			lista.add(TipoDocumento);
			lista.add(Contrato);
			lista.add(MontoTotal);
			lista.add(MontoSaldo);
		
		Combobox combo = new Combobox();
		combo.setModel(new ListModelArray(lista));
		
		return combo;
	}
 
	public void buscarCliente() {
		
		busquedad = new ArrayList<DocumentoFiscal>();
		
		String textoBuscar = txtbuscar.getText().trim().toLowerCase();
		
		if(textoBuscar.length() == 0 )
		{
			documentosFiscales.setModel(new ListModelArray(modelo));
			return;
		}
		System.out.println(campos.getSelectedItem());
		for(DocumentoFiscal documento : modelo)
		{	
			if (campos.getSelectedItem().getValue().equals(NControl))
			{
				if (documento.getNroControl().toString().trim().toLowerCase().contains(textoBuscar))
				{
					busquedad.add(documento);
				}
			}
			else if (campos.getSelectedItem().getValue().equals(NDocumento))
			{
				if (documento.getStrNroDocumento().toString().trim().toLowerCase().contains(textoBuscar))
				{
					busquedad.add(documento);
				}
			} 
			else if (campos.getSelectedItem().getValue().equals(Fecha))
			{
				if (documento.getStrFecha().toString().trim().toLowerCase().contains(textoBuscar))
				{
					busquedad.add(documento);
				}
			} 
			else if (campos.getSelectedItem().getValue().equals(Fecha))
			{
				if (documento.getStrFecha().toString().trim().toLowerCase().contains(textoBuscar))
				{
					busquedad.add(documento);
				}
			} 
			else if (campos.getSelectedItem().getValue().equals(Estado))
			{
				if (documento.getStrEstado().toString().trim().toLowerCase().contains(textoBuscar))
				{
					busquedad.add(documento);
				}
			} 
			else if (campos.getSelectedItem().getValue().equals(TipoDocumento))
			{
				if (documento.getTipoDocumento().getDescripcion().toString().trim().toLowerCase().contains(textoBuscar))
				{
					busquedad.add(documento);
				}
			}
			else if (campos.getSelectedItem().getValue().equals(Contrato))
			{
				if (documento.getStrNroContrato().toString().trim().toLowerCase().contains(textoBuscar))
				{
					busquedad.add(documento);
				}
			}
			else if (campos.getSelectedItem().getValue().equals(MontoTotal))
			{
				if (documento.getStrTotal().toString().trim().toLowerCase().contains(textoBuscar))
				{
					busquedad.add(documento);
				}
			} 
			else if (campos.getSelectedItem().getValue().equals(MontoSaldo))
			{
				if (documento.getStrSaldo().toString().trim().toLowerCase().contains(textoBuscar))
				{
					busquedad.add(documento);
				}
			} 
		}	
		documentosFiscales.setModel(new ListModelArray(busquedad));
	}


	public Listbox getDocumentosFiscales() {
		return documentosFiscales;
	}

	public void setDocumentosFiscales(Listbox documentosFiscales) {
		this.documentosFiscales = documentosFiscales;
	}

	public void modoconsulta(int i) {
		// TODO Auto-generated method stub
		if (Accion.CONSULTAR == i) {
			//seleccionar.setVisible(false);

		}
	}

	@Override
	public void cargarValores(DocumentoFiscal dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		
	}
 
}
