package cpc.demeter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.GenericAutowireComposer;
import org.zkoss.zkex.zul.Fisheye;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zkex.zul.North;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import cpc.ares.interfaz.IMenu;
import cpc.ares.modelo.Accion;
import cpc.ares.modelo.Sede;
import cpc.ares.modelo.UnidadFuncional;
import cpc.ares.modelo.Usuario;
import cpc.demeter.controlador.mantenimiento.ContUsuarioGarantia;
import cpc.demeter.controlador.mantenimiento.garantia.ContGarantia;
import cpc.demeter.controlador.mantenimiento.garantia.ContSelecionarCliente;
import cpc.modelo.demeter.mantenimiento.EnteExterno;
import cpc.modelo.demeter.mantenimiento.Garantia;
import cpc.modelo.demeter.mantenimiento.UsuarioMantenimiento;
import cpc.negocio.demeter.mantenimiento.NegocioGarantia;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.ventanas.CompLogin;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ActivacionGarantia extends GenericAutowireComposer implements	IZkAplicacion {

	private static final long serialVersionUID = 1L;
	private Div contenedor;
	private Div divlista;

	private ContUsuarioGarantia contusuariogarantia;
	private Fisheye btnagregar;
	private Fisheye btnusuario;
	private Fisheye btnconsultar;
	private Fisheye btneditar;
	private Fisheye btnpdf;
	private Window padre;
	private Label lblUser;
	private NegocioGarantia servicio;

	private Combobox combBuscar;
	private Textbox txtBuscar;
	private Button btnbuscar;

	private Listbox listbox;
	private List<Garantia> listaGarantia = new ArrayList<Garantia>();
	private Button cerrarSession;
	private UsuarioMantenimiento usuario;

	public Div getContenedor() {
		return contenedor;
	}

	public void setContenedor(Div contenedor) {
		this.contenedor = contenedor;
	}

	public ContUsuarioGarantia getContusuariogarantia() {
		return contusuariogarantia;
	}

	public void setContusuariogarantia(ContUsuarioGarantia contusuariogarantia) {
		this.contusuariogarantia = contusuariogarantia;
	}

	@Override
	public void doFinally() throws Exception {
		super.doFinally();
		servicio = NegocioGarantia.getInstance();
		listbox = crearLista(listaGarantia);
		divlista.appendChild(listbox);

		if (getContusuariogarantia() == null) {
			ContUsuarioGarantia contenedor = new ContUsuarioGarantia(1, this);
			setContusuariogarantia(contenedor);
		}
		this.btnagregar.addEventListener(Events.ON_CLICK, this);
		this.btnusuario.addEventListener(Events.ON_CLICK, this);
		this.btnbuscar.addEventListener(Events.ON_CLICK, this);
		this.btnconsultar.addEventListener(Events.ON_CLICK, this);
		cerrarSession.addEventListener(Events.ON_CLICK, this);
		this.txtBuscar.addEventListener(Events.ON_OK, this);
		this.btneditar.addEventListener(Events.ON_CLICK, this);
		this.btnpdf.addEventListener(Events.ON_CLICK, this);
	}

	@Override
	public void onEvent(Event evento) throws Exception {
		super.onEvent(evento);

		if (evento.getTarget() == btnagregar) 
		{
			new ContGarantia(Accion.AGREGAR, null, this);
		}
		else if (evento.getTarget() == btnusuario)
		{
			ContSelecionarCliente cont = new ContSelecionarCliente(Accion.CONSULTAR, this);
			this.agregarAEscritorio(cont.getVista());
			cont.getVista().doModal();

		} 
		else if (btnbuscar == evento.getTarget()|| txtBuscar == evento.getTarget())
		{
			buscar();
		}
		else if (btnconsultar == evento.getTarget()) 
		{
			if (this.listbox.getSelectedItem() != null) 
			{
				Garantia garantia = (Garantia) this.listbox.getSelectedItem().getValue();
				new ContGarantia(Accion.CONSULTAR,garantia, this);
			}
			else
			{
				this.mostrarError("Debe Seleccionar  1 Items ");
			}
		} else if (btneditar == evento.getTarget()) {
			if (this.listbox.getSelectedItem() != null) {

				Garantia garantia = (Garantia) this.listbox.getSelectedItem()
						.getValue();
				if (garantia.getEstatus() == 0) {
					new ContGarantia(Accion.EDITAR,garantia, this);
				} else {
					mostrarError("Esta Garantia ya ah Sido Generada no puede Modificarla");
				}
			} else {
				this.mostrarError("Debe Seleccionar  1 Items ");
			}

		} else if (cerrarSession == evento.getTarget()) {
			setContusuariogarantia(null);
			Executions.sendRedirect("/ActivacionGarantia.zul");

		} else if (btnpdf == evento.getTarget()) {
			if (this.listbox.getSelectedItem() != null) {
				Garantia garantia = (Garantia) this.listbox.getSelectedItem()
						.getValue();
				if (garantia.getEstatus() == 0) {
				    new ContGarantia(Accion.IMPRIMIR_ITEM,garantia, this);
				} else {
					new ContGarantia(Accion.IMPRIMIR_ITEM,garantia, this);
				}
			} else {
				this.mostrarError("Debe Seleccionar  1 Items ");
			}
		}
	}

	public String getIp() {
		return desktop.getSession().getRemoteAddr();
	}

	public EnteExterno getenteExterno() {
		// TODO Auto-generated method stub
		return contusuariogarantia.getUsuario().getEnte();
	}

	public North getNorte() {
		// return norte;
		return null;
	}

	public void setNorte(North norte) {
		// /this.norte = norte;
	}

	public Listbox refrescarLista(List<Garantia> lista) {
		Listbox listbox = new Listbox();
		listbox.setMold("paging");
		listbox.setPageSize(15);
		// encabezado
		Listhead encabezado = new Listhead();
		Listheader clum1 = new Listheader("id");
		Listheader clum2 = new Listheader("Maquinaria");
		Listheader clum3 = new Listheader("Propietario");
		Listheader clum4 = new Listheader("Estatus");

		encabezado.appendChild(clum1);
		encabezado.appendChild(clum2);
		encabezado.appendChild(clum3);
		encabezado.appendChild(clum4);

		listbox.appendChild(encabezado);
		// Cuerpo
		Iterator<Garantia> iter = lista.iterator();

		while (iter.hasNext()) {

			Garantia garantia = iter.next();
			Listitem cuerpo = new Listitem();
			cuerpo.setValue(garantia);

			Listcell celda1 = new Listcell(garantia.getId().toString());
			Listcell celda2 = new Listcell(garantia.getMaquinaria()
					.getSerialcarroceria());
			Listcell celda3 = new Listcell(garantia.getMaquinaria()
					.getPropietario().getIdentidadLegal());
			Listcell celda4 = new Listcell(garantia.getEstatusgarantia());
			cuerpo.appendChild(celda1);
			cuerpo.appendChild(celda2);
			cuerpo.appendChild(celda3);
			cuerpo.appendChild(celda4);
			listbox.appendChild(cuerpo);
		}
		return listbox;
	}

	public Listbox crearLista(List<Garantia> lista) {
		Listbox listbox = new Listbox();
		listbox.setMold("paging");
		listbox.setPageSize(15);
		// encabezado
		Listhead encabezado = new Listhead();
		Listheader clum1 = new Listheader("id");
		Listheader clum2 = new Listheader("Maquinaria");
		Listheader clum3 = new Listheader("Propietario");
		Listheader clum4 = new Listheader("Estatus");

		encabezado.appendChild(clum1);
		encabezado.appendChild(clum2);
		encabezado.appendChild(clum3);
		encabezado.appendChild(clum4);

		listbox.appendChild(encabezado);
		// Cuerpo
		Iterator<Garantia> iter = lista.iterator();

		while (iter.hasNext()) {

			Garantia garantia = iter.next();
			Listitem cuerpo = new Listitem();
			cuerpo.setValue(garantia);

			Listcell celda1 = new Listcell(garantia.getId().toString());
			Listcell celda2 = new Listcell(garantia.getMaquinaria()
					.getSerialcarroceria());
			Session session = SessionDao.getInstance().getCurrentSession();
			Transaction t = session.beginTransaction();
			session.refresh(garantia.getMaquinaria().getPropietario());
			Listcell celda3 = new Listcell(garantia.getMaquinaria()
					.getPropietario().getIdentidadLegal());
			Listcell celda4 = new Listcell(garantia.getEstatusgarantia());
			t.commit();
			cuerpo.appendChild(celda1);
			cuerpo.appendChild(celda2);
			cuerpo.appendChild(celda3);
			cuerpo.appendChild(celda4);
			listbox.appendChild(cuerpo);
		}
		return listbox;
	}

	public void crearListaGarantia(EnteExterno ente) throws ExcFiltroExcepcion {

		listaGarantia = servicio.getGarantias(ente);
		if (listbox != null) {
			listbox.detach();
		}

		listbox = crearLista(listaGarantia);
		divlista.appendChild(listbox);

	}

	public void cargarGarantia(Garantia garantia, Integer modoOperacion) {
		listaGarantia.add(garantia);
		Listitem cuerpo = new Listitem();
		cuerpo.setValue(garantia);
		Listcell celda1 = new Listcell(garantia.getId().toString());
		Listcell celda2 = new Listcell(garantia.getMaquinaria()
				.getSerialcarroceria());
		Listcell celda3 = new Listcell(garantia.getMaquinaria()
				.getPropietario().getIdentidadLegal());
		Listcell celda4 = new Listcell(garantia.getEstatusgarantia());
		cuerpo.appendChild(celda1);
		cuerpo.appendChild(celda2);
		cuerpo.appendChild(celda3);
		cuerpo.appendChild(celda4);
		listbox.appendChild(cuerpo);
	}

	public void buscar() {
		List<Garantia> lista = new ArrayList<Garantia>();
		String texto = txtBuscar.getValue();

		if (texto.trim().equals("")) {
			listbox.detach();
			listbox = crearLista(listaGarantia);
			divlista.appendChild(listbox);
		} else {
			if (combBuscar.getSelectedItem() != null) {
				String categoria = (String) combBuscar.getSelectedItem()
						.getValue();

				Iterator<Garantia> iter = listaGarantia.iterator();

				while (iter.hasNext()) {
					Garantia garantia = iter.next();

					if (categoria.equals("Maquinaria")) {
						if (garantia.getMaquinaria().getSerialcarroceria()
								.trim().toUpperCase()
								.contains(texto.trim().toUpperCase())) {
							lista.add(garantia);
						}
					} else if (categoria.equals("Propietario")) // chekar q no
																// esta
																// filtrandoo bn
					{
						if (garantia.getMaquinaria().getPropietario()
								.getIdentidadLegal().toUpperCase()
								.contains(texto.trim().toUpperCase())) {
							lista.add(garantia);
						}
					} else if (categoria.equals("Estatus")) {
						if (garantia.getEstatusgarantia().trim()
								.contains(texto.trim())) {
							lista.add(garantia);
						}
					}

				}
				listbox.detach();
				listbox = crearLista(lista);
				divlista.appendChild(listbox);
			}
		}
	}

	public void MostrarUsaurio(UsuarioMantenimiento usuario) {
		// TODO Auto-generated method stub
		this.usuario = usuario;
		cerrarSession.setVisible(true);
		lblUser.setValue("Usuario :" + usuario.getNombres() + " "+ usuario.getApellidos());

	}

	public void ActualizarListaGarantia() {
		listbox.detach();
		listbox = refrescarLista(listaGarantia);
		divlista.appendChild(listbox);
	}

	@Override
	public Component getSelf() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void agregar(Component comp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void agregarReporte() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setMenuIni(IMenu arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void agregarAEscritorio(Component componente) {
		// TODO Auto-generated method stub
		this.padre.appendChild(componente);

	}

	@Override
	public CompLogin getLogin() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setLogin(CompLogin login) {
		// TODO Auto-generated method stub

	}

	@Override
	public void agregarMenu(Component componente) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEscritorio() {
		// TODO Auto-generated method stub

	}

	@Override
	public void agregarHija(Component componente) {
		// TODO Auto-generated method stub

		this.padre.appendChild(componente);
	}

	@Override
	public Jasperreport getReporte() {
		return null;
		// TODO Auto-generated method stub

	}

	public void setReporte(Jasperreport reporte) {
		// TODO Auto-generated method stub

	}

 
	public Map getXLSParameters(boolean passProtected) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mostrarError(String error) {
		// TODO Auto-generated method stub
		alert(error);

	}

	@Override
	public void mostrarInformacion(String informacion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mostrarConfirmacion(String confirmacion) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mostrarImpresion(String pregunta) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getContextProperty(String caracteristica) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void limpiarEscritorio() {
		// TODO Auto-generated method stub

	}

	@Override
	public void cargarInfUsuario(String usuario, Sede sede) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cargarInfUsuario(String usuario, Sede sede,
			List<UnidadFuncional> unidades) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cargarInfUsuario(String usuario, Sede sede, String foto) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reiniciarInfUsaurio() {
		// TODO Auto-generated method stub

	}

	@Override
	public Sede getSede() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getdatosUsuario() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cambiarFuete(int tamano) {
		// TODO Auto-generated method stub

	}

	@Override
	public void configurarEscritorio() {
		// TODO Auto-generated method stub

	}

	@Override
	public void restablecerEscritorio() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUsuario(Usuario usuario) {
		// TODO Auto-generated method stub

	}

	@Override
	public Usuario getUsuario() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSede(Sede sede) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean conAccesoMultisede(int idUsuario) {
		return false;
	}

	public String getNombreUsuario() {
		return getContusuariogarantia().getUsuario().getCedula();
	}

}
