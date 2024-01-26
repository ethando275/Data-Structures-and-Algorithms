import java.util.Arrays;
import java.util.Comparator;

public class Term implements Comparable<Term> {

    private String query; // instance variable
    private long weight;  // instance variable

    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        if (query == null) {
            throw new IllegalArgumentException("string is null");
        }
        if (weight < 0) {
            throw new IllegalArgumentException("weight is negative");
        }

        // initialization of instance variables
        this.query = query;
        this.weight = weight;
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        return new ReverseWeightOrder();
    }

    // nested class of byReverseWeightOrder
    public static class ReverseWeightOrder implements Comparator<Term> {
        public int compare(Term t1, Term t2) {
            if (t1.weight < t2.weight) {
                return 1;
            }
            if (t1.weight == t2.weight) {
                return 0;
            }
            return -1;
        }
    }

    // Compares the two terms in lexicographic order,
    // but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r) {
        if (r < 0) {
            throw new IllegalArgumentException("r is negative");
        }
        return new PrefixOrder(r);
    }

    // nested class of byPrefixOrder
    public static class PrefixOrder implements Comparator<Term> {
        private int pLength; // prefix length

        // constructor/initialization of pLength
        public PrefixOrder(int r) {
            pLength = r;
        }

        public int compare(Term t1, Term t2) {
            int s1 = t1.query.length(); // length of string in t1
            int s2 = t2.query.length(); // length of string in t2
            String q1 = t1.query; // query of t1
            String q2 = t2.query; // query of t2

            if (s1 > pLength) {
                q1 = t1.query.substring(0, pLength);
            }

            if (s2 > pLength) {
                q2 = t2.query.substring(0, pLength);
            }

            return q1.compareTo(q2);
        }
    }

    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that) {
        return this.query.compareTo(that.query);
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
        return Long.toString(weight) + "\t" + query;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Term test0 = new Term("dog", 9);
        Term test1 = new Term("cat", 2);
        Term test2 = new Term("whale", 8);
        Term test3 = new Term("elephant", 5);
        Term[] test = new Term[4];
        test[0] = test0;
        test[1] = test1;
        test[2] = test2;
        test[3] = test3;


        Arrays.sort(test, Term.byPrefixOrder(1));
        System.out.println(test[0].toString());
        System.out.println(test[1].toString());
        System.out.println(test[2].toString());
        System.out.println(test[3].toString());

        Arrays.sort(test, Term.byReverseWeightOrder());
        System.out.println(test[0].toString());
        System.out.println(test[1].toString());
        System.out.println(test[2].toString());
        System.out.println(test[3].toString());
    }
}
