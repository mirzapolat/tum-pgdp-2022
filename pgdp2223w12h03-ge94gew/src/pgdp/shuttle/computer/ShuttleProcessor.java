package pgdp.shuttle.computer;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import pgdp.shuttle.tasks.ShuttleTask;

/**
 * This class evaluates tasks and then passes them to the TaskChecker until it is shut down.
 * Tasks in priorityTaskQueue are preferred over Tasks in taskQueue.
 */
public class ShuttleProcessor extends Thread implements ShuttleComputerComponent {
	private final LinkedBlockingQueue<ShuttleTask<?, ?>> taskQueue;
	private final LinkedBlockingQueue<ShuttleTask<?, ?>> priorityTaskQueue;
	private final TaskChecker checker;

	private boolean doWork;

	private static final Object taskAddingLock = new Object();

	public ShuttleProcessor(TaskChecker checker) {
		taskQueue = new LinkedBlockingQueue<>();
		priorityTaskQueue = new LinkedBlockingQueue<>();
		this.checker = checker;
		doWork = true;
	}

	public void addTask(ShuttleTask<?, ?> task) throws InterruptedException {
		taskQueue.put(task);
		synchronized (this) {
			notify();
		}
	}

	public void addPriorityTask(ShuttleTask<?, ?> task) throws InterruptedException {
		priorityTaskQueue.put(task);
		synchronized (this) {
			notify();
		}
	}

	@Override
	public void run() {
		try {
			while (doWork) {
				if (taskQueue.isEmpty() && priorityTaskQueue.isEmpty()) {
					synchronized (this) {
						wait();
					}
				} else if (!priorityTaskQueue.isEmpty()) {
					ShuttleTask<?, ?> task = priorityTaskQueue.poll();
					task.evaluate();
					checker.addTask(task);
				} else {
					ShuttleTask<?, ?> task = taskQueue.poll();
					task.evaluate();
					checker.addTask(task);
				}
			}
		}

		catch (InterruptedException e) {
			System.out.println("ShuttleProcessor was interrupted. Shutting down.");
			return;
		}

		System.out.println("ShuttleProcessor shutting down.");
	}

	@Override
	public void shutDown() {
		doWork = false;
		synchronized (this) {
			notify();
		}
	}
}
