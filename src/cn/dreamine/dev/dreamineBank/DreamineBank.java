package cn.dreamine.dev.dreamineBank;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import cn.dreamine.dev.dreamineBank.command.CommandAdmin;
import cn.dreamine.dev.dreamineBank.command.CommandPlayer;
import cn.dreamine.dev.dreamineBank.listeners.BankListener;
import cn.dreamine.dev.dreamineBank.util.MessageIO;
import cn.dreamine.dev.dreamineBank.util.SQL;
import cn.dreamine.dev.dreamineBank.util.StorageIO;
import cn.dreamine.dev.dreamineBank.util.TransactionUtil;

public class DreamineBank extends JavaPlugin implements Listener {

	private StorageIO storageIO;
	private MessageIO messageIO;
	private int port;
    private String host, database, username, password;
    private SQL sql;

	public void onEnable(){
		storageIO = new StorageIO(this);
		messageIO = new MessageIO(this);
		Bukkit.getPluginManager().registerEvents(this, this);
		loadConfig();
		sql = new SQL(host, port, database, username, password);
		this.getServer().getPluginManager().registerEvents(new BankListener(this,messageIO), this);
		this.getCommand("dbank").setExecutor(new CommandPlayer(this,messageIO));
		this.getCommand("dbaa").setExecutor(new CommandAdmin(this,messageIO));
		this.saveDefaultConfig();
		messageIO.saveDefaultMessageConfig();
		TransactionUtil.setPlugin(this);
		//Reader.load(this,"storage.yml");
	}

	public void reload(){
		this.reloadConfig();
		this.loadConfig();
		try {
			storageIO.reloadStorageConfig();

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			storageIO.saveDefaultStorageConfig();
			//e.printStackTrace();
			this.getLogger().warning("Storage file not found! Recreating.");
		}
		try{
			messageIO.reloadMessageConfig();
			messageIO.readMessages();
		}catch(FileNotFoundException | UnsupportedEncodingException e) {
			messageIO.saveDefaultMessageConfig();
			this.getLogger().warning("Message file not found! Recreating.");
		}


	}


	public void onDisable(){
		storageIO.saveStorageConfig();
	}

	public void loadConfig(){
		host = this.getConfig().getString("host");
		port = Integer.parseInt(this.getConfig().getString("port"));
		database = this.getConfig().getString("database");
		username = this.getConfig().getString("username");
		password = this.getConfig().getString("password");

/*		try {
			storageIO.reloadStorageConfig();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}


	public void addPlayer(Player player){
		try {
			sql.addNewPlayerIfNotExists(player.getName());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setPlayerBalance(String playername, int points){
		try {
			sql.changeBalance(playername, points);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addPlayerBalance(String playername, int points){
		int pointsBefore;
		try {
			pointsBefore = sql.getPlayerBalance(playername);
			sql.changeBalance(playername, pointsBefore + points);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean minusPlayerBalance(String playername, int points){
		int pointsBefore;
		try {
			pointsBefore = sql.getPlayerBalance(playername);
			if(points > pointsBefore){
				return false;
			} else {
				sql.changeBalance(playername, pointsBefore - points);
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public void addPlayerOfflinein(String playername, int points){
		int pointsBefore;
		try {
			pointsBefore = sql.getPlayerOfflinein(playername);
			sql.changeOfflinein(playername, pointsBefore + points);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void clearPlayerOfflinein(String playername){
		try {
			sql.changeOfflinein(playername, 0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    public void clearPlayerOfflineout(String playername){
        try {
            sql.changeOfflineout(playername, 0);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

	public void addPlayerOfflineout(String playername, int points){
		int pointsBefore;
		try {
			pointsBefore = sql.getPlayerOfflineout(playername);
			sql.changeOfflineout(playername, pointsBefore + points);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getPlayerBalance(String playername){
		try {
			int points = sql.getPlayerBalance(playername);
			return points;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	public int getPlayerOfflinein(String playername){
		try {
			int points = sql.getPlayerOfflinein(playername);
			return points;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}

	public int getPlayerOfflineout(String playername){
		try {
			int points = sql.getPlayerOfflineout(playername);
			return points;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}


	public MessageIO getMessageIO() {
		return messageIO;
	}

	public StorageIO getStorageIO(){
		return storageIO;
	}

	public SQL getSQL(){
		return sql;
	}


	public void givePlayerMoney(Player player, int amount){
        Economy eco;
        RegisteredServiceProvider<Economy> provider = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (provider != null) {
            eco = provider.getProvider();
        } else {
            throw new RuntimeException("Vault Error: No EconomyProvider found");
        }

        player.sendMessage("在您离线期间收入金币： " + amount);
        EconomyResponse rsp = eco.depositPlayer(player, amount);
        if(rsp.transactionSuccess()){
            player.sendMessage("已将 " + amount + " 金币给您。");
            this.clearPlayerOfflinein(player.getName());
        }

    }

    public void takePlayerMoney(Player player, int amount){
        Economy eco;
        RegisteredServiceProvider<Economy> provider = this.getServer().getServicesManager().getRegistration(Economy.class);
        if (provider != null) {
            eco = provider.getProvider();
        } else {
            throw new RuntimeException("Vault Error: No EconomyProvider found");
        }

        player.sendMessage("在您离线期间花费金币： " + amount);

        EconomyResponse rsp = eco.withdrawPlayer(player, amount);
        if(rsp.transactionSuccess()){
            player.sendMessage("已将您的 " + amount + " 金币扣除。");
            this.clearPlayerOfflineout(player.getName());
        }
    }



}
