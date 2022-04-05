import java.util.Random;

class Philosopher implements Runnable {
	private DiningPhilosophers diningPhilosophers;
	private int philosopherNumber;
	private int numberOfMeals;

	final int PHILOSOPHER_NUM = 5;
	final int MAX_THINK_EAT_SEC = 3;
	final int MAX_MEAL = 10;

	private boolean alreadyStopped = false;
	private Thread thread;

	public Philosopher(int philosopherNumber, DiningPhilosophers diningPhilosophers) {
		this.philosopherNumber = philosopherNumber;
		this.diningPhilosophers = diningPhilosophers;
		numberOfMeals = 0;

		thread = new Thread(this);
		thread.start();
	}

	private void eat() throws InterruptedException {
		Random r = new Random();
		double randomValue = 1 + (MAX_THINK_EAT_SEC - 1) * r.nextDouble();
		Thread.sleep((long) (randomValue * 1000));
	}

	
	@Override
	public void run() {
		while (true) {
			if ((numberOfMeals >= MAX_MEAL || Driver.stopFlag == true) && !alreadyStopped) {
				return;
			}

			diningPhilosophers.takeForks(philosopherNumber);

			try {
				eat();
				numberOfMeals++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			diningPhilosophers.returnForks(philosopherNumber);
		}
	}

	public void stop() {
		Driver.output += "Philosopher " + Integer.toString(philosopherNumber + 1) + " eats "
				+ Integer.toString(numberOfMeals) + " meals.\n";
		thread.stop();
		
		alreadyStopped = true;
	}
}