package xyz.derkades.minigames.board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import xyz.derkades.derkutils.Random;
import xyz.derkades.minigames.Minigames;
import xyz.derkades.minigames.constants.BoardConfig;
import xyz.derkades.minigames.utils.Scheduler;

public class Board {

	public static void performTurns(final List<UUID> won) {
		final List<BoardPlayer> players = Minigames.getOnlinePlayers()
				.stream().map(BoardPlayer::new).collect(Collectors.toList());

		final List<BoardPlayer> winners = players.stream()
				.filter((p) -> won.contains(p.getUniqueId()))
				.collect(Collectors.toList());
		final List<BoardPlayer> losers = players.stream()
				.filter((p) -> !won.contains(p.getUniqueId()))
				.collect(Collectors.toList());

		winners.forEach((p) -> p.sendTitle(BoardConfig.TITLE_WON[0], BoardConfig.TITLE_WON[1]));
		losers.forEach((p) -> p.sendTitle(BoardConfig.TITLE_LOST[0], BoardConfig.TITLE_LOST[1]));

		Scheduler.delay(BoardConfig.TILE_TITLE_DURATION_TICKS, () -> {
			final Map<BoardPlayer, Integer> steps = new HashMap<>();
			winners.forEach((p) -> steps.put(p, openDieAnimationMenu(p,
					BoardConfig.DIE_WINNER_STEPS_MIN,
					BoardConfig.DIE_WINNER_STEPS_MAX)));

			losers.forEach((p) -> steps.put(p, openDieAnimationMenu(p,
					BoardConfig.DIE_WINNER_STEPS_MIN,
					BoardConfig.DIE_WINNER_STEPS_MAX)));


			Scheduler.delay(BoardConfig.DIE_MENU_DURATION_SECONDS + BoardConfig.DIE_MENU_FINAL_STATIC_SECONDS,
					() -> steps.forEach((p, s) -> p.jumpTiles(s)));
		});
	}

	private static int openDieAnimationMenu(final BoardPlayer p, final int min, final int max) {
		final int random = Random.getRandomInteger(min, max);
		new DieAnimationMenu(p, min, max, random).open();
		return random;
	}

}
