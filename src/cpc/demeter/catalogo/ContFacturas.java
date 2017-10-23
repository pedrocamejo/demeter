package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;





import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Script;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.administrativo.ContFactura;
import cpc.demeter.controlador.administrativo.ContFacturaProcesar;
import cpc.demeter.controlador.administrativo.ContFacturaSedes;
import cpc.demeter.controlador.administrativo.ContFacturaVieja;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.negocio.demeter.administrativo.NegocioFactura;
import cpc.persistencia.SessionDao;
import cpc.test.serviciotecnico;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.ventanas.CompVentanaEntrada;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContFacturas extends ContCatalogo<DocumentoFiscal> implements
		EventListener {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;
	private CompVentanaEntrada vistaNroControl = new CompVentanaEntrada(this);

	public ContFacturas(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioFactura servicios = NegocioFactura.getInstance();
		this.app = app;
		//dibujar(accionesValidas, "FACTURAS", servicios.getTodos(), app);
		dibujar(accionesValidas, "FACTURAS", servicios.getTodosProject(), app);
		desactivarTipoReporte();
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Nº Control", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nº Factura", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrNroDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Contrato", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrNroContrato");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cliente", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreBeneficiario");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Ced/Rif", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrIdentidadLegalBeneficiario");
		encabezado.add(titulo);
		
		
		titulo = new CompEncabezado("Estado", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto Total", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTotal");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Saldo", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrSaldo");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		System.out.println(event.getName());
		Component a = event.getTarget();
		System.out.println(event.getTarget());
		
		try
		{
			this.app.limpiarReporteExterno();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		try {
			if (event.getTarget() == vistaNroControl.getAceptar())
			{
				DocumentoFiscal factura = getDatoSeleccionado();
			
				if (nroControlValido(factura, vistaNroControl.getEntrada())) {
					vistaNroControl.close();
					NegocioFactura servicios = NegocioFactura.getInstance();
					factura = servicios.getDocumento(getDatoSeleccionado());
					ContFactura.imprimir(factura, app);
				}
				
			} else {
				int accion = (Integer) event.getTarget().getAttribute("pos");
				
				if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR) 
				{
					//	DocumentoFiscal factura = getDatoSeleccionado();
					NegocioFactura servicios = NegocioFactura.getInstance();
					DocumentoFiscal factura = servicios.getDocumento(getDatoSeleccionado());
					new ContFactura(accion, factura, this, app);
				} 
				else if (accion == Accion.ASOCIAR) {
					new ContFacturaVieja(Accion.AGREGAR, new DocumentoFiscal(),this, app);
				}
				else if (accion == Accion.CORREGIR) {
					new ContFacturaSedes(Accion.AGREGAR, new DocumentoFiscal(),this, app);
				}
				else if (accion == Accion.IMPRIMIR_ITEM) {
					// DocumentoFiscal factura = getDatoSeleccionado();
					mostrarVistaNroControl(app);
				}
				else if (accion == Accion.IMPRIMIR_TODO) {
					Imprimir(accion);
				} 
				else if(accion == Accion.PROCESAR)
				{
					NegocioFactura servicios = NegocioFactura.getInstance();
					DocumentoFiscal factura = servicios.getDocumento(getDatoSeleccionado());
					if(!factura.isCancelada() && factura.getEstado().getId().equals(1))
					{
						new ContFacturaProcesar(accion, factura, this, app);
					}
					else
					{
						Messagebox.show(" No puede Procesar esta Factura "+factura.getStrEstado() +" --- "+ factura.getStrCancelado() );
					}
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	}

	public List cargarDato(DocumentoFiscal dato) {
		return null;
	}

	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

	public void Imprimir(int accion) {
		Script scrip = new Script();
		scrip.setType("text/javascript");
		
		
		String icaro = (String) SpringUtil.getBean("icaro");
		StringBuilder cadena = new StringBuilder();
		cadena.append(icaro);
		cadena.append("rpt_saldofacturas");
		cadena.append("&jdbcUrl=");
		cadena.append(SessionDao.getConfiguration().getProperty("hibernate.connection.url"));
		cadena.append("'); ");
		scrip.setContent(cadena.toString());
		this.app.agregar(scrip);
	}

	public void mostrarVistaNroControl(AppDemeter app) 
	{
		vistaNroControl = new CompVentanaEntrada(this);
		vistaNroControl.setTitle("Verificación del Nº de Control");
		vistaNroControl.settextopregunta("N° de Control");
		app.agregarHija(vistaNroControl);
	}

	public boolean nroControlValido(DocumentoFiscal documento,Textbox txtNroControl) throws InterruptedException
	{
		try {
			if (txtNroControl.getText().trim().isEmpty()) 
			{
				app.mostrarError("El nro de control no puede estar vacío");
				vistaNroControl.getEntrada().setFocus(true);
				return false;
			}
			else if (documento.getNroDocumento().compareTo(Integer.valueOf(txtNroControl.getText())) != 0)
			{
				Messagebox.show("El Nº de Control ingresado no coincide con el de la factura","Error", Messagebox.OK, Messagebox.ERROR);
				return false;
			}
			return true;
		} catch (NumberFormatException e)
		{
			Messagebox.show("El texto debe estar en formato numérico", "Error",Messagebox.OK, Messagebox.ERROR);
			return false;
		}
	}
}
