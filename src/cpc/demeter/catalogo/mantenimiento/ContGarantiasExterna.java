package cpc.demeter.catalogo.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.mantenimiento.ContGarantiaExterna;
import cpc.modelo.demeter.mantenimiento.Garantia;
import cpc.negocio.demeter.mantenimiento.NegocioGarantia;
import cpc.negocio.demeter.mantenimiento.NegocioLote;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContGarantiasExterna extends ContCatalogo<Garantia> {

	private static final long serialVersionUID = -5149724463777733770L;
	private AppDemeter app;

	public ContGarantiasExterna() {
		super();
	}

	public ContGarantiasExterna(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioGarantia negocioGarantia = NegocioGarantia.getInstance();
		this.app = app;
		List<Garantia> lista = negocioGarantia.getGarantias();
		dibujar(accionesValidas, "GARANTIAS    ",
				negocioGarantia.getGarantias(), app);

	}

	public ContGarantiasExterna(int modoOperacion) {
		super(modoOperacion);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Id", 20);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Maquinaria", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMaquinaria");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Propietario  ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrPropietario");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estatus", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getEstatusgarantia");
		encabezado.add(titulo);

		return encabezado;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(Garantia arg0) {

		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {

		return null;
	}

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		int accion = (Integer) event.getTarget().getAttribute("pos");
		NegocioLote negocio = NegocioLote.getInstance();

		if (accion <= Accion.CONSULTAR) 
		{
			Garantia garantia = getDatoSeleccionado();
		
			if (garantia == null && accion != Accion.AGREGAR)
			{
				app.mostrarError("Debe seleccionar un dato del catalago");
			}
			else
			{  
				Integer estatus = (garantia == null? 0:garantia.getEstatus());
				if(estatus != 0   && accion == Accion.EDITAR )
				{
					app.mostrarError("No Se puede Editar esta Garantia ya ah sido Generada ");
				}
				else
				{
					new ContGarantiaExterna(accion, garantia, this, app);
				}
			}
		}
		else if(accion == Accion.ANULAR)
		{
				//se debe quedar la Maquinaria alli 
			Garantia garantia = getDatoSeleccionado();
			
			if (garantia == null) 
			{
				app.mostrarError("Debe seleccionar un dato del catalago");
			}
			else
			{
				if (garantia.getEstatus() == 0 )
				{
					new ContGarantiaExterna(accion, garantia, this, app);
				}
				else
				{
					app.mostrarError("No Puedes Anular Esta Garantia Dado su estado "+garantia.getEstatusgarantia());
				}
			}
		}
		else if (accion == Accion.PROCESAR)
		{
			Garantia garantia = getDatoSeleccionado();
		
			if (garantia == null) 
			{
				app.mostrarError("Debe seleccionar un dato del catalago");
			}
			else
			{
				if (garantia.getEstatus() == 0 )
				{
					new ContGarantiaExterna(accion, garantia, this, app);
				}
				else
				{
					app.mostrarError("Esta Garantia no puede ser Activada Dado su estado "+garantia.getEstatusgarantia());
				}
			}
		} 
		else if (accion == Accion.IMPRIMIR_ITEM)
		{
			Garantia garantia = getDatoSeleccionado();
			if (garantia == null) 
			{
				app.mostrarError("Debe seleccionar un dato del catalago");
			}
			else 
			{
				new ContGarantiaExterna(accion, garantia, this, app);
			}
		}

	}

}
