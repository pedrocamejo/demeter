package cpc.demeter.catalogo.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.mantenimiento.ContPlantaDistribuidora;
import cpc.modelo.demeter.mantenimiento.PlantaDistribuidora;
import cpc.negocio.demeter.mantenimiento.NegocioPlantaDistribuidora;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class CatPlantaDistribuidora extends ContCatalogo<PlantaDistribuidora> {

	private static final long serialVersionUID = 1693946024302981254L;
	private AppDemeter app;
	private NegocioPlantaDistribuidora negocio;

	public CatPlantaDistribuidora(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		super();
		negocio = NegocioPlantaDistribuidora.getInstance();
		this.app = app;
		dibujar(accionesValidas, "PLANTA MAQUINARIA", negocio.getPlantas(), app);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Id", 20);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado(" Rif ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado(" Telefonos ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getTelefonos");
		encabezado.add(titulo);


		titulo = new CompEncabezado(" Clasificacion ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipo");
		encabezado.add(titulo);

		
		
		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {

			int accion = (Integer) event.getTarget().getAttribute("pos");

			PlantaDistribuidora dato = getDatoSeleccionado();

			if (dato == null && accion != Accion.AGREGAR) {
				app.mostrarError("Debe seleccionar un dato del catalago");
			} else {
				if (accion == Accion.AGREGAR || accion == Accion.EDITAR
						|| accion == Accion.CONSULTAR) {
					new ContPlantaDistribuidora(accion, dato, this, app);
				}

			}

		} catch (NullPointerException e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun registro");
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(PlantaDistribuidora arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

}
