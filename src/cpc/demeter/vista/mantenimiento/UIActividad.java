package cpc.demeter.vista.mantenimiento;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.helper.DescendableLinkedList;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
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
import cpc.modelo.demeter.mantenimiento.DetalleGarantina;
import cpc.modelo.demeter.mantenimiento.Periodicidad;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIActividad extends CompVentanaBase<Actividad> {

	private CompGrupoDatos div;
	private Textbox descripcion;

	private Listbox periodicidad;
	private Listbox detalleGarantia;

	private Div divLista;
	private Div divListaGarantia;

	private Button addPeriodicidad;

	private Button addGarantia; // para los distintos niveles de la Garantia
	private Button rmGarantia; // para los distintos niveles de la garantia

	private List<Periodicidad> listPeriodisidad = new ArrayList<Periodicidad>();
	private List<DetalleGarantina> lisDetalle = new ArrayList<DetalleGarantina>();

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public UIActividad() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param titulo
	 * @param ancho
	 */
	public UIActividad(String titulo, int ancho) {
		super(titulo, ancho);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		div = new CompGrupoDatos();

		descripcion = new Textbox();
		descripcion.setMaxlength(250);
		descripcion.setRows(3);
		descripcion.setWidth("90%");
		
		divListaGarantia = new Div();
		divLista = new Div();

		addPeriodicidad = new Button("+");
		addPeriodicidad.setTooltiptext("Agregar periosidad");
		addPeriodicidad.addEventListener(Events.ON_CLICK, getControlador());

		addGarantia = new Button("+");
		addGarantia.setTooltiptext("Agregar Detalle Garantia");
		addGarantia.addEventListener(Events.ON_CLICK, getControlador());

		rmGarantia = new Button("-");
		rmGarantia.setTooltiptext("Quitar Detalle Garantia");
		rmGarantia.addEventListener(Events.ON_CLICK, getControlador());

	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub
		this.setMaximizable(true);
		this.setClosable(true);
 
		descripcion.setMaxlength(250);
		div.setTitulo("Datos Generales");
		div.addComponente("DESCRIPCION ", descripcion);

		div.dibujar(this);

		Vbox vbox = new Vbox();
		vbox.appendChild(addPeriodicidad);

		
		Hbox hbox = new Hbox();
		hbox.appendChild(vbox);
		hbox.appendChild(divLista);

		Groupbox grupo = new Groupbox();
		grupo.appendChild(new Caption("PERIODICIDAD"));
		grupo.appendChild(hbox);

		Vbox vbox1 = new Vbox();
		vbox1.appendChild(addGarantia);
		vbox1.appendChild(rmGarantia);

		Hbox hbox1 = new Hbox();
		hbox1.appendChild(vbox1);
		hbox1.appendChild(divListaGarantia);

		Groupbox grupo2 = new Groupbox();
		grupo2.appendChild(new Caption("Detalle Garantia "));
		grupo2.appendChild(hbox1);

		this.appendChild(grupo2);
		this.appendChild(grupo);
		addBotonera();
	}

	private Listbox ListaPeriodicidad() {
		// TODO Auto-generated method stub
		Listbox lista = new Listbox();
		Listhead head = new Listhead();
		Listheader h1 = new Listheader("Periodicidad");
		Listheader h2 = new Listheader("Frecuencua");

		head.appendChild(h1);
		head.appendChild(h2);
		lista.appendChild(head);

		for (Periodicidad p : listPeriodisidad) {
			Listitem items = new Listitem();
			items.setValue(p);
			Listcell celda = new Listcell(
					Periodicidad.getLisPeriosidad()[p.getTipo()]);
			Listcell celda2 = new Listcell(p.getMedida().toString());
			items.appendChild(celda);
			items.appendChild(celda2);
			lista.appendChild(items);
		}
		return lista;
	}

	private Listbox ListaGarantia() {
		// TODO Auto-generated method stub

		Listbox lista = new Listbox();
		Listhead head = new Listhead();

		Listheader h1 = new Listheader("id ");
		Listheader h2 = new Listheader("Detalle");

		head.appendChild(h1);
		head.appendChild(h2);
		lista.appendChild(head);

		for (DetalleGarantina dg : lisDetalle) {
			Listitem items = new Listitem();
			items.setValue(dg);
			Listcell celda = new Listcell(dg.getId().toString());
			Listcell celda2 = new Listcell(dg.getDescripcion());
			items.appendChild(celda);
			items.appendChild(celda2);
			lista.appendChild(items);
		}
		return lista;
	}

	@Override
	public void cargarValores(Actividad dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		descripcion.setValue(getModelo().getDescripcion());

		for (DetalleGarantina g : getModelo().getDetallesGarantia())
		{
			lisDetalle.add(g);
		}
		
		for (Periodicidad p : getModelo().getPeriodicidad())
		{
			listPeriodisidad.add(p);
		}

		detalleGarantia = ListaGarantia();
		divListaGarantia.appendChild(detalleGarantia);

		periodicidad = ListaPeriodicidad();
		divLista.appendChild(periodicidad);

		getBinder().addBinding(descripcion, "value",getNombreModelo() + ".descripcion", null, null, "save", null, null, null, null);

	}

	public Button getAddPeriodicidad() {
		return addPeriodicidad;
	}

 
	public void setAddPeriodicidad(Button addPeriodicidad) {
		this.addPeriodicidad = addPeriodicidad;
	}
 
	public void addPeriodicidad(Periodicidad p) {
		Iterator<Periodicidad> iter = listPeriodisidad.iterator();
		while (iter.hasNext()) {
			if (iter.next().getTipo().equals(p.getTipo())) {
				return;
			}
		}
		listPeriodisidad.add(p);
		Listitem item = new Listitem();
		item.setValue(p);
		Listcell celda = new Listcell(
				Periodicidad.getLisPeriosidad()[p.getTipo()]);
		Listcell celda2 = new Listcell(p.getMedida().toString());
		item.appendChild(celda);
		item.appendChild(celda2);
		periodicidad.appendChild(item);
	}

	public void addDetalleGarantia(DetalleGarantina detalleGarantia) {
		Iterator<DetalleGarantina> iter = lisDetalle.iterator();
		while (iter.hasNext()) {
			if (detalleGarantia.getId().equals(iter.next().getId())) {
				return;
			}
		}
		lisDetalle.add(detalleGarantia);
		Listitem item = new Listitem();
		item.setValue(detalleGarantia);
		Listcell celda = new Listcell(detalleGarantia.getId().toString());
		Listcell celda2 = new Listcell(detalleGarantia.getDescripcion());
		item.appendChild(celda);
		item.appendChild(celda2);
		this.detalleGarantia.appendChild(item);
	}

	public DetalleGarantina detalleSelect() {
		return (DetalleGarantina) (detalleGarantia.getSelectedItem() == null ? null
				: detalleGarantia.getSelectedItem().getValue());
	}

	public Listbox getPeriodicidad() {
		return periodicidad;
	}

	public void setPeriodicidad(Listbox periodicidad) {
		this.periodicidad = periodicidad;
	}

	public void modoOperacion(Integer modoOperacion) {
		// TODO Auto-generated method stub

		if (modoOperacion == Accion.CONSULTAR) {
			ActivarConsulta();
		}

	}

	private void ActivarConsulta() {
		// TODO Auto-generated method stub
		getAceptar().setDisabled(true);
		descripcion.setDisabled(true);
		addPeriodicidad.setVisible(false);
		// rmPeriodicidad.setVisible(false);
		addGarantia.setVisible(false);
		rmGarantia.setVisible(false);

	}

	private void ActivarModoEdicion() {
		// TODO Auto-generated method stub

	}

	public Textbox getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(Textbox descripcion) {
		this.descripcion = descripcion;
	}

	public Button getAddGarantia() {
		return addGarantia;
	}

	public Button getRmGarantia() {
		return rmGarantia;
	}

	public void setAddGarantia(Button addGarantia) {
		this.addGarantia = addGarantia;
	}

	public void setRmGarantia(Button rmGarantia) {
		this.rmGarantia = rmGarantia;
	}

	public Listbox getDetalleGarantia() {
		return detalleGarantia;
	}

	public void setDetalleGarantia(Listbox detalleGarantia) {
		this.detalleGarantia = detalleGarantia;
	}

	public List<Periodicidad> getListPeriodisidad() {
		return listPeriodisidad;
	}

	public List<DetalleGarantina> getLisDetalle() {
		return lisDetalle;
	}

	public void setListPeriodisidad(List<Periodicidad> listPeriodisidad) {
		this.listPeriodisidad = listPeriodisidad;
	}

	public void setLisDetalle(List<DetalleGarantina> lisDetalle) {
		this.lisDetalle = lisDetalle;
	}

}
