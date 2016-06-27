package ab2;


/**
 * Interface für Suchalgorithmen.
 * 
 * @author Raphael Wigoutschnigg, Judith Michael, Peter Schartner
 */
public interface SearchAlgos{

	/**
	 * Sucht das Element x im sortierten Array data unter Verwendung der binären Suche
	 * @param data geordnete Daten
	 * @param x zu suchendes Element
     * @return index des Elements, -1 wenn nicht gefunden
	 * @throws IllegalArgumentException Bei Problemen mit den übergebenen Parametern
	 */
	public int BinarySearch(int[] data, int x) throws IllegalArgumentException;
	
	/**
	 * Sucht das Element x im sortierten Array data unter Verwendung der Interpolationssuche
	 * @param data geordnete Daten
	 * @param x zu suchendes Element
     * @return index des Elements, -1 wenn nicht gefunden
     * @throws IllegalArgumentException Bei Problemen mit den übergebenen Parametern
	 */

	public int InterpolationSearch(int[] data, int x) throws IllegalArgumentException;
	
}