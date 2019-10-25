/**
 *
 */
package org.clinicalontology.fhir.tools.ig.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author dtsteven
 *
 */
public class FigTreeNode<K, P> implements Iterable<FigTreeNode<K, P>> {

	private final P payload;
	private final K key;
	private final FigTreeNode<K, P> parent;
	private Map<K, FigTreeNode<K, P>> children = Collections.emptyMap();

	public FigTreeNode() {
		this(null, null, null); // root of the tree
	}

	private FigTreeNode(P payload, FigTreeNode<K, P> parent, K key) {
		this.payload = payload;
		this.parent = parent;
		this.key = key;
	}

	/**
	 * add a child node to this tree.
	 *
	 * @param key
	 * @param payload
	 * @return null if node already exists otherwise this for chained calls
	 */
	public FigTreeNode<K, P> add(K key, P payload) {

		if (this.children.containsKey(key)) {
			return null;
		}

		if (this.children.isEmpty()) {
			this.children = new LinkedHashMap<>();
		}

		FigTreeNode<K, P> childNode = new FigTreeNode<K, P>(payload, this, key);
		this.children.put(key, childNode);
		return childNode;
	}

	/**
	 * traverse tree with array of keys and add on last value
	 *
	 * @param keys    array of keys to traverse and find next loc
	 * @param payload
	 * @return null if node exists or path not found otherwise this
	 */
	public FigTreeNode<K, P> add(K[] keys, P payload) {
		if (keys == null || keys.length == 0) {
			return null;
		}
		K[] local = Arrays.copyOf(keys, keys.length - 1);
		FigTreeNode<K, P> node = this.getNode(local);
		if (node != null) {
			return node.add(keys[keys.length - 1], payload);
		} else {
			return null;
		}
	}

	public P get(K key) {
		FigTreeNode<K, P> result = this.getNode(key);
		return result != null ? result.payload : null;
	}

	public P get(K[] keys) {
		FigTreeNode<K, P> result = this.getNode(keys);
		return result != null ? result.payload : null;
	}

	public FigTreeNode<K, P> parent() {
		return this.parent;
	}

	public P payload() {
		return this.payload;
	}

	public K key() {
		return this.key;
	}

	public FigTreeNode<K, P> getNode(K key) {
		return this.children.get(key);
	}

	public FigTreeNode<K, P> getNode(K[] keys) {

		FigTreeNode<K, P> current = this;
		for (K key : keys) {
			current = current.getNode(key);
			if (current == null) {
				return null;
			}
		}
		return current;
	}

	@Override
	public Iterator<FigTreeNode<K, P>> iterator() {
		return this.children.values().iterator();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.payload != null) {
			sb.append(this.payload.toString());
		}
		if (!this.children.isEmpty()) {
			sb.append(this.children.toString());
		}
		return sb.toString();
	}

}