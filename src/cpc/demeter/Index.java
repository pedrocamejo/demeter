package cpc.demeter;

import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import cpc.demeter.vista.UiCambiarMiClave;
import cpc.modelo.demeter.administrativo.ControlSede;
import cpc.modelo.demeter.administrativo.Impuesto;
import cpc.zk.componente.ventanas.CompLogin;

public class Index extends GenericAutowireComposer {

	private static final long serialVersionUID = 4554850522789654257L;
	
	private AppDemeter 			app;
	private Label 				lblInfoUsuario; 
	//private Label 				lblSede;
	private Vbox 				menu, contenedor;
	private Window 				ventana;
	private Button 				imgDocumentacion; //documentacion/index.html
	private CompLogin 			login;
	private Button 				imgReiniciarSesion;
	private Button 				imgCambioClave; 
	private Label 				fechaControl;
	@Override
	public void doFinally() throws Exception {
		super.doFinally();
		this.app = new AppDemeter();
		this.app.configurarBaseDatos();
		this.app.setWin(ventana);
		this.app.setContenedor(contenedor);
		this.app.setPadre(this);
		this.imgReiniciarSesion.addEventListener(Events.ON_CLICK, this);
		this.imgCambioClave.addEventListener(Events.ON_CLICK, this);
		this.imgDocumentacion.setTooltiptext("Documentación sobre el Sistema ");
		this.imgDocumentacion.addEventListener(Events.ON_CLICK, abrirDocumentacion());
		
		CompLogin log=new CompLogin();
		this.app.setLogin(log);
		this.app.getLogin()	.setStyle("background-color:#ececec;filter:alpha(opacity=70);-moz-opacity:0.7;opacity:0.7;");
		this.app.getLogin().setControlador(this.app);
	
	}

	private EventListener abrirDocumentacion() {
		// TODO Auto-generated method stub
		return new EventListener() {
			
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				Executions.getCurrent().sendRedirect("documentacion/index.html","_blank");
				//        Executions.getCurrent().sendRedirect("http://www.google.com", "_blank");
			}
		};
	}

	public ControlSede getControlSede() {
		ControlSede control = (ControlSede) ventana.getDesktop().getSession()
				.getAttribute("controlSede");
		return control;

	}

	public void setControlSede(ControlSede control) {
		ventana.getDesktop().getSession().setAttribute("controlSede", control);
		getApp().getWin().getDesktop().setAttribute("controlSede", control);

	}

	public Impuesto getIva() {
		Impuesto iva = (Impuesto) ventana.getDesktop().getSession()
				.getAttribute("iva");
		return iva;
	}

	public AppDemeter getApp() {
		return app;
	}

	public void setApp(AppDemeter app) {
		this.app = app;
	}

	public Vbox getMenu() {
		return menu;
	}

	public void setMenu(Vbox menu) {
		this.menu = menu;
	}
	public Window getVentana() {
		return ventana;
	}

	public void setVentana(Window ventana) {
		this.ventana = ventana;
	}

	public CompLogin getLogin() {
		return login;
	}

	public void setLogin(CompLogin login) {
		this.login = login;
	}

	public Label getLblInfoUsuario() {
		return lblInfoUsuario;
	}

	public void setLblInfoUsuario(String infoUsuario) {
		System.getenv("APP_MASTER_PASSWORD");
		
		this.lblInfoUsuario.setValue("USUARIO: " + infoUsuario);
	}
 

	@Override
	public void onEvent(Event evento) throws Exception {
	super.onEvent(evento);
		if (evento.getTarget() == imgReiniciarSesion) 
		{
			Sessions.getCurrent().removeAttribute("usuario");
			Sessions.getCurrent().invalidate();
			Executions.sendRedirect("/");
			Messagebox.show("Se ha cerrado su Sesion...", "Sesion Cerrada",	Messagebox.OK, Messagebox.INFORMATION);

		}
		else if (evento.getTarget() == imgCambioClave)
		{
			app.getPadre().getVentana().appendChild(new UiCambiarMiClave(app, app.getUsuario()));
		}
	}

	public Label getFechaControl() {
		return fechaControl;
	}

	public void setFechaControl(Label fechaControl) {
		this.fechaControl = fechaControl;
	}

}