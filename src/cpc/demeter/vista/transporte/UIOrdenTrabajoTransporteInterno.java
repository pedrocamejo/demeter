package cpc.demeter.vista.transporte;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.gestion.DetalleMaquinariaOrdenTrabajo;
import cpc.modelo.demeter.gestion.DetalleOrdenTrabajo;
import cpc.modelo.demeter.gestion.ImplementoUnidad;
import cpc.modelo.demeter.gestion.MaquinariaUnidad;
import cpc.modelo.demeter.gestion.OrdenTrabajoTransporteInterno;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.demeter.transporte.UbicacionTransporte;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompGrillaConBoton;
import cpc.zk.componente.ui.ComponentesAutomaticos;
import cpc.zk.componente.ventanas.CompGrupoBusqueda;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;

public class UIOrdenTrabajoTransporteInterno
		extends CompVentanaMaestroDetalle<OrdenTrabajoTransporteInterno, DetalleMaquinariaOrdenTrabajo> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6040384167133924698L;
	
	private List<Trabajador> operadores;
	private List<UbicacionTransporte> ubicaciones;
	private List<IProducto> articuloVentas;
	private List<UnidadFuncional> unidadesFuncionales;
	
	
	

	
	

	private List<CompEncabezado> listaDetalle;
	private CompBuscar<MaquinariaUnidad> maquinaria;
	private CompBuscar<ImplementoUnidad> implemento;

	private CompGrupoDatos gbGeneral;
	private Label dtmFechaEmision;
	private Datebox dtmFechaInicio;
	private Datebox dtmFechaFin;
	private Label LblNroControl;
	private Label LblSede;
	private CompCombobox<UnidadFuncional> unidad;
	private CompGrupoDatos cgdDirecciones;
	private CompGrupoBusqueda<UbicacionTransporte> origen;
	private CompGrupoBusqueda<UbicacionTransporte> destino;
	
	private int modo;

	private CompGrupoDatos gbServicios;
	private CompGrillaConBoton<DetalleOrdenTrabajo> compListaDetalleOrden ;
	
	private CompGrupoDatos cgdCierre;
	private CompGrupoBusqueda<UbicacionTransporte> origenReal;
	private CompGrupoBusqueda<UbicacionTransporte> destinoReal;
	private Doublebox DblKilometraje;
	private Datebox dtmFechaInicioVerificada;
	private Datebox dtmFechaFinVerificada;
	
	private CompBuscar<IProducto> cmbArticuloventa;

	


	public UIOrdenTrabajoTransporteInterno(String titulo, int ancho, int modo, List<Trabajador> operadores,
			 List<UbicacionTransporte> ubicaciones,List<IProducto> articuloVentas, List<UnidadFuncional> unidadFuncionales
			
			) {
		super(titulo);
		this.operadores = operadores;
		this.ubicaciones = ubicaciones;
		this.articuloVentas =articuloVentas;
		this.unidadesFuncionales=unidadFuncionales;
		this.modo = modo;
		
	}

	@Override
	public void inicializar() {
		//setGbEncabezado(1, "e");
		setGbDetalle("Detalle Maquinaria");
		//setGbPie(1, "sd");
		
		gbGeneral= new CompGrupoDatos("Datos Generales",4);
		
		
		LblNroControl = new Label();
		LblSede = new Label();
		unidad = new CompCombobox<UnidadFuncional>();
		
		dtmFechaEmision= new Label();
		dtmFechaInicio= new Datebox();
		dtmFechaFin=new Datebox();
		cgdDirecciones =new CompGrupoDatos("Direcciones",1);
		try {
			origen = new CompGrupoBusqueda<UbicacionTransporte>("Direccion Origen",
					getEncabezadoUbicaciones(), getModelo().getUbicacionInico(),
					ComponentesAutomaticos.LABEL);
			origen.setNuevo(new UbicacionTransporte());
			origen.setModeloCatalogo(ubicaciones);
			origen.setAnchoValores(130);
			origen.setAnchoCatalogo(850);
			origen.addListener(getControlador());
			//origen.getBuscador().setHeight("850px");
			//aumentamos el ancho del buscador
			
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		try {
			destino = new CompGrupoBusqueda<UbicacionTransporte>("Direccion Llegada",
					getEncabezadoUbicaciones(), getModelo().getUbicacionFinal(),
					ComponentesAutomaticos.LABEL);
			destino.setNuevo(new UbicacionTransporte());
			destino.setModeloCatalogo(ubicaciones);
			destino.setAnchoValores(130);
			destino.setAnchoCatalogo(850);
			destino.addListener(getControlador());
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		if (getModelo().getDetalles()!=null){
			List<DetalleOrdenTrabajo> listaDetalleOrden = getModelo().getDetalles();
			compListaDetalleOrden =new CompGrillaConBoton<DetalleOrdenTrabajo>(cargarListaDetalleOrden(articuloVentas),listaDetalleOrden, encabezadoDetalleOrdenTrabajo() , getControlador());	
		}else compListaDetalleOrden =new CompGrillaConBoton<DetalleOrdenTrabajo>(cargarListaDetalleOrden(articuloVentas),null, encabezadoDetalleOrdenTrabajo() , getControlador());
		compListaDetalleOrden.setNuevo(new DetalleOrdenTrabajo());
		
		listaDetalle= new ArrayList<CompEncabezado>();
	maquinaria= new CompBuscar<MaquinariaUnidad>(getEncabezadoEquipo(), 0);
		implemento= new CompBuscar<ImplementoUnidad>(getEncabezadoEquipo(), 0);

	
		gbServicios = new CompGrupoDatos("Servicios", 1);
		
		cgdCierre = new CompGrupoDatos("Cierre De Orden",1);
	
		DblKilometraje = new Doublebox();
		dtmFechaInicioVerificada = new Datebox();
		dtmFechaFinVerificada = new Datebox();
		try {
			origenReal = new CompGrupoBusqueda<UbicacionTransporte>("Direccion Origen Real",
					getEncabezadoUbicaciones(), getModelo().getUbicacionInico(),
					ComponentesAutomaticos.LABEL);
			origenReal.setNuevo(new UbicacionTransporte());
			origenReal.setModeloCatalogo(ubicaciones);
			origenReal.setAnchoValores(130);
			origenReal.setAnchoCatalogo(850);
			origenReal.addListener(getControlador());
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		try {
			destinoReal = new CompGrupoBusqueda<UbicacionTransporte>("Direccion Llegada Real",
					getEncabezadoUbicaciones(), getModelo().getUbicacionFinal(),
					ComponentesAutomaticos.LABEL);
			destinoReal.setNuevo(new UbicacionTransporte());
			destinoReal.setModeloCatalogo(ubicaciones);
			destinoReal.setAnchoValores(130);
			destinoReal.setAnchoCatalogo(850);
			destinoReal.addListener(getControlador());
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		 cmbArticuloventa= new CompBuscar<IProducto>(
					getEncabezadoArticulo2(), 3);
		unidad.setModelo(unidadesFuncionales);
	
		
		unidad.addEventListener("onSelect",getControlador());
		
		
		
		
	}
	
	private List<CompEncabezado> getEncabezadoUbicaciones() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		
		
		titulo = new CompEncabezado("Municipio", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreMunicipio");
		titulo.setMetodoModelo(".ubicacionMunicipio");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Parroquia", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreParroquia");
		titulo.setMetodoModelo(".ubicacionParroquia");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Sector", 200);
		titulo.setMetodoBinder("getNombreSector");
		titulo.setMetodoModelo(".ubicacionSector");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Direccion", 150);
		titulo.setMetodoBinder("getDireccion");
		titulo.setOrdenable(true);
	
		encabezado.add(titulo);

		titulo = new CompEncabezado("Referencia", 150);
		titulo.setMetodoBinder("getReferencia");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		
		return encabezado;
	}


	@Override
	public void dibujar() {
	
		gbGeneral.addComponente("Fecha Elaboracion", dtmFechaEmision);
		gbGeneral.addComponente("NroControl", LblNroControl);
		gbGeneral.addComponente("Sede", LblSede);
		gbGeneral.addComponente("Unida Ejecutora", unidad);
		gbGeneral.addComponente("Fecha Inicio", dtmFechaInicio);
		gbGeneral.addComponente("Fecha Regreso", dtmFechaFin);
		gbGeneral.dibujar(this);
		
		
		origen.dibujar();
		cgdDirecciones.addComponente(origen);
		cgdDirecciones.addComponente(new Label("deberiamostrarOrigen"));
		destino.dibujar();
		cgdDirecciones.addComponente(destino);
		cgdDirecciones.addComponente(new Label("deberiamostrarDestino"));
		
		cgdDirecciones.dibujar(this);
		
		
	
	

		compListaDetalleOrden.dibujar();
		gbServicios.addComponente("servicio Ejecutado",compListaDetalleOrden);
		
	
		gbServicios.dibujar(this);
		
		origenReal.dibujar();
		cgdCierre.addComponente(origenReal);
		destinoReal.dibujar();
		cgdCierre.addComponente(destinoReal);
		cgdCierre.addComponente("Kilometraje Real",DblKilometraje);
		cgdCierre.addComponente("Fecha de Inicio Verificada",dtmFechaInicioVerificada);
		cgdCierre.addComponente("Fecha de Regreso",dtmFechaFinVerificada);
		
	
		cgdCierre.dibujar(this);
		setDetalles(cargarListaDetalle(),getModelo().getEquipos(), getListaDetalle());
		getDetalle().setNuevo(new DetalleMaquinariaOrdenTrabajo());
		getDetalle().setListenerBorrar(getControlador());
		dibujarEstructura();
		addBotonera();
		
	}

	@Override
	public void cargarValores(OrdenTrabajoTransporteInterno dato)
			throws ExcDatosInvalidos {
		if (modo == Accion.AGREGAR) {
			
		}else
		{	unidad.setSeleccion(dato.getUnidadFuncional());
	
			getDtmFechaEmision().setValue(dato.getStrFecha());
			getDtmFechaInicio().setValue(dato.getInicioServicio());
			getDtmFechaFin().setValue(dato.getFinServicio());
			LblSede.setValue(dato.getSede().getNombre());
			LblNroControl.setValue(dato.getNroControl());
			if (dato.getDetalles()!=null){
				compListaDetalleOrden.setColeccion(dato.getDetalles());
			}
			if (dato.getCerrada()){
				
			getDblKilometraje().setValue(dato.getKilometraje());
			getDtmFechaInicioVerificada().setValue(dato.getInicioServicioVerificado());
			getDtmFechaFinVerificada().setValue(dato.getFinServicioVerificado());
			}
		
		}
		
System.out.println( getNombreModelo() + ".ubicacionInico");
		getBinder().addBinding(origen, "modelo", getNombreModelo() + ".ubicacionInico",
				null, null, "save", null, null, null, null);
		getBinder().addBinding(destino, "modelo", getNombreModelo() + ".ubicacionFinal",
				null, null, "save", null, null, null, null);
		getBinder().addBinding(origenReal, "modelo", getNombreModelo() + ".ubicacionInicoReal",
				null, null, "save", null, null, null, null);
		getBinder().addBinding(destinoReal, "modelo", getNombreModelo() + ".ubicacionFinalReal",
				null, null, "save", null, null, null, null);
		if (dato.getUbicacionInico() != null) {
			try {
				origen.actualizarValores();
				
			} catch (ExcAccesoInvalido e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		if (dato.getUbicacionFinal() != null) {
			try {
				destino.actualizarValores();
				
			} catch (ExcAccesoInvalido e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		if (dato.getUbicacionInicoReal() != null) {
			try {
				origenReal.actualizarValores();
				
			} catch (ExcAccesoInvalido e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		if (dato.getUbicacionFinalReal() != null) {
			try {
				destinoReal.actualizarValores();
				
			} catch (ExcAccesoInvalido e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	 
		
	}
	
	
	private List<CompEncabezado> getListaDetalle() {

		listaDetalle = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Operador", 180);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getOperador");
		titulo.setMetodoModelo(".operador");
		listaDetalle.add(titulo);

		titulo = new CompEncabezado("Maquinaria", 220);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getMaquinaria");
		titulo.setMetodoModelo(".maquinaria");
		listaDetalle.add(titulo);

		// Serial de la Maquinaria
		titulo = new CompEncabezado("Detalle Maquinaria", 170);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getDetalleMaquinaria");
		// titulo.setMetodoModelo(".maquinaria");
		listaDetalle.add(titulo);

		titulo = new CompEncabezado("Implemento", 220);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getImplemento");
		titulo.setMetodoModelo(".implemento");
		listaDetalle.add(titulo);

		// Serial del Implemento
		titulo = new CompEncabezado("Detalle Implemento", 170);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getDetalleImplemento");
		// titulo.setMetodoModelo(".maquinaria");
		listaDetalle.add(titulo);
		return listaDetalle;
	}

	private ArrayList<Component> cargarListaDetalle() {
		ArrayList<Component> lista = new ArrayList<Component>();
		maquinaria = new CompBuscar<MaquinariaUnidad>(getEncabezadoEquipo(), 3);
		implemento = new CompBuscar<ImplementoUnidad>(getEncabezadoEquipo(), 3);
		CompCombobox<Trabajador> operador = new CompCombobox<Trabajador>();
		Label serialMaquinaria = new Label();
		Label serialImplemento = new Label();

		maquinaria.setWidth("180px");
		maquinaria.setAncho(650);
		implemento.setWidth("180px");
		implemento.setAncho(650);
		operador.setWidth("150px");
		serialMaquinaria.setWidth("150px");
		serialImplemento.setWidth("150px");
		operador.setModelo(operadores);

		maquinaria.setAttribute("nombre", "maquinaria");
		implemento.setAttribute("nombre", "implemento");
		maquinaria.setListenerEncontrar(getControlador());
		implemento.setListenerEncontrar(getControlador());

		lista.add(operador);
		lista.add(maquinaria);
		lista.add(serialMaquinaria);
		lista.add(implemento);
		lista.add(serialImplemento);
		return lista;
	}
	
	private List<CompEncabezado> getEncabezadoEquipo() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Codigo. ", 120);
		titulo.setMetodoBinder("getCodigoActivo");
		lista.add(titulo);

		titulo = new CompEncabezado("Ejemplar. ", 120);
		titulo.setMetodoBinder("getIdEjemplarActivo");
		lista.add(titulo);

		titulo = new CompEncabezado("Serial. ", 150);
		titulo.setMetodoBinder("getSerial");
		lista.add(titulo);

		titulo = new CompEncabezado("Denominación. ", 300);
		titulo.setMetodoBinder("getNombre");
		lista.add(titulo);

		titulo = new CompEncabezado("Marca. ", 140);
		titulo.setMetodoBinder("getMarca");
		lista.add(titulo);

		titulo = new CompEncabezado("Modelo. ", 140);
		titulo.setMetodoBinder("getModelo");
		lista.add(titulo);

		return lista;
	}

	private List<CompEncabezado> encabezadoDetalleOrdenTrabajo() {
		
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Labor", 340);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getLabor");
		titulo.setMetodoModelo(".labor");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("unidadTrabajo", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrUnidadTrabajo");
		//titulo.setMetodoModelo(".unidadTrabajo");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("cantidad Solicitada", 80);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidadSolicitada");
		titulo.setMetodoModelo(".cantidadSolicitada");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);
		titulo = new CompEncabezado("cantidad Ejecutada", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidadEjecutada");
		titulo.setMetodoModelo(".cantidadEjecutada");
		
		encabezado.add(titulo);

		
		return encabezado;
		
	}
	
	private ArrayList<Component> cargarListaDetalleOrden(List<IProducto> articuloVentas) {

		ArrayList<Component> lista = new ArrayList<Component>();
		cmbArticuloventa  = new CompBuscar<IProducto>(
				getEncabezadoArticulo2(), 3);

		cmbArticuloventa.setModelo(articuloVentas);
		Label unidad = new Label();
		Doublebox cantidadSolicitada = new Doublebox();
		Doublebox cantidadEjecutada =  new Doublebox();
	//	Doublebox cantidadReal = new Doublebox();
		cmbArticuloventa.setWidth("340px");
		unidad.setWidth("60px");
		cantidadEjecutada.setWidth("80px");
		cantidadSolicitada.setWidth("90px");
		cmbArticuloventa.setAncho(650);
		// para blokear cantidad y servicio

		cantidadSolicitada.setDisabled(false);
		cantidadEjecutada.setDisabled(true);
	//	cantidadReal.setDisabled(true);
		cantidadSolicitada.addEventListener(Events.ON_CHANGE, getControlador());
		cantidadEjecutada.addEventListener(Events.ON_CHANGE, getControlador());
		cmbArticuloventa.setListenerEncontrar(getControlador());
		cmbArticuloventa.setAttribute("nombre", "articulo");

		lista.add(cmbArticuloventa);
		lista.add(unidad);
		lista.add(cantidadSolicitada);
		lista.add(cantidadEjecutada);
	//	lista.add(cantidadReal);

		return lista;
	}
	
	public List<Trabajador> getOperadores() {
		return operadores;
	}

	public void setOperadores(List<Trabajador> operadores) {
		this.operadores = operadores;
	}

	
	
	public CompBuscar<MaquinariaUnidad> getMaquinaria() {
		return maquinaria;
	}

	public void setMaquinaria(CompBuscar<MaquinariaUnidad> maquinaria) {
		this.maquinaria = maquinaria;
	}

	public CompBuscar<ImplementoUnidad> getImplemento() {
		return implemento;
	}

	public void setImplemento(CompBuscar<ImplementoUnidad> implemento) {
		this.implemento = implemento;
	}

	public CompGrupoDatos getGbGeneral() {
		return gbGeneral;
	}

	public void setGbGeneral(CompGrupoDatos gbGeneral) {
		this.gbGeneral = gbGeneral;
	}


	public Label getLblNroControl() {
		return LblNroControl;
	}

	public void setLblNroControl(Label lblNroControl) {
		LblNroControl = lblNroControl;
	}

	public Label getLblSede() {
		return LblSede;
	}

	public void setLblSede(Label lblSede) {
		LblSede = lblSede;
	}

	public int getModo() {
		return modo;
	}

	public void setModo(int modo) {
		this.modo = modo;
	}

	public void setListaDetalle(List<CompEncabezado> listaDetalle) {
		this.listaDetalle = listaDetalle;
	}
	
	

	public void refrescarMaquinaria(List<MaquinariaUnidad> activos) {
		maquinaria.setModelo(activos);
	}

	public void refrescarImplementos(List<ImplementoUnidad> activos) {
		implemento.setModelo(activos);
	}
	
	
	public Label getDtmFechaEmision() {
		return dtmFechaEmision;
	}

	public void setDtmFechaEmision(Label dtmFechaEmision) {
		this.dtmFechaEmision = dtmFechaEmision;
	}

	

	
	private List<CompEncabezado> getEncabezadoArticulo2() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Serv/Art", 100);
		titulo.setMetodoBinder("getStrTipoProducto");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo", 150);
		titulo.setMetodoBinder("getStrTipo");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Clase", 150);
		titulo.setMetodoBinder("getStrClase");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Producto", 250);
		titulo.setMetodoBinder("getDescripcion");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fabricante", 250);
		titulo.setMetodoBinder("getCodigoFabricante");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}
	
	public List<IProducto> getArticuloVentas() {
		return articuloVentas;
	}

	public void setArticuloVentas(List<IProducto> articuloVentas) {
		this.articuloVentas = articuloVentas;
	}

	public CompCombobox<UnidadFuncional> getUnidad() {
		return unidad;
	}

	public void setUnidad(CompCombobox<UnidadFuncional> unidad) {
		this.unidad = unidad;
	}


	public CompGrupoBusqueda<UbicacionTransporte> getOrigen() {
		return origen;
	}

	public void setOrigen(CompGrupoBusqueda<UbicacionTransporte> origen) {
		this.origen = origen;
	}

	public CompGrupoBusqueda<UbicacionTransporte> getDestino() {
		return destino;
	}

	public void setDestino(CompGrupoBusqueda<UbicacionTransporte> destino) {
		this.destino = destino;
	}

	public Doublebox getDblKilometraje() {
		return DblKilometraje;
	}

	public void setDblKilometraje(Doublebox dblKilometraje) {
		DblKilometraje = dblKilometraje;
	}


	public CompBuscar<IProducto> getCmbArticuloventa() {
		return cmbArticuloventa;
	}

	public void setCmbArticuloventa(CompBuscar<IProducto> cmbArticuloventa) {
		this.cmbArticuloventa = cmbArticuloventa;
	}
	
	public void modoAgregar(){
		cgdCierre.detach();
	}
	
	public void modoConsultar(){
		getUnidad().setDisabled(true);
		dtmFechaFin.setDisabled(true);
		dtmFechaInicio.setDisabled(true);
		compListaDetalleOrden.setDisable(true);
		getOrigen().setDisabled(true);
		getDestino().setDisabled(true);
		desactivarDetalle();
		if (!getModelo().getEstado().getDescripcion().equals("TERMINADO"))
		cgdCierre.detach();
		else {
			getOrigenReal().setDisabled(true);
			getDestinoReal().setDisabled(true);
			getDblKilometraje().setReadonly(true);
			dtmFechaFinVerificada.setDisabled(true);
			dtmFechaInicioVerificada.setDisabled(true);
			compListaDetalleOrden.setDisable(true);
		}
	
		
	}
	public void modoProcesar(){
		getOrigen().setDisabled(true);
		getDestino().setDisabled(true);
		getUnidad().setDisabled(true);
		dtmFechaFin.setDisabled(true);
		dtmFechaInicio.setDisabled(true);
		desactivarDetalle();
		getDetalle().setDisabled(true);
		
	
	}
	
	public void setLabores(List<IProducto> articulos){
		cmbArticuloventa.setModelo(articulos);
		
	}

	public Datebox getDtmFechaFin() {
		return dtmFechaFin;
	}

	public void setDtmFechaFin(Datebox dtmFechaFin) {
		this.dtmFechaFin = dtmFechaFin;
	}

	public CompGrupoDatos getGbServicios() {
		return gbServicios;
	}

	public void setGbServicios(CompGrupoDatos gbServicios) {
		this.gbServicios = gbServicios;
	}

	public CompGrillaConBoton<DetalleOrdenTrabajo> getCompListaDetalleOrden() {
		return compListaDetalleOrden;
	}

	public void setCompListaDetalleOrden(
			CompGrillaConBoton<DetalleOrdenTrabajo> compListaDetalleOrden) {
		this.compListaDetalleOrden = compListaDetalleOrden;
	}

	public CompGrupoBusqueda<UbicacionTransporte> getOrigenReal() {
		return origenReal;
	}

	public void setOrigenReal(CompGrupoBusqueda<UbicacionTransporte> origenReal) {
		this.origenReal = origenReal;
	}

	public CompGrupoBusqueda<UbicacionTransporte> getDestinoReal() {
		return destinoReal;
	}

	public void setDestinoReal(CompGrupoBusqueda<UbicacionTransporte> destinoReal) {
		this.destinoReal = destinoReal;
	}

	public Datebox getDtmFechaInicioVerificada() {
		return dtmFechaInicioVerificada;
	}

	public void setDtmFechaInicioVerificada(Datebox dtmFechaInicioVerificada) {
		this.dtmFechaInicioVerificada = dtmFechaInicioVerificada;
	}

	public Datebox getDtmFechaFinVerificada() {
		return dtmFechaFinVerificada;
	}

	public void setDtmFechaFinVerificada(Datebox dtmFechaFinVerificada) {
		this.dtmFechaFinVerificada = dtmFechaFinVerificada;
	}

	public void setDtmFechaInicio(Datebox dtmFechaInicio) {
		this.dtmFechaInicio = dtmFechaInicio;
	}

	public Datebox getDtmFechaInicio() {
		return dtmFechaInicio;
	};
	
	
	
	
}


