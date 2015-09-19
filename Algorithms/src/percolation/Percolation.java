package percolation;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
	private WeightedQuickUnionUF UF;
	private WeightedQuickUnionUF UF1;
	private boolean[][] grid;
	private int N;

	// create N-by-N grid, with all sites blocked
	public Percolation(int N) {
		if (N < 1) {
			throw new java.lang.IllegalArgumentException();
		}
		this.N = N;
		grid = new boolean[N + 1][N + 1];
		UF = new WeightedQuickUnionUF(N * N + 2);
		UF1 = new WeightedQuickUnionUF(N * N + 1);
	}

	// open site (row i, column j) if it is not open already
	public void open(int i, int j) {
		if (i < 1 || j < 1 || i > N || j > N) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		if (!grid[i][j]) {
			if (i == 1) {
				UF.union(0, j);
				UF1.union(0, j);
			}
			if (i == N) {
				UF.union(N * N + 1, (i - 1) * N + j);
			}
			grid[i][j] = true;
			// judge whether this is full or not
			// left
			if (j > 1) {
				if (isOpen(i, j - 1)) {
					UF.union((i - 1) * N + j, (i - 1) * N + j - 1);
					UF1.union((i - 1) * N + j, (i - 1) * N + j - 1);
				}
			}
			// right
			if (j < N) {
				if (isOpen(i, j + 1)) {
					UF.union((i - 1) * N + j, (i - 1) * N + j + 1);
					UF1.union((i - 1) * N + j, (i - 1) * N + j + 1);
				}
			}
			// up
			if (i > 1) {
				if (isOpen(i - 1, j)) {
					UF.union((i - 1) * N + j, (i - 1 - 1) * N + j);
					UF1.union((i - 1) * N + j, (i - 1 - 1) * N + j);
				}
			}
			// down
			if (i < N) {
				if (isOpen(i + 1, j)) {
					UF.union((i - 1) * N + j, (i + 1 - 1) * N + j);
					UF1.union((i - 1) * N + j, (i + 1 - 1) * N + j);
				}
			}
		}
	}

	// is site (row i, column j) open?
	public boolean isOpen(int i, int j) {
		if (i < 1 || j < 1 || i > N || j > N) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		if (grid[i][j]) {
			return true;
		}
		return false;
	}

	// is site (row i, column j) full?
	public boolean isFull(int i, int j) {
		if (i < 1 || j < 1 || i > N || j > N) {
			throw new java.lang.IndexOutOfBoundsException();
		}
		if (grid[i][j])
			if (UF1.connected(0, (i - 1) * N + j)) {
				return true;
			}
		return false;
	}

	// does the system percolate?
	public boolean percolates() {
		return UF.connected(0, N * N + 1);
	}

	// test client (optional)
	public static void main(String[] args) {
	}
}