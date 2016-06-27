package ab5;

import java.util.List;

/**
 * Einfacher B-Baum Knoten
 * 
 * @author Raphael Wigoutschnigg
 *
 * @param <V>
 *            Typ der Werte (muss Comparable sein)
 */
public class BNode<V extends Comparable<V>> {
	private List<V> values;
	private List<BNode<V>> children;

	public List<V> getValues() {
		return values;
	}

	public void setValues(List<V> values) {
		this.values = values;
	}

	public List<BNode<V>> getChildren() {
		return children;
	}

	public void setChildren(List<BNode<V>> children) {
		this.children = children;
	}
}
