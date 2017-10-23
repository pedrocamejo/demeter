package cpc.demeter.vista.transporte;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.CotizacionTransporte;
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.gestion.DetalleMaquinariaOrdenTrabajo;
import cpc.modelo.demeter.gestion.ImplementoUnidad;
import cpc.modelo.demeter.gestion.MaquinariaUnidad;
import cpc.modelo.demeter.gestion.OrdenTrabajoTransporte;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.demeter.transporte.UbicacionTransporte;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompGrillaConBoton;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.ui.ComponentesAutomaticos;
import cpc.zk.componente.ventanas.CompGrupoBusqueda;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;

public class UIOrdenTrabajoTransporte
		extends
		CompVentanaMaestroDetalle<OrdenTrabajoTransporte, DetalleMaquinariaOrdenTrabajo> {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6040384167133924698L;
	
	private List<Trabajador> operadores;
	private List<UbicacionTransporte> ubicaciones;
	private List<IProducto> articuloVentas;
	private List<UnidadFuncional> unidadesFuncionales;
	
	private CompGrupoDatos cgdCotizacion;
	private CompBuscar<CotizacionTransporte> cotizacion;
	private Label LblNombrePagador;
	private Label LblCedulaRifPagado;
	private Label lblDireccionPagador;
	private Label dtmFechaEmision;
	private Label dtmFechaInicio;
	private Label lblFechaHasta;
	private Intbox lblDiasVigencia;
	

	private CompGrupoDatos cgdDireccionOrigen;
	private Label  LblMunicipioOrigen;
	private Label  LblParroquiaOrigen;
	private Label 	LblSectorOrigen;
	private Textbox direccionOrigen;
	private Textbox refereciaOrigen;

	private CompGrupoDatos cgdDireccionDestino;
	private Label  LblMunicipioDestino;
	private Label  LblParroquiaDestino;
	private Label LblSectorDestino;
	private Textbox direccionDestino;
	private Textbox refereciaDestino;
	
	private CompLista<DetalleContrato> compLista ;

	private List<CompEncabezado> listaDetalle;
	private CompBuscar<MaquinariaUnidad> maquinaria;
	private CompBuscar<ImplementoUnidad> implemento;

	private CompGrupoDatos gbGeneral;
	private Label dtmFechaOrden;
	private Label LblNroControl;
	private Label LblSede;
	private CompCombobox<UnidadFuncional> unidad;
	private Datebox fechaSalida;
	
	private int modo;

	private CompGrupoDatos gbServicios;
	
	private CompGrupoDatos cgdCierre;
	private CompGrupoBusqueda<UbicacionTransporte> origen;
	private CompGrupoBusqueda<UbicacionTransporte> destino;
	private Doublebox DblKilometraje; 
	private Datebox	 fechaRegreso;
	private CompGrillaConBoton<DetalleContrato> detallecierre;
	private CompBuscar<IProducto> cmbArticuloventa;

	


	public UIOrdenTrabajoTransporte(String titulo, int ancho, int modo, List<Trabajador> operadores,
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
		
		dtmFechaOrden = new Label();
		LblNroControl = new Label();
		LblSede = new Label();
		unidad = new CompCombobox<UnidadFuncional>();
		fechaSalida = new Datebox();
		
		
		
		
		cgdCotizacion =new CompGrupoDatos("Datos Cotizacion", 2);
		cotizacion = new CompBuscar<CotizacionTransporte>(getEncabezadoCotizacion(),0);
		cotizacion.setAncho(650);
		LblNombrePagador= new Label() ;
		LblCedulaRifPagado= new Label();
		lblDireccionPagador= new Label();
		dtmFechaEmision= new Label();
		dtmFechaInicio= new Label();
		lblFechaHasta= new Label();
		lblDiasVigencia= new Intbox();
		
		
		
		cgdDireccionOrigen= new CompGrupoDatos("Direccion de origen",4);
		LblMunicipioOrigen= new Label();
		LblParroquiaOrigen= new Label();
		LblSectorOrigen= new Label();
		direccionOrigen= new Textbox();
		refereciaOrigen= new Textbox();
		
		
		
		cgdDireccionDestino= new CompGrupoDatos("Direccion de Destino",4);
		LblMunicipioDestino= new Label();
		LblParroquiaDestino= new Label();
		LblSectorDestino= new Label();
		direccionDestino= new Textbox();
		refereciaDestino= new Textbox();
		
		

		
		compLista = new CompLista<DetalleContrato>(encabezadoDetalleContrato());
		listaDetalle= new ArrayList<CompEncabezado>();
	maquinaria= new CompBuscar<MaquinariaUnidad>(getEncabezadoEquipo(), 0);
		implemento= new CompBuscar<ImplementoUnidad>(getEncabezadoEquipo(), 0);

	
		gbServicios = new CompGrupoDatos("Servicios", 1);
		
		cgdCierre = new CompGrupoDatos("Cierre De Orden",1);
		try {
			origen = new CompGrupoBusqueda<UbicacionTransporte>("Direccion Origen",
					getEncabezadoUbicaciones(), getModelo().getUbicacionInico(),
					ComponentesAutomaticos.LABEL);
			origen.setNuevo(new UbicacionTransporte());
			origen.setModeloCatalogo(ubicaciones);
			origen.setAnchoValores(120);
			origen.setAnchoCatalogo(800);
			origen.addListener(getControlador());
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
			destino.setAnchoValores(120);
			destino.setAnchoCatalogo(800);
			destino.addListener(getControlador());
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DblKilometraje = new Doublebox();
		fechaRegreso = new Datebox();
		 if (getModelo().getCotizacionTransporte()!=null)   {
		
			 List<DetalleContrato> original = getModelo().getCotizacionTransporte().getDetallesContrato();
			// ArrayList<DetalleContrato> listacierre = (ArrayList<DetalleContrato>) ((ArrayList<DetalleContrato>) getModelo().getCotizacionTransporte().getDetallesContrato()).clone();
			 List<DetalleContrato> listacierre = new ArrayList<DetalleContrato>();
			try {
				for (DetalleContrato detalleContrato : original) {
					DetalleContrato as = (DetalleContrato) BeanUtils.cloneBean(detalleContrato);
					
					//BeanUtils.copyProperties(as, detalleContrato);
					listacierre.add(as);
				}
				
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
		detallecierre =new CompGrillaConBoton<DetalleContrato>(cargarListaCierre(articuloVentas),listacierre, encabezadosListaCierre() , getControlador());
		}
		else detallecierre =new CompGrillaConBoton<DetalleContrato>(cargarListaCierre(articuloVentas), null, encabezadosListaCierre() , getControlador());;
		detallecierre.setNuevo(new DetalleContrato());
		 cmbArticuloventa= new CompBuscar<IProducto>(
					getEncabezadoArticulo2(), 3);
		unidad.setModelo(unidadesFuncionales);
	
		cotizacion.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
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
	
		gbGeneral.addComponente("Fecha Elaboracion", dtmFechaOrden);
		gbGeneral.addComponente("NroControl", LblNroControl);
		gbGeneral.addComponente("Sede", LblSede);
		gbGeneral.addComponente("Unida Ejecutora", unidad);
		gbGeneral.addComponente("Fecha Salida", fechaSalida);
		gbGeneral.dibujar(this);
		
	
		cgdCotizacion.addComponente("Cotizacion", cotizacion);
		cgdCotizacion.addComponente("Pagador", LblNombrePagador);
		cgdCotizacion.addComponente("Cedula/Rif", LblCedulaRifPagado);
		cgdCotizacion.addComponente("Elaborada", dtmFechaEmision);
		cgdCotizacion.addComponente("Inicio Plazo", dtmFechaInicio);
		cgdCotizacion.addComponente("Fin Plazo", lblFechaHasta);
		cgdCotizacion.addComponente("Dias Vigencia", lblDiasVigencia);
		cgdCotizacion.dibujar(this);
	
		cgdDireccionOrigen.addComponente("Municipio", LblMunicipioOrigen);
		cgdDireccionOrigen.addComponente("Parroquia", LblParroquiaOrigen);
		cgdDireccionOrigen.addComponente("Sector", LblSectorOrigen);
		cgdDireccionOrigen.addComponente("Direccion", direccionOrigen);
		cgdDireccionOrigen.addComponente("Referencia",refereciaOrigen);
		cgdDireccionOrigen.dibujar(this);
		
		
		cgdDireccionDestino.addComponente("Municipio", LblMunicipioDestino);
		cgdDireccionDestino.addComponente("Parroquia", LblParroquiaDestino);
		cgdDireccionDestino.addComponente("Sector", LblSectorDestino);
		cgdDireccionDestino.addComponente("Direccion", direccionDestino);
		cgdDireccionDestino.addComponente("Referencia",refereciaDestino);
		cgdDireccionDestino.dibujar(this);
		
		gbServicios.addComponente(compLista);
		gbServicios.dibujar(this);
		
		origen.dibujar();
		cgdCierre.addComponente(origen);
		destino.dibujar();
		cgdCierre.addComponente(destino);
		cgdCierre.addComponente("Kilometraje Real",DblKilometraje);
		cgdCierre.addComponente("Fecha de Regreso",fechaRegreso);
		detallecierre.dibujar();
		cgdCierre.addComponente("detalleCiere",detallecierre);
		cgdCierre.dibujar(this);
		setDetalles(cargarListaDetalle(),getModelo().getEquipos(), getListaDetalle());
		getDetalle().setNuevo(new DetalleMaquinariaOrdenTrabajo());
		getDetalle().setListenerBorrar(getControlador());
		dibujarEstructura();
		addBotonera();
		
	}

	@Override
	public void cargarValores(OrdenTrabajoTransporte dato)
			throws ExcDatosInvalidos {
		if (modo == Accion.AGREGAR) {
			
		}else
		{	unidad.setSeleccion(dato.getUnidadFuncional());
			cotizacion.setSeleccion((CotizacionTransporte) dato.getCotizacionTransporte());
			cotizacion.setText((dato.getCotizacionTransporte().getStrNroDocumento()));
			getFechaSalida().setValue(dato.getInicioServicio());
			getDtmFechaOrden().setValue(dato.getStrFecha());
			actualizarCotizacion();
			LblSede.setValue(dato.getSede().getNombre());
			LblNroControl.setValue(dato.getNroControl());
			
			if (dato.getCerrada()){
			detallecierre.setColeccion(cotizacion.getSeleccion().getDetallesContrato());
			getDblKilometraje().setValue(dato.getKilometraje());;}
			
			System.out.println(getNombreModelo() + ".ubicacionInico");
			getBinder().addBinding(origen, "modelo", getNombreModelo() + ".ubicacionInico",
					null, null, "save", null, null, null, null);
			getBinder().addBinding(destino, "modelo", getNombreModelo() + ".ubicacionFinal",
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
		 
			
		}
		
	}
	
	public void actualizarCotizacion()	{
	CotizacionTransporte cotizacion=	getCotizacion().getSeleccion();
		getLblNombrePagador().setValue(cotizacion.getNombrePagador());
		getLblCedulaRifPagado().setValue(cotizacion.getPagador().getIdentidadLegal());
		getDtmFechaInicio().setValue(cotizacion.getFechaDesdeString());
		getLblFechaHasta().setValue(cotizacion.getFechaHastaString());
		getLblDiasVigencia().setValue(cotizacion.getDiasVigencia());
		
		UbicacionTransporte origen = cotizacion.getDireccionOrigen();		
		getLblMunicipioOrigen().setValue(origen.getNombreMunicipio());
		getLblParroquiaOrigen().setValue(origen.getNombreParroquia());;
		getLblSectorOrigen().setValue(origen.getNombreSector());;
		getDireccionOrigen().setValue(origen.getDireccion());;
		getRefereciaOrigen().setValue(origen.getReferencia());;
				
		UbicacionTransporte Destino = cotizacion.getDireccionLlegada();		
		getLblMunicipioDestino().setValue(Destino.getNombreMunicipio());
		getLblParroquiaDestino().setValue(Destino.getNombreParroquia());
		getLblSectorDestino().setValue(Destino.getNombreSector());
		getDireccionDestino().setValue(Destino.getDireccion());
		getRefereciaDestino().setValue(Destino.getReferencia());
		
		List<DetalleContrato> lista = cotizacion.getDetallesContrato();
		
		compLista.setModelo(cotizacion.getDetallesContrato());
		
	}
	

	private List<CompEncabezado> encabezadoDetalleContrato() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Servicio/Articulo", 340);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getProducto");
		titulo.setMetodoModelo(".producto");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cantidad", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidad");
		titulo.setMetodoModelo(".cantidad");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Precio Und", 80);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getPrecioUnidad");
		titulo.setMetodoModelo(".precioUnidad");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);
		titulo = new CompEncabezado("Precio", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getSubtotal");
		titulo.setMetodoModelo(".subtotal");
		
		encabezado.add(titulo);

		
		return encabezado;
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

	private List<CompEncabezado> getEncabezadoCotizacion() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Control", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrNroDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getFechaString");
		encabezado.add(titulo);

		// titulo = new CompEncabezado("Cliente",290);
		titulo = new CompEncabezado("Pagador", 150);
		titulo.setOrdenable(true);
		// de prueba titulo.setMetodoBinder("getbeneficiado");
		titulo.setMetodoBinder("getNombreCliente");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Desde", 90);
		titulo.setOrdenable(true);
		// de prueba titulo.setMetodoBinder("getbeneficiado");
		titulo.setMetodoBinder("getFechaDesde");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Hasta", 90);
		titulo.setOrdenable(true);
		// de prueba titulo.setMetodoBinder("getbeneficiado");
		titulo.setMetodoBinder("getFechaHastaString");
		encabezado.add(titulo);


		
		return encabezado;
	}

	public List<Trabajador> getOperadores() {
		return operadores;
	}

	public void setOperadores(List<Trabajador> operadores) {
		this.operadores = operadores;
	}

	public CompGrupoDatos getCgdCotizacion() {
		return cgdCotizacion;
	}

	public void setCgdCotizacion(CompGrupoDatos cgdCotizacion) {
		this.cgdCotizacion = cgdCotizacion;
	}

	public CompBuscar<CotizacionTransporte> getCotizacion() {
		return cotizacion;
	}

	public void setCotizacion(CompBuscar<CotizacionTransporte> cotizacion) {
		this.cotizacion = cotizacion;
	}

	public Label getLblNombrePagador() {
		return LblNombrePagador;
	}

	public void setLblNombrePagador(Label lblNombrePagador) {
		LblNombrePagador = lblNombrePagador;
	}

	public Label getLblCedulaRifPagado() {
		return LblCedulaRifPagado;
	}

	public void setLblCedulaRifPagado(Label lblCedulaRifPagado) {
		LblCedulaRifPagado = lblCedulaRifPagado;
	}

	public Label getLblDireccionPagador() {
		return lblDireccionPagador;
	}

	public void setLblDireccionPagador(Label lblDireccionPagador) {
		this.lblDireccionPagador = lblDireccionPagador;
	}

	public Label getLblFechaHasta() {
		return lblFechaHasta;
	}

	public void setLblFechaHasta(Label lblFechaHasta) {
		this.lblFechaHasta = lblFechaHasta;
	}

	public Intbox getLblDiasVigencia() {
		return lblDiasVigencia;
	}

	public void setLblDiasVigencia(Intbox lblDiasVigencia) {
		this.lblDiasVigencia = lblDiasVigencia;
	}

	public CompGrupoDatos getCgdDireccionOrigen() {
		return cgdDireccionOrigen;
	}

	public void setCgdDireccionOrigen(CompGrupoDatos cgdDireccionOrigen) {
		this.cgdDireccionOrigen = cgdDireccionOrigen;
	}

	public Label getLblMunicipioOrigen() {
		return LblMunicipioOrigen;
	}

	public void setLblMunicipioOrigen(Label lblMunicipioOrigen) {
		LblMunicipioOrigen = lblMunicipioOrigen;
	}

	public Label getLblParroquiaOrigen() {
		return LblParroquiaOrigen;
	}

	public void setLblParroquiaOrigen(Label lblParroquiaOrigen) {
		LblParroquiaOrigen = lblParroquiaOrigen;
	}

	public Label getLblSectorOrigen() {
		return LblSectorOrigen;
	}

	public void setLblSectorOrigen(Label lblSectorOrigen) {
		LblSectorOrigen = lblSectorOrigen;
	}

	public Textbox getDireccionOrigen() {
		return direccionOrigen;
	}

	public void setDireccionOrigen(Textbox direccionOrigen) {
		this.direccionOrigen = direccionOrigen;
	}

	public Textbox getRefereciaOrigen() {
		return refereciaOrigen;
	}

	public void setRefereciaOrigen(Textbox refereciaOrigen) {
		this.refereciaOrigen = refereciaOrigen;
	}

	public CompGrupoDatos getCgdDireccionDestino() {
		return cgdDireccionDestino;
	}

	public void setCgdDireccionDestino(CompGrupoDatos cgdDireccionDestino) {
		this.cgdDireccionDestino = cgdDireccionDestino;
	}

	public Label getLblMunicipioDestino() {
		return LblMunicipioDestino;
	}

	public void setLblMunicipioDestino(Label lblMunicipioDestino) {
		LblMunicipioDestino = lblMunicipioDestino;
	}

	public Label getLblParroquiaDestino() {
		return LblParroquiaDestino;
	}

	public void setLblParroquiaDestino(Label lblParroquiaDestino) {
		LblParroquiaDestino = lblParroquiaDestino;
	}

	public Label getLblSectorDestino() {
		return LblSectorDestino;
	}

	public void setLblSectorDestino(Label lblSectorDestino) {
		LblSectorDestino = lblSectorDestino;
	}

	public Textbox getDireccionDestino() {
		return direccionDestino;
	}

	public void setDireccionDestino(Textbox direccionDestino) {
		this.direccionDestino = direccionDestino;
	}

	public Textbox getRefereciaDestino() {
		return refereciaDestino;
	}

	public void setRefereciaDestino(Textbox refereciaDestino) {
		this.refereciaDestino = refereciaDestino;
	}

	public CompLista<DetalleContrato> getCompLista() {
		return compLista;
	}

	public void setCompLista(CompLista<DetalleContrato> compLista) {
		this.compLista = compLista;
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
	
	public void refrescarCotizacion(List<CotizacionTransporte> cotizaciones){
		cotizacion.setModelo(cotizaciones);
		compLista.clear();
		compLista.setModelo(getModelo().getCotizacionTransporte().getDetallesContrato());
	}

	public Label getDtmFechaEmision() {
		return dtmFechaEmision;
	}

	public void setDtmFechaEmision(Label dtmFechaEmision) {
		this.dtmFechaEmision = dtmFechaEmision;
	}

	public Label getDtmFechaInicio() {
		return dtmFechaInicio;
	}

	public void setDtmFechaInicio(Label dtmFechaInicio) {
		this.dtmFechaInicio = dtmFechaInicio;
	}

	public Label getDtmFechaOrden() {
		return dtmFechaOrden;
	}

	public void setDtmFechaOrden(Label dtmFechaOrden) {
		this.dtmFechaOrden = dtmFechaOrden;
	}

	private ArrayList<Component> cargarListaCierre(List<IProducto> articuloVentas) {

		ArrayList<Component> lista = new ArrayList<Component>();
		cmbArticuloventa  = new CompBuscar<IProducto>(
				getEncabezadoArticulo2(), 3);

		cmbArticuloventa.setModelo(articuloVentas);
		Doublebox cantidad = new Doublebox();
		Doublebox precioUnidad = new Doublebox();
		Doublebox precio = new Doublebox();
	//	Doublebox cantidadReal = new Doublebox();
		cmbArticuloventa.setWidth("340px");
		cantidad.setWidth("60px");
		precioUnidad.setWidth("80px");
		precio.setWidth("90px");
		cmbArticuloventa.setAncho(650);
		// para blokear cantidad y servicio

		cantidad.setDisabled(false);
		precio.setDisabled(true);
		precioUnidad.setDisabled(true);
	//	cantidadReal.setDisabled(true);
		cantidad.addEventListener(Events.ON_CHANGE, getControlador());
		precioUnidad.addEventListener(Events.ON_CHANGE, getControlador());
		cmbArticuloventa.setListenerEncontrar(getControlador());
		cmbArticuloventa.setAttribute("nombre", "articulo");

		lista.add(cmbArticuloventa);
		lista.add(cantidad);
		lista.add(precioUnidad);
		lista.add(precio);
	//	lista.add(cantidadReal);

		return lista;
	}
	
	private List<CompEncabezado> encabezadosListaCierre() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Servicio/Articulo", 360);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getProducto");
		titulo.setMetodoModelo(".producto");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cantidad", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidad");
		titulo.setMetodoModelo(".cantidad");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Precio Und", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getPrecioUnidad");
		titulo.setMetodoModelo(".precioUnidad");
		// titulo.setOrdenable(true);a
		encabezado.add(titulo);
		titulo = new CompEncabezado("Precio", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getSubtotal");
		titulo.setMetodoModelo(".subtotal");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cantidad Real", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidadReal");
		titulo.setMetodoModelo(".cantidadReal");
		// titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
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

	public CompGrillaConBoton<DetalleContrato> getDetallecierre() {
		return detallecierre;
	}

	public void refrescarCierre(List<IProducto> servicos,List<DetalleContrato> modelo){
		getDetallecierre().clear();
		this.articuloVentas=servicos;
		
		getDetallecierre().setColeccion(modelo);;
		
		
	}
	
	public void setDetallecierre(CompGrillaConBoton<DetalleContrato> detallecierre) {
		this.detallecierre = detallecierre;
	}
	public void actualizarDetalleCierre(DetalleContrato modelo){
		getDetallecierre().setModelo(modelo);
		
	}

	public CompCombobox<UnidadFuncional> getUnidad() {
		return unidad;
	}

	public void setUnidad(CompCombobox<UnidadFuncional> unidad) {
		this.unidad = unidad;
	}

	public Datebox getFechaSalida() {
		return fechaSalida;
	}

	public void setFechaSalida(Datebox fechaSalida) {
		this.fechaSalida = fechaSalida;
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

	public Datebox getFechaRegreso() {
		return fechaRegreso;
	}

	public void setFechaRegreso(Datebox fechaRegreso) {
		this.fechaRegreso = fechaRegreso;
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
		fechaSalida.setDisabled(true);
		getCotizacion().setDisabled(true);
		
		desactivarDetalle();
		if (!getModelo().getEstado().getDescripcion().equals("TERMINADO"))
		cgdCierre.detach();
		else {
			getOrigen().setDisabled(true);
			getDestino().setDisabled(true);
			getDblKilometraje().setReadonly(true);
			getDetallecierre().setDisabled(true);
			getDetallecierre().getGrilla().setDisable(true);
			
		}
		lblDiasVigencia.setReadonly(true);
		
	}
	public void modoProcesar(){
		getCotizacion().setDisabled(true);
		getUnidad().setDisabled(true);
		fechaSalida.setDisabled(true);
		desactivarDetalle();
		getDetallecierre().getBoton().detach();	
		getDetalle().setDisabled(true);
		lblDiasVigencia.setReadonly(true);
	}
	
	public void setLabores(List<IProducto> articulos){
		cmbArticuloventa.setModelo(articulos);
		
	};
}
