package cpc.demeter.vista;

import org.zkoss.zul.Label;

import cpc.modelo.demeter.sincronizacion.Exportar;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiExportar  extends CompVentanaBase<Exportar> {


	private static final long serialVersionUID = 1L;

	private CompGrupoDatos gbGeneral;
	private Label		tareas; //para medir el progreso del trabajoo 
	
	
	public UiExportar(String titulo, int ancho) throws ExcDatosInvalidos {
		super(titulo, ancho);
	}

	public UiExportar(String titulo) throws ExcDatosInvalidos {
		super(titulo);

	}
	
	@Override
	public void inicializar() {
		// TODO Auto-generated method stub

		 gbGeneral = new CompGrupoDatos(2);
		 tareas = new Label(" 0 %"); //para medir el progreso del trabajoo 
 	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub

		 gbGeneral = new CompGrupoDatos(2);
		 gbGeneral.addComponente(" Tareas  :", tareas);
		 
		 gbGeneral.dibujar(this);

		 this.addBotonera();
		 
	}
 
	public void desactivar(int modooperacion)
	{
		
	}

	@Override
	public void cargarValores(Exportar dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		
	}

}
