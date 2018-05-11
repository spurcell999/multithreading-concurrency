package liveness.fileAndNetworkDeadlock;

public class MyNetworkCon
{
	public void access()
	{
		System.out.println("Network is being accessed by " + Thread.currentThread().getName());
	}
}
