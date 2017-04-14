package de.dis2011;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Kleine Helferklasse zum Einlesen von Formulardaten
 */
public class FormUtil {
	/**
	 * Liest einen String vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesene Zeile
	 */
	public static String readString(String label) {
		String ret = null;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print(label+": ");
			ret = stdin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	/**
	 * Liest einen Integer vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesener Integer
	 */
	public static int readInt(String label) {
		int ret = 0;
		boolean finished = false;

		while(!finished) {
			String line = readString(label);
			
			try {
				ret = Integer.parseInt(line);
				finished = true;
			} catch (NumberFormatException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Zahl an!");
			}
		}
		
		return ret;
	}
	
	public static double readDouble(String label) {
		double ret = 0;
		boolean finished = false;

		while(!finished) {
			String line = readString(label);
			
			try {
				ret = Double.parseDouble(line);
				finished = true;
			} catch (NumberFormatException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Zahl an!");
			}
		}
		
		return ret;
	}
	
	public static boolean readBoolean(String label) {
		boolean ret = false;
		boolean finished = false;

		while (!finished) {
			String line = readString(label);

			if (line.equalsIgnoreCase("y")) {
				ret = true;
			}
			finished = true;
		}
		return ret;
	}
	
	public static Date readDate(String label) {
		Date ret = null;
		boolean finished = false;
		while (!finished) {
			String line = readString(label);
			try {
				ret = new SimpleDateFormat("dd MM yyyy").parse(line);
				finished = true;
			} catch (ParseException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie ein Datum an! (dd MM yyyy)");
			}
			finished = true;
		}
		
		
		return ret;
	}
}
