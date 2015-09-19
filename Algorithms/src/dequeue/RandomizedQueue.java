package dequeue;

import java.util.Iterator;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private int N;
	private Node front, end;
	private class Node {
		Item item;
		Node right, left;
		Node(Item item, Node left, Node right) {
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
			front.left = end;
		} else {
			Node temp = new Node(item, null, end);
			end.left = temp;
			end = temp;
		}
		N++;
	}

	// remove and return a random item
	public Item dequeue() {
		if (N == 0) {
			throw new java.util.NoSuchElementException();
		}
		
		Item item;
		if (N == 1) {
			item = front.item;
			front = end = null;
		} else {
			int index = StdRandom.uniform(1, N + 1);
			Node temp = front;
			for (int i = 0; i < index - 1; i++) {
				temp = temp.left;
			}
			if (index == 1) {
				front = front.left;
			}
			if (index == N) {
				end = end.right;
			}
			// is left null?
			if (temp.left != null) {
				temp.left.right = temp.right;
			}
			// is right null?
			if (temp.right != null) {
				temp.right.left = temp.left;
			}
			item = temp.item;
		}
		N--;
		return item;
	}

	// return (but do not remove) a random item
	public Item sample() {
		if (N == 0) {
			throw new java.util.NoSuchElementException();
		}
		int index = StdRandom.uniform(1, N + 1);
		Node temp = front;
		for (int i = 0; i < index - 1; i++) {
			temp = temp.left;
		}
		return temp.item;
	}

	// return an independent iterator over items in random order
	public Iterator<Item> iterator() {
		Iterator<Item> it = new IteratorOfRand<Item>(N);
		return it;
	}
	private class IteratorOfRand<Item> implements Iterator<Item> {
		int num = N;
		Node temp = front;
		Object[] rand;
		
		IteratorOfRand(int N) {
			rand = new Object[N];
			for (int i = 0; i < N; i++) {
				rand[i] = temp.item;
			}
		}
		
		@Override
		public boolean hasNext() {
			return N > 0;
		}

		@Override
		public Item next() {
			if (num == 0) throw new java.util.NoSuchElementException();
			int index = StdRandom.uniform(num);
			@SuppressWarnings("unchecked")
			Item result = (Item)rand[index];
			rand[index] = null;
			swap(rand, index, num - 1);
			num--;
			return result;
		}
		
		private void swap(Object[] arr, int a, int b) {
			Object obj = arr[a];
			arr[a] = arr[b];
			arr[b] = obj;
		}
		
	}

	// unit testing
	public static void main(String[] args) {
		RandomizedQueue<Integer> unittest = new RandomizedQueue<Integer>();
		unittest.enqueue(1);
		unittest.enqueue(2);
		unittest.enqueue(3);
		unittest.enqueue(4);
		unittest.enqueue(5);
		unittest.enqueue(6);
		unittest.enqueue(7);
		for (int i = 0; i < 7; i++) {
			System.out.println(i+"th sample:" + unittest.sample() +" rand:"+unittest.dequeue());
			
		}
	}
}