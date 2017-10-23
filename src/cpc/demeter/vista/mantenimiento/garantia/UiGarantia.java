package cpc.demeter.vista.mantenimiento.garantia;
  
 
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption; 
import org.zkoss.zul.Div; 
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Hbox; 
import org.zkoss.zul.Label; 
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import cpc.ares.modelo.Accion; 
import cpc.demeter.controlador.mantenimiento.garantia.ContGarantia;
import cpc.modelo.demeter.mantenimiento.Garantia;
import cpc.modelo.demeter.mantenimiento.MaquinariaExterna; 
import cpc.modelo.ministerio.gestion.Cliente; 
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
 
public class UiGarantia extends CompVentanaBase<Garantia> {

	private static final long serialVersionUID = -2943180995656327003L;

	private Label			 	nombreuser;
	private Label 			 	direccionuser;
	private Label 		 	 	cedulauser;

	private Label 			 	nombreuserContacto;
	private Label		 	 	direccionuserContacto;
	private Label 			 	cedulauserContacto;

	private Label 		   	 	tipoGarantia;
	private	Label 	 			anofabricacion;
	public Textbox getLocalidad() {
		return localidad;
	}

	public void setLocalidad(Textbox localidad) {
		this.localidad = localidad;
	}

	private Label  				serialcarroceria;
	
	private  Textbox 			localidad;   
	  

	private Button 			 	cargarMaquina;
	private Button			 	detalleMaquina;
	
	private Button			 	cargarcliente;
	private Button 			 	detallecliente;
	
	private Button			 	cargarcontacto;
	private Button 			 	detallecontacto;

	
	private CompGrupoDatos 		 divMaquinaria ;
	private CompGrupoDatos 		 divPropietario;
	private CompGrupoDatos 		 divContacto;
	private CompGrupoDatos 		 divGeneral;
	
	private Cliente				 propietario;
	private MaquinariaExterna    maquinaria;
	private Cliente				contacto; 
 

	public UiGarantia() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UiGarantia(String titulo, int ancho) {
		super(titulo, ancho);
		// TODO Auto-generated constructor stub
	}

	public UiGarantia(String titulo) {
		super(titulo);
		// TODO Auto-generated constructor stub
	}
   
 
	public UiGarantia(String titulo, int ancho, Garantia garantia,ContGarantia controlador, int modoOperacion) throws ExcDatosInvalidos
	{
		super(titulo, ancho);
		this.setControlador(controlador);
		setModelo(garantia);
		inicializar();
		dibujar();
		cargarValores(garantia);
		this.modoOperacion(modoOperacion);
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		nombreuser = new Label();
		direccionuser = new Label();
		cedulauser = new Label();
		
		nombreuserContacto = new Label();
		direccionuserContacto = new Label();
		cedulauserContacto = new Label();

		
		tipoGarantia = new Label();
		anofabricacion = new Label();
		serialcarroceria = new Label();
		
		localidad = new Textbox("");
			localidad.setTooltiptext("Dirección Fisica donde sera trasladada la Maquinaria");
			localidad.setMaxlength(250);
			localidad.setRows(3);
			localidad.setWidth("300px");
			
			
		cargarMaquina  = new Button("Cargar");
			cargarMaquina.setTooltiptext("Seleccionar una Maquinaria ");
			cargarMaquina.setImage("/iconos/24x24/agregar.png");
			cargarMaquina.addEventListener(Events.ON_CLICK,getControlador());
			cargarMaquina.setHeight("12px");
			
		detalleMaquina = new Button("Detalle ");
			detalleMaquina.setTooltiptext(" Visualizar Maquinaria Seleccionada ");
			detalleMaquina.setImage("/iconos/24x24/consultar.png");
			detalleMaquina.addEventListener(Events.ON_CLICK,getControlador());
			detalleMaquina.setHeight("30px");
			
		cargarcliente = new Button("Cargar");
			cargarcliente.setTooltiptext("Seleccionar un Cliente ");
			cargarcliente.setImage("/iconos/24x24/agregar.png");
			cargarcliente.addEventListener(Events.ON_CLICK,getControlador());
			cargarcliente.setHeight("30px");
			
		detallecliente = new Button("Detalle");
			detallecliente.setTooltiptext(" Visualizar el del Cliente ");
			detallecliente.setImage("/iconos/24x24/consultar.png");
			detallecliente.addEventListener(Events.ON_CLICK,getControlador());
			detallecliente.setHeight("30px");
			
		cargarcontacto = new Button("Cargar");
			cargarcontacto.setTooltiptext("Seleccionar un Contacto para la Maquinaria ");
			cargarcontacto.setImage("/iconos/24x24/agregar.png");
			cargarcontacto.addEventListener(Events.ON_CLICK,getControlador());
			cargarcontacto.setHeight("30px");
			
		detallecontacto = new Button("Detalle");
			detallecontacto.setTooltiptext("Seleccionar un Detalle ");
			detallecontacto.setImage("/iconos/24x24/consultar.png");;
			detallecontacto.addEventListener(Events.ON_CLICK,getControlador());
			detallecontacto.setHeight("30px");
			
		 divMaquinaria = new CompGrupoDatos("Detalles Maquinaria ",2);
 		 divPropietario = new CompGrupoDatos("Detalles Propietario",2);
 		 divContacto = new CompGrupoDatos("Detalles Contacto ",2);
 		 divGeneral = new CompGrupoDatos("Detalles Garantia ",1);
	
	}
 
