package cpc.demeter.comando;

import java.util.Hashtable;
import java.util.Map;

import cpc.ares.interfaz.IAplicacion;
import cpc.ares.interfaz.IComando;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.zk.componente.interfaz.IZkAplicacion;

public class ComandoOrdenServicio implements IComando {

	private static final long serialVersionUID = -2688081335887322152L;
	private AccionFuncionalidad funcionalidades;
	private Map<String, Object> mapa = new Hashtable<String, Object>();
	private int id;
	private IZkAplicacion app;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Object seleccionar(int arg0) {

		return null;
	}

	public void ejecutar() {
		/*
		 * try { ContOrdenesServicios ordSer = new
		 * ContOrdenesServicios(getAccionFuncionalidad());
		 * ordSer.setIdFuncionalidad(id);
		 * 
		 * } catch (SQLException e) { // TODO Auto-generated block
		 * e.printStackTrace(); } catch (ExcArgumentoInvalido e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); } catch
		 * (PropertyVetoException e) { // TODO Auto-generated catch block
		 * e.printStackTrace();
		 * 
		 * } catch (Exception e){ e.printStackTrace(); }
		 */
	}

	public AccionFuncionalidad getAccionFuncionalidad() {

		return funcionalidades;
	}

}
