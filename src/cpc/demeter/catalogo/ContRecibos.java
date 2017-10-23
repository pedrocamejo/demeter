package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;





import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Paging;
import org.zkoss.zul.Script;
import org.zkoss.zul.ext.Paginated;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.administrativo.ContRecibo;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.negocio.demeter.administrativo.NegocioRecibo;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContRecibos extends ContCatalogo<Recibo> implements EventListener {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContRecibos(AccionFuncionalidad accionesValidas, AppDemeter app) throws SQLException, ExcAccesoInvalido, PropertyVetoException, ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,ExcArgumentoInvalido, ExcFiltroExcepcion
	{
		NegocioRecibo servicios = NegocioRecibo.getInstance();
		this.app = app;
		try {
			
			List lista = servicios.getTodosProject() ;

			dibujar(accionesValidas, "RECIBOS", lista, this.app);
			desactivarTipoReporte();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	public ContRecibos(AccionFuncionalidad accionesValidas, AppDemeter app,boolean consulta) throws SQLException, ExcAccesoInvalido, PropertyVetoException, ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,ExcArgumentoInvalido, ExcFiltroExcepcion
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

		titulo = new CompEncabezado("Nro Recibo", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getControl");
		encabezado.add(titulo);
 
		titulo = new CompEncabezado("Fecha", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Beneficiario", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrPagador");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Ced/Rif", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrIdentidadLegalPagador");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Concepto", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getConcepto");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto", 90);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getMonto");
		encabezado.add(titulo); 

		titulo = new CompEncabezado("Saldo", 90);
		titulo.setOrdenable(false);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getSaldo");
		encabezado.add(titulo);
		
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
				NegocioRecibo reciboServicio = NegocioRecibo.getInstance();
				
				Recibo recibo = getDatoSeleccionado();
				if(accion != Accion.AGREGAR)
				{
					reciboServicio.setRecibo(recibo);
					recibo = reciboServicio.getRecibo();
				} 
				new ContRecibo(accion, recibo, this,this.app);
				
			} 
			else if (accion == Accion.IMPRIMIR_ITEM)
			{
				NegocioRecibo reciboServicio = NegocioRecibo.getInstance();
				Recibo recibo = getDatoSeleccionado();
				reciboServicio.setRecibo(recibo);
				recibo = reciboServicio.getRecibo();
				ContRecibo.imprimir(recibo, app);
			}
			else if (accion == Accion.IMPRIMIR_ITEM	|| accion == Accion.IMPRIMIR_TODO)
			{
				Script scrip = new Script();
				scrip.setType("text/javascript");
				String icaro = (String) SpringUtil.getBean("icaro");
				StringBuilder cadena = new StringBuilder();
				cadena.append(icaro);
				cadena.append("rpt_clientecomitesaldo");
				cadena.append("&jdbcUrl=");
				cadena.append(SessionDao.getConfiguration().getProperty("hibernate.connection.url"));
				cadena.append("'); ");
				scrip.setContent(cadena.toString());
				app.agregar(scrip);
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
	public List cargarDato(Recibo dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
