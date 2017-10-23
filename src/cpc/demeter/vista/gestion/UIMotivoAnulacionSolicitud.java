package cpc.demeter.vista.gestion;

import java.util.List;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.ares.modelo.Accion;
import cpc.modelo.demeter.basico.TipoSolicitud;
import cpc.modelo.demeter.gestion.MotivoAnulacionSolicitud;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.CompCombobox;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UIMotivoAnulacionSolicitud extends
		CompVentanaBase<MotivoAnulacionSolicitud> {

	private static final long serialVersionUID = 5107922780341632881L;
	private int accion;
	private Textbox txtdescripcion;
	private CompCombobox<TipoSolicitud> tipo;
	private List<TipoSolicitud> tipos;
	private Label lbldescripcion, estado;
	private CompGrupoDatos gbGeneral;

	public UIMotivoAnulacionSolicitud(String titulo, int ancho, int modo,
			List<TipoSolicitud> tipos) {
		super(titulo, ancho);
		this.accion = modo;
		this.tipos = tipos;
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		gbGeneral = new CompGrupoDatos("Motivo", 2);
		tipo = new CompCombobox<TipoSolicitud>();
		estado = new Label();
		estado.setVisible(true);
		estado.setWidth("100px");
		estado.setStyle("font-size: 18px; font-weigth: bold;");
		tipo.setDisabled(true);
		if (accion == Accion.AGREGAR || accion == Accion.EDITAR) {
			txtdescripcion = new Textbox();
			tipo.setDisabled(false);
		}
		if (accion == Accion.CONSULTAR)
			lbldescripcion = new Label();
		tipo.setWidth("150px");
		tipo.setModelo(tipos);
		// tipo.addEventListener(CompBuscar.ON_SELECCIONO, getControlador());

	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub

		if (accion == Accion.AGREGAR || accion == Accion.EDITAR) {
			txtdescripcion.setWidth("350px");
			txtdescripcion.setMaxlength(50);
			gbGeneral.addComponente("Descripción: ", txtdescripcion);

			gbGeneral.dibujar(this);
		}

		if (accion == Accion.CONSULTAR) {
			gbGeneral.addComponente("Descripción: ", lbldescripcion);

			gbGeneral.dibujar(this);
		}
		gbGeneral.addComponente("Estado", estado);
		gbGeneral.addComponente("Tipo de Solicitud", tipo);
		addBotonera();
	}

	@Override
	public void cargarValores(MotivoAnulacionSolicitud arg0)
			throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		if (accion == Accion.AGREGAR || accion == Accion.EDITAR) {
			txtdescripcion.setValue(getModelo().getMotivo());
			getBinder().addBinding(txtdescripcion, "value",
					getNombreModelo() + ".motivo", null, null, "save", null,
					null, null, null);
		}

		if (accion == Accion.CONSULTAR)
			lbldescripcion.setValue(getModelo().getMotivo());
		estado.setValue(getModelo().getStrEstado());
		tipo.setSeleccion(getModelo().getTipoSolicitud());
		getBinder().addBinding(tipo, "seleccion",
				getNombreModelo() + ".tipoSolicitud", null, null, "save", null,
				null, null, null);

	}

	public int getAccion() {
		return accion;
	}

	public void setAccion(int accion) {
		this.accion = accion;
	}

	public Textbox getTxtdescripcion() {
		return txtdescripcion;
	}

	public void setTxtdescripcion(Textbox txtdescripcion) {
		this.txtdescripcion = txtdescripcion;
	}

	public Label getLbldescripcion() {
		return lbldescripcion;
	}

	public void setLbldescripcion(Label lbldescripcion) {
		this.lbldescripcion = lbldescripcion;
	}

	public CompCombobox<TipoSolicitud> getTipo() {
		return tipo;
	}

	public void setTipo(CompCombobox<TipoSolicitud> tiposolicitud) {
		this.tipo = tiposolicitud;

	}

}
