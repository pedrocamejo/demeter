package cpc.demeter.controlador.administrativo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

import org.apache.commons.io.FileUtils;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.ListModelList;

import sun.security.krb5.internal.APOptions;
import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.Index;
import cpc.demeter.vista.administrativo.UIAprobacionExoneracionContrato;
import cpc.modelo.demeter.administrativo.AprobacionExoneracionContrato;
import cpc.modelo.demeter.administrativo.Recibo;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.negocio.demeter.administrativo.NegocioAprobacionExoneracionContrato;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.interfaz.IZkAplicacion;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContAprobacionExoneracionContrato extends
		ContVentanaBase<AprobacionExoneracionContrato> {

	/**
	 * 
	 */
	
	/**
	 * 
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = -17263053974870326L;
	/**
	 * 
	 */
	
	private NegocioAprobacionExoneracionContrato servicio;
	private UIAprobacionExoneracionContrato vista;
	// private Sede sede;
	private AppDemeter app;

	public ContAprobacionExoneracionContrato(int modoOperacion,
			AprobacionExoneracionContrato aprobacion,
			ContCatalogo<AprobacionExoneracionContrato> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido {
		super(modoOperacion);
		this.app = app;

		servicio = NegocioAprobacionExoneracionContrato.getInstance();
		
		setDato(aprobacion);
		if (aprobacion == null || modoAgregar()) {

			aprobacion = new AprobacionExoneracionContrato();
			aprobacion.setFechaRecepcion(new Date());
				
		}
	
		setear(aprobacion, new UIAprobacionExoneracionContrato(
				"Aprobacion de ExoneraCion Contrato (" + Accion.TEXTO[modoOperacion]
						+ ")", 600, modoOperacion), llamador, app);
		vista = (UIAprobacionExoneracionContrato) getVista();
		//vistasolictud.desactivar(modoOperacion);
		/*
		 * vistaCierre.getSede().setValue(sede.getNombre());
		 * vistaCierre.getDetalleDeposito
		 * ().setModelo(servicio.getDepositos(servicio
		 * .getCierreDiario().getFecha()));
		 * vistaCierre.getAsiento().setModelo
		 * (servicio.generarAsientoDiario(servicio.getCierreDiario()));
		 */
		vista.desactivar(modoOperacion,getDato());
	}

	public void eliminar() {

	}

	public void guardar() {
		try {
			getVista().actualizarModelo();
			cargarModelo();
			//getDato().setContrato(vistasolictud.getContrato().getSeleccion());
			servicio.guardar(getDato());
			//crear el archivo y actualizar
			//String ruta = creararchivozipxml(getDato());
			//byte[] bytearchivo = archivoxmlzipTobytes(ruta); 
			//getDato().setArchivoSolicitud(bytearchivo);
			//servicio.guardar(getDato());
			
			

		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void cargarModelo() {
		/*
		 * getDato().setDetalles(vistaFactura.getModeloDetalle());
		 * getDato().setBeneficiario
		 * (vistaFactura.getRazonSocial().getValorObjeto());
		 * getDato().setDireccionBeneficiario
		 * (vistaFactura.getDireccion().getSeleccion());
		 * getDato().setMontoSaldo(getDato().getMontoTotal());
		 */
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {


	}

	public void onEvent(Event event) {
	
		try {
			if (event.getName().equals(Events.ON_CLICK)) {
				if (event.getTarget() instanceof Button ) {
					Button boton = (Button) event.getTarget();
					String nombre = boton.getLabel();
					if 	(nombre.equals("Subir Archivo")) {
						System.gc();
						Media media = Fileupload.get();
						Desktop desktop = app.getWin().getDesktop();
						String rutaO = desktop.getWebApp().getRealPath("/entradas")
								+ "/";
						String rutaD = desktop.getWebApp().getRealPath("/entradas")
								+ "/descomprimidos/";
						System.out.println(rutaO);
						boolean as = SubirArchivos(media, rutaO);
						
						Events.postEvent("onClick", vista.getAceptar(), null);
						
						System.out.println("ok");
					}
					if 	(nombre.equals("Bajar Archivo")){
					//	Row fila =(Row)event.getTarget().getParent();
					//	Row fila3 =(Row)event.getTarget().getParent();
						bajarArchivo();
					}
					if 	(nombre.equals("Aprobar")){
						
						getDato().setAprobado(true);
						
						Events.postEvent("onClick", vista.getAceptar(), null);
						System.out.println("ok");
						
					}
					if 	(nombre.equals("Rechazar")){
						getDato().setAprobado(false);
						
						Events.postEvent("onClick", vista.getAceptar(), null);
						System.out.println("ok");
					}
				} // end if (event.getTarget() instanceof Button )
			}
			if (event.getTarget() == getVista().getAceptar()) {
				validar();
				
				procesarCRUD(event);
			}
		
			
			
		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError(e.getMessage());
		}
	}

	public void actualizarDatos() {

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

try {
	getDato().setFechaAprobacion(new Date());
	servicio.guardar(getDato());
	String ruta = creararchivozipxml(getDato());
	byte[] bytearchivo = archivoxmlzipTobytes(ruta); 
	getDato().setArchivoAprobacion(bytearchivo);
	servicio.guardar(getDato());
	bajarArchivo();
	vista.close();
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	}
	
	
	
	public static boolean saveFile(Media media, String path) {
		boolean uploaded = false;
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			InputStream ins = media.getStreamData();
			in = new BufferedInputStream(ins);

			String fileName = media.getName();
			File arc = new File(path + fileName);
			OutputStream aout = new FileOutputStream(arc);
			out = new BufferedOutputStream(aout);

			byte buffer[] = new byte[1024];
			int ch = in.read(buffer);
			while (ch != -1) {
				out.write(buffer, 0, ch);
				ch = in.read(buffer);
			}
			uploaded = true;
		} catch (IOException ie) {
			throw new RuntimeException(ie);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return uploaded;
	}

	
	public  boolean SubirArchivos(Media media, String path) throws ParseException {
		boolean uploaded = false;
		  try {  
			  
			  InputStream datos = media.getStreamData();
			  byte[] bytes = InputStreamToByteArray(datos);
			  getDato().setArchivoAprobacion(bytes);
			  uploaded=true;
			  File archivosubido = transformarBytesaFile( getDato().getArchivoAprobacion());
			  Desktop desktop = app.getWin().getDesktop();
				String rutadescomprimir = desktop.getWebApp().getRealPath("/entradas")+ "/";
			 String xml = unZipIt(archivosubido.getAbsolutePath(), rutadescomprimir);
			  cargarDatosXml(xml);
		} 
		catch (IOException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		}
		return uploaded;
	}

	public void bajarArchivo() throws IOException, ClassNotFoundException {
		
	
		
	 
	    InputStream  arraydedatos = ByteArrayToInputStreamTo(getDato().getArchivoAprobacion());
	    Desktop desktop = app.getWin().getDesktop();
		String path = desktop.getWebApp().getRealPath("/entradas")+ "/";
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(arraydedatos);
			String fileName = "Aprobacion_"+getDato().getNumeroExoneracion()+".zip";
			File arc = new File(path + fileName);
			OutputStream aout = new FileOutputStream(arc);
			out = new BufferedOutputStream(aout);
			byte buffer[] = new byte[1024];
			int ch = in.read(buffer);
			while (ch != -1) {
				out.write(buffer, 0, ch);
				ch = in.read(buffer);
			}
			System.out.println(arc.isFile());
			System.out.println(arc.exists());
		FileInputStream axu = new FileInputStream(arc);
			Filedownload.save(axu, "text/html", arc.getName()); 
		} catch (IOException ie) {
			throw new RuntimeException(ie);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		//el archivo file sole existe en memoria por lo que hace una  
		/// java.io.FileNotFoundException hay que verificar que este escrito en disco para leerlo
	}

	public static byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream b = new ByteArrayOutputStream();
		ObjectOutputStream o = new ObjectOutputStream(b);
		o.writeObject(obj);
		return b.toByteArray();
	}

	public static Object deserialize(byte[] bytes) throws IOException,
			ClassNotFoundException {
		ByteArrayInputStream b = new ByteArrayInputStream(bytes);
		ObjectInputStream o = new ObjectInputStream(b);
		return o.readObject();
	}

	public static byte[] InputStreamToByteArray(InputStream is) throws IOException{
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[16384];
		while ((nRead = is.read(data, 0, data.length)) != -1) {
		  buffer.write(data, 0, nRead);
		}
		buffer.flush();
		return buffer.toByteArray();
		
	}
	
	public static InputStream ByteArrayToInputStreamTo(byte[] is) throws IOException{
		InputStream myInputStream = new ByteArrayInputStream(is);
		return myInputStream;
	}

	/*public void ActualizarContrato(){
		Contrato contrato = vistasolictud.getContrato().getSeleccion();
		vistasolictud.getContrato().setText(contrato.getStrNroDocumento());
		vistasolictud.getPagador().setValue(contrato.getPagador().getNombres());
		vistasolictud.getCedrif().setValue(contrato.getPagador().getIdentidadLegal());
		vistasolictud.getMonto().setValue(contrato.getMonto().toString());
		vistasolictud.getFechacontrato().setValue(contrato.getFechaString());
		
	} */
	
	
	

public  String creararchivozipxml(AprobacionExoneracionContrato aprobacion) throws IOException{
String xml=	servicio.GenerarxlmAprobacion(aprobacion);

System.out.println(xml);
Desktop desktop = app.getWin().getDesktop();
		String path = desktop.getWebApp().getRealPath("/entradas")+ "/";

String ruta = path;
File archivoXml = new File(ruta+aprobacion.getNumeroExoneracion());
FileUtils.writeStringToFile(archivoXml, xml);

byte[] buffer = new byte[1024];

try{

	FileOutputStream fos = new FileOutputStream(ruta+"Aprovacion_"+aprobacion.getNumeroExoneracion()+".zip");
	ZipOutputStream zos = new ZipOutputStream(fos);
	ZipEntry ze= new ZipEntry("xml.xml");
	zos.putNextEntry(ze);
	FileInputStream in = new FileInputStream(archivoXml);

	int len;
	while ((len = in.read(buffer)) > 0) {
		zos.write(buffer, 0, len);
	}

	in.close();
	zos.closeEntry();

	//remember close it
	zos.close();

	System.out.println("Done");
	System.out.println(ruta+"Aprovacion_"+aprobacion.getNumeroExoneracion()+".zip");
	return ruta+"Aprovacion_"+aprobacion.getNumeroExoneracion()+".zip";

}catch(IOException ex){
   ex.printStackTrace();
}
return ruta;

}
public  byte[] archivoxmlzipTobytes(String ruta) throws IOException{
	File archivozip =new File(ruta);
	   FileInputStream fileInputStream = new FileInputStream(archivozip);
	    byte[] bFile = new byte[(int) archivozip.length()];
		fileInputStream.read(bFile);
	    fileInputStream.close();
	    return bFile;
}

public String unZipIt(String zipFile, String directorioSalida){

    byte[] buffer = new byte[1024];

    try{

   	//create output directory is not exists
   	File folder = new File(directorioSalida);
   	if(!folder.exists()){
   		folder.mkdir();
   	}

   	//get the zip file content
   	ZipInputStream zis =
   		new ZipInputStream(new FileInputStream(zipFile));
   	//get the zipped file list entry
   	ZipEntry ze = zis.getNextEntry();

   	while(ze!=null){

   	   String fileName = ze.getName();
          File newFile = new File(directorioSalida + File.separator + fileName);

          System.out.println("file unzip : "+ newFile.getAbsoluteFile());

           //create all non exists folders
           //else you will hit FileNotFoundException for compressed folder
           new File(newFile.getParent()).mkdirs();

           FileOutputStream fos = new FileOutputStream(newFile);

           int len;
           while ((len = zis.read(buffer)) > 0) {
      		fos.write(buffer, 0, len);
           }

           fos.close();
           return leerStringXmldeArchivo(newFile.getAbsolutePath());
         //  ze = zis.getNextEntry();
   	}

       zis.closeEntry();
   	zis.close();

   	System.out.println("Done");
   	
   }catch(IOException ex){
      ex.printStackTrace();
   }
	return null;
  }

private String leerStringXmldeArchivo(String filePath) throws IOException {
    StringBuffer fileData = new StringBuffer();
    BufferedReader reader = new BufferedReader(
            new FileReader(filePath));
    char[] buf = new char[1024];
    int numRead=0;
    while((numRead=reader.read(buf)) != -1){
        String readData = String.valueOf(buf, 0, numRead);
        fileData.append(readData);
    }
    reader.close();
    return fileData.toString();
}
public File transformarBytesaFile(byte[] byteArray){
	

	
	 
    
	
    Desktop desktop = app.getWin().getDesktop();
	String path = desktop.getWebApp().getRealPath("/entradas")+ "/";
	BufferedInputStream in = null;
	BufferedOutputStream out = null;
	try {
		InputStream arraydedatos = ByteArrayToInputStreamTo(byteArray);
		in = new BufferedInputStream(arraydedatos);
		String fileName = "Sol_"+getDato().getNumeroExoneracion()+".zip";
		File arc = new File(path + fileName);
		OutputStream aout = new FileOutputStream(arc);
		out = new BufferedOutputStream(aout);
		byte buffer[] = new byte[1024];
		int ch = in.read(buffer);
		while (ch != -1) {
			out.write(buffer, 0, ch);
			ch = in.read(buffer);
		}
		System.out.println(arc.isFile());
		System.out.println(arc.exists());
		return arc;
	//FileInputStream axu = new FileInputStream(arc);
	//	Filedownload.save(axu, "text/html", arc.getName()); 
	} catch (IOException ie) {
		throw new RuntimeException(ie);
	} catch (Exception e) {
		throw new RuntimeException(e);
	} finally {
		try {
			if (out != null)
				out.close();
			if (in != null)
				in.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
}

public void cargarDatosXml(String xml) throws ParseException{
 AprobacionExoneracionContrato dato =servicio.leerxlmSolicitud(xml, getDato());
	vista.getNumerocontrato().setValue(dato.getNumeroContrato());
	vista.getNumerosolicitud().setValue(dato.getNumeroExoneracion());
	vista.getPagador().setValue(dato.getPagador());
	vista.getCedrif().setValue(dato.getCedRif());
	vista.getSede().setValue(dato.getSede());
	vista.getMonto().setValue(dato.getStrMontoBase());
	vista.getFechacontrato().setValue(dato.getFechaContrato().toString());
	vista.getFechaSolictud().setValue(dato.getFechaSolicitud().toString());
	vista.getMotivo().setValue(dato.getMotivo());

	if (dato.getDetalleExoneracionContrato() != null)
		vista.getListDetalles().setModel(new ListModelList(dato
				.getDetalleExoneracionContrato()));
};

@SuppressWarnings({ "rawtypes", "unchecked" })
public static void imprimir(AprobacionExoneracionContrato documento, IZkAplicacion app) {
	try {
		HashMap parametros = new HashMap();
		parametros.put("logo",Index.class.getResource("/").getPath()+ "../../imagenes/cintillo_inst.png");
		List<AprobacionExoneracionContrato> aprobaciones = new ArrayList<AprobacionExoneracionContrato>();
		aprobaciones.add(documento);
		//JRDataSource ds = new JRBeanCollectionDataSource(recibos);
		String url = (Index.class.getResource("/").getPath()+ "../../reportes/Aprobacionexoneraciondepago.jasper").trim();
		JasperReport reporte = (JasperReport) JRLoader.loadObject(new File(url));
		byte[] resultado = JasperRunManager.runReportToPdf(reporte,parametros,new JRBeanCollectionDataSource(aprobaciones));
		Filedownload.save(resultado,"application/pdf","Aprobacion_"+documento.getNumeroExoneracion()+".pdf");

	} catch (Exception e) {
		e.printStackTrace();
		app.mostrarError(e.getMessage());
	}
}
}