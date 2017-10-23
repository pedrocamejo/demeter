package cpc.demeter.vista.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zhtml.Caption;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.mantenimiento.Actividad;
import cpc.modelo.demeter.mantenimiento.PlanMantenimiento;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIPlanMantenimiento extends CompVentanaBase<PlanMantenimiento> {

	private static final long serialVersionUID = 1L;

	private CompGrupoDatos 	div;
	private Div 			divLista; // para actividades

	private Textbox		 	descripcion;
	private Listbox 		actividades;

	private Button 			agregarActividad;
	private Button 			quitarActividad;
	private Button 			editarActividad;
	private Button 			detalleActividad;

	private List<Actividad> actividad = new ArrayList<Actividad>();

	public UIPlanMantenimiento() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UIPlanMantenimiento(String titulo, int ancho) {
		super(titulo, ancho);
		// TODO Auto-generated constructor stub
	}

	public UIPlanMantenimiento(String titulo) {
		super(titulo);
	}

	public void moverTodoVista() {
		for (Actividad a : getModelo().getActividades()) {
			actividad.add(a);
		}

	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		div = new CompGrupoDatos(2);
		
		descripcion = new Textbox();
		descripcion.setRows(3);
		descripcion.setMaxlength(250);
		descripcion.setWidth("90%");
		
		divLista = new Div();

		agregarActividad = new Button("Agregar ");
		agregarActividad.setWidth("70px");
		agregarActividad.setTooltiptext(" agregar una actividad a esta maquinaria ");
		agregarActividad.addEventListener(Events.ON_CLICK, getControlador());

		quitarActividad = new Button("Quitar ");
		quitarActividad.setWidth("70px");
		quitarActividad.setTooltiptext("Quitar una Actividad  a una maquinaria ");
		quitarActividad.addEventListener(Events.ON_CLICK, getControlador());

		detalleActividad = new Button("Detalle");
		detalleActividad.setWidth("70px");
		detalleActividad.setTooltiptext("Detalle ");
		detalleActividad.addEventListener(Events.ON_CLICK, getControlador());

		editarActividad = new Button("Editar");
		editarActividad.setWidth("70px");
		editarActividad.setTooltiptext("Editar Actividad");
		editarActividad.addEventListener(Events.ON_CLICK, getControlador());

	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub
	

		div.addComponente("Descripción ", descripcion);
		div.dibujar(this);

		Vbox vbox = new Vbox();
		vbox.appendChild(agregarActividad);
		vbox.appendChild(editarActividad);
		vbox.appendChild(quitarActividad);
		vbox.appendChild(detalleActividad);

		Hbox div1 = new Hbox();
		div1.appendChild(divLista);
		div1.appendChild(vbox);

		Groupbox grupo = new Groupbox();
		Caption titulo = new Caption("Actividad");
		grupo.appendChild(titulo);
		grupo.appendChild(div1);

		this.appendChild(grupo);
		addBotonera();

	}

	@Override
	public void cargarValores(PlanMantenimiento dato) throws ExcDatosInvalidos {

		descripcion.setText(getModelo().getDescripcion());
		moverTodoVista();
		getBinder().addBinding(descripcion, "value",getNombreModelo() + ".descripcion", null, null, "save", null,	null, null, null);
		dibujarLisbox();
	}

	public void deshabilitar(int i) {
		// TODO Auto-generated method stub
		if (i == Accion.CONSULTAR) {
			descripcion.setDisabled(true);
			aceptar.setDisabled(true);
			agregarActividad.setDisabled(true);
			quitarActividad.setDisabled(true);
			;
			editarActividad.setDisabled(true);
			;
		}
	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.descripcion = descripcion;
	}

	private void dibujarLisbox() {
		actividades = new Listbox();
		Listhead head = new Listhead();
		Listheader h1 = new Listheader("Actividad");
		h1.setWidth("60%");
		Listheader h2 = new Listheader("Periosidad");
		h2.setWidth("40%");
		head.appendChild(h1);
		head.appendChild(h2);

		actividades.appendChild(head);

		for (Actividad actividad : this.actividad) {
			Listitem items = new Listitem();
			items.setValue(actividad);
			Listcell cel1 = new Listcell(actividad.getDescripcion());
			Listcell cel2 = new Listcell(actividad.getPeriodicidadM());
			items.appendChild(cel1);
			items.appendChild(cel2);
			actividades.appendChild(items);
		}
		this.divLista.appendChild(actividades);
	}

	public void AgregarActividad(Actividad actividad) {
		Listitem items = new Listitem();
		items.setValue(actividad);
		Listcell cel1 = new Listcell(actividad.getDescripcion());
		Listcell cel2 = new Listcell(actividad.getPeriodicidadM());
		items.appendChild(cel1);
		items.appendChild(cel2);

		actividades.appendChild(items);
		actividad.setPlanMantenimiento(getModelo());
		this.actividad.add(actividad);

	}

	public Button getAgregarActividad() {
		return agregarActividad;
	}

	public Button getQuitarActividad() {
		return quitarActividad;
	}

	public Button getEditarActividad() {
		return editarActividad;
	}

	public Button getDetalleActividad() {
		return detalleActividad;
	}

	public List<Actividad> getActividad() {
		return actividad;
	}

	public void setActividad(List<Actividad> actividad) {
		this.actividad = actividad;
	}

	public Actividad getSelectActividad() {
		return (Actividad) (actividades.getSelectedItem() == null ? null
				: actividades.getSelectedItem().getValue());
	}

	public void actualizarActividades() {

		actividades.detach();
		dibujarLisbox();
	}

}
