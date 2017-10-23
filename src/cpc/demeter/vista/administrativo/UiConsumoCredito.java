package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.ConsumoCredito;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.NotaCredito;
import cpc.modelo.demeter.administrativo.NotaDebito;
import cpc.modelo.demeter.basico.TipoConsumo;
import cpc.persistencia.demeter.implementacion.administrativo.PerNotaDebito;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UiConsumoCredito extends CompVentanaBase<ConsumoCredito> {

	private static final long serialVersionUID = -5739695024627594930L;

	private CompGrupoDatos gbGeneral, gbConcepto;
	private CompBuscar<NotaCredito> nota;
	private CompBuscar<DocumentoFiscal> factura;
	private CompBuscar<NotaDebito> debito;
	private Textbox facturaTxt;
	private Textbox debitoFacturaTxt;
	private CompCombobox<TipoConsumo> tipoConsumo;

	private Label facturaLbl, saldoLbl;
	private Label debitoLbl, saldodebitoLbl, facturaafectada;

	private Datebox fecha;
	private Textbox concepto;
	private Doublebox montoNota;
	private Doublebox monto;
	private Doublebox montoSaldoFactura;
	private Doublebox montoSaldoDebito;
	private Doublebox saldoNota;

	private List<NotaCredito> notas;
	private List<TipoConsumo> tipos;
	private Date fechaCierre;
	private int modo;

	public UiConsumoCredito(String titulo, int ancho, List<NotaCredito> notas,
			List<TipoConsumo> tipos, Date fecha, int modo)
			throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.notas = notas;
		this.tipos = tipos;
		fechaCierre = fecha;
		this.modo = modo;
	}

	public UiConsumoCredito(String titulo) throws ExcDatosInvalidos {
		super(titulo);
	}

	@SuppressWarnings("unchecked")
	public void inicializar() {
		facturaLbl = new Label("Factura:");
		saldoLbl = new Label("Saldo factura:");
		debitoLbl = new Label("Nota Debito:");
		saldodebitoLbl = new Label("Saldo Debito:");
		facturaafectada = new Label("Factura Afectada");
		gbGeneral = new CompGrupoDatos("Datos generales", 4);
		gbConcepto = new CompGrupoDatos("", 2);
		nota = new CompBuscar<NotaCredito>(cargarNotas(), 0);
		montoNota = new Doublebox();
		montoSaldoDebito = new Doublebox();
		factura = new CompBuscar<DocumentoFiscal>(cargarFacturas(), 0);
		facturaTxt = new Textbox();
		debito = new CompBuscar<NotaDebito>(cargarDebitos(), 0);
		debitoFacturaTxt = new Textbox();
		tipoConsumo = new CompCombobox<TipoConsumo>();
		fecha = new Datebox(fechaCierre);
		monto = new Doublebox();
		saldoNota = new Doublebox();
		montoSaldoFactura = new Doublebox();
		concepto = new Textbox();

		monto.setWidth("100px");
		montoSaldoFactura.setWidth("100px");
		montoSaldoFactura.setDisabled(true);

		nota.setWidth("120px");
		factura.setWidth("120px");

		nota.setAncho(590);
		factura.setAncho(600);

		nota.setModelo(notas);
		tipoConsumo.setModelo(tipos);
		montoNota.setDisabled(true);
		fecha.setDisabled(true);
		saldoNota.setDisabled(true);
		montoSaldoFactura.setDisabled(true);

		tipoConsumo.addEventListener(Events.ON_SELECTION, getControlador());
		monto.addEventListener(Events.ON_CHANGE, getControlador());
		nota.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
		factura.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
		debito.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
		concepto.setWidth("350px");
	}

	public void dibujar() {
		// gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Nota:", nota);
		gbGeneral.addComponente("Saldo:", montoNota);
		gbGeneral.addComponente("Fecha:", fecha);
		gbGeneral.addComponente("Tipo:", tipoConsumo);
		gbGeneral.addComponente(facturaLbl);
		// if (modo == Accion.AGREGAR)
		// gbGeneral.addComponente(factura);
		// else
		// gbGeneral.addComponente(facturaTxt);
		gbGeneral.addComponente(factura);
		gbGeneral.addComponente(saldoLbl);
		gbGeneral.addComponente(montoSaldoFactura);

		gbGeneral.addComponente("Monto:", monto);
		gbGeneral.addComponente("Saldo Nota:", saldoNota);

		gbGeneral.addComponente(debitoLbl);
		gbGeneral.addComponente(debito);

		gbGeneral.addComponente(saldodebitoLbl);
		gbGeneral.addComponente(montoSaldoDebito);

		gbGeneral.addComponente(facturaafectada);
		gbGeneral.addComponente(debitoFacturaTxt);

		/*
		 * gbGeneral.addComponente("SALDO DEBITO", montoSaldoDebito);
		 * gbGeneral.addComponente("FACTURA AFECTADA", debitoFacturaTxt );
		 */

		gbGeneral.dibujar(this);
		gbConcepto.addComponente("Concepto : ", concepto);
		gbConcepto.dibujar(this);
		desactivarFactura();
		desactivarDebito();
		addBotonera();

	}

	@Override
	public void cargarValores(ConsumoCredito consumo) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		try {
			nota.setSeleccion(getModelo().getNota());
			if (getModelo().getNota() != null)
				nota.setValue(getModelo().getNota().getStrNroDocumento());
			getBinder().addBinding(nota, "seleccion",
					getNombreModelo() + ".nota", null, null, "save", null,
					null, null, null);

			tipoConsumo.setSeleccion(getModelo().getTipoConsumo());
			getBinder().addBinding(tipoConsumo, "seleccion",
					getNombreModelo() + ".tipoConsumo", null, null, "save",
					null, null, null, null);

			fecha.setValue(getModelo().getFecha());
			// getBinder().addBinding(fecha, "value",
			// getNombreModelo()+".fecha", null, null, "save", null, null, null,
			// null);

			monto.setValue(getModelo().getMonto());
			getBinder().addBinding(monto, "value",
					getNombreModelo() + ".monto", null, null, "save", null,
					null, null, null);

			// montoAbonado.setValue(consumo.getAbonado());

			concepto.setValue(getModelo().getConcepto());
			getBinder().addBinding(concepto, "value",
					getNombreModelo() + ".concepto", null, null, "save", null,
					null, null, null);

			if (consumo.getDocumento() != null) {
				if (consumo.getDocumento().getTipoDocumento().isTipoFactura()) {

					getBinder().addBinding(factura, "seleccion",
							getNombreModelo() + ".documento", null, null,
							"save", null, null, null, null);
					// facturaTxt.setText(getModelo().getDocumento().getStrNroDocumento());
					factura.setSeleccion(getModelo().getDocumento());
					factura.setValue(getModelo().getDocumento()
							.getStrNroDocumento());
					activarFactura();
				}
				if (!consumo.getDocumento().getTipoDocumento().isTipoFactura()
						&& !consumo.getDocumento().getTipoDocumento().isHaber()) {

					getBinder().addBinding(debito, "seleccion",
							getNombreModelo() + ".documento", null, null,
							"save", null, null, null, null);
					debito.setSeleccion((NotaDebito) getModelo().getDocumento());
					debito.setValue(getModelo().getDocumento()
							.getStrNroDocumento());
					activarDebito();
					debitoFacturaTxt.detach();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void activarFactura() {
		factura.setVisible(true);
		facturaTxt.setVisible(true);
		facturaLbl.setVisible(true);
		saldoLbl.setVisible(true);
		montoSaldoFactura.setVisible(true);
	}

	public void desactivarFactura() {
		factura.setVisible(false);
		facturaTxt.setVisible(false);
		facturaLbl.setVisible(false);
		saldoLbl.setVisible(false);
		montoSaldoFactura.setVisible(false);
	}

	public void desactivarDebito() {

		debitoLbl.setVisible(false);
		debito.setVisible(false);

		facturaafectada.setVisible(false);
		debitoFacturaTxt.setVisible(false);

		saldodebitoLbl.setVisible(false);
		montoSaldoDebito.setVisible(false);
	}

	public void activarDebito() {
		debito.setVisible(true);
		debitoFacturaTxt.setVisible(true);
		debitoFacturaTxt.setReadonly(true);
		debitoLbl.setVisible(true);
		saldodebitoLbl.setVisible(true);
		montoSaldoDebito.setVisible(true);
		facturaafectada.setVisible(true);
	}

	private List<CompEncabezado> cargarDebitos() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Nº Control", 60);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nº Nota Debito", 100);
		titulo.setOrdenable(true);
		// titulo.setAlineacion(CompEncabezado.CENTRO);
		titulo.setMetodoBinder("getStrNroDocumento");
		// titulo.setMetodoModelo("usuario.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		// titulo.setMetodoModelo("usuario.identidad.cedula");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cliente", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreBeneficiario");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Factura Afec", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFacturaAfectada");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto Total", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTotal");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Por pagar", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrSaldo");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);
		return encabezado;

	}

	@SuppressWarnings("unchecked")
	private List cargarFacturas() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("NroControl", 90);
		titulo.setOrdenable(true);
		// titulo.setAlineacion(CompEncabezado.CENTRO);
		titulo.setMetodoBinder("getStrNroDocumento");
		// titulo.setMetodoModelo("usuario.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		// titulo.setMetodoModelo("usuario.identidad.cedula");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cliente", 300);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreBeneficiario");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Saldo", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMontoSaldo");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);
		return encabezado;
	}

	@SuppressWarnings("unchecked")
	private List cargarNotas() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("NroControl", 100);
		titulo.setOrdenable(true);
		// titulo.setAlineacion(CompEncabezado.CENTRO);
		titulo.setMetodoBinder("getStrNroDocumento");
		// titulo.setMetodoModelo("usuario.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		// titulo.setMetodoModelo("usuario.identidad.cedula");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cliente", 300);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreBeneficiario");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMontoSaldo");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);
		return encabezado;
	}

	public CompBuscar<NotaCredito> getNota() {
		return nota;
	}

	public CompBuscar<DocumentoFiscal> getFactura() {
		return factura;
	}

	public CompCombobox<TipoConsumo> getTipoConsumo() {
		return tipoConsumo;
	}

	public Datebox getFecha() {
		return fecha;
	}

	public Textbox getConcepto() {
		return concepto;
	}

	public Doublebox getMontoNota() {
		return montoNota;
	}

	public Doublebox getMonto() {
		return monto;
	}

	public Doublebox getMontoSaldoFactura() {
		return montoSaldoFactura;
	}

	public Doublebox getSaldoNota() {
		return saldoNota;
	}

	public CompBuscar<NotaDebito> getDebito() {
		return debito;
	}

	public void setDebito(CompBuscar<NotaDebito> debito) {
		this.debito = debito;
	}

	public Label getDebitoLbl() {
		return debitoLbl;
	}

	public void setDebitoLbl(Label debitoLbl) {
		this.debitoLbl = debitoLbl;
	}

	public Label getSaldodebitoLbl() {
		return saldodebitoLbl;
	}

	public void setSaldodebitoLbl(Label saldodebitoLbl) {
		this.saldodebitoLbl = saldodebitoLbl;
	}

	public Doublebox getMontoSaldoDebito() {
		return montoSaldoDebito;
	}

	public void setMontoSaldoDebito(Doublebox montoSaldoDebito) {
		this.montoSaldoDebito = montoSaldoDebito;
	}

	public Textbox getDebitoFacturaTxt() {
		return debitoFacturaTxt;
	}

	public void setDebitoFacturaTxt(Textbox debitoFacturaTxt) {
		this.debitoFacturaTxt = debitoFacturaTxt;
	}

	public void modoConsulta() {

		nota.setDisabled(true);
		factura.setDisabled(true);
		debito.setDisabled(true);
		facturaTxt.setDisabled(true);
		debitoFacturaTxt.setDisabled(true);
		tipoConsumo.setDisabled(true);

		fecha.setDisabled(true);
		concepto.setDisabled(true);
		montoNota.setDisabled(true);
		monto.setDisabled(true);
		montoSaldoFactura.setDisabled(true);
		montoSaldoDebito.setDisabled(true);
		saldoNota.setDisabled(true);
	}

}
