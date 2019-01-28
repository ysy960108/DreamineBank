package cn.dreamine.dev.dreamineBank.util;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import cn.dreamine.dev.dreamineBank.DreamineBank;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class TransactionUtil {
	private static DreamineBank plugin;
	private static Economy eco;

	private TransactionUtil(){
		RegisteredServiceProvider<Economy> provider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (provider != null) {
            eco = provider.getProvider();
        } else {
            throw new RuntimeException("Vault Error: No EconomyProvider found");
        }
	}

	public static void depositOffline(OfflinePlayer player, double amount){
		String playername = player.getName();
		plugin.addPlayerOfflinein(playername,(int) amount);
	}

	public static void withdrawOffline(OfflinePlayer player, double amount){
		String playername = player.getName();
		plugin.addPlayerOfflineout(playername,(int) amount);
	}

	public static boolean deposit(Player player, double amount){
		EconomyResponse rsp = eco.depositPlayer(player, amount);
		if(rsp.transactionSuccess()){
			return true;
		}
		return false;
	}

	public static boolean withdraw(Player player, double amount){
		EconomyResponse rsp = eco.withdrawPlayer(player, amount);
		if(rsp.transactionSuccess()){
			return true;
		}
		return false;
	}

	public static void setPlugin(DreamineBank dreaminebank){
		plugin = dreaminebank;
	}

}
