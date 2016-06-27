package ab4;

/**
 * Stellt einen generischen Knoten des Baumes dar, welcher einen Wert (ungleich
 * null) speichert.
 *
 * @author Raphael Wigoutschnigg, Judith Michael, Peter Schartner
 *
 */
public class Node<V> {
	/**
	 * Erzeugt einen Knoten mit dem Wert value. Der Wert value kann nicht mehr
	 * geändert werden, weil dadurch die Konsistanze eines Baumes verletzt
	 * werden könnte.
	 *
	 * @param value
	 *            Wert des Knotens (read-only)
	 * @throws IllegalArgumentException
	 *             Falls value == null ist
	 */
	public Node(V value) throws IllegalArgumentException {
		this.value = value;
	}

	/**
	 * Linkes Kind des Knotens
	 */
	private Node<V> left;

	/**
	 * rechtes Kind des Knotens
	 */
	private Node<V> right;

	/**
	 * Elternknoten
	 */
	private Node<V> parent;

	/**
	 * Wert des Knotens
	 */
	private V value;

	/**
	 * Höhe des Subbaumes dieses Knotens
	 */
	private int heightOfSubtree;

	/**
	 * Liefert das linke Kind des Knotens
	 *
	 * @return linkes Kind
	 */
	public Node<V> getLeft() {
		return left;
	}

	/**
	 * Setzt das linke Kind des Knotens
	 *
	 * @param left
	 *            linkes Kind
	 */
	public void setLeft(Node<V> left) {
		//Zuerst vom alten "left" den Parent löschen
		if (this.left != null)
			this.left.setParent(null);
		this.left = left;
		if (left != null)
			left.setParent(this);
	}

	/**
	 * Liefert das rechte Kind des Knotens
	 *
	 * @return rechtes Kind
	 */
	public Node<V> getRight() {
		return right;
	}

	/**
	 * Setzt das rechte Kind des Knotens
	 *
	 * @param right
	 *            rechtes Kind
	 */
	public void setRight(Node<V> right) {
		//Zuerst vom alten "right" den Parent löschen
		if (this.right != null)
			this.right.setParent(null);

		this.right = right;
		if (right != null)
			right.setParent(this);
	}

	/**
	 * Liefert den Elternknoten
	 *
	 * @return Elternknoten
	 */
	public Node<V> getParent() {
		return parent;
	}

	/**
	 * Setzt den Elternknoten
	 *
	 * @param parent
	 *            Elternknoten
	 */
	private void setParent(Node<V> parent) {
		this.parent = parent;
	}

	/**
	 * Liefert den Wert des Knotens
	 *
	 * @return Wert des Knotens
	 */
	public V getValue() {
		return value;
	}

	/**
	 * Liefert die Höhe des Subbaumes dieses Knotens
	 * @return Höhe des Subbaums
	 */
	public int getHeightOfSubtree() {
		return heightOfSubtree;
	}

	/**
	 * Setzt die Höhe des Subbaums
	 * @param heightOfSubtree Höhe des Subbaums
	 */
	public void setHeightOfSubtree(int heightOfSubtree) {
		this.heightOfSubtree = heightOfSubtree;
	}

	/**
	 * Liefert die Höhendifferenz der beiden Teilbäume. Liefert nur dann korrekte Werte, wenn getHeightOfSubtree korrekte Werte liefert
	 * @return Höhendifferenz der beiden Teilbäume
	 */
	public int getHeightDiff()
	{
		if(right == null && left == null)
			return 0;

		if(right != null && left == null)
			return right.getHeightOfSubtree() + 1;

		if(right == null && left != null)
			return -left.getHeightOfSubtree() - 1;

		if(right != null && left != null)
			return right.getHeightOfSubtree() - left.getHeightOfSubtree();

		return 0;

	}
}