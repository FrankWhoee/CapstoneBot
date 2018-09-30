package bot.capstonebot.constants;

import java.awt.Color;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

import bot.capstonebot.core.App;

public class Ref {
	
	public static final String prefix = "!";
	
	public static final Color AP_BLUE = new Color(0x28ABE3); 
	public static final Color AP_GREEN = new Color(0x95C84F); 
	
	public static final String LOGO_URL = "https://raw.githubusercontent.com/VikingsDev/VikingsDev.github.io/master/assets/vikingsdev.jpg";
	public static final String WEBSITE_URL = "https://vikingsdev.github.io";
	public static final String spreadsheetId = "1WMpzigZYbpnznO5X7_pAjYZVbA7ruv-QhB5Tx0_wyyg";
	public static String jarPath = ""; 
	public static File CapstoneBotDB = new File("CapstoneBot/events.json");
	public static File CREDENTIALS = new File("CapstoneBot/credentials.json");
	public static File VikingBotDBParent  = new File(".");
	
	public static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public static final ArrayList<Long> adminIds = new ArrayList<>(Arrays.asList(194857448673247235L));

	//Used when running project as a jar file.
	static{
		try {
			CapstoneBotDB = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "CapstoneBot/events.json");
			VikingBotDBParent = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "CapstoneBot");
			CREDENTIALS = new File(App.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "CapstoneBot/credentials.json");
		}catch(Exception e) {
			System.out.println("Error getting JAR file path.");
		}
	}
	
	public static final String helpMessage = 
				   "```CAPSTONEBOT MANUAL"  
			+"\n"
			+ "\n" + prefix + "events - Returns a list of events/tests/homework coming up."
			+ "\n" + prefix + "role @role - Assigns @role to you. [CURRENTLY UNAVAILABLE]"
			+ "\n"
			+ "\n" + "FUN:"
			+ "\n" + prefix + "join - Bot joins the voice channel that you are currently in."
			+ "\n" + prefix + "leave - Bot leaves voice channel."
			+ "\n" + prefix + "play URL - Bot plays the youtube/soundcloud URL provided."
			+ ""
			
			+"```";
}
