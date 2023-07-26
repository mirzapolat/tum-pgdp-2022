package pgdp.ds;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MultiStack {

	private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

	private final Stack stacks;

	public MultiStack() {
		stacks = new Stack(1);
	}

	public void push(int val) {
		try {
			lock.writeLock().lock();	// lock
			stacks.push(val);
		}
		finally {
			lock.writeLock().unlock();	// unlock
		}
	}

	public int pop() {
		try {
			lock.writeLock().lock();	// lock

			if (stacks.isEmpty()) {
				return Integer.MIN_VALUE;
			}
			return stacks.pop();
		}
		finally {
			lock.writeLock().unlock();	// unlock
		}
	}

	public int top() {
		try {
			lock.readLock().lock();			// lock read
			if (stacks.isEmpty()) {
				return Integer.MIN_VALUE;
			}
			return stacks.top();
		}
		finally {
			lock.readLock().unlock();		// unlock this read (top)
		}
	}

	public int size() {
		try {
			lock.readLock().lock();		// lock
			if (stacks.isEmpty()) {
				return 0;
			}
			return stacks.size();
		}
		finally {
			lock.readLock().unlock();		// unlock this read (size)
		}
	}

	public int search(int element) {
		try {
			lock.readLock().lock();		// lock
			if (stacks.isEmpty()) {
				return -1;
			}
			return stacks.search(element);
		}
		finally {
			lock.readLock().unlock();		// unlock this read (search)
		}
	}

	@Override
	public String toString() {
		return stacks.toString();
	}
}
