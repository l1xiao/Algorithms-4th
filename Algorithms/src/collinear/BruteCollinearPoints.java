package collinear;
import java.util.*;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
	// finds all line segments containing 4 points
	private int N;
	private ArrayList<LineSegment> segment = new ArrayList<LineSegment>();

	public BruteCollinearPoints(Point[] points) {
		if (points == null)
			throw new java.lang.NullPointerException();
		N = 0;
		Arrays.sort(points);
		for (int i = 0; i < points.length - 3; i++) {
			for (int j = i + 1; j < points.length - 2; j++) {
				double slope1 = points[i].slopeTo(points[j]);
				for (int k = j + 1; k < points.length - 1; k++) {
					double slope2 = points[i].slopeTo(points[k]);
					if (slope1 != slope2)
						continue;
					int temp = 0;
					for (int l = k + 1; l < points.length; l++) {
						double slope3 = points[i].slopeTo(points[l]);
						if (slope1 == slope3) temp = l;
						if ((l == points.length - 1) && (temp != 0)) {
							N++;
							segment.add(new LineSegment(points[i], points[temp]));
						}
					}
				}
			}
		}
	}

	// the number of line segments
	public int numberOfSegments() {
		return N;
	}

	// the line segments
	public LineSegment[] segments() {
		LineSegment[] results = new LineSegment[N];
		for (int i = 0; i < N; i++) {
			results[i] = segment.get(i);
		}
		return results;
	}

	public static void main(String[] args) {

		// read the N points from a file
		// In in = new In(args[0]);
		In in = new In("./collinear/rs1423.txt");
		int N = in.readInt();
		System.out.println(N);
		Point[] points = new Point[N];
		for (int i = 0; i < N; i++) {
			int x = in.readInt();
			int y = in.readInt();
			System.out.println("x:" + x + " y:" + y);
			points[i] = new Point(x, y);
		}

		// draw the points
		StdDraw.show(0);
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		for (Point p : points) {
			p.draw();
		}
		StdDraw.show();

		// print and draw the line segments
		BruteCollinearPoints collinear = new BruteCollinearPoints(points);
		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}
	}
}