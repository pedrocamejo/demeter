package cpc.demeter.vista.mantenimiento.garantia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
 
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events; 
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
 
import cpc.demeter.catalogo.mantenimiento.CatMaquinariaSelect;
import cpc.modelo.demeter.mantenimiento.MaquinariaExterna; 
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UISelectMaquinaria extends CompVentanaBase<MaquinariaExterna> {

	private List<MaquinariaExterna> maquinarias;

	private Listbox 				lista;
	private Combobox 				campo;
	private Textbox 				dato;
	private Button 					seleccionar;
	private Button 					busquedad;
	private IZkAplicacion 			 app;
	private int						 modoOperacion;
	
	public UISelectMaquinaria(String titulo, int ancho,	List<MaquinariaExterna> lista,IZkAplicacion app,CatMaquinariaSelect controldor) throws ExcDatosInvalidos 
	{
		// TODO Auto-generated constructor stub
		super(titulo,ancho);
		this.setControlador(controldor);
		this.setApp(app);
		this.maquinarias = lista;
		this.dibujarVentana();
		this.cargar();
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		lista = new Listbox();
		lista.setMold("paging");
		lista.setPageSize(10);

		Listhead encabezado = new Listhead();
		Listheader clum2 = new Listheader("Serial Carroceria");
		clum2.setWidth("10%");
		Listheader clum3 = new Listheader("Serial Motor");
		clum3.setWidth("10%");
		Listheader clum4 = new Listheader("Propietario");
		clum4.setWidth("20%");
		Listheader clum5 = new Listheader("Modelo");
		clum5.setWidth("45%");

		encabezado.appendChild(clum2);
		encabezado.appendChild(clum3);
		encabezado.appendChild(clum4);
		encabezado.appendChild(clum5);

		lista.appendChild(encabezado);
		
		lista.setItemRenderer(new ListitemRenderer() {
			@Override
			public void render(Listitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				arg0.setValue(arg1);
				MaquinariaExterna maquinaria = (MaquinariaExterna) arg1;
				new Listcell(maquinaria.getSerialcarroceria()).setParent(arg0);
				new Listcell(maquinaria.getSerialMotor()).setParent(arg0);;
				new Listcell((maquinaria.getPropietario() == null ? "No Definido ":maquinaria.getPropietario().getIdentidadLegal())).setParent(arg0);;
				new Listcell(maquinaria.getDescripcionModelo()).setParent(arg0);;
			}
		});
		
		lista.setModel(new ListModelList(maquinarias));
		
		campo = new Combobox();
		Object[] objectos = { "Serial Carroceria", "Serial Motor","Propietario", "Modelo" };
		campo.setModel(new ListModelArray(objectos));

		dato = new Textbox();
		dato.setMaxlength(200);
		dato.setWidth("90%");
		dato.addEventListener(Events.ON_OK, buscarEvento());

		busquedad = new Button("buscar");
		busquedad.addEventListener(Events.ON_CLICK, buscarEvento());

		seleccionar = new Button("Seleccionar");
		seleccionar.addEventListener(Events.ON_CLICK, getControlador());

		cancelar = new Button("cancelar");
		cancelar.addEventListener(Events.ON_CLICK, getControlador());
		

	}

	public Button getCancelar() {
		return cancelar;
	}

	public void setCancelar(Button scancelar) {
		cancelar = scancelar;
	}

	public EventListener buscarEvento() {
		return new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				buscar();
			}
		};
	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub
		Hbox compBuscar =new Hbox();
		compBuscar.setAlign("right");
		compBuscar.appendChild(campo);
		compBuscar.appendChild(dato);
		compBuscar.appendChild(busquedad);
		CompGrupoDatos listado = new CompGrupoDatos("Seleccionar Maquinaria Garantia ",1);
		
		
		Hbox botones = new Hbox();
		botones.setAlign("right");
		botones.appendChild(seleccionar);
		botones.appendChild(cancelar);
	
		listado.addComponente(compBuscar);
		listado.addComponente(lista);
		listado.addComponente(botones);
	
		listado.dibujar(this);
		
	}

	@Override
	public void cargarValores(MaquinariaExterna dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub

	}

	public List<MaquinariaExterna> getMaquinarias() {
		return maquinarias;
	}

	public Listbox getLista() {
		return lista;
	}

	public void setMaquinarias(List<MaquinariaExterna> maquinarias) {
		this.maquinarias = maquinarias;
	}

	public void setLista(Listbox lista) {
		this.lista = lista;
	}

	public IZkAplicacion getApp() {
		return app;
	}

	public int getModoOperacion() {
		return modoOperacion;
	}

	public void setApp(IZkAplicacion app) {
		this.app = app;
	}

	public void setModoOperacion(int modoOperacion) {
		this.modoOperacion = modoOperacion;
	}

	public Button getSeleccionar() {
		return seleccionar;
	}

	public void setSeleccionar(Button seleccionar) {
		this.seleccionar = seleccionar;
	}
 

	public void buscar() 
	{
		List<MaquinariaExterna> lisbusqdad = new ArrayList<MaquinariaExterna>();
		String texto = dato.getValue();

		if (texto.trim().equals("")) 
		{
			lista.setModel(new ListModelList(maquinarias));
		}
		else 
		{
			if (campo.getSelectedItem() != null) 
			{
				String categoria = (String) campo.getSelectedItem().getValue();

				Iterator<MaquinariaExterna> iter = maquinarias.iterator();

				while (iter.hasNext())
				{
					MaquinariaExterna maquinaria = iter.next();
					
					if (categoria.equals("Serial Carroceria"))
					{
						if (maquinaria.getSerialcarroceria().trim().toUpperCase().contains(texto.trim().toUpperCase()))
						{
							lisbusqdad.add(maquinaria);
						}
					} 
					else if (categoria.equals("Serial Motor"))
					{
						if (maquinaria.getSerialMotor().trim().toUpperCase().contains(texto.trim().toUpperCase()))
						{
							lisbusqdad.add(maquinaria);
						}

					}
					else if (categoria.equals("Propietario"))
					{
						if (maquinaria.getPropietario().getIdentidadLegal().trim().toUpperCase().contains(texto.trim().toUpperCase()))
						{
							lisbusqdad.add(maquinaria);
						}

					} 
					else if (categoria.equals("Modelo"))
					{
						if (maquinaria.getTipo().getModelo().toString().trim().toUpperCase().contains(texto.trim().toUpperCase()))
						{
							lisbusqdad.add(maquinaria);
						}

					}
				}
				lista.setModel(new ListModelList(lisbusqdad));
			}
		}
	}

	public Textbox getDato() {
		return dato;
	}
 

	public MaquinariaExterna datoSeleccionado()
	{
		// TODO Auto-generated method stub
		return (lista.getSelectedItem() == null ? null:(MaquinariaExterna) lista.getSelectedItem().getValue());
	}

	public void agregarMaquinaria(MaquinariaExterna dato)
	{
		// TODO Auto-generated method stub
		if(dato != null)
		{
			maquinarias.add(dato);
			lista.setModel(new ListModelArray(maquinarias));
			
		}
	}
}
