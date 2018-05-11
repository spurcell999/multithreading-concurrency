package signalling.restaurant.blockingQueue;

import java.util.concurrent.ThreadLocalRandom;

public class Customer implements Runnable
{
	private final String name;

	public Customer(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return this.name;
	}

	private void arrive()
	{
		ThreadLocalRandom tlr = ThreadLocalRandom.current();

		// up to 60 minutes after closing time
		int timeBeforeArrival = Math.abs(tlr.nextInt())
				% (BarRestaurantSimulation.barOpeningTime + 60);

		try
		{
			Thread.sleep(timeBeforeArrival
					* BarRestaurantSimulation.TIME_SCALER);
		}
		catch (InterruptedException e)
		{
			Thread.currentThread().interrupt();
		}
	}

	public void waitToBeSeated()
	{
		CustomerRequestHandler.act(this, Request.SEATING_REQUEST);
	}

	private void orderMeal()
	{
		CustomerRequestHandler.act(this, Request.ORDER_REQUEST);
	}

	private void waitForMeal()
	{
		CustomerRequestHandler.act(this, Request.SERVE_REQUEST);
	}

	private void eatMeal()
	{
		System.out.println(name + " is eating.");

		ThreadLocalRandom tlr = ThreadLocalRandom.current();

		// 30 to 60 minutes
		int eatingTime = tlr.nextInt() % 30 + 30;

		long doneEatingTime = System.currentTimeMillis() + eatingTime
				* BarRestaurantSimulation.TIME_SCALER;

		long timeLeft;

		while ((timeLeft = doneEatingTime - System.currentTimeMillis()) > 0)
		{
			try
			{
				Thread.sleep(timeLeft);
			}
			catch (InterruptedException e)
			{
				// Can ignore, we're allowed to finish eating
			}
		}

		System.out.println(name + " has eaten.");

	}

	private void payCheque()
	{
		CustomerRequestHandler.act(this, Request.CHEQUE_REQUEST);
	}

	public void run()
	{
		arrive();

		BarRestaurantSimulation.numCustomersInBar.incrementAndGet();

		// Bar is closed?
		if (BarRestaurantSimulation.closed)
		{
			// Come earlier next time
			System.out.println(name
					+ " has arrived after closing time - too late!");

			BarRestaurantSimulation.numCustomersInBar.decrementAndGet();

			return;
		}

		System.out.println(name + " has arrived.");

		waitToBeSeated();
		orderMeal();
		waitForMeal();
		eatMeal();
		payCheque();

		System.out.println(name + " has left.");

		BarRestaurantSimulation.numCustomersInBar.decrementAndGet();
	}
}
