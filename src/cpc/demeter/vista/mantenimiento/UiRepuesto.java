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
import cpc.modelo.demeter.mantenimiento.Repuesto;
import cpc.modelo.demeter.mantenimiento.RepuestoEquivalente;
import cpc.negocio.demeter.mantenimiento.NegocioRepuesto;
import cpc.persistencia.demeter.implementacion.basico.PerArticuloVenta;
import cpc.persistencia.demeter.implementacion.mantenimiento.PerRepuesto;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class UiRepuesto extends CompVentanaMaestroDetalle<Repuesto, Repuesto> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7972502841695438874L;
	// private UnidadAdministrativa unidadAdministrativa;
	// private List<repuestoEquivalente> repuestoEquivalentes;
	private List<Repuesto> repuestoEquivalentes;
	private Repuesto repuesto;
	private int accion;
	private NegocioRepuesto servicio = NegocioRepuesto.getInstance();
	private CompGrupoDatos Cmbgruporepuesto;

	private CompBuscar<ArticuloVenta> cmbarticulo;
	// private CompBuscar<repuestoEquivalente> cmbrepuestoEq;
	private CompBuscar<Repuesto> cmbrepuestoEq;
	private CompBuscar<ArticuloVenta> cmbArticuloEq;
	private Textbox txtCodCCNU;
	private Textbox txtCodSigesp;
	private Textbox txtCodFabricante;
	private Textbox txtDenFabricante;
	private CompLista<RepuestoEquivalente> detallerepuesto;

	public UiRepuesto(String titulo, int ancho, int modo, Repuesto repuesto,
			List<RepuestoEquivalente> repuestoEquivalente) {
		super(titulo, ancho);
		// this.unidadAdministrativa = unidadAdministrativa;
		setModelo(repuesto);
		this.accion = modo;
		this.repuesto = repuesto;

		List<Repuesto> repuestoss = new ArrayList<Repuesto>();
		if (repuestoEquivalente != null) {
			for (RepuestoEquivalente repuestoEquivalente2 : repuestoEquivalente) {
				Repuesto x = repuestoEquivalente2.getRepuestoOri();
				Repuesto z = repuestoEquivalente2.getRepuestoEq();
				repuestoss.add(z);
			}
		}

		if (accion == Accion.EDITAR) {
			try {
				repuestoss.addAll(servicio.getTodos());
			} catch (ExcFiltroExcepcion e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// this.repuestoEquivalentes = repuestoEquivalente;

		this.repuestoEquivalentes = repuestoss;

	}

	@Override
	public void inicializar() {

		Cmbgruporepuesto = new CompGrupoDatos("Datos del Repuesto", 4);
		// setGbEncabezado(1, "");
		detallerepuesto = new CompLista<RepuestoEquivalente>(
				encabezadosPrimario());
		txtCodCCNU = new Textbox();
		txtCodSigesp = new Textbox();
		txtCodFabricante = new Textbox();
		txtDenFabricante = new Textbox();
		cmbarticulo = new CompBuscar<ArticuloVenta>(getEncabezadoArticulo(), 3);
		cmbarticulo.setAncho(600);
		// servicio =Negociorepuesto.getInstance();
		// setGbDetalle("repuestos Equivalentes");

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

		Cmbgruporepuesto.addComponente("Articulo", cmbarticulo);
		// Cmbgruporepuesto.addComponente(" ",null);
		Cmbgruporepuesto.addComponente("DenFabricante", txtDenFabricante);
		Cmbgruporepuesto.addComponente("CodFabricante", txtCodFabricante);
		Cmbgruporepuesto.addComponente("CodSigesp", txtCodSigesp);
		Cmbgruporepuesto.addComponente("CodCCNU", txtCodCCNU);

		// setDetalles(cargarLista(), getModelo().getrepuestoEquivalente(),
		// encabezadosPrimario());
		setDetalles(cargarListados(), null, encabezadosPrimariodos());
		getDetalle().setNuevo(new Repuesto());
		getGbDetalle().appendChild(detallerepuesto);
		if (accion != Accion.EDITAR) {

		}

		// actualizarModelo();
		// setDetalles(cargarLista(), getModelo().getEntradas(),
		// encabezadosPrimario());}
		// getDetalle().setNuevo(new EntradaActivo());
		// getDetalle().setListenerBorrar(getControlador());
		// dibujarEstructura();
		// setAltoDetalle(200);
		Cmbgruporepuesto.dibujar(this);
		dibujarEstructura();
		cmbarticulo.setListenerEncontrar(getControlador());
		// cmbarticulo.addEventListener(Events.ON_SELECTION, getControlador());
		addBotonera();

	}

	public void cargarValores(Repuesto repuesto) throws ExcDatosInvalidos {

		if (accion != Accion.AGREGAR) {
			txtCodCCNU.setValue(repuesto.getCodigoCCNU());
			// getBinder().addBinding (txtCodCCNU, "value",
			// getNombreModelo()+".descripcion", null, null, "save", null, null,
			// null, null);
			txtCodFabricante.setValue(repuesto.getCodigoFabricante());

			txtDenFabricante.setValue(repuesto.getDenominacionFabricante());

			txtCodSigesp.setValue(repuesto.getCodigoSIGESP());

			cmbarticulo.setSeleccion(repuesto.getArticuloVenta());
			getBinder().addBinding(cmbarticulo, "seleccion",
					getNombreModelo() + ".articuloVenta", null, null, "save",
					null, null, null, null);
			cmbarticulo.setText(repuesto.getCodigoFabricante());

			if (getModelo().getRepuestoEquivalente() != null)
				detallerepuesto.setModelo(getModelo().getRepuestoEquivalente());
		}

		if (accion == Accion.AGREGAR) {
			try {
				cmbarticulo.setModelo(servicio.getArticulosSinRepuesto());
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
		titulo = new CompEncabezado("repuesto Eq ", 120);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getrepuestoEq");
		titulo.setMetodoModelo(".repuestoEquivalente");

		encabezado.add(titulo);

		titulo = new CompEncabezado("DenominacionFabricanteEq", 180);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getDenominacionFabricanteEq");
		titulo.setOrdenable(true);
		// titulo.setMetodoModelo(".repuestoEquivalente");
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
		titulo = new CompEncabezado("repuesto Eq ", 120);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getArticuloVenta");
		titulo.setMetodoModelo(".articuloVenta");

		encabezado.add(titulo);

		titulo = new CompEncabezado("DenominacionFabricante", 180);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getDenominacionFabricante");

		titulo.setOrdenable(true);
		// titulo.setMetodoModelo(".repuestoEquivalente");
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

	private List<CompEncabezado> getEncabezadorepuestoEq() {
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
		cmbrepuestoEq = new CompBuscar<Repuesto>(getEncabezadorepuestoEq(), 3);
		cmbrepuestoEq.setModelo(repuestoEquivalentes);
		Label denomiacionFabricanteeq = new Label();
		Label codSigesp = new Label();
		Label codFabricante = new Label();
		Label codccnu = new Label();

		cmbrepuestoEq.setWidth("320px");

		denomiacionFabricanteeq.setWidth("150px");
		codSigesp.setWidth("150px");
		codFabricante.setWidth("150px");
		codccnu.setWidth("150px");
		// observaciones.setRows(2);

		/*
		 * denomiacionFabricanteeq.setMaxlength(250); codccnu.setMaxlength(250);
		 * codSigesp.setMaxlength(250); codFabricante.setMaxlength(250);
		 */
		cmbrepuestoEq.setAncho(500);

		// codigoActivo.setModelo(activos);
		// almacen.setModelo(almacenes);
		/*
		 * List<repuestoEquivalente> z = repuestoEquivalentes; repuesto []
		 * repuestos = new repuesto[z.size()]; int a = 0; for
		 * (repuestoEquivalente repuestoEquivalente : z) { repuestos[a]=
		 * (repuestoEquivalente.getrepuestoEquivalente()); a++; }
		 */

		// repuestoEq.setAttribute("repuesto", "repuesto");

		// estadoActivo.setModelo(estadosActivo);

		cmbrepuestoEq.setListenerEncontrar(getControlador());

		lista.add(cmbrepuestoEq);
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
				getEncabezadorepuestoEq(), 3);
		try {
			cmbArticuloEq.setModelo(servicio.getArticulosRepuesto());
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
		 * List<repuestoEquivalente> z = repuestoEquivalentes; repuesto []
		 * repuestos = new repuesto[z.size()]; int a = 0; for
		 * (repuestoEquivalente repuestoEquivalente : z) { repuestos[a]=
		 * (repuestoEquivalente.getrepuestoEquivalente()); a++; }
		 */

		// repuestoEq.setAttribute("repuesto", "repuesto");

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

			((CompBuscar<Repuesto>) row.getChildren().get(0)).setDisabled(true);

		}
		setrepuestosEq();
	}

	public void setrepuestosEq() {

		List<Repuesto> a = cmbrepuestoEq.getCatalogo().getModelo();
		try {
			a.addAll(servicio.getTodos());
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		cmbrepuestoEq.setModelo(a);
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			activarConsulta();
		if (modoOperacion == Accion.AGREGAR)
			activarAgregar();
		if (modoOperacion == Accion.EDITAR)
			activarEditar();
	}

	public List<Repuesto> getrepuestoEquivalentes() {
		return repuestoEquivalentes;
	}

	public void setrepuestoEquivalentes(List<Repuesto> repuestoEquivalentes) {
		this.repuestoEquivalentes = repuestoEquivalentes;
	}

	public Repuesto getrepuesto() {
		return repuesto;
	}

	public void setrepuesto(Repuesto repuesto) {
		this.repuesto = repuesto;
	}

	public int getAccion() {
		return accion;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	public CompGrupoDatos getCmbgruporepuesto() {
		return Cmbgruporepuesto;
	}

	public void setCmbgruporepuesto(CompGrupoDatos cmbgruporepuesto) {
		Cmbgruporepuesto = cmbgruporepuesto;
	}

	public CompBuscar<ArticuloVenta> getCmbarticulo() {
		return cmbarticulo;
	}

	public void setCmbarticulo(CompBuscar<ArticuloVenta> cmbarticulo) {
		this.cmbarticulo = cmbarticulo;
	}

	public CompBuscar<Repuesto> getCmbrepuestoEq() {
		return cmbrepuestoEq;
	}

	public void setCmbrepuestoEq(CompBuscar<Repuesto> cmbrepuestoEq) {
		this.cmbrepuestoEq = cmbrepuestoEq;
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
