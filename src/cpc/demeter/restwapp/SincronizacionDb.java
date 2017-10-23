package cpc.demeter.restwapp;
 
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

/*
import org.glassfish.jersey.media.multipart.BodyPart;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.server.Uri;
*/
 


@Path(value="sincronizaciondb/")
public class SincronizacionDb {
	/*
	private SedeDemeter sede;
	private NegocioExportar negocioExportar = NegocioExportar.getInstance();
 
	
	@GET
	@Path("/Sede/")
	//@Produces( MediaType.TEXT_XML)
	@Produces( MediaType.TEXT_HTML )
	public String getSede()
	//public String  getSede()
	{
 		String idsede = (String) SpringUtil.getBean("idsede");

		SedeDemeter sede = negocioExportar.getSede(idsede);
		//validar si es Hija y Vaina :-D si es asi darle permisos :-D 
		if(sede == null)
		{
			throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity("Sede NO Encontrada ").type(MediaType.TEXT_PLAIN).build());
		}
		return sede.toString();
	}
	
	
 	@SuppressWarnings({ "rawtypes", "unused" })
	@POST
	//@Produces(MediaType.MULTIPART_FORM_DATA  )
//@Produces("multipart/mixed")
	@Produces("multipart/form-data")
 //	@Consumes("multipart/form-data")
	public  Response post(  FormDataMultiPart multipart) throws IOException, ClassNotFoundException 
	{
		
//		Map mapa=  multipart.getFields() ;
	
		
	//	List lista2 =   multipart.getBodyParts() ;
		
		//BodyPart flecha = multipart.getBodyParts().get(0);
	//Object flechazho = flecha.getEntity(); 
		
 		List lista =    multipart.getFields("archivo");
		
		FormDataBodyPart parte =  (FormDataBodyPart) lista.get(0);
		
		File file = parte.getEntityAs(File.class);
		
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
				file));
		Exportar exportar = (Exportar) ois.readObject();
		System.out.println(exportar);
		
		SedeDemeter sede =  multipart.getField("sede").getValueAs(SedeDemeter.class);
		String usuario = multipart.getField("usuario").getValueAs(String.class);
		String clave = multipart.getField("clave").getValueAs(String.class);
		

	//	guardarArchivo(file, sede);
		
		
		
		//validar la sede y guardar el Archivo 
	//	JOptionPane.showMessageDialog(null, Desktop.getDesktop(),		 "Antes de importar",JOptionPane.WARNING_MESSAGE);
		//return Response.ok().build();
    //post(Entity.entity(formMultipart,formMultipart.getMediaType()))
		
		
		//se envia el archivo exportar actualizado
		
		Exportar exportar2=null;
		File file2 = null ;
		try {
			exportar2 = new PerImportar().importarData(exportar);
			 file2 = new NegocioExportar().CrearArchivoExportar(exportar2,crearruta(exportar2));
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExcFiltroExcepcion e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	FileDataBodyPart filePart2  = new FileDataBodyPart("archivo",file2);
		
		FormDataMultiPart formMultipart2 = new FormDataMultiPart();
		SedeDemeter sededestino = sede;
		
	formMultipart2.field("sede",sededestino,MediaType.TEXT_XML_TYPE);
	formMultipart2.bodyPart(filePart2);
		
	System.out.println(formMultipart2.getMediaType());

	
	//Exportar exportactualizado = exportar2;
//	GenericEntity<Exportar> entity = new GenericEntity<Exportar>(exportar2) {};
	GenericEntity<FormDataMultiPart> entity = new GenericEntity<FormDataMultiPart>(formMultipart2) {};
	//Response response = Response.ok(entity).build();
	
	
	
	 //Response	d =Response.build();
	 
	//System.out.println(d.getMediaType());
	//Response.ok(formMultipart2).build();
	return Response.status(Status.OK).entity(formMultipart2).type(formMultipart2.getMediaType()).build();
			//ok(Entity.entity(formMultipart2,formMultipart2.getMediaType())).build();
	
	//return formMultipart2;
	}
	 
 	
 	
 	
 	
 	@Path("/Respaldo/")
 	@SuppressWarnings({ "rawtypes", "unused" })
	@POST
	@Produces("multipart/form-data")
	public  Response post2(  String id) throws IOException, ClassNotFoundException 
	{
		

		String ruta = crearRespaldoDB(id);
		File file2 = new File(ruta) ;
	
		
	FileDataBodyPart filePart2  = new FileDataBodyPart("archivo",file2);
	FormDataMultiPart formMultipart2 = new FormDataMultiPart();
	SedeDemeter sededestino = sede;
		
	//formMultipart2.field("sede",sededestino,MediaType.TEXT_XML_TYPE);
	formMultipart2.bodyPart(filePart2);
		
	System.out.println(formMultipart2.getMediaType());

	
	//Exportar exportactualizado = exportar2;
//	GenericEntity<Exportar> entity = new GenericEntity<Exportar>(exportar2) {};
	GenericEntity<FormDataMultiPart> entity = new GenericEntity<FormDataMultiPart>(formMultipart2) {};
	//Response response = Response.ok(entity).build();
	
	
	
	 //Response	d =Response.build();
	 
	//System.out.println(d.getMediaType());
	//Response.ok(formMultipart2).build();
	return Response.status(Status.OK).entity(formMultipart2).type(formMultipart2.getMediaType()).build();
			//ok(Entity.entity(formMultipart2,formMultipart2.getMediaType())).build();
	
	//return formMultipart2;
	}
 	
 	void guardarArchivo(File file,SedeDemeter sede ) throws IOException
 	{
 		//Crear Carpeta 
 		String ruta = new String("/home/rchirinos/DemeterFile/Importaciones/");
 		ruta = ruta.concat(sede.getIdSede());
 		File dir = new File(ruta.trim());
 		
 		if (!dir.isDirectory())
 		{
 			dir.mkdirs();
 		}
 		
 		byte[] bfile = new byte[(int) file.length()];
		FileInputStream fileinput = new FileInputStream(file);
		fileinput.read(bfile);
		fileinput.close();
		
		File file2 = new File(ruta+"/archivo.zip");
		
		file2.createNewFile();
		
		FileOutputStream fileout = new FileOutputStream(file2);
		fileout.write(bfile);
		fileout.flush();
		fileout.close();
		
 	}
	public String crearruta(Exportar exportar )
 	{
 		// El patron seria /home/sincronizacion/mes/idexportrar
 		String ruta = new String("/home/sincronizacion/");
 	 	Calendar calendar = Calendar.getInstance();
 	 	int	mes = calendar.get(Calendar.MONTH) ;
 	 	int	año = calendar.get(Calendar.YEAR) ;
 	 	System.out.println(ruta+mes+"_"+año);
 	 	 ruta=(ruta+año+"/"+mes+"/");
 	 	 File carpeta= new File(ruta);
 	 	//	ruta = ruta.concat(sede.getIdSede());
 	 		File dir = new File(ruta.trim());
 	 		
 	 		if (!dir.exists()) { System.out.println(" escribimos algo si no existe"); 
 	 		if (!dir.isDirectory())
 	 		{
 	 		boolean d = dir.mkdirs();
 	 		System.out.println(d);
 	 		}
 	 		}
 	 		System.out.println(ruta+exportar.getId());
 	return ruta+exportar.getId(); 		
 	}
	
	public String crearRutaRespaldo(String id )
 	{
 		// El patron seria /home/sincronizacion/mes/idexportrar
 		String ruta = new String("/home/respaldo/");
 	 	Calendar calendar = Calendar.getInstance();
 	 	int	mes = calendar.get(Calendar.MONTH) ;
 	 	int	año = calendar.get(Calendar.YEAR) ;
 	 	System.out.println(ruta+mes+"_"+año);
 	 	 ruta=(ruta+año+"/"+mes+"/");
 	 	 File carpeta= new File(ruta);
 	 	//	ruta = ruta.concat(sede.getIdSede());
 	 		File dir = new File(ruta.trim());
 	 		
 	 		if (!dir.exists()) { System.out.println(" escribimos algo si no existe"); 
 	 		if (!dir.isDirectory())
 	 		{
 	 		boolean d = dir.mkdirs();
 	 		System.out.println(d);
 	 		}
 	 		}
 	 		System.out.println(ruta+"e"+id);
 	return ruta+"e"+id; 		
 	}
	
	public String crearRespaldoDB(String id){
		try {

			
			@SuppressWarnings("unused")
			Runtime r = Runtime.getRuntime();

			String user = (String) SpringUtil.getBean("respaldo");
			String path = (String) SpringUtil.getBean("ruta-comprimida");
			String comando = (String) SpringUtil.getBean("rutacomando");
			String ip = (String) SpringUtil.getBean("ipDB");
			//path = path + "db_demeter" + app.getSede().getIdSede() + ".backup";// PostgreSQL
																				// variables
			//Desktop desktop = app.getWin().getDesktop();
			//String path2 = desktop.getWebApp().getRealPath("/respaldo")+ "/db_demeter" + app.getSede().getIdSede() + ".backup";
			String path2 = crearRutaRespaldo(id)+".backup";
			String dbase = "db_demeter";
			@SuppressWarnings("unused")
			Process p;
			ProcessBuilder pb;
			r = Runtime.getRuntime();
			// String ruta =
			// "/home/imedina/Escritorio/db_demetertrujillo.backup";

			// este es el bueno
			// pb = new ProcessBuilder(comando,"--host", ip, "--port", "5432",
			// "--username" , "dba",
			// "--format","plain","--blobs","--verbose","--file",path,"db_demeter");
			// pb.environment().put("PGPASSWORD", "d84@54");
			// processbuilderpb = new ProcessBuilder(comando,"--host",
			// "localhost", "--port", "5432", "--username" , "postgres",
			// "--format","plain","--blobs","--verbose","--file",path,"db_demeter");
			// pb = new ProcessBuilder("/usr/bin/pg_dump","--host", "localhost",
			// "--port", "5432", "--username" , "postgres",
			// "--format","plain","--blobs","--verbose","--file",ruta,"db_demeter_central2");
			// PGUSER=dba PGPASSWORD=d84@54
			// usr/bin/pg_dump --host localhost --port 5432 --username postgres
			// --format custom --blobs --verbose --file
			// "/home/imedina/Escritorio/db_demetertrujillo.backup" db_ares
			// String[] command = {comando};

			// pb = new ProcessBuilder("/bin/sh",comando,"/.homer.sh");
			// pb.environment().put("PGPASSWORD", "d84@54");
			// pb = new ProcessBuilder("/usr/bin/pg_dump", "-v", "-F", "c",
			// "-U", user, "-f",path, dbase);
			// pb = new ProcessBuilder("/bin/pg_dump", "-v", "-F", "c", "-U",
			// user, "-f",path, dbase);

			// -v -F c -U postgres db_demeter | gzip >
			// /var/www/html/respaldo/demeter.backup.gz//
			// pb = new ProcessBuilder("/bin/pg_dump", "-i", "-h", host, "-p",
			// "5432","-U", user,"-W",password, "-F","c","-b", "-v","-f",path,
			// dbase);
			// pb.environment().put("PGPASSWORD", password);
			// pb.redirectErrorStream(true);
			// System.out.println(pb.command());
			// p=pb.start();

			// p=r.getRuntime().exec("/bin/sh /opt/tomcat/webapps/demeterSedes/respaldo/respaldo.sh /.homer.sh");
			/*System.out
					.println("pg_dump --host "
							+ ip
							+ " --port 5432 --username dba --format plain --blobs --verbose --file "
							+ path2 + " db_demeter");*/
/*			System.out
			.println("pg_dump --host "
					+ ip
					+ " --port 5432 --username dba --password --format custom --clean --blobs --verbose --file "
					+ path2 + " db_demeter");
			p = r.getRuntime()
					.exec("pg_dump --host "
							+ ip
							+ " --port 5432 --username dba --password --format custom --clean --blobs --verbose --file "
							+ path2 + " db_demeter");

			/*
			 * Desktop desktop = app.getWin().getDesktop(); Object a =
			 * desktop.getWebApp(); java.io.InputStream is =
			 * desktop.getWebApp().
			 * getResourceAsStream("respaldo/"+"db_demeter"+app
			 * .getSede().getIdSede()+".backup");
			 */
	//		System.out.println(p.getErrorStream());

