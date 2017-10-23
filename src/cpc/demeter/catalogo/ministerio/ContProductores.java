package cpc.demeter.catalogo.ministerio;

import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.hibernate.Hibernate;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Jasperreport;

import com.sun.org.apache.bcel.internal.generic.IfInstruction;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.controlador.ministerio.ContProductor;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.demeter.vistas.ClienteAdministrativoView;
import cpc.modelo.demeter.vistas.ProductorView;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.negocio.ministerio.basico.NegocioProductor;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContProductores extends ContCatalogo<ProductorView> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContProductores(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioProductor servicios = NegocioProductor.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Productores", servicios.getProductoresView(), app);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Tipo", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getTipo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre", 250);
		titulo.setMetodoBinder("getNombre");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Id.Legal", 100);
		titulo.setMetodoBinder("getRif");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Organizacion", 150);
		titulo.setMetodoBinder("getOrganizacion");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Ingreso", 100);
		titulo.setMetodoBinder("getIngreso");
		titulo.setOrdenable(true);
		encabezado.add(titulo);
		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion == Accion.IMPRIMIR_TODO) {
				NegocioProductor servicio = NegocioProductor.getInstance();
				List<Productor> productores = servicio.getTodosProductores();
				for (Productor productor1 : productores) {
					servicio.getPersistencia().getDatos(productor1.getId());
					// Hibernate.initialize(productor1.getClienteAdministrativo().getCliente().getTelefonos());
				}
				imprimirproductores(productores);
			}

			if (accion <= Accion.CONSULTAR) {
				Productor productor = getProductor();
				if (productor == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un productor");
				else
					new ContProductor(accion, productor, this, app);
			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un tipo de riego");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void imprimirproductores(List<Productor> productores)
			throws ExcAccesoInvalido, ExcFiltroExcepcion {
		HashMap parametros = new HashMap();
		parametros.put("usuario", app.getUsuario().toString());
		parametros.put("logo", Index.class.getResource("/").getPath()
				+ "../../imagenes/cintillo_inst.png");
		JRDataSource ds = new JRBeanCollectionDataSource(productores);
		app.agregarReporte();
		Jasperreport reporte = app.getReporte();
		reporte.setSrc("reportes/listadodeproductores.jasper");
		reporte.setParameters(parametros);
		reporte.setDatasource(ds);
		reporte.setType(getTipoReporte());
	}
 

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

 
	public Productor getProductor() {
		NegocioProductor servicios = NegocioProductor.getInstance();
		ProductorView productorView = super.getDatoSeleccionado();
		if(productorView != null)
			return  servicios.buscarId(productorView.getId());
		else
			return null;
	}

	@Override
	public List cargarDato(ProductorView dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void agregarDato(Object dato) {
		if(dato instanceof Productor){
			Productor productor = (Productor) dato;
			ProductorView view = new ProductorView();
			view.setId(productor.getId());
			view.setIngreso(productor.getSrtFechaIngreso());
			view.setNombre(productor.getNombres());
			view.setOrganizacion(productor.getStrOrganizacion());
			view.setRif(productor.getIdentidadLegal());
			view.setTipo(productor.getStrTipo());
			view.setTipo_id(productor.getTipo().getId());
			super.agregarDato(view);
		}
	}
 
	@Override
	public void editarDato(Object dato) {
		if(dato instanceof Productor){
			Productor productor = (Productor) dato;
			ProductorView view = new ProductorView();
			view.setId(productor.getId());
			view.setIngreso(productor.getSrtFechaIngreso());
			view.setNombre(productor.getNombres());
			view.setOrganizacion(productor.getStrOrganizacion());
			view.setRif(productor.getIdentidadLegal());
			view.setTipo(productor.getStrTipo());
			view.setTipo_id(productor.getTipo().getId());
			super.editarDato(view);
		}		
	}
}
