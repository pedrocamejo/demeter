package cpc.demeter.vista.transporte;
 
import java.util.ArrayList;
import java.util.List;
 
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Textbox;


 
import cpc.ares.modelo.Accion;
import cpc.demeter.controlador.transporte.ContSolicitudTransporte;
import cpc.modelo.demeter.transporte.EstadoSolicitudTransporte;
import cpc.modelo.demeter.transporte.Gerencia;
import cpc.modelo.demeter.transporte.SolicitudTransporte;
import cpc.modelo.ministerio.dimension.UbicacionSector;
 
import cpc.zk.componente.excepciones.ExcDatosInvalidos; 
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UISolicitudTransporte extends CompVentanaBase<SolicitudTransporte>{
	 
	
		private static final long serialVersionUID = 1L;
		
		private CompGrupoDatos		 			gbGeneral;		
		private CompGrupoDatos		 			gbGerencia;		
		private CompGrupoDatos		 			gbEstad0;		
		

		
		private Combobox	 				gerencia;
		private CompBuscar<UbicacionSector> sector;
		private Datebox					fechasalida;
		private Datebox 				fechallegada;
		private Textbox 				lugarSalida;
		private Textbox 				motivo;
		private Intbox	 				nroPasajero;
		private Label				 	estado; 
		private Textbox 				justificacion;
		
		private List<Gerencia> 			gerencias = new ArrayList<Gerencia>();
		private List<UbicacionSector> 	sectores = new ArrayList<UbicacionSector>();
		private Combobox 				procesar;
		
		private Comboitem  rechazada 	= new Comboitem("Rechazada");
		private Comboitem  aceptada 	= new Comboitem("Aceptada");
		private Comboitem  procesada 	= new Comboitem("Procesada");
		
		public UISolicitudTransporte(String titulo, int ancho,List<Gerencia> gerencias,List<UbicacionSector> sectores) {
			super(titulo, ancho);
			this.gerencias = gerencias;
			this.sectores = sectores;
			// TODO Auto-generated constructor stub
		}



		@Override
		public void cargarValores(SolicitudTransporte arg0) 	throws ExcDatosInvalidos {
			// TODO Auto-generated method stub
			//cargar Geencias  
			//gerencia.setValue(getModelo().getGerencia().getDescripcion());
			sector.setSeleccion(getModelo().getSector());
			fechasalida.setValue(getModelo().getFechasalida());
			fechallegada.setValue(getModelo().getFechallegada());
			lugarSalida.setValue(getModelo().getLugarSalida());
			motivo.setValue(getModelo().getMotivo());
			nroPasajero.setValue(getModelo().getNroPasajero());
			estado.setValue(getModelo().getEstadoStr()); 
			justificacion.setValue(getModelo().getJustificacion());
			
			getBinder().addBinding(gerencia, "selectedItem",	getNombreModelo() + ".gerencia", null, null, "save", null,null, null, null);
			getBinder().addBinding(sector, "valorObjeto",	getNombreModelo() + ".sector", null, null, "save", null,null, null, null);
			getBinder().addBinding(fechasalida, "value",	getNombreModelo() + ".fechasalida", null, null, "save", null,null, null, null);
			getBinder().addBinding(fechallegada, "value",	getNombreModelo() + ".fechallegada", null, null, "save", null, null,null, null);
			getBinder().addBinding(lugarSalida, "value",	getNombreModelo() + ".lugarSalida", null, null, "save", null,null, null, null);
			getBinder().addBinding(motivo, "value",	getNombreModelo() + ".motivo", null, null, "save", null,null, null, null);
			getBinder().addBinding(nroPasajero, "value",	getNombreModelo() + ".nroPasajero", null, null, "save", null, null,null, null);
			getBinder().addBinding(justificacion, "value",	getNombreModelo() + ".justificacion", null, null, "save", null, null,null, null);
		}

		@Override
		public void dibujar() {
			// TODO Auto-generated method stub
				
		
		
			gbGeneral.addComponente("Estado ",estado);		
			gbGeneral.addComponente("N° Pasajeros ",nroPasajero);
			gbGeneral.addComponente("Fecha Salida",fechasalida);
			gbGeneral.addComponente("Fecha Llegada",fechallegada);
			gbGeneral.addComponente("Destino ",sector);
			gbGeneral.addComponente("Lugar Salida",lugarSalida);
	

			gbGerencia.addComponente("Motivo ",motivo);
			gbGerencia.addComponente("Gerencia",gerencia);
			
			gbGeneral.dibujar(this);
			gbGerencia.dibujar(this);
			
			if(((ContSolicitudTransporte)getControlador()).modoProcesar())
			{
				gbEstad0.addComponente("Justificacion ",justificacion);
				gbEstad0.addComponente("Estado ",procesar);
				gbEstad0.dibujar(this);
			}

			addBotonera();
		}	



		@Override
		public void inicializar() {
			// TODO Auto-generated method stub
			
			 gbGeneral = new CompGrupoDatos("Datos Solicitud de Transporte",4);
			 gbGerencia = new CompGrupoDatos("Datos Gerencia ",4);
			 gbEstad0 = new CompGrupoDatos("Estado Solicitud ", 4);

			 gerencia = new Combobox();
			 gerencia.setModel(new ListModelArray(gerencias));

			 gerencia.setItemRenderer(new ComboitemRenderer() {
				
				@Override
				public void render(Comboitem arg0, Object arg1) throws Exception {
					// TODO Auto-generated method stub
					Gerencia g = (Gerencia) arg1;
					arg0.setLabel(g.getDescripcion());
					arg0.setValue(arg1);
				}
			});
			 
			 fechasalida = new Datebox();
			 
			 fechallegada = new Datebox();

			 lugarSalida = new Textbox();
			 lugarSalida.setMaxlength(250);
			 lugarSalida.setRows(4);
			 
			 motivo = new Textbox();
			 motivo.setRows(3);
			 motivo.setMaxlength(250);
			 
			 nroPasajero = new Intbox();
			 
			 estado = new Label(); 
			 
			 justificacion = new Textbox();
			 justificacion.setMaxlength(250);
			 justificacion.setRows(3);

			 
			 sector = new CompBuscar<UbicacionSector>(getEncabezadoSector(),0);
			 sector.setAncho(400);
			 sector.setWidth("200px");
			 sector.setModelo(sectores);
		
		
			 procesar = new Combobox();
			 
			 rechazada.setValue(EstadoSolicitudTransporte.CANCELADA);
			 aceptada.setValue(EstadoSolicitudTransporte.ACEPTADA);
			 procesada.setValue(EstadoSolicitudTransporte.PROCESADA);

			 procesar.appendChild(rechazada);
			 procesar.appendChild(aceptada);
			 procesar.appendChild(procesada);
			 
			 
		}

		private List<CompEncabezado> getEncabezadoSector() {
			List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
			CompEncabezado titulo;

			titulo = new CompEncabezado("Sector", 250);
			titulo.setMetodoBinder("getNombre");
			titulo.setOrdenable(true);
			encabezado.add(titulo);

			titulo = new CompEncabezado("parroquia", 200);
			titulo.setMetodoBinder("getStrParroquia");
			titulo.setOrdenable(true);
			encabezado.add(titulo);

			titulo = new CompEncabezado("municipio", 200);
			titulo.setMetodoBinder("getStrMunicipio");
			titulo.setOrdenable(true);
			encabezado.add(titulo);

			titulo = new CompEncabezado("estado", 200);
			titulo.setMetodoBinder("getStrEstado");
			titulo.setOrdenable(true);
			encabezado.add(titulo);

			titulo = new CompEncabezado("pais", 150);
			titulo.setMetodoBinder("getStrPais");
			titulo.setOrdenable(true);
			encabezado.add(titulo);

			return encabezado;

		}



		public void desactivar(int modoOperacion) {
			// TODO Auto-generated method stub
			if(modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			{
				gerencia.setDisabled(true);
				sector.setDisabled(true);
				fechasalida.setDisabled(true);
				fechallegada.setDisabled(true);
				lugarSalida.setDisabled(true);
				motivo.setDisabled(true);
				nroPasajero.setDisabled(true);
				justificacion.setDisabled(true);
			}
			
			if(modoOperacion == Accion.PROCESAR)
			{
				gerencia.setDisabled(true);
				sector.setDisabled(true);
				fechasalida.setDisabled(true);
				fechallegada.setDisabled(true);
				lugarSalida.setDisabled(true);
				motivo.setDisabled(true);
				nroPasajero.setDisabled(true);
			}
			
		}



		public CompGrupoDatos getGbGeneral() {
			return gbGeneral;
		}



		public void setGbGeneral(CompGrupoDatos gbGeneral) {
			this.gbGeneral = gbGeneral;
		}



		public Combobox getGerencia() {
			return gerencia;
		}



		public void setGerencia(Combobox gerencia) {
			this.gerencia = gerencia;
		}



		public CompBuscar<UbicacionSector> getSector() {
			return sector;
		}



		public void setSector(CompBuscar<UbicacionSector> sector) {
			this.sector = sector;
		}



		public Datebox getFechasalida() {
			return fechasalida;
		}



		public void setFechasalida(Datebox fechasalida) {
			this.fechasalida = fechasalida;
		}



		public Datebox getFechallegada() {
			return fechallegada;
		}



		public void setFechallegada(Datebox fechallegada) {
			this.fechallegada = fechallegada;
		}



		public Textbox getLugarSalida() {
			return lugarSalida;
		}



		public void setLugarSalida(Textbox lugarSalida) {
			this.lugarSalida = lugarSalida;
		}



		public Textbox getMotivo() {
			return motivo;
		}



		public void setMotivo(Textbox motivo) {
			this.motivo = motivo;
		}



		public Intbox getNroPasajero() {
			return nroPasajero;
		}



		public void setNroPasajero(Intbox nroPasajero) {
			this.nroPasajero = nroPasajero;
		}



		public Label getEstado() {
			return estado;
		}



		public void setEstado(Label estado) {
			this.estado = estado;
		}



		public Textbox getJustificacion() {
			return justificacion;
		}



		public void setJustificacion(Textbox justificacion) {
			this.justificacion = justificacion;
		}



		public List<Gerencia> getGerencias() {
			return gerencias;
		}



		public void setGerencias(List<Gerencia> gerencias) {
			this.gerencias = gerencias;
		}



		public List<UbicacionSector> getSectores() {
			return sectores;
		} 


		public void setSectores(List<UbicacionSector> sectores) {
			this.sectores = sectores;
		}



		public Combobox getProcesar() {
			return procesar;
		}



		public void setProcesar(Combobox procesar) {
			this.procesar = procesar;
		}
		
		
	 
}
