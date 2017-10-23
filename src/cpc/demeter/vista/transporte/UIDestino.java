package cpc.demeter.vista.transporte;
 
 
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Combobox; 
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Intbox; 
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Textbox;  

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.transporte.Destino;
import cpc.modelo.ministerio.dimension.UbicacionEstado;
import cpc.modelo.ministerio.dimension.UbicacionMunicipio;
import cpc.modelo.ministerio.dimension.UbicacionParroquia;
import cpc.modelo.ministerio.dimension.UbicacionSector;

import cpc.test.pruebaAlmacen;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIDestino extends CompVentanaBase<Destino>{
	 
	
		private static final long serialVersionUID = 1L;
		
		private CompGrupoDatos						gbGeneral;		
		private Intbox 								id;
		
		private CompCombobox<UbicacionEstado>		estado;
		private CompCombobox<UbicacionMunicipio>	municipio;
		private CompCombobox<UbicacionParroquia>	parroquia;
		private CompCombobox<UbicacionSector>		sector;
		
		private Textbox 						    destino;
		private List<UbicacionEstado>			    estados;
		
		
		
		public UIDestino(String titulo, int tamano) {
			// TODO Auto-generated constructor stub
			super(titulo,tamano);
		}

		@Override
		public void inicializar() {
			// TODO Auto-generated method stub
			id        = new Intbox();
			id.setDisabled(true);
			
			estado    = new CompCombobox<UbicacionEstado>();
			municipio = new CompCombobox<UbicacionMunicipio>();
			parroquia = new CompCombobox<UbicacionParroquia>();
			sector    = new CompCombobox<UbicacionSector>();
			
			destino   = new Textbox();
			destino.setRows(3);
			destino.setMaxlength(250);
			
			gbGeneral = new CompGrupoDatos();
		
			//			agregar.addEventListener(Events.ON_CLICK,getControlador());
			estado.addEventListener(Events.ON_CHANGE,getControlador());
			municipio.addEventListener(Events.ON_CHANGE,getControlador());
			parroquia.addEventListener(Events.ON_CHANGE,getControlador());
			sector.addEventListener(Events.ON_CHANGE,getControlador());
			 
	}

		public Textbox getDestino() {
			return destino;
		}
		public void setDestino(Textbox destino) {
			this.destino = destino;
		}
		@Override
		public void dibujar() {
			// TODO Auto-generated method stub
			gbGeneral.setAnchoColumna(0, 100);
			gbGeneral.addComponente(" ID :",id);
			estado.setWidth("70%");
			municipio.setWidth("70%");
			parroquia.setWidth("70%");
			sector.setWidth("70%");
			
			gbGeneral.addComponente("Estado:",estado);
			gbGeneral.addComponente("Municipio:",municipio);
			gbGeneral.addComponente("Parroquia:",parroquia);
			gbGeneral.addComponente("Sector:",sector);
			
			
			destino.setWidth("99%");
			gbGeneral.addComponente("Destino:",destino);
			gbGeneral.dibujar(this);


			addBotonera();
			id.setValue(getModelo().getId());
			destino.setValue(getModelo().getDescripcion());
			 
		}
		
		@Override
		public void cargarValores(Destino dato) throws ExcDatosInvalidos {
 
		}
		public void desactivar(int modoOperacion){
			System.out.println("Buenas Soy  Yo22");
			if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ELIMINAR)
			{
				activarConsulta();
			} 
			else 
			{
				modoEdicion();
			}
		}

		public void activarConsulta(){
			destino.setDisabled(true);
			id.setDisabled(true);
			estado.setDisabled(true);
			municipio.setDisabled(true);
			parroquia.setDisabled(true);
			sector.setDisabled(true);
			destino.setDisabled(true);
		}
		
		 
		public CompCombobox<UbicacionMunicipio> getMunicipio() {
			return municipio;
		}
		public void setMunicipio(CompCombobox<UbicacionMunicipio> municipio) {
			this.municipio = municipio;
		}
		public CompCombobox<UbicacionParroquia> getParroquia() {
			return parroquia;
		}
		public void setParroquia(CompCombobox<UbicacionParroquia> parroquia) {
			this.parroquia = parroquia;
		}
		public List<UbicacionEstado> getEstados() {
			return estados;
		}
		public void setEstados(List<UbicacionEstado> estados) {
			this.estados = estados;
		}
		public void setSector(CompCombobox<UbicacionSector> sector) {
			this.sector = sector;
		}
		 
		
		
		public CompCombobox<UbicacionEstado> getEstado() {
			return estado;
		}
		public void setEstado(CompCombobox<UbicacionEstado> estado) {
			this.estado = estado;
		}
		public CompCombobox<UbicacionSector> getSector() {
			return sector;
		}
		public void CargarCampos(List<UbicacionEstado> estados,List<UbicacionMunicipio> municipios,List<UbicacionParroquia> parroquias,List<UbicacionSector> sectores){ 
		
			Destino dest = getModelo();

			
			municipio.setModelo(municipios);
			parroquia.setModelo(parroquias);
			sector.setModelo(sectores);
			estado.setModelo(estados);
			
			estado.setSeleccion(dest.getSector().getParroquia().getMunicipio().getEstado());
			municipio.setSeleccion(dest.getSector().getParroquia().getMunicipio());
			parroquia.setSeleccion(dest.getSector().getParroquia());
			sector.setSeleccion(dest.getSector());
			id.setText(dest.getId().toString());
			destino.setText(dest.getDescripcion());
			
			
		}
		public void CargarListados(List<UbicacionEstado> estados)
		{
			estado.setModelo(estados);
		}
		 
		public void modoEdicion(){
	
			destino.setDisabled(false);
			id.setDisabled(true);
			estado.setDisabled(false);
			municipio.setDisabled(false);
			parroquia.setDisabled(false);
			sector.setDisabled(false);
			destino.setDisabled(false);
			
		}
 
		
		
	}
	 