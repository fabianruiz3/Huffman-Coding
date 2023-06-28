package p2.DataStructures.SortedList;

import p2.DataStructures.Tree.BTNode;

/**
 * Implementation of a Sorted List using a Singly Linked List structure
 * 
 * @author Fabian Ruiz - fabianruiz3
 * @param <E> Generic comparable data type
 */
public class SortedLinkedList<E extends Comparable<? super E>> extends AbstractSortedList<E> {

	@SuppressWarnings("unused")
	private static class Node<E> {

		private E value;
		private Node<E> next;

		public Node(E value, Node<E> next) {
			this.value = value;
			this.next = next;
		}

		public Node(E value) {
			this(value, null); // Delegate to other constructor
		}

		public Node() {
			this(null, null); // Delegate to other constructor
		}

		public E getValue() {
			return value;
		}

		public void setValue(E value) {
			this.value = value;
		}

		public Node<E> getNext() {
			return next;
		}

		public void setNext(Node<E> next) {
			this.next = next;
		}

		public void clear() {
			value = null;
			next = null;
		}				
	} // End of Node class

	
	private Node<E> head; // First DATA node (This is NOT a dummy header node)
	
	/**
	 * Constructs an empty list, where the head is the first node.
	 * 
	 */
	public SortedLinkedList() {
		head = null;
		currentSize = 0;
	}
	
	/**
     * Adds an element to the list in its sorted position.
     * 
     * @param e the element to add to the list
     * @throws IllegalArgumentException if the element to be added is null
     */
	@Override
	public void add(E e) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when the new value is the smallest */
		
		// Check for null input
		if(e == null)
			throw new IllegalArgumentException();
		
		// Initialize node to be added
		Node<E> newNode = new Node<>(e);
		
		// Increase the size of the list
		currentSize++;
		
		// If the list is empty, the new node becomes the head
		if(head == null) {
			head = newNode;
			return;
		}
		
		// If the value to be added is smaller than the heads value, make it the new head
		if(head.getValue().compareTo(newNode.getValue()) >= 0) {
			newNode.setNext(head);
			head = newNode;
			return;
		}
		
		// Iterate through the list to find the position to add the new node
		Node<E> curNode = head;
		while(curNode.getNext() != null && curNode.getNext().getValue().compareTo(newNode.getValue()) <= 0) {
			curNode = curNode.getNext();
		}
		
		// Insert the new node
		newNode.setNext(curNode.getNext());
		curNode.setNext(newNode);
		
	}
	
	/**
     * Removes the first occurrence of the specified element from the list.
     * 
     * @param e the element to removed to the list
     * @return True if the element is found and removed, false otherwise
     */
	@Override
	public boolean remove(E e) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when the value is found at the head node */
		Node<E> rmNode;
		
		// Check for null input and empty list
		if(e == null || head == null)
			return false;
		
		// If the element to be removed is at the head, change the head, delete the old head, and decrease the size of the list
		if(head.getValue().equals(e)) {
			rmNode = head;
			head = head.getNext();
			rmNode.clear();
			rmNode = null;
			currentSize--;
			return true;
		}
		
		// Iterate through the list to find the node to be removed
		Node<E> curNode = head;
		while(curNode.getNext() != null) {
			
			// If the element is found, remove it and decrease the size of the list
			if(curNode.getNext().getValue().equals(e)) {
				rmNode = curNode.getNext();
				curNode.setNext(curNode.getNext().getNext());
				rmNode.clear();
				rmNode = null;
				currentSize--;
				return true;
			}
			curNode = curNode.getNext();
		}
		return false;
	}

	/**
	 * Removes and returns the element at the specified position in this list.
	 *
	 * @param index the index of the element to be removed
	 * @return The element previously at the specified position
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size())
	 */
	@Override
	public E removeIndex(int index) {
		/* TODO ADD CODE HERE */
		/* Special case: Be careful when index = 0 */
		
		// Check if the index is valid
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		
		Node<E> rmNode, curNode;
		E value = null;
		
		// If the value to be removed is at the head, change the head, delete the old head, and decrease the size of the list
		if(index == 0) {
			rmNode = head;
			value = head.getValue();
			head = head.getNext();
			rmNode.clear();
			rmNode = null;
			currentSize--;
			return value;
		}
		
		// Iterate through the list till before the node to be removed
		curNode = head;
		for(int i = 1; i < index; i++) {
			curNode = curNode.getNext();
		}
		
		// Remove the node at the specified index and decrease the size of the list
		rmNode = curNode.getNext();
		value = curNode.getNext().getValue();
		curNode.setNext(curNode.getNext().getNext());
		rmNode.clear();
		rmNode = null;
		currentSize--;
		return value; 
	}

	/**
	 * Retrieves the index of the first occurrence of the specified element in the list, or -1 if the list does not contain the element.
	 *
	 * @param e the element to search for in the list
	 * @return The index of the first occurrence of the specified element, or -1 if the list does not contain the element
	 */
	@Override
	public int firstIndex(E e) {
		/* TODO ADD CODE HERE */
		
		// Iterate through the list
		Node<E> curNode = head;
		for(int i = 0; curNode != null; curNode = curNode.getNext(), i++) {
			
			// If the element is found, return the index
			if(curNode.getValue().equals(e)) {
				return i;
			}
		}
		// Element not found
		return -1; 
	}
	
	/**
	 * Retrieves the element at the specified index in the list.
	 * 
	 * @param index the index of the element to be retrieved
	 * @return The element at the specified index in the list
	 * @throws IndexOutOfBoundsException if the index is out of range (index &lt; 0 || index &gt;= size())
	 */
	@Override
	public E get(int index) {
		/* TODO ADD CODE HERE */
		
		// Check if the index is valid
		if (index < 0 || index >= size())
			throw new IndexOutOfBoundsException();
		
		// Iterate through the list till the specified index
		Node<E> curNode = head;
		for(int i = 0; i < index; curNode = curNode.getNext(), i++);
		
		// Return the element at the specified index
		return curNode.getValue(); 
	}

	@SuppressWarnings("unchecked")
	@Override
	public Comparable<E>[] toArray() {
		int index = 0;
		Comparable[] theArray = new Comparable[size()]; // Cannot use Object here
		for(Node<E> curNode = this.head; index < size() && curNode  != null; curNode = curNode.getNext(), index++) {
			theArray[index] = curNode.getValue();
		}
		return theArray;
	}

}