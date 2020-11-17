package ex1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class WGraph_Algo implements weighted_graph_algorithms {

	public static void main(String[] args) {}
	private weighted_graph algoGraph;
	
	@Override
	public void init(weighted_graph g) {
		this.algoGraph = g;
		
	}

	@Override
	public weighted_graph getGraph() {
		return this.algoGraph;
	}

	// going through all the nodes and edges to recreate the same graph O(2N) => O(n)
	@Override
	public weighted_graph copy() {
		if(!(this.algoGraph instanceof WGraph_DS))return null; // we have no other classes..

		 // adding all the nodes in proper keys to the graph
		WGraph_DS deep = new WGraph_DS();
		Iterator<node_info> iterator = this.algoGraph.getV().iterator();
		
		while (iterator.hasNext()) {
			node_info node = iterator.next();
			deep.addNode(node.getKey());
		}
		
		// adding all the connections
		iterator = this.algoGraph.getV().iterator();
		
		while (iterator.hasNext()) {
			node_info node = iterator.next();
			
			Iterator<node_info> iterator2 = this.algoGraph.getV(node.getKey()).iterator();
			while (iterator2.hasNext()) {
				node_info neighboor = iterator2.next();
				deep.connect(node.getKey(), neighboor.getKey(), this.algoGraph.getEdge(node.getKey(), neighboor.getKey()));
			}  
		}	 
		return deep;
	}

    //returns true if the graph is connected by using BFS search we can count how many nodes are connected and then compare it to the node count.
	@Override
	public boolean isConnected() {
		if(this.algoGraph.nodeSize() < 2)return true;
		resetGraph();
		
		int counter = 0;
		
		Queue<node_info> queue = new ArrayDeque<>();		
		node_info currentNode = algoGraph.getV().iterator().next(), neighboor;
		queue.add(currentNode);
		
		while(!queue.isEmpty()) {
			currentNode = queue.remove();
			
			Iterator<node_info> iterator = this.algoGraph.getV(currentNode.getKey()).iterator();
			while (iterator.hasNext()) {
			    neighboor = iterator.next();
			    
				if(neighboor.getTag() == -1)
			    {
			    	queue.add(neighboor);
			    	neighboor.setTag(1);
			    	counter++;
			    }
			}
		}
		if(counter == algoGraph.nodeSize())return true;
		else return false;
	}
	
    /**
     * returns the shortest distance between 2 nodes in the graph by using BFS search with Priority Queue.
     * by using the tag we can calculate the distance value of each node and by going through the search using Priority Queue
     * this logic becomes Dijkstra's Algorithm!
     * @param src - starting node
     * @param dest - finish node
     * @return the distance (Double).
     */
	@Override
	public double shortestPathDist(int src, int dest) {
		if(this.algoGraph.nodeSize() < 2)return 0;
		resetGraph();
		
		PriorityQueue<node_info> pq = new PriorityQueue<>(new TagComparator()); 	
		node_info currentNode = algoGraph.getNode(src), neighboor;
		currentNode.setTag(0);
		pq.add(currentNode);
		
		while(!pq.isEmpty()) {
			currentNode = pq.poll();
			
			Iterator<node_info> iterator = this.algoGraph.getV(currentNode.getKey()).iterator();
			while (iterator.hasNext()) {
			    neighboor = iterator.next();  
				
				if(neighboor.getTag() == -1)
			    {
			    	neighboor.setTag(currentNode.getTag()+this.algoGraph.getEdge(currentNode.getKey(), neighboor.getKey()));
			    	if(neighboor.getKey() == dest)return neighboor.getTag();
			    	pq.add(neighboor);
			    }
			}
		}
		return -1; // path doesn't exists.
	}
	
    /**
     * returns the shortest list of nodes between 2 nodes in the graph by using BFS search with Priority Queue.
     * by using the tag we can calculate the distance value of each node and by going through the search using Priority Queue
     * this logic becomes Dijkstra's Algorithm! and to save the data in the list we use an helper HashMap with the parent id
     * each child key indicates its parent node in its value within the HashMap so once we find the wanted node we can calculate the path.
     * @param src - starting node
     * @param dest - finish node
     * @return the shortest list of nodes between 2 nodes (List<node_info>).
     */
	@Override
	public List<node_info> shortestPath(int src, int dest) {
		if(this.algoGraph.nodeSize() < 2)return null;
		resetGraph();
		HashMap<Integer, node_info> parents = new HashMap<Integer, node_info>();
		
		PriorityQueue<node_info> pq = new PriorityQueue<>(new TagComparator()); 	
		node_info currentNode = algoGraph.getNode(src), neighboor;
		currentNode.setTag(0);
		pq.add(currentNode);
		
		while(!pq.isEmpty()) {
			currentNode = pq.poll();
			
			Iterator<node_info> iterator = this.algoGraph.getV(currentNode.getKey()).iterator();
			while (iterator.hasNext()) {
			    neighboor = iterator.next();  
				
				if(neighboor.getTag() == -1)
			    {
			    	neighboor.setTag(currentNode.getTag()+this.algoGraph.getEdge(currentNode.getKey(), neighboor.getKey()));
					parents.put(neighboor.getKey(), currentNode); // Making a HashMap where the parent is contained in the key of its child's
					
					if(neighboor.getKey() == dest) // found --> go by the helping HashMap and find parents from bottom to top and put them in the list.
					{
						List<node_info> path = new ArrayList<node_info>();
						currentNode = neighboor;
				        while(currentNode != algoGraph.getNode(src))
				        {
				        	path.add(currentNode);
				        	currentNode = parents.get(currentNode.getKey());
				        }
				        path.add(algoGraph.getNode(src));
				        // reverse order to top to bottom:
				        List<node_info> path2 = new ArrayList<node_info>(); 
				        for (int i = path.size()-1; i >= 0; i--) 
				        	path2.add(path.get(i)); 
				        
				        return path2;
					}
			    	pq.add(neighboor);
			    }
			}
		}
		return null;
	}

	// saves the graph using a string template like this:
	// string: key,info,tag
	// and if there is neighboors in the node we continue the line with:
	// key,weight,key,weight...etc..\n
	// we use StringBuilder because string is mutable and we want efficiency..
	@Override
	public boolean save(String file) {
		try {
			PrintWriter pw = new PrintWriter(new File(file));
			
			StringBuilder sb = new StringBuilder();
			for(node_info node : this.algoGraph.getV())
			{
				sb.append(node.getKey());
				sb.append(",");
				sb.append(node.getInfo());
				sb.append(",");
				sb.append(node.getTag());
				
				for(node_info neighboor : this.algoGraph.getV(node.getKey()))
				{
					sb.append(",");	
					sb.append(neighboor.getKey());
					sb.append(",");
					sb.append(this.algoGraph.getEdge(node.getKey(), neighboor.getKey()));
				}
				sb.append("\n");

				pw.write(sb.toString());
				sb.setLength(0);
			}
			
			pw.close();
		}
		catch(IOException e) {
			return false;
		}
		return true;
	}

	// loads the graph line by line because the template is like this:
	// string: key,info,tag, key,weight,key,weight...etc..\n
	@Override
	public boolean load(String file) {
		String line = "";
		weighted_graph loadGraph = new WGraph_DS();
		
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(file));
			while((line = br.readLine()) != null)
			{
				String[] nodeData = line.split(",");

				int keyValue = Integer.parseInt(nodeData[0]);

				loadGraph.addNode(keyValue);
				loadGraph.getNode(keyValue).setInfo(" ");
				loadGraph.getNode(keyValue).setTag(Double.parseDouble(nodeData[2]));
				
				for(int i = 3; i < nodeData.length; i+=2)
					loadGraph.connect(keyValue, Integer.parseInt(nodeData[i]), Double.parseDouble(nodeData[i+1]));
			}
		}
		catch(IOException e){
			return false;
		}
		
		this.algoGraph = loadGraph;
		return true;
	}
	// reset and not copy for faster run time..
	public void resetGraph() {
		Iterator<node_info> iterator = this.algoGraph.getV().iterator();
		
		while (iterator.hasNext()) {
			node_info node = iterator.next();
			node.setTag(-1);
		}
	}
	
	// for priority queue
	public class TagComparator implements Comparator<node_info>{

		@Override
		public int compare(node_info a, node_info b) {    
	        if(a.getTag() > b.getTag())
	            return 1;
	        else if(a.getTag() == b.getTag())
	             return 0;
	         return -1;
	    }
	}
}
