package cpc.demeter.catalogo;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.ContTipoDocumentoTierra;
import cpc.modelo.demeter.basico.TipoDocumentoTierra;
import cpc.negocio.demeter.basico.NegocioTipoDocumentoTierra;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContTiposDocumentosPropiedad extends
		ContCatalogo<TipoDocumentoTierra> implements
		ICompCatalogo<TipoDocumentoTierra> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5076693812681242345L;
	private AppDemeter app;

	public ContTiposDocumentosPropiedad(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		super();
		NegocioTipoDocumentoTierra cargos = NegocioTipoDocumentoTierra
				.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Documentos propiedad Tierra",
				cargos.getTodos(), app);
	}

	public ContTiposDocumentosPropiedad() {
		super();
	}

	public ContTiposDocumentosPropiedad(int modoOperacion) {
		super(modoOperacion);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		// titulo = new CompEncabezado("Id",20);
		// titulo.setOrdenable(true);
		// titulo.setMetodoBinder("getId");
		// encabezado.add(titulo);

		titulo = new CompEncabezado("Descripcion", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				NegocioTipoDocumentoTierra servicio = NegocioTipoDocumentoTierra
						.getInstance();
				TipoDocumentoTierra tipo = getDatoSeleccionado();
				servicio.setModelo(tipo);
				if (tipo == null && accion != Accion.AGREGAR)
					app.mostrarError("Seleccione un Tipo");
				else
					new ContTipoDocumentoTierra(accion, servicio.getModelo(),
							this, app);
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
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(TipoDocumentoTierra arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}