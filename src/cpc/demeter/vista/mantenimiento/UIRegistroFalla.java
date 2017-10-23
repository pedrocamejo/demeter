package cpc.demeter.vista.mantenimiento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Messagebox;
import cpc.demeter.AppDemeter;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.mantenimiento.MomentoFalla;
import cpc.modelo.demeter.mantenimiento.ObjetoMantenimiento;
import cpc.modelo.demeter.mantenimiento.RegistroFalla;
import cpc.modelo.demeter.mantenimiento.TipoFalla;
import cpc.negocio.demeter.mantenimiento.NegocioRegistroFalla;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class UIRegistroFalla extends CompVentanaBase<RegistroFalla> {

	private static final long serialVersionUID = 995855838440816728L;
	private CompGrupoDatos agrupador;
	private CompBuscar<ObjetoMantenimiento> catObjetoMantenimiento;
	private CompCombobox<TipoFalla> cmbTipoFalla;
	private CompCombobox<MomentoFalla> cmbMomentoFalla;
	private Datebox fechaFalla;
	private CompBuscar<Trabajador> catTrabajadorReporta;

	@SuppressWarnings("unused")
	private AppDemeter app;

	public UIRegistroFalla() {

	}

	public UIRegistroFalla(String titulo, int ancho, AppDemeter app) {
		super(titulo, ancho);
		this.app = app;
	}

	public void inicializar() {
		agrupador = new CompGrupoDatos("", 2);

		catObjetoMantenimiento = new CompBuscar<ObjetoMantenimiento>(
				getEncabezadoObjetoMantenimiento(), 1);
		cmbTipoFalla = new CompCombobox<TipoFalla>();
		cmbMomentoFalla = new CompCombobox<MomentoFalla>();
		fechaFalla = new Datebox(new Date());
		catTrabajadorReporta = new CompBuscar<Trabajador>(
				getEncabezadoTrabajadorReporta(), 1);

	}

	public void dibujar() {
		agrupador.setAnchoColumna(0, 200);
		agrupador.addComponente("Objeto Mantenimiento :",
				catObjetoMantenimiento);
		agrupador.addComponente("Tipo Falla :", cmbTipoFalla);
		agrupador.addComponente("Momento Falla :", cmbMomentoFalla);
		agrupador.addComponente("Fecha Falla:", fechaFalla);
		agrupador.addComponente("Trabajador que Reporta :",
				catTrabajadorReporta);
		catObjetoMantenimiento.setAncho(650);
		catTrabajadorReporta.setAncho(450);
		agrupador.dibujar(this);
		addBotonera();

		setTiposFallas();
		setMomentoFalla();
		setObjetoMantenimiento();
		setTrabajadorReporta();
		fechaFalla.setConstraint("no future: Hoy o en el pasado");
	}

	private List<CompEncabezado> getEncabezadoObjetoMantenimiento() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Codigo", 100);
		titulo.setMetodoBinder("getCodigo");
		lista.add(titulo);

		titulo = new CompEncabezado("Nombre", 200);
		titulo.setMetodoBinder("getNombre");
		lista.add(titulo);

		titulo = new CompEncabezado("Tipo", 200);
		titulo.setMetodoBinder("getTipo");
		lista.add(titulo);

		titulo = new CompEncabezado("Modelo", 200);
		titulo.setMetodoBinder("getModelo");
		lista.add(titulo);

		return lista;
	}

	private List<CompEncabezado> getEncabezadoTrabajadorReporta() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Cedula", 100);
		titulo.setMetodoBinder("getNroCedula");
		lista.add(titulo);

		titulo = new CompEncabezado("Nombre", 200);
		titulo.setMetodoBinder("getNombre");
		lista.add(titulo);

		titulo = new CompEncabezado("Apellido", 200);
		titulo.setMetodoBinder("getApellido");
		lista.add(titulo);

		return lista;
	}

	private void setMomentoFalla() {
		try {
			cmbMomentoFalla.setModelo(NegocioRegistroFalla.getInstance()
					.getMomentoFalla());
		} catch (ExcFiltroExcepcion e) {
			try {
				Messagebox
						.show("ocurrio un Error al cargar los momentos de falla");
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void setTiposFallas() {
		try {
			cmbTipoFalla.setModelo(NegocioRegistroFalla.getInstance()
					.getTipoFalla());
		} catch (ExcFiltroExcepcion e) {
			try {
				Messagebox
						.show("ocurrio un Error al cargar los tipos de fallas");
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void setObjetoMantenimiento() {

		try {
			catObjetoMantenimiento.setModelo(NegocioRegistroFalla.getInstance()
					.getObjetosMantenimiento());
		} catch (ExcFiltroExcepcion e) {
			try {
				Messagebox
						.show("ocurrio un Error al cargar los ojetos de mantenimiento");
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

	}

	private void setTrabajadorReporta() {

		try {
			catTrabajadorReporta.setModelo(NegocioRegistroFalla.getInstance()
					.getTrabajadores());
		} catch (ExcFiltroExcepcion e) {
			try {
				Messagebox.show("ocurrio un Error al cargar los trabajadores");
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

	}

	public void cargarValores(RegistroFalla arg0) throws ExcDatosInvalidos {

		catObjetoMantenimiento.setSeleccion(getModelo()
				.getObjetoMantenimiento());
		getBinder().addBinding(catObjetoMantenimiento, "seleccion",
				getNombreModelo() + ".ObjetoMantenimiento", null, null, "save",
				null, null, null, null);

		cmbTipoFalla.setSeleccion(getModelo().getTipoFalla());
		getBinder().addBinding(cmbTipoFalla, "seleccion",
				getNombreModelo() + ".TipoFalla", null, null, "save", null,
				null, null, null);

		cmbMomentoFalla.setSeleccion(getModelo().getMomentoFalla());
		getBinder().addBinding(cmbMomentoFalla, "seleccion",
				getNombreModelo() + ".MomentoFalla", null, null, "save", null,
				null, null, null);

		fechaFalla.setValue(getModelo().getFechaFalla());
		getBinder().addBinding(fechaFalla, "value",
				getNombreModelo() + ".FechaFalla", null, null, "save", null,
				null, null, null);

		catTrabajadorReporta.setSeleccion(getModelo().getTrabajadorReporta());
		getBinder().addBinding(catTrabajadorReporta, "seleccion",
				getNombreModelo() + ".TrabajadorReporta", null, null, "save",
				null, null, null, null);

	}

	public CompBuscar<ObjetoMantenimiento> getCatObjetoMantenimiento() {
		return catObjetoMantenimiento;
	}

	public void setCatObjetoMantenimiento(
			CompBuscar<ObjetoMantenimiento> catObjetoMantenimiento) {
		this.catObjetoMantenimiento = catObjetoMantenimiento;
	}

	public CompCombobox<TipoFalla> getCmbTipoFalla() {
		return cmbTipoFalla;
	}

	public void setCmbTipoFalla(CompCombobox<TipoFalla> cmbTipoFalla) {
		this.cmbTipoFalla = cmbTipoFalla;
	}

	public CompCombobox<MomentoFalla> getCmbMomentoFalla() {
		return cmbMomentoFalla;
	}

	public void setCmbMomentoFalla(CompCombobox<MomentoFalla> cmbMomentoFalla) {
		this.cmbMomentoFalla = cmbMomentoFalla;
	}

	public Datebox getFechaFalla() {
		return fechaFalla;
	}

	public void setFechaFalla(Datebox fechaFalla) {
		this.fechaFalla = fechaFalla;
	}

	public CompBuscar<Trabajador> getCatTrabajadorReporta() {
		return catTrabajadorReporta;
	}

	public void setCatTrabajadorReporta(
			CompBuscar<Trabajador> catTrabajadorReporta) {
		this.catTrabajadorReporta = catTrabajadorReporta;
	}

}