package cpc.demeter.restwapp;

import java.io.File;

public class prueba3 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		File f = new File("/home/rchirinos/holaloco/");
		
		System.out.println(f.isDirectory());
		
		if(!f.isDirectory())
		{
			f.mkdir();
		}
		
	}

}
