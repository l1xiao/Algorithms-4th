package puzzule;


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;

public class Board {
	private int N;
	private Board twin;
	private int x, y;
	private int manhattan = -1, hamming = -1;
	private short[] blocks;
	private Stack<Board> neighbors = null;

	// construct a board from an N-by-N array of blocks
	// (where blocks[i][j] = block in row i, column j)
	public Board(int[][] blocks) {
		this.N = blocks.length;
		this.blocks = new short[N * N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (blocks[i][j] == 0) {
					x = i;
					y = j;
				}
				this.blocks[i * N + j] = (short) blocks[i][j];
			}
		}
		this.manhattan = manhattan();
	}

	// board dimension N
	public int dimension() {
		return N;
	}

	// number of blocks out of place
	public int hamming() {
		if (this.hamming != -1)
			return this.hamming;
		int result = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if ((i != N - 1 || j != N - 1)
						&& blocks[i * N + j] != i * N + j + 1) {
					result++;
					continue;
				}
			}
		}
		return this.hamming = result;
	}

	// sum of Manhattan distances between blocks and goal
	public int manhattan() {
		if (this.manhattan != -1)
			return this.manhattan;
		int result = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				if (blocks[i * N + j] != i * N + j + 1
						&& blocks[i * N + j] != 0) {
					// int column = (blocks[i][j] - 1) % N;
					// int row = (blocks[i][j] - 1) / N;
					result = result + Math.abs((blocks[i * N + j] - 1) / N - i)
							+ Math.abs((blocks[i * N + j] - 1) % N - j);
					continue;
				}
			}
		}
		return this.manhattan = result;
	}

	// is this board the goal board?
	public boolean isGoal() {
		return this.manhattan == 0;
	}

	// a board that is obtained by exchanging any pair of blocks
	public Board twin() {
		int row = 0;
		if (twin == null) {
			int[][] temp = new int[N][N];
			for (int i = 0; i < N; i++) {
				for (int j = 0; j < N; j++) {
					temp[i][j] = this.blocks[i * N + j];
					if (temp[i][j] == 0)
						row = i;
				}
			}
			if (row >= 1)
				row = row - 1;
			else
				row = row + 1;
			int swap = temp[row][0];
			temp[row][0] = temp[row][1];
			temp[row][1] = swap;
			this.twin = new Board(temp);
		}
		return twin;
	}

	// does this board equal y?
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null) {
            return false;
        }
        if (this.getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (this.blocks == that.blocks) {
            return true;
        }
        if (this.dimension() != that.dimension()) {
            return false;
        }
        for (int i = 0; i < (int) N; i++)
            for (int j = 0; j < (int) N; j++) {
                if (this.blocks[i * N + j] != that.blocks[i * N + j]) {
                    return false;
                }
            }
        return true;
    }

	// all neighboring boards
	public Iterable<Board> neighbors() {
		if (neighbors != null)
			return neighbors;
		neighbors = new Stack<Board>();
		int[] horizontal = { y - 1, y + 1 };
		int[] vertical = { x - 1, x + 1 };
		// int left = y - 1, right = y + 1;
		// int up = x - 1, down = x + 1;
		if (x == 0) {
			vertical[0] = -1;
		} else if (x == N - 1) {
			vertical[1] = -1;
		}
		if (y == 0) {
			horizontal[0] = -1;
		} else if (y == N - 1) {
			horizontal[1] = -1;
		}
		// if (left >= 0) {}
		// if (right <= N - 1){}
		// if (up >= 0) {}
		// if (down <= N-1) {}
		for (int i = 0; i < horizontal.length; i++) {
			if (horizontal[i] != -1) {
				int[][] neighbor = new int[N][N];
				for (int k = 0; k < N; k++) {
					for (int l = 0; l < N; l++) {
						neighbor[k][l] = blocks[k * N + l];
					}
				}
				int temp = neighbor[x][horizontal[i]];
				neighbor[x][horizontal[i]] = 0;
				neighbor[x][y] = temp;
				neighbors.push(new Board(neighbor));
			}
		}
		for (int j = 0; j < vertical.length; j++) {
			if (vertical[j] != -1) {
				int[][] neighbor = new int[N][N];
				for (int k = 0; k < N; k++) {
					for (int l = 0; l < N; l++) {
						neighbor[k][l] = blocks[k * N + l];
					}
				}
				int temp = neighbor[vertical[j]][y];
				neighbor[vertical[j]][y] = 0;
				neighbor[x][y] = temp;
				neighbors.push(new Board(neighbor));
			}
		}
		return neighbors;
	}

	// string representation of this board (in the output format specified
	// below)
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(N + "\n");
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				s.append(String.format("%2d ", blocks[i * N + j]));
			}
			s.append("\n");
		}
		return s.toString();
	}

	// unit tests (not graded)
	public static void main(String[] args) {
		In in = new In("8puzzle/puzzle4x4-00.txt");
		int N = in.readInt();
		// System.out.println(N);
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);
		// System.out.println(initial.manhattan());
		System.out.println(initial.isGoal());
		System.out.println(initial);
	}
}