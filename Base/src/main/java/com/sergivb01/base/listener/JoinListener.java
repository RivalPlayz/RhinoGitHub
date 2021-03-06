package com.sergivb01.base.listener;

import com.sergivb01.base.BasePlugin;
import com.sergivb01.base.user.BaseUser;
import com.sergivb01.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

import java.util.UUID;

public class JoinListener
		implements Listener{
	private final BasePlugin plugin;
	private ItemStack itemStack = new ItemBuilder(Material.BOOK)
			.displayName(ChatColor.DARK_AQUA + "Open Kits Menu")
			.lore(ChatColor.GRAY + "Right click to open kits GUI.")
			.build();

	public JoinListener(BasePlugin plugin){
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		BaseUser baseUser = this.plugin.getUserManager().getUser(uuid);
		if(this.plugin.getServerHandler().isLockdown()) //TODO: This doesn't have any sense
			for(Player player1 : Bukkit.getServer().getOnlinePlayers()){
				if(!player1.hasPermission("command.staffchat")) continue;
				if(!baseUser.getNotes().isEmpty()){
					player1.sendMessage(ChatColor.YELLOW + player.getName() + ChatColor.BOLD + " has the following notes" + ChatColor.RED + '\u2193');
				}
				for(String notes : baseUser.getNotes()){
					player1.sendMessage(notes);
				}
			}

		baseUser.tryLoggingName(player);
		baseUser.tryLoggingAddress(player.getAddress().getAddress().getHostAddress());
	}

	@EventHandler
	public void onPlayerReSpawn(PlayerRespawnEvent event){
		if(!plugin.getServerHandler().isKitmap()){
			return;
		}

		Player player = event.getPlayer();

		player.getInventory().setHeldItemSlot(4);
		Bukkit.getScheduler().runTaskLater(plugin, () -> player.getInventory().setItem(4, itemStack), 2L);
	}

	@EventHandler
	public void onPlayerSpawn(PlayerSpawnLocationEvent event){
		if(!plugin.getServerHandler().isKitmap()){
			return;
		}

		Player player = event.getPlayer();
		player.getInventory().setHeldItemSlot(4);
		Bukkit.getScheduler().runTaskLater(plugin, () -> player.getInventory().setItem(4, itemStack), 2L);
	}

	@EventHandler
	public void onItemInteract(PlayerInteractEvent event){
		if(!plugin.getServerHandler().isKitmap()){
			return;
		}

		Action a = event.getAction();
		ItemStack is = event.getItem();
		if((a == Action.PHYSICAL) || (is == null) || (is.getType() == Material.AIR)){
			return;
		}

		if(a.equals(Action.LEFT_CLICK_AIR) || a.equals(Action.LEFT_CLICK_BLOCK)){
			return;
		}

		Player player = event.getPlayer();
		ItemStack itemStack = event.getItem();
		if(itemStack.getItemMeta().getDisplayName().equals(ChatColor.DARK_AQUA + "Open Kits Menu")){
			Bukkit.dispatchCommand(player, "kit gui");
		}
	}


}




