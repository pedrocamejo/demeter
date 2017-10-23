package cpc.demeter.vista.mantenimiento;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treecol;
import org.zkoss.zul.Treecols;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Vbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.mantenimiento.MantenimientoMaquinaria;
import cpc.modelo.demeter.mantenimiento.PlanMantenimiento;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiMantenimientoMaquinaria extends CompVentanaBase<MantenimientoMaquinaria> {

	private CompGrupoDatos div;

	private Combobox comb_modelos;

	private Tree tree_planMaquinaria;
	private Treeitem raiz;

	private Div divTree;
	private Vbox botoneraPieza;

	private Button agregarPieza;
	private Button editarPieza;
	private Button quitarPieza;
	private Button detallePieza;
	private List<Modelo> listModelo = new ArrayList<Modelo>();
	private int modoOperacion;

	private PlanMantenimiento PlanMantenimiento = new PlanMantenimiento();  
	
	public UiMantenimientoMaquinaria() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UiMantenimientoMaquinaria(String titulo, int ancho) {
		super(titulo, ancho);
		// TODO Auto-generated constructor stub
	}

	public UiMantenimientoMaquinaria(String titulo) {
		super(titulo);
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		div = new CompGrupoDatos(2);
		comb_modelos = comboModelo();
		botoneraPieza = new Vbox();
		botoneraPieza.setVisible(false);

		comb_modelos.addEventListener(Events.ON_SELECT, getControlador());
		tree_planMaquinaria = new Tree();
		tree_planMaquinaria.setWidth("90%");
		divTree = new Div();

		agregarPieza = new Button("AGREGAR");
		agregarPieza.setTooltiptext("desglosar una pieza de la maquinaria");
		agregarPieza.setWidth("100px");
		agregarPieza.addEventListener(Events.ON_CLICK, getControlador());

		quitarPieza = new Button("QUITAR");
		quitarPieza.setTooltiptext("quitar un desglose");
		quitarPieza.setWidth("100px");
		quitarPieza.addEventListener(Events.ON_CLICK, getControlador());

		detallePieza = new Button("DETALLE");
		detallePieza.setTooltiptext("detalle de Pieza");
		detallePieza.setWidth("100px");
		detallePieza.addEventListener(Events.ON_CLICK, getControlador());

		editarPieza = new Button("EDITAR");
		editarPieza.setTooltiptext("Editar Piezas");
		editarPieza.setWidth("100px");
		editarPieza.addEventListener(Events.ON_CLICK, getControlador());

	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub

		div.addComponente("Modelo :", comb_modelos);
		div.dibujar(this);

		botoneraPieza.appendChild(new Label("Pieza de Maquinaria "));
		botoneraPieza.appendChild(agregarPieza);
		botoneraPieza.appendChild(editarPieza);
		botoneraPieza.appendChild(quitarPieza);
		botoneraPieza.appendChild(detallePieza);

		botoneraPieza.appendChild(new Label(" Actividad de Piezas "));

		Hbox hbox = new Hbox();
		divTree.setWidth("400px");
		hbox.appendChild(divTree);
		hbox.appendChild(botoneraPieza);

		this.appendChild(hbox);
		addBotonera();
	}

	@Override
	public void cargarValores(MantenimientoMaquinaria dato)
			throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		if (getModelo().getTipogarantia() != null) 
		{
			comb_modelos.setText(getModelo().getTipogarantia().getModelo()
					.toString());
		}
		if (modoOperacion == Accion.EDITAR || modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ELIMINAR)
		{
			mostrarDesglose();
		}

	}

	public void desactivar(int modoOperacion) {
		// TODO Auto-generated method stub
		if (modoOperacion == Accion.EDITAR || modoOperacion == Accion.CONSULTAR	|| modoOperacion == Accion.ELIMINAR) 
		{
			comb_modelos.setDisabled(true);
			if (modoOperacion == Accion.CONSULTAR|| modoOperacion == Accion.ELIMINAR)
			{
				agregarPieza.setDisabled(true);
				editarPieza.setDisabled(true);
				quitarPieza.setDisabled(true);
			}
		}
	}

	public Combobox comboModelo() 
	{
		Combobox combobox = new Combobox();
		Iterator<Modelo> iter = listModelo.iterator();
		while (iter.hasNext())
		{
			Modelo modelo = iter.next();
			Comboitem items = new Comboitem(modelo.toString());
			items.setValue(modelo);
			combobox.appendChild(items);
		}
		return combobox;
	}

	public List<Modelo> getListModelo() {
		return listModelo;
	}

	public void setListModelo(List<Modelo> listModelo) {
		this.listModelo = listModelo;
	}

	public Modelo getdatoselecionado() {
		// TODO Auto-generated method stub
		return (Modelo) (comb_modelos.getSelectedItem() != null ? comb_modelos
				.getSelectedItem().getValue() : null);

	}

	public Combobox getComb_modelos() {
		return comb_modelos;
	}

	public void setComb_modelos(Combobox comb_modelos) {
		this.comb_modelos = comb_modelos;
	}

	public void mostrarDesglose() 
	{
		// TODO Auto-generated method stub
		tree_planMaquinaria.detach();
		tree_planMaquinaria = new Tree();
		Treecols columnas = new Treecols();
		Treecol column1 = new Treecol("Pieza");
		Treecol column2 = new Treecol("Actividades");
		columnas.appendChild(column1);
		columnas.appendChild(column2);

		Treechildren treeChildren = new Treechildren();
		creararbol(getModelo().getPlanMaquinaria(), treeChildren);

		tree_planMaquinaria.appendChild(columnas);
		tree_planMaquinaria.appendChild(treeChildren);

		divTree.appendChild(tree_planMaquinaria);
		// ]Activo la botonera ;:-d
		botoneraPieza.setVisible(true);
		actualizarModelo();
	}

	public void creararbol(PlanMantenimiento plan, Treechildren padre)
	{
		Treeitem item = new Treeitem();
		item.setValue(plan);
		item.setLabel(plan.getDescripcion());
		padre.appendChild(item);
		
		if (plan.getPiezas().size() > 0)
		{
			Treechildren planPadre = new Treechildren();
			item.appendChild(planPadre);
			for (PlanMantenimiento p : plan.getPiezas())
			{
				creararbol(p, planPadre);
			}
		}
	}

	public PlanMantenimiento getPlanMantenimientoSeleccionado()
	{
		return (PlanMantenimiento) (tree_planMaquinaria.getSelectedItem() != null ? tree_planMaquinaria.getSelectedItem().getValue() : null);

	}

	public Tree getTree_planMaquinaria() {
		return tree_planMaquinaria;
	}

	public Button getAgregarPieza() {
		return agregarPieza;
	}

	public Button getQuitarPieza() {
		return quitarPieza;
	}

	public Button getDetallePieza() {
		return detallePieza;
	}

	public void setTree_planMaquinaria(Tree tree_planMaquinaria) 
	{
		this.tree_planMaquinaria = tree_planMaquinaria;
	}

	public void setAgregarPieza(Button agregarPieza) 
	{
		this.agregarPieza = agregarPieza;
	}

	public void setQuitarPieza(Button quitarPieza)
	{
		this.quitarPieza = quitarPieza;
	}

	public void setDetallePieza(Button detallePieza)
	{
		this.detallePieza = detallePieza;
	}

	public Button getEditarPieza() 
	{
		return editarPieza;
	}

	public void setEditarPieza(Button editarPieza)
	{
		this.editarPieza = editarPieza;
	}

	public void addHijo(PlanMantenimiento padre, PlanMantenimiento hijo) 
	{
		// TODO Auto-generated method stub
		Treeitem tree = tree_planMaquinaria.getSelectedItem();
		if (tree.getTreechildren() == null) 
		{
			Treechildren treechildren = new Treechildren();

			Treeitem item = new Treeitem();
			item.setValue(hijo);
			item.setLabel(hijo.getDescripcion());

			treechildren.appendChild(item);
			tree.appendChild(treechildren);
		} 
		else 
		{
			Treechildren treeChildren = tree.getTreechildren();
			Treeitem item = new Treeitem();
			item.setValue(hijo);
			item.setLabel(hijo.getDescripcion());

			treeChildren.appendChild(item);
			tree.appendChild(treeChildren);
		}
	}

	public void setModoOperacion(int i) {
		// TODO Auto-generated method stub
		modoOperacion = i;
	}

}
