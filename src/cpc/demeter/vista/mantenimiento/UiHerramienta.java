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
import cpc.modelo.demeter.mantenimiento.Herramienta;
import cpc.modelo.demeter.mantenimiento.HerramientaEquivalente;
import cpc.negocio.demeter.mantenimiento.NegocioHerramienta;
import cpc.persistencia.demeter.implementacion.basico.PerArticuloVenta;
import cpc.persistencia.demeter.implementacion.mantenimiento.PerHerramienta;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class UiHerramienta extends
		CompVentanaMaestroDetalle<Herramienta, Herramienta> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7972502841695438874L;
	// private UnidadAdministrativa unidadAdministrativa;
	// private List<herramientaEquivalente> herramientaEquivalentes;
	private List<Herramienta> herramientaEquivalentes;
	private Herramienta herramienta;
	private int accion;
	private NegocioHerramienta servicio = NegocioHerramienta.getInstance();
	private CompGrupoDatos Cmbgrupoherramienta;

	private CompBuscar<ArticuloVenta> cmbarticulo;
	// private CompBuscar<herramientaEquivalente> cmbherramientaEq;
	private CompBuscar<Herramienta> cmbherramientaEq;
	private CompBuscar<ArticuloVenta> cmbArticuloEq;
	private Textbox txtCodCCNU;
	private Textbox txtCodSigesp;
	private Textbox txtCodFabricante;
	private Textbox txtDenFabricante;
	private CompLista<HerramientaEquivalente> detalleherramienta;

	public UiHerramienta(String titulo, int ancho, int modo,
			Herramienta herramienta,
			List<HerramientaEquivalente> herramientaEquivalente) {
		super(titulo, ancho);
		// this.unidadAdministrativa = unidadAdministrativa;
		setModelo(herramienta);
		this.accion = modo;
		this.herramienta = herramienta;

		List<Herramienta> herramientass = new ArrayList<Herramienta>();
		if (herramientaEquivalente != null) {
			for (HerramientaEquivalente herramientaEquivalente2 : herramientaEquivalente) {
				Herramienta x = herramientaEquivalente2.getHerramientaOri();
				Herramienta z = herramientaEquivalente2.getHerramientaEq();
				herramientass.add(z);
			}
		}

		if (accion == Accion.EDITAR) {
			try {
				herramientass.addAll(servicio.getTodos());
			} catch (ExcFiltroExcepcion e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// this.herramientaEquivalentes = herramientaEquivalente;

		this.herramientaEquivalentes = herramientass;

	}

	@Override
	public void inicializar() {

		Cmbgrupoherramienta = new CompGrupoDatos("Datos de la Herramienta", 4);
		// setGbEncabezado(1, "");
		detalleherramienta = new CompLista<HerramientaEquivalente>(
				encabezadosPrimario());
		txtCodCCNU = new Textbox();
		txtCodSigesp = new Textbox();
		txtCodFabricante = new Textbox();
		txtDenFabricante = new Textbox();
		cmbarticulo = new CompBuscar<ArticuloVenta>(getEncabezadoArticulo(), 3);
		cmbarticulo.setAncho(600);
		// servicio =Negocioherramienta.getInstance();
		// setGbDetalle("herramientas Equivalentes");

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

		Cmbgrupoherramienta.addComponente("Articulo", cmbarticulo);
		// Cmbgrupoherramienta.addComponente(" ",null);
		Cmbgrupoherramienta.addComponente("DenFabricante", txtDenFabricante);
		Cmbgrupoherramienta.addComponente("CodFabricante", txtCodFabricante);
		Cmbgrupoherramienta.addComponente("CodSigesp", txtCodSigesp);
		Cmbgrupoherramienta.addComponente("CodCCNU", txtCodCCNU);

		// setDetalles(cargarLista(), getModelo().getherramientaEquivalente(),
		// encabezadosPrimario());
		setDetalles(cargarListados(), null, encabezadosPrimariodos());
		getDetalle().setNuevo(new Herramienta());
		getGbDetalle().appendChild(detalleherramienta);
		if (accion != Accion.EDITAR) {

		}

		// actualizarModelo();
		// setDetalles(cargarLista(), getModelo().getEntradas(),
		// encabezadosPrimario());}
		// getDetalle().setNuevo(new EntradaActivo());
		// getDetalle().setListenerBorrar(getControlador());
		// dibujarEstructura();
		// setAltoDetalle(200);
		Cmbgrupoherramienta.dibujar(this);
		dibujarEstructura();
		cmbarticulo.setListenerEncontrar(getControlador());
		// cmbarticulo.addEventListener(Events.ON_SELECTION, getControlador());
		addBotonera();

	}

	public void cargarValores(Herramienta herramienta) throws ExcDatosInvalidos {

		if (accion != Accion.AGREGAR) {
			txtCodCCNU.setValue(herramienta.getCodigoCCNU());
			// getBinder().addBinding (txtCodCCNU, "value",
			// getNombreModelo()+".descripcion", null, null, "save", null, null,
			// null, null);
			txtCodFabricante.setValue(herramienta.getCodigoFabricante());

			txtDenFabricante.setValue(herramienta.getDenominacionFabricante());

			txtCodSigesp.setValue(herramienta.getCodigoSIGESP());

			cmbarticulo.setSeleccion(herramienta.getArticuloVenta());
			getBinder().addBinding(cmbarticulo, "seleccion",
					getNombreModelo() + ".articuloVenta", null, null, "save",
					null, null, null, null);
			cmbarticulo.setText(herramienta.getCodigoFabricante());

			if (getModelo().getHerramientaEquivalente() != null)
				detalleherramienta.setModelo(getModelo()
						.getHerramientaEquivalente());
		}

		if (accion == Accion.AGREGAR) {
			try {
				cmbarticulo.setModelo(servicio.getArticulosSinherramienta());
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
		titulo = new CompEncabezado("herramienta Eq ", 120);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getherramientaEq");
		titulo.setMetodoModelo(".herramientaEquivalente");

		encabezado.add(titulo);

		titulo = new CompEncabezado("DenominacionFabricanteEq", 180);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getDenominacionFabricanteEq");
		titulo.setOrdenable(true);
		// titulo.setMetodoModelo(".herramientaEquivalente");
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
		titulo = new CompEncabezado("herramienta Eq ", 120);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getArticuloVenta");
		titulo.setMetodoModelo(".articuloVenta");

		encabezado.add(titulo);

		titulo = new CompEncabezado("DenominacionFabricante", 180);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getDenominacionFabricante");

		titulo.setOrdenable(true);
		// titulo.setMetodoModelo(".herramientaEquivalente");
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

	private List<CompEncabezado> getEncabezadoherramientaEq() {
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
		cmbherramientaEq = new CompBuscar<Herramienta>(
				getEncabezadoherramientaEq(), 3);
		cmbherramientaEq.setModelo(herramientaEquivalentes);
		Label denomiacionFabricanteeq = new Label();
		Label codSigesp = new Label();
		Label codFabricante = new Label();
		Label codccnu = new Label();

		cmbherramientaEq.setWidth("320px");

		denomiacionFabricanteeq.setWidth("150px");
		codSigesp.setWidth("150px");
		codFabricante.setWidth("150px");
		codccnu.setWidth("150px");
		// observaciones.setRows(2);

		/*
		 * denomiacionFabricanteeq.setMaxlength(250); codccnu.setMaxlength(250);
		 * codSigesp.setMaxlength(250); codFabricante.setMaxlength(250);
		 */
		cmbherramientaEq.setAncho(500);

		// codigoActivo.setModelo(activos);
		// almacen.setModelo(almacenes);
		/*
		 * List<herramientaEquivalente> z = herramientaEquivalentes; herramienta
		 * [] herramientas = new herramienta[z.size()]; int a = 0; for
		 * (herramientaEquivalente herramientaEquivalente : z) {
		 * herramientas[a]=
		 * (herramientaEquivalente.getherramientaEquivalente()); a++; }
		 */

		// herramientaEq.setAttribute("herramienta", "herramienta");

		// estadoActivo.setModelo(estadosActivo);

		cmbherramientaEq.setListenerEncontrar(getControlador());

		lista.add(cmbherramientaEq);
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
				getEncabezadoherramientaEq(), 3);
		try {
			cmbArticuloEq.setModelo(servicio.getArticulosherramienta());
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
		 * List<herramientaEquivalente> z = herramientaEquivalentes; herramienta
		 * [] herramientas = new herramienta[z.size()]; int a = 0; for
		 * (herramientaEquivalente herramientaEquivalente : z) {
		 * herramientas[a]=
		 * (herramientaEquivalente.getherramientaEquivalente()); a++; }
		 */

		// herramientaEq.setAttribute("herramienta", "herramienta");

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

			((CompBuscar<Herramienta>) row.getChildren().get(0))
					.setDisabled(true);

		}
		setherramientasEq();
	}

	public void setherramientasEq() {

		List<Herramienta> a = cmbherramientaEq.getCatalogo().getModelo();
		try {
			a.addAll(servicio.getTodos());
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cmbherramientaEq.setModelo(a);
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			activarConsulta();
		if (modoOperacion == Accion.AGREGAR)
			activarAgregar();
		if (modoOperacion == Accion.EDITAR)
			activarEditar();
	}

	public List<Herramienta> getherramientaEquivalentes() {
		return herramientaEquivalentes;
	}

	public void setherramientaEquivalentes(
			List<Herramienta> herramientaEquivalentes) {
		this.herramientaEquivalentes = herramientaEquivalentes;
	}

	public Herramienta getherramienta() {
		return herramienta;
	}

	public void setherramienta(Herramienta herramienta) {
		this.herramienta = herramienta;
	}

	public int getAccion() {
		return accion;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	public CompGrupoDatos getCmbgrupoherramienta() {
		return Cmbgrupoherramienta;
	}

	public void setCmbgrupoherramienta(CompGrupoDatos cmbgrupoherramienta) {
		Cmbgrupoherramienta = cmbgrupoherramienta;
	}

	public CompBuscar<ArticuloVenta> getCmbarticulo() {
		return cmbarticulo;
	}

	public void setCmbarticulo(CompBuscar<ArticuloVenta> cmbarticulo) {
		this.cmbarticulo = cmbarticulo;
	}

	public CompBuscar<Herramienta> getCmbherramientaEq() {
		return cmbherramientaEq;
	}

	public void setCmbherramientaEq(CompBuscar<Herramienta> cmbherramientaEq) {
		this.cmbherramientaEq = cmbherramientaEq;
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
