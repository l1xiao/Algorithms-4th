package wordnet;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
	private Digraph G;

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
		if (G == null) {
			throw new java.lang.NullPointerException();
		}
		this.G = new Digraph(G);
	}

	// length of shortest ancestral path between v and w; -1 if no such path
	public int length(int v, int w) {
		return sap(v, w)[1];
	}

	// a common ancestor of v and w that participates in a shortest ancestral
	// path; -1 if no such path
	public int ancestor(int v, int w) {
		return sap(v, w)[0];
	}

	// length of shortest ancestral path between any vertex in v and any vertex
	// in w; -1 if no such path
	public int length(Iterable<Integer> v, Iterable<Integer> w) {
		int length = Integer.MAX_VALUE;
		for (int vertice1 : v) {
			for (int vertice2 : w) {
				length = Math.min(sap(vertice1, vertice2)[1], length);
			}
		}
		return length;
	}

	// a common ancestor that participates in shortest ancestral path; -1 if no
	// such path
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
		int length = Integer.MAX_VALUE;
		Integer p1 = null, p2 = null;
		for (int vertice1 : v) {
			for (int vertice2 : w) {
				int temp = sap(vertice1, vertice2)[1];
				if (length > temp) {
					p1 = vertice1;
					p2 = vertice2;
					length = temp;
				}

			}
		}
		return sap(p1, p2)[0];
	}

	private int[] sap(int v, int w) {
		// TODO v == w condition
		int[] result = new int[2];
		Queue<Integer> qv = new LinkedList<>();
		Queue<Integer> qw = new LinkedList<>();
		Map<Integer, Integer> neighborOfp = new HashMap<>();
		Map<Integer, Integer> neighborOfq = new HashMap<>();
		qv.add(v);
		qv.add(-1);
		qw.add(w);
		qw.add(-1);
		int count1 = 0;
		int count2 = 0;
		while (true) {
			// in a graph, traversal a
			while (!qv.isEmpty() && qv.peek() != -1) {
				if (!G.adj(qv.peek()).iterator().hasNext()) {
					if (neighborOfp.containsKey(qv.peek())) {
						qv.poll();
						continue;
					}
					neighborOfp.put(qv.poll(), count1);
				} else {
					neighborOfp.put(qv.peek(), count1);
					count1++;
					for (Integer integer : G.adj(qv.poll())) {

						// if (neighborOfp.containsKey(integer)) continue;
						neighborOfp.put(integer, count1);
						qv.add(integer);
					}
				}
			}
			if (!qv.isEmpty() && qv.peek() == -1) {
				qv.poll();
				if (!qv.isEmpty()) {
					qv.add(-1);
				}
			}
			while (!qw.isEmpty() && qw.peek() != -1) {
				if (!G.adj(qw.peek()).iterator().hasNext()) {
					if (neighborOfq.containsKey(qw.peek())) {
						qw.poll();
						continue;
					}
					neighborOfq.put(qw.poll(), count2);
				} else {
					neighborOfq.put(qw.peek(), count2);
					count2++;
					for (Integer integer : G.adj(qw.poll())) {

						neighborOfq.put(integer, count2);
						qw.add(integer);
					}
				}
			}
			if (!qw.isEmpty() && qw.peek() == -1) {
				qw.poll();
				if (!qw.isEmpty()) {
					qw.add(-1);

				}
			}
			Set<Integer> intersection = new HashSet<Integer>(
					neighborOfp.keySet());
			intersection.retainAll(neighborOfq.keySet());
			// find solution
			if (intersection.size() > 0) {
				int min = Integer.MAX_VALUE;
				int ancestor = -1;
				int length = -1;
				for (Integer temp : intersection) {
					length = neighborOfp.get(temp) + neighborOfq.get(temp);
					if (length < min) {
						min = length;
						ancestor = temp;
					}
				}
				result[0] = ancestor;
				result[1] = min;
				break;
			}
			// find no solution
			if (qw.size() == 0 && qv.size() == 0) {
				result[0] = -1;
				result[1] = -1;
				break;
			}
		}
		return result;
	}

	// do unit testing of this class
	// public static void main(String[] args) {
	// Digraph G = new Digraph(8);
	// G.addEdge(0, 2);
	// G.addEdge(2, 3);
	// G.addEdge(1, 3);
	// G.addEdge(3, 5);
	// G.addEdge(4, 5);
	// G.addEdge(6, 7);
	// SAP test = new SAP(G);
	// int v = 5;
	// int w = 5;
	// System.out.println(test.ancestor(v, w) + "   length:" + test.length(v,
	// w));
	// }
	public static void main(String[] args) {
		In in = new In("./wordnet/digraph1.txt");
		// In in = new In(args[0]);
		Digraph G = new Digraph(in);
		SAP sap = new SAP(G);
		// while (!StdIn.isEmpty()) {
		// int v = StdIn.readInt();
		// int w = StdIn.readInt();
		// System.out.println(v + " " + w);
		// int length = sap.length(v, w);
		// int ancestor = sap.ancestor(v, w);
		// StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
		// }
		for (int v = 0; v < 12; v++) {
			for (int w = v + 1; w < 13; w++) {
				System.out.println(v + " " + w);
				int length = sap.length(v, w);
				int ancestor = sap.ancestor(v, w);
				StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
			}
		}
	}
}