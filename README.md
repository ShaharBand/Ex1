# Object-Oriented-Programming-Course-Ariel-University-
this repository will contain my exercises solutions. [2020]

we make weighted graphs using node_info as an inner class.
the weight and connections is saved in a manner that the hashmap contains key and weight of the neighboor connection.

the weighted graph algorithm is based on the interface aswell it is an hashmap of nodes and keys that we utilize.

the graph algorithm is utilizeing
uses the shortest list of nodes between 2 nodes in the graph by using BFS search with Priority Queue.
by using the tag we can calculate the distance value of each node and by going through the search using Priority Queue
this logic becomes Dijkstra's Algorithm!

the save and load methods are using a split ',' to get the proper data in which the template is like this:

currentNode Key, currentNode info, currentNode Tag, Neighboor1Id, Neighboor1Weight, Neighboor2Id, Neighboor2Weight ... etc

by reading line by line we can extract the data properly.
