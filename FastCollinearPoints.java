package com.algorithms.week3;

import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints {

	private LineSegment[] lines;
	private int counter=0;
	private Point[] pointMin, pointMax;

	public FastCollinearPoints(Point[] points) {
		if (null == points) {
			throw new java.lang.IllegalArgumentException();
		}

		// check for null point
		int length = points.length;
		for (int i=0;i<length;i++) {
			if (points[i]==null) {
				throw new java.lang.IllegalArgumentException();
			}
		}

		// check for duplicate
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


		pointMin = new Point[length];
		pointMax = new Point[length];
		// finds all line segments containing 4 or more points
		sortPointsBySlope(points, aux);

		pointMin = Arrays.copyOf(pointMin, counter);
		pointMax = Arrays.copyOf(pointMax, counter);

		pointMin = pointMin.clone();
		pointMax = pointMax.clone();

		mergeSortTwoArray(pointMax, pointMin);
		mergeSortTwoArray(pointMin, pointMax);

		lines = new LineSegment[counter];
		int runner =0;
		int idx =0;
		while (runner < counter-1) {

			if (pointMin[runner].compareTo(pointMin[runner+1])==0
					&& pointMax[runner].compareTo(pointMax[runner+1])==0) {
				int subrunner = runner+1;
				while (subrunner < counter &&
						(pointMin[runner].compareTo(pointMin[subrunner])==0
						&& pointMax[runner].compareTo(pointMax[subrunner])==0)) {
					subrunner++;
				}
				LineSegment line = new LineSegment(pointMin[runner], pointMax[runner]);
				lines[idx++] = line;
				runner = subrunner;

			} else {
				LineSegment line = new LineSegment(pointMin[runner], pointMax[runner]);
				lines[idx++] = line;
				runner++;

				if (runner==counter-1) {
					LineSegment line1 = new LineSegment(pointMin[runner], pointMax[runner]);
					lines[idx++] = line1;
				}
			}
		}

		lines = Arrays.copyOf(lines, idx);
	}


	private void mergeSortTwoArray(Point[] sortArray, Point[] followArray) {
		int size = sortArray.length;

		Point[] sortArrayCp = new Point[size];
		Point[] followArrayCp = new Point[size];
		sort(sortArray, followArray, sortArrayCp, followArrayCp, 0, size-1);
	}




	private void sort(Point[] sortArray, Point[] followArray, Point[] sortArrayCp, Point[] followArrayCp, int lo, int hi) {
		if (hi <= lo) {
			return;
		}

		int mid = lo + (hi-lo)/2;
		sort(sortArray, followArray, sortArrayCp, followArrayCp, lo, mid);
		sort(sortArray, followArray, sortArrayCp, followArrayCp, mid+1, hi);
		if (sortArray[mid+1].compareTo(sortArray[mid]) >= 0) {
			return;
		}

		merge(sortArray, followArray, sortArrayCp, followArrayCp, lo, mid, hi);
	}


	private void merge(Point[] sortArray, Point[] followArray, Point[] sortArrayCp, Point[] followArrayCp,
			int lo,
			int mid,
			int hi) {
		for (int k=lo;k<=hi;k++) {
			sortArrayCp[k] = sortArray[k];
			followArrayCp[k] = followArray[k];
		}

		int i=lo,j=mid+1;
		for (int k=lo;k<=hi;k++) {
			if (i>mid) {
				sortArray[k]=sortArrayCp[j];
				followArray[k]=followArrayCp[j];
				j++;
			} else if (j>hi) {
				sortArray[k]=sortArrayCp[i];
				followArray[k]=followArrayCp[i];
				i++;
			} else if (sortArrayCp[i].compareTo(sortArrayCp[j]) <= 0 ) {
				sortArray[k]=sortArrayCp[i];
				followArray[k]=followArrayCp[i];
				i++;
			} else {
				sortArray[k]=sortArrayCp[j];
				followArray[k]=followArrayCp[j];
				j++;
			}
		}

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

	private void sortPointsBySlope(Point[] points, Point[] aux) {
		for (int i=0; i<points.length; i++) {
			Point ref = points[i];
			Arrays.sort(aux, ref.slopeOrder());
			findEqualSlopesMoreThan4(aux);
		}
	}


	private void findEqualSlopesMoreThan4(Point[] aux) {
		int length = aux.length;
		int flagger = 2;

		double slopeBase = aux[0].slopeTo(aux[1]);
		Point max = maxBetween2(aux[0], aux[1]);
		Point min = minBetween2(aux[0], aux[1]);

		for (int i=2;i<length;i++) {
			double slopeToFirstPoint = aux[0].slopeTo(aux[i]);

			if (Double.compare(slopeBase, slopeToFirstPoint) == 0) {
				max = maxBetween2(max, aux[i]);
				min = minBetween2(min, aux[i]);
				flagger++;

				// corner case, last iteration
				if (i==length-1 && flagger >= 4) {
					updateTrackingArray(max, min);
				}

			} else {
				if (flagger >= 4) {
					updateTrackingArray(max, min);
				}
				slopeBase = slopeToFirstPoint;
				max = maxBetween2(aux[0],aux[i]);
				min = minBetween2(aux[0], aux[i]);

				flagger=2;
			}
		}

	}

	private void updateTrackingArray(Point max, Point min) {
		// check memory
		if (counter == pointMin.length) {
			Point[] temp1 = new Point[counter*2];
			Point[] temp2 = new Point[counter*2];
			for (int k=0;k<counter;k++) {
				temp1[k] = pointMin[k];
				temp2[k] = pointMax[k];
			}
			pointMin = temp1;
			pointMax = temp2;

		}

		pointMin[counter] = min;
		pointMax[counter] = max;
		counter++;
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
	    FastCollinearPoints collinear = new FastCollinearPoints(points);
	    for (LineSegment segment : collinear.segments()) {
	        StdOut.println(segment);
	        segment.draw();
	    }
	    StdDraw.show();
	}
}