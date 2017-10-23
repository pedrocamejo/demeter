package cpc.demeter.vista.mantenimiento;

import java.util.Map;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.zkoss.zkex.zul.Jasperreport;
import org.zkoss.zul.Div;
import org.zkoss.zul.Progressmeter;
import org.zkoss.zul.Window;

import cpc.zk.componente.interfaz.IZkAplicacion;

public class UIMostrarReporte extends Window {

	private Jasperreport reporte;
	private Div div;
	private IZkAplicacion app;

	private String url;
	private String type;
	private JRBeanCollectionDataSource dataSource;
	private Integer maxProgreso = 2;
	private Map parametros;

	/**
	 * @param title
	 * @param border
	 * @param closable
	 * @throws InterruptedException
	 */
	public UIMostrarReporte(String title, String border, boolean closable,
			JRBeanCollectionDataSource dataSource, String url, String type,
			Map parametros) throws InterruptedException {
		super(title, border, closable);
		// TODO Auto-generated constructor stub
		this.setMaximizable(true);
		this.setMaximized(true);
		this.setMode("modal");
		this.url = url;
		this.type = type;
		this.dataSource = dataSource;
		this.parametros = parametros;
		inicializar();
		dibujar();

	}

	private void dibujar() {
		// TODO Auto-generated method stub

		div.setWidth("99%");
		div.setHeight("99%");
		reporte.setWidth("99%");
		reporte.setHeight("99%");
		reporte.setType(type);
		reporte.setSrc(url);
		reporte.setParameters(parametros);
		reporte.setDatasource(dataSource);

		div.appendChild(reporte);

		this.appendChild(div);

	}

	private void inicializar() {
		// TODO Auto-generated method stub
		reporte = new Jasperreport();
		div = new Div();
	}

}