			// pb2.wait(50000);

			// Runtime.getRuntime().exec();
			/*
			 * path= path+"db_demeter"+app.getSede().getIdSede()+".backup";
			 * String path2 = (String) SpringUtil.getBean("ruta-comprimida");
			 * path2=path2+"db_demeter"+app.getSede().getIdSede()+".7z";
			 * ProcessBuilder pb2 = new
			 * ProcessBuilder("7za","a","-mhe=on","-pr3sp4ld4d0",path2,path);
			 */

			// 7z a -mhe=on -pmi_contraseña
			// /home/imedina/Escritorio/flossblog.7z
			// /home/imedina/Escritorio/db_demetertrujillo.backup

			// comprimir(path2, path2);
			// app.mostrarConfirmacion("Respaldo Realizado");

	/*		BufferedReader stdInput = new BufferedReader(new InputStreamReader(
					p.getInputStream()));

			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
					p.getOutputStream()));

			BufferedReader stdError = new BufferedReader(new InputStreamReader(
					p.getErrorStream()));

			out.write("d84@54");
			out.newLine();
			out.flush();
			out.close();
			// Leemos la salida del comando
			System.out.println("Ésta es la salida standard del comando:\n");
			String s;
			// while ((s = stdInput.readLine()) != null) {
			// System.out.println(s);
			// }

			// Leemos los errores si los hubiera
			System.out
					.println("Ésta es la salida standard de error del comando (si la hay):\n");
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
			return path2;
		} catch (Exception e) {
			e.printStackTrace();
	//		app.mostrarError("Problema al generar respaldo");
		}
		return null;
	}
	*/
}
