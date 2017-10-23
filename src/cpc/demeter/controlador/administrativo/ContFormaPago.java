package cpc.demeter.controlador.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.administrativo.UiDepositoGeneral;
import cpc.demeter.vista.administrativo.UiFormaPago;
import cpc.modelo.demeter.administrativo.Cheque;
import cpc.modelo.demeter.administrativo.FormaPago;
import cpc.modelo.demeter.administrativo.FormaPagoCheque;
import cpc.modelo.demeter.basico.TipoFormaPago;
import cpc.modelo.sigesp.basico.Banco;
import cpc.modelo.sigesp.basico.CuentaBancaria;
import cpc.negocio.demeter.NegocioCuentaBancaria;
import cpc.negocio.demeter.administrativo.NegocioCheque;
import cpc.negocio.demeter.administrativo.NegocioFormaPago;
import cpc.negocio.demeter.administrativo.NegocioRecibo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContFormaPago extends ContVentanaBase<FormaPago> {

	private NegocioFormaPago 		servicio;
	private AppDemeter 				app;
	private UiDepositoGeneral 		vista;
	private ContRecibo				llamador;
	private FormaPago				modelo;
	
	public ContFormaPago(int modoOperacion,ContRecibo llamador, AppDemeter app,FormaPago modelo) throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcFiltroExcepcion
	{
		super(modoOperacion);
		this.app = app;
		this.setModelo(modelo);
		try {
			servicio = NegocioFormaPago.getInstance();
			this.llamador = llamador;
			vista = new UiDepositoGeneral("FORMA PAGO ","750px");
			setear(modoOperacion,modelo, vista, app);
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		}
	}

	private void setear(int modoOperacion,FormaPago obj, UiDepositoGeneral vista , AppDemeter app2) 
	{
		try {
			vista.setModelo(obj);
			vista.setMode("modal");
			vista.setControlador(this);
			vista.inicializar();
			vista.dibujar();
			vista.cargarValores(getDato());
		 	vista.desactivar(modoOperacion);
			app.agregarHija(vista);
			vista.doModal();
		
		} catch (Exception e) {
			e.printStackTrace();
			//throw new ExcDatosInvalidos(e.getMessage());
		}
	}

	public void eliminar() {


	}

	public void guardar() {

		
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

		if(vista.getMonto().getValue() <=  0 )
		{
			throw new WrongValueException(vista.getMonto()," Monto Invalido ");
		}
		
		if(vista.getFecha().getValue() == null )
		{
			throw new WrongValueException(vista.getFecha()," Fecha Invalida  ");
		}

	}

	public void onEvent(Event arg0) throws Exception {
		if(arg0.getTarget() == vista.getAceptar())
		{
			validar();
			FormaPago modelo = (FormaPago) vista.getModelo();
			llamador.agregarFormaPago(modelo);
			vista.detach();
		}
		else if (vista.getCancelar() == arg0.getTarget())
		{
			vista.detach();
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

	public FormaPago getModelo() {
		return modelo;
	}

	public void setModelo(FormaPago modelo) {
		this.modelo = modelo;
	}

	 
 

}
