package signalling.restaurant.firstVersion;

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
		// TODO: Wait until requests received or home-time

		currentRequest = BarRestaurantSimulation.requests.poll();

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

		System.out.println(name + " is waiting for a customer request.");
		
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

				// TODO: Let customer know
				
				System.out.println(name + " is waiting for a customer request.");
			}
		}

		System.out.println(name + " is going home.");
	}
}
