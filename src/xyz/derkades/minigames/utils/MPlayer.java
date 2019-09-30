package xyz.derkades.minigames.utils;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.derkades.derkutils.bukkit.BlockUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.LocationUtils;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.SneakPrevention;

public class MPlayer {

	private final Player player;

	public MPlayer(final Player player) {
		this.player = player;
	}

	public MPlayer(final PlayerEvent event) {
		this.player = event.getPlayer();
	}

	public MPlayer(final EntityEvent event) {
		this.player = (Player) event.getEntity();
	}

	public void setDisableDamage(final boolean disableDamage) {
		Utils.setMetadata(this.player, "disable_damage", disableDamage);
	}

	public boolean getDisableDamage() {
		return Utils.getMetadata(this.player, "disable_damage").asBoolean();
	}

	public void setDisableHunger(final boolean disableHunger) {
		Utils.setMetadata(this.player, "disable_hunger", disableHunger);
	}

	public boolean getDisableHunger() {
		try {
			return Utils.getMetadata(this.player, "disable_hunger").asBoolean();
		} catch (final IndexOutOfBoundsException e) {
			return true;
		}
	}

	public void setDisableItemMoving(final boolean disableItemMoving) {
		Utils.setMetadata(this.player, "disable_item_moving", disableItemMoving);
	}

	public boolean getDisableItemMoving() {
		return Utils.getMetadata(this.player, "disable_item_moving").asBoolean();
	}

	public void setDisableSneaking(final boolean disableSneaking) {
		//Utils.setMetadata(this.player, "disable_sneaking", disableSneaking);
		SneakPrevention.setCanSneak(this.player, !disableSneaking);
	}

	public boolean getDisableSneaking() {
		//return Utils.getMetadata(this.player, "disable_sneaking").asBoolean();
		return !SneakPrevention.getCanSneak(this.player);
	}

	public Player bukkit() {
		return this.player;
	}

	public Player.Spigot spigot(){
		return this.player.spigot();
	}

	public void teleport(final Location location) {
		this.player.teleport(location);
	}

	public void teleport(final World world, final double x, final double y, final double z) {
		this.player.teleport(new Location(world, x, y, z));
	}

	public boolean isIn2dBounds(final Location cornerOne, final Location cornerTwo) {
		return LocationUtils.isIn2dBounds(this.player.getLocation(), cornerOne, cornerTwo);
	}

	public boolean isIn2dBounds(final World world, final double x1, final double z1, final double x2, final double z2) {
		return this.isIn2dBounds(new Location(world, x1, 0, z1), new Location(world, x2, 0, z2));
	}

	public Location getLocation() {
		return this.player.getLocation();
	}

	public double getX() {
		return this.getLocation().getX();
	}

	public double getY() {
		return this.getLocation().getY();
	}

	public double getZ() {
		return this.getLocation().getZ();
	}

	public int getBlockX() {
		return this.getLocation().getBlockX();
	}

	public int getBlockY() {
		return this.getLocation().getBlockY();
	}

	public int getBlockZ() {
		return this.getLocation().getBlockZ();
	}

    public void giveLobbyInventoryItems() {
    	/*if (this.player.hasPermission("games.torch")) {
			this.player.getInventory().setItem(7, new ItemBuilder(Material.REDSTONE_TORCH)
					.name(ChatColor.AQUA + "" + ChatColor.BOLD + "Staff lounge key")
					.lore(ChatColor.YELLOW + "Place in upper-south-east-corner on gray terracotta")
					.canPlaceOn("cyan_terracotta")
					.create());
		}*/

		this.player.getInventory().setItem(8, new ItemBuilder(Material.COMPARATOR)
				.name(ChatColor.AQUA + "" + ChatColor.BOLD + "Menu")
				.lore(ChatColor.YELLOW + "Click to open menu")
				.create());
    }

    public void setExp(final float exp) {
    	this.player.setExp(exp);
    }

