package cpc.demeter.vista.mantenimiento;

import org.zkoss.zul.Textbox;

import cpc.modelo.demeter.mantenimiento.DetalleGarantina;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.ventanas.CompGrupoDatos;
import cpc.zk.componente.ventanas.CompVentanaBase;

public class UISelectDetalleGarantia extends CompVentanaBase<DetalleGarantina> {

	private static final long serialVersionUID = 1L;
	private CompGrupoDatos divGeneral;
	private Textbox descripcion;

	public UISelectDetalleGarantia(String titulo, int ancho) {
		super(titulo, ancho);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void inicializar() {
		// TODO Auto-generated method stub
		divGeneral = new CompGrupoDatos();
		descripcion = new Textbox();
	}

	@Override
	public void dibujar() {
		// TODO Auto-generated method stub
		divGeneral.addComponente(" Detalle ", descripcion);
		divGeneral.dibujar(this);
		addBotonera();
	}

	@Override
	public void cargarValores(DetalleGarantina dato) throws ExcDatosInvalidos {
		// TODO Auto-generated method stub
		descripcion.setText(getModelo().getDescripcion());
		getBinder().addBinding(descripcion, "value",
				getNombreModelo() + ".descripcion", null, null, "save", null,
				null, null, null);

	}

}
