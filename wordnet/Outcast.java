import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wn; // wordnet

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        if (wordnet == null) {
            throw new IllegalArgumentException("null argument");
        }
        wn = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null) {
            throw new IllegalArgumentException("null argument");
        }
        String outcast = nouns[0];
        int distance = 0;
        for (int i = 0; i < nouns.length; i++) {
            int currD = 0;
            for (int j = 0; j < nouns.length; j++) {
                currD = currD + (wn.distance(nouns[i], nouns[j]));
                if (distance < currD) {
                    distance = currD;
                    outcast = nouns[i];
                }
            }
        }
        return outcast;
    }

    // test client (see below)
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

}
