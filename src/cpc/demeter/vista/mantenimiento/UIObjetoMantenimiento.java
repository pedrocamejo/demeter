package cpc.demeter.vista.mantenimiento;

import cpc.demeter.AppDemeter;
import cpc.modelo.demeter.mantenimiento.ObjetoMantenimiento;
import cpc.negocio.demeter.mantenimiento.NegocioObjetoMantenimiento;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIObjetoMantenimiento extends CompVentanaBase<ObjetoMantenimiento> {

	private static final long serialVersionUID = -5203328325790656931L;
	private UIDatosObjetoMantenimiento datosGenerales;
	private NegocioObjetoMantenimiento negocio;
	@SuppressWarnings("unused")
	private AppDemeter app;

	public UIObjetoMantenimiento(String titulo, int ancho, AppDemeter app,
			NegocioObjetoMantenimiento negocio) {
		super(titulo, ancho);
		this.app = app;
		this.negocio = negocio;
	}

	public void inicializar() {
		datosGenerales = new UIDatosObjetoMantenimiento(this.negocio,
				getModelo(), this);
	}

	public void dibujar() {
		addBotonera();
	}

	public void cargarValores(ObjetoMantenimiento dato)
			throws ExcDatosInvalidos {

	}

	public UIDatosObjetoMantenimiento getDatosGenerales() {
		return datosGenerales;
	}

	public void setDatosGenerales(UIDatosObjetoMantenimiento datosGenerales) {
		this.datosGenerales = datosGenerales;
	}

}
