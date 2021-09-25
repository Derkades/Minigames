package derkades.minigames.utils;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
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
import org.bukkit.entity.HumanEntity;
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

import derkades.minigames.Minigames;
import derkades.minigames.Points;
import derkades.minigames.Var;
import derkades.minigames.modules.SneakPrevention;
import derkades.minigames.utils.queue.TaskQueue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.ChatColor;
import xyz.derkades.derkutils.Colors;
import xyz.derkades.derkutils.bukkit.BlockUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.LocationUtils;

public class MPlayer {

	private static final String METADATA_PREFIX = "mg_";

	private final Player player;

	public MPlayer(final Player player) {
		this.player = player;
	}

	public MPlayer(final HumanEntity human) {
		this.player = (Player) human;
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

	public boolean hasDisabledDamage() {
		return this.getMetadataBool("disable_damage", true);
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
		return this.getMetadataBool("disable_item_moving", true);
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

	public BlockFace getFacingAsBlockFace() {
		return LocationUtils.getYawAsBlockFace(this.getYaw());
	}

	public boolean yawInBounds(final float min, final float max) {
		return LocationUtils.yawInBounds(this.getYaw(), min, max);
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
    	TaskQueue.add(() -> this.player.teleportAsync(location));
    }

    public void queueTeleport(final Location location, final Runnable callback) {
    	TaskQueue.add(() -> this.player.teleportAsync(location).thenRun(callback));
    }

//    private Location getRandomLobbyLocation() {
//    	final float f = ThreadLocalRandom.current().nextFloat();
//    	int x, z;
//    	if (f < 0.25f) {
//    		x = -3; z = 0;
//    	} else if (f < 0.5f) {
//    		x = 3; z = 0;
//    	} else if (f < 0.75f) {
//    		x = 0; z = -3;
//    	} else {
//    		x = 0; z = 3;
//    	}
//    	final float yaw = ThreadLocalRandom.current().nextFloat() * 360;
//    	return new Location(GameWorld.STEAMPUNK_LOBBY.getWorld(), x + 0.5, 69, z + 0.5, yaw, 0);
//    }

    private void afterTeleport() {
//		final double force = 0.3f;
//		final Vector vec = new Vector(force * ThreadLocalRandom.current().nextDouble(), 0, force * ThreadLocalRandom.current().nextDouble());
    	final Vector vec = new Vector(ThreadLocalRandom.current().nextDouble() - 0.5, 0.3, -0.8);
		Scheduler.delay(1, () -> this.player.setVelocity(vec));

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

//    	if (this.player.hasPermission("games.torch")) {
//			this.player.getInventory().setItem(7, new ItemBuilder(Material.REDSTONE_TORCH)
//					.name(ChatColor.AQUA + "" + ChatColor.BOLD + "Staff lounge key")
//					.lore(ChatColor.YELLOW + "Place in upper-south-east-corner on gray terracotta")
//					.canPlaceOn("cyan_terracotta")
//					.create());
//		}

		this.player.getInventory().setItem(8, new ItemBuilder(Material.COMPARATOR)
				.name(ChatColor.AQUA + "" + ChatColor.BOLD + "Menu")
				.lore(ChatColor.YELLOW + "Click to open menu")
				.create());
    }

//    public void teleportSteampunkLobby() {
//    	this.player.teleport(getRandomLobbyLocation());
//    	afterTeleport();
//    }
//
//    public void teleportSteampunkLobbyAsync() {
//    	queueTeleport(getRandomLobbyLocation(), this::afterTeleport);
//    }

    public void teleportLobby() {
    	this.player.teleport(Var.LOBBY_LOCATION);
    	afterTeleport();
    }

    public void teleportLobbyAsync() {
    	queueTeleport(Var.LOBBY_LOCATION, this::afterTeleport);
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
		this.sendPlainActionBar("You are now a specator. Use /spec <player> to spectate a player.");
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

	@Deprecated
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

	@Deprecated
	public void giveLobbyInventoryItems() {
//    	if (this.player.hasPermission("games.torch")) {
//			this.player.getInventory().setItem(7, new ItemBuilder(Material.REDSTONE_TORCH)
//					.name(ChatColor.AQUA + "" + ChatColor.BOLD + "Staff lounge key")
//					.lore(ChatColor.YELLOW + "Place in upper-south-east-corner on gray terracotta")
//					.canPlaceOn("cyan_terracotta")
//					.create());
//		}

		this.player.getInventory().setItem(8, new ItemBuilder(Material.COMPARATOR)
				.name(ChatColor.AQUA + "" + ChatColor.BOLD + "Menu")
				.lore(ChatColor.YELLOW + "Click to open menu")
				.create());
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
		this.removeFire();
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

	public void placeCage(final boolean cage, final Material material) {
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
			BlockUtils.replaceBlocks(Material.AIR, material, blocks);
		} else {
			BlockUtils.replaceBlocks(material, Material.AIR, blocks);
		}
	}

	public void placeCage(final boolean cage) {
		placeCage(cage, Material.GLASS);
	}

	public void sendFormattedPlainActionBar(final String message, final Object... replacements) {
		this.sendActionBar(Component.text(String.format(message, replacements), NamedTextColor.GRAY));
	}

	public void sendPlainActionBar(final String message) {
		this.sendActionBar(Component.text(message, NamedTextColor.GRAY));
	}

	public void sendActionBar(final Component message) {
		this.player.sendActionBar(message);
	}

	public void sendFormattedPlainChat(final String message, final Object... replacements) {
		this.sendChat(Component.text(String.format(message, replacements), NamedTextColor.GRAY));
	}

	public void sendPlainChat(final String message) {
		this.sendChat(Component.text(message, NamedTextColor.GRAY));
	}

	public void sendChat(final Component message) {
		this.player.sendMessage(message);
	}

	@Deprecated
	public void sendTitle(final String title, final String subtitle) {
		this.player.sendTitle(title, subtitle, 10, 70, 20);
	}

	public void sendTitle(final Title title) {
		this.player.showTitle(title);
	}

	public void sendTitle(final Component mainTitle, final Component subTitle) {
		this.player.showTitle(Title.title(mainTitle, subTitle));
	}

	public void sendPlainTitle(final String title, final String subtitle) {
		final Component a = title == null ? Component.empty() : Component.text(title, NamedTextColor.GRAY);
		final Component b = subtitle == null ? Component.empty() : Component.text(title, NamedTextColor.GRAY);
		this.sendTitle(a, b);
	}

	/**
	 * 0.0 - 0.5
	 * Lower value means more visible gradient
	 */
	private static final float GRADIENT_MARGIN = 0.2f;
	private static final String TAB_NAME_PADDING = "    ";

	private Component generateGradientName() {
		final int rgb1 = Colors.randomPastelColor().getRGB();
		final int rgb2 = Colors.randomPastelColor().getRGB();
		final TextComponent.Builder b = Component.text();
		float f = GRADIENT_MARGIN;
		final float f_step = (1.0f - 2*GRADIENT_MARGIN) / (this.player.getName().length() - 1);
		for (final char c : this.player.getName().toCharArray()) {
			final int r1 = (int) ((1-f) * (0xFF & rgb1));
			final int g1 = (int) ((1-f) * ((0xFF00 & rgb1) >> 8)) << 8;
			final int b1 = (int) ((1-f) * ((0xFF0000 & rgb1) >> 16)) << 16;
			final int r2 = (int) (f * (0xFF & rgb2));
			final int g2 = (int) (f * ((0xFF00 & rgb2) >> 8)) << 8;
			final int b2 = (int) (f * ((0xFF0000 & rgb2) >> 16)) << 16;
			b.append(Component.text(c, TextColor.color(r1 + g1 + b1 + r2 + g2 + b2)));
			f += f_step;
			if (f > 1.0f) {
				f = 1.0f;
			}
		}

		return b.asComponent();
	}

	public void setDisplayName(final Component displayName) {
		this.player.displayName(displayName == null ? generateGradientName() : displayName);
		this.player.playerListName(Component.text(TAB_NAME_PADDING).append(this.player.displayName()).append(Component.text(TAB_NAME_PADDING)));
	}

	public Component getDisplayName() {
		return this.player.displayName();
	}

}
