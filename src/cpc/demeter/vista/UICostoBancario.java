package cpc.demeter.vista;

import java.util.List;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Textbox;
import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.Impuesto;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.basico.CostoBancario;
import cpc.modelo.demeter.solicitud.Aprobacion;
import cpc.modelo.sigesp.basico.TipoArticuloSIGESP;
import cpc.modelo.sigesp.basico.UnidadMedidaSIGESP;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UICostoBancario extends CompVentanaBase<CostoBancario> {

	private static final long serialVersionUID = 6312240548253365129L;

	private List<Impuesto> impuestos;

	private CompGrupoDatos grpGeneral;

	private CompCombobox<Impuesto> cmbImpuesto;

	private Checkbox activo;
	private Doublebox precioBase;
	private Doublebox iva;
	private Doublebox precio;

	private Textbox txtDescripcion;

	public UICostoBancario(String titulo, int ancho, List<Impuesto> impuestos)
			throws ExcDatosInvalidos {
		super(titulo, ancho);

		this.impuestos = impuestos;

		inicializar();
	}

	public void inicializar() {
		grpGeneral = new CompGrupoDatos("Datos Generales", 2);
		txtDescripcion = new Textbox();

		activo = new Checkbox();
		cmbImpuesto = new CompCombobox<Impuesto>();

		precio = new Doublebox();
		precioBase = new Doublebox();
		iva = new Doublebox();

		cmbImpuesto.setModelo(impuestos);

		activo.setDisabled(true);
		cmbImpuesto.setReadonly(true);

		iva.setDisabled(true);
		precio.setDisabled(true);
	}

	public void dibujar() {
		txtDescripcion.setWidth("350px");
		txtDescripcion.setRows(2);

		txtDescripcion.setMaxlength(250);

		grpGeneral.addComponente("Descripción :", txtDescripcion);

		grpGeneral.addComponente("Impuesto Asociado:", cmbImpuesto);

		grpGeneral.addComponente("Precio Base:", precioBase);
		grpGeneral.addComponente("Activo", activo);
		grpGeneral.addComponente("Impuesto:", iva);
		grpGeneral.addComponente("Precio Venta:", precio);

		grpGeneral.dibujar(this);

		cmbImpuesto.addEventListener(Events.ON_SELECTION, getControlador());
		precioBase.addEventListener(Events.ON_CHANGE, getControlador());
		addBotonera();
	}

	public void cargarValores(CostoBancario articulo) throws ExcDatosInvalidos {
		txtDescripcion.setValue(getModelo().getDescripcion());
		getBinder().addBinding(txtDescripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);

		cmbImpuesto.setSeleccion(getModelo().getImpuesto());
		getBinder().addBinding(cmbImpuesto, "seleccion",
				getNombreModelo() + ".impuesto", null, null, "save", null,
				null, null, null);

		activo.setChecked(getModelo().getActivo());

		try {

			Double precio = getModelo().getPrecio();
			if (precio != null) {
				this.precioBase.setValue(precio);
				this.iva.setValue(precio
						* getModelo().getImpuesto().getPorcentaje() / 100);
				this.precio.setValue(precio + iva.getValue());
			}

		} catch (NullPointerException e) {

		}
		getBinder().addBinding(precioBase, "value",
				getNombreModelo() + ".precio", null, null, "save", null, null,
				null, null);

	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {

		txtDescripcion.setDisabled(true);

		activo.setDisabled(true);
		cmbImpuesto.setDisabled(true);

		precio.setDisabled(true);
		precioBase.setDisabled(true);
		iva.setDisabled(true);

	}

	public void modoEdicion() {

	}

	public CompCombobox<Impuesto> getCmbImpuesto() {
		return cmbImpuesto;
	}

	public Textbox getDescripcion() {
		return txtDescripcion;
	}

	public Doublebox getPrecioBase() {
		return precioBase;
	}

	public Doublebox getIva() {
		return iva;
	}

	public Doublebox getPrecio() {
		return precio;
	}

	public Checkbox getActivo() {
		return activo;
	}

}