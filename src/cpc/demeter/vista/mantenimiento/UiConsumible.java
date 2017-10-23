package cpc.demeter.vista.mantenimiento;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.DetalleDocumentoFiscal;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.basico.Servicio;
import cpc.modelo.demeter.gestion.TrabajoRealizadoMecanizado;
import cpc.modelo.demeter.mantenimiento.Consumible;
import cpc.modelo.demeter.mantenimiento.ConsumibleEquivalente;
import cpc.negocio.demeter.mantenimiento.NegocioConsumible;
import cpc.persistencia.demeter.implementacion.basico.PerArticuloVenta;
import cpc.persistencia.demeter.implementacion.mantenimiento.PerConsumible;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class UiConsumible extends
		CompVentanaMaestroDetalle<Consumible, Consumible> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7972502841695438874L;
	// private UnidadAdministrativa unidadAdministrativa;
	// private List<ConsumibleEquivalente> consumibleEquivalentes;
	private List<Consumible> consumibleEquivalentes;
	private Consumible consumible;
	private int accion;
	private NegocioConsumible servicio = NegocioConsumible.getInstance();
	private CompGrupoDatos Cmbgrupoconsumible;

	private CompBuscar<ArticuloVenta> cmbarticulo;
	// private CompBuscar<ConsumibleEquivalente> cmbconsumibleEq;
	private CompBuscar<Consumible> cmbconsumibleEq;
	private CompBuscar<ArticuloVenta> cmbArticuloEq;
	private Textbox txtCodCCNU;
	private Textbox txtCodSigesp;
	private Textbox txtCodFabricante;
	private Textbox txtDenFabricante;
	private CompLista<ConsumibleEquivalente> detalleconsumible;

	public UiConsumible(String titulo, int ancho, int modo,
			Consumible consumible,
			List<ConsumibleEquivalente> consumibleEquivalente) {
		super(titulo, ancho);
		// this.unidadAdministrativa = unidadAdministrativa;
		setModelo(consumible);
		this.accion = modo;
		this.consumible = consumible;

		List<Consumible> consumibless = new ArrayList<Consumible>();
		if (consumibleEquivalente != null) {
			for (ConsumibleEquivalente consumibleEquivalente2 : consumibleEquivalente) {
				Consumible x = consumibleEquivalente2.getConsumibleOri();
				Consumible z = consumibleEquivalente2.getConsumibleEq();
				consumibless.add(z);
			}
		}

		if (accion == Accion.EDITAR) {
			try {
				consumibless.addAll(servicio.getTodos());
			} catch (ExcFiltroExcepcion e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// this.consumibleEquivalentes = consumibleEquivalente;

		this.consumibleEquivalentes = consumibless;

	}

	@Override
	public void inicializar() {

		Cmbgrupoconsumible = new CompGrupoDatos("Datos del Consumible", 4);
		// setGbEncabezado(1, "");
		detalleconsumible = new CompLista<ConsumibleEquivalente>(
				encabezadosPrimario());
		txtCodCCNU = new Textbox();
		txtCodSigesp = new Textbox();
		txtCodFabricante = new Textbox();
		txtDenFabricante = new Textbox();
		cmbarticulo = new CompBuscar<ArticuloVenta>(getEncabezadoArticulo(), 3);
		cmbarticulo.setAncho(600);
		// servicio =NegocioConsumible.getInstance();
		// setGbDetalle("Consumibles Equivalentes");

		if (accion == Accion.AGREGAR) {

		}

		if (accion == Accion.CONSULTAR) {

		}

		if (accion == Accion.ANULAR) {

		}
		txtCodCCNU.setReadonly(true);
		txtCodFabricante.setReadonly(true);
		txtCodSigesp.setReadonly(true);
		txtDenFabricante.setReadonly(true);

	}

	@Override
	public void dibujar() {

		Cmbgrupoconsumible.addComponente("Articulo", cmbarticulo);
		// Cmbgrupoconsumible.addComponente(" ",null);
		Cmbgrupoconsumible.addComponente("DenFabricante", txtDenFabricante);
		Cmbgrupoconsumible.addComponente("CodFabricante", txtCodFabricante);
		Cmbgrupoconsumible.addComponente("CodSigesp", txtCodSigesp);
		Cmbgrupoconsumible.addComponente("CodCCNU", txtCodCCNU);

		// setDetalles(cargarLista(), getModelo().getConsumibleEquivalente(),
		// encabezadosPrimario());
		setDetalles(cargarListados(), null, encabezadosPrimariodos());
		getDetalle().setNuevo(new Consumible());
		getGbDetalle().appendChild(detalleconsumible);
		if (accion != Accion.EDITAR) {

		}

		// actualizarModelo();
		// setDetalles(cargarLista(), getModelo().getEntradas(),
		// encabezadosPrimario());}
		// getDetalle().setNuevo(new EntradaActivo());
		// getDetalle().setListenerBorrar(getControlador());
		// dibujarEstructura();
		// setAltoDetalle(200);
		Cmbgrupoconsumible.dibujar(this);
		dibujarEstructura();
		cmbarticulo.setListenerEncontrar(getControlador());
		// cmbarticulo.addEventListener(Events.ON_SELECTION, getControlador());
		addBotonera();

	}

	public void cargarValores(Consumible consumible) throws ExcDatosInvalidos {

		if (accion != Accion.AGREGAR) {
			txtCodCCNU.setValue(consumible.getCodigoCCNU());
			// getBinder().addBinding (txtCodCCNU, "value",
			// getNombreModelo()+".descripcion", null, null, "save", null, null,
			// null, null);
			txtCodFabricante.setValue(consumible.getCodigoFabricante());

			txtDenFabricante.setValue(consumible.getDenominacionFabricante());

			txtCodSigesp.setValue(consumible.getCodigoSIGESP());

			cmbarticulo.setSeleccion(consumible.getArticuloVenta());
			getBinder().addBinding(cmbarticulo, "seleccion",
					getNombreModelo() + ".articuloVenta", null, null, "save",
					null, null, null, null);
			cmbarticulo.setText(consumible.getCodigoFabricante());

			if (getModelo().getConsumibleEquivalente() != null)
				detalleconsumible.setModelo(getModelo()
						.getConsumibleEquivalente());
		}

		if (accion == Accion.AGREGAR) {
			try {
				cmbarticulo.setModelo(servicio.getArticulosSinConsumible());
			} catch (ExcFiltroExcepcion e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		titulo = new CompEncabezado("Consumible Eq ", 120);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getConsumibleEq");
		titulo.setMetodoModelo(".ConsumibleEquivalente");

		encabezado.add(titulo);

		titulo = new CompEncabezado("DenominacionFabricanteEq", 180);
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
		titulo = new CompEncabezado("Consumible Eq ", 120);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getArticuloVenta");
		titulo.setMetodoModelo(".articuloVenta");

		encabezado.add(titulo);

		titulo = new CompEncabezado("DenominacionFabricante", 180);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getDenominacionFabricante");

		titulo.setOrdenable(true);
		// titulo.setMetodoModelo(".consumibleEquivalente");
		encabezado.add(titulo);

		titulo = new CompEncabezado("CodigoSIGESP", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCodigoSIGESP");

		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("CodigoFabricante", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCodigoFabricante");

		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("CodigoCCNU", 60);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCodigoCCNU");

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

	private List<CompEncabezado> getEncabezadoarticuloEq() {
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
		cmbconsumibleEq = new CompBuscar<Consumible>(
				getEncabezadoconsumibleEq(), 3);
		cmbconsumibleEq.setModelo(consumibleEquivalentes);
		Label denomiacionFabricanteeq = new Label();
		Label codSigesp = new Label();
		Label codFabricante = new Label();
		Label codccnu = new Label();

		// cmbconsumibleEq.setWidth("320px");

		denomiacionFabricanteeq.setWidth("150px");
		codSigesp.setWidth("150px");
		codFabricante.setWidth("150px");
		codccnu.setWidth("150px");
		// observaciones.setRows(2);

		/*
		 * denomiacionFabricanteeq.setMaxlength(250); codccnu.setMaxlength(250);
		 * codSigesp.setMaxlength(250); codFabricante.setMaxlength(250);
		 */
		cmbconsumibleEq.setAncho(100);

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

		cmbconsumibleEq.setListenerEncontrar(getControlador());

		lista.add(cmbconsumibleEq);
		lista.add(denomiacionFabricanteeq);
		lista.add(codccnu);
		lista.add(codFabricante);
		lista.add(codSigesp);
		/*
		 * for (Component component : lista) { component.getChildren(); if
		 * (component.getChildren().get(0) ==null){ lista.remove(component); } }
		 */

		return lista;
	}

	private ArrayList<Component> cargarListados() {
		ArrayList<Component> lista = new ArrayList<Component>();
		cmbArticuloEq = new CompBuscar<ArticuloVenta>(
				getEncabezadoconsumibleEq(), 3);
		try {
			cmbArticuloEq.setModelo(servicio.getArticulosConsumible());
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Label denomiacionFabricanteeq = new Label();
		Label codSigesp = new Label();
		Label codFabricante = new Label();
		Label codccnu = new Label();

		cmbArticuloEq.setWidth("220px");

		denomiacionFabricanteeq.setWidth("150px");
		codSigesp.setWidth("150px");
		codFabricante.setWidth("150px");
		codccnu.setWidth("150px");
		// observaciones.setRows(2);

		/*
		 * denomiacionFabricanteeq.setMaxlength(250); codccnu.setMaxlength(250);
		 * codSigesp.setMaxlength(250); codFabricante.setMaxlength(250);
		 */
		cmbArticuloEq.setAncho(650);

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

		cmbArticuloEq.setListenerEncontrar(getControlador());

		lista.add(cmbArticuloEq);
		lista.add(denomiacionFabricanteeq);
		lista.add(codccnu);
		lista.add(codFabricante);
		lista.add(codSigesp);
		/*
		 * for (Component component : lista) { component.getChildren(); if
		 * (component.getChildren().get(0) ==null){ lista.remove(component); } }
		 */

		return lista;
	}

	public void activarAgregar() {
		getDetalle().setVisible(false);

	}

	public void activarConsulta() {

		// getDetalle().setVisible(false);
		getCmbarticulo().setReadonly(true);
		getCmbarticulo().setButtonVisible(false);
		desactivarDetalle();
	}

	public void activarEditar() {

		// getDetalle().setVisible(false);
		getCmbarticulo().setDisabled(true);
		// desactivarfilas();

	}

	@SuppressWarnings("unchecked")
	public void desactivarfilas() {
		List<Row> fila = getDetalle().getFilas().getChildren();
		for (Row row : fila) {

			((CompBuscar<Consumible>) row.getChildren().get(0))
					.setDisabled(true);

		}
		setConsumiblesEq();
	}

	public void setConsumiblesEq() {

		List<Consumible> a = cmbconsumibleEq.getCatalogo().getModelo();
		try {
			a.addAll(servicio.getTodos());
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cmbconsumibleEq.setModelo(a);
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			activarConsulta();
		if (modoOperacion == Accion.AGREGAR)
			activarAgregar();
		if (modoOperacion == Accion.EDITAR)
			activarEditar();
	}

	public List<Consumible> getConsumibleEquivalentes() {
		return consumibleEquivalentes;
	}

	public void setConsumibleEquivalentes(
			List<Consumible> consumibleEquivalentes) {
		this.consumibleEquivalentes = consumibleEquivalentes;
	}

	public Consumible getConsumible() {
		return consumible;
	}

	public void setConsumible(Consumible consumible) {
		this.consumible = consumible;
	}

	public int getAccion() {
		return accion;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	public CompGrupoDatos getCmbgrupoconsumible() {
		return Cmbgrupoconsumible;
	}

	public void setCmbgrupoconsumible(CompGrupoDatos cmbgrupoconsumible) {
		Cmbgrupoconsumible = cmbgrupoconsumible;
	}

	public CompBuscar<ArticuloVenta> getCmbarticulo() {
		return cmbarticulo;
	}

	public void setCmbarticulo(CompBuscar<ArticuloVenta> cmbarticulo) {
		this.cmbarticulo = cmbarticulo;
	}

	public CompBuscar<Consumible> getCmbconsumibleEq() {
		return cmbconsumibleEq;
	}

	public void setCmbconsumibleEq(CompBuscar<Consumible> cmbconsumibleEq) {
		this.cmbconsumibleEq = cmbconsumibleEq;
	}

	public Textbox getTxtCodCCNU() {
		return txtCodCCNU;
	}

	public void setTxtCodCCNU(Textbox txtCodCCNU) {
		this.txtCodCCNU = txtCodCCNU;
	}

	public Textbox getTxtCodSigesp() {
		return txtCodSigesp;
	}

	public void setTxtCodSigesp(Textbox txtCodSigesp) {
		this.txtCodSigesp = txtCodSigesp;
	}

	public Textbox getTxtCodFabricante() {
		return txtCodFabricante;
	}

	public void setTxtCodFabricante(Textbox txtCodFabricante) {
		this.txtCodFabricante = txtCodFabricante;
	}

	public Textbox getTxtDenFabricante() {
		return txtDenFabricante;
	}

	public void setTxtDenFabricante(Textbox txtDenFabricante) {
		this.txtDenFabricante = txtDenFabricante;
	}

}
