package pgdp.ds;

import java.util.ArrayList;
import java.util.Arrays;

public class SparseGraph implements Graph {

	SimpleSet[] myNodes;

	public SparseGraph(int nodes) {
		if (nodes < 0) myNodes = new SimpleSet[0];
		else myNodes = new SimpleSet[nodes];

		for (int i = 0; i < nodes; i++) myNodes[i] = new SimpleSet();
	}

	@Override
	public int getNumberOfNodes() {
		return myNodes.length;
	}

	@Override
	public void addEdge(int from, int to) {
		if (from >= myNodes.length || to >= myNodes.length || from < 0 || to < 0) return;
		if (myNodes[from] == null) return;
		myNodes[from].add(to);
	}

	@Override
	public boolean isAdj(int from, int to) {
		if (myNodes[from] == null) return false;
		return myNodes[from].contains(to);
	}

	@Override
	public int[] getAdj(int id) {
		if (myNodes[id] == null) return null;
		return myNodes[id].toArray();
	}
}
