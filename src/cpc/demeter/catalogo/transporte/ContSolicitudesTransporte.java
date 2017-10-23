package cpc.demeter.catalogo.transporte;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Messagebox;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.transporte.ContDestino;
import cpc.demeter.controlador.transporte.ContSolicitudTransporte;
import cpc.modelo.demeter.transporte.Destino;
import cpc.modelo.demeter.transporte.EstadoSolicitudTransporte;
import cpc.modelo.demeter.transporte.SolicitudTransporte;
import cpc.negocio.demeter.transporte.NegocioDestino;
import cpc.negocio.demeter.transporte.NegocioSolicitudTransporte;
import cpc.zk.componente.controlador.ContCatalogo;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;

public class ContSolicitudesTransporte extends ContCatalogo<SolicitudTransporte> implements EventListener,Serializable{
	 
		private static final long serialVersionUID = 1L;

		private AppDemeter app;
		
		public ContSolicitudesTransporte(AccionFuncionalidad accionesValidas, AppDemeter app) throws Exception 
		{
			
			NegocioSolicitudTransporte servicios =  NegocioSolicitudTransporte.getInstance();
			this.app = app;
 
			dibujar(accionesValidas, "SOLICITUD TRANSPORTE",servicios.getTodos(), app); 
		   }	
		 
		@Override
		public List<CompEncabezado> cargarEncabezado() {
			CompEncabezado titulo; 
			List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
			
			titulo = new CompEncabezado("ID",100);
			titulo.setOrdenable(true);
			titulo.setMetodoBinder("getIdSolicitud");
			encabezado.add(titulo);
			
			titulo = new CompEncabezado("lugarSalida",100);
			titulo.setOrdenable(true);
			titulo.setMetodoBinder("getLugarSalida");
			encabezado.add(titulo);
		
			titulo = new CompEncabezado("Fecha Salida",100);
			titulo.setOrdenable(true);
			titulo.setMetodoBinder("getFechasalida");
			encabezado.add(titulo);

			titulo = new CompEncabezado("Gerencia ",100);
			titulo.setOrdenable(true);
			titulo.setMetodoBinder("getGerenciaStr");
			encabezado.add(titulo);

			
			titulo = new CompEncabezado("Estado",100);
			titulo.setOrdenable(true);
			titulo.setMetodoBinder("getEstadoStr");
			encabezado.add(titulo);
			 
			return encabezado;
		}

		 
		@Override
		protected List<List> cargarDatos() throws ExcArgumentoInvalido {
			// TODO Auto-generated method stub
			return null;
		}
		
		
		@Override
		public void onEvent(Event event) throws Exception {
			try {
				int accion = (Integer) event.getTarget().getAttribute("pos");

				if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR) 
				{
					NegocioDestino servicio = NegocioDestino.getInstance();
					SolicitudTransporte destino = getDatoSeleccionado();
					if (destino  == null && accion != Accion.AGREGAR)
					{
						app.mostrarError("Seleccione un tipo de riego");
					}
					else if(accion == Accion.ANULAR && destino.getEstado() != EstadoSolicitudTransporte.NUEVA)
					{
						app.mostrarError("No Puedes Anular Esta Solicitud ");
					}
					else
					{
						new ContSolicitudTransporte(accion,destino,this, app);
					}
				}
				else if(accion <= Accion.PROCESAR)
				{
					NegocioDestino servicio = NegocioDestino.getInstance();
					SolicitudTransporte destino = getDatoSeleccionado();
				
					if (destino  == null )
					{
						app.mostrarError("Seleccione un tipo de riego");
					}
					else
				    {
						if(destino.getEstado() == EstadoSolicitudTransporte.ANULADA)
						{
							app.mostrarError("Esta Solicitud no puede ser Procesada ");
						}
						else
						{
							new ContSolicitudTransporte(accion,destino,this, app);
						}
				    }

				}
			} catch (NullPointerException e) {
				e.printStackTrace();
				app.mostrarError("Seleccione un tipo de riego");
			} catch (Exception e) {
				e.printStackTrace();
				app.mostrarError(e.getMessage());
			}
		}

	 
		@Override
		public List cargarDato(SolicitudTransporte dato) {
			// TODO Auto-generated method stub
			return null;
		}

	}