	@Override
	public void dibujar() {
  
		  
		/******** Maquinaria ***************/
		divMaquinaria.addComponente("Serial Carroceria ",serialcarroceria);
		divMaquinaria.addComponente("Marca - Modelo ",tipoGarantia);
		divMaquinaria.addComponente("Año de Fabricación ",anofabricacion);
		
		Div botones = new Div();
			botones.appendChild(cargarMaquina);
			botones.appendChild(detalleMaquina);
		
		divMaquinaria.appendChild(botones);
		divMaquinaria.dibujar();
		
			
		/********  Propietario   *********************************************/
		
		divPropietario.addComponente("Cedula ",cedulauser);
		divPropietario.addComponente("Nombre ",nombreuser);
		divPropietario.addComponente("Dirección ",direccionuser);
		

		botones = new Div();
			botones.appendChild(cargarcliente);
			botones.appendChild(detallecliente);
		
		divPropietario.appendChild(botones);
		divPropietario.dibujar();
		
		/********** Contacto *******************************/
		
		divContacto.addComponente("Cedula ",cedulauserContacto);
		divContacto.addComponente("Nombre ",nombreuserContacto);
		divContacto.addComponente("Dirección ",direccionuserContacto);
		

		botones = new Div();
			botones.appendChild(cargarcontacto);
			botones.appendChild(detallecontacto);
		
		divContacto.appendChild(botones);
		divContacto.dibujar();
		
		/***************** general ***********************/
		
		divGeneral.addComponente(divMaquinaria);
		divGeneral.addComponente(divPropietario);
		Hbox hbox = new Hbox();
			hbox.appendChild(new Label("Localidad Maquinaria "));
			hbox.appendChild(localidad);
		divGeneral.addComponente(hbox);
		divGeneral.dibujar(this);
		addBotonera();

	}
 

	public void cargarPropietario(Cliente propietario) throws InterruptedException 
	{
		if(maquinaria == null)
		{
			Messagebox.show("Debes Seleccionar Una Maquinaria antes de su Propietario !!");
		}
		else
		{
			this.propietario = propietario;
			nombreuser.setValue(propietario.getNombres());
			cedulauser.setValue(propietario.getIdentidadLegal());
			direccionuser.setValue(propietario.getDireccion());
		}

	}

	@Override
	public void cargarValores(Garantia dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub

		maquinaria = getModelo().getMaquinaria();
		propietario = (maquinaria == null ? null:maquinaria.getPropietario());
		contacto = (maquinaria == null ?  null:maquinaria.getContacto());
		
		cargarMaquinaria(maquinaria);
		try 
		{
			if(propietario != null)
			{
				cargarPropietario(propietario);
			}
			if(contacto != null)
			{
				contacto();
				cargarContacto(contacto);
			}
		}
		catch (InterruptedException e)
		{
				// TODO Auto-generated catch block
				e.printStackTrace();
		}
		localidad.setValue((maquinaria == null? "":maquinaria.getLocalidad()));
		
		actualizarModelo();
	}
  

