package dequeue;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private int N;
	Node front, end;
	private class Node {
		Item item;
		Node right, left;
		Node(Item item, Node right, Node left) {
			this.item = item;
			this.right = right;
			this.left = left;
		}
	}
	// construct an empty randomized queue
	public RandomizedQueue() {
		N = 0;
		front = null;
		end = null;
	}

	// is the queue empty?
	public boolean isEmpty() {
		return N == 0;
	}

	// return the number of items on the queue
	public int size() {
		return N;
	}

	// add the item
	public void enqueue(Item item) {
		if (N == 0) {
			front = end = new Node(item, null, null);
		} else if (N == 1){
			end = new Node(item, null, front);
		} else {
			Node temp = new Node(item, null, end);
			end = temp;
		}
		N++;
	}

	// remove and return a random item
	public Item dequeue() {
		if (N == 0) {
			throw new java.util.NoSuchElementException();
		}
		Item item = front.item;
		if (N == 1) {
			front = end = null;
		} else {
			front = front.left;
			front.right = null;
		}
		N--;
		return item;
	}

	// return (but do not remove) a random item
	public Item sample() {
		return null;
	}

	// return an independent iterator over items in random order
	public Iterator<Item> iterator() {
		return new Iterator<Item>(){
			int index = N;
			Node current = front;
			@Override
			public boolean hasNext() {
				return N > 0;
			}

			@Override
			public Item next() {
				if (index == 0) throw new java.util.NoSuchElementException();
				return null;
			}
			
		};
	}

	// unit testing
	public static void main(String[] args) {
	}
}