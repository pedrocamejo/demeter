package cpc.demeter.configuracion;

import java.io.IOException;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.Resource;

public class Entorno extends PropertiesFactoryBean {

	@Override
	public void setLocation(Resource location) {
		// TODO Auto-generated method stub
		try {
			System.out.println("---->"+location.getURL().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.setLocation(location);
	}

}
