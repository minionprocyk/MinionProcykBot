package procyk.industries.mpbot;


import org.jibble.pircbot.PircBot;



public class MPBOT extends PircBot{
	public MPBOT(String name){
		this.setName(name);	
	}
	
	public void onMessage(String channel, String sender, String login, String hostname, String message){
		//que message into the message list
		Messages.AddPastReadMessages(sender, message);
		//begin message manipulation to identify the message
		String[] messageArray = message.split(" ");
			
		//check if the sender is a mod
		boolean isMod = Mods.isMod(sender);
		
		if(isMod){
			//mod only commands
			if(message.startsWith("!add") && message.substring(5,6).equalsIgnoreCase("!")){
				//message.substring(5) gets the ! symbol
				//i think this message is a command. lets add it
				String command = messageArray[1];
				
				//  If the command does not exist. try to add it. otherwise....dont 
				if(!Commands.commandExists(command) &&
					command != "!add" &&
					command != "!delete" &&
					command != "!edit" &&
					command != "!commands" &&
					command != "!recent" &&
					command != "!test" &&
					command != "!startTimer" &&
					command != "!stopTimer"){
					System.out.println("Preparing to add "+command);
					if(Commands.addCommand(message.substring(5))){
						sendMessage(channel, sender,command+" has been added.");
					}else{
						sendMessage(channel, sender,command+" could not be added. Formatting is !add !command message.");
						sendMessage(channel, sender, "!add will not allow the following commands: !add, !delete, !edit,"+
													 " !commands, !recent, !test, !startTimer");
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
			}

		}
		
		/*
		 * check if messages are commands for non-mods. if they aren't. Fuck'em
		 * if they are. Commands.Execute(command)
		 */
		if(Commands.commandExists(messageArray[0])){
			sendMessage(channel,sender,Commands.Execute(messageArray[0]));
		}else if(message.startsWith("!commands")){
			sendMessage(channel,sender,Commands.listCommands());
		}
		
		//check the que if messages were queued up (prevent bot from getting banned)
		for(Message m : Messages.getQueuedMessages()){
			sendMessage(channel, sender, m.getMessage());
		}
	}//end OnMessage
	public void onOp(String channel,String sourceNick,String sourceLogin,String sourceHostname,String recipient){
		
		sendMessage(channel, Messages.newOp + "*"+recipient+"*");			
		
	}//end onOp
	public void sendMessage(String channel, String sender, String message){
		//preface the message to make it personable
		String preface="";
		//assume the name of the master is the channel name without the #
		if(sender.equals(channel.substring(1))){
			preface = Messages.master;
		}else if(Mods.isMod(sender)){
			preface = Messages.chief+sender+". ";
		}else{
			preface = Messages.minion+sender+". ";
		}
		//manage messages as a que with a timeout.
		if(Messages.canSendMessage()){
			Messages.AddRecentSentMessages(sender, message);
			sendMessage(channel, preface+message);
		}else{
			Messages.QueMessage(sender,message);
			System.out.println("Could not send message.");
		}
	}//end sendMessage
}//end MPBot