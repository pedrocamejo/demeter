package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.LibroVenta;
import cpc.modelo.demeter.administrativo.LibroVentaDetalle;

import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompLista;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UILibroVenta extends CompVentanaBase<LibroVenta> {

	private static final long serialVersionUID = 6312240548253365129L;

	private CompGrupoDatos gbControl, gbDetalle;
	private Doublebox ingresoBase, IngresoTotal;
	private Intbox cantidadDocumentos;
	private Combobox mes;
	private Spinner ano;
	private Textbox sede;
	private Button generar;
	private CompLista<LibroVentaDetalle> detalle;

	public UILibroVenta(String titulo) {
		super(titulo, 520);
	}

	@SuppressWarnings("deprecation")
	public void inicializar() {
		gbControl = new CompGrupoDatos("General", 2);
		gbControl.setWidth("500px");

		gbDetalle = new CompGrupoDatos("Documento", 1);
		gbDetalle.setWidth("500px");

		generar = new Button("Generar Libro");
		sede = new Textbox();
		ingresoBase = new Doublebox();
		IngresoTotal = new Doublebox();
		cantidadDocumentos = new Intbox();
		ano = new Spinner();
		mes = new Combobox();

		agregarCombo();
		mes.setAutodrop(true);
		mes.setAutocomplete(true);

		detalle = new CompLista<LibroVentaDetalle>(encabezadosDocumentos());

		ingresoBase.setDisabled(true);
		ingresoBase.setFormat("##,###,###,###,##0.00");
		IngresoTotal.setDisabled(true);
		IngresoTotal.setFormat("##,###,###,###,##0.00");
		cantidadDocumentos.setDisabled(true);
		ano.setValue(new Date().getYear() + 1900);
		sede.setDisabled(true);
		mes.setReadonly(true);

	}

	public void dibujar() {
		gbControl.addComponente("Sede :", sede);
		gbControl.addComponente("Mes :", mes);
		gbControl.addComponente("Año :", ano);
		gbControl.addComponente("Monto Base :", ingresoBase);
		gbControl.addComponente("Monto Total :", IngresoTotal);
		gbControl.addComponente("Documentos Seleccionados :",
				cantidadDocumentos);
		gbControl.addComponente("", generar);
		gbControl.dibujar(this);
		gbDetalle.addComponente(detalle);
		gbDetalle.dibujar(this);
		addBotonera();
		generar.addEventListener(Events.ON_CLICK, getControlador());
	}

	@Override
	public void cargarValores(LibroVenta arg0) throws ExcDatosInvalidos {
		if (getModelo().getAno() != 0)
			ano.setValue(getModelo().getAno());
		if (getModelo().getMes() > 0)
			mes.setSelectedIndex(getModelo().getMes() - 1);
		ingresoBase.setValue(getModelo().getMontoBase());
		IngresoTotal.setValue(getModelo().getMontoTotal());
		cantidadDocumentos.setValue(getModelo().getCantidadDocumentos());
		if (getModelo().getDetalles() != null)
			detalle.setModelo(getModelo().getDetalles());
	}

	private List<CompEncabezado> encabezadosDocumentos() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Factura", 130);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrDocumento");
		// titulo.setMetodoModelo(".tipoFormaPago");

		encabezado.add(titulo);
		titulo = new CompEncabezado("Fecha", 115);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getFecha");
		titulo.setAlineacion(CompEncabezado.DERECHA);

		encabezado.add(titulo);
		titulo = new CompEncabezado("Ingreso Bruto", 115);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrCSBruto");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Ingreso Neto", 115);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getStrCSNeto");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		return encabezado;
	}

	private void agregarCombo() {
		Comboitem item;
		mes.setValue("");
		mes.getChildren().clear();
		int i = 1;
		for (String dato : LibroVenta.MESES) {
			item = new Comboitem();
			item.setLabel(dato.toString());
			item.setAttribute("dato", i++);
			mes.appendChild(item);
		}
	}

	public Textbox getSede() {
		return sede;
	}

	public CompGrupoDatos getGbControl() {
		return gbControl;
	}

	public void setGbControl(CompGrupoDatos gbControl) {
		this.gbControl = gbControl;
	}

	public CompGrupoDatos getGbDetalle() {
		return gbDetalle;
	}

	public void setGbDetalle(CompGrupoDatos gbDetalle) {
		this.gbDetalle = gbDetalle;
	}

	public Doublebox getIngresoBase() {
		return ingresoBase;
	}

	public void setIngresoBase(Doublebox ingresoBase) {
		this.ingresoBase = ingresoBase;
	}

	public Doublebox getIngresoTotal() {
		return IngresoTotal;
	}

	public void setIngresoTotal(Doublebox ingresoTotal) {
		IngresoTotal = ingresoTotal;
	}

	public Intbox getCantidadDocumentos() {
		return cantidadDocumentos;
	}

	public void setCantidadDocumentos(Intbox cantidadDocumentos) {
		this.cantidadDocumentos = cantidadDocumentos;
	}

	public Combobox getMes() {
		return this.mes;
	}

	public void setMes(CompCombobox<String> mes) {
		this.mes = mes;
	}

	public Spinner getAño() {
		return ano;
	}

	public void setAño(Spinner ano) {
		this.ano = ano;
	}

	public CompLista<LibroVentaDetalle> getDetalle() {
		return detalle;
	}

	public void setDetalle(CompLista<LibroVentaDetalle> detalle) {
		this.detalle = detalle;
	}

	public void setSede(Textbox sede) {
		this.sede = sede;
	}

	public Spinner getAno() {
		return ano;
	}

	public void setAno(Spinner ano) {
		this.ano = ano;
	}

	public void setMes(Combobox mes) {
		this.mes = mes;
	}

	public void actualizarDetalle(List<LibroVentaDetalle> detalles) {
		detalle.setModelo(detalles);
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		ano.setDisabled(true);
		mes.setDisabled(true);
		generar.setDisabled(true);
	}

	public void modoEdicion() {
		ano.setDisabled(false);
		mes.setDisabled(false);
		generar.setDisabled(false);
	}

}