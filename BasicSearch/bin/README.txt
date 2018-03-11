Authors: 
Rebecca Barsotti - reb140130
Keene Chin - kac140530

Instructions:

The code is written in Java and takes three arguments in the form of Strings. The first argument specifies the search type. The second specifies whether to use constant or variable costs for moves. The third argument should be a path to the training file. 

An example of how to compile from the command line is below. This will run the program on the first set of test and training data provided for this assignment.

This program was developed on a Mac (OS X) system.



javac GenericSearch.java
java GenericSearch [-cost] <BFS|DFS|UCS|GS|A-star> <inputfile>

ex.

java GenericSearch -cost UCS ./tile1.txt

||

java GenericSearch BFS ./tile2.txt