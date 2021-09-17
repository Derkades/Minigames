package derkades.minigames.games;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import derkades.minigames.utils.MPlayer;

public class TeamManager {
	
	private final Map<UUID, GameTeam> uuidToTeam = new HashMap<>();
	private final SetMultimap<GameTeam, UUID> teamToUuids = MultimapBuilder.hashKeys().hashSetValues().build();
	private final Set<GameTeam> allowedTeams;
	
	public TeamManager() {
		this.allowedTeams = null;
	}
	
	public TeamManager(Set<GameTeam> allowedTeams) {
		this.allowedTeams = allowedTeams;
	}
	
	public GameTeam getTeam(MPlayer player) {
		return uuidToTeam.get(player.getUniqueId());
	}
	
	private void checkTeam(GameTeam team, boolean allowNull) {
		if (team == null) {
			if (!allowNull) {
				throw new IllegalArgumentException("Provided team is null");
			}
		} else if (allowedTeams != null &&
				!allowedTeams.contains(team)) {
			throw new IllegalArgumentException("Illegal team: " + team);
		}
	}

	public void setTeam(MPlayer player, GameTeam team) {
		checkTeam(team, true);
		
		UUID uuid = player.getUniqueId();
		GameTeam oldTeam = uuidToTeam.get(uuid);
		if (oldTeam != null) {
			teamToUuids.remove(oldTeam, uuid);
		}
		if (team == null) {
			uuidToTeam.remove(uuid);
		} else {
			uuidToTeam.put(uuid, team);
			teamToUuids.put(team, uuid);
		}
	}
	
	public boolean isTeamMember(MPlayer player, GameTeam team) {
		checkTeam(team, false);
		return uuidToTeam.get(player.getUniqueId()) == team;
	}
	
	public Set<UUID> getMembers(GameTeam team) {
		checkTeam(team, false);
		return Collections.unmodifiableSet(teamToUuids.get(team));
	}
	
	public int getMemberCount(GameTeam team) {
		checkTeam(team, false);
		return teamToUuids.get(team).size();
	}
	
	public GameTeam anyEmptyTeam() {
		for (GameTeam team : teamToUuids.keys()) {
			if (teamToUuids.get(team).size() == 0) {
				return team;
			}
		}
		return null;
	}

}
