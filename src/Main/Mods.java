package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Mods {
	public static String[] Mods;
	
	public Mods(){
		init();
	}
	
	public static boolean isMod(String mod){
		for(int i=0;i<Mods.length;i++){
			if(Mods[i].equalsIgnoreCase(mod))return true;
		}
		return false;
	}
	private void init(){
		 try{
				BufferedReader br = new BufferedReader(new FileReader(StaticVars.ModsDirectory)); 
		        StringBuilder sb = new StringBuilder();
		        String line = br.readLine();

		        while (line != null) {
		            sb.append(line);
		            sb.append(System.lineSeparator());
		            line = br.readLine();
		        }
		        br.close();
		        String everything = sb.toString();
		        
		        String[] delimitedFile = everything.split(", ");
		        Mods = new String[delimitedFile.length];
		        for(int i=0;i<delimitedFile.length;i++){
		        	//System.out.println(delimitedFile[i]);
		        	Mods[i]=delimitedFile[i];
		        }
		    } catch (IOException e) {
				e.printStackTrace();
			}
	}
}
