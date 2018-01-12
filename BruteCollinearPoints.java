package com.algorithms.week3;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class BruteCollinearPoints {
	private LineSegment[] lines;
	private int linesIdx = 0;

	public BruteCollinearPoints(Point[] points) {

		if (null == points) {
			throw new java.lang.IllegalArgumentException();
		}

		int length = points.length;
		for (int i=0;i<length;i++) {
			if (points[i]==null) {
				throw new java.lang.IllegalArgumentException();
			}
		}

		Point[] aux = points.clone();
		Arrays.sort(aux);

		for (int i=0;i<length;i++) {
			if (i!= length-1) {
				if (aux[i].compareTo(aux[i+1]) == 0) {
					// duplicate points
					throw new java.lang.IllegalArgumentException();
				}
			}
		}


		lines = new LineSegment[length];
		for (int i=0; i<length-3; i++) {
			for (int j=i+1; j<length-2; j++) {
				for (int k=j+1; k<length-1; k++) {
					for (int l=k+1; l<length; l++) {

						double ij = points[i].slopeTo(points[j]);
						double ik = points[i].slopeTo(points[k]);
						double il = points[i].slopeTo(points[l]);


						if (Double.compare(ij, ik) == 0 && Double.compare(ik, il)== 0) {
							Point max = max(points[i], points[j], points[k], points[l]);
							Point min = min(points[i], points[j], points[k], points[l]);

							addToLines(min, max);

						}
					}
				}
			}
		}

		LineSegment[] temp = new LineSegment[linesIdx];
		for (int newIdx=0; newIdx<linesIdx; newIdx++) {
			temp[newIdx] = lines[newIdx];
		}
		lines = temp;
	}

	private void addToLines(Point min, Point max) {

		LineSegment line = new LineSegment(min, max);
		if (linesIdx == lines.length) {
			LineSegment[] temp = new LineSegment[2*lines.length];
			for (int newIdx=0; newIdx<lines.length; newIdx++) {
				temp[newIdx] = lines[newIdx];
			}
			lines = temp;
		}
		lines[linesIdx++] = line;
	}

	private Point min(Point point, Point point2, Point point3, Point point4) {
		Point temp = minBetween2(point, point2);
		temp = minBetween2(temp, point3);
		temp = minBetween2(temp, point4);
		return temp;
	}

	private Point max(Point point, Point point2, Point point3, Point point4) {
		Point temp = maxBetween2(point, point2);
		temp = maxBetween2(temp, point3);
		temp = maxBetween2(temp, point4);
		return temp;
	}

	private Point maxBetween2(Point point, Point point2) {
		if (point.compareTo(point2) >= 0) {
			return point;
		} else {
			return point2;
		}
	}

	private Point minBetween2(Point point, Point point2) {
		if (point.compareTo(point2) >= 0) {
			return point2;
		} else {
			return point;
		}
	}

	public int numberOfSegments() {
		return lines.length;
	}

	public LineSegment[] segments() {
		return lines.clone();
	}



	public static void main(String[] args) {

	    // read the n points from a file
	    In in = new In(args[0]);
	    int n = in.readInt();
	    Point[] points = new Point[n];
	    for (int i = 0; i < n; i++) {
	        int x = in.readInt();
	        int y = in.readInt();
	        points[i] = new Point(x, y);
	    }

	    // draw the points
	    StdDraw.enableDoubleBuffering();
	    StdDraw.setXscale(0, 32768);
	    StdDraw.setYscale(0, 32768);
	    for (Point p : points) {
	        p.draw();
	    }
	    StdDraw.show();

	    // print and draw the line segments
	    //FastCollinearPoints collinear = new FastCollinearPoints(points);
	    BruteCollinearPoints collinear = new BruteCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    StdDraw.show();
	}
}