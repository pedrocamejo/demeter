package cpc.demeter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Script;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

import cpc.ares.interfaz.IMenu;
import cpc.ares.modelo.Modulo;
import cpc.ares.modelo.Sede;
import cpc.ares.modelo.UnidadFuncional;
import cpc.ares.modelo.Usuario;
import cpc.ares.persistencia.PerSede;
import cpc.ares.persistencia.PerUsuario;
import cpc.modelo.demeter.administrativo.ControlSede;
import cpc.negocio.demeter.administrativo.NegocioFactura;
import cpc.negocio.sigesp.NegocioSede;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.menus.CompAcordeon;
import cpc.zk.componente.ventanas.CompLogin;
import cpc.zk.componente.ventanas.CompVentana;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.utilidades.Fecha;

 import org.springframework.jdbc.datasource.SingleConnectionDataSource;

public class AppDemeter implements EventListener, IZkAplicacion {

	private static final long serialVersionUID = 5978637347380678522L;
	private Integer 		sistema;
	private Usuario 		usuario;
	private Sede 			sede;
	private Index 			padre;
	private CompLogin 		login;
	private Window			win;
	private Vbox 			contenedor;
	private Jasperreport 	reporte;
	private Script 			reporteExterno;
	
	public AppDemeter(){
		sistema = new Integer(SpringUtil.getBean("sistema").toString());
	}
	
	
	public void agregar(Component componente) {
		if (componente instanceof Script) {
			reporteExterno = (Script) componente;
			agregarReporteExt(reporteExterno);
		} else
			this.contenedor.appendChild(componente);

	}

	public void agregarReporteExt(Script componente) {
		this.getPadre().getVentana().appendChild(componente);
	}

	public void agregarHija(Component componente) {
		contenedor.getParent().appendChild(componente);
	}

