import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers implements DiningServer {
	enum State {
		THINKING, HUNGRY, EATING
	};

	private State[] state = new State[5];
	private Condition[] conditions = new Condition[Driver.PHILOSOPHER_NUM];
	private Lock lock = new ReentrantLock();
	private Philosopher[] philosophers= new Philosopher[Driver.PHILOSOPHER_NUM];

	public DiningPhilosophers() {
		for (int i = 0; i < Driver.PHILOSOPHER_NUM; i++) {
			conditions[i] = lock.newCondition();
		}
		
		for (int i = 0; i < Driver.PHILOSOPHER_NUM; i++) {
			state[i] = State.THINKING;

			Random r = new Random();
			double randomValue = 1 + (Driver.MAX_THINK_EAT_SEC - 1) * r.nextDouble();
			System.out.println("Philosopher " + (i + 1) + " is thinking");
			try {
				Thread.sleep((long) (randomValue * 1000));
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		for (int i = 0; i < Driver.PHILOSOPHER_NUM; i++) {
			philosophers[i] = new Philosopher(i, this);
		}
	}
	
	public void takeForks(int i) {
		lock.lock();

		state[i] = State.HUNGRY;
		System.out.println("Philosopher " + (i + 1) + " is hungry");
		// See if we can eat now
		signalEating(i);
		// If not, wait
		if (state[i] != State.EATING) {
			try {
				conditions[i].await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		lock.unlock();
	}

	@Override
	public void returnForks(int philosopherNumber) {
		lock.lock();

		state[philosopherNumber] = State.THINKING;
		System.out.println("Philosopher " + philosopherNumber + " is done eating");
		System.out.println("Philosopher " + philosopherNumber + " is thinking");
		// See if left and right neighbors need the forks
		signalEating((philosopherNumber + Driver.PHILOSOPHER_NUM - 1) % Driver.PHILOSOPHER_NUM);
		signalEating((philosopherNumber + 1) % Driver.PHILOSOPHER_NUM);

		lock.unlock();
	}

	private void signalEating(int philosopherNumber) {
		if ((state[(philosopherNumber + Driver.PHILOSOPHER_NUM - 1) % Driver.PHILOSOPHER_NUM] != State.EATING)
				&& (state[philosopherNumber] == State.HUNGRY)
				&& (state[(philosopherNumber + 1) % Driver.PHILOSOPHER_NUM] != State.EATING)) {
			state[philosopherNumber] = State.EATING;
			System.out.println("Philosopher " + philosopherNumber + " is eating");
			conditions[philosopherNumber].signal();
		}
	}

	public void stopAll() {
		for(int i = 0; i < Driver.PHILOSOPHER_NUM; i++) {
			philosophers[i].stop();
		}
	}
}
