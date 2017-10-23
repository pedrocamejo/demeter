package cpc.demeter.vista.gestion;

import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

import cpc.modelo.sigesp.basico.ActivoAlmacen;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UiDesincorporacionActivo extends CompVentanaBase<ActivoAlmacen> {

	private static final long serialVersionUID = -2721253130249689320L;
	private CompGrupoDatos agrupador;
	private Textbox motivo;
	private Label lblserial, lblmarca, lblmodelo, lblalmacen;

	public UiDesincorporacionActivo(String titulo, int ancho) {
		super(titulo, ancho);
	}

	public void inicializar() {
		agrupador = new CompGrupoDatos(2);
		motivo = new Textbox();
		motivo.setWidth("290px");
		motivo.setMaxlength(200);
		motivo.setRows(2);

		lblserial = new Label();
		lblmarca = new Label();
		lblmodelo = new Label();
		lblalmacen = new Label();
	}

	public void dibujar() {
		agrupador.addComponente("Serial: ", lblserial);
		agrupador.addComponente("Marca: ", lblmarca);
		agrupador.addComponente("Modelo: ", lblmodelo);
		agrupador.addComponente("Almacen: ", lblalmacen);
		agrupador.addComponente("Motivo: ", motivo);

		agrupador.dibujar(this);
		addBotonera();
	}

	public void cargarValores(ActivoAlmacen arg0) throws ExcDatosInvalidos {
		lblserial.setValue(getModelo().getSerialActivo());
		lblmarca.setValue(getModelo().getMarcaActivo());
		lblmodelo.setValue(getModelo().getModeloActivo());
		lblalmacen.setValue(getModelo().getNombreAlmacen());
		motivo.setValue(getModelo().getMotivoDesincorporacion());
		getBinder().addBinding(motivo, "value",
				getNombreModelo() + ".motivoDesincorporacion", null, null,
				"save", null, null, null, null);
	}

	public Textbox getMotivo() {
		return motivo;
	}

	public void setMotivo(Textbox motivo) {
		this.motivo = motivo;
	}

}
