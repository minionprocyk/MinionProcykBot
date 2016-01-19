package Main;

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
	// build a hashmap of commands onLoad
	public static HashMap<String, String> commands;

	public Commands() {
		init();
	}

	private void init() {
		commands = new HashMap<String, String>();
		loadCommands();
	}

	private void loadCommands() {
		load(StaticVars.CommandsDirectoy, commands);
	}


	private void load(String fileName, HashMap<String, String> map) {
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
				File file = new File("lib/Commands.txt");
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
		String fileName = "lib/Commands.txt";
		
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
			//load the entire message into a normal string
			String everything = sb.toString();

			// split for windows platform only. '\r\n'
			// use '\n' if used on a Unix system
			String[] delimitedFile = everything.split("\n");

			for (int i = 0; i < delimitedFile.length; i++) {
				String[] delimitedCommand = delimitedFile[i].split("=");
				if (delimitedCommand[0].equals(command)) {
					// System.out.println(command+" already exists");
					return true;
				}
				// System.out.println(delimitedFile[i]);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(command+" does not exist");
		return false;
	}
	public static boolean Delete(String command) {
		//parse through the txt file of commands. find the command that the string matches
		//when we find the matching string delete the entire line (try to only delete the one line and not rewrite the whole thing)
		//return a response saying the string was deleted.
		
		
		//create a temp file that writes the desired content. then renames the file to Commands.txt
		try {
			BufferedReader br = new BufferedReader(new FileReader("lib/Commands.txt"));
			
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
		//	System.out.println("New text file to be written | "+sbFile.toString());
			br.close();
			//write the contents of sbFile to the temp file
			System.out.println("Removing "+command+" from hashmap");
			commands.remove(command);
			
			System.out.println("Writing new Commands file");
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

	public static void Update(String command, String message) {
		Commands.Delete(command);
		Commands.addCommand(message);
	}
	public static String Execute(String command) {		
		return commands.get(command);
	}

}
