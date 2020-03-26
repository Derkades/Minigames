package xyz.derkades.minigames.worlds;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import xyz.derkades.derkutils.bukkit.VoidGenerator;
import xyz.derkades.minigames.Logger;
import xyz.derkades.minigames.Var;

public enum GameWorld {

	// DO NOT CHANGE THESE VALUES!!

	BOWSPLEEF_BOWSPLEEF,

	BTB_PROTOTYPE,
	BTB_JUNGLE,
	
	CONTROL_PROTOTYPE,

	CREEPERATTACK_HEDGES,
	CREEPERATTACK_MINESHAFT,
	
	DECAY_SPRUCEBRICK,
	DECAY_SQUAREDONUT,

	DIGDUG_GROOVES,
	DIGDUG_PROTOTYPE,

	DROPPER_BLACKWHITE,
	DROPPER_RAINBOW,
	DROPPER_REDSTONE,
	DROPPER_TREES,

	ELYTRA_CAVE,
	ELYTRA_SPACE,

	FREEFALL_PROTOTYPE,

	HG_TREEHOUSE,
	HG_WINDMILL,
	HG_MANSION,
	HG_NETHER,

	ICYBLOWBACK_ICYBLOWBACK,

	MGR_ISLAND,
	MGR_SANTIAGO,
	
	MOLEPVP_PROTOTYPE,

	MM_HAUNTEDHOUSE,

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

	SPEEDRUN_BACKWARS,
	SPEEDRUN_CLASSIC,
	SPEEDRUN_CONSTRUCTION,

	SPLEEF_BIGSTADIUM,
	SPLEEF_LITTLESTADIUM,
	SPLEEF_ORIGINAL,

	TBB_FOREST,
	TBB_MEDIEVALMOUNTAIN, // http://www.minecraftmaps.com/creation-maps/medieval-mountain

	TNTRUN_AQUA,
	TNTRUN_FUTURE,
	TNTRUN_JUNGLE,
	TNTRUN_WATERLAVA,

	TRON_PROTOTYPE,

	;

	private GameWorld(){}

	public String getName() {
		return "worlds/" + toString().toLowerCase();
	}

	public World getWorld() {
		World world = Bukkit.getWorld(getName());
		if (world == null) {
			world = load();
		}

		return world;
	}

	/**
	 * Creates world or just loads it if it already exists
	 */
	public World load() {
		Logger.debug("Loading %s", toString());

		final WorldCreator creator = new WorldCreator(getName());
		creator.generateStructures(false);
		creator.type(WorldType.FLAT);
		creator.generator(new VoidGenerator());
		creator.environment(Environment.NORMAL);

		final World world = Bukkit.getServer().createWorld(creator);

		world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
		world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
		world.setTime(6000);

		world.setGameRule(GameRule.SPECTATORS_GENERATE_CHUNKS, false);
		world.setGameRule(GameRule.SPAWN_RADIUS, 0);
		world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
		world.setGameRule(GameRule.DO_FIRE_TICK, false);
		world.setGameRule(GameRule.RANDOM_TICK_SPEED, 0);
		world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
		world.setGameRule(GameRule.DO_ENTITY_DROPS, false);
		world.setGameRule(GameRule.MAX_ENTITY_CRAMMING, 0);
		world.setGameRule(GameRule.DO_MOB_LOOT, false);
		world.setGameRule(GameRule.DO_TILE_DROPS, false);
		world.setGameRule(GameRule.KEEP_INVENTORY, true);
		world.setGameRule(GameRule.MOB_GRIEFING, false);

		world.setDifficulty(Difficulty.NORMAL);

		world.setSpawnLocation(0, 65, 0);

		return world;
	}

	public void unload() {
		final World world = Bukkit.getWorld(getName());
		if (world == null) {
			Logger.debug("%s is already unloaded", toString());
		} else {
			Logger.debug("Unloading %s", toString());
			Bukkit.unloadWorld(getWorld(), true);
		}
	}

	public static void init() {
		Bukkit.getOnlinePlayers().stream().filter((p) -> Arrays.asList(GameWorld.values()).stream().map(GameWorld::getName).collect(Collectors.toList()).contains(p.getWorld().getName())).forEach((p) -> p.teleport(Var.LOBBY_LOCATION));
	}

}
