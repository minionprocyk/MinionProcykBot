package procyk.industries.mpbot;

import java.util.concurrent.ConcurrentLinkedDeque;

public class MessageHandler {
	private ConcurrentLinkedDeque<Message> queMessage;
	public static String master = "Yes Master. ";
	public static String newOp = "Congragulations on becoming our newest Chief Minion";
	public static String chief = "I obey chief minion ";
	public static String minion = "I serve my fellow minion ";
	private static MessageHandler handler;
	private RateLimiter rateLimiter;
	private MessageHandler()
	{
		rateLimiter = new RateLimiter("30:30");
		queMessage = new ConcurrentLinkedDeque<>();
	}
	public static MessageHandler getInstance()
	{
		if(handler==null)
		{
			handler = new MessageHandler();
		}
		return handler;
	}
	public Message getNextMessage()
	{
		if(queMessage.isEmpty()==false && rateLimiter.warn()==false)
		{
			rateLimiter.Count();
			return queMessage.poll();
		}else
		{
			return null;
		}
	}
	public void QueMessage(String channel, String sender, String msg){
		queMessage.offer(new Message(channel,sender,msg));
	}
	
	
}
