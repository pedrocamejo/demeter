package cpc.demeter.controlador.mantenimiento;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Window;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.mantenimiento.CatPlantaDistribuidora;
import cpc.demeter.vista.mantenimiento.UIPlantaDistribuidora;
import cpc.modelo.demeter.mantenimiento.PlantaDistribuidora;
import cpc.modelo.demeter.mantenimiento.TipoGarantia;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.negocio.demeter.mantenimiento.NegocioPlantaDistribuidora;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContPlantaDistribuidora extends
		ContVentanaBase<PlantaDistribuidora> {

	private static final long serialVersionUID = 1L;
	private AppDemeter app;
	private UIPlantaDistribuidora vista;
	private NegocioPlantaDistribuidora servicio;
	private CatPlantaDistribuidora llamador;

	public ContPlantaDistribuidora(int i) {
		super(i);
		// TODO Auto-generated constructor stub
	}

	public ContPlantaDistribuidora(int accion, PlantaDistribuidora dato,
			CatPlantaDistribuidora catPlantaDistribuidora, AppDemeter app2)
			throws ExcDatosInvalidos {
		super(accion);

		app = app2;
		llamador = catPlantaDistribuidora;
		if (modoAgregar()) {
			dato = new PlantaDistribuidora();
		}
		servicio = NegocioPlantaDistribuidora.getInstance();
		vista = new UIPlantaDistribuidora("Planta  (" + Accion.TEXTO[accion]
				+ ")", 800, servicio.getmodelos_TipoGarantia());
		setear(dato, vista, catPlantaDistribuidora, app);
		this.vista = (UIPlantaDistribuidora) getVista();
		vista.desactivar(accion);

	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub

		if (vista.getCargar() == arg0.getTarget())
		{
			Window w = vista.getVentanaaux();
			vista.appendChild(w);
			w.setWidth("500px");
			w.doModal();
		} 
		else if (vista.getQuitar() == arg0.getTarget())
		{
			TipoGarantia datoSeleccionado = vista.getDatoSeleccionado();
			if (datoSeleccionado != null)
			{
				for (TipoGarantia tp : vista.getLisProductos()) {
					if (tp.getId().equals(datoSeleccionado.getId())) {
						vista.getLisProductos().remove(tp);
						break;
					}
				}
				vista.actualizarproductos();
			}
		} 
		else if (vista.getAceptaraux() == arg0.getTarget()) 
		{
			Modelo datoSeleccionar = vista.getModelos().getSeleccion();
			if (datoSeleccionar != null)
			{
				TipoGarantia tipoGarantia = servicio
						.getTipoGarantia(datoSeleccionar);
				// buscar que no exista en la lista de productos de la planta
				for (TipoGarantia tp : vista.getLisProductos())
				{
					if (tp.getId().equals(tipoGarantia.getId())) {
						return;
					}
				}
				vista.getLisProductos().add(tipoGarantia);
				vista.actualizarproductos();
				vista.getVentanaaux().detach();
			}
		} 
		else {
			validar();
			getDato().setProductos(vista.getLisProductos());
			procesarCRUD(arg0);
		}
	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		try {
			servicio = NegocioPlantaDistribuidora.getInstance();
			getDato().setTipo(
					(Integer) vista.getTipoFabrica().getSelectedItem()
							.getValue());
			servicio.Guardar(getDato());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		vista.detach();
	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		if (vista.getNombre().getValue() == null) {
			throw new WrongValueException(vista.getNombre(),
					"Indique una nombre");
		}
		if (vista.getDocumento().getValue() == null) {
			throw new WrongValueException(vista.getDocumento(),
					"Indique un RIF");
		}
		if (vista.getDireccion().getValue() == null) {
			throw new WrongValueException(vista.getDireccion(),
					"Indique una Dirección");
		}
		if (vista.getTipoFabrica().getSelectedItem() == null) {
			throw new WrongValueException(vista.getTipoFabrica(),
					"Seleccione un tipo Fabrica Pauny o Don Roque ");
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