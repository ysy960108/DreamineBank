package cn.dreamine.dev.dreamineBank.listeners;

import cn.dreamine.dev.dreamineBank.task.GiveMoneyTask;
import cn.dreamine.dev.dreamineBank.task.TakeMoneyTask;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import cn.dreamine.dev.dreamineBank.DreamineBank;
import cn.dreamine.dev.dreamineBank.util.MessageIO;
import cn.dreamine.dev.dreamineBank.util.TransactionUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.scheduler.BukkitTask;

public class BankListener implements Listener {

	private DreamineBank plugin;
	private MessageIO messages;
	//private Economy eco;

	public BankListener(DreamineBank plugin, MessageIO messages){
		this.plugin = plugin;
		this.messages = messages;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){

        Economy eco;

        RegisteredServiceProvider<Economy> provider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (provider != null) {
            eco = provider.getProvider();
        } else {
            throw new RuntimeException("Vault Error: No EconomyProvider found");
        }

		Player player = event.getPlayer();
		String playername = player.getName();
		plugin.addPlayer(player);
		if(plugin.getPlayerOfflinein(playername) != 0){
			int amount = plugin.getPlayerOfflinein(playername);
            BukkitTask task = new GiveMoneyTask(plugin,player,amount).runTaskLater(this.plugin, 100);
/*			player.sendMessage("在您离线期间收入金币： " + amount);

            EconomyResponse rsp = eco.depositPlayer(player, amount);
            if(rsp.transactionSuccess()){
                player.sendMessage("已将 " + amount + " 金币给您。");
                plugin.clearPlayerOfflinein(playername);
            }*/

/*
			if(TransactionUtil.deposit(player, amount)){

			}*/
		}

		if(plugin.getPlayerOfflineout(playername) != 0){
			int amount = plugin.getPlayerOfflineout(playername);
            BukkitTask task = new TakeMoneyTask(plugin,player,amount).runTaskLater(this.plugin, 100);
			/*player.sendMessage("在您离线期间花费金币： " + amount);

            EconomyResponse rsp = eco.withdrawPlayer(player, amount);
            if(rsp.transactionSuccess()){
                player.sendMessage("已将您的 " + amount + " 金币扣除。");
                plugin.clearPlayerOfflineout(playername);
            }*/

			/*if(TransactionUtil.withdraw(player, amount)){

			}*/
		}

	}





}
