import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.LinkedList;

// WordNet is a semantic lexicon for the English language that
// computational linguists and cognitive scientists use extensively.
// For example, WordNet was a key component in IBM’s Jeopardy-playing
// Watson computer system. WordNet groups words into sets of synonyms
// called synsets. For example, { AND circuit, AND gate } is a synset
// that represent a logical gate that fires only when all of its inputs
// fire. WordNet also describes semantic relationships between synsets.
// One such relationship is the is-a relationship, which connects
// a hyponym (more specific synset) to a hypernym (more general synset).
// For example, the synset { gate, logic gate } is a hypernym of {
// AND circuit, AND gate } because an AND gate is a kind of logic gate.

public class WordNet {

    private Digraph wordNet; // wordnet digraph
    private HashMap<Integer, String> synset; // hashmap of synsets
    private HashMap<String, LinkedList<Integer>> id; // hashmap of nouns and ids
    private ShortestCommonAncestor scaVar; // shortest common ancestor variable

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("null argument");
        }
        id = new HashMap<String, LinkedList<Integer>>();
        synset = new HashMap<Integer, String>();
        In syn = new In(synsets);
        In hyp = new In(hypernyms);
        int count = 0;

        // synset initialization
        while (syn.hasNextLine()) {
            count++;
            String[] nextLine = syn.readLine().split(",");
            int index = Integer.parseInt(nextLine[0]);
            String[] nouns = nextLine[1].split(" ");
            for (String string : nouns) {
                synset.put(index, nextLine[1]);
                LinkedList<Integer> ids = id.get(string);
                if (ids == null) {
                    ids = new LinkedList<Integer>();
                }
                ids.add(index);
                id.put(string, ids);
            }
        }
        wordNet = new Digraph(count);

        // hypernym initialization
        while (hyp.hasNextLine()) {
            String[] nextLine = hyp.readLine().split(",");
            int index = Integer.parseInt(nextLine[0]);
            for (int i = 1; i < nextLine.length; i++) {
                int x = Integer.parseInt(nextLine[i]);
                wordNet.addEdge(index, x);
            }
        }
        scaVar = new ShortestCommonAncestor(wordNet);
    }

    // the set of all WordNet nouns
    public Iterable<String> nouns() {
        return id.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("null argument");
        }
        return id.containsKey(word);
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        if (noun1 == null || noun2 == null) {
            throw new IllegalArgumentException("null argument");
        }

        LinkedList<Integer> a = id.get(noun1);
        LinkedList<Integer> b = id.get(noun2);

        if (a != null && b != null) {
            int scaID = scaVar.ancestorSubset(a, b);
            return synset.get(scaID);
        }
        else
            throw new IllegalArgumentException("null argument");
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        if (noun1 == null || noun2 == null) {
            throw new IllegalArgumentException("null argument");
        }

        LinkedList<Integer> a = id.get(noun1);
        LinkedList<Integer> b = id.get(noun2);

        if (a != null && b != null) {
            return scaVar.lengthSubset(a, b);
        }
        else
            throw new IllegalArgumentException("null argument");
    }

    // unit testing (required)
    public static void main(String[] args) {
        WordNet word = new WordNet("synsets.txt", "hypernyms.txt");
        StdOut.println("isNoun(): " + word.isNoun("dog"));
        StdOut.println("nouns(): " + word.nouns());
        StdOut.println(word.sca("two-grain_spelt", "Old_World_chat"));
        StdOut.println(word.distance("two-grain_spelt", "Old_World_chat"));
    }
}
