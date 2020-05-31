package teamdjg.wildescape.worldborderCommands;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Difficulty;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import teamdjg.wildescape.main.Main;

public class WorldborderStartercommand implements CommandExecutor {
	
	Main mainclass;
	
	public WorldborderStartercommand(Main plugin)
	{
		this.mainclass = plugin;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {		
		
		if(sender instanceof Player)
		{
			if(!sender.hasPermission(mainclass.borderstartPermission))
			{
				sender.sendMessage(mainclass.pluginPrefix + ChatColor.DARK_RED + "ERROR: you don't have permission for this plugin.");
				return true;
			}
		}
		
		if(mainclass.WorldborderCenterCheck == false)
		{
			if (sender instanceof Player) 
			{
				mainclass.ClearChat((Player)sender);
				sender.sendMessage(mainclass.ChatLine());
				sender.sendMessage(mainclass.pluginPrefix + ChatColor.DARK_RED + "ERROR:" + ChatColor.GOLD + "You first need to use the  - /bordercenter - before you can start the game");
				sender.sendMessage(mainclass.ChatLine());
			}
			else
			{
				System.out.println(mainclass.pluginPrefix + ChatColor.DARK_RED + "ERROR:" + ChatColor.GOLD + "You first need to use the  - /bordercenter - before you can start the game");
			}
			return true;
		}
		
		World gameWorld = mainclass.getServer().getWorld(mainclass.WorldBorderWorldName);
		
		//start game :
		
		mainclass.GameRunning = true;
		
		//change difficulty
		gameWorld.setDifficulty(Difficulty.getByValue(mainclass.gameDifficulty));
		
		//set game time
		gameWorld.setTime(mainclass.gameStartTime);
		
		
		//start border clock
		mainclass.BorderTimer(gameWorld);
		mainclass._WorldborderMechanics.BorderResumeMoving();
		
		//start message
		mainclass.getServer().broadcastMessage(mainclass.pluginPrefix + ChatColor.GOLD + "The game has begon. The last team alive will win.");
		
		Random random = new Random();
		
		int minX = mainclass.WorldCenter.getBlockX() - (mainclass.WorldBorderMax/2);
		int minZ = mainclass.WorldCenter.getBlockZ() - (mainclass.WorldBorderMax/2);
		
		boolean tpCommandPlayer = true;
		
		if(args.length > 0)
		{
			if(args[0].equals("false"))
			{
				tpCommandPlayer = false;
			}
		}
		
		for(Player player : mainclass.getServer().getOnlinePlayers())
		{
			if(tpCommandPlayer == false || !(sender instanceof Player))
			{
				int X = minX + Math.round(mainclass.WorldBorderMax * random.nextFloat());
				int Z = minZ + Math.round(mainclass.WorldBorderMax * random.nextFloat());
				int Y = getTopBlock(X, Z, player.getWorld());
				
				player.getInventory().clear();
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1200, 50));
				player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 19200, 4));
				player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1200, 4));
				
				if(!(player.equals(sender)))
				{
					player.teleport(new Location(player.getWorld(),X,Y,Z));
				}	
			}
			else
			{
				int X = minX + Math.round(mainclass.WorldBorderMax * random.nextFloat());
				int Z = minZ + Math.round(mainclass.WorldBorderMax * random.nextFloat());
				int Y = getTopBlock(X, Z, player.getWorld());		
				player.teleport(new Location(player.getWorld(),X,Y,Z));
				
				player.getInventory().clear();
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1200, 50));
				player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 19200, 4));
				player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 1200, 4));
			}
			
		}
		
		//TODO clear inventory from all team members
		
		//TODO teleport teams

		//TODO change gamemode for the players
		
		//TODO maybe a starter kit for the players?
		
		return true;
		
	}
	
	public int getTopBlock(int X, int Z, World world)
	{
		return world.getHighestBlockAt(new Location(world,X,0,Z)).getY();
	}

}
