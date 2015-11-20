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
	Map<Integer, ArrayList<String>> synsets = new HashMap<>();
	Map<Integer, ArrayList<Integer>> hypernyms = new HashMap<>();
	Set<String> dict = new HashSet<>();
	Digraph G;
	SAP sap;
	// constructor takes the name of the two input files
	public WordNet(String synsets, String hypernyms) {
		// read file and store data
		In in = new In(synsets);
		while (!in.isEmpty()) {
			String line = in.readLine();
			String[] token = line.split(",");
			Integer id = Integer.parseInt(token[0]);
			ArrayList<String> nouns = new ArrayList<>(Arrays.asList(token[1].split(" ")));
			this.synsets.put(id, nouns);
		}
		in = new In(hypernyms);
		while (!in.isEmpty()) {
			String line = in.readLine();
			String[] token = line.split(",", 1);
			ArrayList<Integer> nouns = new ArrayList<>();
			for (String hypernym : token) {
				if (hypernym != token[0]) {
					nouns.add(Integer.parseInt(hypernym));
				}
			}
			this.hypernyms.put(Integer.parseInt(token[0]), nouns);
		}
		// build the graph
		G = new Digraph(this.synsets.size());
		for (Integer v : this.synsets.keySet()) {
			if (this.hypernyms.containsKey(v)) {
				for (Integer w : this.hypernyms.get(v)) {
					for (String noun : this.synsets.get(v)) {
						dict.add(noun);
					}
					for (String noun : this.synsets.get(w)) {
						dict.add(noun);
					}
					G.addEdge(v, w);
				}
			}
		}
		this.synsets.clear();
		this.hypernyms.clear();
		this.sap = new SAP(this.G);
	}

	// returns all WordNet nouns
	public Iterable<String> nouns() {
		return dict;
	}

	// is the word a WordNet noun?
	public boolean isNoun(String word) {
		return dict.contains(word);
	}

	// distance between nounA and nounB (defined below)
	public int distance(String nounA, String nounB) {
		return sap.length(findID(nounA), findID(nounB));
	}
	private ArrayList<Integer> findID(String nounA) {
		return null;
	}
	// a synset (second field of synsets.txt) that is the common ancestor of
	// nounA and nounB
	// in a shortest ancestral path (defined below)
	public String sap(String nounA, String nounB) {
		return null;
	}

	// do unit testing of this class
	public static void main(String[] args) {
	}
}