package procyk.industries.mpbot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

/*
 * The purpose of this class is to acquire a list of commands stored on a text file and
 * check if that command is executed. if it is, know how to respond to the command.
 */
public class Commands {
	public static HashMap<String, String> commands;
	private static String[] restrictedCommands;
	
	static
	{
		commands = new HashMap<String, String>();
		restrictedCommands = new String[]{"!add","!delete","!edit","!commands",
				"!recent","!test","!startTimer","!stopTimer","!quote"};
		load(StaticVars.CommandsDirectoy, commands);
	}

	private static void load(String fileName, HashMap<String, String> map) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));

			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			br.close();
			String everything = sb.toString();

			String[] delimitedFile = everything.split("\n");
			for (int i = 0; i < delimitedFile.length; i++) {
				String[] delimitedLine = delimitedFile[i].split("=");
				if (delimitedLine.length == 1) {
					return; // done loading
				}
				System.out.println("Command=" + delimitedLine[0] + " Message="	+ delimitedLine[1]);
				map.put(delimitedLine[0], delimitedLine[1]);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * addCommand takes a message string (command+message) in variable command
	 * and writes to Commands.txt writing !command=message
	 * 
	 */
	public static boolean addCommand(String command) {
		
			String[] splitString = command.split(" ");
			String commandName;
			StringBuilder sbCommandMessage = new StringBuilder();
			if (splitString.length > 1) {
				//split the message into command & delim & message
				for (int i = 1; i < splitString.length; i++) {
					sbCommandMessage.append(splitString[i] + " ");
				}
				// we have an appropriate string command
				commandName = splitString[0];
				String commandMessage = sbCommandMessage.toString();
				if (commandMessage.length() > 500) {
					// command message way to long...
					return false;
				}
				File file = new File(StaticVars.CommandsDirectoy);
				FileWriter fw;
				try {
					fw = new FileWriter(file.getAbsoluteFile(), true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(commandName + "=" + commandMessage);
					bw.newLine();
					bw.close();
					commands.put(commandName, commandMessage);

				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println(command + " has been added");
				return true;
			} else {
				// message split string not greater than 1; does not fit criteria
				return false;
			}
	}

	/*
	 * Search for the commmand name. if the command name is the same
	 * as what's contained in the string 'command' variable then return true
	 */
	public static boolean commandExists(String command) {
		return commands.get(command)!=null;
	}
	public static boolean Delete(String command) {
		//parse through the txt file of commands. find the command that the string matches
		//when we find the matching string delete the entire line (try to only delete the one line and not rewrite the whole thing)
		//return a response saying the string was deleted.
		
		//create a temp file that writes the desired content. then renames the file to Commands.txt
		String fileName = StaticVars.CommandsDirectoy;
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			
			//read the entire file and skip the line that contains the command
			System.out.println("Appending file to string builder");
			String line=null;
			StringBuilder sbFile = new StringBuilder();
			while ((line = br.readLine()) != null){
				if(line.contains(command)){
					continue;
				}
				sbFile.append(line);
				sbFile.append(System.lineSeparator());
			}
			br.close();
			//write the contents of sbFile to the temp file
			System.out.println("Removing "+command+" from hashmap");
			commands.remove(command);
			FileWriter fw = new FileWriter("lib/Commands.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(sbFile.toString());
			bw.close();
			fw.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("File could not be found when reading from Commands.txt");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static String listCommands(){
		StringBuilder sbCommands = new StringBuilder();
		for(String command: commands.keySet()){
			sbCommands.append(command+", ");
		}
		return sbCommands.toString();
	}
	public static String listRestrictedCommands(){
		StringBuilder sbCommands = new StringBuilder();
		for(String command: restrictedCommands){
			sbCommands.append(command+", ");
		}
		return sbCommands.toString();
	}
	public static boolean isRestricted(String command)
	{
		for(String s: restrictedCommands)
		{
			if(command.equals(s))return true;
		}
		return false;
	}
	public static void Update(String command, String message) {
		Commands.Delete(command);
		Commands.addCommand(message);
	}
	public static String Execute(String command) {		
		return commands.get(command);
	}

}
