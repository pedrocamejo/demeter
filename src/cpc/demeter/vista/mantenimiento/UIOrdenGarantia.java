package cpc.demeter.vista.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;

import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.mantenimiento.Actividad;
import cpc.modelo.demeter.mantenimiento.DetalleGarantina;
import cpc.modelo.demeter.mantenimiento.OrdenGarantia;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIOrdenGarantia extends CompVentanaBase<OrdenGarantia> {

	private CompGrupoDatos 				divOrden;
	private CompGrupoDatos				divMaquinaria;
	private CompGrupoDatos 				procesar;
	
	private Textbox 					serialCarroceria;
	private Textbox						propietario;
	private Textbox						marcaModelo;
	private Textbox						direccionMaquinaria;
	private Textbox 					encargado;
	private Textbox		 				nota;
	
	
	private List<DetalleGarantina> 	 	lisGarantia;
	private DetalleGarantina 			detalleGarantia;
	private Combobox 					combGarantia;
	private List<Actividad>				lisActividades = new ArrayList<Actividad>();
	private Listbox			 			actividades;
	private Div 						lisDiv; 

	private List<DetalleGarantina> 		detallesGarantia = new ArrayList<DetalleGarantina>();
	private Label 						cantidad; // para mostrar la cantidad
	private Datebox			 			fecha;
	private Intbox 						km;
	
	private Button						detalle;
	
	
	public UIOrdenGarantia(String titulo, int ancho) {
		super(titulo, ancho);
		// TODO Auto-generated constructor stub
	}

 
	public UIOrdenGarantia(String titulo) {
		super(titulo);
		// TODO Auto-generated constructor stub
	}

	public List<DetalleGarantina> getLisGarantia() {
		return lisGarantia;
	}

	public void setLisGarantia(List<DetalleGarantina> lisGarantia) {
		this.lisGarantia = lisGarantia;
	}

	public DetalleGarantina getDetalleGarantia() {
		return detalleGarantia;
	}

	public void setDetalleGarantia(DetalleGarantina detalleGarantia) {
		this.detalleGarantia = detalleGarantia;
	}

	public List<Actividad> getLisActividades() {
		return lisActividades;
	}

	public void setLisActividades(List<Actividad> lisActividades) {
		this.lisActividades = lisActividades;
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub

		procesar = new CompGrupoDatos("Procesar Orden de Trabajo",4);
		divOrden = new CompGrupoDatos("Orde de Trabajo  ",4);
		divMaquinaria = new CompGrupoDatos("Datos Maquinaria ",4 );

		
		serialCarroceria = new Textbox();
		serialCarroceria.setDisabled(true);
		serialCarroceria.setWidth("90%");
 
		propietario = new Textbox();
		propietario.setDisabled(true);
		propietario.setWidth("90");

		marcaModelo = new Textbox();
		marcaModelo.setDisabled(true);
		marcaModelo.setWidth("90");

		direccionMaquinaria = new Textbox();
		direccionMaquinaria.setDisabled(true);
		direccionMaquinaria.setWidth("90");
 
		combGarantia = new Combobox();
		cantidad = new Label();
		fecha = new Datebox();
		km = new Intbox();
		
		nota = new Textbox();
		nota.setMaxlength(250);
		nota.setWidth("90%");
		nota.setRows(3);

		combGarantia.addEventListener(Events.ON_SELECT, getControlador());
		combGarantia.setTooltiptext("Selecionar un 1er mantenimiento 2do , 3er ... ect");

		encargado = new Textbox();

		lisDiv = new Div();
		
		detalle = new Button("detalle");
		detalle.addEventListener(Events.ON_CLICK,getControlador());

	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub
		
		divMaquinaria.addComponente("Serial Carroceria ",serialCarroceria);
		divMaquinaria.addComponente(" Marca Modelo ",marcaModelo);
		divMaquinaria.addComponente(" Prpietario ",propietario);
		divMaquinaria.addComponente(" Direccion Maquinaria  ",direccionMaquinaria);
		divMaquinaria.dibujar(this);

		divOrden.addComponente("Encargado ", encargado);
		divOrden.addComponente("Detalle Garantia ", combGarantia);
		divOrden.dibujar(this);
		divOrden.appendChild(detalle);

		CompGrupoDatos cont = new CompGrupoDatos("Listado de Actividades  ",1);
		cont.addComponente(detalle);
		cont.addComponente(lisDiv);
		cont.addComponente(cantidad);
		cont.dibujar();
		divOrden.appendChild(cont);
		
		crearCombo();
		procesar.addComponente("Fecha ", fecha);
		procesar.addComponente("Km ", km);
		procesar.addComponente("Nota ", nota);
 


		addBotonera();

	} 

	private void crearCombo() {
		// TODO Auto-generated method stub
		for (DetalleGarantina g : detallesGarantia) {
			Comboitem items = new Comboitem();
			items.setValue(g);
			items.setLabel(g.getDescripcion());
			combGarantia.appendChild(items);
			combGarantia.setSelectedItem(items);
		}
	}

	@Override
	public void cargarValores(OrdenGarantia dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
 
		serialCarroceria.setText(getModelo().getSerialcarroceria());
		propietario.setText(getModelo().getMaquinaria().getPropietario().getNombres());
		marcaModelo.setText(getModelo().getMaquinaria().getTipo().getMarcaSTR() + " "+ getModelo().getMaquinaria().getTipo().getModeloSTR());
		direccionMaquinaria.setText(getModelo().getMaquinaria().getDescripcionModelo());

		encargado.setText(getModelo().getEncargado());
		km.setValue(getModelo().getKm());
		fecha.setValue(getModelo().getFecha());
		nota.setValue(getModelo().getNota());

		if (getModelo().getDetalleGarantia() != null) {
			detalleGarantia = getModelo().getDetalleGarantia();
		}
		lisActividades = getModelo().getActividades();

		getBinder().addBinding(encargado, "value",
				getNombreModelo() + ".encargado", null, null, "save", null,
				null, null, null);
		getBinder().addBinding(km, "value", getNombreModelo() + ".km", null,
				null, "save", null, null, null, null);
		getBinder().addBinding(fecha, "value", getNombreModelo() + ".fecha",
				null, null, "save", null, null, null, null);
		getBinder().addBinding(nota, "value", getNombreModelo() + ".nota",
				null, null, "save", null, null, null, null);

		MostrarActividades(getModelo().getActividades());
		seleccionarItems(getModelo().getDetalleGarantia());

	}

	public void seleccionarItems(DetalleGarantina detalleGarantia) {
		if (detalleGarantia != null) {
			Comboitem items = new Comboitem();
			items.setValue(detalleGarantia);
			items.setLabel(detalleGarantia.getDescripcion());
			combGarantia.appendChild(items);
			combGarantia.setSelectedItem(items);
		}
	}

	public void actualizarMaquinaria() {
		// TODO Auto-generated method stub
		serialCarroceria.setText(getModelo().getSerialcarroceria());
		 
		getModelo().setDetalleGarantia(null);
	 
	}
	
	public List<DetalleGarantina> getDetallesGarantia() {
		return detallesGarantia;
	}

	public void setDetallesGarantia(List<DetalleGarantina> detallesGarantia) {
		this.detallesGarantia = detallesGarantia;
	}

	public Combobox getCombGarantia() {
		return combGarantia;
	}

	public void setCombGarantia(Combobox combGarantia) {
		this.combGarantia = combGarantia;
	}

	public void MostrarActividades(List<Actividad> actividades) {
		// TODO Auto-generated method stub
		if (this.actividades != null) {
			this.actividades.detach();
		}
		this.actividades = new Listbox();
		this.actividades.setMold("paging");
		this.actividades.setPageSize(5);
		Listhead titulos = new Listhead();
		Listheader h1 = new Listheader(" Descripción ");
		titulos.appendChild(h1);
		for (Actividad a : actividades) {
			Listitem items = new Listitem();
			items.setValue(a);
			items.setLabel(a.getDescripcion());
			this.actividades.appendChild(items);
		}
		cantidad.setValue(" Cantidad de Actividades :"	+ new Integer(actividades.size()).toString());
		this.lisDiv.appendChild(this.actividades);

	}

	public Actividad getActividadSeleccionada() {
		return (Actividad) (this.actividades.getSelectedItem() == null ? null: this.actividades.getSelectedItem().getValue());
	}

	public Listbox getActividades() {
		return actividades;
	}

	public void setActividades(Listbox actividades) {
		this.actividades = actividades;
	}

	public Textbox getEncargado() {
		return encargado;
	}

	public void setEncargado(Textbox encargado) {
		this.encargado = encargado;
	}

	public DetalleGarantina seleccionarDetalleGarantia() {
		return (DetalleGarantina) (combGarantia.getSelectedItem() != null ? combGarantia
				.getSelectedItem().getValue() : null);
	}

	public void MostrarProcesar() {
		// TODO Auto-generated method stub
		procesar.dibujar(divOrden);
	}

	public Datebox getFecha() {
		return fecha;
	}

	public void setFecha(Datebox fecha) {
		this.fecha = fecha;
	}

	public Intbox getKm() {
		return km;
	}

	public void setKm(Intbox km) {
		this.km = km;
	}

	public void modoConsuta(int accion) {
		// TODO Auto-generated method stub

		if (accion == Accion.CONSULTAR || accion == Accion.ELIMINAR
				|| accion == Accion.IMPRIMIR_ITEM || accion == Accion.ANULAR) {
			if (getModelo().getEstatus() == 3) {
				MostrarProcesar();
			}
			km.setDisabled(true);
			fecha.setDisabled(true);
			encargado.setDisabled(true);
			combGarantia.setDisabled(true);
		} else if (Accion.PROCESAR == accion) {
			encargado.setDisabled(true);
			combGarantia.setDisabled(true);
		}

	}

	public Textbox getNota() {
		return nota;
	}

	public void setNota(Textbox nota) {
		this.nota = nota;
	}


	public Button getDetalle() {
		return detalle;
	}


	public void setDetalle(Button detalle) {
		this.detalle = detalle;
	}

}
