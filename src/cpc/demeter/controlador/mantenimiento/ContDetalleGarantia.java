package cpc.demeter.controlador.mantenimiento;

import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.mantenimiento.UISelectDetalleGarantia;
import cpc.modelo.demeter.mantenimiento.DetalleGarantina;
import cpc.negocio.demeter.mantenimiento.NegocioDetalleGarantia;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContDetalleGarantia extends ContVentanaBase<DetalleGarantina> {

	private AppDemeter app;
	private Integer modoOperacion;
	private ContTipoGarantia llamador;
	private NegocioDetalleGarantia servicio;
	private UISelectDetalleGarantia vista;

	public ContDetalleGarantia(int modoOperacion,
			DetalleGarantina detalleGarantia, ContTipoGarantia llamador,
			AppDemeter app) throws ExcDatosInvalidos, ExcFiltroExcepcion {
		super(modoOperacion);

		if (modoOperacion == Accion.AGREGAR) {
			detalleGarantia = new DetalleGarantina();

		}
		setDato(detalleGarantia);
		this.app = app;
		this.llamador = llamador;
		servicio = NegocioDetalleGarantia.getInstance();
		vista = new UISelectDetalleGarantia("Seleccionar Detalle Garantia ",
				700);
		setear(detalleGarantia, vista, this, app);

	}

	private void setear(DetalleGarantina detalleGarantia,
			UISelectDetalleGarantia vista2,
			ContDetalleGarantia contDetalleGarantia, AppDemeter app2) {
		try {
			this.vista.setMode("modal");
			this.vista.setModelo(getDato());
			this.vista.setControlador(this);
			this.vista.dibujarVentana();
			this.vista.cargar();
			app.agregarHija(this.vista);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub
		if (vista.getAceptar() == arg0.getTarget()) {
			getDato().setTipoGarantia(llamador.getDato());
			llamador.AgregarDetalleGarantia(getDato());
			vista.detach();

		} else if (vista.getCancelar() == arg0.getTarget()) {
			vista.detach();
		}
	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub

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
