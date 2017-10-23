package cpc.demeter.vista.administrativo;

import java.util.ArrayList;
import java.util.List;
 

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Intbox; 
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell; 
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Textbox;  

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.Cheque;
import cpc.modelo.demeter.administrativo.Recibo; 
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiCheque extends CompVentanaBase<Cheque> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CompGrupoDatos  grupos; // grupo general
	private CompGrupoDatos  conCheque; // contenedor para los cheques
	
	private List<Recibo>    recibos = new ArrayList<Recibo>();
	private Intbox 			id;
	private Textbox			nroCheque;
	private Textbox 		nroCuenta;
	private Doublebox 		monto;
	
	private Listbox         listrecibos;


	public UiCheque(String titulo, Integer height,List<Recibo> recibos) {
		// TODO Auto-generated constructor stub
		super(titulo, height);
		this.recibos = recibos;
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub

		grupos = new CompGrupoDatos(); // grupo general
		conCheque = new CompGrupoDatos("Datos del Cheque "); // contenedor para

		id = new Intbox();
		id.setDisabled(true);

		nroCheque = new Textbox();
			nroCheque.addEventListener(Events.ON_BLUR,contMayuscula());
			nroCheque.setMaxlength(30);
			
		nroCuenta = new Textbox();
			nroCuenta.addEventListener(Events.ON_BLUR,contMayuscula());
			nroCuenta.setMaxlength(30);
			
		monto = new Doublebox( );
			monto.setValue(0);
			//monto.setFormat("################,00");
			monto.setFormat("##,###,###,##0.00");
			
			monto.setMaxlength(40);
			
		getAceptar().addEventListener(Events.ON_CLICK,getControlador());
		
		listrecibos = new Listbox();
		
			Listhead titulo =  new Listhead();
			Listheader tcontrol =	new Listheader("Nro control");
			Listheader tcliente =	new Listheader("cliente");
			Listheader testado =	new Listheader("estado");
			Listheader tmonto =	new Listheader("monto");
			Listheader tsaldo =	new Listheader("saldo");
			
			titulo.appendChild(tcontrol);
			titulo.appendChild(tcliente);
			titulo.appendChild(testado);
			titulo.appendChild(tmonto);
			titulo.appendChild(tsaldo);
			
			listrecibos.appendChild(titulo);
			
			listrecibos.setItemRenderer(new ListitemRenderer() {
				
				@Override
				public void render(Listitem arg0, Object arg1) throws Exception {
					// TODO Auto-generated method stub
					arg0.setValue(arg1);
					Recibo recibo = (Recibo) arg1;
					
					new Listcell(recibo.getControl()).setParent(arg0);
					String nombre =recibo.getCliente().getNombres();
					nombre = nombre.substring(0,(nombre.length() < 20 ?  nombre.length() : 20));
					new Listcell(recibo.getCliente().getIdentidadLegal()+" "+nombre).setParent(arg0);
					new Listcell(recibo.getEstado()).setParent(arg0);
					new Listcell(recibo.getStrMonto()).setParent(arg0);
					new Listcell(recibo.getStrSaldo()).setParent(arg0);

				}
			});
	}
	

	//para campos tipo text convierte el contenido del campo en Mayuscula al pasar un evento
	public EventListener contMayuscula()
	{
		return new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				Textbox campo =  (Textbox) arg0.getTarget();
				campo.setValue(campo.getValue().trim().toUpperCase());
			}
		};
	}
	
	
	@Override
	public void dibujar() {
		// TODO Auto-generated method stub

		conCheque.addComponente("ID", id);
		conCheque.addComponente("Numero ", nroCheque);
		conCheque.addComponente("Numero de Cuenta ", nroCuenta);
		conCheque.addComponente("Monto", monto);
		conCheque.dibujar(grupos);
		listrecibos.setModel(new ListModelArray(recibos));
		grupos.addComponente(listrecibos);
		grupos.dibujar(this);
		
		addBotonera();
	}

	@Override
	public void cargarValores(Cheque dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub

		id.setValue(dato.getId());
		nroCheque.setValue(getModelo().getNroCheque());
		nroCuenta.setValue(getModelo().getNroCuenta());
		monto.setValue((getModelo().getMonto()==null? 0:getModelo().getMonto()));

		getBinder().addBinding(this.nroCheque, "value",	getNombreModelo() + ".nroCheque", null, null, "save", null,null, null, null);
		getBinder().addBinding(this.nroCuenta, "value",	getNombreModelo() + ".nroCuenta", null, null, "save", null,null, null, null);
		getBinder().addBinding(this.monto, "value",	getNombreModelo() + ".monto", null, null, "save", null, null,null, null);

	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else
			modoEdicion();
	}

	public void activarConsulta() {
		// descripcion.setDisabled(true);
		id.setDisabled(true);
		nroCheque.setDisabled(true);
		nroCuenta.setDisabled(true);
		monto.setDisabled(true);
	}

	public void modoEdicion() {
		// descripcion.setDisabled(false);
	}

	public Textbox getNroCheque() {
		return nroCheque;
	}

	public void setNroCheque(Textbox nroCheque) {
		this.nroCheque = nroCheque;
	}

	public Textbox getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(Textbox nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public Doublebox getMonto() {
		return monto;
	}

	public void setMonto(Doublebox monto) {
		this.monto = monto;
	}

	public CompGrupoDatos getGrupos() {
		return grupos;
	}

	public void setGrupos(CompGrupoDatos grupos) {
		this.grupos = grupos;
	}

	public CompGrupoDatos getConCheque() {
		return conCheque;
	}

	public void setConCheque(CompGrupoDatos conCheque) {
		this.conCheque = conCheque;
	}

	public void setId(Intbox id) {
		this.id = id;
	}

}
