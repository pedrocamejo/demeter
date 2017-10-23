package cpc.demeter.vista.administrativo;

import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.AprobacionDebitoInterno;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIAprobacionDebitoInterno extends CompVentanaBase<AprobacionDebitoInterno> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8974484486749034768L;
	/**
	 * 
	 */
	
	private int accion;
	private CompGrupoDatos gbGeneral;
	private Textbox txtNombreResponsable,txtCedulaResponsable,txtNombreEjecutor,txtCedulaEjecutor,txtNombreBeneficiario,txtCedRifBenefiario,txtNroControlDocumento,txtNroDocuemnto;
	private CompCombobox<UnidadAdministrativa> unidad;
	private List<UnidadAdministrativa> unidadAdministrativa;
	private Datebox fechaaprobacion;
	private Doublebox semilla;
	private Doublebox monto;
	private Textbox codigo;
	
	private Textbox txtObservacion,txtMotivo;
	

	// private Label lproduccion;

	public UIAprobacionDebitoInterno(String titulo, int ancho, int modo,
			List<UnidadAdministrativa> unidadAdministrativas)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.accion = modo;
		this.unidadAdministrativa = unidadAdministrativas;
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);
		fechaaprobacion=new Datebox();
		txtNombreResponsable =new Textbox ();
		txtCedulaResponsable =new Textbox ();
		txtNombreEjecutor =new Textbox ();
		txtCedulaEjecutor =new Textbox ();
		txtNombreBeneficiario =new Textbox ();
		txtCedRifBenefiario =new Textbox ();
		txtNroControlDocumento =new Textbox ();
		txtNroDocuemnto =new Textbox ();
		txtObservacion =new Textbox ();
		txtObservacion.setMultiline(true);
		txtObservacion.setWidth("600px");
		txtMotivo =new Textbox ();
		txtMotivo.setWidth("600px");
		txtMotivo.setMultiline(true);
		semilla = new Doublebox();
		monto=new Doublebox();
		codigo = new Textbox();
		unidad = new CompCombobox<UnidadAdministrativa>();

	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 150);
		gbGeneral.addComponente("Fecha", fechaaprobacion);
		fechaaprobacion.setReadonly(true);
		fechaaprobacion.setButtonVisible(false);

		gbGeneral.addComponente("Unidad", unidad);
		unidad.setModelo(unidadAdministrativa);
		if (accion == Accion.AGREGAR) {
			gbGeneral.addComponente("ingrese codigo de peticion", semilla);
			// gbGeneral.addComponente("codigo de respuesta",codigo );
		}
		gbGeneral.addComponente("Nombre Responsable",txtNombreResponsable );
		gbGeneral.addComponente("Cedula Responsable",txtCedulaResponsable );
		gbGeneral.addComponente("Nombre Ejecutor",txtNombreEjecutor );
		gbGeneral.addComponente("Cedula Ejecutor",txtCedulaEjecutor );
		gbGeneral.addComponente("Nombre Beneficiario",txtNombreBeneficiario );
		gbGeneral.addComponente("CedRif Benefiario",txtCedRifBenefiario );
		gbGeneral.addComponente("Nro ControlDocumento",txtNroControlDocumento );
		gbGeneral.addComponente("Nro Docuemnto",txtNroDocuemnto );
		gbGeneral.addComponente("monto",monto);
		gbGeneral.addComponente("Observacion",txtObservacion );
		gbGeneral.addComponente("Motivo",txtMotivo );

		
		
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
		unidad.setReadonly(true);
		unidad.setButtonVisible(false);
		txtNombreResponsable.setReadonly(true);
		txtCedulaResponsable.setReadonly(true);
		txtNombreEjecutor.setReadonly(true);
		txtCedulaEjecutor.setReadonly(true);
		txtNombreBeneficiario.setReadonly(true);
		txtCedRifBenefiario.setReadonly(true);
		txtNroControlDocumento.setReadonly(true);
		txtNroDocuemnto.setReadonly(true);
		txtObservacion.setReadonly(true);
		txtMotivo.setReadonly(true);
		monto.setReadonly(true);
		
	}

	public void modoEdicion() {

	}

	@Override
	public void cargarValores(AprobacionDebitoInterno dato)
			throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		AprobacionDebitoInterno a = getModelo();
	
		fechaaprobacion.setValue(getModelo().getFechaAprobacion());
	
		if (accion == Accion.CONSULTAR) {
			unidad.setSeleccion(getModelo().getUnidadAdministrativa());
			
		}
		
		txtNombreResponsable.setValue(getModelo().getNombreResponsable() ); 
		txtCedulaResponsable.setValue(getModelo().getCedulaResponsable() ); 
		txtNombreEjecutor.setValue(getModelo().getNombreEjecutor() ); 
		txtCedulaEjecutor.setValue(getModelo().getCedulaEjecutor() ); 
		txtNombreBeneficiario.setValue(getModelo().getNombreBeneficiario() ); 
		txtCedRifBenefiario.setValue(getModelo().getIdLegalBeneficiario() ); 
		txtNroControlDocumento.setValue(getModelo().getNroControlDocumentoAfectado() ); 
		txtNroDocuemnto.setValue(getModelo().getNroDocumentoAfectado() ); 
		txtObservacion.setValue(getModelo().getObservacion() ); 
		txtMotivo.setValue(getModelo().getMotivo() ); 
		monto.setValue(getModelo().getMonto());
		if (accion != Accion.AGREGAR) {
		int alto = (getModelo().getObservacion().length()/8);
		if (alto>=50)
		txtMotivo.setHeight(alto+"px");
		int alto2 = (getModelo().getMotivo().length()/8);
		if (alto2>=50)
		txtMotivo.setHeight(alto2+"px");
		}
		
		
	}

	public Textbox getTxtNombreResponsable() {
		return txtNombreResponsable;
	}

	public void setTxtNombreResponsable(Textbox txtNombreResponsable) {
		this.txtNombreResponsable = txtNombreResponsable;
	}

	public Textbox getTxtCedulaResponsable() {
		return txtCedulaResponsable;
	}

	public void setTxtCedulaResponsable(Textbox txtCedulaResponsable) {
		this.txtCedulaResponsable = txtCedulaResponsable;
	}

	public Textbox getTxtNombreEjecutor() {
		return txtNombreEjecutor;
	}

	public void setTxtNombreEjecutor(Textbox txtNombreEjecutor) {
		this.txtNombreEjecutor = txtNombreEjecutor;
	}

	public Textbox getTxtCedulaEjecutor() {
		return txtCedulaEjecutor;
	}

	public void setTxtCedulaEjecutor(Textbox txtCedulaEjecutor) {
		this.txtCedulaEjecutor = txtCedulaEjecutor;
	}

	public Textbox getTxtNombreBeneficiario() {
		return txtNombreBeneficiario;
	}

	public void setTxtNombreBeneficiario(Textbox txtNombreBeneficiario) {
		this.txtNombreBeneficiario = txtNombreBeneficiario;
	}

	public Textbox getTxtCedRifBenefiario() {
		return txtCedRifBenefiario;
	}

	public void setTxtCedRifBenefiario(Textbox txtCedRifBenefiario) {
		this.txtCedRifBenefiario = txtCedRifBenefiario;
	}

	public Textbox getTxtNroControlDocumento() {
		return txtNroControlDocumento;
	}

	public void setTxtNroControlDocumento(Textbox txtNroControlDocumento) {
		this.txtNroControlDocumento = txtNroControlDocumento;
	}

	public Textbox getTxtNroDocuemnto() {
		return txtNroDocuemnto;
	}

	public void setTxtNroDocuemnto(Textbox txtNroDocuemnto) {
		this.txtNroDocuemnto = txtNroDocuemnto;
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

	public Textbox getTxtObservacion() {
		return txtObservacion;
	}

	public void setTxtObservacion(Textbox txtObservacion) {
		this.txtObservacion = txtObservacion;
	}

	public Textbox getTxtMotivo() {
		return txtMotivo;
	}

	public void setTxtMotivo(Textbox txtMotivo) {
		this.txtMotivo = txtMotivo;
	}

	public Doublebox getMonto() {
		return monto;
	}

	public void setMonto(Doublebox monto) {
		this.monto = monto;
	}

}