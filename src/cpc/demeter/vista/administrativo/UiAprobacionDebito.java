package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.AprobacionDebito;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UiAprobacionDebito extends CompVentanaBase<AprobacionDebito> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3020559583323446096L;
	private int accion;
	private CompGrupoDatos gbGeneral;
	private Textbox txtUsuario, txtCedulaRif, txtPagador;
	private Textbox txtNroRecibo, txtNroFactura, txtMotivo;
	private CompCombobox<UnidadAdministrativa> unidad;
	private List<UnidadAdministrativa> unidadAdministrativa;
	private Datebox dtmFechaAprobacion;
	private Doublebox dblMontoRecibo;
	private Doublebox semilla;
	private Textbox codigo;

	// private Label lproduccion;

	public UiAprobacionDebito(String titulo, int ancho, int modo,
			List<UnidadAdministrativa> unidadAdministrativas)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.accion = modo;
		this.unidadAdministrativa = unidadAdministrativas;
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);
		txtNroRecibo = new Textbox();
		txtNroFactura = new Textbox();
		txtMotivo = new Textbox();
		txtUsuario = new Textbox();
		txtCedulaRif = new Textbox();
		txtPagador = new Textbox();
		txtMotivo = new Textbox();
		txtMotivo.setMaxlength(250);
		txtMotivo.setRows(3);
		txtMotivo.setMultiline(true);
		unidad = new CompCombobox<UnidadAdministrativa>();
		dtmFechaAprobacion = new Datebox();
		dblMontoRecibo = new Doublebox();
		semilla = new Doublebox();
		codigo = new Textbox();
		// lproduccion = new Label("Unidad Medida produccion:");

	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 150);
		gbGeneral.addComponente("usuario", txtUsuario);
		txtUsuario.setReadonly(true);
		gbGeneral.addComponente("Fecha", dtmFechaAprobacion);
		dtmFechaAprobacion.setReadonly(true);
		dtmFechaAprobacion.setButtonVisible(false);

		gbGeneral.addComponente("Unidad", unidad);
		unidad.setModelo(unidadAdministrativa);
		gbGeneral.addComponente("Pagador", txtPagador);
		gbGeneral.addComponente("Cedula/Rif",txtCedulaRif);
		gbGeneral.addComponente("NroRecibo", txtNroRecibo);
		gbGeneral.addComponente("NroFactura", txtNroFactura);
		gbGeneral.addComponente("MontoRecibo", dblMontoRecibo);
		gbGeneral.addComponente("Motivo", txtMotivo);
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

	//public void cargarValores( servicio) throws ExcDatosInvalidos {

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

	//}

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
		txtPagador.setReadonly(true);
		txtCedulaRif.setReadonly(true);
		txtNroFactura.setReadonly(true);
		txtNroRecibo.setReadonly(true);
		unidad.setReadonly(true);
		unidad.setButtonVisible(false);
	    dblMontoRecibo.setReadonly(true);
	}

	public void modoEdicion() {

	}

	@Override
	public void cargarValores(AprobacionDebito dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		AprobacionDebito a = getModelo();
		
		txtNroRecibo.setValue(dato.getNroRecibo());
		txtNroFactura.setValue(dato.getNroFactura());
		txtMotivo.setValue(dato.getMotivo());
		txtUsuario.setValue(dato.getUsuario());
		txtCedulaRif.setValue(dato.getCedurif());
		txtPagador.setValue(dato.getPagador());
		txtMotivo.setValue(dato.getMotivo());
		//unidad;
		dtmFechaAprobacion.setValue(dato.getFechaAprobacion());
		dblMontoRecibo.setValue(dato.getMontoRecibo());
		
		
		// getBinder().addBinding(txtusuario, "value",
		// getNombreModelo()+".usuario", null, null, "save", null, null, null,
		// null);

		
		// getBinder().addBinding(fechaaprobacion, "value",
		// getNombreModelo()+".fechaaprobacion", null, null, "save", null, null,
		// null, null);
		if (accion == Accion.CONSULTAR) {
			unidad.setSeleccion(getModelo().getUnidadAdministrativa());
			// getBinder().addBinding(unidad ,"seleccion",
			// getNombreModelo()+".unidadAdministrativa", null, null, "save",
			// null, null, null, null);
		}

		
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

	
	public Textbox getTxtUsuario() {
		return txtUsuario;
	}

	public void setTxtUsuario(Textbox txtUsuario) {
		this.txtUsuario = txtUsuario;
	}

	public Textbox getTxtCedulaRif() {
		return txtCedulaRif;
	}

	public void setTxtCedulaRif(Textbox txtCedulaRif) {
		this.txtCedulaRif = txtCedulaRif;
	}

	public Textbox getTxtPagador() {
		return txtPagador;
	}

	public void setTxtPagador(Textbox txtPagador) {
		this.txtPagador = txtPagador;
	}

	public Textbox getTxtNroRecibo() {
		return txtNroRecibo;
	}

	public void setTxtNroRecibo(Textbox txtNroRecibo) {
		this.txtNroRecibo = txtNroRecibo;
	}

	public Textbox getTxtNroFactura() {
		return txtNroFactura;
	}

	public void setTxtNroFactura(Textbox txtNroFactura) {
		this.txtNroFactura = txtNroFactura;
	}

	public Textbox getTxtMotivo() {
		return txtMotivo;
	}

	public void setTxtMotivo(Textbox txtMotivo) {
		this.txtMotivo = txtMotivo;
	}

	public Datebox getDtmFechaAprobacion() {
		return dtmFechaAprobacion;
	}

	public void setDtmFechaAprobacion(Datebox dtmFechaAprobacion) {
		this.dtmFechaAprobacion = dtmFechaAprobacion;
	}

	public Doublebox getDblMontoRecibo() {
		return dblMontoRecibo;
	}

	public void setDblMontoRecibo(Doublebox dblMontoRecibo) {
		this.dblMontoRecibo = dblMontoRecibo;
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