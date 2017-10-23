package cpc.demeter.controlador.inventario;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.WrongValuesException;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Doublebox;
import org.zkoss.zul.Fileupload;
import org.zkoss.zul.Row;

import cpc.demeter.AppDemeter;
import cpc.demeter.catalogo.inventario.ContEntradasArticulos;
import cpc.demeter.vista.inventario.UiEntradaArticulo;
import cpc.modelo.demeter.basico.ArticuloVenta;
import cpc.modelo.demeter.mantenimiento.DetalleEntradaArticulo;
import cpc.modelo.demeter.mantenimiento.EntradaArticulo;
import cpc.modelo.sigesp.basico.Almacen;
import cpc.negocio.demeter.mantenimiento.NegocioEntradaArticulo;
import cpc.persistencia.DaoGenerico;
import cpc.zk.componente.excepciones.ExcDatosInvalidos;
import cpc.zk.componente.listas.CompBuscar;
import cpc.zk.componente.listas.ContVentanaBase;
import cva.pc.demeter.excepciones.ExcEntradaInconsistente;
import cva.pc.demeter.excepciones.ExcFiltroExcepcion;
import cva.pc.demeter.excepciones.ExcSeleccionNoValida;

public class ContEntradaArticulo extends ContVentanaBase<EntradaArticulo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8596195572997106144L;
	private NegocioEntradaArticulo servicio;
	private AppDemeter app;
	private UiEntradaArticulo vista;

	public ContEntradaArticulo(int modoOperacion,
			EntradaArticulo entradaArticulo, List<Almacen> almacenesOrigen,
			List<Almacen> almacenesDestino, ContEntradasArticulos llamador,
			AppDemeter app) throws ExcDatosInvalidos, ExcSeleccionNoValida {
		super(modoOperacion);
		this.app = app;

		try {
			if (entradaArticulo == null || modoAgregar()) {
				entradaArticulo = new EntradaArticulo();

			}
			setDato(entradaArticulo);
			setear(getDato(),
					new UiEntradaArticulo("Entrada de Articulos", 1200,
							modoOperacion, entradaArticulo, entradaArticulo
									.getDetalleEntradaArticulos(),
							almacenesOrigen, almacenesDestino), llamador, app);
			vista = ((UiEntradaArticulo) getVista());
			vista.desactivar(getModoOperacion());
			// prueba();

		} catch (ExcDatosInvalidos e) {
			e.printStackTrace();
			this.app.mostrarError("No ha seleccionado ningun campo");
		}

	}

	@SuppressWarnings("unchecked")
	public void eliminar() {

	}

	public void guardar() {

		NegocioEntradaArticulo negocio = servicio.getInstance();
		getDato().setUsuario(app.getUsuario().toString());
		getDato().setFecha(new Date());
		getDato().setPlaca(vista.getTxtplaca().getValue());
		getDato().setResponsable(vista.getTxtResponsable().getValue());
		getDato().setTransporte(vista.getTxtvehiculo().getValue());

		List<DetalleEntradaArticulo> detalleEntradaArticulo = vista
				.getDetalle().getColeccion();

		getDato().setDetalleEntradaArticulos(detalleEntradaArticulo);
		negocio.setEntradaArticulo(getDato());
		try {
			negocio.guardar(negocio.getEntradaArticulo());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public List<ArticuloVenta> getArticuloVentas() {
		List<ArticuloVenta> za = new ArrayList<ArticuloVenta>();
		List<Row> filas = vista.getDetalle().getFilas().getChildren();
		for (Row row : filas) {
			CompBuscar<ArticuloVenta> a = (CompBuscar<ArticuloVenta>) row
					.getChildren().get(0);
			za.add(a.getSeleccion());
		}

		return za;

	}

	public void validar() throws WrongValuesException, ExcEntradaInconsistente {
		boolean a = vista.getTxtplaca().getValue().isEmpty();
		if (a) {
			throw new WrongValueException(vista.getTxtplaca(),
					"indique la placa del vehiculo");
		}
		boolean b = vista.getTxtResponsable().getValue().isEmpty();
		if (b) {
			throw new WrongValueException(vista.getTxtResponsable(),
					"indique el responsable del traslado");
		}
		String sss = vista.getTxtvehiculo().getValue();
		boolean c = vista.getTxtvehiculo().getValue().isEmpty();
		if (c) {
			throw new WrongValueException(vista.getTxtvehiculo(),
					"indique el vehiculo");
		}
		List<Row> filas = vista.getDetalle().getFilas().getChildren();
		boolean z = vista.getDetalle().getFilas().getChildren().isEmpty();
		if (z) {
			throw new WrongValueException(vista.getDetalle(),
					"debe indicar al menos un articulo");
		}
		validarDetalle();
	}

	@SuppressWarnings({ "unchecked", "unused" })
	public void validarDetalle() throws WrongValuesException,
			ExcEntradaInconsistente {
		List<Row> filas = vista.getDetalle().getFilas().getChildren();

		for (Row row : filas) {

			CompBuscar<ArticuloVenta> articulo = (CompBuscar<ArticuloVenta>) row
					.getChildren().get(0);
			CompBuscar<Almacen> almacenOrigen = (CompBuscar<Almacen>) row
					.getChildren().get(2);
			CompBuscar<Almacen> almacenDestino = (CompBuscar<Almacen>) row
					.getChildren().get(3);
			Doublebox cantidad = (Doublebox) row.getChildren().get(1);
			if (articulo.getSeleccion() == null) {
				throw new WrongValueException(articulo,
						"El Articulo no puede ser nulo");
			}

			if (almacenOrigen.getSeleccion() == null) {
				throw new WrongValueException(almacenOrigen,
						"indique el almacen de origen");
			}

			if (almacenDestino.getSeleccion() == null) {
				throw new WrongValueException(almacenDestino,
						"indique el almacen de Destino");
			}

			if (cantidad.getValue() == null || (cantidad.getValue() == 0)) {
				throw new WrongValueException(cantidad,
						"la cantidad no puede ser nula o cero");
			}

		}
	}

	public synchronized void onEvent(Event event) throws Exception {

		if (event.getName().equals(Events.ON_CLICK)) {
			if (event.getTarget().equals(vista.getCargararchivo())) {
				boolean placas = vista.getTxtplaca().getValue().isEmpty();
				if (placas) {
					throw new WrongValueException(vista.getTxtplaca(),
							"indique la placa del vehiculo");
				}
				boolean b = vista.getTxtResponsable().getValue().isEmpty();
				if (b) {
					throw new WrongValueException(vista.getTxtResponsable(),
							"indique el responsable del traslado");
				}
				String sss = vista.getTxtvehiculo().getValue();
				boolean c = vista.getTxtvehiculo().getValue().isEmpty();
				if (c) {
					throw new WrongValueException(vista.getTxtvehiculo(),
							"indique el vehiculo");
				}

				System.gc();
				Media a = Fileupload.get();
				Desktop desktop = app.getWin().getDesktop();
				String rutaO = desktop.getWebApp().getRealPath("/entradas")
						+ "/";
				String rutaD = desktop.getWebApp().getRealPath("/entradas")
						+ "/descomprimidos/";
				System.out.println(rutaO);
				String nombresql = rutaD
						+ a.getName().substring(0, (a.getName().length() - 3))
						+ ".sql";
				boolean as = saveFile(a, rutaO);
				System.out.println("Ok");
				descomprimir(rutaO + a.getName(), rutaD);
				wait(1000);
				String ss = leer(nombresql);
				HashMap mapa = new HashMap();

				mapa.put("placa", vista.getTxtplaca().getValue());
				mapa.put("responsable", vista.getTxtResponsable().getValue());
				mapa.put("transporte", vista.getTxtvehiculo().getValue());
				mapa.put("usuario", app.getUsuario().getNombre());
				
				new DaoGenerico<EntradaArticulo, Integer>(EntradaArticulo.class)
						.correrSqlUpdateinsert(ss, mapa);
				vista.close();
			}

		}

		if (event.getName().equals(CompBuscar.ON_SELECCIONO)) {
			/*
			 * if (event.getTarget() == vista.getCmbarticulo()){
			 * 
			 * } else { CompBuscar<ArticuloVenta> componente =
			 * (CompBuscar<ArticuloVenta>) event.getTarget();
			 * 
			 * }
			 */
		}

		else if (event.getTarget() == getVista().getAceptar()) {

			procesarCRUD(event);

		}

	}

	@Override
	public void anular() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void correjir() throws ExcFiltroExcepcion {
		// TODO Auto-generated method stub

	}

	@Override
	public void procesar() throws WrongValuesException, ExcEntradaInconsistente {
		// TODO Auto-generated method stub

	}

	public static boolean saveFile(Media media, String path) {
		boolean uploaded = false;
		BufferedInputStream in = null;
		BufferedOutputStream out = null;
		try {
			InputStream ins = media.getStreamData();
			in = new BufferedInputStream(ins);

			String fileName = media.getName();
			File arc = new File(path + fileName);
			OutputStream aout = new FileOutputStream(arc);
			out = new BufferedOutputStream(aout);

			byte buffer[] = new byte[1024];
			int ch = in.read(buffer);
			while (ch != -1) {
				out.write(buffer, 0, ch);
				ch = in.read(buffer);
			}
			uploaded = true;
		} catch (IOException ie) {
			throw new RuntimeException(ie);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return uploaded;
	}

	public void descomprimir(String rutaO, String rutaD) throws IOException {
		ProcessBuilder pb2 = new ProcessBuilder("7za", "e", "-pr3sp4ld4d0","-y", "-o" + rutaD, rutaO);
		pb2.start();
		System.out.println(pb2.command());
	}

	public String leer(String nombre)
	{

		try {

			File f;
			FileReader lectorArchivo;

			// Creamos el objeto del archivo que vamos a leer
			f = new File(nombre);

			// Creamos el objeto FileReader que abrira el flujo(Stream) de datos
			// para realizar la lectura
			lectorArchivo = new FileReader(f);

			// Creamos un lector en buffer para recopilar datos a travez del
			// flujo "lectorArchivo" que hemos creado
			BufferedReader br = new BufferedReader(lectorArchivo);

			String l = "";
			// Esta variable "l" la utilizamos para guardar mas adelante toda la
			// lectura del archivo

			String aux = "";/* variable auxiliar */

			while (true)
			// este ciclo while se usa para repetir el proceso de lectura, ya
			// que se lee solo 1 linea de texto a la vez
			{
				aux = br.readLine();
				// leemos una linea de texto y la guardamos en la variable
				// auxiliar
				if (aux != null)
					// l=l+aux+"n";
					l = l + aux;
				/*
				 * si la variable aux tiene datos se va acumulando en la
				 * variable l, 07 * en caso de ser nula quiere decir que ya nos
				 * hemos leido todo 08 * el archivo de texto
				 */

				else
					break;
			}

			br.close();

			lectorArchivo.close();

			return l;

		} catch (IOException e) {
			System.out.println("Error:" + e.getMessage());
		}
		return null;
	}

}