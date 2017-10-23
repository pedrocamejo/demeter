package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.controlador.administrativo.ContClienteAdministrativo;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.demeter.vistas.ClienteAdministrativoView;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.modelo.sigesp.basico.CuentaBancaria;
import cpc.negocio.demeter.NegocioCuentaBancaria;
import cpc.negocio.demeter.administrativo.NegocioClienteAdministrativo;
import cpc.negocio.demeter.administrativo.NegocioRecibo;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.ventanas.CompVentanaDesdeHasta;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContClientesAdministrativos extends
		ContCatalogo<ClienteAdministrativoView> implements EventListener {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;
	private CompVentanaDesdeHasta vistaReporte;
	private NegocioClienteAdministrativo servicios;
	
	public ContClientesAdministrativos(AccionFuncionalidad accionesValidas, AppDemeter app) 
			throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		this.app = app;
		servicios = NegocioClienteAdministrativo.getInstance();
		dibujar(accionesValidas, "CLIENTES ADMINISTRATIVO",servicios.getTodosView(), app);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Cedula/RIF", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getRif");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Razon Social", 300);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cuenta Cobro", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCuentaCobro");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cuenta Pago", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCuentaPago");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			Integer accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion != null) {

				ClienteAdministrativo cliente = getClienteAdminsitrativo();
				if (accion <= Accion.CONSULTAR) {
					new ContClienteAdministrativo(accion, cliente, this, app);
				}
				if (accion == Accion.IMPRIMIR_ITEM)
					menuImprimirCliente();
				else if (accion == Accion.IMPRIMIR_TODO)
					menuImprimir();
			} else
				imprimirClientes();
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(ClienteAdministrativoView dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public void imprimirClientes() throws ExcAccesoInvalido, ExcDatosNoValido {

		if (vistaReporte.getOtro().getSeleccion() == "listado de cuentas contables") {
			HashMap parametros = new HashMap();
			NegocioClienteAdministrativo negocio = NegocioClienteAdministrativo.getInstance();
			List<ClienteAdministrativo> precios = negocio.getTodos();
			JRDataSource ds = new JRBeanCollectionDataSource(precios);
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();
			reporte.setSrc("reportes/ListadoClientes.jasper");
			parametros.put("usuario", app.getUsuario().toString());
			parametros.put("logo", Index.class.getResource("/").getPath()
					+ "../../imagenes/cintillo_inst.png");
			reporte.setParameters(parametros);
			reporte.setDatasource(ds);
			reporte.setType(getTipoReporte());
		} 
		else if (vistaReporte.getOtro().getSeleccion() == "saldo de cliente") {
			imprimirExpediente();
		}
		else if (vistaReporte.getOtro().getSeleccion() == "Operaciones de Cliente") {
			imprimirCliente();
		}
		else {
			HashMap parametros = new HashMap();
			NegocioClienteAdministrativo negocio = NegocioClienteAdministrativo
					.getInstance();
			List<DocumentoFiscal> operaciones = negocio.getoperacionescliente();
			JRDataSource ds = new JRBeanCollectionDataSource(operaciones);
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();
			reporte.setSrc("reportes/rpt_operacionesporcliente.jasper");
			parametros.put("usuario", app.getUsuario().toString());
			parametros.put("logo", Index.class.getResource("/").getPath()
					+ "../../imagenes/cintillo_inst.png");
			reporte.setParameters(parametros);
			reporte.setDatasource(ds);
			reporte.setType(getTipoReporte());
		}
	}

	@SuppressWarnings("rawtypes")
	public void menuImprimir() throws ExcFiltroExcepcion {
		List<String> tipo = new ArrayList<String>();
		tipo.add("saldo");
		tipo.add("listado de cuentas contables");
		vistaReporte = new CompVentanaDesdeHasta(this);
		vistaReporte.setTexto("Menu Clientes");
		vistaReporte.setSoloOtro("tipo:", tipo);
		app.agregarHija(vistaReporte);
	}

	
	@SuppressWarnings("rawtypes")
	public void menuImprimirCliente() throws ExcFiltroExcepcion {
		List<String> tipo = new ArrayList<String>();
		tipo.add("saldo de cliente");
		tipo.add("Operaciones de Cliente");
		vistaReporte = new CompVentanaDesdeHasta(this);
		vistaReporte.setTexto("Menu Clientes");
		vistaReporte.setSoloOtro("tipo:", tipo);
		app.agregarHija(vistaReporte);
	}
	
	
	@SuppressWarnings("unchecked")
	public void imprimirCliente() throws ExcAccesoInvalido, ExcDatosNoValido {
		HashMap parametros = new HashMap();
		ClienteAdministrativo cliente = getClienteAdminsitrativo();
		if (cliente == null) {
			app.mostrarError("No ha indicado Expediente");
			return;
		}

		NegocioClienteAdministrativo negocio = NegocioClienteAdministrativo.getInstance();
		NegocioRecibo negocioRecibo = NegocioRecibo.getInstance();
		List<DocumentoFiscal> operaciones = negocio.getoperacionescliente(cliente.getCliente());
		List<Recibo> recibos = negocioRecibo.getRecibos(cliente.getCliente());
		List<Object[]> objetos = new ArrayList<Object[]>();

		Object[] rCliente = new Object[3] ;
		rCliente[0]=cliente;
		rCliente[1]=operaciones;
		rCliente[2]=recibos;
		
		objetos.add(rCliente);
		JRDataSource ds = new JRBeanCollectionDataSource(objetos);
		app.agregarReporte();
		Jasperreport reporte = app.getReporte();
		reporte.setSrc("reportes/OperacionesPorCliente2.jasper");
		parametros.put("usuario", app.getUsuario().toString());
		parametros.put("logo", Index.class.getResource("/").getPath()
				+ "../../imagenes/cintillo_inst.png");
		reporte.setParameters(parametros);
		reporte.setDatasource(ds);
		reporte.setType(getTipoReporte());
	}
	
	
	@SuppressWarnings({ "unchecked" })
	public void imprimirExpediente() {
		try {
			ClienteAdministrativo cliente = getClienteAdminsitrativo();
			NegocioClienteAdministrativo negocio = NegocioClienteAdministrativo
					.getInstance();
			NegocioCuentaBancaria cuentas = NegocioCuentaBancaria.getInstance();
			String a = new String();
			HashMap parametros = new HashMap();
			parametros.put("usuario", app.getUsuario().toString());
			parametros.put("logo", Index.class.getResource("/").getPath()
					+ "../../imagenes/cintillo_inst.png");
			List<CuentaBancaria> b = cuentas.CuentaBancarias("007");
			for (CuentaBancaria cuentaBancaria : b) {
				if (cuentaBancaria != null) {
					String c = (cuentaBancaria.getStrBanco() + " " + " "
							+ cuentaBancaria.getNroCuenta() + " " + "<br>");
					if (c != null)
						a = a + c;
				}
			}
			parametros.put("cuenta", a);
			JRDataSource ds = null;
			System.out.println(vistaReporte.getOtro().getSeleccion());
			ds = new JRBeanCollectionDataSource(negocio.getPendiente(cliente));
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();
			reporte.setSrc("reportes/clienteAdministrativoFiltro.jasper");
			reporte.setParameters(parametros);
			reporte.setDatasource(ds);
			reporte.setType("pdf");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ClienteAdministrativo getClienteAdminsitrativo() {
		ClienteAdministrativoView clienteView = super.getDatoSeleccionado();
		if(clienteView != null)
			return servicios.findById(clienteView.getId());
		else
			return null;
	}
}
