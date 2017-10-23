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
import cpc.demeter.vista.administrativo.UIAprobacionReversoRecibo;
import cpc.demeter.vista.administrativo.UICierreDiario;

import cpc.modelo.demeter.administrativo.AprobacionDescuento;
import cpc.modelo.demeter.administrativo.AprobacionReversoRecibo;
import cpc.modelo.demeter.administrativo.CierreDiario;
import cpc.modelo.demeter.administrativo.FormaPago;
import cpc.modelo.demeter.interfaz.iFactura;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.modelo.sigesp.indice.SedePK;
import cpc.negocio.demeter.administrativo.NegocioAprobacionDescuento;
import cpc.negocio.demeter.administrativo.NegocioAprobacionReversoRecibo;
import cpc.negocio.demeter.administrativo.NegocioCierreDiario;
import cpc.persistencia.sigesp.implementacion.PerUnidadAdministrativa;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContAprobacionReversoRecibo extends
		ContVentanaBase<AprobacionReversoRecibo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3414827193033542789L;
	/**
	 * 
	 */
	
	private NegocioAprobacionReversoRecibo servicio;
	private UIAprobacionReversoRecibo vistaAprobacion;
	// private Sede sede;
	private AppDemeter app;

	public ContAprobacionReversoRecibo(int modoOperacion,
			AprobacionReversoRecibo aprobacionReversoRecibo,
			ContCatalogo<AprobacionReversoRecibo> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido {
		super(modoOperacion);
		this.app = app;

		servicio = NegocioAprobacionReversoRecibo.getInstance();

		try {
			List<UnidadAdministrativa> pagos = new PerUnidadAdministrativa()
					.getAll();

			setDato(aprobacionReversoRecibo);
			if (aprobacionReversoRecibo == null || modoAgregar()) {

				aprobacionReversoRecibo = servicio.nuevo();
				aprobacionReversoRecibo.setusuario(app.getUsuario().getNombre());
				aprobacionReversoRecibo.setfechaaprobacion(new Date());
				aprobacionReversoRecibo.setMontoRecibo(0.0);;

			}
			/*
			 * else{ servicio.setAprobacionDescuento(aprobacionDescuento) ;
			 * pagos = (List<UnidadAdministrativa>)
			 * aprobacionDescuento.getUnidadAdministrativa(); }
			 */

			// lo veo raro
			setear(aprobacionReversoRecibo, new UIAprobacionReversoRecibo(
					"Aprobacion de Reverso Recibo (" + Accion.TEXTO[modoOperacion]
							+ ")", 300, modoOperacion, pagos), llamador, app);
			vistaAprobacion = (UIAprobacionReversoRecibo) getVista();
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
			getDato().setUnidadAdministrativa(vistaAprobacion.getUnidad().getSeleccion());
			getDato().setBeneficiario(vistaAprobacion.getBeneficiario().getValue());
			getDato().setBeneficiariocedurif(vistaAprobacion.getCedulaRifbeneficiario().getValue());
			getDato().setCedulasolicitante(vistaAprobacion.getCedulaRifSolicitante().getValue());
			getDato().setNombresolicitante(vistaAprobacion.getSolictante().getValue());
			getDato().setObservacion(vistaAprobacion.getObservacion().getValue());
			getDato().setRecibo(vistaAprobacion.getNroRecibo().getValue());
			getDato().setMontoRecibo(vistaAprobacion.getMontoRecibo().getValue());
			// servicio.setModelo(getDato());
			AprobacionReversoRecibo as = getDato();
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

		if (vistaAprobacion.getBeneficiario().getValue()== null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe seleccionar especificar el beneficiado");

		if (vistaAprobacion.getCedulaRifbeneficiario().getValue() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el Rif/Cedula del beneficiado");

		if (vistaAprobacion.getMontoRecibo().getValue() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el Monto del recibo");

		if (vistaAprobacion.getSolictante().getValue()== null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe seleccionar especificar el solictante");

		if (vistaAprobacion.getCedulaRifSolicitante().getValue() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el Rif/Cedula del solictante");

		if (vistaAprobacion.getNroRecibo().getValue() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el numero del recibo");

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
