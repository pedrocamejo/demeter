package cpc.demeter.controlador.administrativo;

import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter; 
import cpc.demeter.Servicios;
import cpc.demeter.vista.administrativo.UiAprobacionDebito;
import cpc.modelo.demeter.administrativo.AprobacionDebito;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.negocio.demeter.administrativo.NegocioAprobacionDebito;
import cpc.persistencia.sigesp.implementacion.PerUnidadAdministrativa;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContAprobacionDebito extends
		ContVentanaBase<AprobacionDebito> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5895982450580199685L;
	private NegocioAprobacionDebito servicio;
	private UiAprobacionDebito vistaAprobacion;
	// private Sede sede;
	private AppDemeter app;

	public ContAprobacionDebito(int modoOperacion,
			AprobacionDebito aprobacionDebito,
			ContCatalogo<AprobacionDebito> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido {
		super(modoOperacion);
		this.app = app;

		servicio = NegocioAprobacionDebito.getInstance();

		try {
			List<UnidadAdministrativa> pagos = new PerUnidadAdministrativa()
					.getAll();

			setDato(aprobacionDebito);
			if (aprobacionDebito == null || modoAgregar()) {

				aprobacionDebito = servicio.nuevo();
				aprobacionDebito.setUsuario(app.getUsuario().getNombre());
				aprobacionDebito.setFechaAprobacion(new Date());
				

			}
			/*
			 * else{ servicio.setAprobacionDescuento(aprobacionDescuento) ;
			 * pagos = (List<UnidadAdministrativa>)
			 * aprobacionDescuento.getUnidadAdministrativa(); }
			 */

			// lo veo raro
			setear(aprobacionDebito, new UiAprobacionDebito(
					"Aprobacion de Nota De Debito (" + Accion.TEXTO[modoOperacion]
							+ ")", 300, modoOperacion, pagos), llamador, app);
			vistaAprobacion = (UiAprobacionDebito) getVista();
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
		 * getDato().setdescuento(					vistaAprobacion.getPorcentajedescuento().getValue());
			getDato().setStr_beneficiado(
					vistaAprobacion.getBeneficiado().getValue());
			getDato().setStr_cedurif(vistaAprobacion.getCedulaRif().getValue());
			getDato().setUnidadAdministrativa(
					vistaAprobacion.getUnidad().getSeleccion());
			// servicio.setModelo(getDato());
			AprobacionDescuento as = getDato();
		 */
		getDato().setNroRecibo(vistaAprobacion.getTxtNroRecibo().getValue());
		getDato().setMontoRecibo(vistaAprobacion.getDblMontoRecibo().getValue());
		getDato().setNroFactura(vistaAprobacion.getTxtNroFactura().getValue());
		getDato().setCedurif(vistaAprobacion.getTxtCedulaRif().getValue());
		getDato().setPagador(vistaAprobacion.getTxtPagador().getValue());
		getDato().setUnidadAdministrativa(vistaAprobacion.getUnidad().getSeleccion());
		getDato().setMotivo(vistaAprobacion.getTxtMotivo().getValue());

		
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

		if (vistaAprobacion.getUnidad().getSeleccion() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe seleccionar la unidad");
	String a = vistaAprobacion.getTxtPagador().getText();
	String b = vistaAprobacion.getTxtPagador().getValue();
	
		if (vistaAprobacion.getTxtPagador().getText() == "")
			throw new WrongValueException(vistaAprobacion.getTxtPagador(),
					"debe seleccionar especificar el pagador");

		if (vistaAprobacion.getTxtCedulaRif().getText() == "")
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el Rif/Cedula");

		if (vistaAprobacion.getDblMontoRecibo().getValue() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el Monto");
		
		if (vistaAprobacion.getTxtNroRecibo().getText() == "")
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar Numero Del recibo");
		if (vistaAprobacion.getTxtNroFactura().getText() == "")
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el numero de la factura");
		if (vistaAprobacion.getTxtMotivo().getValue() == "")
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el motivo");
		if (vistaAprobacion.getSemilla().getValue() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el codigo de peticion");

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
