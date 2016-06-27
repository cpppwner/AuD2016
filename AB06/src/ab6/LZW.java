package ab6;

/**
 * Definiert die Schnittstelle zur Implementierung des LZW-Verfahrens. Das
 * Verfahren basiert auf einem 8-Bit Grundalphabet (ein byte) und erlaubt eine
 * frei wählbare Codetabellengröße, welche in Bit angegeben wird. Werden Daten
 * codiert, so sind die codierten Daten möglichst platzsparend zu speichern (dh
 * 2x12 Bit werden nicht in 2x2 Byte abgelegt sondern aneinandergehängt in 3
 * Byte). Ist die auszugebende Bitlänge kein Vielfaches von 8, sind am Ende der
 * Daten entsprechend viele 0-Bits anzufügen.
 *
 * @author Raphael Wigoutschnigg
 *
 */
public interface LZW {
	/**
	 * Setzt die größe der Codetabelle, welche verwendet werden soll. size ist
	 * in Bit (z.B. 10 Bit bedeutet Codes von 0 bis 1023
	 *
	 * @param size
	 * @throws IllegalStateException
	 *             falls die Codierung oder Decodierung bereits begonnen hat und
	 *             noch nicht beendet ist
	 * @throws IllegalArgumentException
	 *             falls size < 9 ist.
	 */
	public void setCodetableSize(int size) throws IllegalStateException, IllegalArgumentException;

	/**
	 * Gibt an, ob codiert oder decodiert werden soll
	 *
	 * @param cmode
	 *            ob es sich um einen Codierer (true) oder Decodierer (false)
	 *            handelt.
	 * @throws IllegalStateException
	 *             falls die Codierung oder Decodierung bereits begonnen hat und
	 *             noch nicht beendet ist
	 */
	public void setMode(boolean cmode) throws IllegalStateException;

	/**
	 * Übergibt Daten an die (De-)Codierung. Die Methode kann mehrfach
	 * aufgerufen werden. Wird die Methode mehrfach aufgerufen, so wird die
	 * (De-)Codierung nicht von vorne gestartet sondern läuft dort weiter, wo
	 * sie stehen geblieben ist (Zustand bleibt erhalten).
	 *
	 * @param data
	 *            die zu (de-)codierenden Daten.
	 * @throws IllegalStateException
	 *             falls size oder cmode nicht gesetzt sind.
	 * @throws IllegalArgumentException
	 *             falls data==null ist
	 */
	public void data(byte[] data) throws IllegalStateException, IllegalArgumentException;

	/**
	 * Beendet die Operation und liefert alle berechneten Daten zurück und setzt
	 * den (De-)Codierer zurück. Danach können wieder von vorne neue Daten
	 * übergebenen werden. Size und cmode bleiben bestehen, können aber wieder
	 * neue gesetzt werden.
	 *
	 * @return die berechneten Daten
	 * @throws IllegalStateException
	 *             falls size oder cmode nicht gesetzt sind.
	 */
	public byte[] finish() throws IllegalStateException;

	/**
	 * Setzt den (De-)Codierer auf den Startzustand zurück. size und cmode sind
	 * wieder zu setzen.
	 */
	public void clear();

}
