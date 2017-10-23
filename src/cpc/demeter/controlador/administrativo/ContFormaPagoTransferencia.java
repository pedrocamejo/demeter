package cpc.demeter.controlador.administrativo;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.administrativo.UIFormaPagoTransferencia;
import cpc.modelo.demeter.administrativo.FormaPagoTransferencia;
import cpc.modelo.sigesp.basico.Banco;
import cpc.modelo.sigesp.basico.CuentaBancaria;
import cpc.negocio.demeter.NegocioCuentaBancaria;
import cpc.negocio.demeter.administrativo.NegocioFormaPago;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContFormaPagoTransferencia extends ContVentanaBase<FormaPagoTransferencia> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private NegocioFormaPago 			servicio;
	private AppDemeter 					app;
	private UIFormaPagoTransferencia	vista;
	private ContRecibo					llamador;
	private FormaPagoTransferencia		modelo;
	
	public ContFormaPagoTransferencia(int modoOperacion,ContRecibo llamador, AppDemeter app,FormaPagoTransferencia modelo) throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcFiltroExcepcion
	{
		super(modoOperacion);
		this.app = app;
		this.setModelo(modelo);
		try {
			servicio = NegocioFormaPago.getInstance();
			this.llamador = llamador;
			vista = new UIFormaPagoTransferencia("FORMA PAGO ","750px",servicio.getBancos());
			setear(modoOperacion);
			vista.desactivar(modoOperacion);
			
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		}
	}

	private void setear(int modoOperacion) 
	{
		try {
			vista.setModelo(getModelo());
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
			//throw new ExcDatosInvalidos(e.getMessage());
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

		if(vista.getDocumento().getValue().trim().length() ==  0 )
		{
			throw new WrongValueException(vista.getDocumento()," Ingrese Un Numero de Documento ");
		}
		// Numero Unico Validar 
		if(servicio.getformaPagoUsadaTranferencia(vista.getDocumento().getValue().trim()))
		{
			throw new WrongValueException(vista.getDocumento()," Este Documento Ya ah Sido Utilizado ");
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
			FormaPagoTransferencia modelo = vista.getModelo();
			llamador.agregarFormaPago(modelo);
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
		else if (vista.getCancelar() == arg0.getTarget())
		{
			vista.detach();
		}
			
	}

	public FormaPagoTransferencia getModelo() {
		return modelo;
	}

	public void setModelo(FormaPagoTransferencia modelo) {
		this.modelo = modelo;
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
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		
	}
}
