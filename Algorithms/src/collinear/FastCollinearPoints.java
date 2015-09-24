package collinear;
import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
	private int N;
	private ArrayList<LineSegment> segment = new ArrayList<LineSegment>();

	public FastCollinearPoints(Point[] points) {
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
}