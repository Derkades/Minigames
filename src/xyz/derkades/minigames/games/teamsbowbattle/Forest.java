package xyz.derkades.minigames.games.teamsbowbattle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffectType;

import xyz.derkades.minigames.Var;
import xyz.derkades.minigames.games.maps.MapSize;
import xyz.derkades.minigames.utils.Utils;
import xyz.derkades.minigames.worlds.GameWorld;

public class Forest extends TeamsBowBattleMap {

	@Override
	public String getName() {
		return "Forest";
	}

	@Override
	public MapSize getSize() {
		return MapSize.LARGE;
	}

	@Override
	public Location getTeamRedSpawnLocation() {
		return new Location(Var.WORLD, 315.5, 69, 315.5, 0, 0);
	}

	@Override
	public Location getTeamBlueSpawnLocation() {
		return new Location(Var.WORLD, 315.5, 69, 385.5, -180, 0);
	}

	@Override
	public void onGameStart() {
		Bukkit.getOnlinePlayers().forEach((player) -> Utils.giveInfiniteEffect(player, PotionEffectType.SPEED, 1));
	}

	@Override
	public GameWorld getGameWorld() {
		return null;
	}

}
