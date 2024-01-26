import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int R = 256; // standard ASCII size

    // apply move-to-front encoding, reading from StdIn and writing to stdout
    public static void encode() {
        int[] rad = new int[R];
        for (int i = 0; i < R; i++) {
            rad[i] = i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int b = BinaryStdIn.readChar();
            int prev = rad[0];
            int r = 0;
            while (r < R) {
                if (r > 0) {
                    int temp = rad[r];
                    rad[r] = prev;
                    prev = temp;
                }
                if (b == prev) {
                    break;
                }
                r++;
            }
            rad[0] = b;
            BinaryStdOut.write((byte) r);
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from StdIn and writing to stdout
    public static void decode() {
        char[] rad = new char[R];
        for (int i = 0; i < R; i++) {
            rad[i] = (char) i;
        }
        while (!BinaryStdIn.isEmpty()) {
            int input = BinaryStdIn.readInt(8);
            BinaryStdOut.write(rad[input]);
            decodeHelper(rad, input, rad[input]);
        }
        BinaryStdOut.close();
    }

    // helper for decode
    private static void decodeHelper(char[] rad, int input, char radIndex) {
        for (int i = input; i > 0; i--) {
            rad[i] = rad[i - 1];
        }
        rad[0] = radIndex;
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) MoveToFront.encode();
        if (args[0].equals("+")) MoveToFront.decode();
    }
}
