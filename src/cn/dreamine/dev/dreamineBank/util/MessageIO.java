package cn.dreamine.dev.dreamineBank.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.libs.jline.internal.InputStreamReader;
import org.bukkit.plugin.java.JavaPlugin;

import cn.dreamine.dev.dreamineBank.DreamineBank;

public class MessageIO {

	private DreamineBank plugin;


	public MessageIO(DreamineBank plugin){
		this.plugin = plugin;
		//Add default help messages.
		helps.add("========DreaminePoints Help========");
		helps.add("Check your current Exp-Points: /dmpvp me");
		helps.add("Look another player's Exp-Points: /dmpvp look <player name>");
		helps.add("You can use /dmp instead of /dmpvp");
		try {
			this.readMessages();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			this.saveDefaultMessageConfig();
			e.printStackTrace();
		}
	}

	private FileConfiguration messageConfig = null;
	private File messageConfigFile = null;

	private String
			messagePrefix = "&c[DreaminePoints]",
			unknownCommand = "Unknown Command!",
			showMyBal = "Your currently balance: ",
			winnerMessage1 = "You beat player: ",
			winnerMessage2 = ", you have get Exp-Points: ",
			loserMessage1 = "You are killed by: ",
			loserMessage2 = ", you lose Exp-Points: ",
			pvpBroadcast1 = "Player: ",
			pvpBroadcast2 = " has just killed: ",
			noSwapPointsWin = "This guy is lack of EXP-Points, you only get: ",
			noSwapPointsLose = "You are lack of Exp-Points, all points will be taken.",
			playerNotFound = "Player not found!",
			lookOthersExp1 = "Player ",
			lookOthersExp2 = "'s Exp-Points: ",
			helpLook = "Use /dmpvp look <player name>, to look a player's Exp-Points.",
			unknownAdminCommand = "Unknown Command!",
			noPermission = "§c§lYou don't have permission to do this!",
			reloadSuccess = "Reload complete.";


	private List<String>
			helps = new ArrayList<String>(),
			adminHelps = new ArrayList<String>();


	public void checkFile(){

	}

	public void readMessages() throws FileNotFoundException, UnsupportedEncodingException{
		this.setMessagePrefix(this.getMessageConfig().getString("message-prefix"));
		this.setUnknownCommand(this.getMessageConfig().getString("unknown-command"));
		this.setShowMyBal(this.getMessageConfig().getString("show-my-bal"));

		this.setWinnerMessage1(this.getMessageConfig().getString("winner-message-1"));
		this.setWinnerMessage2(this.getMessageConfig().getString("winner-message-2"));

		this.setLoserMessage1(this.getMessageConfig().getString("loser-message-1"));
		this.setLoserMessage2(this.getMessageConfig().getString("loser-message-2"));

		this.setPvpBroadcast1(this.getMessageConfig().getString("PVP-broadcast-1"));
		this.setPvpBroadcast2(this.getMessageConfig().getString("PVP-broadcast-2"));

		this.setNoSwapPointsWin(this.getMessageConfig().getString("no-swap-points-win"));
		this.setNoSwapPointsLose(this.getMessageConfig().getString("no-swap-points-lose"));

		this.setPlayerNotFound(this.getMessageConfig().getString("player-not-found"));

		this.setLookOthersExp1(this.getMessageConfig().getString("look-others-exp-1"));
		this.setLookOthersExp2(this.getMessageConfig().getString("look-others-exp-2"));

		this.setHelpLook(this.getMessageConfig().getString("help-look"));
		this.setUnknownAdminCommand(this.getMessageConfig().getString("unknown-admin-command"));
		this.setNoPermission(this.getMessageConfig().getString("no-permission"));

		this.setHelps(this.getMessageConfig().getStringList("help"));
		this.setAdminHelps(this.getMessageConfig().getStringList("admin-helps"));

	}

