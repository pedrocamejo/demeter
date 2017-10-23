package cpc.demeter.vista;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.administrativo.sigesp.ExtracionDatosSigesp;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class ExportarSigespUI  extends CompVentanaBase<ExtracionDatosSigesp> {

	private static final long serialVersionUID = -5739695024627594930L;
	
	private CompGrupoDatos gbGeneral;
	private	Combobox		mes;
	private Combobox		ano;
	private Textbox 	usuario;

	Integer[]  meses = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12};
	
	List<Integer> anos = new ArrayList<Integer>();
	
	
	public ExportarSigespUI(String titulo, int ancho) throws ExcDatosInvalidos {
		super(titulo, ancho);
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 4);
		/// Años 

		anos.add(new Date().getYear()+1900);
		anos.add(new Date().getYear()+1900-1);  

		mes = new Combobox();
		mes.setModel(new ListModelArray(meses));
		mes.setItemRenderer(new ComboitemRenderer() {
			
			@Override
			public void render(Comboitem arg0, Object arg1) throws Exception {
				// TODO Auto-generated method stub
				arg0.setValue(arg1);
				if(arg1.equals(1)){
					arg0.setLabel("ENERO");
				}
				if(arg1.equals(2)){
					arg0.setLabel("FEBRERO");
				}
				if(arg1.equals(3)){
					arg0.setLabel("MARZO");
				}
				if(arg1.equals(4)){
					arg0.setLabel("ABRIL");
				}
				if(arg1.equals(5)){
					arg0.setLabel("MAYO");
				}
				if(arg1.equals(6)){
					arg0.setLabel("JUNIO");
				}
				if(arg1.equals(7)){
					arg0.setLabel("JULIO");
				}
				if(arg1.equals(8)){
					arg0.setLabel("AGOSTO");
				}
				if(arg1.equals(9)){
					arg0.setLabel("SEPTIEMBRE");
				}
				if(arg1.equals(10)){
					arg0.setLabel("OCTUBRE");
				}
				if(arg1.equals(11)){
					arg0.setLabel("NOVIEMBRE");
				}
				if(arg1.equals(12)){
					arg0.setLabel("DICIEMBRE");
				}
			}
		});
		this.ano = new Combobox();
		this.ano.setModel(new ListModelArray(anos));
		
		usuario = new Textbox();
		usuario.setDisabled(true);
	
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente(" Mes ",mes);
		gbGeneral.addComponente(" Año", ano);
		gbGeneral.addComponente("Usuario",usuario);
		gbGeneral.dibujar(this);
		addBotonera();
	}

	@Override
	public void cargarValores(ExtracionDatosSigesp arg0) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		
		usuario.setValue(getModelo().getUsuario());
		ano.setValue((getModelo().getAno()==null? null: getModelo().getAno().toString()));
		mes.setValue((getModelo().getMes()==null? null: getModelo().getMes().toString()));
		getBinder().addBinding(usuario, "value",getNombreModelo() + ".usuario", null, null, "save", null,null, null, null);
		getBinder().addBinding(ano, "selectedItem",getNombreModelo() + ".ano", null, null, "save", null,null, null, null);
		getBinder().addBinding(mes, "selectedItem",getNombreModelo() + ".mes", null, null, "save", null,null, null, null);
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.CORREGIR)
		{
			activarConsulta();
		}
	
	}

	public void activarConsulta() {
		ano.setDisabled(true);
		mes.setDisabled(true);
	}

	public Combobox getMes() {
		return mes;
	}

	public void setMes(Combobox mes) {
		this.mes = mes;
	}

	public Combobox getAno() {
		return ano;
	}

	public void setAno(Combobox ano) {
		this.ano = ano;
	}

	public Textbox getUsuario() {
		return usuario;
	}

	public void setUsuario(Textbox usuario) {
		this.usuario = usuario;
	}
	
	
	
}