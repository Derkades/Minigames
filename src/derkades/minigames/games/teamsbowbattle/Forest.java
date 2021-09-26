package derkades.minigames.games.teamsbowbattle;

import org.bukkit.Location;
import org.bukkit.potion.PotionEffectType;

import derkades.minigames.Minigames;
import derkades.minigames.random.Size;
import derkades.minigames.worlds.GameWorld;
import org.jetbrains.annotations.NotNull;

class Forest extends TeamsBowBattleMap {

	@Override
	public @NotNull String getName() {
		return "Forest";
	}

	@Override
	public void onStart() {
		Minigames.getOnlinePlayers().forEach((p) -> p.giveInfiniteEffect(PotionEffectType.SPEED, 1));
	}

	@Override
	public @NotNull GameWorld getGameWorld() {
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
	public @NotNull String getIdentifier() {
		return "teamsbowbattle_forest";
	}

	@Override
	Location getTeamRedSpawnLocation() {
		return new Location(this.getWorld(), 0.5, 63, -34.5, 0f, 0f);
	}

	@Override
	Location getTeamBlueSpawnLocation() {
		return new Location(this.getWorld(), 0.5, 63, 35.5, -180f, 0f);
	}

}