    public void setLevel(final int level) {
    	this.player.setLevel(level);
    }

    public String getName() {
    	return this.player.getName();
    }

    public UUID getUniqueId() {
    	return this.player.getUniqueId();
    }

    public void clearInventory() {
		final PlayerInventory inv = this.player.getInventory();
		inv.clear();
		final ItemStack air = new ItemStack(Material.AIR);
		inv.setHelmet(air);
		inv.setChestplate(air);
		inv.setLeggings(air);
		inv.setBoots(air);
    }

    public void queueTeleport(final Location location) {
    	Queue.add(() -> this.player.teleport(location));
    }

    public void setAllowFlight(final boolean allowFlight) {
    	this.player.setAllowFlight(allowFlight);
    }

    public boolean getAllowFlight() {
    	return this.player.getAllowFlight();
    }

    public void setGameMode(final GameMode gameMode) {
    	this.player.setGameMode(gameMode);
    }

    public GameMode getGameMode() {
    	return this.player.getGameMode();
    }

	public void clearPotionEffects(){
		for (final PotionEffect effect : this.player.getActivePotionEffects()) {
			this.player.removePotionEffect(effect.getType());
		}
	}

	public void sendTitle(final String title, final String subtitle) {
		this.player.sendTitle(title, subtitle, 10, 70, 20);
	}

	public PlayerInventory getInventory() {
		return this.player.getInventory();
	}

	public void giveItem(final ItemStack... itemStacks) {
		this.player.getInventory().addItem(itemStacks);
	}

	public void teleportUp(final int yUp) {
		final Location loc = this.player.getLocation();
		loc.setY(loc.getY() + yUp);
		this.player.teleport(loc);
	}

	public void dieTo(final Location location) {
		this.player.teleport(location);
		this.die();
	}

	public void dieUp(final int yUp) {
		this.teleportUp(yUp);
		this.die();
	}

	public void finishTo(final Location location) {
		this.player.teleport(location);
		this.finish();
	}

	public void finishUp(final int yUp) {
		this.teleportUp(yUp);
		this.finish();
	}

	public void die() {
		this.sendTitle( "", ChatColor.RED + "You've died");
		this.spectator();
	}

	public void finish() {
		this.sendTitle("", ChatColor.GREEN + "You've finished");
		this.spectator();
	}

	public void spectator() {
		this.player.setGameMode(GameMode.SPECTATOR);
		SneakPrevention.setCanSneak(this.player, true);
		this.sendActionBar(new ComponentBuilder("You are now a specator. Use /spec <player> to spectate a player.")
				.color(ChatColor.GRAY).create());
	}

	public void giveEffect(final PotionEffectType type, final int duration, final int amplifier) {
		this.player.addPotionEffect(new PotionEffect(type, duration * 20, amplifier, true, false));
	}

	public void giveInfiniteEffect(final PotionEffectType type, final int amplifier){
		this.player.addPotionEffect(new PotionEffect(type, 100000, amplifier, true, false));
	}

	public void giveInfiniteEffect(final PotionEffectType type){
		this.player.addPotionEffect(new PotionEffect(type, 100000, 0, true, false));
	}

	public void giveInvisibility(){
		this.giveInfiniteEffect(PotionEffectType.INVISIBILITY);
	}

	public void applyLobbySettings() {
		this.setDisableDamage(true);
		this.setDisableHunger(true);
		this.setDisableItemMoving(true);
		this.setDisableSneaking(false);

		this.setGameMode(GameMode.ADVENTURE);
		this.setAllowFlight(false);

		this.player.setExp(0.0f);
		this.player.setLevel(0);

		this.heal();
		this.clearInventory();
		this.clearPotionEffects();
		this.giveLobbyInventoryItems();
	}

	public void addPoints(final int points) {
		Points.addPoints(this.player, points);
	}

	public int getPoints() {
		return Points.getPoints(this.player);
	}

