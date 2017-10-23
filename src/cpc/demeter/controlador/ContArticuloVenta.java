package cpc.demeter.controlador;

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
import cpc.demeter.vista.UIArticuloVenta;
import cpc.modelo.demeter.administrativo.Impuesto;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.sigesp.basico.TipoArticuloSIGESP;
import cpc.modelo.sigesp.basico.UnidadMedidaSIGESP;
import cpc.modelo.demeter.basico.UsoPreciosProducto;
import cpc.negocio.demeter.basico.NegocioArticuloVenta;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContArticuloVenta extends ContVentanaBase<ArticuloVenta> {

	private static final long serialVersionUID = 6184414588153046382L;
	private AppDemeter app;
	private UIArticuloVenta vista;
	private NegocioArticuloVenta negocio;

	public ContArticuloVenta(int modoOperacion, ArticuloVenta labor,
			ICompCatalogo<ArticuloVenta> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			negocio = NegocioArticuloVenta.getInstance();
			if (labor == null || modoAgregar()) {
				negocio.nuevo();
				labor = negocio.getModelo();
			}
			// List<ClaseArticulo> clase = negocio.getClases();
			List<TipoArticuloSIGESP> tipos = negocio.getTipoArticulos();
			List<Impuesto> impuestos = negocio.getImpuestos();
			List<UnidadMedidaSIGESP> unidades = negocio.getUnidades();
			vista = new UIArticuloVenta("ARTICULO VENTA ("
					+ Accion.TEXTO[modoOperacion] + ")", 590, tipos, impuestos,
					unidades);
			setear(labor, vista, llamador, app);
			vista = ((UIArticuloVenta) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun Servicio");
		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
		}
	}

	public void eliminar() {
		try {
			negocio = NegocioArticuloVenta.getInstance();
			negocio.setModelo(getDato());
			negocio.borrar();

		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void anular() {
		try {
			negocio = NegocioArticuloVenta.getInstance();
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
			negocio = NegocioArticuloVenta.getInstance();
			negocio.setModelo(getDato());
			getDato().setActivo(vista.getActivo().isChecked());
			negocio.guardar(negocio.getModelo().getId());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR) {
			if ((vista.getDescripcion().getValue() == null || vista
					.getDescripcion().getValue() == "")) {
				throw new WrongValueException(vista.getDescripcion(),
						"Indique una Descripcion");
			}

			if ((vista.getCmbTipo().getSeleccion() == null)) {
				throw new WrongValueException(vista.getCmbTipo(),
						"Indique un Tipo de producto");
			}
			if ((vista.getPrecioBase().getValue() == null)) {
				throw new WrongValueException(vista.getPrecioBase(),
						"Indique un precio");
			}
			if ((vista.getCmbImpuesto().getSeleccion() == null)) {
				throw new WrongValueException(vista.getCmbImpuesto(),
						"Indique un Impuesto");
			}
		}
	}

	public void onEvent(Event event) throws Exception {

		if (event.getTarget() == getVista().getAceptar()) {

			validar();
			procesarCRUD(event);
		}

		else if (event.getName() == Events.ON_SELECTION
				&& event.getTarget() == vista.getCmbTipo()) {
			actualizarClases();
		} else if (event.getName() == Events.ON_SELECTION
				&& event.getTarget() == vista.getCmbImpuesto()) {
			actualizarPrecio();
		} else if (event.getName() == Events.ON_CHANGE) {
			actualizarPrecio();
		}
	}

	public void actualizarClases() throws ExcFiltroExcepcion {
		try {
			if (vista.getCmbTipo().getSeleccion() != null) {
				negocio = NegocioArticuloVenta.getInstance();
				// vista.refrescarClase(negocio.getClases(vista.getTipo().getSeleccion()));
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
			imp = vista.getCmbImpuesto().getSeleccion().getPorcentaje();
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
		modelo.setImpuesto(imp);
		modelo.setPrecio(valor.getValue());
		((Doublebox) item.getChildren().get(2)).setValue(modelo.getIva());
		((Doublebox) item.getChildren().get(3)).setValue(modelo.getTotal());
	}

	public void actualizarPrecio() {
		try {
			Double precio = vista.getPrecioBase().getValue();
			Double porcentaje = 0.0;
			Impuesto imp = vista.getCmbImpuesto().getSeleccion();
			if (imp != null)
				porcentaje = imp.getPorcentaje();
			vista.getIva().setValue(
					precio.doubleValue() * porcentaje.doubleValue() / 100);
			vista.getPrecio()
					.setValue(
							precio.doubleValue()
									* (1 + porcentaje.doubleValue() / 100));
		} catch (NullPointerException e) {
			e.printStackTrace();
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
