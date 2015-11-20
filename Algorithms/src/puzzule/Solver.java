package puzzule;

import java.util.ArrayList;
import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
	private Node goal;

	private class Node implements Comparable<Node>{
		Node last;
		ArrayList<Node> parents;
		int moves;
		Board current;
		int manhattan, priority;

		Node(int moves, Board current, Node last) {
			this.moves = moves;
			this.current = current;
			this.last = last;
			manhattan = current.manhattan();
			if (last != null) {
				parents = new ArrayList<Node>(last.parents);
				parents.add(0, last);
				if (parents.size() == 6) {
					parents.remove(5);
				}
			} else {
				parents = new ArrayList<Node>();
			}
			priority = manhattan + moves;
		}

		@Override
		public int compareTo(Node o2) {
			if (this.priority > o2.priority)
				return 1;
			else if (this.priority < o2.priority)
				return -1;
			else if (this.manhattan > o2.manhattan)
				return 1;
			else if (this.manhattan < o2.manhattan)
				return -1;
			return 1;
		}
	}

	// find a solution to the initial board (using the A* algorithm)
	public Solver(Board initial) {
		Node search = new Node(0, initial, null);
		Node twinSearch = new Node(0, initial.twin(), null);
		MinPQ<Node> twinPQ = new MinPQ<Node>();
		MinPQ<Node> PQ = new MinPQ<Node>();
		PQ.insert(search);
		twinPQ.insert(twinSearch);
		while (!PQ.isEmpty() && !twinPQ.isEmpty()) {
//			count1++;
//			maxsize = Math.max(maxsize, PQ.size());
			Node current = PQ.delMin();
			Node twinCurrent = twinPQ.delMin();
			if (current.current.isGoal()) {
				this.goal = current;
				break;
			}
			if (twinCurrent.current.isGoal()) {
				this.goal = null;
				break;
			}
			Iterator<Board> neighbors = current.current.neighbors().iterator();
			Iterator<Board> twinNeighbors = twinCurrent.current.neighbors()
					.iterator();
			while (neighbors.hasNext()) {
				Board neighbor = neighbors.next();
				// boolean flag = false;
				// Board aparent = null;
				// int index = -1;
				// for (int i = 0; i < current.parents.size(); i++) {
				// if (current.parents.get(i).current.equals(neighbor)) {
				// flag = true;
				// aparent = current.parents.get(i).current;
				// index = i;
				// break;
				// }
				// }
				// if (flag) {
				// System.out.println(aparent.toString() +"\nindex:"+index +" "+
				// neighbor);
				// System.out.println("---");
				// continue;
				// } else {
				// insert++;
				// PQ.insert(new Node(current.moves + 1, neighbor, current));
				// }
				if (current.last != null
						&& neighbor.equals(current.last.current)) {
					continue;
				}
//				insert++;
				PQ.insert(new Node(current.moves + 1, neighbor, current));
			}
			while (twinNeighbors.hasNext()) {
				Board twinNeighbor = twinNeighbors.next();
				if (twinCurrent.last != null
						&& twinNeighbor.equals(twinCurrent.last.current)) {
					continue;
				}
				twinPQ.insert(new Node(twinCurrent.moves + 1, twinNeighbor,
						current));
			}
		}
//		System.out.println(insert + "," + count1 + "," + maxsize);
	}

	// is the initial board solvable?
	public boolean isSolvable() {
		return goal != null;
	}

	// min number of moves to solve initial board; -1 if unsolvable
	public int moves() {
		if (isSolvable())
			return goal.moves;
		return -1;
	}

	// sequence of boards in a shortest solution; null if unsolvable
	public Iterable<Board> solution() {
		if (!isSolvable())
			return null;
		ArrayList<Board> solution = new ArrayList<Board>();
		Node temp = goal;
		while (temp != null) {
			solution.add(0, temp.current);
			temp = temp.last;
		}
		return solution;
	}

	// solve a slider puzzle (given below)
	public static void main(String[] args) {
		// create initial board from file
		// args[0] = "./puzzle2x2-00.txt";
		In in = new In("8puzzle/puzzle34.txt");
		int N = in.readInt();
		System.out.println(N);
		int[][] blocks = new int[N][N];
		for (int i = 0; i < N; i++)
			for (int j = 0; j < N; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
	}
}
