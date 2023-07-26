package pgdp.shuttle.computer;

import java.util.concurrent.LinkedBlockingQueue;

import pgdp.shuttle.tasks.ShuttleTask;

/**
 *	This class outputs Task results until it's shut down.
 */
public class ShuttleOutput extends Thread implements ShuttleComputerComponent {
	private final LinkedBlockingQueue<ShuttleTask<?, ?>> taskQueue;
	private boolean doWork;

	public ShuttleOutput() {
		doWork = true;
		taskQueue = new LinkedBlockingQueue<>();
	}

	public void addTask(ShuttleTask<?, ?> task) throws InterruptedException {
		taskQueue.put(task);
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
					System.out.println("Result: " + task.getResult().toString());
				}
			}
		}

		catch (InterruptedException e) {
			System.out.println("ShuttleOutput was interrupted. Shutting down.");
			return;
		}

		System.out.println("ShuttleOutput shutting down.");
	}

	@Override
	public void shutDown() {
		doWork = false;
		synchronized (this) {
			notify();
		}
	}
}
