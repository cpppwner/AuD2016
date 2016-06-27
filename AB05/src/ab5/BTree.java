package ab5;

import java.util.List;

/**
 * Schnittstelle für die Implementierung eines BBaums
 * 
 * @author Raphael Wigoutschnigg
 *
 * @param <V>
 *            Typ der Werte (muss Comparable sein)
 */
public interface BTree<V extends Comparable<V>> {
	/**
	 * Setzt den Minimalgrad des Baumes. Diese Methode muss als erstes und darf
	 * kein weiteres Mal aufgerufen werden, solange clear nicht aufgerufen wird.
	 * 
	 * @param t
	 *            Minimalgrad
	 * @throws IllegalStateException
	 *             falls die Methode ein zweites Mal aufgerufen wird, ohne dass
	 *             clear aufgerufen wurde.
	 * @throws IllegalArgumentException
	 *             Falls t ungültigen ist (t<2)
	 */
	public void setMinDegree(int t) throws IllegalStateException, IllegalArgumentException;

	/**
	 * Fügt einen Wert in den Baum ein. Duplikate sind im Baum nicht erlaubt.
	 * 
	 * @param value
	 *            einzufügender Wert
	 * @return false, wenn der Wert bereits vorhanden war, andernfalls true
	 * @throws IllegalStateException
	 *             Falls setMinDegree noch nicht aufgerufen wurde
	 * @throws IllegalArgumentException
	 *             Falls value == null ist
	 */
	public boolean add(V value) throws IllegalStateException, IllegalArgumentException;

	/**
	 * Löscht einen Wert aus dem Baum
	 * 
	 * @param value
	 *            zu löschender Wert
	 * @return true, falls der Wert vorhanden war. Sonst false.
	 * @throws IllegalStateException
	 *             Falls setMinDegree noch nicht aufgerufen wurde
	 * @throws IllegalArgumentException
	 *             Falls value == null ist
	 */
	public boolean delete(V value) throws IllegalStateException, IllegalArgumentException;

	/**
	 * Prüft, ob ein Wert bereits im Baum vorhanden ist
	 * 
	 * @param value
	 * @return Liefert den gespeicherten Wert, sonst null.
	 * @throws IllegalStateException
	 *             Falls setMinDegree noch nicht aufgerufen wurde
	 * @throws IllegalArgumentException
	 *             Falls value == null ist
	 */
	public V contains(V value) throws IllegalStateException, IllegalArgumentException;

	/**
	 * Liefert den Root-Knoten des Baumes.
	 * 
	 * @return Root-Knoten
	 * @throws IllegalStateException
	 *             Falls setMinDegree noch nicht aufgerufen wurde
	 */
	public BNode<V> getRoot() throws IllegalStateException;

	/**
	 * Gibt die Werte des Baumes (sortiert) als Array zurück
	 * 
	 * @return sortiertes Array der Werte
	 * @throws IllegalStateException
	 *             Falls setMinDegree noch nicht aufgerufen wurde
	 */
	public List<V> toList() throws IllegalStateException;

	/**
	 * Setzt den Baum zurück. setMinDegree muss wieder aufgerufen werden
	 */
	public void clear();
}
