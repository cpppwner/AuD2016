package ab2;

import java.io.IOException;

/**
 * 
 * @author Raphael Wigoutschnigg, Judith Michael, Peter Schartner
 *
 */
public class ExportSample {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		String[] header = {"Name", "Versuche", "n", "Dauer"};
		Object[][] data = new Object[3][4];
		
		data[0][0] = "Algo1";
		data[0][1] = 1000000;
		data[0][2] = 100;
		data[0][3] = 3.47;
		
		data[1][0] = "Algo2";
		data[1][1] = 10000;
		data[1][2] = 1000;
		data[1][3] = 21.87;
		
		data[2][0] = "Algo3";
		data[2][1] = 10000;
		data[2][2] = 10000;
		data[2][3] = 145.3;
		
		Helper.exportCSV(header, data, "test.csv");
	}

}
