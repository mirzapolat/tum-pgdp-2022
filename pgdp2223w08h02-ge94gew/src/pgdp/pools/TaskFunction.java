package pgdp.pools;

import java.util.Objects;
import java.util.function.Function;

public class TaskFunction<T, R> {

	private final int ID;
	private final Function<T, R> function;

	private static int IDcounter;

	public TaskFunction(Function<T, R> function) {
		this.function = function;
		ID = IDcounter++;
	}

	public R apply(T input) {
		return function.apply(input);
	}

	@Override
	public int hashCode() {
		return Objects.hash(ID);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (obj.getClass() == TaskFunction.class) return ((TaskFunction<?, ?>) obj).ID == this.ID;
		else return false;
	}

	public static void main(String[] args) {
		TaskFunction<Integer, Integer> f1 = new TaskFunction<>(FunctionLib.SQUARE);
		TaskFunction<Integer, Integer> f2 = new TaskFunction<>(FunctionLib.SUM_OF_HALFS);
		TaskFunction<Integer, Integer> f3 = new TaskFunction<>(FunctionLib.SQUARE);
		System.out.println(f1.equals(f2)); // false
		System.out.println(f1.equals(f3)); // false
		System.out.println(f1.equals(f1)); // true
		System.out.println(f1.apply(2)); // 4
	}
}
