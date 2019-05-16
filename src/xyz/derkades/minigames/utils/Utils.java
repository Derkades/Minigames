package xyz.derkades.minigames.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.Var;

public class Utils {
	
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	    return map.entrySet()
	              .stream()
	              .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
	              .collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, 
	                (e1, e2) -> e1, 
	                LinkedHashMap::new
	              ));
	}
	
	public static void setGameRule(String gameRule, boolean setting){
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule " + gameRule + " " + setting);
	}
	
	public static void clearPotionEffects(Player player){
		for (PotionEffect effect : player.getActivePotionEffects())
	        player.removePotionEffect(effect.getType());
	}
	
	public static void clearPotionEffects() {
		for (Player player : Bukkit.getOnlinePlayers())
			clearPotionEffects(player);
	}
	
	public static void clearInventory(Player player){
		PlayerInventory inv = player.getInventory();
		inv.clear();
		ItemStack air = new ItemStack(Material.AIR);
		inv.setHelmet(air);
		inv.setChestplate(air);
		inv.setLeggings(air);
		inv.setBoots(air);
	}
	
	public static void giveInfiniteEffect(Player player, PotionEffectType type, int amplifier){
		player.addPotionEffect(new PotionEffect(type, 100000, amplifier, true, false));
	}
	
	public static void giveInfiniteEffect(Player player, PotionEffectType type){
		player.addPotionEffect(new PotionEffect(type, 100000, 0, true, false));
	}
	
	public static void giveInvisibility(Player player){
		giveInfiniteEffect(player, PotionEffectType.INVISIBILITY);
	}
	
	public static void setArmor(Player player, ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots){
		PlayerInventory inv = player.getInventory();
		inv.setHelmet(helmet);
		inv.setChestplate(chestplate);
		inv.setLeggings(leggings);
		inv.setBoots(boots);
	}
	
	public static void setArmor(Player player, Material helmet, Material chestplate, Material leggings, Material boots){
		setArmor(player,
				new ItemStack(helmet),
				new ItemStack(chestplate),
				new ItemStack(leggings),
				new ItemStack(boots));
	}
	
	public static void teleportToLobby(Player player){
		player.teleport(Var.LOBBY_LOCATION);
	}
	
	public static List<Player> getWinnersFromIsDeadHashMap(Map<String, Boolean> isDead){
		List<Player> winners = new ArrayList<Player>();
		for (Player player: Bukkit.getOnlinePlayers()){
			if (!isDead.get(player.getName())){
				winners.add(player);
			}
		}
		return winners;
	}
	
	public static List<Player> getWinnersFromFinishedHashMap(Map<String, Boolean> hasFinished){
		List<Player> winners = new ArrayList<Player>();
		for (Player player: Bukkit.getOnlinePlayers()){
			if (hasFinished.get(player.getName())){
				winners.add(player);
			}
		}
		return winners;
	}
	
	public static List<Player> getWinnersFromPointsHashmap(Map<UUID, Integer> points){
		int maxPoints = Collections.max(points.values());
		
		List<Player> winners = new ArrayList<>();
		
		for (Map.Entry<UUID, Integer> entry : points.entrySet()) {
			if (entry.getValue() == maxPoints) {
				Player player = Bukkit.getPlayer(entry.getKey());
				if (player != null) {
					winners.add(player);
				}
			}
		}
		
		return winners;
	}
	
	public static boolean allPlayersWon(List<UUID> winners) {
		boolean missing = false;
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!winners.contains(player.getUniqueId())) {
				missing = true;
			}
		}
		
		return !missing;
	}
	
	public static int getAliveCountFromDeadList(List<UUID> dead) {
		int alive = 0;
		
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!dead.contains(player.getUniqueId())) {
				alive++;
			}
		}
		
		return alive;
	}
	
	public static List<Player> getPlayerListFromUUIDList(List<UUID> list){
		List<Player> players = new ArrayList<>();
		for (UUID uuid : list) {
			Player player = Bukkit.getPlayer(uuid);
			if (player != null) {
				players.add(player);
			}
		}
		return players;
	}
	
	public static List<Player> getWinnersFromDeadList(List<UUID> dead){
		List<Player> winners = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (!dead.contains(player.getUniqueId())) {
				winners.add(player);
			}
		}
		return winners;
	}
	
	public static void playSoundForAllPlayers(Sound sound, float pitch){
		for (Player player : Bukkit.getOnlinePlayers())
			player.playSound(player.getLocation(), sound, 1, pitch);
	}
	
	public static Block getBlockStandingOn(Player player){
		Block block = player.getLocation().getBlock();
		Block below = block.getRelative(BlockFace.DOWN);
		return below;
	}
	
	public static <Key> Map<Key, Integer> getHighestValuesFromHashMap(Map<Key, Integer> map){
		Integer max = map.entrySet()
	            .stream()
	            .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
	            .get()
	            .getValue();

	    List<Key> listOfMax = map.entrySet()
	            .stream()
	            .filter(entry -> entry.getValue() == max)
	            .map(Map.Entry::getKey)
	            .collect(Collectors.toList());

	    Map<Key, Integer> highest = new HashMap<Key, Integer>();
	    for (Key key : listOfMax){
	    	highest.put(key, map.get(key));
	    }
	    
	    return highest;
	}
	
	public static void setXpBarValue(float fill, int level){
		for (Player player : Bukkit.getOnlinePlayers()){
			player.setExp(fill);
			player.setLevel(level);
		}
	}
	
	// Taken from https://stackoverflow.com/a/11926952
	public static <E> E getWeightedRandom(Map<E, Double> weights) {
	    E result = null;
	    double bestValue = Double.MAX_VALUE;

	    for (E element : weights.keySet()) {
	        double value = -Math.log(Random.getRandomDouble()) / weights.get(element);

	        if (value < bestValue) {
	            bestValue = value;
	            result = element;
	        }
	    }

	    return result;
	}
	
	public static void particle(final Particle particle, final Location location, final double speed, final int count, final double offsetX, final double offsetY, final int offsetZ) {
		Var.WORLD.spawnParticle(particle, location, count, offsetX, offsetY, offsetX, speed);
	}
	
	public static void particle(final Particle particle, final double x, final double y, final double z, final double speed, final int count, final double offsetX, final double offsetY, final int offsetZ) {
		Var.WORLD.spawnParticle(particle, new Location(Var.WORLD, x, y, z), count, offsetX, offsetY, offsetX, speed);
	}
	
	public static void particle(final Particle particle, final Location location, final int count) {
		Var.WORLD.spawnParticle(particle, location, count);
	}

}
