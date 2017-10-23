package cpc.demeter;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Profile("production")
@PropertySource("file:/opt/tomcat/conf/${name.application}.${spring.profiles.active}.properties")
@ComponentScan("cpc.demeter")
public class ConfiguracionProduccion {

	
}
