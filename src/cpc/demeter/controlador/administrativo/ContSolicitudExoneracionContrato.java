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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.zkoss.util.media.Media;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;

import com.sun.org.apache.xpath.internal.operations.Gte;

import cpc.ares.modelo.Accion;
import cpc.demeter.AppDemeter;
import cpc.demeter.vista.administrativo.UISolicitudExoneracionContrato;
import cpc.modelo.demeter.administrativo.ArchivoContrato;
import cpc.modelo.demeter.administrativo.Contrato;
import cpc.modelo.demeter.administrativo.SolicitudExoneracionContrato;
import cpc.modelo.excepcion.ExcDatosNoValido;
import cpc.negocio.demeter.administrativo.NegocioSolicitudExoneracion;
import cpc.zk.componente.controlador.ContCatalogo;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContSolicitudExoneracionContrato extends
		ContVentanaBase<SolicitudExoneracionContrato> {

	/**
	 * 
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8153373180930815569L;
	/**
	 * 
	 */
	
	private NegocioSolicitudExoneracion servicio;
	private UISolicitudExoneracionContrato vistasolictud;
	// private Sede sede;
	private AppDemeter app;

	public ContSolicitudExoneracionContrato(int modoOperacion,
			SolicitudExoneracionContrato solicitud,
			ContCatalogo<SolicitudExoneracionContrato> llamador, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida, ExcDatosNoValido {
		super(modoOperacion);
		this.app = app;

		servicio = NegocioSolicitudExoneracion.getInstance();
		List<Contrato> contratos=new ArrayList<Contrato>();
		setDato(solicitud);
		if (solicitud == null || modoAgregar()) {

			solicitud = new SolicitudExoneracionContrato();
			solicitud.setFechaSolicitud(new Date());
			contratos=servicio.getContratosPorExonerar();
			
			
		}
		/*
		 * else{ servicio.setAprobacionDescuento(aprobacionDescuento) ;
		 * pagos = (List<UnidadAdministrativa>)
		 * aprobacionDescuento.getUnidadAdministrativa(); }
		 */

		// lo veo raro
		setear(solicitud, new UISolicitudExoneracionContrato(
				"Solicitud ExoneraCion Contrato (" + Accion.TEXTO[modoOperacion]
						+ ")", 800, modoOperacion,contratos), llamador, app);
		vistasolictud = (UISolicitudExoneracionContrato) getVista();
		//vistasolictud.desactivar(modoOperacion);
		/*
		 * vistaCierre.getSede().setValue(sede.getNombre());
		 * vistaCierre.getDetalleDeposito
		 * ().setModelo(servicio.getDepositos(servicio
		 * .getCierreDiario().getFecha()));
		 * vistaCierre.getAsiento().setModelo
		 * (servicio.generarAsientoDiario(servicio.getCierreDiario()));
		 */
		vistasolictud.desactivar(modoOperacion,solicitud);
	}

	public void eliminar() {

	}

	public void guardar() {
		try {
			getVista().actualizarModelo();
			cargarModelo();
			getDato().setContrato(vistasolictud.getContrato().getSeleccion());
			getDato().setMotivo(vistasolictud.getMotivo().getValue());
			servicio.guardar(getDato());
			//crear el archivo y actualizar
			String ruta = creararchivozipxml(getDato());
			byte[] bytearchivo = archivoxmlzipTobytes(ruta); 
			getDato().setArchivoSolicitud(bytearchivo);
			servicio.guardar(getDato());
			bajarArchivo();
			Messagebox.show("Debe Enviar Este Archivo A Central", "Advertencia",
					Messagebox.OK, Messagebox.INFORMATION);
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
						Events.postEvent("onClick", vistasolictud.getAceptar(), null);
						System.out.println("ok");
					}
					if 	(nombre.equals("Bajar Archivo")){
					//	Row fila =(Row)event.getTarget().getParent();
					//	Row fila3 =(Row)event.getTarget().getParent();
						bajarArchivo();
					}
				} // end if (event.getTarget() instanceof Button )
			}
			if (event.getTarget() == getVista().getAceptar()) {
				validar();
				
				

				procesarCRUD(event);
			}
			if(event.getName().equals(CompBuscar.ON_SELECCIONO)){
				if(event.getTarget().equals(vistasolictud.getContrato()))
					ActualizarContrato();
				
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
	servicio.guardar(getDato());
	
	Messagebox.show("La Solicitud ha cambiado a el estado de: "+getDato().getEstado(), "Advertencia",
			Messagebox.OK, Messagebox.INFORMATION);
	vistasolictud.close();
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

	//se rediceño estaba guardando la ruta a el archivo y no el archivo
	public  boolean SubirArchivos(Media media, String path) throws InterruptedException {
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
			  SolicitudExoneracionContrato solicitud =servicio.leerxlmAprobacion(xml, getDato());
		} 
		catch (IOException e1) {
			e1.printStackTrace();
			throw new RuntimeException(e1);
		} catch (WrongValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			 Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 Messagebox.show(e.getMessage(), "Error", Messagebox.OK, Messagebox.ERROR);
			e.printStackTrace();
		}
		return uploaded;
	}

	public void bajarArchivo() throws IOException, ClassNotFoundException {
		
	
		
	 
	    InputStream  arraydedatos = ByteArrayToInputStreamTo(getDato().getArchivoSolicitud());
	    Desktop desktop = app.getWin().getDesktop();
		String path = desktop.getWebApp().getRealPath("/entradas")+ "/";
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			in = new BufferedInputStream(arraydedatos);
			String fileName = "SolicitudExoneracion_"+getDato().getNroControl()+".zip";
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

	public void ActualizarContrato(){
		Contrato contrato = vistasolictud.getContrato().getSeleccion();
		vistasolictud.getContrato().setText(contrato.getStrNroDocumento());
		vistasolictud.getPagador().setValue(contrato.getPagador().getNombres());
		vistasolictud.getCedrif().setValue(contrato.getPagador().getIdentidadLegal());
		vistasolictud.getMonto().setValue(contrato.getMonto().toString());
		vistasolictud.getFechacontrato().setValue(contrato.getFechaString());
		vistasolictud.getListDetalles().setModel(new ListModelList(contrato.getDetallesContrato()));
		
	} 
	
	
	

public  String creararchivozipxml(SolicitudExoneracionContrato solictud) throws IOException{
String xml=	NegocioSolicitudExoneracion.getInstance().GenerarxlmSolicitud(solictud);

System.out.println(xml);
Desktop desktop = app.getWin().getDesktop();
		String path = desktop.getWebApp().getRealPath("/entradas")+ "/";

String ruta = path;
File archivoXml = new File(ruta+solictud.getNroControl());
FileUtils.writeStringToFile(archivoXml, xml);

byte[] buffer = new byte[1024];

try{

	FileOutputStream fos = new FileOutputStream(ruta+"Sol_"+solictud.getNroControl()+".zip");
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
	System.out.println(ruta+"Sol_"+solictud.getNroControl()+".zip");
	return ruta+"Sol_"+solictud.getNroControl()+".zip";

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
		String fileName = "Aprobacion_"+getDato().getNroControl()+".zip";
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
}
