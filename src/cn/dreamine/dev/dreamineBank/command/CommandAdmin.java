package cn.dreamine.dev.dreamineBank.command;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import cn.dreamine.dev.dreamineBank.DreamineBank;
import cn.dreamine.dev.dreamineBank.util.MessageIO;

public class CommandAdmin implements CommandExecutor {

	private DreamineBank plugin;
	private MessageIO messages;

	public CommandAdmin(DreamineBank plugin, MessageIO messages){
		this.plugin = plugin;
		this.messages = messages;
	}

	public CommandAdmin(){}


	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(cmd.getName().equalsIgnoreCase("dba")){
			if(sender.isOp()){

				if(args.length < 1 ){
					sender.sendMessage(messages.getUnknownAdminCommand());
					return true;
				}

				if(args[0].equalsIgnoreCase("reload")){
					plugin.reload();
					sender.sendMessage(messages.getReloadSuccess());
					return true;

				}else if(args[0].equalsIgnoreCase("help")){
					showAdminHelp(sender);
					return true;
				}

			}else{
				sender.sendMessage(messages.getNoPermission());
				return true;
			}
		}
		return false;
	}

	public void showAdminHelp(CommandSender sender){
		for(int i = 0;i < messages.getAdminHelps().size() ; i++){
			sender.sendMessage(messages.getMessagePrefix() + messages.getAdminHelps().get(i));
		}
	}

}
