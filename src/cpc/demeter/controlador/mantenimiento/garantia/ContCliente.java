package cpc.demeter.controlador.mantenimiento.garantia;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.zkoss.zhtml.I;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import cpc.demeter.ActivacionGarantia;

import cpc.demeter.vista.mantenimiento.garantia.UICliente;
import cpc.demeter.vista.mantenimiento.garantia.UITelefono;
import cpc.modelo.ministerio.basico.Telefono;
import cpc.modelo.ministerio.gestion.Cliente;

import cpc.negocio.demeter.mantenimiento.NegocioGarantia;
import cpc.negocio.demeter.mantenimiento.NegocioTipoGarantia;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContCliente extends ContVentanaBase<Cliente> {

	private UICliente vista;
	private UITelefono vistaTelefono;
	private IZkAplicacion app;
	private Cliente cliente;
	private NegocioGarantia servicio;
	private NegocioTipoGarantia tipoServicio;
	private Boolean operacion = false;

	public Boolean getOperacion() {
		return operacion;
	}

	public void setOperacion(Boolean operacion) {
		this.operacion = operacion;
	}

	public UICliente getVistaCliente() {
		return this.vista;
	}

	public ContCliente(int i, Cliente cliente, IZkAplicacion app) throws SuspendNotAllowedException, InterruptedException,
			ExcDatosInvalidos {
		super(i);
		this.app = app;
		servicio = NegocioGarantia.getInstance();
		tipoServicio = NegocioTipoGarantia.getInstance();

		vista = new UICliente();

		if (cliente == null) {
			cliente = new Cliente();
		}
		this.cliente = cliente;
		vista.setModelo(this.cliente);
		vista.setControlador(this);
		vista.setMaximizable(true);
		vista.setClosable(true);
		vista.setTitle("Editar Cliente");
		vista.setWidth("600px");
		vista.inicializar();
		vista.dibujar();
		try {
			vista.cargar();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		vista.desactivar(i);
		app.agregarHija(vista);
		vista.doModal();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub

		if (arg0.getTarget() == vista.getAgregar()) {
			vistaTelefono = new UITelefono("Edit Telefono", 400);
			vistaTelefono.setCodigoAreas(tipoServicio.getcodigoAreas());
			vistaTelefono.setControlador(this);
			vistaTelefono.setModelo(new Telefono());
			vistaTelefono.inicializar();
			vistaTelefono.dibujar();

			app.agregarAEscritorio(vistaTelefono);
			vistaTelefono.doModal();
		} else if (arg0.getTarget() == vista.getQuitar()) { // / quitar Telefono
															// de la Lista :-D
			Set set = vista.getTelefonos().getSelectedItems();
			if (set.size() != 1) {
				app.mostrarError("Debe Seleccionar 1 Items");
			} else {
				int dato = Messagebox.show("Desea Quitar Este Telefono ?",
						"Telefono", Messagebox.OK | Messagebox.CANCEL,
						Messagebox.INFORMATION);
				if (dato == Messagebox.OK) {
					Telefono telefono = (Telefono) vista.getTelefonos()
							.getSelectedItem().getValue();
					vista.quitarTelefono(telefono);
				}
			}
		} else if (arg0.getTarget() == vista.getAceptar()) {
			validar();
			guardar();
		} else if (vistaTelefono != null) {
			onEventAddTelefono(arg0);
		}
	}

	public void onEventAddTelefono(Event e) throws WrongValuesException,
			ExcEntradaInconsistente {
		if (vistaTelefono.getAceptar() == e.getTarget()) {
			valirdarTelefono();
			Telefono telefono = vistaTelefono.getModelo();
			telefono.setCodigoArea(vistaTelefono.getCodigoArea().getSeleccion());
			vista.settelefono(telefono);
			vistaTelefono.detach();
		}

	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		Cliente cliente = vista.getModelo();

		cliente.setIdentidadLegal(vista.getIdentidadLegalProcesada());
		List<Telefono> lista = vista.gettelefonosmodelo();
		cliente.setTelefonos(lista);

		if (cliente.getId() == null) {
			cliente.setActivo(true);
		}
		try {

			IZkAplicacion a = (IZkAplicacion) TransactionSynchronizationManager
					.getResource("obj");
			boolean b = TransactionSynchronizationManager.hasResource("obj");

			boolean igual = app.equals(a);
			if (!igual) {
				if (b) {
					TransactionSynchronizationManager.unbindResource("obj");
				}
				TransactionSynchronizationManager.bindResource("obj", app);
			}
			servicio.GuardarCliente(cliente);
			vista.detach();
			operacion = true;

		} catch (Exception e) {

			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		// throw new WrongValueException(vista.getTxtNombre(),
		// "Debe llenar este campo");

		if (vista.getDocumentoIdentidad().getValue().trim().length() == 0) {
			throw new WrongValueException(vista.getDocumentoIdentidad(),
					"Debe Ingresar una Cedula O un rif ");
		}
		if (vista.getTipoidentidad().getSelectedItem().getValue() == null) {
			throw new WrongValueException(vista.getTipoidentidad(),
					" Debe Seleccionar su tipo de Documento ");
		}

		if (servicio
				.perteneceidentidadLegal(vista.getIdentidadLegalProcesada())) {
			throw new WrongValueException(vista.getDocumentoIdentidad(),
					" Identidad Legal Invalida Pertenece a Otro Usuario");
		}

		if (vista.getNombres().getValue().trim().length() == 0) {
			throw new WrongValueException(vista.getNombres(),
					"Debe Ingresar Nombres ");
		}

		if (vista.getDireccion().getValue().trim().length() == 0) {
			throw new WrongValueException(vista.getDireccion(),
					"Debe Ingresar alguna Dirección ");
		}

		if (vista.getTelefonos().getItems().size() == 0) {
			throw new WrongValueException(vista.getTelefonos(),
					" Ingrese un Numero Telefonico !!! ");
		}

	}

	public void valirdarTelefono() throws WrongValuesException,
			ExcEntradaInconsistente {
		if (vistaTelefono.getNumero().getValue().trim().length() == 0) {
			throw new WrongValueException(vistaTelefono.getNumero(),
					"Debe llenar este campo ");
		}
		if (vistaTelefono.getCodigoArea().getSeleccion() == null) {
			throw new WrongValueException(vistaTelefono.getCodigoArea(),
					"Debe Seleccionar Un Codigo de Area");
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
