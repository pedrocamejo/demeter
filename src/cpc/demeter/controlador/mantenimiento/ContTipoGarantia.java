package cpc.demeter.controlador.mantenimiento;

import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.zkoss.zhtml.Messagebox;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.mantenimiento.ContTiposGarantias;
import cpc.demeter.vista.mantenimiento.UITipoGarantia;
import cpc.modelo.demeter.mantenimiento.DetalleGarantina;
import cpc.modelo.demeter.mantenimiento.TipoGarantia;
import cpc.negocio.demeter.mantenimiento.NegocioTipoGarantia;
import cpc.negocio.demeter.mantenimiento.NegocioTipoServicio;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

/*
 * historia buena que contar jajaj heee la clase compList tiene un errorsirijillo o algo asi por el estilo cuando 
 * creas una lista bn le añades un modelo bn pero el modelo esta limpio esta en 0 bn verdad agregas algoo bn por que agregaste algo 
 * a la lista alegrate cuando llamas a getmodelo este viene con algo bn alegrate coñoo vale sabes por que por esto  if (modelo.size()>0) return modelo; else return getDatos(); 
 * bn no te da el modelo te da los datos haora todo bn haora fijate
 * 
 *  cuando vas a modificar un objecto cargas el binder y toda la Pajuela jeje
 * luego cuando cargas esta le dices algo tan simple como lista.setmodelo(tulistademodelo) luego refrescar esta bn crea la lista refrescala pero esto 
 * ok luego cuando le agregas algo llamas al metodo   agregar(obj); <- le pasas un objecto bn pero esta no agrega el obj al la lista del modelo por lo cual al tu querer
 * obtener los cambios nuevos de esa vaina comete esto te da la lista del modelo Original por que por que ella es mayor que 0 vez :-D ella tiene algo lo que se le
 * agregoo no mas ni menos bueno eso hace que la ultimas coasas que ageges nuevas noOO se guarden en el modelo :-D estoo esta mal lo voy a arreglar asi bueno no lo voy a arreglar hay me da euqal :-D
 * cuando agrege un objecto a la lista llamo al objecto y le digo agrega cuando le quite algo llamo a la lista y le digo quita haora hay algo cuando el objecto tenga 2 
 * direcciones diferentes coñoo voy a tener q hacerlo mas a pie jeje que felicidad :-D
 * */
public class ContTipoGarantia extends ContVentanaBase<TipoGarantia> {

	private static final long serialVersionUID = 1L;
	private AppDemeter app;
	private TipoGarantia tipoGarantia;

	private UITipoGarantia vista;

	private NegocioTipoGarantia servicio;

	public ContTipoGarantia(int modoOperacion, TipoGarantia tipoGarantia,	ContTiposGarantias llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcFiltroExcepcion {
		super(modoOperacion);
		this.app = app;
		this.tipoGarantia = tipoGarantia;

		if (modoAgregar()) {
			this.tipoGarantia = new TipoGarantia();

		}
		servicio = NegocioTipoGarantia.getInstance();

		vista = new UITipoGarantia("Modelo de Maquinarias Garantia("+ Accion.TEXTO[modoOperacion] + ")", 700,
				servicio.getmodelos(), servicio.getClaseMaquinaria(),
				servicio.getTipoMaquinariaVenta(),servicio.getSeries());
		
		vista.setApp(app);
		setear(this.tipoGarantia, vista, llamador, app);
		this.vista = (UITipoGarantia) getVista();
		vista.setmodooperacion(modoOperacion);

	}

	@Override
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub

		if (arg0.getTarget() == vista.getAceptar()) {
			procesarCRUD(arg0);
		} 
		else if (vista.getAgregarDetalle() == arg0.getTarget()) 
		{
			new ContDetalleGarantia(Accion.AGREGAR, null, this, app);
		}
		else if (vista.getQuitarDetalle() == arg0.getTarget()) 
		{
			DetalleGarantina detalle = vista.getDatoSeleccionado();
		
			if (detalle == null)
			{
				app.mostrarError("Debe Seleccionar al menos 1 Items");
			}
			else
			{
				int i = Messagebox.show("Seleccione al Menos 1 Itemes",	"Seleccion", Messagebox.NO | Messagebox.YES,Messagebox.EXCLAMATION);

				if (i == Messagebox.YES)
				{
					vista.quitardetalle(detalle);
				}
			}
		}
	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
	 	try {
			servicio = NegocioTipoGarantia.getInstance();
			tipoGarantia = getDato();
			tipoGarantia.setSerie(vista.getSerie().getSeleccion());
			tipoGarantia.setDetallesGarantia(vista.getDetalleslista());
			servicio.Guardar(tipoGarantia);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		vista.detach();
	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		try {
			servicio = NegocioTipoGarantia.getInstance();
			tipoGarantia = getDato();
			servicio.Borrar(tipoGarantia);
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
			vista.detach();
		}
 	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		
		if(vista.getDescripcion().getValue().trim().length() == 0)
		{
			throw new WrongValueException(vista.getDescripcion(),"Indique una Descripcion"); 
		}
		if(vista.getModeloMaquina().getSeleccion()== null)
		{
			throw new WrongValueException(vista.getModeloMaquina(),"Seleccione un Modelo "); 
		}
		if(vista.getSerie().getSeleccion() == null)
		{
			throw new WrongValueException(vista.getSerie(),"Seleccione el Numero de Serie ");
		}
		
		if(vista.getClaseMaquinaria().getSeleccion()== null)
		{
			throw new WrongValueException(vista.getClaseMaquinaria(),"Seleccioen una Clase"); 
		}
		if(vista.getTipoMaquinaria().getSeleccion()== null)
		{
			throw new WrongValueException(vista.getTipoMaquinaria(),"Indique un Tipo"); 
		}
		if(vista.getColorPrimario().getValue().trim().length()== 0)
		{
			throw new WrongValueException(vista.getColorPrimario(),"Ingrese un Color Primario "); 
		}
		if(vista.getColorSecundario().getValue().trim().length()== 0)
		{
			throw new WrongValueException(vista.getColorSecundario(),"Ingrese un Color Secuendario"); 
		}

		if(vista.getMotormodelo().getSeleccion() ==null)
		{
			throw new WrongValueException(vista.getMotormodelo(),"Seleccione Modelo "); 
		}
		
		if(vista.getMotortipo().getValue().trim().length() == 0)
		{
			throw new WrongValueException(vista.getMotortipo(),"Ingrese el Tipo de Motor"); 
		}
		if(vista.getFuerza().getValue().trim().length() == 0)
		{
			throw new WrongValueException(vista.getFuerza(),"Indique la fuerza del Motor de la Maquinaria "); 
		}
		if(vista.getPotencia().getValue().trim().length() == 0)
		{
			throw new WrongValueException(vista.getPotencia(),"Indique la Potencia del Motor"); 
		}
		 
		
	}

	public void AgregarDetalleGarantia(DetalleGarantina dt) {
		// TODO Auto-generated method stub
		getDato().getDetallesGarantia().add(dt);
		vista.agregardetalle(dt);
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
