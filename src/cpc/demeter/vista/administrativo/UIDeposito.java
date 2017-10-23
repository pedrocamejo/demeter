package cpc.demeter.vista.administrativo;

import java.util.ArrayList; 
import java.util.List;

import javax.ws.rs.FormParam;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

import cpc.ares.modelo.Accion; 
import cpc.modelo.demeter.administrativo.Deposito;
import cpc.modelo.demeter.administrativo.DetalleDeposito;
import cpc.modelo.demeter.administrativo.FormaPago;
import cpc.modelo.demeter.administrativo.FormaPagoCheque;
import cpc.modelo.demeter.administrativo.FormaPagoEfectivo;
import cpc.modelo.sigesp.basico.CuentaBancaria;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UIDeposito extends CompVentanaBase<Deposito> {

	private List<CuentaBancaria>    cuentas;
	private CompBuscar<CuentaBancaria> cuenta;
	
	private Datebox 		fecha;
	private Textbox 		nroDeposito;
	private Textbox 		concepto;
	private Doublebox 		total;
	private Doublebox 		efectivo;
	private Doublebox 		totalCheques;
	
	private Listbox 		detalle;

	private Button 			agregar;
	private Button 			quitar;
	
	private Label			banco;
	private Label 			tipocuenta;
	
	private CompGrupoDatos  gbBasicos, gbTotales, gbDetalle;
	
	private List 		modelos = new ArrayList();   ///este es si es uno o es de Otro tipo o forma de pago o detalle deposito 
	
	
	private Integer 		accion;

	public UIDeposito(String titulo, int ancho, List<CuentaBancaria> cuentas, Integer accion) throws ExcDatosInvalidos {
			super(titulo, ancho);
			this.cuentas = cuentas;
			this.accion = accion;
	}

	public void inicializar() {
		gbBasicos = new CompGrupoDatos("Deposito",4);
		gbDetalle = new CompGrupoDatos("Detalle ", 1);
		gbTotales = new CompGrupoDatos("Totales", 4);

		fecha = new Datebox();
		
		efectivo = new Doublebox();
		
		nroDeposito = new Textbox();
		
		cuenta = new CompBuscar<CuentaBancaria>(getEncabezadoCuenta(), 2);

		total = new Doublebox();
		totalCheques = new Doublebox();
		total.setStyle("font-size: 20px;");
		
		concepto = new Textbox();
		concepto.setRows(3);
		
		total.setDisabled(true);
		
		totalCheques.setDisabled(true);
		
		efectivo.setDisabled(true);
		
		cuenta.setModelo(cuentas);
		cuenta.setAncho(670);
		
		detalle = new Listbox();
		
		if(accion != Accion.AGREGAR)
		{
			Listhead titulo = new Listhead();
	
			new Listheader(" Nro Cuenta ").setParent(titulo);
			new Listheader(" Nro Cheque  ").setParent(titulo);
			new Listheader("Fecha  ").setParent(titulo);
			new Listheader(" Monto  ").setParent(titulo);
			titulo.setParent(detalle);
			detalle.setItemRenderer(new ListitemRenderer() {
			@Override
			public void render(Listitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				arg0.setValue(arg1);
				DetalleDeposito deposito = (DetalleDeposito) arg1;
				new Listcell(deposito.getCuenta()).setParent(arg0);
				new Listcell( deposito.getDocumento() ).setParent(arg0);
				new Listcell( deposito.getStrFecha()).setParent(arg0);
				new Listcell( deposito.getStrMonto()).setParent(arg0);
				}
			});
		}
		else
		{
			Listhead titulo = new Listhead();
			new Listheader("Nro Recibo").setParent(titulo);
			new Listheader("Fecha ").setParent(titulo);
			new Listheader("Tipo Forma Pago ").setParent(titulo);
			new Listheader("Datos Forma ").setParent(titulo);
			new Listheader("Monto ").setParent(titulo);
			titulo.setParent(detalle);
			detalle.setItemRenderer(new ListitemRenderer() {
				@Override
				public void render(Listitem arg0, Object arg1) throws Exception {
					// TODO Auto-generated method stub
					arg0.setValue(arg1);
					if (arg1.getClass().equals(FormaPagoCheque.class))
					{
						FormaPagoCheque formaPagoCheque = (FormaPagoCheque) arg1;
						new Listcell(formaPagoCheque.getRecibo().getControl()).setParent(arg0);
						new Listcell(formaPagoCheque.getStrFecha()).setParent(arg0);
						new Listcell(formaPagoCheque.getTipoFormaPago().getDescripcion()).setParent(arg0);
						String detalle = new String();
						detalle = formaPagoCheque.detalle();
						new Listcell(detalle).setParent(arg0);
						new Listcell(formaPagoCheque.getStrMonto()).setParent(arg0);
					}
					if (arg1.getClass().equals(FormaPagoEfectivo.class))
					{
						FormaPagoEfectivo formaPagoEfectivo = (FormaPagoEfectivo) arg1;
						new Listcell(formaPagoEfectivo.getRecibo().getControl()).setParent(arg0);
						new Listcell(formaPagoEfectivo.getStrFecha()).setParent(arg0);
						new Listcell(formaPagoEfectivo.getTipoFormaPago().getDescripcion()).setParent(arg0);
						String detalle = new String();
						detalle = "PAGO EFECTIVO ";
						new Listcell(detalle).setParent(arg0);
						new Listcell(formaPagoEfectivo.getStrMonto()).setParent(arg0);
					}
					}
				});
		}
		
		
		banco = new Label();
		tipocuenta = new Label();
		agregar = new Button("+");
		agregar.addEventListener(Events.ON_CLICK,getControlador());
		
		quitar = new Button("-");
		quitar.addEventListener(Events.ON_CLICK,getControlador());
		
	}

	public void dibujar() {

		cuenta.setWidth("250px");
		nroDeposito.setWidth("200px");
		efectivo.setWidth("200px");
		total.setWidth("200px");
		totalCheques.setWidth("200px");
		efectivo.setFormat("#,###,###,##0.00");
		concepto.setWidth("300px");
		total.setFormat("#,###,###,##0.00");
		totalCheques.setFormat("#,###,###,##0.00");

		gbBasicos.addComponente("Cuenta :", cuenta);
		gbBasicos.addComponente("Banco :", banco);
		gbBasicos.addComponente("Tipo cuenta  :", tipocuenta);

		gbBasicos.addComponente("Nro de deposito  :", nroDeposito);
		gbBasicos.addComponente("Fecha  :", fecha);
		gbBasicos.addComponente("concepto",concepto);
		gbBasicos.dibujar(this);
		
		Vbox vbox = new Vbox();

		Hbox hbox = new Hbox();
			hbox.setAlign("left");
			hbox.appendChild(agregar);
			hbox.appendChild(quitar);
		
		vbox.appendChild(hbox);
		vbox.appendChild(detalle);

		gbDetalle.addComponente(vbox);
		gbDetalle.dibujar(this);
		
		gbTotales.addComponente("Total Cheques :", totalCheques);
		gbTotales.addComponente("Efectivo  :", efectivo);
		gbTotales.addComponente(" ", new Label());
		gbTotales.addComponente("Total Deposito :", total);
		gbTotales.dibujar(this);
		cuenta.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());
		addBotonera();
	}

	
	public void cargarValores(Deposito recibo) throws ExcDatosInvalidos {

		cuenta.setSeleccion(getModelo().getCuentaBancaria());
		nroDeposito.setValue(getModelo().getNroDeposito());
		efectivo.setValue(getModelo().getMontoEfectivo());
		total.setValue(getModelo().getMontoTotal());
		fecha.setValue(getModelo().getFecha());
		concepto.setValue(getModelo().getConcepto());
		
		totalCheques.setValue(getModelo().getMontoTotal() - getModelo().getMontoEfectivo());
		
		getBinder().addBinding(cuenta, "seleccion",	getNombreModelo() + ".cuentaBancaria", null, null, "save",null, null, null, null);
		getBinder().addBinding(nroDeposito, "value",getNombreModelo() + ".nroDeposito", null, null, "save", null,null, null, null);
		getBinder().addBinding(efectivo, "value",getNombreModelo() + ".montoEfectivo", null, null, "save", null,null, null, null);
		getBinder().addBinding(total, "value",getNombreModelo() + ".montoTotal", null, null, "save", null,null, null, null);
		getBinder().addBinding(fecha, "value",getNombreModelo() + ".fecha", null, null, "save", null,null, null, null);
		getBinder().addBinding(concepto, "value",getNombreModelo() + ".concepto", null, null, "save", null,null, null, null);
		
		
		totalCheques.setValue(getModelo().getMontoTotal() - getModelo().getMontoEfectivo());

		detalle.setModel(new ListModelList(getModelo().getCheques()));
	}

	private List<CompEncabezado> getEncabezadoCuenta() {
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Tipo", 120);
		titulo.setMetodoBinder("getStrTipoCuenta");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Banco", 250);
		titulo.setMetodoBinder("getStrBanco");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nro Cuenta", 200);
		titulo.setMetodoBinder("getNroCuenta");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		return encabezado;
	}



	public Doublebox getTotal() {
		return total;
	}

	public Doublebox getSaldo() {
		return efectivo;
	}

	
	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ELIMINAR )
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		agregar.setVisible(false);
		quitar.setVisible(false);
		
		efectivo.setDisabled(true);
		cuenta.setDisabled(true);
		nroDeposito.setDisabled(true);
		concepto.setDisabled(true);
		fecha.setDisabled(true);
		total.setDisabled(true);
		totalCheques.setDisabled(true);
		
	}

	public void modoEdicion() {
		cuenta.setDisabled(false);
 
	}

	public Doublebox getTotalCheques() {
		return totalCheques;
	}

	public void addTotalCheques(Double valor) {
		totalCheques.setValue(totalCheques.getValue() + valor);
		addTotal();
	}

	public void addTotal() {
		total.setValue(efectivo.getValue() + totalCheques.getValue());
	}

	public Textbox getNroDeposito() {
		return nroDeposito;
	}

	public void setNroDeposito(Textbox nroDeposito) {
		this.nroDeposito = nroDeposito;
	}

	public CompBuscar<CuentaBancaria> getCuenta() {
		return cuenta;
	}

	public void setCuenta(CompBuscar<CuentaBancaria> cuenta) {
		this.cuenta = cuenta;
	}

	public Doublebox getEfectivo() {
		return efectivo;
	}

	public void setEfectivo(Doublebox efectivo) {
		this.efectivo = efectivo;
	}

	public void setTotal(Doublebox total) {
		this.total = total;
	}

	public void setTotalCheques(Doublebox totalCheques) {
		this.totalCheques = totalCheques;
	}

	public Datebox getFecha() {
		return fecha;
	}


	public Label getBanco() {
		return banco;
	}

	public void setBanco(Label banco) {
		this.banco = banco;
	}

	public Label getTipocuenta() {
		return tipocuenta;
	}

	public void setTipocuenta(Label tipocuenta) {
		this.tipocuenta = tipocuenta;
	}

	public CompGrupoDatos getGbDetalle() {
		return gbDetalle;
	}

	public Button getAgregar() {
		return agregar;
	}

	public List getModelos() {
		return modelos;
	}

	public void setModelos(List modelos) {
		this.modelos = modelos;
	}

	public void setAgregar(Button agregar) {
		this.agregar = agregar;
	}

	public Button getQuitar() {
		return quitar;
	}

	public void setQuitar(Button quitar) {
		this.quitar = quitar;
	}

	public Listbox getDetalle() {
		return detalle;
	}

	public void setDetalle(Listbox detalle) {
		this.detalle = detalle;
	}

	public void agregarFormaPago(FormaPago pago) {
		// TODO Auto-generated method stub
		for(Object obj : modelos)
		{
			FormaPago p = (FormaPago) obj;
			
			if(p.getId().equals(pago.getId()))
			{
				return;
			}
		}
		modelos.add(pago);
		detalle.setModel(new ListModelList(modelos));
		calcularSaldos();
	}

	public void quitarFormaPago(FormaPago pago) {
		// TODO Auto-generated method stub
	
		for(Object obj : modelos)
		{
			FormaPago p = (FormaPago) obj;
			if(p.getId().equals(pago.getId()))
			{
				modelos.remove(obj);
				detalle.setModel(new ListModelList(modelos));
				calcularSaldos();
				return;
			}
		}
	}

	private void calcularSaldos() {
		// TODO Auto-generated method stub
		Double efectivo = new Double(0);
		Double cheque = new Double(0);
		Double	total = new Double(0);
		
		
		for(Object obj : modelos)
		{
			if(obj.getClass().equals(FormaPagoEfectivo.class))
			{
				FormaPagoEfectivo formaPago  = (FormaPagoEfectivo) obj;
				efectivo += formaPago.getMonto();
			}
			if(obj.getClass().equals(FormaPagoCheque.class))
			{
				FormaPagoCheque formaPago  = (FormaPagoCheque) obj;
				cheque += formaPago.getMonto();
			}
		}
		total = efectivo + cheque;
		this.total.setValue(total);
		this.efectivo.setValue(efectivo);
		this.totalCheques.setValue(cheque);
		
	}



}