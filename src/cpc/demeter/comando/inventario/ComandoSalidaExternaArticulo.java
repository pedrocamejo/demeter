package cpc.demeter.comando.inventario;

import java.util.Hashtable;
import java.util.Map;

import cpc.ares.interfaz.IAplicacion;
import cpc.ares.interfaz.IComando;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.inventario.ContArticuloAlmacenCantidades;
import cpc.demeter.catalogo.inventario.ContEntradasArticulos;
import cpc.demeter.catalogo.inventario.ContSalidasExternasArticulos;
import cpc.demeter.catalogo.inventario.ContSalidasInternasArticulos;

import cpc.zk.componente.interfaz.IZkAplicacion;

//cpc.demeter.comando.mantenimiento.ComandoConsumible
public class ComandoSalidaExternaArticulo implements IComando {

	private static final long serialVersionUID = 8711779167087354401L;
	private AccionFuncionalidad funcionalidades;
	private Map<String, Object> mapa = new Hashtable<String, Object>();
	private int id;
	private IZkAplicacion app;

	public void ejecutar() {

		try {
			ContSalidasExternasArticulos servicio = new ContSalidasExternasArticulos(
					getAccionFuncionalidad(), (AppDemeter) app);
			servicio.setIdFuncionalidad(id);
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

	public Map<String, Object> getParametros() {
		// TODO Auto-generated method stub
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

	public AccionFuncionalidad getAccionFuncionalidad() {
		return funcionalidades;
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

}