

import java.util.ArrayList;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
	private final static int R = 256;
	// get alphabet list with index
	private static void alphabet(int[] arr) {
		for (int i = 0; i < R; i++) {
			arr[i] = i;
		}
	}
	private static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}

	// apply move-to-front encoding, reading from standard input and writing to standard output
	public static void encode() {
		// init
		int[] alphabet = new int[R];
		alphabet(alphabet);
		int head = 0;
		ArrayList<Integer> result = new ArrayList<Integer>();
		// reading from standard input
		char[] inputCharArray = BinaryStdIn.readString().toCharArray();
		BinaryStdIn.close();
		for (char c : inputCharArray) {
			BinaryStdOut.write(alphabet[c]);
			BinaryStdOut.flush();
//			System.out.println(c);
			swap(alphabet, head, c);
			head = c;
		}
		BinaryStdOut.close();
//		System.out.println();
//		for (int n : result) {
//			System.out.print(n + " ");
//		}
	}

	// apply move-to-front decoding, reading from standard input and writing to standard output
	public static void decode() {
	}

	// if args[0] is '-', apply move-to-front encoding
	// if args[0] is '+', apply move-to-front decoding
	public static void main(String[] args) {
		// resset
		encode();
		// bananaaa
	}
}
