package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.administrativo.ContRecibo;
import cpc.demeter.controlador.administrativo.ContSeleccionarCheque;
import cpc.modelo.demeter.administrativo.Cheque;
import cpc.modelo.demeter.administrativo.FormaPagoDeposito;
import cpc.modelo.sigesp.basico.Banco;
import cpc.modelo.sigesp.basico.CuentaBancaria;
import cpc.negocio.demeter.NegocioCuentaBancaria;
import cpc.negocio.demeter.administrativo.NegocioFormaPago;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContFormaPagoDeposito extends ContVentanaBase<FormaPagoDeposito> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NegocioFormaPago 		servicio;
	private AppDemeter 				app;
	private UIFormaPagoDeposito		vista;
	private ContRecibo				llamador;
	//private FormaPago				modelo;
	
	public ContFormaPagoDeposito(int modoOperacion,ContRecibo llamador, AppDemeter app,FormaPagoDeposito modelo) throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcFiltroExcepcion
	{
		super(modoOperacion);
		this.app = app;
		this.setDato(modelo);
		try {
			servicio = NegocioFormaPago.getInstance();
			this.llamador = llamador;
			List<Banco> bancos = servicio.getBancos();
			vista = new UIFormaPagoDeposito("FORMA PAGO ","750px",bancos);
			setear(modoOperacion,modelo, vista, app);
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		}
	}

	private void setear(int modoOperacion,FormaPagoDeposito obj, UIFormaPagoDeposito vista , AppDemeter app) 
	{
		try {
			vista.setModelo(obj);
			vista.setMode("modal");
			vista.setControlador(this);
			vista.inicializar();
			vista.dibujar();
			vista.cargarValores();
		 	vista.desactivar(modoOperacion);
			app.agregarHija(vista);
			vista.doModal();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

		if(vista.getDocumento().getValue().trim().length() ==  0 )
		{
			throw new WrongValueException(vista.getDocumento()," Ingrese Un Numero de Documento ");
		}
		if(servicio.getformaPagoUsada(vista.getDocumento().getValue().trim()))
		{
			throw new WrongValueException(vista.getDocumento()," Este Documento Ya ah Sido Utilizado  ");
		}
		
		if(vista.getFecha().getValue() == null )
		{
			throw new WrongValueException(vista.getFecha()," Fecha Invalida  ");
		}
		
		if(vista.getBanco().getSelectedItem() == null)
		{
			throw new WrongValueException(vista.getBanco(),"Seleccione un Banco ");
		}
		
		if (vista.getCuenta().getSelectedItem()==null)
		{
			throw new WrongValueException(vista.getCuenta(),"Seleccione una Cuenta Bancaria  ");
			
		}
		if(vista.getMonto().getValue() <=  0 )
		{
			throw new WrongValueException(vista.getMonto()," Monto Invalido ");
		}

	}

	public void onEvent(Event arg0) throws Exception {
	
		if(arg0.getTarget() == vista.getAceptar())
		{
			validar();
			FormaPagoDeposito modelo = vista.getModelo();
			llamador.agregarFormaPago(modelo);
			vista.detach();
		}
		else if(arg0.getTarget() == vista.getCancelar())
		{
			vista.detach();
		}
		else if(arg0.getTarget() == vista.getBanco())
		{
			Banco banco = (Banco) vista.getBanco().getSelectedItem().getValue();
			if(banco != null)
			{	
				List<CuentaBancaria> lista =NegocioCuentaBancaria.getInstance().CuentaBancarias(banco.getId());
				if(lista.size() != 0)
				{
					vista.setCuentas(lista);
				}
				else
				{
					vista.setCuentas(new ArrayList<CuentaBancaria>());
					vista.getCuenta().setText("");
					app.mostrarAdvertencia("Este Banco No Posee Cuentas Asociadas ");
				}
			}
		}
		else if(arg0.getTarget() == vista.getEfectivo())
		{
			vista.actualizarMontos();
		}
		else if(arg0.getTarget() == vista.getAgregar())
		{
		   new ContSeleccionarCheque(Accion.AGREGAR,this, app);
		}
		else if(arg0.getTarget() == vista.getQuitar())
		{
			if (vista.getListCheques().getSelectedItem() != null)
			{
				Cheque cheque = (Cheque) vista.getListCheques().getSelectedItem().getValue();
				vista.quitarCheque(cheque);
			}
			else
			{
				Messagebox.show("Seleccione un Cheque ");
			}
		}
			
	}

	public void anular() throws ExcFiltroExcepcion {
	}

	public void correjir() throws ExcFiltroExcepcion {
	}

	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
	}

	public void eliminar() {
	}

	public void guardar() {
	}


	public void agregarCheque(Cheque cheque) throws InterruptedException {
		// TODO Auto-generated method stub
		for(Cheque chequeAgregardo :vista.getCheques())
		{
			if(chequeAgregardo.equals(cheque))
			{
				Messagebox.show("Este Cheque Ya ah Sido Asignado ");
				return;
			}
			
		}
		 vista.agregarCheque(cheque);
	}
}
