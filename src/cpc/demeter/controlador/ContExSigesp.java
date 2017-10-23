package cpc.demeter.controlador;

import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Filedownload;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContExportarSigesp;
import cpc.demeter.vista.ExportarSigespUI;
import cpc.modelo.demeter.administrativo.sigesp.ExtracionDatosSigesp;
import cpc.negocio.demeter.administrativo.NegocioLibroVenta;
import cpc.negocio.demeter.administrativo.sigesp.NegocioExtracionDatosSigesp;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContExSigesp extends ContVentanaBase<ExtracionDatosSigesp> {

	private static final long serialVersionUID = 6184414588153046382L;
	private NegocioExtracionDatosSigesp servicio;
	private AppDemeter app;
	private ExportarSigespUI vista;

	public ContExSigesp(int modoOperacion, ExtracionDatosSigesp modelo, ContExportarSigesp llamador,AppDemeter app) throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		servicio = NegocioExtracionDatosSigesp.getInstance();
	
		try {
			if (modelo == null || modoAgregar())
			{
				modelo = new ExtracionDatosSigesp();
				modelo.setUsuario(app.getUsuario().getNombreIdentidad());
			}
			setear(modelo, new ExportarSigespUI("Exportar (" + Accion.TEXTO[modoOperacion]+ ")", 550), llamador, app);
			vista = ((ExportarSigespUI) getVista());
			vista.desactivar(modoOperacion);
		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		}
	}

	public void eliminar() {

		try {
			servicio = NegocioExtracionDatosSigesp.getInstance();
			servicio.eliminar(getDato());
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void guardar() {

		try {
			servicio = NegocioExtracionDatosSigesp.getInstance();
			servicio.guardar(getDato());
			byte[]  archivo = servicio.generarZip(getDato());
			if(archivo.length != 0)
			{
				
				Filedownload.save(archivo,"application/zip",getDato().getNombreSede()+".zip");
			}
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}

	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
			if(vista.getAno().getSelectedItem() == null )
			{
				throw new  WrongValueException(vista.getAno(),"Seleccione un ano");
			}
			if(vista.getMes().getSelectedItem() == null )
			{
				throw new  WrongValueException(vista.getAno(),"Seleccione un ano");
			}
			// validar Mes y ano 
			ExtracionDatosSigesp dato = null;
			try {
				dato = servicio.getExtracionDatosSigesp((Integer)vista.getAno().getSelectedItem().getValue(),(Integer)vista.getMes().getSelectedItem().getValue());
			}
			catch (ExcFiltroExcepcion e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new WrongValueException(vista,"Esta Fecha ah sido Procesada ");
			}
			if(dato != null )
			{	
				throw new WrongValueException(vista,"Esta Fecha ah sido Procesada ");
			}
			// se puede crear solamente si ya esta listo el libro  de venta que es mensual 
			NegocioLibroVenta venta = NegocioLibroVenta.getInstance();
			if (venta.getLibro((Integer)vista.getMes().getSelectedItem().getValue(),
					(Integer)vista.getAno().getSelectedItem().getValue()) == null )
			{
				throw new WrongValueException(vista,"Este Mes no Tiene Libro de Venta ");
			}
			
	}

	public void onEvent(Event arg0) throws Exception {
		procesarCRUD(arg0);
	}

	@Override
	public void anular() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		byte[] archivo;
		try {
			archivo = servicio.generarZip(getDato());
			if(archivo.length != 0)
			{
				Filedownload.save(archivo,"application/zip",getDato().getNombreSede()+".zip");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new ExcFiltroExcepcion("Error al Generar el zip ");
		}
	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub

	}

}
