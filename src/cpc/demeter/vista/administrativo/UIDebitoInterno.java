package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.modelo.demeter.administrativo.DebitoInterno;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UIDebitoInterno extends CompVentanaBase<DebitoInterno> {

	private static final long serialVersionUID = 6312240548253365129L;
	private Date fechaCierre;

	private List<DocumentoFiscal> facturas;
	private CompBuscar<DocumentoFiscal> factura;
	private Textbox control, sede, txtUsuario;
	private Datebox fecha;
	private Textbox razonSocial;
	private CompGrupoDatos cliente, encrecibo;
	private Doublebox total, saldo;
	private Label saldoInicial;
	private Textbox estado;
	private Textbox concepto;
	private AppDemeter app;
	private Button botonAutorizar ;
	public UIDebitoInterno(String titulo, int ancho,
			List<DocumentoFiscal> facturas, Date fechaCierre, AppDemeter app)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.fechaCierre = fechaCierre;
		this.facturas = facturas;
		this.app = app;
		inicializar();
	}

	public UIDebitoInterno(DebitoInterno modeloIn, String titulo, int ancho,
			AppDemeter app) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.app = app;
		setModelo(modeloIn);
	}

	public void inicializar() {
		control = new Textbox();
		sede = new Textbox();
		fecha = new Datebox();
		txtUsuario = new Textbox();
		razonSocial = new Textbox();
		concepto = new Textbox();
		concepto.setMultiline(true);
		saldo = new Doublebox();
		cliente = new CompGrupoDatos("Datos Generales", 2);
		encrecibo = new CompGrupoDatos("Datos Debito Interno", 4);
		factura = new CompBuscar<DocumentoFiscal>(getEncabezadoFactura(), 0);
		estado = new Textbox();
		saldoInicial = new Label();
		total = new Doublebox();

		control.setDisabled(true);
		sede.setDisabled(true);
		estado.setDisabled(true);
		txtUsuario.setDisabled(true);
		saldo.setDisabled(true);
		total.setDisabled(true);
		razonSocial.setDisabled(true);
		fecha.setDisabled(true);
		factura.setModelo(facturas);
		botonAutorizar = new Button("Autorizar");

	}

	public void dibujar() {
		razonSocial.setWidth("650px");
		factura.setAncho(740);
		concepto.setWidth("650px");
		concepto.setMaxlength(30);
		sede.setWidth("250px");
		txtUsuario.setWidth("250px");
		saldo.setWidth("200px");
		total.setWidth("200px");
		estado.setWidth("280px");
		encrecibo.addComponente("Nro Control  :", control);
		encrecibo.addComponente("Fecha :", fecha);
		cliente.addComponente("Nro factura: ", factura);
		cliente.addComponente("Saldo factura:", saldoInicial);
		cliente.addComponente("cliente:", razonSocial);
		cliente.addComponente("Concepto:", concepto);
		cliente.addComponente("Sede  :", sede);
		encrecibo.addComponente("Monto: ", total);
		encrecibo.addComponente("Saldo:", saldo);
		encrecibo.addComponente(botonAutorizar);
		cliente.dibujar(this);
		encrecibo.dibujar(this);
		txtUsuario.setText(app.getdatosUsuario());
		total.setFormat("##,###,###,##0.00");
		saldo.setFormat("##,###,###,##0.00");
		factura.setListenerEncontrar(getControlador());
		total.addEventListener(Events.ON_CHANGE, getControlador());
		botonAutorizar.addEventListener(Events.ON_CLICK, getControlador());
		this.addBotonera();
	}

	public void cargarValores(DebitoInterno recibo) throws ExcDatosInvalidos {
		control.setValue(getModelo().getControl());
		getBinder().addBinding(factura, "seleccion",
				getNombreModelo() + ".documento", null, null, "save", null,
				null, null, null);
		try {
			factura.setSeleccion(getModelo().getDocumento());
			razonSocial.setValue(getModelo().getStrPagador());
			saldo.setValue(getModelo().getSaldoFactura());
			factura.setValue(getModelo().getStrFactura());
		} catch (NullPointerException e) {

		}
		factura.setConstraint("no empty : Factura no valida");
		fecha.setValue(getModelo().getFecha());
		estado.setValue(getModelo().getEstado());
		concepto.setValue(getModelo().getConcepto());
		getBinder().addBinding(concepto, "value",
				getNombreModelo() + ".concepto", null, null, "save", null,
				null, null, null);
		total.setValue(getModelo().getMonto());
		getBinder().addBinding(total, "value", getNombreModelo() + ".monto",
				null, null, "save", null, null, null, null);
		total.setConstraint("no negative : Monto no valido");
		concepto.setConstraint("no empty : Concepto no valido");
		concepto.setConstraint("no empty : Concepto no valido");
	}

	public Label getSaldoInicial() {
		return saldoInicial;
	}

	public void setSaldoInicial(Label saldoInicial) {
		this.saldoInicial = saldoInicial;
	}

	private List<CompEncabezado> getEncabezadoFactura() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Nro", 60);
		titulo.setMetodoBinder("getStrNroDocumento");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("fecha", 60);
		titulo.setMetodoBinder("getStrFecha");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Razon Social", 350);
		titulo.setMetodoBinder("getNombreBeneficiario");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Saldo", 80);
		titulo.setMetodoBinder("getMontoSaldo");
		titulo.setOrdenable(true);
		encabezado.add(titulo);
		return encabezado;
	}

	public CompBuscar<DocumentoFiscal> getFactura() {
		return factura;
	}

	public Textbox getSede() {
		return sede;
	}

	public Doublebox getTotal() {
		return total;
	}

	public Doublebox getSaldo() {
		return saldo;
	}

	public Textbox getRazonSocial() {
		return razonSocial;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		factura.setDisabled(true);
		concepto.setDisabled(true);
		total.setDisabled(true);
		botonAutorizar.setVisible(false);

	}

	public void modoEdicion() {
		factura.setDisabled(false);
		concepto.setDisabled(false);
		total.setDisabled(false);
	}

	public Button getBotonAutorizar() {
		return botonAutorizar;
	}

	public void setBotonAutorizar(Button botonAutorizar) {
		this.botonAutorizar = botonAutorizar;
	}

	public Textbox getControl() {
		return control;
	}

	public void setControl(Textbox control) {
		this.control = control;
	}

	public Textbox getConcepto() {
		return concepto;
	}

	public void setConcepto(Textbox concepto) {
		this.concepto = concepto;
	}

}