	public static FileConfiguration load(JavaPlugin plugin, String fileName){
		File file = new File(plugin.getDataFolder(), fileName);
		if(!file.exists()){
			try{
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return YamlConfiguration.loadConfiguration(file);
	}

	public void reloadMessageConfig() throws FileNotFoundException, UnsupportedEncodingException {
		if (messageConfigFile == null) {
			messageConfigFile = new File(plugin.getDataFolder(), "message.yml");
		}
		messageConfig = YamlConfiguration.loadConfiguration(messageConfigFile);

		// Look for defaults in the jar
		//Reader defConfigStream = null;
		/*InputStream in = new FileInputStream("message.yml");*/
		//defConfigStream = new InputStreamReader(in);
		Reader defConfigStream = new InputStreamReader(plugin.getResource("message.yml"), "UTF8");
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
			messageConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getMessageConfig() throws FileNotFoundException, UnsupportedEncodingException {
		if (messageConfig == null) {
			reloadMessageConfig();
		}
		return messageConfig;
	}

	public void saveMessageConfig() {
		if (messageConfig == null || messageConfigFile == null) {
			return;
		}
		try {
			getMessageConfig().save(messageConfigFile);
		} catch (IOException ex) {
			plugin.getLogger().log(Level.SEVERE, "Could not save config to " + messageConfigFile, ex);
		}
	}

	public void saveDefaultMessageConfig() {
		if (messageConfigFile == null) {
			messageConfigFile = new File(plugin.getDataFolder(), "message.yml");
		}
		if (!messageConfigFile.exists()) {
			plugin.saveResource("message.yml", false);
		}
	}

	public String getMessagePrefix() {
		return messagePrefix;
	}

	public void setMessagePrefix(String messagePrefix) {
		this.messagePrefix = messagePrefix;
	}

	public String getUnknownCommand() {
		return messagePrefix + unknownCommand;
	}

	public void setUnknownCommand(String unknownCommand) {
		this.unknownCommand = unknownCommand;
	}

	public String getWinnerMessage1() {
		return messagePrefix + winnerMessage1;
	}

	public void setWinnerMessage1(String winnerMessage1) {
		this.winnerMessage1 = winnerMessage1;
	}

	public String getWinnerMessage2() {
		return winnerMessage2;
	}

	public void setWinnerMessage2(String winnerMessage2) {
		this.winnerMessage2 = winnerMessage2;
	}

	public String getLoserMessage1() {
		return messagePrefix + loserMessage1;
	}

	public void setLoserMessage1(String losermessage1) {
		this.loserMessage1 = losermessage1;
	}

	public String getLoserMessage2() {
		return loserMessage2;
	}

	public void setLoserMessage2(String loserMessage2) {
		this.loserMessage2 = loserMessage2;
	}

	public String getPvpBroadcast1() {
		return messagePrefix + pvpBroadcast1;
	}

	public void setPvpBroadcast1(String pvpBroadcast1) {
		this.pvpBroadcast1 = pvpBroadcast1;
	}

	public String getPvpBroadcast2() {
		return pvpBroadcast2;
	}

	public void setPvpBroadcast2(String pvpBroadcast2) {
		this.pvpBroadcast2 = pvpBroadcast2;
	}

	public String getPlayerNotFound() {
		return messagePrefix + playerNotFound;
	}

	public void setPlayerNotFound(String playerNotFound) {
		this.playerNotFound = playerNotFound;
	}

	public String getLookOthersExp1() {
		return messagePrefix + lookOthersExp1;
	}

	public void setLookOthersExp1(String lookOthersExp1) {
		this.lookOthersExp1 = lookOthersExp1;
	}

	public String getHelpLook() {
		return messagePrefix + helpLook;
	}

	public void setHelpLook(String helpLook) {
		this.helpLook = helpLook;
	}

	public List<String> getHelps() {
		return helps;
	}

	public void setHelps(List<String> list) {
		this.helps = list;
	}

	public String getLookOthersExp2() {
		return lookOthersExp2;
	}

	public void setLookOthersExp2(String lookOthersExp2) {
		this.lookOthersExp2 = lookOthersExp2;
	}

	public String getNoSwapPointsWin() {
		return messagePrefix + noSwapPointsWin;
	}

	public void setNoSwapPointsWin(String noSwapPointsWin) {
		this.noSwapPointsWin = noSwapPointsWin;
	}

	public String getNoSwapPointsLose() {
		return messagePrefix + noSwapPointsLose;
	}

	public void setNoSwapPointsLose(String noSwapPointsLose) {
		this.noSwapPointsLose = noSwapPointsLose;
	}

	public String getUnknownAdminCommand() {
		return messagePrefix + unknownAdminCommand;
	}

	public void setUnknownAdminCommand(String unknownAdminCommand) {
		this.unknownAdminCommand = unknownAdminCommand;
	}

	public String getNoPermission() {
		return noPermission;
	}

	public void setNoPermission(String noPermission) {
		this.noPermission = noPermission;
	}

	public List<String> getAdminHelps() {
		return adminHelps;
	}

	public void setAdminHelps(List<String> adminhelps) {
		this.adminHelps = adminhelps;
	}

	public String getReloadSuccess() {
		return messagePrefix +  reloadSuccess;
	}

	public void setReloadSuccess(String reloadSuccess) {
		this.reloadSuccess = reloadSuccess;
	}

	public String getShowMyBal() {
		return showMyBal;
	}

	public void setShowMyBal(String showMyBal) {
		this.showMyBal = showMyBal;
	}


}
