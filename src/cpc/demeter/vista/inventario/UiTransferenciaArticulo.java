package cpc.demeter.vista.inventario;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.DetalleDocumentoFiscal;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.basico.Servicio;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.gestion.TrabajoRealizadoMecanizado;
import cpc.modelo.demeter.mantenimiento.Consumible;
import cpc.modelo.demeter.mantenimiento.ConsumibleEquivalente;
import cpc.modelo.demeter.mantenimiento.DetalleEntradaArticulo;
import cpc.modelo.demeter.mantenimiento.DetalleTransferenciaArticulo;
import cpc.modelo.demeter.mantenimiento.EntradaArticulo;
import cpc.modelo.demeter.mantenimiento.Herramienta;
import cpc.modelo.demeter.mantenimiento.Repuesto;
import cpc.modelo.demeter.mantenimiento.TransferenciaArticulo;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.negocio.demeter.mantenimiento.NegocioConsumible;
import cpc.negocio.demeter.mantenimiento.NegocioEntradaArticulo;
import cpc.negocio.demeter.mantenimiento.NegocioTransferenciaArticulo;
import cpc.persistencia.demeter.implementacion.basico.PerArticuloVenta;
import cpc.persistencia.demeter.implementacion.mantenimiento.PerConsumible;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.ui.ComponentesAutomaticos;
import cpc.zk.componente.ventanas.CompGrupoBusqueda;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class UiTransferenciaArticulo
		extends
		CompVentanaMaestroDetalle<TransferenciaArticulo, DetalleTransferenciaArticulo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7972502841695438874L;
	private List<DetalleTransferenciaArticulo> detalleTransferenciaArticulos;
	private List<Almacen> almacenesOrigen;
	private List<Almacen> almacenesDestino;
	private List<Trabajador> trabajadores;
	private TransferenciaArticulo transferenciaArticulo;
	private int accion;
	private NegocioTransferenciaArticulo servicio = NegocioTransferenciaArticulo
			.getInstance();
	private CompGrupoDatos Cmbencabezado, cmbdetalles;
	private Textbox txtUsuario, txtFecha, txtNumeroControl;
	private CompBuscar<ArticuloVenta> cmbArticuloventa;
	private CompBuscar<Trabajador> cmbTrabajador;

	private CompGrupoBusqueda<Consumible> auxConsumible;
	private CompGrupoBusqueda<Herramienta> auxHerramienta;
	private CompGrupoBusqueda<Repuesto> auxRepuesto;

	public UiTransferenciaArticulo(String titulo, int ancho, int modo,
			TransferenciaArticulo transferenciaArticulo,
			List<DetalleTransferenciaArticulo> transferenciaArticulos,
			List<Almacen> almacenesOrigen, List<Almacen> almacenesDestino,
			List<Trabajador> trabajadores) {
		super(titulo, ancho);
		// this.unidadAdministrativa = unidadAdministrativa;
		setModelo(transferenciaArticulo);
		this.accion = modo;
		this.almacenesDestino = almacenesDestino;
		this.almacenesOrigen = almacenesOrigen;
		this.transferenciaArticulo = transferenciaArticulo;
		this.trabajadores = trabajadores;

		// this.consumibleEquivalentes = consumibleEquivalente;

		this.detalleTransferenciaArticulos = transferenciaArticulos;

	}

	@Override
	public void inicializar() {

		Cmbencabezado = new CompGrupoDatos("Datos de Control", 4);
		cmbdetalles = new CompGrupoDatos("Datos de la Transferencia", 4);

		// setGbEncabezado(1, "");

		txtFecha = new Textbox();
		txtNumeroControl = new Textbox();
		txtUsuario = new Textbox();
		cmbArticuloventa = new CompBuscar<ArticuloVenta>(
				getEncabezadoArticulo(), 3);
		cmbTrabajador = new CompBuscar<Trabajador>(getEncabezadoTrabajador(), 1);
		cmbTrabajador.setAncho(650);
		// servicio =NegocioConsumible.getInstance();
		// setGbDetalle("Consumibles Equivalentes");

		try {
			auxConsumible = new CompGrupoBusqueda<Consumible>("Sector",
					getEncabezadoArticulo(), null,
					ComponentesAutomaticos.TEXTBOX);
			auxConsumible.setNuevo(new Consumible());
			auxConsumible.setModeloCatalogo(null);
			auxConsumible.setAnchoValores(120);
			auxConsumible.setAnchoCatalogo(800);
			auxConsumible.addListener(getControlador());
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			auxHerramienta = new CompGrupoBusqueda<Herramienta>("Sector",
					getEncabezadoArticulo(), null,
					ComponentesAutomaticos.TEXTBOX);
			auxHerramienta.setNuevo(new Herramienta());
			auxHerramienta.setModeloCatalogo(null);
			auxHerramienta.setAnchoValores(120);
			auxHerramienta.setAnchoCatalogo(800);
			auxHerramienta.addListener(getControlador());
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			auxRepuesto = new CompGrupoBusqueda<Repuesto>("Sector",
					getEncabezadoArticulo(), null,
					ComponentesAutomaticos.TEXTBOX);
			auxRepuesto.setNuevo(new Repuesto());
			auxRepuesto.setModeloCatalogo(null);
			auxRepuesto.setAnchoValores(120);
			auxRepuesto.setAnchoCatalogo(800);
			auxRepuesto.addListener(getControlador());
		} catch (ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (accion == Accion.AGREGAR) {

		}

		if (accion == Accion.CONSULTAR) {

		}

		if (accion == Accion.ANULAR) {

		}
		txtFecha.setReadonly(true);
		txtUsuario.setReadonly(true);
		txtNumeroControl.setReadonly(true);

	}

	@Override
	public void dibujar() {

		Cmbencabezado.addComponente("Usuario", txtUsuario);
		Cmbencabezado.addComponente("fecha", txtFecha);
		Cmbencabezado.addComponente("N° Control", txtNumeroControl);

		cmbTrabajador.setWidth("350px");
		cmbTrabajador.setAncho(600);
		cmbTrabajador.setModelo(trabajadores);
		cmbdetalles.addComponente("Responsable", cmbTrabajador);

		// Cmbgrupoconsumible.addComponente(" ",null);

		// setDetalles(cargarLista(), getModelo().getConsumibleEquivalente(),
		// encabezadosPrimario());
		List<DetalleTransferenciaArticulo> a = getModelo()
				.getDetalleTransferenciaArticulos();
		setDetalles(cargarLista(), a, encabezadosPrimariodos());
		getDetalle().setNuevo(new DetalleTransferenciaArticulo());

		if (accion != Accion.EDITAR) {

		}

		// actualizarModelo();
		// setDetalles(cargarLista(), getModelo().getEntradas(),
		// encabezadosPrimario());}
		// getDetalle().setNuevo(new EntradaActivo());
		// getDetalle().setListenerBorrar(getControlador());
		// dibujarEstructura();
		// setAltoDetalle(200);
		Cmbencabezado.dibujar(this);
		cmbdetalles.dibujar(this);
		// getDetalle().dibujar();
		dibujarEstructura();
		cmbArticuloventa.setListenerEncontrar(getControlador());
		// cmbarticulo.addEventListener(Events.ON_SELECTION, getControlador());
		addBotonera();

	}

	public void cargarValores(TransferenciaArticulo transferenciaArticulo)
			throws ExcDatosInvalidos {

		if (accion != Accion.AGREGAR) {
			txtUsuario.setValue(transferenciaArticulo.getUsuario());
			// getBinder().addBinding (txtCodCCNU, "value",
			// getNombreModelo()+".descripcion", null, null, "save", null, null,
			// null, null);
			txtFecha.setValue(transferenciaArticulo.getFecha().toString());

			txtNumeroControl.setValue(transferenciaArticulo
					.getStrNroDocumento());

			cmbTrabajador.setSeleccion(getModelo().getTrabajadorSolicitante());
			getBinder().addBinding(cmbTrabajador, "seleccion",
					getNombreModelo() + ".trabajadorSolicitante", null, null,
					"save", null, null, null, null);
			cmbTrabajador.setText(getModelo().getTrabajadorSolicitante().getNombreCompleto());
		}

		if (accion == Accion.AGREGAR) {

		}

		if (accion == Accion.CONSULTAR) {

		}

		if (accion == Accion.ANULAR) {

		}
	}

	private List<CompEncabezado> getEncabezadoArticulo() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("CodigoSIGESP", 150);
		titulo.setMetodoBinder("getCodigoSIGESP");
		titulo.setOrdenable(true);
		lista.add(titulo);

		titulo = new CompEncabezado("CodigoFabricante", 150);
		titulo.setMetodoBinder("getCodigoFabricante");
		titulo.setOrdenable(true);
		lista.add(titulo);

		titulo = new CompEncabezado("DenominacionFabricante()", 150);
		titulo.setMetodoBinder("getDenominacionFabricante");
		titulo.setOrdenable(true);
		lista.add(titulo);

		titulo = new CompEncabezado("CodigoCCNU", 150);
		titulo.setMetodoBinder("getCodigoCCNU");
		titulo.setOrdenable(true);
		lista.add(titulo);

		return lista;
	}

	private List<CompEncabezado> encabezadosPrimario() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Consumible Eq ", 240);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getConsumibleEq");
		titulo.setMetodoModelo(".ConsumibleEquivalente");

		encabezado.add(titulo);

		titulo = new CompEncabezado("DenominacionFabricanteEq", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getDenominacionFabricanteEq");
		titulo.setOrdenable(true);
		// titulo.setMetodoModelo(".consumibleEquivalente");
		encabezado.add(titulo);

		titulo = new CompEncabezado("CodigoSIGESP", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCodigoSIGESPEq");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("CodigoFabricante", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCodigoFabricanteEq");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("CodigoCCNU", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCodigoCCNUEq");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> encabezadosPrimariodos() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Articulo ", 180);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getArticuloVenta");
		titulo.setMetodoModelo(".articuloVenta");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cantidad", 180);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCantidad");
		titulo.setMetodoModelo(".cantidad");
		titulo.setOrdenable(true);
		// titulo.setMetodoModelo(".consumibleEquivalente");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Almacen O", 180);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getAlmacenOrigen");
		titulo.setMetodoModelo(".almacenOrigen");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Almacen D", 180);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getAlmacenDestino");
		titulo.setMetodoModelo(".almacenDestino");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Observacion", 180);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getObservacion");
		titulo.setMetodoModelo(".observacion");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> getEncabezadoconsumibleEq() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		/*
		 * titulo = new CompEncabezado("CodigoSIGESP", 150);
		 * titulo.setMetodoBinder("getCodigoSIGESPEq");
		 * titulo.setOrdenable(true); lista.add(titulo);
		 * 
		 * titulo = new CompEncabezado("CodigoFabricante", 150);
		 * titulo.setMetodoBinder("getCodigoFabricanteEq");
		 * titulo.setOrdenable(true); lista.add(titulo);
		 * 
		 * titulo = new CompEncabezado("DenominacionFabricante", 150);
		 * titulo.setMetodoBinder("getDenominacionFabricanteEq");
		 * titulo.setOrdenable(true); lista.add(titulo);
		 * 
		 * titulo = new CompEncabezado("CodigoCCNU", 150);
		 * titulo.setMetodoBinder("getCodigoCCNUEq"); titulo.setOrdenable(true);
		 * lista.add(titulo);
		 */
		titulo = new CompEncabezado("CodigoSIGESP", 150);
		titulo.setMetodoBinder("getCodigoSIGESP");
		titulo.setOrdenable(true);
		lista.add(titulo);

		titulo = new CompEncabezado("CodigoFabricante", 150);
		titulo.setMetodoBinder("getCodigoFabricante");
		titulo.setOrdenable(true);
		lista.add(titulo);

		titulo = new CompEncabezado("DenominacionFabricante", 150);
		titulo.setMetodoBinder("getDenominacionFabricante");
		titulo.setOrdenable(true);
		lista.add(titulo);

		titulo = new CompEncabezado("CodigoCCNU", 150);
		titulo.setMetodoBinder("getCodigoCCNU");
		titulo.setOrdenable(true);
		lista.add(titulo);

		return lista;
	}

	private List<CompEncabezado> getEncabezadoarticulo() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		/*
		 * titulo = new CompEncabezado("CodigoSIGESP", 150);
		 * titulo.setMetodoBinder("getCodigoSIGESPEq");
		 * titulo.setOrdenable(true); lista.add(titulo);
		 * 
		 * titulo = new CompEncabezado("CodigoFabricante", 150);
		 * titulo.setMetodoBinder("getCodigoFabricanteEq");
		 * titulo.setOrdenable(true); lista.add(titulo);
		 * 
		 * titulo = new CompEncabezado("DenominacionFabricante", 150);
		 * titulo.setMetodoBinder("getDenominacionFabricanteEq");
		 * titulo.setOrdenable(true); lista.add(titulo);
		 * 
		 * titulo = new CompEncabezado("CodigoCCNU", 150);
		 * titulo.setMetodoBinder("getCodigoCCNUEq"); titulo.setOrdenable(true);
		 * lista.add(titulo);
		 */
		titulo = new CompEncabezado("CodigoSIGESP", 150);
		titulo.setMetodoBinder("getCodigoSIGESP");
		titulo.setOrdenable(true);
		lista.add(titulo);

		titulo = new CompEncabezado("CodigoFabricante", 150);
		titulo.setMetodoBinder("getCodigoFabricante");
		titulo.setOrdenable(true);
		lista.add(titulo);

		titulo = new CompEncabezado("DenominacionFabricante", 150);
		titulo.setMetodoBinder("getDenominacionFabricante");
		titulo.setOrdenable(true);
		lista.add(titulo);

		titulo = new CompEncabezado("CodigoCCNU", 150);
		titulo.setMetodoBinder("getCodigoCCNU");
		titulo.setOrdenable(true);
		lista.add(titulo);

		return lista;
	}

	private ArrayList<Component> cargarLista() {
		ArrayList<Component> lista = new ArrayList<Component>();
		cmbArticuloventa = new CompBuscar<ArticuloVenta>(
				getEncabezadoArticulo(), 3);

		Doublebox cantidad = new Doublebox();
		CompBuscar<Almacen> almacenOrigen = new CompBuscar<Almacen>(
				getEncabezadoAlmacen(), 0);
		CompBuscar<Almacen> almacenDestino = new CompBuscar<Almacen>(
				getEncabezadoAlmacen(), 0);
		Textbox observacion = new Textbox();
		// Label codccnu = new Label();

		cmbArticuloventa.setWidth("180px");

		almacenOrigen.setWidth("180px");
		almacenDestino.setWidth("180px");
		cantidad.setWidth("180px");
		observacion.setWidth("180px");
		// codccnu.setWidth("150px");
		// observaciones.setRows(2);

		/*
		 * denomiacionFabricanteeq.setMaxlength(250); codccnu.setMaxlength(250);
		 * codSigesp.setMaxlength(250); codFabricante.setMaxlength(250);
		 */
		cmbArticuloventa.setAncho(600);
		almacenDestino.setAncho(600);
		almacenOrigen.setAncho(600);

		// codigoActivo.setModelo(activos);
		// almacen.setModelo(almacenes);
		/*
		 * List<ConsumibleEquivalente> z = consumibleEquivalentes; Consumible []
		 * consumibles = new Consumible[z.size()]; int a = 0; for
		 * (ConsumibleEquivalente consumibleEquivalente : z) { consumibles[a]=
		 * (consumibleEquivalente.getConsumibleEquivalente()); a++; }
		 */

		// consumibleEq.setAttribute("Consumible", "consumible");

		// estadoActivo.setModelo(estadosActivo);

		try {
			cmbArticuloventa.setModelo(servicio.getArticulosVentaExistentes());
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		almacenOrigen.setModelo(almacenesOrigen);
		almacenDestino.setModelo(almacenesDestino);

		cmbArticuloventa.setListenerEncontrar(getControlador());
		almacenDestino.setListenerEncontrar(getControlador());
		almacenOrigen.setListenerEncontrar(getControlador());

		lista.add(cmbArticuloventa);
		lista.add(cantidad);
		// lista.add(codccnu);
		lista.add(almacenOrigen);
		lista.add(almacenDestino);
		lista.add(observacion);
		/*
		 * for (Component component : lista) { component.getChildren(); if
		 * (component.getChildren().get(0) ==null){ lista.remove(component); } }
		 */

		return lista;
	}

	public void activarAgregar() {

	}

	public void activarConsulta() {

		// getDetalle().setVisible(false);
		getCmbTrabajador().setDisabled(true);
		desactivarDetalle();
	}

	public void activarEditar() {

		// getDetalle().setVisible(false);

		// desactivarfilas();

	}

	/*
	 * @SuppressWarnings("unchecked") public void desactivarfilas(){ List<Row>
	 * fila = getDetalle().getFilas().getChildren(); for (Row row : fila) {
	 * 
	 * ((CompBuscar<Consumible>) row.getChildren().get(0)).setDisabled(true);
	 * 
	 * } setConsumiblesEq(); }
	 * 
	 * 
	 * public void setConsumiblesEq(){
	 * 
	 * List<Consumible> a = cmbconsumibleEq.getCatalogo().getModelo(); try {
	 * a.addAll(servicio.getTodos()); } catch (ExcFiltroExcepcion e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); }
	 * 
	 * cmbconsumibleEq.setModelo(a); }
	 */

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			activarConsulta();
		if (modoOperacion == Accion.AGREGAR)
			activarAgregar();
		if (modoOperacion == Accion.EDITAR)
			activarEditar();
	}

	public int getAccion() {
		return accion;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	private List<CompEncabezado> getEncabezadoAlmacen() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Nombre. ", 100);
		titulo.setMetodoBinder("getNombre");
		lista.add(titulo);

		titulo = new CompEncabezado("Descripción. ", 100);
		titulo.setMetodoBinder("getDescripcion");
		lista.add(titulo);

		titulo = new CompEncabezado("Localidad. ", 250);
		titulo.setMetodoBinder("getLocalidad");
		lista.add(titulo);

		return lista;
	}

	public Textbox getTxtUsuario() {
		return txtUsuario;
	}

	public void setTxtUsuario(Textbox txtUsuario) {
		this.txtUsuario = txtUsuario;
	}

	public Textbox getTxtNumeroControl() {
		return txtNumeroControl;
	}

	public void setTxtNumeroControl(Textbox txtNumeroControl) {
		this.txtNumeroControl = txtNumeroControl;
	}

	public List<CompEncabezado> getEncabezadoTrabajador() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Cedula", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroCedula");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombres", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Apellidos", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getApellido");
		encabezado.add(titulo);

		return encabezado;
	}

	public CompBuscar<Trabajador> getCmbTrabajador() {
		return cmbTrabajador;
	}

	public void setCmbTrabajador(CompBuscar<Trabajador> cmbTrabajador) {
		this.cmbTrabajador = cmbTrabajador;
	}

	public CompGrupoBusqueda<Consumible> getAuxConsumible() {
		return auxConsumible;
	}

	public void setAuxConsumible(CompGrupoBusqueda<Consumible> auxConsumible) {
		this.auxConsumible = auxConsumible;
	}

	public CompGrupoBusqueda<Herramienta> getAuxHerramienta() {
		return auxHerramienta;
	}

	public void setAuxHerramienta(CompGrupoBusqueda<Herramienta> auxHerramienta) {
		this.auxHerramienta = auxHerramienta;
	}

	public CompGrupoBusqueda<Repuesto> getAuxRepuesto() {
		return auxRepuesto;
	}

	public void setAuxRepuesto(CompGrupoBusqueda<Repuesto> auxRepuesto) {
		this.auxRepuesto = auxRepuesto;
	}

}