package cpc.demeter.restwapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;
import org.mvel.tests.main.AbstractTest.Bean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

////import prueba.AuditorUsuario;
import util.Seguridad;
import cpc.modelo.demeter.administrativo.ControlSede;
import cpc.modelo.demeter.administrativo.LibroVenta;
import cpc.negocio.demeter.administrativo.NegocioLibroVenta;
import cpc.persistencia.demeter.implementacion.administrativo.PerFactura;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;

@Path(value="restlibro/")
public class RestLibro{
	NegocioLibroVenta servicio = NegocioLibroVenta.getInstance();
	
		/*@Context
		UriInfo uriInfo;
		@Context
		Request request;
*/

	
		@GET
		@Produces("aplication/xml")
		public File getDetallesLibro( @HeaderParam("ano") Integer ano, @HeaderParam("mes") Integer mes) throws ExcFiltroExcepcion, IOException, ExcEntradaInconsistente {
		
			Element libro = servicio.getLibroXml(mes, ano);
		
		
		//	FormDataMultiPart formMultipart = new FormDataMultiPart();
			
			
			Document documento = new Document(libro);
			File archivo = new File("/home/erivas/xmls.xml");
			FileOutputStream out = new FileOutputStream(archivo);
			
			XMLOutputter xmxl = new XMLOutputter();
			xmxl.output(documento,out);
			
		//	FileDataBodyPart filePart  = new FileDataBodyPart("archivo",archivo);
		//	formMultipart.bodyPart(filePart);
			
			return archivo;
		}

		@POST
		@Produces(MediaType.TEXT_HTML)
		public String ActualizarLibro(@DefaultValue("2") @HeaderParam("ano") Integer ano,  @DefaultValue("1") @HeaderParam("mes") Integer mes) throws Exception {
			System.out.println("actualizar: "+ano+" " +mes );
			boolean b = TransactionSynchronizationManager.hasResource("obj");
			if (b)
			{
				TransactionSynchronizationManager.unbindResource("obj");
			}
			///TransactionSynchronizationManager.bindResource("obj", new AuditorUsuario());
			servicio.setDeclarado(mes,ano);
			return "ok";
		}
	
}
