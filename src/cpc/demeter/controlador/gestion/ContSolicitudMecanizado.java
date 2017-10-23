package cpc.demeter.controlador.gestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
 
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.vista.gestion.UiSolicitudMecanizado;
import cpc.modelo.demeter.administrativo.DocumentoFiscal;
import cpc.modelo.demeter.basico.CicloProductivo;
import cpc.modelo.demeter.basico.Labor;
import cpc.modelo.demeter.basico.PlanServicio;
import cpc.modelo.demeter.basico.Rubro;
import cpc.modelo.demeter.basico.Servicio;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.basico.UnidadMedida;
import cpc.modelo.demeter.gestion.AnulacionSolicitud;
import cpc.modelo.demeter.gestion.DetalleSolicitud;
import cpc.modelo.demeter.gestion.MotivoAnulacionSolicitud;
import cpc.modelo.demeter.gestion.SolicitudMecanizado;
import cpc.modelo.demeter.gestion.UnidadSolicitada;
import cpc.modelo.demeter.interfaz.IProducto;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.modelo.ministerio.gestion.InstitucionCrediticia;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.modelo.sigesp.basico.CuentaBancaria;
import cpc.negocio.demeter.NegocioCuentaBancaria;
import cpc.negocio.demeter.administrativo.NegocioClienteAdministrativo;
import cpc.negocio.demeter.gestion.NegocioSolicitudMecanizado;
import cpc.persistencia.demeter.implementacion.gestion.PerDetalleSolicitud;
import cpc.persistencia.demeter.implementacion.gestion.PerEstadoSolicitud;
import cpc.persistencia.demeter.implementacion.gestion.PerMotivoAnulacionSolicitud;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompDetalleGrilla;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcAgregacionInvalida;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContSolicitudMecanizado extends
		ContVentanaBase<SolicitudMecanizado> {

	private static final long serialVersionUID = 6184414588153046382L;
	private AppDemeter app;
	private UiSolicitudMecanizado vista;
	private NegocioSolicitudMecanizado negocio;

	public ContSolicitudMecanizado(int modoOperacion,
			SolicitudMecanizado solicitud,
			ICompCatalogo<SolicitudMecanizado> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {
			negocio = NegocioSolicitudMecanizado.getInstance();
			if (solicitud == null || modoAgregar()) {
				solicitud = negocio.getNuevaSolicitud();
			}
			List<Servicio> labores = negocio.getServicios();
			List<Productor> productores = negocio.getProductoresProject();
			List<CicloProductivo> ciclos = negocio.getCiclosProductivos();
			List<PlanServicio> planes = negocio.getPlanesServicios();
			List<Trabajador> trabajadores = negocio.getTrabajadores();
			List<UnidadFuncional> unidadesEjecutoras = negocio
					.getUnidadesEjecutoras();
			List<Rubro> rubros = negocio.getRubros();
			List<InstitucionCrediticia> instituciones = negocio
					.getFinanciamientos();
			List<MotivoAnulacionSolicitud> motivos = null;
			if (modoConsulta()) {
				motivos = new PerMotivoAnulacionSolicitud().getAll();
			} else {
				motivos = negocio.getmotivosanulacion();
			}

			vista = new UiSolicitudMecanizado("Solicitud Mecanizado ("
					+ Accion.TEXTO[modoOperacion] + ")", 720, ciclos,
					unidadesEjecutoras, planes, labores, trabajadores,
					productores, rubros, instituciones, motivos);
			vista.DesactivarCantidad(modoOperacion);
			setear(solicitud, vista, llamador, app);

			vista = ((UiSolicitudMecanizado) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun Servicio");
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * public void eliminar() {
	 * 
	 * try { negocio = NegocioSolicitudMecanizado.getInstance();
	 * negocio.setModelo(getDato()); negocio.borrar(); } catch (Exception e) {
	 * e.printStackTrace(); app.mostrarError(e.getMessage()); } }
	 */

	public void anular() {
		try {
			if (negocio.getHijosActivos(getDato().getId().intValue()) > 0)
				throw new ExcArgumentoInvalido(
						"La Solicitud tiene documentos activos asociado, no puede anular por esto");
			if (vista.getmotivo().getSeleccion() == null)
				throw new ExcArgumentoInvalido(
						"La Solicitud no tiene un motivo de anulacion definido, no puede anular por esto");
			System.out.println("va a borrar" + getDato().getNroControl());

			AnulacionSolicitud anul = new AnulacionSolicitud();
			anul.setusuario(app.getUsuario().toString());
			anul.setSolicitud(getDato());
			anul.setmotivoanulacionsolicitud(vista.getmotivo().getSeleccion());
			getDato().setEstadosolicitud(
					negocio.getEstadoSolicitud(vista.getEstadoSolictud()
							.getValue().toString()));
			negocio.anular(getDato(), anul);
			refrescarCatalogo();
			vista.close();
			// refrescarCatalogo();
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void procesar() {
		String a = new PerEstadoSolicitud().getrechazada().getEstado();
		String b = vista.getEstadoSolictud().getValue();
		boolean c = false;
		if (a.equals(b))
			c = true;
		if (c) {
			// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
			// "Advertencia 17",JOptionPane.WARNING_MESSAGE);
			anular();
		} else {
			getDato().setEstadosolicitud(
					negocio.getEstadoSolicitud(vista.getEstadoSolictud()
							.getValue().toString()));
			try {
				guardar();
			} catch (ExcFiltroExcepcion e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			refrescarCatalogo();
			vista.close();
		}

	}

	public void guardar() throws ExcFiltroExcepcion {
		try {
			negocio = NegocioSolicitudMecanizado.getInstance();
			actualizar();
			negocio.setModelo(getDato());
			 
			String idsede = (String)  SpringUtil.getBean("idsedesincronizar");
			negocio.guardar(negocio.getModelo(),idsede);
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError("Error al Guarda Labor, " + e.getMessage());
		}
		// refrescarCatalogo();
		// vista.close();
	}

	@SuppressWarnings("unchecked")
	private void actualizar() throws ExcAgregacionInvalida {
		try {
			List<DetalleSolicitud> detalles = new ArrayList<DetalleSolicitud>();
			List<Row> filas = vista.getDetalle().getFilas().getChildren();
			DetalleSolicitud detalle;
			for (Row item : filas) {
				detalle = (DetalleSolicitud) item.getAttribute("dato");
				actualizarUnidades((CompDetalleGrilla<UnidadSolicitada>) item
						.getChildren().get(3), detalle);
				detalles.add(detalle);
			}
			getDato().setDetalles(detalles);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcAgregacionInvalida("Problema con Unidades, "
					+ e.getMessage());
		}

	}

	@SuppressWarnings("unchecked")
	private void actualizarUnidades(
			CompDetalleGrilla<UnidadSolicitada> componente,
			DetalleSolicitud detalle) throws ExcAgregacionInvalida {
		try {
			List<UnidadSolicitada> unidades = new ArrayList<UnidadSolicitada>();
			List<Hbox> filas = componente.getChildren();
			UnidadSolicitada unidad;
			for (Hbox item : filas) {
				unidad = (UnidadSolicitada) item.getAttribute("dato");
				unidad.setDetalleSolicitud(detalle);
				unidad.setCantidad(((Doublebox) item.getChildren().get(0))
						.getValue());
				unidades.add(unidad);
			}
			detalle.setSolicitado(unidades);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcAgregacionInvalida("Problema con Unidades");
		}

	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		if (getModoOperacion() != Accion.ELIMINAR) {
			if ((vista.getServicio().getSeleccion() == null)) {
				throw new WrongValueException(vista.getServicio(),
						"Indique un servicio");
			}
			if (vista.getCedula().getSeleccion() == null) {
				throw new WrongValueException(vista.getCedula(),
						"Indique productor");
			}
			if (vista.getUnidadFuncional().getSeleccion() == null) {
				throw new WrongValueException(vista.getUnidadFuncional(),
						"Indique Unidad Funcional");
			}
			if (vista.getUnidadProductiva().getSeleccion() == null) {
				throw new WrongValueException(vista.getUnidadProductiva(),
						"Indique Unidad Productiva");
			}
			if (vista.getCicloProductivo().getSeleccion() == null) {
				throw new WrongValueException(vista.getCicloProductivo(),
						"Indique ciclo");
			}
			if (vista.getResponsable().getSeleccion() == null) {
				throw new WrongValueException(vista.getResponsable(),
						"Indique responsable");
			}
			if (vista.getRubro().getSeleccion() == null) {
				throw new WrongValueException(vista.getRubro(), "Indique rubro");
			}
			if (vista.getPlanServicio().getSeleccion() == null) {
				throw new WrongValueException(vista.getPlanServicio(),
						"Indique Plan de servicio");
			}
			if (vista.getDetalle().getFilas().getChildren().size() < 1)
				throw new WrongValueException(vista.getDetalle(),
						"Indique labor, o labores");
			validarDetalle();
		}
	}

	@SuppressWarnings("unchecked")
	public void validarDetalle() throws WrongValuesException,
			ExcEntradaInconsistente {
		List<DetalleSolicitud> detalles = new ArrayList<DetalleSolicitud>();
		List<Row> filas = vista.getDetalle().getFilas().getChildren();
		List<Hbox> filasUnidades;
		Servicio servicio = vista.getServicio().getSeleccion();
		CompBuscar<IProducto> producto;
		Doublebox cantidad;

		for (Row item : filas) {
			producto = (CompBuscar<IProducto>) item.getChildren().get(0);
			if (producto.getSeleccion() == null)
				throw new WrongValueException(producto, "Labor no seleccionada");
			if (servicio.getManejaPases()) {
				cantidad = (Doublebox) item.getChildren().get(2);
				if (cantidad.getValue() == null)
					throw new WrongValueException(cantidad,
							"No ha indicado valor");
			}
			if (servicio.getManejaCantidades()) {
				cantidad = (Doublebox) item.getChildren().get(1);
				if (cantidad.getValue() == null)
					throw new WrongValueException(cantidad,
							"No ha indicado valor");
			}
			filasUnidades = ((CompDetalleGrilla<UnidadSolicitada>) item
					.getChildren().get(3)).getChildren();
			for (Hbox item2 : filasUnidades) {
				cantidad = ((Doublebox) item2.getChildren().get(0));
				if (cantidad.getValue() == null)
					throw new WrongValueException(cantidad,
							"No ha indicado valor");
			}
		}
		for (Row item : filas) {
			int a = 0;
			producto = (CompBuscar<IProducto>) item.getChildren().get(0);
			for (Row item2 : filas) {

				CompBuscar<IProducto> producto2 = (CompBuscar<IProducto>) item2
						.getChildren().get(0);
				if (producto.getSeleccion().getId() == producto2.getSeleccion()
						.getId()) {
					a++;
					System.out.println("en el ciclo tamaño" + a);
				}
				System.out.println("tamaño" + a);
				if (a > 1) {
					throw new WrongValueException(producto,
							"tiene servicos repetidos");
				}
			}

		}

		getDato().setDetalles(detalles);
	}

	@SuppressWarnings("unchecked")
	public void onEvent(Event event) throws Exception {
		System.out.println("hay evento " + event.getName() + " en "
				+ event.getTarget().getClass().getName());
		if (event.getTarget() == getVista().getAceptar()) {

			try {
				procesarCRUD(event);
			} catch (Exception e) {
				e.printStackTrace();
				app.mostrarError(e.getMessage());
			}

			/*
			 * }else if (event.getName() == Events.ON_SELECTION &&
			 * event.getTarget() == vista.getUnidadGestion()){
			 * actualizarUnidadCobro();
			 */
		} else if (event.getName() == Events.ON_SELECTION
				&& event.getTarget() == vista.getServicio()) {
			actualizarServicio();
		} else if (event.getName() == CompBuscar.ON_SELECCIONO && event.getTarget() == vista.getCedula())
		{
			actualizarDatosProdcutor();
		} 
		else if (event.getName() == Events.ON_SELECTION
				&& event.getTarget() == vista.getUnidadProductiva()) {
			vista.getcodigounidadProductiva().setValue(
					vista.getUnidadProductiva().getSeleccion().getId()
							.toString());
			System.out.println(vista.getUnidadProductiva().getSeleccion()
					.getId());
			System.out.println(vista.getcodigounidadProductiva().getValue());

		} else if (event.getName() == CompBuscar.ON_SELECCIONO) {
			actualizarUnidadesGestion((CompBuscar<IProducto>) event.getTarget());
		} else if (event.getName() == Events.ON_CLICK
				&& event.getTarget() == vista.getRechazar()) {
			// vista.getAprobado().setChecked(false);
			vista.getEstadoSolictud().setValue(
					new PerEstadoSolicitud().getrechazada().getEstado());
			vista.activarAnular();
		} else if (event.getName() == Events.ON_CLICK
				&& event.getTarget() == vista.getAprovar()) {
			// vista.getAprobado().setChecked(true);
			vista.getEstadoSolictud().setValue(
					new PerEstadoSolicitud().getaprobada().getEstado());

		}
	}

	public void actualizarServicio() throws ExcFiltroExcepcion {
		Servicio servicio = vista.getServicio().getSeleccion();
		if (servicio != null) {
			negocio = NegocioSolicitudMecanizado.getInstance();
			if (modoAgregar()) {
				List<IProducto> a = negocio.getLaboresActivas(servicio);
				vista.setProductos(a);
			} else {
				List<IProducto> a = negocio.getLabores(servicio);
				vista.setProductos(a);
			}
			// IProducto b = a.get(0);

			vista.refrescarServicio();
			boolean pase, cantidad;
			if (servicio.getManejaPases() == null)
				pase = false;
			else
				pase = servicio.getManejaPases();
			if (servicio.getManejaCantidades() == null)
				cantidad = false;
			else
				cantidad = servicio.getManejaCantidades();
			vista.actualizarEncabezados(pase, cantidad);
		}
	}

	@SuppressWarnings("unchecked")
	public void actualizarUnidadesGestion(CompBuscar<IProducto> producto)
			throws ExcFiltroExcepcion {
		System.out.println("actualizando detalle");
		Row registro = (Row) producto.getParent();
		Labor labor = (Labor) producto.getSeleccion();
		if (labor != null) {
			negocio = NegocioSolicitudMecanizado.getInstance();
			List<UnidadMedida> unidades = negocio.getUnidadesMedida(labor);
			vista.refrescarUnidades(unidades,
					(CompDetalleGrilla<UnidadSolicitada>) registro
							.getChildren().get(3));
		}
	}

	public void actualizarDatosProdcutor() throws Exception {
		Productor productor = vista.getCedula().getSeleccion();
		if (productor != null) 
		{
			productor=	 negocio.getProductorProject(productor);
			productor = negocio.enriqueserProdcutor(productor);
			vista.refrescarProductor(productor);
			   //genero reporte de deudas
			   NegocioClienteAdministrativo negocio = NegocioClienteAdministrativo.getInstance();
				 Object[] deudor = negocio.getPendienteDocumentosNotasTotales(productor);
				   if ((((Double)deudor[3]+(Double)deudor[4]))>0){
						  Messagebox.show("Este Productor Posee Deudas Asociadas con la Institucion ");
						List<Object[]> deudores = new ArrayList<Object[]>();
						deudores.add(deudor);
							HashMap parametros = new HashMap();
							parametros.put("usuario", app.getUsuario().toString());
							parametros.put("logo", Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");

						
							JRDataSource ds = null;
							ds = new JRBeanCollectionDataSource(deudores);
							app.agregarReporte();
							Jasperreport reporte = app.getReporte();
							reporte.setSrc("reportes/reporteconsolidadodedeudas.jasper");
							reporte.setParameters(parametros);
							reporte.setDatasource(ds);
							reporte.setType("pdf");
						
						 
					 }//en if Deuda 
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void imprimir(SolicitudMecanizado solicitudMecanizado,
			IZkAplicacion app) {
		try {
			NegocioSolicitudMecanizado negocio = NegocioSolicitudMecanizado
					.getInstance();
			List<DetalleSolicitud> detallesSolicitud = new PerDetalleSolicitud()
					.getTodasErriquesidas(solicitudMecanizado);

			// negocio.getPersistencia().getDatos(solicitudMecanizado).getDetalles();

			HashMap parametros = new HashMap();
			// parametros.put("filtro", String.format("%1$td/%1$tm/%1$tY",
			// inicio));
			parametros.put(
					"fecha",
					String.format("%1$td/%1$tm/%1$tY",
							solicitudMecanizado.getFecha()));
			parametros.put("usuario", app.getUsuario().toString());
			parametros.put("logo", Index.class.getResource("/").getPath()
					+ "../../imagenes/cintillo_inst.png");
			JRDataSource ds = new JRBeanCollectionDataSource(detallesSolicitud);
			app.agregarReporte();
			Jasperreport reporte = app.getReporte();
			reporte.setSrc("reportes/solicitudMecanizado.jasper");
			
			for (DetalleSolicitud detalleSolicitud : detallesSolicitud) {
			System.out.println(detalleSolicitud.getCantidadSolicitada());
			System.out.println(detalleSolicitud.getSolicitado().size());
				System.out.println(detalleSolicitud.getProducto().getPrecioBase(detalleSolicitud.getSolicitud().getProductor().getTipo() ));
			}
			reporte.setParameters(parametros);
			reporte.setDatasource(ds);
			reporte.setType("pdf");
		} catch (NullPointerException e) {
			e.printStackTrace();
			app.mostrarError("No ha seleccionado ninguna solicitud");
		} catch (Exception e) {
			app.mostrarError(e.getMessage());
		}
	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}
}
