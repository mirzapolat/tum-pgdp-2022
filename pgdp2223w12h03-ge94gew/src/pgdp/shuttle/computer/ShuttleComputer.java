package pgdp.shuttle.computer;

import pgdp.shuttle.tasks.ErrorProneTaskGenerator;
import pgdp.shuttle.tasks.ErrorlessTaskGenerator;
import pgdp.shuttle.tasks.TaskGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * This class assembles all ShuttleComputerComponents together
 */
public class ShuttleComputer extends Thread {

	private final long tasksToGenerate;
	private final TaskGenerator generator;
	private final long sleepTime;

	public ShuttleComputer(long tasksToGenerate, TaskGenerator generator, long sleepTime) {
		this.tasksToGenerate = tasksToGenerate;
		this.generator = generator;
		this.sleepTime = sleepTime;
	}

	@Override
	public void run() {
		System.out.println("ShuttleComputer booting up.");

		ShuttleOutput output = new ShuttleOutput();
		List<ShuttleProcessor> processors = new ArrayList<>();
		TaskChecker checker = new TaskChecker(processors, output);
		processors.add(new ShuttleProcessor(checker));
		processors.add(new ShuttleProcessor(checker));
		processors.add(new ShuttleProcessor(checker));
		processors.add(new ShuttleProcessor(checker));
		TaskDistributer distributer = new TaskDistributer(tasksToGenerate, processors, generator);

		output.start();
		checker.start();
		for (ShuttleProcessor p : processors) p.start();
		distributer.start();

		try {
			sleep(sleepTime);
		} catch (InterruptedException e) {
			System.out.println("ShuttleComputer crashed (interrupted while waiting for TaskDistributer)!");
		}

		try {
			distributer.shutDown();
			distributer.join();

			for (ShuttleProcessor p : processors) p.shutDown();
			for (ShuttleProcessor p : processors) p.join();

			checker.shutDown();
			checker.join();

			output.shutDown();
			output.join();
		} catch (InterruptedException e) {
			System.out.println("ShuttleComputer crashed (interrupted while waiting for ShuttleComputerComponent)!");
		}

		System.out.println("ShuttleComputer shutting down.");
	}

	public static void main(String[] args) {
		boolean errorless = false;
		int tasks = 1000000;
		long sleepTime = 100;

		if (errorless) {
			// Random tasks with deterministic results
			ErrorlessTaskGenerator g = new ErrorlessTaskGenerator(42);
			ShuttleComputer sG = new ShuttleComputer(tasks, g, sleepTime);
			sG.start();
			try {
				sG.join();
			} catch (InterruptedException i) {
				i.printStackTrace();
			}
		} else {
			// Equal tasks with random result
			ErrorProneTaskGenerator e = new ErrorProneTaskGenerator(20);
			ShuttleComputer sE = new ShuttleComputer(tasks, e, sleepTime);
			sE.start();
			try {
				sE.join();
			} catch (InterruptedException i) {
				i.printStackTrace();
			}
			// task count output for ErrorProneTaskGenerator (may give a hint, if everything works as intended)
			System.out.println(e.getCount());
		}
	}
}
