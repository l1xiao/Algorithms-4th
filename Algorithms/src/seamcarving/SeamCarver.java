package seamcarving;

import java.awt.Color;
import java.util.HashMap;
import java.util.LinkedList;

import edu.princeton.cs.algs4.DirectedEdge;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
	private Picture picture;
	private double[] energy;
	private DirectedEdge[] edgeTo;
	private double[] distTo;
	private int N;
	private boolean transpose = false;
	// two different ways to get the same pixel, key mean transpose is false, value mean transpose is true.  
	private HashMap<Integer, Integer> table = new HashMap<Integer, Integer>();

	// create a seam carver object based on the given picture
	public SeamCarver(Picture picture) {
		this.picture = picture;
		int width = width();
		int height = height();
		this.N = width * height + 2;
		energy = new double[N];
		distTo = new double[N];		
		for (int v = 1; v < N - 1; v++) {
			energy[v] = energy(getCol(v), getRow(v));
		}
		getTable();
		edgeTo = new DirectedEdge[N];
		// x is column, y is row
		energy[N - 1] = 0;
	}
	// use table to transpose the image
	private void getTable() {
		table.put(0, 0);
		table.put(N - 1, N - 1);
		for (int v = 1; v < N - 1; v++) {
			int col = getCol(v);
			int row = getRow(v);
			transpose = true;
			int i = getI(row, col);
			transpose = false;
			table.put(i, v);
		}
	}

	// current picture
	public Picture picture() {
		// 
		return picture;
	}

	// width of current picture
	public int width() {
		if (transpose) {
			return picture.height();
		}
		return picture.width();
	}

	// height of current picture
	public int height() {
		if (transpose) {
			return picture.width();
		}
		return picture.height();
	}

	// energy of pixel at column x and row y
	public double energy(int x, int y) {
		boolean flag = false;
		if (transpose) {
			int node = getI(x, y);
			int newNode = getNode(node);
			transpose = false;
			x = getCol(newNode);
			y = getRow(newNode);
			flag = true;
		}
		if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1) {
			throw new java.lang.IndexOutOfBoundsException(); 
		}
		if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
			if (flag) {
				transpose = true;
			}
			return 1000;
		}
		Color c1 = null, c2 = null, r1 = null, r2 = null;
		try {
			c1 = this.picture.get(x - 1, y);
			c2 = this.picture.get(x + 1, y);
			// in a column with 2 rows
			r1 = this.picture.get(x, y - 1);
			r2 = this.picture.get(x, y + 1);
		} catch(Exception e) {
			System.out.println();
			System.out.println("y:" + y + "\theight:" + height());
			System.out.println("x:" + x + "\twidth:" + width());
			throw e;
		}
		if (flag) {
			transpose = true;
		}
		return Math.sqrt((double)getDelta(c1, c2) + getDelta(r1, r2));
	}

	// sequence of indices for horizontal seam
	public int[] findHorizontalSeam() {
		transpose = true;
		int[] seam = findVerticalSeam();
		transpose = false;
		for (int i = 0; i < seam.length; i++) {
			seam[i] = getNode(seam[i]);
		}
		return seam;
	}

	
	// sequence of indices for vertical seam
	public int[] findVerticalSeam() {
		for (int v = 0; v < N; v++) {
			distTo[v] = Double.POSITIVE_INFINITY;
		}
		distTo[0] = 0;
		edgeTo[0] = null;
		for (int i = 0; i < N - 1; i++) {
			Iterable<DirectedEdge> adj = getAdj(i);
			for (DirectedEdge e : adj) {
				relax(e);
			}
		}
		int node = edgeTo[N - 1].from();
		int length = height();
		int[] seam = new int[length];
		while (length != 0) {
			seam[length - 1] = getCol(node);
			length -= 1;
			DirectedEdge e = edgeTo[node];
			node = e.from();
		}
		return seam;
	}

	// remove horizontal seam from current picture
	public void removeHorizontalSeam(int[] seam) {
		// check valid seam
		int count = 0;

	}

	// remove vertical seam from current picture
	public void removeVerticalSeam(int[] seam) {
	}

	
	private int getNode(int node) {
		if (transpose) {
			return table.get(node);
		}
		return node;
	}
	
	private double getDelta(Color c1, Color c2) {
		return  (Math.pow(c1.getRed() - c2.getRed(), 2)
				+ Math.pow(c1.getGreen() - c2.getGreen(), 2) + Math.pow(
				c1.getBlue() - c2.getBlue(), 2));
	}
	
	private int getCol(int a) {
		int col = (a - 1) % width(); 
		return (a - 1) % width();
	}

	private int getRow(int a) {
		int row = (a - 1) / width();
		return (a - 1) / width();
	}

	private int getI(int col, int row) {
		return row * width() + col + 1;
	}
	
	private Iterable<DirectedEdge> getAdj(int i) {
		LinkedList<DirectedEdge> edges = new LinkedList<DirectedEdge>();
		if (i == 0) {
			for (int j = 1; j < width() + 1; j++) {
				edges.add(new DirectedEdge(0, j, 1000));
			}
			return edges;
		} else if (i == N - 1) {
			return edges;
		}
		int x = getCol(i);
		int y = getRow(i);
		if (y == height() - 1) {
			edges.add(new DirectedEdge(i, N - 1, 0));
			return edges;
		} else {
			if (x - 1 > -1) {
				edges.add(new DirectedEdge(i, getI(x - 1, y + 1), energy(x - 1, y + 1)));
			}
			if (x + 1 < width()) {
				
			
				edges.add(new DirectedEdge(i, getI(x + 1, y + 1), energy(x + 1, y + 1)));
			}
			
			edges.add(new DirectedEdge(i, getI(x, y + 1), energy(x, y + 1)));
		}
		return edges;
	}

	private void relax(DirectedEdge e) {
		int v = e.from();
		int w = e.to();
		if (distTo[w] > distTo[v] + e.weight()) {
			distTo[w] = distTo[v] + e.weight();
			edgeTo[w] = e;
		}
	}


	// show energy
	public static void main(String[] args) {
		String path = "./seamCarving/3x4.png";
		/*
		Picture picture = new Picture(path);
		StdOut.printf("image is %d columns by %d rows\n", picture.width(),
				picture.height());
		picture.show();
		SeamCarver sc = new SeamCarver(picture);

		StdOut.printf("Displaying energy calculated for each pixel.\n");
		SCUtility.showEnergy(sc);
		/*
		Picture picture = new Picture(path);
        StdOut.printf("image is %d pixels wide by %d pixels high.\n", picture.width(), picture.height());
        
        SeamCarver sc = new SeamCarver(picture);
        
        StdOut.printf("Printing energy calculated for each pixel.\n");        

        for (int j = 0; j < sc.height(); j++) {
            for (int i = 0; i < sc.width(); i++)
                StdOut.printf("%9.0f ", sc.energy(i, j));
            StdOut.println();
        }
        */
	}
}