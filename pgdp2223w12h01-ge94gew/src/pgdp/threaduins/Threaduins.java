package pgdp.threaduins;

import java.io.PrintStream;


//
// WORKAHOLIC CLASS -------------------------------------
//

class WorkaholicThread extends Thread {

	PrintStream s;
	boolean inter = false;

	public WorkaholicThread(PrintStream s) {
		this.s = s;
	}

	@Override
	public void run() {
		while (!inter)
			s.println(Threaduins.WORKAHOLIC_WORKING_MSG);
		s.println(Threaduins.WORKAHOLIC_STOP_MSG);
	}

	@Override
	public void interrupt() {
		inter = true;
	}
}

//
// PROCRASTINATOR CLASS --------------------
//

class ProcrastinatingThread extends Thread {
	PrintStream s;

	public ProcrastinatingThread(PrintStream s) {
		this.s = s;
	}

	@Override
	public void run() {
		s.println(Threaduins.PROCRASTINATOR_PROCRASTINATING_MSG);

		synchronized (this) {
			try {
				wait();
			} catch (Exception e) {
			}
		}

		s.println(Threaduins.LUCKY_PROCRASTINATOR_WORKING_MSG);
	}
}

public final class Threaduins {

	static final String WORKAHOLIC_WORKING_MSG = "Leave me alone! I'm working!";
	static final String WORKAHOLIC_STOP_MSG = "No! Why would you interrupt me?!?";

	static final String STOP_MSG = "Waiting for Signal!";
	static final String STOPPED_MSG = "HeHe :D";

	static final String PROCRASTINATOR_PROCRASTINATING_MSG = "Ahhhh, I will do this later ...";
	static final String LUCKY_PROCRASTINATOR_WORKING_MSG = "OH SHIT! The deadline is coming! I'll have to start right now.";

	private static Signal signal = new ConsoleSignal();

	protected Threaduins() {
		throw new UnsupportedOperationException();
	}

	/**
	 * This is a method you can use to set a custom Signal during testing.
	 * 
	 * @param s custom Signal implementation.
	 */
	protected static void setSignal(Signal s) {
		signal = s;
	}

	/**
	 * Returns a workaholic penguin that keeps working unless someone forcefully
	 * stops him.
	 * 
	 * @param s PrintStream the penguin uses to communicate.
	 * @return Thread of a workaholic penguin.
	 */
	public static Thread getWorkaholic(PrintStream s) { return new WorkaholicThread(s); }

	/**
	 * Method that starts a workaholic penguin. Once the specified signal is given
	 * the penguin is forced to stop working.
	 * 
	 * @param workaholic Thread of a workaholic penguin.
	 * @throws InterruptedException The Exception
	 */
	public static void stopWorkaholic(Thread workaholic) {
		workaholic.start();
		System.out.println(Threaduins.STOP_MSG);
		signal.await();
		workaholic.interrupt();
			try {
				workaholic.join();
				System.out.println(Threaduins.STOPPED_MSG);
			} catch (InterruptedException e) {
			}

	}

	/**
	 * Returns a procrastinating penguin that won't do anything until someone sends
	 * a reminder for the nearing deadline of the PGdP exercise.
	 * 
	 * @param s PrintStream the penguin uses to communicate.
	 * @return Thread of a procrastinating penguin.
	 */
	public static Thread getLuckyProcrastinator(PrintStream s) { return new ProcrastinatingThread(s); }

	/**
	 * Method that starts a workaholic penguin. Once the specified signal is given
	 * the penguin will be reminded of the deadline.
	 * 
	 * @param procrastinator Thread of a procrastinating penguin.
	 */
	public static void stopProcrastinator(Thread procrastinator) {
		try {
			procrastinator.start();
			System.out.println(Threaduins.STOP_MSG);
			signal.await();

			synchronized (procrastinator) {
				procrastinator.notify();
			}

			procrastinator.join();
			System.out.println(Threaduins.STOPPED_MSG);
		}
		catch (InterruptedException e) { }
	}

	public static void main(String... args) {
		// set a custom Signal
		// in this case we use another ConsoleSignal
		Threaduins.setSignal(new ConsoleSignal());

		// workaholic example
		//final Thread workaholic = getWorkaholic(System.out);
		//stopWorkaholic(workaholic);

		// procrastinator example
		final Thread luckyProc = getLuckyProcrastinator(System.out);
		stopProcrastinator(luckyProc);
	}

}
