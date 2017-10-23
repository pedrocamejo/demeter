package cpc.demeter.controlador.administrativo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Messagebox;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.Identidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.Servicios;
import cpc.demeter.vista.administrativo.UIAprobacionDebitoInterno;
import cpc.modelo.demeter.administrativo.AprobacionDebitoInterno;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.negocio.demeter.administrativo.NegocioAprobacionDebitoInterno;
import cpc.persistencia.sigesp.implementacion.PerUnidadAdministrativa;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContAprobacionDebitoInterno extends
		ContVentanaBase<AprobacionDebitoInterno> {

	/**
	 * 
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1877105240012828160L;
	/**
	 * 
	 */
	
	private NegocioAprobacionDebitoInterno servicio;
	private UIAprobacionDebitoInterno vistaAprobacion;
	// private Sede sede;
	private AppDemeter app;

	public ContAprobacionDebitoInterno(int modoOperacion,
			AprobacionDebitoInterno aprobacion,
			ContCatalogo<AprobacionDebitoInterno> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido {
		super(modoOperacion);
		this.app = app;

		servicio = NegocioAprobacionDebitoInterno.getInstance();

		try {
			List<UnidadAdministrativa> pagos = new PerUnidadAdministrativa()
					.getAll();

			setDato(aprobacion);
			if (aprobacion == null || modoAgregar()) {
				aprobacion=new AprobacionDebitoInterno();
Identidad aux = app.getUsuario().getIdentidad();
			System.out.println(app.getUsuario().getIdentidad().getNombreCompleto());
			System.out.println(app.getUsuario().getIdentidad().getCedula());
				aprobacion.setNombreResponsable(app.getUsuario().getIdentidad().getNombreCompleto());
				aprobacion.setCedulaResponsable(app.getUsuario().getIdentidad().getCedula());
				aprobacion.setFechaAprobacion(new Date());
			

			}
			/*
			 * else{ servicio.setAprobacionDescuento(aprobacionDescuento) ;
			 * pagos = (List<UnidadAdministrativa>)
			 * aprobacionDescuento.getUnidadAdministrativa(); }
			 */

			// lo veo raro
			setear(aprobacion, new UIAprobacionDebitoInterno(
					"Aprobacion DebitoInterno (" + Accion.TEXTO[modoOperacion]
							+ ")", 850, modoOperacion, pagos), llamador, app);
			vistaAprobacion = (UIAprobacionDebitoInterno) getVista();
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
			getDato().setNombreEjecutor(vistaAprobacion.getTxtNombreEjecutor().getValue());
			getDato().setCedulaEjecutor(vistaAprobacion.getTxtCedulaEjecutor().getValue());
			getDato().setNombreBeneficiario(vistaAprobacion.getTxtNombreBeneficiario().getValue());
			getDato().setIdLegalBeneficiario(vistaAprobacion.getTxtCedRifBenefiario().getValue());
			getDato().setNroControlDocumentoAfectado(vistaAprobacion.getTxtNroControlDocumento().getValue());
			getDato().setNroDocumentoAfectado(vistaAprobacion.getTxtNroDocuemnto().getValue());
			getDato().setMotivo(vistaAprobacion.getTxtMotivo().getValue());
			getDato().setObservacion(vistaAprobacion.getTxtObservacion().getValue());
			getDato().setMonto(vistaAprobacion.getMonto().getValue());
			// servicio.setModelo(getDato());
			AprobacionDebitoInterno as = getDato();
			servicio.guardar(getDato());

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

		if (vistaAprobacion.getTxtNombreBeneficiario().getValue()== null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe seleccionar especificar el beneficiado");

		if (vistaAprobacion.getTxtCedRifBenefiario().getValue() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el Rif/Cedula del beneficiado");
		
		if (vistaAprobacion.getTxtNombreEjecutor().getValue()== null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe seleccionar especificar quien Realiza el Debito Interno");

		if (vistaAprobacion.getTxtCedulaEjecutor().getValue() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el Rif/Cedula de quien Realiza el Debito Interno");
		
		if (vistaAprobacion.getTxtNroControlDocumento().getValue() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el Nro Control Documento Fiscal que afecta el Debito Interno");
		if (vistaAprobacion.getTxtNroDocuemnto().getValue() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el Nro Documento que afecta el Debito Interno");

		if (vistaAprobacion.getTxtObservacion().getValue() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar la Observacion del Debito Interno");
		
		if (vistaAprobacion.getTxtMotivo().getValue() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el motivo del Debito Interno");
		
		if (vistaAprobacion.getMonto().getValue() == null)
			throw new WrongValueException(vistaAprobacion.getUnidad(),
					"debe especificar el Monto del Debito Interno");
		


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

	@SuppressWarnings("rawtypes")
	public static void imprimir(AprobacionDebitoInterno aprobacionDebitoInterno, IZkAplicacion app) {
		try {
			HashMap parametros = new HashMap();
			parametros.put("logo",Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");
			parametros.put("usuario", app.getUsuario().toString());
			List<AprobacionDebitoInterno> aprobaciones = new ArrayList<AprobacionDebitoInterno>();
			aprobaciones.add(aprobacionDebitoInterno);
			JRDataSource ds = new JRBeanCollectionDataSource(aprobaciones);
			String url = (Index.class.getResource("/").getPath()+ "../../reportes/aprobaciondebitointerno.jasper").trim();
			JasperReport reporte = (JasperReport) JRLoader.loadObject(new File(url));
			byte[] resultado = JasperRunManager.runReportToPdf(reporte,parametros,new JRBeanCollectionDataSource(aprobaciones));
			Filedownload.save(resultado,"application/pdf","AprobacionDebitoInterno_"+aprobacionDebitoInterno.getId()+".pdf");
	
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}
}