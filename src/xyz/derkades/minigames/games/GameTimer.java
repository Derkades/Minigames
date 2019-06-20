package xyz.derkades.minigames.games;

import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.Minigames;

@Deprecated
public abstract class GameTimer {

	public GameTimer(final Game game, final int gameSeconds, final int preStartSeconds){
		game.sendMessage(String.format("The game will start in %s seconds.", preStartSeconds));

		new BukkitRunnable() {

			private int secondsLeft = gameSeconds + preStartSeconds;

			@Override
			public void run() {
				this.secondsLeft--;

				// pre-start countdown
				if (this.secondsLeft > gameSeconds) {
					return;
				}

				if (this.secondsLeft == gameSeconds) {
					game.sendMessage("The game has started.");
					GameTimer.this.onStart();
					return;
				}

				this.secondsLeft = GameTimer.this.gameTimer(this.secondsLeft);

				if (this.secondsLeft <= 0) {
					this.cancel();
					GameTimer.this.onEnd();
					return;
				}

				if (this.secondsLeft == 60 || this.secondsLeft == 30 || this.secondsLeft == 10 || this.secondsLeft <= 5) {
					game.sendMessage(String.format("%s seconds left", this.secondsLeft));
				}
			}

		}.runTaskTimer(Minigames.getInstance(), 0, 20);
	}

	public abstract void onStart();

	public abstract int gameTimer(int secondsLeft);

	public abstract void onEnd();

}
