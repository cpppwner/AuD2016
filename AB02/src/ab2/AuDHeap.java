package ab2;

/**
 * Schnittstelle, welche einen Min-Heap darstellt. Der Heap wird mit einer
 * maximalen Größe angelegt. Der Heap muss nicht voll mit Elemente belegt sein.
 * Eine dynamische Vergrößerung des Heaps (bei drohender oder tatsächlicher
 * Überfüllung) ist nicht zu implementieren, obwohl dies in der Praxis häufig
 * nötig ist. Keine Methode darf die Heap-Eigenschaft zerstören. Inital (im
 * Konstruktur) ist ein leerer Heap mit der Größe 37 anzulegen.
 *
 * @author Raphael Wigoutschnigg, Judith Michael, Peter Schartner
 */
public interface AuDHeap {

	/**
	 * Erzeugt einen leeren Heap der Größe heapSize. Ein bisher vorhandener Heap
	 * wird verworfen.
	 *
	 * @param heapSize
	 *            Größe des Heaps
	 * @throws IllegalStateException
	 *             falls heapSize < 0
	 */
	public void createEmptyHeap(int heapSize) throws IllegalArgumentException;

	/**
	 * Liefert die Anzahl der Elemente des Heaps zurück
	 *
	 * @return Anzahl der Elemente des Heaps
	 */
	public int elementCount();

	/**
	 * Liefert die Größe des Heaps zurück
	 *
	 * @return Größe des Heaps (nicht Anzahl der gespeicherten Elemente)
	 */
	public int size();

	/**
	 * Liefert den Heap (die Elemente) als Array zurück
	 *
	 * @return Heap als Array
	 */
	public int[] toArray();

	/**
	 * Erzeugt einen Heap der Größe heapSize und belegt diesen mit den
	 * übergebenen Daten. Ein bisher vorhandener Heap wird verworfen.
	 *
	 * @param data
	 *            die initialen Daten
	 * @param heapSize
	 *            Größe des Heaps
	 * @throws IllegalArgumentException
	 *             falls heapSize < data.length
	 */
	public void createHeap(int[] data, int heapSize)
			throws IllegalArgumentException;

	/**
	 * Fügt ein Element in den Heap ein, falls im Heap noch Platz ist. Ist der
	 * Heap voll, ist eine IllegalArgumentException zu werfen.
	 *
	 * @param element
	 *            einzufügendes Element
	 * @throws IllegalStateException
	 *             falls der Heap bereits voll ist
	 */
	public void addElement(int element) throws IllegalStateException;

	/**
	 * Löscht das erste Element aus dem Heap und gibt dieses Element zurück.
	 *
	 * @return Liefert das erste Element zurück
	 * @throws IllegalStateException
	 *             falls der Heap leer ist (und somit kein erstes Element hat)
	 */
	public int removeFirst() throws IllegalStateException;

	/**
	 * Löscht das Element an der Stelle idx aus dem Heap.
	 *
	 * @param idx
	 *            Der Index, dessen Element gelöscht werden soll
	 * @return Das gelöschte Element
	 * @throws IllegalArgumentException
	 *             falls an dem Index kein Element gespeichert ist (z.B. bei
	 *             elementCount() <= idx)
	 */
	public int remove(int idx) throws IllegalArgumentException;

}