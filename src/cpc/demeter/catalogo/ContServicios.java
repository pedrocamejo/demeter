package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;





import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Script;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.ContServicio;
import cpc.modelo.demeter.basico.Servicio;
import cpc.negocio.demeter.basico.NegocioServicio;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContServicios extends ContCatalogo<Servicio> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContServicios(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioServicio negocioFacturas = NegocioServicio.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Servicios", negocioFacturas.getTodos(), app);

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Tipo de Servicio", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipoServicio");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Servicio", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Unidad Media", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipoUnidad");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				Servicio servicio = getDatoSeleccionado();

				if (servicio == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Servicio");
				else {
					NegocioServicio negocio = NegocioServicio.getInstance();
					negocio.setServicio(servicio);
					new ContServicio(accion, negocio.getModelo(), this, app);
				}
			}
			if (accion == Accion.IMPRIMIR_TODO) {
				imprimirListadoServicios();
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un Servicio");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(Servicio arg0) {
		return null;
	}

	private void imprimirListadoServicios() {
		Script scrip = new Script();
		scrip.setType("text/javascript");
		String icaro = (String) SpringUtil.getBean("icaro");
		StringBuilder cadena = new StringBuilder();
		cadena.append(icaro);
		cadena.append("rpt_serviciosaprestarsede&sede=");
		cadena.append(app.getSede().getNombre());
		cadena.append("&jdbcUrl=");
		cadena.append(SessionDao.getConfiguration().getProperty(
				"hibernate.connection.url"));
		cadena.append("'); ");
		scrip.setContent(cadena.toString());
		this.app.agregar(scrip);

	}

}
