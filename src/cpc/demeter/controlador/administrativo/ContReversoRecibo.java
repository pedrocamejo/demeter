package cpc.demeter.controlador.administrativo;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.event.ListDataListener;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.Servicios;
import cpc.demeter.vista.administrativo.UIRecibo;
import cpc.demeter.vista.administrativo.UIReversoRecibo;
import cpc.demeter.vista.administrativo.UISeleccionFormaPago;
import cpc.demeter.vista.transporte.UIDestino;
import cpc.modelo.demeter.administrativo.FormaPago;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.demeter.administrativo.ReversoFormaPago;
import cpc.modelo.demeter.administrativo.ReversoRecibo;
import cpc.negocio.demeter.administrativo.NegocioRecibo;
import cpc.negocio.demeter.administrativo.NegocioReversoRecibo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cpc.zk.componente.ventanas.CompVentanaEntrada;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContReversoRecibo extends ContVentanaBase<ReversoRecibo> {
	
	

	private UIReversoRecibo 				 vista;
	private AppDemeter app;
	private CompVentanaEntrada vistaCodigo =  new CompVentanaEntrada(this);


	public ContReversoRecibo(int modoOperacion, ReversoRecibo reversoRecibo,	ContCatalogo<ReversoRecibo> llamador, AppDemeter app,List<Recibo> recibos) throws ExcDatosInvalidos {
		super(modoOperacion);
		this.app=app;
	//	setDato(reversoRecibo);
		setear(reversoRecibo,new UIReversoRecibo("REVERSO RECIBOS", 850, recibos), llamador, this.app);
		vista = (UIReversoRecibo) getVista();
		if (modoOperacion !=Accion.AGREGAR){
			vista.activarConsulta();
		}
		if (modoOperacion ==Accion.AGREGAR||modoOperacion ==Accion.ANULAR){
			vista.getAceptar().setDisabled(true);
			vista.getBotonAutorizar().setVisible(true);
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 3598357256768366849L;

	@Override
	public void onEvent(Event arg0) throws Exception {
		System.out.println(arg0.getName());
		 if (arg0.getTarget() == vistaCodigo.getAceptar()) 
		{ 
			if (codigoValido(vistaCodigo.getEntrada(),vistaCodigo.gettextopregunta()))
			{
			vista.getAceptar().setDisabled(false);
				vistaCodigo.close();
				Events.postEvent("onClick", vista.getAceptar(), null);
				
				System.out.println("ok");
				
			}
		
			
		}
		 if (arg0.getTarget() == vista.getBotonAutorizar()){
			  validar();
				mostrarvistaCodigo(app);
			} 
		if (vista.getRecibo()==arg0.getTarget())
			actualizarRecibo();
		
		 else
		   {
		
		   	    procesarCRUD(arg0);
		   }
	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		
		NegocioReversoRecibo servicio = new NegocioReversoRecibo();
		ReversoRecibo	reverso =getDato();
		reverso.setReciboAfectado(vista.getRecibo().getSeleccion());
		reverso.setMontoReversado(vista.getMontoRecibo().getValue());
		reverso.setObservacion(vista.getObservacion().getValue());
		reverso.setFecha(new Date());
		
		List<ReversoFormaPago> reversoFormaPagos  = new ArrayList<ReversoFormaPago>();
		for (FormaPago fPago : reverso.getReciboAfectado().getFormaspago()) {
			
		ReversoFormaPago reversoForma = new ReversoFormaPago();
		reversoForma.setFormaPago(fPago);
		reversoForma.setMonto(fPago.getMonto());
		
		reversoForma.setEstado(true);
		reversoForma.setReversoRecibo(reverso);
		reversoFormaPagos.add(reversoForma);
		}
		reverso.setReversoFormaPagos(reversoFormaPagos);
		try {
			servicio.guardar(reverso);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		
		if (vista.getRecibo().getSeleccion()==null)
			throw new  WrongValueException(vista.getRecibo() ,"Indique un recibo a reversar");
		if (vista.getObservacion().getValue().isEmpty())
		{			
			throw new  WrongValueException(vista.getObservacion() ,"Indique un Motivo de reverso o observacion");
		}	
		
	
		
		
	}

	@Override
	public void anular() throws ExcFiltroExcepcion {
		ReversoRecibo reversoRecibo=getDato();
	new NegocioReversoRecibo().anular(reversoRecibo);
		vista.close();
	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		
	}
	

	public void actualizarRecibo(){
		Recibo recibo = vista.getRecibo().getSeleccion();
		
		vista.getRecibo().setText(recibo.getControl());
		vista.getCliente().setValue(recibo.getStrPagador());
		vista.getCedulaRif().setValue(recibo.getStrIdentidadLegalPagador());
		vista.getMontoRecibo().setValue(recibo.getMonto());
		
		
		List<FormaPago> formaPago = recibo.getFormaspago();
		
		List<ReversoFormaPago> reversoFormaPagos  = new ArrayList<ReversoFormaPago>();
		for (FormaPago fPago : formaPago) {
			
		ReversoFormaPago reversoForma = new ReversoFormaPago();
		reversoForma.setFormaPago(fPago);
		reversoForma.setMonto(fPago.getMonto());
		//reversoForma.setReversoRecibo(getDato());
		reversoFormaPagos.add(reversoForma);	
		}
		if (modoAgregar()){
	
			vista.getListReversoFormaPago().setModel(new ListModelList(reversoFormaPagos));
		}
		
	
		
	}

	public void mostrarvistaCodigo(AppDemeter app) {
		vistaCodigo = new CompVentanaEntrada(this);
		Servicios servicio = new Servicios(app);
		vistaCodigo
				.setTitle("introduzca el codigo correspondiente a este numero");
		vistaCodigo.getGbGeneral().getLblTitulo()
				.setValue("introduzca el codigo correspondiente a este numero");
		vistaCodigo.settextopregunta(servicio.generarsemilla().toString());
		try {
			System.out.println(servicio.generarHash(vistaCodigo
					.gettextopregunta().getValue()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		app.agregarHija(vistaCodigo);
	}
	
	public boolean codigoValido(Textbox txtNroControl, Label codigo)
			throws InterruptedException {
		Servicios servicio = new Servicios(app);
		try {
			if (txtNroControl.getText().trim().isEmpty()) {
				app.mostrarError("El nro de control no puede estar vacio");
				vistaCodigo.getEntrada().setFocus(true);
				return false;
			} else if (!txtNroControl.getText().equals(
					servicio.generarHash(codigo.getValue()))) {
				Messagebox.show("El codigo no coincide", "Error",
						Messagebox.OK, Messagebox.ERROR);
				return false;
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			Messagebox.show("El texto debe estar en formato numérico", "Error",
					Messagebox.OK, Messagebox.ERROR);
			return false;
		}
	}

}
