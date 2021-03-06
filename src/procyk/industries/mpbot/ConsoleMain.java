package procyk.industries.mpbot;

import java.io.IOException;

import org.jibble.pircbot.IrcException;
import org.jibble.pircbot.NickAlreadyInUseException;

public class ConsoleMain{

	public static void main(String[] args){
		if(args.length==8)
		{
			String ircServer=args[0];
			Integer port = Integer.parseInt(args[1]);
			String oath=args[2];
			
			
			StaticVars.CommandsDirectoy=args[3];
			StaticVars.ModsDirectory=args[4];
			StaticVars.QuotesDirectory=args[5];
			String channel = args[6];
			String botName = args[7];
			
			System.out.println("Running with set vars:");
			System.out.println("IRCServer="+ircServer);
			System.out.println("PORT="+port);
			System.out.println("OAUTH="+oath);
			System.out.println("CommandsFile="+StaticVars.CommandsDirectoy);
			System.out.println("ModsFile="+StaticVars.ModsDirectory);
			System.out.println("QuotesFile="+StaticVars.QuotesDirectory);
			System.out.println("Channel="+channel);
			System.out.println("BotName="+botName);
			
			MPBOT mpBot = new MPBOT(botName);
			mpBot.setVerbose(true);
			try
			{
				mpBot.connect(ircServer, port,oath);
			}
			catch (NickAlreadyInUseException e)
			{
				e.printStackTrace();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			catch (IrcException e)
			{
				e.printStackTrace();
			}
			mpBot.joinChannel(channel);
		}
		else
		{
			System.out.println("Wrong Number of Arguments ("+args.length+") Should be 8");
			System.out.println("Here's what was passed...");
			for(String s: args)
			{
				System.out.println(s);
			}
		}
	}
}