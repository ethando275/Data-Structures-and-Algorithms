import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.Arrays;

public class SeamCarver {

    private Picture copy; // copy of the picture argument

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("null argument");
        }
        copy = new Picture(picture);
    }

    // current picture
    public Picture picture() {
        return new Picture(copy);
    }

    // width of current picture
    public int width() {
        return copy.width();
    }

    // height of current picture
    public int height() {
        return copy.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > copy.width() || y < 0 || y > copy.height()) {
            throw new IllegalArgumentException("argument out of bounds");
        }
        return Math.sqrt(gradientX(x, y) + gradientY(x, y));
    }

    // returns gradient of x
    private double gradientX(int p1, int p2) {
        int left;
        int right;

        if (width() == 1) {
            left = copy.getRGB(0, p2);
            right = copy.getRGB(0, p2);
        }
        else if (p1 == width() - 1) {
            left = copy.getRGB(p1 - 1, p2);
            right = copy.getRGB(0, p2);
        }
        else if (p1 == 0) {
            left = copy.getRGB(width() - 1, p2);
            right = copy.getRGB(p1 + 1, p2);
        }
        else {
            left = copy.getRGB(p1 - 1, p2);
            right = copy.getRGB(p1 + 1, p2);
        }

        int red = Math.abs(((right >> 16) & 0xFF) - ((left >> 16) & 0xFF));
        int green = Math.abs(((right >> 8) & 0xFF) - ((left >> 8) & 0xFF));
        int blue = Math.abs(((right >> 0) & 0xFF) - ((left >> 0) & 0xFF));

        return Math.pow(red, 2) + Math.pow(green, 2) + Math.pow(blue, 2);
    }

    // returns gradient of y
    private double gradientY(int p1, int p2) {
        int up;
        int down;

        if (height() == 1) {
            up = copy.getRGB(p1, 0);
            down = copy.getRGB(p1, 0);
        }
        else if (p2 == height() - 1) {
            up = copy.getRGB(p1, p2 - 1);
            down = copy.getRGB(p1, 0);
        }
        else if (p2 == 0) {
            up = copy.getRGB(p1, height() - 1);
            down = copy.getRGB(p1, p2 + 1);
        }
        else {
            up = copy.getRGB(p1, p2 - 1);
            down = copy.getRGB(p1, p2 + 1);
        }

        int red = Math.abs(((down >> 16) & 0xFF) - ((up >> 16) & 0xFF));
        int green = Math.abs(((down >> 8) & 0xFF) - ((up >> 8) & 0xFF));
        int blue = Math.abs(((down >> 0) & 0xFF) - ((up >> 0) & 0xFF));

        return Math.pow(red, 2) + Math.pow(green, 2) + Math.pow(blue, 2);
    }

    // transposes copy of picture
    private void transpose() {
        Picture transposed = new Picture(height(), width());
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                transposed.setRGB(y, x, copy.getRGB(x, y));
            }
        }
        copy = transposed;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] seam = findVerticalSeam();
        transpose();
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // dijkstra's implementation
        double[][] distTo = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];
        double[][] energyList = new double[width()][height()];

        // energy
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                energyList[x][y] = energy(x, y);
            }
        }

        // distTo
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (y != 0) {
                    distTo[x][y] = Double.POSITIVE_INFINITY;
                }
                else {
                    distTo[x][y] = energyList[x][y];
                }
            }
        }

        // edge relaxation of bottom neighbors
        for (int y = 0; y < height() - 1; y++) {
            for (int x = 0; x < width(); x++) {
                // check left neighbor
                if (x - 1 >= 0) {
                    if (distTo[x - 1][y + 1] > distTo[x][y] +
                            energyList[x - 1][y + 1]) {
                        distTo[x - 1][y + 1] = distTo[x][y] +
                                energyList[x - 1][y + 1];
                        edgeTo[x - 1][y + 1] = x;
                    }
                }
                // check right neighbor
                if (x + 1 < width()) {
                    if (distTo[x + 1][y + 1] > distTo[x][y] + energyList[x + 1][y + 1]) {
                        distTo[x + 1][y + 1] = distTo[x][y] + energyList[x + 1][y + 1];
                        edgeTo[x + 1][y + 1] = x;
                    }
                }
                // check center
                if (distTo[x][y + 1] > distTo[x][y] + energyList[x][y + 1]) {
                    distTo[x][y + 1] = distTo[x][y] + energyList[x][y + 1];
                    edgeTo[x][y + 1] = x;
                }
            }
        }
        // find seam
        int minX = 0;
        double minE = Double.POSITIVE_INFINITY;
        for (int x = 0; x < width(); x++) {
            if (minE > distTo[x][height() - 1]) {
                minE = distTo[x][height() - 1];
                minX = x;
            }
        }
        int[] seam = new int[height()];
        int minY = height() - 1;
        while (minY >= 0) {
            seam[minY] = minX;
            minX = edgeTo[minX][minY--];
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        // validate seam
        if (seam == null || seam.length != height() || width() <= 1) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i + 1] - seam[i]) > 1) {
                throw new IllegalArgumentException();
            }
        }

        Picture newPicture = new Picture(width() - 1, height());
        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width() - 1; x++) {
                if (x > seam[y]) {
                    newPicture.setRGB(x, y, copy.getRGB(x + 1, y));
                }
                else {
                    newPicture.setRGB(x, y, copy.getRGB(x, y));
                }
            }
        }
        copy = newPicture;
    }

    //  unit testing (required)
    public static void main(String[] args) {
        Picture test = new Picture("7x10.png");
        SeamCarver carve = new SeamCarver(test);
        StdOut.println("picture(): " + carve.picture());
        StdOut.println("width(): " + carve.width());
        StdOut.println("height(): " + carve.height());
        StdOut.println("energy(0, 0): " + carve.energy(0, 0));
        Stopwatch test1 = new Stopwatch();
        StdOut.println(
                "horizontal and vertical: " +
                        Arrays.toString(carve.findHorizontalSeam()) + " "
                        + Arrays.toString(
                        carve.findVerticalSeam()));
        carve.removeVerticalSeam(carve.findVerticalSeam());
        StdOut.println("width(): " + carve.width());
        carve.removeHorizontalSeam(carve.findHorizontalSeam());
        StdOut.println("height(): " + carve.height());
        StdOut.println("elapsedTime() " + test1.elapsedTime());
    }
}

