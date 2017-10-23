package cpc.demeter.comando.reporte;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Button;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Messagebox;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import cpc.ares.interfaz.IAplicacion;
import cpc.ares.interfaz.IComando;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.Index;
import cpc.demeter.comando.ContValidarSolvencia;
import cpc.demeter.vista.reporte.EstadoCuentaUI;
import cpc.modelo.demeter.administrativo.ControlSede;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.ministerio.basico.TipoProductor;
import cpc.modelo.ministerio.dimension.UbicacionEstado;
import cpc.modelo.ministerio.dimension.UbicacionMunicipio;
import cpc.modelo.ministerio.dimension.UbicacionParroquia;
import cpc.modelo.ministerio.dimension.UbicacionSector;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.modelo.sigesp.basico.CuentaBancaria;
import cpc.negocio.demeter.NegocioCuentaBancaria;
import cpc.negocio.demeter.NegocioReporteCodigo;
import cpc.negocio.demeter.administrativo.NegocioClienteAdministrativo;
import cpc.negocio.demeter.administrativo.NegocioFactura;
import cpc.negocio.ministerio.basico.NegocioEje;
import cpc.persistencia.demeter.implementacion.basico.PerTipoProducto;
import cpc.persistencia.ministerio.basico.PerTipoProductor;
import cpc.zk.componente.interfaz.IZkAplicacion;

public class ComandoEstadosCuenta implements IComando, EventListener {

	private static final long serialVersionUID = -2688081335887322152L;
	private AccionFuncionalidad funcionalidades;
	private Map<String, Object> mapa = new Hashtable<String, Object>();
	private int id;
	private IZkAplicacion app;

	private EstadoCuentaUI 		vista;

	private NegocioEje	negocioEje = NegocioEje.getInstance();
	
	private List<DocumentoFiscal> documentos = new ArrayList<DocumentoFiscal>();
	
