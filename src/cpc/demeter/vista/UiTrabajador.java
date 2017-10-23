package cpc.demeter.vista;

import java.util.List;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Textbox;
import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.basico.CargoTrabajador;
import cpc.modelo.demeter.basico.FuncionTrabajador;
import cpc.modelo.demeter.basico.TipoTrabajador;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompListaMultiple;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiTrabajador extends CompVentanaBase<Trabajador> {

	private static final long serialVersionUID = -5739695024627594930L;

	private CompGrupoDatos gbGeneral, gbLaboral, gbDireccion, gbTelefono;
	private Textbox nombre, cedula, apellido, direccion, telefonoFijo,
			telefonoMovil;
	private CompCombobox<TipoTrabajador> tipoTrabajador;
	private CompListaMultiple responsabilidad;
	private CompCombobox<CargoTrabajador> cargo;

	private List<TipoTrabajador> tiposTrabajadores;
	private List<CargoTrabajador> cargos;
	private List<FuncionTrabajador> funcionalidades;

	public UiTrabajador(String titulo, int ancho,
			List<TipoTrabajador> tiposTrabajadores,
			List<CargoTrabajador> cargos,
			List<FuncionTrabajador> funcionalidades) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.tiposTrabajadores = tiposTrabajadores;
		this.cargos = cargos;
		this.funcionalidades = funcionalidades;
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);
		gbDireccion = new CompGrupoDatos("Direccion", 1);
		gbLaboral = new CompGrupoDatos("Laboral", 2);
		gbTelefono = new CompGrupoDatos("Telefonos", 4);
		nombre = new Textbox();
		apellido = new Textbox();
		cedula = new Textbox();
		direccion = new Textbox();
		telefonoFijo = new Textbox();
		telefonoMovil = new Textbox();
		responsabilidad = new CompListaMultiple("Funciones");
		cargo = new CompCombobox<CargoTrabajador>();
		tipoTrabajador = new CompCombobox<TipoTrabajador>();
		cedula.setWidth("120px");
		tipoTrabajador.setWidth("130px");
		nombre.setWidth("300px");
		apellido.setWidth("300px");
		direccion.setWidth("450px");
		direccion.setMultiline(true);
		direccion.setRows(2);
		cargo.setModelo(cargos);
		responsabilidad.setModelo(funcionalidades);
		telefonoFijo.setWidth("120px");
		telefonoMovil.setWidth("120px");
		tipoTrabajador.setModelo(tiposTrabajadores);
		tipoTrabajador.setWidth("180px");
		cargo.setWidth("180px");
		responsabilidad.setWidth("180px");
		responsabilidad.setRows(5);
		tipoTrabajador.addEventListener(Events.ON_SELECTION, getControlador());
	}

	public void dibujar() {
		gbGeneral.addComponente("Cedula:", cedula);
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Nombres:", nombre);
		gbGeneral.addComponente("Apellidos :", apellido);
		gbDireccion.addComponente("Direccion:", direccion);
		gbLaboral.addComponente("Tipo :", tipoTrabajador);
		gbLaboral.addComponente("Cargo Contratacion :", cargo);
		gbLaboral.addComponente("Funciones :", responsabilidad);
		gbTelefono.addComponente("Residencial:", telefonoFijo);
		gbTelefono.addComponente("Celular:", telefonoMovil);

		gbGeneral.dibujar(this);
		gbLaboral.dibujar(this);
		gbDireccion.dibujar(this);
		gbTelefono.dibujar(this);
		addBotonera();
	}

	public void cargarValores(Trabajador arg0) throws ExcDatosInvalidos {
		nombre.setValue(getModelo().getNombre());
		getBinder().addBinding(nombre, "value", getNombreModelo() + ".nombre",
				null, null, "save", null, null, null, null);
		cedula.setValue(getModelo().getNroCedula());
		getBinder().addBinding(cedula, "value",
				getNombreModelo() + ".nroCedula", null, null, "save", null,
				null, null, null);
		apellido.setValue(getModelo().getApellido());
		getBinder().addBinding(apellido, "value",
				getNombreModelo() + ".apellido", null, null, "save", null,
				null, null, null);
		direccion.setValue(getModelo().getDireccion());
		getBinder().addBinding(direccion, "value",
				getNombreModelo() + ".direccion", null, null, "save", null,
				null, null, null);

		telefonoFijo.setValue(getModelo().getTlfFijo());
		getBinder().addBinding(telefonoFijo, "value",
				getNombreModelo() + ".tlfFijo", null, null, "save", null, null,
				null, null);
		telefonoMovil.setValue(getModelo().getTlfMovil());
		getBinder().addBinding(telefonoMovil, "value",
				getNombreModelo() + ".tlfMovil", null, null, "save", null,
				null, null, null);

		tipoTrabajador.setSeleccion(getModelo().getTipoTrabajador());
		getBinder().addBinding(tipoTrabajador, "seleccion",
				getNombreModelo() + ".tipoTrabajador", null, null, "save",
				null, null, null, null);

		if (getModelo().getFunciones() != null)
			responsabilidad.setModeloSelect(getModelo().getFunciones());
		getBinder().addBinding(responsabilidad, "modeloSelect",
				getNombreModelo() + ".funciones", null, null, "save", null,
				null, null, null);

		cargo.setSeleccion(getModelo().getCargo());
		getBinder().addBinding(cargo, "seleccion",
				getNombreModelo() + ".cargo", null, null, "save", null, null,
				null, null);

	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		nombre.setDisabled(true);
		apellido.setDisabled(true);
		cedula.setDisabled(true);
		direccion.setDisabled(true);
		telefonoFijo.setDisabled(true);
		telefonoMovil.setDisabled(true);
		cargo.setDisabled(true);
		responsabilidad.setDisabled(true);
		tipoTrabajador.setDisabled(true);
	}

	public void modoEdicion() {
		nombre.setDisabled(false);
	}

	public Textbox getDescripcion() {
		return nombre;
	}

	public void setDescripcion(Textbox descripcion) {
		this.nombre = descripcion;
	}

	public Textbox getNombre() {
		return nombre;
	}

	public Textbox getCedula() {
		return cedula;
	}

	public Textbox getApellido() {
		return apellido;
	}

	public Textbox getDireccion() {
		return direccion;
	}

	public Textbox getTelefonoFijo() {
		return telefonoFijo;
	}

	public Textbox getTelefonoMovil() {
		return telefonoMovil;
	}

	public CompCombobox<TipoTrabajador> getTipoTrabajador() {
		return tipoTrabajador;
	}

}
