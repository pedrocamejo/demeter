package cpc.demeter.controlador.mantenimiento.garantia;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.ActivacionGarantia;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.mantenimiento.ContGarantiasExterna;
import cpc.demeter.controlador.administrativo.ContRecibo;
import cpc.demeter.controlador.administrativo.ContReintegro;
import cpc.demeter.controlador.mantenimiento.ContGarantiaExterna;
import cpc.demeter.vista.mantenimiento.garantia.UISeleccionarCliente;
import cpc.modelo.demeter.mantenimiento.Garantia;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.negocio.demeter.administrativo.NegocioClienteAdministrativo;
import cpc.negocio.demeter.administrativo.NegocioFactura;
import cpc.negocio.demeter.mantenimiento.NegocioGarantia;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContSelecionarCliente extends ContVentanaBase<Cliente> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UISeleccionarCliente 		vista;
	private NegocioGarantia			 	servicio;
	private IZkAplicacion 				app;
	private Cliente 					cliente;
	private Object 						llamador;
	private Boolean						contacto;
	private Map							parametros = new HashMap();
	
	public ContSelecionarCliente(int i, ActivacionGarantia app, Object llamador,Boolean contacto,Map parametros) throws ExcFiltroExcepcion, SuspendNotAllowedException, InterruptedException, ExcDatosInvalidos {
		super(i);
		this.app = app;
		this.contacto = contacto;
		this.llamador = llamador;
		if(parametros!= null)
		{
			this.parametros = parametros;
		}
		servicio = NegocioGarantia.getInstance();
		vista = new UISeleccionarCliente("Selección de Garantia ", 1200,	NegocioFactura.getInstance().getClientesProject(),this,app);
		app.agregarHija(vista);
	}

	public ContSelecionarCliente(int consultar,IZkAplicacion app) throws SuspendNotAllowedException, InterruptedException, ExcFiltroExcepcion, ExcDatosInvalidos {
		super(consultar);
		// TODO Auto-generated constructor stub
		this.app = app;
		servicio = NegocioGarantia.getInstance();
		vista = new UISeleccionarCliente("Selección de Garantia ", 1200,	NegocioFactura.getInstance().getClientesProject(),this,app);
		app.agregarHija(vista);
	}
 
	public ContSelecionarCliente(int i, Object llamador ,AppDemeter app,Map parametros) throws SuspendNotAllowedException, InterruptedException, ExcDatosInvalidos, ExcFiltroExcepcion {
		// TODO Auto-generated constructor stub
		super(i);
		this.app = app;
		this.llamador  = llamador; 
		if(parametros!= null)
		{
			this.parametros = parametros;
		}
		vista = new UISeleccionarCliente("Selección de Cliente  ", 850,	NegocioFactura.getInstance().getClientesProject(),this,app);
		vista.modoconsulta(Accion.CONSULTAR);
		app.agregarHija(vista);
	}



	@Override
	public void onEvent(Event arg0) throws Exception, WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
	
		if ((arg0.getTarget() == vista.getBuscar())	|| (arg0.getTarget() == vista.getTxtbuscar()))
		{
			if (vista.getTxtbuscar().getValue().trim().length() == 0) 
			{
				vista.CrearListaModelo(); 

			}
			if (vista.getCampos().getSelectedItem() == null) 
			{
				throw new WrongValueException(vista.getCampos(),"Seleccione un campo para buscar ");
			}
			vista.buscarCliente();
		
		}
		else if (arg0.getTarget() == vista.getAgregar())
		{
			Cliente cliente = null;
			ContCliente contcliente = new ContCliente(0, cliente, app);
			cliente = contcliente.getVistaCliente().getModelo();
			if (contcliente.getOperacion()) 
			{
				vista.getModeloCliente().add(cliente);
				vista.CrearLista(vista.getModeloCliente());
			}
		}
		else if (arg0.getTarget() == vista.getDetalle()) 
		{
			try {
				Cliente cliente = vista.getseleccionarCliente();
				cliente = NegocioFactura.getInstance().getCliente(cliente);
				if (cliente == null) {
					throw new Exception("Debe Seleccionar al menos 1 Items");
				}
				new ContCliente(Accion.CONSULTAR, cliente, app);
			}
			catch (Exception e)
			{
				// TODO: handle exception
				app.mostrarError(e.getMessage());
			}
		} 
	    else if (arg0.getTarget() == vista.getSeleccionar())
		{
			try {
				cliente = vista.getseleccionarCliente();
				cliente = NegocioFactura.getInstance().getCliente(cliente);
				if (cliente == null)
				{
					throw new Exception("Debe Seleccionar al menos 1 Items");
				} 
				else {
					if (llamador.getClass().equals(ContRecibo.class))
					{
						ContRecibo  contRecibo = (ContRecibo) llamador;
						contRecibo.agregarCliente(cliente);
					}
					else if (llamador.getClass().equals(ContGarantia.class))
					{
						ContGarantia contGarantia  = (ContGarantia) llamador;
						if(!contacto)
						{
							contGarantia.seleccionarPropietario(cliente);
						}
						else
						{
							contGarantia.seleccionarContacto(cliente);
						}
					}
					else if(llamador.getClass().equals(ContReintegro.class))
					{
						ContReintegro contReintegro = (ContReintegro) llamador;
						contReintegro.agregarCliente(cliente);
					}
					else if(llamador.getClass().equals(ContGarantiaExterna.class))
					{
						ContGarantiaExterna contenedor = (ContGarantiaExterna) llamador;
						if(parametros.get("propietario")  != null)
						{
							contenedor.seleccionarPropietario(cliente);
						}
						else
						{
							contenedor.seleccionarContacto(cliente);
						}
					}
					vista.detach();
				}
			} catch (Exception e) {
				// TODO: handle exception
				app.mostrarError(e.getMessage());
			}
		}

	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub

	}

	public UISeleccionarCliente getVista() {
		return vista;
	}

	public void setVista(UISeleccionarCliente vista) {
		this.vista = vista;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
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

}
