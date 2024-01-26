import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class Autocomplete {

    // list of terms
    private Term[] termList;

    // Initializes the data structure from the given array of terms.
    public Autocomplete(Term[] terms) {
        if (terms == null) {
            throw new IllegalArgumentException("Argument is null");
        }
        for (Term temp : terms) {
            if (temp == null) {
                throw new IllegalArgumentException("Entry is null");
            }
        }

        termList = new Term[terms.length];
        for (int i = 0; i < terms.length; i++) {
            termList[i] = terms[i];
        }
        Arrays.sort(termList);
    }

    // Returns all terms that start with the given prefix,
    // in descending order of weight.
    public Term[] allMatches(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Argument is null");
        }

        Term key = new Term(prefix, 0);
        Comparator<Term> comparator = Term.byPrefixOrder(prefix.length());
        int first = BinarySearchDeluxe.firstIndexOf(termList, key,
                                                    comparator); // first index
        int last = BinarySearchDeluxe.lastIndexOf(termList,
                                                  key, comparator); // last index
        Term[] matchList;

        if (first != -1) {
            matchList = new Term[last - first + 1]; // length + 1 to avoid being 0
        }
        else {
            matchList = new Term[0];
            return matchList;
        }

        for (int i = 0; i < matchList.length; i++) {
            matchList[i] = termList[first++];
        }
        Arrays.sort(matchList, Term.byReverseWeightOrder());
        return matchList;
    }

    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Argument is null");
        }

        Term key = new Term(prefix, 0);
        Comparator<Term> comparator = Term.byPrefixOrder(prefix.length());
        int first = BinarySearchDeluxe.firstIndexOf(termList, key, comparator);
        int last = BinarySearchDeluxe.lastIndexOf(termList, key, comparator);

        return last - first + 1;
    }

    // unit testing (required)
    public static void main(String[] args) {

        // read in the terms from a file
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        Term[] terms = new Term[n];
        for (int i = 0; i < n; i++) {
            long weight = in.readLong();           // read the next weight
            in.readChar();                         // scan past the tab
            String query = in.readLine();          // read the next query
            terms[i] = new Term(query, weight);    // construct the term
        }

        // read in queries from standard input and print the top k matching terms
        int k = Integer.parseInt(args[1]);
        Autocomplete autocomplete = new Autocomplete(terms);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            StdOut.printf("%d matches\n", autocomplete.numberOfMatches(prefix));
            for (int i = 0; i < Math.min(k, results.length); i++)
                StdOut.println(results[i]);
        }
    }

}
