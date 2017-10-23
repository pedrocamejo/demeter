package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.AprobacionDescuento;
import cpc.modelo.demeter.basico.Labor;
import cpc.modelo.demeter.basico.Servicio;
import cpc.modelo.demeter.basico.TipoServicio;
import cpc.modelo.demeter.basico.TipoUnidadMedida;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UiAprobacionDescuento extends CompVentanaBase<AprobacionDescuento> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3020559583323446096L;
	private int accion;
	private CompGrupoDatos gbGeneral;
	private Textbox txtusuario, cedulaRif, beneficiado;
	private CompCombobox<UnidadAdministrativa> unidad;
	private List<UnidadAdministrativa> unidadAdministrativa;
	private Datebox fechaaprobacion;
	private Doublebox porcentajedescuento;
	private Doublebox semilla;
	private Textbox codigo;

	// private Label lproduccion;

	public UiAprobacionDescuento(String titulo, int ancho, int modo,
			List<UnidadAdministrativa> unidadAdministrativas)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.accion = modo;
		this.unidadAdministrativa = unidadAdministrativas;
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);
		txtusuario = new Textbox();
		cedulaRif = new Textbox();
		beneficiado = new Textbox();
		unidad = new CompCombobox<UnidadAdministrativa>();
		fechaaprobacion = new Datebox();
		porcentajedescuento = new Doublebox();
		semilla = new Doublebox();
		codigo = new Textbox();
		// lproduccion = new Label("Unidad Medida produccion:");

	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 150);
		gbGeneral.addComponente("usuario", txtusuario);
		txtusuario.setReadonly(true);
		gbGeneral.addComponente("Fecha", fechaaprobacion);
		fechaaprobacion.setReadonly(true);
		fechaaprobacion.setButtonVisible(false);

		gbGeneral.addComponente("Unidad", unidad);
		unidad.setModelo(unidadAdministrativa);
		gbGeneral.addComponente("Beneficiario", beneficiado);
		gbGeneral.addComponente("Cedula/Rif", cedulaRif);
		gbGeneral.addComponente("Descuento", porcentajedescuento);
		if (accion == Accion.AGREGAR) {
			gbGeneral.addComponente("ingrese codigo de peticion", semilla);
			// gbGeneral.addComponente("codigo de respuesta",codigo );
		}
		gbGeneral.dibujar(this);

		addBotonera();
		semilla.addEventListener(Events.ON_CHANGE, getControlador());
	}

	/*
	 * gbGeneral.addComponente("Tipo de Servicio:", tipoServicio);
	 * gbGeneral.addComponente("Descripcion:", descripcion);
	 * gbGeneral.addComponente("Tipo Unidad Medida:", tipoUnidad);
	 * gbGeneral.addComponente("Unidad Medida Trabajo:", unidadTrabajo);
	 * gbGeneral.dibujar(this); addBotonera();
	 * tipoUnidad.addEventListener(Events.ON_SELECTION, getControlador());
	 * produccion.addEventListener(Events.ON_CHECK, getControlador()); }
	 */

	public void cargarValores(Servicio servicio) throws ExcDatosInvalidos {

		/*
		 * tipoServicio.setSeleccion(getModelo().getTipoServicio());
		 * getBinder().addBinding(tipoServicio, "seleccion",
		 * getNombreModelo()+".tipoServicio", null, null, "save", null, null,
		 * null, null);
		 * tipoUnidad.setSeleccion(getModelo().getTipoUnidadMedida());
		 * getBinder().addBinding(tipoUnidad, "seleccion",
		 * getNombreModelo()+".tipoUnidadMedida", null, null, "save", null,
		 * null, null, null);
		 * descripcion.setValue(getModelo().getDescripcion());
		 * getBinder().addBinding(descripcion, "value",
		 * getNombreModelo()+".descripcion", null, null, "save", null, null,
		 * null, null);
		 * unidadTrabajo.setSeleccion(getModelo().getTipoUnidadTrabajo());
		 * getBinder().addBinding(unidadTrabajo, "seleccion",
		 * getNombreModelo()+".tipoUnidadTrabajo", null, null, "save", null,
		 * null, null, null);
		 * unidadProduccion.setSeleccion(getModelo().getTipoUnidadProduccion());
		 * getBinder().addBinding(unidadProduccion, "seleccion",
		 * getNombreModelo()+".tipoUnidadProduccion", null, null, "save", null,
		 * null, null, null); if (getModelo().getManejaPases() != null)
		 * pases.setChecked(getModelo().getManejaPases());
		 * getBinder().addBinding(pases, "checked",
		 * getNombreModelo()+".manejaPases", null, null, "save", null, null,
		 * null, null); if (getModelo().getProduccion() != null){
		 * produccion.setChecked(getModelo().getProduccion());
		 * visibilidadProduccion(getModelo().getProduccion()); }else
		 * visibilidadProduccion(false); getBinder().addBinding(produccion,
		 * "checked", getNombreModelo()+".produccion", null, null, "save", null,
		 * null, null, null); if (getModelo().getManejaCantidades() != null)
		 * cantidad.setChecked(getModelo().getManejaCantidades());
		 * getBinder().addBinding(cantidad, "checked",
		 * getNombreModelo()+".manejaCantidades", null, null, "save", null,
		 * null, null, null); if (getModelo().getLabores() != null)
		 * labores.setModelo(getModelo().getLabores());
		 */

	}

	private List<CompEncabezado> getEncabezadoLabor() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Descripcion", 250);
		titulo.setMetodoBinder("getDescripcion");

		encabezado.add(titulo);

		titulo = new CompEncabezado("Uni Labor", 125);
		titulo.setMetodoBinder("getStrUnidadMedidagestion");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Uni Cobro", 125);
		titulo.setMetodoBinder("getStrUnidadMedidaCobro");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		codigo.setVisible(false);
		semilla.setVisible(false);
		beneficiado.setReadonly(true);
		cedulaRif.setReadonly(true);
		unidad.setReadonly(true);
		unidad.setButtonVisible(false);
		porcentajedescuento.setReadonly(true);
	}

	public void modoEdicion() {

	}

	@Override
	public void cargarValores(AprobacionDescuento dato)
			throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		AprobacionDescuento a = getModelo();
		txtusuario.setValue(getModelo().getusuario());
		// getBinder().addBinding(txtusuario, "value",
		// getNombreModelo()+".usuario", null, null, "save", null, null, null,
		// null);

		fechaaprobacion.setValue(getModelo().getfechaaprobacion());
		// getBinder().addBinding(fechaaprobacion, "value",
		// getNombreModelo()+".fechaaprobacion", null, null, "save", null, null,
		// null, null);
		if (accion == Accion.CONSULTAR) {
			unidad.setSeleccion(getModelo().getUnidadAdministrativa());
			// getBinder().addBinding(unidad ,"seleccion",
			// getNombreModelo()+".unidadAdministrativa", null, null, "save",
			// null, null, null, null);
		}

		porcentajedescuento.setValue(getModelo().getdescuento());
		beneficiado.setText(getModelo().getStr_beneficiado());
		cedulaRif.setText(getModelo().getStr_cedurif());
	}

	public int getAccion() {
		return accion;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	public CompGrupoDatos getGbGeneral() {
		return gbGeneral;
	}

	public void setGbGeneral(CompGrupoDatos gbGeneral) {
		this.gbGeneral = gbGeneral;
	}

	public Textbox getTxtusuario() {
		return txtusuario;
	}

	public void setTxtusuario(Textbox txtusuario) {
		this.txtusuario = txtusuario;
	}

	public Textbox getCedulaRif() {
		return cedulaRif;
	}

	public void setCedulaRif(Textbox cedulaRif) {
		this.cedulaRif = cedulaRif;
	}

	public Textbox getBeneficiado() {
		return beneficiado;
	}

	public void setBeneficiado(Textbox beneficiado) {
		this.beneficiado = beneficiado;
	}

	public CompCombobox<UnidadAdministrativa> getUnidad() {
		return unidad;
	}

	public void setUnidad(CompCombobox<UnidadAdministrativa> unidad) {
		this.unidad = unidad;
	}

	public List<UnidadAdministrativa> getUnidadAdministrativa() {
		return unidadAdministrativa;
	}

	public void setUnidadAdministrativa(
			List<UnidadAdministrativa> unidadAdministrativa) {
		this.unidadAdministrativa = unidadAdministrativa;
	}

	public Datebox getFechaaprobacion() {
		return fechaaprobacion;
	}

	public void setFechaaprobacion(Datebox fechaaprobacion) {
		this.fechaaprobacion = fechaaprobacion;
	}

	public Doublebox getPorcentajedescuento() {
		return porcentajedescuento;
	}

	public void setPorcentajedescuento(Doublebox porcentajedescuento) {
		this.porcentajedescuento = porcentajedescuento;
	}

	public Doublebox getSemilla() {
		return semilla;
	}

	public void setSemilla(Doublebox semilla) {
		this.semilla = semilla;
	}

	public Textbox getCodigo() {
		return codigo;
	}

	public void setCodigo(Textbox codigo) {
		this.codigo = codigo;
	}
}
