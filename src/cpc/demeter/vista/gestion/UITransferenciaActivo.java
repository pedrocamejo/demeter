package cpc.demeter.vista.gestion;

import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.gestion.MotivoTransferenciaActivo;
import cpc.modelo.demeter.gestion.Movimiento;
import cpc.modelo.demeter.gestion.TransferenciaActivo;
import cpc.modelo.sigesp.basico.Activo;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.modelo.sigesp.basico.UnidadAdministrativa;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaMaestroDetalle;
import cva.pc.componentes.CompEncabezado;

public class UITransferenciaActivo extends
		CompVentanaMaestroDetalle<Movimiento, TransferenciaActivo> {

	private static final long serialVersionUID = 150239458707852037L;
	private List<UnidadAdministrativa> unidadesAdministrativas;
	private List<Activo> activos;
	private List<Almacen> almacenes;
	private List<MotivoTransferenciaActivo> motivos;
	private int accion;
	private Boolean unidadesGlobales;

	private CompGrupoDatos cuatroColumnas, dosColumnas;
	private Label lblNumero, lblFecha, lblUnidadAdministrativa, lblUsuario;
	private CompBuscar<UnidadAdministrativa> cmbUnidadAdministrativas;

	public UITransferenciaActivo(String titulo, int ancho,
			List<UnidadAdministrativa> unidadAdministrativa,
			List<Activo> activos, List<Almacen> almacenes,
			List<MotivoTransferenciaActivo> motivos, int modo, Boolean global) {
		super(titulo, ancho);
		this.unidadesAdministrativas = unidadAdministrativa;
		this.activos = activos;
		this.almacenes = almacenes;
		this.motivos = motivos;
		this.accion = modo;
		this.unidadesGlobales = global;
	}

	public UITransferenciaActivo(String titulo, int ancho, int modo) {
		super(titulo, ancho);
		this.accion = modo;
	}

	@Override
	public void inicializar() {
		setGbEncabezado(1);
		cuatroColumnas = new CompGrupoDatos(4);
		dosColumnas = new CompGrupoDatos(2);

		lblNumero = new Label();
		lblFecha = new Label();
		if (accion == Accion.AGREGAR)
			if (unidadesGlobales)
				cmbUnidadAdministrativas = new CompBuscar<UnidadAdministrativa>(
						getEncabezadoUnidad(), 1);
			else
				lblUnidadAdministrativa = new Label();
		else if (accion == Accion.CONSULTAR)
			lblUnidadAdministrativa = new Label();
		lblUsuario = new Label();

	}

	@Override
	public void dibujar() {
		lblNumero.setWidth("300px");
		lblFecha.setWidth("500px");

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
		} else if (accion == Accion.CONSULTAR) {
			lblUnidadAdministrativa.setWidth("400px");
			dosColumnas.addComponente("Unidad Administrativa: ",
					lblUnidadAdministrativa);
		}
		dosColumnas.addComponente("Registrado por: ", lblUsuario);
		dosColumnas.dibujar(getGbEncabezado());

		if (accion == Accion.CONSULTAR)
			setDetalles(cargarListaDos(), getModelo().getTransferencias(),
					encabezadosPrimarioDos());
		else
			setDetalles(cargarLista(), getModelo().getTransferencias(),
					encabezadosPrimario());

		getDetalle().setNuevo(new TransferenciaActivo());
		getDetalle().setListenerBorrar(getControlador());
		dibujarEstructura();

		setAltoDetalle(200);

		addBotonera();

	}

	@Override
	public void cargarValores(Movimiento arg0) throws ExcDatosInvalidos {
		if (accion == Accion.AGREGAR) {
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
		} else if (accion == Accion.CONSULTAR) {
			lblUnidadAdministrativa.setValue(getModelo()
					.getUnidadAdministrativa().getNombre());
		}
		lblUsuario.setValue(getModelo().getUsuario());
		try {
			lblNumero.setValue(getModelo().getControl());
			lblFecha.setValue(getModelo().getfechastring());
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

	}

	private List<CompEncabezado> getEncabezadoUnidad() {
		List<CompEncabezado> lista = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;

		titulo = new CompEncabezado("Codigo. ", 150);
		titulo.setMetodoBinder("getSede");
		lista.add(titulo);

		titulo = new CompEncabezado("Nombre. ", 300);
		titulo.setMetodoBinder("getNombre");
		lista.add(titulo);

		return lista;
	}

	private List<CompEncabezado> encabezadosPrimario() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Activo ", 210);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getActivo");
		titulo.setMetodoModelo(".activo");

		encabezado.add(titulo);

		titulo = new CompEncabezado("Nomb. Activo", 180);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNombreActivo");
		// titulo.setMetodoModelo(".cantidad");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Almacen Ant.", 160);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNombreAlmacenAnterior");
		// titulo.setMetodoModelo(".complementoDescripcion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Almacen Act.", 210);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getAlmacenActual");
		titulo.setMetodoModelo(".almacenActual");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Observaciones. ", 240);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getObservaciones");
		titulo.setMetodoModelo(".observaciones");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Motivo. ", 230);
		titulo.setMetodoComponente("seleccion");
		titulo.setMetodoBinder("getMotivo");
		titulo.setMetodoModelo(".motivo");
		encabezado.add(titulo);

		return encabezado;
	}

	private List<CompEncabezado> encabezadosPrimarioDos() {

		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();
		CompEncabezado titulo;
		titulo = new CompEncabezado("Activo ", 200);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getSerialActivo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nomb. Activo", 200);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNombreActivo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Almacen Ant.", 200);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNombreAlmacenAnterior");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Almacen Act.", 200);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getNombreAlmacenActual");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Observaciones. ", 240);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getObservaciones");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Motivo. ", 230);
		titulo.setMetodoComponente("value");
		titulo.setMetodoBinder("getDescripcionMotivo");
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

	private List<CompEncabezado> getEncabezadoMotivo() {
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
		Label almacenAnter = new Label();
		CompBuscar<Almacen> almacenActua = new CompBuscar<Almacen>(
				getEncabezadoAlmacen(), 0);
		Textbox observaciones = new Textbox();
		CompBuscar<MotivoTransferenciaActivo> motivo = new CompBuscar<MotivoTransferenciaActivo>(
				getEncabezadoMotivo(), 0);

		codigoActivo.setWidth("180px");
		nombreActivo.setWidth("300px");
		almacenAnter.setWidth("180px");
		almacenActua.setWidth("180px");
		observaciones.setWidth("230px");
		motivo.setWidth("200px");
		observaciones.setRows(2);
		observaciones.setMaxlength(250);

		codigoActivo.setAncho(1000);
		almacenActua.setAncho(600);
		motivo.setAncho(400);

		codigoActivo.setModelo(activos);
		almacenActua.setModelo(almacenes);
		motivo.setModelo(motivos);
		codigoActivo.setAttribute("nombre", "activo");
		almacenActua.setAttribute("nombre", "almacen");

		codigoActivo.setListenerEncontrar(getControlador());
		almacenActua.setListenerEncontrar(getControlador());

		lista.add(codigoActivo);
		lista.add(nombreActivo);
		lista.add(almacenAnter);
		lista.add(almacenActua);
		lista.add(observaciones);
		lista.add(motivo);

		return lista;
	}

	private ArrayList<Component> cargarListaDos() {
		ArrayList<Component> lista = new ArrayList<Component>();
		Label codigoActivo = new Label();
		Label nombreActivo = new Label();
		Label almacenAnter = new Label();
		Label almacenActua = new Label();
		Label observaciones = new Label();
		Label motivo = new Label();

		codigoActivo.setWidth("180px");
		nombreActivo.setWidth("300px");
		almacenAnter.setWidth("180px");
		almacenActua.setWidth("180px");
		observaciones.setWidth("250px");
		motivo.setWidth("200px");

		lista.add(codigoActivo);
		lista.add(nombreActivo);
		lista.add(almacenAnter);
		lista.add(almacenActua);
		lista.add(observaciones);
		lista.add(motivo);

		return lista;
	}

	public List<UnidadAdministrativa> getUnidadAdministrativa() {
		return unidadesAdministrativas;
	}

	public void setUnidadAdministrativa(
			List<UnidadAdministrativa> unidadAdministrativa) {
		this.unidadesAdministrativas = unidadAdministrativa;
	}

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

	public CompBuscar<UnidadAdministrativa> getCmbUnidadAdministrativas() {
		return cmbUnidadAdministrativas;
	}

	public void setCmbUnidadAdministrativas(
			CompBuscar<UnidadAdministrativa> cmbUnidadAdministrativas) {
		this.cmbUnidadAdministrativas = cmbUnidadAdministrativas;
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR)
			desactivarDetalle();
	}

}
