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
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.controlador.mantenimiento.ContSelecionarMaquinaria;
import cpc.demeter.controlador.mantenimiento.garantia.ContSelecionarCliente;
import cpc.modelo.demeter.mantenimiento.Garantia;
import cpc.modelo.demeter.mantenimiento.MaquinariaExterna;
import cpc.modelo.demeter.mantenimiento.TipoGarantia;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UISeleccionarMaquinaria extends  CompVentanaBase<MaquinariaExterna> 
{


	private CompGrupoDatos 				div;
	private Listbox 					maquinarias;
	private List<MaquinariaExterna> 	lista;
	private List<MaquinariaExterna> 	busquedad;


	private Combobox	 		campos; // campos para la busquedad
	private Textbox 			txtbuscar;
	private Button 				buscar;
	private Button 				seleccionar;

	
	public UISeleccionarMaquinaria(String titulo, int ancho, List<MaquinariaExterna> maquinarias, ContSelecionarMaquinaria controlador , IZkAplicacion app) throws SuspendNotAllowedException, InterruptedException, ExcDatosInvalidos {
		// TODO Auto-generated constructor stub
		super(titulo, ancho);
		lista = maquinarias;
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

		maquinarias = new Listbox();
		maquinarias.setMold("paging");
		maquinarias.setPageSize(15);
		maquinarias.setWidth("98%");

		Listhead titulo = new Listhead();

		Listheader column1 = new Listheader("ID ");
		Listheader column2 = new Listheader("serial carroceria ");
		Listheader column3 = new Listheader("serial Motor ");
		Listheader column4 = new Listheader("año fabricacion ");
		Listheader column5 = new Listheader("Modelo ");
		Listheader column6 = new Listheader("Marca ");

		titulo.appendChild(column1);
		titulo.appendChild(column2);
		titulo.appendChild(column3);
		titulo.appendChild(column4);
		titulo.appendChild(column5);
		titulo.appendChild(column6);

		maquinarias.appendChild(titulo);

		maquinarias.setItemRenderer(new ListitemRenderer() {
			
			@Override
			public void render(Listitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				MaquinariaExterna maquinaria = (MaquinariaExterna) arg1;
				arg0.setValue(arg1);
				arg0.appendChild(new Listcell(maquinaria.getId().toString()));
				arg0.appendChild(new Listcell(maquinaria.getSerialcarroceria()));
				arg0.appendChild(new Listcell(maquinaria.getSerialMotor()));
				arg0.appendChild(new Listcell(maquinaria.getAnofabricacion().toString()));
				arg0.appendChild(new Listcell(maquinaria.getDescripcionModelo()));
				arg0.appendChild(new Listcell(maquinaria.getStr_Marca()));
			}
		});
		
		
		
		
		campos = crearbuscar();
		txtbuscar = new Textbox();
		buscar = new Button("Buscar");
		buscar.addEventListener(Events.ON_CLICK, getControlador());
		txtbuscar.addEventListener(Events.ON_OK, getControlador());
		seleccionar.addEventListener(Events.ON_CLICK, getControlador());
		
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
  
		div.addComponente(div1);
		div.dibujar(this);
		
		this.appendChild(maquinarias);
		 
	}

	@Override
	public void cargarValores(MaquinariaExterna dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		maquinarias.setModel(new ListModelArray(lista));
		
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
 

	public Button getSeleccionar() {
		return seleccionar;
	}

	public void setSeleccionar(Button seleccionar) {
		this.seleccionar = seleccionar;
	}

	public Combobox crearbuscar() {
		List lista = new ArrayList();
	 
		Combobox combo = new Combobox();
		combo.setModel(new ListModelArray(lista));

		return combo;
	}
 
	public void buscarCliente() {
 
	
	
	}

	public Object getSeleccionarDato() {
		return (maquinarias.getSelectedItem() == null ? null: maquinarias.getSelectedItem().getValue()); 
	}

	public void modoconsulta(int i) {
		// TODO Auto-generated method stub

		if (Accion.CONSULTAR == i) {
			//agregar.setVisible(false);
			//seleccionar.setVisible(false);

		}
	}
}
