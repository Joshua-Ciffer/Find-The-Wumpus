package src.datFile.backend;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.File;
import java.util.Scanner;
/**
 * 
 * @author Brian Williams, Joshua Ciffer
 * @version 12/19/2017
 */
public class DatRunner {
	static Scanner myScan = new Scanner(System.in);
	public static void main (String[]args) throws IOException, FileNotFoundException {
		File txtFile = new File("H:\\ff.txt");
		PrintStream datPrint = new PrintStream(txtFile);
		datPrint.println("Herro");
		datPrint.println("Do you ruff me?");
		String response = myScan.nextLine();
		datPrint.println("I don't know what " + response + " means but I'll take that as a yesh . . .");
	}
}
