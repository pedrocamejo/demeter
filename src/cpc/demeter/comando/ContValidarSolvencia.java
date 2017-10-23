package cpc.demeter.comando;


import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

import cpc.demeter.comando.reporte.ComandoEstadosCuenta;
import cpc.demeter.vista.UIValidarSolvencia;
import cpc.modelo.demeter.ReporteCodigo;
import cpc.negocio.demeter.NegocioReporteCodigo;
import cpc.negocio.demeter.basico.NegocioVerificacionSuelo;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContValidarSolvencia  implements EventListener {

	private IZkAplicacion 			 	app;
	private NegocioReporteCodigo negocioReporteCodigo = NegocioReporteCodigo.getInstance(); 
    
	private ComandoEstadosCuenta    	llamador;
	private UIValidarSolvencia  		vista;
	private Integer 				    modoOperacion; // 1 Solvente 2 Deudor 

	public ContValidarSolvencia(Integer modoOperaion,ComandoEstadosCuenta llamador,IZkAplicacion app) throws SuspendNotAllowedException, InterruptedException, ExcFiltroExcepcion {
		this.app = app;
		this.llamador = llamador;
		this.setModoOperacion(modoOperaion);
		vista = new  UIValidarSolvencia("Validar Reporte ","none",true,this);
		app.agregarHija(vista);
		vista.doModal();
		
	}
 

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub
		if(vista.getBuscar() == arg0.getTarget())
		{
			if(vista.getBusquedad().getValue().length() != 0 )
			{
				ReporteCodigo reporte = negocioReporteCodigo.getReporteCodigo(vista.getBusquedad().getValue());
				if(reporte != null)
				{
					vista.getUsuario().setValue(reporte.getUsername());
					vista.getFecha().setValue(reporte.getFecha().toString());
					vista.ok();
					Messagebox.show("Codigo Encontrado");
					
				}
				else
				{
					vista.getUsuario().setValue("----");
					vista.getFecha().setValue("----");
					vista.no_ok();
					Messagebox.show("Este Codigo No Coincide ");
				}
			}
		}
	}


	public Integer getModoOperacion() {
		return modoOperacion;
	}

	public void setModoOperacion(Integer modoOperacion) {
		this.modoOperacion = modoOperacion;
	}

}
