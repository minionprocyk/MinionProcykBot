package Main;

import java.util.ArrayList;
import java.util.Iterator;

public class Messages {
	public static String twitterAd = "Want to know when I'll be streaming and what games I'll be playing? Follow me at twitter.com/minionprocyk for more information!";
	public static String master = "Yes Master. ";
	public static String newOp = "Congragulations on becoming our newest Chief Minion";
	public static String chief = "I obey chief minion ";
	public static String minion = "I serve my fellow minion ";
	
	private static ArrayList<Message> pastReadMessages;
	private static ArrayList<Message> recentSentMessages;
	private static ArrayList<Message> queMessage;
	
	public Messages(){
		init();
	}
	private void init(){
		pastReadMessages = new ArrayList<Message>();
		recentSentMessages = new ArrayList<Message>();
		queMessage = new ArrayList<Message>();
	}
	public static void AddPastReadMessages(String sender, String message){
		//hold the last 30 messages sent
		//use fifo
		if(pastReadMessages.size()>=30){
			pastReadMessages.remove(0);
		}
		pastReadMessages.add(new Message(sender,message));
	}
	public static void AddRecentSentMessages(String sender, String message){
		recentSentMessages.add(new Message(sender, message));
	}
	public static boolean Contains(String message, ArrayList<String> messages){
		for(String msg: messages){
			if(msg.equals(message))return true;
		}
		return false;
	}
	public static boolean canSendMessage(){
		updateSentMessages();
		if(recentSentMessages.size()>30){
			return false;
		}else{
			return true;
		}
	}
	private static void updateSentMessages(){
		//write a routine that removes messages sent more than 30 seconds ago
		
		Iterator<Message> iMessage = recentSentMessages.iterator();
		while(iMessage.hasNext()){
			Message nextMessage = iMessage.next();
			
			if((int) (System.currentTimeMillis()-nextMessage.getTimeStamp())>((int) 30*1000)){
				//if current time in mili > message.time in mili 
				System.out.println("Removing message: "+nextMessage.getMessage());
				iMessage.remove();
			}
		}
		System.out.println("Messages updated.");
	}
	public static void QueMessage(String sender, String msg){
		System.out.println(sender+"'s message: "+msg+" has been queued");
		if(queMessage==null)queMessage = new ArrayList<Message>();
		
		queMessage.add(new Message(sender,msg));
	}
	public static ArrayList<Message> getQueuedMessages(){
		//eliminate the que and send its contents
		if(queMessage==null)queMessage = new ArrayList<Message>();
		ArrayList<Message> tempMessages = new ArrayList<Message>();
		tempMessages.addAll(queMessage);
		queMessage=null;
		return tempMessages;
	}
	
	//test methods
	public static Integer getNumRecentSent(){
		return recentSentMessages.size();
	}
			
}
