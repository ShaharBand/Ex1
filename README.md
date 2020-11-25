# Exercise 1
This repository will contain my exercises solutions. [2020], Exersice 1 is about weighted graphs and Junit tests.

We make weighted graphs using node_info as an inner class.
The weight and connections contained in a manner that the HashMap contains key and weight of the neighbooring connections.
The weighted graph algorithm is based on the interface aswell, It is an HashMap of nodes and keys that we utilize in the Algorithms.

The graph algorithm is utilizeing BFS search and Dijkstra's Algorithms.
We get the shortest path between 2 nodes in the graph by using BFS search with Priority Queue which is the equvalent to the logic of Dijkstra's Algorithm.
By using the tag of each node we can calculate the distance value of each node and by going through the search using Priority Queue and adding its fathering node value.

The save and load methods are using a split ',' to get the proper data in which the template is like this:
currentNode Key, currentNode info, currentNode Tag, Neighboor1Id, Neighboor1Weight, Neighboor2Id, Neighboor2Weight ... etc
By reading line by line we can extract the data properly.

The Junit tests check the limitations and implementations of the classes.

- Have a nice day, Shahar Band.
