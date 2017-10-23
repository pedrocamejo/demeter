package cpc.demeter.controlador.gestion;

import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.gestion.ContCambiosTransportesProduccion;
import cpc.demeter.vista.gestion.UiCambioOrdenTransporte;
import cpc.modelo.demeter.basico.UnidadArrime;
import cpc.modelo.demeter.gestion.DetalleOrdenTransporteProduccion;
import cpc.modelo.demeter.gestion.FallaRecepcionSilo;
import cpc.negocio.demeter.gestion.NegocioDetalleOrdenTransporteProduccion;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContCambioOrdenTransporte extends
		ContVentanaBase<DetalleOrdenTransporteProduccion> {

	private static final long serialVersionUID = 1180434079481225766L;
	private NegocioDetalleOrdenTransporteProduccion servicio;
	private UiCambioOrdenTransporte vista;
	private AppDemeter app;

	public ContCambioOrdenTransporte(int modoOperacion,
			DetalleOrdenTransporteProduccion momento,
			ContCambiosTransportesProduccion llamador, AppDemeter app)
			throws ExcDatosInvalidos {
		super(modoOperacion);
		this.app = app;
		this.servicio = NegocioDetalleOrdenTransporteProduccion.getInstance();
		try {
			if (datoNulo(momento) || modoAgregar())
				throw new ExcDatosInvalidos("Dato nulo, o modo no valido");
		} catch (ExcSeleccionNoValida e1) {
			throw new ExcDatosInvalidos(e1.getMessage());
		}
		List<FallaRecepcionSilo> fallas = null;
		List<UnidadArrime> unidades = null;
		try {
			fallas = servicio.getFallas();
			unidades = servicio.getSilos();
		} catch (Exception e) {
			// TODO: handle exception
		}
		setear(momento, new UiCambioOrdenTransporte("Cambio Ruta Transporte  ("
				+ Accion.TEXTO[modoOperacion] + ")", 550, unidades, fallas,
				modoConsulta()), llamador, this.app);
		this.vista = (UiCambioOrdenTransporte) getVista();
	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		try {
			DetalleOrdenTransporteProduccion datoI = actualizarModelo();
			servicio.guardar(getDato(), datoI);
			getLlamador().agregarDato(datoI);
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("ERROR AL GUARDAR");
		}
	}

	private DetalleOrdenTransporteProduccion actualizarModelo() {
		DetalleOrdenTransporteProduccion datoI = new DetalleOrdenTransporteProduccion();
		getDato().setEfectivo(false);
		getDato().setFalla(vista.getCausa().getSeleccion());
		datoI.setDestino(vista.getDestino().getSeleccion());
		datoI.setOrigen(vista.getOrigen().getModelo());
		datoI.setOrden(getDato().getOrden());
		datoI.setEfectivo(true);
		datoI.setFechaSalida(vista.getFecha().getValue());
		return datoI;
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (vista.getCausa().getSeleccion() == null)
			throw new WrongValueException(vista.getCausa(), "Indique un valor");
		if (vista.getDestino().getSeleccion() == null)
			throw new WrongValueException(vista.getDestino(),
					"Indique un valor");
		if (vista.getDistancia().getValue() == null)
			throw new WrongValueException(vista.getDistancia(),
					"Indique un valor");
		if (vista.getDias().getValue() == null)
			throw new WrongValueException(vista.getDias(), "Indique un valor");
	}

	public void onEvent(Event event) throws Exception {
		System.out.println("hay evento " + event.getName() + " en "
				+ event.getTarget().getClass().getName());
		if (event.getTarget() == getVista().getAceptar()) {
			if (!modoProcesar())
				procesarCRUD(event);
			else {
				validar();
				guardar();
				refrescarCatalogo();
				vista.close();
			}
		} else if (event.getName() == CompBuscar.ON_SELECCIONO) {
			vista.actualizarModelo();
		} else if (event.getName() == Events.ON_SELECT) {
			vista.actualizarModelo();
		}
	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

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
