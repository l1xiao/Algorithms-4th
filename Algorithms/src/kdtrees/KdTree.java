package kdtrees;

import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
	private int size;
	private Node root;

	private class Node {
		Point2D point;
		Node left, right, parent;
		int level;
		RectHV rect;
	}

	// construct an empty set of points
	public KdTree() {
	};

	// is the set empty?
	public boolean isEmpty() {
		return size() == 0;
	};

	// number of points in the set
	public int size() {
		return size;
	};

	// add the point to the set (if it is not already in the set)
	public void insert(Point2D p) {
		if (p == null) {
			throw new java.lang.NullPointerException();
		}
		if (contains(p))
			return;
		if (root == null) {
			root = new Node();
			root.level = 0;
			root.rect = new RectHV(0, 0, 1, 1);
			root.point = p;
		} else {
			double[] axis = {p.x(), p.y()};
			insert(axis, root, 0);
		}
		size++;
	};

	private void insert(double[] p, Node root, int level) {
		if (root.point == null) {
			root.point = new Point2D(p[0], p[1]);
			root.level = level;
			root.rect = root.parent.rect;
			RectHV lastrect = root.parent.rect;
			if (root.parent.level % 2 == 0) {
				if (p[0] < root.parent.point.x()) {
					root.rect = new RectHV(lastrect.xmin(), lastrect.ymin(), root.parent.point.x(), lastrect.ymax());
				} else if (p[0] > root.parent.point.x()){
					root.rect = new RectHV(root.parent.point.x(), lastrect.ymin(), lastrect.xmax(), lastrect.ymax());
				}
			} else {
				if (p[1] < root.parent.point.y()) {
					root.rect = new RectHV(lastrect.xmin(), lastrect.ymin(), lastrect.xmax(), root.parent.point.y());
				} else if (p[1] > root.parent.point.y()) {
					root.rect = new RectHV(lastrect.xmin(), root.parent.point.y(), lastrect.xmax(), lastrect.ymax());
				}
			}
		} else {
			if (level % 2 == 0) {
				if (p[0] < root.point.x()) {
					if (root.left == null) {
						root.left = new Node();
						root.left.parent = root;
					}
					insert(p, root.left, level + 1);
				} else if (p[0] > root.point.x()) {
					if (root.right == null) {
						root.right = new Node();
						root.right.parent = root;
					}
					insert(p, root.right, level + 1);
				} else {
					if (root.right == null) {
						root.right = new Node();
						root.right.parent = root;
					}
					if (p[1] != root.point.y()) {
						insert(p, root.right, level + 1);
					}
				}
			} else {
				if (p[1] < root.point.y()) {
					if (root.left == null) {
						root.left = new Node();
						root.left.parent = root;
					}
					insert(p, root.left, level + 1);
				} else if (p[1] > root.point.y()) {
					if (root.right == null) {
						root.right = new Node();
						root.right.parent = root;
					}
					insert(p, root.right, level + 1);
				} else {
					if (p[0] != root.point.x()) {
						if (root.right == null) {
							root.right = new Node();
							root.right.parent = root;
						}
						insert(p, root.right, level + 1);
					}
				}
			}
		}
	}

	// does the set contain point p?
	public boolean contains(Point2D p) {
		return contains(p, root);
	};

	private boolean contains(Point2D p, Node root) {
		if (root == null) {
			return false;
		} else {
			if (root.point.x() == p.x() && root.point.y() == p.y()) {
				return true;
			} else {
				if (root.level % 2 == 0 && p.x() < root.point.x()
						|| root.level % 2 == 1 && p.y() < root.point.y()) {
					return contains(p, root.left);
				} else {
					return contains(p, root.right);
				}
			}
		}
	}

	// draw all points to standard draw
	public void draw() {
		draw(root);
	};

	private void draw(Node root) {
		if (root != null) {
			StdDraw.setPenColor(StdDraw.BLACK);
			root.point.draw();
			if (root.level % 2 == 0) {
				StdDraw.setPenColor(StdDraw.BLUE);
			} else {
				StdDraw.setPenColor(StdDraw.RED);
			}
			root.rect.draw();
			draw(root.left);
			draw(root.right);
		}
	}

	// all points that are inside the rectangle
	public Iterable<Point2D> range(RectHV rect) {
		ArrayList<Point2D> inRect = new ArrayList<Point2D>();
		range(rect, root, inRect);
		return inRect;
	};

	//
	private void range(RectHV rect, Node root, ArrayList<Point2D> inRect) {
		if (root == null) {
			return;
		} else {
			if (root.rect.intersects(rect)) {
				// determine this point
				if (root.point.x() <= rect.xmax()
						&& root.point.x() >= rect.xmin()
						&& root.point.y() >= rect.ymin()
						&& root.point.y() <= rect.ymax()) {
					inRect.add(root.point);
				}
				// judge which subtree
				range(rect, root.left, inRect);
				range(rect, root.right, inRect);
			} else {
				return;
			}
		}
	}

	// a nearest neighbor in the set to point p; null if the set is empty
	public Point2D nearest(Point2D p) {
		if (root != null) {
			Point2D[] nearest = { root.point };
			nearest(p, root, nearest);
			return nearest[0];
		}
		return null;
	};

	private void nearest(Point2D p, Node root, Point2D[] nearestPoint) {
		if (root == null)
			return;
		if (p.distanceSquaredTo(root.point) <= nearestPoint[0]
				.distanceSquaredTo(p)) {
			// update nearest point
			nearestPoint[0] = root.point;
		}
		// decide x or y
		if (root.level % 2 == 0) {
			// point in the left side
			if (p.x() < root.point.x()) {
				nearest(p, root.left, nearestPoint);
				if (root.right != null
						&& root.right.rect.distanceSquaredTo(p) < p
								.distanceSquaredTo(nearestPoint[0])) {
					// can ignore right subtree
					nearest(p, root.right, nearestPoint);
				}
			} else {
				// point in the right side
				nearest(p, root.right, nearestPoint);
				if (root.left != null
						&& root.left.rect.distanceSquaredTo(p) < p
								.distanceSquaredTo(nearestPoint[0])) {
					nearest(p, root.left, nearestPoint);
				}
			}
		} else {
			if (p.y() < root.point.y()) {
				nearest(p, root.left, nearestPoint);
				if (root.right != null
						&& root.right.rect.distanceSquaredTo(p) < p
								.distanceSquaredTo(nearestPoint[0])) {
					// can ignore right subtree
					nearest(p, root.right, nearestPoint);
				}
			} else {
				// point in the right side
				nearest(p, root.right, nearestPoint);
				if (root.left != null
						&& root.left.rect.distanceSquaredTo(p) < p
								.distanceSquaredTo(nearestPoint[0])) {
					nearest(p, root.left, nearestPoint);
				}
			}
		}
	}

	// unit testing of the methods (optional)
	public static void main(String[] args) {
		KdTree test = new KdTree();
		Point2D p1 = new Point2D(0.5, 0.5);
		Point2D p2 = new Point2D(0.4, 0);
		Point2D p3 = new Point2D(0.6, 1);
		// Point2D p4 = new Point2D(0.3, 0.7);
		// Point2D p5 = new Point2D(0.2, 0.9);
		test.insert(p1);
		test.insert(p2);
		test.insert(p3);
		// test.insert(p4);
		// test.insert(p5);
		// System.out.println(test.contains(p5));
		System.out.println(test.nearest(new Point2D(0.4, 1)));
	};
}
