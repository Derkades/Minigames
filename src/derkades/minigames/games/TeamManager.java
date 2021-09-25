package derkades.minigames.games;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import derkades.minigames.utils.MPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class TeamManager {

	private final Map<UUID, GameTeam> uuidToTeam = new HashMap<>();
	private final SetMultimap<GameTeam, UUID> teamToUuids = MultimapBuilder.hashKeys().hashSetValues().build();
	private final Set<GameTeam> allowedTeams;

	public TeamManager() {
		this.allowedTeams = null;
	}

	public TeamManager(final Set<GameTeam> allowedTeams) {
		this.allowedTeams = allowedTeams;
	}

	public GameTeam getTeam(final MPlayer player) {
		return this.uuidToTeam.get(player.getUniqueId());
	}

	private void checkTeam(final GameTeam team, final boolean allowNull) {
		if (team == null) {
			if (!allowNull) {
				throw new IllegalArgumentException("Provided team is null");
			}
		} else if (this.allowedTeams != null &&
				!this.allowedTeams.contains(team)) {
			throw new IllegalArgumentException("Illegal team: " + team);
		}
	}

	public void setTeam(final MPlayer player, final GameTeam team, final boolean notifyPlayer) {
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
		}

		if (notifyPlayer) {
			player.sendTitle(Component.empty(),
					Component.text("You are in the ", NamedTextColor.GRAY)
					.append(team.getColoredDisplayName())
					.append(Component.text(" team", NamedTextColor.GRAY))
					);
//					 String.format("%sYou are in the %s%sRED%s team", ChatColor.GRAY, ChatColor.RED, ChatColor.BOLD, ChatColor.GRAY));
		}
	}

	public boolean isTeamMember(final MPlayer player, final GameTeam team) {
		checkTeam(team, false);
		return this.uuidToTeam.get(player.getUniqueId()) == team;
	}

	public boolean isInSameTeam(final MPlayer a, final MPlayer b) {
		final GameTeam team = this.getTeam(a);
		return team != null && team == this.getTeam(b);
	}

	public Set<UUID> getMembers(final GameTeam team) {
		checkTeam(team, false);
		return Collections.unmodifiableSet(this.teamToUuids.get(team));
	}

	public int getMemberCount(final GameTeam team) {
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
