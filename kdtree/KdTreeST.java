import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdOut;

// A 2d-tree is a generalization of a BST to two-dimensional keys.
// The idea is to build a BST with points in the nodes, using the
// x- and y-coordinates of the points as keys in strictly alternating
// sequence, starting with the x-coordinates. The prime advantage of
// a 2d-tree over a BST is that it supports efficient implementation
// of range search and nearest-neighbor search. Each node corresponds
// to an axis-aligned rectangle, which encloses all the points in its subtree.

public class KdTreeST<Value> {

    // private instance variable for the root node
    private Node root;
    // private instance keeps track of the size of the tree
    private int size;

    // constructor for class
    public KdTreeST() {
        root = null;
        size = 0;
    }

    private class Node {
        // instance variables for key and associated val
        private Point2D point;
        // instance variable for the value of the Node
        private Value val;

        // instance references to the left and right trees
        private Node left, right;

        // instance variable referring to the rectangle the node falls in
        private RectHV rect;

        // node/point constructor
        private Node(Point2D point, Value val, double xmin, double ymin,
                     double xmax, double ymax) {
            this.val = val;
            this.point = point;
            rect = new RectHV(xmin, ymin, xmax, ymax);
        }
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points
    public int size() {
        return size;
    }

    // private helper method to determine whether to get x compare or y compare
    private double getCmp(boolean vert, Node current, Point2D point) {
        double cmp;
        if (vert)
            cmp = current.point.x() - point.x();
        else
            cmp = current.point.y() - point.y();
        return cmp;

    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new IllegalArgumentException("Null argument");
        boolean vert = true;

        double max = Double.POSITIVE_INFINITY, min = Double.NEGATIVE_INFINITY;

        root = put(root, p, val, vert, min, min, max, max, false);
    }

    // helper put method for recursion
    private Node put(Node current, Point2D point, Value val, boolean vert,
                     double xmin, double ymin, double xmax,
                     double ymax, boolean duplicate) {

        // creating new Node at null point with updated
        if (current == null && !duplicate) {
            size++;
            return new Node(point, val, xmin, ymin, xmax, ymax);
        }

        // checking if the points are the same
        if (point.equals(current.point)) {
            current.val = val;
            duplicate = true;
            return current;
        }

        // comparing the correct x or y coordinate of each point
        double cmp = getCmp(vert, current, point);

        // if x/y of new point less, go left
        if (cmp > 0) {
            // updating required x max / y max variables for the rectangle
            if (vert)
                xmax = current.point.x();
            else
                ymax = current.point.y();
            current.left = put(current.left, point, val, !vert,
                               xmin, ymin, xmax, ymax, duplicate);
        }
        // if x/y of new point is greater, go right
        else if (cmp <= 0) {
            if (vert)
                xmin = current.point.x();
            else
                ymin = current.point.y();
            current.right = put(current.right, point, val, !vert,
                                xmin, ymin, xmax, ymax, duplicate);
        }

        return current;
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument is null");
        }
        return get(root, p, true);
    }

