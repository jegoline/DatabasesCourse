package ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Kleine Helferklasse f�r Men�s
 * Zuvor m�ssen mit addEntry Men�optionen hinzugef�gt werden. Mit
 * der Methode show() wird das Men� angezeigt und die mit der Option
 * angegebene Konstante zur�ckgeliefert.
 * 
 * Beispiel:
 * Menu m = new Menu("Hauptmen�");
 * m.addEntry("Hart arbeiten", 0);
 * m.addEntry("Ausruhen", 1);
 * m.addEntry("Nach Hause gehen", 2);
 * int wahl = m.show();
 * 
 * Angezeigt wird dann das Men�:
 * Hauptmen�:
 * [1] Hart arbeiten
 * [2] Ausruhen
 * [3] Nach Hause gehen
 * --
 */
public class Menu {
	private String title;
	private ArrayList<String> labels = new ArrayList<String>();
	private ArrayList<Integer> returnValues = new ArrayList<Integer>();
	
	/**
	 * Konstruktor.
	 * @param title Titel des Men�s z.B. "Hauptmen�"
	 */
	public Menu(String title) {
		super();
		this.title = title;
	}
	
	/**
	 * F�gt einen Men�eintrag zum Men� hinzu
	 * @param label Name des Eintrags
	 * @param returnValue Konstante, die bei Wahl dieses Eintrags zur�ckgegeben wird
	 */
	public void addEntry(String label, int returnValue) {
		this.labels.add(label);
		this.returnValues.add(new Integer(returnValue));
	}
	
	/**
	 * Zeigt das Men� an
	 * @return Die Konstante des ausgew�hlten Men�eintrags
	 */
	public int show()  {
		int selection = -1;
		
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		
		while(selection == -1) {
			System.out.println(title+":");
			
			for(int i = 0; i < labels.size(); ++i) {
				System.out.println("["+(i+1)+"] "+labels.get(i));
			}
			
			System.out.print("-- ");
			try {
				selection = Integer.parseInt(stdin.readLine());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(selection < 1 || selection > returnValues.size()) {
				System.err.println("Ung�ltige Eingabe!");
				selection = -1;
			} 
		}
		
		return returnValues.get(selection-1);
	}
}
