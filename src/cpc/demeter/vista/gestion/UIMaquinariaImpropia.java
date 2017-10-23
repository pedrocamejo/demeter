package cpc.demeter.vista.gestion;
 

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.modelo.demeter.gestion.MaquinariaImpropia;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.modelo.sigesp.basico.Categoria;
import cpc.modelo.sigesp.basico.Marca;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.modelo.sigesp.basico.Tipo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIMaquinariaImpropia extends CompVentanaBase<MaquinariaImpropia> {

	private static final long serialVersionUID = -3572524741697517560L;
	
	private Textbox			serialChasis;
	private Textbox			serialOtro;
	private Textbox			procedencia;
	private Textbox			nombre;
	
	private List<Marca>     marcas = new ArrayList<Marca>();
   	private Combobox		marca;
   	
   	private List<Modelo>    modelos = new ArrayList<Modelo>();
   	private Combobox    	modeloMaquinaria;
   	
   	private List<Categoria>	categorias = new ArrayList<Categoria>();
   	private Combobox 		categoria;
   	
   	private List<Tipo>     tipos= new ArrayList<Tipo>(); 
   	private Combobox 		tipo;

   	private List<UnidadFuncional> 	unidades = new ArrayList<UnidadFuncional>();
   	private Combobox 			maquinariaUnidad;
   	
   	private Textbox				observacion;
   	
   	private CompGrupoDatos    contenedor;
   	
   	
	public UIMaquinariaImpropia(String titulo, int ancho, AppDemeter app, List<Marca> marcas , List<Categoria> categorias, List<UnidadFuncional> 	unidades ) 
	{
		// TODO Auto-generated constructor stub
		super(titulo,ancho);
		setWidth("70%");
		setMaximizable(true);
		this.unidades = unidades;
		this.marcas = marcas;
		this.categorias = categorias;
		
	}

	@Override
	public void cargarValores(MaquinariaImpropia arg0) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		
		serialChasis.setValue(getModelo().getSerialChasis());
		serialOtro.setValue(getModelo().getSerialOtro());
		procedencia.setValue(getModelo().getProcedencia());
		nombre.setValue(getModelo().getNombre());
		observacion.setValue(getModelo().getObservacion());
		
		if(getModelo().getMarca() != null )
		{	
			Comboitem item = new Comboitem(getModelo().getMarca().getDescripcion());
			item.setDescription(getModelo().getMarca().getCodigoMarca());
			item.setValue(getModelo().getMarca());
			marca.appendChild(item);
			marca.setSelectedItem(item);
		}

		if(getModelo().getModelo() != null )
		{
			Comboitem item = new Comboitem(getModelo().getModelo().getDescripcionModelo());
			item.setDescription(getModelo().getModelo().getCodigoModelo());
			item.setValue(getModelo().getModelo());
			modeloMaquinaria.appendChild(item);
			modeloMaquinaria.setSelectedItem(item);
		}
		
		if(getModelo().getCategoria() != null)
		{
			Comboitem item = new Comboitem(getModelo().getCategoria().getDescripcionCategoria());
			item.setDescription(getModelo().getCategoria().getCodigoCategoria());
			item.setValue(getModelo().getCategoria());
			categoria.appendChild(item);
			categoria.setSelectedItem(item);
			
		}
		
		if(getModelo().getTipo() != null)
		{
			Comboitem item = new Comboitem(getModelo().getTipo().getDescripcionTipo());
			item.setDescription(getModelo().getTipo().getCodigoTipo());
			item.setValue(getModelo().getTipo());
			tipo.appendChild(item);
			tipo.setSelectedItem(item);
		}
		

		if(getModelo().getMaquinariaUnidad().getUnidad() != null)
		{
			Comboitem item = new Comboitem();
			item.setValue(getModelo().getMaquinariaUnidad().getUnidad());
			item.setLabel(getModelo().getMaquinariaUnidad().getUnidad().getStrUbicacion());
			item.setDescription(getModelo().getMaquinariaUnidad().getUnidad().getDescripcion() );

			maquinariaUnidad.appendChild(item);
			maquinariaUnidad.setSelectedItem(item);

		}
		
		getBinder().addBinding(serialChasis,"value",getNombreModelo()+".serialChasis",null, null, "save",null,null,null,null);
		getBinder().addBinding(serialOtro,"value",getNombreModelo()+".serialOtro",null, null, "save",null,null,null,null);
		getBinder().addBinding(procedencia,"value",getNombreModelo()+".procedencia",null, null, "save",null,null,null,null);
		getBinder().addBinding(nombre,"value",getNombreModelo()+".nombre",null, null, "save",null,null,null,null);

		getBinder().addBinding(marca,"selectedItem",getNombreModelo()+".marca",null, null, "save",null,null,null,null);
		getBinder().addBinding(modeloMaquinaria,"selectedItem",getNombreModelo()+".modelo",null, null, "save",null,null,null,null);
		getBinder().addBinding(categoria,"selectedItem",getNombreModelo()+".categoria",null, null, "save",null,null,null,null);
		getBinder().addBinding(tipo,"selectedItem",getNombreModelo()+".tipo",null, null, "save",null,null,null,null);
				
		
	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub
		
		contenedor.addComponente("Serial Chasis ",serialChasis);
		contenedor.addComponente("Serial Otro  ",serialOtro);

		contenedor.addComponente(new Label("Marca "));
		contenedor.addComponente(marca);
		
		contenedor.addComponente(new Label("Modelo "));
		contenedor.addComponente(modeloMaquinaria);

		contenedor.addComponente(new Label("Categoria "));
		contenedor.addComponente(categoria);
		
		contenedor.addComponente(new Label(" tipo "));
		contenedor.addComponente(tipo);

		contenedor.addComponente(" Nombre Maquinaria  ", nombre);
		contenedor.addComponente(" Procedencia  Maquinaria ",procedencia);
		contenedor.addComponente("Unidad : ",maquinariaUnidad);
		contenedor.addComponente("Observaciones  : ",observacion);

		
		contenedor.dibujar(this);
		addBotonera();
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub

		contenedor = new CompGrupoDatos("Datos Maquinaria ",4);
		
		serialChasis = new Textbox();
		serialChasis.setMaxlength(35);
		serialChasis.addEventListener(Events.ON_BLUR,new EventListener() {
			
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				serialChasis.setValue(serialChasis.getText().toUpperCase().trim());
			}
		});
		
		serialOtro = new Textbox();
		serialOtro.setMaxlength(35);
		serialOtro.addEventListener(Events.ON_BLUR,new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				serialOtro.setValue(serialOtro.getText().toUpperCase().trim());
			}
		});
		
		
		procedencia = new Textbox();
		procedencia.setRows(3);
		procedencia.setMaxlength(250);
		procedencia.setWidth("99%");

		
		nombre = new Textbox();
		nombre.setMaxlength(50);
		
		marca = new Combobox();
		marca.setTooltiptext(" Marcas "); 
		marca.setAutocomplete(true);
		marca.addEventListener(Events.ON_SELECT,getControlador());
		marca.setItemRenderer(new ComboitemRenderer() {
			@Override
			public void render(Comboitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				Marca marca = (Marca) arg1;
				arg0.setValue(marca);
				arg0.setLabel(marca.getDescripcion());
				arg0.setDescription(marca.getCodigoMarca());
			}
		});
		marca.setModel(new ListModelArray(marcas));
		
		modeloMaquinaria = new Combobox();
		modeloMaquinaria.setTooltiptext(" Modelos  "); 
		modeloMaquinaria.setAutocomplete(true);
		
		modeloMaquinaria.setItemRenderer(new ComboitemRenderer() {
			@Override
			public void render(Comboitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				Modelo modelo = (Modelo) arg1;
				arg0.setValue(modelo);
				arg0.setLabel(modelo.getDescripcionModelo());
			    arg0.setDescription(modelo.getCodigoModelo());
			}
		});

		categoria= new Combobox();
		categoria.setTooltiptext(" Categoria   "); 
		categoria.setAutocomplete(true);
		categoria.addEventListener(Events.ON_SELECT,getControlador());
		
		categoria.setItemRenderer(new ComboitemRenderer() {
			@Override
			public void render(Comboitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				Categoria categoria = (Categoria) arg1;
				arg0.setValue(categoria);
				arg0.setLabel(categoria.getDescripcionCategoria());
				arg0.setDescription(categoria.getCodigoCategoria());
			}
		});

		categoria.setModel(new ListModelArray(categorias));

		
		tipo= new Combobox();
		tipo.setTooltiptext(" Categoria   "); 
		tipo.setAutocomplete(true);
		
		tipo.setItemRenderer(new ComboitemRenderer() {
			@Override
			public void render(Comboitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				Tipo tipo = (Tipo) arg1;
				arg0.setValue(tipo);
				arg0.setLabel(tipo.getDescripcionTipo());
				arg0.setDescription(tipo.getCodigoTipo());
				
			}
		});
		
		

		maquinariaUnidad = new Combobox();
		maquinariaUnidad.setItemRenderer(new ComboitemRenderer() {
			
			@Override
			public void render(Comboitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				UnidadFuncional unidad = (UnidadFuncional) arg1;
				arg0.setValue(unidad);
				arg0.setLabel(unidad.getStrUbicacion());
				arg0.setDescription(unidad.getDescripcion());
			}
		});
		maquinariaUnidad.setModel(new ListModelList(unidades));

		observacion = new Textbox();
		observacion.setDisabled(true);
		observacion.setRows(5);
		observacion.setWidth("99%");

	}

	public Textbox getSerialChasis() {
		return serialChasis;
	}

	public void setSerialChasis(Textbox serialChasis) {
		this.serialChasis = serialChasis;
	}

	public Textbox getSerialOtro() {
		return serialOtro;
	}

	public void setSerialOtro(Textbox serialOtro) {
		this.serialOtro = serialOtro;
	}

	public Textbox getProcedencia() {
		return procedencia;
	}

	public void setProcedencia(Textbox procedencia) {
		this.procedencia = procedencia;
	}

	public Textbox getNombre() {
		return nombre;
	}

	public void setNombre(Textbox nombre) {
		this.nombre = nombre;
	}

	public List<Marca> getMarcas() {
		return marcas;
	}

	public void setMarcas(List<Marca> marcas) {
		this.marcas = marcas;
	}

	public Combobox getMarca() {
		return marca;
	}

	public void setMarca(Combobox marca) {
		this.marca = marca;
	}

	public List<Modelo> getModelos() {
		return modelos;
	}

	public void setModelos(List<Modelo> modelos) {
		this.modelos = modelos;
		modeloMaquinaria.setModel(new ListModelArray(modelos));
	}

	public Combobox getModeloMaquinaria() {
		return modeloMaquinaria;
	}

	public void setModeloMaquinaria(Combobox modeloMaquinaria) {
		this.modeloMaquinaria = modeloMaquinaria;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public Combobox getCategoria() {
		return categoria;
	}

	public void setCategoria(Combobox categoria) {
		this.categoria = categoria;
	}

	public List<Tipo> getTipos() {
		return tipos;
	}

	public void setTipos(List<Tipo> tipos) {
		this.tipos = tipos;
		tipo.setModel(new ListModelArray(tipos));

	}

	public Combobox getTipo() {
		return tipo;
	}

	public void setTipo(Combobox tipo) {
		this.tipo = tipo;
	}

	public CompGrupoDatos getContenedor() {
		return contenedor;
	}

	public void setContenedor(CompGrupoDatos contenedor) {
		this.contenedor = contenedor;
	}

	public void desactivar(int modoOperacion) {
		// TODO Auto-generated method stub
	
		if(modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ELIMINAR)
		{
			serialChasis.setDisabled(true);
			serialOtro.setDisabled(true);
			procedencia.setDisabled(true);
			nombre.setDisabled(true);
			marca.setDisabled(true);
		   	modeloMaquinaria.setDisabled(true);
		   	categoria.setDisabled(true);
		   	tipo.setDisabled(true);
		   	maquinariaUnidad.setDisabled(true);
		}
		
		if(modoOperacion == Accion.EDITAR )
		{
			serialChasis.setDisabled(true);
			serialOtro.setDisabled(true);
		}
		
	}

	public Combobox getMaquinariaUnidad() {
		return maquinariaUnidad;
	}

	public void setMaquinariaUnidad(Combobox maquinariaUnidad) {
		this.maquinariaUnidad = maquinariaUnidad;
	}
	
	
	
}