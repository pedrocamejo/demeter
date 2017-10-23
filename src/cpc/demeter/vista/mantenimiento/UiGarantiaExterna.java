package cpc.demeter.vista.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import com.sun.accessibility.internal.resources.accessibility;
import com.sun.xml.internal.fastinfoset.sax.Properties;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.modelo.demeter.mantenimiento.Garantia;
import cpc.modelo.demeter.mantenimiento.MaquinariaExterna;
import cpc.modelo.demeter.mantenimiento.TipoGarantia;
import cpc.modelo.ministerio.gestion.Cliente;

import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UiGarantiaExterna extends CompVentanaBase<Garantia> {

	private CompGrupoDatos 		 divMaquinaria = new CompGrupoDatos();
	private CompGrupoDatos 	 	 divPropietario = new CompGrupoDatos();
	private CompGrupoDatos 	 	 divContacto = new CompGrupoDatos();
	private CompGrupoDatos 		 divGeneral = new CompGrupoDatos();

	private Button 				addPropietario;
	private Button 				addContacto;
	private Button 				addMaquinaria;
 
	private Label				 estatus;
	private Textbox				 ubicacion;
	
	private Textbox		 		identidadLegalPropietario; // Cédula o RIF
	private Textbox 			nombresPropietario; // Razón social o nombre
	
	private Textbox		 		identidadLegalContacto; // Cédula o RIF
	private Textbox 			nombresContacto; // Razón social o nombre

	
	private Textbox				serialMaquinaria;
	private Textbox				serialMotor;
	private Textbox			    anofabricacion;
 
	private AppDemeter 			 app;
	
	private Cliente				propietario;
	private Cliente				contacto;
	private MaquinariaExterna	maquinaria;

 
	public UiGarantiaExterna(String titulo, int ancho )
	{
		super(titulo, ancho); 
	}

	/**
	 * @param titulo
	 */
	@Override
	public void inicializar() {
		// TODO Auto-generated method stub

		serialMaquinaria = new Textbox();
		serialMaquinaria.setDisabled(true);
		serialMaquinaria.setWidth("90%");
		
		
		serialMotor = new Textbox();;
		serialMotor.setDisabled(true);
		serialMotor.setWidth("90%");
		
		anofabricacion = new Textbox();
		anofabricacion.setDisabled(true);
		anofabricacion.setWidth("90%");
		 
		estatus = new Label();
		
		ubicacion = new Textbox();
		ubicacion.setRows(3);
		ubicacion.setMaxlength(250);
		ubicacion.setWidth("90%");

		addPropietario = new Button();
		addPropietario.setImage("iconos/24x24/agregar.png");
		addPropietario.setSclass("btnEdicion");
		addPropietario.addEventListener(Events.ON_CLICK,getControlador());
		
		addContacto = new Button();
		addContacto.setImage("iconos/24x24/agregar.png");
		addContacto.setSclass("btnEdicion");
		addContacto.addEventListener(Events.ON_CLICK,getControlador());
		
		
		addMaquinaria = new Button();
		addMaquinaria.setImage("iconos/24x24/agregar.png");
		addMaquinaria.setSclass("btnEdicion");
		addMaquinaria.addEventListener(Events.ON_CLICK,getControlador());
		
		
		identidadLegalPropietario = new Textbox();
		identidadLegalPropietario.setDisabled(true);
		identidadLegalPropietario.setWidth("90%");
		
		nombresPropietario = new Textbox();
		nombresPropietario.setDisabled(true);
		nombresPropietario.setWidth("90%");
		
		identidadLegalContacto = new Textbox();
		identidadLegalContacto.setDisabled(true);
		identidadLegalContacto.setWidth("90%");

		nombresContacto = new Textbox();
		nombresContacto.setDisabled(true);
		nombresContacto.setWidth("90%");
		
		
		divPropietario = new CompGrupoDatos("Propietario",5);
		divContacto = new CompGrupoDatos("Contacto ",5);
		divMaquinaria = new CompGrupoDatos("Datos Maquinaria ",4);
		divGeneral = new CompGrupoDatos("Garantia ",1);
		
	}

   

	@Override
	public void dibujar() {

		divMaquinaria.addComponente("Serial Maquinaria ",serialMaquinaria);
		divMaquinaria.addComponente("Serial Motor",serialMotor);
		divMaquinaria.addComponente("Año Fabricacion",anofabricacion);
		divMaquinaria.addComponente("Ubicacion",ubicacion);
		divMaquinaria.addComponente(addMaquinaria);
		divMaquinaria.dibujar(divGeneral);
		
		divPropietario.addComponente("Identidad ",identidadLegalPropietario);
		divPropietario.addComponente("Nombres ",nombresPropietario);
		divPropietario.addComponente(addPropietario);
		divPropietario.dibujar(divGeneral);
		
		divContacto.addComponente("Identidad",identidadLegalContacto);
		divContacto.addComponente("Nombres",nombresContacto);
		divContacto.addComponente(addContacto);
		divContacto.dibujar(divGeneral);
		
		divGeneral.dibujar(this);
		
		addBotonera();
	}

	@Override
	public void cargarValores(Garantia dato) throws ExcDatosInvalidos {

		estatus.setValue(getModelo().getEstatusgarantia());
		maquinaria = getModelo().getMaquinaria();
		if(maquinaria != null)
		{
			propietario = maquinaria.getPropietario();
			contacto = maquinaria.getContacto();
			ubicacion.setValue(maquinaria.getLocalidad());
		}
		estatus.setValue(getModelo().getEstatusgarantia());

		//datos maquinaria 
		if(maquinaria != null)
		{
			serialMaquinaria.setValue(maquinaria.getSerialcarroceria());
			serialMotor.setValue(maquinaria.getSerialMotor());
			anofabricacion.setValue(maquinaria.getAnofabricacion().toString());
		}
		
		if(propietario!= null)
		{
			nombresPropietario.setValue(propietario.getNombres());
			identidadLegalPropietario.setValue(propietario.getIdentidadLegal());
		}
		
		if(contacto != null)
		{
			nombresContacto.setValue(contacto.getNombres());
			identidadLegalContacto.setValue(contacto.getIdentidadLegal());
		}
	  
	}

	public void desactivar(int modoOperacion) {
		
		if(modoOperacion == Accion.EDITAR )
		{
			addMaquinaria.setVisible(false);
		}
		if(modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.IMPRIMIR_ITEM || modoOperacion == Accion.PROCESAR)
		{
			addMaquinaria.setVisible(false);
			addPropietario.setVisible(false);
			addContacto.setVisible(false);
			ubicacion.setDisabled(true);
		
		}
	}

	public Button getAddPropietario() {
		return addPropietario;
	}

	public void setAddPropietario(Button addPropietario) {
		this.addPropietario = addPropietario;
	}

	public Button getAddContacto() {
		return addContacto;
	}

	public void setAddContacto(Button addContacto) {
		this.addContacto = addContacto;
	}

	public Button getAddMaquinaria() {
		return addMaquinaria;
	}

	public void setAddMaquinaria(Button addMaquinaria) {
		this.addMaquinaria = addMaquinaria;
	}

	public Cliente getPropietario() {
		return propietario;
	}

	public void setPropietario(Cliente propietario) {
		this.propietario = propietario;
	}

	public Cliente getContacto() {
		return contacto;
	}

	public void setContacto(Cliente contacto) {
		this.contacto = contacto;
	}

	public MaquinariaExterna getMaquinaria() {
		return maquinaria;
	}

	public void setMaquinaria(MaquinariaExterna maquinaria) {
		this.maquinaria = maquinaria;
	}

	public void seleccionarPropietario(Cliente cliente) {
		// TODO Auto-generated method stub
		
		if(cliente!= null)
		{
			propietario = cliente;
			nombresPropietario.setValue(propietario.getNombres());
			identidadLegalPropietario.setValue(propietario.getIdentidadLegal());
		}
		
	}

	public void seleccionarContacto(Cliente cliente) {
		// TODO Auto-generated method stub
	
		if(cliente != null)
		{
			contacto = cliente;
			nombresContacto.setValue(contacto.getNombres());
			identidadLegalContacto.setValue(contacto.getIdentidadLegal());
		}
	}

	public void seleccionarMaquinaria(MaquinariaExterna maquinaria2) {
		// TODO Auto-generated method stub
		if(maquinaria2 != null)
		{
			maquinaria = maquinaria2;
			serialMaquinaria.setValue(maquinaria.getSerialcarroceria());
			serialMotor.setValue(maquinaria.getSerialMotor());
			anofabricacion.setValue(maquinaria.getAnofabricacion().toString());
		}
	
	
	}

	public Textbox getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Textbox ubicacion) {
		this.ubicacion = ubicacion;
	}

	public void actualizar() {
		// TODO Auto-generated method stub
		getModelo().setMaquinaria(maquinaria);
		maquinaria.setGarantia(getModelo());
		maquinaria.setLocalidad(ubicacion.getValue());
		maquinaria.setPropietario(propietario);
		maquinaria.setContacto(contacto);
	} 

	
	
	
}