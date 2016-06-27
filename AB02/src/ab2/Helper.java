package ab2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

/**
 * Hilfsmethoden
 * 
 * @author Raphael Wigoutschnigg, Judith Michael, Peter Schartner
 * 
 */
public class Helper {
	private static Random rand = new Random(System.currentTimeMillis());
	
	/**
	 * Gibt die erste Primzahl zurück, die größer oder gleich n ist
	 * @param n kleinste mögliche gesuchte Primzahl
	 * @return die erste Primzahl p mit p >= n
	 */
	public static int getPrimeGreaterOrEqualThan(int n)
	{
		while(!BigInteger.valueOf(n).isProbablePrime(100))
			n++;
		return n;
	}

	/**
	 * Liefert n Zufallszahlen (Integer)
	 * @param n ... Anzahl der Zufallszahlen
	 * @return n Zufallszahlen
	 * @author Raphael Wigoutschnigg
	 */
	public static int[] getData(int n) {
		int[] data = new int[n];
		for (int i = 0; i < n; i++)
			data[i] = rand.nextInt();
		return data;
	}
	
	/**
	 * Liefert n Zufallszahlen (Integer), aufsteigend sortiert
	 * @param n ... Anzahl der Zufallszahlen
	 * @return n Zufallszahlen
	 * @author Raphael Wigoutschnigg
	 */
	public static int[] getSortedData(int n) {
		int[] data = new int[n];
		for (int i = 0; i < n; i++)
			data[i] = rand.nextInt();
		Arrays.sort(data);
		return data;
	}


	/**
	 * Erzeugt eine Datei und schreibt die übergebenen Daten im CSV-Format in diese
	 * @param header ... Namen der Spalten
	 * @param data ... zu exportierende Daten
	 * @param filename ... Dateiname der CSV-Datei (wird überschrieben)
	 * @throws IOException ... Wenn es Probleme beim Schreiben der Datei gibt
	 * @throws IllegalArgumentException ... Wenn die Anzahl der Spalten der Daten-Matrix nicht mit der Anzahl der Header-Spalten übereinstimmt.
	 * @author Raphael Wigoutschnigg
	 */
	public static void exportCSV(String[] header, Object[][] data,
			String filename) throws IOException {
		
		if(data.length > 0 && header.length != data[0].length)
			throw new IllegalArgumentException("Falsche Anzahl an Spalten in der Daten-Matrix");
		
		File f = new File(filename);
		FileWriter fw = new FileWriter(f);

		String sep = ";";

		String csv = "";
		for (String s : header)
			csv += "\"" + s + "\"" + sep;
		csv += "\n";

		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++)
			{
				if(data[i][j] instanceof Number)
					csv += (data[i][j] + sep).replaceAll("\\.", ",");
				else
					csv += "\"" + data[i][j] + "\"" + sep;
			}
			csv += "\n";
		}
		
		fw.write(csv);
		fw.close();
	}
}
