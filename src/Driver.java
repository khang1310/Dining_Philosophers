import java.util.Random;
import java.util.concurrent.locks.*;

public class Driver {
	public static boolean stopFlag = false;
	public static String output = "";
	public static final int PHILOSOPHER_NUM = 5;
	public static final int MAX_THINK_EAT_SEC = 3;
	
	public static void main(String args[]) throws InterruptedException {
		int runTime = Integer.parseInt(args[0]);

		DiningPhilosophers diningPhilosophers = new DiningPhilosophers();

		Thread.sleep(runTime * 1000);

		stopFlag = true;
		diningPhilosophers.stopAll();
		
		System.out.println("\n\n Result:\n");
		System.out.println(output);
	}
}
