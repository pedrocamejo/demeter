package cpc.demeter.vista.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Textbox;

import cpc.demeter.AppDemeter;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.mantenimiento.Maquinaria;
import cpc.modelo.demeter.mantenimiento.TipoMedidaRendimiento;
import cpc.negocio.demeter.mantenimiento.NegocioMaquinaria;
import cpc.negocio.demeter.mantenimiento.NegocioObjetoMantenimiento;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.utilidades.Color;

public class UIMaquinaria extends CompVentanaBase<Maquinaria> {

	private static final long serialVersionUID = -1486155148499016421L;
	private Doublebox txtCaballoFuerzas;
	private Textbox txtSerialCarroceria;
	private Textbox txtSerialMotor;
	private Textbox txtPlaca;
	private UIDatosObjetoMantenimiento grpDatosObjetoMant;
	private CompBuscar<Trabajador> catOperadorResponsable;
	private CompCombobox<TipoMedidaRendimiento> cmbTipoMedidaRendimiento;
	private NegocioMaquinaria negocio;
	private AppDemeter app;

	public UIMaquinaria(String titulo, int ancho, AppDemeter app,
			NegocioMaquinaria negocio) {
		super(titulo, ancho);
		this.negocio = negocio;
		this.app = app;

	}

	public void inicializar() {
		grpDatosObjetoMant = new UIDatosObjetoMantenimiento(
				NegocioObjetoMantenimiento.getInstance(), getModelo(), this);
		txtPlaca = new Textbox();
		txtSerialCarroceria = new Textbox();
		txtSerialMotor = new Textbox();
		txtCaballoFuerzas = new Doublebox();
		catOperadorResponsable = new CompBuscar<Trabajador>(
				getEncabezadoOperadores(), 2);
		cmbTipoMedidaRendimiento = new CompCombobox<TipoMedidaRendimiento>();
	}

	public void dibujar() {
		grpDatosObjetoMant.addComponente("Operador Responsable :",
				catOperadorResponsable);
		grpDatosObjetoMant.addComponente("Placa o Matricula    :", txtPlaca);
		grpDatosObjetoMant.addComponente("Serial de Carroceria :",
				txtSerialCarroceria);
		grpDatosObjetoMant.addComponente("Serial del Motor     :",
				txtSerialMotor);
		grpDatosObjetoMant.addComponente("Caballos de Fuerza   :",
				txtCaballoFuerzas);
		grpDatosObjetoMant.addComponente("Tipo de Medida Rendimiento  :",
				cmbTipoMedidaRendimiento);

		cmbTipoMedidaRendimiento.setReadonly(true);
		try {
			cmbTipoMedidaRendimiento.setModelo(negocio
					.getTipoMedidasRendimiento());
		} catch (ExcFiltroExcepcion e) {
			app.mostrarError("Error al cargar los Tipos de Medida de Rendimiento. "
					+ e.getMensajeError());
			e.printStackTrace();
		}
		addBotonera();

	}

