package cpc.demeter.vista;


import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cpc.demeter.comando.ContValidarSolvencia;
import cpc.zk.componente.ventanas.CompGrupoDatos;

public class UIValidarSolvencia extends Window {


	private CompGrupoDatos 				contenedor;
	private CompGrupoDatos 				contbusquedad;

	private Textbox 					busquedad;
	private Button 						buscar;
	private Label						usuario;
	private Label						fecha;
	private Image						imagen;
	private ContValidarSolvencia	 	 controlador;
 

	public UIValidarSolvencia(String title, String border, boolean closable,ContValidarSolvencia controlador) {
		super(title, border, closable);
		this.setWidth("850px");
		this.controlador = controlador;

		inicializar();
		dibujar();
	}

	private void dibujar() {
		// TODO Auto-generated method stub
		
		contenedor.addComponente("Codigo Reporte ",busquedad);
		contenedor.addComponente(imagen);
		contenedor.addComponente(buscar);
		
		contbusquedad.addComponente("Usuario",usuario);
		contbusquedad.addComponente("Fecha",fecha);
		
		contenedor.dibujar(this);
		contbusquedad.dibujar(this);
	}

	private void inicializar() {
		// TODO Auto-generated method stub 
		contenedor = new CompGrupoDatos("Verificar Reporte  ",4);
		contbusquedad = new CompGrupoDatos(" ------ ",4);
		
		busquedad = new Textbox();
		busquedad.setWidth("80%");
		
		buscar = new Button("Buscar");
		buscar.addEventListener(Events.ON_CLICK,controlador);

		usuario = new Label();
		imagen = new Image("");
		fecha = new Label();
	}

	public CompGrupoDatos getContenedor() {
		return contenedor;
	}

	public void setContenedor(CompGrupoDatos contenedor) {
		this.contenedor = contenedor;
	}

	public CompGrupoDatos getContbusquedad() {
		return contbusquedad;
	}

	public void setContbusquedad(CompGrupoDatos contbusquedad) {
		this.contbusquedad = contbusquedad;
	}

	public Textbox getBusquedad() {
		return busquedad;
	}

	public void setBusquedad(Textbox busquedad) {
		this.busquedad = busquedad;
	}

	public Button getBuscar() {
		return buscar;
	}

	public void setBuscar(Button buscar) {
		this.buscar = buscar;
	}

	public Label getUsuario() {
		return usuario;
	}

	public void setUsuario(Label usuario) {
		this.usuario = usuario;
	}

	public Label getFecha() {
		return fecha;
	}

	public void setFecha(Label fecha) {
		this.fecha = fecha;
	}

	public ContValidarSolvencia getControlador() {
		return controlador;
	}

	public void setControlador(ContValidarSolvencia controlador) {
		this.controlador = controlador;
	}
	
	public void ok()
	{
		imagen.setSrc("iconos/ok.png");
	}
	
	public void no_ok()
	{
		imagen.setSrc("iconos/not_ok.png");
	}

}
