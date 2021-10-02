package derkades.minigames.games;

import derkades.minigames.Minigames;
import derkades.minigames.utils.MPlayer;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;
import java.util.function.BiConsumer;

public abstract class RedBlueTeamGame<MapType extends GameMap> extends TeamGame<MapType> {

	private static final Set<GameTeam> RED_AND_BLUE = EnumSet.of(GameTeam.RED, GameTeam.BLUE);

	public RedBlueTeamGame(@NotNull String identifier, @NotNull String name, @NotNull String@NotNull[] description,
						   @NotNull Material material, @NotNull GameMap@NotNull[] gameMaps) {
		super(identifier, name, description, material, gameMaps, RED_AND_BLUE);
	}

	protected void splitPlayers(BiConsumer<MPlayer, GameTeam> forEach) {
		boolean teamBool = false;

		for (final MPlayer player : Minigames.getOnlinePlayersInRandomOrder()) {
			final GameTeam team = teamBool ? GameTeam.RED : GameTeam.BLUE;
			this.getTeams().setTeam(player, team, true);
			forEach.accept(player, team);
			teamBool = !teamBool;
		}
	}

}
