package cpc.demeter.reporte.datasource;

import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class CustomDataSourceMovimientoActivo implements JRDataSource {

	private List<Object[]> lista;
	private Object[][] data;
	private int index = -1;

	public CustomDataSourceMovimientoActivo() {
		super();
	}

	public CustomDataSourceMovimientoActivo(List<Object[]> lista) {
		super();
		this.lista = lista;
		AgregarLista();
	}

	public List<Object[]> getLista() {
		return lista;
	}

	public void setLista(List<Object[]> lista) {
		this.lista = lista;
	}

	public boolean next() throws JRException {
		index++;

		return (index < data.length);
	}

	public Object getFieldValue(JRField campo) throws JRException {
		Object valor = null;

		String nombreCampo = campo.getName();

		if ("id".equals(nombreCampo))
			valor = data[index][0];
		else if ("fecha".equals(nombreCampo))
			valor = data[index][1];
		else if ("idtipomovimiento".equals(nombreCampo))
			valor = data[index][2];
		else if ("descripcion".equals(nombreCampo))
			valor = data[index][3];
		else if ("serial".equals(nombreCampo))
			valor = data[index][4];
		else if ("chapa".equals(nombreCampo))
			valor = data[index][5];
		else if ("nombre".equals(nombreCampo))
			valor = data[index][6];
		else if ("marca".equals(nombreCampo))
			valor = data[index][7];
		else if ("modelo".equals(nombreCampo))
			valor = data[index][8];
		else if ("unidad".equals(nombreCampo))
			valor = data[index][9];
		else if ("estadoActivo".equals(nombreCampo))
			valor = data[index][10];
		else if ("observaciones".equals(nombreCampo))
			valor = data[index][11];
		else if ("control".equals(nombreCampo))
			valor = data[index][12];
		else if ("prefijo".equals(nombreCampo))
			valor = data[index][13];
		else if ("idActivo".equals(nombreCampo))
			valor = data[index][14];

		return valor;
	}

	public void AgregarLista() {
		int i = 0;
		this.data = new Object[this.lista.size()][15];
		for (Object[] obj : this.lista) {
			this.data[i][0] = obj[0];
			this.data[i][1] = obj[1];
			this.data[i][2] = obj[2];
			this.data[i][3] = obj[3];
			this.data[i][4] = obj[4];
			this.data[i][5] = obj[5];
			this.data[i][6] = obj[6];
			this.data[i][7] = obj[7];
			this.data[i][8] = obj[8];
			this.data[i][9] = obj[9];
			this.data[i][10] = obj[10];
			this.data[i][11] = obj[11];
			this.data[i][12] = obj[12];
			this.data[i][13] = obj[13];
			this.data[i][14] = obj[14];
			i++;
		}
	}

}
