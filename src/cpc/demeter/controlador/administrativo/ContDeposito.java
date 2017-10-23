package cpc.demeter.controlador.administrativo;
 

import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.administrativo.UIDeposito;
import cpc.modelo.demeter.administrativo.Deposito;
import cpc.modelo.demeter.administrativo.FormaPago;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.sigesp.basico.CuentaBancaria;
import cpc.negocio.demeter.administrativo.NegocioDeposito;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContDeposito extends ContVentanaBase<Deposito> {

	private static final long serialVersionUID = -4005176298792986376L;
	private NegocioDeposito servicio;
	private UIDeposito      vistaDeposito;
	private AppDemeter      app;
	
	public ContDeposito(int modoOperacion, Deposito deposito,ContCatalogo<Deposito> llamador, AppDemeter app) throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido, ExcFiltroExcepcion {
		super(modoOperacion);
		
		this.app = app;
		servicio = NegocioDeposito.getInstance();

		List<CuentaBancaria> cuentas = servicio.getCuentasbancarias();
		
		if (modoAgregar())
		{
			deposito = new Deposito();
			deposito.setFecha(servicio.getFechaCierre());
		}

		vistaDeposito = new UIDeposito("DEPOSITO (" + Accion.TEXTO[modoOperacion] + ")",820,cuentas,modoOperacion);
		setear(deposito,vistaDeposito, llamador,this.app);
		vistaDeposito = (UIDeposito) getVista();
		vistaDeposito.desactivar(modoOperacion);
	}

	public void eliminar() {
		servicio.eliminar(getDato());
	}

	public void guardar() {
		
		if(modoAgregar())
		{
			List formaPagos = vistaDeposito.getModelos();
			try {
				servicio.guardar(getDato(),formaPagos);

			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (vistaDeposito.getCuenta().getSeleccion() == null)
		{
			throw new WrongValueException(vistaDeposito.getCuenta(),"Seleccione la Cuenta");
		}
		if (vistaDeposito.getNroDeposito().getValue().trim().length() == 0 )
		{
			throw new WrongValueException(vistaDeposito.getNroDeposito(),"Indique el Numero Deposito");
		}
		if (vistaDeposito.getFecha().getValue() == null )
		{
			throw new WrongValueException(vistaDeposito.getFecha(),"Indique la Fecha del Deposito");
		}
		if(getModoOperacion() == Accion.AGREGAR)
		{
			if (vistaDeposito.getModelos().size() == 0 )
			{
				throw new WrongValueException(vistaDeposito.getDetalle(),"Ingrese un Item");
			}
		}
	}

	private void refrescarLabel(CompBuscar<CuentaBancaria> cuenta) {
		if (cuenta.getSeleccion() != null) {
			vistaDeposito.getBanco().setValue(cuenta.getSeleccion().getStrBanco());
			vistaDeposito.getTipocuenta().setValue(cuenta.getSeleccion().getStrTipoCuenta());
		}
	}

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		if (event.getTarget() == getVista().getAceptar())
		{
			procesarCRUD(event);
		}
		else if (event.getName().equals(CompBuscar.ON_SELECCIONO))
		{
			if (event.getTarget() == vistaDeposito.getCuenta())
			{
					refrescarLabel((CompBuscar<CuentaBancaria>) event.getTarget()); //por q hacer cosas de la vista en el controlador ?
			}
		}
		else if(vistaDeposito.getAgregar() == event.getTarget())
		{
			new ContSeleccionarFormaPago(this.getModoOperacion(),this,this.app);
		}
		else if(vistaDeposito.getQuitar() == event.getTarget())
		{
			if(vistaDeposito.getDetalle().getSelectedItem() == null)
			{
				this.app.mostrarError("Seleccione un Item ");
			}
			else
			{
				FormaPago pago = (FormaPago) vistaDeposito.getDetalle().getSelectedItem().getValue();
				vistaDeposito.quitarFormaPago(pago);
			}
		}
	}


	public void agregar_FormaPago(FormaPago pago)
	{
		vistaDeposito.agregarFormaPago(pago);
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
