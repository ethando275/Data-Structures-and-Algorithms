import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {
    private int length; // length of string
    private Integer[] index; // array of indices

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }

        char[] val = s.toCharArray(); // list of char values
        length = s.length();
        index = new Integer[s.length()];
        for (int i = 0; i < s.length(); i++) {
            index[i] = i;
        }

        Arrays.sort(index, (Integer i1, Integer i2) -> {
            for (int i = 0; i < length; i++) {
                // break if different character
                if (val[(i1 + i) % length] > val[(i2 + i) % length]) {
                    return 1;
                }
                if (val[(i2 + i) % length] > val[(i1 + i) % length]) {
                    return -1;
                }
            }
            // compare indices
            return i1.compareTo(i2);
        });
    }

    // length of s
    public int length() {
        return length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= length) {
            throw new IllegalArgumentException();
        }
        return index[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray test = new CircularSuffixArray("ABRACADABRA!");
        for (int i = 0; i < test.length(); i++) {
            StdOut.println("indices: " + test.index(i) + " ");
        }
        StdOut.println("length(): " + test.length());

    }

}
