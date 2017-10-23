package cpc.demeter.controlador.administrativo;

import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.Sede;
import cpc.demeter.AppDemeter;
import cpc.demeter.Servicios;
import cpc.demeter.vista.administrativo.UICierreDiario;
import cpc.demeter.vista.administrativo.UiAprobacionDescuento;
import cpc.modelo.demeter.administrativo.AprobacionDescuento;
import cpc.modelo.demeter.administrativo.CierreDiario;
import cpc.modelo.demeter.administrativo.FormaPago;
import cpc.modelo.demeter.interfaz.iFactura;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.modelo.sigesp.indice.SedePK;
import cpc.negocio.demeter.administrativo.NegocioAprobacionDescuento;
import cpc.negocio.demeter.administrativo.NegocioCierreDiario;
import cpc.persistencia.sigesp.implementacion.PerUnidadAdministrativa;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContAprobacionDescuento extends
		ContVentanaBase<AprobacionDescuento> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5895982450580199685L;
	private NegocioAprobacionDescuento servicio;
	private UiAprobacionDescuento vistaAprobacion;
	// private Sede sede;
	private AppDemeter app;

	public ContAprobacionDescuento(int modoOperacion,
			AprobacionDescuento aprobacionDescuento,
			ContCatalogo<AprobacionDescuento> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido {
		super(modoOperacion);
		this.app = app;

		servicio = NegocioAprobacionDescuento.getInstance();

		try {
			List<UnidadAdministrativa> pagos = new PerUnidadAdministrativa()
					.getAll();

			setDato(aprobacionDescuento);
			if (aprobacionDescuento == null || modoAgregar()) {

				aprobacionDescuento = servicio.nuevo();
				aprobacionDescuento.setusuario(app.getUsuario().getNombre());
				aprobacionDescuento.setfechaaprobacion(new Date());
				aprobacionDescuento.setdescuento(00.0);

			}
			/*
			 * else{ servicio.setAprobacionDescuento(aprobacionDescuento) ;
			 * pagos = (List<UnidadAdministrativa>)
			 * aprobacionDescuento.getUnidadAdministrativa(); }
			 */

			// lo veo raro
			setear(aprobacionDescuento, new UiAprobacionDescuento(
					"Aprobacion de Descuento (" + Accion.TEXTO[modoOperacion]
							+ ")", 300, modoOperacion, pagos), llamador, app);
			vistaAprobacion = (UiAprobacionDescuento) getVista();
			vistaAprobacion.desactivar(modoOperacion);
			/*
			 * vistaCierre.getSede().setValue(sede.getNombre());
			 * vistaCierre.getDetalleDeposito
			 * ().setModelo(servicio.getDepositos(servicio
			 * .getCierreDiario().getFecha()));
			 * vistaCierre.getAsiento().setModelo
			 * (servicio.generarAsientoDiario(servicio.getCierreDiario()));
			 */} catch (ExcFiltroExcepcion e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void eliminar() {

	}

	public void guardar() {
		try {
			getVista().actualizarModelo();
			cargarModelo();
			getDato().setdescuento(
					vistaAprobacion.getPorcentajedescuento().getValue());
			getDato().setStr_beneficiado(
					vistaAprobacion.getBeneficiado().getValue());
			getDato().setStr_cedurif(vistaAprobacion.getCedulaRif().getValue());
			getDato().setUnidadAdministrativa(
					vistaAprobacion.getUnidad().getSeleccion());
			// servicio.setModelo(getDato());
			AprobacionDescuento as = getDato();
			servicio.guardar(getDato().getId());

		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void cargarModelo() {
		/*
		 * getDato().setDetalles(vistaFactura.getModeloDetalle());
		 * getDato().setBeneficiario
		 * (vistaFactura.getRazonSocial().getValorObjeto());
		 * getDato().setDireccionBeneficiario
		 * (vistaFactura.getDireccion().getSeleccion());
		 * getDato().setMontoSaldo(getDato().getMontoTotal());
		 */
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

		if (vistaAprobacion.getUnidad().getSeleccion() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe seleccionar la unidad");

		if (vistaAprobacion.getBeneficiado().getValue() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe seleccionar especificar el beneficiado");

		if (vistaAprobacion.getCedulaRif().getValue() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el Rif/Cedula del beneficiado");

		if (vistaAprobacion.getPorcentajedescuento().getValue() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el porcentaje de descuento");

	}

	public void onEvent(Event event) {
		System.out.println("hay evento " + event.getName() + " en "
				+ event.getTarget().getClass().getName());
		try {
			if (event.getTarget() == getVista().getAceptar()) {
				validar();
				Servicios codr = new Servicios(app);
				Double valorDado = vistaAprobacion.getSemilla().getValue();
				Integer a = (int) Math.floor(valorDado);
				String b = codr.generarHash(a.toString());
				Messagebox.show("el codigo a usar el es " + b, "Advertencia",
						Messagebox.OK, Messagebox.INFORMATION);

				procesarCRUD(event);
			}
			if (event.getTarget() == vistaAprobacion.getSemilla()) {

			}
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void actualizarDatos() {

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
