package threadpools.scheduled.restaurant;

public class Waiter implements Runnable
{
	private final String name;
	private final Kitchen kitchen;
	private Request currentRequest;

	public Waiter(String name, Kitchen kitchen)
	{
		this.name = name;
		this.kitchen = kitchen;
	}

	private void waitForCustomerRequest()
	{
		System.out.println(name + " is waiting for a customer request.");

		currentRequest = null;

		while (true)
		{
			try
			{
				currentRequest = BarRestaurantSimulation.requests.take();
			}
			catch (InterruptedException e)
			{
				// We'll use the are we closed check to escape
			}
			// If we have a request we need not wait
			if (currentRequest != null)
			{
				break;
			}

			// If there are no customers in the bar
			if (BarRestaurantSimulation.numCustomersInBar.get() == 0)
			{
				// If we're closed we can exit.
				if (BarRestaurantSimulation.closed)
				{
					break;
				}
			}
		}

		if (currentRequest != null)
		{
			System.out.println(name + " has a request from "
					+ currentRequest.getCustomer().getName() + ": "
					+ currentRequest.getRequestType());
		}
	}

	private void seatCustomer()
	{
		System.out.println(name + " is seating a customer.");
	}

	private void takeOrder()
	{
		System.out.println(name + " is taking an order.");
	}

	private void serveCustomer()
	{
		System.out.println(name + " is waiting for the kitchen to make the meal.");
		
		Request request = currentRequest;
		
		kitchen.requestMeal(new Runnable() {
			public void run() {
				System.out.println(name + " is bringing the meal over.");
				
				request.setRequestBeingHandled();

				synchronized (request)
				{
					request.notify();
				}
			}
		});
		
		System.out.println(name + " will serve the customer as soon as the kitchen has made the food.");
	}

	private void getCheque()
	{
		System.out.println(name + " is getting the cheque.");
	}

	public void run()
	{
		System.out.println(name + " has shown up for work.");

		while (!BarRestaurantSimulation.closed
				|| BarRestaurantSimulation.numCustomersInBar.get() > 0)
		{
			waitForCustomerRequest();

			if (currentRequest != null)
			{
				switch (currentRequest.getRequestType())
				{
					case Request.SEATING_REQUEST:
						seatCustomer();
						break;
					case Request.ORDER_REQUEST:
						takeOrder();
						break;
					case Request.SERVE_REQUEST:
						serveCustomer();
						break;
					case Request.CHEQUE_REQUEST:
						getCheque();
						break;
				}

				if (currentRequest.getRequestType() != Request.SERVE_REQUEST) {
					currentRequest.setRequestBeingHandled();

					synchronized (currentRequest)
					{
						currentRequest.notify();
					}
				}
			}
		}

		System.out.println(name + " is going home.");
	}
}
