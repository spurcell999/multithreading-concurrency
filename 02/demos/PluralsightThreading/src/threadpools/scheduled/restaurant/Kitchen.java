package threadpools.scheduled.restaurant;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Kitchen
{
	private ScheduledExecutorService dumbWaiter;
	
	private static final int delayMs = 15 * BarRestaurantSimulation.TIME_SCALER;
	
	public void open() {
		dumbWaiter = Executors.newSingleThreadScheduledExecutor();
		
		System.out.println("Kitchen is open");
	}
	
	public void requestMeal(Runnable mealNotifier) {
		System.out.println("Meal request sent to the kitchen");
		
		dumbWaiter.schedule(mealNotifier, delayMs, TimeUnit.MILLISECONDS);
	}
	
	public void close() {
		dumbWaiter.shutdownNow();
		
		System.out.println("Kitchen is closed");
	}
}
