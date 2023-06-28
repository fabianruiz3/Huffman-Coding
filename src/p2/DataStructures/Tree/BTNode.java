package p2.DataStructures.Tree;

import p2.Utils.BinaryTreePrinter.PrintableNode;

/**
 * Binary Tree Node Class
 * 
 * This class represents a binary tree node with key, value,
 * left child, right child, and parent.
 * @author Fabian Ruiz - fabianruiz3
 *
 * @param <K> Generic type for the key of the values to be stored in the nodes
 * @param <V> Generic type for the values to be stored in the nodes
 */
public class BTNode<K extends Comparable<? super K>, V extends Comparable<? super V>> implements PrintableNode, Comparable<BTNode<K,V>> {
	
	private K key;
	private V value;
	private BTNode<K, V> leftChild, rightChild, parent;

	public BTNode(K key, V value, BTNode<K, V> parent) {
		this.key = key;
		this.value = value;
		this.parent = parent;
		this.leftChild = this.rightChild = null;
	}
	
	public BTNode(K key, V value) {
		this(key, value, null);
	}

	public BTNode() {
		this(null, null, null);
	}
	
	public K getKey() {
		return key;
	}
	
	public void setKey(K key) {
		this.key = key;
	}
	
	public V getValue() {
		return value;
	}
	
	public void setValue(V value) {
		this.value = value;
	}
	
	public BTNode<K, V> getLeftChild() {
		return leftChild;
	}
	
	public void setLeftChild(BTNode<K, V> leftChild) {
		this.leftChild = leftChild;
	}
	
	public BTNode<K, V> getRightChild() {
		return rightChild;
	}
	
	public void setRightChild(BTNode<K, V> rightChild) {
		this.rightChild = rightChild;
	}
	
	public BTNode<K, V> getParent() {
		return parent;
	}
	
	public void setParent(BTNode<K, V> newParent) {
		parent = newParent;
	}
	
	public void clear() {
		key = null;
		value = null;
		leftChild = rightChild = parent = null;
	}
	
	/**
	 * Compares the current BTNode with another BTNode based on their keys and values.
	 * The method compares the keys of the two nodes. 
	 * If the keys are the same, it compares their values
	 * and returns the result. Otherwise, it returns the result of comparing the keys.
	 * 
	 * @param o the BTNode to be compared
	 * @return a negative integer, zero, or a positive integer as this BTNode is less than, equal to, or greater than the specified BTnode.
	 */
	@Override
	public int compareTo(BTNode<K, V> o) {
		
		// If the keys are the same, return the comparison of the values
		if(key.compareTo(o.getKey()) == 0)
			return value.compareTo(o.getValue());
		
		// Return the comparison of the keys
	    return key.compareTo(o.getKey());
	}

	/**
	 * The methods below are merely to comply with the PrintableNode interface,
	 * used by the BinaryTreePrinter class to nicely display a binary tree.
	 * Could have renamed the methods, but just didn't feel like it.
	 */
	
	@Override
	public PrintableNode getLeft() {
		return getLeftChild();
	}
	@Override
	public PrintableNode getRight() {
		return getRightChild();
	}
	@Override
	public String getText() {
		return key.toString() + ":" + value.toString();
	}

}