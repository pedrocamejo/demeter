package cpc.demeter.vista.mantenimiento.garantia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.controlador.mantenimiento.garantia.ContSelecionarCliente;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UISeleccionarCliente extends  CompVentanaBase<Cliente> 
{

	private static final long serialVersionUID = 1L;

	private static String Bcedula = "CEDULA";
	private static String BNombre = "Nombre";

	private CompGrupoDatos div;
	private Listbox clientes;
	private List<Cliente> modeloCliente;
	private List<Cliente> busquedad;

	private Button agregar;
	private Combobox campos; // campos para la busquedad
	private Textbox txtbuscar;
	private Button detalle;
	private Button buscar;
	private Button seleccionar;

	
	public UISeleccionarCliente(String titulo, int ancho, List<Cliente> clientes,	ContSelecionarCliente controlador , IZkAplicacion app) throws SuspendNotAllowedException, InterruptedException, ExcDatosInvalidos {
		// TODO Auto-generated constructor stub
		super(titulo,1200);
		this.setPosition("center");
		this.setMaximizable(true);
//		this.setMaximized()
		this.modeloCliente = clientes;
		setControlador(controlador);
		setClosable(true);
		dibujarVentana();
		cargar();
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub

		div = new CompGrupoDatos();
		div.setAlign("right");

		seleccionar = new Button("Seleccionar");
		seleccionar.addEventListener(Events.ON_CLICK, getControlador());
		
		clientes = CrearLista(modeloCliente);
		
		agregar = new Button();
		agregar.addEventListener(Events.ON_CLICK, getControlador());
		
		campos = crearbuscar();
		txtbuscar = new Textbox();
		txtbuscar.addEventListener(Events.ON_OK, getControlador());
		
		buscar = new Button("Buscar");
		buscar.addEventListener(Events.ON_CLICK, getControlador());
		
		detalle = new Button();
		detalle.addEventListener(Events.ON_CLICK, getControlador());
		
		detalle.setImage("/iconos/24x24/consultar.png");
		agregar.setImage("/iconos/24x24/agregar.png");

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
			div2.appendChild(agregar);
			div2.appendChild(detalle);

		div.addComponente(div2, div1);
		div.dibujar(this);
		
		this.appendChild(clientes);
		 
	}

	@Override
	public void cargarValores(Cliente dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub

	}

	public void CrearListaModelo() {
		clientes.detach();
		clientes = CrearLista(modeloCliente);
		this.appendChild(clientes);
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

	public Button getAgregar() {
		return agregar;
	}

	public void setAgregar(Button agregar) {
		this.agregar = agregar;
	}

	public Button getDetalle() {
		return detalle;
	}

	public void setDetalle(Button detalle) {
		this.detalle = detalle;
	}

	public List<Cliente> getModeloCliente() {
		return modeloCliente;
	}

	public void setModeloCliente(List<Cliente> modeloCliente) {
		this.modeloCliente = modeloCliente;
	}

	public Button getSeleccionar() {
		return seleccionar;
	}

	public void setSeleccionar(Button seleccionar) {
		this.seleccionar = seleccionar;
	}

	public Combobox crearbuscar() {
		List lista = new ArrayList();
		lista.add(Bcedula);
		lista.add(BNombre);
		Combobox combo = new Combobox();
		combo.setModel(new ListModelArray(lista));

		return combo;
	}

	public Listbox CrearLista(List<Cliente> clientes) {
		Listbox lista = new Listbox();
		lista.setSclass("tabla_catalago");
		lista.setMold("paging");
 		lista.setPageSize(15);
		lista.setWidth("98%");

		Listhead titulo = new Listhead();

		Listheader column1 = new Listheader("ID ");
		column1.setWidth("10%");
		column1.setAlign("center");
		column1.setSort("auto");

		Listheader column2 = new Listheader("CEDULA ");
		column2.setWidth("40%");
		column2.setAlign("center");

		Listheader column3 = new Listheader("NOMBRE ");
		column3.setWidth("50%");
		column3.setAlign("center");
		column3.setSort("auto");
		/*
		Listheader column4 = new Listheader("Dirección ");
		column4.setWidth("35%");
		column4.setAlign("center");
		column4.setSort("auto");

		Listheader column5 = new Listheader("Telefonos ");
		column5.setWidth("10%");
		column5.setAlign("center");
		column5.setSort("auto");
	*/
		
		titulo.appendChild(column1);
		titulo.appendChild(column2);
		titulo.appendChild(column3);
		//titulo.appendChild(column4);
		//titulo.appendChild(column5);

		lista.appendChild(titulo);

		Iterator<Cliente> iter = clientes.iterator();
		while (iter.hasNext()) {
			Cliente cliente = iter.next();
			Listitem fila = new Listitem();
			fila.setValue(cliente);
			Listcell celda1 = new Listcell(cliente.getId().toString());
			Listcell celda2 = new Listcell(cliente.getIdentidadLegal());
			Listcell celda3 = new Listcell(cliente.getNombres());
			//Listcell celda4 = new Listcell(cliente.getDireccion());
			//Listcell celda5 = new Listcell(cliente.getStrTelefonos());
			fila.appendChild(celda1);
			fila.appendChild(celda2);
			fila.appendChild(celda3);
			//fila.appendChild(celda4);
			//fila.appendChild(celda5);
			
			lista.appendChild(fila);

		}

		return lista;
	}

	public void buscarCliente() {
		busquedad = new ArrayList<Cliente>();

		String dato = txtbuscar.getText().trim();
		String buscarpor = (String) campos.getSelectedItem().getValue();
		Iterator<Cliente> iter = modeloCliente.iterator();

		while (iter.hasNext()) {
			Cliente cliente = iter.next();

			if (buscarpor == BNombre) {
				if (cliente.getNombres().toLowerCase().trim()
						.contains(dato.toLowerCase().trim())) {
					busquedad.add(cliente);
				}
			}
			if (buscarpor == Bcedula) {
				if (cliente.getIdentidadLegal().toLowerCase().trim()
						.contains(dato.toLowerCase().trim())) {

					busquedad.add(cliente);
				}
			}
		}
		clientes.detach();
		clientes = CrearLista(busquedad);
		this.appendChild(clientes);
	}

	public Cliente getseleccionarCliente() {

		Set set = clientes.getSelectedItems();

		if (set.size() == 0) {
			return null;
		}
		Listitem item = (Listitem) set.iterator().next();
		return (Cliente) item.getValue();
	}

	public void modoconsulta(int i) {
		// TODO Auto-generated method stub

		if (Accion.CONSULTAR == i) {
			agregar.setVisible(false);
			//seleccionar.setVisible(false);

		}
	}
}
