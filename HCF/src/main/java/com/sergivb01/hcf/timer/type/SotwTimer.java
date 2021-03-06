package com.sergivb01.hcf.timer.type;

import com.sergivb01.hcf.HCF;
import com.sergivb01.hcf.utils.config.ConfigurationService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SotwTimer{

	public List<UUID> enabledSotw = new ArrayList<>();

	private SotwRunnable sotwRunnable;

	public boolean cancel(){
		if(this.sotwRunnable != null){
			this.sotwRunnable.cancel();
			this.sotwRunnable = null;
			return true;
		}
		return false;
	}

	public void start(final long millis){
		if(this.sotwRunnable == null){
			(this.sotwRunnable = new SotwRunnable(this, millis)).runTaskLater(HCF.getPlugin(), millis / 50L);
		}
	}

	public SotwRunnable getSotwRunnable(){
		return this.sotwRunnable;
	}

	public static class SotwRunnable extends BukkitRunnable{
		private SotwTimer sotwTimer;
		private long startMillis;
		private long endMillis;

		public SotwRunnable(final SotwTimer sotwTimer, final long duration){
			super();
			this.sotwTimer = sotwTimer;
			this.startMillis = System.currentTimeMillis();
			this.endMillis = this.startMillis + duration;
		}

		public long getRemaining(){
			return this.endMillis - System.currentTimeMillis();
		}

		public boolean isSotwEnabled(Player player) {
			if (HCF.getInstance().getSotwTimer().enabledSotw.contains(player.getUniqueId())) {
				return true;
			} else {
				return false;
			}
		}

		public void run(){
			HCF.getInstance().getSotwTimer().enabledSotw.clear();
			Bukkit.broadcastMessage(ConfigurationService.SOTW_ENDED_ONE);
			Bukkit.broadcastMessage(ConfigurationService.SOTW_ENDED_TWO);
			this.cancel();
			this.sotwTimer.sotwRunnable = null;
		}


	}


}
