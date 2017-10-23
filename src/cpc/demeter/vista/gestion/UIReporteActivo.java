package cpc.demeter.vista.gestion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import cpc.modelo.sigesp.basico.Activo;
import cpc.negocio.demeter.gestion.NegocioReporteActivo;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentana;
import cva.pc.componentes.CompEncabezado;

public class UIReporteActivo extends CompVentana {

	private static final long serialVersionUID = 1443529190126217020L;
	private Listbox cmbTipoReporte;
	private CompGrupoDatos grupo, grupoFiltro, grupoFiltroActivos;
	private Datebox dtmFechaInicial, dtmFechaFinal;
	private CompBuscar<Activo> cmbActivosI, cmbActivosH;
	private Label textoFormatoRpt;
	private CompCombobox<String> cmbFormatoRpt;
	public static final String PDF = "pdf";
	public static final String XLS = "xls";

	public UIReporteActivo(EventListener controlador, String titulo, int ancho) {
		super(controlador, titulo, ancho);
	}

	@Override
	public void inicializar() {
		grupo = new CompGrupoDatos("Datos Para Seleccionar reporte", 2);
		grupoFiltro = new CompGrupoDatos("Filtros para fechas", 2);
		grupoFiltroActivos = new CompGrupoDatos("Filtro para Activos", 2);

		cmbTipoReporte = new Listbox();
		cmbTipoReporte.setMold("select");

		cmbActivosI = new CompBuscar<Activo>(getEncabezadoActivo(), 2);
		cmbActivosH = new CompBuscar<Activo>(getEncabezadoActivo(), 2);

		dtmFechaFinal = new Datebox(new Date());
		dtmFechaInicial = new Datebox(new Date());

		cmbFormatoRpt = new CompCombobox<String>(); // Formato de Reporte
		textoFormatoRpt = new Label("Formato");
		cargarFormatos();

	}

	@Override
	public void dibujar() {
		rellenarTiposReportes();
		cmbTipoReporte.setWidth("300px");
		cmbTipoReporte.addEventListener(Events.ON_SELECT, getControlador());
		cmbTipoReporte.setAttribute("nombre", "reporte");
		dtmFechaFinal.setWidth("200px");
		dtmFechaInicial.setWidth("200px");

		cmbActivosI.setWidth("200px");
		cmbActivosI.setModelo(NegocioReporteActivo.getInstance()
				.obtenerActivos());
		cmbActivosI.setAncho(600);

		cmbActivosH.setWidth("200px");
		cmbActivosH.setModelo(NegocioReporteActivo.getInstance()
				.obtenerActivos());
		cmbActivosH.setAncho(600);

		grupo.addComponente("Reportes", cmbTipoReporte);
		grupo.dibujar(this);
		grupo.addComponente(textoFormatoRpt);
		grupo.addComponente(cmbFormatoRpt);

		grupoFiltro.addComponente("Desde: ", dtmFechaInicial);
		grupoFiltro.addComponente("Hasta: ", dtmFechaFinal);
		grupoFiltroActivos.addComponente("Desde: ", cmbActivosI);
		grupoFiltroActivos.addComponente("Hasta: ", cmbActivosH);
		grupoFiltro.dibujar(this);
		grupoFiltro.setVisible(false);
		grupoFiltroActivos.dibujar(this);
		grupoFiltroActivos.setVisible(false);
		// agregarGrupoDatos(grupo);

		addBotonera();
	}

	private void rellenarTiposReportes() {
		Listitem item = new Listitem();
		item.setLabel("Inventario de Activos");
		item.setValue("InventarioActivos.jasper");
		item.setSelected(true);
		cmbTipoReporte.appendChild(item);
		item = new Listitem();
		item.setLabel("Movimiento de Activos");
		item.setValue("MovimientosActivos.jasper");
		cmbTipoReporte.appendChild(item);
		item = new Listitem();
		item.setLabel("Movimientos por Activos");
		item.setValue("MovimientosPorActivos.jasper");
		cmbTipoReporte.appendChild(item);
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

	public void grupoFiltroVisible(int valor) {
		switch (valor) {
		case 0:
			grupoFiltro.setVisible(false);
			grupoFiltroActivos.setVisible(false);
			break;
		case 1:
			grupoFiltroActivos.setVisible(false);
			grupoFiltro.setVisible(true);
			break;
		case 2:
			grupoFiltro.setVisible(false);
			grupoFiltroActivos.setVisible(true);
			break;
		}
	}

	public Listbox getCmbTipoReporte() {
		return cmbTipoReporte;
	}

	public void setCmbTipoReporte(Listbox cmbTipoReporte) {
		this.cmbTipoReporte = cmbTipoReporte;
	}

	public CompGrupoDatos getGrupo() {
		return grupo;
	}

	public void setGrupo(CompGrupoDatos grupo) {
		this.grupo = grupo;
	}

	public CompGrupoDatos getGrupoFiltro() {
		return grupoFiltro;
	}

	public void setGrupoFiltro(CompGrupoDatos grupoFiltro) {
		this.grupoFiltro = grupoFiltro;
	}

	public Datebox getDtmFechaInicial() {
		return dtmFechaInicial;
	}

	public void setDtmFechaInicial(Datebox dtmFechaInicial) {
		this.dtmFechaInicial = dtmFechaInicial;
	}

	public Datebox getDtmFechaFinal() {
		return dtmFechaFinal;
	}

	public void setDtmFechaFinal(Datebox dtmFechaFinal) {
		this.dtmFechaFinal = dtmFechaFinal;
	}

	public CompBuscar<Activo> getCmbActivosI() {
		return cmbActivosI;
	}

	public void setCmbActivosI(CompBuscar<Activo> cmbActivosI) {
		this.cmbActivosI = cmbActivosI;
	}

	public CompBuscar<Activo> getCmbActivosH() {
		return cmbActivosH;
	}

	public void setCmbActivosH(CompBuscar<Activo> cmbActivosH) {
		this.cmbActivosH = cmbActivosH;
	}

	public CompGrupoDatos getGrupoFiltroActivos() {
		return grupoFiltroActivos;
	}

	public void setGrupoFiltroActivos(CompGrupoDatos grupoFiltroActivos) {
		this.grupoFiltroActivos = grupoFiltroActivos;
	}

	public CompCombobox<String> getCmbFormatoRpt() {
		return cmbFormatoRpt;
	}

	public void setCmbFormatoRpt(CompCombobox<String> cmbFormatoRpt) {
		this.cmbFormatoRpt = cmbFormatoRpt;
	}

	private void cargarFormatos() {
		cmbFormatoRpt.appendItem("PDF").setValue(PDF);
		cmbFormatoRpt.appendItem("EXCEL").setValue(XLS);
	}

}
