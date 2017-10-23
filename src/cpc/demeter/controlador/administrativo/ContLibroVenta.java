package cpc.demeter.controlador.administrativo;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.Sede;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.administrativo.UILibroVenta;
import cpc.modelo.demeter.administrativo.LibroVenta;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.negocio.demeter.administrativo.NegocioLibroVenta;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcEntradaInvalida;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContLibroVenta extends ContVentanaBase<LibroVenta> {

	private static final long serialVersionUID = -3321414973066647454L;
	private NegocioLibroVenta servicio;
	private UILibroVenta vistaLibro;
	private Sede sede;
	private AppDemeter app;

	public ContLibroVenta(int modoOperacion, LibroVenta libro,ContCatalogo<LibroVenta> llamador, AppDemeter app)throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido {
		super(modoOperacion);
		this.app = app;
		
		if (modoOperacion == Accion.AGREGAR)
		{
			libro = new LibroVenta();
		}
		
		setDato(libro);
		servicio = NegocioLibroVenta.getInstance();
		sede = app.getSede();
		servicio.setLibro(libro);
		
		setear(servicio.getLibro(), new UILibroVenta("LIBRO VENTAS ("+ Accion.TEXTO[modoOperacion] + ")"), llamador, this.app);

		vistaLibro = (UILibroVenta) getVista();
		vistaLibro.desactivar(modoOperacion);
		vistaLibro.getSede().setValue(sede.getNombre());
	}

	public void eliminar() {

		try {
			servicio.setLibro(getDato());
			servicio.eliminar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {

		try {
			getVista().actualizarModelo();
			cargarModelo();
			servicio.setLibro(getDato());
			servicio.guardar();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

	public void cargarModelo() {
		/*
		 * getDato().setDetalles(vistaFactura.getModeloDetalle());
		 * getDato().setBeneficiario
		 * (vistaFactura.getRazonSocial().getValorObjeto());
		 * getDato().setDireccionBeneficiario
		 * (vistaFactura.getDireccion().getSeleccion());
		 * getDato().setMontoSaldo(getDato().getMontoTotal());
		 */
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

	}

	private void procesarlibro() throws WrongValueException,ExcEntradaInvalida, ExcEntradaInconsistente {

		System.out.println("Año : " + vistaLibro.getAno().getValue() + "----"+ vistaLibro.getMes().getSelectedItem().getAttribute("dato"));
		
		
		setDato(servicio.nuevoLibro(vistaLibro.getAno().getValue(),	(Integer) vistaLibro.getMes().getSelectedItem().getAttribute("dato")));
		vistaLibro.actualizarDetalle(getDato().getDetalles());
	
	}

	public void onEvent(Event event) 
	{
		try {

			if (event.getTarget() == getVista().getAceptar()) 
			{
				procesarCRUD(event);
			} 
			else
			{
				procesarlibro();
			}
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	}

	public void actualizarDatos() {

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

	}

}
