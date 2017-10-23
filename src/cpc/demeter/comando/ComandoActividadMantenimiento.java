package cpc.demeter.comando;

import java.util.Hashtable;
import java.util.Map;

import cpc.ares.interfaz.IAplicacion;
import cpc.ares.interfaz.IComando;
import cpc.ares.modelo.AccionFuncionalidad;

import cpc.zk.componente.interfaz.IZkAplicacion;

public class ComandoActividadMantenimiento implements IComando {

	private static final long serialVersionUID = -3423700244116595537L;
	private AccionFuncionalidad funcionalidades;
	private Map<Integer, Object> mapa = new Hashtable<Integer, Object>();
	private int id;
	private IZkAplicacion app;

	public void ejecutar() {

		try {
			/*
			 * ContActividadesMantenimientos servicio = new
			 * ContActividadesMantenimientos(getAccionFuncionalidad(),
			 * (AppDemeter)app); servicio.setIdFuncionalidad(id);
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public IAplicacion getApp() {
		return app;
	}

	public void setApp(IAplicacion arg0) {
		this.app = (IZkAplicacion) arg0;
	}

	public AccionFuncionalidad getAccionFuncionalidad() {
		return funcionalidades;
	}

	public void setAccionFuncionalidad(AccionFuncionalidad funcionalidades) {
		this.funcionalidades = funcionalidades;

	}

	public void agregarParametro(int codigo, Object valor) {
		mapa.put(codigo, valor);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Map<String, Object> getParametros() {

		return null;
	}

	public Object seleccionar(int arg0) {

		return null;
	}

	public void setParametros(Map<String, Object> arg0) {

	}

	public void agregarParametro(String arg0, Object arg1) {

	}

}
