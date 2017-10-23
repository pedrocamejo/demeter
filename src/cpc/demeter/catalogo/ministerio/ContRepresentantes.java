package cpc.demeter.catalogo.ministerio;

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
import cpc.demeter.controlador.ministerio.ContRepresentante;
import cpc.modelo.ministerio.gestion.Representante;
import cpc.negocio.ministerio.basico.NegocioRepresentante;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContRepresentantes extends ContCatalogo<Representante> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContRepresentantes(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioRepresentante negocioFacturas = NegocioRepresentante
				.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Representantes", negocioFacturas.getTodos(),
				app);

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Cedula", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCedula");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombres", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombres");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Apellidos", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getApellidos");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				Representante representante = getDatoSeleccionado();

				if (representante == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Servicio");
				else {
					NegocioRepresentante negocio = NegocioRepresentante
							.getInstance();
					negocio.setRepresentante(representante);
					new ContRepresentante(accion, negocio.getModelo(), this,
							app);
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
	public List cargarDato(Representante arg0) {
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
