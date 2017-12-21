package src.datFile.backend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.File;

/**
 * 
 * @author Brian Williams, Joshua Ciffer
 * @version 12/19/2017
 */
public class DatRunner {
	public static void main (String[]args) throws IOException, FileNotFoundException {
		File txtFile = new File("H:\\ff.txt");
		PrintStream datPrint = new PrintStream(txtFile);
		datPrint.println("Herro");
	}
}
