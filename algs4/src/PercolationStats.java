import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
	private double[] results;
	private double mean;
	private double stddev;
	private double confidenceLo;
	private double confidenceHi;
	private Percolation perc;

	// perform T independent experiments on an N-by-N grid
	public PercolationStats(int N, int T) {
		if (N < 1 || T < 1) {
			throw new java.lang.IllegalArgumentException();
		}
		results = new double[T];
		for (int i = 0; i < T; i++) {
			perc = new Percolation(N);
			int result = 0;
			while (!perc.percolates()) {
				int x = StdRandom.uniform(N) + 1;
				int y = StdRandom.uniform(N) + 1;
				while (perc.isOpen(x, y)) {
					x = StdRandom.uniform(N) + 1;
					y = StdRandom.uniform(N) + 1;
				}
				perc.open(x, y);
				result++;
			}
			perc = null;
			results[i] = result / (N * N * 1.0);
		}
		mean = StdStats.mean(results);
		stddev = StdStats.stddev(results);
		confidenceLo = mean - 1.96 * stddev / Math.sqrt(T);
		confidenceHi = mean + 1.96 * stddev / Math.sqrt(T);
	}

	// sample mean of percolation threshold
	public double mean() {
		return mean;
	}

	// sample standard deviation of percolation threshold
	public double stddev() {
		return stddev;
	}

	// low endpoint of 95% confidence interval
	public double confidenceLo() {
		return confidenceLo;
	}

	// high endpoint of 95% confidence interval
	public double confidenceHi() {
		return confidenceHi;
	}

	// test client (described below)
	public static void main(String[] args) {
		int N = StdIn.readInt();
		int T = StdIn.readInt();
		PercolationStats unittest = new PercolationStats(N, T);
		System.out.println("mean                    = " + unittest.mean());
		System.out.println("stddev                  = " + unittest.stddev());
		System.out.println("95% confidence interval = "
				+ unittest.confidenceLo() + ", " + unittest.confidenceHi());

	}
}
