package xyz.derkades.minigames.utils;

import java.util.UUID;
import java.util.function.Consumer;

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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import xyz.derkades.derkutils.bukkit.BlockUtils;
import xyz.derkades.derkutils.bukkit.LocationUtils;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.Points;
import xyz.derkades.minigames.utils.queue.TaskQueue;

public class MPlayer {
	
	private static final String METADATA_PREFIX = "mg_";

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
	
	public void setMetadata(final String key, final Object value) {
		this.removeMetadata(key);
		this.player.setMetadata(METADATA_PREFIX + key, new FixedMetadataValue(Minigames.getInstance(), value));
	}
	
	public void removeMetadata(final String key) {
		this.player.removeMetadata(METADATA_PREFIX + key, Minigames.getInstance());
	}
	
	public MetadataValue getMetadata(final String key) {
		return this.player.getMetadata(METADATA_PREFIX + key).get(0);
	}
	
	public boolean hasMetadata(final String key) {
		return this.player.hasMetadata(METADATA_PREFIX + key);
	}
	
	public boolean getMetadataBool(final String key) {
		return this.getMetadata(key).asBoolean();
	}
	
	public boolean getMetadataBool(final String key, final boolean def) {
		if (this.hasMetadata(key)) {
			return this.getMetadataBool(key);
		} else {
			return def;
		}
	}
	
	public void setDisableDamage(final boolean disableDamage) {
		this.setMetadata("disable_damage", disableDamage);
	}

	public boolean getDisableDamage() {
		return this.getMetadataBool("disable_damage");
	}

	public void setDisableHunger(final boolean disableHunger) {
		this.setMetadata("disable_hunger", disableHunger);
	}

	public boolean getDisableHunger() {
		return this.getMetadataBool("disable_hunger", true);
	}

	public void setDisableItemMoving(final boolean disableItemMoving) {
		this.setMetadata("disable_item_moving", disableItemMoving);
	}

	public boolean getDisableItemMoving() {
		return this.getMetadataBool("disable_item_moving");
	}
	
	public void enableSneakPrevention(final Consumer<MPlayer> onPunish) {
		SneakPrevention.enable(this, onPunish);
	}
	
	public void disableSneakPrevention() {
		SneakPrevention.disable(this);
	}
	
	public boolean sneakPreventionEnabled() {
		return SneakPrevention.isEnabled(this);
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
	
	public boolean yawInBounds(final float min, final float max) {
		if (min < 0) {
			if (max == 0) {
				return getYaw() > min && getYaw() < 0 ||
						getYaw() > 360 + min && getYaw() < 360;
			} else {
				return yawInBounds(min, 0) || yawInBounds(0, max);
			}
		} else {
			return getYaw() > min && getYaw() < max;
		}
	}

	public boolean isIn2dBounds(final Location cornerOne, final Location cornerTwo) {
		return LocationUtils.isIn2dBounds(this.player.getLocation(), cornerOne, cornerTwo);
	}

	public boolean isIn2dBounds(final World world, final double x1, final double z1, final double x2, final double z2) {
		return this.isIn2dBounds(new Location(world, x1, 0, z1), new Location(world, x2, 0, z2));
	}
	
	public boolean isIn3dBounds(final Location cornerOne, final Location cornerTwo) {
		return LocationUtils.isIn3dBounds(this.player.getLocation(), cornerOne, cornerTwo);
	}
	
	public boolean isIn3dBounds(final World world, final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
		return this.isIn3dBounds(new Location(world, x1, y1, z1), new Location(world, x2, y2, z2));
	}

	public Location getLocation() {
		return this.player.getLocation();
	}

	public double getX() {
		return getLocation().getX();
	}

	public double getY() {
		return getLocation().getY();
	}

	public double getZ() {
		return getLocation().getZ();
	}
	
	public float getYaw() {
		return getLocation().getYaw();
	}
	
	public float getPitch() {
		return getLocation().getPitch();
	}

	public int getBlockX() {
		return getLocation().getBlockX();
	}

	public int getBlockY() {
		return getLocation().getBlockY();
	}

	public int getBlockZ() {
		return getLocation().getBlockZ();
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
    	TaskQueue.add(() -> this.player.teleport(location));
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
		die();
	}

	public void dieUp(final int yUp) {
		teleportUp(yUp);
		die();
	}

	public void finishTo(final Location location) {
		this.player.teleport(location);
		finish();
	}

	public void finishUp(final int yUp) {
		teleportUp(yUp);
		finish();
	}

	public void die() {
		sendTitle( "", ChatColor.RED + "You've died");
		spectator();
	}

	public void finish() {
		sendTitle("", ChatColor.GREEN + "You've finished");
		spectator();
	}

	public void spectator() {
		this.player.setGameMode(GameMode.SPECTATOR);
		this.disableSneakPrevention();
		this.sendActionBar(new ComponentBuilder("You are now a specator. Use /spec <player> to spectate a player.")
				.color(ChatColor.GRAY).create());
	}

	public boolean isSpectator() {
		return getGameMode().equals(GameMode.SPECTATOR);
	}
	
	public void giveEffect(final PotionEffect effect) {
		this.player.addPotionEffect(effect);
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
		this.disableSneakPrevention();

		this.setGameMode(GameMode.ADVENTURE);
		this.setAllowFlight(false);

		this.player.setExp(0.0f);
		this.player.setLevel(0);

		this.heal();
		this.clearInventory();
		this.clearPotionEffects();
		this.giveLobbyInventoryItems();
	}

	public void giveLobbyInventoryItems() {
//    	if (this.player.hasPermission("games.torch")) {
//			this.player.getInventory().setItem(7, new ItemBuilder(Material.REDSTONE_TORCH)
//					.name(ChatColor.AQUA + "" + ChatColor.BOLD + "Staff lounge key")
//					.lore(ChatColor.YELLOW + "Place in upper-south-east-corner on gray terracotta")
//					.canPlaceOn("cyan_terracotta")
//					.create());
//		}

//		this.player.getInventory().setItem(8, new ItemBuilder(Material.COMPARATOR)
//				.name(ChatColor.AQUA + "" + ChatColor.BOLD + "Menu")
//				.lore(ChatColor.YELLOW + "Click to open menu")
//				.create());
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
		return getBlockIn().getRelative(BlockFace.DOWN);
	}
	
	public BlockFace getFacingAsBlockFace() {
		float yaw = this.getLocation().getYaw();
		if (yaw < 0) {
			yaw += 360;
		}
		if (yaw < 45 || yaw >= 315) { // 0, 360
			return BlockFace.SOUTH; // +Z
		} else if (yaw >= 45 && yaw < 135) { // 90
			return BlockFace.WEST; // -X
		} else if (yaw >= 135 && yaw < 225) { // 180
			return BlockFace.NORTH; // -Z
		} else if (yaw >= 225 && yaw < 315) { // 270
			return BlockFace.EAST; // +X
		} else {
			throw new IllegalStateException("Impossible yaw: " + yaw);
		}
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
			if (item != null) {
				dropItem(item);
			}
		}

		for (final ItemStack item : this.player.getInventory().getArmorContents()) {
			if (item != null) {
				dropItem(item);
			}
		}

		for (final ItemStack item : this.player.getInventory().getExtraContents()) {
			if (item != null) {
				dropItem(item);
			}
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

	public void sendActionBar(final BaseComponent... components) {
		this.player.spigot().sendMessage(ChatMessageType.ACTION_BAR, components);
	}

	public void sendChat(final String message) {
		this.player.sendMessage(message);
	}

	public void sendChat(final BaseComponent... components) {
		this.player.spigot().sendMessage(components);
	}

}
