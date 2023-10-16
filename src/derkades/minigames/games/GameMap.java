package derkades.minigames.games;

import derkades.minigames.Minigames;
import derkades.minigames.random.RandomlyPickable;
import derkades.minigames.utils.Disableable;
import derkades.minigames.worlds.GameWorld;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GameMap implements RandomlyPickable, Disableable {

	@NotNull
	public abstract String getName();

	@NotNull
	public abstract GameWorld getGameWorld();

	@Nullable
	public abstract String getCredits();

	@NotNull
	public abstract String getIdentifier();

	public void onPreStart() {}

	public void onStart() {}

	public void onEnd() {}

	public void onTimer(final int secondsLeft) {}

	public World getWorld() {
		return this.getGameWorld().getWorld();
	}

	@Override
	public void setWeight(final double weight) {
		final String configPath = "game-voting.map." + this.getIdentifier();
		Minigames.getInstance().getConfig().set(configPath, weight);
		Minigames.getInstance().queueConfigSave();
	}

	@Override
	public double getWeight() {
		final String configPath = "game-voting.map." + this.getIdentifier();
		return Minigames.getInstance().getConfig().getDouble(configPath, 1);
	}

	@Override
	public boolean isDisabled() {
		return false;
	}

}
