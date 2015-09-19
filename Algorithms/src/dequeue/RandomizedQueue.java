package dequeue;

import java.util.Iterator;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
	private int N;
	private Item[] item;

	// construct an empty randomized queue
	@SuppressWarnings("unchecked")
	public RandomizedQueue() {
		item = (Item[]) new Object[2];
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
		if (item == null)
			throw new java.lang.NullPointerException();
		if (N > this.item.length / 2) {
			resize(2 * this.item.length);
		}
		this.item[N] = item;
		N++;
	}

	// remove and return a random item
	public Item dequeue() {
		if (N == 0) {
			throw new java.util.NoSuchElementException();
		}
		int index = StdRandom.uniform(N);
		if (N < this.item.length / 4) {
			resize(this.item.length / 2);
		}
		Item temp = this.item[index];
		this.item[index] = this.item[N - 1];
		this.item[N - 1] = null;
		N--;
		return temp;
	}

	// return (but do not remove) a random item
	public Item sample() {
		if (N == 0) {
			throw new java.util.NoSuchElementException();
		}
		int index = StdRandom.uniform(N);
		return this.item[index];
	}

	// return an independent iterator over items in random order
	public Iterator<Item> iterator() {
		Iterator<Item> it = new IteratorOfRand();
		return it;
	}

	private class IteratorOfRand implements Iterator<Item> {
		private int used = 0;
		private int size = N;
		private int[] shuffle;

		IteratorOfRand() {
			shuffle = new int[N];
			for (int i = 0; i < N; i++) {
				shuffle[i] = i;
			}
			StdRandom.shuffle(shuffle);
		}

		@Override
		public boolean hasNext() {
			if (size != N) {
				size = N;
				shuffle = new int[N];
				for (int i = 0; i < N; i++) {
					shuffle[i] = i;
				}
				StdRandom.shuffle(shuffle);
			}
			if (used == N) {
				used = 0;
				return false;
			} else {
				return true;
			}
		}

		@Override
		public Item next() {
			if (!this.hasNext())
				throw new java.util.NoSuchElementException();
			Item result = item[shuffle[used]];
			used++;
			return result;
		}
	}

	@SuppressWarnings("unchecked")
	private void resize(int n) {
		Item[] temp = this.item;
		item = (Item[]) new Object[n];
		int j = 0;
		for (int i = 0; i < N; i++) {
			item[j++] = temp[i];
		}
	}

	// unit testing
	public static void main(String[] args) {
		RandomizedQueue<Integer> unittest = new RandomizedQueue<Integer>();
		unittest.enqueue(1);
		unittest.enqueue(2);
		Iterator it =  unittest.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
		unittest.enqueue(3);
		while(it.hasNext()) {
			System.out.println(it.next());
		}
//		for (int i = 0; i < 7; i++) {
//		 System.out.println(i+"th sample:" + unittest.sample()
//		 +" rand:"+unittest.dequeue());
//		
//		 }
	}
}