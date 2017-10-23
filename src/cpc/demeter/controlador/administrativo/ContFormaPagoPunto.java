package cpc.demeter.controlador.administrativo;


import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.demeter.AppDemeter;
import cpc.demeter.vista.administrativo.UIFormaPagoPunto;
import cpc.modelo.demeter.administrativo.FormaPagoPunto;
import cpc.modelo.sigesp.basico.Banco;
import cpc.negocio.demeter.administrativo.NegocioFormaPago;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContFormaPagoPunto extends ContVentanaBase<FormaPagoPunto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private NegocioFormaPago 			servicio;
	private AppDemeter 					app;
	private UIFormaPagoPunto		 	vista;
	private ContRecibo					llamador;
	private FormaPagoPunto				modelo;
	
	public ContFormaPagoPunto(int modoOperacion,ContRecibo llamador, AppDemeter app,FormaPagoPunto modelo) throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcFiltroExcepcion
	{
		super(modoOperacion);
		this.app = app;
		this.setModelo(modelo);
		try {
			servicio = NegocioFormaPago.getInstance();
			this.llamador = llamador;
			vista = new UIFormaPagoPunto("FORMA PAGO PUNTO DE VENTA ","750px",servicio.getBancos());
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

		if(vista.getNrotarjeta().getValue().trim().length() ==  0 )
		{
			throw new WrongValueException(vista.getNrotarjeta()," Ingrese Un Numero de la tarjeta ");
		}
		if(vista.getDocumento().getValue() == null )
		{
			throw new WrongValueException(vista.getDocumento()," Ingrese Un Numero de documento  ");
		}
		if(vista.getFecha().getValue() == null )
		{
			throw new WrongValueException(vista.getFecha()," Fecha Invalida  ");
		}
		if(vista.getBanco().getSelectedItem() == null)
		{
			throw new WrongValueException(vista.getBanco(),"Seleccione un Banco ");
		}
		if (vista.getTipo().getSelectedItem()==null)
		{
			throw new WrongValueException(vista.getTipo(),"Seleccione un Tipo ");
		}
		if(vista.getMonto().getValue() <=  0 )
		{
			throw new WrongValueException(vista.getMonto()," Monto Invalido ");
		}

		if (vista.getDocumento().getValue().trim().length() >  0 && vista.getNrotarjeta().getValue().trim().length() >  0 )
		{
			String documento = vista.getDocumento().getValue().trim();
			String nrotarjeta = vista.getNrotarjeta().getValue().trim();
			if(servicio.getFormaPagoPuntoActiva(nrotarjeta,documento) != null)
			{
				throw new WrongValueException(vista,"el numero de documento y el numero de tarjeta ya han sido registradas en otro recibo.");
			}
		}
	}

	public void onEvent(Event arg0) throws Exception {
	
		if(arg0.getTarget() == vista.getAceptar())
		{
			validar();
			FormaPagoPunto modelo = vista.getModelo();
			llamador.agregarFormaPago(modelo);
			vista.detach();
		}
		else if (vista.getCancelar() == arg0.getTarget())
		{
			vista.detach();
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

	public FormaPagoPunto getModelo() {
		return modelo;
	}

	public void setModelo(FormaPagoPunto modelo) {
		this.modelo = modelo;
	}

	
}
