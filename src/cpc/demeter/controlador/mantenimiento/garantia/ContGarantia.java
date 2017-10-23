package cpc.demeter.controlador.mantenimiento.garantia;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Messagebox;

import cpc.ares.modelo.Accion;
import cpc.demeter.ActivacionGarantia;
import cpc.demeter.Index;
import cpc.demeter.catalogo.mantenimiento.CatMaquinariaSelect;
import cpc.demeter.controlador.administrativo.ContSeleccionarCheque;
import cpc.demeter.controlador.mantenimiento.ContMaquinariaExterna;
import cpc.demeter.vista.mantenimiento.UIMostrarReporte;
import cpc.demeter.vista.mantenimiento.UITipoGarantia;
import cpc.demeter.vista.mantenimiento.garantia.UiGarantia;
import cpc.modelo.demeter.mantenimiento.Garantia;
import cpc.modelo.demeter.mantenimiento.MaquinariaExterna;
import cpc.modelo.demeter.mantenimiento.TipoGarantia;
import cpc.modelo.ministerio.gestion.Cliente;
import cpc.modelo.sigesp.basico.Marca;
import cpc.modelo.sigesp.basico.Modelo;
import cpc.negocio.demeter.administrativo.NegocioDeposito;
import cpc.negocio.demeter.mantenimiento.NegocioGarantia;
import cpc.persistencia.SessionDao;

import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

public class ContGarantia extends ContVentanaBase<Garantia> {

	private static final long serialVersionUID = 6184414588153046382L;

	private NegocioGarantia 			servicio;
	private ActivacionGarantia 		 	app;
	private UiGarantia 					vista;
	private ContSelecionarCliente 		contSelecionarCliente;
	private Garantia					garantia;
	
	
	public ContGarantia(int i, Garantia garantia, ActivacionGarantia app) throws SuspendNotAllowedException, InterruptedException, ExcFiltroExcepcion, ExcDatosInvalidos
	{
		super(i);
		servicio = NegocioGarantia.getInstance(); 
		this.app = app;
		this.garantia = garantia;

		if (garantia == null)
		{
			this.garantia = new Garantia();
			this.garantia.setEstatus(0); // nueva :-D
		}

		vista = new UiGarantia("Activacion de Garantia ", 850, this.garantia,this,i);
		app.agregarAEscritorio(vista);
		vista.modoOperacion(i);
		vista.doModal();
	}

