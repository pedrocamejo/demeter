package cpc.demeter.controlador.mantenimiento;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import jbarcodebean.JBarcodeBean;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.apache.commons.collections.map.HashedMap;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.zkoss.zk.ui.SuspendNotAllowedException;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zul.Messagebox;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.catalogo.mantenimiento.CatMaquinariaSelect;
import cpc.demeter.catalogo.mantenimiento.ContGarantiasExterna;
import cpc.demeter.controlador.mantenimiento.garantia.ContSelecionarCliente;
import cpc.demeter.vista.mantenimiento.UILote;
import cpc.demeter.vista.mantenimiento.UIMostrarReporte;
import cpc.demeter.vista.mantenimiento.UiGarantiaExterna;
import cpc.modelo.demeter.mantenimiento.EnteExterno;
import cpc.modelo.demeter.mantenimiento.Garantia;
import cpc.modelo.demeter.mantenimiento.Lote;
import cpc.modelo.demeter.mantenimiento.MaquinariaExterna;
import cpc.modelo.demeter.mantenimiento.TipoGarantia;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.modelo.sigesp.basico.Marca;
import cpc.negocio.demeter.administrativo.NegocioFactura;
import cpc.negocio.demeter.mantenimiento.NegocioGarantia;
import cpc.negocio.demeter.mantenimiento.NegocioLote;
import cpc.negocio.demeter.mantenimiento.NegocioMaquinariaExterna;
import cpc.negocio.demeter.mantenimiento.NegocioTipoGarantia;
import cpc.persistencia.SessionDao;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.ICompCatalogo;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContGarantiaExterna extends ContVentanaBase<Garantia> {

	private static final long serialVersionUID = 6184414588153046382L;

	private NegocioGarantia				 servicio;
	private AppDemeter					 app;
	private UiGarantiaExterna 			 vista;
	private Garantia					 garantia;

	public ContGarantiaExterna(int modoOperacion, Garantia garantia,ContGarantiasExterna llamador, AppDemeter app)	throws SuspendNotAllowedException, InterruptedException,
			ExcFiltroExcepcion, ExcDatosInvalidos 
	{
		super(modoOperacion);
		servicio = NegocioGarantia.getInstance();
		this.app = app;
		this.garantia = garantia;
		if (modoOperacion == Accion.AGREGAR) 
		{
			this.garantia = new Garantia();
			EnteExterno ente = this.servicio.entepordefault();
			this.garantia.setEnte(ente);
		} 
		vista = new UiGarantiaExterna("Garantias (" + Accion.TEXTO[modoOperacion] + ")",800);
		vista.setMaximizable(true);
		setear(this.garantia, vista, llamador, app);
		this.vista = (UiGarantiaExterna) getVista();
		vista.desactivar(getModoOperacion());
	}
   
	public void guardar() {
		try {
	  	    vista.actualizar();
			servicio.guardar(getDato());
			app.mostrarConfirmacion("Garantia  Almacenada ");
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void onEvent(Event arg0) throws Exception { 
		
		 
		//	new ContSelecionarCliente(Accion.CONSULTAR,this,app);
	 	if(vista.getAddPropietario() == arg0.getTarget())
		{
	 		Map parametros = new HashMap();
	 		parametros.put("propietario","true");
	 		new ContSelecionarCliente(Accion.CONSULTAR,this,app,parametros);
		}
	 	else if(vista.getAddContacto() == arg0.getTarget())
	 	{
	 		Map parametros = new HashMap();
	 		parametros.put("contacto","true");
	 		new ContSelecionarCliente(Accion.CONSULTAR,this,app,parametros);
	 	}
	 	else if(vista.getAddMaquinaria() == arg0.getTarget())
	 	{
	 		new ContSelecionarMaquinaria(Accion.CONSULTAR,this,app,null);
	 	}
	 	else
	 	{
	 		procesarCRUD(arg0);
	 	}
	 	  
	}

	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		

	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

		if(vista.getMaquinaria() == null )
		{
			throw new WrongValueException(vista.getAddMaquinaria(),"Seleccione una Maquinaria ");
		}
		if(vista.getPropietario() == null )
		{
			throw new WrongValueException(vista.getAddPropietario()	,"Seleccione un Propietario  ");
		}
		
		if(vista.getUbicacion().getText().trim().length() == 0)
		{
			throw new WrongValueException(vista.getUbicacion(),"Ingrese la Ubicacion Fiscia de la Maquinaria ");
		}
	}

	
	public void MostrarGarantia() throws Exception {

		String url = getDato().getMaquinaria().getTipo().getUrlReporte();

		if (url != null) 
		{
			try {

				if (getDato().getEstatus() == 0)
				{
					// No puedo llamar al metodo CRUD
					IZkAplicacion a = (IZkAplicacion) TransactionSynchronizationManager.getResource("obj");
					boolean b = TransactionSynchronizationManager.hasResource("obj");
					boolean igual = app.equals(a);
					
					if (!igual) {
						if (b) {
							TransactionSynchronizationManager.unbindResource("obj");
						}
						TransactionSynchronizationManager.bindResource("obj",app);
					}
					getDato().setEstatus(1); // Generada
					servicio.guardar(getDato());
					refrescarCatalogo();
				}

				List<Garantia> lista = new ArrayList<Garantia>();
				lista.add(getDato());
				String type = "pdf";
				/*** el codigo de Barras ***/

				JBarcodeBean barcode = new JBarcodeBean();

				// id mas serial de carroceria 
				String codigoBarras = getDato().getId() + "-" + getDato().getMaquinaria().getSerialcarroceria();
				barcode.setCode(codigoBarras);
				// creo la imagen
				BufferedImage bufferImagen = barcode.draw(new BufferedImage(300, 50, BufferedImage.TYPE_INT_RGB)); 
				File file1 = new File(codigoBarras.trim() + ".png"); 
				ImageIO.write(bufferImagen, "png", file1);  
				/***** fin del codigo de barras **/
				File file = new File(Index.class.getResource("/").getPath()+ "../../imagenes/cval.jpg");
				File file3 = new File(Index.class.getResource("/").getPath()+ "../../imagenes/gobierno.jpg");
				File file4 = new File(Index.class.getResource("/").getPath()+ "../../imagenes/MPPAT.jpg");
				File file5 = new File(Index.class.getResource("/").getPath() + "../../imagenes/logopc.png");

				Map mapa = new HashedMap();
				mapa.put("imgCval", file);
				mapa.put("BarCodigo", file1);
				mapa.put("imgGobierno", file3);
				mapa.put("imgMppat", file4);
				mapa.put("imgLogo", file5);
				
				String urltotoal = Index.class.getResource("/").getPath() + "../../" + url; // los sub reportes la ubicacion total
											// desde la raiz
				// mapa.put("SUBREPORT_DIR",Index.class.getResource("/").getPath()+"../../reportes/mantenimiento/1/");
				mapa.put("SUBREPORT_DIR", urltotoal);

				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(lista);
				UIMostrarReporte mostrarReporte;
				mostrarReporte = new UIMostrarReporte("Reporte Garantia ",	"normal", true, dataSource, url + "Garantia.jasper",type, mapa);
				app.agregarHija(mostrarReporte);
				mostrarReporte.doModal();
				file1.delete();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			app.mostrarError("Esta Garantia no Posee PDF");
		}
	}

	@Override
	public void anular() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		if (getDato().getEstatus() == 2) 
		{
			try {
				getDato().setEstatus(2);
				servicio.guardar(getDato());
				refrescarCatalogo();
				vista.detach();
				app.mostrarConfirmacion("Garantia  Anulada  ");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				app.mostrarError(e.getMessage());
			}
		}
		else
		{
			app.mostrarError("NO se Puede Anular Esta Garantia Posee mantenimientos ");
		}
	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		if (getDato().getEstatus() == 0 ) 
		{
			try {
				getDato().setEstatus(1); // activar Garantia para x persona 
				servicio.guardar(getDato());
				refrescarCatalogo();
				vista.detach();
				app.mostrarConfirmacion("Garantia  Activada ");
			} 
			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				app.mostrarError(e.getMessage());
			}
		}
		else
		{
			app.mostrarError("No se puede Asignar el Estado en Proceso a esta Garantia Posee un estatus Procesada ");
		}
	}




	public void seleccionarPropietario(Cliente cliente) {
		// TODO Auto-generated method stub

			vista.seleccionarPropietario(cliente);
	}




	public void seleccionarContacto(Cliente cliente) {
		// TODO Auto-generated method stub
		vista.seleccionarContacto(cliente);
	}




	public void agregarMaquinaria(MaquinariaExterna maquinaria) {
		// TODO Auto-generated method stub
		vista.seleccionarMaquinaria(maquinaria);
	}

}
