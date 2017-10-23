package cpc.demeter.controlador.mantenimiento;

import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.mantenimiento.ContMantntosMaquinaria;
import cpc.demeter.vista.mantenimiento.UiMantenimientoMaquinaria;
import cpc.modelo.demeter.mantenimiento.MantenimientoMaquinaria;
import cpc.modelo.demeter.mantenimiento.PlanMantenimiento;
import cpc.modelo.demeter.mantenimiento.TipoGarantia;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.negocio.demeter.mantenimiento.NegocioMantenimientoMaquinaria;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContMantenimientoMaquinaria extends
		ContVentanaBase<MantenimientoMaquinaria> {

	private static final long serialVersionUID = 6184414588153046382L;

	private NegocioMantenimientoMaquinaria servicio;
	private AppDemeter app;
	private UiMantenimientoMaquinaria vista;

	public ContMantenimientoMaquinaria(int operacion,MantenimientoMaquinaria mntntoMaquinaria,ContMantntosMaquinaria llamador, AppDemeter app)
	throws SuspendNotAllowedException, InterruptedException, ExcFiltroExcepcion, ExcDatosInvalidos
	{
		super(operacion);
		servicio = NegocioMantenimientoMaquinaria.getInstance();
		setDato(mntntoMaquinaria);
		this.app = app;
		if (operacion == Accion.AGREGAR) {
			mntntoMaquinaria = new MantenimientoMaquinaria();
			mntntoMaquinaria.setPlanMaquinaria(new PlanMantenimiento());
		}

		servicio = NegocioMantenimientoMaquinaria.getInstance();

		vista = new UiMantenimientoMaquinaria("Mantenimiento Maquinaria ("+ Accion.TEXTO[operacion] + ")", 800);
		vista.setModoOperacion(getModoOperacion());
		vista.setMaximizable(true);
		vista.setListModelo(servicio.modeloConTipoGarantia());

		setear(mntntoMaquinaria, vista,(ICompCatalogo<MantenimientoMaquinaria>) llamador, app);
		this.vista = (UiMantenimientoMaquinaria) getVista();
		vista.desactivar(getModoOperacion());
	}

	public void guardar() {
		try {
			// getDato().getPlan_Maquinaria().getPiezas().add(vista.getPlanMantenimientoSeleccionado());
			servicio.Guardar(getDato());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub
		if (vista.getComb_modelos() == arg0.getTarget()) 
		{
			Modelo modelo = vista.getdatoselecionado();
			if (modelo != null) 
			{
				TipoGarantia tg = servicio.getTipoGarantia(modelo);
				if (tg != null)
				{
					getDato().setTipogarantia(tg);
					getDato().getPlanMaquinaria().setDescripcion(tg.getModelo().toString());
					vista.mostrarDesglose();
				}
			}
		} 
		else if (vista.getDetallePieza() == arg0.getTarget())
		{
			PlanMantenimiento plan = vista.getPlanMantenimientoSeleccionado();
			if (plan != null)
			{  
				new ContPlanMantenimiento(Accion.CONSULTAR,vista.getPlanMantenimientoSeleccionado(), app, this,getDato().getTipogarantia());
			} 
			else
			{
				app.mostrarError("Debe Seleccionar un items");
			}
		} 
		else if (vista.getAgregarPieza() == arg0.getTarget())
		{
			PlanMantenimiento plan = vista.getPlanMantenimientoSeleccionado();
			if (plan != null) 
			{
				new ContPlanMantenimiento(Accion.AGREGAR, null, app, this,		getDato().getTipogarantia());
			} 
			else
			{
				app.mostrarError("Debe Seleccionar un items");
			}
		} 
		else if (vista.getQuitarPieza() == arg0.getTarget())
		{
			PlanMantenimiento plan = vista.getPlanMantenimientoSeleccionado();
			if (plan != null)
			{
				if (plan != getDato().getPlanMaquinaria())
				{
					PlanMantenimiento padre = plan.getPadre();
					padre.getPiezas().remove(plan);
					plan.setPadre(null);
					// borrrar sii esta bn :-d
					vista.getTree_planMaquinaria().getSelectedItem().detach();
				}
				else
				{
					app.mostrarError("No Puedes Quitar la Maquinaria Padre ");
				}
			} 
			else 
			{
				app.mostrarError("Debe Seleccionar un items");
			}
		} 
		else if (vista.getEditarPieza() == arg0.getTarget())
		{
			PlanMantenimiento plan = vista.getPlanMantenimientoSeleccionado();
			if (plan != null)
			{
				new ContPlanMantenimiento(Accion.EDITAR,vista.getPlanMantenimientoSeleccionado(), app, this,getDato().getTipogarantia());
			}
			else 
			{
				app.mostrarError("Debe Seleccionar un items");
			}
		} 
		else if (vista.getCancelar() == arg0.getTarget())
		{
			vista.detach();
		} 
		else {
			procesarCRUD(arg0);
		}
	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		try {
			servicio.Eliminar(getDato());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

	}

	public void actualizarDesglose() {
		vista.mostrarDesglose();
	}

	public void agregarPieza(PlanMantenimiento dato) 
	{
		// TODO Auto-generated method stub
		PlanMantenimiento padre = vista.getPlanMantenimientoSeleccionado();
		if (padre != null) 
		{
			dato.setPadre(padre);
			padre.getPiezas().add(dato);
			vista.addHijo(padre, dato);
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
}
