package cpc.demeter.vista.mantenimiento.garantia;

 
import java.util.List;
 
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events; 
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;  
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox; 
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
 

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.mantenimiento.ContMaquinariaExterna;
import cpc.modelo.demeter.mantenimiento.MaquinariaExterna;
import cpc.modelo.demeter.mantenimiento.TipoGarantia; 
import cpc.modelo.sigesp.basico.Marca;  
import cpc.zk.componente.excepciones.ExcDatosInvalidos; 
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiMaquinariaExterna extends CompVentanaBase<MaquinariaExterna> {

	private CompGrupoDatos		maquinaria;
 
	private Intbox				 anoFabricacion;
	
	private Textbox				 serialCarroceria;
	private Textbox				 serialMotor;
 
	private Combobox		 	 marcaMa;
	private Combobox			 modeloMa;
	private Div					 contenedor;
	
	private List<Marca>			 marcas;
	private List<TipoGarantia>	 modelos;
 

	private IZkAplicacion		 app;
	private Integer				 modoOperacion;


	
	public UiMaquinariaExterna() {
		super();  
		
		// TODO Auto-generated constructor stub
	}
	public UiMaquinariaExterna(String titulo, int ancho) {
		super(titulo, ancho);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param titulo
	 */
	public UiMaquinariaExterna(String titulo) {
		super(titulo);
		// TODO Auto-generated constructor stub
	}

	public UiMaquinariaExterna(String titulo, int ancho, List<Marca> marcas, List<TipoGarantia> modelos) {
		// TODO Auto-generated constructor stub
		super(titulo,ancho);
		this.marcas = marcas;
		this.modelos = modelos;
	}

	public UiMaquinariaExterna(String titulo, int ancho,List<Marca> marcas, List<TipoGarantia>  modelos,ContMaquinariaExterna controlador,MaquinariaExterna dato,IZkAplicacion app) throws ExcDatosInvalidos
	{
		super(titulo,ancho);
		this.marcas = marcas;
		this.modelos = modelos;
		this.setControlador(controlador);
		this.setModelo(dato);
		this.setApp(app);
		this.dibujarVentana();
		this.cargar();
	}
	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		maquinaria = new CompGrupoDatos("Datos Maquinaria ",4);
 
		
		anoFabricacion = new Intbox(2015);
		anoFabricacion.setMaxlength(4);
		anoFabricacion.setMaxlength(20);

		serialCarroceria = new Textbox();
		serialCarroceria.addEventListener(Events.ON_CHANGE, cambiarmayor());
		serialCarroceria.setMaxlength(25);
		serialCarroceria.setWidth("90%");
		
		serialMotor = new Textbox();
		serialMotor.addEventListener(Events.ON_CHANGE, cambiarmayor());
		serialMotor.setMaxlength(25);
		serialMotor.setWidth("90%");
		
		
		//localidad = new Textbox();

		marcaMa = new Combobox(); 
			for(Marca marca : marcas)
			{
				Comboitem combo = new Comboitem(marca.getDescripcion());
				combo.setValue(marca);
				marcaMa.appendChild(combo); 
			}
		marcaMa.addEventListener(Events.ON_SELECT, getControlador());
		
		
		modeloMa = new Combobox();
			for(TipoGarantia tipo : modelos)
			{
				Comboitem combo = new Comboitem(tipo.getDescripcion());
				combo.setValue(tipo);
				modeloMa.appendChild(combo);
			}

		contenedor = new Div();	
	} 

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub
  
		maquinaria.addComponente("Serial Carroceria ", serialCarroceria);
		maquinaria.addComponente("Serial Motor ", serialMotor);
		maquinaria.addComponente("Marca ",marcaMa);
		contenedor.appendChild(modeloMa);
		maquinaria.addComponente("Modelo",contenedor);
		maquinaria.addComponente("año Fabricacion", anoFabricacion);
	
		maquinaria.dibujar(this);
		this.addBotonera();
	}

	@Override
	public void cargarValores(MaquinariaExterna dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub

		MaquinariaExterna maquinaria = getModelo();

		getBinder().addBinding(serialCarroceria, "value",getNombreModelo() + ".serialcarroceria", null, null, "save",	null, null, null, null);
		getBinder().addBinding(serialMotor, "value",getNombreModelo() + ".serialMotor", null, null, "save", null,null, null, null);
		getBinder().addBinding(anoFabricacion, "value",	getNombreModelo() + ".anofabricacion", null, null, "save",null, null, null, null);

		serialCarroceria.setValue(maquinaria.getSerialcarroceria());
		serialMotor.setValue(maquinaria.getSerialMotor());
	 

		/********************************** Si es una empresa el Contacto **********************************/
		
		if(getModelo().getTipo() != null)
		{
			TipoGarantia  modelo = getModelo().getTipo();
			seleccionarMarca(modelo.getModelo().getMarca());
			seleccionarModelo(modelo);
		}
		
	}

	 
 
	public EventListener cambiarmayor() {
		return new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				Textbox texto = (Textbox) arg0.getTarget();
				texto.setValue(texto.getValue().trim().toUpperCase());
			}
		};
	}
 

	public Intbox getAnoFabricacion() {
		return anoFabricacion;
	}

	public void setAnoFabricacion(Intbox anoFabricacion) {
		this.anoFabricacion = anoFabricacion;
	}

	public Textbox getSerialCarroceria() {
		return serialCarroceria;
	}

	public void setSerialCarroceria(Textbox serialCarroceria) {
		this.serialCarroceria = serialCarroceria;
	}

	public Textbox getSerialMotor() {
		return serialMotor;
	}

	public void setSerialMotor(Textbox serialMotor) {
		this.serialMotor = serialMotor;
	}
 
	public Combobox getMarcaMa() {
		return marcaMa;
	}

	public void setMarcaMa(Combobox marcaMa) {
		this.marcaMa = marcaMa;
	}

	public Combobox getModeloMa() {
		return modeloMa;
	}

	public void setModeloMa(Combobox modeloMa) {
		this.modeloMa = modeloMa;
	}

	public List<Marca> getMarcas() {
		return marcas;
	}

	public void setMarcas(List<Marca> marcas) {
		this.marcas = marcas;
	}

	public List<TipoGarantia> getModelos() {
		return modelos;
	}

	public void setModelos(List<TipoGarantia> modelos) {
		this.modelos = modelos;
	}
	
	public IZkAplicacion getApp() {
		return app;
	}

	public Integer getModoOperacion() {
		return modoOperacion;
	}

	public void setApp(IZkAplicacion app) {
		this.app = app;
	}

	public void setModoOperacion(Integer modoOperacion) {
		this.modoOperacion = modoOperacion;
	}
 

	private void seleccionarModelo(TipoGarantia modelo) {
		// TODO Auto-generated method stub 
		for (Object iter:  modeloMa.getChildren())
		{
			Comboitem item = (Comboitem) iter; 
			TipoGarantia modelo2 = (TipoGarantia) item.getValue();
			
			if (modelo2.getId().equals(modelo.getId()))
			{
				modeloMa.setSelectedItem(item);
				return;
			}
		}
		
	}

	private void seleccionarMarca(Marca marca) {
		// TODO Auto-generated method stub
	 
		for (Object iter: marcaMa.getChildren())
		{
			Comboitem comboitem = (Comboitem) iter;
			Marca marca2 = (Marca)comboitem.getValue();
			if (marca2.getCodigoMarca().equals(marca.getCodigoMarca()))
			{
				marcaMa.setSelectedItem(comboitem);
				return;
			}
		}
	} 

	public void modoOperacion(int i) {
		// TODO Auto-generated method stub
		if (i == Accion.CONSULTAR || i == Accion.ELIMINAR) {
			serialCarroceria.setDisabled(true);
			serialMotor.setDisabled(true); 
			marcaMa.setDisabled(true);
			modeloMa.setDisabled(true);
			anoFabricacion.setDisabled(true);
			getAceptar().setVisible(false); 
		}
		if (i == Accion.EDITAR) {
			serialCarroceria.setDisabled(true);

		}
		if(Accion.ELIMINAR == i)
		{
			getAceptar().setVisible(true); 
		}
	}

	public void CargarModelo(List<TipoGarantia> lismodelo) {
		// TODO Auto-generated method stub
		this.modelos = lismodelo;
		if(modeloMa != null)
		{
			modeloMa.detach();
		}
		modeloMa= new Combobox();
				for(TipoGarantia tipo : modelos)
		{
			Comboitem combo = new Comboitem(tipo.getDescripcion());
			combo.setValue(tipo);
			modeloMa.appendChild(combo);
		}
		contenedor.appendChild(modeloMa);
	}

}
