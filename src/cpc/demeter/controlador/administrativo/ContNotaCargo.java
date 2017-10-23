package cpc.demeter.controlador.administrativo;

import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.administrativo.UIFactura;
import cpc.demeter.vista.administrativo.UiCheque;
import cpc.demeter.vista.administrativo.UiNotaCargo;
import cpc.modelo.demeter.administrativo.ConceptoNotaCargo;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.NotaCargo;
import cpc.modelo.demeter.administrativo.ReciboDocumentoFiscal;
import cpc.modelo.demeter.administrativo.ReciboNotaCargo;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.negocio.demeter.administrativo.NegocioCheque;
import cpc.negocio.demeter.administrativo.NegocioNotaCargo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.controlador.ContVentanaMaestroDetalle;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.utilidades.Fecha;

public class ContNotaCargo extends ContVentanaBase<NotaCargo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7771385105156200328L;
	
	private NegocioNotaCargo		servicio;
	private AppDemeter 			app;
	private UiNotaCargo 			vista;
	
	public ContNotaCargo(int modoOperacion, NotaCargo nota, ContCatalogo<NotaCargo> llamador, AppDemeter app) throws ExcDatosInvalidos, ExcFiltroExcepcion{	
		super(modoOperacion);
		this.app=app;
		this.servicio = NegocioNotaCargo.getInstance();
		List<Cliente> clientes = servicio.getClientesProject();
		List<ConceptoNotaCargo> conceptos =servicio.getConceptos();
		Date fechaCierre = servicio.getFechaCierre();
		if (modoOperacion == Accion.AGREGAR) {
			Date fechaservidor = new Date();
			
			String strfechas = Fecha.obtenerFecha(fechaservidor);
			String strfechac  = Fecha.obtenerFecha(fechaCierre );
			
			boolean condicion = strfechac.equals(strfechas);
				if (!condicion)
					throw new ExcDatosInvalidos("No se puede Crear, causa: Fecha de cierre no es igual a la fecha actual ");
			} 
			
			if (modoOperacion == Accion.ANULAR) 
			{
				if (!fechaCierre.equals(nota.getFecha()))
					throw new ExcDatosInvalidos("No se puede anular, causa: Fecha de factura no pertenece al dia a cerrar");
			}
		
		if (modoAgregar()||nota==null) 
			nota=new NotaCargo();
		
		vista = new UiNotaCargo("Nota de Cargos Extras (" + Accion.TEXTO[modoOperacion]
				+ ")", 850, conceptos,clientes);
		setear(nota, vista, llamador, app);
		
		
		
		
		
	};
	

	@Override
	public void onEvent(Event event) throws Exception {
		// TODO Auto-generated method stub
	 if (event.getTarget() == getVista().getAceptar())
		{
			procesarCRUD(event);

		}
		else if (event.getName().equals(Events.ON_SELECT)) 
		{
			if (event.getTarget()==vista.getConcepto()){
				
			}
		} 
		else if (event.getName().equals(CompBuscar.ON_SELECCIONO))
		{
			if (event.getTarget() == vista.getCliente())
			{
				Cliente cliente = servicio.getClienteproject(vista.getCliente().getSeleccion());
				vista.getCliente().setText(cliente.getIdentidadLegal());
				vista.getCliente().setSeleccion(cliente);
				vista.getNombre().setValue(cliente.getNombres()); 
				vista.getTelefonos().setValue(cliente.getStrTelefonos());
			}
		}
		else if (event.getName().equals(Events.ON_CHANGE)){
			if (event.getTarget() == vista.getMonto()){
				vista.getSaldo().setValue(vista.getMonto().getValue());
			}
		}
		
	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
	
		try {
			getVista().actualizarModelo();
			NotaCargo nota = getDato();
			servicio.guardar(nota);
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
		// TODO Auto-generated method stub
		if (vista.getMonto().getValue()<=new Double(0))
		throw new WrongValueException(vista.getMonto(),
				"monto no valido");
		
		if (vista.getCliente().getSeleccion()==null)
		throw new WrongValueException(vista.getCliente(),
				"Seleccione un Cliente");
		if (vista.getConcepto().getSeleccion()==null)
				throw new WrongValueException(vista.getConcepto(),
						"Tipo no valido");
		
	}

	@Override
	public void anular() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		servicio.anular(getDato());
		refrescarCatalogo();
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
	
	public EventListener getDetalleRecibo() {
		// TODO Auto-generated method stub
		return new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				Object obj = vista.getListRecibos().getSelectedItem().getValue();
				if(obj.getClass() == ReciboNotaCargo.class)
				{
					ReciboNotaCargo reciboNotaCargo = (ReciboNotaCargo) obj;
					ContNotaCargo controlador = (ContNotaCargo) vista.getControlador();
					
					ContRecibo contRecibo = new ContRecibo(Accion.CONSULTAR, reciboNotaCargo.getRecibo(),controlador,controlador.app);
				
				}
				
				
			}
		};
	};

}
