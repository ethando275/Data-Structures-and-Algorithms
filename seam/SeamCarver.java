import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    // keeps track of current picture width
    private int width;
    // keep tracks of current picture height
    private int height;
    // current copy of Picture to be resized
    private Picture picture;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();
    }

    // creates defensive copy of current picture
    public Picture picture() {
        return new Picture(picture);
    }


    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // helper function to energy(), gets the x Values of energy to be used in
    // gradient function. Also ensures that energy will wrap around
    private int[] getXEnergy(int x, int y) {
        int x1, x2;
        // special case for when picture width is 1
        if (picture.width() == 1) {
            x1 = picture.getRGB(0, y);
            x2 = picture.getRGB(0, y);
        }
        // special case for edges
        else if (x == 0) {
            x1 = picture.getRGB(picture.width() - 1, y);
            x2 = picture.getRGB(x + 1, y);
        }
        // special case for edges
        else if (x == picture.width() - 1) {
            x1 = picture.getRGB(x - 1, y);
            x2 = picture.getRGB(0, y);
        }
        // otherwise, takes regular neighbors' colors for energy computation
        else {
            x1 = picture.getRGB(x - 1, y);
            x2 = picture.getRGB(x + 1, y);
        }
        int[] xColors = { x1, x2 };
        return xColors;
    }

    // helper function to energy(), gets the y Values of energy to be used in
    // gradient function. Also ensures that energy will wrap around
    private int[] getYEnergy(int x, int y) {
        int y1, y2;
        // special case where picture height is 1
        if (picture.height() == 1) {
            y1 = picture.getRGB(x, 0);
            y2 = picture.getRGB(x, 0);
        }
        // special case for picture edges
        else if (y == 0) {
            y1 = picture.getRGB(x, picture.height() - 1);
            y2 = picture.getRGB(x, y + 1);
        }
        // special case for picture edges
        else if (y == picture.height() - 1) {
            y1 = picture.getRGB(x, y - 1);
            y2 = picture.getRGB(x, 0);
        }
        // otherwise, gets regular neighbors' colors for energy computation
        else {
            y1 = picture.getRGB(x, y - 1);
            y2 = picture.getRGB(x, y + 1);
        }
        int[] yColors = { y1, y2 };
        return yColors;
    }

    // helper function for energy, calculates gradient
    private double gradient(int color1RGB, int color2RGB) {
        int red1 = (color1RGB >> 16) & 0xFF;
        int green1 = (color1RGB >> 8) & 0xFF;
        int blue1 = (color1RGB >> 0) & 0xFF;
        int red2 = (color2RGB >> 16) & 0xFF;
        int green2 = (color2RGB >> 8) & 0xFF;
        int blue2 = (color2RGB >> 0) & 0xFF;
        return Math.pow(red2 - red1, 2) + Math.pow(
                blue2 - blue1, 2) + Math.pow(
                green2 - green1, 2);
    }

    // calculates energy of pixel at column x and row y using helpers above
    public double energy(int x, int y) {
        if (x >= width() || y >= height || x < 0 || y < 0) {
            throw new IllegalArgumentException();
        }
        int[] xColors = getXEnergy(x, y);
        int[] yColors = getYEnergy(x, y);
        int x1 = xColors[0];
        int x2 = xColors[1];
        int y1 = yColors[0];
        int y2 = yColors[1];
        return Math.sqrt(gradient(x1, x2) + gradient(y1, y2));
    }

    // creates double[][] energy array to calculate energy of all pixels
    // using energy() function above. called when finding seams
    private double[][] initializeEnergy() {
        double[][] energy = new double[width][height];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                energy[col][row] = energy(col, row);
            }
        }
        return energy;
    }

    // finds the minimum seam (used for both vert/horz) using a modified version
    // of dijkstra's and relaxing edges algorithms discussed in lecture
    public int[] findVerticalSeam() {
        // initialize energy only within local to save space/time
        double[][] energy = initializeEnergy();
        // applying dikstra's, but use double arrays instead
        int[][] edgeTo = new int[width][height];
        double[][] distTo = new double[width][height];
        // initialize distTo
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                // row zero has no "previous" to add to cost
                if (row == 0) {
                    distTo[col][row] = energy[col][row];
                }
                else {
                    distTo[col][row] = Double.POSITIVE_INFINITY;
                }
            }
        }

        // construct disTo[][] and edgeTo[][]
        // a variation of relax where edge Weight ~= distTo
        for (int r = 0; r < height - 1; r++) {
            for (int c = 0; c < width; c++) {
                double currCost = distTo[c][r] + energy[c][r + 1];
                if (distTo[c][r + 1] > currCost) {
                    distTo[c][r + 1] = currCost;
                    edgeTo[c][r + 1] = c;
                }
                // check left neighbor
                if (c - 1 >= 0) {
                    double leftNeighborCost = distTo[c][r] + energy[c - 1][r + 1];
                    if (distTo[c - 1][r + 1] > leftNeighborCost) {
                        distTo[c - 1][r + 1] = leftNeighborCost;
                        edgeTo[c - 1][r + 1] = c;
                    }
                }
                // check right neighbor
                if (c + 1 < width) {
                    double rightNeighborCost = distTo[c][r] + energy[c + 1][r + 1];
                    if (distTo[c + 1][r + 1] > rightNeighborCost) {
                        distTo[c + 1][r + 1] = rightNeighborCost;
                        edgeTo[c + 1][r + 1] = c;
                    }
                }
            }
        }
        // after constructing distTo and edgeTo, can find min vertical seam
        return getMinSeam(distTo, edgeTo);
    }

    // helper function for finding min seam, determines the col at bottom w/
    // the minimum energy using constructed distTo array
    private int[] getMinSeam(double[][] distTo, int[][] edgeTo) {
        int minCol = 0;
        double minEnergy = Double.POSITIVE_INFINITY;
        for (int col = 0; col < width; col++) {
            double currColEnergy = distTo[col][height - 1];
            if (minEnergy > currColEnergy) {
                minEnergy = currColEnergy;
                minCol = col;
            }
        }
        //
        return getSeam(edgeTo, minCol);
    }

    // works backwards from bottom to get/return min seam using constructed edgeTo
    private int[] getSeam(int[][] edgeTo, int minCol) {
        int[] seam = new int[height];
        int row = height - 1;
        while (row >= 0) {
            seam[row] = minCol;
            minCol = edgeTo[minCol][row--];
        }
        return seam;
    }

    // finds min horz seam by transposing picture and then using findVerticalSeam
    public int[] findHorizontalSeam() {
        transpose();
        int[] horzSeam = findVerticalSeam();
        transpose();
        return horzSeam;
    }

    // validates seam before seam removal
    private boolean checkForInvalidEntries(int[] seam, int maxValAllowed) {
        // illegal entry if two adjacent entries differ by more than 1
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i + 1] - seam[i]) > 1) {
                return true;
            }
        }
        // illegal entry if outside bounds (width/height dep on orientation)
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= maxValAllowed) {
                return true;
            }
        }
        return false;
    }


    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        // validates seam
        if (seam == null || seam.length != height || width() <= 1
                || checkForInvalidEntries(seam, width())) {
            throw new IllegalArgumentException();
        }
        // set up new pic with removed seam
        Picture temp = new Picture(width - 1, height);
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width - 1; col++) {
                // if pixel before deleted pixel, it's the same as old pic
                if (col < seam[row]) {
                    temp.setRGB(col, row, picture.getRGB(col, row));
                }
                // if pixel after deleted pixel, get next pixel from old pic
                else {
                    temp.setRGB(col, row, picture.getRGB(col + 1, row));
                }
            }
        }
        // update instance variables with new data
        picture = temp;
        width--;
    }

    // remove horizontal seam from current picture by transposing picture and
    // then using removeVerticalSeam
    public void removeHorizontalSeam(int[] seam) {
        // validate seam
        if (seam == null || seam.length != width() || height() <= 1 ||
                checkForInvalidEntries(seam, height())) {
            throw new IllegalArgumentException();
        }
        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // transpose the current pic
    private void transpose() {
        // swap height and width
        Picture temp = new Picture(height, width);
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < height; col++) {
                temp.setRGB(col, row, picture.getRGB(row, col));
            }
        }
        // update instance variables
        picture = temp;
        int newWidth = height;
        height = width;
        width = newWidth;
    }

    // unit testing
    public static void main(String[] args) {
        System.out.println("Unit testing: testing constructor with 6x5.png");
        Picture pic = new Picture("1x1.png");
        SeamCarver sc = new SeamCarver(pic);
        System.out.println("Successfully constructed ");
        System.out.println("Testing width() method, should print 6: " + sc.width());
        System.out.println("Testing width() method, should print 6: " + sc.height());
        System.out.println("Testing energy method at (1, 2), should print"
                                   + " 138.69: " + sc.energy(1, 2));
        System.out.println("Testing energy method on border pixels: ");
        System.out.println("Testing energy method at (0,0), should print "
                                   + "240.18: " + sc.energy(0, 0));
        System.out.println("Testing energy method at (5,0), should print "
                                   + "192.99: " + sc.energy(5, 0));
        System.out.println("Testing energy method at (0,4), should print "
                                   + "179.82: " + sc.energy(0, 4));
        System.out.println("Testing energy method at (5,4), should print "
                                   + "191.2: " + sc.energy(5, 4));
        System.out.println("Testing of energy method complete.");
        System.out.println("Testing of picture() function: ");
        sc.picture();
        System.out.println("picture() run successfully");
        System.out.println("Testing of findHorizontalSeam() function: ");
        System.out.println("Finding horizontal seam of 6x5 png, "
                                   + "should print {2, 2, 1, 2, 1, 2}");
        int[] horzSeam = sc.findHorizontalSeam();
        System.out.print("found horz seam: ");
        for (int elt : horzSeam) {
            System.out.print(elt + " ");
        }
        System.out.println();
        System.out.println("Testing of findVerticalSeam() function: ");
        System.out.println("Finding vertical seam of 6x5 png, "
                                   + "should print {3, 4, 3, 2, 2}");
        int[] vertSeam = sc.findVerticalSeam();
        System.out.print("found vert seam: ");
        for (int elt : vertSeam) {
            System.out.print(elt + " ");
        }
        System.out.println();
        System.out.println("Testing finding seams on more complicated "
                                   + "image (diagonals.png): ");
        Picture pic2 = new Picture("diagonals.png");
        SeamCarver sc2 = new SeamCarver(pic2);
        int[] vertSeam2 = sc2.findVerticalSeam();
        System.out.println("Finding vertical seam, should print: "
                                   + "{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }");
        System.out.print("found vert seam: ");
        for (int elt : vertSeam2) {
            System.out.print(elt + " ");
        }
        System.out.println();
        System.out.println("Testing removing seams on 6x5.png by removing "
                                   + "above min vert seam: ");
        sc.removeVerticalSeam(vertSeam);
        System.out.println("New picture should be 5x5. Calling width() and "
                                   + "height() to check");
        System.out.println("new width: " + sc.width() + " and new height: "
                                   + sc.height());
        System.out.println("Finding and removing min horz seam: ");
        sc.removeHorizontalSeam(sc.findHorizontalSeam());
        System.out.println("New picture should be 5x4. Calling width() and "
                                   + "height() to check");
        System.out.println("new width: " + sc.width() + " and new height: "
                                   + sc.height());
        System.out.println("All methods functional, unit testing complete");

    }
}
