package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.listas.CompListaMultiple;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UIClienteAdministrativo extends
		CompVentanaBase<ClienteAdministrativo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739695024627594930L;
	private CompListaMultiple usuarios;

	private Textbox nombre;
	private Textbox cedula;
	private Textbox cuentaPagar;
	private Textbox cuentaCobrar;
	private CompLista<DocumentoFiscal> detalleDeuda;
	private CompLista<DocumentoFiscal> detalleFavor;
	private CompLista<Recibo> detalleRecibos;
	private Doublebox montoDeuda;
	private Doublebox montoFavor;
	// private Button aceptar, cancelar;
	// private DataBinder binder;
	private int modo;
	private CompGrupoDatos gbdatos, gbControl, gbFacturas, gbNotas,gbRecibos;

	private List<DocumentoFiscal> documentosFavor;
	private List<DocumentoFiscal> documentosDeuda;
	private List<Recibo> recibosFavor;
	List<ClienteAdministrativo> clientes;
	private double saldoDeuda, saldoFavor;

	public UIClienteAdministrativo(String titulo, int ancho, int modo,
			List<ClienteAdministrativo> clientes) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.modo = modo;
		this.clientes = clientes;
	}

	public UIClienteAdministrativo(String titulo, int ancho, int modo,
			List<DocumentoFiscal> documentosFavor,
			List<DocumentoFiscal> documentosDeuda,List<Recibo> recibosFavor) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.modo = modo;
		this.documentosDeuda = documentosDeuda;
		this.documentosFavor = documentosFavor;
		this.recibosFavor    = recibosFavor;
	}

	public void inicializar() {
		if (modo == Accion.AGREGAR) {
			usuarios = new CompListaMultiple("Clientes");
			usuarios.setWidth("520px");
			usuarios.setHeight("115px");
			usuarios.setRows(8);
		} else {
			detalleDeuda = new CompLista<DocumentoFiscal>(
					encabezadosDocumentos());
			detalleFavor = new CompLista<DocumentoFiscal>(
					encabezadosDocumentos());
			detalleRecibos = new CompLista<Recibo>(encabezadosRecibos());
			montoFavor = new Doublebox();
			montoDeuda = new Doublebox();
			nombre = new Textbox();
			cedula = new Textbox();
			cuentaCobrar = new Textbox();
			cuentaPagar = new Textbox();
			gbControl = new CompGrupoDatos("General", 4);
			gbFacturas = new CompGrupoDatos("Detalle Cuentas por Cobrar", 1);
			gbNotas = new CompGrupoDatos("Detalle cuentas por Pagar", 1);
			gbRecibos = new CompGrupoDatos("Recibos a Favor", 1);
			gbdatos = new CompGrupoDatos("Datos Personales", 2);
			
			cedula.setWidth("150px");
			nombre.setWidth("350px");
			cuentaCobrar.setWidth("130px");
			cuentaPagar.setWidth("130px");
			cuentaCobrar.setDisabled(true);
			cuentaPagar.setDisabled(true);
			nombre.setDisabled(true);
			cedula.setDisabled(true);
			montoDeuda.setDisabled(true);
			montoFavor.setDisabled(true);
		}
	}

	private List<CompEncabezado> encabezadosRecibos() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Nro Control", 120);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getControl");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 100);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrMonto");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Saldo", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrSaldo");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		return encabezado;
		
	}

	private List<CompEncabezado> encabezadosDocumentos() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Tipo Documento", 120);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrTipoDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nro Control", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrNroDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 100);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getMontoTotal");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Saldo", 90);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getMontoSaldo");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		return encabezado;
	}

	private void calcularSaldos() {
		saldoDeuda = 0.0;
		for (DocumentoFiscal item : documentosDeuda) {
			saldoDeuda += item.getMontoSaldo();
		}
		saldoFavor = 0.0;
		for (DocumentoFiscal item : documentosFavor) {
			saldoFavor += Math.abs(item.getMontoSaldo());
		}
	}

	public void dibujar() {
		if (modo == Accion.AGREGAR) {
			this.appendChild(usuarios);

		} else {
			gbdatos.addComponente("Cedula/RIF:", cedula);
			gbdatos.addComponente("Razon Social:", nombre);
			gbControl.addComponente("Cuenta por cobrar:", cuentaCobrar);
			gbControl.addComponente("Monto Deudor", montoDeuda);
			gbControl.addComponente("Cuenta por Pagar", cuentaPagar);
			gbControl.addComponente("Monto a Favor", montoFavor);
			gbFacturas.addComponente(detalleDeuda);
			gbNotas.addComponente(detalleFavor);
			gbRecibos.addComponente(detalleRecibos);
			gbdatos.dibujar(this);
			gbControl.dibujar(this);
			gbFacturas.dibujar(this);
			gbNotas.dibujar(this);
			gbRecibos.dibujar(this);
		}
		addBotonera();
	}

	@Override
	public void cargarValores(ClienteAdministrativo arg0)
			throws ExcDatosInvalidos {
		try {
			if (modo == Accion.AGREGAR) {
				usuarios.setModelo(clientes);
			} else {
				cuentaCobrar.setValue(getModelo().getCuentaCobro());
				cuentaPagar.setValue(getModelo().getCuentaPago());
				cedula.setValue(getModelo().getCedulaCliente());
				nombre.setValue(getModelo().getNombreCliente());
				detalleDeuda.setModelo(documentosDeuda);
				detalleFavor.setModelo(documentosFavor);
				detalleRecibos.setModelo(recibosFavor);
				calcularSaldos();
				montoDeuda.setValue(saldoDeuda);
				montoFavor.setValue(saldoFavor);
				}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcDatosInvalidos(e.getMessage());
		}
	}

	public CompLista<DocumentoFiscal> getDetalleDeuda() {
		return detalleDeuda;
	}

	public CompLista<DocumentoFiscal> getDetalleFavor() {
		return detalleFavor;
	}

	public Doublebox getMontoDeuda() {
		return montoDeuda;
	}

	public Doublebox getMontoFavor() {
		return montoFavor;
	}

	public CompListaMultiple getUsuarios() {
		return usuarios;
	}

	public Textbox getCuentaPagar() {
		return cuentaPagar;
	}

	public Textbox getCuentaCobrar() {
		return cuentaCobrar;
	}

	public void setDetalleDeuda(CompLista<DocumentoFiscal> detalleDeuda) {
		this.detalleDeuda = detalleDeuda;
	}

	@SuppressWarnings("unchecked")
	public List<ClienteAdministrativo> getClientesSeleccionados() {
		return usuarios.getModeloSelect();
	}

}
