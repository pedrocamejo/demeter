package cpc.demeter.vista;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.CuentasTipoServicio;
import cpc.modelo.demeter.basico.TipoServicio;
import cpc.modelo.sigesp.basico.CuentaContable;
import cpc.modelo.sigesp.basico.Sede;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UiCuentasTipoServicio extends CompVentanaBase<CuentasTipoServicio> {

	private static final long serialVersionUID = -5739695024627594930L;
	private CompGrupoDatos gbGeneral;
	@SuppressWarnings("unused")
	private Label codEmp, codSede;
	private Textbox cuentaPresupuestaria;
	private Sede sede;

	private CompCombobox<TipoServicio> cmbTipoServicio;
	private CompBuscar<CuentaContable> cuentaContable;
	private List<CuentaContable> cuentasContables;
	private List<TipoServicio> tiposServicios;

	public UiCuentasTipoServicio(String titulo, int ancho,
			List<CuentaContable> cuentas, List<TipoServicio> servicios,
			Sede sede) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.tiposServicios = servicios;
		this.cuentasContables = cuentas;
		this.sede = sede;
	}

	public UiCuentasTipoServicio(String titulo) throws ExcDatosInvalidos {
		super(titulo);
	}

	@SuppressWarnings("unchecked")
	public void inicializar() {
		gbGeneral = new CompGrupoDatos("", 1);
		cuentaContable = new CompBuscar<CuentaContable>(getEncabezado(), 0);
		cmbTipoServicio = new CompCombobox<TipoServicio>();

		cuentaPresupuestaria = new Textbox();
		codEmp = new Label(this.sede.getId().getCodigoEmpresa());
		codSede = new Label(this.sede.getId().getId());

		cuentaPresupuestaria.setWidth("400px");
		codEmp.setWidth("50px");
	}

	public void dibujar() {
		cmbTipoServicio.setModelo(this.tiposServicios);
		cuentaContable.setModelo(cuentasContables);
		cuentaContable.setAncho(500);
		cmbTipoServicio.setWidth("300px");

		gbGeneral.setAnchoColumna(0, 300);

		gbGeneral.addComponente("Tipo de Servicio :", cmbTipoServicio);
		gbGeneral.addComponente("Cuenta Contable de Ingreso:", cuentaContable);
		gbGeneral.addComponente("Cuenta Presupuestaria de Ingreso:",
				cuentaPresupuestaria);
		gbGeneral.dibujar(this);

		addBotonera();

	}

	@Override
	public void cargarValores(CuentasTipoServicio arg0)
			throws ExcDatosInvalidos {

		cmbTipoServicio.setSeleccion(getModelo().getTipoServicio());
		getBinder().addBinding(cmbTipoServicio, "seleccion",
				getNombreModelo() + ".tipoServicio", null, null, "save", null,
				null, null, null);

		cuentaContable.setValue(getModelo().getCuentaContableIngresosSede());
		getBinder().addBinding(cuentaContable, "value",
				getNombreModelo() + ".cuentaContableIngresosSede", null, null,
				"save", null, null, null, null);

		cuentaPresupuestaria.setValue(getModelo()
				.getCuentaPresupuestariaIngresosSede());
		getBinder().addBinding(cuentaPresupuestaria, "value",
				getNombreModelo() + ".cuentaPresupuestariaIngresosSede", null,
				null, "save", null, null, null, null);

		getModelo().setSede(this.sede);
		// getBinder().addBinding(codSede, "value",""+this.sede, null, null,
		// "save", null, null, null, null);

	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		cuentaPresupuestaria.setDisabled(true);
	}

	public void modoEdicion() {
		cuentaPresupuestaria.setDisabled(false);
	}

	@SuppressWarnings("unchecked")
	public List getEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Codigo", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre", 450);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDenominacion");
		encabezado.add(titulo);

		return encabezado;
	}

	public Textbox getCuentaPresupuestaria() {
		return cuentaPresupuestaria;
	}

	public void setCuentaPresupuestaria(Textbox cuentaPresupuestaria) {
		this.cuentaPresupuestaria = cuentaPresupuestaria;
	}

	public CompCombobox<TipoServicio> getCmbTipoServicio() {
		return cmbTipoServicio;
	}

	public void setCmbTipoServicio(CompCombobox<TipoServicio> cmbTipoServicio) {
		this.cmbTipoServicio = cmbTipoServicio;
	}

	public CompBuscar<CuentaContable> getCuentaContable() {
		return cuentaContable;
	}

	public void setCuentaContable(CompBuscar<CuentaContable> cuentaContable) {
		this.cuentaContable = cuentaContable;
	}

}
