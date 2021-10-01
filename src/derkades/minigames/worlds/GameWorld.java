package derkades.minigames.worlds;

import derkades.minigames.Logger;
import derkades.minigames.Minigames;
import derkades.minigames.Minigames.ShutdownReason;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.jetbrains.annotations.Nullable;
import xyz.derkades.derkutils.bukkit.VoidGenerator;

import java.util.Arrays;

public enum GameWorld {

	// DO NOT CHANGE THESE VALUES!!

	STEAMPUNK_LOBBY,

	BOWSPLEEF_BOWSPLEEF,

	BTB_CAKE,
	BTB_HOLLOWHILLS,
	BTB_PROTOTYPE(false),
	BTB_JUNGLE(false),

	BUILDCOPY_PROTOTYPE,

	CONTROL_PROTOTYPE,

	CREEPERATTACK_HEDGES,
	CREEPERATTACK_MINESHAFT,
	CREEPERATTACK_DECKEDOUTFOREST,

	DECAY_SPRUCEBRICK,
	DECAY_SQUAREDONUT,

	DIGDUG_GROOVES(false),
	DIGDUG_PROTOTYPE,

	DROPPER_BLACKWHITE,
	DROPPER_RAINBOW,
	DROPPER_REDSTONE,
	DROPPER_TREES,

	ELYTRA_CAVE,
	ELYTRA_SPACE(false),

	FREEFALL_PROTOTYPE(false),

	HARVEST_PROTOTYPE,

	HG_TREEHOUSE,
	HG_WINDMILL,
	HG_MANSION,
	HG_NETHER,

	ICYBLOWBACK_ICYBLOWBACK,

	MISSILERACER_PROTOTYPE,

//	MGR_ISLAND(false),
	MGR_SNOW(false), // "Northwatch"
	MGR_SANTIAGO,

	MAZEPVP_PROTOTYPE,

	MOLEPVP_PROTOTYPE,

	MM_HAUNTEDHOUSE,
	MM_DECKEDOUT,

	OITQ_BARN,
	OITQ_CASTLE,
	OITQ_DESERT,
	OITQ_FARMHOUSE,
	OITQ_SNOW,

	PARKOUR_JUNGLE,
	PARKOUR_REDSTONECAVE,
	PARKOUR_SNOW,
	PARKOUR_TNT,

	PLATFORM_DESERT,
	PLATFORM_ICE,

	SPEEDRUN_BACKWARS(false), // TODO fix typo
	SPEEDRUN_CLASSIC,
	SPEEDRUN_CONSTRUCTION,

	SPLEEF_BIGSTADIUM,
	SPLEEF_LITTLESTADIUM,
	SPLEEF_ORIGINAL,

	TBB_FOREST,
//	TBB_MEDIEVALMOUNTAIN(false), // http://www.minecraftmaps.com/creation-maps/medieval-mountain

	TNTRUN_AQUA,
	TNTRUN_FUTURE,
	TNTRUN_JUNGLE,
	TNTRUN_WATERLAVA,

	TRON_PROTOTYPE,

	// Testing worlds, move up when assigned to game
	PICKLES_PROTOTYPE(false),
	MISSILES_PROTOTYPE(false),
	TAKECOVER_PROTOTYPE(false),

	// Reserved worlds
	RESERVED_BACKUP(false),
	RESERVED_TESTING(false),

	;

	/**
	 * Specifies whether a world should be loaded at startup
	 */
	private final boolean load;

	GameWorld() {
		this(true);
	}

	GameWorld(final boolean load) {
		this.load = load;
	}

	public String getName() {
		return "worlds/" + toString().toLowerCase();
	}

	public World getWorld() {
		World world = Bukkit.getWorld(getName());
		if (world == null) {
			Logger.warning("World '%s' was requested while it was not loaded yet.", this.name());
			world = load();
		}

		if (world == null) {
			Minigames.shutdown(ShutdownReason.EMERGENCY_AUTOMATIC, "World still null after loading it: " + this.name());
		}

		return world;
	}

