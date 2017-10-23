package cpc.demeter;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.zkoss.zk.ui.Desktop;
import org.zkoss.zkex.zul.Jasperreport;


public class Servicios {
	AppDemeter app;

	public Servicios(AppDemeter app) {
		// TODO Auto-generated constructor stub
	}

	public void EnviarCorreo(String textocorreo, Jasperreport reporte,
			AppDemeter app, String destino, String origen, String clave,
			String Subjetc, String nombreArchivo) {
		try {
			// se obtiene el objeto Session. La configuración es para
			// una cuenta de gmail.
			Properties props = new Properties();
			// props.put("mail.smtp.host", "smtp.gmail.com");
			props.put("mail.smtp.host", "correo.cvapedrocamejo.gob.ve");

			props.setProperty("mail.smtp.starttls.enable", "true");
			props.setProperty("mail.smtp.port", "587");
			// props.setProperty("mail.smtp.user", "erixrivas@gmail.com");
			props.setProperty("mail.smtp.user", origen);
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.smtp.ssl.trust",
					"correo.cvapedrocamejo.gob.ve");

			Session session = Session.getDefaultInstance(props, null);
			// session.setDebug(true);

			// Se compone la parte del texto
			BodyPart texto = new MimeBodyPart();
			texto.setText(textocorreo);

			// Se compone el adjunto con la imagen
			BodyPart adjunto = new MimeBodyPart();

			// Una MultiParte para agrupar texto e imagen.
			MimeMultipart multiParte = new MimeMultipart();
			multiParte.addBodyPart(texto);
			multiParte.addBodyPart(adjunto);

			// Se compone el correo, dando to, from, subject y el
			// contenido.
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(origen));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(
					destino));
			message.setSubject(Subjetc);
			message.setContent(multiParte);

			JRExporter exporter = new JRPdfExporter();
			Desktop desktop = app.getWin().getDesktop();
			String outputFile = desktop.getWebApp().getRealPath("/respaldo")
					+ "/1" + ".pdf";
			System.out.println(outputFile);
			String b = desktop.getWebApp().getRealPath("/reportes")
					+ "/cierreDetalle.jasper";
			System.out.println(b);
			JasperPrint jasperPrint = JasperFillManager.fillReport(b,reporte.getParameters(), reporte.getDatasource());
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
					outputFile);
			exporter.exportReport();
			FileDataSource bb = new FileDataSource(outputFile);
			DataHandler a = new DataHandler(bb);
			adjunto.setDataHandler(a);
			System.out.println(reporte.toString());
			String aaa = reporte.getSrc();
			System.out.println(aaa);
			System.out.println(reporte.toString());
			System.out.println(nombreArchivo);
			adjunto.setFileName(nombreArchivo);

			// probamos el ping InetAddress ina;
			String serverHostname = "10.1.9.4";
			// la IP del ordenador

			InetAddress ina = InetAddress.getByName(serverHostname);
			if (ina.isReachable(5000)) { // 5000=tiempo durante el que esperamos
											// por la respuesta
				System.out.println("OK");

				Transport t = session.getTransport("smtp");
				t.connect(origen, clave);
				t.sendMessage(message, message.getAllRecipients());
				System.out.println("ok");
				t.close();

			}

			// Se envia el correo.

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public Integer generarsemilla() {
		Double valorDado = Math.floor(Math.random() * 2000 + 1);
		Integer a = (int) Math.floor(valorDado);
		return a;
	}

	public String generarHash(String semilla) throws NoSuchAlgorithmException {

		// Variable que almacena el criptograma generado
		String hash = "";
		// Variable que guardara el digest generadamapolaso
		byte[] digest = null;
		// Variable que obtiene el buffer del texto
		byte[] buffer = semilla.getBytes();

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.reset();
		md.update(buffer);
		digest = md.digest();
		hash = toHexadecimal(digest);
		System.out.println(hash);
		String b = hash.substring(0, 6);
		System.out.println(b);
		return b;
	}

	private String toHexadecimal(byte[] digest) {
		String hash = "";
		for (byte aux : digest) {
			int b = aux & 0xff; // Hace un cast del byte a hexadecimal
			if (Integer.toHexString(b).length() == 1)
				hash += "0";
			hash += Integer.toHexString(b);
		}
		return hash;
	}

}
