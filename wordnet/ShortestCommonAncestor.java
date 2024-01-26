import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

// An ancestral path between two vertices v and w in a rooted DAG is
// a directed path from v to a common ancestor x, together with a
// directed path from w to the same ancestor x. The shortest ancestral
// path is an ancestral path of minimum total length. We refer to the
// common ancestor in the shortest ancestral path as the shortest common
// ancestor. Note that the shortest common ancestor always exists because
// the root is an ancestor of every vertex. Note also that an ancestral
// path is a path, but not a directed path.

public class ShortestCommonAncestor {

    private Digraph graph; // digraph
    private BreadthFirstDirectedPaths graph1, graph2; // BFS of arguments

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (G == null) {
            throw new IllegalArgumentException("null argument");
        }
        DirectedCycle DC = new DirectedCycle(G);
        if (DC.hasCycle()) {
            throw new IllegalArgumentException("digraph G is not acyclic");
        }

        int count = 0;

        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0) {
                count++;
            }
        }
        if (count != 1) {
            throw new IllegalArgumentException("not rooted");
        }

        graph = new Digraph(G);
    }

    // length of the shortest ancestral path between v and w
    public int length(int v, int w) {
        if (v < 0 || v >= graph.V() || w < 0 || w >= graph.V()) {
            throw new IllegalArgumentException("out of range");
        }
        graph1 = bfsHelp(v); // bfs v
        graph2 = bfsHelp(w); // bfs w
        int numV = graph.V(); // number of vertices
        int min = Integer.MAX_VALUE; // minimum length
        for (int i = 0; i < numV; i++) {
            if (graph1.hasPathTo(i) && graph2.hasPathTo(i)) {
                if (min > graph1.distTo(i) + graph2.distTo(i)) {
                    min = graph1.distTo(i) + graph2.distTo(i);
                }
            }
        }
        if (min != Integer.MAX_VALUE)
            return min;
        else
            return -1;
    }

    // BFS initialization helper method
    private BreadthFirstDirectedPaths bfsHelp(int x) {
        BreadthFirstDirectedPaths g = new BreadthFirstDirectedPaths(graph, x);
        return g;
    }

    // the shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        if (v < 0 || v > graph.V() || w < 0 || w >= graph.V()) {
            throw new IllegalArgumentException("out of range");
        }
        graph1 = bfsHelp(v); // bfs v
        graph2 = bfsHelp(w); // bfs w
        int numV = graph.V(); // number of vertices
        int min = Integer.MAX_VALUE; // minimum length
        int ancestor = -1;
        for (int i = 0; i < numV; i++) {
            if (graph1.hasPathTo(i) && graph2.hasPathTo(i)) {
                if (min > graph1.distTo(i) + graph2.distTo(i)) {
                    min = graph1.distTo(i) + graph2.distTo(i);
                    ancestor = i;
                }
            }
        }
        if (min != Integer.MAX_VALUE)
            return ancestor;
        else
            return -1;
    }

    // length of the shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null) {
            throw new IllegalArgumentException("null argument");
        }
        graph1 = new BreadthFirstDirectedPaths(graph, subsetA); // bfs subsetA
        graph2 = new BreadthFirstDirectedPaths(graph, subsetB); // bfs subsetB
        int numV = graph.V(); // number of vertices
        int min = Integer.MAX_VALUE; // minimum length
        for (int i = 0; i < numV; i++) {
            if (graph1.hasPathTo(i) && graph2.hasPathTo(i)) {
                if (min > graph1.distTo(i) + graph2.distTo(i)) {
                    min = graph1.distTo(i) + graph2.distTo(i);
                }
            }
        }
        if (min != Integer.MAX_VALUE)
            return min;
        else
            return -1;

    }

    // the shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null) {
            throw new IllegalArgumentException("null argument");
        }
        graph1 = new BreadthFirstDirectedPaths(graph, subsetA); // bfs subsetA
        graph2 = new BreadthFirstDirectedPaths(graph, subsetB); // bfs subsetB
        int numV = graph.V(); // number of vertices
        int min = Integer.MAX_VALUE; // minimum length
        int ancestor = -1;
        for (int i = 0; i < numV; i++) {
            if (graph1.hasPathTo(i) && graph2.hasPathTo(i)) {
                if (min > graph1.distTo(i) + graph2.distTo(i)) {
                    min = graph1.distTo(i) + graph2.distTo(i);
                    ancestor = i;
                }
            }
        }
        if (min != Integer.MAX_VALUE)
            return ancestor;
        else
            return -1;
    }

    // unit testing (required)
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

