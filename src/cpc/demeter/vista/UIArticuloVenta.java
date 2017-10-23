package cpc.demeter.vista;

import java.util.List;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Textbox;
import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.Impuesto;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.solicitud.Aprobacion;
import cpc.modelo.sigesp.basico.TipoArticuloSIGESP;
import cpc.modelo.sigesp.basico.UnidadMedidaSIGESP;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIArticuloVenta extends CompVentanaBase<ArticuloVenta> {

	private static final long serialVersionUID = 6312240548253365129L;
	private List<TipoArticuloSIGESP> tiposArticulos;
	private List<Impuesto> impuestos;
	private List<UnidadMedidaSIGESP> unidades;
	private CompGrupoDatos grpGeneral, grpOtrosIdentificadores;

	private CompCombobox<Impuesto> cmbImpuesto;
	private CompCombobox<TipoArticuloSIGESP> cmbTipo;
	private CompCombobox<UnidadMedidaSIGESP> cmbUnidad;
	private Checkbox activo;
	private Doublebox precioBase;
	private Doublebox iva;
	private Doublebox precio;

	private Textbox txtCodigoSIGESP, txtCodigoCCNU, txtCodigoFabricante,
			txtDenFabricante, txtDescripcion, txtCuentaPresupuestariaIngreso;

	public UIArticuloVenta(String titulo, int ancho,
			List<TipoArticuloSIGESP> tiposProductores,
			List<Impuesto> impuestos, List<UnidadMedidaSIGESP> unidades)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.tiposArticulos = tiposProductores;
		this.impuestos = impuestos;
		this.unidades = unidades;

		inicializar();
	}

	public void inicializar() {
		grpGeneral = new CompGrupoDatos("Datos Generales", 2);
		grpOtrosIdentificadores = new CompGrupoDatos("Identificadores SIGESP",
				2);
		txtDescripcion = new Textbox();
		txtCodigoSIGESP = new Textbox();
		txtCodigoCCNU = new Textbox();
		txtCodigoFabricante = new Textbox();
		txtDenFabricante = new Textbox();
		txtCuentaPresupuestariaIngreso = new Textbox();
		activo = new Checkbox();
		cmbImpuesto = new CompCombobox<Impuesto>();
		cmbTipo = new CompCombobox<TipoArticuloSIGESP>();
		cmbUnidad = new CompCombobox<UnidadMedidaSIGESP>();
		precio = new Doublebox();
		precioBase = new Doublebox();
		iva = new Doublebox();

		cmbImpuesto.setModelo(impuestos);
		cmbTipo.setModelo(tiposArticulos);
		cmbUnidad.setModelo(unidades);

		activo.setDisabled(true);
		cmbImpuesto.setReadonly(true);
		cmbTipo.setReadonly(true);
		cmbUnidad.setReadonly(true);
		iva.setDisabled(true);
		precio.setDisabled(true);
	}

	public void dibujar() {
		txtDescripcion.setWidth("350px");
		txtDescripcion.setRows(2);

		txtDescripcion.setMaxlength(250);

		cmbTipo.setWidth("350px");

		txtDenFabricante.setWidth("350px");
		txtDenFabricante.setRows(2);
		txtDenFabricante.setMaxlength(254);

		txtCuentaPresupuestariaIngreso.setMaxlength(25);

		grpGeneral.addComponente("Descripción :", txtDescripcion);
		grpGeneral.addComponente("Tipo Articulo :", cmbTipo);
		grpGeneral.addComponente("Impuesto Asociado:", cmbImpuesto);
		grpGeneral.addComponente("Unidad de Medida:", cmbUnidad);
		grpGeneral.addComponente("Precio Base:", precioBase);
		grpGeneral.addComponente("Activo", activo);
		grpGeneral.addComponente("Impuesto:", iva);
		grpGeneral.addComponente("Precio Venta:", precio);

		grpGeneral.dibujar(this);

		grpOtrosIdentificadores.addComponente("Codigo SIGESP :",
				txtCodigoSIGESP);
		grpOtrosIdentificadores.addComponente("Codigo CCNU :", txtCodigoCCNU);
		grpOtrosIdentificadores.addComponente("Codigo Fabricante :",
				txtCodigoFabricante);
		grpOtrosIdentificadores.addComponente("Nombre de Fabricante :",
				txtDenFabricante);
		grpOtrosIdentificadores.addComponente("Cta. Presupuestaria Ingreso",
				txtCuentaPresupuestariaIngreso);

		grpOtrosIdentificadores.dibujar(this);

		cmbImpuesto.addEventListener(Events.ON_SELECTION, getControlador());
		precioBase.addEventListener(Events.ON_CHANGE, getControlador());
		addBotonera();
	}

	public void cargarValores(ArticuloVenta articulo) throws ExcDatosInvalidos {
		txtDescripcion.setValue(getModelo().getDescripcion());
		getBinder().addBinding(txtDescripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);

		cmbImpuesto.setSeleccion(getModelo().getImpuesto());
		getBinder().addBinding(cmbImpuesto, "seleccion",
				getNombreModelo() + ".impuesto", null, null, "save", null,
				null, null, null);

		cmbTipo.setSeleccion(getModelo().getTipoArticuloSIGESP());
		getBinder().addBinding(cmbTipo, "seleccion",
				getNombreModelo() + ".tipoArticuloSIGESP", null, null, "save",
				null, null, null, null);

		cmbUnidad.setSeleccion(getModelo().getUnidadMedidaSIGESP());
		getBinder().addBinding(cmbUnidad, "seleccion",
				getNombreModelo() + ".unidadMedidaSIGESP", null, null, "save",
				null, null, null, null);

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

		txtCodigoCCNU.setValue(articulo.getCodigoCCNU());
		txtCodigoFabricante.setValue(articulo.getCodigoFabricante());
		txtCodigoSIGESP.setValue(articulo.getCodigoSIGESP());
		txtDenFabricante.setValue(articulo.getDenominacionFabricante());
		txtCuentaPresupuestariaIngreso.setValue(articulo
				.getCuentaPresupuestariaIngreso());

		getBinder().addBinding(txtCuentaPresupuestariaIngreso, "value",
				getNombreModelo() + ".cuentaPresupuestariaIngreso", null, null,
				"save", null, null, null, null);
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {

		txtDescripcion.setDisabled(true);
		txtCodigoSIGESP.setDisabled(true);
		txtCodigoCCNU.setDisabled(true);
		txtCodigoFabricante.setDisabled(true);
		txtDenFabricante.setDisabled(true);
		txtCuentaPresupuestariaIngreso.setDisabled(true);
		activo.setDisabled(true);
		cmbImpuesto.setDisabled(true);
		cmbTipo.setDisabled(true);
		cmbUnidad.setDisabled(true);
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

	public CompCombobox<TipoArticuloSIGESP> getCmbTipo() {
		return cmbTipo;
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