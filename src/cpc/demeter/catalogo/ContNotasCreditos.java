package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Script;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.administrativo.ContNotaCredito;
import cpc.modelo.demeter.administrativo.NotaCredito;
import cpc.negocio.demeter.administrativo.NegocioNotaCredito;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.ventanas.CompVentanaEntrada;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContNotasCreditos extends ContCatalogo<NotaCredito> implements
		EventListener {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;
	private CompVentanaEntrada vistaNroControl = new CompVentanaEntrada(this);

	public ContNotasCreditos(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioNotaCredito servicios = NegocioNotaCredito.getInstance();
		this.app = app;
		dibujar(accionesValidas, "NOTAS DE CREDITO", servicios.getTodos(), app);
		desactivarTipoReporte();
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Nº Control", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNroDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nº Nota Credito", 50);
		titulo.setOrdenable(true);
		// titulo.setAlineacion(CompEncabezado.CENTRO);
		titulo.setMetodoBinder("getStrNroDocumento");
		// titulo.setMetodoModelo("usuario.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		// titulo.setMetodoModelo("usuario.identidad.cedula");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cliente", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreBeneficiario");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Ced/Rif", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrIdentidadLegalBeneficiario");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Estado", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Factura Afec", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFacturaAfectada");
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto Total", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTotal");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Por pagar", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrSaldo");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		// titulo.setMetodoModelo("usuario.identidad.nombre");
		encabezado.add(titulo);
		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			if (event.getTarget() == vistaNroControl.getAceptar()) {
				NotaCredito nota = getDatoSeleccionado();
				if (nroControlValido(nota, vistaNroControl.getEntrada())) {
					vistaNroControl.close();
					ContNotaCredito.imprimir(nota, app);
				}
			} else {
				int accion = (Integer) event.getTarget().getAttribute("pos");
				if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR) {
					NotaCredito nota = getDatoSeleccionado();
					new ContNotaCredito(accion, nota, this, this.app);
				} else if (accion == Accion.IMPRIMIR_ITEM) {
					// NotaCredito nota = getDatoSeleccionado();
					mostrarVistaNroControl(app);
				} else if (accion == Accion.IMPRIMIR_TODO) {
					Script scrip = new Script();
					scrip.setType("text/javascript");
					StringBuilder cadena = new StringBuilder();
					cadena.append(SpringUtil.getBean("icaro"));
					cadena.append("rpt_clientecomitesaldo");
					cadena.append("&jdbcUrl=");
					cadena.append(SessionDao.getConfiguration().getProperty(
							"hibernate.connection.url"));
					cadena.append("'); ");
					scrip.setContent(cadena.toString());
					this.app.agregar(scrip);
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	}

	public void mostrarVistaNroControl(AppDemeter app) {
		vistaNroControl = new CompVentanaEntrada(this);
		vistaNroControl.setTitle("Verificación del Nº de Control");
		vistaNroControl.settextopregunta("N° de Control");
		app.agregarHija(vistaNroControl);
	}

	public boolean nroControlValido(NotaCredito nota, Textbox txtNroControl)
			throws InterruptedException {
		try {
			if (txtNroControl.getText().trim().isEmpty()) {
				app.mostrarError("El nro de control no puede estar vacío");
				vistaNroControl.getEntrada().setFocus(true);
				return false;
			} else if (nota.getNroDocumento().compareTo(
					Integer.valueOf(txtNroControl.getText())) != 0) {
				Messagebox
						.show("El Nº de Control ingresado no coincide con el de la Nota de Crédito",
								"Error", Messagebox.OK, Messagebox.ERROR);
				return false;
			}
			return true;
		} catch (NumberFormatException e) {
			// TODO: handle exception
			Messagebox.show("El texto debe estar en formato numérico", "Error",
					Messagebox.OK, Messagebox.ERROR);
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List cargarDato(NotaCredito dato) {
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
