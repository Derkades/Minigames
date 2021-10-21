package derkades.minigames.utils;

import derkades.minigames.Minigames;
import derkades.minigames.Points;
import derkades.minigames.SpecialCharacter;
import derkades.minigames.Var;
import derkades.minigames.modules.LobbyEffects;
import derkades.minigames.modules.SneakPrevention;
import derkades.minigames.utils.queue.TaskQueue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.Title.Times;
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
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.derkades.derkutils.bukkit.AdventureUtil;
import xyz.derkades.derkutils.bukkit.BlockUtils;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.LocationUtils;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.TextDecoration.BOLD;

public class MPlayer {

	private static final String METADATA_PREFIX = "mg_";

	@NotNull
	private final Player player;

	public MPlayer(@NotNull final Player player) {
		this.player = player;
	}

	public MPlayer(@NotNull final HumanEntity human) {
		this.player = (Player) human;
	}

	public MPlayer(@NotNull final PlayerEvent event) {
		this.player = event.getPlayer();
	}

	public MPlayer(@NotNull final EntityEvent event) {
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

	public @NotNull Player bukkit() {
		return this.player;
	}

	public Player.@NotNull Spigot spigot(){
		return this.player.spigot();
	}

	public void teleport(@NotNull final Location location) {
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

	@NotNull
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

	@NotNull
	@Deprecated
	public String getName() {
		return this.player.getName();
	}

	@NotNull
	public String getOriginalName() {
		return player.getName();
	}

	@NotNull
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

	private static final int TITLE_FADE_TICKS = 5;
	@SuppressWarnings("null")
	@NotNull
	private static final Title TITLE_FADE_OUT = Title.title(text(SpecialCharacter.BLACK_BOX), Component.empty(),
			Times.of(Duration.ofMillis(TITLE_FADE_TICKS * 50), Duration.ofMillis(100), Duration.ofMillis(0)));
	@SuppressWarnings("null")
	@NotNull
	private static final Title TITLE_BLACK = Title.title(text(SpecialCharacter.BLACK_BOX), Component.empty(),
			Times.of(Duration.ofMillis(0), Duration.ofMillis(100), Duration.ofMillis(0)));
	@SuppressWarnings("null")
	@NotNull
	private static final Title TITLE_FADE_IN = Title.title(text(SpecialCharacter.BLACK_BOX), Component.empty(),
			Times.of(Duration.ofMillis(0), Duration.ofMillis(0), Duration.ofMillis(TITLE_FADE_TICKS * 50)));

	public void queueTeleport(@NotNull final Location location) {
		queueTeleport(location, null);
	}

	public void queueTeleport(@NotNull final Location location, @Nullable final Runnable callback) {
		this.sendTitle(TITLE_FADE_OUT);

		// Wait for fade-out to complete before teleporting
		Scheduler.delay(TITLE_FADE_TICKS, () -> {
			// Refresh black screen every tick (50ms)
			final BukkitTask task = Scheduler.repeat(1, () -> this.sendTitle(TITLE_BLACK));

			TaskQueue.add(() -> this.player.teleportAsync(location).thenRun(() -> {
				task.cancel();
				this.sendTitle(TITLE_FADE_IN);
				if (callback != null) {
					callback.run();
				}
			}));
		});
	}

	// Used on join
	public void queueTeleportNoFadeIn(@NotNull final Location location) {
		queueTeleportNoFadeOut(location, () -> {});
	}

	// Used on join
	public void queueTeleportNoFadeOut(@NotNull final Location location, @Nullable final Runnable callback) {
		// Refresh black screen every tick (50ms)
		final BukkitTask task = Scheduler.repeat(1, () -> this.sendTitle(TITLE_BLACK));

		TaskQueue.add(() -> this.player.teleportAsync(location).thenRun(() -> {
			task.cancel();
			this.sendTitle(TITLE_FADE_IN);
			if (callback != null) {
				callback.run();
			}
		}));
	}

//	private Location getRandomLobbyLocation() {
//		final float f = ThreadLocalRandom.current().nextFloat();
//		int x, z;
//		if (f < 0.25f) {
//			x = -3;
//			z = 0;
//		} else if (f < 0.5f) {
//			x = 3;
//			z = 0;
//		} else if (f < 0.75f) {
//			x = 0;
//			z = -3;
//		} else {
//			x = 0;
//			z = 3;
//		}
//		final float yaw = ThreadLocalRandom.current().nextFloat() * 360;
//		return new Location(GameWorld.STEAMPUNK_LOBBY.getWorld(), x + 0.5, 69, z + 0.5, yaw, 0);
//	}

    public void afterLobbyTeleport() {
//		final double force = 0.3f;
//		final Vector vec = new Vector(force * ThreadLocalRandom.current().nextDouble(), 0, force * ThreadLocalRandom.current().nextDouble());
    	final Vector vec = new Vector(ThreadLocalRandom.current().nextDouble() - 0.5, 0.3, -0.8);
		Scheduler.delay(1, () -> this.player.setVelocity(vec));

		this.setDisableHunger(true);
		this.setDisableItemMoving(true);
		this.disableSneakPrevention();
		this.removeMetadata(LobbyEffects.META_LOBBY_WATER_TELEPORTING);

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
				.name(text("Menu", AQUA).decorate(BOLD))
				.lore(text("Click to open menu", YELLOW))
				.create());
    }

	public void queueLobbyTeleport() {
		queueTeleport(Var.LOBBY_LOCATION, this::afterLobbyTeleport);
	}

	public void queueLobbyTeleport(final Runnable afterTeleport) {
		queueTeleport(Var.LOBBY_LOCATION, () -> {
			this.afterLobbyTeleport();
			afterTeleport.run();
		});
	}

    // Used on join
	void teleportLobbyNoFadeIn() {
		queueTeleportNoFadeOut(Var.LOBBY_LOCATION, this::afterLobbyTeleport);
	}

	public void setAllowFlight(final boolean allowFlight) {
		this.player.setAllowFlight(allowFlight);
	}

	public boolean getAllowFlight() {
		return this.player.getAllowFlight();
	}

	public void setGameMode(@NotNull final GameMode gameMode) {
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

	public void giveItem(@NotNull final ItemStack... itemStacks) {
		this.player.getInventory().addItem(itemStacks);
	}

	public void teleportUp(final int yUp) {
		final Location loc = this.player.getLocation();
		loc.setY(loc.getY() + yUp);
		this.player.teleport(loc);
	}

	public void dieTo(@NotNull final Location location) {
		this.player.teleport(location);
		die();
	}

	public void dieUp(final int yUp) {
		teleportUp(yUp);
		die();
	}

	public void finishTo(@NotNull final Location location) {
		this.player.teleport(location);
		finish();
	}

	public void finishUp(final int yUp) {
		teleportUp(yUp);
		finish();
	}

	@NotNull
	private static final Title DIE_TITLE = Title.title(Component.empty(), text("You've died", NamedTextColor.RED));

	public void die() {
		this.sendTitle(DIE_TITLE);
		this.spectator();
	}

	@NotNull
	private static final Title FINISH_TITLE = Title.title(Component.empty(), text("You've finished", NamedTextColor.GREEN));

	public void finish() {
		this.sendTitle(FINISH_TITLE);
		this.spectator();
	}

	public void spectator() {
		this.player.setGameMode(GameMode.SPECTATOR);
		this.disableSneakPrevention();
		this.clearInventory();
		this.sendPlainActionBar("You are now a spectator. Use /spec <player> to spectate a player.");
	}

	public boolean isSpectator() {
		return getGameMode().equals(GameMode.SPECTATOR);
	}

	public void giveEffect(@NotNull final PotionEffect effect) {
		this.player.addPotionEffect(effect);
	}

	@Deprecated
	public void giveEffect(@NotNull final PotionEffectType type, final int duration, final int amplifier) {
		this.player.addPotionEffect(new PotionEffect(type, duration * 20, amplifier, true, false));
	}

	@Deprecated
	public void giveInfiniteEffect(@NotNull final PotionEffectType type, final int amplifier){
		this.player.addPotionEffect(new PotionEffect(type, 100000, amplifier, true, false));
	}

	@Deprecated
	public void giveInfiniteEffect(@NotNull final PotionEffectType type){
		this.player.addPotionEffect(new PotionEffect(type, 100000, 0, true, false));
	}

	@SuppressWarnings("null")
	@Deprecated
	public void giveInvisibility(){
		this.giveInfiniteEffect(PotionEffectType.INVISIBILITY);
	}

	public void addPoints(final int points) {
		// TODO call an "add points" event that the leaderboard code can listen for
		Points.addPoints(this.player, points);
	}

	public int getPoints() {
		return Points.getPoints(this.player);
	}

	public void removeFire() {
		this.player.setFireTicks(0);
	}

	@SuppressWarnings("null")
	public void heal() {
		this.removeFire();
		this.player.setHealth(this.player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
	}

	@SuppressWarnings("null")
	public void hideForEveryoneElse() {
		Bukkit.getOnlinePlayers().forEach((player2) -> player2.hidePlayer(Minigames.getInstance(), this.player));
	}

	public void setArmor(final ItemStack helmet, final ItemStack chestplate, final ItemStack leggings, final ItemStack boots) {
		final PlayerInventory inv = this.player.getInventory();
		inv.setHelmet(helmet);
		inv.setChestplate(chestplate);
		inv.setLeggings(leggings);
		inv.setBoots(boots);
	}

	public void setArmor(final Material helmet, final Material chestplate, final Material leggings, final Material boots) {
		this.setArmor(
				helmet != null ? new ItemStack(helmet) : null,
				chestplate != null ? new ItemStack(chestplate) : null,
				leggings != null ? new ItemStack(leggings) : null,
				boots != null ? new ItemStack(boots) : null
				);
	}

	public Block getBlockIn() {
		return this.player.getLocation().getBlock();
	}

	public Block getBlockOn() {
		return getBlockIn().getRelative(BlockFace.DOWN);
	}

	public void playSound(@NotNull final Sound sound, final float pitch) {
		this.player.playSound(this.player.getLocation(), sound, 1, pitch);
	}

	public void playSound(final net.kyori.adventure.sound.@NotNull Sound sound) {
		this.player.playSound(sound);
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

	@SuppressWarnings("null")
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

	private void dropItem(@NotNull final ItemStack item) {
		this.player.getLocation().getWorld().dropItemNaturally(this.player.getLocation(), item);
	}

	public void placeCage(final boolean cage, @NotNull final Material material) {
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

	@SuppressWarnings("null")
	public void sendFormattedPlainActionBar(@NotNull final String message, @NotNull final Object... replacements) {
		this.sendActionBar(text(String.format(message, replacements), NamedTextColor.GRAY));
	}

	public void sendPlainActionBar(@NotNull final String message) {
		this.sendActionBar(text(message, NamedTextColor.GRAY));
	}

	public void sendActionBar(@NotNull final Component message) {
		this.player.sendActionBar(message);
	}

	@SuppressWarnings("null")
	public void sendFormattedPlainChat(@NotNull final String message, @NotNull final Object... replacements) {
		this.sendChat(text(String.format(message, replacements), NamedTextColor.GRAY));
	}

	public void sendPlainChat(@NotNull final String message) {
		this.sendChat(text(message, NamedTextColor.GRAY));
	}

	public void sendChat(@NotNull final Component message) {
		this.player.sendMessage(message);
	}

	@Deprecated
	public void sendTitle(final String title, final String subtitle) {
		this.player.sendTitle(title, subtitle, 10, 70, 20);
	}

	public void sendTitle(@NotNull final Title title) {
		this.player.showTitle(title);
	}

	public void sendTitle(@NotNull final Component mainTitle, @NotNull final Component subTitle) {
		this.player.showTitle(Title.title(mainTitle, subTitle));
	}

	public void sendPlainTitle(@Nullable final String title, @Nullable final String subtitle) {
		final Component a = title == null ? Component.empty() : text(title, NamedTextColor.GRAY);
		final Component b = subtitle == null ? Component.empty() : text(subtitle, NamedTextColor.GRAY);
		this.sendTitle(a, b);
	}

	private Component generateGradientName() {
		return AdventureUtil.gradient(this.getOriginalName(), 0.15f);
	}

	private static final String TAB_NAME_PADDING = "    ";

	public void setDisplayName(final Component displayName) {
		this.player.displayName(displayName == null ? generateGradientName() : displayName);
		this.player.playerListName(text(TAB_NAME_PADDING).append(this.player.displayName()).append(text(TAB_NAME_PADDING)));
	}

	@NotNull
	public Component getDisplayName() {
		return this.player.displayName();
	}

}
