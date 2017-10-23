package cpc.demeter.controlador.administrativo;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
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
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Filedownload;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.Servicios;
import cpc.demeter.vista.administrativo.UIDebitoInterno;
import cpc.modelo.demeter.administrativo.AprobacionDebitoInterno;
import cpc.modelo.demeter.administrativo.DebitoInterno;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.negocio.demeter.administrativo.NegocioDebitoInterno;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cpc.zk.componente.ventanas.CompVentanaEntrada;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;
import cva.pc.demeter.utilidades.Formateador;

public class ContDebitoInterno extends ContVentanaBase<DebitoInterno> {

	private static final long serialVersionUID = -4005176298792986376L;
	private NegocioDebitoInterno servicio;
	private UIDebitoInterno vistaDebitoInterno;
	private Date fechaCierre;
	private double saldo, monto;
	private AppDemeter app;
	private CompVentanaEntrada vistaCodigo =  new CompVentanaEntrada(this);

	public ContDebitoInterno(int modoOperacion, DebitoInterno debito,
			ContCatalogo<DebitoInterno> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido {
		super(modoOperacion);
		this.app = app;
		servicio = NegocioDebitoInterno.getInstance();
		fechaCierre = servicio.getFechaCierre();
		if (modoOperacion == Accion.ANULAR
				&& !fechaCierre.equals(debito.getFecha()))
			throw new ExcDatosInvalidos(
					"No se puede anular, causa: Fecha de recibo no pertenece al dia a cerrar");
		if (modoOperacion == Accion.ANULAR
				&& debito.isAnulado())
			throw new ExcDatosInvalidos(
					"No se puede anular, causa: ya esta anulado");
		if (datoNulo(debito) || modoAgregar()) {
			debito = servicio.DebitoNuevo();
		}
		List<DocumentoFiscal> facturas = null;
		try {
			facturas = servicio.getDocumentosConSaldo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setear(debito,
				new UIDebitoInterno("Debito Interno  ("
						+ Accion.TEXTO[modoOperacion] + ")", 900, facturas,
						debito.getFecha(), this.app), llamador, this.app);
		vistaDebitoInterno = (UIDebitoInterno) getVista();
		vistaDebitoInterno.getSede().setText(this.app.getSede().toString());
		vistaDebitoInterno.desactivar(modoOperacion);
		if (modoOperacion ==Accion.AGREGAR){
			vistaDebitoInterno.getAceptar().setDisabled(true);
			vistaDebitoInterno.getBotonAutorizar().setVisible(true);
		}
	}

	public void eliminar() throws ExcFiltroExcepcion {
		servicio.eliminar();
	}

	public void anular() {
		try {
			servicio.anular(getDato());
			refrescarCatalogo();
			vistaDebitoInterno.close();
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	}

	public void guardar() throws ExcFiltroExcepcion {
		try {
			getVista().actualizarModelo();
			// cargarModelo();
			servicio.guardar(getDato());
			
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("Error al generar recibo");
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if ((vistaDebitoInterno.getFactura().getSeleccion().getDetalles() == null))
			throw new WrongValueException(vistaDebitoInterno.getFactura(),
					"Indique la Factura");
		if ((vistaDebitoInterno.getTotal().getValue()) > vistaDebitoInterno
				.getSaldo().getValue())
			throw new WrongValueException(vistaDebitoInterno.getTotal(),
					"Monto no valido, superior al saldo");
		if ((vistaDebitoInterno.getTotal().getValue()) ==new Double(0))
			throw new WrongValueException(vistaDebitoInterno.getTotal(),
					"Monto no valido");
		if ((vistaDebitoInterno.getConcepto().getValue()==null))
			throw new WrongValueException(vistaDebitoInterno.getTotal(),
					"Indique Concepto");
	}

	public void onEvent(Event event) throws Exception {
		
		
		
		
		System.out.println("hay evento " + event.getName() + " en "
				+ event.getTarget().getClass().getName());
		
		
		 if (event.getTarget() == vistaCodigo.getAceptar()) 
			{ 
				if (codigoValido(vistaCodigo.getEntrada(),vistaCodigo.gettextopregunta()))
				{
				vistaDebitoInterno.getAceptar().setDisabled(false);
					vistaCodigo.close();
					Events.postEvent("onClick", vistaDebitoInterno.getAceptar(), null);
					
					System.out.println("ok");
					
				}
			
				
			}
			 if (event.getTarget() == vistaDebitoInterno.getBotonAutorizar()){
				  validar();
					mostrarvistaCodigo(app);
				} 
		
		if (event.getTarget() == getVista().getAceptar()) {

			procesarCRUD(event);

		} else if (event.getName() == CompBuscar.ON_SELECCIONO) {
			actualizarDatos();
		} else if (event.getName() == Events.ON_CHANGE) {
			if (monto == vistaDebitoInterno.getTotal().getValue())
				actualizarSaldo();
		}
	}

	public void actualizarDatos() {
		DocumentoFiscal factura = vistaDebitoInterno.getFactura()
				.getValorObjeto();
		vistaDebitoInterno.getSaldo().setValue(factura.getMontoSaldo());
		saldo = factura.getMontoSaldo();
		monto = 0;
		vistaDebitoInterno.getRazonSocial().setValue(
				factura.getNombreBeneficiario());
		vistaDebitoInterno.getSaldoInicial().setValue(
				Formateador.formatearMoneda(factura.getMontoSaldo()));
		vistaDebitoInterno.getTotal().setValue(monto);
	}

	private void actualizarSaldo() {
		double monto = vistaDebitoInterno.getTotal().getValue();
		if (monto != 0 && saldo < monto)
			throw new WrongValueException(vistaDebitoInterno.getTotal(),
					"Monto no valido, mayor al saldo");
		else
			vistaDebitoInterno.getSaldo().setValue(saldo - monto);
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
	public static void imprimir(DebitoInterno debitoInterno, IZkAplicacion app) {
		try {
			HashMap parametros = new HashMap();
			parametros.put("logo",Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");
			parametros.put("usuario", app.getUsuario().toString());
			List<DebitoInterno> aprobaciones = new ArrayList<DebitoInterno>();
			aprobaciones.add(debitoInterno);
			JRDataSource ds = new JRBeanCollectionDataSource(aprobaciones);
			String url = (Index.class.getResource("/").getPath()+ "../../reportes/debitointerno.jasper").trim();
			JasperReport reporte = (JasperReport) JRLoader.loadObject(new File(url));
			byte[] resultado = JasperRunManager.runReportToPdf(reporte,parametros,new JRBeanCollectionDataSource(aprobaciones));
			Filedownload.save(resultado,"application/pdf","DebitoInterno_"+debitoInterno.getControl()+".pdf");
	
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}
	public void mostrarvistaCodigo(AppDemeter app) {
		vistaCodigo = new CompVentanaEntrada(this);
		Servicios servicio = new Servicios(app);
		vistaCodigo
				.setTitle("introduzca el codigo correspondiente a este numero");
		vistaCodigo.getGbGeneral().getLblTitulo()
				.setValue("introduzca el codigo correspondiente a este numero");
		vistaCodigo.settextopregunta(servicio.generarsemilla().toString());
		try {
			System.out.println(servicio.generarHash(vistaCodigo
					.gettextopregunta().getValue()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		app.agregarHija(vistaCodigo);
	}
	
	public boolean codigoValido(Textbox txtNroControl, Label codigo)
			throws InterruptedException {
		Servicios servicio = new Servicios(app);
		try {
			if (txtNroControl.getText().trim().isEmpty()) {
				app.mostrarError("El nro de control no puede estar vacio");
				vistaCodigo.getEntrada().setFocus(true);
				return false;
			} else if (!txtNroControl.getText().equals(
					servicio.generarHash(codigo.getValue()))) {
				Messagebox.show("El codigo no coincide", "Error",
						Messagebox.OK, Messagebox.ERROR);
				return false;
			}
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			Messagebox.show("El texto debe estar en formato numérico", "Error",
					Messagebox.OK, Messagebox.ERROR);
			return false;
		}
	}
}
