package cn.dreamine.dev.dreamineBank.task;

import cn.dreamine.dev.dreamineBank.DreamineBank;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by ysy on 2019/1/28.
 */
public class GiveMoneyTask extends BukkitRunnable{

    private DreamineBank plugin;
    private Player player;
    private int money;

    public GiveMoneyTask(DreamineBank plugin, Player player, int money){
        this.plugin = plugin;
        this.player = player;
        this.money = money;
    }

    @Override
    public void run(){
        plugin.givePlayerMoney(player,money);
    }

}
