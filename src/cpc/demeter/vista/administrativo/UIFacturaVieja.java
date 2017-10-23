package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UIFacturaVieja extends CompVentanaBase<DocumentoFiscal> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5739695024627594930L;
	// constraint="/.+@.+\.[a-z]+ /: correo no valido" />

	private Intbox control;
	private Datebox fecha;
	private CompBuscar<Cliente> cedula;
	private Label telefono, razonSocial;
	private Textbox direccion;
	private CompGrupoDatos cliente, encfactura;
	private Doublebox total, saldo;
	private Textbox observacion;

	private List<Cliente> clientes;
	private Date inicio;

	// private Button aceptar, cancelar;
	// private DataBinder binder;

	public UIFacturaVieja(String titulo, int ancho, List<Cliente> clientes,
			Date inicio) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.clientes = clientes;
		this.inicio = inicio;
	}

	public UIFacturaVieja(String titulo) throws ExcDatosInvalidos {
		super(titulo);
	}

	public void inicializar() {
		control = new Intbox();
		cedula = new CompBuscar<Cliente>(getEncabezadoCliente(), 0);
		telefono = new Label();
		fecha = new Datebox();
		cliente = new CompGrupoDatos("Nombre o Razon Social", 2);
		encfactura = new CompGrupoDatos("Datos basicos", 4);
		razonSocial = new Label();
		direccion = new Textbox();
		total = new Doublebox();
		saldo = new Doublebox();
		observacion = new Textbox();
		cedula.setModelo(clientes);
		direccion.setMultiline(true);
		direccion.setRows(2);
		cedula.setListenerEncontrar(getControlador());
	}

	public void dibujar() {
		observacion.setWidth("400px");
		razonSocial.setWidth("550px");
		cedula.setAncho(650);
		direccion.setWidth("550px");

		saldo.setFormat("##,###,###,##0.00");
		total.setFormat("##,###,###,##0.00");
		cliente.setAnchoColumna(0, 200);
		encfactura.addComponente("Nro Control  :", control);
		encfactura.addComponente("Fecha :", fecha);
		encfactura.addComponente("Monto Total  :", total);
		encfactura.addComponente("Saldo Pendiente  :", saldo);
		encfactura.dibujar(this);

		Grid grilla = new Grid();
		Columns columnas = new Columns();
		grilla.setStyle("border-style : hidden; border-color :white");
		columnas.appendChild(new Column());
		columnas.appendChild(new Column());
		columnas.appendChild(new Column());
		grilla.appendChild(columnas);

		Rows filas = new Rows();
		Row row = new Row();
		row.setStyle("background-color:white; color:black; border-style : hidden; border-color :white");
		row.appendChild(cedula);
		row.appendChild(new Label("Telefono:"));
		row.appendChild(telefono);
		filas.appendChild(row);
		grilla.appendChild(filas);

		cliente.addComponente("Cedula/RIF:", grilla);
		cliente.addComponente("Cliente:", razonSocial);
		cliente.addComponente("Direccion:", direccion);
		cliente.dibujar(this);

		addBotonera();

	}

	private List<CompEncabezado> getEncabezadoCliente() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Cedula", 120);
		titulo.setMetodoBinder("getIdentidadLegal");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre", 450);
		titulo.setMetodoBinder("getNombres");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}

	public void cargarValores(DocumentoFiscal arg0) throws ExcDatosInvalidos {
		getBinder().addBinding(cedula, "seleccion",
				getNombreModelo() + ".beneficiario", null, null, "save", null,
				null, null, null);
		try {
			razonSocial.setValue(getModelo().getBeneficiario().getNombres());
			cedula.setSeleccion(getModelo().getBeneficiario());
			if (getModelo().getBeneficiario().getTelefonos().size() > 0)
				telefono.setValue(getModelo().getBeneficiario().getTelefonos()
						.get(0).getNumero());
		} catch (NullPointerException e) {
			// TODO: handle exception
		}
		// getBinder().addBinding(direccion, "value",
		// getNombreModelo()+".direccionFiscal", null, null, "save", null, null,
		// null, null);
		getBinder().addBinding(total, "value",
				getNombreModelo() + ".montoTotal", null, null, "save", null,
				null, null, null);
		getBinder().addBinding(saldo, "value",
				getNombreModelo() + ".montoSaldo", null, null, "save", null,
				null, null, null);

		getBinder().addBinding(cedula, "seleccion",
				getNombreModelo() + ".beneficiario", null, null, "save", null,
				null, null, null);
		getBinder().addBinding(control, "value",
				getNombreModelo() + ".nroControl", null, null, "save", null,
				null, null, null);
		getBinder().addBinding(fecha, "value", getNombreModelo() + ".fecha",
				null, null, "save", null, null, null, null);

		cedula.setConstraint("no empty : Razon Social no valida");
		fecha.setConstraint("before "
				+ String.format("%1$tY%1$tm%1$td", inicio));
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		cedula.setDisabled(true);
		direccion.setDisabled(true);
		observacion.setDisabled(true);
	}

	public void modoEdicion() {
		cedula.setDisabled(false);
		direccion.setDisabled(false);
		observacion.setDisabled(false);
	}

	public Intbox getControl() {
		return control;
	}

	public void setControl(Intbox control) {
		this.control = control;
	}

	public Datebox getFecha() {
		return fecha;
	}

	public void setFecha(Datebox fecha) {
		this.fecha = fecha;
	}

	public CompBuscar<Cliente> getCedula() {
		return cedula;
	}

	public void setCedula(CompBuscar<Cliente> cedula) {
		this.cedula = cedula;
	}

	public Doublebox getTotal() {
		return total;
	}

	public void setTotal(Doublebox total) {
		this.total = total;
	}

	public Doublebox getSaldo() {
		return saldo;
	}

	public void setSaldo(Doublebox saldo) {
		this.saldo = saldo;
	}

	public Label getTelefono() {
		return telefono;
	}

	public Label getRazonSocial() {
		return razonSocial;
	}

	public Textbox getDireccion() {
		return direccion;
	}

}
