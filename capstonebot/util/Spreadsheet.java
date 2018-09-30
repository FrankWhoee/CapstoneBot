package bot.capstonebot.util;


import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import com.google.gson.*;

import bot.capstonebot.constants.Ref;
import bot.capstonebot.db.Event;

public class Spreadsheet {
	
	public static JsonArray objJson;
	public static ArrayList<Event> events = new ArrayList<>();
	
	public static void updateLocalSheet() {
		String json = ""; 
		int attempts = 25;
		for(int i = 1; i <= attempts; i++) {
			cloudExec("rm " + Ref.CapstoneBotDB.getPath());
			cloudExec("gsjson "+Ref.spreadsheetId+" >> " + Ref.CapstoneBotDB.getPath() + " -s " + Ref.CREDENTIALS.getPath());
			cloudExec("chmod +rw " + Ref.CapstoneBotDB.getPath() + "");
			json = cloudExec("cat " + Ref.CapstoneBotDB.getPath() + "");
			try {
				objJson = new JsonParser().parse(json).getAsJsonArray();
				break;
			}catch(Exception e) {
				System.err.println("Spreadsheet failed to download. Trying again... (Attempt " + i + ")");
			}
		}
		try {
			objJson = new JsonParser().parse(json).getAsJsonArray();
		}catch(Exception e) {
			System.err.println("Spreadsheet failed to download after " + attempts + " attempts.");
		}
		
	}

	public static ArrayList<Event> scanSheet() {
		ArrayList<Event> sps = new ArrayList<>();
		for(JsonElement element : objJson){
			
			JsonObject e = element.getAsJsonObject();
			
			String name = e.get("name").getAsString();
			String description = e.get("description").getAsString();
			String type = e.get("type").getAsString();
			String endDate;
			if(e.get("duedate").getAsString().equals("<never>")) {
				endDate = "2020/06/31 12:59:59";
			}else{
				endDate = e.get("duedate").getAsString().replaceAll("<", "").replaceAll(">", "");
			}
			String imageURL = e.get("imageurl").getAsString();
			Color colour = new Color(Integer.parseInt(e.get("colour").getAsString(),16));
			
			Event event = null;
			try {
				event = new Event(name,description, Ref.dateFormat.parse(endDate),imageURL, type,colour);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			sps.add(event);
		}
		return sps;
	}
	
	 public static String executeCommand(String command) {
	    	//Build command 
	    	Process process = null;
			try {
				process = new ProcessBuilder(new String[] {"bash", "-c", command})
				    .redirectErrorStream(true)
				    .directory(new File("."))
				    .start();
			} catch (IOException e1) {
			
				e1.printStackTrace();
			}

	        //Read output
	        StringBuilder out = new StringBuilder();
	        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        String line = null, previous = null;
	        
	        String output = "";
	        try {
				while ((line = br.readLine()) != null)
				    if (!line.equals(previous)) {
				        previous = line;
				        out.append(line).append('\n');
				        output += line + "\n";
				    }
			} catch (IOException e) {
				e.printStackTrace();
			}
	        return output;
	    }
	    
	    public static String cloudExec(String command) {
	    	//Build command 
	    	Process process = null;
			try {
				process = new ProcessBuilder(new String[] {"bash", "-c", command})
				    .redirectErrorStream(true)
				    .directory(new File("../"))
				    .start();
			} catch (IOException e1) {
				e1.printStackTrace();
			}

	        //Read output
	        StringBuilder out = new StringBuilder();
	        BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
	        String line = null, previous = null;
	        
	        String output = "";
	        try {
				while ((line = br.readLine()) != null)
				    if (!line.equals(previous)) {
				        previous = line;
				        out.append(line).append('\n');
				        output += line + "\n";
				    }
			} catch (IOException e) {
				e.printStackTrace();
			}
	        return output;
	    }
}
