/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class BruteCollinearPoints {

    private  LineSegment[] lines = new LineSegment[2];
    private int tail = 0;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.IllegalArgumentException();
        Point[] pointsOnSlope = new Point[4];
        for (int i = 0; i < points.length; i++) {
            isNull(points[i]);
            Comparator<Point> order = points[i].slopeOrder();
            pointsOnSlope[0] = points[i];
            for (int j = i + 1; j < points.length; j++) {
                isNull(points[j]);
                pointsOnSlope[1] = points[j];
                for (int f = j + 1; f < points.length; f++) {
                    isNull(points[f]);
                    if (order.compare(points[j], points[f]) != 0) {
                        continue;
                    }
                    pointsOnSlope[2] = points[f];
                    for (int k = f + 1; k < points.length; k++) {
                        isNull(points[k]);
                        if (order.compare(points[f], points[k]) == 0) {
                            pointsOnSlope[3] = points[k];
                            Point[] p = checkPoints(pointsOnSlope);
                            addSegment(new LineSegment(p[0], p[1]));

                        }
                    }
                }
            }
        }
        // finds all line segments containing 4 points
    }

    public int numberOfSegments() {
        return tail;
    }

    public LineSegment[] segments() {
        LineSegment[] lineSegment = new LineSegment[tail];
        for (int i = 0; i < tail; i++) {
            lineSegment[i] = lines[i];
        }
        return lineSegment;
    }

    private void addSegment(LineSegment lineSegment) {
        if (lines.length <= tail) {
            resize();
        }
        lines[tail] = lineSegment;
        tail++;


    }

    private void resize() {
        LineSegment[] copy = new LineSegment[lines.length * 2];
            for (int i = 0; i < tail; i++) {
                copy[i] = lines[i];
            }
            lines = copy;
    }

    private void isNull(Point p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
    }
    private Point[] checkPoints(Point[] p) {
        Comparator oreder = pointOrder();
        Point minPoint = p[0];
        Point maxPoint = p[0];
        for (int i = 0; i < p.length; i++) {
            if (oreder.compare(p[i], minPoint) < 0) {
                minPoint = p[i];
            } else if (oreder.compare(p[i], maxPoint)  > 0) {
                maxPoint = p[i];
            }
        }
        return new Point[] { minPoint, maxPoint };
    }

    private Comparator<Point> pointOrder() {
        return new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                return p1.compareTo(p2);
            }
        };
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
