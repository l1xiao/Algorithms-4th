package burrows;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
	// apply Burrows-Wheeler encoding, reading from standard input and writing
	// to standard output
	public static void encode() {
		String s = BinaryStdIn.readString();
		BinaryStdIn.close();
		// String s = "ABRACADABRA!";
		CircularSuffixArray csa = new CircularSuffixArray(s);
		char[] result = new char[s.length()];
		for (int i = 0; i < s.length(); i++) {
			int index = csa.index(i);
			if (index == 0)
				BinaryStdOut.write(i);
			char c = s.charAt((index - 1 + s.length()) % s.length());
			result[i] = c;
		}
		for (char c : result) {
			BinaryStdOut.write(c);
		}
		BinaryStdOut.flush();
		BinaryStdOut.close();
	}

	// apply Burrows-Wheeler decoding, reading from standard input and writing
	// to standard output
	public static void decode() {
		int first = BinaryStdIn.readInt();
		char[] t = BinaryStdIn.readString().toCharArray();
		// int first = 3;
		// char[] t = "ARD!RCAAAABB".toCharArray();
		char[] first_line = t.clone();
		Arrays.sort(first_line);
		HashMap<Character, ArrayList<Integer>> map = new HashMap<Character, ArrayList<Integer>>();
		for (int i = 0; i < t.length; i++) {
			if (map.containsKey(t[i])) {
				map.get(t[i]).add(i);
			} else {
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add(i);
				map.put(t[i], temp);
			}
		}
		HashMap<Character, Integer> cnt = new HashMap<Character, Integer>();
		for (Character c : map.keySet()) {
			cnt.put(c, 0);
		}
		int[] next = new int[t.length];
		// calculate next[]
		for (int i = 0; i < next.length; i++) {
			char current_char = first_line[i];
			ArrayList<Integer> indices = map.get(current_char);
			next[i] = indices.get(cnt.get(current_char));
			cnt.put(current_char, cnt.get(current_char) + 1);
		}
		for (int i = 0; i < next.length; i++) {
			BinaryStdOut.write(first_line[first]);
			// System.out.println(next[i]);
			// System.out.println(first_line[first]);
			first = next[first];
		}
		BinaryStdOut.flush();
		BinaryStdOut.close();
	}

	// if args[0] is '-', apply Burrows-Wheeler encoding
	// if args[0] is '+', apply Burrows-Wheeler decoding
	public static void main(String[] args) {
		if (args[0].equals("-"))
			encode();
		else if (args[0].equals("+"))
			decode();
		else
			throw new IllegalArgumentException("Illegal command line argument");
	}
}