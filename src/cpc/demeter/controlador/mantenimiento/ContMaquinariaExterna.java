package cpc.demeter.controlador.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.bytecode.buildtime.ExecutionException;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Messagebox;

import cpc.ares.modelo.Accion;
import cpc.demeter.ActivacionGarantia;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.mantenimiento.CatMaquinariaExterna;
import cpc.demeter.catalogo.mantenimiento.CatMaquinariaSelect;
import cpc.demeter.controlador.mantenimiento.garantia.ContCliente;
import cpc.demeter.controlador.mantenimiento.garantia.ContSelecionarCliente;
import cpc.demeter.vista.mantenimiento.garantia.UiMaquinariaExterna;
import cpc.modelo.demeter.mantenimiento.MaquinariaExterna;
import cpc.modelo.demeter.mantenimiento.TipoGarantia;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.modelo.sigesp.basico.Marca;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.modelo.sigesp.basico.Tipo;
import cpc.negocio.demeter.mantenimiento.NegocioGarantia;
import cpc.negocio.demeter.mantenimiento.NegocioMaquinariaExterna;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContMaquinariaExterna extends ContVentanaBase<MaquinariaExterna> {

	private static final long serialVersionUID = 6184414588153046382L;


	private NegocioGarantia 				servicio =  NegocioGarantia.getInstance();
 	private UiMaquinariaExterna 			vista;
	private MaquinariaExterna				maquinariaExterna;
	private CatMaquinariaSelect			    llamadorselect; // para agregar datos desde el carMaquinariaSelect 


	public ContMaquinariaExterna(int i, MaquinariaExterna maquinariaExterna, CatMaquinariaExterna llamador, AppDemeter app)throws SuspendNotAllowedException, InterruptedException,ExcFiltroExcepcion, ExcDatosInvalidos
	{
		super(i);
		List<Marca> listMarca = servicio.getMarcasxGarantia();
		List<TipoGarantia> lismodelo = new ArrayList<TipoGarantia>();
	 
		this.setAppa(app);
		this.maquinariaExterna = maquinariaExterna;

		if (Accion.AGREGAR == i)
		{
			this.maquinariaExterna = new MaquinariaExterna();
		}
		else
		{
			lismodelo = servicio.getModeloxGarantia(maquinariaExterna.getTipo().getModelo().getMarca());
		}
		
		vista = new UiMaquinariaExterna("Maquinaria Externa  (" + Accion.TEXTO[i] + ")", 700,servicio.getMarcasxGarantia(),lismodelo);
		setear(this.maquinariaExterna, vista, llamador, app);
		this.vista = (UiMaquinariaExterna) getVista();
		this.vista.modoOperacion(getModoOperacion());

	}
 

	public ContMaquinariaExterna(int editar, MaquinariaExterna maquinaria, ContOrdenGarantia contOrdenGarantia,IZkAplicacion app) throws ExcDatosInvalidos, InterruptedException {
		super(editar);
		// metodo para puro consultar :-D
		this.setAppa(app);
		servicio = NegocioGarantia.getInstance();
		NegocioGarantia servici2 = new NegocioGarantia();
		this.maquinariaExterna = maquinaria;
		vista = new UiMaquinariaExterna("Maquinaria Externa  ("	+ Accion.TEXTO[editar] + ")", 700);
		vista.setControlador(this);
		vista.setMarcas(servicio.getMarcasxGarantia());
		vista.setApp(app);
		List<TipoGarantia> lista = servicio.getModeloxGarantia(this.maquinariaExterna.getTipo().getModelo().getMarca());
		vista.setModelos(lista);
		vista.setMode("modal");
		vista.setModelo(maquinaria);
		vista.dibujarVentana();
		vista.cargar();
		vista.modoOperacion(editar);
		app.agregarHija(this.vista);
		this.vista.doModal();
	}

	public ContMaquinariaExterna(int consultar, MaquinariaExterna maquinaria,IZkAplicacion app) throws ExcDatosInvalidos
	{
		super(consultar);
		this.setAppa(app);
		this.maquinariaExterna = maquinaria;
		servicio = NegocioGarantia.getInstance();
		
		List<Marca> listMarca = servicio.getMarcasxGarantia();
		
		List<TipoGarantia> lismodelo = new ArrayList<TipoGarantia>();
		
		if (Accion.AGREGAR == consultar)
		{
			this.maquinariaExterna = new MaquinariaExterna();
			this.setDato(maquinariaExterna);
			
		}
		else
		{
			lismodelo = servicio.getModeloxGarantia(maquinariaExterna.getTipo().getModelo().getMarca());
		}
		
		vista = new UiMaquinariaExterna("Maquinaria Externa  (" + Accion.TEXTO[consultar] + ")", 700,servicio.getMarcasxGarantia(),lismodelo,this,maquinariaExterna,app);
		vista.getAceptar().addEventListener(Events.ON_CLICK,botonaceptar());// para que pueda guardar y recargar la lista de CatMaquinariaSelect 
		app.agregarHija(vista);
		setVista(vista);
		vista.modoOperacion(consultar);
		
		
	}

	//Sobre escribo  el boton aceptar puesto que requiero hacer unas operaciones diferentes, a las que realiza el metodo CRUL 
	public EventListener botonaceptar()
	{
		return new EventListener() {
			
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				IZkAplicacion a = (IZkAplicacion) TransactionSynchronizationManager.getResource("obj");
				boolean b = TransactionSynchronizationManager.hasResource("obj");
				boolean igual = getAppa().equals(a);
				if (!igual)
				{
					if (b)
					{
						TransactionSynchronizationManager.unbindResource("obj");
					}
					TransactionSynchronizationManager.bindResource("obj",getAppa());
				}
				validar();
				servicio.guardar(getDato());
				llamadorselect.agregarMaquinaria(getDato());
				vista.detach();
				

			}
		};
	}

	public void guardar() {
		try {
			getDato().setTipo((TipoGarantia) vista.getModeloMa().getSelectedItem().getValue());
			servicio.guardar(getDato());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub

		if (vista.getMarcaMa() == arg0.getTarget()) 
		{
			if (vista.getMarcaMa().getSelectedItem() != null) 
			{
				Marca marca = (Marca) vista.getMarcaMa().getSelectedItem().getValue();
				List<TipoGarantia> lismodelo = servicio.getModeloxGarantia(marca);
			 	vista.CargarModelo(lismodelo);

			}
		} 
		else if (vista.getAceptar() == arg0.getTarget()|| vista.getCancelar() == arg0.getTarget())
		{
			procesarCRUD(arg0);
		}

	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		try {
			
			if(getDato().getPropietario() != null )
			{
				throw new ExecutionException("NO Puedes Eliminar Esta Maquinaria Puesto que ah sido Vendida ");
			}
			servicio.borrar(getDato());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		vista.actualizarModelo(); // para que agarre el ]Mayor :D

		if (vista.getSerialCarroceria().getValue().trim().length() == 0)
		{
			throw new WrongValueException(vista.getSerialCarroceria()," Ingrese el Serial de Carroceria de la Maquinaria ");
		}
		if (servicio.perteneceserialcarroceria(vista.getSerialCarroceria().getValue(), vista.getModelo().getId()))
		{
			throw new WrongValueException(vista.getSerialCarroceria(),"Esta Placa Pertenece a otra Maquinaria ");
		}

		if (vista.getSerialMotor().getValue().trim().length() == 0)
		{
			throw new WrongValueException(vista.getSerialMotor(),"Ingrese el Serial del Motor");
		}

		if (servicio.perteneceserialMotor(vista.getSerialMotor().getValue().trim().toUpperCase(), vista.getModelo().getId()))
		{
			throw new WrongValueException(vista.getSerialMotor(),"Serial Motor Invalido Pertenece otra Maquinaria");
		}

		if (vista.getAnoFabricacion().getValue() < 1900) 
		{
			throw new WrongValueException(vista.getAnoFabricacion(),"Fecha Invalida");
		}
		
		if(vista.getMarcaMa().getSelectedItem() == null)
		{
			throw new WrongValueException(vista.getMarcaMa(),"Seleccione una Marca ");
		}
		
		if(vista.getModeloMa().getSelectedItem() == null)
		{
			throw new WrongValueException(vista.getModeloMa(),"Seleccione un Modelo ");
		}
		
	}

	@Override
	public void anular() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub

	}


	public CatMaquinariaSelect getLlamadorselect() {
		return llamadorselect;
	}


	public void setLlamadorselect(CatMaquinariaSelect llamadorselect) {
		this.llamadorselect = llamadorselect;
	}

}
