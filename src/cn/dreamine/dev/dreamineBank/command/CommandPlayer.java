package cn.dreamine.dev.dreamineBank.command;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import cn.dreamine.dev.dreamineBank.DreamineBank;
import cn.dreamine.dev.dreamineBank.util.MessageIO;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class CommandPlayer implements CommandExecutor{

	private DreamineBank plugin;
	private MessageIO messages;
	private Economy eco;

	public CommandPlayer(DreamineBank plugin, MessageIO messages){
		this.plugin = plugin;
		this.messages = messages;
		RegisteredServiceProvider<Economy> provider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (provider != null) {
            eco = provider.getProvider();
        } else {
            throw new RuntimeException("Vault Error: No EconomyProvider found");
        }
	}

	public CommandPlayer(){}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		String player = sender.getName();
		if(cmd.getName().equalsIgnoreCase("dbank")){
			if(args.length < 1){
				sender.sendMessage(messages.getUnknownCommand());
				return true;
			}
			if(args[0].equalsIgnoreCase("me")){
				if(sender instanceof Player){
					try{
						sender.sendMessage(messages.getShowMyBal() + plugin.getPlayerBalance(player));
					} catch(NullPointerException e){
						e.printStackTrace();
					}

				}else{
					sender.sendMessage("You must be a Player to excute this command!");
				}
				return true;

			}

			if(args[0].equalsIgnoreCase("take")){
				if(sender instanceof Player){
					if(args.length < 2){
						sender.sendMessage(messages.getUnknownCommand());
						return true;
					}
					if (args[1].matches("[0-9]+")){
						int bal = plugin.getPlayerBalance(player);
						int totake = Integer.parseInt(args[1]);
						if(bal>=totake){
							EconomyResponse rsp = eco.depositPlayer(player, totake);
							if(rsp.transactionSuccess()){
								int newbal = bal - totake;
								plugin.setPlayerBalance(player, newbal);
								sender.sendMessage("已成功取款 " + totake + " 金币。");
							} else {
								sender.sendMessage("取款失败，未知错误！");
							}
						} else {
							sender.sendMessage("你的存款不足！");
							return true;
						}
					} else {
						sender.sendMessage(messages.getUnknownCommand());
						return true;
					}
					try{
						sender.sendMessage(messages.getShowMyBal() + plugin.getPlayerBalance(player));
					} catch(NullPointerException e){
						e.printStackTrace();
					}

				}else{
					sender.sendMessage("You must be a Player to excute this command!");
				}
				return true;
			}

			if(args[0].equalsIgnoreCase("save")){
				if(sender instanceof Player){
					if(args.length < 2){
						sender.sendMessage(messages.getUnknownCommand());
						return true;
					}
					if (args[1].matches("[0-9]+")){
						int tosave = Integer.parseInt(args[1]);
						EconomyResponse rsp = eco.withdrawPlayer(player, tosave);
						if(rsp.transactionSuccess()){
							int newbal = plugin.getPlayerBalance(player) + tosave;
							plugin.setPlayerBalance(player, newbal);
							sender.sendMessage("已成功存款 " + tosave + " 金币。");
						} else {
							sender.sendMessage("你的现金不足！");
							return true;
						}


					} else {
						sender.sendMessage(messages.getUnknownCommand());
						return true;
					}
					try{
						sender.sendMessage(messages.getShowMyBal() + plugin.getPlayerBalance(player));
					} catch(NullPointerException e){
						e.printStackTrace();
					}
				} else {
					sender.sendMessage("You must be a Player to excute this command!");
				}
				return true;
			}

			/*else if(args[0].equalsIgnoreCase("ex")){
				if(sender instanceof Player){
					if(args.length<2) {
						sender.sendMessage(messages.getUnknownCommand());
					} else {
						String reward = args[1];
						try {
							if(plugin.getStorageIO().getStorageConfig()
									.getConfigurationSection("rewards")
									.get(reward) != null) {
							int points = plugin.getStorageIO().getStorageConfig()
									.getConfigurationSection("rewards")
									.getConfigurationSection(reward)
									.getInt("points");
							if(plugin.getPlayerPoints(player) < points - 1) {
								sender.sendMessage("You don't have so many points!");
							} else {
								plugin.minusPlayerPoints(player, points);
								List<String> commands = new ArrayList<String>();
								sender.sendMessage("你的D点现在是：" + plugin.getPlayerPoints(player));
								commands = plugin.getStorageIO().getStorageConfig()
										  .getConfigurationSection("rewards")
										  .getConfigurationSection(reward)
										  .getStringList("commands");
								for(String command: commands) {
									command = command.replaceAll("%player%", player);
									plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
								}
							}


							}
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}else{
					sender.sendMessage("You must be a Player to excute this command!");
				}
				return true;

			}else if(args[0].equalsIgnoreCase("exs")){
				if(sender instanceof Player){
					if(args.length<2) {
						sender.sendMessage(messages.getUnknownCommand());
					} else {
						String reward = args[1];
						try {
							if(plugin.getStorageIO().getStorageConfig()
									.getConfigurationSection("rewards")
									.get(reward) != null) {
								if(!plugin.getStorageIO().getStorageConfig()
										  .getConfigurationSection("rewards")
										  .getConfigurationSection(reward).getBoolean("limit")){
									sender.sendMessage("非限量品!");
									return true;
								}
								if(plugin.getStorageIO().getStorageConfig()
										  .getConfigurationSection("rewards")
										  .getConfigurationSection(reward).getBoolean("global")){
									player = "__global__";
								}
							int points = plugin.getStorageIO().getStorageConfig()
									.getConfigurationSection("rewards")
									.getConfigurationSection(reward)
									.getInt("points");
							if(plugin.getRewardPlayerPoints(player,reward) >= points) {
								sender.sendMessage("已经达到限制!");
								List<String> commands = new ArrayList<String>();
								commands = plugin.getStorageIO().getStorageConfig()
										  .getConfigurationSection("rewards")
										  .getConfigurationSection(reward)
										  .getStringList("commandlimit");
								for(String command: commands) {
									command = command.replaceAll("%player%", sender.getName());
									plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
								}
							} else {

								plugin.addRewardPlayerPoints(player, reward);
								List<String> commands = new ArrayList<String>();
								sender.sendMessage("已使用次数：" + plugin.getRewardPlayerPoints(player,reward));
								commands = plugin.getStorageIO().getStorageConfig()
										  .getConfigurationSection("rewards")
										  .getConfigurationSection(reward)
										  .getStringList("commands");
								for(String command: commands) {
									command = command.replaceAll("%player%", sender.getName());
									plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
								}
							}


							}
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				}else{
					sender.sendMessage("You must be a Player to excute this command!");
				}
				return true;

			}*/
			else if (args[0].equalsIgnoreCase("help")){
				for(int i = 0;i < messages.getHelps().size() ; i++){
					sender.sendMessage(messages.getMessagePrefix() + messages.getHelps().get(i));
				}
				return true;

			} else {
				sender.sendMessage(messages.getUnknownCommand());
				return true;
			}
		}
		return false;

	}



}
