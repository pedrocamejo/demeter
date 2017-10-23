package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.administrativo.ContDeposito;
import cpc.demeter.controlador.administrativo.ContRecibo;
import cpc.modelo.demeter.administrativo.Deposito;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.negocio.demeter.administrativo.NegocioDeposito;
import cpc.negocio.demeter.administrativo.NegocioFormaPago;
import cpc.negocio.demeter.administrativo.NegocioRecibo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContChequesRecibos extends ContCatalogo<Object> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;
	private AccionFuncionalidad acciones;
	

	public class ListaCatalogo{
		private  String 	clienteNombre;
		private  Integer	esCheque;
		private  boolean	reciboAnulado;
		private  String 	clienteIdLegal;
		private  String		chequesNroCheque;
		private  Double	reciboMonto;
		private  String		chequeNroCheque;
		private  String		reciboConcepto;
		private  String		clienteDireccion;
		private  java.util.Date 	reciboFecha;
		private  Long	reciboID;
		private  Integer	formaChequeID;
		private  Integer	formaDepositoId;
		private  Integer	chequesId;
		private  String		reciboControl;
		private  Double	reciboSaldo;
		private  Integer	clienteID;
		private  Integer	chequeId;
		private  Integer	esDeposito;
		
		public ListaCatalogo(Object object) {
			
			super();
			HashMap<String, Object>mapa =(HashMap<String, Object>) object;
			
			this.clienteNombre = (String) mapa.get("clienteNombre");
			this.esCheque = (Integer) mapa.get("esCheque");
			this.reciboAnulado = (Boolean) mapa.get("reciboAnulado");
			this.clienteIdLegal = (String) mapa.get("clienteIdLegal");
			this.chequesNroCheque = (String) mapa.get("chequesNroCheque");
			this.reciboMonto = (Double) mapa.get("reciboMonto");
			this.chequeNroCheque = (String) mapa.get("chequeNroCheque");
			this.reciboConcepto = (String) mapa.get("reciboConcepto");
			this.clienteDireccion = (String) mapa.get("clienteDireccion");
			this.reciboFecha = ((java.sql.Date) mapa.get("reciboFecha"));
			this.reciboID = (Long) mapa.get("reciboID");
			this.formaChequeID = (Integer) mapa.get("formaChequeID");
			this.formaDepositoId = (Integer) mapa.get("formaDepositoId");
			this.chequesId = (Integer) mapa.get("chequesId");
			this.reciboControl = (String) mapa.get("reciboControl");
			this.reciboSaldo = (Double) mapa.get("reciboSaldo");
			this.clienteID = (Integer) mapa.get("clienteID");
			this.chequeId = (Integer) mapa.get("chequeId");
			this.esDeposito = (Integer) mapa.get("esDeposito");
			
			
			
		}

		public String getClienteNombre() {
			return clienteNombre;
		}

		public void setClienteNombre(String clienteNombre) {
			this.clienteNombre = clienteNombre;
		}

		public String getClienteIdLegal() {
			return clienteIdLegal;
		}

		public void setClienteIdLegal(String clienteIdLegal) {
			this.clienteIdLegal = clienteIdLegal;
		}

		public Double getReciboMonto() {
			return reciboMonto;
		}

		public void setReciboMonto(Double reciboMonto) {
			this.reciboMonto = reciboMonto;
		}

		public Long getReciboID() {
			return reciboID;
		}

		public void setReciboID(Long reciboID) {
			this.reciboID = reciboID;
		}

		public String getReciboControl() {
			return reciboControl;
		}

		public void setReciboControl(String reciboControl) {
			this.reciboControl = reciboControl;
		}

		public Double getReciboSaldo() {
			return reciboSaldo;
		}

		public void setReciboSaldo(Double reciboSaldo) {
			this.reciboSaldo = reciboSaldo;
		}

		public String getTipo(){
			if (esCheque!=null)
				return  "Cheque";
			if (esDeposito !=null)
				return "Cheque en Deposito";
			return "";
			
		}
		
		public String getCheque(){
			if (esCheque!=null)
				return  chequeNroCheque;
			if (esDeposito !=null)
				return chequesNroCheque;
			return "";
			
		}
        
    };
	public ContChequesRecibos(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioFormaPago servicios = NegocioFormaPago.getInstance();
		List<HashMap<String, Object>> listaObjetos = servicios.getChequesRecibos();
		
		List<Object> chequesRecibos = new ArrayList<Object>();
		
for (Object object : listaObjetos) {
		chequesRecibos.add(this.getListaCatalogo(object));	
		}
		
		this.app = app;
		this.acciones = accionesValidas;
		dibujar(accionesValidas, "CHEQUES RECIBO",chequesRecibos ,
				app);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		titulo = new CompEncabezado("recibo", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getReciboControl");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Cliente", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getClienteNombre");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Idlegal", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getClienteIdLegal");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("ReciboMonto", 180);
		titulo.setMetodoBinder("getReciboMonto");
		titulo.setOrdenable(true);
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("ReciboSaldo", 180);
		titulo.setMetodoBinder("getReciboSaldo");
		titulo.setOrdenable(true);
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Tipo", 150);
		titulo.setMetodoBinder("getTipo");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cheque", 90);
		titulo.setMetodoBinder("getCheque");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		
		return encabezado;

	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			NegocioRecibo reciboServicio = NegocioRecibo.getInstance();
			Recibo reciboaux = new Recibo();
			Long id = ((ListaCatalogo)getDatoSeleccionado()).reciboID;
			reciboaux.setId(id);
			Recibo recibo = reciboServicio.getReciboProject(reciboaux);
			if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR)
			{
				
				if(accion != Accion.AGREGAR)
				{
					
				} 
				
				ContCatalogo<Recibo> llamador = new ContRecibos(acciones, app,true);
				new ContRecibo(accion, recibo,llamador,this.app);
				
			} 
			else if (accion == Accion.IMPRIMIR_ITEM)
			{
				
				
				ContRecibo.imprimir(recibo, app);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(Object dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}
	public ListaCatalogo getListaCatalogo(Object object) {
        return new ListaCatalogo(object);
        
    }
}
