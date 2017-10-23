package cpc.demeter.vista.gestion;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import cpc.ares.modelo.Accion;
//import cpc.modelo.demeter.gestion.ActaAutorizacion;
import cpc.modelo.demeter.gestion.EstadoMovimientoActivo;
import cpc.modelo.demeter.gestion.SalidaActivo;
import cpc.modelo.demeter.gestion.TrasladoActivo;
import cpc.modelo.sigesp.basico.Activo;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;

public class UISalidaActivo extends
		CompVentanaMaestroDetalle<TrasladoActivo, SalidaActivo> {

	private static final long serialVersionUID = 150239458707852037L;
	private List<UnidadAdministrativa> unidadesAdministrativas,
			unidadesATrasladar;
	private String usuario;
	private int accion;
	private EstadoMovimientoActivo anulado;
	private List<Activo> activos;
	private List<Modelo> modelos;
	// private List<ActaAutorizacion> actas;
	private Boolean unidadesGlobales;

	private CompGrupoDatos cuatroColumnas, dosColumnas, pieDosColumnas,
			filtroActivos;
	private Label lblNumero, lblFecha, lblUnidadAdministrativa, lblUsuario,
			lblEstado, lblAnuladoPor, lblFechaAnulacion,
			lblResponsableTraslado, lblCedulaResponsable, lblVehiculo,
			lblPlacaVehiculo, lblMotivoAnulación, lblObservacionGeneral;
	// lblActaAutorizacion,
	private Textbox txtMotivoAnulacion, txtObservacionGeneral,
			txtResponsableTraslado, txtCedulaResponsable, txtVehiculo,
			txtPlacaVehiculo;

	// private CompBuscar<ActaAutorizacion> cmpActaMovimiento;
	private CompBuscar<UnidadAdministrativa> cmbUnidadAdministrativas,
			cmbUnidadesParaTrasladar;
	private CompBuscar<Modelo> cmbModelos;

	public UISalidaActivo(String titulo, int ancho, int modo,
			EstadoMovimientoActivo estadoAnulado,
			List<UnidadAdministrativa> unidades, Boolean unidadesGlobales,
			List<Activo> activos, List<Modelo> modelosActivos,
			List<UnidadAdministrativa> unidadesTraslado) {
		super(titulo, ancho);
		this.unidadesAdministrativas = unidades;
		this.accion = modo;
		this.anulado = estadoAnulado;
		// this.actas = actasAutorizacion;
		this.unidadesGlobales = unidadesGlobales;
		this.activos = activos;
		this.modelos = modelosActivos;
		this.unidadesATrasladar = unidadesTraslado;
	}

	public UISalidaActivo(String titulo, int ancho, int modo,
			EstadoMovimientoActivo estadoAnulado) {
		super(titulo, ancho);
		this.accion = modo;
		this.anulado = estadoAnulado;
	}

	private List<CompEncabezado> getEncabezadoModelo() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Marca. ", 150);
		titulo.setMetodoBinder("getDescripcionMarca");
		lista.add(titulo);

		titulo = new CompEncabezado("Modelo. ", 150);
		titulo.setMetodoBinder("getDescripcionModelo");
		lista.add(titulo);

		return lista;
	}

	/*
	 * public List<CompEncabezado> encabezadoActaAutorizacion(){
	 * List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
	 * CompEncabezado titulo;
	 * 
	 * titulo = new CompEncabezado("Número Acta",200);
	 * titulo.setMetodoBinder("getNumeroActa"); encabezado.add(titulo);
	 * 
	 * titulo = new CompEncabezado("Tipo Acta", 150);
	 * titulo.setMetodoBinder("getNombreTipoActa"); encabezado.add(titulo);
	 * 
	 * titulo = new CompEncabezado("Fecha", 150);
	 * titulo.setMetodoBinder("getFechaString"); encabezado.add(titulo);
	 * 
	 * return encabezado; }
	 */

	@Override
	public void inicializar() {
		setGbEncabezado(1);
		setGbPie(1);
		cuatroColumnas = new CompGrupoDatos(4);
		dosColumnas = new CompGrupoDatos(2);
		pieDosColumnas = new CompGrupoDatos(2);
		lblNumero = new Label();
		lblFecha = new Label();
		lblUsuario = new Label();
		lblEstado = new Label();

		switch (accion) {
		case Accion.AGREGAR:
			// cmpActaMovimiento = new
			// CompBuscar<ActaAutorizacion>(encabezadoActaAutorizacion(), 0);
			if (unidadesGlobales) {
				cmbUnidadAdministrativas = new CompBuscar<UnidadAdministrativa>(
						getEncabezadoUnidad(), 1);
			} else {
				lblUnidadAdministrativa = new Label();
			}
			cmbUnidadesParaTrasladar = new CompBuscar<UnidadAdministrativa>(
					getEncabezadoUnidad(), 1);
			txtResponsableTraslado = new Textbox();
			txtCedulaResponsable = new Textbox();
			txtVehiculo = new Textbox();
			txtPlacaVehiculo = new Textbox();
			cmbModelos = new CompBuscar<Modelo>(getEncabezadoModelo(), 1);
			filtroActivos = new CompGrupoDatos(2);
			txtObservacionGeneral = new Textbox();
			break;
		case Accion.CONSULTAR:
			lblUnidadAdministrativa = new Label();
			// lblActaAutorizacion = new Label();
			lblResponsableTraslado = new Label();
			lblCedulaResponsable = new Label();
			lblVehiculo = new Label();
			lblPlacaVehiculo = new Label();
			lblObservacionGeneral = new Label();
			if (getModelo().getEstado().getIdEstado()
					.equals(anulado.getIdEstado())) {
				lblAnuladoPor = new Label();
				lblFechaAnulacion = new Label();
				lblMotivoAnulación = new Label();
			}
			break;
		case Accion.ANULAR:
			lblUnidadAdministrativa = new Label();
			lblResponsableTraslado = new Label();
			lblCedulaResponsable = new Label();
			lblVehiculo = new Label();
			lblPlacaVehiculo = new Label();
			lblAnuladoPor = new Label();
			lblFechaAnulacion = new Label();
			// lblActaAutorizacion = new Label();
			txtMotivoAnulacion = new Textbox();
			lblObservacionGeneral = new Label();
			break;
		}
	}

	@Override
	public void dibujar() {
		lblNumero.setWidth("300px");
		lblFecha.setWidth("500px");
		lblUsuario.setWidth("200px");

		cuatroColumnas.addComponente("Nro Transferencia: ", lblNumero);
		cuatroColumnas.addComponente("Fecha: ", lblFecha);
		cuatroColumnas.dibujar(getGbEncabezado());

		switch (accion) {
		case Accion.AGREGAR:
			if (unidadesGlobales) {
				cmbUnidadAdministrativas.setAncho(600);
				cmbUnidadAdministrativas.setWidth("500px");
				cmbUnidadAdministrativas.setModelo(unidadesAdministrativas);
				/*
				 * cmbUnidadAdministrativas.setAttribute("nombre", "unidades");
				 * cmbUnidadAdministrativas
				 * .setListenerEncontrar(getControlador());
				 */
				dosColumnas.addComponente("Unidad Administrativa: ",
						cmbUnidadAdministrativas);
			} else {
				lblUnidadAdministrativa.setWidth("400px");
				dosColumnas.addComponente("Unidad Administrativa: ",
						lblUnidadAdministrativa);
			}
			/*
			 * cmpActaMovimiento.setAncho(500);
			 * cmpActaMovimiento.setWidth("200px");
			 * cmpActaMovimiento.setListenerEncontrar(getControlador());
			 * cmpActaMovimiento.setAttribute("nombre", "acta");
			 * dosColumnas.addComponente("Acta De Autorización: ",
			 * cmpActaMovimiento);
			 */
			cmbUnidadesParaTrasladar.setAncho(600);
			cmbUnidadesParaTrasladar.setWidth("500px");
			cmbUnidadesParaTrasladar.setModelo(unidadesATrasladar);
			cmbUnidadesParaTrasladar.setAttribute("nombre", "unidadesTraslado");
			dosColumnas.addComponente("Trasladado a: ",
					cmbUnidadesParaTrasladar);
			txtResponsableTraslado.setWidth("500px");
			txtResponsableTraslado.setMaxlength(50);
			dosColumnas.addComponente("Responsable del Traslado: ",
					txtResponsableTraslado);
			txtCedulaResponsable.setWidth("200px");
			txtCedulaResponsable.setMaxlength(10);
			dosColumnas.addComponente("Cédula del Responsable: ",
					txtCedulaResponsable);
			txtVehiculo.setWidth("500px");
			txtVehiculo.setMaxlength(50);
			dosColumnas.addComponente("Vehículo: ", txtVehiculo);
			txtPlacaVehiculo.setWidth("200px");
			txtPlacaVehiculo.setMaxlength(10);
			dosColumnas.addComponente("Placa del Vehículo: ", txtPlacaVehiculo);
			dosColumnas.addComponente("Registrado por: ", lblUsuario);
			dosColumnas.addComponente("Estado: ", lblEstado);
			dosColumnas.dibujar(getGbEncabezado());
			cmbModelos.setAncho(450);
			cmbModelos.setWidth("450px");
			cmbModelos.setModelo(modelos);
			cmbModelos.setAttribute("nombre", "modelos");
			cmbModelos.setListenerEncontrar(getControlador());
			filtroActivos.setTitulo("Filtro para la lista de Activos");
			filtroActivos.addComponente("Marca y Modelo: ", cmbModelos);
			filtroActivos.dibujar(getGbEncabezado());
			txtObservacionGeneral.setRows(2);
			txtObservacionGeneral.setWidth("400px");
			txtObservacionGeneral.setMaxlength(200);
			pieDosColumnas.addComponente("Observación General: ",
					txtObservacionGeneral);
			pieDosColumnas.dibujar(getGbPie());
			setDetalles(cargarLista(), getModelo().getSalidas(),
					encabezadosPrimario());
			getDetalle().setListenerBorrar(getControlador());
			getDetalle().setNuevo(new SalidaActivo());
			break;
		case Accion.CONSULTAR:
			lblUnidadAdministrativa.setWidth("400px");
			dosColumnas.addComponente("Unidad Administrativa: ",
					lblUnidadAdministrativa);
			// dosColumnas.addComponente("Acta De Autorización:",
			// lblActaAutorizacion);
			dosColumnas.addComponente("Responsable del Traslado: ",
					lblResponsableTraslado);
			dosColumnas.addComponente("Cédula del Responsable: ",
					lblCedulaResponsable);
			dosColumnas.addComponente("Vehículo: ", lblVehiculo);
			dosColumnas.addComponente("Placa del Vehículo: ", lblPlacaVehiculo);
			dosColumnas.addComponente("Registrado por: ", lblUsuario);
			dosColumnas.addComponente("Estado: ", lblEstado);
			pieDosColumnas.addComponente("Observación General: ",
					lblObservacionGeneral);
			if (getModelo().getEstado().getIdEstado()
					.equals(anulado.getIdEstado())) {
				dosColumnas.addComponente("Fecha Anulación: ",
						lblFechaAnulacion);
				dosColumnas.addComponente("Anulado Por: ", lblAnuladoPor);
				dosColumnas.addComponente("Motivo Anulación: ",
						lblMotivoAnulación);
			}
			dosColumnas.dibujar(getGbEncabezado());
			pieDosColumnas.dibujar(getGbPie());
			setDetalles(cargarListaDos(), getModelo().getSalidas(),
					encabezadosPrimarioDos());
			getDetalle().setNuevo(new SalidaActivo());
			break;
		case Accion.ANULAR:
			lblUnidadAdministrativa.setWidth("400px");
			dosColumnas.addComponente("Unidad Administrativa: ",
					lblUnidadAdministrativa);
			txtMotivoAnulacion.setWidth("600px");
			txtMotivoAnulacion.setRows(2);
			txtMotivoAnulacion.setMaxlength(200);
			// dosColumnas.addComponente("Acta De Autorización:",
			// lblActaAutorizacion);
			dosColumnas.addComponente("Responsable del Traslado: ",
					lblResponsableTraslado);
			dosColumnas.addComponente("Cédula del Responsable: ",
					lblCedulaResponsable);
			dosColumnas.addComponente("Vehículo: ", lblVehiculo);
			dosColumnas.addComponente("Placa del Vehículo: ", lblPlacaVehiculo);
			dosColumnas.addComponente("Registrado por: ", lblUsuario);
			dosColumnas.addComponente("Estado: ", lblEstado);
			dosColumnas.addComponente("Fecha Anulación: ", lblFechaAnulacion);
			dosColumnas.addComponente("Anulado Por: ", lblAnuladoPor);
			dosColumnas.addComponente("Motivo Anulación: ", txtMotivoAnulacion);
			dosColumnas.dibujar(getGbEncabezado());
			pieDosColumnas.addComponente("Observación General: ",
					lblObservacionGeneral);
			pieDosColumnas.dibujar(getGbPie());
			setDetalles(cargarListaDos(), getModelo().getSalidas(),
					encabezadosPrimarioDos());
			getDetalle().setNuevo(new SalidaActivo());
			break;
		}
		getDetalle().getGrilla().setMold("");
		dibujarEstructura();
		setAltoDetalle(200);
		addBotonera();

	}

	@Override
	public void cargarValores(TrasladoActivo arg0) throws ExcDatosInvalidos {
		lblUsuario.setValue(getModelo().getUsuario());

		try {
			lblNumero.setValue(getModelo().getControl());
			lblFecha.setValue(getModelo().getfechastring());
			lblEstado.setValue(getModelo().getNombreEstado());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		switch (accion) {
		case Accion.AGREGAR:
			if (unidadesGlobales) {
				cmbUnidadAdministrativas.setSeleccion(getModelo()
						.getUnidadAdministrativa());
				getBinder().addBinding(cmbUnidadAdministrativas, "seleccion",
						getNombreModelo() + ".unidadAdministrativa", null,
						null, "save", null, null, null, null);
			} else {
				lblUnidadAdministrativa.setValue(getModelo()
						.getUnidadAdministrativa().getNombre());
			}
			/*
			 * cmpActaMovimiento.setModelo(actas);
			 * getBinder().addBinding(cmpActaMovimiento, "seleccion",
			 * getNombreModelo()+".actaAutorizacion", null, null, "save", null,
			 * null, null, null);
			 */
			txtResponsableTraslado.setValue(getModelo()
					.getResponsableTraslado());
			getBinder().addBinding(txtResponsableTraslado, "value",
					getNombreModelo() + ".responsableTraslado", null, null,
					"save", null, null, null, null);
			txtCedulaResponsable.setValue(getModelo().getCedulaResponsable());
			getBinder().addBinding(txtCedulaResponsable, "value",
					getNombreModelo() + ".cedulaResponsable", null, null,
					"save", null, null, null, null);
			txtVehiculo.setValue(getModelo().getVehiculo());
			getBinder().addBinding(txtVehiculo, "value",
					getNombreModelo() + ".vehiculo", null, null, "save", null,
					null, null, null);
			txtPlacaVehiculo.setValue(getModelo().getPlacaVehiculo());
			getBinder().addBinding(txtPlacaVehiculo, "value",
					getNombreModelo() + ".placaVehiculo", null, null, "save",
					null, null, null, null);
			txtObservacionGeneral.setValue(getModelo().getObservaciones());
			getBinder().addBinding(txtObservacionGeneral, "value",
					getNombreModelo() + ".observaciones", null, null, "save",
					null, null, null, null);
			break;
		case Accion.CONSULTAR:
			lblUnidadAdministrativa.setValue(getModelo()
					.getUnidadAdministrativa().getNombre());
			// lblActaAutorizacion.setValue(getModelo().getNumeroActaAutorizacion());
			lblResponsableTraslado.setValue(getModelo()
					.getResponsableTraslado());
			lblCedulaResponsable.setValue(getModelo().getCedulaResponsable());
			lblVehiculo.setValue(getModelo().getVehiculo());
			lblPlacaVehiculo.setValue(getModelo().getPlacaVehiculo());
			lblObservacionGeneral.setValue(getModelo().getObservaciones());
			if (getModelo().getEstado().getIdEstado()
					.equals(anulado.getIdEstado())) {
				lblFechaAnulacion.setValue(getModelo()
						.getFechaAnulacionString());
				lblAnuladoPor.setValue(getModelo().getAnuladoPor());
				lblMotivoAnulación.setValue(getModelo().getMotivoAnulacion());
			}
			break;
		case Accion.ANULAR:
			lblUnidadAdministrativa.setValue(getModelo()
					.getUnidadAdministrativa().getNombre());
			// lblActaAutorizacion.setValue(getModelo().getNumeroActaAutorizacion());
			lblResponsableTraslado.setValue(getModelo()
					.getResponsableTraslado());
			lblCedulaResponsable.setValue(getModelo().getCedulaResponsable());
			lblVehiculo.setValue(getModelo().getVehiculo());
			lblPlacaVehiculo.setValue(getModelo().getPlacaVehiculo());
			lblFechaAnulacion.setValue(getModelo().getFechaAnulacionString());
			lblAnuladoPor.setValue(getModelo().getAnuladoPor());
			txtMotivoAnulacion.setValue(getModelo().getMotivoAnulacion());
			getBinder().addBinding(txtMotivoAnulacion, "value",
					getNombreModelo() + ".motivoAnulacion", null, null, "save",
					null, null, null, null);
			lblObservacionGeneral.setValue(getModelo().getObservaciones());
			break;
		}
	}

	private List<CompEncabezado> encabezadosPrimario() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Serial del Activo.", 250);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getActivo");
		titulo.setMetodoModelo(".activo");

		encabezado.add(titulo);

		titulo = new CompEncabezado("Nomb. Activo", 220);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNombreActivo");
		encabezado.add(titulo);

		/*
		 * titulo = new CompEncabezado("Trasladado a.", 330);
		 * titulo.setMetodoComponente("seleccion");
		 * titulo.setMetodoBinder("getUnidadAdministrativa");
		 * titulo.setMetodoModelo(".unidadAdministrativa");
		 * encabezado.add(titulo);
		 */

		titulo = new CompEncabezado("Almacen", 190);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNombreAlmacen");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Observaciones. ", 250);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getObservaciones");
		titulo.setMetodoModelo(".observaciones");
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> encabezadosPrimarioDos() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Serial del Activo.", 200);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getSerialActivo");

		encabezado.add(titulo);

		titulo = new CompEncabezado("Nomb. Activo", 200);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNombreActivo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Trasladado a.", 290);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNombreUnidadAdministrativa");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Almacen", 190);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNombreAlmacen");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado del Activo. ", 250);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getDescripcionMotivo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Observaciones. ", 220);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getObservaciones");
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> getEncabezadoActivo() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Codigo. ", 120);
		titulo.setMetodoBinder("getCodigoActivo");
		lista.add(titulo);

		titulo = new CompEncabezado("Ejemplar. ", 120);
		titulo.setMetodoBinder("getIdEjemplarActivo");
		lista.add(titulo);

		titulo = new CompEncabezado("Serial. ", 150);
		titulo.setMetodoBinder("getSerial");
		lista.add(titulo);

		titulo = new CompEncabezado("Denominación. ", 300);
		titulo.setMetodoBinder("getNombre");
		lista.add(titulo);

		titulo = new CompEncabezado("Marca. ", 140);
		titulo.setMetodoBinder("getDescripcionMarca");
		lista.add(titulo);

		titulo = new CompEncabezado("Modelo. ", 140);
		titulo.setMetodoBinder("getDescripcionModelo");
		lista.add(titulo);

		return lista;
	}

	private List<CompEncabezado> getEncabezadoUnidad() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Codigo. ", 150);
		titulo.setMetodoBinder("getSede");
		lista.add(titulo);

		titulo = new CompEncabezado("Nombre. ", 250);
		titulo.setMetodoBinder("getNombre");
		lista.add(titulo);

		return lista;
	}

	private ArrayList<Component> cargarLista() {
		ArrayList<Component> lista = new ArrayList<Component>();
		CompBuscar<Activo> codigoActivo = new CompBuscar<Activo>(
				getEncabezadoActivo(), 2);
		Label nombreActivo = new Label();
		// CompBuscar<UnidadAdministrativa> cmbUnidadAdministrativa = new
		// CompBuscar<UnidadAdministrativa>(getEncabezadoUnidad(), 1);
		Label almacen = new Label();
		Textbox observaciones = new Textbox();

		codigoActivo.setWidth("230px");
		nombreActivo.setWidth("180px");
		// cmbUnidadAdministrativa.setWidth("300px");
		almacen.setWidth("180px");
		observaciones.setWidth("230px");
		observaciones.setRows(2);
		observaciones.setMaxlength(250);

		codigoActivo.setAncho(1000);
		codigoActivo.setModelo(activos);
		codigoActivo.setAttribute("nombre", "activo");
		codigoActivo.setListenerEncontrar(getControlador());

		/*
		 * cmbUnidadAdministrativa.setAncho(600);
		 * cmbUnidadAdministrativa.setModelo(unidadesATrasladar);
		 * cmbUnidadAdministrativa.setAttribute("nombre", "unidadTraslado");
		 * cmbUnidadAdministrativa.setListenerEncontrar(getControlador());
		 */

		lista.add(codigoActivo);
		lista.add(nombreActivo);
		// lista.add(cmbUnidadAdministrativa);
		lista.add(almacen);
		lista.add(observaciones);

		return lista;
	}

	private ArrayList<Component> cargarListaDos() {
		ArrayList<Component> lista = new ArrayList<Component>();
		Label codigoActivo = new Label();
		Label nombreActivo = new Label();
		Label unidad = new Label();
		Label almacen = new Label();
		Label estadoActivo = new Label();
		Label observaciones = new Label();

		codigoActivo.setWidth("200px");
		nombreActivo.setWidth("180px");
		unidad.setWidth("250px");
		almacen.setWidth("180px");
		estadoActivo.setWidth("220");
		observaciones.setWidth("230px");

		lista.add(codigoActivo);
		lista.add(nombreActivo);
		lista.add(unidad);
		lista.add(almacen);
		lista.add(estadoActivo);
		lista.add(observaciones);

		return lista;
	}

	public CompGrupoDatos getCuatroColumnas() {
		return cuatroColumnas;
	}

	public void setCuatroColumnas(CompGrupoDatos cuatroColumnas) {
		this.cuatroColumnas = cuatroColumnas;
	}

	public CompGrupoDatos getDosColumnas() {
		return dosColumnas;
	}

	public void setDosColumnas(CompGrupoDatos dosColumnas) {
		this.dosColumnas = dosColumnas;
	}

	public Label getLblNumero() {
		return lblNumero;
	}

	public void setLblNumero(Label lblNumero) {
		this.lblNumero = lblNumero;
	}

	public Label getLblFecha() {
		return lblFecha;
	}

	public void setLblFecha(Label lblFecha) {
		this.lblFecha = lblFecha;
	}

	public Label getLblUnidadAdministrativa() {
		return lblUnidadAdministrativa;
	}

	public void setLblUnidadAdministrativa(Label lblUnidadAdministrativa) {
		this.lblUnidadAdministrativa = lblUnidadAdministrativa;
	}

	public Label getLblUsuario() {
		return lblUsuario;
	}

	public void setLblUsuario(Label lblUsuario) {
		this.lblUsuario = lblUsuario;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/*
	 * public CompBuscar<ActaAutorizacion> getCmpActaMovimiento() { return
	 * cmpActaMovimiento; }
	 * 
	 * public void setCmpActaMovimiento(CompBuscar<ActaAutorizacion>
	 * cmpActaMovimiento) { this.cmpActaMovimiento = cmpActaMovimiento; }
	 */

	public Textbox getTxtMotivoAnulacion() {
		return txtMotivoAnulacion;
	}

	public void setTxtMotivoAnulacion(Textbox txtMotivoAnulacion) {
		this.txtMotivoAnulacion = txtMotivoAnulacion;
	}

	public CompBuscar<UnidadAdministrativa> getCmbUnidadAdministrativas() {
		return cmbUnidadAdministrativas;
	}

	public void setCmbUnidadAdministrativas(
			CompBuscar<UnidadAdministrativa> cmbUnidadAdministrativas) {
		this.cmbUnidadAdministrativas = cmbUnidadAdministrativas;
	}

	public List<Activo> getActivos() {
		return activos;
	}

	public void setActivos(List<Activo> activos) {
		this.activos = activos;
	}

	public List<Modelo> getModelos() {
		return modelos;
	}

	public void setModelos(List<Modelo> modelos) {
		this.modelos = modelos;
	}

	public CompBuscar<Modelo> getCmbModelos() {
		return cmbModelos;
	}

	public void setCmbModelos(CompBuscar<Modelo> cmbModelos) {
		this.cmbModelos = cmbModelos;
	}

	public CompBuscar<UnidadAdministrativa> getCmbUnidadesParaTrasladar() {
		return cmbUnidadesParaTrasladar;
	}

	public void setCmbUnidadesParaTrasladar(
			CompBuscar<UnidadAdministrativa> cmbUnidadesParaTrasladar) {
		this.cmbUnidadesParaTrasladar = cmbUnidadesParaTrasladar;
	}

	public Textbox getTxtObservacionGeneral() {
		return txtObservacionGeneral;
	}

	public void setTxtObservacionGeneral(Textbox txtObservacionGeneral) {
		this.txtObservacionGeneral = txtObservacionGeneral;
	}

	public Textbox getTxtResponsableTraslado() {
		return txtResponsableTraslado;
	}

	public void setTxtResponsableTraslado(Textbox txtResponsableTraslado) {
		this.txtResponsableTraslado = txtResponsableTraslado;
	}

	public Textbox getTxtCedulaResponsable() {
		return txtCedulaResponsable;
	}

	public void setTxtCedulaResponsable(Textbox txtCedulaResponsable) {
		this.txtCedulaResponsable = txtCedulaResponsable;
	}

	public Textbox getTxtVehiculo() {
		return txtVehiculo;
	}

	public void setTxtVehiculo(Textbox txtVehiculo) {
		this.txtVehiculo = txtVehiculo;
	}

	public Textbox getTxtPlacaVehiculo() {
		return txtPlacaVehiculo;
	}

	public void setTxtPlacaVehiculo(Textbox txtPlacaVehiculo) {
		this.txtPlacaVehiculo = txtPlacaVehiculo;
	}

	public int getAccion() {
		return accion;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR) {
			desactivarDetalle();
		}

	}

}
