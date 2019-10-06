package xyz.derkades.minigames.board;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import xyz.derkades.derkutils.Random;
import xyz.derkades.derkutils.bukkit.ItemBuilder;
import xyz.derkades.derkutils.bukkit.menu.IconMenu;
import xyz.derkades.derkutils.bukkit.menu.MenuCloseEvent;
import xyz.derkades.derkutils.bukkit.menu.OptionClickEvent;
import xyz.derkades.minigames.Minigames;

public class DiceAnimationMenu extends IconMenu {

	private static final int ANIMATION_INTERVAL_TICKS = 5;
	private static final int ANIMATION_DURATION_SECONDS = 3;
	private static final int FINAL_STATIC_SECONDS = 2;

	private static final int MAX_ITERATIONS = ANIMATION_DURATION_SECONDS * 20 / ANIMATION_INTERVAL_TICKS;
	private static final int FINAL_ITERATIONS = FINAL_STATIC_SECONDS * 20 / ANIMATION_INTERVAL_TICKS;

	private static final Minigames plugin = Minigames.getInstance();

	private AnimationTimer timer;

	private final int min;
	private final int max;
	private final int endResult;

	public DiceAnimationMenu(final Player player, final int min, final int max, final int endResult) {
		super(plugin, "Dice", 6*9, player);
		this.min = min;
		this.max = max;
		this.endResult = endResult;
	}

	@Override
	public void open() {
		this.timer = new AnimationTimer();
		this.timer.runTaskTimer(plugin, 0, ANIMATION_INTERVAL_TICKS);
		super.open();
	}

	@Override
	public void onClose(final MenuCloseEvent event) {
		this.timer.cancel();
	}

	@Override
	public boolean onOptionClick(final OptionClickEvent event) {
		return false;
	}

	private void displayNumber(final int number) {
		this.items.clear();

		// TODO Proper animation
		this.items.put(22, new ItemBuilder(Material.STONE).amount(number).create());
		this.refreshItems();
	}

	private class AnimationTimer extends BukkitRunnable {

		int previous = -1;
		int i = -1;

		@Override
		public void run() {
			this.i++;

			if (this.i >= MAX_ITERATIONS + FINAL_ITERATIONS) {
				DiceAnimationMenu.this.close();
				return;
			}

			if (this.i == MAX_ITERATIONS) {
				DiceAnimationMenu.this.displayNumber(DiceAnimationMenu.this.endResult);
				return;
			}

			if (this.i > MAX_ITERATIONS) {
				return; // static final result
			}

			int random;
			do {
				random = Random.getRandomInteger(DiceAnimationMenu.this.min, DiceAnimationMenu.this.max);
			} while(random == this.previous);

			this.previous = random;
			DiceAnimationMenu.this.displayNumber(random);
		}
	}

}
