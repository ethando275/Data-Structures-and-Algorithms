import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;

// Create a symbol-table data type whose keys are two-dimensional
// points. Use a 2d-tree to support efficient range search
// (find all of the points contained in a query rectangle) and
// nearest-neighbor search (find a closest point to a query point).
// 2d-trees have numerous applications, ranging from classifying
// astronomical objects and computer animation to speeding up
// neural networks and data mining. Brute-force implementation.
// Write a mutable data type PointST.java that uses a red–black
// BST to represent a symbol table whose keys are two-dimensional points

public class PointST<Value> {

    // instance variable treemap of points
    private RedBlackBST<Point2D, Value> points;

    // construct an empty symbol table of points
    public PointST() {
        points = new RedBlackBST<>();
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return (points.size() == 0);
    }

    // number of points
    public int size() {
        return points.size();
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null) {
            throw new IllegalArgumentException("an argument is null");
        }
        points.put(p, val);
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument is null");
        }
        return points.get(p);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument is null");
        }
        return points.contains(p);
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        return points.keys();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("argument is null");
        }
        RedBlackBST<Point2D, Value> pointRange = new RedBlackBST<>();
        for (Point2D p : points.keys()) {
            if (rect.contains(p)) {
                pointRange.put(p, points.get(p));
            }
        }
        return pointRange.keys();
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument is null");
        }
        if (isEmpty()) {
            return null;
        }
        Point2D nearest = null; // value of closest point to p
        double minD = Double.POSITIVE_INFINITY; // minimum distance of points
        for (Point2D p2 : points.keys()) {
            double d = p2.distanceSquaredTo(p);
            if (d < minD) {
                nearest = p2;
                minD = d;
            }
        }
        return nearest;
    }

    // unit testing (required)
    public static void main(String[] args) {
        PointST<Integer> tree = new PointST<Integer>();
        tree.put(new Point2D(2, 3), 1);
        tree.put(new Point2D(4, 5), 1);
        tree.put(new Point2D(1, 2), 1);
        tree.put(new Point2D(0, 4), 1);
        tree.put(new Point2D(3, 2), 1);
        tree.put(new Point2D(1, 1), 1);

        StdOut.println("contains(): " + tree.contains(new Point2D(2, 3)));
        StdOut.println("get(): " + tree.get(new Point2D(2, 3)));
        StdOut.println("isEmpty(): " + tree.isEmpty());
        StdOut.println("nearest(): " + tree.nearest(new Point2D(3, 4)));
        StdOut.println("point(): " + tree.points());
        StdOut.println("range(): " + tree.range(new RectHV(0, 0, 4, 5)));
        StdOut.println("size(): " + tree.size());

        // PointST<Integer> tester = new PointST<Integer>();

        // while (!StdIn.isEmpty()) {

        // double x = StdIn.readDouble();
        // double y = StdIn.readDouble();
        // tester.put(new Point2D(x, y), 0);

        // }
        // Stopwatch clock = new Stopwatch();
        // int callsToNearest = 0;

        // for (Point2D p : tester.points()) {
        // tester.nearest(p);
        // callsToNearest++;
        // if (callsToNearest == 1000)
        // break;
        // }
        // double time = clock.elapsedTime();
        // StdOut.print("Calls:" + callsToNearest + ", Time: " + time);
    }

}
