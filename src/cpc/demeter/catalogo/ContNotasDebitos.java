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
import cpc.demeter.controlador.administrativo.ContNotaDebito;
import cpc.demeter.controlador.administrativo.ContNotaDebitoCheque;
import cpc.demeter.controlador.administrativo.ContNotaDebitoRecibo;
import cpc.modelo.demeter.administrativo.NotaCredito;
import cpc.modelo.demeter.administrativo.NotaDebito;
import cpc.negocio.demeter.administrativo.NegocioNotaDebito;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cpc.zk.componente.ventanas.CompVentanaEntrada;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContNotasDebitos extends ContCatalogo<NotaDebito> implements
		EventListener {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;
	private CompVentanaEntrada vistaNroControl = new CompVentanaEntrada(this);

	public ContNotasDebitos(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws SQLException, ExcAccesoInvalido, PropertyVetoException,
			ExcColumnasInvalidas, cpc.ares.excepciones.ExcAccesoInvalido,
			ExcArgumentoInvalido, ExcFiltroExcepcion {
		NegocioNotaDebito servicios = NegocioNotaDebito.getInstance();
		this.app = app;
		dibujar(accionesValidas, "NOTAS DE DEBITO", servicios.getTodos(),
				this.app);
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

		titulo = new CompEncabezado("Nº Nota Debito", 50);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrNroDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFecha");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cliente", 250);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreBeneficiario");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Ced/Rif", 80);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrIdentidadLegalBeneficiario");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Factura Afec", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrFacturaAfectada");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto Total", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTotal");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Por pagar", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrSaldo");
		titulo.setAlineacion(CompEncabezado.DERECHA);
		encabezado.add(titulo);
		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			if (event.getTarget() == vistaNroControl.getAceptar())
			{
				NotaDebito nota = getDatoSeleccionado();
			
				if (nroControlValido(nota, vistaNroControl.getEntrada()))
				{
					vistaNroControl.close();
					ContNotaDebito.imprimir(nota, app);
				}
			} 
			else
			{
				int accion = (Integer) event.getTarget().getAttribute("pos");

				if (accion == Accion.CORREGIR) 
				{ //hay que definir que va a pasar con los cheques :-) 
				 //	NotaDebito nota = getDatoSeleccionado();
				//	new ContNotaDebitoCheque(accion, nota, this, this.app);

				}
				
				if (accion == Accion.PROCESAR) 
				{
					NotaDebito nota = getDatoSeleccionado();
					
					if (!nota.isCancelada())
					{
						new ContNotaDebitoRecibo(accion, nota, this, this.app);
					}
					else
					{
						this.app.mostrarError("Nota Cancelada ");
					}
				}
				
				else if (accion <= Accion.CONSULTAR	|| accion == Accion.ANULAR)
				{
					NotaDebito nota = getDatoSeleccionado();
					new ContNotaDebito(accion, nota, this, this.app);
				}
				
				else if (accion == Accion.IMPRIMIR_ITEM) 
				{
					// NotaDebito nota = getDatoSeleccionado();
					mostrarVistaNroControl(app);
				}
				
				else if (accion == Accion.IMPRIMIR_TODO)
				{
					Script scrip = new Script();
					scrip.setType("text/javascript");
					
					
					String icaro = (String) SpringUtil.getBean("icaro");
					StringBuilder cadena = new StringBuilder();
					cadena.append(icaro);
					cadena.append("rpt_clientecomitesaldo");
					cadena.append("&jdbcUrl=");
					cadena.append(SessionDao.getConfiguration().getProperty("hibernate.connection.url"));
					cadena.append("'); ");
					scrip.setContent(cadena.toString());
					app.agregar(scrip);
				}

			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void mostrarVistaNroControl(AppDemeter app) {
		vistaNroControl = new CompVentanaEntrada(this);
		vistaNroControl.setTitle("Verificación del Nº de Control");
		vistaNroControl.settextopregunta("N° de Control");
		app.agregarHija(vistaNroControl);
	}

	public boolean nroControlValido(NotaDebito nota, Textbox txtNroControl)
			throws InterruptedException {
		try {
			if (txtNroControl.getText().trim().isEmpty()) {
				app.mostrarError("El nro de control no puede estar vacío");
				vistaNroControl.getEntrada().setFocus(true);
				return false;
			} else if (nota.getNroDocumento().compareTo(
					Integer.valueOf(txtNroControl.getText())) != 0) {
				Messagebox
						.show("El Nº de Control ingresado no coincide con el de la Nota de Débito",
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
	public List cargarDato(NotaDebito dato) {
		return null;
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		// TODO Auto-generated method stub
		return null;
	}

}
