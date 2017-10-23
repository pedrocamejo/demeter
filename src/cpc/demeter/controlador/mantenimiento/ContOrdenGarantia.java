package cpc.demeter.controlador.mantenimiento;

import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.mantenimiento.CatMaquinariaSelect;
import cpc.demeter.catalogo.mantenimiento.CatlogoOrdenGarantia;
import cpc.demeter.vista.mantenimiento.UIOrdenGarantia;
import cpc.modelo.demeter.mantenimiento.Actividad;
import cpc.modelo.demeter.mantenimiento.DetalleGarantina;
import cpc.modelo.demeter.mantenimiento.MaquinariaExterna;
import cpc.modelo.demeter.mantenimiento.OrdenGarantia;
import cpc.negocio.demeter.mantenimiento.NegocioMaquinariaExterna;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContOrdenGarantia extends ContVentanaBase<OrdenGarantia> {

	private AppDemeter 						app;
	private UIOrdenGarantia 				vista;
	private NegocioMaquinariaExterna	 	negocio;
	private CatlogoOrdenGarantia 			llamador;

	private static final long serialVersionUID = 1L;

	public ContOrdenGarantia(int i) {
		super(i);
		// TODO Auto-generated constructor stub
	}
	public ContOrdenGarantia(int accion, OrdenGarantia dato,CatlogoOrdenGarantia catlogoOrdenGarantia, AppDemeter app) 	throws ExcDatosInvalidos, InterruptedException {
		super(accion);

		negocio = NegocioMaquinariaExterna.getInstance();

		setDato(dato);
		this.app = app;
		this.llamador = catlogoOrdenGarantia;
		this.vista = new UIOrdenGarantia("Orden de garantia", 850);

		if (accion == Accion.AGREGAR) 
		{
			dato = new OrdenGarantia();
			dato.setEstatus(0);
			setDato(dato);
			new CatMaquinariaSelect(app, this);
		} 
		else
		{
			this.app = app;
			this.vista = new UIOrdenGarantia("Orden de garantia", 700);
			this.vista.setDetallesGarantia(negocio.detalle_garantiasinMaquinaria(getDato().getMaquinaria()));
			setear(getDato(), this.vista, this.llamador, app);
			this.vista.modoConsuta(accion);
			if (accion == Accion.PROCESAR) {
				// Mostrar los datos del procesar
				this.vista.MostrarProcesar();

			}
		}
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub

		if (arg0.getTarget() == vista.getCombGarantia()) {
			
			if (vista.getCombGarantia().getSelectedItem() != null) {

				DetalleGarantina dt = (DetalleGarantina) vista.getCombGarantia().getSelectedItem().getValue();
				List<Actividad> actividades = negocio.actividades(dt, getDato().getMaquinaria().getTipo().getModelo());
				vista.setDetalleGarantia(dt);
				vista.setLisActividades(actividades);
				this.vista.MostrarActividades(actividades);
			}
		} 
		else if (arg0.getTarget() == vista.getDetalle()) 
		{
			Actividad actividad = vista.getActividadSeleccionada();
			if (actividad != null) {
				new ContActividad(Accion.CONSULTAR, actividad, this, app);
			}
		} 
		else {
			if (modoProcesar() || modoAnular()) {
				validar();
				procesarCRUD(arg0);
			} else {
				procesarCRUD(arg0);
			}
		}

	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

		try {
			getDato().setDetalleGarantia(vista.getDetalleGarantia());
			getDato().setActividades(vista.getLisActividades());
			negocio.guardar(getDato());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		if (getDato().getEstatus() == 0) {
			try {
				negocio.eliminar(getDato());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		if (vista.getEncargado().getValue().trim().length() == 0) {
			throw new WrongValueException(vista.getEncargado(),
					"Indique un Encargado ");
		}
		if (this.vista.getDetalleGarantia() == null) {
			throw new WrongValueException(vista.getCombGarantia(),
					"Seleccione una Detalle Garantia ");
		}
		if (this.vista.getLisActividades().size() == 0) {
			throw new WrongValueException(
					vista.getActividades(),
					" la Oden de Trabajo debe tener al menos 1 actividad seleccione un Detalle Garantia con Actividad ");
		}
		if (this.vista.getFecha().getValue() == null && modoProcesar()) {
			throw new WrongValueException(vista.getFecha(),
					" Ingrese la Fecha en la cual se llevo a Cabo las Operaciones  ");
		}
		if (vista.getKm().getValue() == null && modoProcesar()) {
			throw new WrongValueException(vista.getKm(),
					" Ingrese el Km de la Maquinaria  ");
		}
	}

	public void actualizarMaquinaria(MaquinariaExterna maquinariaExterna)
			throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		getDato().setMaquinaria(maquinariaExterna);
		this.vista = new UIOrdenGarantia("Orden de garantia", 700);
		this.vista.setDetallesGarantia(negocio
				.detalle_garantiasinMaquinaria(maquinariaExterna));
		setear(getDato(), this.vista, this.llamador, app);
	}

	@Override
	public void anular() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		try {
			this.vista.actualizarModelo();
			getDato().setEstatus(3);
			negocio.guardar(getDato());
			this.vista.detach();
			refrescarCatalogo();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		try {
			this.vista.actualizarModelo();
			getDato().setEstatus(2);
			negocio.guardar(getDato());
			this.vista.detach();
			refrescarCatalogo();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
