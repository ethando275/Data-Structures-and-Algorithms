import java.util.Comparator;

public class BinarySearchDeluxe {

    // Returns the index of the first key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] a, Key key,
                                         Comparator<Key> comparator) {
        if (a == null || key == null || comparator == null) {
            throw new IllegalArgumentException("An argument is null");
        }

        int low = 0;
        int high = a.length - 1;
        int index = -1;

        while (low <= high) {
            int mid = (high - low) / 2 + low; // middle index
            if (comparator.compare(key, a[mid]) > 0) {
                low = mid + 1;
            }
            else if (comparator.compare(key, a[mid]) < 0) {
                high = mid - 1;
            }
            else {
                high = mid - 1;
                index = mid;
            }
        }
        return index;
    }

    // Returns the index of the last key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        if (a == null || key == null || comparator == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }
        int low = 0;
        int high = a.length;
        int index = -1;

        while (low <= high) {
            int mid = (low + high) / 2 + low;
            if (comparator.compare(key, a[mid]) > 0) {
                low = mid + 1;
            }
            else if (comparator.compare(key, a[mid]) < 0) {
                high = mid - 1;
            }
            else {
                low = mid + 1;
                index = mid;
            }
        }
        return index;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Term test0 = new Term("dog", 9);
        Term test1 = new Term("cat", 2);
        Term test2 = new Term("whale", 8);
        Term test3 = new Term("elephant", 5);
        Term[] test = new Term[4];
        Term key = new Term("cat", 2);

        test[0] = test0;
        test[1] = test1;
        test[2] = test2;
        test[3] = test3;

        System.out.println(BinarySearchDeluxe.firstIndexOf(test, key,
                                                           Term.byPrefixOrder(1)));
        System.out.println(BinarySearchDeluxe.lastIndexOf(test, key,
                                                          Term.byPrefixOrder(1)));
    }
}
