package signalling.restaurant.waitNotifyVersion;

public class Waiter implements Runnable
{
	private final String name;
	private Request currentRequest;

	public Waiter(String name)
	{
		this.name = name;
	}

	private void waitForCustomerRequest()
	{
		synchronized (BarRestaurantSimulation.requests)
		{
			while (true)
			{
				currentRequest = BarRestaurantSimulation.requests.poll();

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

				try
				{
					BarRestaurantSimulation.requests.wait();
				}
				catch (InterruptedException e)
				{
					// Can ignore since closed will be set
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
		System.out.println(name + " serving food.");
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

				synchronized (currentRequest)
				{
					currentRequest.setRequestBeingHandled();
					currentRequest.notify();
				}
			}
		}

		System.out.println(name + " is going home.");
	}
}
