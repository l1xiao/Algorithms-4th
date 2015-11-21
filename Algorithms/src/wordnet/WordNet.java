package wordnet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

public class WordNet {
	private Map<Integer, String> synsets = new HashMap<>();
	private Map<Integer, Set<Integer>> hypernyms = new HashMap<>();
	private Map<String, Set<Integer>> findId = new HashMap<>();
	private Digraph G;
	private SAP sap;

	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		// read file and store data
		In in = new In(synsets);
		while (!in.isEmpty()) {
			String line = in.readLine();
			String[] token = line.split(",");
			Integer id = Integer.parseInt(token[0]);
			ArrayList<String> nouns = new ArrayList<>(Arrays.asList(token[1]
					.split(" ")));
			this.synsets.put(id, token[1]);
			for (String noun : nouns) {
				if (findId.containsKey(noun)) {
					findId.get(noun).add(id);
				} else {
					Set<Integer> set = new HashSet<>();
					set.add(id);
					findId.put(noun, set);
				}

			}
		}
		in = new In(hypernyms);
		while (!in.isEmpty()) {
			String line = in.readLine();
			String[] token = line.split(",");
			Set<Integer> nouns = new HashSet<>();
			for (String hypernym : token) {
				if (hypernym != token[0]) {
					nouns.add(Integer.parseInt(hypernym));
				}
			}
			if (this.hypernyms.containsKey(Integer.parseInt(token[0]))) {
				this.hypernyms.get(Integer.parseInt(token[0])).addAll(nouns);
			} else {
				this.hypernyms.put(Integer.parseInt(token[0]), nouns);
			}
		}
		// build the graph
		G = new Digraph(this.synsets.size());
		for (Integer v : this.synsets.keySet()) {
			if (this.hypernyms.containsKey(v)) {
				for (Integer w : this.hypernyms.get(v)) {
					G.addEdge(v, w);
				}
			}
		}
		// this.synsets.clear();
		this.hypernyms.clear();
		this.sap = new SAP(this.G);
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return findId.keySet();
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		return findId.keySet().contains(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		return sap.length(findID(nounA), findID(nounB));
	}

	private Set<Integer> findID(String nounA) {
		return findId.get(nounA);
	}

	// a synset (second field of synsets.txt) that is the common ancestor of
	// nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		Set<Integer> a = findID(nounA);
		Set<Integer> b = findID(nounB);
		int id = sap.ancestor(a, b);
		return synsets.get(id);
	}

	// do unit testing of this class
	public static void main(String[] args) {
		String synsets = "./wordnet/synsets11.txt";
		String hypernyms = "./wordnet/hypernyms11AmbiguousAncestor.txt";
		WordNet test = new WordNet(synsets, hypernyms);
		// for(String noun:test.nouns()) {
		// System.out.println(noun);
		// }
		// 0,a,one
		// 1,b,two
		// 2,c,three
		// 3,d,four
		// 4,e,five
		// 5,f,six
		// 6,g,seven
		// 7,h,eight
		// 8,i,nine
		// 9,j,ten
		// 10,k,eleven

		String v = "d";
		String w = "i";
		System.out.println("ancestor:" + test.sap(v, w) + " length:"
				+ test.distance(v, w));
	}
}