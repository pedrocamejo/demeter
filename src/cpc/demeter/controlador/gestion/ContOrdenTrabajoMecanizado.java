package cpc.demeter.controlador.gestion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.gestion.UiOrdenTrabajoMecanizado;
import cpc.modelo.demeter.administrativo.Contrato;
import cpc.modelo.demeter.administrativo.ContratoMecanizado;
import cpc.modelo.demeter.administrativo.DetalleContrato;
import cpc.modelo.demeter.basico.CicloProductivo;
import cpc.modelo.demeter.basico.Labor;
import cpc.modelo.demeter.basico.PlanServicio;
import cpc.modelo.demeter.basico.Rubro;
import cpc.modelo.demeter.basico.Servicio;
import cpc.modelo.demeter.basico.Trabajador;
import cpc.modelo.demeter.gestion.DetalleMaquinariaOrdenTrabajo;
import cpc.modelo.demeter.gestion.DetalleOrdenTrabajo;
import cpc.modelo.demeter.gestion.ImplementoUnidad;
import cpc.modelo.demeter.gestion.LaborOrdenServicio;
import cpc.modelo.demeter.gestion.MaquinariaUnidad;
import cpc.modelo.demeter.gestion.OrdenTrabajoMecanizado;
import cpc.modelo.demeter.gestion.SolicitudMecanizado;
import cpc.modelo.demeter.gestion.TrabajoRealizadoMecanizado;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.modelo.ministerio.gestion.InstitucionCrediticia;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.negocio.demeter.gestion.NegocioOrdenTrabajoMecanizado;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcAgregacionInvalida;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContOrdenTrabajoMecanizado extends ContVentanaBase<OrdenTrabajoMecanizado> {

	private AppDemeter 						app;
	private UiOrdenTrabajoMecanizado 		vista;
	private NegocioOrdenTrabajoMecanizado 	negocio;
	private LaborOrdenServicio 				labor;
	private int 							anularSolicitud;

	public ContOrdenTrabajoMecanizado(int modoOperacion,OrdenTrabajoMecanizado ordenTrabajo,ICompCatalogo<OrdenTrabajoMecanizado> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida 
	{
		super(modoOperacion);
		this.app = app;
		try {
			negocio = NegocioOrdenTrabajoMecanizado.getInstance();
			boolean anulado = false;
			boolean nuevo = false;
			boolean cerrado = false;

			if (ordenTrabajo == null || modoAgregar())
			{
				ordenTrabajo = negocio.getNuevaOrdenTrabajo();
				anulado = false;
				cerrado = false;
				nuevo = true;
			}
			else
			{
				nuevo = false;
				if (ordenTrabajo.getEstado().isFinalizada())
				{
					cerrado = true;
					if (modoAnular())
					{
						throw new ExcSeleccionNoValida("No puede Anular una orden Cerrada");
					}
					if (modoProcesar())
					{
						throw new ExcSeleccionNoValida("No puede Cerrar una orden ya Cerrada");
					}
				}
				if (!ordenTrabajo.getEstado().isActiva()) 
				{
					anulado = true;
				
					if (modoAnular())
					{
						throw new ExcSeleccionNoValida("No puede Anular una orden ya Anulada");
					}
					if (modoProcesar())
					{
						throw new ExcSeleccionNoValida(	"No puede Cerrar una orden Anulada");
					}
				}
			}
			
			List<Labor> labores = negocio.getLabores();
			List<CicloProductivo> ciclos = negocio.getCiclosProductivos();
			List<PlanServicio> planes = negocio.getPlanesServicios();
			List<Trabajador> trabajadores = negocio.getOperadores();
			List<Trabajador> tecnicos = negocio.getTecnicosCampos();
			List<UnidadFuncional> unidadesEjecutoras = negocio.getUnidadesEjecutoras();
			List<InstitucionCrediticia> financiamientos = negocio.getFinanciamientos();
			List<Rubro> rubros = negocio.getRubros();
			
			labor = ordenTrabajo.getLaborOrden();
		
			vista = new UiOrdenTrabajoMecanizado("Orden Trabajo Mecanizado (" + Accion.TEXTO[modoOperacion] + ")", 720, ciclos,
					unidadesEjecutoras, planes, labores, trabajadores, rubros,	financiamientos, tecnicos, nuevo, anulado, cerrado);

			setear(ordenTrabajo, vista, llamador, app);
			
			vista = ((UiOrdenTrabajoMecanizado) getVista());
			vista.desactivar(modoOperacion);
		
			if (modoAgregar())
			{
				cambiarModo("Contrato");
			}
			if (!modoAgregar())
			{
				refrescarSaldos();
			}
		} 
		catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun Servicio");
		} 
		catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcSeleccionNoValida(e.getMessage());
		}
	}

	public void eliminar() {

		try 
		{
			negocio = NegocioOrdenTrabajoMecanizado.getInstance();
			//negocio.setModelo(getDato());
			negocio.borrar();
		}
		catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() throws ExcFiltroExcepcion {
		try
		{
			negocio = NegocioOrdenTrabajoMecanizado.getInstance();
			actualizar();
			//negocio.setModelo(getDato());
			if (modoAgregar())
			{
				negocio.guardar(getDato());
			}
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError("Error guardando la orden, " + e.getMessage());
		}
	}

	public void cerrar() throws ExcFiltroExcepcion {
		if (modoProcesar()) 
		{
			try {
				validarCierre();
				actualizarCierre();
				
				negocio.cerrar(getDato());
				refrescarCatalogo();
				vista.close();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					app.mostrarError("Error Cerrando la orden, " + e.getMessage());
				}
		}
	}

	public void anular() throws ExcFiltroExcepcion 
	{
		if (modoAnular())
		{
			try
			{
				if (getDato().getSolicitud() != null)
				{
					//que es esto igual la va a anular ?  
					anularSolicitud = Messagebox.show("Desea anular la solicitud", "Pregunta",
							Messagebox.OK | Messagebox.CANCEL,	Messagebox.QUESTION);
				}
				if (getDato().getTrabajosRealizadosMecanizado().size() > 0)
				{
					throw new ExcDatosInvalidos("Tiene trabajos asociados");
				}

				negocio.anular(getDato(), (anularSolicitud == 1));
				refrescarCatalogo();
				vista.close();
			
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
				app.mostrarError("Error anulando la orden, " + e.getMessage());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private void refrescarSaldos() throws ExcAgregacionInvalida {
		try 
		{
			List<Listitem> filas = vista.getDetalleTrabajos().getItems();
			TrabajoRealizadoMecanizado detalle;
			double saldoProducido = 0;
			double saldoTrabajado = 0;
		
			for (Listitem item : filas) 
			{
				detalle = (TrabajoRealizadoMecanizado) item	.getAttribute("dato");
				if (getDato().getProduccion())
				{	if (detalle.getCantidadProduccion() != null)
					{
						saldoProducido += detalle.getCantidadProduccion();
					}
				
				}
				else saldoTrabajado += detalle.getCantidadTrabajo();
			}
			vista.getTotalTrabajado().setValue(saldoTrabajado);
			vista.getTotalPosibleProducido().setValue(saldoProducido);
		} 
		catch (Exception e) {
			e.printStackTrace();
			throw new ExcAgregacionInvalida("Problema Actualizado equipos, "
					+ e.getMessage());
		}
	}

	private void actualizarCierre() throws ExcAgregacionInvalida {
		
		if (modoProcesar())
		{
			DetalleOrdenTrabajo detalle = new DetalleOrdenTrabajo();
			detalle.setOrden(getDato());
			detalle.setLabor(getDato().getLaborOrden().getLabor());
			detalle.setUnidadTrabajo(getDato().getLaborOrden().getUnidad());
			detalle.setCantidadEjecutada(vista.getTotalTrabajado().getValue());
		
			if (getDato().getSolicitud() != null) 
			{
				detalle.setCantidadSolicitada(getDato().getLaborOrden().getCalculo());
			} 
			else
			{
				detalle.setCantidadSolicitada(0.00);
			}
			
			List<DetalleOrdenTrabajo> detalles = new ArrayList<DetalleOrdenTrabajo>();
			detalles.add(detalle);
			getDato().setDetalles(detalles);
		}
		getDato().setTransportado(vista.getUsaTransporte().isChecked());
		getDato().setCantidadLaborada(vista.getTotalLaborado().getValue());
		getDato().setProduccion(labor.getLabor().getServicio().getProduccion());
	}

	private void actualizar() throws ExcAgregacionInvalida, ExcFiltroExcepcion 
	{
		if (modoAgregar()) 
		{
			labor = new LaborOrdenServicio();
			labor.setLabor(vista.getLabor().getSeleccion());
			labor.setFisico(vista.getFisico().getValue());
			labor.setUnidad(vista.getUnidadFisica().getModelo());
			labor.setCantidad(vista.getCantidad().getValue());
			labor.setPases(vista.getPases().getValue());
			labor.setOrden(getDato());
		} 
		else
		{
			labor = getDato().getLaborOrden();
		}
		
		getDato().setProduccion(labor.getLabor().getServicio().getProduccion());
		getDato().setLaborOrden(labor);
		
		if (vista.getContrato().getSeleccion() != null)
		{
			getDato().setContrato(negocio.getEnriqueserCtto(vista.getContrato().getSeleccion().getContrato()));
			
		}
		actualizarMaquinaria();
	}

	@SuppressWarnings("unchecked")
	private void actualizarMaquinaria() throws ExcAgregacionInvalida 
	{
		try 
		{
			List<DetalleMaquinariaOrdenTrabajo> detalles = new ArrayList<DetalleMaquinariaOrdenTrabajo>();
			List<Row> filas = vista.getDetalle().getFilas().getChildren();
			DetalleMaquinariaOrdenTrabajo detalle;
			for (Row item : filas)
			{
				detalle = (DetalleMaquinariaOrdenTrabajo) item.getAttribute("dato");
				detalle.setOrdenTrabajo(getDato());
				detalles.add(detalle);
			}
			getDato().setEquipos(detalles);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExcAgregacionInvalida("Problema Actualizado equipos, "
					+ e.getMessage());
		}
	}

	public void validarCierre() throws WrongValuesException,
			ExcEntradaInconsistente {

		if ((vista.getTotalFisicoTrabajado().getValue() == null)) 
		{
			throw new WrongValueException(vista.getTotalFisicoTrabajado(),"Indique un valor para el total verificado");

		}
		if ((vista.getTotalLaborado().getValue() == null)) 
		{
			throw new WrongValueException(vista.getTotalLaborado(),	"Indique un valor para el total laborado");
		}
		if ((vista.getTotalTrabajado().getValue() == null))
		{
			throw new WrongValueException(vista.getTotalTrabajado(),"No tiene trabajo en labores");
		}
		if (getDato().getProduccion()) 
		{
			if (vista.getTotalPosibleProducido().getValue() == null) 
			{
				throw new WrongValueException(vista.getTotalPosibleProducido(),	"No tiene produccion en labores");
			}
			if (vista.getTotalRealProducido().getValue() == null) 
			{
				throw new WrongValueException(vista.getTotalRealProducido(),"No tiene produccion real en labores");
			}
		}
		if (getDato().getTrabajosRealizadosMecanizado().size() < 1) 
		{
			throw new WrongValueException(vista.getDetalleTrabajos(),"No tiene trabajos asociados");
		}
	}

	@SuppressWarnings("unchecked")
	public void validarDetalle() throws WrongValuesException,ExcEntradaInconsistente
	{
		List<Row> filas = vista.getDetalle().getFilas().getChildren();
		if (filas.size() < 1)
		{	
			throw new WrongValueException(vista.getDetalle(),"Indique Operador y equipo");
		}
		CompCombobox<Trabajador> trabajador;
		CompBuscar<MaquinariaUnidad> maquinaria;
		
		for (Row item : filas) 
		{
			trabajador = (CompCombobox<Trabajador>) item.getChildren().get(0);
			maquinaria = (CompBuscar<MaquinariaUnidad>) item.getChildren().get(1);
			if (trabajador.getSeleccion() == null)
			{
				throw new WrongValueException(trabajador,"Operador no seleccionado");
			}
			if (maquinaria.getSeleccion() == null)
			{
				throw new WrongValueException(maquinaria,"Maquinaria no seleccionado");
			}
		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente 
	{
		if (getModoOperacion() != Accion.ELIMINAR) 
		{
			if ((vista.getLabor().getSeleccion() == null))
			{
				throw new WrongValueException(vista.getLabor(),	"Indique una labor");
			}
			if (vista.getCedula().getSeleccion() == null)
			{
				throw new WrongValueException(vista.getCedula(),"Indique productor");
			}
			if (vista.getUnidadProductiva().getSeleccion() == null)
			{
				throw new WrongValueException(vista.getUnidadProductiva(),"Indique Unidad Produccion");
			}
			if (vista.getUnidadFuncional().getSeleccion() == null)
			{
				throw new WrongValueException(vista.getUnidadFuncional(),"Indique Unidad Funcional");
			}
			if (vista.getRubro().getSeleccion() == null)
			{
				throw new WrongValueException(vista.getRubro(), "Indique Rubro");
			}
			if (vista.getPlanServicio().getSeleccion() == null)
			{
				throw new WrongValueException(vista.getPlanServicio(),"Indique Plan de Servicio");
			}
			if (vista.getCicloProductivo().getSeleccion() == null)
			{
				throw new WrongValueException(vista.getCicloProductivo(),"Indique Ciclo");
			}
			if (vista.getFisico().getValue() == null && vista.getFisico().getValue() == 0)
			{
				throw new WrongValueException(vista.getFisico(),"Indique valor");
				
			}
			validarDetalle();
		}

	}

	public void onEvent(Event event) throws Exception {

		if (event.getTarget() == getVista().getAceptar()) 
		{
			procesarCRUD(event);

		}
		else if (event.getName() == CompBuscar.ON_SELECCIONO && event.getTarget() == vista.getContrato()) 
		{
			DetalleContrato decontrato = vista.getContrato().getSeleccion();
			decontrato =negocio.getDetalleContratoProject(decontrato);
			//hay que revisar que el metodo negocio.getContratosFiltrados() para que solo traiga contratos de mecanizado
			Class<? extends Contrato> clasea = decontrato.getContrato().getClass();
			Class<ContratoMecanizado> claseb = ContratoMecanizado.class;
			if (!clasea.toString().equals(claseb.toString()))
				throw new WrongValueException(vista.getContrato(),	"el contrato no es de mecanizado");
			actualizarContrato(decontrato);
		}
		else if (event.getName() == CompBuscar.ON_SELECCIONO && event.getTarget() == vista.getSolicitud()) 
		{
			actualizarSolicitud(vista.getSolicitud().getSeleccion());
		} else if (event.getName() == Events.ON_SELECTION && event.getTarget() == vista.getUnidadFuncional())
		{
			actualizarActivos(vista.getUnidadFuncional().getSeleccion());
		}
		else if (event.getName() == CompBuscar.ON_SELECCIONO && event.getTarget() == vista.getCedula()) {
			actualizarDatosProductor(vista.getCedula().getSeleccion());
		}
		else if (event.getName() == Events.ON_SELECTION && event.getTarget() == vista.getLabor()) {
			actualizarLabor(vista.getLabor().getSeleccion());
		}
		else if (event.getName() == Events.ON_SELECTION && event.getTarget() == vista.getModoTrabajo()) {
			cambiarModo(vista.getModoTrabajo().getSeleccion());
		}
		else if (event.getName().equals(CompBuscar.ON_SELECCIONO) 	&& event.getTarget().getAttribute("nombre")	.equals("maquinaria"))
		{
			// Para Maquinaria
			CompBuscar<MaquinariaUnidad> componente = (CompBuscar<MaquinariaUnidad>) event.getTarget();
			actualizarSerialMaquinaria(componente);
		} 
		else if (event.getName().equals(CompBuscar.ON_SELECCIONO)&& event.getTarget().getAttribute("nombre").equals("implemento"))
		{
			// Para implemento
			CompBuscar<ImplementoUnidad> componente = (CompBuscar<ImplementoUnidad>) event.getTarget();
			actualizarSerialImplemento(componente);
		} 
		else 
		{
			actualizarCantidad();
		}
	}

	public void mostrarAlertaMaquinaria(
			List<OrdenTrabajoMecanizado> ordenesConMismaMaquinaria,
			CompBuscar<MaquinariaUnidad> componente)
			throws InterruptedException {
		if (ordenesConMismaMaquinaria != null) {
			if (ordenesConMismaMaquinaria.size() > 0) {
				componente.setStyle("color: red;");
				return;
				// app.mostrarAdvertencia("Esta Maquinaria se encuentra asignada a otras "
				// + ordenesConMismaMaquinaria.size() +
				// " Ordenes de Trabajo activas");
			}
		}
		componente.setStyle("color: black;");
	}

	public void mostrarAlertaImplemento(
			List<OrdenTrabajoMecanizado> ordenesConMismoImplemento,
			CompBuscar<ImplementoUnidad> componente)
			throws InterruptedException {
		if (ordenesConMismoImplemento != null) {
			if (ordenesConMismoImplemento.size() > 0) {
				componente.setStyle("color: red;");
				return;
				// app.mostrarAdvertencia("Esta Maquinaria se encuentra asignada a otras "
				// + ordenesConMismaMaquinaria.size() +
				// " Ordenes de Trabajo activas");
			}
		}
		componente.setStyle("color: black;");
	}

	public void actualizarSerialMaquinaria(
			CompBuscar<MaquinariaUnidad> componente) throws ExcFiltroExcepcion {
		try {
			Row registro = (Row) componente.getParent();
			((Label) registro.getChildren().get(2)).setValue(componente.getValorObjeto().toString());
		} catch (Exception e) {
			throw new ExcFiltroExcepcion("Problemas con la Maquinaria");
		}
	}

	public void actualizarSerialImplemento(
			CompBuscar<ImplementoUnidad> componente) throws ExcFiltroExcepcion {
		try {
			Row registro = (Row) componente.getParent();
			((Label) registro.getChildren().get(4)).setValue(componente
					.getValorObjeto().toString());
		} catch (Exception e) {
			throw new ExcFiltroExcepcion("Problemas con el Implemento");
		}
	}

	public void cambiarModo(String Seleccion) throws ExcFiltroExcepcion {
		int index = 0;
		if (Seleccion == "Contrato")
			index = 0;
		if (Seleccion == "Solicitud")
			index = 1;
		if (Seleccion == "Sin Entrada")
			index = 2;
		vista.suichearModo(index);
		List<DetalleContrato> lista;
		switch (index) {
		case 0:
			/*
			 * //System.out.println("pqc en la lista ");
			 * lista=negocio.getContratos(); for (DetalleContrato
			 * detalleContrato : lista) { ContratoMecanizado c =
			 * negocio.getEnriqueserCtto(detalleContrato.getContrato());
			 * detalleContrato
			 * .getContrato().setPagador(c.getUnidadProductiva().getProductor
			 * ());
			 * 
			 * }
			 */
			if (!modoAgregar()){
				
				List<DetalleContrato> contratos = getDato().getContrato().getDetallesContrato();
				vista.refrescarContrato(contratos );
		//		vista.refrescarContrato(negocio.getContratos());
			}
			else
				vista.refrescarContrato(negocio.getContratosFiltrados());
			break;
		case 1:
			vista.refrescarSolicitud(negocio.getSolicitudes());
			break;
		case 2:
			vista.refrescarProductores(negocio.getProductores());
			break;
		}
	}

	public void actualizarActivos(UnidadFuncional unidad) throws ExcFiltroExcepcion 
	{
		vista.getDetalle().clear();
		List<ImplementoUnidad> implementos = negocio.getImplementos(unidad);
		List<MaquinariaUnidad> equipos = negocio.getMaquinarias(unidad);
	
		if (!modoAgregar())
		{
			agregarMaquinariasDesincorporadasOTransferidas(equipos);
			agregarImpementosDesicorporadosOTransferidos(implementos);
		}
		vista.refrescarImplementos(implementos);
		vista.refrescarMaquinaria(equipos);
	}

	public void agregarMaquinariasDesincorporadasOTransferidas(
			List<MaquinariaUnidad> equipos) {
		List<DetalleMaquinariaOrdenTrabajo> listaDMOTA = getDato().getEquipos();
		for (DetalleMaquinariaOrdenTrabajo detalleMaquinariaOrdenTrabajo : listaDMOTA) {
			// Agregar Maquinarias Faltantes
			MaquinariaUnidad maquinariaActual = detalleMaquinariaOrdenTrabajo
					.getMaquinaria();
			if (maquinariaActual != null && !equipos.contains(maquinariaActual)) {
				equipos.add(maquinariaActual);
			}
		}
	}

	public void agregarImpementosDesicorporadosOTransferidos(
			List<ImplementoUnidad> implementos) {
		List<DetalleMaquinariaOrdenTrabajo> listaDMOTA = getDato().getEquipos();
		for (DetalleMaquinariaOrdenTrabajo detalleMaquinariaOrdenTrabajo : listaDMOTA) {
			// Agregar Implementos Faltantes
			ImplementoUnidad implementoActual = detalleMaquinariaOrdenTrabajo
					.getImplemento();
			if (implementoActual != null
					&& !implementos.contains(implementoActual)) {
				implementos.add(implementoActual);
			}
		}
	}

	public void actualizarContrato() throws ExcFiltroExcepcion {
		DetalleContrato detalle = negocio.getDetalleContrato(getDato());
		// vista.getContrato().setSeleccion(detalle);
		// detalle.setUnidadCobro(null);
		actualizarContrato(detalle);
		// vista.getContrato().setSeleccion(detalle);
	}

	public void actualizarContrato(DetalleContrato ctto) throws ExcFiltroExcepcion 
	{
		ContratoMecanizado contrato = negocio.getEnriqueserCtto(ctto.getContrato());
		
		if (contrato != null)
		{
			if (contrato.getSolicitud() != null)
			{
				List<SolicitudMecanizado> solicitudes = new ArrayList<SolicitudMecanizado>();
				List<DetalleContrato> detalles = new ArrayList<DetalleContrato>();
			
				detalles.add(ctto);
				vista.refrescarContrato(detalles);
				vista.getContrato().setSeleccion(ctto);
				
				solicitudes.add(contrato.getSolicitud());
				
				vista.refrescarSolicitud(solicitudes);
				vista.getSolicitud().setSeleccion(contrato.getSolicitud());
				
				actualizarSolicitud(contrato.getSolicitud());
				actualizarServicio(ctto, contrato.getSolicitud());
				
			} 
			else 
			{
				List<SolicitudMecanizado> solicitudes = new ArrayList<SolicitudMecanizado>();
				vista.refrescarSolicitud(solicitudes);
				// vista.getSolicitud().setSeleccion(null);
				vista.getSolicitud().setValue("");
				vista.getUnidadFuncional().setDisabled(false);
				vista.getCicloProductivo().setDisabled(false);
				vista.getRubro().setDisabled(false);
				vista.getPlanServicio().setDisabled(false);
				vista.getUnidadFuncional().setSeleccion(null);
				vista.getCicloProductivo().setSeleccion(null);
				vista.getRubro().setSeleccion(null);
				vista.getPlanServicio().setSeleccion(null);
				List<Productor> productores = new ArrayList<Productor>();
				productores.add(contrato.getUnidadProductiva().getProductor());
				vista.refrescarProductores(productores);
				vista.getCedula().setSeleccion(
						contrato.getUnidadProductiva().getProductor());
				actualizarDatosProductor(contrato.getUnidadProductiva()
						.getProductor());
				vista.getUnidadProductiva().setSeleccion(
						contrato.getUnidadProductiva());
				actualizarServicio(ctto);
			}
		}
	}

	public void actualizarSolicitud(SolicitudMecanizado solicitud)throws ExcFiltroExcepcion 
	{
		if (solicitud != null) 
		{
			vista.getCicloProductivo().setSeleccion(solicitud.getCiclo());
			vista.getUnidadFuncional().setSeleccion(solicitud.getUnidadEjecutora());
			vista.getPlanServicio().setSeleccion(solicitud.getPlan());
			vista.getRubro().setSeleccion(solicitud.getRubro());
	
			List<Productor> productores = new ArrayList<Productor>();
			productores.add(solicitud.getProductor());
			
			vista.refrescarProductores(productores);
			vista.getCedula().setSeleccion(solicitud.getProductor());
			actualizarDatosProductor(solicitud.getProductor());
			
			vista.getUnidadProductiva().setSeleccion(solicitud.getDireccion());
	
			// corre
			// actualizarServicio(solicitud);

			actualizarActivos(solicitud.getUnidadEjecutora());
			
			vista.getFinanciamiento().setSeleccion(solicitud.getFinanciamiento());
			vista.getUnidadFuncional().setDisabled(true);
			vista.getCicloProductivo().setDisabled(true);
			vista.getRubro().setDisabled(true);
			vista.getPlanServicio().setDisabled(true);
		}
	}

	public void actualizarDatosProductor(Productor productor)
			throws ExcFiltroExcepcion {
		if (productor != null) {
			productor = negocio.enriqueserProductor(productor);
			vista.refrescarProductor(productor);
		}
	}

	public void actualizarServicio(SolicitudMecanizado solicitud)
			throws ExcFiltroExcepcion {
		List<Labor> labores = null;
		if (solicitud == null)
			labores = negocio.getLabores();
		else {
			labores = negocio.getLabores(solicitud);
		}
		vista.refrescarLabor(labores);
		if (labores.size() == 1) {
			vista.getLabor().setSeleccion(labores.get(0));
			vista.getLabor().setDisabled(true);
			actualizarLabor(labores.get(0));
		}
	}

	public void actualizarServicio(DetalleContrato ctto)
			throws ExcFiltroExcepcion {
		List<Labor> labores = null;

		if (ctto == null)
			labores = negocio.getLabores();
		else {
			labores = new ArrayList<Labor>();
			labores.add((Labor) ctto.getProducto());
		}
		vista.refrescarLabor(labores);
		if (labores.size() == 1) {
			vista.getLabor().setSeleccion(labores.get(0));
			vista.getLabor().setDisabled(true);
			actualizarLabor(labores.get(0));
			LaborOrdenServicio laborE = negocio.getDetalleLabor(ctto);
			if (modoAgregar())
				vista.refrescarDatosLabor(laborE, false);
			else
				vista.refrescarDatosLabor(laborE, false);
		}
	}

	public void actualizarServicio(DetalleContrato ctto,
			SolicitudMecanizado solicitud) throws ExcFiltroExcepcion {
		List<Labor> labores = null;
		if (solicitud == null)
			labores = negocio.getLabores();
		else {
			labores = negocio.getLabores(solicitud);
		}

		vista.refrescarLabor(labores);

		for (Labor labor : labores) {
			String a = labor.getDescripcion();
			String b = ctto.getProducto().getDescripcion();
			if (a.equals(b)) {
				vista.getLabor().setSeleccion(labor);
				vista.getLabor().setDisabled(true);
				actualizarLabor(labor);
				break;

			}
		}

	}

	public void actualizarLabor(Labor labor) throws ExcFiltroExcepcion {
		if (labor != null) {
			SolicitudMecanizado solicitud = vista.getSolicitud().getSeleccion();
			if (solicitud == null) {
				LaborOrdenServicio laborE = negocio.getDetalleLabor(labor);
				if (modoAgregar())
					vista.refrescarDatosLabor(laborE, true);
				else
					vista.refrescarDatosLabor(laborE, false);
			} else {
				LaborOrdenServicio laborE = negocio.getDetalleLabor(solicitud,
						labor);
				vista.refrescarDatosLabor(laborE, false);
			}
		}
	}

	private void actualizarCantidad() {
		double valor = vista.getFisico().getValue()
				* vista.getCantidad().getValue() * vista.getPases().getValue();
		vista.getTotal().setValue(valor);
	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		try {
			cerrar();
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			app.mostrarError(e.getMensajeError());
		}

	}

}
