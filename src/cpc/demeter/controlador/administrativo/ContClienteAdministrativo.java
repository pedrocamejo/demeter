package cpc.demeter.controlador.administrativo;

import java.util.List;

import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContClientesAdministrativos;
import cpc.demeter.vista.administrativo.UIClienteAdministrativo;
import cpc.modelo.demeter.administrativo.ClienteAdministrativo;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.demeter.vistas.ClienteAdministrativoView;
import cpc.negocio.demeter.administrativo.NegocioClienteAdministrativo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContClienteAdministrativo extends
		ContVentanaBase<ClienteAdministrativo> {

	private static final long serialVersionUID = -6731889375711288897L;
	private NegocioClienteAdministrativo servicio;
	private ContClientesAdministrativos llamadorR;
	private AppDemeter app;
	private UIClienteAdministrativo vistaCliente;

	public ContClienteAdministrativo(int modoOperacion,
			ClienteAdministrativo cliente,
			ContClientesAdministrativos llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			servicio = NegocioClienteAdministrativo.getInstance();
			this.llamadorR = llamador;
			this.setDato(cliente);
			if (modoAgregar()) {
				List<ClienteAdministrativo> clientes = servicio.getNuevos();
				setVista(new UIClienteAdministrativo("CLIENTE ADMINISTRATIVO ("
						+ Accion.TEXTO[modoOperacion] + ")", 550,
						Accion.AGREGAR, clientes));
			} else {
				try {
					List<DocumentoFiscal> afavor = servicio
							.getSaldoFavor(cliente.getCliente());
					List<DocumentoFiscal> deudas = servicio
							.getSaldoPendiente(cliente.getCliente());
					List<Recibo> recibos = servicio.getSaldoRecibo(cliente.getCliente());
					setVista(new UIClienteAdministrativo(
							"CLIENTE ADMINISTRATIVO ("
									+ Accion.TEXTO[modoOperacion] + ")", 550,
							Accion.CONSULTAR, afavor, deudas,recibos));
					setAppa(app);
					getVista().setModelo(cliente);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			vistaCliente = (UIClienteAdministrativo) getVista();
			this.getVista().setControlador(this);
			this.getVista().dibujarVentana();
			this.getVista().cargar();
			app.agregar(this.getVista());
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		}
	}

	public void eliminar() throws ExcFiltroExcepcion {

	}

	public void guardar() throws ExcFiltroExcepcion {
		if (modoAgregar()) {
			getVista().actualizarModelo();
			try {
				for (ClienteAdministrativo cliente : vistaCliente
						.getClientesSeleccionados()) {
					servicio.setCliente(cliente);
					servicio.guardar();
					refrescarCatalogo(cliente);
					getVista().close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				app.mostrarError(e.getMessage());
			}
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

	}

	public void onEvent(Event arg0) throws Exception {
		setAppa(app);
		procesarCRUD(arg0);
	}

	public void refrescarCatalogo(ClienteAdministrativo dato) {
		int fila = llamadorR.getIdposi();
		ClienteAdministrativoView cliente =  servicio.findByIdView(dato.getId());
		if (!modoEliminar()) {
			if (fila != -1 && !modoAgregar()) {
				llamadorR.getInterfaz().editarDato(cliente);
			} else
				llamadorR.getInterfaz().agregarDato(cliente);
		} else
			llamadorR.getInterfaz().eliminarDato();
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
