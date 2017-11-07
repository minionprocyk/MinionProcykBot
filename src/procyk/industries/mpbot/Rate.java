package procyk.industries.mpbot;

//this class was imported from a c# project. I don't wana hear it.
public class Rate
{
	 public int Requests;
     public int Seconds;

     public Rate(String s)
     {
         SetRateFromString(s);
     }
     public void SetRateFromString(String rate)
     {
    	 String [] sRate = rate.split(":");
         Requests = Integer.parseInt(sRate[0]);
         Seconds = Integer.parseInt(sRate[1]);
     }

}
