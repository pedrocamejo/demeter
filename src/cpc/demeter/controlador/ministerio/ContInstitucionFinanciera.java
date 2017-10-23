package cpc.demeter.controlador.ministerio;

import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ministerio.ContInstitucionesCrediticias;
import cpc.demeter.vista.ministerio.UiInstitucionCrediticia;
import cpc.modelo.ministerio.gestion.InstitucionCrediticia;
import cpc.modelo.ministerio.gestion.TipoFinanciamientoCrediticio;
import cpc.negocio.ministerio.basico.NegocioInstitucionFinanciera;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContInstitucionFinanciera extends
		ContVentanaBase<InstitucionCrediticia> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioInstitucionFinanciera servicio;
	private AppDemeter app;
	private UiInstitucionCrediticia vista;

	public ContInstitucionFinanciera(int modoOperacion,
			InstitucionCrediticia tipo, ContInstitucionesCrediticias llamador,
			AppDemeter app) throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			if (tipo == null || modoAgregar())
				tipo = new InstitucionCrediticia();
			servicio = NegocioInstitucionFinanciera.getInstance();
			List<TipoFinanciamientoCrediticio> tipos;
			tipos = servicio.getTiposFinanciamientos();
			setear(tipo, new UiInstitucionCrediticia("INSTITUCION CREDITICIA ("
					+ Accion.TEXTO[modoOperacion] + ")", 550, tipos), llamador,
					app);
			vista = ((UiInstitucionCrediticia) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void eliminar() {

		try {
			servicio = NegocioInstitucionFinanciera.getInstance();
			servicio.setModelo(getDato());
			servicio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {

		try {
			servicio = NegocioInstitucionFinanciera.getInstance();
			servicio.setModelo(getDato());
			servicio.guardar(getDato().getId());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR
				&& (vista.getDescripcion().getValue() == null || vista
						.getDescripcion().getValue() == "")) {
			throw new WrongValueException(vista.getDescripcion(),
					"Indique una Descripcion");
		}
	}

	public void onEvent(Event arg0) throws Exception {
		validar();
		procesarCRUD(arg0);
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
