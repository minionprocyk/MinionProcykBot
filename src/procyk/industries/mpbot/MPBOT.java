package procyk.industries.mpbot;


import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

import org.jibble.pircbot.PircBot;

public class MPBOT extends PircBot{
	/**
	 * Message timer that constantly hits the custom sendMessage. This will accelerate 
	 * any messages held in the queue for whatever reason without waiting for an event to
	 * rise in the chat to alleviate the queue.
	 */
	private Timer internalMessageTimer=null;
	
	/**
	 * A custom timer implemented at runtime to send messages in the chat
	 */
	private Timer t=null;
	public MPBOT(String name){
		this.setName(name);	
		internalMessageTimer = new Timer("messagetimer");
		internalMessageTimer.scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				finalizeMessage();
			}
		}, 2000L, 2000L);
	}
	public void onMessage(String channel, String sender, String login, String hostname, String message){
		//begin message manipulation to identify the message
		String[] messageArray = message.split(" ");
		//check if the sender is a mod
		boolean isMod = Mods.isMod(sender);
		
		if(isMod){	//mod only commands
			if(message.startsWith("!add") && message.substring(5,6).equalsIgnoreCase("!")){
				//message.substring(5) gets the ! symbol
				//i think this message is a command. lets add it
				String command = messageArray[1];
				
				//  If the command does not exist. try to add it. otherwise....dont 
				if(Commands.commandExists(command)==false && Commands.isRestricted(command)==false){
					System.out.println("Preparing to add "+command);
					if(Commands.addCommand(message.substring(5))){
						sendMessage(channel, sender,command+" has been added.");
					}else{
						sendMessage(channel, sender,command+" could not be added. Formatting is !add !command message. "+
					"!add will also not allow already existing commands ("+Commands.listRestrictedCommands()+")");
					}
				}
			}else if(message.startsWith(".ban") && message.substring(5).startsWith("@")){
				/*
				 * .ban command
				 * ex. '.ban @minionprocyk'
				 * sendMessage(/ban minionprocyk)
				 */
				sendMessage(channel,sender,"/ban "+message.substring(6, message.length()-1));
			}else if(message.startsWith("!edit") && message.substring(6,7).equalsIgnoreCase("!")){
				/*
				 * !edit command
				 * ex.'!edit !minion this is an edit'
				 * if the command !minion exists then Commands.Update(baseCommand,Command+Message)
				 */
				String command = messageArray[1];
				if(Commands.commandExists(command)){
					Commands.Update(command, message.substring(6));
					sendMessage(channel, sender, command+" has been edited");
				}
			}else if(message.startsWith("!delete") && message.substring(8,9).equalsIgnoreCase("!")){
				/*
				 * !delete command
				 * ex. '!delete !minion'
				 * if !minion exists in Commands.txt, create a new text file
				 * without !minion and remove it from the hashmap
				 */
				String command = messageArray[1];
				if(Commands.commandExists(command)){
					Commands.Delete(command);
					sendMessage(channel, sender, command+" has been deleted");
				}
			}else if( messageArray.length==3
					&& messageArray[0].equalsIgnoreCase("!startTimer") 
					&& messageArray[1].startsWith("!")
					&& Commands.commandExists(messageArray[1])
					&& messageArray[2].matches("[0-9]+")){
				if(t==null)t = new Timer();
				long delay = Long.parseLong(messageArray[2]);
				t.scheduleAtFixedRate(new TimerTask()
				{
					
					@Override
					public void run()
					{
						sendMessage(channel, sender, Commands.Execute(messageArray[1]));
					}
				}, delay,delay);
				sendMessage(channel, sender, "Timer started");
			}else if(message.equalsIgnoreCase("!stopTimer") || message.equalsIgnoreCase("!stopTimers")){
				if(t!=null)t.cancel();
				t=null;
				sendMessage(channel, sender, "All timers have been stopped");
			}

		}//end if is mod
		
		/*
		 * check if messages are commands for non-mods. if they aren't.
		 * if they are. Commands.Execute(command)
		 */
		if(Commands.commandExists(messageArray[0])){
			sendMessage(channel,sender,Commands.Execute(messageArray[0]));
		}else if(message.equals("!commands")){
			sendMessage(channel,sender,"Try using !quote. There's a large number of specific quotes like !quote24");
		}else if(message.equals("!quote")){
			//send a random quote from the list of commands
			String command = Commands.commands.keySet().stream().filter(cmd -> cmd.startsWith("!quote"))
						.collect(Collectors.collectingAndThen(Collectors.toList(), collected ->{
							Collections.shuffle(collected);
							return collected.stream();
						}))
						.findFirst()
						.get();
			sendMessage(channel,sender,Commands.Execute(command));
						
		}
		
		//check the que if messages were queued up (prevent bot from getting banned)
		finalizeMessage();
	}//end OnMessage
	public void onOp(String channel,String sourceNick,String sourceLogin,String sourceHostname,String recipient){
		sendMessage(channel, MessageHandler.newOp + "*"+recipient+"*");			
		
	}//end onOp
	public void sendMessage(String channel, String sender, String message){
		//preface the message to make it personable
		String preface="";
		//assume the name of the master is the channel name without the #
		if(sender.equals(channel.substring(1))){
			preface = MessageHandler.master;
		}else if(Mods.isMod(sender)){
			preface = MessageHandler.chief+sender+". ";
		}else{
			preface = MessageHandler.minion+sender+". ";
		}
		MessageHandler.getInstance().QueMessage(channel, sender,preface.concat(message));
	}//end sendMessage
	private void finalizeMessage()
	{
		Message m = MessageHandler.getInstance().getNextMessage();
		if(m!=null)sendMessage(m.getSender(), m.getMessage());
	}
}//end MPBot
