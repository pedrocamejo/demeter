package cpc.demeter.controlador.gestion;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.demeter.AppDemeter;
import cpc.demeter.vista.gestion.UiDesincorporacionActivo;
import cpc.modelo.sigesp.basico.ActivoAlmacen;
import cpc.negocio.demeter.gestion.NegocioActivoAlmacen;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContDesincorporacionActivo extends ContVentanaBase<ActivoAlmacen> {

	private static final long serialVersionUID = 1041268910174438358L;
	private NegocioActivoAlmacen negocio;
	private UiDesincorporacionActivo vista;
	private AppDemeter app;

	public ContDesincorporacionActivo(int modoOperacion,
			ActivoAlmacen activoAlmacen, ContCatalogo<ActivoAlmacen> llamador,
			AppDemeter app) {
		super(modoOperacion);
		this.app = app;
		this.negocio = NegocioActivoAlmacen.getInstance();

		try {
			if (!datoNulo(activoAlmacen) && modoEditar()) {
				this.vista = new UiDesincorporacionActivo(
						"DESINCORPORAR ACTIVO", 400);
				setear(activoAlmacen, vista, llamador, app);
			}
		} catch (ExcSeleccionNoValida e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	}

	public void eliminar() throws ExcFiltroExcepcion {

	}

	public void guardar() throws ExcFiltroExcepcion {
		try {
			negocio.setActivoAlmacen(getDato());
			negocio.getActivoAlmacen().setDesincorporado(true);
			negocio.guardar();
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("ERROR AL GUARDAR");
		}

	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (vista.getMotivo().getValue().isEmpty()) {
			throw new WrongValueException(vista.getMotivo(),
					"Debe indicar el motivo");
		}
	}

	public void onEvent(Event event) throws Exception {
		if (event.getTarget() == getVista().getAceptar())
			procesarCRUD(event);
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
