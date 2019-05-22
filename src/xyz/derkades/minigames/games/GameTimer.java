package xyz.derkades.minigames.games;

import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.minigames.Minigames;

public abstract class GameTimer {
	
	public GameTimer(final Game game, final int gameSeconds, final int preStartSeconds){
		game.sendMessage(String.format("The game will start in %s seconds.", preStartSeconds));
		
		new BukkitRunnable() {

			private int secondsLeft = gameSeconds + preStartSeconds;
			
			@Override
			public void run() {
				secondsLeft--;
				
				// pre-start countdown
				if (secondsLeft > gameSeconds) {
					return;
				}
				
				if (secondsLeft == gameSeconds) {
					game.sendMessage("The game has started.");
					GameTimer.this.onStart();
					return;
				}
				
				secondsLeft = GameTimer.this.gameTimer(secondsLeft);
				
				if (secondsLeft <= 0) {
					this.cancel();
					GameTimer.this.onEnd();
					return;
				}
				
				if (secondsLeft == 60 || secondsLeft == 30 || secondsLeft == 10 || secondsLeft <= 5) {
					game.sendMessage(String.format("%s seconds left", secondsLeft));
				}
			}
			
		}.runTaskTimer(Minigames.getInstance(), 0, 20);
	}
	
	public abstract void onStart();
	
	public abstract int gameTimer(int secondsLeft);
	
	public abstract void onEnd();

}