	public void removeFire() {
		this.player.setFireTicks(0);
	}

	public void heal() {
		this.player.setHealth(this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
	}

	public void hideForEveryoneElse() {
		Bukkit.getOnlinePlayers().forEach((player2) -> player2.hidePlayer(Minigames.getInstance(), this.player));
	}

	public void setArmor(final ItemStack helmet, final ItemStack chestplate, final ItemStack leggings, final ItemStack boots){
		final PlayerInventory inv = this.player.getInventory();
		inv.setHelmet(helmet);
		inv.setChestplate(chestplate);
		inv.setLeggings(leggings);
		inv.setBoots(boots);
	}

	public void setArmor(Material helmet, Material chestplate, Material leggings, Material boots){
		if (helmet == null) {
			helmet = Material.AIR;
		}

		if (chestplate == null) {
			chestplate = Material.AIR;
		}

		if (leggings == null) {
			leggings = Material.AIR;
		}

		if (boots == null) {
			boots = Material.AIR;
		}

		this.setArmor(
				new ItemStack(helmet),
				new ItemStack(chestplate),
				new ItemStack(leggings),
				new ItemStack(boots));
	}

	public Block getBlockIn() {
		return this.player.getLocation().getBlock();
	}

	public Block getBlockOn() {
		return this.getBlockIn().getRelative(BlockFace.DOWN);
	}

	public void playSound(final Sound sound, final float pitch) {
		this.player.playSound(this.player.getLocation(), sound, 1, pitch);
	}

	public void showPlayer(final MPlayer target) {
		this.player.showPlayer(Minigames.getInstance(), target.bukkit());
	}

	public void hidePlayer(final MPlayer target) {
		this.player.hidePlayer(Minigames.getInstance(), target.bukkit());
	}

	public boolean isFlying() {
		return this.player.isFlying();
	}

	public void launch(final double upwardVelocity, final double multiplyInLookingDirection){
		this.player.setVelocity(this.player.getLocation().getDirection().multiply(multiplyInLookingDirection));
		this.player.setVelocity(new Vector(this.player.getVelocity().getX(), upwardVelocity, this.player.getVelocity().getZ()));
	}

	public void dropItems() {
		for (final ItemStack item : this.player.getInventory().getContents()) {
			this.dropItem(item);
		}

		for (final ItemStack item : this.player.getInventory().getArmorContents()) {
			this.dropItem(item);
		}

		for (final ItemStack item : this.player.getInventory().getExtraContents()) {
			this.dropItem(item);
		}
	}

	private void dropItem(final ItemStack item) {
		this.player.getLocation().getWorld().dropItemNaturally(this.player.getLocation(), item);
	}

	public void placeCage(final boolean cage) {
		final Block block = this.player.getLocation().getBlock();
		final Block[] blocks = new Block[] {
				block.getRelative(BlockFace.NORTH),
				block.getRelative(BlockFace.EAST),
				block.getRelative(BlockFace.SOUTH),
				block.getRelative(BlockFace.WEST),
				block.getRelative(BlockFace.UP).getRelative(BlockFace.NORTH),
				block.getRelative(BlockFace.UP).getRelative(BlockFace.EAST),
				block.getRelative(BlockFace.UP).getRelative(BlockFace.SOUTH),
				block.getRelative(BlockFace.UP).getRelative(BlockFace.WEST),
				block.getRelative(BlockFace.UP).getRelative(BlockFace.UP),
		};

		if (cage) {
			BlockUtils.replaceBlocks(Material.AIR, Material.GLASS, blocks);
		} else {
			BlockUtils.replaceBlocks(Material.GLASS, Material.AIR, blocks);
		}
	}

	public void sendActionBar(final String message) {
		this.sendActionBar(TextComponent.fromLegacyText(message));
	}

	public void sendActionBar(final BaseComponent[] components) {
		this.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, components);
	}

}
