package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Script;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.administrativo.ContFactura;
import cpc.demeter.controlador.administrativo.ContFacturaProcesar;
import cpc.demeter.controlador.administrativo.ContFacturaSedes;
import cpc.demeter.controlador.administrativo.ContFacturaVieja;
import cpc.demeter.controlador.administrativo.ContRecibo;
import cpc.demeter.controlador.administrativo.ContReintegro;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.demeter.administrativo.Reintegro;
import cpc.negocio.demeter.administrativo.NegocioFactura;
import cpc.negocio.demeter.administrativo.NegocioRecibo;
import cpc.negocio.demeter.administrativo.NegocioReintegro;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContReintegros  extends ContCatalogo<Reintegro> implements EventListener {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContReintegros(AccionFuncionalidad accionesValidas, AppDemeter app) throws SQLException, ExcAccesoInvalido, PropertyVetoException, ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,ExcArgumentoInvalido, ExcFiltroExcepcion
	{
		NegocioReintegro servicios = NegocioReintegro.getInstance();
		this.app = app;
		try {
			
			List lista = servicios.getall();
			dibujar(accionesValidas, "REINTEGROS", lista, this.app);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("ID", 30);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);
  
		titulo = new CompEncabezado("Control", 60);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getControl");
		encabezado.add(titulo);
	
		titulo = new CompEncabezado("Beneficiario ", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCliente");
		encabezado.add(titulo);
		
		
		titulo = new CompEncabezado("Tipo ", 60);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipo");
		encabezado.add(titulo);
		
		
		titulo = new CompEncabezado("Monto ", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMontoTotal");
		encabezado.add(titulo);
		
		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			
			if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR) 
			{
				Reintegro reintegro  = getDatoSeleccionado();
				new ContReintegro(accion,reintegro, this, app);
			}
			else if(accion == Accion.IMPRIMIR_ITEM)
			{
				Reintegro reintegro  = getDatoSeleccionado();

				if(reintegro != null)
				{
					ContReintegro.imprimir(reintegro, app);
				}

			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
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

	@Override
	public List cargarDato(Reintegro dato) {
		// TODO Auto-generated method stub
		return null;
	}


}
