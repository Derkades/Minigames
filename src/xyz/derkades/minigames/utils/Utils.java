package xyz.derkades.minigames.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
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
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Var;

public class Utils {

	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map) {
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

	public static void sendTitle(final Player player, final String title, final String subtitle) {
		player.sendTitle(title, subtitle, 10, 70, 20);
	}

	public static void setGameRule(final String gameRule, final boolean setting){
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule " + gameRule + " " + setting);
	}

	public static void clearPotionEffects(final Player player){
		for (final PotionEffect effect : player.getActivePotionEffects())
	        player.removePotionEffect(effect.getType());
	}

	public static void clearPotionEffects() {
		Bukkit.getOnlinePlayers().forEach(Utils::clearPotionEffects);
	}

	public static void clearInventory(final Player player){
		final PlayerInventory inv = player.getInventory();
		inv.clear();
		final ItemStack air = new ItemStack(Material.AIR);
		inv.setHelmet(air);
		inv.setChestplate(air);
		inv.setLeggings(air);
		inv.setBoots(air);
	}
	
	public static void clearInventory() {
		Bukkit.getOnlinePlayers().forEach(Utils::clearInventory);
	}

	public static void giveEffect(final Player player, final int duration, final PotionEffectType type, final int amplifier) {
		player.addPotionEffect(new PotionEffect(type, duration * 20, amplifier, true, false));
	}

	public static void giveInfiniteEffect(final Player player, final PotionEffectType type, final int amplifier){
		player.addPotionEffect(new PotionEffect(type, 100000, amplifier, true, false));
	}

	public static void giveInfiniteEffect(final Player player, final PotionEffectType type){
		player.addPotionEffect(new PotionEffect(type, 100000, 0, true, false));
	}

	public static void giveInvisibility(final Player player){
		giveInfiniteEffect(player, PotionEffectType.INVISIBILITY);
	}

	public static void setArmor(final Player player, final ItemStack helmet, final ItemStack chestplate, final ItemStack leggings, final ItemStack boots){
		final PlayerInventory inv = player.getInventory();
		inv.setHelmet(helmet);
		inv.setChestplate(chestplate);
		inv.setLeggings(leggings);
		inv.setBoots(boots);
	}

	public static void setArmor(final Player player, final Material helmet, final Material chestplate, final Material leggings, final Material boots){
		setArmor(player,
				new ItemStack(helmet),
				new ItemStack(chestplate),
				new ItemStack(leggings),
				new ItemStack(boots));
	}

	public static List<Player> getWinnersFromPointsHashmap(final Map<UUID, Integer> points){
		if (points == null || points.isEmpty()) {
			return new ArrayList<>();
		}

		final int maxPoints = Collections.max(points.values());

		if (maxPoints == 0) {
			return new ArrayList<>();
		}
		
		return points.entrySet().stream().filter(e -> e.getValue() == maxPoints)
				.map(e -> Bukkit.getPlayer(e.getKey())).filter(p -> p != null)
				.collect(Collectors.toList());
	}

	@Deprecated
	public static boolean allPlayersWon(final List<UUID> winners) {
		boolean missing = false;

		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (!winners.contains(player.getUniqueId())) {
				missing = true;
			}
		}

		return !missing;
	}

	public static int getAliveCountFromDeadList(final List<UUID> dead) {
		return Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).filter(dead::contains).toArray().length;
	}

	public static int getAliveAcountFromDeadAndAllList(final List<UUID> dead, final List<UUID> all) {
		int alive = 0;

		for (final Player player : getPlayerListFromUUIDList(all)) {
			if (!dead.contains(player.getUniqueId())) {
				alive++;
			}
		}

		return alive;
	}

	public static List<Player> getPlayerListFromUUIDList(final List<UUID> list){
		return list.stream().map(Bukkit::getPlayer).filter(p -> p != null).collect(Collectors.toList());
	}

	@Deprecated
	public static List<Player> getWinnersFromDeadList(final List<UUID> dead){
		return getWinnersFromDeadAndAllList(dead, Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toList()), true);
	}

	public static List<Player> getWinnersFromDeadAndAllList(final List<UUID> dead, final List<UUID> all, final boolean multipleWinnersAllowed){
		final List<Player> winners = all.stream().map(Bukkit::getPlayer).filter(p -> p != null)
				.filter(p -> !dead.contains(p.getUniqueId())).collect(Collectors.toList());

		if (multipleWinnersAllowed) {
			return winners;
		}

		if (winners.size() == 1) {
			return winners;
		} else {
			return new ArrayList<>();
		}
	}
	
	public static List<UUID> getOnlinePlayersUuidList(){
		return Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toList());
	}

	public static void playSoundForAllPlayers(final Sound sound, final float pitch) {
		for (final Player player : Bukkit.getOnlinePlayers())
			player.playSound(player.getLocation(), sound, 1, pitch);
	}

	@Deprecated
	public static Block getBlockStandingOn(final Player player){
		return player.getLocation().getBlock().getRelative(BlockFace.DOWN);
	}

	public static <Key> List<Key> getHighestValuesFromHashMap(final Map<Key, Integer> map){
		final Integer max = map.entrySet()
	            .stream()
	            .max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1)
	            .get().getValue();

	    return map.entrySet() .stream()
	            .filter(entry -> entry.getValue() == max)
	            .map(Map.Entry::getKey)
	            .collect(Collectors.toList());
	}

	public static void setXpBarValue(final float fill, final int level){
		for (final Player player : Bukkit.getOnlinePlayers()){
			player.setExp(fill);
			player.setLevel(level);
		}
	}

	// Taken from https://stackoverflow.com/a/11926952
	public static <E> E getWeightedRandom(final Map<E, Double> weights) {
	    E result = null;
	    double bestValue = Double.MAX_VALUE;

	    for (final E element : weights.keySet()) {
	        final double value = -Math.log(Random.getRandomDouble()) / weights.get(element);

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

	public static void hideForEveryoneElse(final Player player) {
		Bukkit.getOnlinePlayers().forEach((player2) -> player2.hidePlayer(Minigames.getInstance(), player));
	}

	public static void showEveryoneToEveryone() {
		for (final Player player1 : Bukkit.getOnlinePlayers()) {
			for (final Player player2 : Bukkit.getOnlinePlayers()) {
				player1.showPlayer(Minigames.getInstance(), player2);
			}
		}
	}

	public static void delayedTeleport(final Location location, final Player... players) {
		int delay = 0;

		for (final Player player : players) {
			Scheduler.delay(delay, () -> player.teleport(location));
			delay +=2;
		}
	}

	public static void delayedTeleport(final Location location, final Consumer<Player> runnable, final Player... players) {
		int delay = 0;

		for (final Player player : players) {
			Scheduler.delay(delay, () -> {
				player.teleport(location);
				runnable.accept(player);
			});
			delay +=2;
		}
	}

	public static void delayedTeleport(final Location location, final Collection<? extends Player> players) {
		delayedTeleport(location, players.toArray(new Player[] {}));
	}

	public static void delayedTeleport(final Location location, final Consumer<Player> runnable, final Collection<? extends Player> players) {
		delayedTeleport(location, runnable, players.toArray(new Player[] {}));
	}

	public static void launch(final Player player, final double upwardVelocity, final double multiplyInLookingDirection){
		player.setVelocity(player.getLocation().getDirection().multiply(multiplyInLookingDirection));
		player.setVelocity(new Vector(player.getVelocity().getX(), upwardVelocity, player.getVelocity().getZ()));
	}

	public static boolean isIn2dBounds(final Location location, final Location cornerOne, final Location cornerTwo) {
		final int maxX = Math.max(cornerOne.getBlockX(), cornerTwo.getBlockX());
		final int minX = Math.min(cornerOne.getBlockX(), cornerTwo.getBlockX());
		final int maxZ = Math.max(cornerOne.getBlockZ(), cornerTwo.getBlockZ());
		final int minZ = Math.min(cornerOne.getBlockZ(), cornerTwo.getBlockZ());

		final int x = location.getBlockX();
		final int z = location.getBlockZ();

		return x > minX && x < maxX &&
				z > minZ && z < maxZ;
	}

	public static boolean isIn2dBounds(final Player player, final Location cornerOne, final Location cornerTwo) {
		return isIn2dBounds(player.getLocation(), cornerOne, cornerTwo);
	}

	public static String getChatPrefix(final ChatColor color, final char c) {
		return ChatColor.BLACK + "[" + color + c + ChatColor.BLACK + "]" + ChatColor.DARK_GRAY + " | " + ChatColor.GRAY;
	}
	
	public static ComponentBuilder getComponentBuilderWithPrefix(ChatColor prefixColor, char prefixChar) {
		return new ComponentBuilder("").appendLegacy(Utils.getChatPrefix(prefixColor, prefixChar));
	}

}
