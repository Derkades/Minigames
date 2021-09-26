package derkades.minigames.games;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import derkades.minigames.utils.MPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class TeamManager {

	@NotNull
	private final Map<@NotNull UUID, GameTeam> uuidToTeam = new HashMap<>();
	@SuppressWarnings("null")
	@NotNull
	private final SetMultimap<@NotNull GameTeam, @NotNull UUID> teamToUuids = MultimapBuilder.hashKeys().hashSetValues().build();
	@Nullable
	private final Set<@NotNull GameTeam> allowedTeams;

	public TeamManager() {
		this.allowedTeams = null;
	}

	public TeamManager(@NotNull final Set<@NotNull GameTeam> allowedTeams) {
		this.allowedTeams = allowedTeams;
	}

	public GameTeam getTeam(final MPlayer player) {
		return this.uuidToTeam.get(player.getUniqueId());
	}

	@SuppressWarnings("null")
	private void checkTeam(@Nullable final GameTeam team, final boolean allowNull) {
		if (team == null) {
			if (!allowNull) {
				throw new IllegalArgumentException("Provided team is null");
			}
		} else if (this.allowedTeams != null &&
				!this.allowedTeams.contains(team)) {
			throw new IllegalArgumentException("Illegal team: " + team);
		}
	}

	public void setTeam(@NotNull final MPlayer player, @Nullable final GameTeam team, final boolean notifyPlayer) {
		checkTeam(team, true);

		final UUID uuid = player.getUniqueId();
		final GameTeam oldTeam = this.uuidToTeam.get(uuid);
		if (oldTeam != null) {
			this.teamToUuids.remove(oldTeam, uuid);
		}
		if (team == null) {
			this.uuidToTeam.remove(uuid);
		} else {
			this.uuidToTeam.put(uuid, team);
			this.teamToUuids.put(team, uuid);

			if (notifyPlayer) {
				player.sendTitle(Component.empty(),
						Component.text("You are in the ", NamedTextColor.GRAY)
						.append(team.getColoredDisplayName())
						.append(Component.text(" team", NamedTextColor.GRAY))
						);
			}
		}
	}

	public boolean isTeamMember(@NotNull final MPlayer player, @NotNull final GameTeam team) {
		checkTeam(team, false);
		return this.uuidToTeam.get(player.getUniqueId()) == team;
	}

	public boolean isInSameTeam(@NotNull final MPlayer a, @NotNull final MPlayer b) {
		final GameTeam team = this.getTeam(a);
		return team != null && team == this.getTeam(b);
	}

	public Set<@NotNull UUID> getMembers(@NotNull final GameTeam team) {
		checkTeam(team, false);
		return Collections.unmodifiableSet(this.teamToUuids.get(team));
	}

	public int getMemberCount(@NotNull final GameTeam team) {
		checkTeam(team, false);
		return this.teamToUuids.get(team).size();
	}

	public GameTeam anyEmptyTeam() {
		for (final GameTeam team : this.teamToUuids.keys()) {
			if (this.teamToUuids.get(team).size() == 0) {
				return team;
			}
		}
		return null;
	}

}
