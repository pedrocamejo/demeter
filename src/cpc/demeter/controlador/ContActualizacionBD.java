package cpc.demeter.controlador;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Messagebox;

import cpc.demeter.AppDemeter;
import cpc.demeter.vista.UiBanco;
import cpc.modelo.demeter.mantenimiento.EntradaArticulo;
import cpc.persistencia.DaoGenerico;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContActualizacionBD implements EventListener {

	private AppDemeter app;
	private UiBanco vista;

	public ContActualizacionBD(int modoOperacion, AppDemeter app)
			throws ExcDatosInvalidos, ExcSeleccionNoValida,
			InterruptedException, IOException {
		super();
		this.app = app;

		this.actualizarBD();

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

	public synchronized void actualizarBD() throws InterruptedException,
			IOException {
		System.gc();
		Media a = null;
		Fileupload fu = new Fileupload();
		a = fu.get();
		Desktop desktop = app.getWin().getDesktop();
		String rutaO = desktop.getWebApp().getRealPath("/entradas") + "/";
		String rutaD = desktop.getWebApp().getRealPath("/entradas")
				+ "/descomprimidos/";
		System.out.println(rutaO);
		String nombresql = rutaD
				+ a.getName().substring(0, (a.getName().length() - 4)) + ".sql";

		boolean as = saveFile(a, rutaO);

		boolean tipo = a.getName().toLowerCase().endsWith(".7za");
		if (!tipo) {
			throw new WrongValueException(vista, "el archivo no es un .zip");
		}
		/*
		 * File file = null;
		 * 
		 * JFileChooser fc = new JFileChooser();
		 * 
		 * // int returnVal = fc.showSaveDialog(FileChooserDemo.this); int
		 * returnVal = fc.showSaveDialog(fc); if (returnVal ==
		 * JFileChooser.APPROVE_OPTION) { file = fc.getSelectedFile();
		 * 
		 * boolean ss = file.getName().toLowerCase().endsWith(".7za"); if (!ss){
		 * throw new WrongValueException(vista, "el archivo no es un .zip"); } }
		 * 
		 * 
		 * Desktop desktop = app.getWin().getDesktop(); String rutaO =
		 * desktop.getWebApp().getRealPath("/entradas") + "/"; String rutaD =
		 * desktop.getWebApp().getRealPath("/entradas") + "/descomprimidos/";
		 * System.out.println(rutaO); String nombresql = rutaD +
		 * file.getName().substring(0, (file.getName().length() - 4)) + ".sql";
		 * 
		 * boolean as = saveFile(file, rutaO); System.out.println("Ok");
		 */

		descomprimir(rutaO + a.getName(), rutaD);
		// notify();
		wait(1000);
		String ss = leer(nombresql);
		// fu.get().getStreamData().close();
		// fu.get().getReaderData().close();
		try {
			new DaoGenerico<EntradaArticulo, Integer>(EntradaArticulo.class)
					.correrSqlinsertupdate(ss);
			Messagebox.show("actualizacion exitosa", "OK", Messagebox.OK,
					Messagebox.ERROR);
			// JOptionPane.showMessageDialog(null, "OK"
			// ,"Actualizacion Completa",JOptionPane.WARNING_MESSAGE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Messagebox.show(e.getMessage() + ": " + e.getCause().getMessage(),
					"Error", Messagebox.OK, Messagebox.ERROR);
			// JOptionPane.showMessageDialog(null, e.getMessage()
			// ,"ERROR SQL",JOptionPane.WARNING_MESSAGE);

		}

		// finalize();
		// vista.close();
		/*
		 * String gt = app.getLogin().getDesktop().getSession().getLocalAddr();
		 * Execution ad = Executions.getCurrent();
		 * //System.out.println(desktop.getWebApp().get); String p =
		 * desktop.getWebApp().getDirectory(); String ñ =
		 * desktop.getWebApp().getAppName(); Object l =
		 * desktop.getWebApp().getNativeContext(); String j =
		 * desktop.getWebApp().getUpdateURI();
		 */
		// Session kkk = app.getWin().getDesktop().getSession();
		// String n = kkk.getRemoteAddr();
		// String uri = app.getWin().getDesktop().getWebApp().getUpdateURI();
		// Execution ad = Executions.getCurrent();
		// ad.sendRedirect("http://"+n+":8080/demeterSedes/index.zul");
	}

	public void descomprimir(String rutaO, String rutaD) throws IOException {

		ProcessBuilder pb2 = new ProcessBuilder("7za", "e", "-pr3sp4ld4d0",
				"-y", "-o" + rutaD, rutaO);
		// pb2.wait(50000);

		pb2.start();

		System.out.println(pb2.command());

	}

	public String leer(String nombre) throws InterruptedException

	{

		try {

			File f;
			FileReader lectorArchivo;

			// Creamos el objeto del archivo que vamos a leer
			f = new File(nombre);

			// Creamos el objeto FileReader que abrira el flujo(Stream) de datos
			// para realizar la lectura
			lectorArchivo = new FileReader(f);

			// Creamos un lector en buffer para recopilar datos a travez del
			// flujo "lectorArchivo" que hemos creado
			BufferedReader br = new BufferedReader(lectorArchivo);

			String l = "";
			// Esta variable "l" la utilizamos para guardar mas adelante toda la
			// lectura del archivo

			String aux = "";/* variable auxiliar */

			while (true)
			// este ciclo while se usa para repetir el proceso de lectura, ya
			// que se lee solo 1 linea de texto a la vez
			{
				aux = br.readLine();
				// leemos una linea de texto y la guardamos en la variable
				// auxiliar
				if (aux != null)
					// l=l+aux+"n";
					l = l + aux;
				/*
				 * si la variable aux tiene datos se va acumulando en la
				 * variable l, 07 * en caso de ser nula quiere decir que ya nos
				 * hemos leido todo 08 * el archivo de texto
				 */

				else
					break;
			}

			br.close();

			lectorArchivo.close();

			return l;

		} catch (IOException e) {
			System.out.println("Error:" + e.getMessage());
		}
		return null;
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

	public static boolean saveFile(File media, String path) {
		boolean uploaded = false;
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			InputStream ins = new FileInputStream(media);
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

}
