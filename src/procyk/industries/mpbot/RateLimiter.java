package procyk.industries.mpbot;

import java.util.Timer;
import java.util.TimerTask;

//this class was imported from a c# project. I don't wana hear it.
public class RateLimiter
{
	public Rate rateRequests;
	public Rate rateRequestsCount;
	public RateLimiter(String rateRequests)
	{
		this.rateRequests = new Rate(rateRequests);
		this.rateRequestsCount = new Rate("1:1");
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask()
		{
			
			@Override
			public void run()
			{
				if(rateRequestsCount.Requests>0)
				{
					rateRequestsCount.Requests-=1;
				}
				
			}
		}, 1000L, 1000L);
	}
	public void Count()
	{
		rateRequestsCount.Requests++;
	}
	public boolean warn()
	{
		if((rateRequests.Requests - rateRequestsCount.Requests)<=5)
		{
			return true;
		}else
		{
			return false;
		}
	}
}
