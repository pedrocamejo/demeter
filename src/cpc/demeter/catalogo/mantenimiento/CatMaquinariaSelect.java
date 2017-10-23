package cpc.demeter.catalogo.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import cpc.ares.modelo.Accion;
import cpc.demeter.ActivacionGarantia;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.mantenimiento.ContGarantiaExterna;
import cpc.demeter.controlador.mantenimiento.ContMaquinariaExterna;
import cpc.demeter.controlador.mantenimiento.ContOrdenGarantia;
import cpc.demeter.controlador.mantenimiento.garantia.ContGarantia;
import cpc.demeter.vista.mantenimiento.garantia.UISelectMaquinaria;
import cpc.modelo.demeter.mantenimiento.MaquinariaExterna;
import cpc.negocio.demeter.mantenimiento.NegocioMaquinariaExterna;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;

public class CatMaquinariaSelect implements EventListener {

	private IZkAplicacion 				 app;
	private MaquinariaExterna 			 maquinariaExterna;
	private UISelectMaquinaria 			 vista;
	private NegocioMaquinariaExterna 	 servicio;
	private ContOrdenGarantia			 contOrdenGarantia;
	private ContGarantia				 contgarantia;// garantias Externas :-D
	

	public CatMaquinariaSelect(IZkAplicacion app , ContOrdenGarantia contOrdenGarantia) throws InterruptedException, ExcDatosInvalidos
	{
		// TODO Auto-generated constructor stub
		super();
		this.app = app ;
		this.contOrdenGarantia = contOrdenGarantia;
		servicio = NegocioMaquinariaExterna.getInstance();
		List<Integer> estatus = new ArrayList<Integer>();
		estatus.add(1);
		List<MaquinariaExterna> lista = servicio.getMaqnaConGarantiaActiva(estatus);
		this.vista = new UISelectMaquinaria("Seleccionar Maquinaria", 1000,lista,app,this);
		app.agregarHija(this.vista);
	}

	public CatMaquinariaSelect(IZkAplicacion app,ContGarantia contGarantia)throws InterruptedException, ExcDatosInvalidos {
		// TODO Auto-generated constructor stub
		super();
		this.app = app;
		this.contgarantia =  contGarantia;
		servicio = NegocioMaquinariaExterna.getInstance();
		List<MaquinariaExterna> lista = servicio.getMaquinariaSinPropietario();
		this.vista = new UISelectMaquinaria("Seleccionar Maquinaria", 1000,lista,app,this);
		app.agregarHija(this.vista);
	}



	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub
		if (this.vista.getCancelar() == arg0.getTarget()) {
			this.vista.detach();
		} 
		else if (this.vista.getSeleccionar() == arg0.getTarget())  {
			maquinariaExterna = this.vista.datoSeleccionado();
			if (maquinariaExterna == null) 	{ 
				app.mostrarInformacion("Debe Seleccionar al menos 1 Items ");
			} 
			else {
				if (contOrdenGarantia != null) {
					contOrdenGarantia.actualizarMaquinaria(maquinariaExterna);
				}
				else if(contgarantia != null) {
					contgarantia.setMaquinaria(maquinariaExterna);
				}
				this.vista.detach();
			}
		}
	}

	public MaquinariaExterna getMaquinariaExterna() {
		return maquinariaExterna;
	}

	public void setMaquinariaExterna(MaquinariaExterna maquinariaExterna) {
		this.maquinariaExterna = maquinariaExterna;
	}

	public void agregarMaquinaria(MaquinariaExterna dato) {
		// TODO Auto-generated method stub
		vista.agregarMaquinaria(dato);
	}

}
