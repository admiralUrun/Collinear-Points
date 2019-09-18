/* *****************************************************************************
 *  Name: Andrii Yakovenko
 *  Date: 07.27.2019
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {

    private  LineSegment[] lines = new LineSegment[2];
    private int segmentTail = 0;
    private int dublicateTail = 0;
    private Dublicate[] dublicates = new Dublicate[2];

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();

        Point[] aux = new Point[points.length];
        sort(points, aux, 0, points.length - 1, pointOrder());
        Point[] copy = Arrays.copyOf(points, points.length);
        Point point;
        for (int i = 0; i < points.length; i++) {
            points = copyArray(copy);
            point = points[i];
            sort(points, aux, i, points.length - 1, point.slopeOrder());
            addSegments(points, i + 1, point);
        }
    }
    public int numberOfSegments() {
        return segmentTail;
    }
    public LineSegment[] segments() {
        LineSegment[] lineSegment = new LineSegment[segmentTail];
        for (int i = 0; i < segmentTail; i++) {
            lineSegment[i] = lines[i];
        }
        return lineSegment;
    }

    private void sort(Point[] a, Point[] aux, int low, int high, Comparator<Point> order) {
        if (high <= low) return;
        int mid = low + (high - low) / 2;

        sort(a, aux, low, mid, order);
        sort(a, aux, mid + 1, high, order);

        if (!isLessThen(a[mid + 1], a[mid], order)) return;

        merge(a, aux, low, mid, high, order);
    }
    private void merge(Point[] a, Point[] aux, int low, int mid, int high, Comparator<Point> order) {
        assert isSorted(a, low, mid, order);
        assert isSorted(a, mid + 1, high, order);

        for (int k = low; k <= high; k++) {
            aux[k] = a[k];
        }

        int i = low, j = mid + 1;
        for (int k = low; k <= high; k++) {
            if (i > mid) {
                a[k] = aux[j++];
            } else if (j > high) {
                a[k] = aux[i++];
            } else if (isLessThen(aux[j], aux[i], order)) {
                a[k] = aux[j++];
            } else {
                a[k] = aux[i++];
            }
        }
        assert isSorted(a, low, high, order);
    }

    private boolean isLessThen(Point p1, Point p2, Comparator<Point> order) {
        if (p1 == null || p2 == null) throw new IllegalArgumentException();

        return order.compare(p1, p2) < 0;
    }

    private class Dublicate {
        Point endPoint;
        double slope;

        Dublicate(double s, Point eP) {
            endPoint = eP;
            slope = s;
        }
    }

    private void addSegments(Point[] a, int startIndex, Point startPoint) {
        if (startIndex >= a.length) return;
        double slope = startPoint.slopeTo(a[startIndex]);
        int onOneSlope = 0;
        for (int i = startIndex; i < a.length; i++) {
            if (slope == startPoint.slopeTo(a[i])) {
                onOneSlope++;
            } else {
                if (onOneSlope >= 3 && checkDublicate(slope, a[i - 1])) {
                    addSegment(new LineSegment(startPoint, a[i - 1]));
                    addDublicate(new Dublicate(slope, a[i - 1]));
                    onOneSlope = 1;
                    slope = startPoint.slopeTo(a[i]);
                } else {
                    onOneSlope = 1;
                    slope = startPoint.slopeTo(a[i]);
                }
            }
        }
        if (onOneSlope >= 3 && checkDublicate(slope, a[a.length - 1])) {
            addSegment(new LineSegment(startPoint, a[a.length - 1]));
            addDublicate(new Dublicate(slope, a[a.length - 1]));
        }
    }

    private boolean isSorted(Point[] a, int low, int hight, Comparator<Point> comparator) {
        for (int i = low; i < hight; i++) {
            if (isLessThen(a[i], a[i - 1], comparator)) return false;
        }
        return true;
    }

    // private Point cheangeEndPoint(Point end, Point point) {
    //     if (point.compareTo(end) < 0) {
    //         return point;
    //     }
    //     return end;
    // }
    // private Point cheangeStartPoint(Point end, Point point) {
    //     if (point.compareTo(end) > 0) {
    //         return point;
    //     }
    //     return end;
    // }

    private Comparator<Point> pointOrder() {
        return new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                return p1.compareTo(p2);
            }
        };
    }

    private boolean checkDublicate(double slope, Point end) {
        if (dublicates.length == 0) { return true; }
        for (int i = 0; i < dublicateTail; i++) {
            if (dublicates[i].endPoint.compareTo(end) == 0 && dublicates[i].slope == slope) {
                return false;
            }
        }
        return true;
    }
    private void addSegment(LineSegment lineSegment) {
        if (segmentTail >= lines.length) {
            resizeLineSegment();
        }
        lines[segmentTail] = lineSegment;
        segmentTail++;
    }
    private void addDublicate(Dublicate dublicate) {
        if (dublicateTail >= dublicates.length) {
            resizeDublicate();
        }
        dublicates[dublicateTail] = dublicate;
        dublicateTail++;
    }
    private Point[] copyArray(Point[] points) {
        Point[] copy = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            copy[i] = points[i];
        }
        return copy;
    }

    private void resizeLineSegment() {
        LineSegment[] copy = new LineSegment[lines.length * 2];
        // System.arraycopy();
        for (int i = 0; i < segmentTail; i++) {
            copy[i] = lines[i];
        }
        lines = copy;
    }
    private void resizeDublicate() {
        Dublicate[] copy = new Dublicate[lines.length * 2];
        for (int i = 0; i < dublicateTail; i++) {
            copy[i] = dublicates[i];
        }
        dublicates = copy;
    }

    public static void main(String[] args) {
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
    }
}
