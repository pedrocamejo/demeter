package cpc.demeter.comando.gestion;

import java.util.Hashtable;
import java.util.Map;

import cpc.ares.interfaz.IAplicacion;
import cpc.ares.interfaz.IComando;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.gestion.ContDesincorporacionesActivos;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;

public class ComandoDesincorporacionActivo implements IComando {

	private static final long serialVersionUID = 6424907238718865742L;
	private AccionFuncionalidad funcionalidades;
	private Map<String, Object> mapa = new Hashtable<String, Object>();
	private int id;
	private IZkAplicacion app;

	public void ejecutar() {
		try {
			ContDesincorporacionesActivos servicio = new ContDesincorporacionesActivos(
					funcionalidades, (AppDemeter) app);
			servicio.setIdFuncionalidad(id);
		} catch (ExcColumnasInvalidas e) {
			e.printStackTrace();
		} catch (ExcArgumentoInvalido e) {
			e.printStackTrace();
		}

	}

	public IAplicacion getApp() {
		return app;
	}

	public void setApp(IAplicacion arg0) {
		this.app = (IZkAplicacion) arg0;
	}

	public Map<String, Object> getParametros() {
		return mapa;
	}

	public void setParametros(Map<String, Object> parametros) {
		this.mapa = parametros;
	}

	public void agregarParametro(String codigo, Object valor) {
		mapa.put(codigo, valor);

	}

	public void setAccionFuncionalidad(AccionFuncionalidad funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Object seleccionar(int arg0) {

		return null;
	}

	public AccionFuncionalidad getAccionFuncionalidad() {
		return funcionalidades;
	}

}
