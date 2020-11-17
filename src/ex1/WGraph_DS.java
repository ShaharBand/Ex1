package ex1;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class WGraph_DS implements weighted_graph {
	
	public static class NodeInfo implements node_info {

		private HashMap<Integer, Double> neighboors = new HashMap<Integer, Double>(); // Key, Weight
		private String data;
		private double tag;
		private int key;
		
		public NodeInfo(int key)
		{
			this.data = null;
			this.tag = 0;
			this.key = key;
		}

		@Override
		public int getKey() {
			return this.key;
		}

		public HashMap<Integer, Double> getNi() {
	        return neighboors;
		}

		public boolean hasNi(int key) {
			return (neighboors.containsKey(key) && key != this.getKey());
		}

		public void addNi(node_info t, double w) {
			neighboors.put(t.getKey(), Math.abs(w));
		}

		public void removeNode(node_info node) {
			if(!hasNi(node.getKey()))return;
			neighboors.remove(node.getKey());
		}

		@Override
		public String getInfo() {
			return this.data;
		}

		@Override
		public void setInfo(String s) {
			this.data = s;
		}

		@Override
		public double getTag() {
			return this.tag;
		}

		@Override
		public void setTag(double t) {
			this.tag = t;
		}
	}
	
	private HashMap<Integer, node_info> nodes = new HashMap<Integer, node_info>();
	private int amountOfEdges = 0;
	private int modeCount = 0;

	@Override
	public node_info getNode(int key) {
		if(!nodes.containsKey(key)) return null;
		return nodes.get(key);
	}

	@Override
	public boolean hasEdge(int node1, int node2) {
		if(node1 == node2)return false;
		if(!nodes.containsKey(node1) || !nodes.containsKey(node2))return false;
		return ((NodeInfo)getNode(node1)).hasNi(node2);
	}

	@Override
	public double getEdge(int node1, int node2) {
		if(!hasEdge(node1,node2))return -1;
		return ((NodeInfo) getNode(node1)).getNi().get(node2);
	}
	
	@Override
	public void addNode(int key) {
		if(nodes.containsKey(key))return;
		nodes.put(key, new NodeInfo(key));
		modeCount++;
	}

	@Override
	public void connect(int node1, int node2, double w) {
		if(node1 == node2)return;
		if(!nodes.containsKey(node1) || !nodes.containsKey(node2))return;
		if(w < 0)return;
		
		if(!hasEdge(node1,node2))amountOfEdges++;
		modeCount++;
		((NodeInfo)getNode(node1)).addNi(getNode(node2), w);
		((NodeInfo)getNode(node2)).addNi(getNode(node1), w);
	}

	@Override
	public Collection<node_info> getV() {
		return (Collection<node_info>) new HashMap<Integer, node_info>(nodes).values();
	}

	@Override
	public Collection<node_info> getV(int node_id) {
		if(!nodes.containsKey(node_id)) return null;

		Collection<node_info> collectionV = new HashSet<node_info>();

		for (Integer key : ((NodeInfo)getNode(node_id)).getNi().keySet()) 
		    collectionV.add(getNode(key));
			
		return collectionV;
	}

	public node_info removeNode(int key) {
		if(!nodes.containsKey(key)) return null;
		
		Iterator<node_info> iterator = getV(key).iterator();
		 
		while (iterator.hasNext()) {
			node_info node = iterator.next();
		    if(nodes.containsKey(node.getKey())) 
		    {
		    	((NodeInfo)node).removeNode(getNode(key));
			    amountOfEdges--;
			    modeCount++;
		   }
		}
		NodeInfo data = (NodeInfo)getNode(key);
		data.getNi().clear();
		
		nodes.remove(key);
		modeCount++;
		return data;
	}

	@Override
	public void removeEdge(int node1, int node2) {
		if(node1 == node2)return;
		if(!nodes.containsKey(node1) || !nodes.containsKey(node2))return;
		if(!hasEdge(node1, node2))return;
		
		((NodeInfo)getNode(node1)).removeNode(getNode(node2));
		((NodeInfo)getNode(node2)).removeNode(getNode(node1));
		amountOfEdges--;
		modeCount++;
	}

	@Override
	public int nodeSize() {
		return nodes.size();
	}

	@Override
	public int edgeSize() {
		return this.amountOfEdges;
	}

	@Override
	public int getMC() {
		return this.modeCount;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(!(other instanceof WGraph_DS))return false;
		
		WGraph_DS g2 = (WGraph_DS)other;
		if(g2.nodeSize() != this.nodeSize())return false;
		if(g2.edgeSize() != this.edgeSize())return false;
		
		for(int i = 0; i < g2.nodeSize(); i++)
			for(int j = 0; j < g2.nodeSize(); j++)
				if(g2.getEdge(i, j) != this.getEdge(i, j))
					return false;
		
		return true;
	}
}


