package cpc.demeter.catalogo;

import java.beans.PropertyVetoException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;





import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Script;
import org.zkoss.zul.Window;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.administrativo.ContContratoMecanizado;
import cpc.modelo.demeter.administrativo.ContratoMecanizado;
import cpc.modelo.demeter.administrativo.EstadoContrato;
import cpc.negocio.demeter.administrativo.NegocioContratoMecanizado;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcAccesoInvalido;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContContratosMecanizado extends ContCatalogo<ContratoMecanizado>
		implements EventListener {

	private static final long serialVersionUID = -8012014410778713889L;
	private AppDemeter app;
	private NegocioContratoMecanizado negocioContrato;

	public ContContratosMecanizado(AccionFuncionalidad accionesValidas,
			AppDemeter app) throws SQLException, ExcAccesoInvalido,
			PropertyVetoException, ExcColumnasInvalidas,
			cpc.ares.excepciones.ExcAccesoInvalido, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		
		negocioContrato = NegocioContratoMecanizado.getInstance();
		List<ContratoMecanizado> contratos = negocioContrato.getContratosProject();
		System.out.println(contratos.size());
		dibujar(accionesValidas, "CONTRATOS", negocioContrato.getContratosProject(),
				app);
		this.app = app;
	}

	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Control", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrNroDocumento");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Fecha", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getFechaString");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Cliente", 300);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getNombreCliente");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Monto Base", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getMonto");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getEstado");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipo");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			int accion = (Integer) event.getTarget().getAttribute("pos");
			if (accion <= Accion.CONSULTAR || accion == Accion.ANULAR
					|| accion == Accion.PROCESAR) {
				ContratoMecanizado contrato = getDatoSeleccionado();
				contrato =negocioContrato.getInstance().getContratoProject(contrato);
				if (contrato != null
						&& accion == Accion.ANULAR
						&& contrato.getEstado().getId()
								.equals(EstadoContrato.ESTADO_ANULADO))
					throw new Exception("El Documento ya esta Anulado");
				if (contrato != null && accion == Accion.PROCESAR) {
					boolean a = (contrato.getEstado().getDescripcion()
							.equals("Por Firmar"));
					boolean b = (contrato.getEstado().getDescripcion()
							.equals("Activo"));
					boolean c = (!a && !b);
					if (c)
						throw new Exception(
								"Solo puede Procesar Documentos en estado Por Firmar O Activo");
				}

				if (contrato == null || accion == Accion.AGREGAR)
					contrato = new ContratoMecanizado();
				new ContContratoMecanizado(accion, contrato, this, this.app);

			} else
				imprimirDetalle();

		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ningun registro");
		} catch (Exception e) {
			app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(ContratoMecanizado arg0) {
		return null;
	}

	private void imprimirDetalle() throws ExcFiltroExcepcion {
		
		
		String icaro = (String) SpringUtil.getBean("contrato");
		String cadena = "" + icaro;
		ContratoMecanizado ctto = getDatoSeleccionado();
		ctto=negocioContrato.getInstance().getContratoProject(ctto);
		switch (ctto.getTipoContrato().getId()) {
		case 1:
			cadena = cadena
					+ "rpt_contratoserviciomecanizadogravado&numerocontrato=";
			break;
		case 2:
			cadena = cadena
					+ "rpt_contratoserviciomecanizadoexonerado_carta&numerocontrato=";
			break;
		case 3:
			cadena = cadena
					+ "rpt_contratoserviciomecanizadocredito_carta&numerocontrato=";
			break;
		}
		cadena = cadena
				+ ctto.getId()
				+ "&Sede="
				+ app.getSede().getNombre()
				+ "&jdbcUrl="
				+ SessionDao.getConfiguration().getProperty(
						"hibernate.connection.url");

		Execution ad = Executions.getCurrent();
		System.out.println(cadena);
		ad.sendRedirect(cadena);
	}

}
