package signalling.restaurant.waitNotifyVersion;

class CustomerRequestHandler
{	
	private static void placeRequest(Request request)
	{
		synchronized (BarRestaurantSimulation.requests)
		{
			BarRestaurantSimulation.requests.offer(request);

			BarRestaurantSimulation.requests.notify();
		}
	}
	
	private static void waitForRequestToBeHandled(Request request)
	{
		synchronized (request)
		{
			while (!request.getRequestBeingHandled())
			{
				try
				{
					request.wait();
				}
				catch (InterruptedException e)
				{
					// Can ignore
				}
			}
		}
	}
	
	private static String getPreRequestMessage(String requestType)
	{
		switch (requestType) {
			case Request.SEATING_REQUEST:
				return " is requesting to be seated.";
			case Request.ORDER_REQUEST:
				return " is requesting to order.";
			case Request.SERVE_REQUEST:
				return " is waiting to be served the meal.";
			case Request.CHEQUE_REQUEST:
				return " is requesting the cheque.";
			default:
				return " is ... I'm not sure what they are requesting!";
		}
	}
	
	private static String getPostActionMessage(String requestType)
	{
		switch (requestType) {
			case Request.SEATING_REQUEST:
				return " has been seated.";
			case Request.ORDER_REQUEST:
				return " has ordered.";
			case Request.SERVE_REQUEST:
				return "'s food has arrived.";
			case Request.CHEQUE_REQUEST:
				return " has paid.";
			default:
				return " has ... I'm not sure what they have done!";
		}
	}

	
	public static void act(Customer customer, String requestType)
	{
		Request request = new Request(customer, requestType);

		System.out.println(customer.getName() + getPreRequestMessage(requestType));

		placeRequest(request);

		waitForRequestToBeHandled(request);

		System.out.println(customer.getName() + getPostActionMessage(requestType));
	}
}
