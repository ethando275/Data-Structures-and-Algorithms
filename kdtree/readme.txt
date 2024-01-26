Programming Assignment 5: K-d Trees

/* *****************************************************************************
 *  First, fill out the mid-semester survey:
 *  https://forms.gle/LdhX4bGvaBYYYXs97
 *
 *  If you're working with a partner, please do this separately.
 *
 *  Type your initials below to confirm that you've completed the survey.
 **************************************************************************** */
ED, DB

/* *****************************************************************************
 *  Describe the Node data type you used to implement the
 *  2d-tree data structure.
 **************************************************************************** */
It’s a Node that contains a point, a piece of data (the <Value>), and a pointer
to the left tree and a pointer to the right tree

/* *****************************************************************************
 *  Describe your method for range search in a k-d tree.
 **************************************************************************** */
The range search method uses a helper method that recursively checks the root as
well as all the children of the root if it intersects with the inputted rectangle
object. It will add the node into a local array if intersection occurs, otherwise
it will go to the next child until all nodes checked (until null is checked).
Once null is found, the array of intersected points is returned


/* *****************************************************************************
 *  Describe your method for nearest neighbor search in a k-d tree.
 **************************************************************************** */
Nearest neighbor search utilizes the nearest helper method that will return the
root node if minimum point is non exist or if the inputted point is the root point.
Else it will check the left and right node and discard accordingly based on if the
point is smaller or large than each child.

/* *****************************************************************************
 *  How many nearest-neighbor calculations can your PointST implementation
 *  perform per second for input1M.txt (1 million points), where the query
 *  points are random points in the unit square?
 *
 *  Fill in the table below, rounding each value to use one digit after
 *  the decimal point. Use at least 1 second of CPU time. Do not use -Xint.
 *  (Do not count the time to read the points or to build the 2d-tree.)
 *
 *  Repeat the same question but with your KdTreeST implementation.
 *
 **************************************************************************** */


                 # calls to         /   CPU time     =   # calls to nearest()
                 client nearest()       (seconds)        per second
                ------------------------------------------------------
PointST:        1000 calls / 57.855 secs = 17.275 calls/sec

KdTreeST:       1000 calls / 0.12 secs = 8,333.33 calls/sec

Note: more calls per second indicates better performance.


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */



/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */




/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on  how helpful the class meeting was and on how much you learned
 * from doing the assignment, and whether you enjoyed doing it.
 **************************************************************************** */
