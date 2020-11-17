package ex1;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ex1.WGraph_DS.NodeInfo;

class JunitTest {

    /**
     * returns a new weighted graph for testing by selecting its properties.
     * Note: if the weights conflict the graph class returns the absolute value.
     * @param vertices - amount of graph nodes
     * @param edges - amount of graph connections
     * @param minWeight - minimum weight value for connection
     * @param maxWeight - maximum weight value for connection
     * @return WGraph_DS
     */
	public static WGraph_DS graphCreator(int vertices, int edges, int minWeight, int maxWeight)
	{
		WGraph_DS g = new WGraph_DS();
		for(int i = 0; i < vertices; i++)
			g.addNode(i);
		
        while(g.edgeSize() < edges) {
            int a = getRandomNumber(0,vertices);
            int b = getRandomNumber(0,vertices);
            g.connect(a, b, getRandomNumber(minWeight,maxWeight));
        }
	
		return g;
	}

    /**
     * returns a random number within a given range using Math library.
     * @param min - minimum value of the range
     * @param max - maximum value of the range
     * @return Integer
     */
	public static int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}
	
	@BeforeAll
	public static void graphCreatorTest() {
		WGraph_DS g = graphCreator(5,3,1,4);
		if(g.edgeSize() > 3)
			fail("Failed - 'Graph Creator' function: the amount of edges isnt correct!");
		
		if(g.nodeSize() < 5)
			fail("Failed - 'Graph Creator' function: the amount of nodes isnt correct!");

		for(node_info n : g.getV())
		{
			assertEquals(n.getClass(), NodeInfo.class, "Failed - 'WGraph_DS': dosent use the correct instance for nodes!");
			NodeInfo node = (NodeInfo)n;
			
			for(double w : node.getNi().values())
			{
				if(w < 1)
					fail("Failed - 'Graph Creator' function: implements a weighted graph with incorrect weights!");
				else if(w > 4)
					fail("Failed - 'Graph Creator' function: implements a weighted graph with incorrect weights!");
			}
		}
	}
	
	@Test
	void addNodeTest()
	{
		WGraph_DS g = graphCreator(3,3,1,4);
		
		g.addNode(3);
		assertEquals(g.nodeSize(), 4, "Failed - 'WGraph_DS': the amount of nodes isnt correct!");

		g.addNode(3);
		assertEquals(g.nodeSize(), 4, "Failed - 'WGraph_DS': there is already a node with such a key -> no action should be performed!");
	}
	
	@Test
	void connectionTest()
	{
		int w = 1;
		WGraph_DS g = graphCreator(4,0,w,w);
		g.connect(0, 1, w);
		
		assertEquals(g.getEdge(0, 1), w, "Failed - 'WGraph_DS': the weight implementation is incorrect!");	
		
		NodeInfo node = (NodeInfo)g.getNode(0);
		assertTrue(node.getNi().containsKey(1), "Failed - '(WGraph_DS) -> NodeInfo': the connection hashmap key set isnt properly implemented!");
		assertEquals(node.getNi().get(1), w, "Failed - '(WGraph_DS) -> NodeInfo': the connection hashmap value set isnt properly implemented!");
		
		node = (NodeInfo)g.getNode(1);
		assertTrue(node.getNi().containsKey(0), "Failed - '(WGraph_DS) -> NodeInfo': the connection keys isnt set in two-way direction!");
		assertEquals(node.getNi().get(0), w, "Failed - '(WGraph_DS) -> NodeInfo': the connection weight isnt set in two-way direction!");
		
		g.connect(0, 0, w);
		assertEquals(g.nodeSize(), 4, "Failed - 'WGraph_DS': connecting node to itself isnt allowed!");
		
		assertTrue(g.hasEdge(0, 1), "Failed - 'WGraph_DS': the graph dosent find an edge properly!");
	}
	
	@Test
	void removeNodeTest()
	{
		WGraph_DS g = graphCreator(4,0,1,1);
		g.connect(0, 1, 1.0);
		
		g.removeNode(0);
		assertNull(g.getNode(0), "Failed - 'WGraph_DS': removing a node isnt functioned properly!");
		
		g.removeNode(0);
		assertEquals(g.edgeSize(), 0, "Failed - 'WGraph_DS': the graph edge size isnt correct!");
		assertTrue(!g.hasEdge(0, 1), "Failed - 'WGraph_DS': the graph dosent find an edge properly!");
		
		g.addNode(0);
		g.connect(0, 1, 1.0);
		g.removeEdge(0, 1);
		
		assertEquals(g.edgeSize(), 0, "Failed - 'WGraph_DS': the graph edge size isnt correct!");
		assertTrue(!g.hasEdge(0, 1), "Failed - 'WGraph_DS': the graph dosent find an edge properly!");
	}
	
	@Test
	void bigGraphTest()
	{
		long start = new Date().getTime();
		
		int amountOfV = 1000000; // 1M
		
		WGraph_DS g = new WGraph_DS();
		for(int i = 0; i < amountOfV; i++)
			g.addNode(i);
		
		for(int i = 0; i < 10; i++) // thats 9,999,945 edges
			for(int j = 0; j < amountOfV; j++)
				g.connect(i, j, 1.0);
		
		for(int i = 11; i < 66; i++) // to 10 million edges
			g.connect(i,i+1,1.0);

        long end = new Date().getTime();
        double dt = (end-start)/1000.0;
        boolean t = dt<20;
        assertTrue(t, "runtime test is longer than 20sec to make non-random graph of 1m verts, time taken in seconds: " + dt);
	}
	
	@Test
	void deepCopy()
	{
		WGraph_Algo ga = new WGraph_Algo();		
		ga.init(graphCreator(3,3,1,1));	
		
		assertEquals(ga.copy().edgeSize(),ga.getGraph().edgeSize(), "Failed - 'WGraph_Algo': copy() failed to make similar edge amount!");
		assertEquals(ga.copy().nodeSize(),ga.getGraph().nodeSize(), "Failed - 'WGraph_Algo': copy() failed to make similar node amount!");
		
		for(int i = 0; i < ga.getGraph().nodeSize(); i++)
			for(int j = 0; j < ga.getGraph().nodeSize(); j++)
			assertEquals(ga.copy().getEdge(i, j),ga.getGraph().getEdge(i, j), "Failed - 'WGraph_Algo': copy() failed to make similar values and keys!");
	}
	
	@Test
	void isConnectedTest() {
		WGraph_Algo ga = new WGraph_Algo();
		ga.init(graphCreator(3,3,1,1));
		
		assertTrue(ga.isConnected(), "Failed - 'WGraph_Algo': isConnected() failed!");
		
		ga.init(graphCreator(3,1,1,1));
		assertFalse(ga.isConnected(), "Failed - 'WGraph_Algo': isConnected() failed!");
		
		ga.init(graphCreator(2,1,1,1));
		ga.getGraph().removeEdge(0, 1);
		assertFalse(ga.isConnected(), "Failed - 'WGraph_Algo': isConnected() failed!");
		
		ga.getGraph().addNode(2);
		ga.getGraph().connect(0, 2, 1);
		ga.getGraph().connect(1, 2, 1);
		assertTrue(ga.isConnected(), "Failed - 'WGraph_Algo': isConnected() failed!");
		
		ga.getGraph().addNode(3);
		assertFalse(ga.isConnected(), "Failed - 'WGraph_Algo': isConnected() failed!");
	}
	
	@Test
	void pathDistTest() {
		WGraph_Algo ga = new WGraph_Algo();
		ga.init(graphCreator(2,1,1,1));
		
		ga.getGraph().addNode(2);
		ga.getGraph().connect(0, 2, 5);
		
		assertEquals(ga.shortestPathDist(1, 2), 6.0, "Failed - 'WGraph_Algo': shortestPathDist() failed to calculate the distance!");
		
		ga.getGraph().connect(0, 2, 1);
		assertEquals(ga.shortestPathDist(1, 2), 2.0, "Failed - 'WGraph_Algo': shortestPathDist() failed after changing weight value!");
		
		ga.getGraph().removeEdge(0, 2);
		assertEquals(ga.shortestPathDist(1, 2), -1, "Failed - 'WGraph_Algo': shortestPathDist() failed after removing edge!");
		
		ga.getGraph().addNode(2);
		ga.getGraph().connect(0, 2, 1);
		ga.getGraph().removeNode(0);
		assertEquals(ga.shortestPathDist(1, 2), -1, "Failed - 'WGraph_Algo': shortestPathDist() failed after removing a node!");	
	}
	
	@Test
	void pathListTest() {		
		WGraph_DS g = new WGraph_DS();
		g.addNode(0);
		g.addNode(1);
		g.addNode(2);
		g.addNode(3);
		g.addNode(4);
		g.connect(0, 1, 2);
		
		WGraph_Algo ga = new WGraph_Algo();
		ga.init(g);
		assertNull(ga.shortestPath(0, 3),"Failed - 'WGraph_Algo': shortestPath() didnt return null on a no path scenario!");
		
		ga.getGraph().connect(1, 2, 3);
		ga.getGraph().connect(3, 2, 2);
		
		List<node_info> path = new ArrayList<node_info>(); 
		path.add(ga.getGraph().getNode(0));
		path.add(ga.getGraph().getNode(1));
		path.add(ga.getGraph().getNode(2));
		path.add(ga.getGraph().getNode(3));
		
		for(int i = 0; i < path.size(); i++)
			assertEquals(path.get(i), ga.shortestPath(0, 3).get(i), "Failed - 'WGraph_Algo': shortestPath() didnt return the correct node!");
	}
	
	@Test
	void saveAndLoadGraphTest() {
		WGraph_Algo ga = new WGraph_Algo();
		ga.init(graphCreator(2,1,1,1));
		
		ga.getGraph().addNode(2);
		ga.getGraph().connect(0, 2, 5);
		
		try {
			ga.save("graph.txt");
		}
		catch(Exception e){
			fail("Failed - 'WGraph_Algo': save() failed to save the graph data into a file!");
		}
		WGraph_DS oldG = (WGraph_DS)ga.getGraph();
		
		ga.load("graph.txt");
		
		assertEquals(ga.getGraph().getV().size(),oldG.getV().size(), "Failed - 'WGraph_Algo': load() failed to return similar graph!");
		assertEquals(ga.getGraph().edgeSize(),oldG.edgeSize(), "Failed - 'WGraph_Algo': load() failed to return similar graph!");
		
		assertEquals(ga.getGraph(),oldG);
	}
}
