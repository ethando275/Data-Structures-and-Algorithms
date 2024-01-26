Programming Assignment 7: Seam Carving


/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */
To find a vertical seam, my algorithm implements dijikstra's algorithm but instead
of using directed edges and digraphs, it utilizes 2D arrays. To find horizontal
seams, it transposes the image so that all x values become y and vice versa. There
it is able to use the vertical seam finder of dijikstra and return a "vertical"
but actually horizontal seam. Then it transposes the image again to its original
form and returns the seam.

/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */
Images that have uniform background or parts of the image are good for seam carving.
Such as skies or plain colored walls. Images that are too complex and have a lot
going are a bad for seam carving such as cityscapes, intricate art pieces, etc.
Seam carving would also dependon size or aspect ratio of images in terms of its
usefulness. As an image too small may be unsuitable for the algorithm.

/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column and one row (i.e., one call each to
 *  findVerticalSeam(), removeVerticalSeam(), findHorizontalSeam(), and
 *  removeHorizontalSeam()). Use a "doubling" hypothesis, where you
 *  successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */

(keep W constant)
 W = 2000
 multiplicative factor (for H) = 2

 H           time (seconds)      ratio       log ratio
------------------------------------------------------
250         0.279                 -              -
500         0.391                1.401         0.486
1000        0.710                1.816         0.861
2000        1.299                1.830         0.872
4000        2.506                1.929         0.948
...


(keep H constant)
 H = 2000
 multiplicative factor (for W) = 2

 W           time (seconds)      ratio       log ratio
------------------------------------------------------
250         0.301                 -              -
500         0.470                1.561         0.642
1000        0.745                1.586         0.665
2000        1.241                1.666         0.736
4000        2.473                1.993         0.995
...



/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Briefly explain how you determined the formula for the running time.
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */


Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:


    ~ 4.4*10^-9 * W^1.16 * H^2.06
       _______________________________________
I took multiple trial runs of H = 4000 and W = 4000 respectively and used the
equation: running time = a * 4000^x : where x = number of trials. found a by
dividing and isolating "a" to find the coefficients for each



/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */



/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
Finding a vertical seam was extremely tough and it took a while to find something
that kind of worked



/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
