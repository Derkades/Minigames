package xyz.derkades.minigames.board;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.utils.MPlayer;
import xyz.derkades.minigames.utils.Scheduler;

public class Board {

	public static int WINNER_MOVE_MIN = 2;
	public static int WINNER_MOVE_MAX = 10;
	public static int LOSER_MOVE_MIN = 1;
	public static int LOSER_MOVE_MAX = 4;

	public static int TITLE_DURATION_TICKS = 3*20;

	public static String[] TITLE_WON = {
			"You won!",
			WINNER_MOVE_MIN + "-" + WINNER_MOVE_MAX + " tiles forward"
	};

	public static String[] TITLE_LOST = {
			"You lost",
			LOSER_MOVE_MIN + "-" + LOSER_MOVE_MAX + " tiles forward"
	};

	public static void performTurns(final List<UUID> won, final List<UUID> lost) {
		final List<MPlayer> players = Minigames.getOnlinePlayers();

		final List<MPlayer> winners = players.stream()
				.filter((p) -> won.contains(p.getUniqueId()))
				.collect(Collectors.toList());

		final List<MPlayer> losers = players.stream()
				.filter((p) -> lost.contains(p.getUniqueId()))
				.collect(Collectors.toList());

		winners.forEach((p) -> p.sendTitle(TITLE_WON[0], TITLE_WON[1]));
		losers.forEach((p) -> p.sendTitle(TITLE_LOST[0], TITLE_LOST[1]));

		Scheduler.delay(TITLE_DURATION_TICKS, () -> {
			winners.forEach((p) -> {
				final int random = Random.getRandomInteger(WINNER_MOVE_MIN, WINNER_MOVE_MAX);
				// TODO make menu use MPlayer instead of Player
				new DiceAnimationMenu(p.bukkit(), WINNER_MOVE_MIN, WINNER_MOVE_MAX, random).open();
			});

			losers.forEach((p) -> {
				final int random = Random.getRandomInteger(LOSER_MOVE_MIN, LOSER_MOVE_MAX);
				new DiceAnimationMenu(p.bukkit(), LOSER_MOVE_MIN, LOSER_MOVE_MAX, random).open();
			});
		});

		// TODO use constant from dice menu
		Scheduler.delay(TITLE_DURATION_TICKS + 5*20, () -> {

		});
	}

}
