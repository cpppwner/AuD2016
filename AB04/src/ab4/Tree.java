package ab4;

import java.util.List;

/**
 * Schnittstelle, welche einen sortierten binären Wurzelnbaum (kurz Binärbaum)
 * darstellt. Null ist als Wert nicht erlaubt (IllegalArgumentException). Der
 * Baum darf keine doppelten Werte enthalten. Verwenden Sie zur Bestimmung der
 * Ordnung der Elemente die .compareTo-Methode.
 *
 * @author Raphael Wigoutschnigg, Judith Michael, Peter Schartner
 */
public interface Tree<V extends Comparable<V>> {

	/**
	 * Liefert die Höhe des Baums zurück. Die Höhe ist um eines geringer als die
	 * Anzahl der Ebenen. Besitzt der Baum keine Elemente, so soll -1 zurück gegeben werden.
	 *
	 * @return Höhe des Baumes
	 */
	public int getHeight();

	/**
	 * Liefert die Wurzel
	 *
	 * @return Wurzel des Baumes
	 */
	public Node<V> getRoot();

	/**
	 * Gibt die Anzahl der gespeicherten Werte zurück
	 *
	 * @return Anzahl der Werte des Baumes
	 */
	public int size();

	/**
	 * Fürgt einen Wert in den Baum ein.
	 *
	 * @param c
	 *            Wert, der eingefügt werden soll
	 * @return true, wenn das Einfügen geklappt hat. False, wenn der Wert
	 *         bereits im Baum vorhanden war.
	 * @throws IllegalArgumentException
	 *             Falls c == null
	 */
	public boolean addValue(V value) throws IllegalArgumentException;

	/**
	 * Liefert true, wenn der Wert bereits im Baum vorhanden ist
	 *
	 * @param c
	 *            zu überprüfender Wert
	 * @return true, wenn der Wert im Baum vorhanden ist
	 * @throws IllegalArgumentException
	 *             Fall c == null
	 */
	public boolean containsValue(V value) throws IllegalArgumentException;

	/**
	 * Löscht einen Wert aus dem Baum
	 *
	 * @param c
	 *            zu löschender Wert
	 * @return true, wenn der Wert vorhanden war (und daher gelöscht wurde)
	 * @throws IllegalArgumentException
	 *             Fall c == null
	 */
	public boolean removeValue(V value) throws IllegalArgumentException;

	/**
	 * Entfernt alle Knoten aus dem Baum
	 */
	public void clear();

	/**
	 * Gibt die RWL-Ordnung zurück
	 *
	 * @return RWL-Ordnung
	 */
	public List<V> toRWL();

	/**
	 * Gibt die LWR-Ordnung zu zurück
	 *
	 * @return LWR-Ordnung
	 */
	public List<V> toLWR();

	/**
	 * Gibt die LRW-Ordnung zu zurück
	 *
	 * @return LRW-Ordnung
	 */
	public List<V>toLRW();

	/**
	 * Gibt die RLW-Ordnung zu zurück
	 *
	 * @return RLW-Ordnung
	 */
	public List<V> toRLW();

	/**
	 * Gibt die WLR-Ordnung zu zurück
	 *
	 * @return WLR-Ordnung
	 */
	public List<V> toWLR();

	/**
	 * Gibt die WRL-Ordnung zu zurück
	 *
	 * @return WRL-Ordnung
	 */
	public List<V> toWRL();
}