package cpc.demeter.vista;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Column;
import org.zkoss.zul.Columns;
import org.zkoss.zul.Grid;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Rows;
import org.zkoss.zul.Vbox;
import cpc.modelo.demeter.administrativo.CuotasAPagarCliente;
import cpc.modelo.demeter.administrativo.DefinicionDeCuotas;
import cpc.zk.componente.modelo.RowLabelComparator;

public class UIInfoPlanFinanciamiento extends Vbox implements EventListener {
	private static final long serialVersionUID = 3479978615044638751L;
	private Grid gridFinanciamientoContrato;
	private List<CuotasAPagarCliente> lista;
	private Button btnOcultar;
	@SuppressWarnings("unchecked")
	private Comparator asc = new RowLabelComparator(true, 0);
	@SuppressWarnings("unchecked")
	private Comparator dsc = new RowLabelComparator(false, 0);

	public UIInfoPlanFinanciamiento() throws InterruptedException {
		gridFinanciamientoContrato = new Grid();
		this.appendChild(gridFinanciamientoContrato);
		this.setWidth("700px");
		this.setHeight("100px");
		this.btnOcultar = new Button("OCULTAR");
		/*
		 * this.setTitle("Plan de Financiamiento para el Pagador");
		 * this.setBorder("normal"); this.setClosable(true);
		 */

		try {
			dibujarColsGridFinanciamineto();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	private void dibujarColsGridFinanciamineto() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException {
		Rows rows = new Rows();
		Columns cols = new Columns();
		Column col;

		col = new Column();
		col.appendChild(new Label("Cuota"));
		col.setSortAscending(asc);
		col.setSortDescending(dsc);
		col.setWidth("30px");
		cols.appendChild(col);

		col = new Column();
		col.setWidth("50px");
		col.appendChild(new Label("Monto"));
		col.setAlign("right");
		col.setSortAscending(asc);
		col.setSortDescending(dsc);

		cols.appendChild(col);

		col = new Column();
		col.appendChild(new Label("Vence"));
		col.setSortAscending(asc);
		col.setSortDescending(dsc);
		col.setWidth("30px");
		col.setAlign("right");

		cols.appendChild(col);

		col = new Column();
		col.appendChild(new Label("Ultimo Llamado"));
		col.setSortAscending(asc);
		col.setSortDescending(dsc);
		col.setWidth("30px");
		col.setAlign("right");
		cols.appendChild(col);

		col = new Column();
		col.appendChild(new Label("Vencido"));
		col.setSortAscending(asc);
		col.setSortDescending(dsc);
		col.setWidth("30px");
		col.setAlign("center");
		cols.appendChild(col);

		col = new Column();
		col.appendChild(new Label("Cancelado"));
		col.setSortAscending(asc);
		col.setSortDescending(dsc);
		col.setWidth("30px");
		col.setAlign("center");
		cols.appendChild(col);

		this.gridFinanciamientoContrato.appendChild(cols);
		this.gridFinanciamientoContrato.appendChild(rows);
		this.gridFinanciamientoContrato.setHeight("300px");
		this.btnOcultar.addEventListener(Events.ON_CLICK, this);
		this.appendChild(btnOcultar);

	}

	public void dibujarFilasFinanciamiento(double monto, DefinicionDeCuotas def) {
		Row row;
		lista = new ArrayList<CuotasAPagarCliente>();
		double montoCuota = monto / def.getCantidadCuotas();
		this.gridFinanciamientoContrato.getRows().getChildren().clear();
		Calendar fechaPago = Calendar.getInstance();
		Calendar fechaTope = Calendar.getInstance();
		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		for (int i = 0; i < def.getCantidadCuotas(); i++) {
			fechaPago.add(Calendar.DATE, def.getFrecuenciaPago()
					.getDiasDelPeriodo());
			fechaTope
					.set(fechaPago.get(Calendar.YEAR),
							fechaPago.get(Calendar.MONTH),
							fechaPago.get(Calendar.DATE));
			fechaTope.add(Calendar.DATE, def.getDiasDeEsperaParaPago());

			row = new Row();
			CuotasAPagarCliente capc = new CuotasAPagarCliente();

			String cuota = def.getFrecuenciaPago().getPeridoTiempo() + " "
					+ (i + 1);
			row.appendChild(new Label(cuota));
			capc.setCuota(cuota);

			row.appendChild(new Label("" + montoCuota + " Bs"));
			capc.setMontoCuota(montoCuota);

			row.appendChild(new Label(sdf.format(fechaPago.getTime())));
			capc.setFechaVencimiento(fechaPago.getTime());

			row.appendChild(new Label(sdf.format(fechaTope.getTime())));
			capc.setFechaUltimoLlamado(fechaTope.getTime());

			Checkbox chk = new Checkbox();
			chk.setChecked(false);
			chk.setDisabled(true);
			row.appendChild(chk);
			chk = new Checkbox();
			chk.setChecked(false);
			chk.setDisabled(true);
			row.appendChild(chk);

			this.gridFinanciamientoContrato.getRows().appendChild(row);
			lista.add(capc);

		}

	}

	public void mostrarCuotasAPagarDelCliente(List<CuotasAPagarCliente> lista) {
		Row row;

		this.gridFinanciamientoContrato.getRows().getChildren().clear();
		DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		for (CuotasAPagarCliente cuota : lista) {

			row = new Row();
			row.appendChild(new Label(cuota.getCuota()));
			row.appendChild(new Label("" + cuota.getMontoCuota() + " BsF"));
			row.appendChild(new Label(sdf.format(cuota.getFechaVencimiento()
					.getTime())));
			row.appendChild(new Label(sdf.format(cuota.getFechaUltimoLlamado()
					.getTime())));
			Checkbox chk = new Checkbox();

			chk.setChecked(cuota.isVencido());
			chk.setDisabled(true);
			row.appendChild(chk);

			chk = new Checkbox();
			chk.setChecked(cuota.isCancelado());
			chk.setDisabled(true);
			row.appendChild(chk);

			this.gridFinanciamientoContrato.getRows().appendChild(row);
			this.getParent().getParent().getParent().getParent()
					.setVisible(true);
		}
	}

	public List<CuotasAPagarCliente> getCuotasAPagarCliente() {
		return lista;
	}

	public void onEvent(Event evento) throws Exception {
		if (evento.getTarget() == this.btnOcultar) {
			this.getParent().getParent().getParent().getParent()
					.setVisible(false);
		}

	}

}
