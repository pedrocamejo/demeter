package cpc.demeter.controlador.gestion;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.gestion.UIMaquinariaImpropia;
import cpc.modelo.demeter.gestion.EstadoMaquinariaImpropia;
import cpc.modelo.demeter.gestion.MaquinariaImpropia;
import cpc.modelo.demeter.gestion.MaquinariaUnidad;
import cpc.modelo.ministerio.dimension.UnidadFuncional;
import cpc.modelo.sigesp.basico.Categoria;
import cpc.modelo.sigesp.basico.Marca;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.modelo.sigesp.basico.Tipo;
import cpc.negocio.demeter.gestion.NegocioMaquinariaImpropia;
import cpc.negocio.ministerio.basico.NegocioUnidadFuncional;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContMaquinariaImpropia extends ContVentanaBase<MaquinariaImpropia> {

	private NegocioMaquinariaImpropia	 servicio;
	private UIMaquinariaImpropia		 vista;
	private AppDemeter					 app;

	public ContMaquinariaImpropia(int modoOperacion, MaquinariaImpropia maquinaria, ContCatalogo<MaquinariaImpropia> llamador, AppDemeter app) throws ExcDatosInvalidos, ExcFiltroExcepcion {
		super(modoOperacion);
		this.app = app;
		this.servicio = NegocioMaquinariaImpropia.getInstance();
		NegocioUnidadFuncional  servicioUnidad = NegocioUnidadFuncional.getInstance();
	
		if (modoAgregar())
		{
			maquinaria = new MaquinariaImpropia();
				// el Amigo 
			MaquinariaUnidad maquinariaUnidad =new MaquinariaUnidad();
			maquinariaUnidad.setOperativo(false);
			maquinariaUnidad.setMaquinariaImpropia(maquinaria);
			maquinaria.setMaquinariaUnidad(maquinariaUnidad);
		}
		List<Marca> marcas = servicio.marcas();
		List<Categoria> categorias = servicio.categorias();
		List<UnidadFuncional> unidades = servicioUnidad.getTodos();
	
		vista = new UIMaquinariaImpropia("Maquinaria Impropia (" + Accion.TEXTO[modoOperacion] + ")",900, this.app,marcas,categorias,unidades);
		setear(maquinaria, vista , llamador, this.app);
		this.vista = (UIMaquinariaImpropia) getVista();
		this.vista.desactivar(modoOperacion);

	}

	
	public void eliminar() {
		try {
			servicio.eliminar(getDato());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {
		try {
			//Maquinaria Impropia 
			UnidadFuncional unidad=	(UnidadFuncional) vista.getMaquinariaUnidad().getSelectedItem().getValue();
			getDato().getMaquinariaUnidad().setUnidad(unidad);
			servicio.guardar(getDato());
		} 
		catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());

		}
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

		if (modoAgregar())
		{
			// validar unicidad Serial Chasis Serial Otro 
			if (vista.getSerialChasis().getValue().trim().length() == 0)
			{
				throw new WrongValueException(vista.getSerialChasis(),"Ingrese el Serial de Chasis de la Maquinaria ");
			}

			if (vista.getSerialOtro().getValue().trim().length() == 0)
			{
				throw new WrongValueException(vista.getSerialOtro(),"Ingrese Otro  Serial de la Maquinaria ");
			}
			if (servicio.getMaquinaria(vista.getSerialChasis().getValue()) != null )
			{
				throw new WrongValueException(vista.getSerialChasis(),"Este Serial ah sido Asignado a Otra Maquinaria ");
			}
			if (servicio.getMaquinariaOtroSerial(vista.getSerialOtro().getValue()) != null )
			{
				throw new WrongValueException(vista.getSerialOtro(),"Este Serial ah sido Asignado a Otra Maquinaria ");
			}
		}
		if (vista.getProcedencia().getValue().trim().length() == 0)
		{
			throw new WrongValueException(vista.getProcedencia(),"Ingrese la procedencia de la Maquinaria ");
		}
		if (vista.getNombre().getValue().trim().length() == 0)
		{
			throw new WrongValueException(vista.getNombre(),"Ingrese la nombre  de la Maquinaria ");
		}
		if (vista.getNombre().getValue().trim().length() == 0)
		{
			throw new WrongValueException(vista.getNombre(),"Ingrese la nombre  de la Maquinaria ");
		}
		if (vista.getMarca().getSelectedItem() == null )
		{
			throw new WrongValueException(vista.getMarca(),"Seleccione la Marca de la Maquinaria ");
		}
		if (vista.getModeloMaquinaria().getSelectedItem() == null )
		{
			throw new WrongValueException(vista.getModeloMaquinaria(),"Seleccione el Modelo de la Maquinaria ");
		}
		if (vista.getCategoria().getSelectedItem() == null )
		{
			throw new WrongValueException(vista.getCategoria(),"Seleccione el Categoria de la Maquinaria ");
		}
		if (vista.getTipo().getSelectedItem() == null )
		{
			throw new WrongValueException(vista.getTipo(),"Seleccione el Tipo  de la Maquinaria ");
		}
		if (vista.getMaquinariaUnidad().getSelectedItem() == null)
		{
			throw new WrongValueException(vista.getMaquinariaUnidad(),"Asigne una Unidad ");
		}

	}

	public void onEvent(Event event) throws Exception {

		if (event.getTarget() == getVista().getAceptar()) {
			procesarCRUD(event);
		} 
		
		if(event.getTarget() == vista.getMarca())
		{
			Marca marca = (Marca) vista.getMarca().getSelectedItem().getValue();
			List<Modelo> modelos = servicio.modelos(marca);
			vista.setModelos(modelos);
		}
		
		if(event.getTarget() == vista.getCategoria())
		{
			Categoria categoria = (Categoria) vista.getCategoria().getSelectedItem().getValue();
			List<Tipo> tipos = servicio.tipos(categoria);
			vista.setTipos(tipos);
		}
		
	}

	@Override
	public void anular() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub

	}
}