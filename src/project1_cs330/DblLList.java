/**
 * 
 */
package project1_cs330;

import java.util.*;

/**
 * @author Janet Gordon CS310 Nordstrom Project 1
 * 		   Double Linked List Class that has inner classes DNode for node
 *         construction and LinkedListIterator for iterations through list
 *         DblLList methods are: insert() to add new nodes to list remove() to
 *         remove a node from the list isEmpty() to return whether the list is
 *         empty or not getNode() to return a specified node via index location
 *         size() to return the real size of the list, minus dummy nodes
 */
public class DblLList<String> implements Iterable<String> {
	/*
	 * Private inner class for nodes for double linked list contains String
	 * data, a previous node and next node to point to.
	 */
	class DNode {

		public String data;
		public DNode next;
		public DNode prev;

		/*
		 * Constructor for DNode class, requires String data, prev and next are
		 * automatically set to null
		 */
		public DNode(String string) {
			data = string;
			next = null;
			prev = null;

		}

		public String getData() {
			// returns the String data for the node that calls it
			return data;
		}

		public boolean hasPrev() {
			// returns true if prev is not null, false if prev is null
			return prev != null;
		}

		public DNode getPrev() {
			// returns previous node in list.
			if (!hasPrev())
				return null;
			return prev;
		}

		public boolean hasNext() {
			// returns true if next is not null, false if it is null.
			return next != null;
		}

		public DNode getNext() {
			// returns the next node in list.
			if (!hasNext())
				return null;
			return next;
		}
	}

	// private class variables for DblLList
	protected int size;
	protected DNode head, tail;

	public DblLList() {
		/*
		 * Constructor for DblLList. Head and Tail dummy nodes are only nodes in
		 * list, it is considered ' empty' and size set to zero
		 */
		head = new DNode(null);
		tail = new DNode(null);
		head.next = tail;
		tail.prev = head;
		size = 0;
	}

	public void remove(int index) {
		// removes specified node from list and decrements list size by 1
		DNode current = getNode(index, 1, size);
		current.prev.next = current.next;
		current.next.prev = current.prev;
		size--;

	}

	public DNode getNode(int index, int low, int high) {
		/*
		 * method returns the node at position index, which must be between low
		 * and high bounds. If index is out of bounds, will throw exception. If
		 * index is in the first half, begins search at head, otherwise begins
		 * search at tail and reverse searches.
		 */
		DNode p = null;
		if (index < low || index > high)
			throw new IndexOutOfBoundsException();
		if (index <= size() / 2) {
			p = head;
			for (int i = 0; i < index; i++)
				p = p.next;
		} else if (index > size() / 2) {
			p = tail;
			for (int i = size(); i >= index; i--)
				p = p.prev;
		}
		return p;
	}

	public DNode getFirst() {
		/*
		 * method returns the node at first real node position. If list is
		 * empty, will throw exception.
		 */
		if (head.next == null)
			throw new NoSuchElementException();

		return head.next;
	}

	public DNode getHead() {
		// returns head node
		return head;
	}

	public boolean isEmpty() {
		// returns true if only dummy nodes head and tail are in list, false if
		// not
		return (head.next == tail);
	}

	public int size() {
		// returns size of real nodes in list, does not count dummy nodes
		return size;
	}

	public void insert(String str, int index) {
		/*
		 * inserts a new node after a specified node in list and increments size
		 * of list by 1
		 */
		DNode current = getNode(index, 0, size);
		DNode newNode = new DNode(str);
		newNode.prev = current;
		newNode.next = current.next;
		newNode.prev.next = newNode;
		newNode.next.prev = newNode;
		size++;

	}

	public void clear() {
		// removes all nodes from list(other than head and tail)
		for (int i = 1; i <= size(); i++)
			remove(i);

	}

	public String getData(int index) {
		// returns the String data of a specified index in the list
		return getNode(index, 0, size).getData();
	}

	public void display(int index) {
		// displays the index and data of a node at the given index in the list
		System.out.printf("%d: %s\n", index, getNode(index, 0, size).getData());
	}

	// private class Iterator() implementation for DblLList
	private class MyIterator implements Iterator<String> {

		private DNode start;

		// Constructor for Iterator class
		public MyIterator() {
			start = head;
		}

		@Override
		public boolean hasNext() {
			// returns true if there is a next node that is not the tail, false
			// otherwise
			return start.next != tail;
		}

		@Override
		public String next() {
			// returns the data of the next node and increments the list by 1
			if (!hasNext())
				throw new NoSuchElementException();
			String str = start.getNext().data;
			start = start.next;
			return str;
		}

	}

	@Override
	public Iterator<String> iterator() {
		// returns Iterator for DblLList
		return new MyIterator();
	}
}
