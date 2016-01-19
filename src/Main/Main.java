package Main;
public class Main{
	
	public static void main(String[] args) throws Exception{
		//Load the static class mods with a populated list of current mods
		new Mods();
		//Load the static class commands with a populated list of current commands
		new Commands();
		//load the static class messages with predefined messages and other stuff
		new Messages();
		
		MPBOT mpBot = new MPBOT();
		mpBot.setVerbose(true);
		mpBot.connect("irc.twitch.tv", 6667,"oauth:7oj2jf4xpqule0srjcmjk2twxdg5ub");
		mpBot.joinChannel("#minionprocyk");
		
		
	}

}


/*
 * This code tests that all mods are included from Mods.txt
for(int i=0;i<Mods.Mods.length;i++){
	System.out.println("Mod["+i+"] = "+Mods.Mods[i]);
}
*/