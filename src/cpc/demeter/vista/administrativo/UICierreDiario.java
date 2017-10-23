package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Transient;

import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Textbox;

import cpc.modelo.demeter.administrativo.CierreDiario;
import cpc.modelo.demeter.administrativo.CierreDiarioAsiento;
import cpc.modelo.demeter.administrativo.CierreDiarioCuentaAdelanto;
import cpc.modelo.demeter.administrativo.CierreDiarioCuentaCobrar;
import cpc.modelo.demeter.administrativo.CierreDiarioCuentaPagar;
import cpc.modelo.demeter.administrativo.CierreDiarioDocumento;
import cpc.modelo.demeter.administrativo.CierreDiarioReversoRecibo;
import cpc.modelo.demeter.administrativo.Deposito;
import cpc.modelo.demeter.administrativo.FormaPago;
import cpc.modelo.demeter.administrativo.ReversoRecibo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UICierreDiario extends CompVentanaBase<CierreDiario> {

	private static final long serialVersionUID = 6312240548253365129L;

	private Datebox fecha;
	
	private CompGrupoDatos gbControl, gbFacturas, gbCaja, gbCuentasCobrar,
			gbCuentasCobradas, gbDepositos, gbAsientos, gbObservacion,
			gbCuentasAdelantos;
	
	private Doublebox ingresoFactura, impuestoFactura, cuentasCobrar,
			montoRecibo, cuentasPagar, montoDeposito, montoAdelanto;
	
	private Textbox observacion, sede;

	private CompLista<CierreDiarioDocumento> detalleDocumento;
	
	private CompLista<CierreDiarioCuentaCobrar> detalleCuentasCobrar;
	private CompLista<CierreDiarioCuentaPagar> detalleCuentasPagas;
	private CompLista<CierreDiarioCuentaAdelanto> detalleAdelantos;
	
	private CompLista<Deposito> detalleDeposito;
	
	private CompLista<FormaPago> detalleOtrosIngresos;
	private List<FormaPago> pagoCaja;

	private CompLista<CierreDiarioAsiento> asiento;
	
	private CompGrupoDatos gbReversosRecibos;
	
	private CompLista<CierreDiarioReversoRecibo> detalleReversos;

	public UICierreDiario(String titulo, List<FormaPago> pagoCaja) {
		super(titulo, 720);
		this.pagoCaja = pagoCaja;
	}

	public void inicializar() {
		gbObservacion = new CompGrupoDatos("", 2);
		gbObservacion.setWidth("700px");

		gbControl = new CompGrupoDatos("Numeros de Control", 4);
		gbControl.setWidth("700px");

		gbFacturas = new CompGrupoDatos("Facturas", 1);
		gbFacturas.setWidth("700px");

		gbCaja = new CompGrupoDatos("Ingresos en Caja", 1);
		gbCaja.setWidth("700px");

		gbCuentasCobrar = new CompGrupoDatos("Cuentas Por Cobrar", 1);
		gbCuentasCobrar.setWidth("700px");

		gbCuentasAdelantos = new CompGrupoDatos("Adelantos", 1);
		gbCuentasAdelantos.setWidth("700px");

		gbCuentasCobradas = new CompGrupoDatos("Cuentas por pagar", 1);
		gbCuentasCobradas.setWidth("700px");

		gbDepositos = new CompGrupoDatos("Depositos", 1);
		gbDepositos.setWidth("700px");

		gbAsientos = new CompGrupoDatos("Asientos Contable", 1);
		gbAsientos.setWidth("700px");
		sede = new Textbox();
		observacion = new Textbox();
		ingresoFactura = new Doublebox();
		impuestoFactura = new Doublebox();
		cuentasCobrar = new Doublebox();
		montoRecibo = new Doublebox();
		cuentasPagar = new Doublebox();
		montoDeposito = new Doublebox();
		montoAdelanto = new Doublebox();
		fecha = new Datebox();
		
		detalleDocumento = new CompLista<CierreDiarioDocumento>(encabezadosDocumentos());
		detalleCuentasCobrar = new CompLista<CierreDiarioCuentaCobrar>(encabezadosCuentasCobrar());
		detalleAdelantos = new CompLista<CierreDiarioCuentaAdelanto>(encabezadosCuentasAdelantos());
		detalleCuentasPagas = new CompLista<CierreDiarioCuentaPagar>(encabezadosCuentasPagas());
		detalleDeposito = new CompLista<Deposito>(encabezadosDepositos());
		detalleOtrosIngresos = new CompLista<FormaPago>(encabezadosRecibos());
		asiento = new CompLista<CierreDiarioAsiento>(encabezadosAsientos());

		sede.setDisabled(true);
		ingresoFactura.setDisabled(true);
		impuestoFactura.setDisabled(true);
		cuentasCobrar.setDisabled(true);
		montoRecibo.setDisabled(true);
		cuentasPagar.setDisabled(true);
		montoAdelanto.setDisabled(true);
		
		fecha.setDisabled(true);
		montoDeposito.setDisabled(true);
		
		gbReversosRecibos= new CompGrupoDatos("Reverso", 1);
		detalleReversos = new CompLista<CierreDiarioReversoRecibo>(getencabezadoRecibos());
		
		
	}

	

	public void dibujar() {
		observacion.setWidth("550px");

		gbControl.addComponente("Sede :", sede);
		gbControl.addComponente("Fecha :", fecha);
		gbControl.addComponente("Monto Facturado :", ingresoFactura);
		/*
		 * gbControl.addComponente("Monto Recaudado :",montoRecibo);
		 * gbControl.addComponente("Monto Impuesto Generado :",impuestoFactura);
		 * gbControl.addComponente("Monto CuentasxCobrar :",cuentasCobrar);
		 * gbControl.addComponente("Monto CuentasxPagar :",cuentasPagar);
		 * gbControl.addComponente("Monto Depositado :",montoDeposito);
		 * gbControl.addComponente("Monto Adelantos :",montoAdelanto);
		 */
		gbControl.dibujar(this);
		gbObservacion.addComponente("Observacion :", observacion);
		gbObservacion.dibujar(this);

		gbFacturas.addComponente(detalleDocumento);
		gbDepositos.addComponente(detalleDeposito);
		gbCuentasCobrar.addComponente(detalleCuentasCobrar);
		gbCuentasCobradas.addComponente(detalleCuentasPagas);
		gbCuentasAdelantos.addComponente(detalleAdelantos);
		gbCaja.addComponente(detalleOtrosIngresos);
		gbCaja.addComponente(detalleOtrosIngresos);
		gbAsientos.addComponente(asiento);
		gbFacturas.dibujar(this);
		gbCuentasCobrar.dibujar(this);
		gbCuentasCobradas.dibujar(this);
		gbCuentasAdelantos.dibujar(this);
		gbCaja.dibujar(this);
		gbDepositos.dibujar(this);
		gbAsientos.dibujar(this);
		gbReversosRecibos.addComponente(detalleReversos);
		gbReversosRecibos.dibujar(this);
		addBotonera();
	}

	@Override
	public void cargarValores(CierreDiario arg0) throws ExcDatosInvalidos {
		fecha.setValue(getModelo().getFecha());
		cuentasCobrar.setValue(getModelo().getCuentasPorCobrar());
		cuentasPagar.setValue(getModelo().getCuentasPorPagar());
		impuestoFactura.setValue(getModelo().getImpuestoFacturado());
		ingresoFactura.setValue(getModelo().getIngresoFacturado());
		montoDeposito.setValue(getModelo().getMontoDepositado());
		montoRecibo.setValue(getModelo().getMontoRecibos());
		observacion.setValue(getModelo().getObservacion());
		montoAdelanto.setValue(getModelo().getMontoAdelantos());
		getBinder().addBinding(observacion, "value",
				getNombreModelo() + ".observacion", null, null, "save", null,
				null, null, null);

		// cargarDetalle(detalleDocumento, listaComponenteDocumentos(),
		// encabezadosDocumentos(), getModelo().getDocumentos());
		detalleDocumento.setModelo(getModelo().getDocumentos());

		detalleCuentasCobrar.setModelo(getModelo().getCuentasCobrar());
		detalleCuentasPagas.setModelo(getModelo().getCuentasPagadas());

		// detalleDeposito.setModelo(getModelo().getDepositos());
		detalleOtrosIngresos.setModelo(pagoCaja);
		detalleAdelantos.setModelo(getModelo().getCuentasAdelantos());
		if (getModelo().getReversos()!=null)
		detalleReversos.setModelo(getModelo().getReversos());
		// detalleDocumento.setModelo(getModelo().getDocumentos());

		/*
		 * cargarDetalle(detalleAsiento, listaComponenteCuentas(),
		 * encabezadosCuentas(), null);
		 */
	}

	private List<CompEncabezado> encabezadosDocumentos() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Factura", 130);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrDocumento");
		// titulo.setMetodoModelo(".tipoFormaPago");

		encabezado.add(titulo);
		titulo = new CompEncabezado("Ingreso Bruto", 115);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrCSBruto");
		titulo.setAlineacion(CompEncabezado.DERECHA);

		encabezado.add(titulo);
		titulo = new CompEncabezado("Descuento", 115);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrDescuento");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		// titulo.setMetodoModelo(".cuenta");

		encabezado.add(titulo);
		titulo = new CompEncabezado("Impuesto", 115);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrImpuesto");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		// titulo.setMetodoModelo(".documento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Ingreso Neto", 115);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrCSNeto");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> encabezadosCuentasCobrar() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Cuenta", 160);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrCuenta");

		encabezado.add(titulo);
		titulo = new CompEncabezado("Cliente", 250);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrCliente");

		encabezado.add(titulo);
		titulo = new CompEncabezado("Cobrar", 120);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrCobrar");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cobrado", 120);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrCobrado");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> encabezadosCuentasAdelantos() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Cliente", 350);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrCliente");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cobrado", 120);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrCobrado");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> encabezadosCuentasPagas() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Cuenta", 160);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrCuenta");

		encabezado.add(titulo);
		titulo = new CompEncabezado("Cliente", 250);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrCliente");

		encabezado.add(titulo);
		titulo = new CompEncabezado("Pagar", 120);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrPagar");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Pagado", 120);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrPagada");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> encabezadosRecibos() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Tipo pago", 120);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrTipoFormaPago");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Banco", 150);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrBanco");

		encabezado.add(titulo);
		titulo = new CompEncabezado("Nro Cuenta", 120);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getCuenta");

		encabezado.add(titulo);
		titulo = new CompEncabezado("Nro documento", 120);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getDocumento");

		encabezado.add(titulo);
		titulo = new CompEncabezado("Monto", 100);
		titulo.setMetodoComponente("value");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getStrMonto");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 100);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrFecha");
		// encabezado.add(titulo);
		return encabezado;
	}

	private List<CompEncabezado> encabezadosAsientos() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Cuenta", 120);
		titulo.setMetodoComponente("value");
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrCuenta");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Renglon", 250);
		titulo.setMetodoComponente("value");
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getRenglon");

		encabezado.add(titulo);
		titulo = new CompEncabezado("DEBE", 100);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setOrdenable(true);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrDebe");

		encabezado.add(titulo);
		titulo = new CompEncabezado("HABER", 100);
		titulo.setMetodoComponente("value");
		titulo.setOrdenable(true);
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getStrHaber");
		encabezado.add(titulo);
		// encabezado.add(titulo);
		return encabezado;
	}

	private List<CompEncabezado> encabezadosDepositos() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Banco", 140);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getBancoCuenta");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cuenta", 120);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrCuenta");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Concepto", 145);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getConcepto");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto", 85);
		titulo.setMetodoComponente("value");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		titulo.setMetodoBinder("getStrMontoTotal");
		encabezado.add(titulo);

		return encabezado;
	}

	public Doublebox getIngresoFactura() {
		return ingresoFactura;
	}

	public void setIngresoFactura(Doublebox ingresoFactura) {
		this.ingresoFactura = ingresoFactura;
	}

	public Doublebox getImpuestoFactura() {
		return impuestoFactura;
	}

	public void setImpuestoFactura(Doublebox impuestoFactura) {
		this.impuestoFactura = impuestoFactura;
	}

	public Doublebox getCuentasCobrar() {
		return cuentasCobrar;
	}

	public void setCuentasCobrar(Doublebox cuentasCobrar) {
		this.cuentasCobrar = cuentasCobrar;
	}

	public Doublebox getMontoRecibo() {
		return montoRecibo;
	}

	public void setMontoRecibo(Doublebox montoRecibo) {
		this.montoRecibo = montoRecibo;
	}

	public Doublebox getCuentasPagar() {
		return cuentasPagar;
	}

	public void setCuentasPagar(Doublebox cuentasPagar) {
		this.cuentasPagar = cuentasPagar;
	}

	public Doublebox getMontoDeposito() {
		return montoDeposito;
	}

	public void setMontoDeposito(Doublebox montoDeposito) {
		this.montoDeposito = montoDeposito;
	}

	public CompLista<Deposito> getDetalleDeposito() {
		return detalleDeposito;
	}

	public Textbox getSede() {
		return sede;
	}

	public CompLista<CierreDiarioAsiento> getAsiento() {
		return asiento;
	}
	private List<CompEncabezado> getencabezadoRecibos() {
		// TODO Auto-generated method stub
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		
		titulo = new CompEncabezado("Nro Control", 140);
		titulo.setMetodoBinder("getStrControlReverso");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Fecha", 140);
		titulo.setMetodoBinder("getStrFechaReverso");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Recibo Afectado", 140);
		titulo.setMetodoBinder("getStrReciboAfectado");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Monto", 140);
		titulo.setMetodoBinder("getStrMontoReversado");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Estado", 140);
		titulo.setMetodoBinder("getStrEstadoReverso");
		encabezado.add(titulo);
		
		return encabezado;
		
	
		
	}

}