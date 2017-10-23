package cpc.demeter.controlador.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import org.zkoss.zkex.zul.Jasperreport;
import cpc.ares.modelo.Accion;
import cpc.ares.modelo.Sede;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.Servicios;
import cpc.demeter.vista.administrativo.UICierreDiario;
import cpc.modelo.demeter.administrativo.CierreDiario;
import cpc.modelo.demeter.administrativo.FormaPago;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.sigesp.indice.SedePK;
import cpc.negocio.demeter.administrativo.NegocioCierreDiario;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContCierreDiario extends ContVentanaBase<CierreDiario> {

	private static final long serialVersionUID = -3321414973066647454L;
	private NegocioCierreDiario servicio;
	private UICierreDiario vistaCierre;
	private Sede sede;
	private AppDemeter app;

	public ContCierreDiario(int modoOperacion, CierreDiario cierre, ContCatalogo<CierreDiario> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido
	{
		super(modoOperacion);
		this.app = app;
		setDato(cierre);
		sede = null;
		servicio = NegocioCierreDiario.getInstance();
		List<FormaPago> pagos = null;
		sede = app.getSede();
		
		if (cierre == null || modoAgregar()) 
		{
			cierre = servicio.nuevoCierre();
		}
		else 
		{
			servicio.SetCierre(cierre);
			cierre = servicio.getCierreDiario();
		}
		try 
		{
			pagos = servicio.getFormaPago(cierre.getFecha());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		setear(cierre, new UICierreDiario("CIERRE DIARIO (" + Accion.TEXTO[modoOperacion] + ")", pagos), llamador, app);
		
		vistaCierre = (UICierreDiario) getVista();
		vistaCierre.getSede().setValue(sede.getNombre());
		vistaCierre.getDetalleDeposito().setModelo(servicio.getDepositos(servicio.getCierreDiario().getFecha()));
		vistaCierre.getAsiento().setModelo(	servicio.generarAsientoDiario(servicio.getCierreDiario()));

	}

	public void eliminar() {

	}

	public void guardar() {
		try {
			getVista().actualizarModelo();
			cargarModelo();
			CierreDiario z = getDato();

			servicio.guardar(getDato(), new SedePK(sede.getEmpresa()
					.getCodigo(), sede.getIdSede()));
			app.getPadre().setControlSede(servicio.actualizarControl());
			app.configurarEscritorio();
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

	}

	public void onEvent(Event event) {
		System.out.println("hay evento " + event.getName() + " en "
				+ event.getTarget().getClass().getName());
		try {
			if (event.getTarget() == getVista().getAceptar()) {
				procesarCRUD(event);
				enviarcorreo();
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

	@SuppressWarnings("unchecked")
	public void enviarcorreo() throws WrongValuesException,
			ExcEntradaInconsistente {

		@SuppressWarnings("rawtypes")
		HashMap parametros = new HashMap();
		parametros.put("fecha",
				String.format("%1$td/%1$tm/%1$tY", getDato().getFecha()));

		/*
		 * negocio.setCierreDiario(cierre); cierre = negocio.getCierreDiario();
		 */

		parametros.put("coleccionDocumentos", new JRBeanCollectionDataSource(
				getDato().getDocumentos()));
		parametros.put("coleccionCtasPorCobrar",
				new JRBeanCollectionDataSource(getDato().getCuentasCobrar()));
		parametros.put("coleccionCtasPorPagar", new JRBeanCollectionDataSource(
				getDato().getCuentasPagadas()));
		parametros.put("coleccionDepositos", new JRBeanCollectionDataSource(
				servicio.getDepositos(servicio.getCierreDiario().getFecha())));
		parametros.put("coleccionAdelantos", new JRBeanCollectionDataSource(
				getDato().getCuentasAdelantos()));
		parametros.put("coleccionIngresoCaja", new JRBeanCollectionDataSource(
				servicio.getFormaPago(getDato().getFecha())));

		parametros.put("SUBREPORT_DIR", Index.class.getResource("/").getPath()
				+ "cpc/demeter/reporte/fuentes/");
		parametros.put("usuario", app.getUsuario().toString());
		parametros.put("logo", Index.class.getResource("/").getPath()
				+ "../../imagenes/cintillo_inst.png");

		List<CierreDiario> lista = new ArrayList<CierreDiario>();
		lista.add(getDato());
		JRDataSource ds = new JRBeanCollectionDataSource(lista);

		Jasperreport reporte2 = new Jasperreport();

		reporte2.setSrc("reportes/cierreDetalle.jasper");

		reporte2.setParameters(parametros);
		reporte2.setDatasource(ds);
		String destino = "facturacionycobranza@cvapedrocamejo.gob.ve";
		Servicios servicio = new Servicios(app);

		String nombreArchivo = "CierreDiario_" + app.getSede().getNombre()	+ "_" + String.format("%1$td/%1$tm/%1$tY", getDato().getFecha()) + ".pdf";
		// String textocorreo, Jasperreport reporte,AppDemeter app,String
		// destino,String origen,String clave,String Subjetc

		String textocorreo = "Cierre Diario Correspondiente al dia "
				+ String.format("%1$td/%1$tm/%1$tY", getDato().getFecha())
				+ " de la sede " + app.getSede().getNombre();
	//	servicio.EnviarCorreo(textocorreo, reporte2, app, destino,"demeter@cvapedrocamejo.gob.ve", "d3m3t3r", textocorreo,	nombreArchivo);

	}

}
