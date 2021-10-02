package derkades.minigames.games;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class TeamGame<M extends GameMap> extends Game<M> {

	@Nullable
	private final Set<GameTeam> allowedTeams;

	// TODO boolean in constructor to disable friendly fire
	public TeamGame(
			@NotNull String identifier, @NotNull String name, @NotNull String@NotNull[] description,
			@NotNull Material material, @NotNull M@NotNull[] gameMaps, int requiredPlayers,
			int duration,
			@Nullable Set<GameTeam> allowedTeams) {
		super(identifier, name, description, material, gameMaps, requiredPlayers, duration);
		this.allowedTeams = allowedTeams;
	}

	@Nullable
	private TeamManager teams;

	@NotNull
	protected TeamManager getTeams() {
		if (teams == null){
			throw new IllegalStateException("Teams backend was never initialized");
		}
		return teams;
	}

	protected void initTeamsBackend() {
		teams = new TeamManager(allowedTeams);
	}

	protected void destroyTeamsBackend() {
		if (teams != null) {
			teams.clearTeams();
		}
		teams = null;
	}

	@Override
	public boolean endEarly() {
		return this.getTeams().anyEmptyTeam() != null;
	}

}
