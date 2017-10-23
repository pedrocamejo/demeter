package cpc.demeter.controlador.administrativo;

import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContFacturas;
import cpc.demeter.vista.administrativo.UIFacturaVieja;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.ministerio.basico.Telefono;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.negocio.demeter.administrativo.NegocioFactura;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContFacturaVieja extends ContVentanaBase<DocumentoFiscal> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioFactura servicio;
	private AppDemeter app;
	private Date inicio;

	public ContFacturaVieja(int modoOperacion, DocumentoFiscal docu,
			ContFacturas llamador, AppDemeter app) throws ExcDatosInvalidos,
			ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			servicio = NegocioFactura.getInstance();
			inicio = servicio.getFechaInico();
			List<Cliente> clientes = null;
			try {
				clientes = servicio.getClientes();
			} catch (ExcFiltroExcepcion e) {
				e.printStackTrace();
			}

			setear(docu,
					new UIFacturaVieja("FACTURA VIEJA ("
							+ Accion.TEXTO[modoOperacion] + ")", 700, clientes,
							inicio), llamador, this.app);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
		}
	}

	public void eliminar() {
	}

	public void guardar() {
		try {
			servicio.setFactura(getDato());
			servicio.guardar(getDato());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		UIFacturaVieja vista = (UIFacturaVieja) getVista();
		if (vista.getControl().getValue() == null)
			throw new WrongValueException(vista.getControl(), "Valor no valido");
		if (vista.getCedula().getSeleccion() == null)
			throw new WrongValueException(vista.getCedula(), "Valor no valido");
		if (vista.getTotal().getValue() == null)
			throw new WrongValueException(vista.getTotal(), "Monto no valido");
		if (vista.getSaldo().getValue() == null)
			throw new WrongValueException(vista.getSaldo(), "Monto no valido");
		if (vista.getFecha().getValue() == null)
			throw new WrongValueException(vista.getFecha(), "Valor no valido");
		if (vista.getTotal().getValue() < vista.getSaldo().getValue())
			throw new WrongValueException(vista.getTotal(),
					"Valor no valido, menor a saldo");
	}

	public void actualizarDatos() throws ExcFiltroExcepcion {
		UIFacturaVieja vistaFactura = (UIFacturaVieja) getVista();
		Cliente cliente = vistaFactura.getCedula().getValorObjeto();
		if (cliente != null) {
			cliente = servicio.getCliente(cliente);
			try {
				if (cliente.getTelefonos().size() >= 0) {
					Telefono tele = cliente.getTelefonos().get(0);
					vistaFactura.getTelefono().setValue(
							"(" + tele.getCodigoArea().getCodigoArea() + ")"
									+ tele.getNumero());
				}
			} catch (IndexOutOfBoundsException e) {
			}
			vistaFactura.getRazonSocial().setValue(cliente.getNombres());
			vistaFactura.getDireccion().setValue(cliente.getDireccion());
		}
	}

	public void onEvent(Event event) throws Exception {
		System.out.println("hay evento " + event.getName() + " en "
				+ event.getTarget().getClass().getName());
		try {
			if (event.getTarget() == getVista().getAceptar())
				procesarCRUD(event);
			else if (event.getName().equals(CompBuscar.ON_SELECCIONO))
				actualizarDatos();

		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
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

	}

}
