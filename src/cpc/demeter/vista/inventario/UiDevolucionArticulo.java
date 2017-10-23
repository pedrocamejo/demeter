package cpc.demeter.vista.inventario;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.mantenimiento.DetalleDevolucionArticulo;
import cpc.modelo.demeter.mantenimiento.DetalleSalidaExternaArticulo;
import cpc.modelo.demeter.mantenimiento.DetalleSalidaInternaArticulo;
import cpc.modelo.demeter.mantenimiento.DevolucionArticulo;
import cpc.modelo.demeter.mantenimiento.MovimientoArticulo;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.negocio.demeter.mantenimiento.NegocioDevolucionArticulo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class UiDevolucionArticulo
		extends
		CompVentanaMaestroDetalle<DevolucionArticulo, DetalleDevolucionArticulo> {

	// private UnidadAdministrativa unidadAdministrativa;
	// private List<ConsumibleEquivalente> consumibleEquivalentes;
	private List<DetalleSalidaInternaArticulo> detalleSalidaInternaArticulos;
	private List<DetalleSalidaExternaArticulo> detalleSalidaExternaArticulos;
	private DevolucionArticulo devolucionArticulo;
	private List<Almacen> almacenesOrigen;
	private List<Almacen> almacenesDestino;
	private List<Trabajador> trabajadores;
	private List<ClienteAdministrativo> clientes;
	private List<MovimientoArticulo> movimientoArticulos;
	private int accion;
	private NegocioDevolucionArticulo servicio = NegocioDevolucionArticulo
			.getInstance();
	private CompGrupoDatos CmbDatosBasicos, Cmbencabezado;

	private CompBuscar<MovimientoArticulo> cmbMovimientoArticulo;

	private CompBuscar<Trabajador> cmbTrabajador;
	private CompBuscar<ClienteAdministrativo> cmbCliente;

	// private CompBuscar<ConsumibleEquivalente> cmbconsumibleEq;

	private CompBuscar<ArticuloVenta> cmbArticulo;
	private Textbox txtUsuario, txtFecha, txtNumeroControl;
	private CompLista<DetalleSalidaInternaArticulo> detalleSalidaInterna;
	private CompLista<DetalleSalidaExternaArticulo> detalleSalidaExterna;

	public UiDevolucionArticulo(String titulo, int ancho, int modo,
			DevolucionArticulo devolucionArticulo, List<Almacen> almacenorigen,
			List<Almacen> almacendestino, List<Trabajador> trabajadores,
			List<ClienteAdministrativo> clientes,
			List<MovimientoArticulo> movimientoArticulos) {
		super(titulo, ancho);
		// this.unidadAdministrativa = unidadAdministrativa;
		setModelo(devolucionArticulo);
		this.accion = modo;
		this.devolucionArticulo = devolucionArticulo;

		if (devolucionArticulo.getSalidaInternaArticulo() != null) {
			if (devolucionArticulo.getSalidaInternaArticulo()
					.getDetalleSalidaInternaArticulos() != null)
				this.detalleSalidaInternaArticulos = devolucionArticulo
						.getSalidaInternaArticulo()
						.getDetalleSalidaInternaArticulos();
		}

		if (devolucionArticulo.getSalidaExternaArticulo() != null) {
			if (devolucionArticulo.getSalidaExternaArticulo()
					.getDetalleSalidaExternaArticulos() != null)
				this.detalleSalidaExternaArticulos = devolucionArticulo
						.getSalidaExternaArticulo()
						.getDetalleSalidaExternaArticulos();
		}

		this.almacenesOrigen = almacenorigen;
		this.almacenesDestino = almacendestino;
		this.trabajadores = trabajadores;
		this.clientes = clientes;
		this.movimientoArticulos = movimientoArticulos;
		if (accion == Accion.EDITAR) {

		}

		// this.consumibleEquivalentes = consumibleEquivalente;

	}

	@Override
	public void inicializar() {

		Cmbencabezado = new CompGrupoDatos("Datos de Control", 4);
		CmbDatosBasicos = new CompGrupoDatos("Datos Basicos", 2);
		setGbDetalle("Detalle Devolucion");
		// setGbEncabezado(1, "");
		detalleSalidaExterna = new CompLista<DetalleSalidaExternaArticulo>(
				encabezadoSalidaExterna());
		detalleSalidaInterna = new CompLista<DetalleSalidaInternaArticulo>(
				encabezadoInternaArticulo());

		txtUsuario = new Textbox();
		txtFecha = new Textbox();
		txtNumeroControl = new Textbox();
		
		txtUsuario.setDisabled(true);;
		txtFecha.setDisabled(true);;
		txtNumeroControl.setDisabled(true);
		
		cmbCliente = new CompBuscar<ClienteAdministrativo>(
				getEncabezadoCliente(), 0);
		cmbCliente.setModelo(clientes);
		cmbCliente.setAncho(650);
		cmbMovimientoArticulo = new CompBuscar<MovimientoArticulo>(
				getEncabezadoMovimiento(), 0);
		cmbMovimientoArticulo.setModelo(movimientoArticulos);
		cmbMovimientoArticulo.setAncho(650);
		cmbTrabajador = new CompBuscar<Trabajador>(getEncabezadoTrabajador(), 2);
		cmbTrabajador.setModelo(trabajadores);
		cmbTrabajador.setAncho(650);
		cmbArticulo = new CompBuscar<ArticuloVenta>(getEncabezadoArticulo(), 3);
		// servicio =NegocioConsumible.getInstance();
		// setGbDetalle("Consumibles Equivalentes");

		if (accion == Accion.AGREGAR) {

		}

		if (accion == Accion.CONSULTAR) {

		}

		if (accion == Accion.ANULAR) {

		}

	}

	private List<CompEncabezado> getEncabezadoMovimiento() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("NroDocumento", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrNroDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Usuario", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getUsuario");
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> getEncabezadoTrabajador() {
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

	private List<CompEncabezado> getEncabezadoCliente() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Cedula", 120);
		titulo.setMetodoBinder("getCedulaCliente");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre", 450);
		titulo.setMetodoBinder("getNombreCliente");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> encabezadoInternaArticulo() {
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

		titulo = new CompEncabezado("Observacion", 180);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getObservacion");
		titulo.setMetodoModelo(".observacion");
		titulo.setOrdenable(true);
		encabezado.add(titulo);
		return encabezado;
	}

	private List<CompEncabezado> encabezadoSalidaExterna() {
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

		titulo = new CompEncabezado("Observacion", 180);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getObservacion");
		titulo.setMetodoModelo(".observacion");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	@Override
	public void dibujar() {

		Cmbencabezado.addComponente("Usuario", txtUsuario);
		Cmbencabezado.addComponente("fecha", txtFecha);
		Cmbencabezado.addComponente("N° Control", txtNumeroControl);
		CmbDatosBasicos.addComponente("Trabajador", cmbTrabajador);
		CmbDatosBasicos.addComponente("Cliente", cmbCliente);
		CmbDatosBasicos.addComponente("Moviento", cmbMovimientoArticulo);
		// setDetalles(cargarLista(), getModelo().getConsumibleEquivalente(),
		// encabezadosPrimario());
		setDetalles(cargarLista(),
				devolucionArticulo.getDetalleDevolucionArticulos(),
				encabezadosPrimario());
		getDetalle().setNuevo(new DetalleDevolucionArticulo());
		getGbDetalle().appendChild(detalleSalidaInterna);
		getGbDetalle().appendChild(detalleSalidaExterna);
		/*
		 * if (devolucionArticulo.getMovimientoArticuloOriginal()!=null){ if
		 * (devolucionArticulo.getMovimientoArticuloOriginal()
		 * .getDetalleSalidaInternaArticulos() != null)
		 * getGbDetalle().appendChild(detalleSalidaInterna); if
		 * (devolucionArticulo.getMovimientoArticuloOriginal()
		 * .getDetalleSalidaExternaArticulos() != null)
		 * getGbDetalle().appendChild(detalleSalidaInterna); }
		 */
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
		CmbDatosBasicos.dibujar(this);
		// forsando
		dibujarEstructura();
		cmbArticulo.setListenerEncontrar(getControlador());
		// cmbarticulo.addEventListener(Events.ON_SELECTION, getControlador());
		cmbArticulo.setListenerEncontrar(getControlador());
		cmbMovimientoArticulo.setListenerEncontrar(getControlador());
		getDetalleSalidaExterna().setVisible(false);
		getDetalleSalidaInterna().setVisible(false);
		addBotonera();

	}

	public void cargarValores(DevolucionArticulo devolucionArticulo)
			throws ExcDatosInvalidos {

		if (accion != Accion.AGREGAR) {
			txtUsuario.setValue(devolucionArticulo.getUsuario());
			txtFecha.setValue(devolucionArticulo.getFecha().toString());
			txtNumeroControl.setValue(devolucionArticulo.getStrNroDocumento());

			if (devolucionArticulo.getSalidaExternaArticulo() != null) {

				// detalleSalidaExterna.setModelo(devolucionArticulo.getSalidaExternaArticulo().getDetalleSalidaExternaArticulos());
				cmbMovimientoArticulo.setSeleccion(devolucionArticulo
						.getSalidaExternaArticulo());
				cmbMovimientoArticulo.setText(devolucionArticulo
						.getSalidaExternaArticulo().getStrNroDocumento());

			}
			;
			if (devolucionArticulo.getSalidaInternaArticulo() != null) {
				// detalleSalidaInterna.setModelo(devolucionArticulo.getSalidaInternaArticulo().getDetalleSalidaInternaArticulos());
				cmbMovimientoArticulo.setSeleccion(devolucionArticulo
						.getSalidaInternaArticulo());
				cmbMovimientoArticulo.setText(devolucionArticulo
						.getSalidaInternaArticulo().getStrNroDocumento());
				System.out.println(cmbMovimientoArticulo.getSeleccion()
						.toString());

			}
			;

			if (devolucionArticulo.getTrabajador() == null)
				cmbTrabajador.setSeleccion(devolucionArticulo.getTrabajador());
			cmbTrabajador.setText(devolucionArticulo.getTrabajador()
					.getNombreCompleto());
			if (devolucionArticulo.getDestinatario() == null)
				cmbCliente.setSeleccion(devolucionArticulo.getDestinatario());
			cmbCliente.setText(devolucionArticulo.getDestinatario()
					.getNombreCliente());

		}

		if (accion == Accion.AGREGAR) {
			System.out.println("s");
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

	private ArrayList<Component> cargarLista() {
		ArrayList<Component> lista = new ArrayList<Component>();
		cmbArticulo = new CompBuscar<ArticuloVenta>(getEncabezadoArticulo(), 3);

		Doublebox cantidad = new Doublebox();
		CompBuscar<Almacen> almacenOrigen = new CompBuscar<Almacen>(
				getEncabezadoAlmacen(), 0);
		CompBuscar<Almacen> almacenDestino = new CompBuscar<Almacen>(
				getEncabezadoAlmacen(), 0);
		Textbox observacion = new Textbox();
		// Label codccnu = new Label();

		cmbArticulo.setWidth("180px");

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
		cmbArticulo.setAncho(600);
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
			cmbArticulo.setModelo(servicio.getArticulosVentas());
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		almacenOrigen.setModelo(almacenesOrigen);
		almacenDestino.setModelo(almacenesDestino);

		cmbArticulo.setListenerEncontrar(getControlador());
		almacenDestino.setListenerEncontrar(getControlador());
		almacenOrigen.setListenerEncontrar(getControlador());

		lista.add(cmbArticulo);
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
		// getDetalle().setVisible(false);

	}

	public void activarConsulta() {

		// getDetalle().setVisible(false);
		// getCmbarticulo().setReadonly(true);
		// getCmbarticulo().setButtonVisible(false);
		getCmbCliente().setDisabled(true);;
		getCmbMovimientoArticulo().setDisabled(true);;
		getCmbTrabajador().setDisabled(true);
		 desactivarDetalle();
	}

	public void activarEditar() {

		// getDetalle().setVisible(false);
		// getCmbarticulo().setDisabled(true);
		// desactivarfilas();

	}

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

	public List<DetalleSalidaInternaArticulo> getDetalleSalidaInternaArticulos() {
		return detalleSalidaInternaArticulos;
	}

	public void setDetalleSalidaInternaArticulos(
			List<DetalleSalidaInternaArticulo> detalleSalidaInternaArticulos) {
		this.detalleSalidaInternaArticulos = detalleSalidaInternaArticulos;
	}

	public List<DetalleSalidaExternaArticulo> getDetalleSalidaExternaArticulos() {
		return detalleSalidaExternaArticulos;
	}

	public void setDetalleSalidaExternaArticulos(
			List<DetalleSalidaExternaArticulo> detalleSalidaExternaArticulos) {
		this.detalleSalidaExternaArticulos = detalleSalidaExternaArticulos;
	}

	public DevolucionArticulo getDevolucionArticulo() {
		return devolucionArticulo;
	}

	public void setDevolucionArticulo(DevolucionArticulo devolucionArticulo) {
		this.devolucionArticulo = devolucionArticulo;
	}

	public List<Almacen> getAlmacenesOrigen() {
		return almacenesOrigen;
	}

	public void setAlmacenesOrigen(List<Almacen> almacenesOrigen) {
		this.almacenesOrigen = almacenesOrigen;
	}

	public List<Almacen> getAlmacenesDestino() {
		return almacenesDestino;
	}

	public void setAlmacenesDestino(List<Almacen> almacenesDestino) {
		this.almacenesDestino = almacenesDestino;
	}

	public List<Trabajador> getTrabajadores() {
		return trabajadores;
	}

	public void setTrabajadores(List<Trabajador> trabajadores) {
		this.trabajadores = trabajadores;
	}

	public List<ClienteAdministrativo> getClientes() {
		return clientes;
	}

	public void setClientes(List<ClienteAdministrativo> clientes) {
		this.clientes = clientes;
	}

	public List<MovimientoArticulo> getMovimientoArticulos() {
		return movimientoArticulos;
	}

	public void setMovimientoArticulos(
			List<MovimientoArticulo> movimientoArticulos) {
		this.movimientoArticulos = movimientoArticulos;
	}

	public NegocioDevolucionArticulo getServicio() {
		return servicio;
	}

	public void setServicio(NegocioDevolucionArticulo servicio) {
		this.servicio = servicio;
	}

	public CompGrupoDatos getCmbDatosBasicos() {
		return CmbDatosBasicos;
	}

	public void setCmbDatosBasicos(CompGrupoDatos cmbDatosBasicos) {
		CmbDatosBasicos = cmbDatosBasicos;
	}

	public CompGrupoDatos getCmbencabezado() {
		return Cmbencabezado;
	}

	public void setCmbencabezado(CompGrupoDatos cmbencabezado) {
		Cmbencabezado = cmbencabezado;
	}

	public CompBuscar<MovimientoArticulo> getCmbMovimientoArticulo() {
		return cmbMovimientoArticulo;
	}

	public void setCmbMovimientoArticulo(
			CompBuscar<MovimientoArticulo> cmbMovimientoArticulo) {
		this.cmbMovimientoArticulo = cmbMovimientoArticulo;
	}

	public CompBuscar<Trabajador> getCmbTrabajador() {
		return cmbTrabajador;
	}

	public void setCmbTrabajador(CompBuscar<Trabajador> cmbTrabajador) {
		this.cmbTrabajador = cmbTrabajador;
	}

	public CompBuscar<ClienteAdministrativo> getCmbCliente() {
		return cmbCliente;
	}

	public void setCmbCliente(CompBuscar<ClienteAdministrativo> cmbCliente) {
		this.cmbCliente = cmbCliente;
	}

	public CompBuscar<ArticuloVenta> getCmbArticulo() {
		return cmbArticulo;
	}

	public void setCmbArticulo(CompBuscar<ArticuloVenta> cmbArticulo) {
		this.cmbArticulo = cmbArticulo;
	}

	public Textbox getTxtUsuario() {
		return txtUsuario;
	}

	public void setTxtUsuario(Textbox txtUsuario) {
		this.txtUsuario = txtUsuario;
	}

	public Textbox getTxtFecha() {
		return txtFecha;
	}

	public void setTxtFecha(Textbox txtFecha) {
		this.txtFecha = txtFecha;
	}

	public Textbox getTxtNumeroControl() {
		return txtNumeroControl;
	}

	public void setTxtNumeroControl(Textbox txtNumeroControl) {
		this.txtNumeroControl = txtNumeroControl;
	}

	public CompLista<DetalleSalidaInternaArticulo> getDetalleSalidaInterna() {
		return detalleSalidaInterna;
	}

	public void setDetalleSalidaInterna(
			CompLista<DetalleSalidaInternaArticulo> detalleSalidaInterna) {
		this.detalleSalidaInterna = detalleSalidaInterna;
	}

	public CompLista<DetalleSalidaExternaArticulo> getDetalleSalidaExterna() {
		return detalleSalidaExterna;
	}

	public void setDetalleSalidaExterna(
			CompLista<DetalleSalidaExternaArticulo> detalleSalidaExterna) {
		this.detalleSalidaExterna = detalleSalidaExterna;
	}

}
