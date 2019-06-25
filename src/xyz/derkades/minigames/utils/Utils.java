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
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.Minigames;

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

	@Deprecated
	public static void sendTitle(final Player player, final String title, final String subtitle) {
		player.sendTitle(title, subtitle, 10, 70, 20);
	}

	@Deprecated
	public static void setGameRule(final String gameRule, final boolean setting){
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule " + gameRule + " " + setting);
	}

	@Deprecated
	public static void clearPotionEffects(final Player player){
		for (final PotionEffect effect : player.getActivePotionEffects())
	        player.removePotionEffect(effect.getType());
	}

	@Deprecated
	public static void clearPotionEffects() {
		Bukkit.getOnlinePlayers().forEach(Utils::clearPotionEffects);
	}

	@Deprecated
	public static void clearInventory(final Player player){
		final PlayerInventory inv = player.getInventory();
		inv.clear();
		final ItemStack air = new ItemStack(Material.AIR);
		inv.setHelmet(air);
		inv.setChestplate(air);
		inv.setLeggings(air);
		inv.setBoots(air);
	}

	@Deprecated
	public static void clearInventory() {
		Bukkit.getOnlinePlayers().forEach(Utils::clearInventory);
	}

	@Deprecated
	public static void giveEffect(final Player player, final int duration, final PotionEffectType type, final int amplifier) {
		player.addPotionEffect(new PotionEffect(type, duration * 20, amplifier, true, false));
	}

	@Deprecated
	public static void giveInfiniteEffect(final Player player, final PotionEffectType type, final int amplifier){
		player.addPotionEffect(new PotionEffect(type, 100000, amplifier, true, false));
	}

	@Deprecated
	public static void giveInfiniteEffect(final Player player, final PotionEffectType type){
		player.addPotionEffect(new PotionEffect(type, 100000, 0, true, false));
	}

	@Deprecated
	public static void giveInvisibility(final Player player){
		giveInfiniteEffect(player, PotionEffectType.INVISIBILITY);
	}

	@Deprecated
	public static void setArmor(final Player player, final ItemStack helmet, final ItemStack chestplate, final ItemStack leggings, final ItemStack boots){
		final PlayerInventory inv = player.getInventory();
		inv.setHelmet(helmet);
		inv.setChestplate(chestplate);
		inv.setLeggings(leggings);
		inv.setBoots(boots);
	}

	@Deprecated
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

	public static int getAliveCountFromDeadList(final List<UUID> dead) {
		return Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).filter(uuid -> !dead.contains(uuid)).toArray().length;
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

	public static List<Player> getWinnersFromFinished(final List<UUID> finished, final List<UUID> all) {
		return all.stream().filter(finished::contains).map(Bukkit::getPlayer).filter(p -> p != null).collect(Collectors.toList());
	}

	public static List<Player> getWinnersFromFinished(final List<UUID> finished) {
		return Bukkit.getOnlinePlayers().stream().filter((p) -> finished.contains(p.getUniqueId())).collect(Collectors.toList());
	}

	public static List<Player> getWinnersFromAliveList(final List<UUID> alive, final boolean multipleWinnersAllowed){
		final List<Player> winners = alive.stream().map(Bukkit::getPlayer).filter(p -> p != null).collect(Collectors.toList());

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

	@Deprecated
	public static void playSoundForAllPlayers(final Sound sound, final float pitch) {
		for (final Player player : Bukkit.getOnlinePlayers())
			player.playSound(player.getLocation(), sound, 1, pitch);
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

	@Deprecated
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

	@Deprecated
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

	@Deprecated
	public static void delayedTeleport(final Location location, final Collection<? extends Player> players) {
		delayedTeleport(location, players.toArray(new Player[] {}));
	}

	@Deprecated
	public static void delayedTeleport(final Location location, final Consumer<Player> runnable, final Collection<? extends Player> players) {
		delayedTeleport(location, runnable, players.toArray(new Player[] {}));
	}

	public static void launch(final Player player, final double upwardVelocity, final double multiplyInLookingDirection){
		player.setVelocity(player.getLocation().getDirection().multiply(multiplyInLookingDirection));
		player.setVelocity(new Vector(player.getVelocity().getX(), upwardVelocity, player.getVelocity().getZ()));
	}

	public static boolean isIn2dBounds(final Location location, final Location cornerOne, final Location cornerTwo) {
		if (!location.getWorld().equals(cornerOne.getWorld())) {
			return false;
		}

		final int maxX = Math.max(cornerOne.getBlockX(), cornerTwo.getBlockX());
		final int minX = Math.min(cornerOne.getBlockX(), cornerTwo.getBlockX());
		final int maxZ = Math.max(cornerOne.getBlockZ(), cornerTwo.getBlockZ());
		final int minZ = Math.min(cornerOne.getBlockZ(), cornerTwo.getBlockZ());

		final int x = location.getBlockX();
		final int z = location.getBlockZ();

		return x > minX && x < maxX &&
				z > minZ && z < maxZ;
	}

	@Deprecated
	public static boolean isIn2dBounds(final Player player, final Location cornerOne, final Location cornerTwo) {
		return isIn2dBounds(player.getLocation(), cornerOne, cornerTwo);
	}

	public static String getChatPrefix(final ChatColor color, final char c) {
		return ChatColor.DARK_GRAY + "[" + color + c + ChatColor.DARK_GRAY + "]" + ChatColor.DARK_GRAY + " | " + ChatColor.GRAY;
	}

	public static ComponentBuilder getComponentBuilderWithPrefix(final ChatColor prefixColor, final char prefixChar) {
		return new ComponentBuilder("").appendLegacy(Utils.getChatPrefix(prefixColor, prefixChar));
	}

	public static void teleportUp(final Player player, final int yUp) {
		final Location loc = player.getLocation();
		loc.setY(loc.getY() + yUp);
		player.teleport(loc);
	}

	public static void setMetadata(final Player player, final String key, final Object value) {
		player.removeMetadata("minigames_" + key, Minigames.getInstance());
		player.setMetadata("minigames_" + key, new FixedMetadataValue(Minigames.getInstance(), value));
	}

	public static MetadataValue getMetadata(final Player player, final String key) {
		return player.getMetadata("minigames_" + key).get(0);
	}

	public static boolean allPlayersFinished(final List<UUID> finished) {
		for (final Player player : Bukkit.getOnlinePlayers()) {
			if (!finished.contains(player.getUniqueId())) {
				return false;
			}
		}
		return true;
	}

}
