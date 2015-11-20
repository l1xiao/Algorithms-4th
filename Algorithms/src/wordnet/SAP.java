package wordnet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;

public class SAP {
	Digraph G;

	// constructor takes a digraph (not necessarily a DAG)
	public SAP(Digraph G) {
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
				if (length < sap(vertice1, vertice2)[1]) {
					p1 = vertice1;
					p2 = vertice2;
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
		qv.add(v);
		qv.add(-1);
		qw.add(w);
		qw.add(-1);
		int count = 0;
		while (true) {
			count++;
			Set<Integer> neighborOfp = new HashSet<>();
			Set<Integer> neighborOfq = new HashSet<>();
			while (qv.peek()!= -1) {
				neighborOfp.addAll((Collection<Integer>) G.adj(qv.poll()));
			}
			while (qw.peek() != -1) {
				neighborOfq.addAll((Collection<Integer>) G.adj(qw.poll()));
			}
			Set<Integer> intersection = new HashSet<Integer>(neighborOfp);
			intersection.retainAll(neighborOfq);
			// find solution
			if (intersection.size() > 0) {
				result[0] = (int) intersection.toArray()[0];
				result[1] = count;
				break;
			}
			// find no solution 
			if (neighborOfq.size() == 0 || neighborOfp.size() == 0) {
				result[0] = -1;
				result[1] = -1;
				break;
			} else {
				// add neighbors to new level
				qv.addAll(neighborOfq);
				qw.addAll(neighborOfp);
				qv.add(-1);
				qw.add(-1);
			}
		}
		return result;
	}

	// do unit testing of this class
	public static void main(String[] args) {
	}
}