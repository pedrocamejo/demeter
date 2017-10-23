package cpc.demeter.vista.administrativo;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Longbox;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.ControlSede;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiControlSede extends CompVentanaBase<ControlSede> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739695024627594930L;
	// constraint="/.+@.+\.[a-z]+ /: correo no valido" />

	private CompGrupoDatos gbControl, gbCuentas, gbGeneral, gbDocumentos,
			gbPrefijos, grpDatosContrato;

	private Textbox sede, cuentaClienteCobro, cuentaIngresos,
			cuentaPresupuestaria, mascaraCliente, cuentaPagarCliente,
			cuentaDescuento, cuentaImpuesto, cuentaCajaPrincipal;
	private Textbox serie, prefijoEntrada, prefijoSalida, prefijoTransferencia,
			prefijoContrato, txtAbogado, txtCedulaCoordinador,
			txtCoordinadorSede, txtDireccionSede, txtImpreAbogado;
	private Intbox controlFactura, controlCliente, controlContrato;
	private Intbox controltransferencia, controlEntrada, controlSalida;
	private Intbox nroFactura, controlNCredito;
	private Intbox controlNDebito;
	private Intbox nroDocumento;
	private Checkbox documentoUnico;
	private Intbox nroNCredito, nroNDebito;
	private Longbox controlRecibo;
	private Doublebox PorcentajeDescuento;
	private Datebox ultimoCierre;

	// private Button aceptar, cancelar;
	// private DataBinder binder;

	public UiControlSede(String titulo, int ancho) throws ExcDatosInvalidos {
		super(titulo, ancho);
	}

	public UiControlSede(String titulo) throws ExcDatosInvalidos {
		super(titulo);

	}

	public void inicializar() {
		gbDocumentos = new CompGrupoDatos("Documentos pre-Impresos", 4);
		gbPrefijos = new CompGrupoDatos("Prefijos", 4);
		gbControl = new CompGrupoDatos("Numeros de Control", 4);
		gbCuentas = new CompGrupoDatos("Cuentas", 2);
		gbGeneral = new CompGrupoDatos("Informacion General", 2);
		grpDatosContrato = new CompGrupoDatos("Datos para El Contrato", 2);

		documentoUnico = new Checkbox("");

		sede = new Textbox();
		serie = new Textbox();
		prefijoContrato = new Textbox();
		prefijoEntrada = new Textbox();
		prefijoSalida = new Textbox();
		prefijoTransferencia = new Textbox();

		cuentaClienteCobro = new Textbox();
		cuentaPagarCliente = new Textbox();
		cuentaIngresos = new Textbox();
		cuentaDescuento = new Textbox();
		cuentaImpuesto = new Textbox();
		cuentaCajaPrincipal = new Textbox();

		cuentaPresupuestaria = new Textbox();
		mascaraCliente = new Textbox();

		controlFactura = new Intbox();
		nroFactura = new Intbox();
		controlNCredito = new Intbox();
		controlNDebito = new Intbox();
		nroNCredito = new Intbox();
		nroNDebito = new Intbox();
		nroDocumento = new Intbox();

		controlRecibo = new Longbox();
		controlCliente = new Intbox();
		controlContrato = new Intbox();
		controlEntrada = new Intbox();
		controlSalida = new Intbox();
		controltransferencia = new Intbox();

		PorcentajeDescuento = new Doublebox();
		ultimoCierre = new Datebox();

		txtAbogado = new Textbox();
		txtCedulaCoordinador = new Textbox();
		txtCoordinadorSede = new Textbox();
		txtDireccionSede = new Textbox();
		txtImpreAbogado = new Textbox();

		sede.setWidth("240px");
		serie.setWidth("50px");
		cuentaClienteCobro.setWidth("200px");
		cuentaIngresos.setWidth("200px");
		cuentaImpuesto.setWidth("200px");
		cuentaDescuento.setWidth("200px");

		cuentaPresupuestaria.setWidth("200px");
		cuentaPagarCliente.setWidth("200px");
		cuentaCajaPrincipal.setWidth("200px");

		mascaraCliente.setWidth("120px");
		prefijoContrato.setWidth("100px");
		prefijoEntrada.setWidth("100px");
		prefijoSalida.setWidth("100px");
		prefijoTransferencia.setWidth("100px");

		nroFactura.setWidth("100px");
		controlNCredito.setWidth("100px");
		controlNDebito.setWidth("100px");
		nroNCredito.setWidth("100px");
		nroNDebito.setWidth("100px");

		controlFactura.setWidth("100px");
		controlRecibo.setWidth("100px");
		controlCliente.setWidth("100px");
		PorcentajeDescuento.setWidth("100px");
		PorcentajeDescuento.setDisabled(true);
		documentoUnico.addEventListener(Events.ON_CHECK, getControlador());

		txtDireccionSede.setRows(3);
		txtDireccionSede.setMaxlength(120);
		txtDireccionSede.setWidth("250px");
	}

	public void dibujar() {
		gbCuentas.setAnchoColumna(0, 300);
		gbGeneral.setAnchoColumna(0, 250);

		gbGeneral.addComponente("Sede", sede);
		gbGeneral.addComponente("Direccion Sede", txtDireccionSede);

		gbGeneral.addComponente("Ultimo Cierre", ultimoCierre);
		gbGeneral.addComponente("Porcentaje descuento", PorcentajeDescuento);
		gbGeneral.dibujar(this);

		gbControl.addComponente("Control factura:", controlFactura);
		gbControl.addComponente("Control Nota Credito:", controlNCredito);
		gbControl.addComponente("Control Nota Debito:", controlNDebito);
		gbControl.addComponente("Control Recibo :", controlRecibo);
		gbControl.addComponente("Control Cliente :", controlCliente);
		gbControl.addComponente("Control Contrato:", controlContrato);
		gbControl.addComponente("Control Entrada Activo:", controlEntrada);
		gbControl.addComponente("Control Salida Activo:", controlSalida);
		gbControl.addComponente("Control Transferencia Activo:",
				controltransferencia);
		gbControl.dibujar(this);
		gbPrefijos.addComponente("Serie Documento Fiscal", serie);
		gbPrefijos.addComponente("Contratos", prefijoContrato);
		gbPrefijos.addComponente("Entrada Activos", prefijoEntrada);
		gbPrefijos.addComponente("Salida Activos", prefijoSalida);
		gbPrefijos.addComponente("Transferencia Activos", prefijoTransferencia);
		gbPrefijos.addComponente("Mascara Cliente :", mascaraCliente);
		gbPrefijos.dibujar(this);
		gbDocumentos.addComponente("Documento unico", documentoUnico);
		gbDocumentos.addComponente("Documento comun :", nroDocumento);
		gbDocumentos.addComponente("Documento Factura :", nroFactura);
		gbDocumentos.addComponente("Documento Nota Credito :", nroNCredito);
		gbDocumentos.addComponente("Documento Nota Debito :", nroNDebito);
		gbDocumentos.dibujar(this);

		// gbCuentas.addComponente( "Contable Ingresos", cuentaIngresos);
		gbCuentas.addComponente("Contable Impuestos", cuentaImpuesto);
		// gbCuentas.addComponente( "Contable Descuentos", cuentaDescuento);
		gbCuentas.addComponente("Contable Caja", cuentaCajaPrincipal);

		// gbCuentas.addComponente( "Presupuestaria",cuentaPresupuestaria);
		gbCuentas.addComponente("Cliente Cuentas Cobrar", cuentaClienteCobro);
		gbCuentas.addComponente("Cliente Cuentas Pagar", cuentaPagarCliente);
		gbCuentas.dibujar(this);

		grpDatosContrato.addComponente("Abogado", txtAbogado);
		grpDatosContrato.addComponente("Impreabogado", txtImpreAbogado);
		grpDatosContrato.addComponente("Cedula del Coordinador",
				txtCedulaCoordinador);
		grpDatosContrato.addComponente("Coordinador", txtCoordinadorSede);
		grpDatosContrato.dibujar(this);

		addBotonera();

	}

	@Override
	public void cargarValores(ControlSede arg0) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		sede.setValue(getModelo().getStrSede());
		sede.setDisabled(true);

		nroDocumento.setValue(getModelo().getNroDocumento());
		getBinder().addBinding(nroDocumento, "value",
				getNombreModelo() + ".nroDocumento", null, null, "save", null,
				null, null, null);
		prefijoContrato.setValue(getModelo().getPrefijo());
		getBinder().addBinding(prefijoContrato, "value",
				getNombreModelo() + ".prefijo", null, null, "save", null, null,
				null, null);
		prefijoEntrada.setValue(getModelo().getPrefijoRecepcionActivo());
		getBinder().addBinding(prefijoEntrada, "value",
				getNombreModelo() + ".prefijoRecepcionActivo", null, null,
				"save", null, null, null, null);
		prefijoSalida.setValue(getModelo().getPrefijoSalidaActivo());
		getBinder().addBinding(prefijoSalida, "value",
				getNombreModelo() + ".prefijoSalidaActivo", null, null, "save",
				null, null, null, null);
		prefijoTransferencia.setValue(getModelo()
				.getPrefijoTransferenciaActivo());
		getBinder().addBinding(prefijoTransferencia, "value",
				getNombreModelo() + ".prefijoTransferenciaActivo", null, null,
				"save", null, null, null, null);
		if (getModelo().getControlUnico() != null)
			documentoUnico.setChecked(getModelo().getControlUnico());
		getBinder().addBinding(documentoUnico, "checked",
				getNombreModelo() + ".controlUnico", null, null, "save", null,
				null, null, null);
		controlContrato.setValue(getModelo().getControlContrato());
		getBinder().addBinding(controlContrato, "value",
				getNombreModelo() + ".controlContrato", null, null, "save",
				null, null, null, null);
		controlEntrada.setValue(getModelo().getControlRecepcionActivo());
		getBinder().addBinding(controlEntrada, "value",
				getNombreModelo() + ".controlRecepcionActivo", null, null,
				"save", null, null, null, null);
		controlSalida.setValue(getModelo().getControlSalidaActivo());
		getBinder().addBinding(controlSalida, "value",
				getNombreModelo() + ".controlSalidaActivo", null, null, "save",
				null, null, null, null);
		controltransferencia.setValue(getModelo()
				.getControlTransferenciaActivo());
		getBinder().addBinding(controltransferencia, "value",
				getNombreModelo() + ".controlTransferenciaActivo", null, null,
				"save", null, null, null, null);

		serie.setValue(getModelo().getSerie());
		getBinder().addBinding(serie, "value", getNombreModelo() + ".serie",
				null, null, "save", null, null, null, null);
		cuentaClienteCobro.setValue(getModelo().getCuentaCLienteCobro());
		cuentaClienteCobro
				.setConstraint("/[0-9]+/: Valor no valido para cuenta");
		getBinder().addBinding(cuentaClienteCobro, "value",
				getNombreModelo() + ".cuentaCLienteCobro", null, null, "save",
				null, null, null, null);

		cuentaPagarCliente.setValue(getModelo().getCuentaCLientePago());
		cuentaPagarCliente
				.setConstraint("/[0-9]+/: Valor no valido para cuenta");
		getBinder().addBinding(cuentaPagarCliente, "value",
				getNombreModelo() + ".cuentaCLientePago", null, null, "save",
				null, null, null, null);

		cuentaIngresos.setValue(getModelo().getCuentaContableIngresosSede());
		cuentaIngresos.setConstraint("/[0-9]+/: Valor no valido para cuenta");
		getBinder().addBinding(cuentaIngresos, "value",
				getNombreModelo() + ".cuentaContableIngresosSede", null, null,
				"save", null, null, null, null);
		cuentaPresupuestaria.setValue(getModelo()
				.getCuentaPresupuestariaIngresosSede());
		cuentaPresupuestaria
				.setConstraint("/[0-9]+/: Valor no valido para cuenta");
		getBinder().addBinding(cuentaPresupuestaria, "value",
				getNombreModelo() + ".cuentaPresupuestariaIngresosSede", null,
				null, "save", null, null, null, null);
		mascaraCliente.setValue(getModelo().getMascaraCliente());
		mascaraCliente.setConstraint("/[0]+/: Valor no valido para mascara");
		getBinder().addBinding(mascaraCliente, "value",
				getNombreModelo() + ".mascaraCliente", null, null, "save",
				null, null, null, null);
		controlFactura.setValue(getModelo().getControlFactura());
		getBinder().addBinding(controlFactura, "value",
				getNombreModelo() + ".controlFactura", null, null, "save",
				null, null, null, null);

		nroFactura.setValue(getModelo().getNroFactura());
		getBinder().addBinding(nroFactura, "value",
				getNombreModelo() + ".nroFactura", null, null, "save", null,
				null, null, null);

		controlNCredito.setValue(getModelo().getControlNotaCredito());
		getBinder().addBinding(controlNCredito, "value",
				getNombreModelo() + ".controlNotaCredito", null, null, "save",
				null, null, null, null);

		controlNDebito.setValue(getModelo().getControlNotaDebito());
		getBinder().addBinding(controlNDebito, "value",
				getNombreModelo() + ".controlNotaDebito", null, null, "save",
				null, null, null, null);

		nroNCredito.setValue(getModelo().getNroNotaCredito());
		getBinder().addBinding(nroNCredito, "value",
				getNombreModelo() + ".nroNotaCredito", null, null, "save",
				null, null, null, null);

		nroNDebito.setValue(getModelo().getNroNotaDebito());
		getBinder().addBinding(nroNDebito, "value",
				getNombreModelo() + ".nroNotaDebito", null, null, "save", null,
				null, null, null);

		controlRecibo.setValue(getModelo().getControlRecibo());
		getBinder().addBinding(controlRecibo, "value",
				getNombreModelo() + ".controlRecibo", null, null, "save", null,
				null, null, null);
		controlCliente.setValue(getModelo().getControlCliente());
		getBinder().addBinding(controlCliente, "value",
				getNombreModelo() + ".controlCliente", null, null, "save",
				null, null, null, null);
		PorcentajeDescuento.setValue(getModelo().getPorcentajeDescuento());
		getBinder().addBinding(PorcentajeDescuento, "value",
				getNombreModelo() + ".porcentajeDescuento", null, null, "save",
				null, null, null, null);
		ultimoCierre.setValue(getModelo().getFechaCierreFactura());
		if (getModelo().getFechaCierreFactura() != null)
			cuentaClienteCobro.setDisabled(true);

		cuentaCajaPrincipal.setValue(getModelo().getCuentaContableCaja());
		getBinder().addBinding(cuentaCajaPrincipal, "value",
				getNombreModelo() + ".cuentaContableCaja", null, null, "save",
				null, null, null, null);

		cuentaImpuesto.setValue(getModelo().getCuentaContableImpuestoSede());
		getBinder().addBinding(cuentaImpuesto, "value",
				getNombreModelo() + ".cuentaContableImpuestoSede", null, null,
				"save", null, null, null, null);

		getBinder().addBinding(ultimoCierre, "value",
				getNombreModelo() + ".fechaCierreFactura", null, null, "save",
				null, null, null, null);

		txtAbogado.setValue(getModelo().getAbogado());
		getBinder().addBinding(txtAbogado, "value",
				getNombreModelo() + ".abogado", null, null, "save", null, null,
				null, null);

		txtImpreAbogado.setValue(getModelo().getImpreAbogado());
		getBinder().addBinding(txtImpreAbogado, "value",
				getNombreModelo() + ".impreAbogado", null, null, "save", null,
				null, null, null);

		txtCedulaCoordinador.setValue(getModelo().getCedulaCoordinador());
		getBinder().addBinding(txtCedulaCoordinador, "value",
				getNombreModelo() + ".cedulaCoordinador", null, null, "save",
				null, null, null, null);

		txtCoordinadorSede.setValue(getModelo().getNombreCoordinadorSede());
		getBinder().addBinding(txtCoordinadorSede, "value",
				getNombreModelo() + ".nombreCoordinadorSede", null, null,
				"save", null, null, null, null);

		txtDireccionSede.setValue(getModelo().getDireccionSede());
		getBinder().addBinding(txtDireccionSede, "value",
				getNombreModelo() + ".direccionSede", null, null, "save", null,
				null, null, null);

	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarmodo() {
		boolean modo = documentoUnico.isChecked();
		nroFactura.setDisabled(modo);
		nroNCredito.setDisabled(modo);
		nroNDebito.setDisabled(modo);
		nroDocumento.setDisabled(!modo);
	}

	public void activarConsulta() {
		serie.setDisabled(true);
		cuentaClienteCobro.setDisabled(true);
		cuentaPagarCliente.setDisabled(true);
		cuentaIngresos.setDisabled(true);
		cuentaDescuento.setDisabled(true);
		cuentaImpuesto.setDisabled(true);
		cuentaCajaPrincipal.setDisabled(true);
		cuentaPresupuestaria.setDisabled(true);

		mascaraCliente.setDisabled(true);
		controlFactura.setDisabled(true);
		controlCliente.setDisabled(true);
		controlRecibo.setDisabled(true);

		controlContrato.setDisabled(true);
		controlEntrada.setDisabled(true);
		controlSalida.setDisabled(true);
		controltransferencia.setDisabled(true);
		controlNCredito.setDisabled(true);
		controlNDebito.setDisabled(true);

		prefijoContrato.setDisabled(true);
		prefijoEntrada.setDisabled(true);
		prefijoSalida.setDisabled(true);
		prefijoTransferencia.setDisabled(true);

		PorcentajeDescuento.setDisabled(true);
		ultimoCierre.setDisabled(true);

		cuentaClienteCobro.setDisabled(true);
		cuentaPagarCliente.setDisabled(true);

		nroFactura.setDisabled(true);
		nroNCredito.setDisabled(true);
		nroNDebito.setDisabled(true);
		nroDocumento.setDisabled(true);
	}

	public void modoEdicion() {
		serie.setDisabled(false);
		cuentaClienteCobro.setDisabled(true);
		cuentaPagarCliente.setDisabled(true);
		cuentaIngresos.setDisabled(true);
		cuentaDescuento.setDisabled(true);
		cuentaImpuesto.setDisabled(true);
		cuentaCajaPrincipal.setDisabled(true);
		cuentaPresupuestaria.setDisabled(true);
		controlRecibo.setDisabled(false);
		controlContrato.setDisabled(false);
		controlEntrada.setDisabled(false);
		controlSalida.setDisabled(false);
		controltransferencia.setDisabled(false);
		controlNCredito.setDisabled(false);
		controlNDebito.setDisabled(false);

		prefijoContrato.setDisabled(false);
		prefijoEntrada.setDisabled(false);
		prefijoSalida.setDisabled(false);
		prefijoTransferencia.setDisabled(false);

		mascaraCliente.setDisabled(false);
		controlFactura.setDisabled(false);
		controlCliente.setDisabled(false);
		PorcentajeDescuento.setDisabled(true);
		activarmodo();
	}

	public Datebox getUltimoCierre() {
		return ultimoCierre;
	}

	public void setUltimoCierre(Datebox ultimoCierre) {
		this.ultimoCierre = ultimoCierre;
	}

}
