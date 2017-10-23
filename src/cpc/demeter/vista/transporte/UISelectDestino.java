package cpc.demeter.vista.transporte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Combobox; 
import org.zkoss.zul.Div;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox; 
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Window;
 
import cpc.demeter.controlador.transporte.ContSolicitudTransporte;
import cpc.modelo.demeter.transporte.Destino;
import cpc.modelo.ministerio.dimension.UbicacionEstado; 
import cpc.modelo.ministerio.dimension.UbicacionParroquia;
import cpc.modelo.ministerio.dimension.UbicacionSector;
import cpc.zk.componente.ventanas.CompGrupoDatos;

public class UISelectDestino extends Window{


	private static final long serialVersionUID = 1L;

	private Div						gbGeneral;		
	private Combobox				estado;
	private Listbox				    destino;
	private Grid					grilla;
	private Button					aceptar,cancelar;
	private Div						divisorLista; // Para el Listbox :D 
	
	private List<UbicacionEstado>   estados;
	private ContSolicitudTransporte       controlador; // controlador para poder tener Poder de los eventos jejejeje
	 
	 

	public UISelectDestino() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Combobox getEstado() {
		return estado;
	}

	public void setEstado(Combobox estado) {
		this.estado = estado;
	}

	public UISelectDestino(String title, String border, boolean closable,List<UbicacionEstado> estados,ContSolicitudTransporte controlador) {
		super(title, border, closable);
		// TODO Auto-generated constructor stub
		
    	this.controlador = controlador;
    	this.setWidth("500px");
		this.estados = estados;
		aceptar = new Button("Aceptar");
		cancelar = new Button("Cancelar");
		
		gbGeneral = new CompGrupoDatos();		
		estado = new Combobox(); 
		destino = new Listbox();
		grilla = new Grid();
		divisorLista = new Div();
		
		dibujar();
	}
	
	public void dibujar()
	{
		Columns columnas = new Columns();
		Column colmn1 = new Column();
		Column colmn2= new Column();
		columnas.appendChild(colmn1);
		columnas.appendChild(colmn2);
		
		grilla.appendChild(columnas);
		Rows filas = new Rows();
		
		ListModel modelo = new ListModelArray(estados);
		estado.setModel(modelo);
		
		
		Row fila1 = new Row();
			Label label1 = new Label("Estados ");
			fila1.appendChild(label1);
			fila1.appendChild(estado);
		filas.appendChild(fila1);
	 
		grilla.appendChild(filas);
		this.appendChild(grilla);
	    
		CargarDestinos(new ArrayList<Object>());
		
		divisorLista.appendChild(destino);
		this.appendChild(divisorLista);
	    
		Div divisor = new Div();
		divisor.appendChild(aceptar);
		divisor.appendChild(cancelar);
		divisor.setAlign("rigth");
		
		this.appendChild(divisor);
		aceptar.addEventListener(Events.ON_CLICK,controlador);
		cancelar.addEventListener(Events.ON_CLICK,controlador);
		estado.addEventListener(Events.ON_CHANGE,controlador); 
	//	parroquia.addEventListener(Events.ON_CHANGE,controlador);
	//	sector.addEventListener(Events.ON_CHANGE,controlador);
		
	}
	
	public void CargarDestinos(List<Object> destinos )
	{
		Map mapa = new HashMap();
		mapa.put("lista",destinos);
		destino.detach();
		destino = (Listbox) Executions.createComponents("/SelectDestino.zul",null,mapa);
		divisorLista.appendChild(destino);
	}

	public Boolean buscar()
	{
		return null;
	}
	
	public Destino DatoSeleccionado()
	{
		return (Destino) destino.getSelectedItem().getValue();
		
	}
	 
	public void CargarSectores(List<UbicacionSector> sectores)
	{	
		ListModel modelo = new ListModelArray(sectores);
	//	sector.setModel(modelo);
		
	}
	public void CargarParroquias(List<UbicacionParroquia> parroquias)
	{	
		ListModel modelo = new ListModelArray(parroquias);
	//	parroquia.setModel(modelo);
	}

	public Listbox getDestino() {
		return destino;
	}

	public void setDestino(Listbox destino) {
		this.destino = destino;
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
	
	public void cerrar()
	{
		this.detach();
		
	}
	
}
