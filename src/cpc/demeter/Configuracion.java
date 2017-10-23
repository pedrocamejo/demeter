package cpc.demeter;

import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.List;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import cpc.modelo.sigesp.basico.ConfiguracionSede;

@Configuration
@PropertySource("classpath:data.properties")
@ComponentScan("cpc.demeter")
public class Configuracion {
	
	@Autowired
	private Environment environment;

	@Bean
	public Integer sistema(){
		return 6;
	}

	@Bean
	public Integer tamanoPagina(){
		return 10;
	}

	@Bean
	public Integer maximoRegistros(){
		return 15000;
	}
	
	@Bean
	public Integer unidades(){
		return 0;
	}
	
	@Bean
	public Integer lineasFactura(){
		return 6;
	}
	
	@Bean
	public String aplicacion(){
		return "Error en la Base de datos";
	}
	
	@Bean
	public List<String> iconosbarra(){
		List<String> lista = new ArrayList<String>();
		lista.add("/iconos/28x28/btnNuevo.png");
		lista.add("/iconos/28x28/btnEditar.png");
		lista.add("/iconos/28x28/btnEliminar.png");
		lista.add("/iconos/28x28/btnVer.png");
		lista.add("/iconos/28x28/btnAsociar.png");
		lista.add("/iconos/28x28/btnProcesar.png");
		lista.add("/iconos/28x28/btnImprimirUno.png");
		lista.add("/iconos/28x28/btnImprimirTodo.png");
		lista.add("/iconos/28x28/btnDeshacer.png");
		lista.add("/iconos/24x24/btnAnular.png");
		return  lista;
	}
 	
	@Bean
	public String iconoBorrar(){
		return "/iconos/24x24/borrar.png";
	}

	@Bean
	public String iconoEditar(){
		return "/iconos/24x24/edit.png";
	}
	
	@Bean
	public String iconoAdd(){ 
			return "/iconos/24x24/add.png";
	}

	@Bean
	public String iconoCrear(){
			return "/iconos/24x24/nuevo.png";
	}
	
	@Bean
	public String iconoConsulta(){
			return "/iconos/24x24/consultar.png";
	}
	
	@Bean
	public Integer idControl(){;
			return 1;
	}

	@Bean
	public Integer idUnidadFuncional(){;
			return 1;
	}

	@Bean
	public String idEmpresa(){
			return "0001";
	}
	
	@Bean
	public String idUnidadAdministrativa(){
			return "0000000001";
	}

	@Bean
	public Boolean funcionamientoActivos(){ 
			return false;
	}

	@Bean
	public ConfiguracionSede configuracionSede(){ 
		ConfiguracionSede con = new ConfiguracionSede();
		con.setIdControl(idControl());
		con.setIdUnidadFuncional(idUnidadFuncional());
		con.setCodigoEmpresa(idEmpresa());
		//<property name="codigoSede" ref="idsede"; 
		con.setCodigoUnidadAdministrativa(idUnidadAdministrativa());
		return con;
	 }  

	@Bean
	public String rutacomando(){
			return "/opt/tomcat/webapps/demeterSedes/respaldo/respaldo.sh"; 
	}  

	@Bean(name="ruta-comprimida")
	public String rutaComprimida(){
			return "/opt/tomcat/webapps/demeterSedes/respaldo/";
	}      
	
	public String respaldo(){
			return "postgres";
	}    
	
	@Bean(destroyMethod="close")
	public ComboPooledDataSource datasourceAres() throws PropertyVetoException{
		ComboPooledDataSource sor = new ComboPooledDataSource();
		sor.setDriverClass("org.postgresql.Driver");
		sor.setJdbcUrl(environment.getProperty("ares.db.url"));
		sor.setUser(environment.getProperty("ares.db.user"));
		sor.setPassword(environment.getProperty("ares.db.password"));      
		sor.setInitialPoolSize(5);   
		sor.setMinPoolSize(5);
		sor.setMaxPoolSize(20);
		sor.setCheckoutTimeout(300); 
		sor.setMaxStatements(50);
		sor.setAutomaticTestTable("C3P0_TEST_TABLE");
		sor.setTestConnectionOnCheckin(true);
		sor.setIdleConnectionTestPeriod(60); 
		sor.setNumHelperThreads(10);
		sor.setMaxAdministrativeTaskTime(600);
		return sor; 
	}

}
