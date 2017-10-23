package cpc.demeter.controlador.administrativo;

import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContConsumosCreditos;
import cpc.demeter.vista.administrativo.UiConsumoCredito;
import cpc.modelo.demeter.administrativo.ConsumoCredito;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.administrativo.NotaCredito;
import cpc.modelo.demeter.administrativo.NotaDebito;
import cpc.modelo.demeter.basico.TipoConsumo;
import cpc.negocio.demeter.administrativo.NegocioConsumoCredito;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContConsumoCredito extends ContVentanaBase<ConsumoCredito> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioConsumoCredito servicio;
	private AppDemeter app;
	private UiConsumoCredito vistaConsumo;

	public ContConsumoCredito(int modoOperacion, ConsumoCredito consumo,
			ContConsumosCreditos llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		servicio = NegocioConsumoCredito.getInstance();
		this.app = app;
		try {
			if (consumo == null || modoAgregar())
				consumo = new ConsumoCredito();
			List<NotaCredito> notas = servicio.getNotasSaldo();
			List<TipoConsumo> tipos = servicio.getTiposConsumo();
			Date fecha = servicio.getFechaCierre();
			setear(consumo, new UiConsumoCredito("CONSUMO NOTA CREDITO ("
					+ Accion.TEXTO[modoOperacion] + ")", 550, notas, tipos,
					fecha, modoOperacion), llamador, app);

			vistaConsumo = (UiConsumoCredito) getVista();
			fijarFormaPago();
			if (modoAnular() || modoConsulta()) {
				vistaConsumo.modoConsulta();
			}
			// vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun campo");
		} catch (ExcFiltroExcepcion e) {
			e.printStackTrace();
			app.mostrarError("problemas con los datos: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError("problemas con los datos: " + e.getMessage());
		}
	}

	public void guardar() {
		TipoConsumo tipo = vistaConsumo.getTipoConsumo().getSeleccion();
		if (tipo.getNombre().equals("FACTURA")) {
			if (vistaConsumo.getFactura().getSeleccion() == null) {
				throw new WrongValueException(vistaConsumo.getFactura(),
						"Indique una factura");
			}

		}
		if (tipo.getNombre().equals("DEBITO")) {
			if (vistaConsumo.getDebito().getSeleccion() == null) {
				throw new WrongValueException(vistaConsumo.getFactura(),
						"Indique una nota de debito");
			}

		}
		try {
			servicio = NegocioConsumoCredito.getInstance();
			servicio.setConsumoCredito(getDato());
			ConsumoCredito a = getDato();
			// System.out.println(a.getMonto());
			servicio.guardar(servicio.getConsumoCredito());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

		/*
		 * if (vista.getTipoCuenta().getSelectedIndex()<0) throw new
		 * WrongValueException
		 * (vista.getTipoCuenta(),"Indique el Tipo de Cuenta"); if
		 * (vista.getBanco().getSelectedIndex() < 0) throw new
		 * WrongValueException(vista.getBanco(),"Indique el Banco"); if
		 * (vista.getNroCuenta().getValue()=="") throw new
		 * WrongValueException(vista
		 * .getNroCuenta(),"Indique el Numero de Cuenta"); if
		 * (vista.getDescripcion().getValue()=="") throw new
		 * WrongValueException(vista.getDescripcion(),"Indique la Descripcion");
		 * if (vista.getCuentaContable().getSelectedIndex()< 0) throw new
		 * WrongValueException
		 * (vista.getCuentaContable(),"Seleccione la Cuenta Contable");
		 */
	}

	public void onEvent(Event event) throws Exception {
		System.out.println("hay evento " + event.getName() + " en "
				+ event.getTarget().getClass().getName());
		if (vistaConsumo == null)
			vistaConsumo = (UiConsumoCredito) getVista();
		try {
			if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {
				if (event.getTarget() == vistaConsumo.getNota())
					actualizarNota();
				if (event.getTarget() == vistaConsumo.getFactura())
					actualizarFactura();
				if (event.getTarget() == vistaConsumo.getDebito())
					actualizarDebito();
			} else if (event.getName().equals(Events.ON_CHANGE))
				actualizarPrecio();
			else if (event.getName().equals(Events.ON_SELECTION))
				fijarFormaPago();
			else if (event.getTarget() == vistaConsumo.getAceptar()) {

				procesarCRUD(event);
			}

		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

	private void actualizarNota() {
		try {
			NotaCredito nota = vistaConsumo.getNota().getSeleccion();
			if (nota != null) {
				servicio = NegocioConsumoCredito.getInstance();
				List<DocumentoFiscal> facturas = servicio.facturasConSaldo(nota
						.getBeneficiario());
				List<NotaDebito> debitos = servicio
						.getNotasDebitosConSaldo(nota.getBeneficiario());
				vistaConsumo.getFactura().setModelo(facturas);
				vistaConsumo.getDebito().setModelo(debitos);
				vistaConsumo.getMontoNota().setValue(
						Math.abs(nota.getMontoSaldo()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void actualizarFactura() {
		try {
			DocumentoFiscal factura = vistaConsumo.getFactura().getSeleccion();
			if (factura != null) {
				vistaConsumo.getMontoSaldoFactura().setValue(
						factura.getMontoSaldo());
			}
			getDato().setDocumento(factura);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void actualizarDebito() {
		try {
			NotaDebito debito = vistaConsumo.getDebito().getSeleccion();
			if (debito != null) {
				vistaConsumo.getMontoSaldoDebito().setValue(
						debito.getMontoSaldo());
				vistaConsumo.getMontoSaldoDebito().setReadonly(true);
				vistaConsumo.getDebitoFacturaTxt().setValue(
						debito.getStrFacturaAfectada());
				vistaConsumo.getDebitoFacturaTxt().setReadonly(true);
				getDato().setDocumento(debito);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void actualizarPrecio() {
		Double monto = vistaConsumo.getMonto().getValue();
		if (vistaConsumo.getNota().getSeleccion() == null)
			throw new WrongValueException(vistaConsumo.getMonto(),
					"Indique La Nota");
		TipoConsumo tipo = vistaConsumo.getTipoConsumo().getSeleccion();
		if (tipo == null)
			throw new WrongValueException(vistaConsumo.getMonto(),
					"Indique La forma de pago");
		Double montoMaximo = vistaConsumo.getMontoNota().getValue();

		if (tipo.isAbono()) {
			if (tipo.getNombre().equals("factura")) {
				Double montoFactura = vistaConsumo.getMontoSaldoFactura()
						.getValue();
				if (montoFactura < montoMaximo)
					montoMaximo = montoFactura;
			}
			if (tipo.getNombre().equals("Debito")) {
				Double montodebito = vistaConsumo.getMontoSaldoDebito()
						.getValue();
				if (montodebito < montoMaximo)
					montoMaximo = montodebito;
			}

		}
		if (monto > montoMaximo)
			monto = montoMaximo;
		vistaConsumo.getMonto().setValue(monto);
		vistaConsumo.getSaldoNota().setValue(
				vistaConsumo.getMontoNota().getValue() - monto);

	}

	private void fijarFormaPago() {
		try {
			TipoConsumo tipo = vistaConsumo.getTipoConsumo().getSeleccion();
			if (tipo != null) {
				if (tipo.getNombre().equals("FACTURA")) {
					vistaConsumo.desactivarDebito();
					vistaConsumo.activarFactura();

				}
				if (tipo.getNombre().equals("DEBITO")) {
					vistaConsumo.desactivarFactura();
					vistaConsumo.activarDebito();
				}
				if (!tipo.isAbono()) {
					vistaConsumo.desactivarFactura();
					vistaConsumo.desactivarDebito();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	public void anular() {
		try {
			servicio.anular(getDato());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
		refrescarCatalogo();
		vistaConsumo.close();
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
