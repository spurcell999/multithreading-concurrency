package creatingThreads.helloWorld;

public class RealHelloWorldMultithreaded
{
	public static class Greeter extends Thread
	{
		private String country;
		
		public Greeter(String country)
		{
			super(country + " thread"); // call the thread constructor to name the thread
			this.country = country;
		}

		public void run()
		{
			System.out.println("Hello " + country + "!");
		}
	}

	public static void main(String[] args)
	{
		String countries[] = { "France", "India", "China", "USA", "Germany" };

		for (String country : countries)
		{
			new Greeter(country).start();
		}

		// java 8
		// Arrays.stream(countries).forEach(country -> System.out.println("hello" + country + "!"));
	}
}