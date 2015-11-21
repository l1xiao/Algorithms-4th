package wordnet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
	private WordNet wordnet;

	// constructor takes a WordNet object
	public Outcast(WordNet wordnet) {
		this.wordnet = wordnet;
	}

	// given an array of WordNet nouns, return an outcast
	public String outcast(String[] nouns) {
		int max = -1;
		int index = -1;
		for (int i = 0; i < nouns.length; i++) {
			int total = 0;
			for (int j = 0; j < nouns.length && i != j; j++) {
				total = wordnet.distance(nouns[i], nouns[j]);
			}
			if (total > max) {
				max = total;
				index = i;
			}
		}
		return nouns[index];
	}

	// see test client below
	public static void main(String[] args) {
		String[] arg = { "./wordnet/synsets.txt", "./wordnet/hypernyms.txt",
				"./wordnet/outcast5.txt", "./wordnet/outcast8.txt",
				"./wordnet/outcast11.txt" };
		// synsets.txt hypernyms.txt outcast5.txt outcast8.txt outcast11.txt
		WordNet wordnet = new WordNet(arg[0], arg[1]);
		Outcast outcast = new Outcast(wordnet);
		// WordNet wordnet = new WordNet(args[0], args[1]);
		// Outcast outcast = new Outcast(wordnet);
		for (int t = 2; t < arg.length; t++) {
			In in = new In(arg[t]);
			String[] nouns = in.readAllStrings();
			StdOut.println(arg[t] + ": " + outcast.outcast(nouns));
		}
	}
}
