package cpc.demeter.controlador.mantenimiento;

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
import cpc.demeter.controlador.mantenimiento.garantia.ContCliente;
import cpc.demeter.controlador.mantenimiento.garantia.ContGarantia;
import cpc.demeter.vista.mantenimiento.garantia.UISeleccionarCliente;
import cpc.demeter.vista.mantenimiento.garantia.UISeleccionarMaquinaria;
import cpc.modelo.demeter.mantenimiento.Garantia;
import cpc.modelo.demeter.mantenimiento.MaquinariaExterna;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.negocio.demeter.administrativo.NegocioClienteAdministrativo;
import cpc.negocio.demeter.administrativo.NegocioFactura;
import cpc.negocio.demeter.mantenimiento.NegocioGarantia;
import cpc.negocio.demeter.mantenimiento.NegocioMaquinariaExterna;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContSelecionarMaquinaria extends ContVentanaBase<MaquinariaExterna> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private UISeleccionarMaquinaria 		vista;
	private NegocioMaquinariaExterna	servicio;
	private IZkAplicacion 				app;
	private Cliente 					cliente;
	private Object 						llamador;
	private Map							parametros = new HashMap();
	
	public ContSelecionarMaquinaria(int i,Object llamador,AppDemeter app,Map parametros) throws ExcFiltroExcepcion, SuspendNotAllowedException, InterruptedException, ExcDatosInvalidos {
		super(i);
		this.app = app;
		this.llamador = llamador;
		if(parametros!= null)
		{
			this.parametros = parametros;
		}
		servicio = NegocioMaquinariaExterna.getInstance();
		vista = new UISeleccionarMaquinaria("Selección de Maquinaria", 850,servicio.getMaquinariaSinPropietario(),this,app);
		app.agregarHija(vista);
	}
 

	@Override
	public void onEvent(Event arg0) throws Exception, WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
	
		if ((arg0.getTarget() == vista.getBuscar())	|| (arg0.getTarget() == vista.getTxtbuscar()))
		{
			if (vista.getTxtbuscar().getValue().trim().length() == 0) 
			{

			}
			if (vista.getCampos().getSelectedItem() == null) 
			{
				throw new WrongValueException(vista.getCampos(),"Seleccione un campo para buscar ");
			}
			vista.buscarCliente();
		
		}
	    else if (arg0.getTarget() == vista.getSeleccionar())
		{
	    	try {
				MaquinariaExterna maquinaria = (MaquinariaExterna) vista.getSeleccionarDato();
				if(maquinaria == null)
				{
					app.mostrarError("Seleccione un Item ");
				}
				else if(llamador.getClass().equals(ContGarantiaExterna.class))
				{
					ContGarantiaExterna contenedor = (ContGarantiaExterna) llamador;
					contenedor.agregarMaquinaria(maquinaria);
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
