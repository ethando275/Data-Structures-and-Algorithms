import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        // read input
        String input = BinaryStdIn.readString();
        // utilize CircularSuffixArray API
        CircularSuffixArray csa = new CircularSuffixArray(input);
        for (int i = 0; i < input.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(i);
            }
        }
        for (int i = 0; i < input.length(); i++) {
            BinaryStdOut.write(input.charAt((csa.index(i) - 1 +
                    input.length()) % input.length()));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String input = BinaryStdIn.readString();
        char[] charArr = new char[input.length()];
        int[] next = new int[input.length() + 1];
        int[] count = new int[257]; // Standard ASCII size + 1

        // count frequencies
        for (int i = 0; i < input.length(); i++) {
            count[input.charAt(i) + 1]++;
        }
        // cumulate counts
        for (int i = 0; i < 256; i++) {
            count[i + 1] += count[i];
        }
        // use auxiliary array to sort and use another array
        // to keep track of next traversal
        for (int i = 0; i < input.length(); i++) {
            next[count[input.charAt(i)]] = i;
            charArr[count[input.charAt(i)]++] = input.charAt(i);
        }
        // rebuild original string
        for (int i = 0; i < input.length(); i++) {
            BinaryStdOut.write(charArr[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
    }
}
