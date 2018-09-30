package bot.capstonebot.util;

import java.util.Timer;
import java.util.TimerTask;

import bot.capstonebot.core.App;

public class SpreadsheetTimer extends TimerTask{
	
	public SpreadsheetTimer(Long interval) {
		Timer spreadsheetTimer = new Timer();
		spreadsheetTimer.scheduleAtFixedRate(this,0 , interval);
	}
	
	@Override
	public void run() {
		App.updateEvents();
	}

}
