package bot.capstonebot.core;

import java.awt.Color;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import com.google.gdata.util.ServiceException;

import bot.capstonebot.constants.Key;
import bot.capstonebot.constants.Ref;
import bot.capstonebot.db.Event;
import bot.capstonebot.util.Music;
import bot.capstonebot.util.Spreadsheet;
import bot.capstonebot.util.SpreadsheetTimer;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import net.dv8tion.jda.core.requests.RestAction;

public class App extends ListenerAdapter
{
	
	static JDA jda;
	static ArrayList<Event> events;
    public static void main( String[] args ) throws LoginException, IOException, ServiceException
    {
        jda = new JDABuilder(AccountType.BOT).setToken(Key.TOKEN).build();
        jda.addEventListener(new App());
        jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, Ref.prefix + "help"));
        Spreadsheet.updateLocalSheet();
        events = Spreadsheet.scanSheet();
        SpreadsheetTimer spreadsheetTimer = new SpreadsheetTimer(60000L);
        Music.setJDA(jda);
        Music.configureMusic();
    }
    
    public static void updateEvents() {
    	Spreadsheet.updateLocalSheet();
    	events = Spreadsheet.scanSheet();
    	//System.out.println("Update");
    }


    
    @Override
    public void onMessageReceived(MessageReceivedEvent evt) {
    	//Objects
    	Message objMsg = evt.getMessage();
    	MessageChannel objMsgCh = evt.getTextChannel();
    	User objUser = evt.getAuthor();
    	Member objMember = evt.getMember();
    	Guild objGuild = evt.getGuild();
    	
    	String raw = objMsg.getContentRaw();
    	if(!raw.startsWith(Ref.prefix) && (!raw.contains("r/"))) {
    		return;
    	}
    	String command = "";
    	String input = "";
    	try{
    		command = raw.substring(Ref.prefix.length(), raw.indexOf(" ")).trim();
    		input = raw.substring(command.length() + Ref.prefix.length() +1).trim();
    	}catch (Exception e) {
    		try {
    			command = raw.substring(Ref.prefix.length()).trim();
    		}catch(Exception exc){
    			return;
    		}	  		
    	}
    	
    	if(command.equalsIgnoreCase("help")) {
    		objMsgCh.sendMessage(Ref.helpMessage).queue();
    	}else if(command.equalsIgnoreCase("invite")) {
    		objMsgCh.sendMessage("https://discord.gg/dmtBXeU").queue();
    	}else if(command.equalsIgnoreCase("events") || command.equalsIgnoreCase("event") || command.equalsIgnoreCase("evt")) {
    		EmbedBuilder title = new EmbedBuilder();
    		title.setTitle("Upcoming Events, Homework, or Tests");
    		
    		title.setColor(Ref.AP_GREEN);
    		String description = "`" + events.size() + " events in total.`\n\n";
    		Map<String, Integer> eventSummary = getEventSummary();
    		for(String type : getEventSummary().keySet()) {
    			if(eventSummary.get(type) > 1) {
    				description += "`" + eventSummary.get(type) + " " + type + "s coming up.`\n";
    			}else {
    				description += "`" + eventSummary.get(type) + " " + type + " coming up.`\n";
    			}
    			
    		}
    		title.setDescription(description);
    		objMsgCh.sendMessage(title.build()).queue();
    		
    		for(Event e: events) {
    		    objMsgCh.sendMessage(e.getEmbed()).queue();
    		}
    	}else if(command.equalsIgnoreCase("join")) {
    		if(objMember.getVoiceState().inVoiceChannel()) {
    			Music.joinChannel(objMember.getVoiceState().getChannel());
    		}else {
    			objMsgCh.sendMessage(objUser.getAsMention() + " You must be in a voice channel to use this command!").queue();
    		}
    		
    	}else if(command.equalsIgnoreCase("leave")) {
    		Music.exitChannel();
    	}else if(command.equalsIgnoreCase("play")) {
    		Music.play(input);
    	}else if(command.equalsIgnoreCase("update")) {
    		Spreadsheet.updateLocalSheet();
    		events = Spreadsheet.scanSheet();
    		objMsgCh.sendMessage("Events updated.").queue();
    	}
    	
    }
    
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
    	
    }
    
    public Map<String, Integer> getEventSummary() {
    	ArrayList<String> eventTypes = new ArrayList<>();
    	for(Event e : events) {
    		if(!eventTypes.contains(e.getType())) {
    			eventTypes.add(e.getType());
    		}
    	}
    	Map<String, Integer> typeSummary = new HashMap<String,Integer>();
    	for(String et : eventTypes) {
    		int count = 0;
    		for(Event e : events) {
    			if(e.getType().equals(et)) {
    				count++;
    			}
    		}
    		typeSummary.put(et, count);
    	}
    	return typeSummary;
    }
}
