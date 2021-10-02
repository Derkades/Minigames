package derkades.minigames.games;

import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public abstract class TeamGame<T extends GameMap> extends Game<T> {

	@Nullable
	private final Set<GameTeam> allowedTeams;

	// TODO boolean in constructor to disable friendly fire
	public TeamGame(
			@NotNull String identifier, @NotNull String name, @NotNull String@NotNull[] description,
			@NotNull Material material, @NotNull GameMap@NotNull[] gameMaps,
			@Nullable Set<GameTeam> allowedTeams) {
		super(identifier, name, description, material, gameMaps);
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
