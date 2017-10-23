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
import cpc.modelo.demeter.administrativo.AprobacionReversoRecibo;
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

public class UIAprobacionReversoRecibo extends CompVentanaBase<AprobacionReversoRecibo> {

	/**
	 * 
	 */
	
	private int accion;
	private CompGrupoDatos gbGeneral;
	private Textbox txtusuario, cedulaRifbeneficiario, beneficiario;
	private CompCombobox<UnidadAdministrativa> unidad;
	private List<UnidadAdministrativa> unidadAdministrativa;
	private Datebox fechaaprobacion;
	private Doublebox semilla;
	private Textbox codigo;
	private Doublebox MontoRecibo;
	private Textbox nroRecibo,cedulaRifSolicitante, solictante,observacion;
	

	// private Label lproduccion;

	public UIAprobacionReversoRecibo(String titulo, int ancho, int modo,
			List<UnidadAdministrativa> unidadAdministrativas)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.accion = modo;
		this.unidadAdministrativa = unidadAdministrativas;
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);
		txtusuario = new Textbox();
		cedulaRifbeneficiario = new Textbox();
		cedulaRifSolicitante = new Textbox();
		beneficiario = new Textbox();
		solictante=new Textbox();
		unidad = new CompCombobox<UnidadAdministrativa>();
		fechaaprobacion = new Datebox();
		MontoRecibo = new Doublebox();
		nroRecibo=new Textbox();
		observacion=new Textbox();
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
		gbGeneral.addComponente("Nro recibo", nroRecibo);
		gbGeneral.addComponente("Monto Recibo", MontoRecibo);
		gbGeneral.addComponente("Cedula/Rif Beneficiario ", cedulaRifbeneficiario);
		gbGeneral.addComponente("Beneficiario", beneficiario);
		
		gbGeneral.addComponente("Cedula/Rif Solicitante", cedulaRifSolicitante);
		gbGeneral.addComponente("Solicitante", solictante );
		gbGeneral.addComponente("Observacion ", observacion);
		if (accion == Accion.AGREGAR) {
			gbGeneral.addComponente("ingrese codigo de peticion", semilla);
			// gbGeneral.addComponente("codigo de respuesta",codigo );
		}
		gbGeneral.dibujar(this);

		addBotonera();
		semilla.addEventListener(Events.ON_CHANGE, getControlador());
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
		beneficiario.setReadonly(true);
		cedulaRifbeneficiario.setReadonly(true);
		unidad.setReadonly(true);
		unidad.setButtonVisible(false);
		nroRecibo.setReadonly(true);
		cedulaRifSolicitante.setReadonly(true);
		solictante.setReadonly(true);
		MontoRecibo.setReadonly(true);
		observacion.setReadonly(true);
		
	}

	public void modoEdicion() {

	}

	@Override
	public void cargarValores(AprobacionReversoRecibo dato)
			throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		AprobacionReversoRecibo a = getModelo();
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
		
		beneficiario.setValue(getModelo().getBeneficiario());
		cedulaRifbeneficiario.setValue(getModelo().getBeneficiariocedurif());
		nroRecibo.setValue(getModelo().getRecibo());
		cedulaRifSolicitante.setValue(getModelo().getCedulasolicitante());
		solictante.setValue(getModelo().getNombresolicitante());
		MontoRecibo.setValue(getModelo().getMontoRecibo());
		observacion.setValue(getModelo().getObservacion());
		
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

	public Textbox getCedulaRifbeneficiario() {
		return cedulaRifbeneficiario;
	}

	public void setCedulaRifbeneficiario(Textbox cedulaRifbeneficiario) {
		this.cedulaRifbeneficiario = cedulaRifbeneficiario;
	}

	public Textbox getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(Textbox beneficiario) {
		this.beneficiario = beneficiario;
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

	public Doublebox getMontoRecibo() {
		return MontoRecibo;
	}

	public void setMontoRecibo(Doublebox montoRecibo) {
		MontoRecibo = montoRecibo;
	}

	public Textbox getNroRecibo() {
		return nroRecibo;
	}

	public void setNroRecibo(Textbox nroRecibo) {
		this.nroRecibo = nroRecibo;
	}

	public Textbox getCedulaRifSolicitante() {
		return cedulaRifSolicitante;
	}

	public void setCedulaRifSolicitante(Textbox cedulaRifSolicitante) {
		this.cedulaRifSolicitante = cedulaRifSolicitante;
	}

	public Textbox getSolictante() {
		return solictante;
	}

	public void setSolictante(Textbox solictante) {
		this.solictante = solictante;
	}

	public Textbox getObservacion() {
		return observacion;
	}

	public void setObservacion(Textbox observacion) {
		this.observacion = observacion;
	}
	
	

}