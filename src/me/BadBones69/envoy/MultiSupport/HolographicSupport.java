package me.BadBones69.envoy.MultiSupport;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.placeholder.PlaceholderReplacer;

import me.BadBones69.envoy.Main;
import me.BadBones69.envoy.Methods;
import me.BadBones69.envoy.api.Envoy;

public class HolographicSupport {
	
	private static HashMap<Location, Hologram> holos = new HashMap<Location, Hologram>();
	private static Plugin plugin = Bukkit.getPluginManager().getPlugin("CrazyEnvoy");
	
	public static void registerPlaceHolders(){
		HologramsAPI.registerPlaceholder(plugin, "{envoy_cooldown}", 1, new PlaceholderReplacer() {
			@Override
			public String update() {
				if(Envoy.isEnvoyActive()){
					return Main.settings.getMessages().getString("Messages.Hologram-Placeholders.On-Going");
				}else{
					return Envoy.getNextEnvoyTime();
				}
			}
		});
		
		HologramsAPI.registerPlaceholder(plugin, "{envoy_time_left}", 1, new PlaceholderReplacer() {
			@Override
			public String update() {
				if(Envoy.isEnvoyActive()){
					return Envoy.getEnvoyRunTimeLeft();
				}else{
					return Main.settings.getMessages().getString("Messages.Hologram-Placeholders.Not-Running");
				}
			}
		});
		
		HologramsAPI.registerPlaceholder(plugin, "{envoy_crates_left}", .5, new PlaceholderReplacer() {
			@Override
			public String update() {
				return Envoy.getActiveEvoys().size() + "";
			}
		});
		
	}
	
	public static void unregisterPlaceHolders(){
		HologramsAPI.unregisterPlaceholders(plugin);
	}
	
	public static void createHologram(Location loc, String tier){
		Hologram hg = HologramsAPI.createHologram(plugin, loc);
		for(String line : Main.settings.getFile(tier).getStringList("Settings.Hologram")){
			hg.appendTextLine(Methods.color(line));
		}
		holos.put(loc, hg);
	}
	
	public static void removeHologram(Location loc){
		if(holos.containsKey(loc)){
			Hologram hg = holos.get(loc);
			holos.remove(loc);
			hg.delete();
		}
	}
	
	public static void removeAllHolograms(){
		for(Location loc : holos.keySet()){
			holos.get(loc).delete();
		}
		holos.clear();
	}
	
}