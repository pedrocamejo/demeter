package cpc.demeter.controlador.mantenimiento;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.mantenimiento.ContOrigen;
import cpc.demeter.vista.mantenimiento.UICeritificado;
import cpc.modelo.demeter.mantenimiento.CertificadoOrigen;
import cpc.modelo.demeter.mantenimiento.MaquinariaExterna;
import cpc.modelo.demeter.mantenimiento.PlantaDistribuidora;
import cpc.negocio.demeter.mantenimiento.NegocioCertificadoOrigen;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContCertificado extends ContVentanaBase<CertificadoOrigen> {

	private static final long serialVersionUID = 1180434079481225766L;
	private NegocioCertificadoOrigen servicio;
	private UICeritificado vista;
	private AppDemeter app;

	public ContCertificado(int modoOperacion, CertificadoOrigen dato,ContOrigen llamador, AppDemeter app) throws ExcDatosInvalidos
	{
		super(modoOperacion);
		this.app = app;
		this.servicio = NegocioCertificadoOrigen.getInstance();
	
		if(modoAgregar())
		{
			dato = new CertificadoOrigen();
		}
		
		List<PlantaDistribuidora> modeloPlanta = new ArrayList<PlantaDistribuidora>();
		
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.EDITAR || modoOperacion == Accion.PROCESAR)
		{
			modeloPlanta = servicio.getplantas(dato.getMaquinariaExterna().getTipo().getModelo());
		}
		
		List<MaquinariaExterna> modeloMaquinaria = servicio.getMaquinariaVendidaSinCertificado();

		setear(dato, new UICeritificado("Certificado  ("+ Accion.TEXTO[modoOperacion] + ")", 550, modeloMaquinaria,	modeloPlanta), llamador, this.app);
		this.vista = (UICeritificado) getVista();
		vista.desactivar(modoOperacion);
	}

	@Override
	public void eliminar() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		try {

			servicio.Guardar(getDato());

		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
			throw new ExcFiltroExcepcion("ERROR AL GUARDAR");
		}
	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

		if (vista.getNroFactura().getValue().trim().length() == 0) {
			throw new WrongValueException(vista.getNroFactura(),
					"Indique una Factura");
		}
		if (vista.getPlantaOrigen().getSeleccion() == null) {
			throw new WrongValueException(vista.getPlantaOrigen(),
					"Seleccióne una Planta");
		}
		if (vista.getMaquinariaExterna().getSeleccion() == null) {
			throw new WrongValueException(vista.getMaquinariaExterna(),
					"Seleccione una Maquinaria");
		}

	}

	public void onEvent(Event event) throws Exception {

		if (event.getTarget() == getVista().getAceptar()) {
			procesarCRUD(event);
		} else if (event.getName() == CompBuscar.ON_SELECCIONO) {
			if (vista.getMaquinariaExterna() == event.getTarget()) {

				MaquinariaExterna maquinaria = vista.getMaquinariaExterna()
						.getSeleccion();
				if (maquinaria != null) {
					List<PlantaDistribuidora> plantas = servicio
							.getplantas(maquinaria.getTipo()
									.getModelo());
					vista.setModeloPlanta(plantas);
					vista.getPlantaOrigen().setModelo(vista.getModeloPlanta());
					vista.getPlantaOrigen().setValue("");
				} else {
					vista.getPlantaOrigen().setValue("");
				}

			}

		}

	}

	@Override
	public void anular() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub

		try {
			int dato = Messagebox.show("Deseas Procesar este Certificado ?",
					"Procesar ", Messagebox.YES | Messagebox.NO,
					Messagebox.INFORMATION);
			if (dato == Messagebox.YES) {
				servicio.procesar(getDato());
				vista.close();

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
