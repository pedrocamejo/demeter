package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Script;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.administrativo.ContDebitoInterno;
import cpc.modelo.demeter.administrativo.DebitoInterno;
import cpc.negocio.demeter.administrativo.NegocioDebitoInterno;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContDebitosInternos extends ContCatalogo<DebitoInterno> implements
		EventListener {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;

	public ContDebitosInternos(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		NegocioDebitoInterno servicios = NegocioDebitoInterno.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Debitos Internos", servicios.getTodos(),
				this.app);
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Nro debito", 90);
		titulo.setOrdenable(true);
		// titulo.setAlineacion(CompEncabezado.CENTRO);
		titulo.setMetodoBinder("getControl");
		// titulo.setMetodoModelo("usuario.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Factura", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFactura");
		// titulo.setMetodoModelo("usuario.identidad.cedula");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrfecha");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Beneficiario", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrPagador");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Concepto", 150);
		titulo.setOrdenable(true);
		// titulo.setAlineacion(CompEncabezado.CENTRO);
		titulo.setMetodoBinder("getConcepto");
		// titulo.setMetodoModelo("usuario.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto", 90);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMonto");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);
		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR) {
				NegocioDebitoInterno reciboServicio = NegocioDebitoInterno
						.getInstance();
				DebitoInterno debito = getDatoSeleccionado();
				if (debito == null || accion == Accion.AGREGAR)
					reciboServicio.setDebitoInterno(new DebitoInterno());
				else
					reciboServicio.setDebitoInterno(debito);
				new ContDebitoInterno(accion,
						reciboServicio.getDebitoInterno(), this, this.app);
			} else if (accion == Accion.IMPRIMIR_ITEM
					|| accion == Accion.IMPRIMIR_TODO) {
				DebitoInterno debito = getDatoSeleccionado();
				ContDebitoInterno.imprimir(debito, app);
			
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
	public List cargarDato(DebitoInterno dato) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
