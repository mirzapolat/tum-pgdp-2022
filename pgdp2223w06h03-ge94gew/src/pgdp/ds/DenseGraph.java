package pgdp.ds;

public class DenseGraph implements Graph {

	boolean [][] theMatrix;

	public DenseGraph(int nodes) {
		theMatrix = new boolean[nodes][nodes];
	}

	@Override
	public int getNumberOfNodes() {
		return theMatrix.length;
	}

	@Override
	public void addEdge(int from, int to) {
		if (from >= theMatrix.length && to >= theMatrix.length) return;
		theMatrix[from][to] = true;
	}

	@Override
	public boolean isAdj(int from, int to) {
		if (from >= theMatrix.length && to >= theMatrix.length) return false;
		return theMatrix[from][to];
	}

	@Override
	public int[] getAdj(int id) {
		if (id >= theMatrix.length) return null;
		return getAdj(id, 0);
	}

	private int[] getAdj(int id, int current) {
		int[] currentState;
		if (theMatrix[id][current]) currentState = new int[] {current};
		else currentState = new int[] {};


		if (current == theMatrix.length - 1) return currentState;
		else return merge(currentState, getAdj(id, current +1));
	}

	private int[] merge(int[] a, int[] b) {
		int[] n = new int[a.length + b.length];
		for (int i = 0; i < a.length; i++) n[i] = a[i];
		for (int i = 0; i < b.length; i++) n[i + a.length] = b[i];
		return n;
	}
}
