package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Script;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.administrativo.ContRecibo;
import cpc.demeter.controlador.administrativo.ContReversoRecibo;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.demeter.administrativo.ReversoRecibo;
import cpc.negocio.demeter.administrativo.NegocioRecibo;
import cpc.negocio.demeter.administrativo.NegocioReversoRecibo;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContReversoRecibos extends ContCatalogo <ReversoRecibo> implements EventListener  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6970759275100714392L;
	/**
	 * 
	 */
	
	private AppDemeter app;

	public ContReversoRecibos(AccionFuncionalidad accionesValidas, AppDemeter app) throws SQLException, ExcAccesoInvalido, PropertyVetoException, ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,ExcArgumentoInvalido, ExcFiltroExcepcion
	{
		NegocioReversoRecibo servicios = new NegocioReversoRecibo();
		this.app = app;
		try {
			
			List lista = servicios.getTodos() ;

			dibujar(accionesValidas, "Reverso De RECIBOS", lista, this.app);
			desactivarTipoReporte();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public ContReversoRecibos(AccionFuncionalidad accionesValidas, AppDemeter app,boolean consulta) throws SQLException, ExcAccesoInvalido, PropertyVetoException, ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,ExcArgumentoInvalido, ExcFiltroExcepcion
	{
		NegocioRecibo servicios = NegocioRecibo.getInstance();
		this.app = app;
		try {
			
		//	List lista = servicios.getTodosProject() ;

			//dibujar(accionesValidas, "RECIBOS", lista, this.app);
			//desactivarTipoReporte();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Nro Reverso", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrNrocontrol");
		encabezado.add(titulo);
 
		titulo = new CompEncabezado("Fecha", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Recibo Afectado", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrReciboAfectado");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Beneficiario", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrPagador");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Ced/Rif", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrIdentidadLegalPagador");
		encabezado.add(titulo);

		/*
*/
		titulo = new CompEncabezado("Monto Reversado", 90);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getMontoReversado");
		encabezado.add(titulo); 

	/*	titulo = new CompEncabezado("Saldo", 90);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getSaldo");
		encabezado.add(titulo);
		*/
		titulo = new CompEncabezado("Estado", 90);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo); 
		
		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {

			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR)
			{
				NegocioReversoRecibo servicio = new NegocioReversoRecibo();
				List<Recibo> recibos = new ArrayList<Recibo>();
				ReversoRecibo dato = getDatoSeleccionado();
				if (accion==Accion.ANULAR && dato.isAnulado()==true)
					throw new ExcDatosInvalidos("No se puede anular, causa: el reverso ya esta anulado");
				if(accion != Accion.AGREGAR)
				{ 
					
				 recibos.add(dato.getReciboAfectado());
				 new ContReversoRecibo(accion, dato, this,this.app,recibos);
				} else{
					
					recibos=servicio.getRecibosActivos();
					new ContReversoRecibo(accion, new ReversoRecibo(), this,this.app,recibos);
					}
				
				
			} 
			else if (accion == Accion.IMPRIMIR_ITEM)
			{
				
			}
			else if (accion == Accion.IMPRIMIR_ITEM	|| accion == Accion.IMPRIMIR_TODO)
			{
				
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
	public List cargarDato(ReversoRecibo dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
