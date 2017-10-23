package cpc.demeter.restwapp;

import java.io.File;
import java.io.FileNotFoundException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
/*
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
*/
import com.sun.xml.internal.messaging.saaj.packaging.mime.MessagingException;

public class Prueba {

	/**
	 * @param args
	 * @throws MessagingException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, MessagingException {
		// TODO Auto-generated method stub

	/*
		Client client = ClientBuilder.newBuilder().register(MultiPartFeature.class).build();
		
		WebTarget target = client.target("http://localhost:8080/demeterSedes/rest").path("sincronizaciondb");
		
		File file = new File("WebContent/prueba.zip");
		
		FileDataBodyPart filePart = new FileDataBodyPart("archivo",file);
		
		FormDataMultiPart multipart = new FormDataMultiPart();
		
		multipart.field("hola","mundo");
		multipart.bodyPart(filePart);
		
		System.out.println(target.request().post(Entity.entity(multipart,multipart.getMediaType())).readEntity(String.class));
		*/
		//System.out.println(target.request(MediaType.TEXT_PLAIN).get(String.class));
		


	}

}















