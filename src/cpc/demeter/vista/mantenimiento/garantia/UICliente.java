package cpc.demeter.vista.mantenimiento.garantia;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.ws.Holder;

import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.ListModelArray;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.demeter.ActivacionGarantia;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.ministerio.basico.Telefono;
import cpc.modelo.ministerio.basico.TipoProductor;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UICliente extends CompVentanaBase<Cliente> {

	private Intbox id;
	private Textbox documentoIdentidad;
	private Textbox nombres;
	private Textbox direccion;
	private Listbox telefonos;
	private Div divTelefono;
	private List<Telefono> modelo;
	private CompGrupoDatos div;
	private Button agregar;
	private Button quitar;
	private Combobox tipoidentidad; // / para el V- E- G- J-
	private ActivacionGarantia app;

	public ActivacionGarantia getApp() {
		return app;
	}

	public void setApp(ActivacionGarantia app) {
		this.app = app;
	}

	public UICliente() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UICliente(String titulo, int ancho) {
		super(titulo, ancho);
	}

	public UICliente(String titulo) {
		super(titulo);
		// TODO Auto-generated constructor stub
	}

	public Button getAgregar() {
		return agregar;
	}

	public Listbox getTelefonos() {
		return telefonos;
	}

	public void setTelefonos(Listbox telefonos) {
		this.telefonos = telefonos;
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

	public Textbox getNombres() {
		return nombres;
	}

	public void setNombres(Textbox nombres) {
		this.nombres = nombres;
	}

	public void setId(Intbox id) {
		this.id = id;
	}

	public Combobox getTipoidentidad() {
		return tipoidentidad;
	}

	public void setTipoidentidad(Combobox tipoidentidad) {
		this.tipoidentidad = tipoidentidad;
	}

	public Textbox getDocumentoIdentidad() {
		return documentoIdentidad;
	}

	public void setDocumentoIdentidad(Textbox documentoIdentidad) {
		this.documentoIdentidad = documentoIdentidad;
	}

	public Textbox getDireccion() {
		return direccion;
	}

	public void setDireccion(Textbox direccion) {
		this.direccion = direccion;
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		agregar = new Button("+");
		quitar = new Button("-");
		id = new Intbox();
		id.setDisabled(true);
		documentoIdentidad = new Textbox();
		documentoIdentidad.setMaxlength(18);

		nombres = new Textbox();
		nombres.setMaxlength(250);
		direccion = new Textbox();
		direccion.setMaxlength(100);
		div = new CompGrupoDatos();
		if (getModelo().getTelefonos() == null) {
			getModelo().setTelefonos(new ArrayList<Telefono>());
		}
		modelo = getModelo().getTelefonos();
		telefonos = InicializarLista();
		divTelefono = new Div();
		agregar.addEventListener(Events.ON_CLICK, getControlador());
		quitar.addEventListener(Events.ON_CLICK, getControlador());

		tipoidentidad = new Combobox();
		Comboitem combitem = new Comboitem("V-");
		combitem.setValue("V-");
		Comboitem combitem2 = new Comboitem("E-");
		combitem2.setValue("E-");
		Comboitem combitem3 = new Comboitem("J-");
		combitem3.setValue("J-");
		Comboitem combitem4 = new Comboitem("G-");
		combitem4.setValue("G-");

		tipoidentidad.appendChild(combitem);
		tipoidentidad.appendChild(combitem2);
		tipoidentidad.appendChild(combitem3);
		tipoidentidad.appendChild(combitem4);

	}

	public List<CompEncabezado> cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("ID", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Codigo Area", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCodigoArea");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Numero", 550);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNumero");
		encabezado.add(titulo);

		return encabezado;
	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub

		id.setWidth("99%");
		div.addComponente("ID", id);

		documentoIdentidad.setWidth("99%");
		Hbox divisor = new Hbox();
		divisor.appendChild(tipoidentidad);
		divisor.appendChild(documentoIdentidad);
		tipoidentidad.setWidth("20%");
		documentoIdentidad.setWidth("80%");

		div.addComponente("Documento Identidad", divisor);

		nombres.setWidth("99%");
		div.addComponente("Nombres :", nombres);

		direccion.setWidth("99%");
		direccion.setRows(3);
		div.addComponente("Dirección : ", direccion);

		Div hbox = new Div();
		hbox.appendChild(agregar);
		hbox.appendChild(quitar);
		divTelefono.appendChild(telefonos);
		div.addComponente(hbox, divTelefono);
		div.dibujar(this);
		addBotonera();

	}

	@Override
	public void cargarValores(Cliente dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub

		id.setValue(getModelo().getId());
		if (getModelo().getIdentidadLegal() != null) {
			documentoIdentidad.setValue(getModelo().getIdentidadLegal()
					.substring(2, getModelo().getIdentidadLegal().length()));
		}
		nombres.setValue(getModelo().getNombres());
		direccion.setValue(getModelo().getDireccion());

		// Seleccionar el Idtipo Legal :-D
		if (getModelo().getIdentidadLegal() != null) {
			String tipo = getModelo().getIdentidadLegal().substring(0, 2);
			tipo = tipo.trim().toUpperCase();
			List lista = tipoidentidad.getItems();
			Iterator iter = lista.iterator();
			while (iter.hasNext()) {
				Comboitem item = (Comboitem) iter.next();
				if (tipo.equals(item.getValue())) {
					tipoidentidad.setSelectedItem(item);
					break;
				}
			}
		}

		getBinder().addBinding(id, "value", getNombreModelo() + ".id", null,
				null, "save", null, null, null, null);
		getBinder().addBinding(documentoIdentidad, "value",
				getNombreModelo() + ".identidadLegal", null, null, "save",
				null, null, null, null);
		getBinder().addBinding(nombres, "value",
				getNombreModelo() + ".nombres", null, null, "save", null, null,
				null, null);
		getBinder().addBinding(direccion, "value",
				getNombreModelo() + ".direccion", null, null, "save", null,
				null, null, null);
		modelo = getModelo().getTelefonos();
		telefonos.detach();
		telefonos = InicializarLista();
		divTelefono.appendChild(telefonos);

		actualizarModelo();
	}

	public Listbox InicializarLista() {
		Listbox lista = new Listbox();
		lista.setMold("paging");
		lista.setPageSize(10);
		lista.setHeight("150px");
		lista.setWidth("98%");

		Listhead titulo = new Listhead();

		Listheader column1 = new Listheader("ID ");
		column1.setWidth("20%");
		column1.setAlign("center");
		column1.setSort("auto");

		Listheader column2 = new Listheader("Codigo Area ");
		column2.setWidth("30%");
		column2.setAlign("center");

		Listheader column3 = new Listheader("Numero ");
		column3.setWidth("30%");
		column3.setAlign("center");
		column3.setSort("auto");

		titulo.appendChild(column1);
		titulo.appendChild(column2);
		titulo.appendChild(column3);

		lista.appendChild(titulo);

		Iterator<Telefono> iter = modelo.iterator();
		while (iter.hasNext()) {
			Telefono telefono = iter.next();
			Listitem fila = new Listitem();
			fila.setValue(telefono);
			Listcell celda1 = new Listcell((telefono.getId() == null ? " "
					: telefono.getId().toString()));
			Listcell celda2 = new Listcell(telefono.getCodigoArea()
					.getCodigoArea());
			Listcell celda3 = new Listcell(telefono.getNumero());
			fila.appendChild(celda1);
			fila.appendChild(celda2);
			fila.appendChild(celda3);
			lista.appendChild(fila);
		}
		return lista;
	}

	public void settelefono(Telefono telefono) {
		// TODO Auto-generated method stub
		modelo.add(telefono);
		// Actualizar la lista :-D
		telefonos.detach();
		telefonos = InicializarLista();
		divTelefono.appendChild(telefonos);
	}

	public void quitarTelefono(Telefono telefono) {
		// TODO Auto-generated method stub

		for (int i = 0; i < modelo.size(); i++) {
			if (modelo.get(i).toString().equals(telefono.toString()))
				modelo.remove(i);
		}

		telefonos.detach();
		telefonos = InicializarLista();
		divTelefono.appendChild(telefonos);
	}

	public List<Telefono> gettelefonosmodelo() {
		// TODO Auto-generated method stub
		return modelo;
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			activarConsulta();
		else if (modoOperacion == Accion.EDITAR) {
			documentoIdentidad.setDisabled(true);
			modoEdicion();
		}
	}

	public void activarConsulta() {
		documentoIdentidad.setDisabled(true);
		nombres.setDisabled(true);
		direccion.setDisabled(true);
		tipoidentidad.setDisabled(true);

		agregar.setVisible(false);
		quitar.setVisible(false);
		getAceptar().setVisible(false);

	}

	public void modoEdicion() {
		documentoIdentidad.setDisabled(true);
		nombres.setDisabled(false);
		direccion.setDisabled(false);
		tipoidentidad.setDisabled(false);

		agregar.setVisible(true);
		quitar.setVisible(true);
		getAceptar().setVisible(true);
	}

	public String getIdentidadLegalProcesada() {

		String cadena = documentoIdentidad.getValue().trim();
		String primero2 = cadena.substring(0, 2);

		List lista = tipoidentidad.getItems();
		Iterator iter = lista.iterator();
		while (iter.hasNext()) {
			Comboitem item = (Comboitem) iter.next();
			if (primero2.equals(item.getLabel())) {
				return cadena;
			}
		}
		String seleccion = (String) tipoidentidad.getSelectedItem().getValue();
		return (seleccion + cadena).trim().toUpperCase();
	}

}
