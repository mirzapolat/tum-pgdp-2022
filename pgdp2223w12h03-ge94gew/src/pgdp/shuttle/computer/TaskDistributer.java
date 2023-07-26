package pgdp.shuttle.computer;

import java.util.List;

import pgdp.shuttle.tasks.ShuttleTask;
import pgdp.shuttle.tasks.TaskGenerator;

/**
 * This class generates tasks using the provided TaskGenerator until it reaches tasksToGenerate or it's shut down,
 * and sends them to each ShuttleProcessors.
 */
public class TaskDistributer extends Thread implements ShuttleComputerComponent {
	private final long tasksToGenerate;
	private final List<ShuttleProcessor> processors;
	private final TaskGenerator generator;

	private long currentTaskCount;

	private boolean doWork;

	public TaskDistributer(long tasksToGenerate, List<ShuttleProcessor> processors, TaskGenerator generator) {
		this.tasksToGenerate = tasksToGenerate;
		this.processors = processors;
		this.generator = generator;
		currentTaskCount = 0;
		doWork = true;
	}

	@Override
	public void run() {
		try {
			System.out.println("TaskDistributer starting to generate tasks.");

			while (currentTaskCount != tasksToGenerate && doWork) {
				ShuttleTask<?, ?> task = generator.generateTask();

				for (ShuttleProcessor prz : processors) {
					prz.addTask(task);
				}

				currentTaskCount++;
			}

			System.out.println("TaskDistributer finished generating " + currentTaskCount + "/" + tasksToGenerate + " tasks. Shutting down.");
		}

		catch (InterruptedException e) {
			System.out.println("TaskDistributer was interrupted after " + currentTaskCount + " tasks!");
		}
	}

	@Override
	public void shutDown() {
		doWork = false;
	}
}
