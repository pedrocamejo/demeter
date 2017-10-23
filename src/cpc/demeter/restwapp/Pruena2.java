package cpc.demeter.restwapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;


@Path(value="Prueba")
public class Pruena2 {

	
	@GET
	public String getid()
	{
		return "<html><title>HOla</title><body>Alex, I Love you </body></html>";
	}
}
