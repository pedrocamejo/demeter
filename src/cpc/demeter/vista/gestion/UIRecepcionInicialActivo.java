package cpc.demeter.vista.gestion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.gestion.EntradaActivo;
import cpc.modelo.demeter.gestion.EstadoMovimientoActivo;
import cpc.modelo.demeter.gestion.MotivoTransferenciaActivo;
import cpc.modelo.demeter.gestion.Movimiento;
import cpc.modelo.sigesp.basico.Activo;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;

public class UIRecepcionInicialActivo extends
		CompVentanaMaestroDetalle<Movimiento, EntradaActivo> {

	private static final long serialVersionUID = 150239458707852037L;
	// private UnidadAdministrativa unidadAdministrativa;
	private List<UnidadAdministrativa> unidadesAdministrativas;
	private List<Activo> activos;
	private List<Almacen> almacenes;
	private List<MotivoTransferenciaActivo> estadosActivo;
	private List<Modelo> modelos;
	private String usuario;
	private int accion;
	private EstadoMovimientoActivo anulado;
	private Boolean unidadesGlobales;

	private CompGrupoDatos cuatroColumnas, dosColumnas, filtroActivos;
	private Label lblNumero, lblFecha, lblUnidadAdministrativa, lblUsuario,
			lblEstado, lblAnuladoPor, lblFechaAnulacion, lblMotivoAnulacion;
	private Textbox txtMotivoAnulacion;

	private CompBuscar<UnidadAdministrativa> cmbUnidadAdministrativas;
	private CompBuscar<Modelo> cmbModelos;

	public UIRecepcionInicialActivo(String titulo, int ancho,
			List<Activo> activos, String usuario, List<Almacen> almacenes,
			int modo, EstadoMovimientoActivo estadoAnulado,
			List<MotivoTransferenciaActivo> estados,
			List<UnidadAdministrativa> unidades, Boolean unidadesGlobales,
			List<Modelo> modelos) {
		super(titulo, ancho);
		// this.unidadAdministrativa = unidadAdministrativa;
		this.activos = activos;
		this.usuario = usuario;
		this.almacenes = almacenes;
		this.accion = modo;
		this.anulado = estadoAnulado;
		this.estadosActivo = estados;
		this.unidadesAdministrativas = unidades;
		this.unidadesGlobales = unidadesGlobales;
		this.modelos = modelos;
	}

	public UIRecepcionInicialActivo(String titulo, int ancho, int modo,
			EstadoMovimientoActivo estadoAnulado) {
		super(titulo, ancho);
		this.accion = modo;
		this.anulado = estadoAnulado;
	}

	@Override
	public void inicializar() {
		setGbEncabezado(1);
		cuatroColumnas = new CompGrupoDatos(4);
		dosColumnas = new CompGrupoDatos(2);

		lblNumero = new Label();
		lblFecha = new Label();
		lblEstado = new Label();
		lblUsuario = new Label();

		if (accion == Accion.AGREGAR) {
			if (unidadesGlobales)
				cmbUnidadAdministrativas = new CompBuscar<UnidadAdministrativa>(
						getEncabezadoUnidad(), 1);
			else
				lblUnidadAdministrativa = new Label();
			filtroActivos = new CompGrupoDatos(2);
			// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
			// "Advertencia 1",JOptionPane.WARNING_MESSAGE);
			cmbModelos = new CompBuscar<Modelo>(getEncabezadoModelo(), 1);
		}

		if (accion == Accion.CONSULTAR) {
			lblUnidadAdministrativa = new Label();
			if (getModelo().getEstado().getIdEstado()
					.equals(anulado.getIdEstado())) {
				lblAnuladoPor = new Label();
				lblFechaAnulacion = new Label();
				lblMotivoAnulacion = new Label();
			}
		}

		if (accion == Accion.ANULAR) {
			lblUnidadAdministrativa = new Label();
			lblAnuladoPor = new Label();
			lblFechaAnulacion = new Label();
			txtMotivoAnulacion = new Textbox();
		}

	}

	@Override
	public void dibujar() {
		lblNumero.setWidth("300px");
		lblFecha.setWidth("500px");
		lblEstado.setWidth("300px");
		lblUsuario.setWidth("200px");

		cuatroColumnas.addComponente("Nro Transferencia: ", lblNumero);
		cuatroColumnas.addComponente("Fecha: ", lblFecha);
		cuatroColumnas.dibujar(getGbEncabezado());

		if (accion == Accion.AGREGAR) {
			if (unidadesGlobales) {
				cmbUnidadAdministrativas.setAncho(600);
				cmbUnidadAdministrativas.setWidth("500px");
				cmbUnidadAdministrativas.setModelo(unidadesAdministrativas);
				cmbUnidadAdministrativas.setAttribute("nombre", "unidades");
				cmbUnidadAdministrativas.setListenerEncontrar(getControlador());
				dosColumnas.addComponente("Unidad Administrativa: ",
						cmbUnidadAdministrativas);

			} else {
				lblUnidadAdministrativa.setWidth("400px");
				dosColumnas.addComponente("Unidad Administrativa: ",
						lblUnidadAdministrativa);
			}
			cmbModelos.setAncho(550);
			cmbModelos.setWidth("300px");
			cmbModelos.setModelo(modelos);
			cmbModelos.setAttribute("nombre", "modelos");
			cmbModelos.setListenerEncontrar(getControlador());

			dosColumnas.addComponente("Estado: ", lblEstado);
			dosColumnas.addComponente("Registrado por: ", lblUsuario);

			filtroActivos.setTitulo("Filtro para la lista de Activos");
			filtroActivos.addComponente("Marca y Modelo: ", cmbModelos);

			dosColumnas.dibujar(getGbEncabezado());
			filtroActivos.dibujar(getGbEncabezado());
		}

		if (accion == Accion.CONSULTAR) {
			lblUnidadAdministrativa.setWidth("400px");
			dosColumnas.addComponente("Unidad Administrativa: ",
					lblUnidadAdministrativa);
			dosColumnas.addComponente("Estado: ", lblEstado);
			dosColumnas.addComponente("Registrado por: ", lblUsuario);
			if (getModelo().getEstado().getIdEstado()
					.equals(anulado.getIdEstado())) {
				dosColumnas.addComponente("Fecha Anulación: ",
						lblFechaAnulacion);
				dosColumnas.addComponente("Anulado Por: ", lblAnuladoPor);
				dosColumnas.addComponente("Motivo Anulación: ",
						lblMotivoAnulacion);
			}
			dosColumnas.dibujar(getGbEncabezado());
		}

		if (accion == Accion.ANULAR) {
			lblUnidadAdministrativa.setWidth("400px");
			dosColumnas.addComponente("Unidad Administrativa: ",
					lblUnidadAdministrativa);
			dosColumnas.addComponente("Estado: ", lblEstado);
			dosColumnas.addComponente("Registrado por: ", lblUsuario);
			dosColumnas.addComponente("Fecha Anulación: ", lblFechaAnulacion);
			dosColumnas.addComponente("Anulado Por: ", lblAnuladoPor);
			txtMotivoAnulacion.setWidth("400px");
			txtMotivoAnulacion.setRows(2);
			txtMotivoAnulacion.setMaxlength(200);
			dosColumnas.addComponente("Motivo Anulación: ", txtMotivoAnulacion);
			dosColumnas.dibujar(getGbEncabezado());
		}

		if (accion == Accion.CONSULTAR || accion == Accion.ANULAR) {
			actualizarModelo();
			setDetalles(cargarListaDos(), getModelo().getEntradas(),
					encabezadosPrimarioDos());
		} else {
			actualizarModelo();
			setDetalles(cargarLista(), getModelo().getEntradas(),
					encabezadosPrimario());
		}
		getDetalle().setNuevo(new EntradaActivo());
		getDetalle().setListenerBorrar(getControlador());
		dibujarEstructura();
		// setAltoDetalle(200);
		addBotonera();

	}

	@Override
	public void cargarValores(Movimiento arg0) throws ExcDatosInvalidos {
		lblUsuario.setValue(getModelo().getUsuario());
		try {
			lblNumero.setValue(getModelo().getControl());
			lblFecha.setValue(getModelo().getfechastring());
			lblEstado.setValue(getModelo().getNombreEstado());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		if (accion == Accion.AGREGAR) {
			if (unidadesGlobales) {
				// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
				// "Advertencia 7",JOptionPane.WARNING_MESSAGE);
				cmbUnidadAdministrativas.setSeleccion(getModelo()
						.getUnidadAdministrativa());
				getBinder().addBinding(cmbUnidadAdministrativas, "seleccion",
						getNombreModelo() + ".unidadAdministrativa", null,
						null, "save", null, null, null, null);
			} else {
				// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
				// "Advertencia 8",JOptionPane.WARNING_MESSAGE);
				lblUnidadAdministrativa.setValue(getModelo()
						.getUnidadAdministrativa().getNombre());
			}
		}

		if (accion == Accion.CONSULTAR) {
			lblUnidadAdministrativa.setValue(getModelo()
					.getUnidadAdministrativa().getNombre());
			if (getModelo().getEstado().getIdEstado()
					.equals(anulado.getIdEstado())) {
				// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
				// "Advertencia 9",JOptionPane.WARNING_MESSAGE);
				lblFechaAnulacion.setValue(getModelo()
						.getFechaAnulacionString());
				lblAnuladoPor.setValue(getModelo().getAnuladoPor());
				lblMotivoAnulacion.setValue(getModelo().getMotivoAnulacion());
			}
		}

		if (accion == Accion.ANULAR) {
			// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
			// "Advertencia 10",JOptionPane.WARNING_MESSAGE);
			lblUnidadAdministrativa.setValue(getModelo()
					.getUnidadAdministrativa().getNombre());
			lblFechaAnulacion.setValue(getModelo().getFechaAnulacionString());
			lblAnuladoPor.setValue(getModelo().getAnuladoPor());
			txtMotivoAnulacion.setValue(getModelo().getMotivoAnulacion());
			// getBinder().addBinding(txtMotivoAnulacion, "value",
			// getNombreModelo()+".motivoAnulacion", null, null, "save", null,
			// null, null, null);
			// getBinder().addBinding(lblAnuladoPor, "value",
			// getNombreModelo()+".anuladoPor", null, null, "save", null, null,
			// null, null);
		}
	}

	private List<CompEncabezado> getEncabezadoModelo() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Marca. ", 250);
		titulo.setMetodoBinder("getDescripcionMarca");
		titulo.setOrdenable(true);
		lista.add(titulo);

		titulo = new CompEncabezado("Modelo. ", 250);
		titulo.setMetodoBinder("getDescripcionModelo");
		titulo.setOrdenable(true);
		lista.add(titulo);

		return lista;
	}

	private List<CompEncabezado> getEncabezadoUnidad() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Codigo. ", 150);
		titulo.setMetodoBinder("getSede");
		lista.add(titulo);

		titulo = new CompEncabezado("Nombre. ", 300);
		titulo.setMetodoBinder("getNombre");
		titulo.setOrdenable(true);
		lista.add(titulo);

		return lista;
	}

	private List<CompEncabezado> encabezadosPrimario() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Serial Activo ", 210);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getActivo");
		titulo.setMetodoModelo(".activo");

		encabezado.add(titulo);

		titulo = new CompEncabezado("Nomb. Activo", 210);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNombreActivo");
		titulo.setOrdenable(true);
		encabezado.add(titulo);

		titulo = new CompEncabezado("Almacen.", 210);
		titulo.setOrdenable(true);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getAlmacen");
		titulo.setMetodoModelo(".almacen");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado del Activo. ", 250);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getMotivo");
		titulo.setMetodoModelo(".motivo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Observaciones. ", 260);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getObservaciones");
		titulo.setMetodoModelo(".observaciones");
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> encabezadosPrimarioDos() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Serial Activo ", 180);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getSerialActivo");

		encabezado.add(titulo);

		titulo = new CompEncabezado("Nomb. Activo", 200);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNombreActivo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Almacen.", 200);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNombreAlmacen");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado del Activo. ", 200);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getDescripcionMotivo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Observaciones. ", 250);
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

	private List<CompEncabezado> getEncabezadoAlmacen() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Nombre. ", 100);
		titulo.setMetodoBinder("getNombre");
		lista.add(titulo);

		titulo = new CompEncabezado("Descripción. ", 100);
		titulo.setMetodoBinder("getDescripcion");
		lista.add(titulo);

		titulo = new CompEncabezado("Localidad. ", 250);
		titulo.setMetodoBinder("getLocalidad");
		lista.add(titulo);

		return lista;
	}

	private List<CompEncabezado> getEncabezadoEstadoActivo() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Descripción. ", 250);
		titulo.setMetodoBinder("getDescripcion");
		lista.add(titulo);

		return lista;
	}

	private ArrayList<Component> cargarLista() {
		ArrayList<Component> lista = new ArrayList<Component>();
		CompBuscar<Activo> codigoActivo = new CompBuscar<Activo>(
				getEncabezadoActivo(), 2);
		Label nombreActivo = new Label();
		CompBuscar<Almacen> almacen = new CompBuscar<Almacen>(
				getEncabezadoAlmacen(), 0);
		CompBuscar<MotivoTransferenciaActivo> estadoActivo = new CompBuscar<MotivoTransferenciaActivo>(
				getEncabezadoEstadoActivo(), 0);
		Textbox observaciones = new Textbox();

		codigoActivo.setWidth("180px");
		nombreActivo.setWidth("300px");
		almacen.setWidth("180px");
		estadoActivo.setWidth("220px");
		observaciones.setWidth("250px");
		observaciones.setRows(2);
		observaciones.setMaxlength(250);

		codigoActivo.setAncho(1000);
		almacen.setAncho(600);
		estadoActivo.setAncho(400);

		codigoActivo.setModelo(activos);
		almacen.setModelo(almacenes);
		codigoActivo.setAttribute("nombre", "activo");
		almacen.setAttribute("nombre", "almacen");
		estadoActivo.setModelo(estadosActivo);

		codigoActivo.setListenerEncontrar(getControlador());
		almacen.setListenerEncontrar(getControlador());

		lista.add(codigoActivo);
		lista.add(nombreActivo);
		lista.add(almacen);
		lista.add(estadoActivo);
		lista.add(observaciones);
		/*
		 * for (Component component : lista) { component.getChildren(); if
		 * (component.getChildren().get(0) ==null){ lista.remove(component); } }
		 */

		return lista;
	}

	private ArrayList<Component> cargarListaDos() {
		ArrayList<Component> lista = new ArrayList<Component>();
		Label codigoActivo = new Label();
		Label nombreActivo = new Label();
		Label almacen = new Label();
		Label estadoActivo = new Label();
		Label observaciones = new Label();

		codigoActivo.setWidth("180px");
		nombreActivo.setWidth("300px");
		almacen.setWidth("180px");
		estadoActivo.setWidth("220");
		observaciones.setWidth("250px");

		lista.add(codigoActivo);
		lista.add(nombreActivo);
		lista.add(almacen);
		lista.add(estadoActivo);
		lista.add(observaciones);
		/*
		 * for (Component component : lista) { component.getChildren(); if
		 * (component.getChildren().get(0) ==null){ lista.remove(component); } }
		 */
		return lista;
	}

	/*
	 * public UnidadAdministrativa getUnidadAdministrativa() { return
	 * unidadAdministrativa; }
	 * 
	 * public void setUnidadAdministrativa(UnidadAdministrativa
	 * unidadAdministrativa) { this.unidadAdministrativa = unidadAdministrativa;
	 * }
	 */

	public List<Activo> getActivos() {
		return activos;
	}

	public void setActivos(List<Activo> activos) {
		this.activos = activos;
	}

	public List<Almacen> getAlmacenes() {
		return almacenes;
	}

	public void setAlmacenes(List<Almacen> almacenes) {
		this.almacenes = almacenes;
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

	public CompBuscar<UnidadAdministrativa> getCmbUnidadAdministrativas() {
		return cmbUnidadAdministrativas;
	}

	public void setCmbUnidadAdministrativas(
			CompBuscar<UnidadAdministrativa> cmbUnidadAdministrativas) {
		this.cmbUnidadAdministrativas = cmbUnidadAdministrativas;
	}

	public CompBuscar<Modelo> getCmbModelos() {
		return cmbModelos;
	}

	public void setCmbModelos(CompBuscar<Modelo> cmbModelos) {
		this.cmbModelos = cmbModelos;
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR) {
			// JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
			// "Advertencia 11",JOptionPane.WARNING_MESSAGE);
			desactivarDetalle();
		}
	}

	public Textbox getTxtMotivoAnulacion() {
		return txtMotivoAnulacion;
	}

	public void setTxtMotivoAnulacion(Textbox txtMotivoAnulacion) {
		this.txtMotivoAnulacion = txtMotivoAnulacion;
	}

}
