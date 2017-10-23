package cpc.demeter.controlador.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.administrativo.UISeleccionarDocumentoFiscal;
import cpc.demeter.vista.mantenimiento.garantia.UISeleccionarCliente;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.negocio.demeter.administrativo.NegocioDocumentoFiscal;
import cpc.negocio.demeter.administrativo.NegocioFactura;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContSeleccionarDocumentoFiscal  extends ContVentanaBase<DocumentoFiscal> {

	
	private UISeleccionarDocumentoFiscal 		vista;
	private NegocioDocumentoFiscal	 			servicio;
	private IZkAplicacion 						app;
	private Cliente 							cliente;
	private List<DocumentoFiscal> 				documentos;
	private ContRecibo							contRecibo;
	
	
	public ContSeleccionarDocumentoFiscal(int i, ContRecibo contRecibo,AppDemeter app, Cliente cliente) throws SuspendNotAllowedException, InterruptedException, ExcDatosInvalidos, ExcFiltroExcepcion {
		// TODO Auto-generated constructor stub
		super(i);
		this.app = app;
		this.cliente = cliente;
		this.contRecibo = contRecibo;
		servicio = NegocioDocumentoFiscal.getInstance();
		documentos = servicio.getDocumentosFiscales(cliente);
		vista = new UISeleccionarDocumentoFiscal("Selecciónar Documento Fiscal ",850,documentos,this,app);
		vista.modoconsulta(Accion.CONSULTAR);
		app.agregarHija(vista);

		
	}

	@Override
	public void onEvent(Event arg0) throws Exception, WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		
		
		if(arg0.getTarget() == vista.getBuscar())
		{
			vista.buscarCliente();
		}
		else if(arg0.getTarget() == vista.getSeleccionar())
		{
			
			if(vista.getDocumentosFiscales().getSelectedItem() == null)
			{
				Messagebox.show("Seleccione al menos 1 Item");
			}
			else
			{
				DocumentoFiscal documento =  (DocumentoFiscal) vista.getDocumentosFiscales().getSelectedItem().getValue();
				//contRecibo.addDocumentoFiscal(documento);
				vista.detach();
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

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
