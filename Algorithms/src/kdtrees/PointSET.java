package kdtrees;

import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
	private SET<Point2D> points;

	// construct an empty set of points
	public PointSET() {
		points = new SET<Point2D>();
	};

	// is the set empty?
	public boolean isEmpty() {
		return points.size() == 0;
	};

	// number of points in the set
	public int size() {
		return points.size();
	};

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null) {
			throw new java.lang.NullPointerException();
		}
		points.add(p);
	};

	// does the set contain point p?
	public boolean contains(Point2D p) {
		return points.contains(p);
	};

	// draw all points to standard draw
	public void draw() {
		for (Point2D point : points) {
			point.draw();
		}
	};

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		ArrayList<Point2D> inRect = new ArrayList<Point2D>();
		for (Point2D point : points) {
			if (point.x() <= rect.xmax() && point.x() >= rect.xmin()
					&& point.y() >= rect.ymin() && point.y() <= rect.ymax()) {
				inRect.add(point);
			}
		}
		return inRect;
	};

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		Point2D nearest = null;
		for (Point2D point : points) {
			if (nearest == null) {
				nearest = point;
				continue;
			} else if (point.distanceTo(p) < nearest.distanceTo(p)) {
				nearest = point;
			}
		}
		return nearest;
	};

	// unit testing of the methods (optional)
	public static void main(String[] args) {
	};
}