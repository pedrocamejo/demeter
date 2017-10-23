package cpc.demeter.catalogo;
 
import java.io.Serializable; 
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.administrativo.ContCheque;
import cpc.modelo.demeter.administrativo.Cheque;
 
import cpc.negocio.demeter.administrativo.NegocioCheque;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContCheques extends ContCatalogo<Cheque> implements EventListener,
		Serializable {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContCheques(AccionFuncionalidad accionFuncionalidad, AppDemeter app)
			throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		// TODO Auto-generated constructor stub
		NegocioCheque servicios = NegocioCheque.getInstance();
		this.app = app;
		dibujar(accionFuncionalidad, "CHEQUES", servicios.getcheques(), app);

	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("ID", 70);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getId");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Numero Cheque ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroCheque");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Numero Cuenta ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroCuenta");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMonto");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");

			if (accion <= Accion.CONSULTAR) {
				{
					Cheque cheque = getDatoSeleccionado();
					if (cheque == null && accion != Accion.AGREGAR) {
						app.mostrarError("Seleccione un Cheque ");
					} else {
						new ContCheque(accion, cheque, this, app);
					}
				}

			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("Seleccione un Banco");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	@Override
	public List cargarDato(Cheque dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
