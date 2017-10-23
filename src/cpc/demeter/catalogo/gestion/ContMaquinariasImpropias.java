package cpc.demeter.catalogo.gestion;

import java.util.ArrayList;
import java.util.List;




import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Script;
import cpc.ares.modelo.Accion;
import cpc.ares.modelo.AccionFuncionalidad;
import cpc.demeter.AppDemeter;
import cpc.demeter.controlador.gestion.ContMaquinariaImpropia;
import cpc.demeter.controlador.gestion.ContMaquinariaImpropiaProcesar;
import cpc.demeter.controlador.gestion.ContSustitucion;
import cpc.modelo.demeter.gestion.EstadoMaquinariaImpropia;
import cpc.modelo.demeter.gestion.MaquinariaImpropia;
import cpc.modelo.demeter.gestion.Sustitucion;
import cpc.modelo.sigesp.basico.Categoria;
import cpc.modelo.sigesp.basico.Marca;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.modelo.sigesp.basico.Tipo;
import cpc.negocio.demeter.gestion.NegocioMaquinariaImpropia;
import cpc.negocio.demeter.gestion.NegocioSustitucion;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcColumnasInvalidas;
import cva.pc.componentes.CompEncabezado;
import cva.pc.demeter.excepciones.ExcArgumentoInvalido;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContMaquinariasImpropias extends ContCatalogo<MaquinariaImpropia> {

	private static final long serialVersionUID = -3727563103572490818L;
	private AppDemeter app;

	public ContMaquinariasImpropias(AccionFuncionalidad accionesValidas, AppDemeter app)
			throws ExcColumnasInvalidas, ExcArgumentoInvalido,
			ExcFiltroExcepcion {
		super();
		NegocioMaquinariaImpropia negocio = NegocioMaquinariaImpropia.getInstance();
		this.app = app;
		dibujar(accionesValidas, "Maqinarias Impromias ", negocio.getTodos(),app);
	}

	public ContMaquinariasImpropias() {
		super();
	}

	public ContMaquinariasImpropias(int modoOperacion) {
		super(modoOperacion);
	}
 
	
	@SuppressWarnings("unchecked")
	public List cargarEncabezado() {
		CompEncabezado titulo;
		List<CompEncabezado> encabezado = new ArrayList<CompEncabezado>();

		titulo = new CompEncabezado("Serial Chasis ", 100);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getSerialChasis");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Serial Otro ", 170);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getSerialOtro");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Marca ", 120);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrMarca");
		encabezado.add(titulo);
		
		titulo = new CompEncabezado("Modelo", 170);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrModelo");
		encabezado.add(titulo);

		titulo = new CompEncabezado(" Categoria ", 180);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrCategoria");
		encabezado.add(titulo);

		
		titulo = new CompEncabezado(" Tipo ", 180);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrTipo");
		encabezado.add(titulo);

		titulo = new CompEncabezado("Estado",180);
		titulo.setOrdenable(true);
		titulo.setMetodoBinder("getStrEstado");
		encabezado.add(titulo);

		return encabezado;
	}

	public void onEvent(Event event) throws Exception {
		try {
			NegocioMaquinariaImpropia servicio = NegocioMaquinariaImpropia.getInstance();

			int accion = (Integer) event.getTarget().getAttribute("pos");

			MaquinariaImpropia  maquinaria = getDatoSeleccionado();

			if(accion <= Accion.CONSULTAR) {
				if(accion == Accion.AGREGAR ) {
					new ContMaquinariaImpropia(accion,maquinaria,this,app);
				}
				else if (maquinaria != null) {
					if(accion == Accion.EDITAR && maquinaria.getEstado().getId().equals(EstadoMaquinariaImpropia.INGRESADO)) {
						new ContMaquinariaImpropia(accion,maquinaria,this,app);
					}
					else if(accion == Accion.CONSULTAR || accion == Accion.ELIMINAR) 	{
						new ContMaquinariaImpropia(accion,maquinaria,this,app);
					}
					else {
						this.app.mostrarError("No Puedes Modificar la Maquinaria Ya ah sido Verificada ");
					}
				}
				else {
					this.app.mostrarError("No ha seleccionado ningun campo");
				}
			}
			else if(accion == Accion.PROCESAR && maquinaria != null  ) 	{
				if(maquinaria.getEstado().getId().equals(EstadoMaquinariaImpropia.INGRESADO))
				{
					new ContMaquinariaImpropiaProcesar(accion,maquinaria,this,app);
				}
				else
				{
					app.mostrarError("Maquinaria Verificada ");
				}
			}
			else if(accion == Accion.ANULAR && maquinaria != null  ) {
				if(maquinaria.getEstado().getId().equals(EstadoMaquinariaImpropia.VERIFICADO)){
					new ContMaquinariaImpropiaProcesar(accion,maquinaria,this,app);
				}
				else{
					this.app.mostrarError("Maquinaria con estado distinto a  Verificado");
				}
			}
			else{
				this.app.mostrarError("No ha seleccionado ningun campo");
			}
		} 
		catch (NullPointerException e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		} catch (Exception e) {
			e.printStackTrace();
			this.app.mostrarError(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List cargarDato(MaquinariaImpropia arg0) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected List<List> cargarDatos() throws ExcArgumentoInvalido {
		return null;
	}
}