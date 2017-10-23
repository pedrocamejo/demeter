package cpc.demeter.vista.reporte;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.components.list.ListDesignConverter;

import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Window;

import cpc.demeter.comando.reporte.ComandoEstadosCuenta;
import cpc.modelo.ministerio.basico.TipoProductor;
import cpc.modelo.ministerio.dimension.UbicacionEstado;
import cpc.modelo.ministerio.dimension.UbicacionMunicipio;
import cpc.modelo.ministerio.dimension.UbicacionParroquia;
import cpc.modelo.ministerio.dimension.UbicacionSector;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.negocio.ministerio.basico.NegocioEje;
import cpc.zk.componente.listas.CompListaMultiple;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class EstadoCuentaUI extends Window  {

	
	
	private EventListener controlador;
	private CompGrupoDatos	 reporte;
	private CompGrupoDatos	 reporteSector;
	
	private CompGrupoDatos	 reporteSolvencia;
	private CompGrupoDatos	 reporteDeudor;

	private Listbox 		deudores ; // listado de todo los productores con deuda 
	private List<Productor>  listadeudores = new ArrayList<Productor>();

	private Listbox 		solventes ; // listado de todo los productores sin deuda :-D  
	private List<Productor> listsolventes = new ArrayList<Productor>();


	private Combobox	tipo;
	private Button		listadoSolvencia;// Listados Generales 
	private Button		validarSolvencia;
	private Button		listadoDeudor; // Listado Generales
	private Button 		addSolvencia;
	private Button 		delSolvencia;
	private Button 		addDeudor;
	private Button	    delDeudor;
	
	private Button 		generar;
	private Button		sector;
	private Button		generarSolvencia;
	private Button		generarDeudor;
	
	private Combobox	estado;
	private Combobox	municipio;
	private Combobox	parroquia;
	private Combobox	ubicacionsector;
	
	private Label 		totalElementosMostrar;
	
	private CompListaMultiple tipoProductor;
	private List<TipoProductor> tipoProductores;
	
	private NegocioEje			negocioEje = NegocioEje.getInstance();

	
	
	public EstadoCuentaUI(String title, String border, boolean closable,EventListener controlador) throws ExcFiltroExcepcion {
		super(title, border, closable);
		this.setWidth("850px;");
		// TODO Auto-generated constructor stub
		this.controlador = controlador;
		iniciar();
		dibujar();
	}

	
	private void dibujar() {
		// TODO Auto-generated method stub
			
		reporte.addComponente("Tipo",this.tipo);
		reporte.addComponente(" ",generar);
		reporte.addComponente("Tipo De Productor",tipoProductor);

		reporteSector.addComponente("Estado",estado);
		reporteSector.addComponente("Municipio",municipio);
		reporteSector.addComponente("Parroquia",parroquia);
		reporteSector.addComponente("Sector",ubicacionsector);
		reporteSector.addComponente("",sector);
		reporteSector.addComponente("Total Elementos Sector ",totalElementosMostrar);
		
		Hbox hbox = new Hbox();
		hbox.appendChild(addSolvencia);
		hbox.appendChild(delSolvencia);
		hbox.appendChild(validarSolvencia);
		hbox.setAlign("right");
		
		Hbox com = new Hbox();
		com.appendChild(hbox);
		com.appendChild(listadoSolvencia);	
		
		reporteSolvencia.addComponente(com);
		reporteSolvencia.addComponente(solventes);
		reporteSolvencia.addComponente(generarSolvencia);

		
		hbox = new Hbox();
		hbox.appendChild(addDeudor);
		hbox.appendChild(delDeudor);
		hbox.setAlign("right");

		com = new Hbox();
		com.appendChild(hbox);
		com.appendChild(listadoDeudor);	

		reporteDeudor.addComponente(com);
		reporteDeudor.addComponente(deudores);
		reporteDeudor.addComponente(generarDeudor);

		
		reporte.dibujar(this);
		reporteSector.dibujar(this);
		reporteSolvencia.dibujar(this);
		reporteDeudor.dibujar(this);
	}


	private void iniciar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

		reporte = new CompGrupoDatos("Reporte Estado de Cuenta General  ",4);
		reporteSector = new CompGrupoDatos("Reporte Estado de Cuenta (Sector de la Unidad Productiva)",4);
		reporteSolvencia = new CompGrupoDatos("Reporte de Solvencia ",1);
		reporteDeudor= new CompGrupoDatos("Reporte de Deudor  ",1); 
 
		
		listadoSolvencia = new Button("general");
		listadoSolvencia.setTooltip("Listado general de Solvencia ");
		listadoSolvencia.addEventListener(Events.ON_CLICK,getControlador());

		listadoDeudor = new Button("general");
		listadoDeudor.setTooltip("Listado general de Deudores ");
		listadoDeudor.addEventListener(Events.ON_CLICK,getControlador());

		
		addSolvencia = new Button("+");
		addSolvencia.addEventListener(Events.ON_CLICK,getControlador());

		delSolvencia = new Button("-");
		delSolvencia.addEventListener(Events.ON_CLICK,getControlador());

		
		addDeudor= new Button("+");
		addDeudor.addEventListener(Events.ON_CLICK,getControlador());

		delDeudor = new Button("-");
		delDeudor.addEventListener(Events.ON_CLICK,getControlador());
		
		validarSolvencia  = new Button("Validar");
		validarSolvencia.addEventListener(Events.ON_CLICK,((ComandoEstadosCuenta)getControlador()).validarSolvencia());
		
		generarDeudor = new Button("Generar");
		generarDeudor.addEventListener(Events.ON_CLICK,getControlador());
		
		deudores = new Listbox() ;
		Listhead head = new Listhead();
		head.setParent(deudores);
		new Listheader("Identidad ").setParent(head);
		new Listheader("Nombre ").setParent(head);
		new Listheader("Direccion ").setParent(head);
		
		deudores.setItemRenderer(new ListitemRenderer() {
			@Override
			public void render(Listitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				Productor p = (Productor) arg1;
				new Listcell(p.getIdentidadLegal()).setParent(arg0);
				new Listcell(p.getNombres()).setParent(arg0);
				new Listcell(p.getDireccion()).setParent(arg0);
			}
		});

		solventes = new Listbox(); 
		head = new Listhead();
		head.setParent(solventes);
		new Listheader("Identidad ").setParent(head);
		new Listheader("Nombre ").setParent(head);
		new Listheader("Direccion ").setParent(head);

		solventes.setItemRenderer(new ListitemRenderer() {
			
			@Override
			public void render(Listitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				Productor p = (Productor) arg1;
				new Listcell(p.getIdentidadLegal()).setParent(arg0);
				new Listcell(p.getNombres()).setParent(arg0);
				new Listcell(p.getDireccion()).setParent(arg0);
			}
		});
		
		generarSolvencia = new Button("Generar");
		generarSolvencia.addEventListener(Events.ON_CLICK,getControlador());
		
		tipo = new Combobox();
	
		generar = new Button("Generar");
		generar.addEventListener(Events.ON_CLICK,controlador);

		sector = new Button("Generar");
		sector.addEventListener(Events.ON_CLICK,controlador);

		estado = new Combobox();
		estado.addEventListener(Events.ON_SELECT,controlador);
		estado.setItemRenderer(new ComboitemRenderer() {
			
			@Override
			public void render(Comboitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				UbicacionEstado estado = (UbicacionEstado) arg1;
				arg0.setValue(arg1);
				arg0.setLabel(estado.getNombre());
			}
		});
		estado.setModel(new ListModelArray(negocioEje.getEstados()));

		municipio = new Combobox();
		municipio.addEventListener(Events.ON_SELECT,controlador);
		municipio.setItemRenderer(new ComboitemRenderer() {
			
			@Override
			public void render(Comboitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				UbicacionMunicipio obj = (UbicacionMunicipio) arg1;
				arg0.setValue(arg1);
				arg0.setLabel(obj.getNombre());
			}
		});

		parroquia  = new Combobox();
		parroquia.addEventListener(Events.ON_SELECT,controlador);
		parroquia.setItemRenderer(new ComboitemRenderer() {
			
			@Override
			public void render(Comboitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				UbicacionParroquia obj = (UbicacionParroquia) arg1;
				arg0.setValue(arg1);
				arg0.setLabel(obj.getNombre());
			}
		});

		ubicacionsector = new Combobox();
		ubicacionsector.addEventListener(Events.ON_SELECT,controlador);
		ubicacionsector.setItemRenderer(new ComboitemRenderer() {
			
			@Override
			public void render(Comboitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				UbicacionSector obj = (UbicacionSector) arg1;
				arg0.setValue(arg1);
				arg0.setLabel(obj.getNombre());
			}
		});

		List<String> tipo = new ArrayList<String>();
		tipo.add("Estados de cuenta total");
		tipo.add("Estados de cuenta documentos asociados");
		tipo.add("Estados de cuenta");
		
		tipo.add("Consolidados Total");
		tipo.add("Consolidados doc asociados");
		tipo.add("Consolidados sin documentos asociados");
		
		this.tipo.setModel(new ListModelArray(tipo));

		totalElementosMostrar = new Label("0 elementos ");
		tipoProductor = new CompListaMultiple("tipoProductor");
		tipoProductor.setRows(5);
		tipoProductor.setWidth("180px");
		//tipoProductor.setModelo(tipoProductores);
	}



	public EventListener getControlador() {
		return controlador;
	}



	public void setControlador(EventListener controlador) {
		this.controlador = controlador;
	}



	public CompGrupoDatos getReporte() {
		return reporte;
	}



	public void setReporte(CompGrupoDatos reporte) {
		this.reporte = reporte;
	}



	public CompGrupoDatos getReporteSector() {
		return reporteSector;
	}



	public void setReporteSector(CompGrupoDatos reporteSector) {
		this.reporteSector = reporteSector;
	}



	public Combobox getTipo() {
		return tipo;
	}



	public void setTipo(Combobox tipo) {
		this.tipo = tipo;
	}



	public Button getGenerar() {
		return generar;
	}



	public void setGenerar(Button generar) {
		this.generar = generar;
	}



	public Button getSector() {
		return sector;
	}



	public void setSector(Button sector) {
		this.sector = sector;
	}



	public Combobox getEstado() {
		return estado;
	}



	public void setEstado(Combobox estado) {
		this.estado = estado;
	}



	public Combobox getMunicipio() {
		return municipio;
	}



	public void setMunicipio(Combobox municipio) {
		this.municipio = municipio;
	}



	public Combobox getParroquia() {
		return parroquia;
	}



	public void setParroquia(Combobox parroquia) {
		this.parroquia = parroquia;
	}



	public Combobox getUbicacionsector() {
		return ubicacionsector;
	}



	public void setUbicacionsector(Combobox ubicacionsector) {
		this.ubicacionsector = ubicacionsector;
	}



	public NegocioEje getNegocioEje() {
		return negocioEje;
	}



	public void setNegocioEje(NegocioEje negocioEje) {
		this.negocioEje = negocioEje;
	}



	public Label getTotalElementosMostrar() {
		return totalElementosMostrar;
	}



	public void setTotalElementosMostrar(Label totalElementosMostrar) {
		this.totalElementosMostrar = totalElementosMostrar;
	}


	public CompGrupoDatos getReporteSolvencia() {
		return reporteSolvencia;
	}


	public void setReporteSolvencia(CompGrupoDatos reporteSolvencia) {
		this.reporteSolvencia = reporteSolvencia;
	}


	public CompGrupoDatos getReporteDeudor() {
		return reporteDeudor;
	}


	public void setReporteDeudor(CompGrupoDatos reporteDeudor) {
		this.reporteDeudor = reporteDeudor;
	}


	public Listbox getDeudores() {
		return deudores;
	}


	public void setDeudores(Listbox deudores) {
		this.deudores = deudores;
	}


	public List<Productor> getListadeudores() {
		return listadeudores;
	}


	public void setListadeudores(List<Productor> listadeudores) {
		this.listadeudores = listadeudores;
	}


	public Listbox getSolventes() {
		return solventes;
	}


	public void setSolventes(Listbox solventes) {
		this.solventes = solventes;
	}


	public List<Productor> getListsolventes() {
		return listsolventes;
	}


	public void setListsolventes(List<Productor> listsolventes) {
		this.listsolventes = listsolventes;
	}


	public Button getAddSolvencia() {
		return addSolvencia;
	}


	public void setAddSolvencia(Button addSolvencia) {
		this.addSolvencia = addSolvencia;
	}


	public Button getDelSolvencia() {
		return delSolvencia;
	}


	public void setDelSolvencia(Button delSolvencia) {
		this.delSolvencia = delSolvencia;
	}


	public Button getAddDeudor() {
		return addDeudor;
	}


	public void setAddDeudor(Button addDeudor) {
		this.addDeudor = addDeudor;
	}


	public Button getDelDeudor() {
		return delDeudor;
	}


	public void setDelDeudor(Button delDeudor) {
		this.delDeudor = delDeudor;
	}


	public Button getGenerarSolvencia() {
		return generarSolvencia;
	}


	public void setGenerarSolvencia(Button generarSolvencia) {
		this.generarSolvencia = generarSolvencia;
	}


	public Button getGenerarDeudor() {
		return generarDeudor;
	}

	
	

	public void setGenerarDeudor(Button generarDeudor) {
		this.generarDeudor = generarDeudor;
	}

	
	

	public Button getListadoSolvencia() {
		return listadoSolvencia;
	}


	public Button getValidarSolvencia() {
		return validarSolvencia;
	}


	public void setValidarSolvencia(Button validarSolvencia) {
		this.validarSolvencia = validarSolvencia;
	}


	public void setListadoSolvencia(Button listadoSolvencia) {
		this.listadoSolvencia = listadoSolvencia;
	}


	public Button getListadoDeudor() {
		return listadoDeudor;
	}


	public void setListadoDeudor(Button listadoDeudor) {
		this.listadoDeudor = listadoDeudor;
	}


	public void agregarSolvente(Productor productor) {
		// TODO Auto-generated method stub
		for(Productor p : listsolventes)
		{
			if(p.getIdentidadLegal().endsWith(productor.getIdentidadLegal()))
			{
				return;
			}
		}
		listsolventes.add(productor);
		solventes.setModel(new ListModelArray(listsolventes));
	}


	public void agregarDeudor(Productor productor) {
		// TODO Auto-generated method stub
		for(Productor p : listadeudores)
		{
			if(p.getIdentidadLegal().endsWith(productor.getIdentidadLegal()))
			{
				return;
			}
		}
		listadeudores.add(productor);
		deudores.setModel(new ListModelArray(listadeudores));
	}


	public void quitarSolvente(Productor productor) {
		// TODO Auto-generated method stub
			listsolventes.remove(productor);
			solventes.setModel(new ListModelArray(listsolventes));
	}


	public void quitarDeudor(Productor productor) {
		// TODO Auto-generated method stub
		listadeudores.remove(productor);
		deudores.setModel(new ListModelArray(listadeudores));
	}


	public CompListaMultiple getTipoProductor() {
		return tipoProductor;
	}


	public void setTipoProductor(CompListaMultiple tipoProductor) {
		this.tipoProductor = tipoProductor;
	}


	public List<TipoProductor> getTipoProductores() {
		return tipoProductores;
	}


	public void setTipoProductores(List<TipoProductor> tipoProductores) {
		this.tipoProductores = tipoProductores;
	}
	
	public void  setearTipoProductores(List<TipoProductor> tipoProductores) {
		setTipoProductores(tipoProductores);
		getTipoProductor().setModelo(tipoProductores);
		
	}
	
	
}