    // private helper method for getting a value at a point
    private Value get(Node current, Point2D point, boolean vert) {
        if (current == null)
            return null;

        double cmp = getCmp(vert, current, point);

        // value is smaller so go left
        if (cmp > 0)
            return get(current.left, point, !vert);
            // value is bigger so go right
        else if (cmp < 0)
            return get(current.right, point, !vert);
            // found value!
        else {
            if (getCmp(!vert, current, point) == 0)
                return current.val;
            else
                return get(current.right, point, !vert);
        }
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("null argument");
        }
        return (get(p) != null);
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        Queue<Point2D> pointQ = new Queue<Point2D>();
        Queue<Node> nodeQ = new Queue<Node>();
        nodeQ.enqueue(root);
        while (!nodeQ.isEmpty()) {
            Node current = nodeQ.dequeue();
            if (current == null) continue;
            pointQ.enqueue(current.point);
            nodeQ.enqueue(current.left);
            nodeQ.enqueue(current.right);
        }
        return pointQ;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("null argument");
        }
        Queue<Point2D> insidePoints = new Queue<Point2D>();
        rangeHelper(root, rect, insidePoints);
        return insidePoints;
    }

    // range helper method that checks every child node for intersection of rect
    private void rangeHelper(Node node, RectHV inputRect, Queue<Point2D> insideRect) {
        if (node == null) {
            return;
        }
        if (inputRect.intersects(node.rect)) {
            if (inputRect.contains(node.point)) {
                insideRect.enqueue(node.point);
            }
            rangeHelper(node.left, inputRect, insideRect);
            rangeHelper(node.right, inputRect, insideRect);
        }
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("null argument");
        }
        if (isEmpty()) {
            return null;
        }
        return nearestHelper(root, p, null);
    }

    // nearest helper method
    private Point2D nearestHelper(Node node, Point2D p, Point2D min) {
        if (node != null) {
            if (min == null ||
                    (node.point.distanceSquaredTo(p) <= min.distanceSquaredTo(p))) {
                if (min == null)
                    node.point.distanceSquaredTo(p);
                min = node.point;
            }
            if (node.right != null && node.right.rect.contains(p)) {
                min = nearestHelper(node.right, p, min);
                if (node.left != null &&
                        node.left.rect.distanceSquaredTo(p) <
                                p.distanceSquaredTo(min))
                    min = nearestHelper(node.left, p, min);
            }
            else {
                min = nearestHelper(node.left, p, min);
                if (node.right != null &&
                        node.right.rect.distanceSquaredTo(p) <
                                p.distanceSquaredTo(min))
                    min = nearestHelper(node.right, p, min);
            }
        }
        return min;
    }

    // unit testing (required)
    public static void main(String[] args) {
        KdTreeST<Integer> tree = new KdTreeST<Integer>();
        tree.put(new Point2D(2, 3), 1);
        tree.put(new Point2D(4, 5), 2);
        tree.put(new Point2D(1, 2), 3);
        tree.put(new Point2D(0, 4), 4);
        tree.put(new Point2D(3, 2), 5);
        tree.put(new Point2D(1, 1), 6);
        tree.put(new Point2D(1, 1), 7);
        RectHV test = new RectHV(0, 0, 4, 5);

        for (Point2D p : tree.points()) {
            System.out.print(p + " ");
        }

        StdOut.println("\nisEmpty(): " + tree.isEmpty());
        StdOut.println("size(): " + tree.size());
        StdOut.println("Contains (2,3): " + tree.contains(new Point2D(2, 3)));
        StdOut.println("Get (2,3): " + tree.get(new Point2D(2, 3)));
        StdOut.println("Test Rect: " + test);
        StdOut.println("Range(new RectHV(0, 0 , 4, 5): " + tree.range(test));
        StdOut.println("nearest(): " + tree.nearest(new Point2D(4.5, 6)));

        // KdTreeST<String> tester = new KdTreeST<String>();


        // KdTreeST<String> test1 = new KdTreeST<String>();
        // tester.put(new Point2D(.75, 1), "A");
        // tester.put(new Point2D(.75, .25), "B");
        // tester.put(new Point2D(1, 1), "C");
        // tester.put(new Point2D(1, .25), "D");
        // tester.put(new Point2D(.5, .5), "E");
        // tester.put(new Point2D(0, 0), "F");
        // tester.put(new Point2D(0.25, 0.75), "G");
        // tester.put(new Point2D(0.25, 0.25), "H");
        // tester.put(new Point2D(0.75, 0.75), "I");
        // tester.put(new Point2D(0.25, 0.5), "J");
        // StdOut.println("nearest() " + tester.nearest(new Point2D(0.35, .11)));
        // StdOut.println("range() " + tester.range(new RectHV(0, .5, 1, .75)));
        //
        // StdOut.print(tester.points());

        // KdTreeST<Integer> tester = new KdTreeST<Integer>();
        //
        // // inputs1M.txt tester
        // while (!StdIn.isEmpty()) {
        //
        //     double x = StdIn.readDouble();
        //     double y = StdIn.readDouble();
        //     tester.put(new Point2D(x, y), 0);
        //
        // }
        // Stopwatch clock = new Stopwatch();
        // int callsToNearest = 0;
        //
        // for (Point2D p : tester.points()) {
        //     tester.nearest(p);
        //     callsToNearest++;
        //     if (callsToNearest == 1000)
        //         break;
        // }
        // double time = clock.elapsedTime();
        // StdOut.print("Calls:" + callsToNearest + ", Time: " + time);
    }
}
