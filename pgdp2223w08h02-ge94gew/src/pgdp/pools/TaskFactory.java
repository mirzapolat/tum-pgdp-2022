package pgdp.pools;

public class TaskFactory<T, R> {

	private final TaskPool<T, R> pool;

	public TaskFactory() {
		pool = new TaskPool<>();
	}

	public Task<T, R> create(T input, TaskFunction<T, R> function) {
		if (pool.getByValue(input, function) == null) return pool.insert(new Task<>(input, function));
		return pool.getByValue(input, function);
	}

	public Task<T, R> intern(Task<T, R> task) {
		return pool.insert(task);
	}

	public static void main(String[] args) {
		TaskFactory<Integer, Integer> tf = new TaskFactory<>();
		TaskFunction<Integer, Integer> f = new TaskFunction<>(FunctionLib.SQUARE);
		Task<Integer, Integer> t1 = tf.create(5, f);
		Task<Integer, Integer> t2 = new Task<>(5, f);
		System.out.println(t1 == tf.create(5, f)); // true
		System.out.println(t1 == tf.intern(t2)); //true
	}

	public TaskPool<T, R> getPool() {
		return this.pool;
	}
}
