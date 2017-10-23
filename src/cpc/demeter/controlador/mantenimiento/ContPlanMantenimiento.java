package cpc.demeter.controlador.mantenimiento;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event; 
import org.zkoss.zul.Messagebox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.mantenimiento.UIPlanMantenimiento;

import cpc.modelo.demeter.mantenimiento.Actividad;
import cpc.modelo.demeter.mantenimiento.PlanMantenimiento;
import cpc.modelo.demeter.mantenimiento.TipoGarantia;
import cpc.negocio.demeter.mantenimiento.NegocioMantenimientoMaquinaria;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContPlanMantenimiento extends ContVentanaBase<PlanMantenimiento> {

	private static final long serialVersionUID = 1L;
	private UIPlanMantenimiento 				vista;
	private NegocioMantenimientoMaquinaria 		servicio;
	private AppDemeter 							app;
	private ContMantenimientoMaquinaria 		llamador;
	private TipoGarantia 						tipoGarantia;

	@SuppressWarnings("unused")
	public ContPlanMantenimiento(int i, PlanMantenimiento dato,	AppDemeter app, ContMantenimientoMaquinaria llamador,	TipoGarantia tg) throws InterruptedException, ExcDatosInvalidos {
		super(i);
		this.tipoGarantia = tg;
		this.app = app;
		this.llamador = llamador;
		
		if (i == Accion.AGREGAR)
		{
			dato = new PlanMantenimiento();
		}
		this.setDato(dato);
		vista = new UIPlanMantenimiento((dato.getDescripcion() == null ? "PLAN DE MANTENIMIENTO": dato.getDescripcion()), 800);
		vista.setClosable(true);
		vista.setMaximizable(true);

		this.vista.setMode("modal");
		this.vista.setModelo(dato);
		this.vista.setControlador(this);
		this.vista.dibujarVentana();
		this.vista.cargar();
		this.vista.deshabilitar(i);
		
		app.agregarHija(this.vista);

	}

	@Override
	public void onEvent(Event arg0) throws Exception {

		if (llamador != null) 
		{
			if (vista.getAceptar() == arg0.getTarget()) 
			{
				vista.actualizarModelo();
				validar();
				vista.getModelo().setActividades(vista.getActividad());
				if (modoAgregar()) 
				{
					llamador.agregarPieza(getDato());
				}
				else if (modoEditar())
				{
					llamador.actualizarDesglose();
				}
				vista.detach();
			} 
			else if (arg0.getTarget() == vista.getAgregarActividad())
			{
				Actividad actividad = new Actividad();
				new ContActividad(Accion.AGREGAR, actividad, this, app);
			}
			else if (arg0.getTarget() == vista.getEditarActividad())
			{
				Actividad actividad = vista.getSelectActividad();
				if (actividad == null)
				{
					app.mostrarError("Debe Seleccionar 1 items");
				} 
				else
				{
					new ContActividad(Accion.EDITAR, actividad, this, app);
				}
			} 
			else if (arg0.getTarget() == vista.getDetalleActividad())
			{
				Actividad actividad = vista.getSelectActividad();
			
				if (actividad == null)
				{
					app.mostrarError("Debe Seleccionar 1 items");
				} 
				else
				{
					new ContActividad(Accion.CONSULTAR, actividad, this, app);
				}
			} 
			else if (arg0.getTarget() == vista.getQuitarActividad()) 
			{
				Actividad actividad = vista.getSelectActividad();

				if (actividad == null) 
				{
					app.mostrarError("Debe Seleccionar 1 items");
				} 
				else 
				{
					int i = Messagebox.show("Deseas Quitar Este Item ?","Advertencia", Messagebox.YES | Messagebox.NO,Messagebox.INFORMATION);
					if (i == Messagebox.YES) 
					{
						getDato().getActividades().remove(actividad);
						vista.actualizarActividades();
					}
				}
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

		if (vista.getDescripcion().getValue().length() == 0)
			throw new WrongValueException(vista.getDescripcion(),
					"Debe llenar este campo");

	}

	public void setActividad(Actividad dato) {

		vista.AgregarActividad(dato);
		// TODO Auto-generated method stub

	}

	public void setEditarActividad() {
		// TODO Auto-generated method stub
		vista.actualizarActividades();
	}

	public TipoGarantia getTipoGarantia() {
		return tipoGarantia;
	}

	public void setTipoGarantia(TipoGarantia tipoGarantia) {
		this.tipoGarantia = tipoGarantia;
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
