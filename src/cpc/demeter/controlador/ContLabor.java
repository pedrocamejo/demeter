package cpc.demeter.controlador;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Row;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContLabores;
import cpc.demeter.vista.UILabor;
import cpc.modelo.demeter.administrativo.Impuesto;
import cpc.modelo.demeter.basico.Labor;
import cpc.modelo.demeter.basico.Servicio;
import cpc.modelo.demeter.basico.UnidadMedida;
import cpc.modelo.demeter.basico.UsoPreciosProducto;
import cpc.modelo.ministerio.basico.TipoProductor;
import cpc.negocio.demeter.basico.NegocioArticuloVenta;
import cpc.negocio.demeter.basico.NegocioLabor;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompListaMultiple;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContLabor extends ContVentanaBase<Labor> {

	private static final long serialVersionUID = 6184414588153046382L;
	private AppDemeter app;
	private UILabor vista;
	private NegocioLabor negocio;
	private List<UsoPreciosProducto> lista;

	public ContLabor(int modoOperacion, Labor labor, ContLabores llamador,
			AppDemeter app) throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			negocio = NegocioLabor.getInstance();
			if (labor == null || modoAgregar()) {
				negocio.nuevo();
				labor = negocio.getModelo();
			} else
				labor.transferirPrecios();

			List<Servicio> servicios = negocio.getServicios();
			List<TipoProductor> tiposProductores = negocio
					.getTiposProductores();
			List<Impuesto> impuestos = negocio.getImpuestos();
			List<UnidadMedida> unidades = negocio.getUnidadesCobro();
			vista = new UILabor("Labor (" + Accion.TEXTO[modoOperacion] + ")",
					590, tiposProductores, servicios, impuestos, unidades);
			setear(labor, vista, llamador, app);
			vista = ((UILabor) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun Registro");
		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
		}
	}

	public void eliminar() {

		try {
			negocio = NegocioLabor.getInstance();
			negocio.setModelo(getDato());
			negocio.borrar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void anular() {
		try {
			negocio = NegocioLabor.getInstance();
			negocio.setModelo(getDato());
			getDato().setActivo(false);
			negocio.guardar(getDato().getId());
			vista.close();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {
		try {
			negocio = NegocioLabor.getInstance();
			UsoPreciosProducto.getMapaPrecios(getDato().getPrecios(), lista,
					getDato());
			negocio.setModelo(getDato());
			getDato().setActivo(vista.getActivo().isChecked());
			negocio.guardar(negocio.getModelo().getId());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError("Error" + e.getMessage());
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR) {
			if ((vista.getDescripcion().getValue() == null || vista
					.getDescripcion().getValue() == "")) {
				throw new WrongValueException(vista.getDescripcion(),
						"Indique una Descripcion");
			} else if (vista.getDescripcion().getValue().length() > 249) {
				throw new WrongValueException(vista.getDescripcion(),
						"Descripción muy larga");
			}
			if ((vista.getServicio().getSeleccion() == null)) {
				throw new WrongValueException(vista.getServicio(),
						"Indique un servicio");
			}
			if ((vista.getUnidadGestion().getSeleccion() == null)) {
				throw new WrongValueException(vista.getUnidadGestion(),
						"Indique un Tipo de Unidad de Medida");
			}
			if ((vista.getUnidadCobro().getSeleccion() == null)) {
				throw new WrongValueException(vista.getUnidadCobro(),
						"Indique un Tipo de Unidad de Medida");
			}
			if ((vista.getImpuesto().getSeleccion() == null)) {
				throw new WrongValueException(vista.getImpuesto(),
						"Indique un Impuesto");
			}

			try {
				negocio = NegocioLabor.getInstance();
				extrarDatos();
				if (!UsoPreciosProducto.verificaTodosTiposProductor(
						negocio.getTiposProductores(), lista))
					throw new WrongValueException(vista.getDetalle(),
							"No todos los Tipos de Productor estan seleccionados");
			} catch (ExcFiltroExcepcion e) {
				e.printStackTrace();
				throw new ExcEntradaInconsistente(
						"No todos los Tipos de Productor estan seleccionados");
			}
		}
	}

	public void onEvent(Event event) throws Exception {
		System.out.println("hay evento " + event.getName() + " en "
				+ event.getTarget().getClass().getName());
		if (event.getTarget() == getVista().getAceptar()) {

			validar();
			procesarCRUD(event);

			/*
			 * }else if (event.getName() == Events.ON_SELECTION &&
			 * event.getTarget() == vista.getUnidadGestion()){
			 * actualizarUnidadCobro();
			 */

		} else if (event.getName() == Events.ON_SELECTION
				&& event.getTarget() == vista.getServicio()) {
			actualizarUnidadGestion();
		} else if (event.getName() == Events.ON_SELECTION
				&& event.getTarget() == vista.getImpuesto()) {
			actualizarDatos();
		} else if (event.getName() == Events.ON_CHANGE) {
			actualizarItem(event.getTarget());
		}
	}

	public void actualizarUnidadGestion() throws ExcFiltroExcepcion {
		try {
			if (vista.getServicio().getSeleccion() != null) {
				negocio = NegocioLabor.getInstance();
				vista.refrescarUnidadGestion(negocio.getUnidadMedida(vista
						.getServicio().getSeleccion()));
				vista.getUnidadGestion().addEventListener(Events.ON_SELECTION,
						this);
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
	}

	public void actualizarUnidadCobro() throws ExcFiltroExcepcion {
		try {
			if (vista.getUnidadGestion().getSeleccion() != null) {
				negocio = NegocioLabor.getInstance();
				vista.refrescarUnidadCobro(negocio.getUnidadMedida(vista
						.getUnidadGestion().getSeleccion()));
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
	}

	public void actualizarItem(Component componente) {
		Doublebox valor = (Doublebox) componente;
		Row item = (Row) valor.getParent();
		UsoPreciosProducto modelo = (UsoPreciosProducto) item
				.getAttribute("dato");
		double imp = 0;
		try {
			imp = vista.getImpuesto().getSeleccion().getPorcentaje();
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
		modelo.setImpuesto(imp);
		modelo.setPrecio(valor.getValue());
		((Doublebox) item.getChildren().get(2)).setValue(modelo.getIva());
		((Doublebox) item.getChildren().get(3)).setValue(modelo.getTotal());
	}

	@SuppressWarnings("unchecked")
	public void actualizarDatos() {
		try {
			if (vista.getImpuesto().getSeleccion() != null) {
				// vista.getDetalle().setNuevo(new
				// UsoPreciosProducto(vista.getImpuesto().getSeleccion()));
				UsoPreciosProducto modelo;
				List<Row> filas = vista.getDetalle().getFilas().getChildren();
				for (Row item : filas) {
					modelo = (UsoPreciosProducto) item.getAttribute("dato");
					modelo.setImpuesto(vista.getImpuesto().getSeleccion()
							.getPorcentaje());
					((Doublebox) item.getChildren().get(2)).setValue(modelo
							.getIva());
					((Doublebox) item.getChildren().get(3)).setValue(modelo
							.getTotal());
				}
			}
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
	}

	@SuppressWarnings("unchecked")
	public void extrarDatos() {
		lista = new ArrayList<UsoPreciosProducto>();
		try {

			UsoPreciosProducto modelo;
			List<Row> filas = vista.getDetalle().getFilas().getChildren();
			for (Row item : filas) {
				modelo = (UsoPreciosProducto) item.getAttribute("dato");
				modelo.setTiposProductores(((CompListaMultiple) item
						.getChildren().get(0)).getModeloSelect());
				lista.add(modelo);
			}
		} catch (NullPointerException e) {

		}

	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub

	}

}
