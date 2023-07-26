package pgdp.shuttle.computer;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import pgdp.shuttle.tasks.ShuttleTask;

/**
 * This class checks, whether a Task was evaluated successful or not and 
 * either passes them to the ShuttleOutput or returns them to the ShuttleProcessors
 * until it is shutDown.
 */
public class TaskChecker extends Thread implements ShuttleComputerComponent {
	private final LinkedBlockingQueue<ShuttleTask<?, ?>> taskQueue;
	private final List<ShuttleProcessor> processors;
	private final ShuttleOutput output;

	private boolean doWork;

	public TaskChecker(List<ShuttleProcessor> processors, ShuttleOutput output) {
		taskQueue = new LinkedBlockingQueue<>();
		this.processors = processors;
		this.output = output;
		doWork = true;
	}

	public void addTask(ShuttleTask<?, ?> task) throws InterruptedException {
		taskQueue.add(task);
		synchronized (this) {
			notify();
		}
	}

	@Override
	public void run() {
		try {
			while (doWork) {
				if (taskQueue.isEmpty()) {
					synchronized (this) {
						wait();
					}
				}
				else {
					ShuttleTask<?, ?> task = taskQueue.poll();
					if (!task.wasCompleted()) {
						if (task.computationSuccessfull()) {
							output.addTask(task);
						}
						else if (task.readyToCheck()) {
							for (ShuttleProcessor prz : processors) {
								prz.addPriorityTask(task);
							}
						}
					}
				}
			}
		}

		catch (InterruptedException e) {
			System.out.println("TaskChecker was interrupted. Shutting down.");
			return;
		}

		System.out.println("TaskChecker shutting down.");
	}

	@Override
	public void shutDown() {
		doWork = false;
		synchronized (this) {
			notify();
		}
	}
}
