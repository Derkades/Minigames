package derkades.minigames.games.takecover;

import derkades.minigames.games.Game;
import derkades.minigames.utils.MPlayer;
import derkades.minigames.utils.Scheduler;
import org.bukkit.Location;
import org.bukkit.Material;

@SuppressWarnings("ALL")
public class TakeCover extends Game<TakeCoverMap> {

	// TODO turn into meteor shower game

	private static final int ARROW_COUNT = 5;
	private static final int ARROW_TIME = 3; // seconds

	public TakeCover() {
		super(
				"takecover",
				"Take Cover",
				new String[] {
						"description", // TODO Description
				},
				Material.FIRE_CHARGE,
				new TakeCoverMap[] {
						new Prototype(),
				},
				2,
				120
		);
	}

	private int coverCount = 0;
	private int roundDuration = 0;
	private int timeInCurrentRound;

	@Override
	public void onPreStart() {
		this.coverCount = 8;
		this.roundDuration = 8;
		this.timeInCurrentRound = 0;
	}

	@Override
	public void onStart() {

	}

	@Override
	public int gameTimer(final int secondsLeft) {
		return secondsLeft;
	}

	@Override
	public boolean endEarly() {
		return false;
	}

	@Override
	public void onEnd() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerJoin(final MPlayer player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPlayerQuit(final MPlayer player) {
		// TODO Auto-generated method stub

	}

	private void spawnArrows() {
		final Location min = this.map.getCoverMin();
		final Location max = this.map.getCoverMax();
		final int interval = (ARROW_TIME*20) / ARROW_COUNT;
		int delay = 0;
		final int y = min.getBlockY();
		for (int i = 0; i < ARROW_COUNT; i++) {
			Scheduler.delay(delay, () -> {
				for (int x = min.getBlockX(); x < max.getBlockX(); x++) {
					for (int z = min.getBlockZ(); z < max.getBlockZ(); z++) {

					}
				}
			});
			delay += interval;
		}
	}

}
