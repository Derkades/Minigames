package derkades.minigames.games;

import derkades.minigames.games.bowspleef.BowSpleef;
import derkades.minigames.games.breaktheblock.BreakTheBlock;
import derkades.minigames.games.buildcopy.BuildCopy;
import derkades.minigames.games.controlpoints.ControlPoints;
import derkades.minigames.games.creeperattack.CreeperAttack;
import derkades.minigames.games.decay.Decay;
import derkades.minigames.games.digdug.DigDug;
import derkades.minigames.games.dropper.Dropper;
import derkades.minigames.games.elytra.Elytra;
import derkades.minigames.games.gladeroyale.GladeRoyale;
import derkades.minigames.games.harvest.Harvest;
import derkades.minigames.games.hungergames.HungerGames;
import derkades.minigames.games.icyblowback.IcyBlowback;
import derkades.minigames.games.missile.racer.MissileRacer;
import derkades.minigames.games.molepvp.MolePvP;
import derkades.minigames.games.murderymister.MurderyMister;
import derkades.minigames.games.oitq.OneInTheQuiver;
import derkades.minigames.games.parkour.Parkour;
import derkades.minigames.games.platform.Platform;
import derkades.minigames.games.speedrun.Speedrun;
import derkades.minigames.games.spleef.RegeneratingSpleef;
import derkades.minigames.games.teamsbowbattle.TeamsBowBattle;
import derkades.minigames.games.tntrun.TntRun;
import derkades.minigames.games.tron.Tron;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Games {

	private static final Map<String, Game<? extends GameMap>> GAME_BY_NAME = new HashMap<>();
	private static final Map<String, GameMap> MAP_BY_IDENTIFIER = new HashMap<>();
	private static final Map<GameMap, Game<? extends GameMap>> GAME_BY_MAP = new HashMap<>();

	public static final Game<? extends GameMap>[] GAMES = new Game<?>[]{
			new BowSpleef(),
			new BreakTheBlock(),
			new BuildCopy(),
			new CreeperAttack(),
			new Decay(),
			new DigDug(),
			new Dropper(),
			new Elytra(),
			new Harvest(),
			new HungerGames(),
			new IcyBlowback(),
			new GladeRoyale(),
//			new MazePvp(),
			new MissileRacer(),
//			new MissileWars(),
			new MolePvP(),
			new MurderyMister(),
			new OneInTheQuiver(),
			new Platform(),
			new ControlPoints(),
			new RegeneratingSpleef(),
			new Parkour(),
//			new SnowFight(),
			new Speedrun(),
			new TeamsBowBattle(),
			new TntRun(),
//			new TntTag(),
			new Tron(),
	};

	static {
		for (final Game<?> game : GAMES) {
			Objects.requireNonNull(game.getIdentifier(), "game identifier null: " + game.getClass().getName());
			Objects.requireNonNull(game.getName(), "game name null: " + game.getClass().getName());
			GAME_BY_NAME.put(game.getIdentifier(), game);
			GAME_BY_NAME.put(game.getName().toLowerCase(Locale.ROOT), game);
			final GameMap[] maps = game.getGameMaps();
			Objects.requireNonNull(maps, "maps array null: " + game.getClass().getName());
			for (final GameMap map : maps) {
				Objects.requireNonNull(map.getIdentifier(), "map identifier null: " + game.getClass().getName() + " " + map.getClass().getName());
				Objects.requireNonNull(map.getName(), "map name is null: " + map.getIdentifier());
				MAP_BY_IDENTIFIER.put(map.getIdentifier(), map);
				GAME_BY_MAP.put(map, game);
			}
		}
	}

	/**
	 * Find game by name, alias or identifier
	 * @param name Game name or identifier
	 * @return Game, or null if not found
	 */
	@Nullable
	public static Game<? extends GameMap> getGame(@NotNull final String name) {
		return GAME_BY_NAME.get(name.toLowerCase(Locale.ROOT));
	}

	@Nullable
	public static Game<? extends GameMap> getGameForMap(GameMap map) {
		return GAME_BY_MAP.get(map);
	}

	@Nullable
	public static GameMap getMapByIdentifier(String identifier) {
		return MAP_BY_IDENTIFIER.get(identifier);
	}

}
