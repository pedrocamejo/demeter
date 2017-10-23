package cpc.demeter.controlador;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
/*
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
*/


import org.zkoss.util.WaitLock;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zkplus.spring.SpringUtil;

import cpc.ares.modelo.Accion;
import cpc.ares.modelo.Usuario;
import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.ContExportaciones;
import cpc.demeter.vista.UiExportar;
import cpc.modelo.demeter.sincronizacion.Exportar;
import cpc.modelo.demeter.sincronizacion.SedeDemeter;
import cpc.negocio.demeter.NegocioExportar;
import cpc.persistencia.demeter.PerExportar;
import cpc.persistencia.demeter.PerImportar;
import cpc.persistencia.demeter.PerSedeDemeter;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContExportar  extends  ContVentanaBase<Exportar> {

	private NegocioExportar servicio = NegocioExportar.getInstance();
	private AppDemeter 		app;
	private UiExportar 		vista;
	private String 	 	    urlPadre = "localhost"; // http:localhost:8080/demeterCentral/ XD 
	private String sedeDemeter;
	private Client   		client;
	private WebTarget 		target;
	
	public ContExportar(int modoOperacion, Exportar exportar, ContExportaciones llamador, AppDemeter app) throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;
		try {

			if (exportar == null || modoAgregar())
			{
				exportar = new Exportar();
			}
			
			urlPadre = this.ipPadre();
		///	client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
			target = client.target(urlPadre).path("rest/sincronizaciondb");
		//	JOptionPane.showMessageDialog(null, Desktop.getDesktop(), "antes de Conexion",JOptionPane.WARNING_MESSAGE);
			sedeDemeter=  Conexion(target);

		//	JOptionPane.showMessageDialog(null, Desktop.getDesktop(),"despues de Conexion",JOptionPane.WARNING_MESSAGE);
			// IP del Padre  
			setear(exportar, new UiExportar("Exportar  (" + Accion.TEXTO[modoOperacion]+ ")", 550), llamador, app);
			
			vista = ((UiExportar) getVista());
			vista.desactivar(modoOperacion);

		} 
		catch (ExcDatosInvalidos e)
		{
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.app.mostrarError("NO se Pudo Establecer la Conexion con el Servidor Padre ");
		}
	}
 

	@Override
	public void onEvent(Event arg0) throws Exception{  
		// TODO Auto-generated method stub
		
		this.procesarCRUD(arg0);
		
	}

	@Override
	public void guardar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		///Generar el Archivo alguna clase de deb generar de hacer esto si ella me da la direccion del archivo .zip Genial :-D 
		// string urlfile = Object.generarExportar()

	/*	Exportar exportar = new Exportar();
		exportar.setCedula("123445");
		exportar.setId(12);
		exportar.setMd5("MD5");
		exportar.setSede(new SedeDemeter());*/
	//	JOptionPane.showMessageDialog(null, Desktop.getDesktop(), "antes de crear exportar",JOptionPane.WARNING_MESSAGE);
	
	
	Exportar exportar = servicio.CrearExportar( app.getUsuario().getCedula(),  "md5");

	//JOptionPane.showMessageDialog(null, Desktop.getDesktop(), "Despues de crearexportar",JOptionPane.WARNING_MESSAGE);
		try {
		
			File file=	servicio.CrearArchivoExportar(exportar,crearruta(exportar));
//			File file =new File("/home/erivas/OBJETO");
			if(!file.isFile())
			{
				 throw new Exception("File Not Found "+ file.getAbsolutePath());
			} 
			enviarArchivo(this.Sede(), file);
			//JOptionPane.showMessageDialog(null, Desktop.getDesktop(),"Despues de OK",JOptionPane.WARNING_MESSAGE);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	//sede que esta enviando 
	private void enviarArchivo(SedeDemeter sedeDemeter,File file) throws Exception {
		// TODO Auto-generated method stub
		/*
		FileDataBodyPart filePart  = new FileDataBodyPart("archivo",file);
		
		FormDataMultiPart formMultipart = new FormDataMultiPart();
		
		
	formMultipart.field("sede",sedeDemeter,MediaType.TEXT_XML_TYPE);
	formMultipart.field("usuario",app.getUsuario().getNombreIdentidad());
	formMultipart.field("clave",app.getUsuario().getContrasena());
	formMultipart.bodyPart(filePart);
System.out.println(	formMultipart.getMediaType());

		Response response = target.request().post(Entity.entity(formMultipart,formMultipart.getMediaType()));
 Object retorno	=	response.readEntity(FormDataMultiPart.class );
 System.out.println(response.getEntity().getClass());
 
 
	List lista =   ((FormDataMultiPart) retorno).getFields("archivo");
	
	FormDataBodyPart parteretorno	 =  (FormDataBodyPart) lista.get(0);
	
	
	File fileretorno	 = parteretorno.getEntityAs(File.class);
	
	ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
			fileretorno	));
	Exportar exportarretorno	 = (Exportar) ois.readObject();
	//target.queryParam("formMultipart", formMultipart);
	//Invocation.Builder invocationBuilder = target.request(formMultipart.getMediaType());
	//Object response = invocationBuilder.get();
//	Object response = target.request(). post(Entity.entity(formMultipart,formMultipart.getMediaType()));
	
//		JOptionPane.showMessageDialog(null, Desktop.getDesktop(), "despues de respuesta",JOptionPane.WARNING_MESSAGE);
		//solo para hacer test
		Object objeto = new Object();
		objeto = response;
		Response aux = ((Response)response);
		
		if (((Response)response).getStatus() != 200)
		{
				//el archivo esta genedao solo hay que pasarlo jejeje 
			app.mostrarError("Error de Transferencia  ");
			
		}else{
		System.out.println("ss");
		servicio.getPerExportar().guardaroupdate(exportarretorno, exportarretorno.getId());*/
	/*	Object objetc = ((Response) response).getEntity();
		
			FormDataMultiPart multipart = (FormDataMultiPart) objeto;
			List lista =    multipart .getFields("archivo");
			
			FormDataBodyPart parte =  (FormDataBodyPart) lista.get(0);
			
			File file3 = parte.getEntityAs(File.class);
			
			ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream(
					file3));
			Exportar exportar3 = (Exportar) ois2.readObject();
			System.out.println(exportar3);
			JOptionPane.showMessageDialog(null, Desktop.getDesktop(),
					 "aqui actualizo exportar",JOptionPane.WARNING_MESSAGE);
			*/
		}
		
	  /*String rutarespaldo = traerRespaldo(exportarretorno);
				
		ejecutarrespaldo(rutarespaldo);
		
				
		vista.close();
	} 
	*/
	
	@Override
	public void eliminar() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub
		
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
	
	
	public String Conexion (WebTarget target) throws Exception
	{
		
		Response response = target.path("Sede").request().get();
		String sedeDemeter  = null;
	 
		if(response.getStatus() != 200)
		{
			throw new Exception("NO SE Pudo EStablecer Cenxion con el Servidor ");
		}
		else
		{
			
		//	sedeDemeter = response.readEntity(SedeDemeter.class);
			sedeDemeter = response.readEntity(String.class);
		}
		
		return null;
	}
	
	public SedeDemeter Sede()//sede actual 
	{
		String ipsede = (String) SpringUtil.getBean("idsedesincronizar");
		SedeDemeter sede = servicio.getSede(ipsede);
		return sede;
		
	}
	
	public String ipPadre()
	{
		String ipsede = (String) SpringUtil.getBean("iPsedePadre");
		String url = new String("http://"+ipsede+":8080/demeterSedes/");
		return url;
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
 	 		
	
 	public String crearrutaBD(String id )
 	{
 		// El patron seria /home/sincronizacion/mes/idexportrar
 		String ruta = new String("/home/respaldoI/");
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
 	 		System.out.println(ruta+"i"+id);
 	return ruta+"i"+id+".backup"; 		
 	}
 	 		

 	public void ejecutarrespaldo(String ruta){
 		try {
 			
 		SedeDemeter sedeactual = servicio.getSedeActual();
 		eliminarConecciones();
 		eliminarBD();
 		crearBD();
 		
 		app.mostrarConfirmacion("iniciando Respaldo");
		@SuppressWarnings("unused")
		Runtime r = Runtime.getRuntime();

		
		String user = (String) SpringUtil.getBean("respaldo");
		String path = (String) SpringUtil.getBean("ruta-comprimida");
		String comando = (String) SpringUtil.getBean("rutacomando");
		String ip = (String) SpringUtil.getBean("ipDB");
	
		
		String path2 = ruta;
		String dbase = "db_demeter";
		@SuppressWarnings("unused")
		Process p;
		ProcessBuilder pb;
		r = Runtime.getRuntime();
		
		System.out
				.println("/usr/bin/pg_restore --host "+ip+" --port 5432 --username dba --dbname db_demeter --role dba --password "
						+ " --verbose "+path2);
		p = r.getRuntime()
				.exec("/usr/bin/pg_restore --host "+ip+" --port 5432 --username dba --dbname db_demeter --role dba --password "
						+ " --verbose "+path2);

	
		System.out.println(p.getErrorStream());


		BufferedReader stdInput = new BufferedReader(new InputStreamReader(
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
		servicio.ActualizarSede(sedeactual);
	} catch (Exception e) {
		e.printStackTrace();
		app.mostrarError("Problema al generar respaldo");
	}
 	}
 	
 	public void eliminarBD() throws IOException{
 		@SuppressWarnings("unused")
		Runtime r = Runtime.getRuntime();

		
		
		org.zkoss.zk.ui.Desktop desktop = app.getWin().getDesktop();
		String ruta = desktop.getWebApp().getRealPath("/WEB-INF")+"/eliminarbd.sql";
		
		String ip = (String) SpringUtil.getBean("ipDB");
		String comando="\"DROP DATABASE db_demeter;\"";
		
		String path2 = ruta;
		String dbase = "db_demeter";
		@SuppressWarnings("unused")
		Process p;
		ProcessBuilder pb;
		r = Runtime.getRuntime();
		
		System.out
				.println("/usr/bin/psql --host "+ip+" --port 5432 --username dba --dbname postgres  --password "
						+ " --file"+path2);
		p = r.getRuntime()
				.exec("/usr/bin/psql --host "+ip+" --port 5432 --username dba --dbname postgres  --password "
						+ " --file "+path2);

	
		System.out.println(p.getErrorStream());


		BufferedReader stdInput = new BufferedReader(new InputStreamReader(
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
 		
 	}
 	
 	public void crearBD() throws IOException{
 		@SuppressWarnings("unused")
		Runtime r = Runtime.getRuntime();

		
		
		org.zkoss.zk.ui.Desktop desktop = app.getWin().getDesktop();
		String ruta = desktop.getWebApp().getRealPath("/WEB-INF")+"/creardb.sql";
		
		String ip = (String) SpringUtil.getBean("ipDB");
		String comando="\""
				+ "CREATE DATABASE db_demeter  WITH OWNER = postgres        ENCODING = 'UTF8'        TABLESPACE = pg_default        LC_COLLATE = 'es_VE.UTF-8'        LC_CTYPE = 'es_VE.UTF-8'        CONNECTION LIMIT = -1;"

				+ "\"";
		
		String path2 = ruta;
		String dbase = "db_demeter";
		@SuppressWarnings("unused")
		Process p;
		ProcessBuilder pb;
		r = Runtime.getRuntime();
		
		System.out
				.println("/usr/bin/psql --host "+ip+" --port 5432 --username dba --dbname postgres  --password "
						+ " --file "+path2);
		p = r.getRuntime()
				.exec("/usr/bin/psql --host "+ip+" --port 5432 --username dba --dbname postgres  --password "
						+ " --file "+path2);

	
		System.out.println(p.getErrorStream());


		BufferedReader stdInput = new BufferedReader(new InputStreamReader(
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
 		
 	}
 	
 	
 	public void eliminarConecciones() throws IOException{
 		@SuppressWarnings("unused")
		Runtime r = Runtime.getRuntime();

		
		
		org.zkoss.zk.ui.Desktop desktop = app.getWin().getDesktop();
		String ruta = desktop.getWebApp().getRealPath("/WEB-INF")+"/eliminarconecciones.sql";
		
		String ip = (String) SpringUtil.getBean("ipDB");
	
		
		String path2 = ruta;
		String dbase = "db_demeter";
		@SuppressWarnings("unused")
		Process p;
		ProcessBuilder pb;
		r = Runtime.getRuntime();
		String Comando ="-c \"SELECT pg_terminate_backend(procpid) from pg_stat_activity where datname ='db_demeter';\"";
		System.out
				.println("/usr/bin/psql --host "+ip+" --port 5432 --username dba --dbname postgres  --password "
						+ "--file "+ path2);
		p = r.getRuntime()
				.exec("/usr/bin/psql --host "+ip+" --port 5432 --username dba --dbname postgres  --password "
						+"--file "+path2);

	
		System.out.println(p.getErrorStream());


		BufferedReader stdInput = new BufferedReader(new InputStreamReader(
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
		// Leemos los errores si los hubiera
		System.out
				.println("Ésta es la salida standard de error del comando (si la hay):\n");
		while ((s = stdError.readLine()) != null) {
			System.out.println(s);
		}
 		
 	}
 	
 	public String traerRespaldo(Exportar exportarretorno){
 	
 		/*Response response2 = target.path("Respaldo").request().post(Entity.entity(exportarretorno.getId().toString(),MediaType.TEXT_HTML));
		FormDataMultiPart retorno2= response2.readEntity(FormDataMultiPart.class );;
		List lista2 =   ((FormDataMultiPart) retorno2).getFields("archivo");
		
		FormDataBodyPart parteretorno2	 =  (FormDataBodyPart) lista2.get(0);
		
		File fileretorno2	 = parteretorno2.getEntityAs(File.class);
		System.out.println(fileretorno2.length());
		File basededatos = new File(crearrutaBD(exportarretorno.getId().toString()));
		
				
				
				
		FileInputStream entrada=null;
        FileOutputStream salida=null;

        try  {
        	System.out.println(fileretorno2.getAbsolutePath());
        	System.out.println(basededatos.getAbsolutePath());
            entrada= new FileInputStream(fileretorno2.getAbsolutePath()) ;
            salida=new FileOutputStream(basededatos.getAbsolutePath());
            int c;
            while((c=entrada.read())!=-1){
                salida.write(c);
            }
       }catch (IOException ex) {
            System.out.println(ex);
       }finally{
//cerrar los flujos de datos
            if(entrada!=null){
                try{
                    entrada.close();
                }catch(IOException ex){}
            }
            if(salida!=null){
                try{
                    salida.close();
                }catch(IOException ex){}
            }
            System.out.println("el bloque finally siempre se ejecuta");
				
				
       }*/
        return "";// (basededatos.getAbsolutePath());
 	}
}
