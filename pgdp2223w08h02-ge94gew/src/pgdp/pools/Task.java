package pgdp.pools;

public class Task<T, R> {
	private final T input;
	private R result;
	private final TaskFunction<T,R> taskFunction;

	protected Task(T input, TaskFunction<T, R> taskFunction) {
		this.taskFunction = taskFunction;
		this.input = input;
	}

	public R getResult() {
		if (this.result == null) this.result = taskFunction.apply(input);
		return this.result;
	}

	@Override
	public int hashCode() {
		return taskFunction.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj.getClass() == Task.class){
			Task<?, ?> myTask = (Task<?, ?>) obj;
			return myTask.taskFunction.equals(this.taskFunction) && myTask.input.equals(this.input);
		}

		return false;
	}

	public static void main(String[] args) {
		TaskFunction<Integer, Integer> f1 = new TaskFunction<>(FunctionLib.INC);
		TaskFunction<Integer, Integer> f2 = new TaskFunction<>(FunctionLib.INC);
		Task<Integer, Integer> t1 = new Task<>(1, f1);
		Task<Integer, Integer> t2 = new Task<>(1, f1);
		Task<Integer, Integer> t3 = new Task<>(1, f2);

		System.out.println(t1.equals(t2)); // true
		System.out.println(t1.equals(t3)); // false

		System.out.println(t1.getResult()); // 2
	}

	public T getInput() {
		return input;
	}

	public TaskFunction<T, R> getTaskFunction() {
		return this.taskFunction;
	}
}
