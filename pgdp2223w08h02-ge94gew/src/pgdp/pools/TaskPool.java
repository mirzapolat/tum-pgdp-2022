package pgdp.pools;

import java.util.ArrayList;

public class TaskPool<T, R> {

	private ArrayList<Task<T, R>> pool;

	protected TaskPool() {
		pool = new ArrayList<>();
	}

	public Task<T, R> insert(Task<T, R> task) {
		for (Task<T, R> trTask : pool) {
			if (task.equals(trTask)) return trTask;
		}

		pool.add(task);
		return task;
	}

	public Task<T, R> getByValue(T input, TaskFunction<T, R> function) {
		for (Task<T, R> trTask : pool) {
			if (trTask.equals(new Task<>(input, function))) return trTask;
		}

		return null;
	}

	public static void main(String[] args) {
		TaskFunction<Integer, Integer> f = new TaskFunction<>(FunctionLib.SQUARE);
		TaskPool<Integer, Integer> tp = new TaskPool<>();

		System.out.println(tp.getByValue(1, f)); // null

		Task<Integer, Integer> t1 = new Task<>(1, f);
		Task<Integer, Integer> t2 = new Task<>(1, f);
		System.out.println(t1 == tp.insert(t1)); // true
		System.out.println(t1 == tp.insert(t2)); // true
		System.out.println(t1 == tp.getByValue(1, f)); // true
	}
}
