package com.sergivb01.hcf.tab;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.balance.EconomyManager;
import com.sergivb01.hcf.faction.FactionManager;
import com.sergivb01.hcf.faction.FactionMember;
import com.sergivb01.hcf.faction.struct.Role;
import com.sergivb01.hcf.faction.type.PlayerFaction;
import com.sergivb01.hcf.tab.com.bizarrealex.azazel.Azazel;
import com.sergivb01.hcf.tab.com.bizarrealex.azazel.tab.TabAdapter;
import com.sergivb01.hcf.tab.com.bizarrealex.azazel.tab.TabTemplate;
import com.sergivb01.hcf.tab.tabs.StaffTab;
import com.sergivb01.hcf.user.FactionUser;
import com.sergivb01.hcf.user.TabStyles;
import com.sergivb01.hcf.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerTab implements TabAdapter, Listener{
	public static List<Player> clean = new ArrayList<>();
	private HCF plugin;
	private Azazel azazel;
	private StaffTab staffTab;
	private UserManager userManager;

	public PlayerTab(HCF plugin){
		this.plugin = plugin;
		this.azazel = new Azazel(plugin, this);
		this.staffTab = new StaffTab();
		this.userManager = HCF.getInstance().getUserManager();
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public TabTemplate getTemplate(Player player){
		final FactionUser factionUser = this.plugin.getUserManager().getUser(player.getUniqueId());
		PlayerFaction playerFaction = this.plugin.getFactionManager().getPlayerFaction(player.getUniqueId());
		int factionBalance = playerFaction.getBalance();
		FactionMember factionMember = playerFaction.getMember(player.getUniqueId());
		UUID uuid = player.getUniqueId();
		FactionMember selfMember = playerFaction.getMember(uuid);
		Role selfRole = selfMember.getRole();
		if(clean.remove(player)){
			return getClearTemplate();
		}

		if(userManager.getUser(player.getUniqueId()).getTabStyle().equals(TabStyles.STAFF)){
			return staffTab.getTemplate(player);
		}

		TabTemplate tabTemplate = new TabTemplate();


		tabTemplate.left(4, "&3&lFaction Info&7:");
		tabTemplate.left(5, ChatColor.GRAY + "DTR:" + playerFaction.getDeathsUntilRaidable());
		tabTemplate.left(6, ChatColor.GRAY + "Online: " + playerFaction.getOnlinePlayers().size() + "/" + playerFaction.getMembers().size());
		tabTemplate.left(7, ChatColor.GRAY + "Balance: " + factionBalance);

		tabTemplate.left(9, "&3&lPlayer Info&7:");
		tabTemplate.left(10, "Kills: " + factionUser.getKills());
		tabTemplate.left(11, "Deaths: " + factionUser.getDeaths());

		tabTemplate.left(13, "&3&lLocation&7:");
		tabTemplate.left(14, "&2Spawn");
		tabTemplate.left(15, "(x, y) [NE]");


		tabTemplate.middle(0, "&3&lHCFactions");
		tabTemplate.middle(2, "Online: &b" + Bukkit.getOnlinePlayers().size());

		tabTemplate.middle(4, ChatColor.GOLD.toString() + playerFaction.getDisplayName(player));
		tabTemplate.middle(5, "" + playerFaction.getLeader().getName());
		tabTemplate.middle(6, "" + playerFaction.getName());
		tabTemplate.middle(7, "" + Role.MEMBER);


		if(userManager.getUser(player.getUniqueId()).getTabStyle().equals(TabStyles.FACTION_LIST)){
			tabTemplate.right(0, "&3&lFaction List&7:");
			for(int i = 1; i < 20; i++){
				tabTemplate.right(i, "FactionName" + (19 - i) + " &b(" + (19 - i) + "/10)");
			}
			return tabTemplate;
		}
		tabTemplate.right(4, "&3&lMap Information&7:");
		tabTemplate.right(5, "Prot I - Sharp I");
		tabTemplate.right(6, "Border: &b3000");
		tabTemplate.right(7, "SOTW Date: &b18/06/18");

		tabTemplate.right(9, "&3&lLast EOTW&7:");
		tabTemplate.right(10, "Capper: &bBradva");
		tabTemplate.right(11, "FFA: &bveilkid");

		tabTemplate.right(13, "&3&lCurrent Event&7:");
		tabTemplate.right(14, "&bSky Koth &f(500, -500)");


		tabTemplate.farRight(9, "&cFor an optimal experience");
		tabTemplate.farRight(10, "&cWe recommend &4&lCheatBreaker");

		return tabTemplate;
	}

	private TabTemplate getClearTemplate(){
		TabTemplate tabTemplate = new TabTemplate();
		for(int i = 0; i < 20; i++){
			tabTemplate.left(0, "");
			tabTemplate.middle(0, "");
			tabTemplate.right(0, "");
			tabTemplate.farRight(0, "");
		}
		return tabTemplate;
	}


}
