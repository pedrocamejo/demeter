package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;

import sun.security.krb5.internal.APOptions;
import cpc.ares.modelo.Accion;
import cpc.demeter.controlador.administrativo.ContFactura;
import cpc.demeter.controlador.administrativo.ContSolicitudExoneracionContrato;
import cpc.modelo.demeter.administrativo.AprobacionExoneracionContrato;
import cpc.modelo.demeter.administrativo.AprobacionReversoRecibo;
import cpc.modelo.demeter.administrativo.Contrato;
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.administrativo.DetalleExoneracionContrato;
import cpc.modelo.demeter.administrativo.ReciboDocumentoFiscal;
import cpc.modelo.demeter.administrativo.SolicitudExoneracionContrato;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UIAprobacionExoneracionContrato extends
		CompVentanaBase<AprobacionExoneracionContrato> {

	/**
	 * 
	 */

	private int accion;
	private CompGrupoDatos gbGeneral;
	private Label fechaRecepcion, numerosolicitud, numerocontrato, motivo,
			pagador, cedrif, monto, fechacontrato, fechaSolictud,
			fechaaprobacionRechazo,sede;

	private Listbox listDetalles;
	private Button SubirArchivo, BajarArchivo;
	private Button Aprobar, Rechazar;

	// private List<Contrato> contratos;
	public UIAprobacionExoneracionContrato(String titulo, int ancho, int modo) {
		super(titulo, ancho);
		this.accion = modo;

	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		gbGeneral = new CompGrupoDatos("Datos generales", 2);
		fechaRecepcion = new Label();
		numerosolicitud = new Label();
		numerocontrato = new Label();
		motivo = new Label();
		pagador = new Label();
		cedrif = new Label();
		monto = new Label();
		fechacontrato = new Label();
		fechaSolictud = new Label();
		fechaaprobacionRechazo = new Label();
		sede = new Label();
		
		listDetalles = new Listbox();
		SubirArchivo = new Button("Subir Archivo");
		SubirArchivo.addEventListener(Events.ON_CLICK, getControlador());
		BajarArchivo = new Button("Bajar Archivo");
		BajarArchivo.addEventListener(Events.ON_CLICK, getControlador());
		Aprobar = new Button("Aprobar");
		Aprobar.addEventListener(Events.ON_CLICK, getControlador());
		Rechazar = new Button("Rechazar");
		Rechazar.addEventListener(Events.ON_CLICK, getControlador());
		Listhead tituloDetalles = new Listhead();

		Listheader servicioProducto = new Listheader("servicio Producto");
		Listheader cantidadinicio = new Listheader(" Cantidad Inicio ");
		Listheader cantidadFinal = new Listheader(" Cantidad Final");
		Listheader Precio = new Listheader(" precio  ");
		Listheader Subtotal = new Listheader("SubTotal");

		tituloDetalles.appendChild(servicioProducto);
		tituloDetalles.appendChild(cantidadinicio);
		tituloDetalles.appendChild(cantidadFinal);
		tituloDetalles.appendChild(Precio);
		tituloDetalles.appendChild(Subtotal);

		listDetalles.appendChild(tituloDetalles);
		listDetalles.setItemRenderer(new ListitemRenderer() {

			@Override
			public void render(Listitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				arg0.setValue(arg1);
				DetalleExoneracionContrato detalle = (DetalleExoneracionContrato) arg1;

				new Listcell(detalle.getProducto()).setParent(arg0);
				new Listcell(detalle.getStrCantidad())
						.setParent(arg0);
				new Listcell(detalle.getStrCantidadReal())
						.setParent(arg0);
				new Listcell(detalle.getStrPrecioUnitario()).setParent(arg0);
				new Listcell(detalle.getStrSubtotal()).setParent(arg0);

				

				// arg0.addEventListener
				// (Events.ON_DOUBLE_CLICK,controlador.getDetalleRecibo() );

			}
		});
	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub
		gbGeneral.addComponente("Fecha Recepcion",fechaRecepcion);
		gbGeneral.addComponente("Fecha Aprobacion / Rechazo",fechaaprobacionRechazo);
		gbGeneral.addComponente("Nº Solicitud",numerosolicitud);
		gbGeneral.addComponente("Sede",sede);
		
		gbGeneral.addComponente("Fecha Solicitud",fechaSolictud);
		gbGeneral.addComponente("Nº Contrato",numerocontrato);
		gbGeneral.addComponente("Fecha Contrato",fechacontrato);
		gbGeneral.addComponente("Pagador Del Contrato",pagador);
		gbGeneral.addComponente("CedRif",cedrif);
		gbGeneral.addComponente("MontoBase",monto);
		gbGeneral.addComponente("Motivo Exoneracion",motivo);
		

		gbGeneral.addComponente("Detalles Contrato", listDetalles);
		gbGeneral.addComponente("Cargar Solicitud", SubirArchivo);
		gbGeneral.addComponente("Bajar Aprobacion/Rechazo", BajarArchivo);
		gbGeneral.addComponente("Aprobar", Aprobar);
		gbGeneral.addComponente("Rechazar", Rechazar);
		gbGeneral.dibujar(this);
		addBotonera();
	}

	@Override
	public void cargarValores(AprobacionExoneracionContrato datos)
			throws ExcDatosInvalidos {
		AprobacionExoneracionContrato dato = getModelo();
		fechaRecepcion.setValue(dato.getStrFechaRecepcion());
		if (accion != Accion.AGREGAR) {

			numerocontrato.setValue(dato.getNumeroContrato());
			numerosolicitud.setValue(dato.getNumeroExoneracion());
			pagador.setValue(dato.getPagador());
			cedrif.setValue(dato.getCedRif());
			sede.setValue(dato.getSede());
			monto.setValue(dato.getStrMontoBase());
			fechacontrato.setValue(dato.getStrFechaContrato());
			fechaSolictud.setValue(dato.getStrFechaSolicitud());
			motivo.setValue(dato.getMotivo());

			if (dato.getFechaAprobacion() != null) {
				fechaaprobacionRechazo.setValue(dato.getStrFechaAprobacion());

			}
			if (dato.getDetalleExoneracionContrato() != null)
				listDetalles.setModel(new ListModelList(dato
						.getDetalleExoneracionContrato()));

			// TODO Auto-generated method stub
		}

	}

	public CompGrupoDatos getGbGeneral() {
		return gbGeneral;
	}

	public void setGbGeneral(CompGrupoDatos gbGeneral) {
		this.gbGeneral = gbGeneral;
	}

	public Label getNumerocontrato() {
		return numerocontrato;
	}

	public void setNumerocontrato(Label numerocontrato) {
		this.numerocontrato = numerocontrato;
	}

	public Label getPagador() {
		return pagador;
	}

	public void setPagador(Label pagador) {
		this.pagador = pagador;
	}

	public Label getCedrif() {
		return cedrif;
	}

	public void setCedrif(Label cedrif) {
		this.cedrif = cedrif;
	}

	public Label getMonto() {
		return monto;
	}

	public void setMonto(Label monto) {
		this.monto = monto;
	}

	public Label getFechacontrato() {
		return fechacontrato;
	}

	public void setFechacontrato(Label fechacontrato) {
		this.fechacontrato = fechacontrato;
	}

	public Label getFechaSolictud() {
		return fechaSolictud;
	}

	public void setFechaSolictud(Label fechaSolictud) {
		this.fechaSolictud = fechaSolictud;
	}

	public Label getFechaaprobacionRechazo() {
		return fechaaprobacionRechazo;
	}

	public void setFechaaprobacionRechazo(Label fechaaprobacionRechazo) {
		this.fechaaprobacionRechazo = fechaaprobacionRechazo;
	}

	public Listbox getListDetalles() {
		return listDetalles;
	}

	public void setListDetalles(Listbox listDetalles) {
		this.listDetalles = listDetalles;
	}

	public Button getSubirArchivo() {
		return SubirArchivo;
	}

	public void setSubirArchivo(Button subirArchivo) {
		SubirArchivo = subirArchivo;
	}

	public Button getBajarArchivo() {
		return BajarArchivo;
	}

	public void setBajarArchivo(Button bajarArchivo) {
		BajarArchivo = bajarArchivo;
	}

	public Label getFechaRecepcion() {
		return fechaRecepcion;
	}

	public void setFechaRecepcion(Label fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}

	public Label getNumerosolicitud() {
		return numerosolicitud;
	}

	public void setNumerosolicitud(Label numerosolicitud) {
		this.numerosolicitud = numerosolicitud;
	}

	public Label getMotivo() {
		return motivo;
	}

	public void setMotivo(Label motivo) {
		this.motivo = motivo;
	}

	public Label getSede() {
		return sede;
	}

	public void setSede(Label sede) {
		this.sede = sede;
	}

	public Button getAprobar() {
		return Aprobar;
	}

	public void setAprobar(Button aprobar) {
		Aprobar = aprobar;
	}

	public Button getRechazar() {
		return Rechazar;
	}

	public void setRechazar(Button rechazar) {
		Rechazar = rechazar;
	}
	
	
	public void desactivar(int modoOperacion,AprobacionExoneracionContrato dato) {
		if (modoOperacion==Accion.AGREGAR)
		modoAgregar();
		if (modoOperacion==Accion.PROCESAR)
			modoProcesar();
		if (modoOperacion==Accion.CONSULTAR)
			modoConsultar(dato);

	}
	public void modoAgregar() {
		BajarArchivo.detach();
		Aprobar.detach();
		Rechazar.detach();
		getAceptar().setDisabled(true);
	}
	
	public void modoProcesar() {
		BajarArchivo.detach();
		SubirArchivo.detach();
		getAceptar().setDisabled(true);
	}
	public void modoConsultar(AprobacionExoneracionContrato dato) {
		Aprobar.detach();
		Rechazar.detach();
		SubirArchivo.detach();
		if (dato.isAprobado()==null) 
			BajarArchivo.detach();
		getAceptar();
	}


}