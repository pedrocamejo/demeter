package cpc.demeter.vista.mantenimiento.garantia;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Textbox;

import cpc.modelo.demeter.basico.CodigoArea;
import cpc.modelo.ministerio.basico.Telefono;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UITelefono extends CompVentanaBase<Telefono> {

	private Intbox id;
	private CompBuscar<CodigoArea> codigoArea;
	private Textbox numero;
	private Checkbox celular;
	private CompGrupoDatos div;
	private List<CodigoArea> codigoAreas;

	public UITelefono() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UITelefono(String titulo, int ancho) {
		super(titulo, ancho);
		// TODO Auto-generated constructor stub

	}

	public UITelefono(String titulo) {
		super(titulo);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub

		id = new Intbox();
		id.setDisabled(true);

		codigoArea = new CompBuscar<CodigoArea>(cargarEncabezado(), 3);
		codigoArea.setModelo(codigoAreas);
		numero = new Textbox();
		numero.setMaxlength(10);
		celular = new Checkbox();
		div = new CompGrupoDatos();

	}

	public List<CompEncabezado> cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("ID", 5);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo", 5);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getTipo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Codigo Area", 5);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getCodigoArea");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Descripón ", 5);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		return encabezado;
	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub
		div.addComponente(" ID ", id);
		codigoArea.setAncho(400);
		div.addComponente("Codigo Area", codigoArea);

		div.addComponente("Numero", numero);
		div.addComponente("Celular", celular);
		div.dibujar(this);
		try {
			this.cargar();
		} catch (ExcDatosInvalidos e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		addBotonera();
	}

	@Override
	public void cargarValores(Telefono dato) throws ExcDatosInvalidos {

		getBinder().addBinding(id, "value", getNombreModelo() + ".id", null,
				null, "save", null, null, null, null);
		getBinder().addBinding(numero, "value", getNombreModelo() + ".numero",
				null, null, "save", null, null, null, null);
		getBinder().addBinding(celular, "checked",
				getNombreModelo() + ".celular", null, null, "save", null, null,
				null, null);
		getBinder().addBinding(codigoArea, "seleccion",
				getNombreModelo() + ".codigoArea", null, null, "save", null,
				null, null, null);

	}

	public CompBuscar<CodigoArea> getCodigoArea() {
		return codigoArea;
	}

	public void setCodigoArea(CompBuscar<CodigoArea> codigoArea) {
		this.codigoArea = codigoArea;
	}

	public List<CodigoArea> getCodigoAreas() {
		return codigoAreas;
	}

	public void setCodigoAreas(List<CodigoArea> codigoAreas) {
		this.codigoAreas = codigoAreas;
	}

	public void setId(Intbox id) {
		this.id = id;
	}

	public Textbox getNumero() {
		return numero;
	}

	public void setNumero(Textbox numero) {
		this.numero = numero;
	}

	public Checkbox getCelular() {
		return celular;
	}

	public void setCelular(Checkbox celular) {
		this.celular = celular;
	}

}