	public void cargarValores(Maquinaria dato) throws ExcDatosInvalidos {

		try {
			grpDatosObjetoMant.getChkPropio().setChecked(
					getModelo().getPropio());
			getModelo()
					.setPropio(grpDatosObjetoMant.getChkPropio().isChecked());

			grpDatosObjetoMant.getTxtCodigo().setValue(getModelo().getCodigo());
			getBinder().addBinding(grpDatosObjetoMant.getTxtCodigo(), "value",
					getNombreModelo() + ".codigo", null, null, "save", null,
					null, null, null);

			grpDatosObjetoMant.getTxtSerial().setValue(
					getModelo().getSerie().toUpperCase());
			getBinder().addBinding(grpDatosObjetoMant.getTxtDescripcion(),
					"value", getNombreModelo() + ".serie", null, null, "save",
					null, null, null, null);

			grpDatosObjetoMant.getTxtDescripcion().setValue(
					getModelo().getNombre().toUpperCase());
			getBinder().addBinding(grpDatosObjetoMant.getTxtDescripcion(),
					"value", getNombreModelo() + ".descripcion", null, null,
					"save", null, null, null, null);

			if (getModelo().getActivo() != null) {
				grpDatosObjetoMant.getCatActivos().setSeleccion(
						getModelo().getActivo());
				grpDatosObjetoMant.getCmbCategoria().setSeleccion(
						getModelo().getActivo().getTipo().getCategoria());
				grpDatosObjetoMant.getCmbTipo().setSeleccion(
						getModelo().getActivo().getTipo());
				grpDatosObjetoMant.getCmbMarca().setSeleccion(
						getModelo().getActivo().getModelo().getMarca());
				grpDatosObjetoMant.getCmbModelo().setSeleccion(
						getModelo().getActivo().getModelo());
				getBinder().addBinding(grpDatosObjetoMant.getCatActivos(),
						"seleccion", getNombreModelo() + ".activo", null, null,
						"save", null, null, null, null);

			} else {

				grpDatosObjetoMant.getCmbCategoria().setSeleccion(
						getModelo().getTipo().getCategoria());
				grpDatosObjetoMant.getCmbTipo().setSeleccion(
						getModelo().getTipo());
				grpDatosObjetoMant.getCmbMarca().setSeleccion(
						getModelo().getModelo().getMarca());
				grpDatosObjetoMant.getCmbModelo().setSeleccion(
						getModelo().getModelo());

			}

			getBinder().addBinding(grpDatosObjetoMant.getCmbTipo(),
					"seleccion", getNombreModelo() + ".tipo", null, null,
					"save", null, null, null, null);
			getBinder().addBinding(grpDatosObjetoMant.getCmbModelo(),
					"seleccion", getNombreModelo() + ".modelo", null, null,
					"save", null, null, null, null);

			grpDatosObjetoMant.getCmbFabricante().setSeleccion(
					getModelo().getFabricante());
			getBinder().addBinding(grpDatosObjetoMant.getCmbFabricante(),
					"seleccion", getNombreModelo() + ".fabricante", null, null,
					"save", null, null, null, null);

			grpDatosObjetoMant.getSpnAnoFabricacion().setValue(
					getModelo().getAnoFabricacion());
			getBinder().addBinding(grpDatosObjetoMant.getSpnAnoFabricacion(),
					"value", getNombreModelo() + ".noFabricacion", null, null,
					"save", null, null, null, null);

			grpDatosObjetoMant.getCmbLote().setSeleccion(getModelo().getLote());
			getBinder().addBinding(grpDatosObjetoMant.getCmbLote(),
					"seleccion", getNombreModelo() + ".lote", null, null,
					"save", null, null, null, null);

			grpDatosObjetoMant.getCmbEstadoFunional().setSeleccion(
					getModelo().getEstado());
			getBinder().addBinding(grpDatosObjetoMant.getCmbEstadoFunional(),
					"seleccion", getNombreModelo() + ".estadoFuncional", null,
					null, "save", null, null, null, null);

			// /*OJO*///
			grpDatosObjetoMant.getCmbColor().setSeleccion(
					getModelo().getColor());
			grpDatosObjetoMant.getDivColor().setStyle(
					"background-color:# " + getModelo().getNumeroColor());
			getBinder().addBinding(grpDatosObjetoMant.getCmbColor(), "value",
					getNombreModelo() + ".color", null, null, "save", null,
					null, null, null);
			getBinder().addBinding(
					grpDatosObjetoMant.getDivColor(),
					"value",
					new Color().getColorHexadecimal(grpDatosObjetoMant
							.getCmbColor().getSelectedIndex()), null, null,
					"save", null, null, null, null);
			// /*OJO*///

			if (getModelo().getPropietario() != null) {
				grpDatosObjetoMant.getCatPropietarios().setSeleccion(
						getModelo().getPropietario());
				getBinder().addBinding(grpDatosObjetoMant.getCatPropietarios(),
						"seleccion", getNombreModelo() + ".propietario", null,
						null, "save", null, null, null, null);

			}

			if (getModelo().getOperador() != null) {
				catOperadorResponsable.setSeleccion(getModelo().getOperador());
				getBinder().addBinding(catOperadorResponsable, "seleccion",
						getNombreModelo() + ".operadorResponsable", null, null,
						"save", null, null, null, null);

			}
			if (getModelo().getTipoMedidaRendimiento() != null) {
				cmbTipoMedidaRendimiento.setSeleccion(getModelo()
						.getTipoMedidaRendimiento());
				getBinder().addBinding(cmbTipoMedidaRendimiento, "seleccion",
						getNombreModelo() + ".tipoMedidaRendimiento", null,
						null, "save", null, null, null, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<CompEncabezado> getEncabezadoOperadores() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Tipo", 150);
		titulo.setMetodoBinder("getStrTipoTrabajador");
		lista.add(titulo);

		titulo = new CompEncabezado("Cedula", 100);
		titulo.setMetodoBinder("getNroCedula");
		lista.add(titulo);

		titulo = new CompEncabezado("Nombre", 300);
		titulo.setMetodoBinder("getNombre");
		lista.add(titulo);

		titulo = new CompEncabezado("Direccion", 300);
		titulo.setMetodoBinder("getDireccion");
		lista.add(titulo);

		return lista;

	}

	public void setOperadores() {
		try {
			this.catOperadorResponsable.setModelo(NegocioMaquinaria
					.getInstance().getOperadores());
			this.catOperadorResponsable.setAncho(550);
		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
		}

	}

	public UIDatosObjetoMantenimiento getGrpDatosObjetoMant() {
		return grpDatosObjetoMant;
	}

	public void setGrpDatosObjetoMant(
			UIDatosObjetoMantenimiento grpDatosObjetoMant) {
		this.grpDatosObjetoMant = grpDatosObjetoMant;
	}

	public Doublebox getTxtCaballoFuerzas() {
		return txtCaballoFuerzas;
	}

	public void setTxtCaballoFuerzas(Doublebox txtCaballoFuerzas) {
		this.txtCaballoFuerzas = txtCaballoFuerzas;
	}

	public Textbox getTxtSerialCarroceria() {
		return txtSerialCarroceria;
	}

	public void setTxtSerialCarroceria(Textbox txtSerialCarroceria) {
		this.txtSerialCarroceria = txtSerialCarroceria;
	}

	public Textbox getTxtSerialMotor() {
		return txtSerialMotor;
	}

	public void setTxtSerialMotor(Textbox txtSerialMotor) {
		this.txtSerialMotor = txtSerialMotor;
	}

	public Textbox getTxtPlaca() {
		return txtPlaca;
	}

	public void setTxtPlaca(Textbox txtPlaca) {
		this.txtPlaca = txtPlaca;
	}

	public CompCombobox<TipoMedidaRendimiento> getCmbTipoMedidaRendimiento() {
		return cmbTipoMedidaRendimiento;
	}

	public void setCmbTipoMedidaRendimiento(
			CompCombobox<TipoMedidaRendimiento> cmbTipoMedidaRendimiento) {
		this.cmbTipoMedidaRendimiento = cmbTipoMedidaRendimiento;
	}

}
