package collinear;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {
	private int N;
	private ArrayList<LineSegment> segment = new ArrayList<LineSegment>();
	private class slope implements Comparable<slope>{
		int i;
		double slope;
		slope(int i, double slope) {
			this.i = i;
			this.slope = slope;
		}
		@Override
		public int compareTo(slope that) {
			if (this.slope == that.slope) return 0;
			else if (this.slope > that.slope) return 1;
			else return -1;
		}
		@Override
		public String toString() {
			return new String(i+":"+slope);
		}
		
		public Comparator<slope> slopeOrder() {
			return new Comparator<slope>(){
				@Override
				public int compare(slope o1, slope o2) {
					double o1slope = o1.slope, o2slope = o2.slope;
					if (o1slope == o2slope) return 0;
					else if (o1slope > o2slope) return 1;
					else return -1;
				}};
	    }
		
	}
//	private HashMap<Integer, Double>  map;
	public FastCollinearPoints(Point[] points) {
		if (points == null)
			throw new java.lang.NullPointerException();
		N = 0;
		Arrays.sort(points);
		for (int i = 0; i < points.length - 1; i++) {
			slope[] slopes = new slope[points.length - i - 1];
			for (int j = i + 1; j < points.length; j++) {
				slopes[j - i - 1] = new slope(j, points[i].slopeTo(points[j]));
				System.out.println(slopes[j - i - 1]);
			}
			System.out.println();
			Arrays.sort(slopes, 0, slopes.length - 1, slopes[0].slopeOrder());
			slope start = slopes[0];
			int count = 1;
			int index = start.i;
			for (int j = 1; j < slopes.length; j++) {
				System.out.println(slopes[j]);
				if (start.slope != slopes[j].slope) {
					if (count > 3) {
						segment.add(new LineSegment(points[i], points[index]));
						count = 1;
					}
					start = slopes[j];
					index = start.i;
				} else {
					if (j == slopes.length - 1 && count > 2) {
						segment.add(new LineSegment(points[i], points[index]));
						break;
					}
					count++;
					index = Math.max(index, slopes[j].i);
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
		In in = new In("./collinear/input10.txt");
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

//		// print and draw the line segments
		FastCollinearPoints collinear = new FastCollinearPoints(points);
		for (LineSegment segment : collinear.segments()) {
			StdOut.println(segment);
			segment.draw();
		}
	}
}