	/**
	 * Creates world or just loads it if it already exists
	 */
	@Nullable
	public World load() {
//		Logger.debug("Loading world %s", toString());

		final WorldCreator creator = new WorldCreator(getName());
		creator.generateStructures(false);
		creator.type(WorldType.FLAT);
		creator.generator(new VoidGenerator());
		creator.environment(Environment.NORMAL);

		final World world = Bukkit.getServer().createWorld(creator);

		if (world == null) {
			return null;
		}

		world.setTime(6000);
		world.setDifficulty(Difficulty.NORMAL);
		world.setSpawnLocation(0, 65, 0);
		world.setKeepSpawnInMemory(false);

		world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false); 			// Whether advancements should be announced in chat
		world.setGameRule(GameRule.COMMAND_BLOCK_OUTPUT, false); 			// Whether command blocks should notify admins when they perform commands
		world.setGameRule(GameRule.DISABLE_ELYTRA_MOVEMENT_CHECK, true); 	// Whether the server should skip checking player speed when the player is wearing elytra. Often helps with jittering due to lag in multiplayer.
		world.setGameRule(GameRule.DISABLE_RAIDS, true); 					// Whether raids are disabled.
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false); 				// Whether the daylight cycle and moon phases progress
		world.setGameRule(GameRule.DO_ENTITY_DROPS, false); 				// Whether entities that are not mobs should have drops
		world.setGameRule(GameRule.DO_FIRE_TICK, false); 					// Whether fire should spread and naturally extinguish
		world.setGameRule(GameRule.DO_INSOMNIA, false); 					// Whether phantoms can spawn in the nighttime
		world.setGameRule(GameRule.DO_IMMEDIATE_RESPAWN, true); 			// Players respawn immediately without showing the death screen
		world.setGameRule(GameRule.DO_LIMITED_CRAFTING, false);			// Whether players should be able to craft only those recipes that they've unlocked first
		world.setGameRule(GameRule.DO_MOB_LOOT, false);					// Whether mobs should drop items
		world.setGameRule(GameRule.DO_MOB_SPAWNING, false);				// Whether mobs should naturally spawn. Does not affect monster spawners.
		world.setGameRule(GameRule.DO_PATROL_SPAWNING, false);				// Whether patrols can spawn
		world.setGameRule(GameRule.DO_TILE_DROPS, false);					// Whether blocks should have drops
		world.setGameRule(GameRule.DO_TRADER_SPAWNING, false);  			// Whether wandering traders can spawn
		world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);				// Whether the weather can change naturally.
		world.setGameRule(GameRule.DROWNING_DAMAGE, false);				// Whether the player should take damage when drowning
		world.setGameRule(GameRule.FALL_DAMAGE, true);						// Whether the player should take fall damage
		world.setGameRule(GameRule.FIRE_DAMAGE, true); 					// Whether the player should take damage in fire, lava, campfires, or on magma blocks‌
		world.setGameRule(GameRule.FORGIVE_DEAD_PLAYERS, true); 			// Makes angered neutral mobs stop being angry when the targeted player dies nearby
		world.setGameRule(GameRule.KEEP_INVENTORY, true);					// Whether the player should keep items and experience in their inventory after death
		world.setGameRule(GameRule.LOG_ADMIN_COMMANDS, true);				// Whether to log admin commands to server log
		world.setGameRule(GameRule.MAX_COMMAND_CHAIN_LENGTH, 50);			// Determines the number at which the chain command block acts as a "chain". (default 35536)
		world.setGameRule(GameRule.MAX_ENTITY_CRAMMING, 0);				// The maximum number of other pushable entities a mob or player can push, before taking 3 suffocation damage per half-second. Setting to 0 or lower disables the rule. Damage affects survival-mode or adventure-mode players, and all mobs but bats. Pushable entities include non-spectator-mode players, any mob except bats, as well as boats and minecarts.
		world.setGameRule(GameRule.MOB_GRIEFING, false);					// Whether creepers, zombies, endermen, ghasts, withers, ender dragons, rabbits, sheep, villagers, silverfish, snow golems, and end crystals should be able to change blocks and whether mobs can pick up items, which also disables bartering. This also affects the capability of zombie-like creatures like zombie pigmen and drowned to pathfind to turtle eggs.
		world.setGameRule(GameRule.NATURAL_REGENERATION, true); 			// Whether the player can regenerate health naturally if their hunger is full enough (doesn't affect external healing, such as golden apples, the Regeneration effect, etc.)
		world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);					// How often a random block tick occurs (such as plant growth, leaf decay, etc.) per chunk section per game tick. 0 disables random ticks [needs testing], higher numbers increase random ticks. Setting to a high integer results in high speeds of decay and growth
		world.setGameRule(GameRule.REDUCED_DEBUG_INFO, false);				// Whether the debug screen shows all or reduced information; and whether the effects of F3 + B (entity hitboxes) and F3 + G (chunk boundaries) are shown.
		world.setGameRule(GameRule.SEND_COMMAND_FEEDBACK, true);			// Whether the feedback from commands executed by a player should show up in chat. Also affects the default behavior of whether command blocks store their output text
		world.setGameRule(GameRule.SHOW_DEATH_MESSAGES, false);			// Whether death messages are put into chat when a player dies. Also affects whether a message is sent to the pet's owner when the pet dies.
		world.setGameRule(GameRule.SPAWN_RADIUS, 0);						// The number of blocks outward from the world spawn coordinates that a player spawns in when first joining a server or when dying without a personal spawnpoint.
		world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);		// Whether players in spectator mode can generate chunks
		world.setGameRule(GameRule.UNIVERSAL_ANGER, true);					// Makes angered neutral mobs attack any nearby player, not just the player that angered them. Works best if forgiveDeadPlayers is disabled.

		return world;
	}

	public void unload() {
		final World world = Bukkit.getWorld(getName());
		if (world == null) {
			Logger.debug("World %s was already unloaded", this.name());
		} else {
			final boolean success = Bukkit.unloadWorld(getWorld(), true);
			Logger.debug(success ? "Unloaded world %s" : "Couldn't unload world %s", this.name());
		}
	}

	/**
	 * Called in onEnable()
	 */
	public static void loadWorlds() {
//		Arrays.stream(GameWorld.values()).filter(w -> w.load).forEach(world -> TaskQueue.add(() -> world.load()));
		final long start = System.nanoTime();
		Arrays.stream(GameWorld.values()).filter(w -> w.load).forEach(GameWorld::load);
		Logger.debug("Loaded worlds, took %.2fms", (System.nanoTime() - start) / 1_000_000d);
	}

}
