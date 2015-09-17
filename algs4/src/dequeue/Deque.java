package dequeue;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
	
	Node front, end;
	private class Node {
		Item item;
		Node left, right;
		Node(Item item, Node left, Node right) {
			this.item = item;
			this.left = left;
			this.right = right;
		}
	}
	
	private int N;
	// construct an empty deque
	public Deque() {
		front = end = null;
		N = 0;
	}

	// is the deque empty?
	public boolean isEmpty() {
		return N == 0;
	}

	// return the number of items on the deque
	public int size() {
		return N;
	}

	// add the item to the front
	public void addFirst(Item item) {
		if (N == 0) {
			front = end = new Node(item, null, null);
		} else if (N == 1){
			front = new Node(item, null, end);
		} else {
			Node temp = new Node(item, null, front);
			front = temp;
		}
		N++;
	}

	// add the item to the end
	public void addLast(Item item) {
		if (N == 0) {
			end = front = new Node(item, null, null);
		} else if (N == 1) {
			end = new Node(item, front, null);
		} else {
			Node temp = new Node(item , end, null);
			end = temp;
		}
		N++;
	}

	// remove and return the item from the front
	public Item removeFirst() {
		if (N == 0) {
			throw new java.util.NoSuchElementException();
		}
		Item result = front.item;
		if (N == 1) {
			front = end = null;
		} else if (N == 2) {
			front = end;
			end.left = end.right = null;
		} else {
			front = front.right;
			front.left = null;
		}
		N--;
		return result;
	}

	// remove and return the item from the end
	public Item removeLast() {
		if (N == 0) {
			throw new java.util.NoSuchElementException();
		}
		Item result = end.item;
		if (N == 1) {
			end = front = null;
		} else if (N == 2) {
			end = front;
			front.left = front.right = null;
		} else {
			end = end.left;
			end.right = null;
		}
		N--;
		return result;
	}

	// return an iterator over items in order from front to end
	public Iterator<Item> iterator() {
		return new Iterator<Item>(){
			int index = N;
			Node current = front;
			@Override
			public boolean hasNext() {
				return index > 0;
			}

			@Override
			public Item next() {
				if (index == 0) throw new java.util.NoSuchElementException();
				Node temp = current;
				current = current.right;
				index--;
				return temp.item;
			}
			
		};
	}

	// unit testing
	public static void main(String[] args) {
	}
}