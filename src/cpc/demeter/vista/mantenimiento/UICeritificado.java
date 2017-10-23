package cpc.demeter.vista.mantenimiento;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.mantenimiento.CertificadoOrigen;
import cpc.modelo.demeter.mantenimiento.MaquinariaExterna;
import cpc.modelo.demeter.mantenimiento.PlantaDistribuidora;

import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UICeritificado extends CompVentanaBase<CertificadoOrigen> {

	private CompGrupoDatos datosGeneral;

	private Label nroControl; // que genera
	private CompBuscar<PlantaDistribuidora> plantaOrigen;
	private CompBuscar<MaquinariaExterna> maquinariaExterna;
	private Textbox nroFactura;

	private List<PlantaDistribuidora> modeloPlanta;
	private List<MaquinariaExterna> modeloMaquinaria;

	public UICeritificado(String titulo, int ancho,
			List<MaquinariaExterna> modeloM,
			List<PlantaDistribuidora> modeloPlanta) {
		super(titulo, ancho);
		this.modeloMaquinaria = modeloM;
		this.modeloPlanta = modeloPlanta;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub

		datosGeneral = new CompGrupoDatos("Datos ", 2);
		nroControl = new Label(); // que genera

		plantaOrigen = new CompBuscar<PlantaDistribuidora>(
				cargarEncabezadoplanta(), 1);
		plantaOrigen.setModelo(modeloPlanta);
		plantaOrigen.setAncho(400);
		plantaOrigen.setWidth("400px");

		maquinariaExterna = new CompBuscar<MaquinariaExterna>(
				cargarEncabezadomaquinaria(), 4);
		maquinariaExterna.setModelo(modeloMaquinaria);
		maquinariaExterna.setAncho(600);
		maquinariaExterna.setWidth("400px");

		nroFactura = new Textbox();

		maquinariaExterna.addEventListener(CompBuscar.ON_SELECCIONO,
				getControlador());

	}

	public CompBuscar<MaquinariaExterna> getMaquinariaExterna() {
		return maquinariaExterna;
	}

	public void setMaquinariaExterna(
			CompBuscar<MaquinariaExterna> maquinariaExterna) {
		this.maquinariaExterna = maquinariaExterna;
	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub

		datosGeneral.addComponente("nroContro", nroControl);
		datosGeneral.addComponente("Nro Factura", nroFactura);
		datosGeneral.addComponente("Maquinaria :", maquinariaExterna);
		datosGeneral.addComponente("Planta ", plantaOrigen);
		datosGeneral.dibujar(this);
		addBotonera();

	}

	public CompGrupoDatos getDatosGeneral() {
		return datosGeneral;
	}

	public void setDatosGeneral(CompGrupoDatos datosGeneral) {
		this.datosGeneral = datosGeneral;
	}

	public Label getNroControl() {
		return nroControl;
	}

	public void setNroControl(Label nroControl) {
		this.nroControl = nroControl;
	}

	public CompBuscar<PlantaDistribuidora> getPlantaOrigen() {
		return plantaOrigen;
	}

	public void setPlantaOrigen(CompBuscar<PlantaDistribuidora> plantaOrigen) {
		this.plantaOrigen = plantaOrigen;
	}

	public Textbox getNroFactura() {
		return nroFactura;
	}

	public void setNroFactura(Textbox nroFactura) {
		this.nroFactura = nroFactura;
	}

	public List<MaquinariaExterna> getModeloMaquinaria() {
		return modeloMaquinaria;
	}

	public void setModeloMaquinaria(List<MaquinariaExterna> modeloMaquinaria) {
		this.modeloMaquinaria = modeloMaquinaria;
	}

	@Override
	public void cargarValores(CertificadoOrigen dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub

		getBinder().addBinding(plantaOrigen, "seleccion",getNombreModelo() + ".plantaOrigen", null, null, "save", null,null, null, null);
		getBinder().addBinding(maquinariaExterna, "seleccion",getNombreModelo() + ".maquinariaExterna", null, null, "save",null, null, null, null);
		getBinder().addBinding(nroFactura, "value",	getNombreModelo() + ".nroFactura", null, null, "save", null,null, null, null);

		nroControl.setValue(getModelo().getNroControl());

		// maquinaria original
		if(getModelo().getMaquinariaExterna() != null )
		{
				modeloMaquinaria.add(getModelo().getMaquinariaExterna());
		}

		maquinariaExterna.setModelo(modeloMaquinaria);
		maquinariaExterna.setSeleccion(getModelo().getMaquinariaExterna());
		
		plantaOrigen.setSeleccion(getModelo().getPlantaOrigen());
		nroFactura.setValue(getModelo().getNroFactura());

	}

	public List<CompEncabezado> cargarEncabezadomaquinaria() {
		CompEncabezado titulo;

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		titulo = new CompEncabezado("Marca ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStr_Marca");
		encabezado.add(titulo);

		titulo = new CompEncabezado(" Modelo", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcionModelo");
		encabezado.add(titulo);

		titulo = new CompEncabezado(" Propietario ", 130);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrPropietario");
		encabezado.add(titulo);

		titulo = new CompEncabezado(" Serial Motor  ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getSerialMotor");
		encabezado.add(titulo);

		titulo = new CompEncabezado(" Serial Carroceria ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getSerialcarroceria");
		encabezado.add(titulo);

		return encabezado;

	}

	// /flojeraaa!! voy a usar el

	public List<CompEncabezado> cargarEncabezadoplanta() {
		CompEncabezado titulo;

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("RIF", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado(" Nombre  ", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Dirección", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDireccion");
		encabezado.add(titulo);

		return encabezado;

	}

	public List<PlantaDistribuidora> getModeloPlanta() {
		return modeloPlanta;
	}

	public void setModeloPlanta(List<PlantaDistribuidora> modeloPlanta) {
		this.modeloPlanta = modeloPlanta;
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR
				|| modoOperacion == Accion.PROCESAR) {
			activarConsulta();
		} else {
			modoEdicion();
		}
		if (modoOperacion == Accion.AGREGAR) {
			maquinariaExterna.setDisabled(false);
		}
		if (modoOperacion == Accion.PROCESAR) {
			aceptar.setDisabled(false);
		}
	}

	public void activarConsulta() {
		aceptar.setDisabled(true);
		nroFactura.setDisabled(true);
		plantaOrigen.setDisabled(true);
		maquinariaExterna.setDisabled(true);

	}

	public void modoEdicion() {
		aceptar.setDisabled(false);
		nroFactura.setDisabled(false);
		plantaOrigen.setDisabled(false);
		maquinariaExterna.setDisabled(true);
	}

}
