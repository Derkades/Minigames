package derkades.minigames.modules;

import derkades.minigames.Var;
import derkades.minigames.utils.Scheduler;

public class LobbyStormDisabler extends Module implements Runnable {

	public LobbyStormDisabler() {
		Scheduler.repeat(20, this);
	}

	@Override
	public void run() {
		Var.LOBBY_WORLD.setStorm(false);
	}

}
