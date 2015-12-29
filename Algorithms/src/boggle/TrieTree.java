package boggle;


import java.util.ArrayList;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class TrieTree {
	private static final int R = 26; // extended ASCII

	private Node root; // root of trie
	private int N; // number of keys in trie
	private boolean has_prefix;
	// R-way trie node
	private static class Node {
		private Node[] next = new Node[R];
		private boolean isString;
	}
	// map 0->A, 1->B, ... 25->Z
	private int charToInt(char c) {
		return c - 'A';
	}
	private char intToChar(int i) {
		return (char)(i + 'A');
	}
	/**
	 * Initializes an empty set of strings.
	 */
	public TrieTree() {
	}

	public boolean contains(String key) {
		Node x = get(root, key, 0);
		if (x == null)
			return false;
		return x.isString;
	}

	private Node get(Node x, String key, int d) {
		if (x == null)
			return null;
		if (d == key.length())
			return x;
		char c = key.charAt(d);
		return get(x.next[charToInt(c)], key, d + 1);
	}
	private Node get(Node x, ArrayList<Character> key, int d) {
		if (x == null)
			return null;
		if (d == key.size())
			return x;
		char c = key.get(d);
		return get(x.next[charToInt(c)], key, d + 1);
	}

	public void add(String key) {
		root = add(root, key, 0);
	}

	private Node add(Node x, String key, int d) {
		if (x == null)
			x = new Node();
		if (d == key.length()) {
			if (!x.isString)
				N++;
			x.isString = true;
		} else {
			char c = key.charAt(d);
			x.next[charToInt(c)] = add(x.next[charToInt(c)], key, d + 1);
		}
		return x;
	}

	public int size() {
		return N;
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean hasPrefix(String prefix) {
		has_prefix = false;
		char[] pre1 = prefix.toCharArray();
		StringBuilder pre = new StringBuilder();
		for (char c:pre1) {
			pre.append(c);
		}
		String string = pre.toString();
		Node x = get(root, string, 0);
		checkPrefix(x, new StringBuilder(string));
		return has_prefix;
	}

	public boolean hasPrefix(ArrayList<Character> prefix) {
		has_prefix = false;
		ArrayList<Character> new_pre = new ArrayList<>();
		for (char c: prefix) {
			new_pre.add(c);
			if (c == 'Q') {
				new_pre.add('U');
			}
		}
		Node x = get(root, new_pre, 0);
		checkPrefix(x, new_pre);
		return has_prefix;
	}
	private void checkPrefix(Node x, StringBuilder prefix) {
		if (x == null) {
			return;
		}
		if (x.isString) {
			has_prefix = true;
			return;
		}
		for (char c = 0; c < R; c++) {
			prefix.append(intToChar(c));
			checkPrefix(x.next[c], prefix);
			if (has_prefix) {
				return;
			}
			prefix.deleteCharAt(prefix.length() - 1);
		}
	}
	private void checkPrefix(Node x, ArrayList<Character> prefix) {
		if (x == null) {
			return;
		}
		if (x.isString) {
			has_prefix = true;
			return;
		}
		for (char c = 0; c < R; c++) {
			prefix.add(intToChar(c));
			checkPrefix(x.next[c], prefix);
			if (has_prefix) {
				break;
			}
			prefix.remove(prefix.size() - 1);
		}
	}
	public static void main(String[] args) {
		TrieTree set = new TrieTree();
		while (!StdIn.isEmpty()) {
			String key = StdIn.readString();
			set.add(key);
		}
		StdOut.println("longestPrefixOf(\"shellsort\"):");
		StdOut.println();
		StdOut.println("longestPrefixOf(\"xshellsort\"):");
		StdOut.println();
		StdOut.println("keysWithPrefix(\"shor\"):");
		StdOut.println();
		StdOut.println("keysWithPrefix(\"shortening\"):");
		StdOut.println();
	}
}