package at.syssec.ss15.aud.ab1;


/**
 * Interface für Suchalgorithmen. Jeder Algorithmus arbeitet In-Place.
 * 
 * @author Raphael Wigoutschnigg
 */
public interface SearchAlgos{

	/**
	 * Sortiert die Daten anhand des BubbleSort-Algorithmus
	 * @param data ... zu sortierende Daten
	 */
	public void BubbleSort(int[] data);
	
	/**
	 * Sortiert die Daten anhand des Qicksort-Algorithmus (Pivotelement ganz rechts).
	 * @param data ... zu sortierende Daten
	 */
	public void QuickSort1(int[] data);
	
	/**
	 * Sortiert die Daten anhand des Qicksort-Algorithmus (Pivotelement als median of three).
	 * @param data ... zu sortierende Daten
	 */
	public void QuickSort3(int[] data);
	
	/**
	 * Sortiert die Daten anhand des Qicksort-Algorithmus (Pivotelement als median for five).
	 * @param data ... zu sortierende Daten
	 */
	public void QuickSort5(int[] data);
	
	
}
