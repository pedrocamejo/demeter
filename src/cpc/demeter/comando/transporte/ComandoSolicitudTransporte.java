package cpc.demeter.comando.transporte;

import java.util.Hashtable;
import java.util.Map;

import cpc.ares.interfaz.IAplicacion;
import cpc.ares.interfaz.IComando;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.transporte.ContDestinos;
import cpc.demeter.catalogo.transporte.ContSolicitudesTransporte;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ComandoSolicitudTransporte implements IComando{

	private static final long serialVersionUID = 1L;
	private AccionFuncionalidad funcionalidades;
	private Map<String, Object> mapa = new Hashtable<String, Object>();
	private int 				id;
	private	IZkAplicacion		 app;
	  
	
	public void ejecutar() {
		try {
			ContSolicitudesTransporte servicio = new ContSolicitudesTransporte(getAccionFuncionalidad(),(AppDemeter) app);
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
		this.funcionalidades =funcionalidades;
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
