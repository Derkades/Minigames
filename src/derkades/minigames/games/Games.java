package derkades.minigames.games;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import derkades.minigames.Logger;
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
import derkades.minigames.games.meteorshower.MeteorShower;
import derkades.minigames.games.missile.racer.MissileRacer;
import derkades.minigames.games.molepvp.MolePvP;
import derkades.minigames.games.murderymister.MurderyMister;
import derkades.minigames.games.oitq.OneInTheQuiver;
import derkades.minigames.games.parkour.Parkour;
import derkades.minigames.games.platform.Platform;
import derkades.minigames.games.speedrun.Speedrun;
import derkades.minigames.games.spleef.RegeneratingSpleef;
import derkades.minigames.games.studyfall.StudyFall;
import derkades.minigames.games.teamsbowbattle.TeamsBowBattle;
import derkades.minigames.games.tntrun.TntRun;
import derkades.minigames.games.tron.Tron;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Games {

	private static final Map<String, Game<? extends GameMap>> NAME_TO_GAME = new HashMap<>();
	private static final Map<String, GameMap> IDENTIFIER_TO_MAP = new HashMap<>();
	private static final Map<GameMap, Game<? extends GameMap>> MAP_TO_GAME = new HashMap<>();
	private static final Multimap<GameLabel, Game<? extends GameMap>> GAMES_BY_LABEL = MultimapBuilder.enumKeys(GameLabel.class).hashSetValues().build();

	public static final Game<? extends GameMap>[] GAMES = new Game<?>[]{
			new BowSpleef(),
			new BreakTheBlock(),
			new BuildCopy(),
			new CreeperAttack(),
			new ControlPoints(),
			new Decay(),
			new DigDug(),
			new Dropper(),
			new Elytra(),
			new GladeRoyale(),
			new Harvest(),
			new HungerGames(),
			new IcyBlowback(),
			new MeteorShower(),
			new MissileRacer(),
//			new MissileWars(),
//			new MazePvp(),
			new MolePvP(),
			new MurderyMister(),
			new OneInTheQuiver(),
			new Parkour(),
			new Platform(),
			new Speedrun(),
			new RegeneratingSpleef(),
			new StudyFall(),
//			new SnowFight(),
			new TeamsBowBattle(),
			new TntRun(),
//			new TntTag(),
			new Tron(),
	};

	static {
		for (final Game<?> game : GAMES) {
			Objects.requireNonNull(game.getIdentifier(), "game identifier null: " + game.getClass().getName());
			Objects.requireNonNull(game.getName(), "game name null: " + game.getClass().getName());
			NAME_TO_GAME.put(game.getIdentifier(), game);
			NAME_TO_GAME.put(game.getName().toLowerCase(Locale.ROOT), game);

			final GameMap[] maps = game.getGameMaps();
			Objects.requireNonNull(maps, "maps array null: " + game.getClass().getName());
			for (final GameMap map : maps) {
				Objects.requireNonNull(map.getIdentifier(), "map identifier null: " + game.getClass().getName() + " " + map.getClass().getName());
				Objects.requireNonNull(map.getName(), "map name is null: " + map.getIdentifier());
				IDENTIFIER_TO_MAP.put(map.getIdentifier(), map);
				MAP_TO_GAME.put(map, game);
			}

			Set<GameLabel> labels = game.getGameLabels();
			if (!labels.contains(GameLabel.MULTIPLAYER) && !labels.contains(GameLabel.SINGLEPLAYER)) {
				Logger.warning("%s missing label multiplayer/singleplayer", game);
			}
			if (!labels.contains(GameLabel.TEAMS) && !labels.contains(GameLabel.NO_TEAMS)) {
				Logger.warning("%s missing label teams/noteams", game);
			}
			labels.forEach(label -> GAMES_BY_LABEL.put(label, game));
		}
	}

	/**
	 * Find game by name, alias or identifier
	 * @param name Game name or identifier
	 * @return Game, or null if not found
	 */
	@Nullable
	public static Game<? extends GameMap> getGame(@NotNull final String name) {
		return NAME_TO_GAME.get(name.toLowerCase(Locale.ROOT));
	}

	@Nullable
	public static Game<? extends GameMap> getGameForMap(GameMap map) {
		return MAP_TO_GAME.get(map);
	}

	@Nullable
	public static GameMap getMapByIdentifier(String identifier) {
		return IDENTIFIER_TO_MAP.get(identifier);
	}

	public static Collection<Game<? extends GameMap>> getGamesWithLabel(GameLabel label) {
		return Collections.unmodifiableCollection(GAMES_BY_LABEL.get(label));
	}

}
