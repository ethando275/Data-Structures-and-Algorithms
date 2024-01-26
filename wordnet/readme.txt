Programming Assignment 6: WordNet


/* *****************************************************************************
 *  Describe concisely the data structure(s) you used to store the
 *  information in synsets.txt. Why did you make this choice?
 **************************************************************************** */
I used 2 hashmaps. One, that stored integers and strings for indices and synsets
respectively. Another, that stored Strings and a linked list of Strings for ids
and nouns. I made this choice because hashmaps are a very efficient and intuitive
data structure for the arbitrary association of object to object. We also went over
this is lecture and hashing was one of the examples given when we went over graphs
and storing information.

/* *****************************************************************************
 *  Describe concisely the data structure(s) you used to store the
 *  information in hypernyms.txt. Why did you make this choice?
 **************************************************************************** */
For hypernyms, I used a digraph that would store its edges as each respective
hypernym.

/* *****************************************************************************
 *  Describe concisely the algorithm you use in the constructor of
 *  ShortestCommonAncestor to check if the digraph is a rooted DAG.
 *  What is the order of growth of the worst-case running times of
 *  your algorithm? Express your answer as a function of the
 *  number of vertices V and the number of edges E in the digraph.
 *  (Do not use other parameters.) Use Big Theta notation to simplify
 *  your answer.
 **************************************************************************** */

Description:
I used the DirectedCycle api and made a conditional that checked if hasCycle() of
DirectedCycle object was true or not and would throw an IllegalArgumentException
if the graph had a cycle.

Order of growth of running time: O(E + V)


/* *****************************************************************************
 *  Describe concisely your algorithm to compute the shortest common ancestor
 *  in ShortestCommonAncestor. For each method, give the order of growth of
 *  the best- and worst-case running times. Express your answers as functions
 *  of the number of vertices V and the number of edges E in the digraph.
 *  (Do not use other parameters.) Use Big Theta notation to simplify your
 *  answers.
 *
 *  If you use hashing, assume the uniform hashing assumption so that put()
 *  and get() take constant time per operation.
 *
 *  Be careful! If you use a BreadthFirstDirectedPaths object, don't forget
 *  to count the time needed to initialize the marked[], edgeTo[], and
 *  distTo[] arrays.
 **************************************************************************** */

Description:
To compute the shortest common ancestor, I made 2 BreadthFirstDirectedPaths object
that stored each vertice v and w in their own BFDP object. I used a for loop to
traverse through each vertice in the digraph. It would check if v and w
had a path to the digraph at vertice i. If yes, then it would compare the summed
distance of both v and w to i and compare it to the previous minimum distance to
an ancestor and store a new min distance champion if the new distance was smaller.
As well as storing the index of the ancestor.

                                 running time
method                  best case            worst case
--------------------------------------------------------
length()                    O(1)                 O(E + V)

ancestor()                  O(1)                   O(E + V)

lengthSubset()              O(1)                 O(E + V)

ancestorSubset()            O(1)                 O(E + V)



/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */



/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */



/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
