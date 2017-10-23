package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.administrativo.ContDeposito;

import cpc.modelo.demeter.administrativo.Deposito;
import cpc.negocio.demeter.administrativo.NegocioDeposito;

import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContDepositos extends ContCatalogo<Deposito> implements
		EventListener, Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContDepositos(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioDeposito servicios = NegocioDeposito.getInstance();
		this.app = app;
		dibujar(accionesValidas, "DEPOSITOS BANCARIOS", servicios.getTodos(),
				app);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		titulo = new CompEncabezado("Banco", 200);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getBancoCuenta");

		encabezado.add(titulo);
		titulo = new CompEncabezado("Cuenta", 180);
		titulo.setMetodoBinder("getStrCuenta");
		titulo.setOrdenable(true);

		encabezado.add(titulo);
		titulo = new CompEncabezado("Nro documento", 150);
		titulo.setMetodoBinder("getNroDeposito");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto", 90);
		titulo.setMetodoBinder("getMontoTotal");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 100);
		titulo.setMetodoBinder("getFecha");
		titulo.setOrdenable(true);
		encabezado.add(titulo);
		return encabezado;

	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.CONSULTAR) {
				Deposito deposito = getDatoSeleccionado();
				if (accion != Accion.AGREGAR)
				{
					if(deposito!= null)
					{
						NegocioDeposito.getInstance().enrriquecer(deposito);
					}
				}
				new ContDeposito(accion, deposito, this, this.app);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(Deposito dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
