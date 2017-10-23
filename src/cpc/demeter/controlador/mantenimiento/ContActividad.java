package cpc.demeter.controlador.mantenimiento;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Groupbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Window;

import cpc.demeter.AppDemeter;
import cpc.demeter.vista.mantenimiento.UIActividad;
import cpc.modelo.demeter.mantenimiento.Actividad;
import cpc.modelo.demeter.mantenimiento.DetalleGarantina;
import cpc.modelo.demeter.mantenimiento.Periodicidad;
import cpc.negocio.demeter.mantenimiento.NegocioMantenimientoMaquinaria;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContActividad extends ContVentanaBase<Actividad> {

	private NegocioMantenimientoMaquinaria servicio;

	private UIActividad vista;
	private AppDemeter app;
	private ContPlanMantenimiento llamador;
	private ContOrdenGarantia llamador2;

	public ContActividad(int i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	public ContActividad(int modoOperacion, Actividad actividad,ContPlanMantenimiento llamador, AppDemeter app) 	throws InterruptedException, ExcDatosInvalidos 
	{
		// TODO Auto-generated constructor stub
		super(modoOperacion);
		servicio = NegocioMantenimientoMaquinaria.getInstance();
		this.llamador = llamador;
		setDato(actividad);
		vista = new UIActividad("Actividades", 700);
		this.vista.setMode("modal");
		vista.setModelo(actividad);
		vista.setControlador(this);
		vista.dibujarVentana();
		vista.cargar();
		vista.modoOperacion(modoOperacion);
		this.app = app;
		app.agregarHija(vista);

	}

	public ContActividad(int modoOperacion, Actividad actividad,ContOrdenGarantia llamador, AppDemeter app) throws InterruptedException, ExcDatosInvalidos 
	{
		super(modoOperacion);
		this.llamador2 = llamador;
		setDato(actividad);
		vista = new UIActividad("Actividades", 700);
		this.vista.setMode("modal");
		vista.setModelo(actividad);
		vista.setControlador(this);
		vista.dibujarVentana();
		vista.cargar();
		vista.modoOperacion(modoOperacion);
		this.app = app;
		app.agregarHija(vista);
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub

		if (vista.getAddPeriodicidad() == arg0.getTarget()) 
		{
			final Window wind = new Window("Periodicidad", "normal", true);
			wind.setWidth("600px");
			final Listbox lista = new Listbox();
			Button seleccionar = new Button("Seleccionar");
			final Intbox frecuencia = new Intbox();
			seleccionar.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener() 
			{
				@Override
				public void onEvent(Event arg0) throws Exception
				{
					if (frecuencia.getValue() != null) {
						if (lista.getSelectedItem() != null) {
							Periodicidad p = new Periodicidad();
							p.setMedida(frecuencia.getValue());
							p.setActividad(getDato());
							p.setTipo((Integer) lista.getSelectedItem()	.getValue());
							vista.addPeriodicidad(p);
							wind.detach();
						} 
						else{
						  app.mostrarError("Debe Seleccionar al Menos 1 Items ");
						}
					} 
					else{
					   Messagebox.show("Ingrese la frecuencia de la Periodicidad");
					}
				}
			});
			
			Listhead head = new Listhead();
			Listheader c1 = new Listheader("Periocidad");
			head.appendChild(c1);
			lista.appendChild(head);
			
			for (int i = 0; i < Periodicidad.getLisPeriosidad().length; i++)
			{
				Listitem items = new Listitem();
				items.setValue(i);
				Listcell celda = new Listcell(Periodicidad.getLisPeriosidad()[i]);
				items.appendChild(celda);
				lista.appendChild(items);
			}

			Groupbox grupo = new Groupbox();
			grupo.appendChild(new Caption(" Frecuencia "));
			grupo.appendChild(frecuencia);
			grupo.appendChild(new Label("Frecuencia :magnitud que mide el número de repeticiones por unidad de tiempo"));
			wind.appendChild(grupo);
			wind.appendChild(lista);
			wind.appendChild(seleccionar);
			app.agregarHija(wind);
			wind.doModal();
		}
		
		else if (vista.getRmGarantia() == arg0.getTarget()) {
			DetalleGarantina dt = vista.detalleSelect();
			if (dt != null) {
				vista.getLisDetalle().remove(dt);
				vista.getDetalleGarantia().getSelectedItem().detach();
			}
		}
		else if (vista.getAddGarantia() == arg0.getTarget()) {
			final Window wind = new Window("Detalle Garantia ", "normal", true);
			wind.setWidth("600px");
			final Listbox lista = new Listbox();
			Button seleccionar = new Button("Seleccionar");
			seleccionar.addEventListener(Events.ON_CLICK, new org.zkoss.zk.ui.event.EventListener()
			{
				@Override
				public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
					if (lista.getSelectedItem() != null) {
						vista.addDetalleGarantia((DetalleGarantina) lista.getSelectedItem().getValue());
						wind.detach();
					}
				}
			});
			
			Listhead head = new Listhead();
			Listheader c1 = new Listheader("Detalle Garantia");
			head.appendChild(c1);
			lista.appendChild(head);
			
			for (DetalleGarantina dt : this.llamador.getTipoGarantia().getDetallesGarantia()) {
				Listitem items = new Listitem();
				items.setValue(dt);
				Listcell celda = new Listcell(dt.getDescripcion());
				items.appendChild(celda);
				lista.appendChild(items);
			}

			wind.appendChild(lista);
			wind.appendChild(seleccionar);
			app.agregarHija(wind);
			wind.doModal();
		} 
		else if (vista.getAceptar() == arg0.getTarget()) {
			validar();
			guardar();
		} 
		else if (vista.getCancelar() == arg0.getTarget()) {
			vista.detach();
		}
	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		vista.actualizarModelo();
		getDato().setDetallesGarantia(vista.getLisDetalle());
		getDato().setPeriodicidad(vista.getListPeriodisidad());

		if (modoAgregar()) {
			llamador.setActividad(getDato());
			vista.detach();
		} 
		else if (modoEditar()) {
			llamador.setEditarActividad();
			vista.detach();
		}
	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

		if (vista.getDescripcion().getValue().trim().length() == 0) {
			throw new WrongValueException(vista.getDescripcion(),"Indique una Descripcion");
		}
		if (vista.getListPeriodisidad().size() == 0) {
			throw new WrongValueException(vista.getPeriodicidad(),"Debe Selecionar al Menos 1 Items");
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
