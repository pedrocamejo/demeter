package cpc.demeter.vista.gestion;

/*import java.awt.Checkbox;*/
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zul.Datebox;
import org.zkoss.zul.Doublebox;
import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.basico.UnidadArrime;
import cpc.modelo.demeter.gestion.*;

import cpc.modelo.ministerio.dimension.UbicacionDireccion;
import cpc.modelo.ministerio.gestion.Productor;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.listas.CompTexto;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;
import cva.pc.componentes.CompEncabezado;

public class UiCambioOrdenTransporte extends
		CompVentanaBase<DetalleOrdenTransporteProduccion> {

	private static final long serialVersionUID = -5739695024627594930L;
	private CompGrupoDatos gbGeneral;
	private CompGrupoDatos gbRutaDescartada;
	private CompGrupoDatos gbNuevaRuta;

	private CompTexto<OrdenTransporteProduccion> ordenTrabajo;
	private CompTexto<Productor> productor;
	// private CompTexto<Trabajador> trabajador;
	private Datebox fecha;

	private CompTexto<UbicacionDireccion> origenCancelado;
	private CompTexto<UnidadArrime> unidadCancelada;
	private CompTexto<UbicacionDireccion> destinoCancelado;
	private CompCombobox<FallaRecepcionSilo> causa;
	private Doublebox distancia;
	private Doublebox dias;

	private CompTexto<UbicacionDireccion> origen;
	private CompBuscar<UnidadArrime> destino;

	private List<FallaRecepcionSilo> causas;
	private List<UnidadArrime> destinos;
	private boolean consulta;

	public UiCambioOrdenTransporte(String titulo, int ancho,
			List<UnidadArrime> destinos, List<FallaRecepcionSilo> causas,
			boolean consulta) throws ExcDatosInvalidos {
		super(titulo, ancho);
		this.causas = causas;
		this.destinos = destinos;
		this.consulta = consulta;
	}

	public void inicializar() {
		gbGeneral = new CompGrupoDatos("Datos generales", 2);
		gbRutaDescartada = new CompGrupoDatos("Ruta descartada", 2);
		gbNuevaRuta = new CompGrupoDatos("Nueva Ruta", 2);
		fecha = new Datebox();
		origenCancelado = new CompTexto<UbicacionDireccion>();
		destinoCancelado = new CompTexto<UbicacionDireccion>();
		unidadCancelada = new CompTexto<UnidadArrime>();
		causa = new CompCombobox<FallaRecepcionSilo>();
		distancia = new Doublebox();
		dias = new Doublebox();
		origen = new CompTexto<UbicacionDireccion>();
		destino = new CompBuscar<UnidadArrime>(cargarEncabezadoSilo(), 2);
		ordenTrabajo = new CompTexto<OrdenTransporteProduccion>();
		// trabajador = new CompTexto<Trabajador>();
		productor = new CompTexto<Productor>();

		causa.setModelo(causas);
		destino.setModelo(destinos);
		destino.setAncho(700);
	}

	public void dibujar() {
		gbGeneral.setAnchoColumna(0, 100);
		gbGeneral.addComponente("Orden Transporte:", ordenTrabajo);
		gbGeneral.addComponente("Fecha :", fecha);
		gbGeneral.addComponente("Productor :", productor);
		// gbGeneral.addComponente("Conductor :", trabajador);

		gbRutaDescartada.addComponente("Origen :", origenCancelado);
		gbRutaDescartada.addComponente("Destino (Unidad Arime) :",
				unidadCancelada);
		gbRutaDescartada.addComponente("Destino (Direccion) :",
				destinoCancelado);
		gbRutaDescartada.addComponente("Causa :", causa);
		gbRutaDescartada.addComponente("Distancia:", distancia);
		gbRutaDescartada.addComponente("Dias Espera:", dias);

		gbNuevaRuta.addComponente("Origen:", origen);
		gbNuevaRuta.addComponente("Destino:", destino);
		gbGeneral.dibujar(this);
		gbRutaDescartada.dibujar(this);
		gbNuevaRuta.dibujar(this);
		addBotonera();
	}

	public void cargarValores(DetalleOrdenTransporteProduccion solicitudMeca)
			throws ExcDatosInvalidos {
		if (getModelo().getFalla() != null)
			causa.setSeleccion(getModelo().getFalla());
		getBinder().addBinding(causa, "seleccion",
				getNombreModelo() + ".falla", null, null, "save", null, null,
				null, null);
		// destino.setSeleccion(getModelo().getDestino());
		// getBinder().addBinding(destino, "seleccion",
		// getNombreModelo()+".destino", null, null, "save", null, null, null,
		// null);
		if (getModelo().getTiempoEspera() != null)
			dias.setValue(getModelo().getTiempoEspera());
		getBinder().addBinding(dias, "value",
				getNombreModelo() + ".tiempoEspera", null, null, "save", null,
				null, null, null);
		if (getModelo().getDistancia() != null)
			distancia.setValue(getModelo().getDistancia());
		getBinder().addBinding(distancia, "value",
				getNombreModelo() + ".distancia", null, null, "save", null,
				null, null, null);

		if (!consulta) {
			ordenTrabajo.setModelo(solicitudMeca.getOrden());
			productor.setModelo(solicitudMeca.getOrden().getProductor());
			origenCancelado.setModelo(solicitudMeca.getOrigen());
			destinoCancelado.setModelo(solicitudMeca.getDestino()
					.getDireccion());
			unidadCancelada.setModelo(solicitudMeca.getDestino());
			origen.setModelo(solicitudMeca.getDestino().getDireccion());
		} else {
			ordenTrabajo.setModelo(solicitudMeca.getOrden());
			productor.setModelo(solicitudMeca.getOrden().getProductor());
			fecha.setDisabled(true);
			destino.setDisabled(true);
			causa.setDisabled(true);
			dias.setDisabled(true);
			distancia.setDisabled(true);
			fecha.setValue(solicitudMeca.getFechaSalida());
			if (getModelo().getEfectivo()) {
				origen.setModelo(solicitudMeca.getOrigen());
				destino.setSeleccion(solicitudMeca.getDestino());
				gbRutaDescartada.setVisible(false);
			} else {
				origenCancelado.setModelo(solicitudMeca.getOrigen());
				destinoCancelado.setModelo(solicitudMeca.getDestino()
						.getDireccion());
				unidadCancelada.setModelo(solicitudMeca.getDestino());
				origen.setModelo(solicitudMeca.getDestino().getDireccion());
				gbNuevaRuta.setVisible(false);
			}
		}
	}

	public List<CompEncabezado> cargarEncabezadoSilo() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Clase", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Tipo de Unidad", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipoSilo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Nombre", 150);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getDescripcion");
		encabezado.add(titulo);

		titulo = new CompEncabezado("ubicacion", 350);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrUbicacionFisica");
		encabezado.add(titulo);

		return encabezado;
	}

	public void desactivar(int modoOperacion) {
		if (modoOperacion == Accion.CONSULTAR || modoOperacion == Accion.ANULAR)
			activarConsulta();
		else if (modoOperacion == Accion.PROCESAR)
			modoNuevo();
	}

	public void activarConsulta() {
		destino.setDisabled(true);
		causa.setDisabled(true);
		distancia.setDisabled(true);
		dias.setDisabled(true);
	}

	public void modoNuevo() {
		destino.setDisabled(false);
		causa.setDisabled(false);
		distancia.setDisabled(false);
		dias.setDisabled(false);
	}

	public void refrescarDatosLabor(LaborOrdenServicio labor, boolean nueva) {
		/*
		 * desactivarLabor(!nueva); unidadFisica.setModelo(labor.getUnidad());
		 * unidadFisica.setValue(labor.getUnidad().getAbreviatura()); //if
		 * (!nueva){ fisico.setValue(labor.getFisico());
		 * pases.setValue(labor.getPases());
		 * cantidad.setValue(labor.getCantidad());
		 * total.setValue(labor.getCalculo());
		 * visibilidadLabor(labor.isPasesVisible(), labor.isCantidadVisible());
		 * //}
		 */
	}

	public CompTexto<Productor> getProductor() {
		return productor;
	}

	public Datebox getFecha() {
		return fecha;
	}

	public CompTexto<UbicacionDireccion> getOrigenCancelado() {
		return origenCancelado;
	}

	public CompTexto<UnidadArrime> getUnidadCancelada() {
		return unidadCancelada;
	}

	public CompTexto<UbicacionDireccion> getDestinoCancelado() {
		return destinoCancelado;
	}

	public CompCombobox<FallaRecepcionSilo> getCausa() {
		return causa;
	}

	public Doublebox getDistancia() {
		return distancia;
	}

	public Doublebox getDias() {
		return dias;
	}

	public CompTexto<UbicacionDireccion> getOrigen() {
		return origen;
	}

	public CompBuscar<UnidadArrime> getDestino() {
		return destino;
	}

}