	public CompGrupoDatos getDivPropietario() {
		return divPropietario;
	}

	public void setDivPropietario(CompGrupoDatos divPropietario) {
		this.divPropietario = divPropietario;
	}

	public void modoOperacion(int i) 
	{
		// TODO Auto-generated method stub
		if (i == Accion.CONSULTAR || i == Accion.IMPRIMIR_ITEM) {
		 
		 	cargarMaquina.setDisabled(true);
		 	cargarcliente.setDisabled(true);
		 	cargarcontacto.setDisabled(true);
		 	localidad.setDisabled(true);
			aceptar.setVisible(false);
			
		}
		if (Accion.IMPRIMIR_ITEM == i) {
			getAceptar().setVisible(true);
		}
		if(i == Accion.EDITAR)
		{
			cargarMaquina.setDisabled(true);
		}
	}


	public void contacto() 
	{
		// TODO Auto-generated method stub
		
		for(Object obj: divPropietario.getChildren())
		{
			if(obj == divContacto)
			{
				return;
			}
		}
		divPropietario.appendChild(divContacto);
	}
	
	
	public void setMaquinaria(MaquinariaExterna maquinaria)
	{
		
		this.maquinaria = maquinaria;
	}

	


	public Button getCargarMaquina() {
		return cargarMaquina;
	}

	public Button getDetalleMaquina() {
		return detalleMaquina;
	}

	public Button getCargarcliente() {
		return cargarcliente;
	}

	public Button getDetallecliente() {
		return detallecliente;
	}

	public Button getCargarcontacto() {
		return cargarcontacto;
	}

	public Button getDetallecontacto() {
		return detallecontacto;
	}

	public void setCargarMaquina(Button cargarMaquina) {
		this.cargarMaquina = cargarMaquina;
	}

	public void setDetalleMaquina(Button detalleMaquina) {
		this.detalleMaquina = detalleMaquina;
	}

	public void setCargarcliente(Button cargarcliente) {
		this.cargarcliente = cargarcliente;
	}

	public void setDetallecliente(Button detallecliente) {
		this.detallecliente = detallecliente;
	}

	public void setCargarcontacto(Button cargarcontacto) {
		this.cargarcontacto = cargarcontacto;
	}

	public void setDetallecontacto(Button detallecontacto) {
		this.detallecontacto = detallecontacto;
	}
 
	public Cliente getPropietario() {
		return propietario;
	}

	public MaquinariaExterna getMaquinaria() {
		return maquinaria;
	}

	public Cliente getContacto() {
		return contacto;
	}

	public void setPropietario(Cliente propietario) {
		this.propietario = propietario;
	}

	public void setContacto(Cliente contacto) {
		this.contacto = contacto;
	}

	public void cargarContacto(Cliente cliente) throws InterruptedException {
		// TODO Auto-generated method stub
		if(maquinaria == null)
		{
			Messagebox.show("Debes Seleccionar Una Maquinaria antes de su Propietario !!");
		}
		else
		{
			this.contacto = cliente;
			nombreuserContacto.setValue(cliente.getNombres());
			cedulauserContacto.setValue(cliente.getIdentidadLegal());
			direccionuserContacto.setValue(cliente.getDireccion());
		}
	}	
	public void cargarMaquinaria(MaquinariaExterna maquinaria)
	{
		this.maquinaria = maquinaria;
		if(maquinaria != null)
		{
			tipoGarantia.setValue(maquinaria.getTipo().getModelo().toString());
			anofabricacion.setValue(maquinaria.getAnofabricacion().toString());
			serialcarroceria.setValue(maquinaria.getSerialcarroceria());
		}
	}

		
	public void cargarModelo() {
		// TODO Auto-generated method stub
		getModelo().setMaquinaria(maquinaria);
		maquinaria.setPropietario(propietario);
		maquinaria.setContacto(contacto);
		maquinaria.setLocalidad(localidad.getValue());
		
	} 
	
	
	
	
	
}
