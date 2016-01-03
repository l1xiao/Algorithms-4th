
import java.util.ArrayList;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
	private final static int R = 256;

	private static ArrayList<Character> alphabet() {
		ArrayList<Character> alphabet = new ArrayList<Character>(R);
		for (int i = 0; i < R; i++) {
			alphabet.add((char)i);
		}
		return alphabet;
	}

	// apply move-to-front encoding, reading from standard input and writing to standard output
	public static void encode() {
		ArrayList<Character> alphabet = alphabet();
		char[] inputCharArray = BinaryStdIn.readString().toCharArray();
		BinaryStdIn.close();
		for (char c : inputCharArray) {
			char index = (char) alphabet.indexOf(c);
			alphabet.remove(index);
			alphabet.add(0, c);
			BinaryStdOut.write(index);
		}
		BinaryStdOut.flush();
		BinaryStdOut.close();
	}

	// apply move-to-front decoding, reading from standard input and writing to standard output
	public static void decode() {
		ArrayList<Character> alphabet = alphabet();
		while (!BinaryStdIn.isEmpty()) {
			char index = BinaryStdIn.readChar();
			char c = alphabet.get(index);
			BinaryStdOut.write(c);
			alphabet.remove(index);
			alphabet.add(0, c);
		}
		BinaryStdOut.flush();
		BinaryStdOut.close();
	}

	// if args[0] is '-', apply move-to-front encoding
	// if args[0] is '+', apply move-to-front decoding
	public static void main(String[] args) {
		if (args[0].equals("-"))
			encode();
		else if (args[0].equals("+"))
			decode();
		else
			throw new IllegalArgumentException("Illegal command line argument");
	}
}
