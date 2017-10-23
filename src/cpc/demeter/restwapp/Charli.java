package cpc.demeter.restwapp;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class Charli {
 
	
	private String tipo0;
	private String tipo1;
	private String tipo2;
	public String getTipo0() {
		return tipo0;
	}
	public String getTipo1() {
		return tipo1;
	}
	public String getTipo2() {
		return tipo2;
	}
	public void setTipo0(String tipo0) {
		this.tipo0 = tipo0;
	}
	public void setTipo1(String tipo1) {
		this.tipo1 = tipo1;
	}
	public void setTipo2(String tipo2) {
		this.tipo2 = tipo2;
	}
	public Charli(String tipo0, String tipo1, String tipo2) {
		super();
		this.tipo0 = tipo0;
		this.tipo1 = tipo1;
		this.tipo2 = tipo2;
	}
	public Charli() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	
	
}