	private List<TipoProductor> tipoProductores = new ArrayList<TipoProductor>();

	
	public void ejecutar() {
		try {
			
			vista = new EstadoCuentaUI("Estados de Cuenta ","none",true,this);
			vista.setearTipoProductores(new PerTipoProductor().getAll());
			app.agregarHija(vista);
			vista.doModal();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 
	public IAplicacion getApp() {
		return app;
	}

	public void setApp(IAplicacion arg0) {
		this.app = (IZkAplicacion) arg0;
	}

	public Map<String, Object> getParametros() {
		// TODO Auto-generated method stub
		return mapa;
	}

	public void setParametros(Map<String, Object> parametros) {
		this.mapa = parametros;
	}

	public void agregarParametro(String codigo, Object valor) {
		mapa.put(codigo, valor);

	}

	public AccionFuncionalidad getAccionFuncionalidad() {

		return funcionalidades;
	}

	public void setAccionFuncionalidad(AccionFuncionalidad funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

	public Object seleccionar(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void onEvent(Event event) throws Exception {

		if (event.getTarget() == vista.getGenerar())
		{  
			//se verifica que al menos hay un tipo de productor seleccionado 
			List aux = vista.getTipoProductor().getModeloSelect();
			if (vista.getTipoProductor().getModeloSelect().size()==0)
				throw new WrongValueException(vista.getTipoProductor(),
						"seleccione al menos un tipo de productor");
			
			try {
				imprimir(vista.getTipoProductor().getModeloSelect());
			} catch (Exception e) {
				e.printStackTrace();
				app.mostrarError(e.getMessage());
			}
		}
		///////////////////////////////////// Generar Listado General Deudores ///////////////////////////////////////
		else if(event.getTarget() == vista.getListadoDeudor())
		{
			List<Productor> deudores = vista.getListadeudores();
			NegocioClienteAdministrativo negocio = NegocioClienteAdministrativo.getInstance();
			List<Productor> productores  = negocio.getProductoresConDeuda();
			List<Object[]> documentos = negocio.getDeudasClientes(productores);

			try {
				NegocioFactura negocioFactura = NegocioFactura.getInstance();
				HashMap parametros = new HashMap();
				ControlSede control = negocioFactura.getControlSede();
				parametros.put("usuario", app.getUsuario().toString());
				parametros.put("logo", Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");
				parametros.put("sede",control.getStrSede());

				JRDataSource ds = null;
				ds = new JRBeanCollectionDataSource(documentos);
				app.agregarReporte();
				Jasperreport reporte = app.getReporte();
				reporte.setSrc("reportes/reporteconsolidadodedeudas.jasper");
				reporte.setParameters(parametros);
				reporte.setDatasource(ds);
				reporte.setType("pdf");
				
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("Error al Intentar Generar el Reporte ");
			}

		}
		///////////////////////////////////// Generar Listado General Solvencia ///////////////////////////////////////
		else if(event.getTarget() == vista.getListadoSolvencia())
		{
			NegocioClienteAdministrativo negocio = NegocioClienteAdministrativo.getInstance();
			List<Productor> solventes  = negocio.getProductoresConDeuda();
			 
			NegocioFactura negocioFactura = NegocioFactura.getInstance();
			ControlSede control = negocioFactura.getControlSede();
			HashMap parametros = new HashMap();
			parametros.put("usuario", app.getUsuario().toString());
			parametros.put("logo",Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");
			parametros.put("sede",control.getStrSede());
			
			JRDataSource ds = null;
			ds = new JRBeanCollectionDataSource(solventes);
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();
			reporte.setSrc("reportes/listadoSolvencia.jasper");
			reporte.setParameters(parametros);
			reporte.setDatasource(ds);
			reporte.setType("pdf");
		}
		///////////////////////////////////// Generar Solvencia Individual ///////////////////////////////////////
		else if(event.getTarget() == vista.getGenerarSolvencia())
		{
			try {
				NegocioReporteCodigo negocioReporteCodigo = NegocioReporteCodigo.getInstance(); 
				List<Productor> solventes = vista.getListsolventes();
				NegocioCuentaBancaria cuentas = NegocioCuentaBancaria.getInstance();
				NegocioFactura negocio = NegocioFactura.getInstance();

				String a = new String();
				Date fecha = new Date();
				HashMap parametros = new HashMap();
				parametros.put("usuario", app.getUsuario().toString());
				parametros.put("logo", Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");
				parametros.put("sede",negocio.getControlSede().getStrSede());
				parametros.put("coordinador",negocio.getControlSede().getNombreCoordinadorSede());
				parametros.put("celula",negocio.getControlSede().getCedulaCoordinador());
				parametros.put("fecha",fecha);

				//fecha y identidad del productor en MD5 
				for(Productor p : solventes)
				{
					negocioReporteCodigo.guadar(p.getIdentidadLegal(),fecha,app.getUsuario().toString());
				}	

				JRDataSource ds = null;
				ds = new JRBeanCollectionDataSource(solventes);
				app.agregarReporte();
				Jasperreport reporte = app.getReporte();
				reporte.setSrc("reportes/clientesolventes.jasper");
				reporte.setParameters(parametros);
				reporte.setDatasource(ds);
				reporte.setType("pdf");
			
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("Error al Intentar Generar el Reporte ");
			}

		}
		///////////////////////////////////// Deudores ///////////////////////////////////////
		else if( event.getTarget() == vista.getGenerarDeudor())
		{
			List<Productor> deudores = vista.getListadeudores();
			//cargar facturas y listo el pollo 
			NegocioClienteAdministrativo negocio = NegocioClienteAdministrativo.getInstance();
			List<Object[]> documentos = negocio.getDeudasClientes(deudores);
			try {
				NegocioCuentaBancaria cuentas = NegocioCuentaBancaria.getInstance();
				String a = new String();
				HashMap parametros = new HashMap();
				parametros.put("usuario", app.getUsuario().toString());
				parametros.put("logo", Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");

				List<CuentaBancaria> b = cuentas.CuentaBancarias("007");
				for (CuentaBancaria cuentaBancaria : b)
				{   if (cuentaBancaria != null)
					{
						String c = (cuentaBancaria.getStrBanco() + " " + " " + cuentaBancaria.getNroCuenta() + " " + "<br>");
						if (c != null) {a = a + c;}
					}
				}
				parametros.put("cuenta", a);
				JRDataSource ds = null;
				ds = new JRBeanCollectionDataSource(documentos);
				app.agregarReporte();
				Jasperreport reporte = app.getReporte();
				reporte.setSrc("reportes/reporteconsolidadodedeudas.jasper");
				reporte.setParameters(parametros);
				reporte.setDatasource(ds);
				reporte.setType("pdf");
			
			} catch (Exception e) {
				e.printStackTrace();
				throw new Exception("Error al Intentar Generar el Reporte ");
			}
			
			
		}
		///////////////////////////  Operaciones para Cargar los listados  ////////////////////////////////////////////
		else if (event.getTarget() == vista.getAddSolvencia())
		{
			NegocioClienteAdministrativo negocio = NegocioClienteAdministrativo.getInstance();
			List<Productor> solventes  = negocio.getProductoresSinDeuda();
			new ContSeleccionarProductor(1,this,this.app, solventes);
		}
		else if (event.getTarget() == vista.getAddDeudor())
		{
			NegocioClienteAdministrativo negocio = NegocioClienteAdministrativo.getInstance();
			List<Productor> solventes  = negocio.getProductoresConDeuda();
			new ContSeleccionarProductor(2,this,this.app, solventes);
		}
		else if (event.getTarget() == vista.getDelDeudor())
		{
			if( vista.getDeudores().getSelectedItem() != null)
			{
				Productor p = (Productor) vista.getDeudores().getSelectedItem().getValue();
				vista.quitarDeudor(p);
			}
		}
		else if (event.getTarget() == vista.getDelSolvencia())
		{
			if( vista.getSolventes().getSelectedItem() != null)
			{
				Productor p = (Productor) vista.getSolventes().getSelectedItem().getValue();
				vista.quitarSolvente(p);
			}
		}
		///////////////////////////////////////////////////////////////////////
		else if(event.getTarget() == vista.getEstado())
		{
			if(vista.getEstado().getSelectedItem() != null)
			{
				documentos = new ArrayList<DocumentoFiscal>();
				vista.getMunicipio().setModel(new ListModelArray(negocioEje.getMunicipios((UbicacionEstado)vista.getEstado().getSelectedItem().getValue())));
			}
		}
		else if(event.getTarget() == vista.getMunicipio())
		{
			if(vista.getMunicipio().getSelectedItem() != null)
			{
				documentos = new ArrayList<DocumentoFiscal>();
				vista.getParroquia().setModel(new ListModelArray(negocioEje.getParroquias((UbicacionMunicipio)vista.getMunicipio().getSelectedItem().getValue())));
			}
		}
		else if(event.getTarget() == vista.getParroquia())
		{
			if(vista.getParroquia().getSelectedItem() != null)
			{
				documentos = new ArrayList<DocumentoFiscal>();
				vista.getUbicacionsector().setModel(new ListModelArray(negocioEje.getSectores((UbicacionParroquia)vista.getParroquia().getSelectedItem().getValue())));
			}
		}
		else if(event.getTarget() == vista.getUbicacionsector())
		{
			if(vista.getUbicacionsector().getSelectedItem() != null)
			{
				NegocioClienteAdministrativo negocio = NegocioClienteAdministrativo.getInstance();
				UbicacionSector sector = (UbicacionSector) vista.getUbicacionsector().getSelectedItem().getValue();
				documentos = negocio.getPendientes(sector); 
				vista.getTotalElementosMostrar().setValue("Sector "+sector.getNombre()+" Total : ("+documentos.size()+") elementos");
			}
			else
			{
				documentos = new ArrayList<DocumentoFiscal>();
			}
				
		}
		else if(event.getTarget()== vista.getSector())
		{
			if(vista.getUbicacionsector().getSelectedItem() != null)
			{
				UbicacionSector sector = (UbicacionSector) vista.getUbicacionsector().getSelectedItem().getValue();
				try {

					NegocioClienteAdministrativo negocio = NegocioClienteAdministrativo.getInstance();
					NegocioCuentaBancaria cuentas = NegocioCuentaBancaria.getInstance();
					String a = new String();
					HashMap parametros = new HashMap();
					parametros.put("usuario", app.getUsuario().toString());
					parametros.put("logo", Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");

					List<CuentaBancaria> b = cuentas.CuentaBancarias("007");
					
					for (CuentaBancaria cuentaBancaria : b)
					{
						if (cuentaBancaria != null)
						{
							String c = (cuentaBancaria.getStrBanco() + " " + " " + cuentaBancaria.getNroCuenta() + " " + "<br>");
							if (c != null)
							{
								a = a + c;
							}
						}
					}
					parametros.put("cuenta", a);
					JRDataSource ds = null;

						ds = new JRBeanCollectionDataSource(documentos);
						app.agregarReporte();
						Jasperreport reporte = app.getReporte();
						reporte.setSrc("reportes/clienteAdministrativoFiltro.jasper");
						reporte.setParameters(parametros);
						reporte.setDatasource(ds);
						reporte.setType("pdf");
				
				} catch (Exception e) {
					e.printStackTrace();
					throw new Exception("Error al Intentar Generar el Reporte ");
				}
			}
		}
	}

	@SuppressWarnings({ "unchecked" })
	public void imprimir(List<TipoProductor > productores) throws Exception {
		try {
	
			NegocioClienteAdministrativo negocio = NegocioClienteAdministrativo.getInstance();
			NegocioCuentaBancaria cuentas = NegocioCuentaBancaria.getInstance();
	
			String a = new String();
			
			HashMap parametros = new HashMap();
			parametros.put("usuario", app.getUsuario().toString());
			parametros.put("logo", Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");

			List<CuentaBancaria> b = cuentas.CuentaBancarias("007");
			
			for (CuentaBancaria cuentaBancaria : b)
			{
				if (cuentaBancaria != null)
				{
					String c = (cuentaBancaria.getStrBanco() + " " + " " + cuentaBancaria.getNroCuenta() + " " + "<br>");
					if (c != null)
					{
						a = a + c;
					}
				}
			}
			parametros.put("cuenta", a);
			JRDataSource ds = null;

			if(vista.getTipo().getSelectedItem() != null )
			{  
				Object tipo = vista.getTipo().getSelectedItem().getValue();
				
				if ( tipo == "Estados de cuenta documentos asociados"|| tipo =="Estados de cuenta total"||tipo== "Estados de cuenta")
				{					
				if (vista.getTipo().getSelectedItem().getValue() =="Estados de cuenta documentos asociados")
				{
					ds = new JRBeanCollectionDataSource(negocio.getPendienteAsociados(productores));
				}
				if (vista.getTipo().getSelectedItem().getValue()  =="Estados de cuenta total")
				{
					ds = new JRBeanCollectionDataSource(negocio.getPendienteTotalAsociados(productores));
				}
				if ( vista.getTipo().getSelectedItem().getValue() =="Estados de cuenta")
				{
					ds = new JRBeanCollectionDataSource(negocio.getPendienteNoAsociados(productores));
				}
				
				app.agregarReporte();
				Jasperreport reporte = app.getReporte();
				reporte.setSrc("reportes/reporteconsolidadodedeudas.jasper");
				reporte.setParameters(parametros);
				reporte.setDatasource(ds);
				reporte.setType("pdf");
			} else {
			
				if (vista.getTipo().getSelectedItem().getValue() =="Consolidados Total")
				{
					ds = new JRBeanCollectionDataSource(negocio.getPendienteConsolidado(productores));
				}
				if (vista.getTipo().getSelectedItem().getValue() =="Consolidados doc asociados")
				{
					ds = new JRBeanCollectionDataSource(negocio.getPendienteAsociadosConsolidado(productores));
				}
				if (vista.getTipo().getSelectedItem().getValue() =="Consolidados sin documentos asociados")
				{
					ds = new JRBeanCollectionDataSource(negocio.getPendienteNoAsociadosConsolidado(productores));
				}
				parametros.put("tipoProductor", productores.toString());
				app.agregarReporte();
				Jasperreport reporte = app.getReporte();
				reporte.setSrc("reportes/clienteAdministrativoConsolidadoXls.jasper");
				parametros.put("exportParameter", app.getXLSParameters(false));
				reporte.setParameters(parametros);
				reporte.setDatasource(ds);
				reporte.setType("xls");
			}
				
				
				}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Error al Intentar Generar el Reporte ");
		}
	}

	public void agregarSolvente(  Productor productor) {
		// TODO Auto-generated method stub
		vista.agregarSolvente(productor);
	}

	public void agregarDeudor( Productor productor) {
		// TODO Auto-generated method stub
		vista.agregarDeudor(productor);
	}

	
	public EventListener validarSolvencia()
	{
		return new EventListener() {
			
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				new ContValidarSolvencia(1,ComandoEstadosCuenta.this,ComandoEstadosCuenta.this.app);
			}
		};
	}
	
}
