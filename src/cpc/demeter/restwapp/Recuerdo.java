package cpc.demeter.restwapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;



@Path(value="recordando")
public class Recuerdo {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getit()
	{
		return "Got it";
	}
	
}
