package cpc.demeter.controlador;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Map;





import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Script;

import cpc.demeter.AppDemeter;
import cpc.demeter.vista.UiBanco;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContCrearRespaldoDB implements EventListener {

	private AppDemeter app;
	private UiBanco vista;
	private String host;

	public ContCrearRespaldoDB(int modoOperacion, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super();
		this.app = app;
		this.crearRespaldo();
		try {
			this.comprimir();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.extraerRespaldo();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private synchronized void crearRespaldo() {
		try {

			app.mostrarConfirmacion("iniciando Respaldo");
			@SuppressWarnings("unused")
			Runtime r = Runtime.getRuntime();

			
			String user = (String) SpringUtil.getBean("respaldo");
			String path = (String) SpringUtil.getBean("ruta-comprimida");
			String comando = (String) SpringUtil.getBean("rutacomando");
			String ip = (String) SpringUtil.getBean("ipDB");
			path = path + "db_demeter" + app.getSede().getIdSede() + ".backup";// PostgreSQL
																				// variables
			Desktop desktop = app.getWin().getDesktop();
			String path2 = desktop.getWebApp().getRealPath("/respaldo")
					+ "/db_demeter" + app.getSede().getIdSede() + ".backup";
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
			System.out
					.println("pg_dump --host "
							+ ip
							+ " --port 5432 --username dba --clean --format custom --blobs --verbose --file "
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
			System.out.println(p.getErrorStream());

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

		} catch (Exception e) {
			e.printStackTrace();
			app.mostrarError("Problema al generar respaldo");
		}
	}

	public void comprimir() throws IOException {
		Runtime r = Runtime.getRuntime();
		Integer IdControl = (Integer) SpringUtil.getBean("idControl");
		String path = (String) SpringUtil.getBean("ruta-comprimida");
		path = path + "db_demeter" + app.getSede().getIdSede() + ".backup";
		String path2 = (String) SpringUtil.getBean("ruta-comprimida");
		path2 = path2 + "db_demeter" + app.getSede().getIdSede() + ".7z";
		Desktop desktop = app.getWin().getDesktop();
		String path3 = desktop.getWebApp().getRealPath("/respaldo") + "/db_demeter" + app.getSede().getIdSede() + ".7z";
		String path4 = desktop.getWebApp().getRealPath("/respaldo") + "/db_demeter" + app.getSede().getIdSede() + ".backup";
		Process p2;
		ProcessBuilder pb2 = new ProcessBuilder("7za", "a", "-mhe=on","-pr3sp4ld4d0", path3, path4);
		pb2.start();
		System.out.println(pb2.command());
	}

	public void extraerRespaldo() throws FileNotFoundException {
		Script scrip = new Script();
		scrip.setType("text/javascript");
		StringBuilder cadena = new StringBuilder();
		cadena.append(host);
		cadena.append("/respaldo/");

		

		// String respaldo = (String) SpringUtil.getBean("ruta-respaldo");
		String respaldo = "/home/imedina/Escritorio/";
		scrip.setContent(respaldo);
		// scrip.setContent(cadena.toString());

		Desktop desktop = app.getWin().getDesktop();
		Map b = desktop.getAttributes();
		WebApp aa = desktop.getWebApp();
		String z = desktop.getWebApp().getRealPath("/");
		String zx = desktop.getWebApp().getRealPath("/");
		String z2 = desktop.getWebApp().getRealPath("/respaldo");
		String zx2 = desktop.getWebApp().getRealPath("/respaldo");

		System.out.println(desktop.getCurrentDirectory());

		System.out.println("db_demeter" + app.getSede().getIdSede() + ".7z");

		FileInputStream inStream = new FileInputStream(desktop.getWebApp()
				.getRealPath("/respaldo")
				+ "/"
				+ "db_demeter"
				+ app.getSede().getIdSede() + ".7z");

		java.io.InputStream is = desktop.getWebApp().getResourceAsStream(
				"respaldo/" + "db_demeter" + app.getSede().getIdSede() + ".7z");
		Filedownload.save(inStream, "text/html", "db_demeter"
				+ app.getSede().getIdSede() + ".7z");

		/*
		 * if (is != null) Filedownload.save (is,
		 * "text/html","db_demeter"+app.getSede().getIdSede()+".7z"); else
		 * 
		 * this.app.agregar(scrip);
		 */
	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {

	}

	public void onEvent(Event arg0) throws Exception {

	}

	public AppDemeter getApp() {
		return app;
	}

	public void setApp(AppDemeter app) {
		this.app = app;
	}

	public UiBanco getVista() {
		return vista;
	}

	public void setVista(UiBanco vista) {
		this.vista = vista;
	}

}