	public void guardar()
	{
		if (Accion.IMPRIMIR_ITEM == getModoOperacion())
		{
			if (garantia.getEstatus() == 0) 
			{
				int i = Messagebox.NO;
			
				try {
					i = Messagebox.show("SI Imprimes la Garantia no podras Modificarla Desea Continuar ? ","Generar Garantia", Messagebox.YES | Messagebox.NO,Messagebox.EXCLAMATION);
				} 
				catch (InterruptedException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if (i == Messagebox.YES) 
				{
					try 
					{
						MostrarGarantia();
					} 
					catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					catch (Exception e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else 
				{
					vista.detach();
				}
			} 
			else
			{
				try {
					MostrarGarantia();
				} 
				catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		else
		{
			try {

				IZkAplicacion a = (IZkAplicacion) TransactionSynchronizationManager.getResource("obj");
				boolean b = TransactionSynchronizationManager.hasResource("obj");
				boolean igual = app.equals(a);
				if (!igual)
				{
					if (b)
					{
						TransactionSynchronizationManager.unbindResource("obj");
					}
					TransactionSynchronizationManager.bindResource("obj", app);
				}
				//ente 
				ActivacionGarantia activacion = app;
				garantia.setEnte(activacion.getenteExterno());
				servicio.guardar(garantia);
				
				if (getModoOperacion() == Accion.AGREGAR)
				{
					app.cargarGarantia(garantia, getModoOperacion());
				
				}
				else if (getModoOperacion() == Accion.ELIMINAR || getModoOperacion() == Accion.EDITAR)
				{
					app.ActualizarListaGarantia();
				}
				vista.detach();
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void MostrarGarantia() throws Exception 
	{
		String url = garantia.getMaquinaria().getTipo().getUrlReporte();
		if (url != null) {
			try {
				if (garantia.getEstatus() == 0) {
					garantia.setEstatus(1); // Generada
					IZkAplicacion a = (IZkAplicacion) TransactionSynchronizationManager	.getResource("obj");
					boolean b = TransactionSynchronizationManager.hasResource("obj");
					boolean igual = app.equals(a);
					if (!igual) {
						if (b) {
							TransactionSynchronizationManager.unbindResource("obj");
						}
						TransactionSynchronizationManager.bindResource("obj",	app);
					}
					servicio.guardar(garantia);
					app.ActualizarListaGarantia();
				}

				List<Garantia> lista = new ArrayList<Garantia>();
				lista.add(garantia);
				String type = "pdf";

				JBarcodeBean barcode = new JBarcodeBean();
				String codigoBarras = garantia.getId()	+ "-"+ garantia.getMaquinaria().getSerialcarroceria().toString();
				barcode.setCode(codigoBarras);
				BufferedImage bufferImagen = barcode.draw(new BufferedImage(300, 50, BufferedImage.TYPE_INT_RGB));
				File file1 = new File(codigoBarras.trim() + ".png");
				ImageIO.write(bufferImagen, "png", file1);

				File file = new File(Index.class.getResource("/").getPath()	+ "../../imagenes/cval.jpg");
				File file3 = new File(Index.class.getResource("/").getPath()+ "../../imagenes/gobierno.jpg");
				File file4 = new File(Index.class.getResource("/").getPath()+ "../../imagenes/MPPAT.jpg");
				File file5 = new File(Index.class.getResource("/").getPath()+ "../../imagenes/logopc.png");

				Map mapa = new HashedMap();
				mapa.put("imgCval", file);
				mapa.put("BarCodigo", file1);
				mapa.put("imgGobierno", file3);
				mapa.put("imgMppat", file4);
				mapa.put("imgLogo", file5);

				String urltotoal = Index.class.getResource("/").getPath()+ "../../" + url; // los sub reportes la ubicacion total
											// desde la raiz
				// mapa.put("SUBREPORT_DIR",Index.class.getResource("/").getPath()+"../../reportes/mantenimiento/1/");
				System.out.println(urltotoal + " ***********************");
				mapa.put("SUBREPORT_DIR", urltotoal);

				JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(	lista);
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
	public void onEvent(Event arg0) throws Exception {
		// TODO Auto-generated method stub

		if(vista.getCargarMaquina() == arg0.getTarget())
		{
			new CatMaquinariaSelect(app,this);
		}
		
		if(vista.getCargarcliente() == arg0.getTarget())
		{
			ContSelecionarCliente selecionarCliente = new ContSelecionarCliente(0, app,this,false,null);
		}
		
		if(vista.getCargarcontacto() == arg0.getTarget())
		{
			ContSelecionarCliente selecionarCliente = new ContSelecionarCliente(0, app,this,true,null);
		}
		
		if(vista.getDetallecontacto() == arg0.getTarget())
		{	System.out.println("aka Toy :-D");
			if(vista.getContacto() != null)
			{	
				new ContCliente(Accion.CONSULTAR,vista.getContacto(), app);
			}
			else{
				app.mostrarError("Carge un Contacto");
			}
		}
		if(vista.getDetallecliente()==arg0.getTarget())
		{	System.out.println("aka Toy :-D"+vista.getPropietario() );
			
			if(vista.getPropietario() != null)
			{	
				System.out.println("aka Toy :-adasdD");
				new ContCliente(Accion.CONSULTAR,vista.getPropietario(), app);
			}
			else
			{
				app.mostrarError("Carge un Propietario");
			}

		}
		if(vista.getDetalleMaquina() == arg0.getTarget())
		{
			if(vista.getMaquinaria() != null)
			{
				new ContMaquinariaExterna(Accion.CONSULTAR,vista.getMaquinaria(),app);
			}
			else{
				app.mostrarError("Carge una Maquinaria ");
			}

		}
		else if (vista.getAceptar() == arg0.getTarget()) 
		{
			validar();
			vista.cargarModelo();
			guardar();
		}

	}

	public void seleccionarPropietario(Cliente cliente) throws InterruptedException
	{
		if (cliente != null)
		{
			String tipo = cliente.getIdentidadLegal().substring(0, 1);
			if (tipo.equals("J") || tipo.equals("G")) 
			{
				int resultado = Messagebox.show("Desea Agregar un Contacto ?","Agregar Contacto", Messagebox.YES | Messagebox.NO,Messagebox.INFORMATION);
				if (resultado == Messagebox.YES) 
				{
					vista.contacto();
				}
			}
			vista.cargarPropietario(cliente);
		}
	}
	
	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		vista.actualizarModelo();
		if(vista.getMaquinaria() == null)
		{
			throw new WrongValueException(vista.getCargarMaquina(), "Seleccione una Maquinaria");
		}
		if (vista.getPropietario() == null) 
		{
			throw new WrongValueException(vista.getCargarcliente(), "Seleccione un Propietario");
		}
		if(vista.getLocalidad().getValue().trim().length() == 0)
		{
			throw new WrongValueException(vista.getLocalidad(), "Ingrese la Localidad de la Maquinaria ");
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

	public void setMaquinaria(MaquinariaExterna maquinariaExterna) {
		// TODO Auto-generated method stub
		vista.cargarMaquinaria(maquinariaExterna);
	}

	public void seleccionarContacto(Cliente cliente) throws InterruptedException {
		// TODO Auto-generated method stub
		if (cliente != null)
		{
			vista.cargarContacto(cliente);
			vista.contacto();
		}
	}

}
