package xyz.derkades.minigames.games.teamsbowbattle;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffectType;

import xyz.derkades.minigames.random.Size;
import xyz.derkades.minigames.utils.Utils;
import xyz.derkades.minigames.worlds.GameWorld;

public class Forest extends TeamsBowBattleMap {

	@Override
	public String getName() {
		return "Forest";
	}

	@Override
	public Location getTeamRedSpawnLocation() {
		return new Location(this.getWorld(), 0.5, 63, -34.5, 0f, 0f);
	}

	@Override
	public Location getTeamBlueSpawnLocation() {
		return new Location(this.getWorld(), 0.5, 63, 35.5, -180f, 0f);
	}

	@Override
	public void onGameStart() {
		Bukkit.getOnlinePlayers().forEach((player) -> Utils.giveInfiniteEffect(player, PotionEffectType.SPEED, 1));
	}

	@Override
	public GameWorld getGameWorld() {
		return GameWorld.TBB_FOREST;
	}

	@Override
	public String getCredits() {
		return "Yaraka";
	}

	@Override
	public Size getSize() {
		return Size.LARGE;
	}

	@Override
	public String getIdentifier() {
		return "teamsbowbattle_forest";
	}

}
