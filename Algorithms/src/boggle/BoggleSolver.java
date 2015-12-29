package boggle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;

public class BoggleSolver {
	private TrieTree dictionary;
	private int height, width;

	// Initializes the data structure using the given array of strings as the dictionary.
	public BoggleSolver(String[] dictionary) {
		this.dictionary = new TrieTree();
		for (String word : dictionary) {
			this.dictionary.add(word);
		}
		System.out.println("finish dictionary");
	}
 
	// Returns the set of all valid words in the given Boggle board, as an Iterable.
	public Iterable<String> getAllValidWords(BoggleBoard board) {
		this.width = board.cols();
		this.height = board.rows();
		HashSet<String> results = new HashSet<>();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				ArrayList<Character> path = new ArrayList<>();
				path.add(board.getLetter(i, j));
				boolean[][] marked = new boolean[height][width];
				getPath(board, results, path, i, j, marked);
			}
		}
		// test
		// ArrayList<Character> path = new ArrayList<>();
		// path.add('E');
		// boolean[][] marked = new boolean[height][width];
		// getPath(board, results, path, 3, 3, marked);

		ArrayList<String> sort = new ArrayList<>();
		sort.addAll(results);
		Collections.sort(sort);
		return sort;
	}

	// Returns the score of the given word if it is in the dictionary, zero
	// otherwise.
	public int scoreOf(String word) {
		if (!dictionary.contains(word)) return 0;
		int length = word.length();
		if (length < 3)
			return 0;
		if (length < 5)
			return 1;
		if (length == 5)
			return 2;
		if (length == 6)
			return 3;
		if (length == 7)
			return 5;
		return 11;
	}

	// Recursively generate all paths
	private void getPath(BoggleBoard board, HashSet<String> results, ArrayList<Character> path, int row, int col,
			boolean[][] marked) {
		marked[row][col] = true;
		// System.out.println(path);
		// if (path.size() == 4 && path.get(0) == 'E' && path.get(1) == 'N' &&
		// path.get(2) == 'I' && path.get(3) == 'E') {
		// System.out.println(path);
		// }
		String string = charToString(path);
		if (!prefixQuery(string))
			return;
		if (!results.contains(string)) {
			// System.out.println(string);
			if (this.dictionary.contains(string) && string.length() > 2) {
				results.add(string);
			}
		}
		ArrayList<Integer[]> neighbors = (ArrayList<Integer[]>) getNeighbors(board, row, col);
		// for(Integer[] xy:neighbors) {
		// System.out.print(board.getLetter(xy[0], xy[1]) + "\t" );
		// }
		// System.out.println();
		for (Integer[] neighbor : neighbors) {
			int x = neighbor[0];
			int y = neighbor[1];
			if (!marked[x][y]) {
				Character c = board.getLetter(x, y);
				path.add(c);
				getPath(board, results, path, x, y, marked);
				path.remove(path.size() - 1);
				marked[x][y] = false;
			}
		}
	}

	private boolean prefixQuery(String query) {
		return this.dictionary.hasPrefix(query);
	}

	private String charToString(ArrayList<Character> path) {
		StringBuffer result = new StringBuffer();
		for (Character c : path) {
			if (c == 'Q') {
				result.append("QU");
			} else {
				result.append(c);
			}
		}
		return result.toString();
	}

	private Iterable<Integer[]> getNeighbors(BoggleBoard board, int row, int col) {
		ArrayList<Integer[]> neighbors = new ArrayList<>();
		// up
		if (row > 0) {
			Integer[] n = { row - 1, col };
			neighbors.add(n);
		}
		// down
		if (row < height - 1) {
			Integer[] n = { row + 1, col };
			neighbors.add(n);
		}
		// left
		if (col > 0) {
			Integer[] n = { row, col - 1 };
			neighbors.add(n);
		}
		// right
		if (col < width - 1) {
			Integer[] n = { row, col + 1 };
			neighbors.add(n);
		}
		// up and left
		if (row > 0 && col > 0) {
			Integer[] n = { row - 1, col - 1 };
			neighbors.add(n);
		}
		// up and right
		if (row > 0 && col < width - 1) {
			Integer[] n = { row - 1, col + 1 };
			neighbors.add(n);
		}
		// down and left
		if (row < height - 1 && col > 0) {
			Integer[] n = { row + 1, col - 1 };
			neighbors.add(n);
		}
		// down and right
		if (row < height - 1 && col < width - 1) {
			Integer[] n = { row + 1, col + 1 };
			neighbors.add(n);
		}
		return neighbors;

	}

	public static void main(String[] args) {
		String dict = "boggle/dictionary-algs4.txt";
		String bord = "boggle/board-q.txt";
		In in = new In(dict);
		String[] dictionary = in.readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);
		BoggleBoard board = new BoggleBoard(bord);
		solver.getAllValidWords(board);
		int score = 0;
		System.out.println(solver.dictionary.contains("END"));
		for (String word : solver.getAllValidWords(board)) {
			StdOut.println(word);
			score += solver.scoreOf(word);
		}
		StdOut.println("Score = " + score);
		// char[][] a = { { 'A', 'B', 'C' }, { 'D', 'E', 'F' } };
		// char[][] a = { { 'A', 'B', 'C', 'D' }, { 'E', 'F', 'G', 'H' }, {'I',
		// 'J', 'K', 'L'}, {'M', 'N', 'O', 'P'} };
		// char[][] a = { { 'A', 'B' }, {'C', 'D'} };
		// String[] dictionary = {"AB", "CFE"};
		// BoggleBoard board = new BoggleBoard(a);
		// BoggleSolver test = new BoggleSolver(dictionary);
		// HashSet<String> results = (HashSet<String>)
		// test.getAllValidWords(board);
		// ArrayList<String> sort = new ArrayList<>();
		// sort.addAll(results);
		// Collections.sort(sort);
		// for (String string : sort) {
		// System.out.println(string);
		// }
	}

}