	@SuppressWarnings("unchecked")
	public void limpiarReporte() {
		try {
			List<Component> componentes = contenedor.getChildren();
			if (reporte != null)
				componentes.remove(reporte);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public void limpiarReporteExterno() {
		try {
			List<Component> componentes = contenedor.getChildren();
			if (reporteExterno != null)
				componentes.remove(reporteExterno);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void agregarReporte() {
		if (reporte == null) {
			reporte = new Jasperreport();
		}
		this.getPadre().getVentana().appendChild(reporte);
	}

	public void agregarAEscritorio(Component componente) {
		this.contenedor.getChildren().clear();
		this.contenedor.appendChild(componente);

	}

	public void agregarMenu(Component menu) {

	}

	public void doAfterCompose(Component componente) throws Exception {

	}

	public CompLogin getLogin() {
		return this.login;
	}

	public Component getSelf() {
		return null;
	}

	public void setEscritorio() {
	}

	public void setLogin(CompLogin login) throws Exception {
		this.login = login;
		usuario = (Usuario) Sessions.getCurrent().getAttribute("usuario");
		if(usuario == null){
			this.login.setCmbSedesModelo(PerSede.obtenerSedes());
			this.login.setVisibleSedes(false);
			this.win.appendChild(login);
		}
		else{
			List<Modulo> modulos = PerUsuario.obtenerPermisos(usuario.getId(), this);
			CompAcordeon acordeon = new CompAcordeon(modulos);
			acordeon.setHeight("80%");
			acordeon.setApp(this.getPadre());
			acordeon.setSistema(this);
			padre.getMenu().appendChild(acordeon);
			setSede(usuario.getSede());
			setUsuario(usuario);
			configurarEscritorio();
		}
	}

	public void setMenuIni(IMenu arg0) {

	}

	public void cambiarFuete(int arg0) {

	}

	public void cargarInfUsuario(String datos, Sede sede) {
		getPadre().setLblInfoUsuario(datos + " ON "+ sede.getNombre() + " " + sede.getEmpresa().getDescripcion());
	}

	public void cargarInfUsuario(String arg0, Sede arg1,
			List<UnidadFuncional> arg2) {
	}

	public void cargarInfUsuario(String arg0, Sede arg1, String arg2) {
	}

	public void configurarEscritorio() throws Exception  {
		List<Modulo> modulos = PerUsuario.obtenerPermisos(usuario.getId(), this);
		CompAcordeon acordeon = new CompAcordeon(modulos);
		acordeon.setHeight("80%");
		acordeon.setApp(this.getPadre());
		acordeon.setSistema(this);
		padre.getMenu().getChildren().clear();
		padre.getMenu().appendChild(acordeon);
		ControlSede control = NegocioFactura.getInstance().getControlSede();
		setFechaControl(Fecha.obtenerFecha(control.getFechaCierreFactura()));
	}

	public Object getContextProperty(String arg0) {
		return null;
	}

	public Sede getSede() {
		return this.sede;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public String getlogin() {
		if (usuario != null)
			return this.usuario.getNombre();
		else
			return "";
	}

	public String getdatosUsuario() {
		return null;
	}

	public void limpiarEscritorio() {

	}

	public void mostrarConfirmacion(String arg0) {

	}

	public void mostrarError(String error) {
		try {
			Messagebox.show(error, "Error", Messagebox.OK, Messagebox.ERROR);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void mostrarImpresion(String arg0) {

	}

	public void mostrarInformacion(String mensaje) {
		try {
			Messagebox.show(mensaje, "Informacion", Messagebox.OK,
					Messagebox.INFORMATION);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void mostrarAdvertencia(String mensaje) {
		try {
			Messagebox.show(mensaje, "Atención", Messagebox.OK,
					Messagebox.EXCLAMATION);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
 
	public void setSede(Sede sede) {
		this.sede = sede;
		System.out.println("SEDE SETEADA " + this.sede);

	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
		cargarInfUsuario(
				this.usuario.getNombreIdentidad() + " "
						+ this.usuario.getApellido() + "["
						+ this.usuario.getNombre() + "]", sede);
	}

	public void mostrarListaSedes() {
		try {
			usuario = PerUsuario.obtenerPorNombreUsuario(login.getJ_username().getText());
			if (usuario != null) { // Encontro al Usuario
				if (conAccesoMultisede(usuario.getId())) {
					// Esta en el Grupo Correspondiente
					login.setVisibleSedes(true);
					login.getCmbSedes().setSeleccion(null);
				} else {
					// No esta en el Grupo MultiSedes
					login.setVisibleSedes(false);
					login.getCmbSedes().setSeleccion(usuario.getSede());
				}
			} else {
				login.setVisibleSedes(false);
			}
		} catch (WrongValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mostrarError(e.getMessage());
		} catch (ExcArgumentoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mostrarError(e.getMessage());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mostrarError(e.getMessage());
		} catch (cpc.ares.excepciones.ExcAccesoInvalido e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mostrarError(e.getMessage());
		}
	}
 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map getXLSParameters(boolean passProtected) {
		Map parametrosExportar = new HashMap();
		parametrosExportar.put(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET,Boolean.FALSE);
		parametrosExportar.put(JRXlsExporterParameter.IS_DETECT_CELL_TYPE,Boolean.TRUE);
		parametrosExportar.put(JRXlsExporterParameter.IGNORE_PAGE_MARGINS,Boolean.FALSE);
		parametrosExportar.put(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND,Boolean.FALSE);
		parametrosExportar.put(JRXlsExporterParameter.IS_IGNORE_CELL_BACKGROUND,Boolean.FALSE);
		parametrosExportar.put(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED,	Boolean.TRUE);
		parametrosExportar.put(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_COLUMNS,Boolean.TRUE);
		parametrosExportar.put(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,Boolean.TRUE);
		parametrosExportar.put(JRXlsExporterParameter.IS_IGNORE_CELL_BORDER,Boolean.FALSE);
		parametrosExportar.put(JRXlsExporterParameter.IS_COLLAPSE_ROW_SPAN,Boolean.TRUE);
 
		if (passProtected)
			parametrosExportar.put(JRXlsExporterParameter.PASSWORD, "auditor");
		return parametrosExportar;
	}

	public void aceptar() {
		try {
			String txtLogin = login.getJ_username().getValue();
			String txtClave = login.getJ_password().getValue();
			
			usuario = PerUsuario.verificarUsuarioXSede(txtLogin, txtClave, (Sede) login.getCmbSedes().getSeleccion(), sistema, this);
			Sede sedeSelec = (Sede) login.getCmbSedes().getSeleccion();
			login.quitarModal();
			cpc.modelo.sigesp.basico.Sede sedeEvaluar = NegocioSede.getInstance().getSede(
							sedeSelec.getEmpresa().getCodigo(),
							sedeSelec.getIdSede());
			if (!sedeEvaluar.getId().getId().trim().equals(usuario.getSede().getIdSede().trim())) {
				this.mostrarAdvertencia("Su Identidad de Usuario no esta Asociado a la Sede Actual. Esto impedira que realice algunas tareas dentro de la Aplicacion. ");
			}
			usuario.setSede(sedeSelec);
			setSede(usuario.getSede());
			setUsuario(usuario);
			configurarEscritorio();
			Sessions.getCurrent().setAttribute("usuario",usuario);
		}
		catch (ExcAccesoInvalido e) {
			mostrarError("Usuario o Clave Incorrecta");
		}
		catch (Exception e) {
			e.printStackTrace();
			mostrarError(e.getMessage());
		}
	}

	public void cancelar() {

	}

	public Integer getSistema() {
		return sistema;
	}

	public void setSistema(Integer sistema) {
		this.sistema = sistema;
	}

	public Index getPadre() {
		return padre;
	}

	public void setPadre(Index padre) {
		this.padre = padre;
	}

	public void onEvent(Event event) throws Exception {

		if (event.getName() == Events.ON_CLICK) {
			if (event.getTarget().getId() == CompLogin.BOTONACEPTAR) { // boton
				// ACEPTAR
				aceptar();
			} else if (event.getTarget().getId() == CompLogin.BOTONRECETEAR) { // boton
				// ACEPTAR
				cancelar();
			} else if (event.getTarget() == login.getCaptcha()) { // boton
				// ACEPTAR
				login.getCaptcha().randomValue();
			}
		}
		if (event.getName() == Events.ON_BLUR) {
			if (event.getTarget().getId() == "u") { // TextBox de Usuarios
				mostrarListaSedes();
			}
		}
		if (event.getName() == Events.ON_OK) {
			if (event.getTarget().getId() == login.getId()) { // TextBox de
																// Usuarios
				// ACEPTAR
				aceptar();
			}
		}

	}

	public Window getWin() {
		return win;
	}

	public void setWin(Window win) {
		this.win = win;
	}

	public Vbox getContenedor() {
		return contenedor;
	}

	public void setContenedor(Vbox contenedor) {
		this.contenedor = contenedor;
	}

	public void quitarComponente(Component componente) {
		this.contenedor.removeChild(componente);
	}

	public Jasperreport getReporte() {
		return reporte;
	}

	public void setReporte(Jasperreport reporte) {
		this.reporte = reporte;
	}

	public void setContextProperty(String arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

	public void agregarVentanaPopup(CompVentana arg0) {
		// TODO Auto-generated method stub

	}
	// Para la Fecha de Control 
	public void setFechaControl(String fecha)
	{
		padre.getFechaControl().setValue("Fecha Cierre  :"+fecha);
	}
	
	public String getIp() {
		return Sessions.getCurrent().getClientAddr();
	}

	public String getNombreUsuario() {
		// TODO Auto-generated method stub
		return getUsuario().getNombre();
	}

	 
	public void setDatosUsuario(String datosUsuario) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reiniciarInfUsaurio() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restablecerEscritorio() {
		// TODO Auto-generated method stub
		
	}

	public void configurarBaseDatos() throws Exception {
		SingleConnectionDataSource dataSource = (SingleConnectionDataSource) SpringUtil.getBean("dataSource"); 
		Configuration cfg = new AnnotationConfiguration().configure("/hibernate.cfg.xml");
		cfg.setProperty("hibernate.connection.url",dataSource.getUrl());
		cfg.setProperty("hibernate.connection.username",dataSource.getUsername());
	 	cfg.setProperty("hibernate.connection.password",dataSource.getPassword());
	 	SessionDao.configurarNuevaSessionFactory(cfg);
		this.setSede(sede);
	}

	@Override
	public boolean conAccesoMultisede(int idUsuario) {
		// TODO Auto-generated method stub
		return false;
	}

}
