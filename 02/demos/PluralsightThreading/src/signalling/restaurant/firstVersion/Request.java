package signalling.restaurant.firstVersion;

public class Request
{
	public static final String SEATING_REQUEST = "SEAT";
	public static final String ORDER_REQUEST = "TAKEORDER";
	public static final String SERVE_REQUEST = "SERVE";
	public static final String CHEQUE_REQUEST = "GETCHEQUE";

	private final Customer customer;
	private final String requestType;

	public Request(Customer customer, String requestType)
	{
		this.customer = customer;
		this.requestType = requestType;
	}

	public Customer getCustomer()
	{
		return customer;
	}

	public String getRequestType()
	{
		return requestType;
	}
}
