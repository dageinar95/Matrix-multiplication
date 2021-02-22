# Matrix multiplication
6 different ways of executing and optimizing matrix multiplication, with parallellization and transposing of matrices.

Each code file contains one combination of a sequential/parallel algorithm with either no transposed matrices, matrice A transposed or matrice B transposed.

This is to show how both parallelization of an algorithm and transposing of matrices can vastly improve performance and execution times.

How to run:

Compile all seven .java-files. You can run each file (except for the precode) like this:
java <file_name> <seed> <n>

where seed is an integer used to randomize the matrices (different seeds give different matrices), and n is the number of rows and columns in each matrix (n*n).

To run e.g. the parallel algorithm with matrice B transposed, with seed = 10 and n = 500:
java ParaBTransposed 10 500

The programs will each provide feedback to confirm that the multiplication was done correctly by comparing to the non-sequential and not transposed version.
It will then print the median runtime after 7 runs, and if n <= it will write the resulting matrix to file. 